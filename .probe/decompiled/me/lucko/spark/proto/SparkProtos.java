package me.lucko.spark.proto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import me.lucko.spark.lib.protobuf.AbstractMessageLite;
import me.lucko.spark.lib.protobuf.ByteString;
import me.lucko.spark.lib.protobuf.CodedInputStream;
import me.lucko.spark.lib.protobuf.ExtensionRegistryLite;
import me.lucko.spark.lib.protobuf.GeneratedMessageLite;
import me.lucko.spark.lib.protobuf.Internal;
import me.lucko.spark.lib.protobuf.InvalidProtocolBufferException;
import me.lucko.spark.lib.protobuf.MapEntryLite;
import me.lucko.spark.lib.protobuf.MapFieldLite;
import me.lucko.spark.lib.protobuf.MessageLiteOrBuilder;
import me.lucko.spark.lib.protobuf.Parser;
import me.lucko.spark.lib.protobuf.WireFormat;

public final class SparkProtos {

    private SparkProtos() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static final class CommandSenderMetadata extends GeneratedMessageLite<SparkProtos.CommandSenderMetadata, SparkProtos.CommandSenderMetadata.Builder> implements SparkProtos.CommandSenderMetadataOrBuilder {

        public static final int TYPE_FIELD_NUMBER = 1;

        private int type_;

        public static final int NAME_FIELD_NUMBER = 2;

        private String name_ = "";

        public static final int UNIQUE_ID_FIELD_NUMBER = 3;

        private String uniqueId_ = "";

        private static final SparkProtos.CommandSenderMetadata DEFAULT_INSTANCE;

        private static volatile Parser<SparkProtos.CommandSenderMetadata> PARSER;

        private CommandSenderMetadata() {
        }

        @Override
        public int getTypeValue() {
            return this.type_;
        }

        @Override
        public SparkProtos.CommandSenderMetadata.Type getType() {
            SparkProtos.CommandSenderMetadata.Type result = SparkProtos.CommandSenderMetadata.Type.forNumber(this.type_);
            return result == null ? SparkProtos.CommandSenderMetadata.Type.UNRECOGNIZED : result;
        }

        private void setTypeValue(int value) {
            this.type_ = value;
        }

        private void setType(SparkProtos.CommandSenderMetadata.Type value) {
            this.type_ = value.getNumber();
        }

        private void clearType() {
            this.type_ = 0;
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
        public String getUniqueId() {
            return this.uniqueId_;
        }

        @Override
        public ByteString getUniqueIdBytes() {
            return ByteString.copyFromUtf8(this.uniqueId_);
        }

        private void setUniqueId(String value) {
            Class<?> valueClass = value.getClass();
            this.uniqueId_ = value;
        }

        private void clearUniqueId() {
            this.uniqueId_ = getDefaultInstance().getUniqueId();
        }

        private void setUniqueIdBytes(ByteString value) {
            checkByteStringIsUtf8(value);
            this.uniqueId_ = value.toStringUtf8();
        }

        public static SparkProtos.CommandSenderMetadata parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.CommandSenderMetadata parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.CommandSenderMetadata parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.CommandSenderMetadata parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.CommandSenderMetadata parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.CommandSenderMetadata parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.CommandSenderMetadata parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.CommandSenderMetadata parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.CommandSenderMetadata parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.CommandSenderMetadata parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.CommandSenderMetadata parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.CommandSenderMetadata parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.CommandSenderMetadata.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkProtos.CommandSenderMetadata.Builder newBuilder(SparkProtos.CommandSenderMetadata prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:77)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.instanceOf(StructContext.java:282)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkProtos$CommandSenderMetadata.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$CommandSenderMetadata;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkProtos$CommandSenderMetadata.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkProtos$CommandSenderMetadata$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkProtos.CommandSenderMetadata();
                case NEW_BUILDER:
                    return new SparkProtos.CommandSenderMetadata.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "type_", "name_", "uniqueId_" };
                    String info = "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001\f\u0002Ȉ\u0003Ȉ";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkProtos.CommandSenderMetadata> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkProtos.CommandSenderMetadata.class) {
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

        public static SparkProtos.CommandSenderMetadata getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkProtos.CommandSenderMetadata> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkProtos.CommandSenderMetadata defaultInstance = new SparkProtos.CommandSenderMetadata();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkProtos.CommandSenderMetadata.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.CommandSenderMetadata, SparkProtos.CommandSenderMetadata.Builder> implements SparkProtos.CommandSenderMetadataOrBuilder {

            private Builder() {
                super(SparkProtos.CommandSenderMetadata.DEFAULT_INSTANCE);
            }

            @Override
            public int getTypeValue() {
                return this.instance.getTypeValue();
            }

            public SparkProtos.CommandSenderMetadata.Builder setTypeValue(int value) {
                this.copyOnWrite();
                this.instance.setTypeValue(value);
                return this;
            }

            @Override
            public SparkProtos.CommandSenderMetadata.Type getType() {
                return this.instance.getType();
            }

            public SparkProtos.CommandSenderMetadata.Builder setType(SparkProtos.CommandSenderMetadata.Type value) {
                this.copyOnWrite();
                this.instance.setType(value);
                return this;
            }

            public SparkProtos.CommandSenderMetadata.Builder clearType() {
                this.copyOnWrite();
                this.instance.clearType();
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

            public SparkProtos.CommandSenderMetadata.Builder setName(String value) {
                this.copyOnWrite();
                this.instance.setName(value);
                return this;
            }

            public SparkProtos.CommandSenderMetadata.Builder clearName() {
                this.copyOnWrite();
                this.instance.clearName();
                return this;
            }

            public SparkProtos.CommandSenderMetadata.Builder setNameBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setNameBytes(value);
                return this;
            }

            @Override
            public String getUniqueId() {
                return this.instance.getUniqueId();
            }

            @Override
            public ByteString getUniqueIdBytes() {
                return this.instance.getUniqueIdBytes();
            }

            public SparkProtos.CommandSenderMetadata.Builder setUniqueId(String value) {
                this.copyOnWrite();
                this.instance.setUniqueId(value);
                return this;
            }

            public SparkProtos.CommandSenderMetadata.Builder clearUniqueId() {
                this.copyOnWrite();
                this.instance.clearUniqueId();
                return this;
            }

            public SparkProtos.CommandSenderMetadata.Builder setUniqueIdBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setUniqueIdBytes(value);
                return this;
            }
        }

        public static enum Type implements Internal.EnumLite {

            OTHER(0), PLAYER(1), UNRECOGNIZED(-1);

            public static final int OTHER_VALUE = 0;

            public static final int PLAYER_VALUE = 1;

            private static final Internal.EnumLiteMap<SparkProtos.CommandSenderMetadata.Type> internalValueMap = new Internal.EnumLiteMap<SparkProtos.CommandSenderMetadata.Type>() {

                public SparkProtos.CommandSenderMetadata.Type findValueByNumber(int number) {
                    return SparkProtos.CommandSenderMetadata.Type.forNumber(number);
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
            public static SparkProtos.CommandSenderMetadata.Type valueOf(int value) {
                return forNumber(value);
            }

            public static SparkProtos.CommandSenderMetadata.Type forNumber(int value) {
                switch(value) {
                    case 0:
                        return OTHER;
                    case 1:
                        return PLAYER;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<SparkProtos.CommandSenderMetadata.Type> internalGetValueMap() {
                return internalValueMap;
            }

            public static Internal.EnumVerifier internalGetVerifier() {
                return SparkProtos.CommandSenderMetadata.Type.TypeVerifier.INSTANCE;
            }

            private Type(int value) {
                this.value = value;
            }

            private static final class TypeVerifier implements Internal.EnumVerifier {

                static final Internal.EnumVerifier INSTANCE = new SparkProtos.CommandSenderMetadata.Type.TypeVerifier();

                @Override
                public boolean isInRange(int number) {
                    return SparkProtos.CommandSenderMetadata.Type.forNumber(number) != null;
                }
            }
        }
    }

    public interface CommandSenderMetadataOrBuilder extends MessageLiteOrBuilder {

        int getTypeValue();

        SparkProtos.CommandSenderMetadata.Type getType();

        String getName();

        ByteString getNameBytes();

        String getUniqueId();

        ByteString getUniqueIdBytes();
    }

    public static final class PlatformMetadata extends GeneratedMessageLite<SparkProtos.PlatformMetadata, SparkProtos.PlatformMetadata.Builder> implements SparkProtos.PlatformMetadataOrBuilder {

        public static final int TYPE_FIELD_NUMBER = 1;

        private int type_;

        public static final int NAME_FIELD_NUMBER = 2;

        private String name_ = "";

        public static final int VERSION_FIELD_NUMBER = 3;

        private String version_ = "";

        public static final int MINECRAFT_VERSION_FIELD_NUMBER = 4;

        private String minecraftVersion_ = "";

        public static final int SPARK_VERSION_FIELD_NUMBER = 7;

        private int sparkVersion_;

        private static final SparkProtos.PlatformMetadata DEFAULT_INSTANCE;

        private static volatile Parser<SparkProtos.PlatformMetadata> PARSER;

        private PlatformMetadata() {
        }

        @Override
        public int getTypeValue() {
            return this.type_;
        }

        @Override
        public SparkProtos.PlatformMetadata.Type getType() {
            SparkProtos.PlatformMetadata.Type result = SparkProtos.PlatformMetadata.Type.forNumber(this.type_);
            return result == null ? SparkProtos.PlatformMetadata.Type.UNRECOGNIZED : result;
        }

        private void setTypeValue(int value) {
            this.type_ = value;
        }

        private void setType(SparkProtos.PlatformMetadata.Type value) {
            this.type_ = value.getNumber();
        }

        private void clearType() {
            this.type_ = 0;
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
        public String getMinecraftVersion() {
            return this.minecraftVersion_;
        }

        @Override
        public ByteString getMinecraftVersionBytes() {
            return ByteString.copyFromUtf8(this.minecraftVersion_);
        }

        private void setMinecraftVersion(String value) {
            Class<?> valueClass = value.getClass();
            this.minecraftVersion_ = value;
        }

        private void clearMinecraftVersion() {
            this.minecraftVersion_ = getDefaultInstance().getMinecraftVersion();
        }

        private void setMinecraftVersionBytes(ByteString value) {
            checkByteStringIsUtf8(value);
            this.minecraftVersion_ = value.toStringUtf8();
        }

        @Override
        public int getSparkVersion() {
            return this.sparkVersion_;
        }

        private void setSparkVersion(int value) {
            this.sparkVersion_ = value;
        }

        private void clearSparkVersion() {
            this.sparkVersion_ = 0;
        }

        public static SparkProtos.PlatformMetadata parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.PlatformMetadata parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.PlatformMetadata parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.PlatformMetadata parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.PlatformMetadata parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.PlatformMetadata parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.PlatformMetadata parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.PlatformMetadata parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.PlatformMetadata parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.PlatformMetadata parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.PlatformMetadata parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.PlatformMetadata parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.PlatformMetadata.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkProtos.PlatformMetadata.Builder newBuilder(SparkProtos.PlatformMetadata prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:77)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.instanceOf(StructContext.java:282)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkProtos$PlatformMetadata.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$PlatformMetadata;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkProtos$PlatformMetadata.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkProtos$PlatformMetadata$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkProtos.PlatformMetadata();
                case NEW_BUILDER:
                    return new SparkProtos.PlatformMetadata.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "type_", "name_", "version_", "minecraftVersion_", "sparkVersion_" };
                    String info = "\u0000\u0005\u0000\u0000\u0001\u0007\u0005\u0000\u0000\u0000\u0001\f\u0002Ȉ\u0003Ȉ\u0004Ȉ\u0007\u0004";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkProtos.PlatformMetadata> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkProtos.PlatformMetadata.class) {
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

        public static SparkProtos.PlatformMetadata getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkProtos.PlatformMetadata> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkProtos.PlatformMetadata defaultInstance = new SparkProtos.PlatformMetadata();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkProtos.PlatformMetadata.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.PlatformMetadata, SparkProtos.PlatformMetadata.Builder> implements SparkProtos.PlatformMetadataOrBuilder {

            private Builder() {
                super(SparkProtos.PlatformMetadata.DEFAULT_INSTANCE);
            }

            @Override
            public int getTypeValue() {
                return this.instance.getTypeValue();
            }

            public SparkProtos.PlatformMetadata.Builder setTypeValue(int value) {
                this.copyOnWrite();
                this.instance.setTypeValue(value);
                return this;
            }

            @Override
            public SparkProtos.PlatformMetadata.Type getType() {
                return this.instance.getType();
            }

            public SparkProtos.PlatformMetadata.Builder setType(SparkProtos.PlatformMetadata.Type value) {
                this.copyOnWrite();
                this.instance.setType(value);
                return this;
            }

            public SparkProtos.PlatformMetadata.Builder clearType() {
                this.copyOnWrite();
                this.instance.clearType();
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

            public SparkProtos.PlatformMetadata.Builder setName(String value) {
                this.copyOnWrite();
                this.instance.setName(value);
                return this;
            }

            public SparkProtos.PlatformMetadata.Builder clearName() {
                this.copyOnWrite();
                this.instance.clearName();
                return this;
            }

            public SparkProtos.PlatformMetadata.Builder setNameBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setNameBytes(value);
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

            public SparkProtos.PlatformMetadata.Builder setVersion(String value) {
                this.copyOnWrite();
                this.instance.setVersion(value);
                return this;
            }

            public SparkProtos.PlatformMetadata.Builder clearVersion() {
                this.copyOnWrite();
                this.instance.clearVersion();
                return this;
            }

            public SparkProtos.PlatformMetadata.Builder setVersionBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setVersionBytes(value);
                return this;
            }

            @Override
            public String getMinecraftVersion() {
                return this.instance.getMinecraftVersion();
            }

            @Override
            public ByteString getMinecraftVersionBytes() {
                return this.instance.getMinecraftVersionBytes();
            }

            public SparkProtos.PlatformMetadata.Builder setMinecraftVersion(String value) {
                this.copyOnWrite();
                this.instance.setMinecraftVersion(value);
                return this;
            }

            public SparkProtos.PlatformMetadata.Builder clearMinecraftVersion() {
                this.copyOnWrite();
                this.instance.clearMinecraftVersion();
                return this;
            }

            public SparkProtos.PlatformMetadata.Builder setMinecraftVersionBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setMinecraftVersionBytes(value);
                return this;
            }

            @Override
            public int getSparkVersion() {
                return this.instance.getSparkVersion();
            }

            public SparkProtos.PlatformMetadata.Builder setSparkVersion(int value) {
                this.copyOnWrite();
                this.instance.setSparkVersion(value);
                return this;
            }

            public SparkProtos.PlatformMetadata.Builder clearSparkVersion() {
                this.copyOnWrite();
                this.instance.clearSparkVersion();
                return this;
            }
        }

        public static enum Type implements Internal.EnumLite {

            SERVER(0), CLIENT(1), PROXY(2), UNRECOGNIZED(-1);

            public static final int SERVER_VALUE = 0;

            public static final int CLIENT_VALUE = 1;

            public static final int PROXY_VALUE = 2;

            private static final Internal.EnumLiteMap<SparkProtos.PlatformMetadata.Type> internalValueMap = new Internal.EnumLiteMap<SparkProtos.PlatformMetadata.Type>() {

                public SparkProtos.PlatformMetadata.Type findValueByNumber(int number) {
                    return SparkProtos.PlatformMetadata.Type.forNumber(number);
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
            public static SparkProtos.PlatformMetadata.Type valueOf(int value) {
                return forNumber(value);
            }

            public static SparkProtos.PlatformMetadata.Type forNumber(int value) {
                switch(value) {
                    case 0:
                        return SERVER;
                    case 1:
                        return CLIENT;
                    case 2:
                        return PROXY;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<SparkProtos.PlatformMetadata.Type> internalGetValueMap() {
                return internalValueMap;
            }

            public static Internal.EnumVerifier internalGetVerifier() {
                return SparkProtos.PlatformMetadata.Type.TypeVerifier.INSTANCE;
            }

            private Type(int value) {
                this.value = value;
            }

            private static final class TypeVerifier implements Internal.EnumVerifier {

                static final Internal.EnumVerifier INSTANCE = new SparkProtos.PlatformMetadata.Type.TypeVerifier();

                @Override
                public boolean isInRange(int number) {
                    return SparkProtos.PlatformMetadata.Type.forNumber(number) != null;
                }
            }
        }
    }

    public interface PlatformMetadataOrBuilder extends MessageLiteOrBuilder {

        int getTypeValue();

        SparkProtos.PlatformMetadata.Type getType();

        String getName();

        ByteString getNameBytes();

        String getVersion();

        ByteString getVersionBytes();

        String getMinecraftVersion();

        ByteString getMinecraftVersionBytes();

        int getSparkVersion();
    }

    public static final class PlatformStatistics extends GeneratedMessageLite<SparkProtos.PlatformStatistics, SparkProtos.PlatformStatistics.Builder> implements SparkProtos.PlatformStatisticsOrBuilder {

        public static final int MEMORY_FIELD_NUMBER = 1;

        private SparkProtos.PlatformStatistics.Memory memory_;

        public static final int GC_FIELD_NUMBER = 2;

        private MapFieldLite<String, SparkProtos.PlatformStatistics.Gc> gc_ = MapFieldLite.emptyMapField();

        public static final int UPTIME_FIELD_NUMBER = 3;

        private long uptime_;

        public static final int TPS_FIELD_NUMBER = 4;

        private SparkProtos.PlatformStatistics.Tps tps_;

        public static final int MSPT_FIELD_NUMBER = 5;

        private SparkProtos.PlatformStatistics.Mspt mspt_;

        public static final int PING_FIELD_NUMBER = 6;

        private SparkProtos.PlatformStatistics.Ping ping_;

        public static final int PLAYER_COUNT_FIELD_NUMBER = 7;

        private long playerCount_;

        public static final int WORLD_FIELD_NUMBER = 8;

        private SparkProtos.WorldStatistics world_;

        private static final SparkProtos.PlatformStatistics DEFAULT_INSTANCE;

        private static volatile Parser<SparkProtos.PlatformStatistics> PARSER;

        private PlatformStatistics() {
        }

        @Override
        public boolean hasMemory() {
            return this.memory_ != null;
        }

        @Override
        public SparkProtos.PlatformStatistics.Memory getMemory() {
            return this.memory_ == null ? SparkProtos.PlatformStatistics.Memory.getDefaultInstance() : this.memory_;
        }

        private void setMemory(SparkProtos.PlatformStatistics.Memory value) {
            value.getClass();
            this.memory_ = value;
        }

        private void mergeMemory(SparkProtos.PlatformStatistics.Memory value) {
            value.getClass();
            if (this.memory_ != null && this.memory_ != SparkProtos.PlatformStatistics.Memory.getDefaultInstance()) {
                this.memory_ = SparkProtos.PlatformStatistics.Memory.newBuilder(this.memory_).mergeFrom(value).buildPartial();
            } else {
                this.memory_ = value;
            }
        }

        private void clearMemory() {
            this.memory_ = null;
        }

        private MapFieldLite<String, SparkProtos.PlatformStatistics.Gc> internalGetGc() {
            return this.gc_;
        }

        private MapFieldLite<String, SparkProtos.PlatformStatistics.Gc> internalGetMutableGc() {
            if (!this.gc_.isMutable()) {
                this.gc_ = this.gc_.mutableCopy();
            }
            return this.gc_;
        }

        @Override
        public int getGcCount() {
            return this.internalGetGc().size();
        }

        @Override
        public boolean containsGc(String key) {
            Class<?> keyClass = key.getClass();
            return this.internalGetGc().containsKey(key);
        }

        @Deprecated
        @Override
        public Map<String, SparkProtos.PlatformStatistics.Gc> getGc() {
            return this.getGcMap();
        }

        @Override
        public Map<String, SparkProtos.PlatformStatistics.Gc> getGcMap() {
            return Collections.unmodifiableMap(this.internalGetGc());
        }

        @Override
        public SparkProtos.PlatformStatistics.Gc getGcOrDefault(String key, SparkProtos.PlatformStatistics.Gc defaultValue) {
            Class<?> keyClass = key.getClass();
            Map<String, SparkProtos.PlatformStatistics.Gc> map = this.internalGetGc();
            return map.containsKey(key) ? (SparkProtos.PlatformStatistics.Gc) map.get(key) : defaultValue;
        }

        @Override
        public SparkProtos.PlatformStatistics.Gc getGcOrThrow(String key) {
            Class<?> keyClass = key.getClass();
            Map<String, SparkProtos.PlatformStatistics.Gc> map = this.internalGetGc();
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException();
            } else {
                return (SparkProtos.PlatformStatistics.Gc) map.get(key);
            }
        }

        private Map<String, SparkProtos.PlatformStatistics.Gc> getMutableGcMap() {
            return this.internalGetMutableGc();
        }

        @Override
        public long getUptime() {
            return this.uptime_;
        }

        private void setUptime(long value) {
            this.uptime_ = value;
        }

        private void clearUptime() {
            this.uptime_ = 0L;
        }

        @Override
        public boolean hasTps() {
            return this.tps_ != null;
        }

        @Override
        public SparkProtos.PlatformStatistics.Tps getTps() {
            return this.tps_ == null ? SparkProtos.PlatformStatistics.Tps.getDefaultInstance() : this.tps_;
        }

        private void setTps(SparkProtos.PlatformStatistics.Tps value) {
            value.getClass();
            this.tps_ = value;
        }

        private void mergeTps(SparkProtos.PlatformStatistics.Tps value) {
            value.getClass();
            if (this.tps_ != null && this.tps_ != SparkProtos.PlatformStatistics.Tps.getDefaultInstance()) {
                this.tps_ = SparkProtos.PlatformStatistics.Tps.newBuilder(this.tps_).mergeFrom(value).buildPartial();
            } else {
                this.tps_ = value;
            }
        }

        private void clearTps() {
            this.tps_ = null;
        }

        @Override
        public boolean hasMspt() {
            return this.mspt_ != null;
        }

        @Override
        public SparkProtos.PlatformStatistics.Mspt getMspt() {
            return this.mspt_ == null ? SparkProtos.PlatformStatistics.Mspt.getDefaultInstance() : this.mspt_;
        }

        private void setMspt(SparkProtos.PlatformStatistics.Mspt value) {
            value.getClass();
            this.mspt_ = value;
        }

        private void mergeMspt(SparkProtos.PlatformStatistics.Mspt value) {
            value.getClass();
            if (this.mspt_ != null && this.mspt_ != SparkProtos.PlatformStatistics.Mspt.getDefaultInstance()) {
                this.mspt_ = SparkProtos.PlatformStatistics.Mspt.newBuilder(this.mspt_).mergeFrom(value).buildPartial();
            } else {
                this.mspt_ = value;
            }
        }

        private void clearMspt() {
            this.mspt_ = null;
        }

        @Override
        public boolean hasPing() {
            return this.ping_ != null;
        }

        @Override
        public SparkProtos.PlatformStatistics.Ping getPing() {
            return this.ping_ == null ? SparkProtos.PlatformStatistics.Ping.getDefaultInstance() : this.ping_;
        }

        private void setPing(SparkProtos.PlatformStatistics.Ping value) {
            value.getClass();
            this.ping_ = value;
        }

        private void mergePing(SparkProtos.PlatformStatistics.Ping value) {
            value.getClass();
            if (this.ping_ != null && this.ping_ != SparkProtos.PlatformStatistics.Ping.getDefaultInstance()) {
                this.ping_ = SparkProtos.PlatformStatistics.Ping.newBuilder(this.ping_).mergeFrom(value).buildPartial();
            } else {
                this.ping_ = value;
            }
        }

        private void clearPing() {
            this.ping_ = null;
        }

        @Override
        public long getPlayerCount() {
            return this.playerCount_;
        }

        private void setPlayerCount(long value) {
            this.playerCount_ = value;
        }

        private void clearPlayerCount() {
            this.playerCount_ = 0L;
        }

        @Override
        public boolean hasWorld() {
            return this.world_ != null;
        }

        @Override
        public SparkProtos.WorldStatistics getWorld() {
            return this.world_ == null ? SparkProtos.WorldStatistics.getDefaultInstance() : this.world_;
        }

        private void setWorld(SparkProtos.WorldStatistics value) {
            value.getClass();
            this.world_ = value;
        }

        private void mergeWorld(SparkProtos.WorldStatistics value) {
            value.getClass();
            if (this.world_ != null && this.world_ != SparkProtos.WorldStatistics.getDefaultInstance()) {
                this.world_ = SparkProtos.WorldStatistics.newBuilder(this.world_).mergeFrom(value).buildPartial();
            } else {
                this.world_ = value;
            }
        }

        private void clearWorld() {
            this.world_ = null;
        }

        public static SparkProtos.PlatformStatistics parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.PlatformStatistics parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.PlatformStatistics parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.PlatformStatistics parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.PlatformStatistics parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.PlatformStatistics parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.PlatformStatistics parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.PlatformStatistics parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.PlatformStatistics parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.PlatformStatistics parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.PlatformStatistics parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.PlatformStatistics parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.PlatformStatistics.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkProtos.PlatformStatistics.Builder newBuilder(SparkProtos.PlatformStatistics prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:77)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.instanceOf(StructContext.java:282)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkProtos$PlatformStatistics.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$PlatformStatistics;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkProtos$PlatformStatistics.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkProtos$PlatformStatistics$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkProtos.PlatformStatistics();
                case NEW_BUILDER:
                    return new SparkProtos.PlatformStatistics.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "memory_", "gc_", SparkProtos.PlatformStatistics.GcDefaultEntryHolder.defaultEntry, "uptime_", "tps_", "mspt_", "ping_", "playerCount_", "world_" };
                    String info = "\u0000\b\u0000\u0000\u0001\b\b\u0001\u0000\u0000\u0001\t\u00022\u0003\u0002\u0004\t\u0005\t\u0006\t\u0007\u0002\b\t";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkProtos.PlatformStatistics> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkProtos.PlatformStatistics.class) {
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

        public static SparkProtos.PlatformStatistics getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkProtos.PlatformStatistics> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkProtos.PlatformStatistics defaultInstance = new SparkProtos.PlatformStatistics();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkProtos.PlatformStatistics.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.PlatformStatistics, SparkProtos.PlatformStatistics.Builder> implements SparkProtos.PlatformStatisticsOrBuilder {

            private Builder() {
                super(SparkProtos.PlatformStatistics.DEFAULT_INSTANCE);
            }

            @Override
            public boolean hasMemory() {
                return this.instance.hasMemory();
            }

            @Override
            public SparkProtos.PlatformStatistics.Memory getMemory() {
                return this.instance.getMemory();
            }

            public SparkProtos.PlatformStatistics.Builder setMemory(SparkProtos.PlatformStatistics.Memory value) {
                this.copyOnWrite();
                this.instance.setMemory(value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder setMemory(SparkProtos.PlatformStatistics.Memory.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setMemory(builderForValue.build());
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder mergeMemory(SparkProtos.PlatformStatistics.Memory value) {
                this.copyOnWrite();
                this.instance.mergeMemory(value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder clearMemory() {
                this.copyOnWrite();
                this.instance.clearMemory();
                return this;
            }

            @Override
            public int getGcCount() {
                return this.instance.getGcMap().size();
            }

            @Override
            public boolean containsGc(String key) {
                Class<?> keyClass = key.getClass();
                return this.instance.getGcMap().containsKey(key);
            }

            public SparkProtos.PlatformStatistics.Builder clearGc() {
                this.copyOnWrite();
                this.instance.getMutableGcMap().clear();
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder removeGc(String key) {
                Class<?> keyClass = key.getClass();
                this.copyOnWrite();
                this.instance.getMutableGcMap().remove(key);
                return this;
            }

            @Deprecated
            @Override
            public Map<String, SparkProtos.PlatformStatistics.Gc> getGc() {
                return this.getGcMap();
            }

            @Override
            public Map<String, SparkProtos.PlatformStatistics.Gc> getGcMap() {
                return Collections.unmodifiableMap(this.instance.getGcMap());
            }

            @Override
            public SparkProtos.PlatformStatistics.Gc getGcOrDefault(String key, SparkProtos.PlatformStatistics.Gc defaultValue) {
                Class<?> keyClass = key.getClass();
                Map<String, SparkProtos.PlatformStatistics.Gc> map = this.instance.getGcMap();
                return map.containsKey(key) ? (SparkProtos.PlatformStatistics.Gc) map.get(key) : defaultValue;
            }

            @Override
            public SparkProtos.PlatformStatistics.Gc getGcOrThrow(String key) {
                Class<?> keyClass = key.getClass();
                Map<String, SparkProtos.PlatformStatistics.Gc> map = this.instance.getGcMap();
                if (!map.containsKey(key)) {
                    throw new IllegalArgumentException();
                } else {
                    return (SparkProtos.PlatformStatistics.Gc) map.get(key);
                }
            }

            public SparkProtos.PlatformStatistics.Builder putGc(String key, SparkProtos.PlatformStatistics.Gc value) {
                Class<?> keyClass = key.getClass();
                Class<?> valueClass = value.getClass();
                this.copyOnWrite();
                this.instance.getMutableGcMap().put(key, value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder putAllGc(Map<String, SparkProtos.PlatformStatistics.Gc> values) {
                this.copyOnWrite();
                this.instance.getMutableGcMap().putAll(values);
                return this;
            }

            @Override
            public long getUptime() {
                return this.instance.getUptime();
            }

            public SparkProtos.PlatformStatistics.Builder setUptime(long value) {
                this.copyOnWrite();
                this.instance.setUptime(value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder clearUptime() {
                this.copyOnWrite();
                this.instance.clearUptime();
                return this;
            }

            @Override
            public boolean hasTps() {
                return this.instance.hasTps();
            }

            @Override
            public SparkProtos.PlatformStatistics.Tps getTps() {
                return this.instance.getTps();
            }

            public SparkProtos.PlatformStatistics.Builder setTps(SparkProtos.PlatformStatistics.Tps value) {
                this.copyOnWrite();
                this.instance.setTps(value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder setTps(SparkProtos.PlatformStatistics.Tps.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setTps(builderForValue.build());
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder mergeTps(SparkProtos.PlatformStatistics.Tps value) {
                this.copyOnWrite();
                this.instance.mergeTps(value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder clearTps() {
                this.copyOnWrite();
                this.instance.clearTps();
                return this;
            }

            @Override
            public boolean hasMspt() {
                return this.instance.hasMspt();
            }

            @Override
            public SparkProtos.PlatformStatistics.Mspt getMspt() {
                return this.instance.getMspt();
            }

            public SparkProtos.PlatformStatistics.Builder setMspt(SparkProtos.PlatformStatistics.Mspt value) {
                this.copyOnWrite();
                this.instance.setMspt(value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder setMspt(SparkProtos.PlatformStatistics.Mspt.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setMspt(builderForValue.build());
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder mergeMspt(SparkProtos.PlatformStatistics.Mspt value) {
                this.copyOnWrite();
                this.instance.mergeMspt(value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder clearMspt() {
                this.copyOnWrite();
                this.instance.clearMspt();
                return this;
            }

            @Override
            public boolean hasPing() {
                return this.instance.hasPing();
            }

            @Override
            public SparkProtos.PlatformStatistics.Ping getPing() {
                return this.instance.getPing();
            }

            public SparkProtos.PlatformStatistics.Builder setPing(SparkProtos.PlatformStatistics.Ping value) {
                this.copyOnWrite();
                this.instance.setPing(value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder setPing(SparkProtos.PlatformStatistics.Ping.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setPing(builderForValue.build());
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder mergePing(SparkProtos.PlatformStatistics.Ping value) {
                this.copyOnWrite();
                this.instance.mergePing(value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder clearPing() {
                this.copyOnWrite();
                this.instance.clearPing();
                return this;
            }

            @Override
            public long getPlayerCount() {
                return this.instance.getPlayerCount();
            }

            public SparkProtos.PlatformStatistics.Builder setPlayerCount(long value) {
                this.copyOnWrite();
                this.instance.setPlayerCount(value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder clearPlayerCount() {
                this.copyOnWrite();
                this.instance.clearPlayerCount();
                return this;
            }

            @Override
            public boolean hasWorld() {
                return this.instance.hasWorld();
            }

            @Override
            public SparkProtos.WorldStatistics getWorld() {
                return this.instance.getWorld();
            }

            public SparkProtos.PlatformStatistics.Builder setWorld(SparkProtos.WorldStatistics value) {
                this.copyOnWrite();
                this.instance.setWorld(value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder setWorld(SparkProtos.WorldStatistics.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setWorld(builderForValue.build());
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder mergeWorld(SparkProtos.WorldStatistics value) {
                this.copyOnWrite();
                this.instance.mergeWorld(value);
                return this;
            }

            public SparkProtos.PlatformStatistics.Builder clearWorld() {
                this.copyOnWrite();
                this.instance.clearWorld();
                return this;
            }
        }

        public static final class Gc extends GeneratedMessageLite<SparkProtos.PlatformStatistics.Gc, SparkProtos.PlatformStatistics.Gc.Builder> implements SparkProtos.PlatformStatistics.GcOrBuilder {

            public static final int TOTAL_FIELD_NUMBER = 1;

            private long total_;

            public static final int AVG_TIME_FIELD_NUMBER = 2;

            private double avgTime_;

            public static final int AVG_FREQUENCY_FIELD_NUMBER = 3;

            private double avgFrequency_;

            private static final SparkProtos.PlatformStatistics.Gc DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.PlatformStatistics.Gc> PARSER;

            private Gc() {
            }

            @Override
            public long getTotal() {
                return this.total_;
            }

            private void setTotal(long value) {
                this.total_ = value;
            }

            private void clearTotal() {
                this.total_ = 0L;
            }

            @Override
            public double getAvgTime() {
                return this.avgTime_;
            }

            private void setAvgTime(double value) {
                this.avgTime_ = value;
            }

            private void clearAvgTime() {
                this.avgTime_ = 0.0;
            }

            @Override
            public double getAvgFrequency() {
                return this.avgFrequency_;
            }

            private void setAvgFrequency(double value) {
                this.avgFrequency_ = value;
            }

            private void clearAvgFrequency() {
                this.avgFrequency_ = 0.0;
            }

            public static SparkProtos.PlatformStatistics.Gc parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Gc parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Gc parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Gc parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Gc parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Gc parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Gc parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Gc parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Gc parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Gc parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Gc parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Gc parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Gc.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.PlatformStatistics.Gc.Builder newBuilder(SparkProtos.PlatformStatistics.Gc prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$PlatformStatistics$Gc.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$PlatformStatistics$Gc;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$PlatformStatistics$Gc.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$PlatformStatistics$Gc$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.PlatformStatistics.Gc();
                    case NEW_BUILDER:
                        return new SparkProtos.PlatformStatistics.Gc.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "total_", "avgTime_", "avgFrequency_" };
                        String info = "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001\u0002\u0002\u0000\u0003\u0000";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.PlatformStatistics.Gc> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.PlatformStatistics.Gc.class) {
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

            public static SparkProtos.PlatformStatistics.Gc getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.PlatformStatistics.Gc> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.PlatformStatistics.Gc defaultInstance = new SparkProtos.PlatformStatistics.Gc();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.PlatformStatistics.Gc.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.PlatformStatistics.Gc, SparkProtos.PlatformStatistics.Gc.Builder> implements SparkProtos.PlatformStatistics.GcOrBuilder {

                private Builder() {
                    super(SparkProtos.PlatformStatistics.Gc.DEFAULT_INSTANCE);
                }

                @Override
                public long getTotal() {
                    return this.instance.getTotal();
                }

                public SparkProtos.PlatformStatistics.Gc.Builder setTotal(long value) {
                    this.copyOnWrite();
                    this.instance.setTotal(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Gc.Builder clearTotal() {
                    this.copyOnWrite();
                    this.instance.clearTotal();
                    return this;
                }

                @Override
                public double getAvgTime() {
                    return this.instance.getAvgTime();
                }

                public SparkProtos.PlatformStatistics.Gc.Builder setAvgTime(double value) {
                    this.copyOnWrite();
                    this.instance.setAvgTime(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Gc.Builder clearAvgTime() {
                    this.copyOnWrite();
                    this.instance.clearAvgTime();
                    return this;
                }

                @Override
                public double getAvgFrequency() {
                    return this.instance.getAvgFrequency();
                }

                public SparkProtos.PlatformStatistics.Gc.Builder setAvgFrequency(double value) {
                    this.copyOnWrite();
                    this.instance.setAvgFrequency(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Gc.Builder clearAvgFrequency() {
                    this.copyOnWrite();
                    this.instance.clearAvgFrequency();
                    return this;
                }
            }
        }

        private static final class GcDefaultEntryHolder {

            static final MapEntryLite<String, SparkProtos.PlatformStatistics.Gc> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.STRING, "", WireFormat.FieldType.MESSAGE, SparkProtos.PlatformStatistics.Gc.getDefaultInstance());
        }

        public interface GcOrBuilder extends MessageLiteOrBuilder {

            long getTotal();

            double getAvgTime();

            double getAvgFrequency();
        }

        public static final class Memory extends GeneratedMessageLite<SparkProtos.PlatformStatistics.Memory, SparkProtos.PlatformStatistics.Memory.Builder> implements SparkProtos.PlatformStatistics.MemoryOrBuilder {

            public static final int HEAP_FIELD_NUMBER = 1;

            private SparkProtos.PlatformStatistics.Memory.MemoryPool heap_;

            private static final SparkProtos.PlatformStatistics.Memory DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.PlatformStatistics.Memory> PARSER;

            private Memory() {
            }

            @Override
            public boolean hasHeap() {
                return this.heap_ != null;
            }

            @Override
            public SparkProtos.PlatformStatistics.Memory.MemoryPool getHeap() {
                return this.heap_ == null ? SparkProtos.PlatformStatistics.Memory.MemoryPool.getDefaultInstance() : this.heap_;
            }

            private void setHeap(SparkProtos.PlatformStatistics.Memory.MemoryPool value) {
                value.getClass();
                this.heap_ = value;
            }

            private void mergeHeap(SparkProtos.PlatformStatistics.Memory.MemoryPool value) {
                value.getClass();
                if (this.heap_ != null && this.heap_ != SparkProtos.PlatformStatistics.Memory.MemoryPool.getDefaultInstance()) {
                    this.heap_ = SparkProtos.PlatformStatistics.Memory.MemoryPool.newBuilder(this.heap_).mergeFrom(value).buildPartial();
                } else {
                    this.heap_ = value;
                }
            }

            private void clearHeap() {
                this.heap_ = null;
            }

            public static SparkProtos.PlatformStatistics.Memory parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Memory parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Memory parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Memory parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Memory parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Memory parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Memory parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Memory parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Memory parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Memory parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Memory parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Memory parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Memory.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.PlatformStatistics.Memory.Builder newBuilder(SparkProtos.PlatformStatistics.Memory prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$PlatformStatistics$Memory.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$PlatformStatistics$Memory;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$PlatformStatistics$Memory.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$PlatformStatistics$Memory$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.PlatformStatistics.Memory();
                    case NEW_BUILDER:
                        return new SparkProtos.PlatformStatistics.Memory.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "heap_" };
                        String info = "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\t";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.PlatformStatistics.Memory> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.PlatformStatistics.Memory.class) {
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

            public static SparkProtos.PlatformStatistics.Memory getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.PlatformStatistics.Memory> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.PlatformStatistics.Memory defaultInstance = new SparkProtos.PlatformStatistics.Memory();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.PlatformStatistics.Memory.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.PlatformStatistics.Memory, SparkProtos.PlatformStatistics.Memory.Builder> implements SparkProtos.PlatformStatistics.MemoryOrBuilder {

                private Builder() {
                    super(SparkProtos.PlatformStatistics.Memory.DEFAULT_INSTANCE);
                }

                @Override
                public boolean hasHeap() {
                    return this.instance.hasHeap();
                }

                @Override
                public SparkProtos.PlatformStatistics.Memory.MemoryPool getHeap() {
                    return this.instance.getHeap();
                }

                public SparkProtos.PlatformStatistics.Memory.Builder setHeap(SparkProtos.PlatformStatistics.Memory.MemoryPool value) {
                    this.copyOnWrite();
                    this.instance.setHeap(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Memory.Builder setHeap(SparkProtos.PlatformStatistics.Memory.MemoryPool.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setHeap(builderForValue.build());
                    return this;
                }

                public SparkProtos.PlatformStatistics.Memory.Builder mergeHeap(SparkProtos.PlatformStatistics.Memory.MemoryPool value) {
                    this.copyOnWrite();
                    this.instance.mergeHeap(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Memory.Builder clearHeap() {
                    this.copyOnWrite();
                    this.instance.clearHeap();
                    return this;
                }
            }

            public static final class MemoryPool extends GeneratedMessageLite<SparkProtos.PlatformStatistics.Memory.MemoryPool, SparkProtos.PlatformStatistics.Memory.MemoryPool.Builder> implements SparkProtos.PlatformStatistics.Memory.MemoryPoolOrBuilder {

                public static final int USED_FIELD_NUMBER = 1;

                private long used_;

                public static final int TOTAL_FIELD_NUMBER = 2;

                private long total_;

                private static final SparkProtos.PlatformStatistics.Memory.MemoryPool DEFAULT_INSTANCE;

                private static volatile Parser<SparkProtos.PlatformStatistics.Memory.MemoryPool> PARSER;

                private MemoryPool() {
                }

                @Override
                public long getUsed() {
                    return this.used_;
                }

                private void setUsed(long value) {
                    this.used_ = value;
                }

                private void clearUsed() {
                    this.used_ = 0L;
                }

                @Override
                public long getTotal() {
                    return this.total_;
                }

                private void setTotal(long value) {
                    this.total_ = value;
                }

                private void clearTotal() {
                    this.total_ = 0L;
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool parseFrom(ByteString data) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool parseFrom(byte[] data) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool parseFrom(InputStream input) throws IOException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool parseDelimitedFrom(InputStream input) throws IOException {
                    return parseDelimitedFrom(DEFAULT_INSTANCE, input);
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool parseFrom(CodedInputStream input) throws IOException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool.Builder newBuilder() {
                    return DEFAULT_INSTANCE.createBuilder();
                }

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool.Builder newBuilder(SparkProtos.PlatformStatistics.Memory.MemoryPool prototype) {
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
                    // 0: getstatic me/lucko/spark/proto/SparkProtos$PlatformStatistics$Memory$MemoryPool.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$PlatformStatistics$Memory$MemoryPool;
                    // 3: aload 0
                    // 4: invokevirtual me/lucko/spark/proto/SparkProtos$PlatformStatistics$Memory$MemoryPool.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                    // 7: checkcast me/lucko/spark/proto/SparkProtos$PlatformStatistics$Memory$MemoryPool$Builder
                    // a: areturn
                }

                @Override
                protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                    switch(method) {
                        case NEW_MUTABLE_INSTANCE:
                            return new SparkProtos.PlatformStatistics.Memory.MemoryPool();
                        case NEW_BUILDER:
                            return new SparkProtos.PlatformStatistics.Memory.MemoryPool.Builder();
                        case BUILD_MESSAGE_INFO:
                            Object[] objects = new Object[] { "used_", "total_" };
                            String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\u0002\u0002\u0002";
                            return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                        case GET_DEFAULT_INSTANCE:
                            return DEFAULT_INSTANCE;
                        case GET_PARSER:
                            Parser<SparkProtos.PlatformStatistics.Memory.MemoryPool> parser = PARSER;
                            if (parser == null) {
                                synchronized (SparkProtos.PlatformStatistics.Memory.MemoryPool.class) {
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

                public static SparkProtos.PlatformStatistics.Memory.MemoryPool getDefaultInstance() {
                    return DEFAULT_INSTANCE;
                }

                public static Parser<SparkProtos.PlatformStatistics.Memory.MemoryPool> parser() {
                    return DEFAULT_INSTANCE.getParserForType();
                }

                static {
                    SparkProtos.PlatformStatistics.Memory.MemoryPool defaultInstance = new SparkProtos.PlatformStatistics.Memory.MemoryPool();
                    DEFAULT_INSTANCE = defaultInstance;
                    GeneratedMessageLite.registerDefaultInstance(SparkProtos.PlatformStatistics.Memory.MemoryPool.class, defaultInstance);
                }

                public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.PlatformStatistics.Memory.MemoryPool, SparkProtos.PlatformStatistics.Memory.MemoryPool.Builder> implements SparkProtos.PlatformStatistics.Memory.MemoryPoolOrBuilder {

                    private Builder() {
                        super(SparkProtos.PlatformStatistics.Memory.MemoryPool.DEFAULT_INSTANCE);
                    }

                    @Override
                    public long getUsed() {
                        return this.instance.getUsed();
                    }

                    public SparkProtos.PlatformStatistics.Memory.MemoryPool.Builder setUsed(long value) {
                        this.copyOnWrite();
                        this.instance.setUsed(value);
                        return this;
                    }

                    public SparkProtos.PlatformStatistics.Memory.MemoryPool.Builder clearUsed() {
                        this.copyOnWrite();
                        this.instance.clearUsed();
                        return this;
                    }

                    @Override
                    public long getTotal() {
                        return this.instance.getTotal();
                    }

                    public SparkProtos.PlatformStatistics.Memory.MemoryPool.Builder setTotal(long value) {
                        this.copyOnWrite();
                        this.instance.setTotal(value);
                        return this;
                    }

                    public SparkProtos.PlatformStatistics.Memory.MemoryPool.Builder clearTotal() {
                        this.copyOnWrite();
                        this.instance.clearTotal();
                        return this;
                    }
                }
            }

            public interface MemoryPoolOrBuilder extends MessageLiteOrBuilder {

                long getUsed();

                long getTotal();
            }
        }

        public interface MemoryOrBuilder extends MessageLiteOrBuilder {

            boolean hasHeap();

            SparkProtos.PlatformStatistics.Memory.MemoryPool getHeap();
        }

        public static final class Mspt extends GeneratedMessageLite<SparkProtos.PlatformStatistics.Mspt, SparkProtos.PlatformStatistics.Mspt.Builder> implements SparkProtos.PlatformStatistics.MsptOrBuilder {

            public static final int LAST1M_FIELD_NUMBER = 1;

            private SparkProtos.RollingAverageValues last1M_;

            public static final int LAST5M_FIELD_NUMBER = 2;

            private SparkProtos.RollingAverageValues last5M_;

            private static final SparkProtos.PlatformStatistics.Mspt DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.PlatformStatistics.Mspt> PARSER;

            private Mspt() {
            }

            @Override
            public boolean hasLast1M() {
                return this.last1M_ != null;
            }

            @Override
            public SparkProtos.RollingAverageValues getLast1M() {
                return this.last1M_ == null ? SparkProtos.RollingAverageValues.getDefaultInstance() : this.last1M_;
            }

            private void setLast1M(SparkProtos.RollingAverageValues value) {
                value.getClass();
                this.last1M_ = value;
            }

            private void mergeLast1M(SparkProtos.RollingAverageValues value) {
                value.getClass();
                if (this.last1M_ != null && this.last1M_ != SparkProtos.RollingAverageValues.getDefaultInstance()) {
                    this.last1M_ = SparkProtos.RollingAverageValues.newBuilder(this.last1M_).mergeFrom(value).buildPartial();
                } else {
                    this.last1M_ = value;
                }
            }

            private void clearLast1M() {
                this.last1M_ = null;
            }

            @Override
            public boolean hasLast5M() {
                return this.last5M_ != null;
            }

            @Override
            public SparkProtos.RollingAverageValues getLast5M() {
                return this.last5M_ == null ? SparkProtos.RollingAverageValues.getDefaultInstance() : this.last5M_;
            }

            private void setLast5M(SparkProtos.RollingAverageValues value) {
                value.getClass();
                this.last5M_ = value;
            }

            private void mergeLast5M(SparkProtos.RollingAverageValues value) {
                value.getClass();
                if (this.last5M_ != null && this.last5M_ != SparkProtos.RollingAverageValues.getDefaultInstance()) {
                    this.last5M_ = SparkProtos.RollingAverageValues.newBuilder(this.last5M_).mergeFrom(value).buildPartial();
                } else {
                    this.last5M_ = value;
                }
            }

            private void clearLast5M() {
                this.last5M_ = null;
            }

            public static SparkProtos.PlatformStatistics.Mspt parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Mspt parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Mspt parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Mspt parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Mspt parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Mspt parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Mspt parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Mspt parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Mspt parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Mspt parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Mspt parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Mspt parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Mspt.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.PlatformStatistics.Mspt.Builder newBuilder(SparkProtos.PlatformStatistics.Mspt prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$PlatformStatistics$Mspt.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$PlatformStatistics$Mspt;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$PlatformStatistics$Mspt.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$PlatformStatistics$Mspt$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.PlatformStatistics.Mspt();
                    case NEW_BUILDER:
                        return new SparkProtos.PlatformStatistics.Mspt.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "last1M_", "last5M_" };
                        String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\t\u0002\t";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.PlatformStatistics.Mspt> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.PlatformStatistics.Mspt.class) {
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

            public static SparkProtos.PlatformStatistics.Mspt getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.PlatformStatistics.Mspt> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.PlatformStatistics.Mspt defaultInstance = new SparkProtos.PlatformStatistics.Mspt();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.PlatformStatistics.Mspt.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.PlatformStatistics.Mspt, SparkProtos.PlatformStatistics.Mspt.Builder> implements SparkProtos.PlatformStatistics.MsptOrBuilder {

                private Builder() {
                    super(SparkProtos.PlatformStatistics.Mspt.DEFAULT_INSTANCE);
                }

                @Override
                public boolean hasLast1M() {
                    return this.instance.hasLast1M();
                }

                @Override
                public SparkProtos.RollingAverageValues getLast1M() {
                    return this.instance.getLast1M();
                }

                public SparkProtos.PlatformStatistics.Mspt.Builder setLast1M(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.setLast1M(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Mspt.Builder setLast1M(SparkProtos.RollingAverageValues.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setLast1M(builderForValue.build());
                    return this;
                }

                public SparkProtos.PlatformStatistics.Mspt.Builder mergeLast1M(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.mergeLast1M(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Mspt.Builder clearLast1M() {
                    this.copyOnWrite();
                    this.instance.clearLast1M();
                    return this;
                }

                @Override
                public boolean hasLast5M() {
                    return this.instance.hasLast5M();
                }

                @Override
                public SparkProtos.RollingAverageValues getLast5M() {
                    return this.instance.getLast5M();
                }

                public SparkProtos.PlatformStatistics.Mspt.Builder setLast5M(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.setLast5M(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Mspt.Builder setLast5M(SparkProtos.RollingAverageValues.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setLast5M(builderForValue.build());
                    return this;
                }

                public SparkProtos.PlatformStatistics.Mspt.Builder mergeLast5M(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.mergeLast5M(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Mspt.Builder clearLast5M() {
                    this.copyOnWrite();
                    this.instance.clearLast5M();
                    return this;
                }
            }
        }

        public interface MsptOrBuilder extends MessageLiteOrBuilder {

            boolean hasLast1M();

            SparkProtos.RollingAverageValues getLast1M();

            boolean hasLast5M();

            SparkProtos.RollingAverageValues getLast5M();
        }

        public static final class Ping extends GeneratedMessageLite<SparkProtos.PlatformStatistics.Ping, SparkProtos.PlatformStatistics.Ping.Builder> implements SparkProtos.PlatformStatistics.PingOrBuilder {

            public static final int LAST15M_FIELD_NUMBER = 1;

            private SparkProtos.RollingAverageValues last15M_;

            private static final SparkProtos.PlatformStatistics.Ping DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.PlatformStatistics.Ping> PARSER;

            private Ping() {
            }

            @Override
            public boolean hasLast15M() {
                return this.last15M_ != null;
            }

            @Override
            public SparkProtos.RollingAverageValues getLast15M() {
                return this.last15M_ == null ? SparkProtos.RollingAverageValues.getDefaultInstance() : this.last15M_;
            }

            private void setLast15M(SparkProtos.RollingAverageValues value) {
                value.getClass();
                this.last15M_ = value;
            }

            private void mergeLast15M(SparkProtos.RollingAverageValues value) {
                value.getClass();
                if (this.last15M_ != null && this.last15M_ != SparkProtos.RollingAverageValues.getDefaultInstance()) {
                    this.last15M_ = SparkProtos.RollingAverageValues.newBuilder(this.last15M_).mergeFrom(value).buildPartial();
                } else {
                    this.last15M_ = value;
                }
            }

            private void clearLast15M() {
                this.last15M_ = null;
            }

            public static SparkProtos.PlatformStatistics.Ping parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Ping parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Ping parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Ping parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Ping parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Ping parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Ping parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Ping parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Ping parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Ping parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Ping parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Ping parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Ping.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.PlatformStatistics.Ping.Builder newBuilder(SparkProtos.PlatformStatistics.Ping prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$PlatformStatistics$Ping.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$PlatformStatistics$Ping;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$PlatformStatistics$Ping.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$PlatformStatistics$Ping$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.PlatformStatistics.Ping();
                    case NEW_BUILDER:
                        return new SparkProtos.PlatformStatistics.Ping.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "last15M_" };
                        String info = "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\t";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.PlatformStatistics.Ping> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.PlatformStatistics.Ping.class) {
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

            public static SparkProtos.PlatformStatistics.Ping getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.PlatformStatistics.Ping> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.PlatformStatistics.Ping defaultInstance = new SparkProtos.PlatformStatistics.Ping();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.PlatformStatistics.Ping.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.PlatformStatistics.Ping, SparkProtos.PlatformStatistics.Ping.Builder> implements SparkProtos.PlatformStatistics.PingOrBuilder {

                private Builder() {
                    super(SparkProtos.PlatformStatistics.Ping.DEFAULT_INSTANCE);
                }

                @Override
                public boolean hasLast15M() {
                    return this.instance.hasLast15M();
                }

                @Override
                public SparkProtos.RollingAverageValues getLast15M() {
                    return this.instance.getLast15M();
                }

                public SparkProtos.PlatformStatistics.Ping.Builder setLast15M(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.setLast15M(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Ping.Builder setLast15M(SparkProtos.RollingAverageValues.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setLast15M(builderForValue.build());
                    return this;
                }

                public SparkProtos.PlatformStatistics.Ping.Builder mergeLast15M(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.mergeLast15M(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Ping.Builder clearLast15M() {
                    this.copyOnWrite();
                    this.instance.clearLast15M();
                    return this;
                }
            }
        }

        public interface PingOrBuilder extends MessageLiteOrBuilder {

            boolean hasLast15M();

            SparkProtos.RollingAverageValues getLast15M();
        }

        public static final class Tps extends GeneratedMessageLite<SparkProtos.PlatformStatistics.Tps, SparkProtos.PlatformStatistics.Tps.Builder> implements SparkProtos.PlatformStatistics.TpsOrBuilder {

            public static final int LAST1M_FIELD_NUMBER = 1;

            private double last1M_;

            public static final int LAST5M_FIELD_NUMBER = 2;

            private double last5M_;

            public static final int LAST15M_FIELD_NUMBER = 3;

            private double last15M_;

            private static final SparkProtos.PlatformStatistics.Tps DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.PlatformStatistics.Tps> PARSER;

            private Tps() {
            }

            @Override
            public double getLast1M() {
                return this.last1M_;
            }

            private void setLast1M(double value) {
                this.last1M_ = value;
            }

            private void clearLast1M() {
                this.last1M_ = 0.0;
            }

            @Override
            public double getLast5M() {
                return this.last5M_;
            }

            private void setLast5M(double value) {
                this.last5M_ = value;
            }

            private void clearLast5M() {
                this.last5M_ = 0.0;
            }

            @Override
            public double getLast15M() {
                return this.last15M_;
            }

            private void setLast15M(double value) {
                this.last15M_ = value;
            }

            private void clearLast15M() {
                this.last15M_ = 0.0;
            }

            public static SparkProtos.PlatformStatistics.Tps parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Tps parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Tps parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Tps parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Tps parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.PlatformStatistics.Tps parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Tps parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Tps parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Tps parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Tps parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Tps parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.PlatformStatistics.Tps parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.PlatformStatistics.Tps.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.PlatformStatistics.Tps.Builder newBuilder(SparkProtos.PlatformStatistics.Tps prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$PlatformStatistics$Tps.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$PlatformStatistics$Tps;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$PlatformStatistics$Tps.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$PlatformStatistics$Tps$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.PlatformStatistics.Tps();
                    case NEW_BUILDER:
                        return new SparkProtos.PlatformStatistics.Tps.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "last1M_", "last5M_", "last15M_" };
                        String info = "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001\u0000\u0002\u0000\u0003\u0000";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.PlatformStatistics.Tps> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.PlatformStatistics.Tps.class) {
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

            public static SparkProtos.PlatformStatistics.Tps getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.PlatformStatistics.Tps> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.PlatformStatistics.Tps defaultInstance = new SparkProtos.PlatformStatistics.Tps();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.PlatformStatistics.Tps.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.PlatformStatistics.Tps, SparkProtos.PlatformStatistics.Tps.Builder> implements SparkProtos.PlatformStatistics.TpsOrBuilder {

                private Builder() {
                    super(SparkProtos.PlatformStatistics.Tps.DEFAULT_INSTANCE);
                }

                @Override
                public double getLast1M() {
                    return this.instance.getLast1M();
                }

                public SparkProtos.PlatformStatistics.Tps.Builder setLast1M(double value) {
                    this.copyOnWrite();
                    this.instance.setLast1M(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Tps.Builder clearLast1M() {
                    this.copyOnWrite();
                    this.instance.clearLast1M();
                    return this;
                }

                @Override
                public double getLast5M() {
                    return this.instance.getLast5M();
                }

                public SparkProtos.PlatformStatistics.Tps.Builder setLast5M(double value) {
                    this.copyOnWrite();
                    this.instance.setLast5M(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Tps.Builder clearLast5M() {
                    this.copyOnWrite();
                    this.instance.clearLast5M();
                    return this;
                }

                @Override
                public double getLast15M() {
                    return this.instance.getLast15M();
                }

                public SparkProtos.PlatformStatistics.Tps.Builder setLast15M(double value) {
                    this.copyOnWrite();
                    this.instance.setLast15M(value);
                    return this;
                }

                public SparkProtos.PlatformStatistics.Tps.Builder clearLast15M() {
                    this.copyOnWrite();
                    this.instance.clearLast15M();
                    return this;
                }
            }
        }

        public interface TpsOrBuilder extends MessageLiteOrBuilder {

            double getLast1M();

            double getLast5M();

            double getLast15M();
        }
    }

    public interface PlatformStatisticsOrBuilder extends MessageLiteOrBuilder {

        boolean hasMemory();

        SparkProtos.PlatformStatistics.Memory getMemory();

        int getGcCount();

        boolean containsGc(String var1);

        @Deprecated
        Map<String, SparkProtos.PlatformStatistics.Gc> getGc();

        Map<String, SparkProtos.PlatformStatistics.Gc> getGcMap();

        SparkProtos.PlatformStatistics.Gc getGcOrDefault(String var1, SparkProtos.PlatformStatistics.Gc var2);

        SparkProtos.PlatformStatistics.Gc getGcOrThrow(String var1);

        long getUptime();

        boolean hasTps();

        SparkProtos.PlatformStatistics.Tps getTps();

        boolean hasMspt();

        SparkProtos.PlatformStatistics.Mspt getMspt();

        boolean hasPing();

        SparkProtos.PlatformStatistics.Ping getPing();

        long getPlayerCount();

        boolean hasWorld();

        SparkProtos.WorldStatistics getWorld();
    }

    public static final class RollingAverageValues extends GeneratedMessageLite<SparkProtos.RollingAverageValues, SparkProtos.RollingAverageValues.Builder> implements SparkProtos.RollingAverageValuesOrBuilder {

        public static final int MEAN_FIELD_NUMBER = 1;

        private double mean_;

        public static final int MAX_FIELD_NUMBER = 2;

        private double max_;

        public static final int MIN_FIELD_NUMBER = 3;

        private double min_;

        public static final int MEDIAN_FIELD_NUMBER = 4;

        private double median_;

        public static final int PERCENTILE95_FIELD_NUMBER = 5;

        private double percentile95_;

        private static final SparkProtos.RollingAverageValues DEFAULT_INSTANCE;

        private static volatile Parser<SparkProtos.RollingAverageValues> PARSER;

        private RollingAverageValues() {
        }

        @Override
        public double getMean() {
            return this.mean_;
        }

        private void setMean(double value) {
            this.mean_ = value;
        }

        private void clearMean() {
            this.mean_ = 0.0;
        }

        @Override
        public double getMax() {
            return this.max_;
        }

        private void setMax(double value) {
            this.max_ = value;
        }

        private void clearMax() {
            this.max_ = 0.0;
        }

        @Override
        public double getMin() {
            return this.min_;
        }

        private void setMin(double value) {
            this.min_ = value;
        }

        private void clearMin() {
            this.min_ = 0.0;
        }

        @Override
        public double getMedian() {
            return this.median_;
        }

        private void setMedian(double value) {
            this.median_ = value;
        }

        private void clearMedian() {
            this.median_ = 0.0;
        }

        @Override
        public double getPercentile95() {
            return this.percentile95_;
        }

        private void setPercentile95(double value) {
            this.percentile95_ = value;
        }

        private void clearPercentile95() {
            this.percentile95_ = 0.0;
        }

        public static SparkProtos.RollingAverageValues parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.RollingAverageValues parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.RollingAverageValues parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.RollingAverageValues parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.RollingAverageValues parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.RollingAverageValues parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.RollingAverageValues parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.RollingAverageValues parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.RollingAverageValues parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.RollingAverageValues parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.RollingAverageValues parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.RollingAverageValues parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.RollingAverageValues.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkProtos.RollingAverageValues.Builder newBuilder(SparkProtos.RollingAverageValues prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:77)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.instanceOf(StructContext.java:282)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkProtos$RollingAverageValues.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$RollingAverageValues;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkProtos$RollingAverageValues.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkProtos$RollingAverageValues$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkProtos.RollingAverageValues();
                case NEW_BUILDER:
                    return new SparkProtos.RollingAverageValues.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "mean_", "max_", "min_", "median_", "percentile95_" };
                    String info = "\u0000\u0005\u0000\u0000\u0001\u0005\u0005\u0000\u0000\u0000\u0001\u0000\u0002\u0000\u0003\u0000\u0004\u0000\u0005\u0000";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkProtos.RollingAverageValues> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkProtos.RollingAverageValues.class) {
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

        public static SparkProtos.RollingAverageValues getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkProtos.RollingAverageValues> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkProtos.RollingAverageValues defaultInstance = new SparkProtos.RollingAverageValues();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkProtos.RollingAverageValues.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.RollingAverageValues, SparkProtos.RollingAverageValues.Builder> implements SparkProtos.RollingAverageValuesOrBuilder {

            private Builder() {
                super(SparkProtos.RollingAverageValues.DEFAULT_INSTANCE);
            }

            @Override
            public double getMean() {
                return this.instance.getMean();
            }

            public SparkProtos.RollingAverageValues.Builder setMean(double value) {
                this.copyOnWrite();
                this.instance.setMean(value);
                return this;
            }

            public SparkProtos.RollingAverageValues.Builder clearMean() {
                this.copyOnWrite();
                this.instance.clearMean();
                return this;
            }

            @Override
            public double getMax() {
                return this.instance.getMax();
            }

            public SparkProtos.RollingAverageValues.Builder setMax(double value) {
                this.copyOnWrite();
                this.instance.setMax(value);
                return this;
            }

            public SparkProtos.RollingAverageValues.Builder clearMax() {
                this.copyOnWrite();
                this.instance.clearMax();
                return this;
            }

            @Override
            public double getMin() {
                return this.instance.getMin();
            }

            public SparkProtos.RollingAverageValues.Builder setMin(double value) {
                this.copyOnWrite();
                this.instance.setMin(value);
                return this;
            }

            public SparkProtos.RollingAverageValues.Builder clearMin() {
                this.copyOnWrite();
                this.instance.clearMin();
                return this;
            }

            @Override
            public double getMedian() {
                return this.instance.getMedian();
            }

            public SparkProtos.RollingAverageValues.Builder setMedian(double value) {
                this.copyOnWrite();
                this.instance.setMedian(value);
                return this;
            }

            public SparkProtos.RollingAverageValues.Builder clearMedian() {
                this.copyOnWrite();
                this.instance.clearMedian();
                return this;
            }

            @Override
            public double getPercentile95() {
                return this.instance.getPercentile95();
            }

            public SparkProtos.RollingAverageValues.Builder setPercentile95(double value) {
                this.copyOnWrite();
                this.instance.setPercentile95(value);
                return this;
            }

            public SparkProtos.RollingAverageValues.Builder clearPercentile95() {
                this.copyOnWrite();
                this.instance.clearPercentile95();
                return this;
            }
        }
    }

    public interface RollingAverageValuesOrBuilder extends MessageLiteOrBuilder {

        double getMean();

        double getMax();

        double getMin();

        double getMedian();

        double getPercentile95();
    }

    public static final class SystemStatistics extends GeneratedMessageLite<SparkProtos.SystemStatistics, SparkProtos.SystemStatistics.Builder> implements SparkProtos.SystemStatisticsOrBuilder {

        public static final int CPU_FIELD_NUMBER = 1;

        private SparkProtos.SystemStatistics.Cpu cpu_;

        public static final int MEMORY_FIELD_NUMBER = 2;

        private SparkProtos.SystemStatistics.Memory memory_;

        public static final int GC_FIELD_NUMBER = 3;

        private MapFieldLite<String, SparkProtos.SystemStatistics.Gc> gc_ = MapFieldLite.emptyMapField();

        public static final int DISK_FIELD_NUMBER = 4;

        private SparkProtos.SystemStatistics.Disk disk_;

        public static final int OS_FIELD_NUMBER = 5;

        private SparkProtos.SystemStatistics.Os os_;

        public static final int JAVA_FIELD_NUMBER = 6;

        private SparkProtos.SystemStatistics.Java java_;

        public static final int UPTIME_FIELD_NUMBER = 7;

        private long uptime_;

        public static final int NET_FIELD_NUMBER = 8;

        private MapFieldLite<String, SparkProtos.SystemStatistics.NetInterface> net_ = MapFieldLite.emptyMapField();

        private static final SparkProtos.SystemStatistics DEFAULT_INSTANCE;

        private static volatile Parser<SparkProtos.SystemStatistics> PARSER;

        private SystemStatistics() {
        }

        @Override
        public boolean hasCpu() {
            return this.cpu_ != null;
        }

        @Override
        public SparkProtos.SystemStatistics.Cpu getCpu() {
            return this.cpu_ == null ? SparkProtos.SystemStatistics.Cpu.getDefaultInstance() : this.cpu_;
        }

        private void setCpu(SparkProtos.SystemStatistics.Cpu value) {
            value.getClass();
            this.cpu_ = value;
        }

        private void mergeCpu(SparkProtos.SystemStatistics.Cpu value) {
            value.getClass();
            if (this.cpu_ != null && this.cpu_ != SparkProtos.SystemStatistics.Cpu.getDefaultInstance()) {
                this.cpu_ = SparkProtos.SystemStatistics.Cpu.newBuilder(this.cpu_).mergeFrom(value).buildPartial();
            } else {
                this.cpu_ = value;
            }
        }

        private void clearCpu() {
            this.cpu_ = null;
        }

        @Override
        public boolean hasMemory() {
            return this.memory_ != null;
        }

        @Override
        public SparkProtos.SystemStatistics.Memory getMemory() {
            return this.memory_ == null ? SparkProtos.SystemStatistics.Memory.getDefaultInstance() : this.memory_;
        }

        private void setMemory(SparkProtos.SystemStatistics.Memory value) {
            value.getClass();
            this.memory_ = value;
        }

        private void mergeMemory(SparkProtos.SystemStatistics.Memory value) {
            value.getClass();
            if (this.memory_ != null && this.memory_ != SparkProtos.SystemStatistics.Memory.getDefaultInstance()) {
                this.memory_ = SparkProtos.SystemStatistics.Memory.newBuilder(this.memory_).mergeFrom(value).buildPartial();
            } else {
                this.memory_ = value;
            }
        }

        private void clearMemory() {
            this.memory_ = null;
        }

        private MapFieldLite<String, SparkProtos.SystemStatistics.Gc> internalGetGc() {
            return this.gc_;
        }

        private MapFieldLite<String, SparkProtos.SystemStatistics.Gc> internalGetMutableGc() {
            if (!this.gc_.isMutable()) {
                this.gc_ = this.gc_.mutableCopy();
            }
            return this.gc_;
        }

        @Override
        public int getGcCount() {
            return this.internalGetGc().size();
        }

        @Override
        public boolean containsGc(String key) {
            Class<?> keyClass = key.getClass();
            return this.internalGetGc().containsKey(key);
        }

        @Deprecated
        @Override
        public Map<String, SparkProtos.SystemStatistics.Gc> getGc() {
            return this.getGcMap();
        }

        @Override
        public Map<String, SparkProtos.SystemStatistics.Gc> getGcMap() {
            return Collections.unmodifiableMap(this.internalGetGc());
        }

        @Override
        public SparkProtos.SystemStatistics.Gc getGcOrDefault(String key, SparkProtos.SystemStatistics.Gc defaultValue) {
            Class<?> keyClass = key.getClass();
            Map<String, SparkProtos.SystemStatistics.Gc> map = this.internalGetGc();
            return map.containsKey(key) ? (SparkProtos.SystemStatistics.Gc) map.get(key) : defaultValue;
        }

        @Override
        public SparkProtos.SystemStatistics.Gc getGcOrThrow(String key) {
            Class<?> keyClass = key.getClass();
            Map<String, SparkProtos.SystemStatistics.Gc> map = this.internalGetGc();
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException();
            } else {
                return (SparkProtos.SystemStatistics.Gc) map.get(key);
            }
        }

        private Map<String, SparkProtos.SystemStatistics.Gc> getMutableGcMap() {
            return this.internalGetMutableGc();
        }

        @Override
        public boolean hasDisk() {
            return this.disk_ != null;
        }

        @Override
        public SparkProtos.SystemStatistics.Disk getDisk() {
            return this.disk_ == null ? SparkProtos.SystemStatistics.Disk.getDefaultInstance() : this.disk_;
        }

        private void setDisk(SparkProtos.SystemStatistics.Disk value) {
            value.getClass();
            this.disk_ = value;
        }

        private void mergeDisk(SparkProtos.SystemStatistics.Disk value) {
            value.getClass();
            if (this.disk_ != null && this.disk_ != SparkProtos.SystemStatistics.Disk.getDefaultInstance()) {
                this.disk_ = SparkProtos.SystemStatistics.Disk.newBuilder(this.disk_).mergeFrom(value).buildPartial();
            } else {
                this.disk_ = value;
            }
        }

        private void clearDisk() {
            this.disk_ = null;
        }

        @Override
        public boolean hasOs() {
            return this.os_ != null;
        }

        @Override
        public SparkProtos.SystemStatistics.Os getOs() {
            return this.os_ == null ? SparkProtos.SystemStatistics.Os.getDefaultInstance() : this.os_;
        }

        private void setOs(SparkProtos.SystemStatistics.Os value) {
            value.getClass();
            this.os_ = value;
        }

        private void mergeOs(SparkProtos.SystemStatistics.Os value) {
            value.getClass();
            if (this.os_ != null && this.os_ != SparkProtos.SystemStatistics.Os.getDefaultInstance()) {
                this.os_ = SparkProtos.SystemStatistics.Os.newBuilder(this.os_).mergeFrom(value).buildPartial();
            } else {
                this.os_ = value;
            }
        }

        private void clearOs() {
            this.os_ = null;
        }

        @Override
        public boolean hasJava() {
            return this.java_ != null;
        }

        @Override
        public SparkProtos.SystemStatistics.Java getJava() {
            return this.java_ == null ? SparkProtos.SystemStatistics.Java.getDefaultInstance() : this.java_;
        }

        private void setJava(SparkProtos.SystemStatistics.Java value) {
            value.getClass();
            this.java_ = value;
        }

        private void mergeJava(SparkProtos.SystemStatistics.Java value) {
            value.getClass();
            if (this.java_ != null && this.java_ != SparkProtos.SystemStatistics.Java.getDefaultInstance()) {
                this.java_ = SparkProtos.SystemStatistics.Java.newBuilder(this.java_).mergeFrom(value).buildPartial();
            } else {
                this.java_ = value;
            }
        }

        private void clearJava() {
            this.java_ = null;
        }

        @Override
        public long getUptime() {
            return this.uptime_;
        }

        private void setUptime(long value) {
            this.uptime_ = value;
        }

        private void clearUptime() {
            this.uptime_ = 0L;
        }

        private MapFieldLite<String, SparkProtos.SystemStatistics.NetInterface> internalGetNet() {
            return this.net_;
        }

        private MapFieldLite<String, SparkProtos.SystemStatistics.NetInterface> internalGetMutableNet() {
            if (!this.net_.isMutable()) {
                this.net_ = this.net_.mutableCopy();
            }
            return this.net_;
        }

        @Override
        public int getNetCount() {
            return this.internalGetNet().size();
        }

        @Override
        public boolean containsNet(String key) {
            Class<?> keyClass = key.getClass();
            return this.internalGetNet().containsKey(key);
        }

        @Deprecated
        @Override
        public Map<String, SparkProtos.SystemStatistics.NetInterface> getNet() {
            return this.getNetMap();
        }

        @Override
        public Map<String, SparkProtos.SystemStatistics.NetInterface> getNetMap() {
            return Collections.unmodifiableMap(this.internalGetNet());
        }

        @Override
        public SparkProtos.SystemStatistics.NetInterface getNetOrDefault(String key, SparkProtos.SystemStatistics.NetInterface defaultValue) {
            Class<?> keyClass = key.getClass();
            Map<String, SparkProtos.SystemStatistics.NetInterface> map = this.internalGetNet();
            return map.containsKey(key) ? (SparkProtos.SystemStatistics.NetInterface) map.get(key) : defaultValue;
        }

        @Override
        public SparkProtos.SystemStatistics.NetInterface getNetOrThrow(String key) {
            Class<?> keyClass = key.getClass();
            Map<String, SparkProtos.SystemStatistics.NetInterface> map = this.internalGetNet();
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException();
            } else {
                return (SparkProtos.SystemStatistics.NetInterface) map.get(key);
            }
        }

        private Map<String, SparkProtos.SystemStatistics.NetInterface> getMutableNetMap() {
            return this.internalGetMutableNet();
        }

        public static SparkProtos.SystemStatistics parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.SystemStatistics parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.SystemStatistics parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.SystemStatistics parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.SystemStatistics parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.SystemStatistics parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.SystemStatistics parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.SystemStatistics parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.SystemStatistics parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.SystemStatistics parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.SystemStatistics parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.SystemStatistics parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.SystemStatistics.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkProtos.SystemStatistics.Builder newBuilder(SparkProtos.SystemStatistics prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:77)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.instanceOf(StructContext.java:282)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkProtos$SystemStatistics.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$SystemStatistics;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkProtos$SystemStatistics.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkProtos$SystemStatistics$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkProtos.SystemStatistics();
                case NEW_BUILDER:
                    return new SparkProtos.SystemStatistics.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "cpu_", "memory_", "gc_", SparkProtos.SystemStatistics.GcDefaultEntryHolder.defaultEntry, "disk_", "os_", "java_", "uptime_", "net_", SparkProtos.SystemStatistics.NetDefaultEntryHolder.defaultEntry };
                    String info = "\u0000\b\u0000\u0000\u0001\b\b\u0002\u0000\u0000\u0001\t\u0002\t\u00032\u0004\t\u0005\t\u0006\t\u0007\u0002\b2";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkProtos.SystemStatistics> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkProtos.SystemStatistics.class) {
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

        public static SparkProtos.SystemStatistics getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkProtos.SystemStatistics> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkProtos.SystemStatistics defaultInstance = new SparkProtos.SystemStatistics();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkProtos.SystemStatistics.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.SystemStatistics, SparkProtos.SystemStatistics.Builder> implements SparkProtos.SystemStatisticsOrBuilder {

            private Builder() {
                super(SparkProtos.SystemStatistics.DEFAULT_INSTANCE);
            }

            @Override
            public boolean hasCpu() {
                return this.instance.hasCpu();
            }

            @Override
            public SparkProtos.SystemStatistics.Cpu getCpu() {
                return this.instance.getCpu();
            }

            public SparkProtos.SystemStatistics.Builder setCpu(SparkProtos.SystemStatistics.Cpu value) {
                this.copyOnWrite();
                this.instance.setCpu(value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder setCpu(SparkProtos.SystemStatistics.Cpu.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setCpu(builderForValue.build());
                return this;
            }

            public SparkProtos.SystemStatistics.Builder mergeCpu(SparkProtos.SystemStatistics.Cpu value) {
                this.copyOnWrite();
                this.instance.mergeCpu(value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder clearCpu() {
                this.copyOnWrite();
                this.instance.clearCpu();
                return this;
            }

            @Override
            public boolean hasMemory() {
                return this.instance.hasMemory();
            }

            @Override
            public SparkProtos.SystemStatistics.Memory getMemory() {
                return this.instance.getMemory();
            }

            public SparkProtos.SystemStatistics.Builder setMemory(SparkProtos.SystemStatistics.Memory value) {
                this.copyOnWrite();
                this.instance.setMemory(value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder setMemory(SparkProtos.SystemStatistics.Memory.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setMemory(builderForValue.build());
                return this;
            }

            public SparkProtos.SystemStatistics.Builder mergeMemory(SparkProtos.SystemStatistics.Memory value) {
                this.copyOnWrite();
                this.instance.mergeMemory(value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder clearMemory() {
                this.copyOnWrite();
                this.instance.clearMemory();
                return this;
            }

            @Override
            public int getGcCount() {
                return this.instance.getGcMap().size();
            }

            @Override
            public boolean containsGc(String key) {
                Class<?> keyClass = key.getClass();
                return this.instance.getGcMap().containsKey(key);
            }

            public SparkProtos.SystemStatistics.Builder clearGc() {
                this.copyOnWrite();
                this.instance.getMutableGcMap().clear();
                return this;
            }

            public SparkProtos.SystemStatistics.Builder removeGc(String key) {
                Class<?> keyClass = key.getClass();
                this.copyOnWrite();
                this.instance.getMutableGcMap().remove(key);
                return this;
            }

            @Deprecated
            @Override
            public Map<String, SparkProtos.SystemStatistics.Gc> getGc() {
                return this.getGcMap();
            }

            @Override
            public Map<String, SparkProtos.SystemStatistics.Gc> getGcMap() {
                return Collections.unmodifiableMap(this.instance.getGcMap());
            }

            @Override
            public SparkProtos.SystemStatistics.Gc getGcOrDefault(String key, SparkProtos.SystemStatistics.Gc defaultValue) {
                Class<?> keyClass = key.getClass();
                Map<String, SparkProtos.SystemStatistics.Gc> map = this.instance.getGcMap();
                return map.containsKey(key) ? (SparkProtos.SystemStatistics.Gc) map.get(key) : defaultValue;
            }

            @Override
            public SparkProtos.SystemStatistics.Gc getGcOrThrow(String key) {
                Class<?> keyClass = key.getClass();
                Map<String, SparkProtos.SystemStatistics.Gc> map = this.instance.getGcMap();
                if (!map.containsKey(key)) {
                    throw new IllegalArgumentException();
                } else {
                    return (SparkProtos.SystemStatistics.Gc) map.get(key);
                }
            }

            public SparkProtos.SystemStatistics.Builder putGc(String key, SparkProtos.SystemStatistics.Gc value) {
                Class<?> keyClass = key.getClass();
                Class<?> valueClass = value.getClass();
                this.copyOnWrite();
                this.instance.getMutableGcMap().put(key, value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder putAllGc(Map<String, SparkProtos.SystemStatistics.Gc> values) {
                this.copyOnWrite();
                this.instance.getMutableGcMap().putAll(values);
                return this;
            }

            @Override
            public boolean hasDisk() {
                return this.instance.hasDisk();
            }

            @Override
            public SparkProtos.SystemStatistics.Disk getDisk() {
                return this.instance.getDisk();
            }

            public SparkProtos.SystemStatistics.Builder setDisk(SparkProtos.SystemStatistics.Disk value) {
                this.copyOnWrite();
                this.instance.setDisk(value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder setDisk(SparkProtos.SystemStatistics.Disk.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setDisk(builderForValue.build());
                return this;
            }

            public SparkProtos.SystemStatistics.Builder mergeDisk(SparkProtos.SystemStatistics.Disk value) {
                this.copyOnWrite();
                this.instance.mergeDisk(value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder clearDisk() {
                this.copyOnWrite();
                this.instance.clearDisk();
                return this;
            }

            @Override
            public boolean hasOs() {
                return this.instance.hasOs();
            }

            @Override
            public SparkProtos.SystemStatistics.Os getOs() {
                return this.instance.getOs();
            }

            public SparkProtos.SystemStatistics.Builder setOs(SparkProtos.SystemStatistics.Os value) {
                this.copyOnWrite();
                this.instance.setOs(value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder setOs(SparkProtos.SystemStatistics.Os.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setOs(builderForValue.build());
                return this;
            }

            public SparkProtos.SystemStatistics.Builder mergeOs(SparkProtos.SystemStatistics.Os value) {
                this.copyOnWrite();
                this.instance.mergeOs(value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder clearOs() {
                this.copyOnWrite();
                this.instance.clearOs();
                return this;
            }

            @Override
            public boolean hasJava() {
                return this.instance.hasJava();
            }

            @Override
            public SparkProtos.SystemStatistics.Java getJava() {
                return this.instance.getJava();
            }

            public SparkProtos.SystemStatistics.Builder setJava(SparkProtos.SystemStatistics.Java value) {
                this.copyOnWrite();
                this.instance.setJava(value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder setJava(SparkProtos.SystemStatistics.Java.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setJava(builderForValue.build());
                return this;
            }

            public SparkProtos.SystemStatistics.Builder mergeJava(SparkProtos.SystemStatistics.Java value) {
                this.copyOnWrite();
                this.instance.mergeJava(value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder clearJava() {
                this.copyOnWrite();
                this.instance.clearJava();
                return this;
            }

            @Override
            public long getUptime() {
                return this.instance.getUptime();
            }

            public SparkProtos.SystemStatistics.Builder setUptime(long value) {
                this.copyOnWrite();
                this.instance.setUptime(value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder clearUptime() {
                this.copyOnWrite();
                this.instance.clearUptime();
                return this;
            }

            @Override
            public int getNetCount() {
                return this.instance.getNetMap().size();
            }

            @Override
            public boolean containsNet(String key) {
                Class<?> keyClass = key.getClass();
                return this.instance.getNetMap().containsKey(key);
            }

            public SparkProtos.SystemStatistics.Builder clearNet() {
                this.copyOnWrite();
                this.instance.getMutableNetMap().clear();
                return this;
            }

            public SparkProtos.SystemStatistics.Builder removeNet(String key) {
                Class<?> keyClass = key.getClass();
                this.copyOnWrite();
                this.instance.getMutableNetMap().remove(key);
                return this;
            }

            @Deprecated
            @Override
            public Map<String, SparkProtos.SystemStatistics.NetInterface> getNet() {
                return this.getNetMap();
            }

            @Override
            public Map<String, SparkProtos.SystemStatistics.NetInterface> getNetMap() {
                return Collections.unmodifiableMap(this.instance.getNetMap());
            }

            @Override
            public SparkProtos.SystemStatistics.NetInterface getNetOrDefault(String key, SparkProtos.SystemStatistics.NetInterface defaultValue) {
                Class<?> keyClass = key.getClass();
                Map<String, SparkProtos.SystemStatistics.NetInterface> map = this.instance.getNetMap();
                return map.containsKey(key) ? (SparkProtos.SystemStatistics.NetInterface) map.get(key) : defaultValue;
            }

            @Override
            public SparkProtos.SystemStatistics.NetInterface getNetOrThrow(String key) {
                Class<?> keyClass = key.getClass();
                Map<String, SparkProtos.SystemStatistics.NetInterface> map = this.instance.getNetMap();
                if (!map.containsKey(key)) {
                    throw new IllegalArgumentException();
                } else {
                    return (SparkProtos.SystemStatistics.NetInterface) map.get(key);
                }
            }

            public SparkProtos.SystemStatistics.Builder putNet(String key, SparkProtos.SystemStatistics.NetInterface value) {
                Class<?> keyClass = key.getClass();
                Class<?> valueClass = value.getClass();
                this.copyOnWrite();
                this.instance.getMutableNetMap().put(key, value);
                return this;
            }

            public SparkProtos.SystemStatistics.Builder putAllNet(Map<String, SparkProtos.SystemStatistics.NetInterface> values) {
                this.copyOnWrite();
                this.instance.getMutableNetMap().putAll(values);
                return this;
            }
        }

        public static final class Cpu extends GeneratedMessageLite<SparkProtos.SystemStatistics.Cpu, SparkProtos.SystemStatistics.Cpu.Builder> implements SparkProtos.SystemStatistics.CpuOrBuilder {

            public static final int THREADS_FIELD_NUMBER = 1;

            private int threads_;

            public static final int PROCESS_USAGE_FIELD_NUMBER = 2;

            private SparkProtos.SystemStatistics.Cpu.Usage processUsage_;

            public static final int SYSTEM_USAGE_FIELD_NUMBER = 3;

            private SparkProtos.SystemStatistics.Cpu.Usage systemUsage_;

            public static final int MODEL_NAME_FIELD_NUMBER = 4;

            private String modelName_ = "";

            private static final SparkProtos.SystemStatistics.Cpu DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.SystemStatistics.Cpu> PARSER;

            private Cpu() {
            }

            @Override
            public int getThreads() {
                return this.threads_;
            }

            private void setThreads(int value) {
                this.threads_ = value;
            }

            private void clearThreads() {
                this.threads_ = 0;
            }

            @Override
            public boolean hasProcessUsage() {
                return this.processUsage_ != null;
            }

            @Override
            public SparkProtos.SystemStatistics.Cpu.Usage getProcessUsage() {
                return this.processUsage_ == null ? SparkProtos.SystemStatistics.Cpu.Usage.getDefaultInstance() : this.processUsage_;
            }

            private void setProcessUsage(SparkProtos.SystemStatistics.Cpu.Usage value) {
                value.getClass();
                this.processUsage_ = value;
            }

            private void mergeProcessUsage(SparkProtos.SystemStatistics.Cpu.Usage value) {
                value.getClass();
                if (this.processUsage_ != null && this.processUsage_ != SparkProtos.SystemStatistics.Cpu.Usage.getDefaultInstance()) {
                    this.processUsage_ = SparkProtos.SystemStatistics.Cpu.Usage.newBuilder(this.processUsage_).mergeFrom(value).buildPartial();
                } else {
                    this.processUsage_ = value;
                }
            }

            private void clearProcessUsage() {
                this.processUsage_ = null;
            }

            @Override
            public boolean hasSystemUsage() {
                return this.systemUsage_ != null;
            }

            @Override
            public SparkProtos.SystemStatistics.Cpu.Usage getSystemUsage() {
                return this.systemUsage_ == null ? SparkProtos.SystemStatistics.Cpu.Usage.getDefaultInstance() : this.systemUsage_;
            }

            private void setSystemUsage(SparkProtos.SystemStatistics.Cpu.Usage value) {
                value.getClass();
                this.systemUsage_ = value;
            }

            private void mergeSystemUsage(SparkProtos.SystemStatistics.Cpu.Usage value) {
                value.getClass();
                if (this.systemUsage_ != null && this.systemUsage_ != SparkProtos.SystemStatistics.Cpu.Usage.getDefaultInstance()) {
                    this.systemUsage_ = SparkProtos.SystemStatistics.Cpu.Usage.newBuilder(this.systemUsage_).mergeFrom(value).buildPartial();
                } else {
                    this.systemUsage_ = value;
                }
            }

            private void clearSystemUsage() {
                this.systemUsage_ = null;
            }

            @Override
            public String getModelName() {
                return this.modelName_;
            }

            @Override
            public ByteString getModelNameBytes() {
                return ByteString.copyFromUtf8(this.modelName_);
            }

            private void setModelName(String value) {
                Class<?> valueClass = value.getClass();
                this.modelName_ = value;
            }

            private void clearModelName() {
                this.modelName_ = getDefaultInstance().getModelName();
            }

            private void setModelNameBytes(ByteString value) {
                checkByteStringIsUtf8(value);
                this.modelName_ = value.toStringUtf8();
            }

            public static SparkProtos.SystemStatistics.Cpu parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Cpu parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Cpu parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Cpu parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Cpu parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Cpu parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Cpu parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Cpu parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Cpu parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Cpu parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Cpu parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Cpu parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Cpu.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.SystemStatistics.Cpu.Builder newBuilder(SparkProtos.SystemStatistics.Cpu prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$SystemStatistics$Cpu.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$SystemStatistics$Cpu;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$SystemStatistics$Cpu.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$SystemStatistics$Cpu$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.SystemStatistics.Cpu();
                    case NEW_BUILDER:
                        return new SparkProtos.SystemStatistics.Cpu.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "threads_", "processUsage_", "systemUsage_", "modelName_" };
                        String info = "\u0000\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0000\u0000\u0001\u0004\u0002\t\u0003\t\u0004Ȉ";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.SystemStatistics.Cpu> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.SystemStatistics.Cpu.class) {
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

            public static SparkProtos.SystemStatistics.Cpu getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.SystemStatistics.Cpu> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.SystemStatistics.Cpu defaultInstance = new SparkProtos.SystemStatistics.Cpu();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.SystemStatistics.Cpu.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.SystemStatistics.Cpu, SparkProtos.SystemStatistics.Cpu.Builder> implements SparkProtos.SystemStatistics.CpuOrBuilder {

                private Builder() {
                    super(SparkProtos.SystemStatistics.Cpu.DEFAULT_INSTANCE);
                }

                @Override
                public int getThreads() {
                    return this.instance.getThreads();
                }

                public SparkProtos.SystemStatistics.Cpu.Builder setThreads(int value) {
                    this.copyOnWrite();
                    this.instance.setThreads(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Cpu.Builder clearThreads() {
                    this.copyOnWrite();
                    this.instance.clearThreads();
                    return this;
                }

                @Override
                public boolean hasProcessUsage() {
                    return this.instance.hasProcessUsage();
                }

                @Override
                public SparkProtos.SystemStatistics.Cpu.Usage getProcessUsage() {
                    return this.instance.getProcessUsage();
                }

                public SparkProtos.SystemStatistics.Cpu.Builder setProcessUsage(SparkProtos.SystemStatistics.Cpu.Usage value) {
                    this.copyOnWrite();
                    this.instance.setProcessUsage(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Cpu.Builder setProcessUsage(SparkProtos.SystemStatistics.Cpu.Usage.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setProcessUsage(builderForValue.build());
                    return this;
                }

                public SparkProtos.SystemStatistics.Cpu.Builder mergeProcessUsage(SparkProtos.SystemStatistics.Cpu.Usage value) {
                    this.copyOnWrite();
                    this.instance.mergeProcessUsage(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Cpu.Builder clearProcessUsage() {
                    this.copyOnWrite();
                    this.instance.clearProcessUsage();
                    return this;
                }

                @Override
                public boolean hasSystemUsage() {
                    return this.instance.hasSystemUsage();
                }

                @Override
                public SparkProtos.SystemStatistics.Cpu.Usage getSystemUsage() {
                    return this.instance.getSystemUsage();
                }

                public SparkProtos.SystemStatistics.Cpu.Builder setSystemUsage(SparkProtos.SystemStatistics.Cpu.Usage value) {
                    this.copyOnWrite();
                    this.instance.setSystemUsage(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Cpu.Builder setSystemUsage(SparkProtos.SystemStatistics.Cpu.Usage.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setSystemUsage(builderForValue.build());
                    return this;
                }

                public SparkProtos.SystemStatistics.Cpu.Builder mergeSystemUsage(SparkProtos.SystemStatistics.Cpu.Usage value) {
                    this.copyOnWrite();
                    this.instance.mergeSystemUsage(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Cpu.Builder clearSystemUsage() {
                    this.copyOnWrite();
                    this.instance.clearSystemUsage();
                    return this;
                }

                @Override
                public String getModelName() {
                    return this.instance.getModelName();
                }

                @Override
                public ByteString getModelNameBytes() {
                    return this.instance.getModelNameBytes();
                }

                public SparkProtos.SystemStatistics.Cpu.Builder setModelName(String value) {
                    this.copyOnWrite();
                    this.instance.setModelName(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Cpu.Builder clearModelName() {
                    this.copyOnWrite();
                    this.instance.clearModelName();
                    return this;
                }

                public SparkProtos.SystemStatistics.Cpu.Builder setModelNameBytes(ByteString value) {
                    this.copyOnWrite();
                    this.instance.setModelNameBytes(value);
                    return this;
                }
            }

            public static final class Usage extends GeneratedMessageLite<SparkProtos.SystemStatistics.Cpu.Usage, SparkProtos.SystemStatistics.Cpu.Usage.Builder> implements SparkProtos.SystemStatistics.Cpu.UsageOrBuilder {

                public static final int LAST1M_FIELD_NUMBER = 1;

                private double last1M_;

                public static final int LAST15M_FIELD_NUMBER = 2;

                private double last15M_;

                private static final SparkProtos.SystemStatistics.Cpu.Usage DEFAULT_INSTANCE;

                private static volatile Parser<SparkProtos.SystemStatistics.Cpu.Usage> PARSER;

                private Usage() {
                }

                @Override
                public double getLast1M() {
                    return this.last1M_;
                }

                private void setLast1M(double value) {
                    this.last1M_ = value;
                }

                private void clearLast1M() {
                    this.last1M_ = 0.0;
                }

                @Override
                public double getLast15M() {
                    return this.last15M_;
                }

                private void setLast15M(double value) {
                    this.last15M_ = value;
                }

                private void clearLast15M() {
                    this.last15M_ = 0.0;
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage parseFrom(ByteString data) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage parseFrom(byte[] data) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage parseFrom(InputStream input) throws IOException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage parseDelimitedFrom(InputStream input) throws IOException {
                    return parseDelimitedFrom(DEFAULT_INSTANCE, input);
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage parseFrom(CodedInputStream input) throws IOException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage.Builder newBuilder() {
                    return DEFAULT_INSTANCE.createBuilder();
                }

                public static SparkProtos.SystemStatistics.Cpu.Usage.Builder newBuilder(SparkProtos.SystemStatistics.Cpu.Usage prototype) {
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
                    // 0: getstatic me/lucko/spark/proto/SparkProtos$SystemStatistics$Cpu$Usage.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$SystemStatistics$Cpu$Usage;
                    // 3: aload 0
                    // 4: invokevirtual me/lucko/spark/proto/SparkProtos$SystemStatistics$Cpu$Usage.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                    // 7: checkcast me/lucko/spark/proto/SparkProtos$SystemStatistics$Cpu$Usage$Builder
                    // a: areturn
                }

                @Override
                protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                    switch(method) {
                        case NEW_MUTABLE_INSTANCE:
                            return new SparkProtos.SystemStatistics.Cpu.Usage();
                        case NEW_BUILDER:
                            return new SparkProtos.SystemStatistics.Cpu.Usage.Builder();
                        case BUILD_MESSAGE_INFO:
                            Object[] objects = new Object[] { "last1M_", "last15M_" };
                            String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\u0000\u0002\u0000";
                            return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                        case GET_DEFAULT_INSTANCE:
                            return DEFAULT_INSTANCE;
                        case GET_PARSER:
                            Parser<SparkProtos.SystemStatistics.Cpu.Usage> parser = PARSER;
                            if (parser == null) {
                                synchronized (SparkProtos.SystemStatistics.Cpu.Usage.class) {
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

                public static SparkProtos.SystemStatistics.Cpu.Usage getDefaultInstance() {
                    return DEFAULT_INSTANCE;
                }

                public static Parser<SparkProtos.SystemStatistics.Cpu.Usage> parser() {
                    return DEFAULT_INSTANCE.getParserForType();
                }

                static {
                    SparkProtos.SystemStatistics.Cpu.Usage defaultInstance = new SparkProtos.SystemStatistics.Cpu.Usage();
                    DEFAULT_INSTANCE = defaultInstance;
                    GeneratedMessageLite.registerDefaultInstance(SparkProtos.SystemStatistics.Cpu.Usage.class, defaultInstance);
                }

                public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.SystemStatistics.Cpu.Usage, SparkProtos.SystemStatistics.Cpu.Usage.Builder> implements SparkProtos.SystemStatistics.Cpu.UsageOrBuilder {

                    private Builder() {
                        super(SparkProtos.SystemStatistics.Cpu.Usage.DEFAULT_INSTANCE);
                    }

                    @Override
                    public double getLast1M() {
                        return this.instance.getLast1M();
                    }

                    public SparkProtos.SystemStatistics.Cpu.Usage.Builder setLast1M(double value) {
                        this.copyOnWrite();
                        this.instance.setLast1M(value);
                        return this;
                    }

                    public SparkProtos.SystemStatistics.Cpu.Usage.Builder clearLast1M() {
                        this.copyOnWrite();
                        this.instance.clearLast1M();
                        return this;
                    }

                    @Override
                    public double getLast15M() {
                        return this.instance.getLast15M();
                    }

                    public SparkProtos.SystemStatistics.Cpu.Usage.Builder setLast15M(double value) {
                        this.copyOnWrite();
                        this.instance.setLast15M(value);
                        return this;
                    }

                    public SparkProtos.SystemStatistics.Cpu.Usage.Builder clearLast15M() {
                        this.copyOnWrite();
                        this.instance.clearLast15M();
                        return this;
                    }
                }
            }

            public interface UsageOrBuilder extends MessageLiteOrBuilder {

                double getLast1M();

                double getLast15M();
            }
        }

        public interface CpuOrBuilder extends MessageLiteOrBuilder {

            int getThreads();

            boolean hasProcessUsage();

            SparkProtos.SystemStatistics.Cpu.Usage getProcessUsage();

            boolean hasSystemUsage();

            SparkProtos.SystemStatistics.Cpu.Usage getSystemUsage();

            String getModelName();

            ByteString getModelNameBytes();
        }

        public static final class Disk extends GeneratedMessageLite<SparkProtos.SystemStatistics.Disk, SparkProtos.SystemStatistics.Disk.Builder> implements SparkProtos.SystemStatistics.DiskOrBuilder {

            public static final int USED_FIELD_NUMBER = 1;

            private long used_;

            public static final int TOTAL_FIELD_NUMBER = 2;

            private long total_;

            private static final SparkProtos.SystemStatistics.Disk DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.SystemStatistics.Disk> PARSER;

            private Disk() {
            }

            @Override
            public long getUsed() {
                return this.used_;
            }

            private void setUsed(long value) {
                this.used_ = value;
            }

            private void clearUsed() {
                this.used_ = 0L;
            }

            @Override
            public long getTotal() {
                return this.total_;
            }

            private void setTotal(long value) {
                this.total_ = value;
            }

            private void clearTotal() {
                this.total_ = 0L;
            }

            public static SparkProtos.SystemStatistics.Disk parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Disk parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Disk parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Disk parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Disk parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Disk parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Disk parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Disk parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Disk parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Disk parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Disk parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Disk parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Disk.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.SystemStatistics.Disk.Builder newBuilder(SparkProtos.SystemStatistics.Disk prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$SystemStatistics$Disk.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$SystemStatistics$Disk;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$SystemStatistics$Disk.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$SystemStatistics$Disk$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.SystemStatistics.Disk();
                    case NEW_BUILDER:
                        return new SparkProtos.SystemStatistics.Disk.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "used_", "total_" };
                        String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\u0002\u0002\u0002";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.SystemStatistics.Disk> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.SystemStatistics.Disk.class) {
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

            public static SparkProtos.SystemStatistics.Disk getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.SystemStatistics.Disk> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.SystemStatistics.Disk defaultInstance = new SparkProtos.SystemStatistics.Disk();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.SystemStatistics.Disk.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.SystemStatistics.Disk, SparkProtos.SystemStatistics.Disk.Builder> implements SparkProtos.SystemStatistics.DiskOrBuilder {

                private Builder() {
                    super(SparkProtos.SystemStatistics.Disk.DEFAULT_INSTANCE);
                }

                @Override
                public long getUsed() {
                    return this.instance.getUsed();
                }

                public SparkProtos.SystemStatistics.Disk.Builder setUsed(long value) {
                    this.copyOnWrite();
                    this.instance.setUsed(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Disk.Builder clearUsed() {
                    this.copyOnWrite();
                    this.instance.clearUsed();
                    return this;
                }

                @Override
                public long getTotal() {
                    return this.instance.getTotal();
                }

                public SparkProtos.SystemStatistics.Disk.Builder setTotal(long value) {
                    this.copyOnWrite();
                    this.instance.setTotal(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Disk.Builder clearTotal() {
                    this.copyOnWrite();
                    this.instance.clearTotal();
                    return this;
                }
            }
        }

        public interface DiskOrBuilder extends MessageLiteOrBuilder {

            long getUsed();

            long getTotal();
        }

        public static final class Gc extends GeneratedMessageLite<SparkProtos.SystemStatistics.Gc, SparkProtos.SystemStatistics.Gc.Builder> implements SparkProtos.SystemStatistics.GcOrBuilder {

            public static final int TOTAL_FIELD_NUMBER = 1;

            private long total_;

            public static final int AVG_TIME_FIELD_NUMBER = 2;

            private double avgTime_;

            public static final int AVG_FREQUENCY_FIELD_NUMBER = 3;

            private double avgFrequency_;

            private static final SparkProtos.SystemStatistics.Gc DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.SystemStatistics.Gc> PARSER;

            private Gc() {
            }

            @Override
            public long getTotal() {
                return this.total_;
            }

            private void setTotal(long value) {
                this.total_ = value;
            }

            private void clearTotal() {
                this.total_ = 0L;
            }

            @Override
            public double getAvgTime() {
                return this.avgTime_;
            }

            private void setAvgTime(double value) {
                this.avgTime_ = value;
            }

            private void clearAvgTime() {
                this.avgTime_ = 0.0;
            }

            @Override
            public double getAvgFrequency() {
                return this.avgFrequency_;
            }

            private void setAvgFrequency(double value) {
                this.avgFrequency_ = value;
            }

            private void clearAvgFrequency() {
                this.avgFrequency_ = 0.0;
            }

            public static SparkProtos.SystemStatistics.Gc parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Gc parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Gc parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Gc parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Gc parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Gc parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Gc parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Gc parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Gc parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Gc parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Gc parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Gc parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Gc.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.SystemStatistics.Gc.Builder newBuilder(SparkProtos.SystemStatistics.Gc prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$SystemStatistics$Gc.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$SystemStatistics$Gc;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$SystemStatistics$Gc.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$SystemStatistics$Gc$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.SystemStatistics.Gc();
                    case NEW_BUILDER:
                        return new SparkProtos.SystemStatistics.Gc.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "total_", "avgTime_", "avgFrequency_" };
                        String info = "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001\u0002\u0002\u0000\u0003\u0000";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.SystemStatistics.Gc> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.SystemStatistics.Gc.class) {
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

            public static SparkProtos.SystemStatistics.Gc getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.SystemStatistics.Gc> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.SystemStatistics.Gc defaultInstance = new SparkProtos.SystemStatistics.Gc();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.SystemStatistics.Gc.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.SystemStatistics.Gc, SparkProtos.SystemStatistics.Gc.Builder> implements SparkProtos.SystemStatistics.GcOrBuilder {

                private Builder() {
                    super(SparkProtos.SystemStatistics.Gc.DEFAULT_INSTANCE);
                }

                @Override
                public long getTotal() {
                    return this.instance.getTotal();
                }

                public SparkProtos.SystemStatistics.Gc.Builder setTotal(long value) {
                    this.copyOnWrite();
                    this.instance.setTotal(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Gc.Builder clearTotal() {
                    this.copyOnWrite();
                    this.instance.clearTotal();
                    return this;
                }

                @Override
                public double getAvgTime() {
                    return this.instance.getAvgTime();
                }

                public SparkProtos.SystemStatistics.Gc.Builder setAvgTime(double value) {
                    this.copyOnWrite();
                    this.instance.setAvgTime(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Gc.Builder clearAvgTime() {
                    this.copyOnWrite();
                    this.instance.clearAvgTime();
                    return this;
                }

                @Override
                public double getAvgFrequency() {
                    return this.instance.getAvgFrequency();
                }

                public SparkProtos.SystemStatistics.Gc.Builder setAvgFrequency(double value) {
                    this.copyOnWrite();
                    this.instance.setAvgFrequency(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Gc.Builder clearAvgFrequency() {
                    this.copyOnWrite();
                    this.instance.clearAvgFrequency();
                    return this;
                }
            }
        }

        private static final class GcDefaultEntryHolder {

            static final MapEntryLite<String, SparkProtos.SystemStatistics.Gc> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.STRING, "", WireFormat.FieldType.MESSAGE, SparkProtos.SystemStatistics.Gc.getDefaultInstance());
        }

        public interface GcOrBuilder extends MessageLiteOrBuilder {

            long getTotal();

            double getAvgTime();

            double getAvgFrequency();
        }

        public static final class Java extends GeneratedMessageLite<SparkProtos.SystemStatistics.Java, SparkProtos.SystemStatistics.Java.Builder> implements SparkProtos.SystemStatistics.JavaOrBuilder {

            public static final int VENDOR_FIELD_NUMBER = 1;

            private String vendor_ = "";

            public static final int VERSION_FIELD_NUMBER = 2;

            private String version_ = "";

            public static final int VENDOR_VERSION_FIELD_NUMBER = 3;

            private String vendorVersion_ = "";

            public static final int VM_ARGS_FIELD_NUMBER = 4;

            private String vmArgs_ = "";

            private static final SparkProtos.SystemStatistics.Java DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.SystemStatistics.Java> PARSER;

            private Java() {
            }

            @Override
            public String getVendor() {
                return this.vendor_;
            }

            @Override
            public ByteString getVendorBytes() {
                return ByteString.copyFromUtf8(this.vendor_);
            }

            private void setVendor(String value) {
                Class<?> valueClass = value.getClass();
                this.vendor_ = value;
            }

            private void clearVendor() {
                this.vendor_ = getDefaultInstance().getVendor();
            }

            private void setVendorBytes(ByteString value) {
                checkByteStringIsUtf8(value);
                this.vendor_ = value.toStringUtf8();
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
            public String getVendorVersion() {
                return this.vendorVersion_;
            }

            @Override
            public ByteString getVendorVersionBytes() {
                return ByteString.copyFromUtf8(this.vendorVersion_);
            }

            private void setVendorVersion(String value) {
                Class<?> valueClass = value.getClass();
                this.vendorVersion_ = value;
            }

            private void clearVendorVersion() {
                this.vendorVersion_ = getDefaultInstance().getVendorVersion();
            }

            private void setVendorVersionBytes(ByteString value) {
                checkByteStringIsUtf8(value);
                this.vendorVersion_ = value.toStringUtf8();
            }

            @Override
            public String getVmArgs() {
                return this.vmArgs_;
            }

            @Override
            public ByteString getVmArgsBytes() {
                return ByteString.copyFromUtf8(this.vmArgs_);
            }

            private void setVmArgs(String value) {
                Class<?> valueClass = value.getClass();
                this.vmArgs_ = value;
            }

            private void clearVmArgs() {
                this.vmArgs_ = getDefaultInstance().getVmArgs();
            }

            private void setVmArgsBytes(ByteString value) {
                checkByteStringIsUtf8(value);
                this.vmArgs_ = value.toStringUtf8();
            }

            public static SparkProtos.SystemStatistics.Java parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Java parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Java parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Java parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Java parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Java parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Java parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Java parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Java parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Java parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Java parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Java parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Java.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.SystemStatistics.Java.Builder newBuilder(SparkProtos.SystemStatistics.Java prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$SystemStatistics$Java.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$SystemStatistics$Java;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$SystemStatistics$Java.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$SystemStatistics$Java$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.SystemStatistics.Java();
                    case NEW_BUILDER:
                        return new SparkProtos.SystemStatistics.Java.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "vendor_", "version_", "vendorVersion_", "vmArgs_" };
                        String info = "\u0000\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0000\u0000\u0001Ȉ\u0002Ȉ\u0003Ȉ\u0004Ȉ";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.SystemStatistics.Java> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.SystemStatistics.Java.class) {
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

            public static SparkProtos.SystemStatistics.Java getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.SystemStatistics.Java> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.SystemStatistics.Java defaultInstance = new SparkProtos.SystemStatistics.Java();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.SystemStatistics.Java.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.SystemStatistics.Java, SparkProtos.SystemStatistics.Java.Builder> implements SparkProtos.SystemStatistics.JavaOrBuilder {

                private Builder() {
                    super(SparkProtos.SystemStatistics.Java.DEFAULT_INSTANCE);
                }

                @Override
                public String getVendor() {
                    return this.instance.getVendor();
                }

                @Override
                public ByteString getVendorBytes() {
                    return this.instance.getVendorBytes();
                }

                public SparkProtos.SystemStatistics.Java.Builder setVendor(String value) {
                    this.copyOnWrite();
                    this.instance.setVendor(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Java.Builder clearVendor() {
                    this.copyOnWrite();
                    this.instance.clearVendor();
                    return this;
                }

                public SparkProtos.SystemStatistics.Java.Builder setVendorBytes(ByteString value) {
                    this.copyOnWrite();
                    this.instance.setVendorBytes(value);
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

                public SparkProtos.SystemStatistics.Java.Builder setVersion(String value) {
                    this.copyOnWrite();
                    this.instance.setVersion(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Java.Builder clearVersion() {
                    this.copyOnWrite();
                    this.instance.clearVersion();
                    return this;
                }

                public SparkProtos.SystemStatistics.Java.Builder setVersionBytes(ByteString value) {
                    this.copyOnWrite();
                    this.instance.setVersionBytes(value);
                    return this;
                }

                @Override
                public String getVendorVersion() {
                    return this.instance.getVendorVersion();
                }

                @Override
                public ByteString getVendorVersionBytes() {
                    return this.instance.getVendorVersionBytes();
                }

                public SparkProtos.SystemStatistics.Java.Builder setVendorVersion(String value) {
                    this.copyOnWrite();
                    this.instance.setVendorVersion(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Java.Builder clearVendorVersion() {
                    this.copyOnWrite();
                    this.instance.clearVendorVersion();
                    return this;
                }

                public SparkProtos.SystemStatistics.Java.Builder setVendorVersionBytes(ByteString value) {
                    this.copyOnWrite();
                    this.instance.setVendorVersionBytes(value);
                    return this;
                }

                @Override
                public String getVmArgs() {
                    return this.instance.getVmArgs();
                }

                @Override
                public ByteString getVmArgsBytes() {
                    return this.instance.getVmArgsBytes();
                }

                public SparkProtos.SystemStatistics.Java.Builder setVmArgs(String value) {
                    this.copyOnWrite();
                    this.instance.setVmArgs(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Java.Builder clearVmArgs() {
                    this.copyOnWrite();
                    this.instance.clearVmArgs();
                    return this;
                }

                public SparkProtos.SystemStatistics.Java.Builder setVmArgsBytes(ByteString value) {
                    this.copyOnWrite();
                    this.instance.setVmArgsBytes(value);
                    return this;
                }
            }
        }

        public interface JavaOrBuilder extends MessageLiteOrBuilder {

            String getVendor();

            ByteString getVendorBytes();

            String getVersion();

            ByteString getVersionBytes();

            String getVendorVersion();

            ByteString getVendorVersionBytes();

            String getVmArgs();

            ByteString getVmArgsBytes();
        }

        public static final class Memory extends GeneratedMessageLite<SparkProtos.SystemStatistics.Memory, SparkProtos.SystemStatistics.Memory.Builder> implements SparkProtos.SystemStatistics.MemoryOrBuilder {

            public static final int PHYSICAL_FIELD_NUMBER = 1;

            private SparkProtos.SystemStatistics.Memory.MemoryPool physical_;

            public static final int SWAP_FIELD_NUMBER = 2;

            private SparkProtos.SystemStatistics.Memory.MemoryPool swap_;

            private static final SparkProtos.SystemStatistics.Memory DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.SystemStatistics.Memory> PARSER;

            private Memory() {
            }

            @Override
            public boolean hasPhysical() {
                return this.physical_ != null;
            }

            @Override
            public SparkProtos.SystemStatistics.Memory.MemoryPool getPhysical() {
                return this.physical_ == null ? SparkProtos.SystemStatistics.Memory.MemoryPool.getDefaultInstance() : this.physical_;
            }

            private void setPhysical(SparkProtos.SystemStatistics.Memory.MemoryPool value) {
                value.getClass();
                this.physical_ = value;
            }

            private void mergePhysical(SparkProtos.SystemStatistics.Memory.MemoryPool value) {
                value.getClass();
                if (this.physical_ != null && this.physical_ != SparkProtos.SystemStatistics.Memory.MemoryPool.getDefaultInstance()) {
                    this.physical_ = SparkProtos.SystemStatistics.Memory.MemoryPool.newBuilder(this.physical_).mergeFrom(value).buildPartial();
                } else {
                    this.physical_ = value;
                }
            }

            private void clearPhysical() {
                this.physical_ = null;
            }

            @Override
            public boolean hasSwap() {
                return this.swap_ != null;
            }

            @Override
            public SparkProtos.SystemStatistics.Memory.MemoryPool getSwap() {
                return this.swap_ == null ? SparkProtos.SystemStatistics.Memory.MemoryPool.getDefaultInstance() : this.swap_;
            }

            private void setSwap(SparkProtos.SystemStatistics.Memory.MemoryPool value) {
                value.getClass();
                this.swap_ = value;
            }

            private void mergeSwap(SparkProtos.SystemStatistics.Memory.MemoryPool value) {
                value.getClass();
                if (this.swap_ != null && this.swap_ != SparkProtos.SystemStatistics.Memory.MemoryPool.getDefaultInstance()) {
                    this.swap_ = SparkProtos.SystemStatistics.Memory.MemoryPool.newBuilder(this.swap_).mergeFrom(value).buildPartial();
                } else {
                    this.swap_ = value;
                }
            }

            private void clearSwap() {
                this.swap_ = null;
            }

            public static SparkProtos.SystemStatistics.Memory parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Memory parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Memory parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Memory parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Memory parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Memory parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Memory parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Memory parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Memory parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Memory parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Memory parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Memory parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Memory.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.SystemStatistics.Memory.Builder newBuilder(SparkProtos.SystemStatistics.Memory prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$SystemStatistics$Memory.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$SystemStatistics$Memory;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$SystemStatistics$Memory.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$SystemStatistics$Memory$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.SystemStatistics.Memory();
                    case NEW_BUILDER:
                        return new SparkProtos.SystemStatistics.Memory.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "physical_", "swap_" };
                        String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\t\u0002\t";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.SystemStatistics.Memory> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.SystemStatistics.Memory.class) {
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

            public static SparkProtos.SystemStatistics.Memory getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.SystemStatistics.Memory> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.SystemStatistics.Memory defaultInstance = new SparkProtos.SystemStatistics.Memory();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.SystemStatistics.Memory.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.SystemStatistics.Memory, SparkProtos.SystemStatistics.Memory.Builder> implements SparkProtos.SystemStatistics.MemoryOrBuilder {

                private Builder() {
                    super(SparkProtos.SystemStatistics.Memory.DEFAULT_INSTANCE);
                }

                @Override
                public boolean hasPhysical() {
                    return this.instance.hasPhysical();
                }

                @Override
                public SparkProtos.SystemStatistics.Memory.MemoryPool getPhysical() {
                    return this.instance.getPhysical();
                }

                public SparkProtos.SystemStatistics.Memory.Builder setPhysical(SparkProtos.SystemStatistics.Memory.MemoryPool value) {
                    this.copyOnWrite();
                    this.instance.setPhysical(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Memory.Builder setPhysical(SparkProtos.SystemStatistics.Memory.MemoryPool.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setPhysical(builderForValue.build());
                    return this;
                }

                public SparkProtos.SystemStatistics.Memory.Builder mergePhysical(SparkProtos.SystemStatistics.Memory.MemoryPool value) {
                    this.copyOnWrite();
                    this.instance.mergePhysical(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Memory.Builder clearPhysical() {
                    this.copyOnWrite();
                    this.instance.clearPhysical();
                    return this;
                }

                @Override
                public boolean hasSwap() {
                    return this.instance.hasSwap();
                }

                @Override
                public SparkProtos.SystemStatistics.Memory.MemoryPool getSwap() {
                    return this.instance.getSwap();
                }

                public SparkProtos.SystemStatistics.Memory.Builder setSwap(SparkProtos.SystemStatistics.Memory.MemoryPool value) {
                    this.copyOnWrite();
                    this.instance.setSwap(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Memory.Builder setSwap(SparkProtos.SystemStatistics.Memory.MemoryPool.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setSwap(builderForValue.build());
                    return this;
                }

                public SparkProtos.SystemStatistics.Memory.Builder mergeSwap(SparkProtos.SystemStatistics.Memory.MemoryPool value) {
                    this.copyOnWrite();
                    this.instance.mergeSwap(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Memory.Builder clearSwap() {
                    this.copyOnWrite();
                    this.instance.clearSwap();
                    return this;
                }
            }

            public static final class MemoryPool extends GeneratedMessageLite<SparkProtos.SystemStatistics.Memory.MemoryPool, SparkProtos.SystemStatistics.Memory.MemoryPool.Builder> implements SparkProtos.SystemStatistics.Memory.MemoryPoolOrBuilder {

                public static final int USED_FIELD_NUMBER = 1;

                private long used_;

                public static final int TOTAL_FIELD_NUMBER = 2;

                private long total_;

                private static final SparkProtos.SystemStatistics.Memory.MemoryPool DEFAULT_INSTANCE;

                private static volatile Parser<SparkProtos.SystemStatistics.Memory.MemoryPool> PARSER;

                private MemoryPool() {
                }

                @Override
                public long getUsed() {
                    return this.used_;
                }

                private void setUsed(long value) {
                    this.used_ = value;
                }

                private void clearUsed() {
                    this.used_ = 0L;
                }

                @Override
                public long getTotal() {
                    return this.total_;
                }

                private void setTotal(long value) {
                    this.total_ = value;
                }

                private void clearTotal() {
                    this.total_ = 0L;
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool parseFrom(ByteString data) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool parseFrom(byte[] data) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool parseFrom(InputStream input) throws IOException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool parseDelimitedFrom(InputStream input) throws IOException {
                    return parseDelimitedFrom(DEFAULT_INSTANCE, input);
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool parseFrom(CodedInputStream input) throws IOException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool.Builder newBuilder() {
                    return DEFAULT_INSTANCE.createBuilder();
                }

                public static SparkProtos.SystemStatistics.Memory.MemoryPool.Builder newBuilder(SparkProtos.SystemStatistics.Memory.MemoryPool prototype) {
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
                    // 0: getstatic me/lucko/spark/proto/SparkProtos$SystemStatistics$Memory$MemoryPool.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$SystemStatistics$Memory$MemoryPool;
                    // 3: aload 0
                    // 4: invokevirtual me/lucko/spark/proto/SparkProtos$SystemStatistics$Memory$MemoryPool.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                    // 7: checkcast me/lucko/spark/proto/SparkProtos$SystemStatistics$Memory$MemoryPool$Builder
                    // a: areturn
                }

                @Override
                protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                    switch(method) {
                        case NEW_MUTABLE_INSTANCE:
                            return new SparkProtos.SystemStatistics.Memory.MemoryPool();
                        case NEW_BUILDER:
                            return new SparkProtos.SystemStatistics.Memory.MemoryPool.Builder();
                        case BUILD_MESSAGE_INFO:
                            Object[] objects = new Object[] { "used_", "total_" };
                            String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\u0002\u0002\u0002";
                            return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                        case GET_DEFAULT_INSTANCE:
                            return DEFAULT_INSTANCE;
                        case GET_PARSER:
                            Parser<SparkProtos.SystemStatistics.Memory.MemoryPool> parser = PARSER;
                            if (parser == null) {
                                synchronized (SparkProtos.SystemStatistics.Memory.MemoryPool.class) {
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

                public static SparkProtos.SystemStatistics.Memory.MemoryPool getDefaultInstance() {
                    return DEFAULT_INSTANCE;
                }

                public static Parser<SparkProtos.SystemStatistics.Memory.MemoryPool> parser() {
                    return DEFAULT_INSTANCE.getParserForType();
                }

                static {
                    SparkProtos.SystemStatistics.Memory.MemoryPool defaultInstance = new SparkProtos.SystemStatistics.Memory.MemoryPool();
                    DEFAULT_INSTANCE = defaultInstance;
                    GeneratedMessageLite.registerDefaultInstance(SparkProtos.SystemStatistics.Memory.MemoryPool.class, defaultInstance);
                }

                public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.SystemStatistics.Memory.MemoryPool, SparkProtos.SystemStatistics.Memory.MemoryPool.Builder> implements SparkProtos.SystemStatistics.Memory.MemoryPoolOrBuilder {

                    private Builder() {
                        super(SparkProtos.SystemStatistics.Memory.MemoryPool.DEFAULT_INSTANCE);
                    }

                    @Override
                    public long getUsed() {
                        return this.instance.getUsed();
                    }

                    public SparkProtos.SystemStatistics.Memory.MemoryPool.Builder setUsed(long value) {
                        this.copyOnWrite();
                        this.instance.setUsed(value);
                        return this;
                    }

                    public SparkProtos.SystemStatistics.Memory.MemoryPool.Builder clearUsed() {
                        this.copyOnWrite();
                        this.instance.clearUsed();
                        return this;
                    }

                    @Override
                    public long getTotal() {
                        return this.instance.getTotal();
                    }

                    public SparkProtos.SystemStatistics.Memory.MemoryPool.Builder setTotal(long value) {
                        this.copyOnWrite();
                        this.instance.setTotal(value);
                        return this;
                    }

                    public SparkProtos.SystemStatistics.Memory.MemoryPool.Builder clearTotal() {
                        this.copyOnWrite();
                        this.instance.clearTotal();
                        return this;
                    }
                }
            }

            public interface MemoryPoolOrBuilder extends MessageLiteOrBuilder {

                long getUsed();

                long getTotal();
            }
        }

        public interface MemoryOrBuilder extends MessageLiteOrBuilder {

            boolean hasPhysical();

            SparkProtos.SystemStatistics.Memory.MemoryPool getPhysical();

            boolean hasSwap();

            SparkProtos.SystemStatistics.Memory.MemoryPool getSwap();
        }

        private static final class NetDefaultEntryHolder {

            static final MapEntryLite<String, SparkProtos.SystemStatistics.NetInterface> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.STRING, "", WireFormat.FieldType.MESSAGE, SparkProtos.SystemStatistics.NetInterface.getDefaultInstance());
        }

        public static final class NetInterface extends GeneratedMessageLite<SparkProtos.SystemStatistics.NetInterface, SparkProtos.SystemStatistics.NetInterface.Builder> implements SparkProtos.SystemStatistics.NetInterfaceOrBuilder {

            public static final int RX_BYTES_PER_SECOND_FIELD_NUMBER = 1;

            private SparkProtos.RollingAverageValues rxBytesPerSecond_;

            public static final int TX_BYTES_PER_SECOND_FIELD_NUMBER = 2;

            private SparkProtos.RollingAverageValues txBytesPerSecond_;

            public static final int RX_PACKETS_PER_SECOND_FIELD_NUMBER = 3;

            private SparkProtos.RollingAverageValues rxPacketsPerSecond_;

            public static final int TX_PACKETS_PER_SECOND_FIELD_NUMBER = 4;

            private SparkProtos.RollingAverageValues txPacketsPerSecond_;

            private static final SparkProtos.SystemStatistics.NetInterface DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.SystemStatistics.NetInterface> PARSER;

            private NetInterface() {
            }

            @Override
            public boolean hasRxBytesPerSecond() {
                return this.rxBytesPerSecond_ != null;
            }

            @Override
            public SparkProtos.RollingAverageValues getRxBytesPerSecond() {
                return this.rxBytesPerSecond_ == null ? SparkProtos.RollingAverageValues.getDefaultInstance() : this.rxBytesPerSecond_;
            }

            private void setRxBytesPerSecond(SparkProtos.RollingAverageValues value) {
                value.getClass();
                this.rxBytesPerSecond_ = value;
            }

            private void mergeRxBytesPerSecond(SparkProtos.RollingAverageValues value) {
                value.getClass();
                if (this.rxBytesPerSecond_ != null && this.rxBytesPerSecond_ != SparkProtos.RollingAverageValues.getDefaultInstance()) {
                    this.rxBytesPerSecond_ = SparkProtos.RollingAverageValues.newBuilder(this.rxBytesPerSecond_).mergeFrom(value).buildPartial();
                } else {
                    this.rxBytesPerSecond_ = value;
                }
            }

            private void clearRxBytesPerSecond() {
                this.rxBytesPerSecond_ = null;
            }

            @Override
            public boolean hasTxBytesPerSecond() {
                return this.txBytesPerSecond_ != null;
            }

            @Override
            public SparkProtos.RollingAverageValues getTxBytesPerSecond() {
                return this.txBytesPerSecond_ == null ? SparkProtos.RollingAverageValues.getDefaultInstance() : this.txBytesPerSecond_;
            }

            private void setTxBytesPerSecond(SparkProtos.RollingAverageValues value) {
                value.getClass();
                this.txBytesPerSecond_ = value;
            }

            private void mergeTxBytesPerSecond(SparkProtos.RollingAverageValues value) {
                value.getClass();
                if (this.txBytesPerSecond_ != null && this.txBytesPerSecond_ != SparkProtos.RollingAverageValues.getDefaultInstance()) {
                    this.txBytesPerSecond_ = SparkProtos.RollingAverageValues.newBuilder(this.txBytesPerSecond_).mergeFrom(value).buildPartial();
                } else {
                    this.txBytesPerSecond_ = value;
                }
            }

            private void clearTxBytesPerSecond() {
                this.txBytesPerSecond_ = null;
            }

            @Override
            public boolean hasRxPacketsPerSecond() {
                return this.rxPacketsPerSecond_ != null;
            }

            @Override
            public SparkProtos.RollingAverageValues getRxPacketsPerSecond() {
                return this.rxPacketsPerSecond_ == null ? SparkProtos.RollingAverageValues.getDefaultInstance() : this.rxPacketsPerSecond_;
            }

            private void setRxPacketsPerSecond(SparkProtos.RollingAverageValues value) {
                value.getClass();
                this.rxPacketsPerSecond_ = value;
            }

            private void mergeRxPacketsPerSecond(SparkProtos.RollingAverageValues value) {
                value.getClass();
                if (this.rxPacketsPerSecond_ != null && this.rxPacketsPerSecond_ != SparkProtos.RollingAverageValues.getDefaultInstance()) {
                    this.rxPacketsPerSecond_ = SparkProtos.RollingAverageValues.newBuilder(this.rxPacketsPerSecond_).mergeFrom(value).buildPartial();
                } else {
                    this.rxPacketsPerSecond_ = value;
                }
            }

            private void clearRxPacketsPerSecond() {
                this.rxPacketsPerSecond_ = null;
            }

            @Override
            public boolean hasTxPacketsPerSecond() {
                return this.txPacketsPerSecond_ != null;
            }

            @Override
            public SparkProtos.RollingAverageValues getTxPacketsPerSecond() {
                return this.txPacketsPerSecond_ == null ? SparkProtos.RollingAverageValues.getDefaultInstance() : this.txPacketsPerSecond_;
            }

            private void setTxPacketsPerSecond(SparkProtos.RollingAverageValues value) {
                value.getClass();
                this.txPacketsPerSecond_ = value;
            }

            private void mergeTxPacketsPerSecond(SparkProtos.RollingAverageValues value) {
                value.getClass();
                if (this.txPacketsPerSecond_ != null && this.txPacketsPerSecond_ != SparkProtos.RollingAverageValues.getDefaultInstance()) {
                    this.txPacketsPerSecond_ = SparkProtos.RollingAverageValues.newBuilder(this.txPacketsPerSecond_).mergeFrom(value).buildPartial();
                } else {
                    this.txPacketsPerSecond_ = value;
                }
            }

            private void clearTxPacketsPerSecond() {
                this.txPacketsPerSecond_ = null;
            }

            public static SparkProtos.SystemStatistics.NetInterface parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.NetInterface parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.NetInterface parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.NetInterface parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.NetInterface parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.NetInterface parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.NetInterface parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.NetInterface parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.NetInterface parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.NetInterface parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.NetInterface parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.NetInterface parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.NetInterface.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.SystemStatistics.NetInterface.Builder newBuilder(SparkProtos.SystemStatistics.NetInterface prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$SystemStatistics$NetInterface.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$SystemStatistics$NetInterface;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$SystemStatistics$NetInterface.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$SystemStatistics$NetInterface$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.SystemStatistics.NetInterface();
                    case NEW_BUILDER:
                        return new SparkProtos.SystemStatistics.NetInterface.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "rxBytesPerSecond_", "txBytesPerSecond_", "rxPacketsPerSecond_", "txPacketsPerSecond_" };
                        String info = "\u0000\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0000\u0000\u0001\t\u0002\t\u0003\t\u0004\t";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.SystemStatistics.NetInterface> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.SystemStatistics.NetInterface.class) {
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

            public static SparkProtos.SystemStatistics.NetInterface getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.SystemStatistics.NetInterface> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.SystemStatistics.NetInterface defaultInstance = new SparkProtos.SystemStatistics.NetInterface();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.SystemStatistics.NetInterface.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.SystemStatistics.NetInterface, SparkProtos.SystemStatistics.NetInterface.Builder> implements SparkProtos.SystemStatistics.NetInterfaceOrBuilder {

                private Builder() {
                    super(SparkProtos.SystemStatistics.NetInterface.DEFAULT_INSTANCE);
                }

                @Override
                public boolean hasRxBytesPerSecond() {
                    return this.instance.hasRxBytesPerSecond();
                }

                @Override
                public SparkProtos.RollingAverageValues getRxBytesPerSecond() {
                    return this.instance.getRxBytesPerSecond();
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder setRxBytesPerSecond(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.setRxBytesPerSecond(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder setRxBytesPerSecond(SparkProtos.RollingAverageValues.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setRxBytesPerSecond(builderForValue.build());
                    return this;
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder mergeRxBytesPerSecond(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.mergeRxBytesPerSecond(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder clearRxBytesPerSecond() {
                    this.copyOnWrite();
                    this.instance.clearRxBytesPerSecond();
                    return this;
                }

                @Override
                public boolean hasTxBytesPerSecond() {
                    return this.instance.hasTxBytesPerSecond();
                }

                @Override
                public SparkProtos.RollingAverageValues getTxBytesPerSecond() {
                    return this.instance.getTxBytesPerSecond();
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder setTxBytesPerSecond(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.setTxBytesPerSecond(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder setTxBytesPerSecond(SparkProtos.RollingAverageValues.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setTxBytesPerSecond(builderForValue.build());
                    return this;
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder mergeTxBytesPerSecond(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.mergeTxBytesPerSecond(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder clearTxBytesPerSecond() {
                    this.copyOnWrite();
                    this.instance.clearTxBytesPerSecond();
                    return this;
                }

                @Override
                public boolean hasRxPacketsPerSecond() {
                    return this.instance.hasRxPacketsPerSecond();
                }

                @Override
                public SparkProtos.RollingAverageValues getRxPacketsPerSecond() {
                    return this.instance.getRxPacketsPerSecond();
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder setRxPacketsPerSecond(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.setRxPacketsPerSecond(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder setRxPacketsPerSecond(SparkProtos.RollingAverageValues.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setRxPacketsPerSecond(builderForValue.build());
                    return this;
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder mergeRxPacketsPerSecond(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.mergeRxPacketsPerSecond(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder clearRxPacketsPerSecond() {
                    this.copyOnWrite();
                    this.instance.clearRxPacketsPerSecond();
                    return this;
                }

                @Override
                public boolean hasTxPacketsPerSecond() {
                    return this.instance.hasTxPacketsPerSecond();
                }

                @Override
                public SparkProtos.RollingAverageValues getTxPacketsPerSecond() {
                    return this.instance.getTxPacketsPerSecond();
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder setTxPacketsPerSecond(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.setTxPacketsPerSecond(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder setTxPacketsPerSecond(SparkProtos.RollingAverageValues.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setTxPacketsPerSecond(builderForValue.build());
                    return this;
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder mergeTxPacketsPerSecond(SparkProtos.RollingAverageValues value) {
                    this.copyOnWrite();
                    this.instance.mergeTxPacketsPerSecond(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.NetInterface.Builder clearTxPacketsPerSecond() {
                    this.copyOnWrite();
                    this.instance.clearTxPacketsPerSecond();
                    return this;
                }
            }
        }

        public interface NetInterfaceOrBuilder extends MessageLiteOrBuilder {

            boolean hasRxBytesPerSecond();

            SparkProtos.RollingAverageValues getRxBytesPerSecond();

            boolean hasTxBytesPerSecond();

            SparkProtos.RollingAverageValues getTxBytesPerSecond();

            boolean hasRxPacketsPerSecond();

            SparkProtos.RollingAverageValues getRxPacketsPerSecond();

            boolean hasTxPacketsPerSecond();

            SparkProtos.RollingAverageValues getTxPacketsPerSecond();
        }

        public static final class Os extends GeneratedMessageLite<SparkProtos.SystemStatistics.Os, SparkProtos.SystemStatistics.Os.Builder> implements SparkProtos.SystemStatistics.OsOrBuilder {

            public static final int ARCH_FIELD_NUMBER = 1;

            private String arch_ = "";

            public static final int NAME_FIELD_NUMBER = 2;

            private String name_ = "";

            public static final int VERSION_FIELD_NUMBER = 3;

            private String version_ = "";

            private static final SparkProtos.SystemStatistics.Os DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.SystemStatistics.Os> PARSER;

            private Os() {
            }

            @Override
            public String getArch() {
                return this.arch_;
            }

            @Override
            public ByteString getArchBytes() {
                return ByteString.copyFromUtf8(this.arch_);
            }

            private void setArch(String value) {
                Class<?> valueClass = value.getClass();
                this.arch_ = value;
            }

            private void clearArch() {
                this.arch_ = getDefaultInstance().getArch();
            }

            private void setArchBytes(ByteString value) {
                checkByteStringIsUtf8(value);
                this.arch_ = value.toStringUtf8();
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

            public static SparkProtos.SystemStatistics.Os parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Os parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Os parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Os parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Os parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.SystemStatistics.Os parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Os parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Os parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Os parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Os parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Os parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.SystemStatistics.Os parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.SystemStatistics.Os.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.SystemStatistics.Os.Builder newBuilder(SparkProtos.SystemStatistics.Os prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$SystemStatistics$Os.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$SystemStatistics$Os;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$SystemStatistics$Os.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$SystemStatistics$Os$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.SystemStatistics.Os();
                    case NEW_BUILDER:
                        return new SparkProtos.SystemStatistics.Os.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "arch_", "name_", "version_" };
                        String info = "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001Ȉ\u0002Ȉ\u0003Ȉ";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.SystemStatistics.Os> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.SystemStatistics.Os.class) {
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

            public static SparkProtos.SystemStatistics.Os getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.SystemStatistics.Os> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.SystemStatistics.Os defaultInstance = new SparkProtos.SystemStatistics.Os();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.SystemStatistics.Os.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.SystemStatistics.Os, SparkProtos.SystemStatistics.Os.Builder> implements SparkProtos.SystemStatistics.OsOrBuilder {

                private Builder() {
                    super(SparkProtos.SystemStatistics.Os.DEFAULT_INSTANCE);
                }

                @Override
                public String getArch() {
                    return this.instance.getArch();
                }

                @Override
                public ByteString getArchBytes() {
                    return this.instance.getArchBytes();
                }

                public SparkProtos.SystemStatistics.Os.Builder setArch(String value) {
                    this.copyOnWrite();
                    this.instance.setArch(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Os.Builder clearArch() {
                    this.copyOnWrite();
                    this.instance.clearArch();
                    return this;
                }

                public SparkProtos.SystemStatistics.Os.Builder setArchBytes(ByteString value) {
                    this.copyOnWrite();
                    this.instance.setArchBytes(value);
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

                public SparkProtos.SystemStatistics.Os.Builder setName(String value) {
                    this.copyOnWrite();
                    this.instance.setName(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Os.Builder clearName() {
                    this.copyOnWrite();
                    this.instance.clearName();
                    return this;
                }

                public SparkProtos.SystemStatistics.Os.Builder setNameBytes(ByteString value) {
                    this.copyOnWrite();
                    this.instance.setNameBytes(value);
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

                public SparkProtos.SystemStatistics.Os.Builder setVersion(String value) {
                    this.copyOnWrite();
                    this.instance.setVersion(value);
                    return this;
                }

                public SparkProtos.SystemStatistics.Os.Builder clearVersion() {
                    this.copyOnWrite();
                    this.instance.clearVersion();
                    return this;
                }

                public SparkProtos.SystemStatistics.Os.Builder setVersionBytes(ByteString value) {
                    this.copyOnWrite();
                    this.instance.setVersionBytes(value);
                    return this;
                }
            }
        }

        public interface OsOrBuilder extends MessageLiteOrBuilder {

            String getArch();

            ByteString getArchBytes();

            String getName();

            ByteString getNameBytes();

            String getVersion();

            ByteString getVersionBytes();
        }
    }

    public interface SystemStatisticsOrBuilder extends MessageLiteOrBuilder {

        boolean hasCpu();

        SparkProtos.SystemStatistics.Cpu getCpu();

        boolean hasMemory();

        SparkProtos.SystemStatistics.Memory getMemory();

        int getGcCount();

        boolean containsGc(String var1);

        @Deprecated
        Map<String, SparkProtos.SystemStatistics.Gc> getGc();

        Map<String, SparkProtos.SystemStatistics.Gc> getGcMap();

        SparkProtos.SystemStatistics.Gc getGcOrDefault(String var1, SparkProtos.SystemStatistics.Gc var2);

        SparkProtos.SystemStatistics.Gc getGcOrThrow(String var1);

        boolean hasDisk();

        SparkProtos.SystemStatistics.Disk getDisk();

        boolean hasOs();

        SparkProtos.SystemStatistics.Os getOs();

        boolean hasJava();

        SparkProtos.SystemStatistics.Java getJava();

        long getUptime();

        int getNetCount();

        boolean containsNet(String var1);

        @Deprecated
        Map<String, SparkProtos.SystemStatistics.NetInterface> getNet();

        Map<String, SparkProtos.SystemStatistics.NetInterface> getNetMap();

        SparkProtos.SystemStatistics.NetInterface getNetOrDefault(String var1, SparkProtos.SystemStatistics.NetInterface var2);

        SparkProtos.SystemStatistics.NetInterface getNetOrThrow(String var1);
    }

    public static final class WindowStatistics extends GeneratedMessageLite<SparkProtos.WindowStatistics, SparkProtos.WindowStatistics.Builder> implements SparkProtos.WindowStatisticsOrBuilder {

        public static final int TICKS_FIELD_NUMBER = 1;

        private int ticks_;

        public static final int CPU_PROCESS_FIELD_NUMBER = 2;

        private double cpuProcess_;

        public static final int CPU_SYSTEM_FIELD_NUMBER = 3;

        private double cpuSystem_;

        public static final int TPS_FIELD_NUMBER = 4;

        private double tps_;

        public static final int MSPT_MEDIAN_FIELD_NUMBER = 5;

        private double msptMedian_;

        public static final int MSPT_MAX_FIELD_NUMBER = 6;

        private double msptMax_;

        public static final int PLAYERS_FIELD_NUMBER = 7;

        private int players_;

        public static final int ENTITIES_FIELD_NUMBER = 8;

        private int entities_;

        public static final int TILE_ENTITIES_FIELD_NUMBER = 9;

        private int tileEntities_;

        public static final int CHUNKS_FIELD_NUMBER = 10;

        private int chunks_;

        public static final int START_TIME_FIELD_NUMBER = 11;

        private long startTime_;

        public static final int END_TIME_FIELD_NUMBER = 12;

        private long endTime_;

        public static final int DURATION_FIELD_NUMBER = 13;

        private int duration_;

        private static final SparkProtos.WindowStatistics DEFAULT_INSTANCE;

        private static volatile Parser<SparkProtos.WindowStatistics> PARSER;

        private WindowStatistics() {
        }

        @Override
        public int getTicks() {
            return this.ticks_;
        }

        private void setTicks(int value) {
            this.ticks_ = value;
        }

        private void clearTicks() {
            this.ticks_ = 0;
        }

        @Override
        public double getCpuProcess() {
            return this.cpuProcess_;
        }

        private void setCpuProcess(double value) {
            this.cpuProcess_ = value;
        }

        private void clearCpuProcess() {
            this.cpuProcess_ = 0.0;
        }

        @Override
        public double getCpuSystem() {
            return this.cpuSystem_;
        }

        private void setCpuSystem(double value) {
            this.cpuSystem_ = value;
        }

        private void clearCpuSystem() {
            this.cpuSystem_ = 0.0;
        }

        @Override
        public double getTps() {
            return this.tps_;
        }

        private void setTps(double value) {
            this.tps_ = value;
        }

        private void clearTps() {
            this.tps_ = 0.0;
        }

        @Override
        public double getMsptMedian() {
            return this.msptMedian_;
        }

        private void setMsptMedian(double value) {
            this.msptMedian_ = value;
        }

        private void clearMsptMedian() {
            this.msptMedian_ = 0.0;
        }

        @Override
        public double getMsptMax() {
            return this.msptMax_;
        }

        private void setMsptMax(double value) {
            this.msptMax_ = value;
        }

        private void clearMsptMax() {
            this.msptMax_ = 0.0;
        }

        @Override
        public int getPlayers() {
            return this.players_;
        }

        private void setPlayers(int value) {
            this.players_ = value;
        }

        private void clearPlayers() {
            this.players_ = 0;
        }

        @Override
        public int getEntities() {
            return this.entities_;
        }

        private void setEntities(int value) {
            this.entities_ = value;
        }

        private void clearEntities() {
            this.entities_ = 0;
        }

        @Override
        public int getTileEntities() {
            return this.tileEntities_;
        }

        private void setTileEntities(int value) {
            this.tileEntities_ = value;
        }

        private void clearTileEntities() {
            this.tileEntities_ = 0;
        }

        @Override
        public int getChunks() {
            return this.chunks_;
        }

        private void setChunks(int value) {
            this.chunks_ = value;
        }

        private void clearChunks() {
            this.chunks_ = 0;
        }

        @Override
        public long getStartTime() {
            return this.startTime_;
        }

        private void setStartTime(long value) {
            this.startTime_ = value;
        }

        private void clearStartTime() {
            this.startTime_ = 0L;
        }

        @Override
        public long getEndTime() {
            return this.endTime_;
        }

        private void setEndTime(long value) {
            this.endTime_ = value;
        }

        private void clearEndTime() {
            this.endTime_ = 0L;
        }

        @Override
        public int getDuration() {
            return this.duration_;
        }

        private void setDuration(int value) {
            this.duration_ = value;
        }

        private void clearDuration() {
            this.duration_ = 0;
        }

        public static SparkProtos.WindowStatistics parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.WindowStatistics parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.WindowStatistics parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.WindowStatistics parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.WindowStatistics parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.WindowStatistics parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.WindowStatistics parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.WindowStatistics parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.WindowStatistics parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.WindowStatistics parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.WindowStatistics parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.WindowStatistics parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.WindowStatistics.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkProtos.WindowStatistics.Builder newBuilder(SparkProtos.WindowStatistics prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:77)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.instanceOf(StructContext.java:282)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkProtos$WindowStatistics.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$WindowStatistics;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkProtos$WindowStatistics.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkProtos$WindowStatistics$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkProtos.WindowStatistics();
                case NEW_BUILDER:
                    return new SparkProtos.WindowStatistics.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "ticks_", "cpuProcess_", "cpuSystem_", "tps_", "msptMedian_", "msptMax_", "players_", "entities_", "tileEntities_", "chunks_", "startTime_", "endTime_", "duration_" };
                    String info = "\u0000\r\u0000\u0000\u0001\r\r\u0000\u0000\u0000\u0001\u0004\u0002\u0000\u0003\u0000\u0004\u0000\u0005\u0000\u0006\u0000\u0007\u0004\b\u0004\t\u0004\n\u0004\u000b\u0002\f\u0002\r\u0004";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkProtos.WindowStatistics> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkProtos.WindowStatistics.class) {
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

        public static SparkProtos.WindowStatistics getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkProtos.WindowStatistics> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkProtos.WindowStatistics defaultInstance = new SparkProtos.WindowStatistics();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkProtos.WindowStatistics.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.WindowStatistics, SparkProtos.WindowStatistics.Builder> implements SparkProtos.WindowStatisticsOrBuilder {

            private Builder() {
                super(SparkProtos.WindowStatistics.DEFAULT_INSTANCE);
            }

            @Override
            public int getTicks() {
                return this.instance.getTicks();
            }

            public SparkProtos.WindowStatistics.Builder setTicks(int value) {
                this.copyOnWrite();
                this.instance.setTicks(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearTicks() {
                this.copyOnWrite();
                this.instance.clearTicks();
                return this;
            }

            @Override
            public double getCpuProcess() {
                return this.instance.getCpuProcess();
            }

            public SparkProtos.WindowStatistics.Builder setCpuProcess(double value) {
                this.copyOnWrite();
                this.instance.setCpuProcess(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearCpuProcess() {
                this.copyOnWrite();
                this.instance.clearCpuProcess();
                return this;
            }

            @Override
            public double getCpuSystem() {
                return this.instance.getCpuSystem();
            }

            public SparkProtos.WindowStatistics.Builder setCpuSystem(double value) {
                this.copyOnWrite();
                this.instance.setCpuSystem(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearCpuSystem() {
                this.copyOnWrite();
                this.instance.clearCpuSystem();
                return this;
            }

            @Override
            public double getTps() {
                return this.instance.getTps();
            }

            public SparkProtos.WindowStatistics.Builder setTps(double value) {
                this.copyOnWrite();
                this.instance.setTps(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearTps() {
                this.copyOnWrite();
                this.instance.clearTps();
                return this;
            }

            @Override
            public double getMsptMedian() {
                return this.instance.getMsptMedian();
            }

            public SparkProtos.WindowStatistics.Builder setMsptMedian(double value) {
                this.copyOnWrite();
                this.instance.setMsptMedian(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearMsptMedian() {
                this.copyOnWrite();
                this.instance.clearMsptMedian();
                return this;
            }

            @Override
            public double getMsptMax() {
                return this.instance.getMsptMax();
            }

            public SparkProtos.WindowStatistics.Builder setMsptMax(double value) {
                this.copyOnWrite();
                this.instance.setMsptMax(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearMsptMax() {
                this.copyOnWrite();
                this.instance.clearMsptMax();
                return this;
            }

            @Override
            public int getPlayers() {
                return this.instance.getPlayers();
            }

            public SparkProtos.WindowStatistics.Builder setPlayers(int value) {
                this.copyOnWrite();
                this.instance.setPlayers(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearPlayers() {
                this.copyOnWrite();
                this.instance.clearPlayers();
                return this;
            }

            @Override
            public int getEntities() {
                return this.instance.getEntities();
            }

            public SparkProtos.WindowStatistics.Builder setEntities(int value) {
                this.copyOnWrite();
                this.instance.setEntities(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearEntities() {
                this.copyOnWrite();
                this.instance.clearEntities();
                return this;
            }

            @Override
            public int getTileEntities() {
                return this.instance.getTileEntities();
            }

            public SparkProtos.WindowStatistics.Builder setTileEntities(int value) {
                this.copyOnWrite();
                this.instance.setTileEntities(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearTileEntities() {
                this.copyOnWrite();
                this.instance.clearTileEntities();
                return this;
            }

            @Override
            public int getChunks() {
                return this.instance.getChunks();
            }

            public SparkProtos.WindowStatistics.Builder setChunks(int value) {
                this.copyOnWrite();
                this.instance.setChunks(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearChunks() {
                this.copyOnWrite();
                this.instance.clearChunks();
                return this;
            }

            @Override
            public long getStartTime() {
                return this.instance.getStartTime();
            }

            public SparkProtos.WindowStatistics.Builder setStartTime(long value) {
                this.copyOnWrite();
                this.instance.setStartTime(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearStartTime() {
                this.copyOnWrite();
                this.instance.clearStartTime();
                return this;
            }

            @Override
            public long getEndTime() {
                return this.instance.getEndTime();
            }

            public SparkProtos.WindowStatistics.Builder setEndTime(long value) {
                this.copyOnWrite();
                this.instance.setEndTime(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearEndTime() {
                this.copyOnWrite();
                this.instance.clearEndTime();
                return this;
            }

            @Override
            public int getDuration() {
                return this.instance.getDuration();
            }

            public SparkProtos.WindowStatistics.Builder setDuration(int value) {
                this.copyOnWrite();
                this.instance.setDuration(value);
                return this;
            }

            public SparkProtos.WindowStatistics.Builder clearDuration() {
                this.copyOnWrite();
                this.instance.clearDuration();
                return this;
            }
        }
    }

    public interface WindowStatisticsOrBuilder extends MessageLiteOrBuilder {

        int getTicks();

        double getCpuProcess();

        double getCpuSystem();

        double getTps();

        double getMsptMedian();

        double getMsptMax();

        int getPlayers();

        int getEntities();

        int getTileEntities();

        int getChunks();

        long getStartTime();

        long getEndTime();

        int getDuration();
    }

    public static final class WorldStatistics extends GeneratedMessageLite<SparkProtos.WorldStatistics, SparkProtos.WorldStatistics.Builder> implements SparkProtos.WorldStatisticsOrBuilder {

        public static final int TOTAL_ENTITIES_FIELD_NUMBER = 1;

        private int totalEntities_;

        public static final int ENTITY_COUNTS_FIELD_NUMBER = 2;

        private MapFieldLite<String, Integer> entityCounts_ = MapFieldLite.emptyMapField();

        public static final int WORLDS_FIELD_NUMBER = 3;

        private Internal.ProtobufList<SparkProtos.WorldStatistics.World> worlds_ = emptyProtobufList();

        private static final SparkProtos.WorldStatistics DEFAULT_INSTANCE;

        private static volatile Parser<SparkProtos.WorldStatistics> PARSER;

        private WorldStatistics() {
        }

        @Override
        public int getTotalEntities() {
            return this.totalEntities_;
        }

        private void setTotalEntities(int value) {
            this.totalEntities_ = value;
        }

        private void clearTotalEntities() {
            this.totalEntities_ = 0;
        }

        private MapFieldLite<String, Integer> internalGetEntityCounts() {
            return this.entityCounts_;
        }

        private MapFieldLite<String, Integer> internalGetMutableEntityCounts() {
            if (!this.entityCounts_.isMutable()) {
                this.entityCounts_ = this.entityCounts_.mutableCopy();
            }
            return this.entityCounts_;
        }

        @Override
        public int getEntityCountsCount() {
            return this.internalGetEntityCounts().size();
        }

        @Override
        public boolean containsEntityCounts(String key) {
            Class<?> keyClass = key.getClass();
            return this.internalGetEntityCounts().containsKey(key);
        }

        @Deprecated
        @Override
        public Map<String, Integer> getEntityCounts() {
            return this.getEntityCountsMap();
        }

        @Override
        public Map<String, Integer> getEntityCountsMap() {
            return Collections.unmodifiableMap(this.internalGetEntityCounts());
        }

        @Override
        public int getEntityCountsOrDefault(String key, int defaultValue) {
            Class<?> keyClass = key.getClass();
            Map<String, Integer> map = this.internalGetEntityCounts();
            return map.containsKey(key) ? (Integer) map.get(key) : defaultValue;
        }

        @Override
        public int getEntityCountsOrThrow(String key) {
            Class<?> keyClass = key.getClass();
            Map<String, Integer> map = this.internalGetEntityCounts();
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException();
            } else {
                return (Integer) map.get(key);
            }
        }

        private Map<String, Integer> getMutableEntityCountsMap() {
            return this.internalGetMutableEntityCounts();
        }

        @Override
        public List<SparkProtos.WorldStatistics.World> getWorldsList() {
            return this.worlds_;
        }

        public List<? extends SparkProtos.WorldStatistics.WorldOrBuilder> getWorldsOrBuilderList() {
            return this.worlds_;
        }

        @Override
        public int getWorldsCount() {
            return this.worlds_.size();
        }

        @Override
        public SparkProtos.WorldStatistics.World getWorlds(int index) {
            return (SparkProtos.WorldStatistics.World) this.worlds_.get(index);
        }

        public SparkProtos.WorldStatistics.WorldOrBuilder getWorldsOrBuilder(int index) {
            return (SparkProtos.WorldStatistics.WorldOrBuilder) this.worlds_.get(index);
        }

        private void ensureWorldsIsMutable() {
            Internal.ProtobufList<SparkProtos.WorldStatistics.World> tmp = this.worlds_;
            if (!tmp.isModifiable()) {
                this.worlds_ = GeneratedMessageLite.mutableCopy(tmp);
            }
        }

        private void setWorlds(int index, SparkProtos.WorldStatistics.World value) {
            value.getClass();
            this.ensureWorldsIsMutable();
            this.worlds_.set(index, value);
        }

        private void addWorlds(SparkProtos.WorldStatistics.World value) {
            value.getClass();
            this.ensureWorldsIsMutable();
            this.worlds_.add(value);
        }

        private void addWorlds(int index, SparkProtos.WorldStatistics.World value) {
            value.getClass();
            this.ensureWorldsIsMutable();
            this.worlds_.add(index, value);
        }

        private void addAllWorlds(Iterable<? extends SparkProtos.WorldStatistics.World> values) {
            this.ensureWorldsIsMutable();
            AbstractMessageLite.addAll(values, this.worlds_);
        }

        private void clearWorlds() {
            this.worlds_ = emptyProtobufList();
        }

        private void removeWorlds(int index) {
            this.ensureWorldsIsMutable();
            this.worlds_.remove(index);
        }

        public static SparkProtos.WorldStatistics parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.WorldStatistics parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.WorldStatistics parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.WorldStatistics parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.WorldStatistics parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkProtos.WorldStatistics parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkProtos.WorldStatistics parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.WorldStatistics parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.WorldStatistics parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.WorldStatistics parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.WorldStatistics parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkProtos.WorldStatistics parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkProtos.WorldStatistics.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkProtos.WorldStatistics.Builder newBuilder(SparkProtos.WorldStatistics prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:77)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.instanceOf(StructContext.java:282)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkProtos$WorldStatistics.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$WorldStatistics;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkProtos$WorldStatistics.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkProtos$WorldStatistics$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkProtos.WorldStatistics();
                case NEW_BUILDER:
                    return new SparkProtos.WorldStatistics.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "totalEntities_", "entityCounts_", SparkProtos.WorldStatistics.EntityCountsDefaultEntryHolder.defaultEntry, "worlds_", SparkProtos.WorldStatistics.World.class };
                    String info = "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0001\u0001\u0000\u0001\u0004\u00022\u0003\u001b";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkProtos.WorldStatistics> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkProtos.WorldStatistics.class) {
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

        public static SparkProtos.WorldStatistics getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkProtos.WorldStatistics> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkProtos.WorldStatistics defaultInstance = new SparkProtos.WorldStatistics();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkProtos.WorldStatistics.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.WorldStatistics, SparkProtos.WorldStatistics.Builder> implements SparkProtos.WorldStatisticsOrBuilder {

            private Builder() {
                super(SparkProtos.WorldStatistics.DEFAULT_INSTANCE);
            }

            @Override
            public int getTotalEntities() {
                return this.instance.getTotalEntities();
            }

            public SparkProtos.WorldStatistics.Builder setTotalEntities(int value) {
                this.copyOnWrite();
                this.instance.setTotalEntities(value);
                return this;
            }

            public SparkProtos.WorldStatistics.Builder clearTotalEntities() {
                this.copyOnWrite();
                this.instance.clearTotalEntities();
                return this;
            }

            @Override
            public int getEntityCountsCount() {
                return this.instance.getEntityCountsMap().size();
            }

            @Override
            public boolean containsEntityCounts(String key) {
                Class<?> keyClass = key.getClass();
                return this.instance.getEntityCountsMap().containsKey(key);
            }

            public SparkProtos.WorldStatistics.Builder clearEntityCounts() {
                this.copyOnWrite();
                this.instance.getMutableEntityCountsMap().clear();
                return this;
            }

            public SparkProtos.WorldStatistics.Builder removeEntityCounts(String key) {
                Class<?> keyClass = key.getClass();
                this.copyOnWrite();
                this.instance.getMutableEntityCountsMap().remove(key);
                return this;
            }

            @Deprecated
            @Override
            public Map<String, Integer> getEntityCounts() {
                return this.getEntityCountsMap();
            }

            @Override
            public Map<String, Integer> getEntityCountsMap() {
                return Collections.unmodifiableMap(this.instance.getEntityCountsMap());
            }

            @Override
            public int getEntityCountsOrDefault(String key, int defaultValue) {
                Class<?> keyClass = key.getClass();
                Map<String, Integer> map = this.instance.getEntityCountsMap();
                return map.containsKey(key) ? (Integer) map.get(key) : defaultValue;
            }

            @Override
            public int getEntityCountsOrThrow(String key) {
                Class<?> keyClass = key.getClass();
                Map<String, Integer> map = this.instance.getEntityCountsMap();
                if (!map.containsKey(key)) {
                    throw new IllegalArgumentException();
                } else {
                    return (Integer) map.get(key);
                }
            }

            public SparkProtos.WorldStatistics.Builder putEntityCounts(String key, int value) {
                Class<?> keyClass = key.getClass();
                this.copyOnWrite();
                this.instance.getMutableEntityCountsMap().put(key, value);
                return this;
            }

            public SparkProtos.WorldStatistics.Builder putAllEntityCounts(Map<String, Integer> values) {
                this.copyOnWrite();
                this.instance.getMutableEntityCountsMap().putAll(values);
                return this;
            }

            @Override
            public List<SparkProtos.WorldStatistics.World> getWorldsList() {
                return Collections.unmodifiableList(this.instance.getWorldsList());
            }

            @Override
            public int getWorldsCount() {
                return this.instance.getWorldsCount();
            }

            @Override
            public SparkProtos.WorldStatistics.World getWorlds(int index) {
                return this.instance.getWorlds(index);
            }

            public SparkProtos.WorldStatistics.Builder setWorlds(int index, SparkProtos.WorldStatistics.World value) {
                this.copyOnWrite();
                this.instance.setWorlds(index, value);
                return this;
            }

            public SparkProtos.WorldStatistics.Builder setWorlds(int index, SparkProtos.WorldStatistics.World.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setWorlds(index, builderForValue.build());
                return this;
            }

            public SparkProtos.WorldStatistics.Builder addWorlds(SparkProtos.WorldStatistics.World value) {
                this.copyOnWrite();
                this.instance.addWorlds(value);
                return this;
            }

            public SparkProtos.WorldStatistics.Builder addWorlds(int index, SparkProtos.WorldStatistics.World value) {
                this.copyOnWrite();
                this.instance.addWorlds(index, value);
                return this;
            }

            public SparkProtos.WorldStatistics.Builder addWorlds(SparkProtos.WorldStatistics.World.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.addWorlds(builderForValue.build());
                return this;
            }

            public SparkProtos.WorldStatistics.Builder addWorlds(int index, SparkProtos.WorldStatistics.World.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.addWorlds(index, builderForValue.build());
                return this;
            }

            public SparkProtos.WorldStatistics.Builder addAllWorlds(Iterable<? extends SparkProtos.WorldStatistics.World> values) {
                this.copyOnWrite();
                this.instance.addAllWorlds(values);
                return this;
            }

            public SparkProtos.WorldStatistics.Builder clearWorlds() {
                this.copyOnWrite();
                this.instance.clearWorlds();
                return this;
            }

            public SparkProtos.WorldStatistics.Builder removeWorlds(int index) {
                this.copyOnWrite();
                this.instance.removeWorlds(index);
                return this;
            }
        }

        public static final class Chunk extends GeneratedMessageLite<SparkProtos.WorldStatistics.Chunk, SparkProtos.WorldStatistics.Chunk.Builder> implements SparkProtos.WorldStatistics.ChunkOrBuilder {

            public static final int X_FIELD_NUMBER = 1;

            private int x_;

            public static final int Z_FIELD_NUMBER = 2;

            private int z_;

            public static final int TOTAL_ENTITIES_FIELD_NUMBER = 3;

            private int totalEntities_;

            public static final int ENTITY_COUNTS_FIELD_NUMBER = 4;

            private MapFieldLite<String, Integer> entityCounts_ = MapFieldLite.emptyMapField();

            private static final SparkProtos.WorldStatistics.Chunk DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.WorldStatistics.Chunk> PARSER;

            private Chunk() {
            }

            @Override
            public int getX() {
                return this.x_;
            }

            private void setX(int value) {
                this.x_ = value;
            }

            private void clearX() {
                this.x_ = 0;
            }

            @Override
            public int getZ() {
                return this.z_;
            }

            private void setZ(int value) {
                this.z_ = value;
            }

            private void clearZ() {
                this.z_ = 0;
            }

            @Override
            public int getTotalEntities() {
                return this.totalEntities_;
            }

            private void setTotalEntities(int value) {
                this.totalEntities_ = value;
            }

            private void clearTotalEntities() {
                this.totalEntities_ = 0;
            }

            private MapFieldLite<String, Integer> internalGetEntityCounts() {
                return this.entityCounts_;
            }

            private MapFieldLite<String, Integer> internalGetMutableEntityCounts() {
                if (!this.entityCounts_.isMutable()) {
                    this.entityCounts_ = this.entityCounts_.mutableCopy();
                }
                return this.entityCounts_;
            }

            @Override
            public int getEntityCountsCount() {
                return this.internalGetEntityCounts().size();
            }

            @Override
            public boolean containsEntityCounts(String key) {
                Class<?> keyClass = key.getClass();
                return this.internalGetEntityCounts().containsKey(key);
            }

            @Deprecated
            @Override
            public Map<String, Integer> getEntityCounts() {
                return this.getEntityCountsMap();
            }

            @Override
            public Map<String, Integer> getEntityCountsMap() {
                return Collections.unmodifiableMap(this.internalGetEntityCounts());
            }

            @Override
            public int getEntityCountsOrDefault(String key, int defaultValue) {
                Class<?> keyClass = key.getClass();
                Map<String, Integer> map = this.internalGetEntityCounts();
                return map.containsKey(key) ? (Integer) map.get(key) : defaultValue;
            }

            @Override
            public int getEntityCountsOrThrow(String key) {
                Class<?> keyClass = key.getClass();
                Map<String, Integer> map = this.internalGetEntityCounts();
                if (!map.containsKey(key)) {
                    throw new IllegalArgumentException();
                } else {
                    return (Integer) map.get(key);
                }
            }

            private Map<String, Integer> getMutableEntityCountsMap() {
                return this.internalGetMutableEntityCounts();
            }

            public static SparkProtos.WorldStatistics.Chunk parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.WorldStatistics.Chunk parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.Chunk parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.WorldStatistics.Chunk parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.Chunk parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.WorldStatistics.Chunk parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.Chunk parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.WorldStatistics.Chunk parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.Chunk parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.WorldStatistics.Chunk parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.Chunk parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.WorldStatistics.Chunk parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.Chunk.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.WorldStatistics.Chunk.Builder newBuilder(SparkProtos.WorldStatistics.Chunk prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$WorldStatistics$Chunk.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$WorldStatistics$Chunk;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$WorldStatistics$Chunk.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$WorldStatistics$Chunk$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.WorldStatistics.Chunk();
                    case NEW_BUILDER:
                        return new SparkProtos.WorldStatistics.Chunk.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "x_", "z_", "totalEntities_", "entityCounts_", SparkProtos.WorldStatistics.Chunk.EntityCountsDefaultEntryHolder.defaultEntry };
                        String info = "\u0000\u0004\u0000\u0000\u0001\u0004\u0004\u0001\u0000\u0000\u0001\u0004\u0002\u0004\u0003\u0004\u00042";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.WorldStatistics.Chunk> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.WorldStatistics.Chunk.class) {
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

            public static SparkProtos.WorldStatistics.Chunk getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.WorldStatistics.Chunk> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.WorldStatistics.Chunk defaultInstance = new SparkProtos.WorldStatistics.Chunk();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.WorldStatistics.Chunk.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.WorldStatistics.Chunk, SparkProtos.WorldStatistics.Chunk.Builder> implements SparkProtos.WorldStatistics.ChunkOrBuilder {

                private Builder() {
                    super(SparkProtos.WorldStatistics.Chunk.DEFAULT_INSTANCE);
                }

                @Override
                public int getX() {
                    return this.instance.getX();
                }

                public SparkProtos.WorldStatistics.Chunk.Builder setX(int value) {
                    this.copyOnWrite();
                    this.instance.setX(value);
                    return this;
                }

                public SparkProtos.WorldStatistics.Chunk.Builder clearX() {
                    this.copyOnWrite();
                    this.instance.clearX();
                    return this;
                }

                @Override
                public int getZ() {
                    return this.instance.getZ();
                }

                public SparkProtos.WorldStatistics.Chunk.Builder setZ(int value) {
                    this.copyOnWrite();
                    this.instance.setZ(value);
                    return this;
                }

                public SparkProtos.WorldStatistics.Chunk.Builder clearZ() {
                    this.copyOnWrite();
                    this.instance.clearZ();
                    return this;
                }

                @Override
                public int getTotalEntities() {
                    return this.instance.getTotalEntities();
                }

                public SparkProtos.WorldStatistics.Chunk.Builder setTotalEntities(int value) {
                    this.copyOnWrite();
                    this.instance.setTotalEntities(value);
                    return this;
                }

                public SparkProtos.WorldStatistics.Chunk.Builder clearTotalEntities() {
                    this.copyOnWrite();
                    this.instance.clearTotalEntities();
                    return this;
                }

                @Override
                public int getEntityCountsCount() {
                    return this.instance.getEntityCountsMap().size();
                }

                @Override
                public boolean containsEntityCounts(String key) {
                    Class<?> keyClass = key.getClass();
                    return this.instance.getEntityCountsMap().containsKey(key);
                }

                public SparkProtos.WorldStatistics.Chunk.Builder clearEntityCounts() {
                    this.copyOnWrite();
                    this.instance.getMutableEntityCountsMap().clear();
                    return this;
                }

                public SparkProtos.WorldStatistics.Chunk.Builder removeEntityCounts(String key) {
                    Class<?> keyClass = key.getClass();
                    this.copyOnWrite();
                    this.instance.getMutableEntityCountsMap().remove(key);
                    return this;
                }

                @Deprecated
                @Override
                public Map<String, Integer> getEntityCounts() {
                    return this.getEntityCountsMap();
                }

                @Override
                public Map<String, Integer> getEntityCountsMap() {
                    return Collections.unmodifiableMap(this.instance.getEntityCountsMap());
                }

                @Override
                public int getEntityCountsOrDefault(String key, int defaultValue) {
                    Class<?> keyClass = key.getClass();
                    Map<String, Integer> map = this.instance.getEntityCountsMap();
                    return map.containsKey(key) ? (Integer) map.get(key) : defaultValue;
                }

                @Override
                public int getEntityCountsOrThrow(String key) {
                    Class<?> keyClass = key.getClass();
                    Map<String, Integer> map = this.instance.getEntityCountsMap();
                    if (!map.containsKey(key)) {
                        throw new IllegalArgumentException();
                    } else {
                        return (Integer) map.get(key);
                    }
                }

                public SparkProtos.WorldStatistics.Chunk.Builder putEntityCounts(String key, int value) {
                    Class<?> keyClass = key.getClass();
                    this.copyOnWrite();
                    this.instance.getMutableEntityCountsMap().put(key, value);
                    return this;
                }

                public SparkProtos.WorldStatistics.Chunk.Builder putAllEntityCounts(Map<String, Integer> values) {
                    this.copyOnWrite();
                    this.instance.getMutableEntityCountsMap().putAll(values);
                    return this;
                }
            }

            private static final class EntityCountsDefaultEntryHolder {

                static final MapEntryLite<String, Integer> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.STRING, "", WireFormat.FieldType.INT32, 0);
            }
        }

        public interface ChunkOrBuilder extends MessageLiteOrBuilder {

            int getX();

            int getZ();

            int getTotalEntities();

            int getEntityCountsCount();

            boolean containsEntityCounts(String var1);

            @Deprecated
            Map<String, Integer> getEntityCounts();

            Map<String, Integer> getEntityCountsMap();

            int getEntityCountsOrDefault(String var1, int var2);

            int getEntityCountsOrThrow(String var1);
        }

        private static final class EntityCountsDefaultEntryHolder {

            static final MapEntryLite<String, Integer> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.STRING, "", WireFormat.FieldType.INT32, 0);
        }

        public static final class Region extends GeneratedMessageLite<SparkProtos.WorldStatistics.Region, SparkProtos.WorldStatistics.Region.Builder> implements SparkProtos.WorldStatistics.RegionOrBuilder {

            public static final int TOTAL_ENTITIES_FIELD_NUMBER = 1;

            private int totalEntities_;

            public static final int CHUNKS_FIELD_NUMBER = 2;

            private Internal.ProtobufList<SparkProtos.WorldStatistics.Chunk> chunks_ = emptyProtobufList();

            private static final SparkProtos.WorldStatistics.Region DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.WorldStatistics.Region> PARSER;

            private Region() {
            }

            @Override
            public int getTotalEntities() {
                return this.totalEntities_;
            }

            private void setTotalEntities(int value) {
                this.totalEntities_ = value;
            }

            private void clearTotalEntities() {
                this.totalEntities_ = 0;
            }

            @Override
            public List<SparkProtos.WorldStatistics.Chunk> getChunksList() {
                return this.chunks_;
            }

            public List<? extends SparkProtos.WorldStatistics.ChunkOrBuilder> getChunksOrBuilderList() {
                return this.chunks_;
            }

            @Override
            public int getChunksCount() {
                return this.chunks_.size();
            }

            @Override
            public SparkProtos.WorldStatistics.Chunk getChunks(int index) {
                return (SparkProtos.WorldStatistics.Chunk) this.chunks_.get(index);
            }

            public SparkProtos.WorldStatistics.ChunkOrBuilder getChunksOrBuilder(int index) {
                return (SparkProtos.WorldStatistics.ChunkOrBuilder) this.chunks_.get(index);
            }

            private void ensureChunksIsMutable() {
                Internal.ProtobufList<SparkProtos.WorldStatistics.Chunk> tmp = this.chunks_;
                if (!tmp.isModifiable()) {
                    this.chunks_ = GeneratedMessageLite.mutableCopy(tmp);
                }
            }

            private void setChunks(int index, SparkProtos.WorldStatistics.Chunk value) {
                value.getClass();
                this.ensureChunksIsMutable();
                this.chunks_.set(index, value);
            }

            private void addChunks(SparkProtos.WorldStatistics.Chunk value) {
                value.getClass();
                this.ensureChunksIsMutable();
                this.chunks_.add(value);
            }

            private void addChunks(int index, SparkProtos.WorldStatistics.Chunk value) {
                value.getClass();
                this.ensureChunksIsMutable();
                this.chunks_.add(index, value);
            }

            private void addAllChunks(Iterable<? extends SparkProtos.WorldStatistics.Chunk> values) {
                this.ensureChunksIsMutable();
                AbstractMessageLite.addAll(values, this.chunks_);
            }

            private void clearChunks() {
                this.chunks_ = emptyProtobufList();
            }

            private void removeChunks(int index) {
                this.ensureChunksIsMutable();
                this.chunks_.remove(index);
            }

            public static SparkProtos.WorldStatistics.Region parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.WorldStatistics.Region parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.Region parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.WorldStatistics.Region parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.Region parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.WorldStatistics.Region parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.Region parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.WorldStatistics.Region parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.Region parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.WorldStatistics.Region parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.Region parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.WorldStatistics.Region parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.Region.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.WorldStatistics.Region.Builder newBuilder(SparkProtos.WorldStatistics.Region prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$WorldStatistics$Region.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$WorldStatistics$Region;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$WorldStatistics$Region.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$WorldStatistics$Region$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.WorldStatistics.Region();
                    case NEW_BUILDER:
                        return new SparkProtos.WorldStatistics.Region.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "totalEntities_", "chunks_", SparkProtos.WorldStatistics.Chunk.class };
                        String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0001\u0000\u0001\u0004\u0002\u001b";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.WorldStatistics.Region> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.WorldStatistics.Region.class) {
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

            public static SparkProtos.WorldStatistics.Region getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.WorldStatistics.Region> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.WorldStatistics.Region defaultInstance = new SparkProtos.WorldStatistics.Region();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.WorldStatistics.Region.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.WorldStatistics.Region, SparkProtos.WorldStatistics.Region.Builder> implements SparkProtos.WorldStatistics.RegionOrBuilder {

                private Builder() {
                    super(SparkProtos.WorldStatistics.Region.DEFAULT_INSTANCE);
                }

                @Override
                public int getTotalEntities() {
                    return this.instance.getTotalEntities();
                }

                public SparkProtos.WorldStatistics.Region.Builder setTotalEntities(int value) {
                    this.copyOnWrite();
                    this.instance.setTotalEntities(value);
                    return this;
                }

                public SparkProtos.WorldStatistics.Region.Builder clearTotalEntities() {
                    this.copyOnWrite();
                    this.instance.clearTotalEntities();
                    return this;
                }

                @Override
                public List<SparkProtos.WorldStatistics.Chunk> getChunksList() {
                    return Collections.unmodifiableList(this.instance.getChunksList());
                }

                @Override
                public int getChunksCount() {
                    return this.instance.getChunksCount();
                }

                @Override
                public SparkProtos.WorldStatistics.Chunk getChunks(int index) {
                    return this.instance.getChunks(index);
                }

                public SparkProtos.WorldStatistics.Region.Builder setChunks(int index, SparkProtos.WorldStatistics.Chunk value) {
                    this.copyOnWrite();
                    this.instance.setChunks(index, value);
                    return this;
                }

                public SparkProtos.WorldStatistics.Region.Builder setChunks(int index, SparkProtos.WorldStatistics.Chunk.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setChunks(index, builderForValue.build());
                    return this;
                }

                public SparkProtos.WorldStatistics.Region.Builder addChunks(SparkProtos.WorldStatistics.Chunk value) {
                    this.copyOnWrite();
                    this.instance.addChunks(value);
                    return this;
                }

                public SparkProtos.WorldStatistics.Region.Builder addChunks(int index, SparkProtos.WorldStatistics.Chunk value) {
                    this.copyOnWrite();
                    this.instance.addChunks(index, value);
                    return this;
                }

                public SparkProtos.WorldStatistics.Region.Builder addChunks(SparkProtos.WorldStatistics.Chunk.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.addChunks(builderForValue.build());
                    return this;
                }

                public SparkProtos.WorldStatistics.Region.Builder addChunks(int index, SparkProtos.WorldStatistics.Chunk.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.addChunks(index, builderForValue.build());
                    return this;
                }

                public SparkProtos.WorldStatistics.Region.Builder addAllChunks(Iterable<? extends SparkProtos.WorldStatistics.Chunk> values) {
                    this.copyOnWrite();
                    this.instance.addAllChunks(values);
                    return this;
                }

                public SparkProtos.WorldStatistics.Region.Builder clearChunks() {
                    this.copyOnWrite();
                    this.instance.clearChunks();
                    return this;
                }

                public SparkProtos.WorldStatistics.Region.Builder removeChunks(int index) {
                    this.copyOnWrite();
                    this.instance.removeChunks(index);
                    return this;
                }
            }
        }

        public interface RegionOrBuilder extends MessageLiteOrBuilder {

            int getTotalEntities();

            List<SparkProtos.WorldStatistics.Chunk> getChunksList();

            SparkProtos.WorldStatistics.Chunk getChunks(int var1);

            int getChunksCount();
        }

        public static final class World extends GeneratedMessageLite<SparkProtos.WorldStatistics.World, SparkProtos.WorldStatistics.World.Builder> implements SparkProtos.WorldStatistics.WorldOrBuilder {

            public static final int NAME_FIELD_NUMBER = 1;

            private String name_ = "";

            public static final int TOTAL_ENTITIES_FIELD_NUMBER = 2;

            private int totalEntities_;

            public static final int REGIONS_FIELD_NUMBER = 3;

            private Internal.ProtobufList<SparkProtos.WorldStatistics.Region> regions_ = emptyProtobufList();

            private static final SparkProtos.WorldStatistics.World DEFAULT_INSTANCE;

            private static volatile Parser<SparkProtos.WorldStatistics.World> PARSER;

            private World() {
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
            public int getTotalEntities() {
                return this.totalEntities_;
            }

            private void setTotalEntities(int value) {
                this.totalEntities_ = value;
            }

            private void clearTotalEntities() {
                this.totalEntities_ = 0;
            }

            @Override
            public List<SparkProtos.WorldStatistics.Region> getRegionsList() {
                return this.regions_;
            }

            public List<? extends SparkProtos.WorldStatistics.RegionOrBuilder> getRegionsOrBuilderList() {
                return this.regions_;
            }

            @Override
            public int getRegionsCount() {
                return this.regions_.size();
            }

            @Override
            public SparkProtos.WorldStatistics.Region getRegions(int index) {
                return (SparkProtos.WorldStatistics.Region) this.regions_.get(index);
            }

            public SparkProtos.WorldStatistics.RegionOrBuilder getRegionsOrBuilder(int index) {
                return (SparkProtos.WorldStatistics.RegionOrBuilder) this.regions_.get(index);
            }

            private void ensureRegionsIsMutable() {
                Internal.ProtobufList<SparkProtos.WorldStatistics.Region> tmp = this.regions_;
                if (!tmp.isModifiable()) {
                    this.regions_ = GeneratedMessageLite.mutableCopy(tmp);
                }
            }

            private void setRegions(int index, SparkProtos.WorldStatistics.Region value) {
                value.getClass();
                this.ensureRegionsIsMutable();
                this.regions_.set(index, value);
            }

            private void addRegions(SparkProtos.WorldStatistics.Region value) {
                value.getClass();
                this.ensureRegionsIsMutable();
                this.regions_.add(value);
            }

            private void addRegions(int index, SparkProtos.WorldStatistics.Region value) {
                value.getClass();
                this.ensureRegionsIsMutable();
                this.regions_.add(index, value);
            }

            private void addAllRegions(Iterable<? extends SparkProtos.WorldStatistics.Region> values) {
                this.ensureRegionsIsMutable();
                AbstractMessageLite.addAll(values, this.regions_);
            }

            private void clearRegions() {
                this.regions_ = emptyProtobufList();
            }

            private void removeRegions(int index) {
                this.ensureRegionsIsMutable();
                this.regions_.remove(index);
            }

            public static SparkProtos.WorldStatistics.World parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.WorldStatistics.World parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.World parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.WorldStatistics.World parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.World parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkProtos.WorldStatistics.World parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.World parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.WorldStatistics.World parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.World parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.WorldStatistics.World parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.World parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkProtos.WorldStatistics.World parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkProtos.WorldStatistics.World.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkProtos.WorldStatistics.World.Builder newBuilder(SparkProtos.WorldStatistics.World prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkProtos$WorldStatistics$World.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkProtos$WorldStatistics$World;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkProtos$WorldStatistics$World.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkProtos$WorldStatistics$World$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkProtos.WorldStatistics.World();
                    case NEW_BUILDER:
                        return new SparkProtos.WorldStatistics.World.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "name_", "totalEntities_", "regions_", SparkProtos.WorldStatistics.Region.class };
                        String info = "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0001\u0000\u0001Ȉ\u0002\u0004\u0003\u001b";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkProtos.WorldStatistics.World> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkProtos.WorldStatistics.World.class) {
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

            public static SparkProtos.WorldStatistics.World getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkProtos.WorldStatistics.World> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkProtos.WorldStatistics.World defaultInstance = new SparkProtos.WorldStatistics.World();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkProtos.WorldStatistics.World.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkProtos.WorldStatistics.World, SparkProtos.WorldStatistics.World.Builder> implements SparkProtos.WorldStatistics.WorldOrBuilder {

                private Builder() {
                    super(SparkProtos.WorldStatistics.World.DEFAULT_INSTANCE);
                }

                @Override
                public String getName() {
                    return this.instance.getName();
                }

                @Override
                public ByteString getNameBytes() {
                    return this.instance.getNameBytes();
                }

                public SparkProtos.WorldStatistics.World.Builder setName(String value) {
                    this.copyOnWrite();
                    this.instance.setName(value);
                    return this;
                }

                public SparkProtos.WorldStatistics.World.Builder clearName() {
                    this.copyOnWrite();
                    this.instance.clearName();
                    return this;
                }

                public SparkProtos.WorldStatistics.World.Builder setNameBytes(ByteString value) {
                    this.copyOnWrite();
                    this.instance.setNameBytes(value);
                    return this;
                }

                @Override
                public int getTotalEntities() {
                    return this.instance.getTotalEntities();
                }

                public SparkProtos.WorldStatistics.World.Builder setTotalEntities(int value) {
                    this.copyOnWrite();
                    this.instance.setTotalEntities(value);
                    return this;
                }

                public SparkProtos.WorldStatistics.World.Builder clearTotalEntities() {
                    this.copyOnWrite();
                    this.instance.clearTotalEntities();
                    return this;
                }

                @Override
                public List<SparkProtos.WorldStatistics.Region> getRegionsList() {
                    return Collections.unmodifiableList(this.instance.getRegionsList());
                }

                @Override
                public int getRegionsCount() {
                    return this.instance.getRegionsCount();
                }

                @Override
                public SparkProtos.WorldStatistics.Region getRegions(int index) {
                    return this.instance.getRegions(index);
                }

                public SparkProtos.WorldStatistics.World.Builder setRegions(int index, SparkProtos.WorldStatistics.Region value) {
                    this.copyOnWrite();
                    this.instance.setRegions(index, value);
                    return this;
                }

                public SparkProtos.WorldStatistics.World.Builder setRegions(int index, SparkProtos.WorldStatistics.Region.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.setRegions(index, builderForValue.build());
                    return this;
                }

                public SparkProtos.WorldStatistics.World.Builder addRegions(SparkProtos.WorldStatistics.Region value) {
                    this.copyOnWrite();
                    this.instance.addRegions(value);
                    return this;
                }

                public SparkProtos.WorldStatistics.World.Builder addRegions(int index, SparkProtos.WorldStatistics.Region value) {
                    this.copyOnWrite();
                    this.instance.addRegions(index, value);
                    return this;
                }

                public SparkProtos.WorldStatistics.World.Builder addRegions(SparkProtos.WorldStatistics.Region.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.addRegions(builderForValue.build());
                    return this;
                }

                public SparkProtos.WorldStatistics.World.Builder addRegions(int index, SparkProtos.WorldStatistics.Region.Builder builderForValue) {
                    this.copyOnWrite();
                    this.instance.addRegions(index, builderForValue.build());
                    return this;
                }

                public SparkProtos.WorldStatistics.World.Builder addAllRegions(Iterable<? extends SparkProtos.WorldStatistics.Region> values) {
                    this.copyOnWrite();
                    this.instance.addAllRegions(values);
                    return this;
                }

                public SparkProtos.WorldStatistics.World.Builder clearRegions() {
                    this.copyOnWrite();
                    this.instance.clearRegions();
                    return this;
                }

                public SparkProtos.WorldStatistics.World.Builder removeRegions(int index) {
                    this.copyOnWrite();
                    this.instance.removeRegions(index);
                    return this;
                }
            }
        }

        public interface WorldOrBuilder extends MessageLiteOrBuilder {

            String getName();

            ByteString getNameBytes();

            int getTotalEntities();

            List<SparkProtos.WorldStatistics.Region> getRegionsList();

            SparkProtos.WorldStatistics.Region getRegions(int var1);

            int getRegionsCount();
        }
    }

    public interface WorldStatisticsOrBuilder extends MessageLiteOrBuilder {

        int getTotalEntities();

        int getEntityCountsCount();

        boolean containsEntityCounts(String var1);

        @Deprecated
        Map<String, Integer> getEntityCounts();

        Map<String, Integer> getEntityCountsMap();

        int getEntityCountsOrDefault(String var1, int var2);

        int getEntityCountsOrThrow(String var1);

        List<SparkProtos.WorldStatistics.World> getWorldsList();

        SparkProtos.WorldStatistics.World getWorlds(int var1);

        int getWorldsCount();
    }
}