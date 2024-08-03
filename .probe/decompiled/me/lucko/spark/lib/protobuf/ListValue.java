package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

public final class ListValue extends GeneratedMessageLite<ListValue, ListValue.Builder> implements ListValueOrBuilder {

    public static final int VALUES_FIELD_NUMBER = 1;

    private Internal.ProtobufList<Value> values_ = emptyProtobufList();

    private static final ListValue DEFAULT_INSTANCE;

    private static volatile Parser<ListValue> PARSER;

    private ListValue() {
    }

    @Override
    public List<Value> getValuesList() {
        return this.values_;
    }

    public List<? extends ValueOrBuilder> getValuesOrBuilderList() {
        return this.values_;
    }

    @Override
    public int getValuesCount() {
        return this.values_.size();
    }

    @Override
    public Value getValues(int index) {
        return (Value) this.values_.get(index);
    }

    public ValueOrBuilder getValuesOrBuilder(int index) {
        return (ValueOrBuilder) this.values_.get(index);
    }

    private void ensureValuesIsMutable() {
        Internal.ProtobufList<Value> tmp = this.values_;
        if (!tmp.isModifiable()) {
            this.values_ = GeneratedMessageLite.mutableCopy(tmp);
        }
    }

    private void setValues(int index, Value value) {
        value.getClass();
        this.ensureValuesIsMutable();
        this.values_.set(index, value);
    }

    private void addValues(Value value) {
        value.getClass();
        this.ensureValuesIsMutable();
        this.values_.add(value);
    }

    private void addValues(int index, Value value) {
        value.getClass();
        this.ensureValuesIsMutable();
        this.values_.add(index, value);
    }

    private void addAllValues(Iterable<? extends Value> values) {
        this.ensureValuesIsMutable();
        AbstractMessageLite.addAll(values, this.values_);
    }

    private void clearValues() {
        this.values_ = emptyProtobufList();
    }

    private void removeValues(int index) {
        this.ensureValuesIsMutable();
        this.values_.remove(index);
    }

    public static ListValue parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static ListValue parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static ListValue parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static ListValue parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static ListValue parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static ListValue parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static ListValue parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static ListValue parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static ListValue parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static ListValue parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static ListValue parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static ListValue parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static ListValue.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static ListValue.Builder newBuilder(ListValue prototype) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 0: getstatic me/lucko/spark/lib/protobuf/ListValue.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/ListValue;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/ListValue.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/ListValue$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new ListValue();
            case NEW_BUILDER:
                return new ListValue.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "values_", Value.class };
                String info = "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001b";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<ListValue> parser = PARSER;
                if (parser == null) {
                    synchronized (ListValue.class) {
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

    public static ListValue getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<ListValue> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        ListValue defaultInstance = new ListValue();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(ListValue.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<ListValue, ListValue.Builder> implements ListValueOrBuilder {

        private Builder() {
            super(ListValue.DEFAULT_INSTANCE);
        }

        @Override
        public List<Value> getValuesList() {
            return Collections.unmodifiableList(this.instance.getValuesList());
        }

        @Override
        public int getValuesCount() {
            return this.instance.getValuesCount();
        }

        @Override
        public Value getValues(int index) {
            return this.instance.getValues(index);
        }

        public ListValue.Builder setValues(int index, Value value) {
            this.copyOnWrite();
            this.instance.setValues(index, value);
            return this;
        }

        public ListValue.Builder setValues(int index, Value.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setValues(index, builderForValue.build());
            return this;
        }

        public ListValue.Builder addValues(Value value) {
            this.copyOnWrite();
            this.instance.addValues(value);
            return this;
        }

        public ListValue.Builder addValues(int index, Value value) {
            this.copyOnWrite();
            this.instance.addValues(index, value);
            return this;
        }

        public ListValue.Builder addValues(Value.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addValues(builderForValue.build());
            return this;
        }

        public ListValue.Builder addValues(int index, Value.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addValues(index, builderForValue.build());
            return this;
        }

        public ListValue.Builder addAllValues(Iterable<? extends Value> values) {
            this.copyOnWrite();
            this.instance.addAllValues(values);
            return this;
        }

        public ListValue.Builder clearValues() {
            this.copyOnWrite();
            this.instance.clearValues();
            return this;
        }

        public ListValue.Builder removeValues(int index) {
            this.copyOnWrite();
            this.instance.removeValues(index);
            return this;
        }
    }
}