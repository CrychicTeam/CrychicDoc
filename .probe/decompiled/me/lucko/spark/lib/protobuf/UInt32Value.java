package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class UInt32Value extends GeneratedMessageLite<UInt32Value, UInt32Value.Builder> implements UInt32ValueOrBuilder {

    public static final int VALUE_FIELD_NUMBER = 1;

    private int value_;

    private static final UInt32Value DEFAULT_INSTANCE;

    private static volatile Parser<UInt32Value> PARSER;

    private UInt32Value() {
    }

    @Override
    public int getValue() {
        return this.value_;
    }

    private void setValue(int value) {
        this.value_ = value;
    }

    private void clearValue() {
        this.value_ = 0;
    }

    public static UInt32Value parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static UInt32Value parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static UInt32Value parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static UInt32Value parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static UInt32Value parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static UInt32Value parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static UInt32Value parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static UInt32Value parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static UInt32Value parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static UInt32Value parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static UInt32Value parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static UInt32Value parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static UInt32Value.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static UInt32Value.Builder newBuilder(UInt32Value prototype) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at java.base/java.util.Collections$2.tryAdvance(Collections.java:4853)
        //   at java.base/java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:129)
        //   at java.base/java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:527)
        //   at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //   at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //   at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:230)
        //   at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:196)
        //   at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //   at java.base/java.util.stream.ReferencePipeline.anyMatch(ReferencePipeline.java:632)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$20(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 0: getstatic me/lucko/spark/lib/protobuf/UInt32Value.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/UInt32Value;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/UInt32Value.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/UInt32Value$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new UInt32Value();
            case NEW_BUILDER:
                return new UInt32Value.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "value_" };
                String info = "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\u000b";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<UInt32Value> parser = PARSER;
                if (parser == null) {
                    synchronized (UInt32Value.class) {
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

    public static UInt32Value getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static UInt32Value of(int value) {
        return newBuilder().setValue(value).build();
    }

    public static Parser<UInt32Value> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        UInt32Value defaultInstance = new UInt32Value();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(UInt32Value.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<UInt32Value, UInt32Value.Builder> implements UInt32ValueOrBuilder {

        private Builder() {
            super(UInt32Value.DEFAULT_INSTANCE);
        }

        @Override
        public int getValue() {
            return this.instance.getValue();
        }

        public UInt32Value.Builder setValue(int value) {
            this.copyOnWrite();
            this.instance.setValue(value);
            return this;
        }

        public UInt32Value.Builder clearValue() {
            this.copyOnWrite();
            this.instance.clearValue();
            return this;
        }
    }
}