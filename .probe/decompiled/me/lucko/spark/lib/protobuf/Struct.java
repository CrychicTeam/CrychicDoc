package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;

public final class Struct extends GeneratedMessageLite<Struct, Struct.Builder> implements StructOrBuilder {

    public static final int FIELDS_FIELD_NUMBER = 1;

    private MapFieldLite<String, Value> fields_ = MapFieldLite.emptyMapField();

    private static final Struct DEFAULT_INSTANCE;

    private static volatile Parser<Struct> PARSER;

    private Struct() {
    }

    private MapFieldLite<String, Value> internalGetFields() {
        return this.fields_;
    }

    private MapFieldLite<String, Value> internalGetMutableFields() {
        if (!this.fields_.isMutable()) {
            this.fields_ = this.fields_.mutableCopy();
        }
        return this.fields_;
    }

    @Override
    public int getFieldsCount() {
        return this.internalGetFields().size();
    }

    @Override
    public boolean containsFields(String key) {
        Class<?> keyClass = key.getClass();
        return this.internalGetFields().containsKey(key);
    }

    @Deprecated
    @Override
    public Map<String, Value> getFields() {
        return this.getFieldsMap();
    }

    @Override
    public Map<String, Value> getFieldsMap() {
        return Collections.unmodifiableMap(this.internalGetFields());
    }

    @Override
    public Value getFieldsOrDefault(String key, Value defaultValue) {
        Class<?> keyClass = key.getClass();
        Map<String, Value> map = this.internalGetFields();
        return map.containsKey(key) ? (Value) map.get(key) : defaultValue;
    }

    @Override
    public Value getFieldsOrThrow(String key) {
        Class<?> keyClass = key.getClass();
        Map<String, Value> map = this.internalGetFields();
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException();
        } else {
            return (Value) map.get(key);
        }
    }

    private Map<String, Value> getMutableFieldsMap() {
        return this.internalGetMutableFields();
    }

    public static Struct parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Struct parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Struct parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Struct parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Struct parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Struct parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Struct parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Struct parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Struct parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static Struct parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Struct parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Struct parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Struct.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static Struct.Builder newBuilder(Struct prototype) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at java.base/java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:526)
        //   at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //   at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //   at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:230)
        //   at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:196)
        //   at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //   at java.base/java.util.stream.ReferencePipeline.anyMatch(ReferencePipeline.java:632)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$20(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 0: getstatic me/lucko/spark/lib/protobuf/Struct.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/Struct;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/Struct.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/Struct$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new Struct();
            case NEW_BUILDER:
                return new Struct.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "fields_", Struct.FieldsDefaultEntryHolder.defaultEntry };
                String info = "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0001\u0000\u0000\u00012";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<Struct> parser = PARSER;
                if (parser == null) {
                    synchronized (Struct.class) {
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

    public static Struct getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<Struct> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        Struct defaultInstance = new Struct();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(Struct.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<Struct, Struct.Builder> implements StructOrBuilder {

        private Builder() {
            super(Struct.DEFAULT_INSTANCE);
        }

        @Override
        public int getFieldsCount() {
            return this.instance.getFieldsMap().size();
        }

        @Override
        public boolean containsFields(String key) {
            Class<?> keyClass = key.getClass();
            return this.instance.getFieldsMap().containsKey(key);
        }

        public Struct.Builder clearFields() {
            this.copyOnWrite();
            this.instance.getMutableFieldsMap().clear();
            return this;
        }

        public Struct.Builder removeFields(String key) {
            Class<?> keyClass = key.getClass();
            this.copyOnWrite();
            this.instance.getMutableFieldsMap().remove(key);
            return this;
        }

        @Deprecated
        @Override
        public Map<String, Value> getFields() {
            return this.getFieldsMap();
        }

        @Override
        public Map<String, Value> getFieldsMap() {
            return Collections.unmodifiableMap(this.instance.getFieldsMap());
        }

        @Override
        public Value getFieldsOrDefault(String key, Value defaultValue) {
            Class<?> keyClass = key.getClass();
            Map<String, Value> map = this.instance.getFieldsMap();
            return map.containsKey(key) ? (Value) map.get(key) : defaultValue;
        }

        @Override
        public Value getFieldsOrThrow(String key) {
            Class<?> keyClass = key.getClass();
            Map<String, Value> map = this.instance.getFieldsMap();
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException();
            } else {
                return (Value) map.get(key);
            }
        }

        public Struct.Builder putFields(String key, Value value) {
            Class<?> keyClass = key.getClass();
            Class<?> valueClass = value.getClass();
            this.copyOnWrite();
            this.instance.getMutableFieldsMap().put(key, value);
            return this;
        }

        public Struct.Builder putAllFields(Map<String, Value> values) {
            this.copyOnWrite();
            this.instance.getMutableFieldsMap().putAll(values);
            return this;
        }
    }

    private static final class FieldsDefaultEntryHolder {

        static final MapEntryLite<String, Value> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.STRING, "", WireFormat.FieldType.MESSAGE, Value.getDefaultInstance());
    }
}