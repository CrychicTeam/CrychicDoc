package me.lucko.spark.lib.protobuf;

public enum Syntax implements Internal.EnumLite {

    SYNTAX_PROTO2(0), SYNTAX_PROTO3(1), UNRECOGNIZED(-1);

    public static final int SYNTAX_PROTO2_VALUE = 0;

    public static final int SYNTAX_PROTO3_VALUE = 1;

    private static final Internal.EnumLiteMap<Syntax> internalValueMap = new Internal.EnumLiteMap<Syntax>() {

        public Syntax findValueByNumber(int number) {
            return Syntax.forNumber(number);
        }
    };

    private final int value;

    @Override
    public final int getNumber() {
        if (this == UNRECOGNIZED) {
            throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
        } else {
            return this.value;
        }
    }

    @Deprecated
    public static Syntax valueOf(int value) {
        return forNumber(value);
    }

    public static Syntax forNumber(int value) {
        switch(value) {
            case 0:
                return SYNTAX_PROTO2;
            case 1:
                return SYNTAX_PROTO3;
            default:
                return null;
        }
    }

    public static Internal.EnumLiteMap<Syntax> internalGetValueMap() {
        return internalValueMap;
    }

    public static Internal.EnumVerifier internalGetVerifier() {
        return Syntax.SyntaxVerifier.INSTANCE;
    }

    private Syntax(int value) {
        this.value = value;
    }

    private static final class SyntaxVerifier implements Internal.EnumVerifier {

        static final Internal.EnumVerifier INSTANCE = new Syntax.SyntaxVerifier();

        @Override
        public boolean isInRange(int number) {
            return Syntax.forNumber(number) != null;
        }
    }
}