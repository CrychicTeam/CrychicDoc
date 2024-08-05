package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class SourceContext extends GeneratedMessageLite<SourceContext, SourceContext.Builder> implements SourceContextOrBuilder {

    public static final int FILE_NAME_FIELD_NUMBER = 1;

    private String fileName_ = "";

    private static final SourceContext DEFAULT_INSTANCE;

    private static volatile Parser<SourceContext> PARSER;

    private SourceContext() {
    }

    @Override
    public String getFileName() {
        return this.fileName_;
    }

    @Override
    public ByteString getFileNameBytes() {
        return ByteString.copyFromUtf8(this.fileName_);
    }

    private void setFileName(String value) {
        Class<?> valueClass = value.getClass();
        this.fileName_ = value;
    }

    private void clearFileName() {
        this.fileName_ = getDefaultInstance().getFileName();
    }

    private void setFileNameBytes(ByteString value) {
        checkByteStringIsUtf8(value);
        this.fileName_ = value.toStringUtf8();
    }

    public static SourceContext parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static SourceContext parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static SourceContext parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static SourceContext parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static SourceContext parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static SourceContext parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static SourceContext parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static SourceContext parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static SourceContext parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static SourceContext parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static SourceContext parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static SourceContext parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static SourceContext.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static SourceContext.Builder newBuilder(SourceContext prototype) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 0: getstatic me/lucko/spark/lib/protobuf/SourceContext.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/SourceContext;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/SourceContext.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/SourceContext$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new SourceContext();
            case NEW_BUILDER:
                return new SourceContext.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "fileName_" };
                String info = "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001Ȉ";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<SourceContext> parser = PARSER;
                if (parser == null) {
                    synchronized (SourceContext.class) {
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

    public static SourceContext getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<SourceContext> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        SourceContext defaultInstance = new SourceContext();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(SourceContext.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<SourceContext, SourceContext.Builder> implements SourceContextOrBuilder {

        private Builder() {
            super(SourceContext.DEFAULT_INSTANCE);
        }

        @Override
        public String getFileName() {
            return this.instance.getFileName();
        }

        @Override
        public ByteString getFileNameBytes() {
            return this.instance.getFileNameBytes();
        }

        public SourceContext.Builder setFileName(String value) {
            this.copyOnWrite();
            this.instance.setFileName(value);
            return this;
        }

        public SourceContext.Builder clearFileName() {
            this.copyOnWrite();
            this.instance.clearFileName();
            return this;
        }

        public SourceContext.Builder setFileNameBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setFileNameBytes(value);
            return this;
        }
    }
}