package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class Value extends GeneratedMessageLite<Value, Value.Builder> implements ValueOrBuilder {

    private int kindCase_ = 0;

    private Object kind_;

    public static final int NULL_VALUE_FIELD_NUMBER = 1;

    public static final int NUMBER_VALUE_FIELD_NUMBER = 2;

    public static final int STRING_VALUE_FIELD_NUMBER = 3;

    public static final int BOOL_VALUE_FIELD_NUMBER = 4;

    public static final int STRUCT_VALUE_FIELD_NUMBER = 5;

    public static final int LIST_VALUE_FIELD_NUMBER = 6;

    private static final Value DEFAULT_INSTANCE;

    private static volatile Parser<Value> PARSER;

    private Value() {
    }

    @Override
    public Value.KindCase getKindCase() {
        return Value.KindCase.forNumber(this.kindCase_);
    }

    private void clearKind() {
        this.kindCase_ = 0;
        this.kind_ = null;
    }

    @Override
    public boolean hasNullValue() {
        return this.kindCase_ == 1;
    }

    @Override
    public int getNullValueValue() {
        return this.kindCase_ == 1 ? (Integer) this.kind_ : 0;
    }

    @Override
    public NullValue getNullValue() {
        if (this.kindCase_ == 1) {
            NullValue result = NullValue.forNumber((Integer) this.kind_);
            return result == null ? NullValue.UNRECOGNIZED : result;
        } else {
            return NullValue.NULL_VALUE;
        }
    }

    private void setNullValueValue(int value) {
        this.kindCase_ = 1;
        this.kind_ = value;
    }

    private void setNullValue(NullValue value) {
        this.kind_ = value.getNumber();
        this.kindCase_ = 1;
    }

    private void clearNullValue() {
        if (this.kindCase_ == 1) {
            this.kindCase_ = 0;
            this.kind_ = null;
        }
    }

    @Override
    public boolean hasNumberValue() {
        return this.kindCase_ == 2;
    }

    @Override
    public double getNumberValue() {
        return this.kindCase_ == 2 ? (Double) this.kind_ : 0.0;
    }

    private void setNumberValue(double value) {
        this.kindCase_ = 2;
        this.kind_ = value;
    }

    private void clearNumberValue() {
        if (this.kindCase_ == 2) {
            this.kindCase_ = 0;
            this.kind_ = null;
        }
    }

    @Override
    public boolean hasStringValue() {
        return this.kindCase_ == 3;
    }

    @Override
    public String getStringValue() {
        String ref = "";
        if (this.kindCase_ == 3) {
            ref = (String) this.kind_;
        }
        return ref;
    }

    @Override
    public ByteString getStringValueBytes() {
        String ref = "";
        if (this.kindCase_ == 3) {
            ref = (String) this.kind_;
        }
        return ByteString.copyFromUtf8(ref);
    }

    private void setStringValue(String value) {
        Class<?> valueClass = value.getClass();
        this.kindCase_ = 3;
        this.kind_ = value;
    }

    private void clearStringValue() {
        if (this.kindCase_ == 3) {
            this.kindCase_ = 0;
            this.kind_ = null;
        }
    }

    private void setStringValueBytes(ByteString value) {
        checkByteStringIsUtf8(value);
        this.kind_ = value.toStringUtf8();
        this.kindCase_ = 3;
    }

    @Override
    public boolean hasBoolValue() {
        return this.kindCase_ == 4;
    }

    @Override
    public boolean getBoolValue() {
        return this.kindCase_ == 4 ? (Boolean) this.kind_ : false;
    }

    private void setBoolValue(boolean value) {
        this.kindCase_ = 4;
        this.kind_ = value;
    }

    private void clearBoolValue() {
        if (this.kindCase_ == 4) {
            this.kindCase_ = 0;
            this.kind_ = null;
        }
    }

    @Override
    public boolean hasStructValue() {
        return this.kindCase_ == 5;
    }

    @Override
    public Struct getStructValue() {
        return this.kindCase_ == 5 ? (Struct) this.kind_ : Struct.getDefaultInstance();
    }

    private void setStructValue(Struct value) {
        value.getClass();
        this.kind_ = value;
        this.kindCase_ = 5;
    }

    private void mergeStructValue(Struct value) {
        value.getClass();
        if (this.kindCase_ == 5 && this.kind_ != Struct.getDefaultInstance()) {
            this.kind_ = Struct.newBuilder((Struct) this.kind_).mergeFrom(value).buildPartial();
        } else {
            this.kind_ = value;
        }
        this.kindCase_ = 5;
    }

    private void clearStructValue() {
        if (this.kindCase_ == 5) {
            this.kindCase_ = 0;
            this.kind_ = null;
        }
    }

    @Override
    public boolean hasListValue() {
        return this.kindCase_ == 6;
    }

    @Override
    public ListValue getListValue() {
        return this.kindCase_ == 6 ? (ListValue) this.kind_ : ListValue.getDefaultInstance();
    }

    private void setListValue(ListValue value) {
        value.getClass();
        this.kind_ = value;
        this.kindCase_ = 6;
    }

    private void mergeListValue(ListValue value) {
        value.getClass();
        if (this.kindCase_ == 6 && this.kind_ != ListValue.getDefaultInstance()) {
            this.kind_ = ListValue.newBuilder((ListValue) this.kind_).mergeFrom(value).buildPartial();
        } else {
            this.kind_ = value;
        }
        this.kindCase_ = 6;
    }

    private void clearListValue() {
        if (this.kindCase_ == 6) {
            this.kindCase_ = 0;
            this.kind_ = null;
        }
    }

    public static Value parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Value parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Value parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Value parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Value parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Value parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Value parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Value parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Value parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static Value parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Value parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Value parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Value.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static Value.Builder newBuilder(Value prototype) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at java.base/java.util.concurrent.ConcurrentHashMap.replaceNode(ConcurrentHashMap.java:1111)
        //   at java.base/java.util.concurrent.ConcurrentHashMap.remove(ConcurrentHashMap.java:1102)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:97)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.instanceOf(StructContext.java:282)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 0: getstatic me/lucko/spark/lib/protobuf/Value.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/Value;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/Value.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/Value$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new Value();
            case NEW_BUILDER:
                return new Value.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "kind_", "kindCase_", Struct.class, ListValue.class };
                String info = "\u0000\u0006\u0001\u0000\u0001\u0006\u0006\u0000\u0000\u0000\u0001?\u0000\u00023\u0000\u0003Ȼ\u0000\u0004:\u0000\u0005<\u0000\u0006<\u0000";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<Value> parser = PARSER;
                if (parser == null) {
                    synchronized (Value.class) {
                        parser = PARSER;
                        if (parser == null) {
                            parser = new GeneratedMessageLite.DefaultInstanceBasedParser<>(DEFAULT_INSTANCE);
                            PARSER = parser;
                        }
                    }
                }
                return parser;
            case GET_MEMOIZED_IS_INITIALIZED:
                return (byte) 1;
            case SET_MEMOIZED_IS_INITIALIZED:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static Value getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<Value> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        Value defaultInstance = new Value();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(Value.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<Value, Value.Builder> implements ValueOrBuilder {

        private Builder() {
            super(Value.DEFAULT_INSTANCE);
        }

        @Override
        public Value.KindCase getKindCase() {
            return this.instance.getKindCase();
        }

        public Value.Builder clearKind() {
            this.copyOnWrite();
            this.instance.clearKind();
            return this;
        }

        @Override
        public boolean hasNullValue() {
            return this.instance.hasNullValue();
        }

        @Override
        public int getNullValueValue() {
            return this.instance.getNullValueValue();
        }

        public Value.Builder setNullValueValue(int value) {
            this.copyOnWrite();
            this.instance.setNullValueValue(value);
            return this;
        }

        @Override
        public NullValue getNullValue() {
            return this.instance.getNullValue();
        }

        public Value.Builder setNullValue(NullValue value) {
            this.copyOnWrite();
            this.instance.setNullValue(value);
            return this;
        }

        public Value.Builder clearNullValue() {
            this.copyOnWrite();
            this.instance.clearNullValue();
            return this;
        }

        @Override
        public boolean hasNumberValue() {
            return this.instance.hasNumberValue();
        }

        @Override
        public double getNumberValue() {
            return this.instance.getNumberValue();
        }

        public Value.Builder setNumberValue(double value) {
            this.copyOnWrite();
            this.instance.setNumberValue(value);
            return this;
        }

        public Value.Builder clearNumberValue() {
            this.copyOnWrite();
            this.instance.clearNumberValue();
            return this;
        }

        @Override
        public boolean hasStringValue() {
            return this.instance.hasStringValue();
        }

        @Override
        public String getStringValue() {
            return this.instance.getStringValue();
        }

        @Override
        public ByteString getStringValueBytes() {
            return this.instance.getStringValueBytes();
        }

        public Value.Builder setStringValue(String value) {
            this.copyOnWrite();
            this.instance.setStringValue(value);
            return this;
        }

        public Value.Builder clearStringValue() {
            this.copyOnWrite();
            this.instance.clearStringValue();
            return this;
        }

        public Value.Builder setStringValueBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setStringValueBytes(value);
            return this;
        }

        @Override
        public boolean hasBoolValue() {
            return this.instance.hasBoolValue();
        }

        @Override
        public boolean getBoolValue() {
            return this.instance.getBoolValue();
        }

        public Value.Builder setBoolValue(boolean value) {
            this.copyOnWrite();
            this.instance.setBoolValue(value);
            return this;
        }

        public Value.Builder clearBoolValue() {
            this.copyOnWrite();
            this.instance.clearBoolValue();
            return this;
        }

        @Override
        public boolean hasStructValue() {
            return this.instance.hasStructValue();
        }

        @Override
        public Struct getStructValue() {
            return this.instance.getStructValue();
        }

        public Value.Builder setStructValue(Struct value) {
            this.copyOnWrite();
            this.instance.setStructValue(value);
            return this;
        }

        public Value.Builder setStructValue(Struct.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setStructValue(builderForValue.build());
            return this;
        }

        public Value.Builder mergeStructValue(Struct value) {
            this.copyOnWrite();
            this.instance.mergeStructValue(value);
            return this;
        }

        public Value.Builder clearStructValue() {
            this.copyOnWrite();
            this.instance.clearStructValue();
            return this;
        }

        @Override
        public boolean hasListValue() {
            return this.instance.hasListValue();
        }

        @Override
        public ListValue getListValue() {
            return this.instance.getListValue();
        }

        public Value.Builder setListValue(ListValue value) {
            this.copyOnWrite();
            this.instance.setListValue(value);
            return this;
        }

        public Value.Builder setListValue(ListValue.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setListValue(builderForValue.build());
            return this;
        }

        public Value.Builder mergeListValue(ListValue value) {
            this.copyOnWrite();
            this.instance.mergeListValue(value);
            return this;
        }

        public Value.Builder clearListValue() {
            this.copyOnWrite();
            this.instance.clearListValue();
            return this;
        }
    }

    public static enum KindCase {

        NULL_VALUE(1),
        NUMBER_VALUE(2),
        STRING_VALUE(3),
        BOOL_VALUE(4),
        STRUCT_VALUE(5),
        LIST_VALUE(6),
        KIND_NOT_SET(0);

        private final int value;

        private KindCase(int value) {
            this.value = value;
        }

        @Deprecated
        public static Value.KindCase valueOf(int value) {
            return forNumber(value);
        }

        public static Value.KindCase forNumber(int value) {
            switch(value) {
                case 0:
                    return KIND_NOT_SET;
                case 1:
                    return NULL_VALUE;
                case 2:
                    return NUMBER_VALUE;
                case 3:
                    return STRING_VALUE;
                case 4:
                    return BOOL_VALUE;
                case 5:
                    return STRUCT_VALUE;
                case 6:
                    return LIST_VALUE;
                default:
                    return null;
            }
        }

        public int getNumber() {
            return this.value;
        }
    }
}