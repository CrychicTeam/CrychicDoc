package moe.wolfgirl.probejs.lang.decompiler.remapper;

import dev.latvian.mods.rhino.mod.util.RemapperException;
import dev.latvian.mods.rhino.mod.util.RemappingHelper;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import moe.wolfgirl.probejs.ProbeJS;

public class RemappedClass {

    public final String realName;

    public final String remappedName;

    public final boolean remapped;

    public Map<String, String> fields;

    public Map<String, String> emptyMethods;

    public Map<String, String> methods;

    private String descriptorString;

    private RemappedClass(String realName, String remappedName, boolean remapped) {
        this.realName = realName;
        this.remappedName = remappedName;
        this.remapped = remapped;
        this.fields = null;
        this.emptyMethods = null;
        this.methods = null;
    }

    private static int readVarInt(InputStream stream) throws Exception {
        return RemappingHelper.readVarInt(stream);
    }

    private static String readUtf(InputStream stream) throws Exception {
        return RemappingHelper.readUtf(stream);
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

    public static Map<String, RemappedClass> loadFrom(InputStream stream) throws Exception {
        Map<String, RemappedClass> classMap = new HashMap();
        if (stream.read() != 0) {
            throw new RemapperException("Invalid Minecraft Remapper file!");
        } else {
            int version = stream.read();
            if (version > 1) {
                throw new RemapperException("Invalid Minecraft Remapper file version!");
            } else {
                ProbeJS.LOGGER.info("Loading mappings for " + readUtf(stream));
                int unmappedTypes = readVarInt(stream);
                RemappedType[] mappedTypes = new RemappedType[readVarInt(stream)];
                int arrayTypes = readVarInt(stream);
                RemappedType[] types = new RemappedType[unmappedTypes + mappedTypes.length + arrayTypes];
                for (int i = 0; i < unmappedTypes; i++) {
                    int index = readVarInt(stream);
                    String name = readUtf(stream);
                    types[index] = new RemappedType(new RemappedClass(name, name, false), 0);
                }
                for (int i = 0; i < mappedTypes.length; i++) {
                    int index = readVarInt(stream);
                    String realName = readUtf(stream);
                    String remappedName = readUtf(stream);
                    types[index] = new RemappedType(new RemappedClass(realName.isEmpty() ? remappedName : realName, remappedName, true), 0);
                    mappedTypes[i] = types[index];
                    classMap.put(types[index].parent.realName, types[index].parent);
                }
                for (int i = 0; i < arrayTypes; i++) {
                    int index = readVarInt(stream);
                    int type = readVarInt(stream);
                    int array = readVarInt(stream);
                    if (type < 0 || type >= types.length || types[type] == null) {
                        throw new RemapperException("Invalid array index: " + type + "!");
                    }
                    types[index] = new RemappedType(types[type].parent, array);
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
                for (RemappedType c : mappedTypes) {
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
                        }
                    }
                }
                return classMap;
            }
        }
    }
}