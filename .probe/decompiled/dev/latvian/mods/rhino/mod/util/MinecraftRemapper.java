package dev.latvian.mods.rhino.mod.util;

import dev.latvian.mods.rhino.util.Remapper;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public class MinecraftRemapper implements Remapper {

    private final Map<String, MinecraftRemapper.RemappedClass> classMap;

    private final Map<String, String> unmapClassMap;

    private static int readVarInt(InputStream stream) throws Exception {
        return RemappingHelper.readVarInt(stream);
    }

    private static String readUtf(InputStream stream) throws Exception {
        return RemappingHelper.readUtf(stream);
    }

    public static MinecraftRemapper load(InputStream stream, boolean debug) throws Exception {
        MinecraftRemapper m = new MinecraftRemapper(new HashMap(), new HashMap());
        m.load0(stream, debug);
        return m;
    }

    private void load0(InputStream stream, boolean debug) throws Exception {
        if (stream.read() != 0) {
            throw new RemapperException("Invalid Minecraft Remapper file!");
        } else {
            int version = stream.read();
            if (version > 1) {
                throw new RemapperException("Invalid Minecraft Remapper file version!");
            } else {
                RemappingHelper.LOGGER.info("Loading mappings for " + readUtf(stream));
                int unmappedTypes = readVarInt(stream);
                MinecraftRemapper.RemappedType[] mappedTypes = new MinecraftRemapper.RemappedType[readVarInt(stream)];
                int arrayTypes = readVarInt(stream);
                MinecraftRemapper.RemappedType[] types = new MinecraftRemapper.RemappedType[unmappedTypes + mappedTypes.length + arrayTypes];
                if (debug) {
                    RemappingHelper.LOGGER.info("Unmapped Types: " + unmappedTypes);
                    RemappingHelper.LOGGER.info("Mapped Types: " + mappedTypes.length);
                    RemappingHelper.LOGGER.info("Array Types: " + arrayTypes);
                    RemappingHelper.LOGGER.info("Total Types: " + types.length);
                }
                for (int i = 0; i < unmappedTypes; i++) {
                    int index = readVarInt(stream);
                    String name = readUtf(stream);
                    types[index] = new MinecraftRemapper.RemappedType(new MinecraftRemapper.RemappedClass(name, name, false), 0);
                }
                for (int i = 0; i < mappedTypes.length; i++) {
                    int index = readVarInt(stream);
                    String realName = readUtf(stream);
                    String remappedName = readUtf(stream);
                    types[index] = new MinecraftRemapper.RemappedType(new MinecraftRemapper.RemappedClass(realName.isEmpty() ? remappedName : realName, remappedName, true), 0);
                    mappedTypes[i] = types[index];
                    this.classMap.put(types[index].parent.realName, types[index].parent);
                }
                for (int i = 0; i < arrayTypes; i++) {
                    int index = readVarInt(stream);
                    int type = readVarInt(stream);
                    int array = readVarInt(stream);
                    if (type < 0 || type >= types.length || types[type] == null) {
                        throw new RemapperException("Invalid array index: " + type + "!");
                    }
                    types[index] = new MinecraftRemapper.RemappedType(types[type].parent, array);
                }
                String[] sig = new String[readVarInt(stream)];
                for (int i = 0; i < sig.length; i++) {
                    int params = readVarInt(stream);
                    StringBuilder sb = new StringBuilder();
                    sb.append('(');
                    for (int j = 0; j < params; j++) {
                        sb.append(types[readVarInt(stream)].descriptorString());
                    }
                    sig[i] = sb.toString();
                }
                for (MinecraftRemapper.RemappedType c : mappedTypes) {
                    if (debug) {
                        RemappingHelper.LOGGER.info(String.format("- %s -> %s", c.parent.realName, c.parent.remappedName));
                    }
                    int fields = readVarInt(stream);
                    int arg0 = readVarInt(stream);
                    int argN = readVarInt(stream);
                    for (int i = 0; i < fields; i++) {
                        String unmappedName = readUtf(stream);
                        String mmName = readUtf(stream);
                        if (!unmappedName.isEmpty() && !mmName.isEmpty() && !unmappedName.equals(mmName)) {
                            if (c.parent.fields == null) {
                                c.parent.fields = new HashMap(arg0 + argN);
                            }
                            c.parent.fields.put(unmappedName, mmName);
                            if (debug) {
                                RemappingHelper.LOGGER.info(String.format("  %s -> %s", unmappedName, mmName));
                            }
                        }
                    }
                    for (int ix = 0; ix < arg0; ix++) {
                        String realName = readUtf(stream);
                        String remappedName = readUtf(stream);
                        if (!realName.isEmpty() && !remappedName.isEmpty() && !realName.equals(remappedName)) {
                            if (c.parent.emptyMethods == null) {
                                c.parent.emptyMethods = new HashMap(arg0);
                            }
                            c.parent.emptyMethods.put(realName, remappedName);
                            if (debug) {
                                RemappingHelper.LOGGER.info(String.format("  %s() -> %s", realName, remappedName));
                            }
                        }
                    }
                    for (int ixx = 0; ixx < argN; ixx++) {
                        String realName = readUtf(stream);
                        String remappedName = readUtf(stream);
                        if (!realName.isEmpty() && !remappedName.isEmpty() && !realName.equals(remappedName)) {
                            if (c.parent.methods == null) {
                                c.parent.methods = new HashMap(argN);
                            }
                            int index = readVarInt(stream);
                            String key = realName + sig[index];
                            c.parent.methods.put(key, remappedName);
                            if (debug) {
                                RemappingHelper.LOGGER.info(String.format("  %s -> %s", key, remappedName));
                            }
                        }
                    }
                }
            }
        }
    }

    MinecraftRemapper(Map<String, MinecraftRemapper.RemappedClass> m1, Map<String, String> m2) {
        this.classMap = m1;
        this.unmapClassMap = m2;
    }

    @Override
    public String getMappedClass(Class<?> from) {
        MinecraftRemapper.RemappedClass c = (MinecraftRemapper.RemappedClass) this.classMap.get(from.getName());
        return c == null ? "" : c.remappedName;
    }

    @Override
    public String getUnmappedClass(String mmName) {
        if (this.classMap.isEmpty()) {
            return "";
        } else {
            String s = (String) this.unmapClassMap.get(mmName);
            if (s == null) {
                s = "";
                for (MinecraftRemapper.RemappedClass c : this.classMap.values()) {
                    if (c.remappedName.equals(mmName)) {
                        s = c.realName;
                    }
                }
                this.unmapClassMap.put(mmName, s);
            }
            return s;
        }
    }

    @Override
    public String getMappedField(Class<?> from, Field field) {
        if (from != null && from != Object.class && !from.getPackageName().startsWith("java.")) {
            MinecraftRemapper.RemappedClass c = (MinecraftRemapper.RemappedClass) this.classMap.get(from.getName());
            return c != null && c.fields != null ? (String) c.fields.getOrDefault(field.getName(), "") : "";
        } else {
            return "";
        }
    }

    @Override
    public String getMappedMethod(Class<?> from, Method method) {
        if (from != null && from != Object.class && !from.getPackageName().startsWith("java.")) {
            MinecraftRemapper.RemappedClass c = (MinecraftRemapper.RemappedClass) this.classMap.get(from.getName());
            if (c == null) {
                return "";
            } else if (method.getParameterCount() == 0) {
                return c.emptyMethods == null ? "" : (String) c.emptyMethods.getOrDefault(method.getName(), "");
            } else if (c.methods == null) {
                return "";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(method.getName());
                sb.append('(');
                for (Class<?> t : method.getParameterTypes()) {
                    sb.append(t.descriptorString());
                }
                return (String) c.methods.getOrDefault(sb.toString(), "");
            }
        } else {
            return "";
        }
    }

    private static final class RemappedClass {

        private final String realName;

        private final String remappedName;

        private final boolean remapped;

        private Map<String, String> fields;

        private Map<String, String> emptyMethods;

        private Map<String, String> methods;

        private String descriptorString;

        private RemappedClass(String realName, String remappedName, boolean remapped) {
            this.realName = realName;
            this.remappedName = remappedName;
            this.remapped = remapped;
            this.fields = null;
            this.emptyMethods = null;
            this.methods = null;
        }

        public String toString() {
            return this.remapped ? this.remappedName + "[" + this.realName + "]" : this.realName;
        }

        public String descriptorString() {
            if (this.descriptorString == null) {
                String var1 = this.realName;
                this.descriptorString = switch(var1) {
                    case "boolean" ->
                        "Z";
                    case "byte" ->
                        "B";
                    case "char" ->
                        "C";
                    case "short" ->
                        "S";
                    case "int" ->
                        "I";
                    case "long" ->
                        "J";
                    case "float" ->
                        "F";
                    case "double" ->
                        "D";
                    case "void" ->
                        "V";
                    default ->
                        "L" + this.realName.replace('.', '/') + ";";
                };
            }
            return this.descriptorString;
        }
    }

    private static final class RemappedType {

        private final MinecraftRemapper.RemappedClass parent;

        private final int array;

        private Optional<Class<?>> realClass;

        private String descriptorString;

        private RemappedType(MinecraftRemapper.RemappedClass parent, int array) {
            this.parent = parent;
            this.array = array;
            this.realClass = null;
        }

        public String toString() {
            return this.array == 0 ? this.parent.toString() : this.parent.toString() + "[]".repeat(this.array);
        }

        public boolean isRemapped() {
            return this.array == 0 && this.parent.remapped;
        }

        @Nullable
        private Class<?> getRealClass(boolean debug) {
            if (this.realClass == null) {
                Optional<Class<?>> r = RemappingHelper.getClass(this.parent.realName);
                if (r.isPresent()) {
                    if (this.array > 0) {
                        this.realClass = Optional.of(Array.newInstance((Class) r.get(), this.array).getClass());
                    } else {
                        this.realClass = r;
                    }
                } else {
                    this.realClass = Optional.empty();
                    if (debug) {
                        RemappingHelper.LOGGER.error("Class " + this.parent.realName + " / " + this.parent.remappedName + " not found!");
                    }
                }
            }
            return (Class<?>) this.realClass.orElse(null);
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else {
                if (obj instanceof MinecraftRemapper.RemappedType type && type.parent == this.parent && type.array == this.array) {
                    return true;
                }
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.parent, this.array });
        }

        public String descriptorString() {
            if (this.descriptorString == null) {
                if (this.array > 0) {
                    this.descriptorString = "[".repeat(this.array) + this.parent.descriptorString();
                } else {
                    this.descriptorString = this.parent.descriptorString();
                }
            }
            return this.descriptorString;
        }
    }
}