package me.lucko.spark.lib.bytebuddy.agent;

import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase.SECURITY_ATTRIBUTES;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.LPVOID;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinNT.SECURITY_DESCRIPTOR;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileLock;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public interface VirtualMachine {

    Properties getSystemProperties() throws IOException;

    Properties getAgentProperties() throws IOException;

    void loadAgent(String var1) throws IOException;

    void loadAgent(String var1, String var2) throws IOException;

    void loadAgentPath(String var1) throws IOException;

    void loadAgentPath(String var1, String var2) throws IOException;

    void loadAgentLibrary(String var1) throws IOException;

    void loadAgentLibrary(String var1, String var2) throws IOException;

    void startManagementAgent(Properties var1) throws IOException;

    String startLocalManagementAgent() throws IOException;

    void detach() throws IOException;

    public abstract static class AbstractBase implements VirtualMachine {

        @Override
        public void loadAgent(String jarFile) throws IOException {
            this.loadAgent(jarFile, null);
        }

        @Override
        public void loadAgentPath(String path) throws IOException {
            this.loadAgentPath(path, null);
        }

        @Override
        public void loadAgentLibrary(String library) throws IOException {
            this.loadAgentLibrary(library, null);
        }
    }

    public static class ForHotSpot extends VirtualMachine.AbstractBase {

        private static final String PROTOCOL_VERSION = "1";

        private static final String LOAD_COMMAND = "load";

        private static final String INSTRUMENT_COMMAND = "instrument";

        private static final String ARGUMENT_DELIMITER = "=";

        private final VirtualMachine.ForHotSpot.Connection connection;

        protected ForHotSpot(VirtualMachine.ForHotSpot.Connection connection) {
            this.connection = connection;
        }

        public static VirtualMachine attach(String processId) throws IOException {
            if (Platform.isWindows()) {
                return attach(processId, new VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.Factory());
            } else {
                return Platform.isSolaris() ? attach(processId, new VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.Factory(15, 100L, TimeUnit.MILLISECONDS)) : attach(processId, VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.Factory.withDefaultTemporaryFolder(15, 100L, TimeUnit.MILLISECONDS));
            }
        }

        public static VirtualMachine attach(String processId, VirtualMachine.ForHotSpot.Connection.Factory connectionFactory) throws IOException {
            return new VirtualMachine.ForHotSpot(connectionFactory.connect(processId));
        }

        private static void checkHeader(VirtualMachine.ForHotSpot.Connection.Response response) throws IOException {
            byte[] buffer = new byte[1];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int length;
            while ((length = response.read(buffer)) != -1) {
                if (length > 0) {
                    if (buffer[0] == 10) {
                        break;
                    }
                    outputStream.write(buffer[0]);
                }
            }
            switch(Integer.parseInt(outputStream.toString("UTF-8"))) {
                case 0:
                    return;
                case 101:
                    throw new IOException("Protocol mismatch with target VM");
                default:
                    buffer = new byte[1024];
                    outputStream = new ByteArrayOutputStream();
                    while ((length = response.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                    throw new IllegalStateException(outputStream.toString("UTF-8"));
            }
        }

        @Override
        public Properties getSystemProperties() throws IOException {
            return this.getProperties("properties");
        }

        @Override
        public Properties getAgentProperties() throws IOException {
            return this.getProperties("agentProperties");
        }

        private Properties getProperties(String command) throws IOException {
            VirtualMachine.ForHotSpot.Connection.Response response = this.connection.execute("1", command, null, null, null);
            Properties var7;
            try {
                checkHeader(response);
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int length;
                while ((length = response.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                Properties properties = new Properties();
                properties.load(new ByteArrayInputStream(outputStream.toByteArray()));
                var7 = properties;
            } finally {
                response.close();
            }
            return var7;
        }

        @Override
        public void loadAgent(String jarFile, String argument) throws IOException {
            this.load(jarFile, false, argument);
        }

        @Override
        public void loadAgentPath(String path, String argument) throws IOException {
            this.load(path, true, argument);
        }

        @Override
        public void loadAgentLibrary(String library, String argument) throws IOException {
            this.load(library, false, argument);
        }

        protected void load(String file, boolean absolute, String argument) throws IOException {
            VirtualMachine.ForHotSpot.Connection.Response response = this.connection.execute("1", "load", "instrument", Boolean.toString(absolute), argument == null ? file : file + "=" + argument);
            try {
                checkHeader(response);
            } finally {
                response.close();
            }
        }

        @Override
        public void startManagementAgent(Properties properties) throws IOException {
            StringBuilder stringBuilder = new StringBuilder("ManagementAgent.start ");
            boolean first = true;
            for (Entry<Object, Object> entry : properties.entrySet()) {
                if (!(entry.getKey() instanceof String) || !((String) entry.getKey()).startsWith("com.sun.management.")) {
                    throw new IllegalArgumentException("Illegal property name: " + entry.getKey());
                }
                if (first) {
                    first = false;
                } else {
                    stringBuilder.append(' ');
                }
                stringBuilder.append(((String) entry.getKey()).substring("com.sun.management.".length())).append('=');
                String value = entry.getValue().toString();
                if (value.contains(" ")) {
                    stringBuilder.append('\'').append(value).append('\'');
                } else {
                    stringBuilder.append(value);
                }
            }
            VirtualMachine.ForHotSpot.Connection.Response response = this.connection.execute("1", "jcmd", stringBuilder.toString(), null, null);
            try {
                checkHeader(response);
            } finally {
                response.close();
            }
        }

        @Override
        public String startLocalManagementAgent() throws IOException {
            VirtualMachine.ForHotSpot.Connection.Response response = this.connection.execute("1", "jcmd", "ManagementAgent.start_local", null, null);
            String var2;
            try {
                checkHeader(response);
                var2 = this.getAgentProperties().getProperty("com.sun.management.jmxremote.localConnectorAddress");
            } finally {
                response.close();
            }
            return var2;
        }

        @Override
        public void detach() throws IOException {
            this.connection.close();
        }

        public interface Connection extends Closeable {

            VirtualMachine.ForHotSpot.Connection.Response execute(String var1, String... var2) throws IOException;

            public interface Factory {

                VirtualMachine.ForHotSpot.Connection connect(String var1) throws IOException;

                public abstract static class ForSocketFile implements VirtualMachine.ForHotSpot.Connection.Factory {

                    private static final String SOCKET_FILE_PREFIX = ".java_pid";

                    private static final String ATTACH_FILE_PREFIX = ".attach_pid";

                    private final String temporaryDirectory;

                    private final int attempts;

                    private final long pause;

                    private final TimeUnit timeUnit;

                    protected ForSocketFile(String temporaryDirectory, int attempts, long pause, TimeUnit timeUnit) {
                        this.temporaryDirectory = temporaryDirectory;
                        this.attempts = attempts;
                        this.pause = pause;
                        this.timeUnit = timeUnit;
                    }

                    @SuppressFBWarnings(value = { "DMI_HARDCODED_ABSOLUTE_FILENAME" }, justification = "File name convention is specified.")
                    @Override
                    public VirtualMachine.ForHotSpot.Connection connect(String processId) throws IOException {
                        File socket = new File(this.temporaryDirectory, ".java_pid" + processId);
                        if (!socket.exists()) {
                            String target = ".attach_pid" + processId;
                            String path = "/proc/" + processId + "/cwd/" + target;
                            File attachFile = new File(path);
                            try {
                                if (!attachFile.createNewFile() && !attachFile.isFile()) {
                                    throw new IllegalStateException("Could not create attach file: " + attachFile);
                                }
                            } catch (IOException var13) {
                                attachFile = new File(this.temporaryDirectory, target);
                                if (!attachFile.createNewFile() && !attachFile.isFile()) {
                                    throw new IllegalStateException("Could not create attach file: " + attachFile);
                                }
                            }
                            try {
                                this.kill(processId, 3);
                                int attempts = this.attempts;
                                while (!socket.exists() && attempts-- > 0) {
                                    this.timeUnit.sleep(this.pause);
                                }
                                if (!socket.exists()) {
                                    throw new IllegalStateException("Target VM did not respond: " + processId);
                                }
                            } catch (InterruptedException var11) {
                                Thread.currentThread().interrupt();
                                throw new IllegalStateException(var11);
                            } finally {
                                if (!attachFile.delete()) {
                                    attachFile.deleteOnExit();
                                }
                            }
                        }
                        return this.doConnect(socket);
                    }

                    protected abstract void kill(String var1, int var2);

                    protected abstract VirtualMachine.ForHotSpot.Connection doConnect(File var1) throws IOException;
                }
            }

            public static class ForJnaPosixSocket extends VirtualMachine.ForHotSpot.Connection.OnPersistentByteChannel<Integer> {

                private final VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.PosixLibrary library;

                private final File socket;

                protected ForJnaPosixSocket(VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.PosixLibrary library, File socket) {
                    this.library = library;
                    this.socket = socket;
                }

                protected Integer connect() {
                    int handle = this.library.socket(1, 1, 0);
                    try {
                        VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.PosixLibrary.SocketAddress address = new VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.PosixLibrary.SocketAddress();
                        Integer var3;
                        try {
                            address.setPath(this.socket.getAbsolutePath());
                            this.library.connect(handle, address, address.size());
                            var3 = handle;
                        } finally {
                            VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.PosixLibrary.SocketAddress var9 = null;
                        }
                        return var3;
                    } catch (RuntimeException var8) {
                        this.library.close(handle);
                        throw var8;
                    }
                }

                protected int read(Integer handle, byte[] buffer) {
                    int read = this.library.read(handle, ByteBuffer.wrap(buffer), buffer.length);
                    return read == 0 ? -1 : read;
                }

                protected void write(Integer handle, byte[] buffer) {
                    this.library.write(handle, ByteBuffer.wrap(buffer), buffer.length);
                }

                protected void close(Integer handle) {
                    this.library.close(handle);
                }

                public void close() {
                }

                public static class Factory extends VirtualMachine.ForHotSpot.Connection.Factory.ForSocketFile {

                    private final VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.PosixLibrary library = (VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.PosixLibrary) Native.loadLibrary("c", VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.PosixLibrary.class);

                    public Factory(String temporaryDirectory, int attempts, long pause, TimeUnit timeUnit) {
                        super(temporaryDirectory, attempts, pause, timeUnit);
                    }

                    public static VirtualMachine.ForHotSpot.Connection.Factory withDefaultTemporaryFolder(int attempts, long pause, TimeUnit timeUnit) {
                        String temporaryDirectory;
                        if (Platform.isMac()) {
                            VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.Factory.MacLibrary library = (VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.Factory.MacLibrary) Native.loadLibrary("c", VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.Factory.MacLibrary.class);
                            Memory memory = new Memory(4096L);
                            try {
                                long length = library.confstr(65537, memory, memory.size());
                                if (length != 0L && length <= 4096L) {
                                    temporaryDirectory = memory.getString(0L);
                                } else {
                                    temporaryDirectory = "/tmp";
                                }
                            } finally {
                                Memory var12 = null;
                            }
                        } else {
                            temporaryDirectory = "/tmp";
                        }
                        return new VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.Factory(temporaryDirectory, attempts, pause, timeUnit);
                    }

                    @Override
                    protected void kill(String processId, int signal) {
                        this.library.kill(Integer.parseInt(processId), signal);
                    }

                    @Override
                    public VirtualMachine.ForHotSpot.Connection doConnect(File socket) {
                        return new VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket(this.library, socket);
                    }

                    public interface MacLibrary extends Library {

                        int CS_DARWIN_USER_TEMP_DIR = 65537;

                        long confstr(int var1, Pointer var2, long var3);
                    }
                }

                protected interface PosixLibrary extends Library {

                    int kill(int var1, int var2) throws LastErrorException;

                    int socket(int var1, int var2, int var3) throws LastErrorException;

                    int connect(int var1, VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.PosixLibrary.SocketAddress var2, int var3) throws LastErrorException;

                    int read(int var1, ByteBuffer var2, int var3) throws LastErrorException;

                    int write(int var1, ByteBuffer var2, int var3) throws LastErrorException;

                    int close(int var1) throws LastErrorException;

                    public static class SocketAddress extends Structure {

                        @SuppressFBWarnings(value = { "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" }, justification = "Field required by native implementation.")
                        public short family = 1;

                        public byte[] path = new byte[100];

                        protected void setPath(String path) {
                            try {
                                System.arraycopy(path.getBytes("UTF-8"), 0, this.path, 0, path.length());
                                System.arraycopy(new byte[] { 0 }, 0, this.path, path.length(), 1);
                            } catch (UnsupportedEncodingException var3) {
                                throw new IllegalStateException(var3);
                            }
                        }

                        protected List<String> getFieldOrder() {
                            return Arrays.asList("family", "path");
                        }
                    }
                }
            }

            public static class ForJnaSolarisDoor implements VirtualMachine.ForHotSpot.Connection {

                private final VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.SolarisLibrary library;

                private final File socket;

                protected ForJnaSolarisDoor(VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.SolarisLibrary library, File socket) {
                    this.library = library;
                    this.socket = socket;
                }

                @SuppressFBWarnings(value = { "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD", "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" }, justification = "This pattern is required for use of JNA.")
                @Override
                public VirtualMachine.ForHotSpot.Connection.Response execute(String protocol, String... argument) throws IOException {
                    int handle = this.library.open(this.socket.getAbsolutePath(), 2);
                    VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.Response var39;
                    try {
                        VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.SolarisLibrary.DoorArgument door = new VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.SolarisLibrary.DoorArgument();
                        try {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            outputStream.write(protocol.getBytes("UTF-8"));
                            outputStream.write(0);
                            for (String anArgument : argument) {
                                if (anArgument != null) {
                                    outputStream.write(anArgument.getBytes("UTF-8"));
                                }
                                outputStream.write(0);
                            }
                            door.dataSize = outputStream.size();
                            Memory dataPointer = new Memory((long) outputStream.size());
                            try {
                                dataPointer.write(0L, outputStream.toByteArray(), 0, outputStream.size());
                                door.dataPointer = dataPointer;
                                Memory result = new Memory(128L);
                                try {
                                    door.resultPointer = result;
                                    door.resultSize = (int) result.size();
                                    if (this.library.door_call(handle, door) != 0) {
                                        throw new IllegalStateException("Door call to target VM failed");
                                    }
                                    if (door.resultSize < 4 || door.resultPointer.getInt(0L) != 0) {
                                        throw new IllegalStateException("Target VM could not execute door call");
                                    }
                                    if (door.descriptorCount != 1 || door.descriptorPointer == null) {
                                        throw new IllegalStateException("Did not receive communication descriptor from target VM");
                                    }
                                    var39 = new VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.Response(this.library, door.descriptorPointer.getInt(4L));
                                } finally {
                                    Memory var38 = null;
                                }
                            } finally {
                                Memory var36 = null;
                            }
                        } finally {
                            VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.SolarisLibrary.DoorArgument var34 = null;
                        }
                    } finally {
                        this.library.close(handle);
                    }
                    return var39;
                }

                public void close() {
                }

                public static class Factory extends VirtualMachine.ForHotSpot.Connection.Factory.ForSocketFile {

                    private final VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.SolarisLibrary library = (VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.SolarisLibrary) Native.loadLibrary("c", VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.SolarisLibrary.class);

                    public Factory(int attempts, long pause, TimeUnit timeUnit) {
                        super("/tmp", attempts, pause, timeUnit);
                    }

                    @Override
                    protected void kill(String processId, int signal) {
                        this.library.kill(Integer.parseInt(processId), signal);
                    }

                    @Override
                    protected VirtualMachine.ForHotSpot.Connection doConnect(File socket) {
                        return new VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor(this.library, socket);
                    }
                }

                protected static class Response implements VirtualMachine.ForHotSpot.Connection.Response {

                    private final VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.SolarisLibrary library;

                    private final int handle;

                    protected Response(VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.SolarisLibrary library, int handle) {
                        this.library = library;
                        this.handle = handle;
                    }

                    @Override
                    public int read(byte[] buffer) {
                        int read = this.library.read(this.handle, ByteBuffer.wrap(buffer), buffer.length);
                        return read == 0 ? -1 : read;
                    }

                    public void close() {
                        this.library.close(this.handle);
                    }
                }

                protected interface SolarisLibrary extends Library {

                    int kill(int var1, int var2) throws LastErrorException;

                    int open(String var1, int var2) throws LastErrorException;

                    int read(int var1, ByteBuffer var2, int var3) throws LastErrorException;

                    int close(int var1) throws LastErrorException;

                    int door_call(int var1, VirtualMachine.ForHotSpot.Connection.ForJnaSolarisDoor.SolarisLibrary.DoorArgument var2) throws LastErrorException;

                    public static class DoorArgument extends Structure {

                        public Pointer dataPointer;

                        public int dataSize;

                        public Pointer descriptorPointer;

                        public int descriptorCount;

                        public Pointer resultPointer;

                        public int resultSize;

                        protected List<String> getFieldOrder() {
                            return Arrays.asList("dataPointer", "dataSize", "descriptorPointer", "descriptorCount", "resultPointer", "resultSize");
                        }
                    }
                }
            }

            public static class ForJnaWindowsNamedPipe implements VirtualMachine.ForHotSpot.Connection {

                private static final int MEM_RELEASE = 32768;

                private final VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.WindowsLibrary library;

                private final VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.WindowsAttachLibrary attachLibrary;

                private final HANDLE process;

                private final LPVOID code;

                private final SecureRandom random;

                protected ForJnaWindowsNamedPipe(VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.WindowsLibrary library, VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.WindowsAttachLibrary attachLibrary, HANDLE process, LPVOID code) {
                    this.library = library;
                    this.attachLibrary = attachLibrary;
                    this.process = process;
                    this.code = code;
                    this.random = new SecureRandom();
                }

                @Override
                public VirtualMachine.ForHotSpot.Connection.Response execute(String protocol, String... argument) {
                    if (!"1".equals(protocol)) {
                        throw new IllegalArgumentException("Unknown protocol version: " + protocol);
                    } else if (argument.length > 4) {
                        throw new IllegalArgumentException("Cannot supply more then four arguments to Windows attach mechanism: " + Arrays.asList(argument));
                    } else {
                        String name = "\\\\.\\pipe\\javatool" + Math.abs(this.random.nextInt() + 1);
                        HANDLE pipe = Kernel32.INSTANCE.CreateNamedPipe(name, 1, 0, 1, 4096, 8192, 0, null);
                        if (pipe == null) {
                            throw new Win32Exception(Native.getLastError());
                        } else {
                            try {
                                LPVOID data = this.attachLibrary.allocate_remote_argument(this.process, name, argument.length < 1 ? null : argument[0], argument.length < 2 ? null : argument[1], argument.length < 3 ? null : argument[2], argument.length < 4 ? null : argument[3]);
                                if (data == null) {
                                    throw new Win32Exception(Native.getLastError());
                                } else {
                                    VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.NamedPipeResponse var21;
                                    try {
                                        HANDLE thread = this.library.CreateRemoteThread(this.process, null, 0, this.code.getPointer(), data.getPointer(), null, null);
                                        if (thread == null) {
                                            throw new Win32Exception(Native.getLastError());
                                        }
                                        try {
                                            int result = Kernel32.INSTANCE.WaitForSingleObject(thread, -1);
                                            if (result != 0) {
                                                throw new Win32Exception(result);
                                            }
                                            IntByReference exitCode = new IntByReference();
                                            if (!this.library.GetExitCodeThread(thread, exitCode)) {
                                                throw new Win32Exception(Native.getLastError());
                                            }
                                            if (exitCode.getValue() != 0) {
                                                throw new IllegalStateException("Target VM could not dispatch command successfully: " + exitCode.getValue());
                                            }
                                            if (!Kernel32.INSTANCE.ConnectNamedPipe(pipe, null)) {
                                                int code = Native.getLastError();
                                                if (code != 535) {
                                                    throw new Win32Exception(code);
                                                }
                                            }
                                            var21 = new VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.NamedPipeResponse(pipe);
                                        } finally {
                                            if (!Kernel32.INSTANCE.CloseHandle(thread)) {
                                                throw new Win32Exception(Native.getLastError());
                                            }
                                        }
                                    } finally {
                                        if (!this.library.VirtualFreeEx(this.process, data.getPointer(), 0, 32768)) {
                                            throw new Win32Exception(Native.getLastError());
                                        }
                                    }
                                    return var21;
                                }
                            } catch (Throwable var20) {
                                if (!Kernel32.INSTANCE.CloseHandle(pipe)) {
                                    throw new Win32Exception(Native.getLastError());
                                } else if (var20 instanceof RuntimeException) {
                                    throw (RuntimeException) var20;
                                } else {
                                    throw new IllegalStateException(var20);
                                }
                            }
                        }
                    }
                }

                public void close() {
                    try {
                        if (!this.library.VirtualFreeEx(this.process, this.code.getPointer(), 0, 32768)) {
                            throw new Win32Exception(Native.getLastError());
                        }
                    } finally {
                        if (!Kernel32.INSTANCE.CloseHandle(this.process)) {
                            throw new Win32Exception(Native.getLastError());
                        }
                    }
                }

                public static class Factory implements VirtualMachine.ForHotSpot.Connection.Factory {

                    public static final String LIBRARY_NAME = "me.lucko.spark.lib.bytebuddy.library.name";

                    private final VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.WindowsLibrary library = (VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.WindowsLibrary) Native.loadLibrary("kernel32", VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.WindowsLibrary.class, W32APIOptions.DEFAULT_OPTIONS);

                    private final VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.WindowsAttachLibrary attachLibrary = (VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.WindowsAttachLibrary) Native.loadLibrary(System.getProperty("me.lucko.spark.lib.bytebuddy.library.name", "attach_hotspot_windows"), VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe.WindowsAttachLibrary.class);

                    @Override
                    public VirtualMachine.ForHotSpot.Connection connect(String processId) {
                        HANDLE process = Kernel32.INSTANCE.OpenProcess(2039803, false, Integer.parseInt(processId));
                        if (process == null) {
                            throw new Win32Exception(Native.getLastError());
                        } else {
                            try {
                                LPVOID code = this.attachLibrary.allocate_remote_code(process);
                                if (code == null) {
                                    throw new Win32Exception(Native.getLastError());
                                } else {
                                    return new VirtualMachine.ForHotSpot.Connection.ForJnaWindowsNamedPipe(this.library, this.attachLibrary, process, code);
                                }
                            } catch (Throwable var4) {
                                if (!Kernel32.INSTANCE.CloseHandle(process)) {
                                    throw new Win32Exception(Native.getLastError());
                                } else if (var4 instanceof RuntimeException) {
                                    throw (RuntimeException) var4;
                                } else {
                                    throw new IllegalStateException(var4);
                                }
                            }
                        }
                    }
                }

                protected static class NamedPipeResponse implements VirtualMachine.ForHotSpot.Connection.Response {

                    private final HANDLE pipe;

                    protected NamedPipeResponse(HANDLE pipe) {
                        this.pipe = pipe;
                    }

                    @Override
                    public int read(byte[] buffer) {
                        IntByReference read = new IntByReference();
                        if (!Kernel32.INSTANCE.ReadFile(this.pipe, buffer, buffer.length, read, null)) {
                            int code = Native.getLastError();
                            if (code == 109) {
                                return -1;
                            } else {
                                throw new Win32Exception(code);
                            }
                        } else {
                            return read.getValue();
                        }
                    }

                    public void close() {
                        try {
                            if (!Kernel32.INSTANCE.DisconnectNamedPipe(this.pipe)) {
                                throw new Win32Exception(Native.getLastError());
                            }
                        } finally {
                            if (!Kernel32.INSTANCE.CloseHandle(this.pipe)) {
                                throw new Win32Exception(Native.getLastError());
                            }
                        }
                    }
                }

                protected interface WindowsAttachLibrary extends StdCallLibrary {

                    LPVOID allocate_remote_code(HANDLE var1);

                    LPVOID allocate_remote_argument(HANDLE var1, String var2, String var3, String var4, String var5, String var6);
                }

                protected interface WindowsLibrary extends StdCallLibrary {

                    Pointer VirtualAllocEx(HANDLE var1, Pointer var2, int var3, int var4, int var5);

                    boolean VirtualFreeEx(HANDLE var1, Pointer var2, int var3, int var4);

                    HANDLE CreateRemoteThread(HANDLE var1, SECURITY_ATTRIBUTES var2, int var3, Pointer var4, Pointer var5, DWORD var6, Pointer var7);

                    boolean GetExitCodeThread(HANDLE var1, IntByReference var2);
                }
            }

            public abstract static class OnPersistentByteChannel<T> implements VirtualMachine.ForHotSpot.Connection {

                private static final byte[] BLANK = new byte[] { 0 };

                @Override
                public VirtualMachine.ForHotSpot.Connection.Response execute(String protocol, String... argument) throws IOException {
                    T connection = this.connect();
                    try {
                        this.write(connection, protocol.getBytes("UTF-8"));
                        this.write(connection, BLANK);
                        for (String anArgument : argument) {
                            if (anArgument != null) {
                                this.write(connection, anArgument.getBytes("UTF-8"));
                            }
                            this.write(connection, BLANK);
                        }
                        return new VirtualMachine.ForHotSpot.Connection.OnPersistentByteChannel.Response(connection);
                    } catch (Throwable var8) {
                        this.close(connection);
                        if (var8 instanceof RuntimeException) {
                            throw (RuntimeException) var8;
                        } else if (var8 instanceof IOException) {
                            throw (IOException) var8;
                        } else {
                            throw new IllegalStateException(var8);
                        }
                    }
                }

                protected abstract T connect() throws IOException;

                protected abstract void close(T var1) throws IOException;

                protected abstract void write(T var1, byte[] var2) throws IOException;

                protected abstract int read(T var1, byte[] var2) throws IOException;

                private class Response implements VirtualMachine.ForHotSpot.Connection.Response {

                    private final T connection;

                    private Response(T connection) {
                        this.connection = connection;
                    }

                    @Override
                    public int read(byte[] buffer) throws IOException {
                        return OnPersistentByteChannel.this.read(this.connection, buffer);
                    }

                    public void close() throws IOException {
                        OnPersistentByteChannel.this.close(this.connection);
                    }
                }
            }

            public interface Response extends Closeable {

                int read(byte[] var1) throws IOException;
            }
        }
    }

    public static class ForOpenJ9 extends VirtualMachine.AbstractBase {

        private static final String IBM_TEMPORARY_FOLDER = "com.ibm.tools.attach.directory";

        private final Socket socket;

        protected ForOpenJ9(Socket socket) {
            this.socket = socket;
        }

        public static VirtualMachine attach(String processId) throws IOException {
            return attach(processId, 5000, (VirtualMachine.ForOpenJ9.Dispatcher) (Platform.isWindows() ? new VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment() : new VirtualMachine.ForOpenJ9.Dispatcher.ForJnaPosixEnvironment(15, 100L, TimeUnit.MILLISECONDS)));
        }

        public static VirtualMachine attach(String processId, int timeout, VirtualMachine.ForOpenJ9.Dispatcher dispatcher) throws IOException {
            File directory = new File(System.getProperty("com.ibm.tools.attach.directory", dispatcher.getTemporaryFolder()), ".com_ibm_tools_attach");
            RandomAccessFile attachLock = new RandomAccessFile(new File(directory, "_attachlock"), "rw");
            VirtualMachine.ForOpenJ9 var257;
            try {
                FileLock attachLockLock = attachLock.getChannel().lock();
                try {
                    RandomAccessFile master = new RandomAccessFile(new File(directory, "_master"), "rw");
                    List<Properties> virtualMachines;
                    try {
                        FileLock masterLock = master.getChannel().lock();
                        try {
                            File[] vmFolder = directory.listFiles();
                            if (vmFolder == null) {
                                throw new IllegalStateException("No descriptor files found in " + directory);
                            }
                            long userId = (long) dispatcher.userId();
                            virtualMachines = new ArrayList();
                            for (File aVmFolder : vmFolder) {
                                if (aVmFolder.isDirectory() && (long) dispatcher.getOwnerIdOf(aVmFolder) == userId) {
                                    File attachInfo = new File(aVmFolder, "attachInfo");
                                    if (attachInfo.isFile()) {
                                        Properties virtualMachine = new Properties();
                                        FileInputStream inputStream = new FileInputStream(attachInfo);
                                        try {
                                            virtualMachine.load(inputStream);
                                        } finally {
                                            inputStream.close();
                                        }
                                        int socket = Integer.parseInt(virtualMachine.getProperty("processId"));
                                        long targetUserId;
                                        try {
                                            targetUserId = Long.parseLong(virtualMachine.getProperty("userUid"));
                                        } catch (NumberFormatException var226) {
                                            targetUserId = 0L;
                                        }
                                        if (userId != 0L && targetUserId == 0L) {
                                            targetUserId = (long) dispatcher.getOwnerIdOf(attachInfo);
                                        }
                                        if ((long) socket == 0L || dispatcher.isExistingProcess(socket)) {
                                            virtualMachines.add(virtualMachine);
                                        } else if (userId == 0L || targetUserId == userId) {
                                            File[] vmFile = aVmFolder.listFiles();
                                            if (vmFile != null) {
                                                for (File aVmFile : vmFile) {
                                                    if (!aVmFile.delete()) {
                                                        aVmFile.deleteOnExit();
                                                    }
                                                }
                                            }
                                            if (!aVmFolder.delete()) {
                                                aVmFolder.deleteOnExit();
                                            }
                                        }
                                    }
                                }
                            }
                        } finally {
                            masterLock.release();
                        }
                    } finally {
                        master.close();
                    }
                    Properties var236 = null;
                    for (Properties virtualMachine : virtualMachines) {
                        if (virtualMachine.getProperty("processId").equalsIgnoreCase(processId)) {
                            var236 = virtualMachine;
                            break;
                        }
                    }
                    if (var236 == null) {
                        throw new IllegalStateException("Could not locate target process info in " + directory);
                    }
                    ServerSocket serverSocket = new ServerSocket(0);
                    try {
                        serverSocket.setSoTimeout(timeout);
                        File receiver = new File(directory, var236.getProperty("vmId"));
                        String key = Long.toHexString(new SecureRandom().nextLong());
                        File reply = new File(receiver, "replyInfo");
                        try {
                            if (reply.createNewFile()) {
                                dispatcher.setPermissions(reply, 384);
                            }
                            FileOutputStream outputStream = new FileOutputStream(reply);
                            try {
                                outputStream.write(key.getBytes("UTF-8"));
                                outputStream.write("\n".getBytes("UTF-8"));
                                outputStream.write(Long.toString((long) serverSocket.getLocalPort()).getBytes("UTF-8"));
                                outputStream.write("\n".getBytes("UTF-8"));
                            } finally {
                                outputStream.close();
                            }
                            HashMap var243 = new HashMap();
                            try {
                                String pid = Long.toString((long) dispatcher.pid());
                                for (Properties virtualMachinex : virtualMachines) {
                                    if (!virtualMachinex.getProperty("processId").equalsIgnoreCase(pid)) {
                                        String attachNotificationSync = virtualMachinex.getProperty("attachNotificationSync");
                                        RandomAccessFile syncFile = new RandomAccessFile(attachNotificationSync == null ? new File(directory, "attachNotificationSync") : new File(attachNotificationSync), "rw");
                                        try {
                                            var243.put(syncFile, syncFile.getChannel().lock());
                                        } catch (IOException var224) {
                                            syncFile.close();
                                        }
                                    }
                                }
                                int notifications = 0;
                                File[] item = directory.listFiles();
                                if (item != null) {
                                    for (File anItem : item) {
                                        String name = anItem.getName();
                                        if (!name.startsWith(".trash_") && !name.equalsIgnoreCase("_attachlock") && !name.equalsIgnoreCase("_master") && !name.equalsIgnoreCase("_notifier")) {
                                            notifications++;
                                        }
                                    }
                                }
                                boolean global = Boolean.parseBoolean(var236.getProperty("globalSemaphore"));
                                dispatcher.incrementSemaphore(directory, "_notifier", global, notifications);
                                try {
                                    Socket socket = serverSocket.accept();
                                    String answer = new String(read(socket), "UTF-8");
                                    if (!answer.contains(' ' + key + ' ')) {
                                        socket.close();
                                        throw new IllegalStateException("Unexpected answered to attachment: " + answer);
                                    }
                                    var257 = new VirtualMachine.ForOpenJ9(socket);
                                } finally {
                                    dispatcher.decrementSemaphore(directory, "_notifier", global, notifications);
                                }
                            } finally {
                                for (Entry<RandomAccessFile, FileLock> entry : var243.entrySet()) {
                                    try {
                                        try {
                                            ((FileLock) entry.getValue()).release();
                                        } finally {
                                            ((RandomAccessFile) entry.getKey()).close();
                                        }
                                    } catch (Throwable var223) {
                                    }
                                }
                            }
                        } finally {
                            if (!reply.delete()) {
                                reply.deleteOnExit();
                            }
                        }
                    } finally {
                        serverSocket.close();
                    }
                } finally {
                    attachLockLock.release();
                }
            } finally {
                attachLock.close();
            }
            return var257;
        }

        @Override
        public Properties getSystemProperties() throws IOException {
            write(this.socket, "ATTACH_GETSYSTEMPROPERTIES".getBytes("UTF-8"));
            Properties properties = new Properties();
            properties.load(new ByteArrayInputStream(read(this.socket)));
            return properties;
        }

        @Override
        public Properties getAgentProperties() throws IOException {
            write(this.socket, "ATTACH_GETAGENTPROPERTIES".getBytes("UTF-8"));
            Properties properties = new Properties();
            properties.load(new ByteArrayInputStream(read(this.socket)));
            return properties;
        }

        @Override
        public void loadAgent(String jarFile, String argument) throws IOException {
            write(this.socket, ("ATTACH_LOADAGENT(instrument," + jarFile + '=' + (argument == null ? "" : argument) + ')').getBytes("UTF-8"));
            String answer = new String(read(this.socket), "UTF-8");
            if (answer.startsWith("ATTACH_ERR")) {
                throw new IllegalStateException("Target VM failed loading agent: " + answer);
            } else if (!answer.startsWith("ATTACH_ACK") && !answer.startsWith("ATTACH_RESULT=")) {
                throw new IllegalStateException("Unexpected response: " + answer);
            }
        }

        @Override
        public void loadAgentPath(String path, String argument) throws IOException {
            write(this.socket, ("ATTACH_LOADAGENTPATH(" + path + (argument == null ? "" : ',' + argument) + ')').getBytes("UTF-8"));
            String answer = new String(read(this.socket), "UTF-8");
            if (answer.startsWith("ATTACH_ERR")) {
                throw new IllegalStateException("Target VM failed loading native agent: " + answer);
            } else if (!answer.startsWith("ATTACH_ACK") && !answer.startsWith("ATTACH_RESULT=")) {
                throw new IllegalStateException("Unexpected response: " + answer);
            }
        }

        @Override
        public void loadAgentLibrary(String library, String argument) throws IOException {
            write(this.socket, ("ATTACH_LOADAGENTLIBRARY(" + library + (argument == null ? "" : ',' + argument) + ')').getBytes("UTF-8"));
            String answer = new String(read(this.socket), "UTF-8");
            if (answer.startsWith("ATTACH_ERR")) {
                throw new IllegalStateException("Target VM failed loading native library: " + answer);
            } else if (!answer.startsWith("ATTACH_ACK") && !answer.startsWith("ATTACH_RESULT=")) {
                throw new IllegalStateException("Unexpected response: " + answer);
            }
        }

        @Override
        public void startManagementAgent(Properties properties) throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            properties.store(outputStream, null);
            write(this.socket, "ATTACH_START_MANAGEMENT_AGENT".getBytes("UTF-8"));
            write(this.socket, outputStream.toByteArray());
            String answer = new String(read(this.socket), "UTF-8");
            if (answer.startsWith("ATTACH_ERR")) {
                throw new IllegalStateException("Target VM could not start management agent: " + answer);
            } else if (!answer.startsWith("ATTACH_ACK") && !answer.startsWith("ATTACH_RESULT=")) {
                throw new IllegalStateException("Unexpected response: " + answer);
            }
        }

        @Override
        public String startLocalManagementAgent() throws IOException {
            write(this.socket, "ATTACH_START_LOCAL_MANAGEMENT_AGENT".getBytes("UTF-8"));
            String answer = new String(read(this.socket), "UTF-8");
            if (answer.startsWith("ATTACH_ERR")) {
                throw new IllegalStateException("Target VM could not start management agent: " + answer);
            } else if (answer.startsWith("ATTACH_ACK")) {
                return answer.substring("ATTACH_ACK".length());
            } else if (answer.startsWith("ATTACH_RESULT=")) {
                return answer.substring("ATTACH_RESULT=".length());
            } else {
                throw new IllegalStateException("Unexpected response: " + answer);
            }
        }

        @Override
        public void detach() throws IOException {
            try {
                write(this.socket, "ATTACH_DETACH".getBytes("UTF-8"));
                read(this.socket);
            } finally {
                this.socket.close();
            }
        }

        private static void write(Socket socket, byte[] value) throws IOException {
            socket.getOutputStream().write(value);
            socket.getOutputStream().write(0);
            socket.getOutputStream().flush();
        }

        private static byte[] read(Socket socket) throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = socket.getInputStream().read(buffer)) != -1) {
                if (length > 0 && buffer[length - 1] == 0) {
                    outputStream.write(buffer, 0, length - 1);
                    break;
                }
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toByteArray();
        }

        public interface Dispatcher {

            String getTemporaryFolder();

            int pid();

            int userId();

            boolean isExistingProcess(int var1);

            int getOwnerIdOf(File var1);

            void setPermissions(File var1, int var2);

            void incrementSemaphore(File var1, String var2, boolean var3, int var4);

            void decrementSemaphore(File var1, String var2, boolean var3, int var4);

            public static class ForJnaPosixEnvironment implements VirtualMachine.ForOpenJ9.Dispatcher {

                private final VirtualMachine.ForOpenJ9.Dispatcher.ForJnaPosixEnvironment.PosixLibrary library;

                private final int attempts;

                private final long pause;

                private final TimeUnit timeUnit;

                public ForJnaPosixEnvironment(int attempts, long pause, TimeUnit timeUnit) {
                    this.attempts = attempts;
                    this.pause = pause;
                    this.timeUnit = timeUnit;
                    this.library = (VirtualMachine.ForOpenJ9.Dispatcher.ForJnaPosixEnvironment.PosixLibrary) Native.loadLibrary("c", VirtualMachine.ForOpenJ9.Dispatcher.ForJnaPosixEnvironment.PosixLibrary.class);
                }

                @Override
                public String getTemporaryFolder() {
                    String temporaryFolder = System.getenv("TMPDIR");
                    return temporaryFolder == null ? "/tmp" : temporaryFolder;
                }

                @Override
                public int pid() {
                    return this.library.getpid();
                }

                @Override
                public int userId() {
                    return this.library.getuid();
                }

                @Override
                public boolean isExistingProcess(int processId) {
                    return this.library.kill(processId, 0) != 3;
                }

                @SuppressFBWarnings(value = { "OS_OPEN_STREAM" }, justification = "The stream life-cycle is bound to its process.")
                @Override
                public int getOwnerIdOf(File file) {
                    try {
                        String statUserSwitch = Platform.isMac() ? "-f" : "-c";
                        Process process = Runtime.getRuntime().exec("stat " + statUserSwitch + " %u " + file.getAbsolutePath());
                        int attempts = this.attempts;
                        boolean exited = false;
                        String line = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8")).readLine();
                        while (true) {
                            try {
                                if (process.exitValue() != 0) {
                                    throw new IllegalStateException("Error while executing stat");
                                }
                                exited = true;
                                break;
                            } catch (IllegalThreadStateException var10) {
                                try {
                                    Thread.sleep(this.timeUnit.toMillis(this.pause));
                                } catch (InterruptedException var9) {
                                    Thread.currentThread().interrupt();
                                    throw new IllegalStateException(var9);
                                }
                                if (--attempts <= 0) {
                                    break;
                                }
                            }
                        }
                        if (!exited) {
                            process.destroy();
                            throw new IllegalStateException("Command for stat did not exit in time");
                        } else {
                            return Integer.parseInt(line);
                        }
                    } catch (IOException var11) {
                        throw new IllegalStateException("Unable to execute stat command", var11);
                    }
                }

                @Override
                public void setPermissions(File file, int permissions) {
                    this.library.chmod(file.getAbsolutePath(), permissions);
                }

                @Override
                public void incrementSemaphore(File directory, String name, boolean global, int count) {
                    this.notifySemaphore(directory, name, count, (short) 1, (short) 0, false);
                }

                @Override
                public void decrementSemaphore(File directory, String name, boolean global, int count) {
                    this.notifySemaphore(directory, name, count, (short) -1, (short) 6144, true);
                }

                @SuppressFBWarnings(value = { "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD", "UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD" }, justification = "Modifier is required by JNA.")
                private void notifySemaphore(File directory, String name, int count, short operation, short flags, boolean acceptUnavailable) {
                    int semaphore = this.library.semget(this.library.ftok(new File(directory, name).getAbsolutePath(), 161), 2, 438);
                    VirtualMachine.ForOpenJ9.Dispatcher.ForJnaPosixEnvironment.PosixLibrary.SemaphoreOperation target = new VirtualMachine.ForOpenJ9.Dispatcher.ForJnaPosixEnvironment.PosixLibrary.SemaphoreOperation();
                    target.operation = operation;
                    target.flags = flags;
                    try {
                        while (count-- > 0) {
                            try {
                                this.library.semop(semaphore, target, 1);
                            } catch (LastErrorException var13) {
                                if (acceptUnavailable && (Native.getLastError() == 11 || Native.getLastError() == 35)) {
                                    break;
                                }
                                throw var13;
                            }
                        }
                    } finally {
                        VirtualMachine.ForOpenJ9.Dispatcher.ForJnaPosixEnvironment.PosixLibrary.SemaphoreOperation var15 = null;
                    }
                }

                protected interface PosixLibrary extends Library {

                    int NULL_SIGNAL = 0;

                    int ESRCH = 3;

                    int EAGAIN = 11;

                    int EDEADLK = 35;

                    short SEM_UNDO = 4096;

                    short IPC_NOWAIT = 2048;

                    int getpid() throws LastErrorException;

                    int getuid() throws LastErrorException;

                    int kill(int var1, int var2) throws LastErrorException;

                    int chmod(String var1, int var2) throws LastErrorException;

                    int ftok(String var1, int var2) throws LastErrorException;

                    int semget(int var1, int var2, int var3) throws LastErrorException;

                    int semop(int var1, VirtualMachine.ForOpenJ9.Dispatcher.ForJnaPosixEnvironment.PosixLibrary.SemaphoreOperation var2, int var3) throws LastErrorException;

                    public static class SemaphoreOperation extends Structure {

                        public short number;

                        public short operation;

                        public short flags;

                        protected List<String> getFieldOrder() {
                            return Arrays.asList("number", "operation", "flags");
                        }
                    }
                }
            }

            public static class ForJnaWindowsEnvironment implements VirtualMachine.ForOpenJ9.Dispatcher {

                private static final int NO_USER_ID = 0;

                private static final String CREATION_MUTEX_NAME = "j9shsemcreationMutex";

                private final VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.WindowsLibrary library = (VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.WindowsLibrary) Native.loadLibrary("kernel32", VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.WindowsLibrary.class, W32APIOptions.DEFAULT_OPTIONS);

                @Override
                public String getTemporaryFolder() {
                    DWORD length = new DWORD(260L);
                    char[] path = new char[length.intValue()];
                    if (Kernel32.INSTANCE.GetTempPath(length, path).intValue() == 0) {
                        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                    } else {
                        return Native.toString(path);
                    }
                }

                @Override
                public int pid() {
                    return Kernel32.INSTANCE.GetCurrentProcessId();
                }

                @Override
                public int userId() {
                    return 0;
                }

                @Override
                public boolean isExistingProcess(int processId) {
                    HANDLE handle = Kernel32.INSTANCE.OpenProcess(1024, false, processId);
                    if (handle == null) {
                        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                    } else {
                        IntByReference exists = new IntByReference();
                        if (!Kernel32.INSTANCE.GetExitCodeProcess(handle, exists)) {
                            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                        } else {
                            return exists.getValue() == 259;
                        }
                    }
                }

                @Override
                public int getOwnerIdOf(File file) {
                    return 0;
                }

                @Override
                public void setPermissions(File file, int permissions) {
                }

                @Override
                public void incrementSemaphore(File directory, String name, boolean global, int count) {
                    VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.AttachmentHandle handle = this.openSemaphore(directory, name, global);
                    try {
                        while (count-- > 0) {
                            if (!this.library.ReleaseSemaphore(handle.getHandle(), 1L, null)) {
                                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                            }
                        }
                    } finally {
                        handle.close();
                    }
                }

                @Override
                public void decrementSemaphore(File directory, String name, boolean global, int count) {
                    VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.AttachmentHandle handle = this.openSemaphore(directory, name, global);
                    try {
                        while (count-- > 0) {
                            int result = Kernel32.INSTANCE.WaitForSingleObject(handle.getHandle(), 0);
                            switch(result) {
                                case 0:
                                case 128:
                                    break;
                                case 258:
                                    return;
                                default:
                                    throw new Win32Exception(result);
                            }
                        }
                    } finally {
                        handle.close();
                    }
                }

                private VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.AttachmentHandle openSemaphore(File directory, String name, boolean global) {
                    SECURITY_DESCRIPTOR securityDescriptor = new SECURITY_DESCRIPTOR(65536);
                    VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.AttachmentHandle var11;
                    try {
                        if (!Advapi32.INSTANCE.InitializeSecurityDescriptor(securityDescriptor, 1)) {
                            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                        }
                        if (!Advapi32.INSTANCE.SetSecurityDescriptorDacl(securityDescriptor, true, null, true)) {
                            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                        }
                        VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.WindowsLibrary.SecurityAttributes securityAttributes = new VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.WindowsLibrary.SecurityAttributes();
                        try {
                            securityAttributes.length = new DWORD((long) securityAttributes.size());
                            securityAttributes.securityDescriptor = securityDescriptor.getPointer();
                            HANDLE mutex = this.library.CreateMutex(securityAttributes, false, "j9shsemcreationMutex");
                            if (mutex == null) {
                                int lastError = Kernel32.INSTANCE.GetLastError();
                                if (lastError != 183) {
                                    throw new Win32Exception(lastError);
                                }
                                mutex = this.library.OpenMutex(2031617, false, "j9shsemcreationMutex");
                                if (mutex == null) {
                                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                                }
                            }
                            int result = Kernel32.INSTANCE.WaitForSingleObject(mutex, 2000);
                            switch(result) {
                                case -1:
                                case 258:
                                    throw new Win32Exception(result);
                            }
                            try {
                                String target = (global ? "Global\\" : "") + (directory.getAbsolutePath() + '_' + name).replaceAll("[^a-zA-Z0-9_]", "") + "_semaphore";
                                HANDLE parent = this.library.OpenSemaphoreW(2031619, false, target);
                                if (parent != null) {
                                    HANDLE child = this.library.OpenSemaphoreW(2031619, false, target + "_set0");
                                    if (child == null) {
                                        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                                    }
                                    return new VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.AttachmentHandle(parent, child);
                                }
                                parent = this.library.CreateSemaphoreW(null, 0L, 2147483647L, target);
                                if (parent == null) {
                                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                                }
                                HANDLE child = this.library.CreateSemaphoreW(null, 0L, 2147483647L, target + "_set0");
                                if (child == null) {
                                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                                }
                                var11 = new VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.AttachmentHandle(parent, child);
                            } finally {
                                if (!this.library.ReleaseMutex(mutex)) {
                                    throw new Win32Exception(Native.getLastError());
                                }
                            }
                        } finally {
                            VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.WindowsLibrary.SecurityAttributes var28 = null;
                        }
                    } finally {
                        SECURITY_DESCRIPTOR var27 = null;
                    }
                    return var11;
                }

                protected static class AttachmentHandle implements Closeable {

                    private final HANDLE parent;

                    private final HANDLE child;

                    protected AttachmentHandle(HANDLE parent, HANDLE child) {
                        this.parent = parent;
                        this.child = child;
                    }

                    protected HANDLE getHandle() {
                        return this.child;
                    }

                    public void close() {
                        boolean closed;
                        try {
                            if (!Kernel32.INSTANCE.CloseHandle(this.child)) {
                                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                            }
                        } finally {
                            closed = Kernel32.INSTANCE.CloseHandle(this.parent);
                        }
                        if (var1_2 == false) {
                            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                        }
                    }
                }

                protected interface WindowsLibrary extends StdCallLibrary {

                    int SEMAPHORE_ALL_ACCESS = 2031619;

                    HANDLE OpenSemaphoreW(int var1, boolean var2, String var3);

                    HANDLE CreateSemaphoreW(SECURITY_ATTRIBUTES var1, long var2, long var4, String var6);

                    boolean ReleaseSemaphore(HANDLE var1, long var2, Long var4);

                    HANDLE CreateMutex(VirtualMachine.ForOpenJ9.Dispatcher.ForJnaWindowsEnvironment.WindowsLibrary.SecurityAttributes var1, boolean var2, String var3);

                    HANDLE OpenMutex(int var1, boolean var2, String var3);

                    boolean ReleaseMutex(HANDLE var1);

                    @SuppressFBWarnings(value = { "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD", "UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD" }, justification = "Field required by native implementation.")
                    public static class SecurityAttributes extends Structure {

                        public DWORD length;

                        public Pointer securityDescriptor;

                        public boolean inherit;

                        protected List<String> getFieldOrder() {
                            return Arrays.asList("length", "securityDescriptor", "inherit");
                        }
                    }
                }
            }
        }
    }

    public static enum Resolver implements PrivilegedAction<Class<? extends VirtualMachine>> {

        INSTANCE;

        public Class<? extends VirtualMachine> run() {
            try {
                Class.forName("com.sun.jna.Platform");
            } catch (ClassNotFoundException var2) {
                throw new IllegalStateException("Optional JNA dependency is not available", var2);
            }
            return System.getProperty("java.vm.name", "").toUpperCase(Locale.US).contains("J9") ? VirtualMachine.ForOpenJ9.class : VirtualMachine.ForHotSpot.class;
        }
    }
}