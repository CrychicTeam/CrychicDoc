package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class Int32Value extends GeneratedMessageLite<Int32Value, Int32Value.Builder> implements Int32ValueOrBuilder {

    public static final int VALUE_FIELD_NUMBER = 1;

    private int value_;

    private static final Int32Value DEFAULT_INSTANCE;

    private static volatile Parser<Int32Value> PARSER;

    private Int32Value() {
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

    public static Int32Value parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Int32Value parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Int32Value parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Int32Value parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Int32Value parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Int32Value parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Int32Value parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Int32Value parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Int32Value parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static Int32Value parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Int32Value parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Int32Value parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Int32Value.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static Int32Value.Builder newBuilder(Int32Value prototype) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at java.base/java.util.HashMap.hash(HashMap.java:338)
        //   at java.base/java.util.HashMap.getNode(HashMap.java:568)
        //   at java.base/java.util.HashMap.containsKey(HashMap.java:594)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.VarType.remap(VarType.java:428)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.remap(GenericType.java:350)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:693)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 0: getstatic me/lucko/spark/lib/protobuf/Int32Value.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/Int32Value;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/Int32Value.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/Int32Value$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new Int32Value();
            case NEW_BUILDER:
                return new Int32Value.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "value_" };
                String info = "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\u0004";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<Int32Value> parser = PARSER;
                if (parser == null) {
                    synchronized (Int32Value.class) {
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

    public static Int32Value getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Int32Value of(int value) {
        return newBuilder().setValue(value).build();
    }

    public static Parser<Int32Value> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        Int32Value defaultInstance = new Int32Value();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(Int32Value.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<Int32Value, Int32Value.Builder> implements Int32ValueOrBuilder {

        private Builder() {
            super(Int32Value.DEFAULT_INSTANCE);
        }

        @Override
        public int getValue() {
            return this.instance.getValue();
        }

        public Int32Value.Builder setValue(int value) {
            this.copyOnWrite();
            this.instance.setValue(value);
            return this;
        }

        public Int32Value.Builder clearValue() {
            this.copyOnWrite();
            this.instance.clearValue();
            return this;
        }
    }
}