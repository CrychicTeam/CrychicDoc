package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class Any extends GeneratedMessageLite<Any, Any.Builder> implements AnyOrBuilder {

    public static final int TYPE_URL_FIELD_NUMBER = 1;

    private String typeUrl_ = "";

    public static final int VALUE_FIELD_NUMBER = 2;

    private ByteString value_ = ByteString.EMPTY;

    private static final Any DEFAULT_INSTANCE;

    private static volatile Parser<Any> PARSER;

    private Any() {
    }

    @Override
    public String getTypeUrl() {
        return this.typeUrl_;
    }

    @Override
    public ByteString getTypeUrlBytes() {
        return ByteString.copyFromUtf8(this.typeUrl_);
    }

    private void setTypeUrl(String value) {
        Class<?> valueClass = value.getClass();
        this.typeUrl_ = value;
    }

    private void clearTypeUrl() {
        this.typeUrl_ = getDefaultInstance().getTypeUrl();
    }

    private void setTypeUrlBytes(ByteString value) {
        checkByteStringIsUtf8(value);
        this.typeUrl_ = value.toStringUtf8();
    }

    @Override
    public ByteString getValue() {
        return this.value_;
    }

    private void setValue(ByteString value) {
        Class<?> valueClass = value.getClass();
        this.value_ = value;
    }

    private void clearValue() {
        this.value_ = getDefaultInstance().getValue();
    }

    public static Any parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Any parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Any parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Any parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Any parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Any parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Any parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Any parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Any parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static Any parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Any parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Any parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Any.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static Any.Builder newBuilder(Any prototype) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at java.base/java.util.stream.StreamOpFlag.fromCharacteristics(StreamOpFlag.java:750)
        //   at java.base/java.util.stream.StreamSupport.stream(StreamSupport.java:70)
        //   at java.base/java.util.Collection.stream(Collection.java:743)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$20(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 0: getstatic me/lucko/spark/lib/protobuf/Any.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/Any;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/Any.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/Any$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new Any();
            case NEW_BUILDER:
                return new Any.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "typeUrl_", "value_" };
                String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001Ȉ\u0002\n";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<Any> parser = PARSER;
                if (parser == null) {
                    synchronized (Any.class) {
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

    public static Any getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<Any> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        Any defaultInstance = new Any();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(Any.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<Any, Any.Builder> implements AnyOrBuilder {

        private Builder() {
            super(Any.DEFAULT_INSTANCE);
        }

        @Override
        public String getTypeUrl() {
            return this.instance.getTypeUrl();
        }

        @Override
        public ByteString getTypeUrlBytes() {
            return this.instance.getTypeUrlBytes();
        }

        public Any.Builder setTypeUrl(String value) {
            this.copyOnWrite();
            this.instance.setTypeUrl(value);
            return this;
        }

        public Any.Builder clearTypeUrl() {
            this.copyOnWrite();
            this.instance.clearTypeUrl();
            return this;
        }

        public Any.Builder setTypeUrlBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setTypeUrlBytes(value);
            return this;
        }

        @Override
        public ByteString getValue() {
            return this.instance.getValue();
        }

        public Any.Builder setValue(ByteString value) {
            this.copyOnWrite();
            this.instance.setValue(value);
            return this;
        }

        public Any.Builder clearValue() {
            this.copyOnWrite();
            this.instance.clearValue();
            return this;
        }
    }
}