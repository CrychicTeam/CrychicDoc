package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

public final class Enum extends GeneratedMessageLite<Enum, Enum.Builder> implements EnumOrBuilder {

    public static final int NAME_FIELD_NUMBER = 1;

    private String name_ = "";

    public static final int ENUMVALUE_FIELD_NUMBER = 2;

    private Internal.ProtobufList<EnumValue> enumvalue_ = emptyProtobufList();

    public static final int OPTIONS_FIELD_NUMBER = 3;

    private Internal.ProtobufList<Option> options_ = emptyProtobufList();

    public static final int SOURCE_CONTEXT_FIELD_NUMBER = 4;

    private SourceContext sourceContext_;

    public static final int SYNTAX_FIELD_NUMBER = 5;

    private int syntax_;

    private static final Enum DEFAULT_INSTANCE;

    private static volatile Parser<Enum> PARSER;

    private Enum() {
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
    public List<EnumValue> getEnumvalueList() {
        return this.enumvalue_;
    }

    public List<? extends EnumValueOrBuilder> getEnumvalueOrBuilderList() {
        return this.enumvalue_;
    }

    @Override
    public int getEnumvalueCount() {
        return this.enumvalue_.size();
    }

    @Override
    public EnumValue getEnumvalue(int index) {
        return (EnumValue) this.enumvalue_.get(index);
    }

    public EnumValueOrBuilder getEnumvalueOrBuilder(int index) {
        return (EnumValueOrBuilder) this.enumvalue_.get(index);
    }

    private void ensureEnumvalueIsMutable() {
        Internal.ProtobufList<EnumValue> tmp = this.enumvalue_;
        if (!tmp.isModifiable()) {
            this.enumvalue_ = GeneratedMessageLite.mutableCopy(tmp);
        }
    }

    private void setEnumvalue(int index, EnumValue value) {
        value.getClass();
        this.ensureEnumvalueIsMutable();
        this.enumvalue_.set(index, value);
    }

    private void addEnumvalue(EnumValue value) {
        value.getClass();
        this.ensureEnumvalueIsMutable();
        this.enumvalue_.add(value);
    }

    private void addEnumvalue(int index, EnumValue value) {
        value.getClass();
        this.ensureEnumvalueIsMutable();
        this.enumvalue_.add(index, value);
    }

    private void addAllEnumvalue(Iterable<? extends EnumValue> values) {
        this.ensureEnumvalueIsMutable();
        AbstractMessageLite.addAll(values, this.enumvalue_);
    }

    private void clearEnumvalue() {
        this.enumvalue_ = emptyProtobufList();
    }

    private void removeEnumvalue(int index) {
        this.ensureEnumvalueIsMutable();
        this.enumvalue_.remove(index);
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

    public static Enum parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Enum parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Enum parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Enum parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Enum parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Enum parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Enum parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Enum parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Enum parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static Enum parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Enum parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Enum parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Enum.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static Enum.Builder newBuilder(Enum prototype) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 0: getstatic me/lucko/spark/lib/protobuf/Enum.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/Enum;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/Enum.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/Enum$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new Enum();
            case NEW_BUILDER:
                return new Enum.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "name_", "enumvalue_", EnumValue.class, "options_", Option.class, "sourceContext_", "syntax_" };
                String info = "\u0000\u0005\u0000\u0000\u0001\u0005\u0005\u0000\u0002\u0000\u0001Ȉ\u0002\u001b\u0003\u001b\u0004\t\u0005\f";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<Enum> parser = PARSER;
                if (parser == null) {
                    synchronized (Enum.class) {
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

    public static Enum getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<Enum> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        Enum defaultInstance = new Enum();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(Enum.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<Enum, Enum.Builder> implements EnumOrBuilder {

        private Builder() {
            super(Enum.DEFAULT_INSTANCE);
        }

        @Override
        public String getName() {
            return this.instance.getName();
        }

        @Override
        public ByteString getNameBytes() {
            return this.instance.getNameBytes();
        }

        public Enum.Builder setName(String value) {
            this.copyOnWrite();
            this.instance.setName(value);
            return this;
        }

        public Enum.Builder clearName() {
            this.copyOnWrite();
            this.instance.clearName();
            return this;
        }

        public Enum.Builder setNameBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setNameBytes(value);
            return this;
        }

        @Override
        public List<EnumValue> getEnumvalueList() {
            return Collections.unmodifiableList(this.instance.getEnumvalueList());
        }

        @Override
        public int getEnumvalueCount() {
            return this.instance.getEnumvalueCount();
        }

        @Override
        public EnumValue getEnumvalue(int index) {
            return this.instance.getEnumvalue(index);
        }

        public Enum.Builder setEnumvalue(int index, EnumValue value) {
            this.copyOnWrite();
            this.instance.setEnumvalue(index, value);
            return this;
        }

        public Enum.Builder setEnumvalue(int index, EnumValue.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setEnumvalue(index, builderForValue.build());
            return this;
        }

        public Enum.Builder addEnumvalue(EnumValue value) {
            this.copyOnWrite();
            this.instance.addEnumvalue(value);
            return this;
        }

        public Enum.Builder addEnumvalue(int index, EnumValue value) {
            this.copyOnWrite();
            this.instance.addEnumvalue(index, value);
            return this;
        }

        public Enum.Builder addEnumvalue(EnumValue.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addEnumvalue(builderForValue.build());
            return this;
        }

        public Enum.Builder addEnumvalue(int index, EnumValue.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addEnumvalue(index, builderForValue.build());
            return this;
        }

        public Enum.Builder addAllEnumvalue(Iterable<? extends EnumValue> values) {
            this.copyOnWrite();
            this.instance.addAllEnumvalue(values);
            return this;
        }

        public Enum.Builder clearEnumvalue() {
            this.copyOnWrite();
            this.instance.clearEnumvalue();
            return this;
        }

        public Enum.Builder removeEnumvalue(int index) {
            this.copyOnWrite();
            this.instance.removeEnumvalue(index);
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

        public Enum.Builder setOptions(int index, Option value) {
            this.copyOnWrite();
            this.instance.setOptions(index, value);
            return this;
        }

        public Enum.Builder setOptions(int index, Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setOptions(index, builderForValue.build());
            return this;
        }

        public Enum.Builder addOptions(Option value) {
            this.copyOnWrite();
            this.instance.addOptions(value);
            return this;
        }

        public Enum.Builder addOptions(int index, Option value) {
            this.copyOnWrite();
            this.instance.addOptions(index, value);
            return this;
        }

        public Enum.Builder addOptions(Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addOptions(builderForValue.build());
            return this;
        }

        public Enum.Builder addOptions(int index, Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addOptions(index, builderForValue.build());
            return this;
        }

        public Enum.Builder addAllOptions(Iterable<? extends Option> values) {
            this.copyOnWrite();
            this.instance.addAllOptions(values);
            return this;
        }

        public Enum.Builder clearOptions() {
            this.copyOnWrite();
            this.instance.clearOptions();
            return this;
        }

        public Enum.Builder removeOptions(int index) {
            this.copyOnWrite();
            this.instance.removeOptions(index);
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

        public Enum.Builder setSourceContext(SourceContext value) {
            this.copyOnWrite();
            this.instance.setSourceContext(value);
            return this;
        }

        public Enum.Builder setSourceContext(SourceContext.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setSourceContext(builderForValue.build());
            return this;
        }

        public Enum.Builder mergeSourceContext(SourceContext value) {
            this.copyOnWrite();
            this.instance.mergeSourceContext(value);
            return this;
        }

        public Enum.Builder clearSourceContext() {
            this.copyOnWrite();
            this.instance.clearSourceContext();
            return this;
        }

        @Override
        public int getSyntaxValue() {
            return this.instance.getSyntaxValue();
        }

        public Enum.Builder setSyntaxValue(int value) {
            this.copyOnWrite();
            this.instance.setSyntaxValue(value);
            return this;
        }

        @Override
        public Syntax getSyntax() {
            return this.instance.getSyntax();
        }

        public Enum.Builder setSyntax(Syntax value) {
            this.copyOnWrite();
            this.instance.setSyntax(value);
            return this;
        }

        public Enum.Builder clearSyntax() {
            this.copyOnWrite();
            this.instance.clearSyntax();
            return this;
        }
    }
}