package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class Timestamp extends GeneratedMessageLite<Timestamp, Timestamp.Builder> implements TimestampOrBuilder {

    public static final int SECONDS_FIELD_NUMBER = 1;

    private long seconds_;

    public static final int NANOS_FIELD_NUMBER = 2;

    private int nanos_;

    private static final Timestamp DEFAULT_INSTANCE;

    private static volatile Parser<Timestamp> PARSER;

    private Timestamp() {
    }

    @Override
    public long getSeconds() {
        return this.seconds_;
    }

    private void setSeconds(long value) {
        this.seconds_ = value;
    }

    private void clearSeconds() {
        this.seconds_ = 0L;
    }

    @Override
    public int getNanos() {
        return this.nanos_;
    }

    private void setNanos(int value) {
        this.nanos_ = value;
    }

    private void clearNanos() {
        this.nanos_ = 0;
    }

    public static Timestamp parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Timestamp parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Timestamp parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Timestamp parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Timestamp parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Timestamp parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Timestamp parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Timestamp parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Timestamp parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static Timestamp parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Timestamp parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Timestamp parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Timestamp.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static Timestamp.Builder newBuilder(Timestamp prototype) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.VarType.remap(VarType.java:428)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.remap(GenericType.java:350)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:693)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 0: getstatic me/lucko/spark/lib/protobuf/Timestamp.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/Timestamp;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/Timestamp.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/Timestamp$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new Timestamp();
            case NEW_BUILDER:
                return new Timestamp.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "seconds_", "nanos_" };
                String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\u0002\u0002\u0004";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<Timestamp> parser = PARSER;
                if (parser == null) {
                    synchronized (Timestamp.class) {
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

    public static Timestamp getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<Timestamp> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        Timestamp defaultInstance = new Timestamp();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(Timestamp.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<Timestamp, Timestamp.Builder> implements TimestampOrBuilder {

        private Builder() {
            super(Timestamp.DEFAULT_INSTANCE);
        }

        @Override
        public long getSeconds() {
            return this.instance.getSeconds();
        }

        public Timestamp.Builder setSeconds(long value) {
            this.copyOnWrite();
            this.instance.setSeconds(value);
            return this;
        }

        public Timestamp.Builder clearSeconds() {
            this.copyOnWrite();
            this.instance.clearSeconds();
            return this;
        }

        @Override
        public int getNanos() {
            return this.instance.getNanos();
        }

        public Timestamp.Builder setNanos(int value) {
            this.copyOnWrite();
            this.instance.setNanos(value);
            return this;
        }

        public Timestamp.Builder clearNanos() {
            this.copyOnWrite();
            this.instance.clearNanos();
            return this;
        }
    }
}