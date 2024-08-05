package dev.latvian.mods.rhino.classfile;

final class TypeInfo {

    static final int TOP = 0;

    static final int INTEGER = 1;

    static final int FLOAT = 2;

    static final int DOUBLE = 3;

    static final int LONG = 4;

    static final int NULL = 5;

    static final int UNINITIALIZED_THIS = 6;

    static final int OBJECT_TAG = 7;

    static final int UNINITIALIZED_VAR_TAG = 8;

    static int OBJECT(int constantPoolIndex) {
        return (constantPoolIndex & 65535) << 8 | 7;
    }

    static int OBJECT(String type, ConstantPool pool) {
        return OBJECT(pool.addClass(type));
    }

    static int UNINITIALIZED_VARIABLE(int bytecodeOffset) {
        return (bytecodeOffset & 65535) << 8 | 8;
    }

    static int getTag(int typeInfo) {
        return typeInfo & 0xFF;
    }

    static int getPayload(int typeInfo) {
        return typeInfo >>> 8;
    }

    static String getPayloadAsType(int typeInfo, ConstantPool pool) {
        if (getTag(typeInfo) == 7) {
            return (String) pool.getConstantData(getPayload(typeInfo));
        } else {
            throw new IllegalArgumentException("expecting object type");
        }
    }

    static int fromType(String type, ConstantPool pool) {
        if (type.length() == 1) {
            return switch(type.charAt(0)) {
                case 'B', 'C', 'I', 'S', 'Z' ->
                    1;
                case 'D' ->
                    3;
                default ->
                    throw new IllegalArgumentException("bad type");
                case 'F' ->
                    2;
                case 'J' ->
                    4;
            };
        } else {
            return OBJECT(type, pool);
        }
    }

    static boolean isTwoWords(int type) {
        return type == 3 || type == 4;
    }

    static int merge(int current, int incoming, ConstantPool pool) {
        int currentTag = getTag(current);
        int incomingTag = getTag(incoming);
        boolean currentIsObject = currentTag == 7;
        boolean incomingIsObject = incomingTag == 7;
        if (current != incoming && (!currentIsObject || incoming != 5)) {
            if (currentTag == 0 || incomingTag == 0) {
                return 0;
            } else if (current == 5 && incomingIsObject) {
                return incoming;
            } else {
                if (currentIsObject && incomingIsObject) {
                    String currentName = getPayloadAsType(current, pool);
                    String incomingName = getPayloadAsType(incoming, pool);
                    String currentlyGeneratedName = (String) pool.getConstantData(2);
                    String currentlyGeneratedSuperName = (String) pool.getConstantData(4);
                    if (currentName.equals(currentlyGeneratedName)) {
                        currentName = currentlyGeneratedSuperName;
                    }
                    if (incomingName.equals(currentlyGeneratedName)) {
                        incomingName = currentlyGeneratedSuperName;
                    }
                    Class<?> currentClass = getClassFromInternalName(currentName);
                    Class<?> incomingClass = getClassFromInternalName(incomingName);
                    if (currentClass.isAssignableFrom(incomingClass)) {
                        return current;
                    }
                    if (incomingClass.isAssignableFrom(currentClass)) {
                        return incoming;
                    }
                    if (incomingClass.isInterface() || currentClass.isInterface()) {
                        return OBJECT("java/lang/Object", pool);
                    }
                    for (Class<?> commonClass = incomingClass.getSuperclass(); commonClass != null; commonClass = commonClass.getSuperclass()) {
                        if (commonClass.isAssignableFrom(currentClass)) {
                            String name = commonClass.getName();
                            name = ClassFileWriter.getSlashedForm(name);
                            return OBJECT(name, pool);
                        }
                    }
                }
                throw new IllegalArgumentException("bad merge attempt between " + toString(current, pool) + " and " + toString(incoming, pool));
            }
        } else {
            return current;
        }
    }

    static String toString(int type, ConstantPool pool) {
        int tag = getTag(type);
        switch(tag) {
            case 0:
                return "top";
            case 1:
                return "int";
            case 2:
                return "float";
            case 3:
                return "double";
            case 4:
                return "long";
            case 5:
                return "null";
            case 6:
                return "uninitialized_this";
            default:
                if (tag == 7) {
                    return getPayloadAsType(type, pool);
                } else if (tag == 8) {
                    return "uninitialized";
                } else {
                    throw new IllegalArgumentException("bad type");
                }
        }
    }

    private static Class<?> getClassFromInternalName(String internalName) {
        try {
            return Class.forName(internalName.replace('/', '.'));
        } catch (ClassNotFoundException var2) {
            throw new RuntimeException(var2);
        }
    }

    private static String toString(int[] types, int typesTop, ConstantPool pool) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < typesTop; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(toString(types[i], pool));
        }
        sb.append("]");
        return sb.toString();
    }

    static void print(int[] locals, int[] stack, ConstantPool pool) {
        print(locals, locals.length, stack, stack.length, pool);
    }

    static void print(int[] locals, int localsTop, int[] stack, int stackTop, ConstantPool pool) {
        System.out.print("locals: ");
        System.out.println(toString(locals, localsTop, pool));
        System.out.print("stack: ");
        System.out.println(toString(stack, stackTop, pool));
        System.out.println();
    }

    private TypeInfo() {
    }
}