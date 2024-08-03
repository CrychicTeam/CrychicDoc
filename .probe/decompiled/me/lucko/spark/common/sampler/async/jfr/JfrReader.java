package me.lucko.spark.common.sampler.async.jfr;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.lucko.spark.common.sampler.async.AsyncStackTraceElement;

public class JfrReader implements Closeable {

    private static final int BUFFER_SIZE = 2097152;

    private static final int CHUNK_HEADER_SIZE = 68;

    private static final int CHUNK_SIGNATURE = 1179406848;

    private final FileChannel ch;

    private ByteBuffer buf;

    private long filePosition;

    public boolean incomplete;

    public long startNanos = Long.MAX_VALUE;

    public long endNanos = Long.MIN_VALUE;

    public long startTicks = Long.MAX_VALUE;

    public long ticksPerSec;

    public final Dictionary<JfrReader.JfrClass> types = new Dictionary<>();

    public final Map<String, JfrReader.JfrClass> typesByName = new HashMap();

    public final Map<Long, String> threads = new HashMap();

    public final Dictionary<JfrReader.ClassRef> classes = new Dictionary<>();

    public final Dictionary<byte[]> symbols = new Dictionary<>();

    public final Dictionary<JfrReader.MethodRef> methods = new Dictionary<>();

    public final Dictionary<JfrReader.StackTrace> stackTraces = new Dictionary<>();

    public final Dictionary<AsyncStackTraceElement> stackFrames = new Dictionary<>();

    public final Map<Integer, String> frameTypes = new HashMap();

    public final Map<Integer, String> threadStates = new HashMap();

    public final Map<String, String> settings = new HashMap();

    private int executionSample;

    private int nativeMethodSample;

    private int allocationInNewTLAB;

    private int allocationOutsideTLAB;

    private int allocationSample;

    private int monitorEnter;

    private int threadPark;

    private int activeSetting;

    private boolean activeSettingHasStack;

    public JfrReader(Path path) throws IOException {
        this.ch = FileChannel.open(path, StandardOpenOption.READ);
        this.buf = ByteBuffer.allocateDirect(2097152);
        this.buf.flip();
        this.ensureBytes(68);
        if (!this.readChunk(0)) {
            throw new IOException("Incomplete JFR file");
        }
    }

    public void close() throws IOException {
        this.ch.close();
    }

    public long durationNanos() {
        return this.endNanos - this.startNanos;
    }

    public List<JfrReader.Event> readAllEvents() throws IOException {
        return this.readAllEvents(null);
    }

    public <E extends JfrReader.Event> List<E> readAllEvents(Class<E> cls) throws IOException {
        ArrayList<E> events = new ArrayList();
        E event;
        while ((event = this.readEvent(cls)) != null) {
            events.add(event);
        }
        Collections.sort(events);
        return events;
    }

    public JfrReader.Event readEvent() throws IOException {
        return this.readEvent(null);
    }

    public <E extends JfrReader.Event> E readEvent(Class<E> cls) throws IOException {
        while (this.ensureBytes(68)) {
            int pos = this.buf.position();
            int size = this.getVarint();
            int type = this.getVarint();
            if (type == 76 && this.buf.getInt(pos) == 1179406848) {
                if (this.readChunk(pos)) {
                    continue;
                }
                break;
            } else {
                if (type != this.executionSample && type != this.nativeMethodSample) {
                    if (type == this.allocationInNewTLAB) {
                        if (cls == null || cls == JfrReader.AllocationSample.class) {
                            return (E) this.readAllocationSample(true);
                        }
                    } else if (type != this.allocationOutsideTLAB && type != this.allocationSample) {
                        if (type == this.monitorEnter) {
                            if (cls == null || cls == JfrReader.ContendedLock.class) {
                                return (E) this.readContendedLock(false);
                            }
                        } else if (type == this.threadPark) {
                            if (cls == null || cls == JfrReader.ContendedLock.class) {
                                return (E) this.readContendedLock(true);
                            }
                        } else if (type == this.activeSetting) {
                            this.readActiveSetting();
                        }
                    } else if (cls == null || cls == JfrReader.AllocationSample.class) {
                        return (E) this.readAllocationSample(false);
                    }
                } else if (cls == null || cls == JfrReader.ExecutionSample.class) {
                    return (E) this.readExecutionSample();
                }
                if ((pos = pos + size) <= this.buf.limit()) {
                    this.buf.position(pos);
                } else {
                    this.seek(this.filePosition + (long) pos);
                }
            }
        }
        return null;
    }

    private JfrReader.ExecutionSample readExecutionSample() {
        long time = this.getVarlong();
        int tid = this.getVarint();
        int stackTraceId = this.getVarint();
        int threadState = this.getVarint();
        return new JfrReader.ExecutionSample(time, tid, stackTraceId, threadState);
    }

    private JfrReader.AllocationSample readAllocationSample(boolean tlab) {
        long time = this.getVarlong();
        int tid = this.getVarint();
        int stackTraceId = this.getVarint();
        int classId = this.getVarint();
        long allocationSize = this.getVarlong();
        long tlabSize = tlab ? this.getVarlong() : 0L;
        return new JfrReader.AllocationSample(time, tid, stackTraceId, classId, allocationSize, tlabSize);
    }

    private JfrReader.ContendedLock readContendedLock(boolean hasTimeout) {
        long time = this.getVarlong();
        long duration = this.getVarlong();
        int tid = this.getVarint();
        int stackTraceId = this.getVarint();
        int classId = this.getVarint();
        if (hasTimeout) {
            this.getVarlong();
        }
        long until = this.getVarlong();
        long address = this.getVarlong();
        return new JfrReader.ContendedLock(time, tid, stackTraceId, duration, classId);
    }

    private void readActiveSetting() {
        long time = this.getVarlong();
        long duration = this.getVarlong();
        int tid = this.getVarint();
        if (this.activeSettingHasStack) {
            this.getVarint();
        }
        long id = this.getVarlong();
        String name = this.getString();
        String value = this.getString();
        this.settings.put(name, value);
    }

    private boolean readChunk(int pos) throws IOException {
        if (pos + 68 <= this.buf.limit() && this.buf.getInt(pos) == 1179406848) {
            int version = this.buf.getInt(pos + 4);
            if (version >= 131072 && version <= 196607) {
                long cpOffset = this.buf.getLong(pos + 16);
                long metaOffset = this.buf.getLong(pos + 24);
                if (cpOffset != 0L && metaOffset != 0L) {
                    this.startNanos = Math.min(this.startNanos, this.buf.getLong(pos + 32));
                    this.endNanos = Math.max(this.endNanos, this.buf.getLong(pos + 32) + this.buf.getLong(pos + 40));
                    this.startTicks = Math.min(this.startTicks, this.buf.getLong(pos + 48));
                    this.ticksPerSec = this.buf.getLong(pos + 56);
                    this.types.clear();
                    this.typesByName.clear();
                    long chunkStart = this.filePosition + (long) pos;
                    this.readMeta(chunkStart + metaOffset);
                    this.readConstantPool(chunkStart + cpOffset);
                    this.cacheEventTypes();
                    this.seek(chunkStart + 68L);
                    return true;
                } else {
                    this.incomplete = true;
                    return false;
                }
            } else {
                throw new IOException("Unsupported JFR version: " + (version >>> 16) + "." + (version & 65535));
            }
        } else {
            throw new IOException("Not a valid JFR file");
        }
    }

    private void readMeta(long metaOffset) throws IOException {
        this.seek(metaOffset);
        this.ensureBytes(5);
        this.ensureBytes(this.getVarint() - this.buf.position());
        this.getVarint();
        this.getVarlong();
        this.getVarlong();
        this.getVarlong();
        String[] strings = new String[this.getVarint()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = this.getString();
        }
        this.readElement(strings);
    }

    private JfrReader.Element readElement(String[] strings) {
        String name = strings[this.getVarint()];
        int attributeCount = this.getVarint();
        Map<String, String> attributes = new HashMap(attributeCount);
        for (int i = 0; i < attributeCount; i++) {
            attributes.put(strings[this.getVarint()], strings[this.getVarint()]);
        }
        JfrReader.Element e = this.createElement(name, attributes);
        int childCount = this.getVarint();
        for (int i = 0; i < childCount; i++) {
            e.addChild(this.readElement(strings));
        }
        return e;
    }

    private JfrReader.Element createElement(String name, Map<String, String> attributes) {
        switch(name) {
            case "class":
                JfrReader.JfrClass type = new JfrReader.JfrClass(attributes);
                if (!attributes.containsKey("superType")) {
                    this.types.put((long) type.id, type);
                }
                this.typesByName.put(type.name, type);
                return type;
            case "field":
                return new JfrReader.JfrField(attributes);
            default:
                return new JfrReader.Element();
        }
    }

    private void readConstantPool(long cpOffset) throws IOException {
        long delta;
        do {
            this.seek(cpOffset);
            this.ensureBytes(5);
            this.ensureBytes(this.getVarint() - this.buf.position());
            this.getVarint();
            this.getVarlong();
            this.getVarlong();
            delta = this.getVarlong();
            this.getVarint();
            int poolCount = this.getVarint();
            for (int i = 0; i < poolCount; i++) {
                int type = this.getVarint();
                this.readConstants(this.types.get((long) type));
            }
        } while (delta != 0L && (cpOffset += delta) > 0L);
    }

    private void readConstants(JfrReader.JfrClass type) {
        String var2 = type.name;
        switch(var2) {
            case "jdk.types.ChunkHeader":
                this.buf.position(this.buf.position() + 71);
                break;
            case "java.lang.Thread":
                this.readThreads(type.field("group") != null);
                break;
            case "java.lang.Class":
                this.readClasses(type.field("hidden") != null);
                break;
            case "jdk.types.Symbol":
                this.readSymbols();
                break;
            case "jdk.types.Method":
                this.readMethods();
                break;
            case "jdk.types.StackTrace":
                this.readStackTraces();
                break;
            case "jdk.types.FrameType":
                this.readMap(this.frameTypes);
                break;
            case "jdk.types.ThreadState":
                this.readMap(this.threadStates);
                break;
            default:
                this.readOtherConstants(type.fields);
        }
    }

    private void readThreads(boolean hasGroup) {
        int count = this.getVarint();
        for (int i = 0; i < count; i++) {
            long id = this.getVarlong();
            String osName = this.getString();
            int osThreadId = this.getVarint();
            String javaName = this.getString();
            long javaThreadId = this.getVarlong();
            if (hasGroup) {
                this.getVarlong();
            }
            this.threads.put(id, javaName != null ? javaName : osName);
        }
    }

    private void readClasses(boolean hasHidden) {
        int count = this.classes.preallocate(this.getVarint());
        for (int i = 0; i < count; i++) {
            long id = this.getVarlong();
            long loader = this.getVarlong();
            long name = this.getVarlong();
            long pkg = this.getVarlong();
            int modifiers = this.getVarint();
            if (hasHidden) {
                this.getVarint();
            }
            this.classes.put(id, new JfrReader.ClassRef(name));
        }
    }

    private void readMethods() {
        int count = this.methods.preallocate(this.getVarint());
        for (int i = 0; i < count; i++) {
            long id = this.getVarlong();
            long cls = this.getVarlong();
            long name = this.getVarlong();
            long sig = this.getVarlong();
            int modifiers = this.getVarint();
            int hidden = this.getVarint();
            this.methods.put(id, new JfrReader.MethodRef(cls, name, sig));
        }
        this.stackFrames.preallocate(count);
    }

    private void readStackTraces() {
        int count = this.stackTraces.preallocate(this.getVarint());
        for (int i = 0; i < count; i++) {
            long id = this.getVarlong();
            int truncated = this.getVarint();
            JfrReader.StackTrace stackTrace = this.readStackTrace();
            this.stackTraces.put(id, stackTrace);
        }
    }

    private JfrReader.StackTrace readStackTrace() {
        int depth = this.getVarint();
        long[] methods = new long[depth];
        byte[] types = new byte[depth];
        int[] locations = new int[depth];
        for (int i = 0; i < depth; i++) {
            methods[i] = this.getVarlong();
            int line = this.getVarint();
            int bci = this.getVarint();
            locations[i] = line << 16 | bci & 65535;
            types[i] = this.buf.get();
        }
        return new JfrReader.StackTrace(methods, types, locations);
    }

    private void readSymbols() {
        int count = this.symbols.preallocate(this.getVarint());
        for (int i = 0; i < count; i++) {
            long id = this.getVarlong();
            if (this.buf.get() != 3) {
                throw new IllegalArgumentException("Invalid symbol encoding");
            }
            this.symbols.put(id, this.getBytes());
        }
    }

    private void readMap(Map<Integer, String> map) {
        int count = this.getVarint();
        for (int i = 0; i < count; i++) {
            map.put(this.getVarint(), this.getString());
        }
    }

    private void readOtherConstants(List<JfrReader.JfrField> fields) {
        int stringType = this.getTypeId("java.lang.String");
        boolean[] numeric = new boolean[fields.size()];
        for (int i = 0; i < numeric.length; i++) {
            JfrReader.JfrField f = (JfrReader.JfrField) fields.get(i);
            numeric[i] = f.constantPool || f.type != stringType;
        }
        int count = this.getVarint();
        for (int i = 0; i < count; i++) {
            this.getVarlong();
            this.readFields(numeric);
        }
    }

    private void readFields(boolean[] numeric) {
        for (boolean n : numeric) {
            if (n) {
                this.getVarlong();
            } else {
                this.getString();
            }
        }
    }

    private void cacheEventTypes() {
        this.executionSample = this.getTypeId("jdk.ExecutionSample");
        this.nativeMethodSample = this.getTypeId("jdk.NativeMethodSample");
        this.allocationInNewTLAB = this.getTypeId("jdk.ObjectAllocationInNewTLAB");
        this.allocationOutsideTLAB = this.getTypeId("jdk.ObjectAllocationOutsideTLAB");
        this.allocationSample = this.getTypeId("jdk.ObjectAllocationSample");
        this.monitorEnter = this.getTypeId("jdk.JavaMonitorEnter");
        this.threadPark = this.getTypeId("jdk.ThreadPark");
        this.activeSetting = this.getTypeId("jdk.ActiveSetting");
        this.activeSettingHasStack = this.activeSetting >= 0 && ((JfrReader.JfrClass) this.typesByName.get("jdk.ActiveSetting")).field("stackTrace") != null;
    }

    private int getTypeId(String typeName) {
        JfrReader.JfrClass type = (JfrReader.JfrClass) this.typesByName.get(typeName);
        return type != null ? type.id : -1;
    }

    private int getVarint() {
        int result = 0;
        int shift = 0;
        while (true) {
            byte b = this.buf.get();
            result |= (b & 127) << shift;
            if (b >= 0) {
                return result;
            }
            shift += 7;
        }
    }

    private long getVarlong() {
        long result = 0L;
        for (int shift = 0; shift < 56; shift += 7) {
            byte b = this.buf.get();
            result |= ((long) b & 127L) << shift;
            if (b >= 0) {
                return result;
            }
        }
        return result | ((long) this.buf.get() & 255L) << 56;
    }

    private String getString() {
        switch(this.buf.get()) {
            case 0:
                return null;
            case 1:
                return "";
            case 2:
            default:
                throw new IllegalArgumentException("Invalid string encoding");
            case 3:
                return new String(this.getBytes(), StandardCharsets.UTF_8);
            case 4:
                char[] chars = new char[this.getVarint()];
                for (int i = 0; i < chars.length; i++) {
                    chars[i] = (char) this.getVarint();
                }
                return new String(chars);
            case 5:
                return new String(this.getBytes(), StandardCharsets.ISO_8859_1);
        }
    }

    private byte[] getBytes() {
        byte[] bytes = new byte[this.getVarint()];
        this.buf.get(bytes);
        return bytes;
    }

    private void seek(long pos) throws IOException {
        this.filePosition = pos;
        this.ch.position(pos);
        this.buf.rewind().flip();
    }

    private boolean ensureBytes(int needed) throws IOException {
        if (this.buf.remaining() >= needed) {
            return true;
        } else {
            this.filePosition = this.filePosition + (long) this.buf.position();
            if (this.buf.capacity() < needed) {
                ByteBuffer newBuf = ByteBuffer.allocateDirect(needed);
                newBuf.put(this.buf);
                this.buf = newBuf;
            } else {
                this.buf.compact();
            }
            while (this.ch.read(this.buf) > 0 && this.buf.position() < needed) {
            }
            this.buf.flip();
            return this.buf.limit() > 0;
        }
    }

    public static class AllocationSample extends JfrReader.Event {

        public final int classId;

        public final long allocationSize;

        public final long tlabSize;

        public AllocationSample(long time, int tid, int stackTraceId, int classId, long allocationSize, long tlabSize) {
            super(time, tid, stackTraceId);
            this.classId = classId;
            this.allocationSize = allocationSize;
            this.tlabSize = tlabSize;
        }

        @Override
        public int hashCode() {
            return this.classId * 127 + this.stackTraceId + (this.tlabSize == 0L ? 17 : 0);
        }

        @Override
        public boolean sameGroup(JfrReader.Event o) {
            if (!(o instanceof JfrReader.AllocationSample)) {
                return false;
            } else {
                JfrReader.AllocationSample a = (JfrReader.AllocationSample) o;
                return this.classId == a.classId && this.tlabSize == 0L == (a.tlabSize == 0L);
            }
        }

        @Override
        public long value() {
            return this.tlabSize != 0L ? this.tlabSize : this.allocationSize;
        }
    }

    public static class ClassRef {

        public final long name;

        public ClassRef(long name) {
            this.name = name;
        }
    }

    public static class ContendedLock extends JfrReader.Event {

        public final long duration;

        public final int classId;

        public ContendedLock(long time, int tid, int stackTraceId, long duration, int classId) {
            super(time, tid, stackTraceId);
            this.duration = duration;
            this.classId = classId;
        }

        @Override
        public int hashCode() {
            return this.classId * 127 + this.stackTraceId;
        }

        @Override
        public boolean sameGroup(JfrReader.Event o) {
            if (o instanceof JfrReader.ContendedLock) {
                JfrReader.ContendedLock c = (JfrReader.ContendedLock) o;
                return this.classId == c.classId;
            } else {
                return false;
            }
        }

        @Override
        public long value() {
            return this.duration;
        }
    }

    static class Element {

        void addChild(JfrReader.Element e) {
        }
    }

    public abstract static class Event implements Comparable<JfrReader.Event> {

        public final long time;

        public final int tid;

        public final int stackTraceId;

        protected Event(long time, int tid, int stackTraceId) {
            this.time = time;
            this.tid = tid;
            this.stackTraceId = stackTraceId;
        }

        public int compareTo(JfrReader.Event o) {
            return Long.compare(this.time, o.time);
        }

        public int hashCode() {
            return this.stackTraceId;
        }

        public boolean sameGroup(JfrReader.Event o) {
            return this.getClass() == o.getClass();
        }

        public long value() {
            return 1L;
        }
    }

    public static class EventAggregator {

        private static final int INITIAL_CAPACITY = 1024;

        private final boolean threads;

        private final boolean total;

        private JfrReader.Event[] keys;

        private long[] values;

        private int size;

        public EventAggregator(boolean threads, boolean total) {
            this.threads = threads;
            this.total = total;
            this.keys = new JfrReader.Event[1024];
            this.values = new long[1024];
        }

        public void collect(JfrReader.Event e) {
            int mask = this.keys.length - 1;
            int i;
            for (i = this.hashCode(e) & mask; this.keys[i] != null; i = i + 1 & mask) {
                if (this.sameGroup(this.keys[i], e)) {
                    this.values[i] = this.values[i] + (this.total ? e.value() : 1L);
                    return;
                }
            }
            this.keys[i] = e;
            this.values[i] = this.total ? e.value() : 1L;
            if (++this.size * 2 > this.keys.length) {
                this.resize(this.keys.length * 2);
            }
        }

        public long getValue(JfrReader.Event e) {
            int mask = this.keys.length - 1;
            int i = this.hashCode(e) & mask;
            while (this.keys[i] != null && !this.sameGroup(this.keys[i], e)) {
                i = i + 1 & mask;
            }
            return this.values[i];
        }

        public void forEach(JfrReader.EventAggregator.Visitor visitor) {
            for (int i = 0; i < this.keys.length; i++) {
                if (this.keys[i] != null) {
                    visitor.visit(this.keys[i], this.values[i]);
                }
            }
        }

        private int hashCode(JfrReader.Event e) {
            return e.hashCode() + (this.threads ? e.tid * 31 : 0);
        }

        private boolean sameGroup(JfrReader.Event e1, JfrReader.Event e2) {
            return e1.stackTraceId == e2.stackTraceId && (!this.threads || e1.tid == e2.tid) && e1.sameGroup(e2);
        }

        private void resize(int newCapacity) {
            JfrReader.Event[] newKeys = new JfrReader.Event[newCapacity];
            long[] newValues = new long[newCapacity];
            int mask = newKeys.length - 1;
            for (int i = 0; i < this.keys.length; i++) {
                if (this.keys[i] != null) {
                    int j = this.hashCode(this.keys[i]) & mask;
                    while (newKeys[j] != null) {
                        j = j + 1 & mask;
                    }
                    newKeys[j] = this.keys[i];
                    newValues[j] = this.values[i];
                }
            }
            this.keys = newKeys;
            this.values = newValues;
        }

        public interface Visitor {

            void visit(JfrReader.Event var1, long var2);
        }
    }

    public static class ExecutionSample extends JfrReader.Event {

        public final int threadState;

        public ExecutionSample(long time, int tid, int stackTraceId, int threadState) {
            super(time, tid, stackTraceId);
            this.threadState = threadState;
        }
    }

    static class JfrClass extends JfrReader.Element {

        final int id;

        final String name;

        final List<JfrReader.JfrField> fields;

        JfrClass(Map<String, String> attributes) {
            this.id = Integer.parseInt((String) attributes.get("id"));
            this.name = (String) attributes.get("name");
            this.fields = new ArrayList(2);
        }

        @Override
        void addChild(JfrReader.Element e) {
            if (e instanceof JfrReader.JfrField) {
                this.fields.add((JfrReader.JfrField) e);
            }
        }

        JfrReader.JfrField field(String name) {
            for (JfrReader.JfrField field : this.fields) {
                if (field.name.equals(name)) {
                    return field;
                }
            }
            return null;
        }
    }

    static class JfrField extends JfrReader.Element {

        final String name;

        final int type;

        final boolean constantPool;

        JfrField(Map<String, String> attributes) {
            this.name = (String) attributes.get("name");
            this.type = Integer.parseInt((String) attributes.get("class"));
            this.constantPool = "true".equals(attributes.get("constantPool"));
        }
    }

    public static class MethodRef {

        public final long cls;

        public final long name;

        public final long sig;

        public MethodRef(long cls, long name, long sig) {
            this.cls = cls;
            this.name = name;
            this.sig = sig;
        }
    }

    public static class StackTrace {

        public final long[] methods;

        public final byte[] types;

        public final int[] locations;

        public StackTrace(long[] methods, byte[] types, int[] locations) {
            this.methods = methods;
            this.types = types;
            this.locations = locations;
        }
    }
}