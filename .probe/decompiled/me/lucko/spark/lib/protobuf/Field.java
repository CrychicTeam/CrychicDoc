package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

public final class Field extends GeneratedMessageLite<Field, Field.Builder> implements FieldOrBuilder {

    public static final int KIND_FIELD_NUMBER = 1;

    private int kind_;

    public static final int CARDINALITY_FIELD_NUMBER = 2;

    private int cardinality_;

    public static final int NUMBER_FIELD_NUMBER = 3;

    private int number_;

    public static final int NAME_FIELD_NUMBER = 4;

    private String name_ = "";

    public static final int TYPE_URL_FIELD_NUMBER = 6;

    private String typeUrl_ = "";

    public static final int ONEOF_INDEX_FIELD_NUMBER = 7;

    private int oneofIndex_;

    public static final int PACKED_FIELD_NUMBER = 8;

    private boolean packed_;

    public static final int OPTIONS_FIELD_NUMBER = 9;

    private Internal.ProtobufList<Option> options_ = emptyProtobufList();

    public static final int JSON_NAME_FIELD_NUMBER = 10;

    private String jsonName_ = "";

    public static final int DEFAULT_VALUE_FIELD_NUMBER = 11;

    private String defaultValue_ = "";

    private static final Field DEFAULT_INSTANCE;

    private static volatile Parser<Field> PARSER;

    private Field() {
    }

    @Override
    public int getKindValue() {
        return this.kind_;
    }

    @Override
    public Field.Kind getKind() {
        Field.Kind result = Field.Kind.forNumber(this.kind_);
        return result == null ? Field.Kind.UNRECOGNIZED : result;
    }

    private void setKindValue(int value) {
        this.kind_ = value;
    }

    private void setKind(Field.Kind value) {
        this.kind_ = value.getNumber();
    }

    private void clearKind() {
        this.kind_ = 0;
    }

    @Override
    public int getCardinalityValue() {
        return this.cardinality_;
    }

    @Override
    public Field.Cardinality getCardinality() {
        Field.Cardinality result = Field.Cardinality.forNumber(this.cardinality_);
        return result == null ? Field.Cardinality.UNRECOGNIZED : result;
    }

    private void setCardinalityValue(int value) {
        this.cardinality_ = value;
    }

    private void setCardinality(Field.Cardinality value) {
        this.cardinality_ = value.getNumber();
    }

    private void clearCardinality() {
        this.cardinality_ = 0;
    }

    @Override
    public int getNumber() {
        return this.number_;
    }

    private void setNumber(int value) {
        this.number_ = value;
    }

    private void clearNumber() {
        this.number_ = 0;
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
    public String getTypeUrl() {
        return this.typeUrl_;
    }

    @Override
    public ByteString getTypeUrlBytes() {
        return ByteString.copyFromUtf8(this.typeUrl_);
    }

    private void setTypeUrl(String value) {
        Class<?> valueClass = value.getClass();
        this.typeUrl_ = value;
    }

    private void clearTypeUrl() {
        this.typeUrl_ = getDefaultInstance().getTypeUrl();
    }

    private void setTypeUrlBytes(ByteString value) {
        checkByteStringIsUtf8(value);
        this.typeUrl_ = value.toStringUtf8();
    }

    @Override
    public int getOneofIndex() {
        return this.oneofIndex_;
    }

    private void setOneofIndex(int value) {
        this.oneofIndex_ = value;
    }

    private void clearOneofIndex() {
        this.oneofIndex_ = 0;
    }

    @Override
    public boolean getPacked() {
        return this.packed_;
    }

    private void setPacked(boolean value) {
        this.packed_ = value;
    }

    private void clearPacked() {
        this.packed_ = false;
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
    public String getJsonName() {
        return this.jsonName_;
    }

    @Override
    public ByteString getJsonNameBytes() {
        return ByteString.copyFromUtf8(this.jsonName_);
    }

    private void setJsonName(String value) {
        Class<?> valueClass = value.getClass();
        this.jsonName_ = value;
    }

    private void clearJsonName() {
        this.jsonName_ = getDefaultInstance().getJsonName();
    }

    private void setJsonNameBytes(ByteString value) {
        checkByteStringIsUtf8(value);
        this.jsonName_ = value.toStringUtf8();
    }

    @Override
    public String getDefaultValue() {
        return this.defaultValue_;
    }

    @Override
    public ByteString getDefaultValueBytes() {
        return ByteString.copyFromUtf8(this.defaultValue_);
    }

    private void setDefaultValue(String value) {
        Class<?> valueClass = value.getClass();
        this.defaultValue_ = value;
    }

    private void clearDefaultValue() {
        this.defaultValue_ = getDefaultInstance().getDefaultValue();
    }

    private void setDefaultValueBytes(ByteString value) {
        checkByteStringIsUtf8(value);
        this.defaultValue_ = value.toStringUtf8();
    }

    public static Field parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Field parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Field parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Field parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Field parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static Field parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static Field parseFrom(InputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Field parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Field parseDelimitedFrom(InputStream input) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static Field parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Field parseFrom(CodedInputStream input) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static Field parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Field.Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    public static Field.Builder newBuilder(Field prototype) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at java.base/java.lang.ThreadLocal.get(ThreadLocal.java:163)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 0: getstatic me/lucko/spark/lib/protobuf/Field.DEFAULT_INSTANCE Lme/lucko/spark/lib/protobuf/Field;
        // 3: aload 0
        // 4: invokevirtual me/lucko/spark/lib/protobuf/Field.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
        // 7: checkcast me/lucko/spark/lib/protobuf/Field$Builder
        // a: areturn
    }

    @Override
    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        switch(method) {
            case NEW_MUTABLE_INSTANCE:
                return new Field();
            case NEW_BUILDER:
                return new Field.Builder();
            case BUILD_MESSAGE_INFO:
                Object[] objects = new Object[] { "kind_", "cardinality_", "number_", "name_", "typeUrl_", "oneofIndex_", "packed_", "options_", Option.class, "jsonName_", "defaultValue_" };
                String info = "\u0000\n\u0000\u0000\u0001\u000b\n\u0000\u0001\u0000\u0001\f\u0002\f\u0003\u0004\u0004Ȉ\u0006Ȉ\u0007\u0004\b\u0007\t\u001b\nȈ\u000bȈ";
                return newMessageInfo(DEFAULT_INSTANCE, info, objects);
            case GET_DEFAULT_INSTANCE:
                return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<Field> parser = PARSER;
                if (parser == null) {
                    synchronized (Field.class) {
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

    public static Field getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<Field> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    static {
        Field defaultInstance = new Field();
        DEFAULT_INSTANCE = defaultInstance;
        GeneratedMessageLite.registerDefaultInstance(Field.class, defaultInstance);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<Field, Field.Builder> implements FieldOrBuilder {

        private Builder() {
            super(Field.DEFAULT_INSTANCE);
        }

        @Override
        public int getKindValue() {
            return this.instance.getKindValue();
        }

        public Field.Builder setKindValue(int value) {
            this.copyOnWrite();
            this.instance.setKindValue(value);
            return this;
        }

        @Override
        public Field.Kind getKind() {
            return this.instance.getKind();
        }

        public Field.Builder setKind(Field.Kind value) {
            this.copyOnWrite();
            this.instance.setKind(value);
            return this;
        }

        public Field.Builder clearKind() {
            this.copyOnWrite();
            this.instance.clearKind();
            return this;
        }

        @Override
        public int getCardinalityValue() {
            return this.instance.getCardinalityValue();
        }

        public Field.Builder setCardinalityValue(int value) {
            this.copyOnWrite();
            this.instance.setCardinalityValue(value);
            return this;
        }

        @Override
        public Field.Cardinality getCardinality() {
            return this.instance.getCardinality();
        }

        public Field.Builder setCardinality(Field.Cardinality value) {
            this.copyOnWrite();
            this.instance.setCardinality(value);
            return this;
        }

        public Field.Builder clearCardinality() {
            this.copyOnWrite();
            this.instance.clearCardinality();
            return this;
        }

        @Override
        public int getNumber() {
            return this.instance.getNumber();
        }

        public Field.Builder setNumber(int value) {
            this.copyOnWrite();
            this.instance.setNumber(value);
            return this;
        }

        public Field.Builder clearNumber() {
            this.copyOnWrite();
            this.instance.clearNumber();
            return this;
        }

        @Override
        public String getName() {
            return this.instance.getName();
        }

        @Override
        public ByteString getNameBytes() {
            return this.instance.getNameBytes();
        }

        public Field.Builder setName(String value) {
            this.copyOnWrite();
            this.instance.setName(value);
            return this;
        }

        public Field.Builder clearName() {
            this.copyOnWrite();
            this.instance.clearName();
            return this;
        }

        public Field.Builder setNameBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setNameBytes(value);
            return this;
        }

        @Override
        public String getTypeUrl() {
            return this.instance.getTypeUrl();
        }

        @Override
        public ByteString getTypeUrlBytes() {
            return this.instance.getTypeUrlBytes();
        }

        public Field.Builder setTypeUrl(String value) {
            this.copyOnWrite();
            this.instance.setTypeUrl(value);
            return this;
        }

        public Field.Builder clearTypeUrl() {
            this.copyOnWrite();
            this.instance.clearTypeUrl();
            return this;
        }

        public Field.Builder setTypeUrlBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setTypeUrlBytes(value);
            return this;
        }

        @Override
        public int getOneofIndex() {
            return this.instance.getOneofIndex();
        }

        public Field.Builder setOneofIndex(int value) {
            this.copyOnWrite();
            this.instance.setOneofIndex(value);
            return this;
        }

        public Field.Builder clearOneofIndex() {
            this.copyOnWrite();
            this.instance.clearOneofIndex();
            return this;
        }

        @Override
        public boolean getPacked() {
            return this.instance.getPacked();
        }

        public Field.Builder setPacked(boolean value) {
            this.copyOnWrite();
            this.instance.setPacked(value);
            return this;
        }

        public Field.Builder clearPacked() {
            this.copyOnWrite();
            this.instance.clearPacked();
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

        public Field.Builder setOptions(int index, Option value) {
            this.copyOnWrite();
            this.instance.setOptions(index, value);
            return this;
        }

        public Field.Builder setOptions(int index, Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.setOptions(index, builderForValue.build());
            return this;
        }

        public Field.Builder addOptions(Option value) {
            this.copyOnWrite();
            this.instance.addOptions(value);
            return this;
        }

        public Field.Builder addOptions(int index, Option value) {
            this.copyOnWrite();
            this.instance.addOptions(index, value);
            return this;
        }

        public Field.Builder addOptions(Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addOptions(builderForValue.build());
            return this;
        }

        public Field.Builder addOptions(int index, Option.Builder builderForValue) {
            this.copyOnWrite();
            this.instance.addOptions(index, builderForValue.build());
            return this;
        }

        public Field.Builder addAllOptions(Iterable<? extends Option> values) {
            this.copyOnWrite();
            this.instance.addAllOptions(values);
            return this;
        }

        public Field.Builder clearOptions() {
            this.copyOnWrite();
            this.instance.clearOptions();
            return this;
        }

        public Field.Builder removeOptions(int index) {
            this.copyOnWrite();
            this.instance.removeOptions(index);
            return this;
        }

        @Override
        public String getJsonName() {
            return this.instance.getJsonName();
        }

        @Override
        public ByteString getJsonNameBytes() {
            return this.instance.getJsonNameBytes();
        }

        public Field.Builder setJsonName(String value) {
            this.copyOnWrite();
            this.instance.setJsonName(value);
            return this;
        }

        public Field.Builder clearJsonName() {
            this.copyOnWrite();
            this.instance.clearJsonName();
            return this;
        }

        public Field.Builder setJsonNameBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setJsonNameBytes(value);
            return this;
        }

        @Override
        public String getDefaultValue() {
            return this.instance.getDefaultValue();
        }

        @Override
        public ByteString getDefaultValueBytes() {
            return this.instance.getDefaultValueBytes();
        }

        public Field.Builder setDefaultValue(String value) {
            this.copyOnWrite();
            this.instance.setDefaultValue(value);
            return this;
        }

        public Field.Builder clearDefaultValue() {
            this.copyOnWrite();
            this.instance.clearDefaultValue();
            return this;
        }

        public Field.Builder setDefaultValueBytes(ByteString value) {
            this.copyOnWrite();
            this.instance.setDefaultValueBytes(value);
            return this;
        }
    }

    public static enum Cardinality implements Internal.EnumLite {

        CARDINALITY_UNKNOWN(0), CARDINALITY_OPTIONAL(1), CARDINALITY_REQUIRED(2), CARDINALITY_REPEATED(3), UNRECOGNIZED(-1);

        public static final int CARDINALITY_UNKNOWN_VALUE = 0;

        public static final int CARDINALITY_OPTIONAL_VALUE = 1;

        public static final int CARDINALITY_REQUIRED_VALUE = 2;

        public static final int CARDINALITY_REPEATED_VALUE = 3;

        private static final Internal.EnumLiteMap<Field.Cardinality> internalValueMap = new Internal.EnumLiteMap<Field.Cardinality>() {

            public Field.Cardinality findValueByNumber(int number) {
                return Field.Cardinality.forNumber(number);
            }
        };

        private final int value;

        @Override
        public final int getNumber() {
            if (this == UNRECOGNIZED) {
                throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
            } else {
                return this.value;
            }
        }

        @Deprecated
        public static Field.Cardinality valueOf(int value) {
            return forNumber(value);
        }

        public static Field.Cardinality forNumber(int value) {
            switch(value) {
                case 0:
                    return CARDINALITY_UNKNOWN;
                case 1:
                    return CARDINALITY_OPTIONAL;
                case 2:
                    return CARDINALITY_REQUIRED;
                case 3:
                    return CARDINALITY_REPEATED;
                default:
                    return null;
            }
        }

        public static Internal.EnumLiteMap<Field.Cardinality> internalGetValueMap() {
            return internalValueMap;
        }

        public static Internal.EnumVerifier internalGetVerifier() {
            return Field.Cardinality.CardinalityVerifier.INSTANCE;
        }

        private Cardinality(int value) {
            this.value = value;
        }

        private static final class CardinalityVerifier implements Internal.EnumVerifier {

            static final Internal.EnumVerifier INSTANCE = new Field.Cardinality.CardinalityVerifier();

            @Override
            public boolean isInRange(int number) {
                return Field.Cardinality.forNumber(number) != null;
            }
        }
    }

    public static enum Kind implements Internal.EnumLite {

        TYPE_UNKNOWN(0),
        TYPE_DOUBLE(1),
        TYPE_FLOAT(2),
        TYPE_INT64(3),
        TYPE_UINT64(4),
        TYPE_INT32(5),
        TYPE_FIXED64(6),
        TYPE_FIXED32(7),
        TYPE_BOOL(8),
        TYPE_STRING(9),
        TYPE_GROUP(10),
        TYPE_MESSAGE(11),
        TYPE_BYTES(12),
        TYPE_UINT32(13),
        TYPE_ENUM(14),
        TYPE_SFIXED32(15),
        TYPE_SFIXED64(16),
        TYPE_SINT32(17),
        TYPE_SINT64(18),
        UNRECOGNIZED(-1);

        public static final int TYPE_UNKNOWN_VALUE = 0;

        public static final int TYPE_DOUBLE_VALUE = 1;

        public static final int TYPE_FLOAT_VALUE = 2;

        public static final int TYPE_INT64_VALUE = 3;

        public static final int TYPE_UINT64_VALUE = 4;

        public static final int TYPE_INT32_VALUE = 5;

        public static final int TYPE_FIXED64_VALUE = 6;

        public static final int TYPE_FIXED32_VALUE = 7;

        public static final int TYPE_BOOL_VALUE = 8;

        public static final int TYPE_STRING_VALUE = 9;

        public static final int TYPE_GROUP_VALUE = 10;

        public static final int TYPE_MESSAGE_VALUE = 11;

        public static final int TYPE_BYTES_VALUE = 12;

        public static final int TYPE_UINT32_VALUE = 13;

        public static final int TYPE_ENUM_VALUE = 14;

        public static final int TYPE_SFIXED32_VALUE = 15;

        public static final int TYPE_SFIXED64_VALUE = 16;

        public static final int TYPE_SINT32_VALUE = 17;

        public static final int TYPE_SINT64_VALUE = 18;

        private static final Internal.EnumLiteMap<Field.Kind> internalValueMap = new Internal.EnumLiteMap<Field.Kind>() {

            public Field.Kind findValueByNumber(int number) {
                return Field.Kind.forNumber(number);
            }
        };

        private final int value;

        @Override
        public final int getNumber() {
            if (this == UNRECOGNIZED) {
                throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
            } else {
                return this.value;
            }
        }

        @Deprecated
        public static Field.Kind valueOf(int value) {
            return forNumber(value);
        }

        public static Field.Kind forNumber(int value) {
            switch(value) {
                case 0:
                    return TYPE_UNKNOWN;
                case 1:
                    return TYPE_DOUBLE;
                case 2:
                    return TYPE_FLOAT;
                case 3:
                    return TYPE_INT64;
                case 4:
                    return TYPE_UINT64;
                case 5:
                    return TYPE_INT32;
                case 6:
                    return TYPE_FIXED64;
                case 7:
                    return TYPE_FIXED32;
                case 8:
                    return TYPE_BOOL;
                case 9:
                    return TYPE_STRING;
                case 10:
                    return TYPE_GROUP;
                case 11:
                    return TYPE_MESSAGE;
                case 12:
                    return TYPE_BYTES;
                case 13:
                    return TYPE_UINT32;
                case 14:
                    return TYPE_ENUM;
                case 15:
                    return TYPE_SFIXED32;
                case 16:
                    return TYPE_SFIXED64;
                case 17:
                    return TYPE_SINT32;
                case 18:
                    return TYPE_SINT64;
                default:
                    return null;
            }
        }

        public static Internal.EnumLiteMap<Field.Kind> internalGetValueMap() {
            return internalValueMap;
        }

        public static Internal.EnumVerifier internalGetVerifier() {
            return Field.Kind.KindVerifier.INSTANCE;
        }

        private Kind(int value) {
            this.value = value;
        }

        private static final class KindVerifier implements Internal.EnumVerifier {

            static final Internal.EnumVerifier INSTANCE = new Field.Kind.KindVerifier();

            @Override
            public boolean isInRange(int number) {
                return Field.Kind.forNumber(number) != null;
            }
        }
    }
}