package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

public final class Api extends GeneratedMessageLite<Api, Api.Builder> implements ApiOrBuilder {

    public static final int NAME_FIELD_NUMBER = 1;

    private String name_ = "";

    public static final int METHODS_FIELD_NUMBER = 2;

    private Internal.ProtobufList<Method> methods_ = emptyProtobufList();

    public static final int OPTIONS_FIELD_NUMBER = 3;

    private Internal.ProtobufList<Option> options_ = emptyProtobufList();

    public static final int VERSION_FIELD_NUMBER = 4;

    private String version_ = "";

    public static final int SOURCE_CONTEXT_FIELD_NUMBER = 5;

    private SourceContext sourceContext_;

    public static final int MIXINS_FIELD_NUMBER = 6;

    private Internal.ProtobufList<Mixin> mixins_ = emptyProtobufList();

    public static final int SYNTAX_FIELD_NUMBER = 7;

    private int syntax_;

    private static final Api DEFAULT_INSTANCE;

    private static volatile Parser<Api> PARSER;

    private Api() {
    }

    @Override
    public String getName() {
        return this.name_;
    }

    @Override
    public ByteString getNameBytes() {
        return ByteString.copyFromUtf8(this.name_);
    }

    private void setName(String value) {
        Class<?> valueClass = value.getClass();
        this.name_ = value;
    }

    private void clearName() {
        this.name_ = getDefaultInstance().getName();
    }

    private void setNameBytes(ByteString value) {
        checkByteStringIsUtf8(value);
        this.name_ = value.toStringUtf8();
    }

    @Override
    public List<Method> getMethodsList() {
        return this.methods_;
    }

    public List<? extends MethodOrBuilder> getMethodsOrBuilderList() {
        return this.methods_;
    }

    @Override
    public int getMethodsCount() {
        return this.methods_.size();
    }

    @Override
    public Method getMethods(int index) {
        return (Method) this.methods_.get(index);
    }

    public MethodOrBuilder getMethodsOrBuilder(int index) {
        return (MethodOrBuilder) this.methods_.get(index);
    }

    private void ensureMethodsIsMutable() {
        Internal.ProtobufList<Method> tmp = this.methods_;
        if (!tmp.isModifiable()) {
            this.methods_ = GeneratedMessageLite.mutableCopy(tmp);
        }
    }

    private void setMethods(int index, Method value) {
        value.getClass();
        this.ensureMethodsIsMutable();
        this.methods_.set(index, value);
    }

    private void addMethods(Method value) {
        value.getClass();
        this.ensureMethodsIsMutable();
        this.methods_.add(value);
    }

    private void addMethods(int index, Method value) {
        value.getClass();
        this.ensureMethodsIsMutable();
        this.methods_.add(index, value);
    }

    private void addAllMethods(Iterable<? extends Method> values) {
        this.ensureMethodsIsMutable();
        AbstractMessageLite.addAll(values, this.methods_);
    }

    private void clearMethods() {
        this.methods_ = emptyProtobufList();
    }

    private void removeMethods(int index) {
        this.ensureMethodsIsMutable();
        this.methods_.remove(index);
    }

    @Override
    public List<Option> getOptionsList() {
        return this.options_;
    }

    public List<? extends OptionOrBuilder> getOptionsOrBuilderList() {
        return this.options_;
    }

    @Override
    public int getOptionsCount() {
        return this.options_.size();
    }

    @Override
    public Option getOptions(int index) {
        return (Option) this.options_.get(index);
    }

    public OptionOrBuilder getOptionsOrBuilder(int index) {
        return (OptionOrBuilder) this.options_.get(index);
    }

    private void ensureOptionsIsMutable() {
        Internal.ProtobufList<Option> tmp = this.options_;
        if (!tmp.isModifiable()) {
            this.options_ = GeneratedMessageLite.mutableCopy(tmp);
        }
    }

    private void setOptions(int index, Option value) {
        value.getClass();
        this.ensureOptionsIsMutable();
        this.options_.set(index, value);
    }

    private void addOptions(Option value) {
        value.getClass();
        this.ensureOptionsIsMutable();
        this.options_.add(value);
    }

    private void addOptions(int index, Option value) {
        value.getClass();
        this.ensureOptionsIsMutable();
        this.options_.add(index, value);
    }

    private void addAllOptions(Iterable<? extends Option> values) {
        this.ensureOptionsIsMutable();
        AbstractMessageLite.addAll(values, this.options_);
    }

    private void clearOptions() {
        this.options_ = emptyProtobufList();
    }

    private void removeOptions(int index) {
        this.ensureOptionsIsMutable();
        this.options_.remove(index);
    }

    @Override
    public String getVersion() {
        return this.version_;
    }

    @Override
    public ByteString getVersionBytes() {
        return ByteString.copyFromUtf8(this.version_);
    }

    private void setVersion(String value) {
        Class<?> valueClass = value.getClass();
        this.version_ = value;
    }

    private void clearVersion() {
        this.version_ = getDefaultInstance().getVersion();
    }

    private void setVersionBytes(ByteString value) {
        checkByteStringIsUtf8(value);
        this.version_ = value.toStringUtf8();
    }

    @Override
    public boolean hasSourceContext() {
        return this.sourceContext_ != null;
    }

    @Override
    public SourceContext getSourceContext() {
        return this.sourceContext_ == null ? SourceContext.getDefaultInstance() : this.sourceContext_;
    }

    private void setSourceContext(SourceContext value) {
        value.getClass();
        this.sourceContext_ = value;
    }

    private void mergeSourceContext(SourceContext value) {
        value.getClass();
        if (this.sourceContext_ != null && this.sourceContext_ != SourceContext.getDefaultInstance()) {
            this.sourceContext_ = SourceContext.newBuilder(this.sourceContext_).mergeFrom(value).buildPartial();
        } else {
            this.sourceContext_ = value;
        }
    }

    private void clearSourceContext() {
        this.sourceContext_ = null;
    }

    @Override
    public List<Mixin> getMixinsList() {
        return this.mixins_;
    }

    public List<? extends MixinOrBuilder> getMixinsOrBuilderList() {
        return this.mixins_;
    }

    @Override
    public int getMixinsCount() {
        return this.mixins_.size();
    }

    @Override
    public Mixin getMixins(int index) {
        return (Mixin) this.mixins_.get(index);
    }

    public MixinOrBuilder getMixinsOrBuilder(int index) {
        return (MixinOrBuilder) this.mixins_.get(index);
    }

    private void ensureMixinsIsMutable() {
        Internal.ProtobufList<Mixin> tmp = this.mixins_;
        if (!tmp.isModifiable()) {
            this.mixins_ = GeneratedMessageLite.mutableCopy(tmp);
        }
    }

    private void setMixins(int index, Mixin value) {
        value.getClass();
        this.ensureMixinsIsMutable();
        this.mixins_.set(index, value);
    }

    private void addMixins(Mixin value) {
        value.getClass();
        this.ensureMixinsIsMutable();
        this.mixins_.add(value);
    }

    private void addMixins(int index, Mixin value) {
        value.getClass();
        this.ensureMixinsIsMutable();
        this.mixins_.add(index, value);
    }

    private void addAllMixins(Iterable<? extends Mixin> values) {
        this.ensureMixinsIsMutable();
        AbstractMessageLite.addAll(values, this.mixins_);
    }

    private void clearMixins() {
        this.mixins_ = emptyProtobufList();
    }

    private void removeMixins(int index) {
        this.ensureMixinsIsMutable();
        this.mixins_.remove(index);
    }

    @Override
    public int getSyntaxValue() {
        return this.syntax_;
    }

    @Override
    public Syntax getSyntax() {
        Syntax result = Syntax.forNumber(this.syntax_);
        return result == null ? Syntax.UNRECOGNIZED : result;
    }

    private void setSyntaxValue(int value) {
        this.syntax_ = value;
    }

    private void setSyntax(Syntax value) {
        this.syntax_ = value.getNumber();
    }

    private void clearSyntax() {
        this.syntax_ = 0;
    }

    public static Api parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Api parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Api parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Api parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Api parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Api parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Api parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Api parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Api parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static Api parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Api parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Api parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Api.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static Api.Builder newBuilder(Api prototype) {
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
        // 0: getstatic me/lucko/spark/lib/protobuf/Api.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/Api;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/Api.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/Api$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new Api();
            case NEW_BUILDER:
                return new Api.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "name_", "methods_", Method.class, "options_", Option.class, "version_", "sourceContext_", "mixins_", Mixin.class, "syntax_" };
                String info = "\u0000\u0007\u0000\u0000\u0001\u0007\u0007\u0000\u0003\u0000\u0001Ȉ\u0002\u001b\u0003\u001b\u0004Ȉ\u0005\t\u0006\u001b\u0007\f";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<Api> parser = PARSER;
                if (parser == null) {
                    synchronized (Api.class) {
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

    public static Api getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<Api> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        Api defaultInstance = new Api();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(Api.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<Api, Api.Builder> implements ApiOrBuilder {

        private Builder() {
            super(Api.DEFAULT_INSTANCE);
        }

        @Override
        public String getName() {
            return this.instance.getName();
        }

        @Override
        public ByteString getNameBytes() {
            return this.instance.getNameBytes();
        }

        public Api.Builder setName(String value) {
            this.copyOnWrite();
            this.instance.setName(value);
            return this;
        }

        public Api.Builder clearName() {
            this.copyOnWrite();
            this.instance.clearName();
            return this;
        }

        public Api.Builder setNameBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setNameBytes(value);
            return this;
        }

        @Override
        public List<Method> getMethodsList() {
            return Collections.unmodifiableList(this.instance.getMethodsList());
        }

        @Override
        public int getMethodsCount() {
            return this.instance.getMethodsCount();
        }

        @Override
        public Method getMethods(int index) {
            return this.instance.getMethods(index);
        }

        public Api.Builder setMethods(int index, Method value) {
            this.copyOnWrite();
            this.instance.setMethods(index, value);
            return this;
        }

        public Api.Builder setMethods(int index, Method.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setMethods(index, builderForValue.build());
            return this;
        }

        public Api.Builder addMethods(Method value) {
            this.copyOnWrite();
            this.instance.addMethods(value);
            return this;
        }

        public Api.Builder addMethods(int index, Method value) {
            this.copyOnWrite();
            this.instance.addMethods(index, value);
            return this;
        }

        public Api.Builder addMethods(Method.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addMethods(builderForValue.build());
            return this;
        }

        public Api.Builder addMethods(int index, Method.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addMethods(index, builderForValue.build());
            return this;
        }

        public Api.Builder addAllMethods(Iterable<? extends Method> values) {
            this.copyOnWrite();
            this.instance.addAllMethods(values);
            return this;
        }

        public Api.Builder clearMethods() {
            this.copyOnWrite();
            this.instance.clearMethods();
            return this;
        }

        public Api.Builder removeMethods(int index) {
            this.copyOnWrite();
            this.instance.removeMethods(index);
            return this;
        }

        @Override
        public List<Option> getOptionsList() {
            return Collections.unmodifiableList(this.instance.getOptionsList());
        }

        @Override
        public int getOptionsCount() {
            return this.instance.getOptionsCount();
        }

        @Override
        public Option getOptions(int index) {
            return this.instance.getOptions(index);
        }

        public Api.Builder setOptions(int index, Option value) {
            this.copyOnWrite();
            this.instance.setOptions(index, value);
            return this;
        }

        public Api.Builder setOptions(int index, Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setOptions(index, builderForValue.build());
            return this;
        }

        public Api.Builder addOptions(Option value) {
            this.copyOnWrite();
            this.instance.addOptions(value);
            return this;
        }

        public Api.Builder addOptions(int index, Option value) {
            this.copyOnWrite();
            this.instance.addOptions(index, value);
            return this;
        }

        public Api.Builder addOptions(Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addOptions(builderForValue.build());
            return this;
        }

        public Api.Builder addOptions(int index, Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addOptions(index, builderForValue.build());
            return this;
        }

        public Api.Builder addAllOptions(Iterable<? extends Option> values) {
            this.copyOnWrite();
            this.instance.addAllOptions(values);
            return this;
        }

        public Api.Builder clearOptions() {
            this.copyOnWrite();
            this.instance.clearOptions();
            return this;
        }

        public Api.Builder removeOptions(int index) {
            this.copyOnWrite();
            this.instance.removeOptions(index);
            return this;
        }

        @Override
        public String getVersion() {
            return this.instance.getVersion();
        }

        @Override
        public ByteString getVersionBytes() {
            return this.instance.getVersionBytes();
        }

        public Api.Builder setVersion(String value) {
            this.copyOnWrite();
            this.instance.setVersion(value);
            return this;
        }

        public Api.Builder clearVersion() {
            this.copyOnWrite();
            this.instance.clearVersion();
            return this;
        }

        public Api.Builder setVersionBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setVersionBytes(value);
            return this;
        }

        @Override
        public boolean hasSourceContext() {
            return this.instance.hasSourceContext();
        }

        @Override
        public SourceContext getSourceContext() {
            return this.instance.getSourceContext();
        }

        public Api.Builder setSourceContext(SourceContext value) {
            this.copyOnWrite();
            this.instance.setSourceContext(value);
            return this;
        }

        public Api.Builder setSourceContext(SourceContext.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setSourceContext(builderForValue.build());
            return this;
        }

        public Api.Builder mergeSourceContext(SourceContext value) {
            this.copyOnWrite();
            this.instance.mergeSourceContext(value);
            return this;
        }

        public Api.Builder clearSourceContext() {
            this.copyOnWrite();
            this.instance.clearSourceContext();
            return this;
        }

        @Override
        public List<Mixin> getMixinsList() {
            return Collections.unmodifiableList(this.instance.getMixinsList());
        }

        @Override
        public int getMixinsCount() {
            return this.instance.getMixinsCount();
        }

        @Override
        public Mixin getMixins(int index) {
            return this.instance.getMixins(index);
        }

        public Api.Builder setMixins(int index, Mixin value) {
            this.copyOnWrite();
            this.instance.setMixins(index, value);
            return this;
        }

        public Api.Builder setMixins(int index, Mixin.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setMixins(index, builderForValue.build());
            return this;
        }

        public Api.Builder addMixins(Mixin value) {
            this.copyOnWrite();
            this.instance.addMixins(value);
            return this;
        }

        public Api.Builder addMixins(int index, Mixin value) {
            this.copyOnWrite();
            this.instance.addMixins(index, value);
            return this;
        }

        public Api.Builder addMixins(Mixin.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addMixins(builderForValue.build());
            return this;
        }

        public Api.Builder addMixins(int index, Mixin.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addMixins(index, builderForValue.build());
            return this;
        }

        public Api.Builder addAllMixins(Iterable<? extends Mixin> values) {
            this.copyOnWrite();
            this.instance.addAllMixins(values);
            return this;
        }

        public Api.Builder clearMixins() {
            this.copyOnWrite();
            this.instance.clearMixins();
            return this;
        }

        public Api.Builder removeMixins(int index) {
            this.copyOnWrite();
            this.instance.removeMixins(index);
            return this;
        }

        @Override
        public int getSyntaxValue() {
            return this.instance.getSyntaxValue();
        }

        public Api.Builder setSyntaxValue(int value) {
            this.copyOnWrite();
            this.instance.setSyntaxValue(value);
            return this;
        }

        @Override
        public Syntax getSyntax() {
            return this.instance.getSyntax();
        }

        public Api.Builder setSyntax(Syntax value) {
            this.copyOnWrite();
            this.instance.setSyntax(value);
            return this;
        }

        public Api.Builder clearSyntax() {
            this.copyOnWrite();
            this.instance.clearSyntax();
            return this;
        }
    }
}