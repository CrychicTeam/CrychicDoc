package me.lucko.spark.lib.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class Type {

    public static final int VOID = 0;

    public static final int BOOLEAN = 1;

    public static final int CHAR = 2;

    public static final int BYTE = 3;

    public static final int SHORT = 4;

    public static final int INT = 5;

    public static final int FLOAT = 6;

    public static final int LONG = 7;

    public static final int DOUBLE = 8;

    public static final int ARRAY = 9;

    public static final int OBJECT = 10;

    public static final int METHOD = 11;

    private static final int INTERNAL = 12;

    private static final String PRIMITIVE_DESCRIPTORS = "VZCBSIFJD";

    public static final Type VOID_TYPE = new Type(0, "VZCBSIFJD", 0, 1);

    public static final Type BOOLEAN_TYPE = new Type(1, "VZCBSIFJD", 1, 2);

    public static final Type CHAR_TYPE = new Type(2, "VZCBSIFJD", 2, 3);

    public static final Type BYTE_TYPE = new Type(3, "VZCBSIFJD", 3, 4);

    public static final Type SHORT_TYPE = new Type(4, "VZCBSIFJD", 4, 5);

    public static final Type INT_TYPE = new Type(5, "VZCBSIFJD", 5, 6);

    public static final Type FLOAT_TYPE = new Type(6, "VZCBSIFJD", 6, 7);

    public static final Type LONG_TYPE = new Type(7, "VZCBSIFJD", 7, 8);

    public static final Type DOUBLE_TYPE = new Type(8, "VZCBSIFJD", 8, 9);

    private final int sort;

    private final String valueBuffer;

    private final int valueBegin;

    private final int valueEnd;

    private Type(int sort, String valueBuffer, int valueBegin, int valueEnd) {
        this.sort = sort;
        this.valueBuffer = valueBuffer;
        this.valueBegin = valueBegin;
        this.valueEnd = valueEnd;
    }

    public static Type getType(String typeDescriptor) {
        return getTypeInternal(typeDescriptor, 0, typeDescriptor.length());
    }

    public static Type getType(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (clazz == int.class) {
                return INT_TYPE;
            } else if (clazz == void.class) {
                return VOID_TYPE;
            } else if (clazz == boolean.class) {
                return BOOLEAN_TYPE;
            } else if (clazz == byte.class) {
                return BYTE_TYPE;
            } else if (clazz == char.class) {
                return CHAR_TYPE;
            } else if (clazz == short.class) {
                return SHORT_TYPE;
            } else if (clazz == double.class) {
                return DOUBLE_TYPE;
            } else if (clazz == float.class) {
                return FLOAT_TYPE;
            } else if (clazz == long.class) {
                return LONG_TYPE;
            } else {
                throw new AssertionError();
            }
        } else {
            return getType(getDescriptor(clazz));
        }
    }

    public static Type getType(Constructor<?> constructor) {
        return getType(getConstructorDescriptor(constructor));
    }

    public static Type getType(Method method) {
        return getType(getMethodDescriptor(method));
    }

    public Type getElementType() {
        int numDimensions = this.getDimensions();
        return getTypeInternal(this.valueBuffer, this.valueBegin + numDimensions, this.valueEnd);
    }

    public static Type getObjectType(String internalName) {
        return new Type(internalName.charAt(0) == '[' ? 9 : 12, internalName, 0, internalName.length());
    }

    public static Type getMethodType(String methodDescriptor) {
        return new Type(11, methodDescriptor, 0, methodDescriptor.length());
    }

    public static Type getMethodType(Type returnType, Type... argumentTypes) {
        return getType(getMethodDescriptor(returnType, argumentTypes));
    }

    public Type[] getArgumentTypes() {
        return getArgumentTypes(this.getDescriptor());
    }

    public static Type[] getArgumentTypes(String methodDescriptor) {
        int numArgumentTypes = 0;
        for (int currentOffset = 1; methodDescriptor.charAt(currentOffset) != ')'; numArgumentTypes++) {
            while (methodDescriptor.charAt(currentOffset) == '[') {
                currentOffset++;
            }
            if (methodDescriptor.charAt(currentOffset++) == 'L') {
                int semiColumnOffset = methodDescriptor.indexOf(59, currentOffset);
                currentOffset = Math.max(currentOffset, semiColumnOffset + 1);
            }
        }
        Type[] argumentTypes = new Type[numArgumentTypes];
        int var7 = 1;
        int currentArgumentTypeIndex = 0;
        while (methodDescriptor.charAt(var7) != ')') {
            int currentArgumentTypeOffset = var7;
            while (methodDescriptor.charAt(var7) == '[') {
                var7++;
            }
            if (methodDescriptor.charAt(var7++) == 'L') {
                int semiColumnOffset = methodDescriptor.indexOf(59, var7);
                var7 = Math.max(var7, semiColumnOffset + 1);
            }
            argumentTypes[currentArgumentTypeIndex++] = getTypeInternal(methodDescriptor, currentArgumentTypeOffset, var7);
        }
        return argumentTypes;
    }

    public static Type[] getArgumentTypes(Method method) {
        Class<?>[] classes = method.getParameterTypes();
        Type[] types = new Type[classes.length];
        for (int i = classes.length - 1; i >= 0; i--) {
            types[i] = getType(classes[i]);
        }
        return types;
    }

    public Type getReturnType() {
        return getReturnType(this.getDescriptor());
    }

    public static Type getReturnType(String methodDescriptor) {
        return getTypeInternal(methodDescriptor, getReturnTypeOffset(methodDescriptor), methodDescriptor.length());
    }

    public static Type getReturnType(Method method) {
        return getType(method.getReturnType());
    }

    static int getReturnTypeOffset(String methodDescriptor) {
        int currentOffset = 1;
        while (methodDescriptor.charAt(currentOffset) != ')') {
            while (methodDescriptor.charAt(currentOffset) == '[') {
                currentOffset++;
            }
            if (methodDescriptor.charAt(currentOffset++) == 'L') {
                int semiColumnOffset = methodDescriptor.indexOf(59, currentOffset);
                currentOffset = Math.max(currentOffset, semiColumnOffset + 1);
            }
        }
        return currentOffset + 1;
    }

    private static Type getTypeInternal(String descriptorBuffer, int descriptorBegin, int descriptorEnd) {
        switch(descriptorBuffer.charAt(descriptorBegin)) {
            case '(':
                return new Type(11, descriptorBuffer, descriptorBegin, descriptorEnd);
            case 'B':
                return BYTE_TYPE;
            case 'C':
                return CHAR_TYPE;
            case 'D':
                return DOUBLE_TYPE;
            case 'F':
                return FLOAT_TYPE;
            case 'I':
                return INT_TYPE;
            case 'J':
                return LONG_TYPE;
            case 'L':
                return new Type(10, descriptorBuffer, descriptorBegin + 1, descriptorEnd - 1);
            case 'S':
                return SHORT_TYPE;
            case 'V':
                return VOID_TYPE;
            case 'Z':
                return BOOLEAN_TYPE;
            case '[':
                return new Type(9, descriptorBuffer, descriptorBegin, descriptorEnd);
            default:
                throw new IllegalArgumentException();
        }
    }

    public String getClassName() {
        switch(this.sort) {
            case 0:
                return "void";
            case 1:
                return "boolean";
            case 2:
                return "char";
            case 3:
                return "byte";
            case 4:
                return "short";
            case 5:
                return "int";
            case 6:
                return "float";
            case 7:
                return "long";
            case 8:
                return "double";
            case 9:
                StringBuilder stringBuilder = new StringBuilder(this.getElementType().getClassName());
                for (int i = this.getDimensions(); i > 0; i--) {
                    stringBuilder.append("[]");
                }
                return stringBuilder.toString();
            case 10:
            case 12:
                return this.valueBuffer.substring(this.valueBegin, this.valueEnd).replace('/', '.');
            case 11:
            default:
                throw new AssertionError();
        }
    }

    public String getInternalName() {
        return this.valueBuffer.substring(this.valueBegin, this.valueEnd);
    }

    public static String getInternalName(Class<?> clazz) {
        return clazz.getName().replace('.', '/');
    }

    public String getDescriptor() {
        if (this.sort == 10) {
            return this.valueBuffer.substring(this.valueBegin - 1, this.valueEnd + 1);
        } else {
            return this.sort == 12 ? 'L' + this.valueBuffer.substring(this.valueBegin, this.valueEnd) + ';' : this.valueBuffer.substring(this.valueBegin, this.valueEnd);
        }
    }

    public static String getDescriptor(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        appendDescriptor(clazz, stringBuilder);
        return stringBuilder.toString();
    }

    public static String getConstructorDescriptor(Constructor<?> constructor) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        Class<?>[] parameters = constructor.getParameterTypes();
        for (Class<?> parameter : parameters) {
            appendDescriptor(parameter, stringBuilder);
        }
        return stringBuilder.append(")V").toString();
    }

    public static String getMethodDescriptor(Type returnType, Type... argumentTypes) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        for (Type argumentType : argumentTypes) {
            argumentType.appendDescriptor(stringBuilder);
        }
        stringBuilder.append(')');
        returnType.appendDescriptor(stringBuilder);
        return stringBuilder.toString();
    }

    public static String getMethodDescriptor(Method method) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        Class<?>[] parameters = method.getParameterTypes();
        for (Class<?> parameter : parameters) {
            appendDescriptor(parameter, stringBuilder);
        }
        stringBuilder.append(')');
        appendDescriptor(method.getReturnType(), stringBuilder);
        return stringBuilder.toString();
    }

    private void appendDescriptor(StringBuilder stringBuilder) {
        if (this.sort == 10) {
            stringBuilder.append(this.valueBuffer, this.valueBegin - 1, this.valueEnd + 1);
        } else if (this.sort == 12) {
            stringBuilder.append('L').append(this.valueBuffer, this.valueBegin, this.valueEnd).append(';');
        } else {
            stringBuilder.append(this.valueBuffer, this.valueBegin, this.valueEnd);
        }
    }

    private static void appendDescriptor(Class<?> clazz, StringBuilder stringBuilder) {
        Class<?> currentClass;
        for (currentClass = clazz; currentClass.isArray(); currentClass = currentClass.getComponentType()) {
            stringBuilder.append('[');
        }
        if (currentClass.isPrimitive()) {
            char descriptor;
            if (currentClass == int.class) {
                descriptor = 'I';
            } else if (currentClass == void.class) {
                descriptor = 'V';
            } else if (currentClass == boolean.class) {
                descriptor = 'Z';
            } else if (currentClass == byte.class) {
                descriptor = 'B';
            } else if (currentClass == char.class) {
                descriptor = 'C';
            } else if (currentClass == short.class) {
                descriptor = 'S';
            } else if (currentClass == double.class) {
                descriptor = 'D';
            } else if (currentClass == float.class) {
                descriptor = 'F';
            } else {
                if (currentClass != long.class) {
                    throw new AssertionError();
                }
                descriptor = 'J';
            }
            stringBuilder.append(descriptor);
        } else {
            stringBuilder.append('L').append(getInternalName(currentClass)).append(';');
        }
    }

    public int getSort() {
        return this.sort == 12 ? 10 : this.sort;
    }

    public int getDimensions() {
        int numDimensions = 1;
        while (this.valueBuffer.charAt(this.valueBegin + numDimensions) == '[') {
            numDimensions++;
        }
        return numDimensions;
    }

    public int getSize() {
        switch(this.sort) {
            case 0:
                return 0;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 9:
            case 10:
            case 12:
                return 1;
            case 7:
            case 8:
                return 2;
            case 11:
            default:
                throw new AssertionError();
        }
    }

    public int getArgumentsAndReturnSizes() {
        return getArgumentsAndReturnSizes(this.getDescriptor());
    }

    public static int getArgumentsAndReturnSizes(String methodDescriptor) {
        int argumentsSize = 1;
        int currentOffset = 1;
        for (int currentChar = methodDescriptor.charAt(currentOffset); currentChar != 41; currentChar = methodDescriptor.charAt(currentOffset)) {
            if (currentChar != 74 && currentChar != 68) {
                while (methodDescriptor.charAt(currentOffset) == '[') {
                    currentOffset++;
                }
                if (methodDescriptor.charAt(currentOffset++) == 'L') {
                    int semiColumnOffset = methodDescriptor.indexOf(59, currentOffset);
                    currentOffset = Math.max(currentOffset, semiColumnOffset + 1);
                }
                argumentsSize++;
            } else {
                currentOffset++;
                argumentsSize += 2;
            }
        }
        int var5 = methodDescriptor.charAt(currentOffset + 1);
        if (var5 == 'V') {
            return argumentsSize << 2;
        } else {
            int returnSize = var5 != 'J' && var5 != 'D' ? 1 : 2;
            return argumentsSize << 2 | returnSize;
        }
    }

    public int getOpcode(int opcode) {
        if (opcode != 46 && opcode != 79) {
            switch(this.sort) {
                case 0:
                    if (opcode != 172) {
                        throw new UnsupportedOperationException();
                    }
                    return 177;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    return opcode;
                case 6:
                    return opcode + 2;
                case 7:
                    return opcode + 1;
                case 8:
                    return opcode + 3;
                case 9:
                case 10:
                case 12:
                    if (opcode != 21 && opcode != 54 && opcode != 172) {
                        throw new UnsupportedOperationException();
                    }
                    return opcode + 4;
                case 11:
                    throw new UnsupportedOperationException();
                default:
                    throw new AssertionError();
            }
        } else {
            switch(this.sort) {
                case 0:
                case 11:
                    throw new UnsupportedOperationException();
                case 1:
                case 3:
                    return opcode + 5;
                case 2:
                    return opcode + 6;
                case 4:
                    return opcode + 7;
                case 5:
                    return opcode;
                case 6:
                    return opcode + 2;
                case 7:
                    return opcode + 1;
                case 8:
                    return opcode + 3;
                case 9:
                case 10:
                case 12:
                    return opcode + 4;
                default:
                    throw new AssertionError();
            }
        }
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Type)) {
            return false;
        } else {
            Type other = (Type) object;
            if ((this.sort == 12 ? 10 : this.sort) != (other.sort == 12 ? 10 : other.sort)) {
                return false;
            } else {
                int begin = this.valueBegin;
                int end = this.valueEnd;
                int otherBegin = other.valueBegin;
                int otherEnd = other.valueEnd;
                if (end - begin != otherEnd - otherBegin) {
                    return false;
                } else {
                    int i = begin;
                    for (int j = otherBegin; i < end; j++) {
                        if (this.valueBuffer.charAt(i) != other.valueBuffer.charAt(j)) {
                            return false;
                        }
                        i++;
                    }
                    return true;
                }
            }
        }
    }

    public int hashCode() {
        int hashCode = 13 * (this.sort == 12 ? 10 : this.sort);
        if (this.sort >= 9) {
            int i = this.valueBegin;
            for (int end = this.valueEnd; i < end; i++) {
                hashCode = 17 * (hashCode + this.valueBuffer.charAt(i));
            }
        }
        return hashCode;
    }

    public String toString() {
        return this.getDescriptor();
    }
}