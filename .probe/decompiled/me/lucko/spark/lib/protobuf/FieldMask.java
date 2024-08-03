package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

public final class FieldMask extends GeneratedMessageLite<FieldMask, FieldMask.Builder> implements FieldMaskOrBuilder {

    public static final int PATHS_FIELD_NUMBER = 1;

    private Internal.ProtobufList<String> paths_ = GeneratedMessageLite.emptyProtobufList();

    private static final FieldMask DEFAULT_INSTANCE;

    private static volatile Parser<FieldMask> PARSER;

    private FieldMask() {
    }

    @Override
    public List<String> getPathsList() {
        return this.paths_;
    }

    @Override
    public int getPathsCount() {
        return this.paths_.size();
    }

    @Override
    public String getPaths(int index) {
        return (String) this.paths_.get(index);
    }

    @Override
    public ByteString getPathsBytes(int index) {
        return ByteString.copyFromUtf8((String) this.paths_.get(index));
    }

    private void ensurePathsIsMutable() {
        Internal.ProtobufList<String> tmp = this.paths_;
        if (!tmp.isModifiable()) {
            this.paths_ = GeneratedMessageLite.mutableCopy(tmp);
        }
    }

    private void setPaths(int index, String value) {
        Class<?> valueClass = value.getClass();
        this.ensurePathsIsMutable();
        this.paths_.set(index, value);
    }

    private void addPaths(String value) {
        Class<?> valueClass = value.getClass();
        this.ensurePathsIsMutable();
        this.paths_.add(value);
    }

    private void addAllPaths(Iterable<String> values) {
        this.ensurePathsIsMutable();
        AbstractMessageLite.addAll(values, this.paths_);
    }

    private void clearPaths() {
        this.paths_ = GeneratedMessageLite.emptyProtobufList();
    }

    private void addPathsBytes(ByteString value) {
        checkByteStringIsUtf8(value);
        this.ensurePathsIsMutable();
        this.paths_.add(value.toStringUtf8());
    }

    public static FieldMask parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static FieldMask parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static FieldMask parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static FieldMask parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static FieldMask parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static FieldMask parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static FieldMask parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static FieldMask parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static FieldMask parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static FieldMask parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static FieldMask parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static FieldMask parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static FieldMask.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static FieldMask.Builder newBuilder(FieldMask prototype) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at java.base/java.util.stream.MatchOps$1MatchSink.accept(MatchOps.java:90)
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
        // 0: getstatic me/lucko/spark/lib/protobuf/FieldMask.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/FieldMask;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/FieldMask.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/FieldMask$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new FieldMask();
            case NEW_BUILDER:
                return new FieldMask.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "paths_" };
                String info = "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001Ț";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<FieldMask> parser = PARSER;
                if (parser == null) {
                    synchronized (FieldMask.class) {
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

    public static FieldMask getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<FieldMask> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        FieldMask defaultInstance = new FieldMask();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(FieldMask.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<FieldMask, FieldMask.Builder> implements FieldMaskOrBuilder {

        private Builder() {
            super(FieldMask.DEFAULT_INSTANCE);
        }

        @Override
        public List<String> getPathsList() {
            return Collections.unmodifiableList(this.instance.getPathsList());
        }

        @Override
        public int getPathsCount() {
            return this.instance.getPathsCount();
        }

        @Override
        public String getPaths(int index) {
            return this.instance.getPaths(index);
        }

        @Override
        public ByteString getPathsBytes(int index) {
            return this.instance.getPathsBytes(index);
        }

        public FieldMask.Builder setPaths(int index, String value) {
            this.copyOnWrite();
            this.instance.setPaths(index, value);
            return this;
        }

        public FieldMask.Builder addPaths(String value) {
            this.copyOnWrite();
            this.instance.addPaths(value);
            return this;
        }

        public FieldMask.Builder addAllPaths(Iterable<String> values) {
            this.copyOnWrite();
            this.instance.addAllPaths(values);
            return this;
        }

        public FieldMask.Builder clearPaths() {
            this.copyOnWrite();
            this.instance.clearPaths();
            return this;
        }

        public FieldMask.Builder addPathsBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.addPathsBytes(value);
            return this;
        }
    }
}