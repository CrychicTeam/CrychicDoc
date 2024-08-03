package me.lucko.spark.lib.asm;

public final class TypePath {

    public static final int ARRAY_ELEMENT = 0;

    public static final int INNER_TYPE = 1;

    public static final int WILDCARD_BOUND = 2;

    public static final int TYPE_ARGUMENT = 3;

    private final byte[] typePathContainer;

    private final int typePathOffset;

    TypePath(byte[] typePathContainer, int typePathOffset) {
        this.typePathContainer = typePathContainer;
        this.typePathOffset = typePathOffset;
    }

    public int getLength() {
        return this.typePathContainer[this.typePathOffset];
    }

    public int getStep(int index) {
        return this.typePathContainer[this.typePathOffset + 2 * index + 1];
    }

    public int getStepArgument(int index) {
        return this.typePathContainer[this.typePathOffset + 2 * index + 2];
    }

    public static TypePath fromString(String typePath) {
        if (typePath != null && typePath.length() != 0) {
            int typePathLength = typePath.length();
            ByteVector output = new ByteVector(typePathLength);
            output.putByte(0);
            int typePathIndex = 0;
            while (typePathIndex < typePathLength) {
                char c = typePath.charAt(typePathIndex++);
                if (c == '[') {
                    output.put11(0, 0);
                } else if (c != '.') {
                    if (c != '*') {
                        if (c < '0' || c > '9') {
                            throw new IllegalArgumentException();
                        }
                        int typeArg = c - '0';
                        while (true) {
                            if (typePathIndex < typePathLength) {
                                c = typePath.charAt(typePathIndex++);
                                if (c >= '0' && c <= '9') {
                                    typeArg = typeArg * 10 + c - 48;
                                    continue;
                                }
                                if (c != ';') {
                                    throw new IllegalArgumentException();
                                }
                            }
                            output.put11(3, typeArg);
                            break;
                        }
                    } else {
                        output.put11(2, 0);
                    }
                } else {
                    output.put11(1, 0);
                }
            }
            output.data[0] = (byte) (output.length / 2);
            return new TypePath(output.data, 0);
        } else {
            return null;
        }
    }

    public String toString() {
        int length = this.getLength();
        StringBuilder result = new StringBuilder(length * 2);
        for (int i = 0; i < length; i++) {
            switch(this.getStep(i)) {
                case 0:
                    result.append('[');
                    break;
                case 1:
                    result.append('.');
                    break;
                case 2:
                    result.append('*');
                    break;
                case 3:
                    result.append(this.getStepArgument(i)).append(';');
                    break;
                default:
                    throw new AssertionError();
            }
        }
        return result.toString();
    }

    static void put(TypePath typePath, ByteVector output) {
        if (typePath == null) {
            output.putByte(0);
        } else {
            int length = typePath.typePathContainer[typePath.typePathOffset] * 2 + 1;
            output.putByteArray(typePath.typePathContainer, typePath.typePathOffset, length);
        }
    }
}