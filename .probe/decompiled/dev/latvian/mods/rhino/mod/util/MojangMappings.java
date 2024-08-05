package dev.latvian.mods.rhino.mod.util;

import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;

public class MojangMappings {

    private final String mcVersion;

    private final Map<String, MojangMappings.ClassDef> classes;

    private final Map<String, MojangMappings.ClassDef> classesMM;

    private final Map<MojangMappings.TypeDef, MojangMappings.TypeDef> allTypes;

    private final Map<MojangMappings.MethodDefSignature, MojangMappings.MethodDefSignature> methodSignatures;

    private final MojangMappings.ClassDef VOID = new MojangMappings.ClassDef(this, "void").descriptor("V");

    private final MojangMappings.ClassDef BOOLEAN = new MojangMappings.ClassDef(this, "boolean").descriptor("Z");

    private final MojangMappings.ClassDef CHAR = new MojangMappings.ClassDef(this, "char").descriptor("C");

    private final MojangMappings.ClassDef BYTE = new MojangMappings.ClassDef(this, "byte").descriptor("B");

    private final MojangMappings.ClassDef SHORT = new MojangMappings.ClassDef(this, "short").descriptor("S");

    private final MojangMappings.ClassDef INT = new MojangMappings.ClassDef(this, "int").descriptor("I");

    private final MojangMappings.ClassDef LONG = new MojangMappings.ClassDef(this, "long").descriptor("J");

    private final MojangMappings.ClassDef FLOAT = new MojangMappings.ClassDef(this, "float").descriptor("F");

    private final MojangMappings.ClassDef DOUBLE = new MojangMappings.ClassDef(this, "double").descriptor("D");

    private final MojangMappings.MethodDefSignature SIG_EMPTY = new MojangMappings.MethodDefSignature();

    private MojangMappings(String mc) {
        this.mcVersion = mc;
        this.classes = new HashMap();
        this.classesMM = new HashMap();
        this.allTypes = new HashMap();
        this.methodSignatures = new HashMap();
        for (MojangMappings.ClassDef c : new MojangMappings.ClassDef[] { this.VOID, this.BOOLEAN, this.CHAR, this.BYTE, this.SHORT, this.INT, this.LONG, this.FLOAT, this.DOUBLE }) {
            this.classes.put(c.rawName, c);
            this.allTypes.put(c.noArrayType, c.noArrayType);
        }
    }

    public MojangMappings.MethodDefSignature getSignature(MojangMappings.TypeDef[] types) {
        if (types.length == 0) {
            return this.SIG_EMPTY;
        } else if (types.length == 1) {
            return types[0].getSingleArgumentSignature();
        } else {
            MojangMappings.MethodDefSignature sig = new MojangMappings.MethodDefSignature(types);
            MojangMappings.MethodDefSignature cached = (MojangMappings.MethodDefSignature) this.methodSignatures.get(sig);
            if (cached != null) {
                return cached;
            } else {
                this.methodSignatures.put(sig, sig);
                return sig;
            }
        }
    }

    public MojangMappings.MethodDefSignature readSignatureFromDescriptor(String descriptor) throws Exception {
        if (descriptor.length() >= 2 && descriptor.charAt(0) == '(' && descriptor.charAt(1) == ')') {
            return this.SIG_EMPTY;
        } else {
            StringReader reader = new StringReader(descriptor);
            ArrayList<MojangMappings.TypeDef> types = new ArrayList(2);
            while (true) {
                int c = reader.read();
                if (c != 40) {
                    int array;
                    for (array = 0; c == 91; c = reader.read()) {
                        array++;
                    }
                    if (c == -1 || c == 41) {
                        if (types.isEmpty()) {
                            return this.SIG_EMPTY;
                        } else {
                            return types.size() == 1 ? ((MojangMappings.TypeDef) types.get(0)).getSingleArgumentSignature() : this.getSignature((MojangMappings.TypeDef[]) types.toArray(new MojangMappings.TypeDef[0]));
                        }
                    }
                    if (c == 76) {
                        StringBuilder sb = new StringBuilder();
                        while (true) {
                            c = reader.read();
                            if (c == -1) {
                                throw new RemapperException("Invalid descriptor: " + descriptor);
                            }
                            if (c == 59) {
                                types.add(this.getType(sb.toString()).parent.array(array));
                                break;
                            }
                            if (c == 47) {
                                sb.append('.');
                            } else {
                                sb.append((char) c);
                            }
                        }
                    } else if (c == 90) {
                        types.add(this.BOOLEAN.array(array));
                    } else if (c == 67) {
                        types.add(this.CHAR.array(array));
                    } else if (c == 66) {
                        types.add(this.BYTE.array(array));
                    } else if (c == 83) {
                        types.add(this.SHORT.array(array));
                    } else if (c == 73) {
                        types.add(this.INT.array(array));
                    } else if (c == 74) {
                        types.add(this.LONG.array(array));
                    } else if (c == 70) {
                        types.add(this.FLOAT.array(array));
                    } else if (c == 68) {
                        types.add(this.DOUBLE.array(array));
                    } else {
                        if (c != 86) {
                            throw new RemapperException("Invalid descriptor: " + descriptor);
                        }
                        types.add(this.VOID.array(array));
                    }
                }
            }
        }
    }

    @Nullable
    public MojangMappings.ClassDef getClass(String name) {
        MojangMappings.ClassDef mmc = (MojangMappings.ClassDef) this.classesMM.get(name);
        return mmc != null ? mmc : (MojangMappings.ClassDef) this.classes.get(name);
    }

    public Collection<MojangMappings.ClassDef> getMMClasses() {
        return this.classesMM.values();
    }

    public MojangMappings.TypeDef getType(String string) {
        int array = 0;
        while (string.endsWith("[]")) {
            array++;
            string = string.substring(0, string.length() - 2);
        }
        while (string.startsWith("[")) {
            array++;
            string = string.substring(1);
        }
        MojangMappings.ClassDef c = this.getClass(string);
        if (c != null) {
            return c.array(array);
        } else {
            c = new MojangMappings.ClassDef(this, string);
            this.classes.put(string, c);
            this.allTypes.put(c.noArrayType, c.noArrayType);
            return c.array(array);
        }
    }

    private static boolean invalidLine(String s) {
        return s.isBlank() || s.startsWith("#") || s.endsWith("init>") || s.contains(".package-info ");
    }

    private void parse0(List<String> lines) {
        lines.removeIf(MojangMappings::invalidLine);
        for (String line : lines) {
            if (line.charAt(line.length() - 1) == ':') {
                String[] s = line.split(" -> ", 2);
                MojangMappings.ClassDef c = new MojangMappings.ClassDef(this, s[1].substring(0, s[1].length() - 1), s[0], new HashMap(0), new HashSet(0));
                c.mapped = true;
                this.classes.put(c.rawName, c);
                this.classesMM.put(c.mmName, c);
                this.allTypes.put(c.noArrayType, c.noArrayType);
            }
        }
        MojangMappings.ClassDef currentClassDef = null;
        for (String linex : lines) {
            if (linex.charAt(0) == ' ') {
                if (currentClassDef == null) {
                    throw new RemapperException("Field or method without class! " + linex);
                }
                linex = linex.substring(Math.max(4, linex.lastIndexOf(58) + 1));
                int typeSpace = linex.indexOf(32);
                MojangMappings.TypeDef type = this.getType(linex.substring(0, typeSpace));
                linex = linex.substring(typeSpace + 1);
                String rawName = linex.substring(linex.lastIndexOf(32) + 1);
                linex = linex.substring(0, linex.indexOf(32));
                String name;
                MojangMappings.MethodDefSignature sig;
                if (linex.charAt(linex.length() - 1) != ')') {
                    name = linex;
                    sig = null;
                } else {
                    int lp = linex.indexOf(40);
                    name = linex.substring(0, lp);
                    linex = linex.substring(lp + 1, linex.length() - 1);
                    if (linex.isEmpty()) {
                        sig = this.SIG_EMPTY;
                    } else {
                        String[] sclasses = linex.split(",");
                        MojangMappings.TypeDef[] types = new MojangMappings.TypeDef[sclasses.length];
                        for (int i = 0; i < sclasses.length; i++) {
                            types[i] = this.getType(sclasses[i]);
                        }
                        sig = this.getSignature(types);
                    }
                }
                MojangMappings.NamedSignature rawNameSig = new MojangMappings.NamedSignature(rawName, sig);
                if (!name.startsWith("lambda$") && !name.startsWith("access$") && !linex.startsWith("val$") && !linex.startsWith("this$")) {
                    MojangMappings.MemberDef m = new MojangMappings.MemberDef(currentClassDef, rawNameSig, name, type, new MutableObject(""));
                    currentClassDef.members.put(rawNameSig, m);
                } else {
                    currentClassDef.ignoredMembers.add(rawNameSig);
                }
            } else if (linex.charAt(linex.length() - 1) == ':') {
                currentClassDef = (MojangMappings.ClassDef) this.classes.get(linex.substring(linex.lastIndexOf(32) + 1, linex.length() - 1));
            }
        }
    }

    public void cleanup() {
        this.classes.values().removeIf(MojangMappings.ClassDef::cleanup);
    }

    public void updateOccurrences() {
        for (MojangMappings.TypeDef c : this.allTypes.values()) {
            c.occurrences = 0;
        }
        for (MojangMappings.MethodDefSignature m : this.methodSignatures.values()) {
            m.occurrences = 0;
        }
        for (MojangMappings.ClassDef c : this.classes.values()) {
            for (MojangMappings.MemberDef m : c.members.values()) {
                if (m.rawName.signature != null && m.rawName.signature.types.length > 0) {
                    m.rawName.signature.occurrences++;
                    for (MojangMappings.TypeDef t : m.rawName.signature.types) {
                        t.occurrences++;
                    }
                }
            }
        }
    }

    private static void writeVarInt(OutputStream stream, int value) throws Exception {
        RemappingHelper.writeVarInt(stream, value);
    }

    private static void writeUtf(OutputStream stream, String value) throws Exception {
        RemappingHelper.writeUtf(stream, value);
    }

    public void write(OutputStream stream) throws Exception {
        this.cleanup();
        this.updateOccurrences();
        ArrayList<MojangMappings.TypeDef> typeDefList = new ArrayList(this.allTypes.values());
        typeDefList.sort(MojangMappings.TypeDef::compareTo);
        ArrayList<MojangMappings.TypeDef> unmappedTypes = new ArrayList();
        ArrayList<MojangMappings.TypeDef> mappedTypes = new ArrayList();
        ArrayList<MojangMappings.TypeDef> arrayTypes = new ArrayList();
        for (int i = 0; i < typeDefList.size(); i++) {
            MojangMappings.TypeDef c = (MojangMappings.TypeDef) typeDefList.get(i);
            c.index = i;
            if (c.array > 0) {
                arrayTypes.add(c);
            } else if (c.parent.mapped) {
                mappedTypes.add(c);
            } else {
                unmappedTypes.add(c);
            }
        }
        ArrayList<MojangMappings.MethodDefSignature> sigList = new ArrayList(this.methodSignatures.values());
        sigList.sort(MojangMappings.MethodDefSignature::compareTo);
        int ix = 0;
        while (ix < sigList.size()) {
            ((MojangMappings.MethodDefSignature) sigList.get(ix)).index = ix++;
        }
        RemappingHelper.LOGGER.info("Total Types: " + typeDefList.size());
        RemappingHelper.LOGGER.info("Total Signatures: " + sigList.size());
        RemappingHelper.LOGGER.info("Unmapped Types: " + unmappedTypes.size());
        RemappingHelper.LOGGER.info("Mapped Types: " + mappedTypes.size());
        RemappingHelper.LOGGER.info("Array Types: " + arrayTypes.size());
        stream.write(0);
        stream.write(1);
        writeUtf(stream, this.mcVersion);
        writeVarInt(stream, unmappedTypes.size());
        writeVarInt(stream, mappedTypes.size());
        writeVarInt(stream, arrayTypes.size());
        for (MojangMappings.TypeDef c : unmappedTypes) {
            writeVarInt(stream, c.index);
            writeUtf(stream, c.parent.rawName);
        }
        for (MojangMappings.TypeDef c : mappedTypes) {
            writeVarInt(stream, c.index);
            writeUtf(stream, (String) c.parent.unmappedName.getValue());
            writeUtf(stream, c.parent.mmName);
        }
        for (MojangMappings.TypeDef c : arrayTypes) {
            writeVarInt(stream, c.index);
            writeVarInt(stream, c.parent.noArrayType.index);
            writeVarInt(stream, c.array);
        }
        writeVarInt(stream, sigList.size());
        for (MojangMappings.MethodDefSignature s : sigList) {
            writeVarInt(stream, s.types.length);
            for (MojangMappings.TypeDef c : s.types) {
                writeVarInt(stream, c.index);
            }
        }
        for (MojangMappings.TypeDef c : mappedTypes) {
            ArrayList<MojangMappings.MemberDef> fields = new ArrayList();
            ArrayList<MojangMappings.MemberDef> arg0methods = new ArrayList();
            ArrayList<MojangMappings.MemberDef> argNmethods = new ArrayList();
            for (MojangMappings.MemberDef f : c.parent.members.values()) {
                if (f.rawName.signature == null) {
                    fields.add(f);
                } else if (f.rawName.signature.types.length == 0) {
                    arg0methods.add(f);
                } else {
                    argNmethods.add(f);
                }
            }
            writeVarInt(stream, fields.size());
            writeVarInt(stream, arg0methods.size());
            writeVarInt(stream, argNmethods.size());
            for (MojangMappings.MemberDef m : fields) {
                writeUtf(stream, (String) m.unmappedName.getValue());
                writeUtf(stream, m.mmName);
            }
            for (MojangMappings.MemberDef m : arg0methods) {
                writeUtf(stream, (String) m.unmappedName.getValue());
                writeUtf(stream, m.mmName);
            }
            for (MojangMappings.MemberDef m : argNmethods) {
                writeUtf(stream, (String) m.unmappedName.getValue());
                writeUtf(stream, m.mmName);
                writeVarInt(stream, m.rawName.signature.index);
            }
        }
    }

    public static MojangMappings parse(String mcVersion, List<String> lines) throws Exception {
        MojangMappings mappings = new MojangMappings(mcVersion);
        mappings.parse0(lines);
        return mappings;
    }

    public static final class ClassDef {

        public final MojangMappings mappings;

        public final String rawName;

        public final String mmName;

        public final String displayName;

        public final Map<MojangMappings.NamedSignature, MojangMappings.MemberDef> members;

        public final Set<MojangMappings.NamedSignature> ignoredMembers;

        private final MutableObject<String> unmappedName;

        public boolean mapped;

        public MojangMappings.TypeDef noArrayType;

        public String rawDescriptor;

        public ClassDef(MojangMappings mappings, String rawName, String mmName, Map<MojangMappings.NamedSignature, MojangMappings.MemberDef> members, Set<MojangMappings.NamedSignature> ignoredMembers) {
            this.mappings = mappings;
            this.rawName = rawName;
            this.mmName = mmName;
            String dn = mmName.isEmpty() ? rawName : mmName;
            int dni = dn.lastIndexOf(46);
            this.displayName = dni == -1 ? dn : dn.substring(dni + 1);
            this.members = members;
            this.ignoredMembers = ignoredMembers;
            this.unmappedName = new MutableObject("");
            this.mapped = false;
            this.noArrayType = new MojangMappings.TypeDef(this, 0);
        }

        public ClassDef(MojangMappings mappings, String name) {
            this(mappings, name, "", Map.of(), Set.of());
        }

        public MojangMappings.ClassDef descriptor(String s) {
            this.rawDescriptor = s;
            return this;
        }

        private MojangMappings.TypeDef array(int a) {
            if (a == 0) {
                return this.noArrayType;
            } else {
                MojangMappings.TypeDef t = new MojangMappings.TypeDef(this, a);
                MojangMappings.TypeDef t1 = (MojangMappings.TypeDef) this.mappings.allTypes.get(t);
                if (t1 == null) {
                    this.mappings.allTypes.put(t, t);
                    return t;
                } else {
                    return t1;
                }
            }
        }

        public int hashCode() {
            return this.rawName.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else {
                if (obj instanceof MojangMappings.ClassDef other && this.rawName.equals(other.rawName)) {
                    return true;
                }
                return false;
            }
        }

        public String toString() {
            return this.displayName;
        }

        public MutableObject<String> unmappedName() {
            return this.unmappedName;
        }

        public String getRawDescriptor() {
            if (this.rawDescriptor == null) {
                this.rawDescriptor = "L" + this.rawName.replace('.', '/') + ";";
            }
            return this.rawDescriptor;
        }

        public boolean cleanup() {
            if (!this.mapped) {
                return false;
            } else {
                this.members.values().removeIf(MojangMappings.MemberDef::cleanup);
                return this.members.isEmpty() && ((String) this.unmappedName.getValue()).isEmpty();
            }
        }
    }

    public static record MemberDef(MojangMappings.ClassDef parent, MojangMappings.NamedSignature rawName, String mmName, MojangMappings.TypeDef type, MutableObject<String> unmappedName) {

        public boolean cleanup() {
            return ((String) this.unmappedName.getValue()).isEmpty();
        }
    }

    public static class MethodDefSignature {

        public final MojangMappings.TypeDef[] types;

        public int occurrences;

        public int index;

        public MethodDefSignature(MojangMappings.TypeDef... types) {
            this.types = types;
            this.occurrences = 0;
            this.index = -1;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else {
                if (o instanceof MojangMappings.MethodDefSignature sig && Arrays.equals(this.types, sig.types)) {
                    return true;
                }
                return false;
            }
        }

        public int hashCode() {
            return Arrays.hashCode(this.types);
        }

        public String toString() {
            if (this.types.length == 0) {
                return "";
            } else if (this.types.length == 1) {
                return this.types[0].toString();
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < this.types.length; i++) {
                    if (i > 0) {
                        sb.append(',');
                    }
                    sb.append(this.types[i]);
                }
                return sb.toString();
            }
        }

        public int compareTo(MojangMappings.MethodDefSignature other) {
            return Integer.compare(other.occurrences, this.occurrences);
        }
    }

    public static record NamedSignature(String name, @Nullable MojangMappings.MethodDefSignature signature) {

        public String toString() {
            return this.signature == null ? this.name : this.name + "(" + this.signature + ")";
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else {
                if (obj instanceof MojangMappings.NamedSignature other && this.name.equals(other.name) && Objects.equals(this.signature, other.signature)) {
                    return true;
                }
                return false;
            }
        }

        public int hashCode() {
            return this.name.hashCode() * 31 + (this.signature == null ? 0 : this.signature.hashCode());
        }
    }

    public static final class TypeDef {

        public final MojangMappings.ClassDef parent;

        public final int array;

        public int occurrences;

        private MojangMappings.MethodDefSignature singleArgumentSignature;

        private String rawDescriptor;

        public int index;

        public TypeDef(MojangMappings.ClassDef parent, int array) {
            this.parent = parent;
            this.array = array;
            this.occurrences = 0;
            this.singleArgumentSignature = null;
            this.rawDescriptor = null;
            this.index = -1;
        }

        public String getRawDescriptor() {
            if (this.rawDescriptor == null) {
                if (this.array > 0) {
                    this.rawDescriptor = "[".repeat(this.array) + this.parent.getRawDescriptor();
                } else {
                    this.rawDescriptor = this.parent.getRawDescriptor();
                }
            }
            return this.rawDescriptor;
        }

        public MojangMappings.MethodDefSignature getSingleArgumentSignature() {
            if (this.singleArgumentSignature == null) {
                this.singleArgumentSignature = new MojangMappings.MethodDefSignature(this);
                this.parent.mappings.methodSignatures.put(this.singleArgumentSignature, this.singleArgumentSignature);
            }
            return this.singleArgumentSignature;
        }

        public int compareTo(MojangMappings.TypeDef other) {
            return Integer.compare(other.occurrences, this.occurrences);
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else {
                if (obj instanceof MojangMappings.TypeDef t && this.parent.equals(t.parent) && this.array == t.array) {
                    return true;
                }
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.parent, this.array });
        }

        public String toString() {
            return this.getRawDescriptor();
        }
    }
}