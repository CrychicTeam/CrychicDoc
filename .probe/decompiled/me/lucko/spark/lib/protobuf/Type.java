package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

public final class Type extends GeneratedMessageLite<Type, Type.Builder> implements TypeOrBuilder {

    public static final int NAME_FIELD_NUMBER = 1;

    private String name_ = "";

    public static final int FIELDS_FIELD_NUMBER = 2;

    private Internal.ProtobufList<Field> fields_ = emptyProtobufList();

    public static final int ONEOFS_FIELD_NUMBER = 3;

    private Internal.ProtobufList<String> oneofs_ = GeneratedMessageLite.emptyProtobufList();

    public static final int OPTIONS_FIELD_NUMBER = 4;

    private Internal.ProtobufList<Option> options_ = emptyProtobufList();

    public static final int SOURCE_CONTEXT_FIELD_NUMBER = 5;

    private SourceContext sourceContext_;

    public static final int SYNTAX_FIELD_NUMBER = 6;

    private int syntax_;

    private static final Type DEFAULT_INSTANCE;

    private static volatile Parser<Type> PARSER;

    private Type() {
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
    public List<Field> getFieldsList() {
        return this.fields_;
    }

    public List<? extends FieldOrBuilder> getFieldsOrBuilderList() {
        return this.fields_;
    }

    @Override
    public int getFieldsCount() {
        return this.fields_.size();
    }

    @Override
    public Field getFields(int index) {
        return (Field) this.fields_.get(index);
    }

    public FieldOrBuilder getFieldsOrBuilder(int index) {
        return (FieldOrBuilder) this.fields_.get(index);
    }

    private void ensureFieldsIsMutable() {
        Internal.ProtobufList<Field> tmp = this.fields_;
        if (!tmp.isModifiable()) {
            this.fields_ = GeneratedMessageLite.mutableCopy(tmp);
        }
    }

    private void setFields(int index, Field value) {
        value.getClass();
        this.ensureFieldsIsMutable();
        this.fields_.set(index, value);
    }

    private void addFields(Field value) {
        value.getClass();
        this.ensureFieldsIsMutable();
        this.fields_.add(value);
    }

    private void addFields(int index, Field value) {
        value.getClass();
        this.ensureFieldsIsMutable();
        this.fields_.add(index, value);
    }

    private void addAllFields(Iterable<? extends Field> values) {
        this.ensureFieldsIsMutable();
        AbstractMessageLite.addAll(values, this.fields_);
    }

    private void clearFields() {
        this.fields_ = emptyProtobufList();
    }

    private void removeFields(int index) {
        this.ensureFieldsIsMutable();
        this.fields_.remove(index);
    }

    @Override
    public List<String> getOneofsList() {
        return this.oneofs_;
    }

    @Override
    public int getOneofsCount() {
        return this.oneofs_.size();
    }

    @Override
    public String getOneofs(int index) {
        return (String) this.oneofs_.get(index);
    }

    @Override
    public ByteString getOneofsBytes(int index) {
        return ByteString.copyFromUtf8((String) this.oneofs_.get(index));
    }

    private void ensureOneofsIsMutable() {
        Internal.ProtobufList<String> tmp = this.oneofs_;
        if (!tmp.isModifiable()) {
            this.oneofs_ = GeneratedMessageLite.mutableCopy(tmp);
        }
    }

    private void setOneofs(int index, String value) {
        Class<?> valueClass = value.getClass();
        this.ensureOneofsIsMutable();
        this.oneofs_.set(index, value);
    }

    private void addOneofs(String value) {
        Class<?> valueClass = value.getClass();
        this.ensureOneofsIsMutable();
        this.oneofs_.add(value);
    }

    private void addAllOneofs(Iterable<String> values) {
        this.ensureOneofsIsMutable();
        AbstractMessageLite.addAll(values, this.oneofs_);
    }

    private void clearOneofs() {
        this.oneofs_ = GeneratedMessageLite.emptyProtobufList();
    }

    private void addOneofsBytes(ByteString value) {
        checkByteStringIsUtf8(value);
        this.ensureOneofsIsMutable();
        this.oneofs_.add(value.toStringUtf8());
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

    public static Type parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Type parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Type parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Type parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Type parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Type parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Type parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Type parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Type parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static Type parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Type parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Type parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Type.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static Type.Builder newBuilder(Type prototype) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:77)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.instanceOf(StructContext.java:282)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 0: getstatic me/lucko/spark/lib/protobuf/Type.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/Type;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/Type.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/Type$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new Type();
            case NEW_BUILDER:
                return new Type.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "name_", "fields_", Field.class, "oneofs_", "options_", Option.class, "sourceContext_", "syntax_" };
                String info = "\u0000\u0006\u0000\u0000\u0001\u0006\u0006\u0000\u0003\u0000\u0001Ȉ\u0002\u001b\u0003Ț\u0004\u001b\u0005\t\u0006\f";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<Type> parser = PARSER;
                if (parser == null) {
                    synchronized (Type.class) {
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

    public static Type getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<Type> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        Type defaultInstance = new Type();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(Type.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<Type, Type.Builder> implements TypeOrBuilder {

        private Builder() {
            super(Type.DEFAULT_INSTANCE);
        }

        @Override
        public String getName() {
            return this.instance.getName();
        }

        @Override
        public ByteString getNameBytes() {
            return this.instance.getNameBytes();
        }

        public Type.Builder setName(String value) {
            this.copyOnWrite();
            this.instance.setName(value);
            return this;
        }

        public Type.Builder clearName() {
            this.copyOnWrite();
            this.instance.clearName();
            return this;
        }

        public Type.Builder setNameBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setNameBytes(value);
            return this;
        }

        @Override
        public List<Field> getFieldsList() {
            return Collections.unmodifiableList(this.instance.getFieldsList());
        }

        @Override
        public int getFieldsCount() {
            return this.instance.getFieldsCount();
        }

        @Override
        public Field getFields(int index) {
            return this.instance.getFields(index);
        }

        public Type.Builder setFields(int index, Field value) {
            this.copyOnWrite();
            this.instance.setFields(index, value);
            return this;
        }

        public Type.Builder setFields(int index, Field.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setFields(index, builderForValue.build());
            return this;
        }

        public Type.Builder addFields(Field value) {
            this.copyOnWrite();
            this.instance.addFields(value);
            return this;
        }

        public Type.Builder addFields(int index, Field value) {
            this.copyOnWrite();
            this.instance.addFields(index, value);
            return this;
        }

        public Type.Builder addFields(Field.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addFields(builderForValue.build());
            return this;
        }

        public Type.Builder addFields(int index, Field.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addFields(index, builderForValue.build());
            return this;
        }

        public Type.Builder addAllFields(Iterable<? extends Field> values) {
            this.copyOnWrite();
            this.instance.addAllFields(values);
            return this;
        }

        public Type.Builder clearFields() {
            this.copyOnWrite();
            this.instance.clearFields();
            return this;
        }

        public Type.Builder removeFields(int index) {
            this.copyOnWrite();
            this.instance.removeFields(index);
            return this;
        }

        @Override
        public List<String> getOneofsList() {
            return Collections.unmodifiableList(this.instance.getOneofsList());
        }

        @Override
        public int getOneofsCount() {
            return this.instance.getOneofsCount();
        }

        @Override
        public String getOneofs(int index) {
            return this.instance.getOneofs(index);
        }

        @Override
        public ByteString getOneofsBytes(int index) {
            return this.instance.getOneofsBytes(index);
        }

        public Type.Builder setOneofs(int index, String value) {
            this.copyOnWrite();
            this.instance.setOneofs(index, value);
            return this;
        }

        public Type.Builder addOneofs(String value) {
            this.copyOnWrite();
            this.instance.addOneofs(value);
            return this;
        }

        public Type.Builder addAllOneofs(Iterable<String> values) {
            this.copyOnWrite();
            this.instance.addAllOneofs(values);
            return this;
        }

        public Type.Builder clearOneofs() {
            this.copyOnWrite();
            this.instance.clearOneofs();
            return this;
        }

        public Type.Builder addOneofsBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.addOneofsBytes(value);
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

        public Type.Builder setOptions(int index, Option value) {
            this.copyOnWrite();
            this.instance.setOptions(index, value);
            return this;
        }

        public Type.Builder setOptions(int index, Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setOptions(index, builderForValue.build());
            return this;
        }

        public Type.Builder addOptions(Option value) {
            this.copyOnWrite();
            this.instance.addOptions(value);
            return this;
        }

        public Type.Builder addOptions(int index, Option value) {
            this.copyOnWrite();
            this.instance.addOptions(index, value);
            return this;
        }

        public Type.Builder addOptions(Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addOptions(builderForValue.build());
            return this;
        }

        public Type.Builder addOptions(int index, Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addOptions(index, builderForValue.build());
            return this;
        }

        public Type.Builder addAllOptions(Iterable<? extends Option> values) {
            this.copyOnWrite();
            this.instance.addAllOptions(values);
            return this;
        }

        public Type.Builder clearOptions() {
            this.copyOnWrite();
            this.instance.clearOptions();
            return this;
        }

        public Type.Builder removeOptions(int index) {
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

        public Type.Builder setSourceContext(SourceContext value) {
            this.copyOnWrite();
            this.instance.setSourceContext(value);
            return this;
        }

        public Type.Builder setSourceContext(SourceContext.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setSourceContext(builderForValue.build());
            return this;
        }

        public Type.Builder mergeSourceContext(SourceContext value) {
            this.copyOnWrite();
            this.instance.mergeSourceContext(value);
            return this;
        }

        public Type.Builder clearSourceContext() {
            this.copyOnWrite();
            this.instance.clearSourceContext();
            return this;
        }

        @Override
        public int getSyntaxValue() {
            return this.instance.getSyntaxValue();
        }

        public Type.Builder setSyntaxValue(int value) {
            this.copyOnWrite();
            this.instance.setSyntaxValue(value);
            return this;
        }

        @Override
        public Syntax getSyntax() {
            return this.instance.getSyntax();
        }

        public Type.Builder setSyntax(Syntax value) {
            this.copyOnWrite();
            this.instance.setSyntax(value);
            return this;
        }

        public Type.Builder clearSyntax() {
            this.copyOnWrite();
            this.instance.clearSyntax();
            return this;
        }
    }
}