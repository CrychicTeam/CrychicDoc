package me.lucko.spark.lib.protobuf;

public enum NullValue implements Internal.EnumLite {

    NULL_VALUE(0), UNRECOGNIZED(-1);

    public static final int NULL_VALUE_VALUE = 0;

    private static final Internal.EnumLiteMap<NullValue> internalValueMap = new Internal.EnumLiteMap<NullValue>() {

        public NullValue findValueByNumber(int number) {
            return NullValue.forNumber(number);
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
    public static NullValue valueOf(int value) {
        return forNumber(value);
    }

    public static NullValue forNumber(int value) {
        switch(value) {
            case 0:
                return NULL_VALUE;
            default:
                return null;
        }
    }

    public static Internal.EnumLiteMap<NullValue> internalGetValueMap() {
        return internalValueMap;
    }

    public static Internal.EnumVerifier internalGetVerifier() {
        return NullValue.NullValueVerifier.INSTANCE;
    }

    private NullValue(int value) {
        this.value = value;
    }

    private static final class NullValueVerifier implements Internal.EnumVerifier {

        static final Internal.EnumVerifier INSTANCE = new NullValue.NullValueVerifier();

        @Override
        public boolean isInRange(int number) {
            return NullValue.forNumber(number) != null;
        }
    }
}