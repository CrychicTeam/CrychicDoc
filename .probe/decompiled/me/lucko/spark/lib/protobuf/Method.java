package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

public final class Method extends GeneratedMessageLite<Method, Method.Builder> implements MethodOrBuilder {

    public static final int NAME_FIELD_NUMBER = 1;

    private String name_ = "";

    public static final int REQUEST_TYPE_URL_FIELD_NUMBER = 2;

    private String requestTypeUrl_ = "";

    public static final int REQUEST_STREAMING_FIELD_NUMBER = 3;

    private boolean requestStreaming_;

    public static final int RESPONSE_TYPE_URL_FIELD_NUMBER = 4;

    private String responseTypeUrl_ = "";

    public static final int RESPONSE_STREAMING_FIELD_NUMBER = 5;

    private boolean responseStreaming_;

    public static final int OPTIONS_FIELD_NUMBER = 6;

    private Internal.ProtobufList<Option> options_ = emptyProtobufList();

    public static final int SYNTAX_FIELD_NUMBER = 7;

    private int syntax_;

    private static final Method DEFAULT_INSTANCE;

    private static volatile Parser<Method> PARSER;

    private Method() {
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
    public String getRequestTypeUrl() {
        return this.requestTypeUrl_;
    }

    @Override
    public ByteString getRequestTypeUrlBytes() {
        return ByteString.copyFromUtf8(this.requestTypeUrl_);
    }

    private void setRequestTypeUrl(String value) {
        Class<?> valueClass = value.getClass();
        this.requestTypeUrl_ = value;
    }

    private void clearRequestTypeUrl() {
        this.requestTypeUrl_ = getDefaultInstance().getRequestTypeUrl();
    }

    private void setRequestTypeUrlBytes(ByteString value) {
        checkByteStringIsUtf8(value);
        this.requestTypeUrl_ = value.toStringUtf8();
    }

    @Override
    public boolean getRequestStreaming() {
        return this.requestStreaming_;
    }

    private void setRequestStreaming(boolean value) {
        this.requestStreaming_ = value;
    }

    private void clearRequestStreaming() {
        this.requestStreaming_ = false;
    }

    @Override
    public String getResponseTypeUrl() {
        return this.responseTypeUrl_;
    }

    @Override
    public ByteString getResponseTypeUrlBytes() {
        return ByteString.copyFromUtf8(this.responseTypeUrl_);
    }

    private void setResponseTypeUrl(String value) {
        Class<?> valueClass = value.getClass();
        this.responseTypeUrl_ = value;
    }

    private void clearResponseTypeUrl() {
        this.responseTypeUrl_ = getDefaultInstance().getResponseTypeUrl();
    }

    private void setResponseTypeUrlBytes(ByteString value) {
        checkByteStringIsUtf8(value);
        this.responseTypeUrl_ = value.toStringUtf8();
    }

    @Override
    public boolean getResponseStreaming() {
        return this.responseStreaming_;
    }

    private void setResponseStreaming(boolean value) {
        this.responseStreaming_ = value;
    }

    private void clearResponseStreaming() {
        this.responseStreaming_ = false;
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

    public static Method parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Method parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Method parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Method parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Method parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Method parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Method parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Method parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Method parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static Method parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Method parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Method parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Method.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static Method.Builder newBuilder(Method prototype) {
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
        // 0: getstatic me/lucko/spark/lib/protobuf/Method.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/Method;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/Method.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/Method$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new Method();
            case NEW_BUILDER:
                return new Method.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "name_", "requestTypeUrl_", "requestStreaming_", "responseTypeUrl_", "responseStreaming_", "options_", Option.class, "syntax_" };
                String info = "\u0000\u0007\u0000\u0000\u0001\u0007\u0007\u0000\u0001\u0000\u0001Ȉ\u0002Ȉ\u0003\u0007\u0004Ȉ\u0005\u0007\u0006\u001b\u0007\f";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<Method> parser = PARSER;
                if (parser == null) {
                    synchronized (Method.class) {
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

    public static Method getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<Method> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        Method defaultInstance = new Method();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(Method.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<Method, Method.Builder> implements MethodOrBuilder {

        private Builder() {
            super(Method.DEFAULT_INSTANCE);
        }

        @Override
        public String getName() {
            return this.instance.getName();
        }

        @Override
        public ByteString getNameBytes() {
            return this.instance.getNameBytes();
        }

        public Method.Builder setName(String value) {
            this.copyOnWrite();
            this.instance.setName(value);
            return this;
        }

        public Method.Builder clearName() {
            this.copyOnWrite();
            this.instance.clearName();
            return this;
        }

        public Method.Builder setNameBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setNameBytes(value);
            return this;
        }

        @Override
        public String getRequestTypeUrl() {
            return this.instance.getRequestTypeUrl();
        }

        @Override
        public ByteString getRequestTypeUrlBytes() {
            return this.instance.getRequestTypeUrlBytes();
        }

        public Method.Builder setRequestTypeUrl(String value) {
            this.copyOnWrite();
            this.instance.setRequestTypeUrl(value);
            return this;
        }

        public Method.Builder clearRequestTypeUrl() {
            this.copyOnWrite();
            this.instance.clearRequestTypeUrl();
            return this;
        }

        public Method.Builder setRequestTypeUrlBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setRequestTypeUrlBytes(value);
            return this;
        }

        @Override
        public boolean getRequestStreaming() {
            return this.instance.getRequestStreaming();
        }

        public Method.Builder setRequestStreaming(boolean value) {
            this.copyOnWrite();
            this.instance.setRequestStreaming(value);
            return this;
        }

        public Method.Builder clearRequestStreaming() {
            this.copyOnWrite();
            this.instance.clearRequestStreaming();
            return this;
        }

        @Override
        public String getResponseTypeUrl() {
            return this.instance.getResponseTypeUrl();
        }

        @Override
        public ByteString getResponseTypeUrlBytes() {
            return this.instance.getResponseTypeUrlBytes();
        }

        public Method.Builder setResponseTypeUrl(String value) {
            this.copyOnWrite();
            this.instance.setResponseTypeUrl(value);
            return this;
        }

        public Method.Builder clearResponseTypeUrl() {
            this.copyOnWrite();
            this.instance.clearResponseTypeUrl();
            return this;
        }

        public Method.Builder setResponseTypeUrlBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setResponseTypeUrlBytes(value);
            return this;
        }

        @Override
        public boolean getResponseStreaming() {
            return this.instance.getResponseStreaming();
        }

        public Method.Builder setResponseStreaming(boolean value) {
            this.copyOnWrite();
            this.instance.setResponseStreaming(value);
            return this;
        }

        public Method.Builder clearResponseStreaming() {
            this.copyOnWrite();
            this.instance.clearResponseStreaming();
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

        public Method.Builder setOptions(int index, Option value) {
            this.copyOnWrite();
            this.instance.setOptions(index, value);
            return this;
        }

        public Method.Builder setOptions(int index, Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setOptions(index, builderForValue.build());
            return this;
        }

        public Method.Builder addOptions(Option value) {
            this.copyOnWrite();
            this.instance.addOptions(value);
            return this;
        }

        public Method.Builder addOptions(int index, Option value) {
            this.copyOnWrite();
            this.instance.addOptions(index, value);
            return this;
        }

        public Method.Builder addOptions(Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addOptions(builderForValue.build());
            return this;
        }

        public Method.Builder addOptions(int index, Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addOptions(index, builderForValue.build());
            return this;
        }

        public Method.Builder addAllOptions(Iterable<? extends Option> values) {
            this.copyOnWrite();
            this.instance.addAllOptions(values);
            return this;
        }

        public Method.Builder clearOptions() {
            this.copyOnWrite();
            this.instance.clearOptions();
            return this;
        }

        public Method.Builder removeOptions(int index) {
            this.copyOnWrite();
            this.instance.removeOptions(index);
            return this;
        }

        @Override
        public int getSyntaxValue() {
            return this.instance.getSyntaxValue();
        }

        public Method.Builder setSyntaxValue(int value) {
            this.copyOnWrite();
            this.instance.setSyntaxValue(value);
            return this;
        }

        @Override
        public Syntax getSyntax() {
            return this.instance.getSyntax();
        }

        public Method.Builder setSyntax(Syntax value) {
            this.copyOnWrite();
            this.instance.setSyntax(value);
            return this;
        }

        public Method.Builder clearSyntax() {
            this.copyOnWrite();
            this.instance.clearSyntax();
            return this;
        }
    }
}