package me.lucko.spark.proto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import me.lucko.spark.lib.protobuf.ByteString;
import me.lucko.spark.lib.protobuf.CodedInputStream;
import me.lucko.spark.lib.protobuf.ExtensionRegistryLite;
import me.lucko.spark.lib.protobuf.GeneratedMessageLite;
import me.lucko.spark.lib.protobuf.Internal;
import me.lucko.spark.lib.protobuf.InvalidProtocolBufferException;
import me.lucko.spark.lib.protobuf.MessageLiteOrBuilder;
import me.lucko.spark.lib.protobuf.Parser;

public final class SparkWebSocketProtos {

    private SparkWebSocketProtos() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static final class ClientConnect extends GeneratedMessageLite<SparkWebSocketProtos.ClientConnect, SparkWebSocketProtos.ClientConnect.Builder> implements SparkWebSocketProtos.ClientConnectOrBuilder {

        public static final int CLIENT_ID_FIELD_NUMBER = 1;

        private String clientId_ = "";

        public static final int DESCRIPTION_FIELD_NUMBER = 2;

        private String description_ = "";

        private static final SparkWebSocketProtos.ClientConnect DEFAULT_INSTANCE;

        private static volatile Parser<SparkWebSocketProtos.ClientConnect> PARSER;

        private ClientConnect() {
        }

        @Override
        public String getClientId() {
            return this.clientId_;
        }

        @Override
        public ByteString getClientIdBytes() {
            return ByteString.copyFromUtf8(this.clientId_);
        }

        private void setClientId(String value) {
            Class<?> valueClass = value.getClass();
            this.clientId_ = value;
        }

        private void clearClientId() {
            this.clientId_ = getDefaultInstance().getClientId();
        }

        private void setClientIdBytes(ByteString value) {
            checkByteStringIsUtf8(value);
            this.clientId_ = value.toStringUtf8();
        }

        @Override
        public String getDescription() {
            return this.description_;
        }

        @Override
        public ByteString getDescriptionBytes() {
            return ByteString.copyFromUtf8(this.description_);
        }

        private void setDescription(String value) {
            Class<?> valueClass = value.getClass();
            this.description_ = value;
        }

        private void clearDescription() {
            this.description_ = getDefaultInstance().getDescription();
        }

        private void setDescriptionBytes(ByteString value) {
            checkByteStringIsUtf8(value);
            this.description_ = value.toStringUtf8();
        }

        public static SparkWebSocketProtos.ClientConnect parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ClientConnect parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ClientConnect parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ClientConnect parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ClientConnect parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ClientConnect parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ClientConnect parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ClientConnect parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ClientConnect parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ClientConnect parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ClientConnect parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ClientConnect parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ClientConnect.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkWebSocketProtos.ClientConnect.Builder newBuilder(SparkWebSocketProtos.ClientConnect prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.concurrent.ConcurrentHashMap.replaceNode(ConcurrentHashMap.java:1111)
            //   at java.base/java.util.concurrent.ConcurrentHashMap.remove(ConcurrentHashMap.java:1102)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:97)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:667)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkWebSocketProtos$ClientConnect.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkWebSocketProtos$ClientConnect;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkWebSocketProtos$ClientConnect.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkWebSocketProtos$ClientConnect$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkWebSocketProtos.ClientConnect();
                case NEW_BUILDER:
                    return new SparkWebSocketProtos.ClientConnect.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "clientId_", "description_" };
                    String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001Ȉ\u0002Ȉ";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkWebSocketProtos.ClientConnect> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkWebSocketProtos.ClientConnect.class) {
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

        public static SparkWebSocketProtos.ClientConnect getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkWebSocketProtos.ClientConnect> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkWebSocketProtos.ClientConnect defaultInstance = new SparkWebSocketProtos.ClientConnect();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkWebSocketProtos.ClientConnect.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkWebSocketProtos.ClientConnect, SparkWebSocketProtos.ClientConnect.Builder> implements SparkWebSocketProtos.ClientConnectOrBuilder {

            private Builder() {
                super(SparkWebSocketProtos.ClientConnect.DEFAULT_INSTANCE);
            }

            @Override
            public String getClientId() {
                return this.instance.getClientId();
            }

            @Override
            public ByteString getClientIdBytes() {
                return this.instance.getClientIdBytes();
            }

            public SparkWebSocketProtos.ClientConnect.Builder setClientId(String value) {
                this.copyOnWrite();
                this.instance.setClientId(value);
                return this;
            }

            public SparkWebSocketProtos.ClientConnect.Builder clearClientId() {
                this.copyOnWrite();
                this.instance.clearClientId();
                return this;
            }

            public SparkWebSocketProtos.ClientConnect.Builder setClientIdBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setClientIdBytes(value);
                return this;
            }

            @Override
            public String getDescription() {
                return this.instance.getDescription();
            }

            @Override
            public ByteString getDescriptionBytes() {
                return this.instance.getDescriptionBytes();
            }

            public SparkWebSocketProtos.ClientConnect.Builder setDescription(String value) {
                this.copyOnWrite();
                this.instance.setDescription(value);
                return this;
            }

            public SparkWebSocketProtos.ClientConnect.Builder clearDescription() {
                this.copyOnWrite();
                this.instance.clearDescription();
                return this;
            }

            public SparkWebSocketProtos.ClientConnect.Builder setDescriptionBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setDescriptionBytes(value);
                return this;
            }
        }
    }

    public interface ClientConnectOrBuilder extends MessageLiteOrBuilder {

        String getClientId();

        ByteString getClientIdBytes();

        String getDescription();

        ByteString getDescriptionBytes();
    }

    public static final class ClientPing extends GeneratedMessageLite<SparkWebSocketProtos.ClientPing, SparkWebSocketProtos.ClientPing.Builder> implements SparkWebSocketProtos.ClientPingOrBuilder {

        public static final int OK_FIELD_NUMBER = 1;

        private boolean ok_;

        public static final int DATA_FIELD_NUMBER = 2;

        private int data_;

        private static final SparkWebSocketProtos.ClientPing DEFAULT_INSTANCE;

        private static volatile Parser<SparkWebSocketProtos.ClientPing> PARSER;

        private ClientPing() {
        }

        @Override
        public boolean getOk() {
            return this.ok_;
        }

        private void setOk(boolean value) {
            this.ok_ = value;
        }

        private void clearOk() {
            this.ok_ = false;
        }

        @Override
        public int getData() {
            return this.data_;
        }

        private void setData(int value) {
            this.data_ = value;
        }

        private void clearData() {
            this.data_ = 0;
        }

        public static SparkWebSocketProtos.ClientPing parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ClientPing parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ClientPing parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ClientPing parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ClientPing parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ClientPing parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ClientPing parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ClientPing parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ClientPing parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ClientPing parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ClientPing parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ClientPing parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ClientPing.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkWebSocketProtos.ClientPing.Builder newBuilder(SparkWebSocketProtos.ClientPing prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.concurrent.ConcurrentHashMap.replaceNode(ConcurrentHashMap.java:1111)
            //   at java.base/java.util.concurrent.ConcurrentHashMap.remove(ConcurrentHashMap.java:1102)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:97)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:667)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkWebSocketProtos$ClientPing.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkWebSocketProtos$ClientPing;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkWebSocketProtos$ClientPing.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkWebSocketProtos$ClientPing$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkWebSocketProtos.ClientPing();
                case NEW_BUILDER:
                    return new SparkWebSocketProtos.ClientPing.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "ok_", "data_" };
                    String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\u0007\u0002\u0004";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkWebSocketProtos.ClientPing> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkWebSocketProtos.ClientPing.class) {
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

        public static SparkWebSocketProtos.ClientPing getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkWebSocketProtos.ClientPing> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkWebSocketProtos.ClientPing defaultInstance = new SparkWebSocketProtos.ClientPing();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkWebSocketProtos.ClientPing.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkWebSocketProtos.ClientPing, SparkWebSocketProtos.ClientPing.Builder> implements SparkWebSocketProtos.ClientPingOrBuilder {

            private Builder() {
                super(SparkWebSocketProtos.ClientPing.DEFAULT_INSTANCE);
            }

            @Override
            public boolean getOk() {
                return this.instance.getOk();
            }

            public SparkWebSocketProtos.ClientPing.Builder setOk(boolean value) {
                this.copyOnWrite();
                this.instance.setOk(value);
                return this;
            }

            public SparkWebSocketProtos.ClientPing.Builder clearOk() {
                this.copyOnWrite();
                this.instance.clearOk();
                return this;
            }

            @Override
            public int getData() {
                return this.instance.getData();
            }

            public SparkWebSocketProtos.ClientPing.Builder setData(int value) {
                this.copyOnWrite();
                this.instance.setData(value);
                return this;
            }

            public SparkWebSocketProtos.ClientPing.Builder clearData() {
                this.copyOnWrite();
                this.instance.clearData();
                return this;
            }
        }
    }

    public interface ClientPingOrBuilder extends MessageLiteOrBuilder {

        boolean getOk();

        int getData();
    }

    public static final class PacketWrapper extends GeneratedMessageLite<SparkWebSocketProtos.PacketWrapper, SparkWebSocketProtos.PacketWrapper.Builder> implements SparkWebSocketProtos.PacketWrapperOrBuilder {

        private int packetCase_ = 0;

        private Object packet_;

        public static final int SERVER_PONG_FIELD_NUMBER = 1;

        public static final int SERVER_CONNECT_RESPONSE_FIELD_NUMBER = 2;

        public static final int SERVER_UPDATE_SAMPLER_FIELD_NUMBER = 3;

        public static final int SERVER_UPDATE_STATISTICS_FIELD_NUMBER = 4;

        public static final int CLIENT_PING_FIELD_NUMBER = 10;

        public static final int CLIENT_CONNECT_FIELD_NUMBER = 11;

        private static final SparkWebSocketProtos.PacketWrapper DEFAULT_INSTANCE;

        private static volatile Parser<SparkWebSocketProtos.PacketWrapper> PARSER;

        private PacketWrapper() {
        }

        @Override
        public SparkWebSocketProtos.PacketWrapper.PacketCase getPacketCase() {
            return SparkWebSocketProtos.PacketWrapper.PacketCase.forNumber(this.packetCase_);
        }

        private void clearPacket() {
            this.packetCase_ = 0;
            this.packet_ = null;
        }

        @Override
        public boolean hasServerPong() {
            return this.packetCase_ == 1;
        }

        @Override
        public SparkWebSocketProtos.ServerPong getServerPong() {
            return this.packetCase_ == 1 ? (SparkWebSocketProtos.ServerPong) this.packet_ : SparkWebSocketProtos.ServerPong.getDefaultInstance();
        }

        private void setServerPong(SparkWebSocketProtos.ServerPong value) {
            value.getClass();
            this.packet_ = value;
            this.packetCase_ = 1;
        }

        private void mergeServerPong(SparkWebSocketProtos.ServerPong value) {
            value.getClass();
            if (this.packetCase_ == 1 && this.packet_ != SparkWebSocketProtos.ServerPong.getDefaultInstance()) {
                this.packet_ = SparkWebSocketProtos.ServerPong.newBuilder((SparkWebSocketProtos.ServerPong) this.packet_).mergeFrom(value).buildPartial();
            } else {
                this.packet_ = value;
            }
            this.packetCase_ = 1;
        }

        private void clearServerPong() {
            if (this.packetCase_ == 1) {
                this.packetCase_ = 0;
                this.packet_ = null;
            }
        }

        @Override
        public boolean hasServerConnectResponse() {
            return this.packetCase_ == 2;
        }

        @Override
        public SparkWebSocketProtos.ServerConnectResponse getServerConnectResponse() {
            return this.packetCase_ == 2 ? (SparkWebSocketProtos.ServerConnectResponse) this.packet_ : SparkWebSocketProtos.ServerConnectResponse.getDefaultInstance();
        }

        private void setServerConnectResponse(SparkWebSocketProtos.ServerConnectResponse value) {
            value.getClass();
            this.packet_ = value;
            this.packetCase_ = 2;
        }

        private void mergeServerConnectResponse(SparkWebSocketProtos.ServerConnectResponse value) {
            value.getClass();
            if (this.packetCase_ == 2 && this.packet_ != SparkWebSocketProtos.ServerConnectResponse.getDefaultInstance()) {
                this.packet_ = SparkWebSocketProtos.ServerConnectResponse.newBuilder((SparkWebSocketProtos.ServerConnectResponse) this.packet_).mergeFrom(value).buildPartial();
            } else {
                this.packet_ = value;
            }
            this.packetCase_ = 2;
        }

        private void clearServerConnectResponse() {
            if (this.packetCase_ == 2) {
                this.packetCase_ = 0;
                this.packet_ = null;
            }
        }

        @Override
        public boolean hasServerUpdateSampler() {
            return this.packetCase_ == 3;
        }

        @Override
        public SparkWebSocketProtos.ServerUpdateSamplerData getServerUpdateSampler() {
            return this.packetCase_ == 3 ? (SparkWebSocketProtos.ServerUpdateSamplerData) this.packet_ : SparkWebSocketProtos.ServerUpdateSamplerData.getDefaultInstance();
        }

        private void setServerUpdateSampler(SparkWebSocketProtos.ServerUpdateSamplerData value) {
            value.getClass();
            this.packet_ = value;
            this.packetCase_ = 3;
        }

        private void mergeServerUpdateSampler(SparkWebSocketProtos.ServerUpdateSamplerData value) {
            value.getClass();
            if (this.packetCase_ == 3 && this.packet_ != SparkWebSocketProtos.ServerUpdateSamplerData.getDefaultInstance()) {
                this.packet_ = SparkWebSocketProtos.ServerUpdateSamplerData.newBuilder((SparkWebSocketProtos.ServerUpdateSamplerData) this.packet_).mergeFrom(value).buildPartial();
            } else {
                this.packet_ = value;
            }
            this.packetCase_ = 3;
        }

        private void clearServerUpdateSampler() {
            if (this.packetCase_ == 3) {
                this.packetCase_ = 0;
                this.packet_ = null;
            }
        }

        @Override
        public boolean hasServerUpdateStatistics() {
            return this.packetCase_ == 4;
        }

        @Override
        public SparkWebSocketProtos.ServerUpdateStatistics getServerUpdateStatistics() {
            return this.packetCase_ == 4 ? (SparkWebSocketProtos.ServerUpdateStatistics) this.packet_ : SparkWebSocketProtos.ServerUpdateStatistics.getDefaultInstance();
        }

        private void setServerUpdateStatistics(SparkWebSocketProtos.ServerUpdateStatistics value) {
            value.getClass();
            this.packet_ = value;
            this.packetCase_ = 4;
        }

        private void mergeServerUpdateStatistics(SparkWebSocketProtos.ServerUpdateStatistics value) {
            value.getClass();
            if (this.packetCase_ == 4 && this.packet_ != SparkWebSocketProtos.ServerUpdateStatistics.getDefaultInstance()) {
                this.packet_ = SparkWebSocketProtos.ServerUpdateStatistics.newBuilder((SparkWebSocketProtos.ServerUpdateStatistics) this.packet_).mergeFrom(value).buildPartial();
            } else {
                this.packet_ = value;
            }
            this.packetCase_ = 4;
        }

        private void clearServerUpdateStatistics() {
            if (this.packetCase_ == 4) {
                this.packetCase_ = 0;
                this.packet_ = null;
            }
        }

        @Override
        public boolean hasClientPing() {
            return this.packetCase_ == 10;
        }

        @Override
        public SparkWebSocketProtos.ClientPing getClientPing() {
            return this.packetCase_ == 10 ? (SparkWebSocketProtos.ClientPing) this.packet_ : SparkWebSocketProtos.ClientPing.getDefaultInstance();
        }

        private void setClientPing(SparkWebSocketProtos.ClientPing value) {
            value.getClass();
            this.packet_ = value;
            this.packetCase_ = 10;
        }

        private void mergeClientPing(SparkWebSocketProtos.ClientPing value) {
            value.getClass();
            if (this.packetCase_ == 10 && this.packet_ != SparkWebSocketProtos.ClientPing.getDefaultInstance()) {
                this.packet_ = SparkWebSocketProtos.ClientPing.newBuilder((SparkWebSocketProtos.ClientPing) this.packet_).mergeFrom(value).buildPartial();
            } else {
                this.packet_ = value;
            }
            this.packetCase_ = 10;
        }

        private void clearClientPing() {
            if (this.packetCase_ == 10) {
                this.packetCase_ = 0;
                this.packet_ = null;
            }
        }

        @Override
        public boolean hasClientConnect() {
            return this.packetCase_ == 11;
        }

        @Override
        public SparkWebSocketProtos.ClientConnect getClientConnect() {
            return this.packetCase_ == 11 ? (SparkWebSocketProtos.ClientConnect) this.packet_ : SparkWebSocketProtos.ClientConnect.getDefaultInstance();
        }

        private void setClientConnect(SparkWebSocketProtos.ClientConnect value) {
            value.getClass();
            this.packet_ = value;
            this.packetCase_ = 11;
        }

        private void mergeClientConnect(SparkWebSocketProtos.ClientConnect value) {
            value.getClass();
            if (this.packetCase_ == 11 && this.packet_ != SparkWebSocketProtos.ClientConnect.getDefaultInstance()) {
                this.packet_ = SparkWebSocketProtos.ClientConnect.newBuilder((SparkWebSocketProtos.ClientConnect) this.packet_).mergeFrom(value).buildPartial();
            } else {
                this.packet_ = value;
            }
            this.packetCase_ = 11;
        }

        private void clearClientConnect() {
            if (this.packetCase_ == 11) {
                this.packetCase_ = 0;
                this.packet_ = null;
            }
        }

        public static SparkWebSocketProtos.PacketWrapper parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.PacketWrapper parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.PacketWrapper parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.PacketWrapper parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.PacketWrapper parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.PacketWrapper parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.PacketWrapper parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.PacketWrapper parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.PacketWrapper parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.PacketWrapper parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.PacketWrapper parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.PacketWrapper parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.PacketWrapper.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkWebSocketProtos.PacketWrapper.Builder newBuilder(SparkWebSocketProtos.PacketWrapper prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.concurrent.ConcurrentHashMap.replaceNode(ConcurrentHashMap.java:1111)
            //   at java.base/java.util.concurrent.ConcurrentHashMap.remove(ConcurrentHashMap.java:1102)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:97)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:667)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkWebSocketProtos$PacketWrapper.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkWebSocketProtos$PacketWrapper;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkWebSocketProtos$PacketWrapper.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkWebSocketProtos$PacketWrapper$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkWebSocketProtos.PacketWrapper();
                case NEW_BUILDER:
                    return new SparkWebSocketProtos.PacketWrapper.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "packet_", "packetCase_", SparkWebSocketProtos.ServerPong.class, SparkWebSocketProtos.ServerConnectResponse.class, SparkWebSocketProtos.ServerUpdateSamplerData.class, SparkWebSocketProtos.ServerUpdateStatistics.class, SparkWebSocketProtos.ClientPing.class, SparkWebSocketProtos.ClientConnect.class };
                    String info = "\u0000\u0006\u0001\u0000\u0001\u000b\u0006\u0000\u0000\u0000\u0001<\u0000\u0002<\u0000\u0003<\u0000\u0004<\u0000\n<\u0000\u000b<\u0000";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkWebSocketProtos.PacketWrapper> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkWebSocketProtos.PacketWrapper.class) {
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

        public static SparkWebSocketProtos.PacketWrapper getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkWebSocketProtos.PacketWrapper> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkWebSocketProtos.PacketWrapper defaultInstance = new SparkWebSocketProtos.PacketWrapper();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkWebSocketProtos.PacketWrapper.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkWebSocketProtos.PacketWrapper, SparkWebSocketProtos.PacketWrapper.Builder> implements SparkWebSocketProtos.PacketWrapperOrBuilder {

            private Builder() {
                super(SparkWebSocketProtos.PacketWrapper.DEFAULT_INSTANCE);
            }

            @Override
            public SparkWebSocketProtos.PacketWrapper.PacketCase getPacketCase() {
                return this.instance.getPacketCase();
            }

            public SparkWebSocketProtos.PacketWrapper.Builder clearPacket() {
                this.copyOnWrite();
                this.instance.clearPacket();
                return this;
            }

            @Override
            public boolean hasServerPong() {
                return this.instance.hasServerPong();
            }

            @Override
            public SparkWebSocketProtos.ServerPong getServerPong() {
                return this.instance.getServerPong();
            }

            public SparkWebSocketProtos.PacketWrapper.Builder setServerPong(SparkWebSocketProtos.ServerPong value) {
                this.copyOnWrite();
                this.instance.setServerPong(value);
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder setServerPong(SparkWebSocketProtos.ServerPong.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setServerPong(builderForValue.build());
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder mergeServerPong(SparkWebSocketProtos.ServerPong value) {
                this.copyOnWrite();
                this.instance.mergeServerPong(value);
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder clearServerPong() {
                this.copyOnWrite();
                this.instance.clearServerPong();
                return this;
            }

            @Override
            public boolean hasServerConnectResponse() {
                return this.instance.hasServerConnectResponse();
            }

            @Override
            public SparkWebSocketProtos.ServerConnectResponse getServerConnectResponse() {
                return this.instance.getServerConnectResponse();
            }

            public SparkWebSocketProtos.PacketWrapper.Builder setServerConnectResponse(SparkWebSocketProtos.ServerConnectResponse value) {
                this.copyOnWrite();
                this.instance.setServerConnectResponse(value);
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder setServerConnectResponse(SparkWebSocketProtos.ServerConnectResponse.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setServerConnectResponse(builderForValue.build());
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder mergeServerConnectResponse(SparkWebSocketProtos.ServerConnectResponse value) {
                this.copyOnWrite();
                this.instance.mergeServerConnectResponse(value);
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder clearServerConnectResponse() {
                this.copyOnWrite();
                this.instance.clearServerConnectResponse();
                return this;
            }

            @Override
            public boolean hasServerUpdateSampler() {
                return this.instance.hasServerUpdateSampler();
            }

            @Override
            public SparkWebSocketProtos.ServerUpdateSamplerData getServerUpdateSampler() {
                return this.instance.getServerUpdateSampler();
            }

            public SparkWebSocketProtos.PacketWrapper.Builder setServerUpdateSampler(SparkWebSocketProtos.ServerUpdateSamplerData value) {
                this.copyOnWrite();
                this.instance.setServerUpdateSampler(value);
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder setServerUpdateSampler(SparkWebSocketProtos.ServerUpdateSamplerData.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setServerUpdateSampler(builderForValue.build());
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder mergeServerUpdateSampler(SparkWebSocketProtos.ServerUpdateSamplerData value) {
                this.copyOnWrite();
                this.instance.mergeServerUpdateSampler(value);
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder clearServerUpdateSampler() {
                this.copyOnWrite();
                this.instance.clearServerUpdateSampler();
                return this;
            }

            @Override
            public boolean hasServerUpdateStatistics() {
                return this.instance.hasServerUpdateStatistics();
            }

            @Override
            public SparkWebSocketProtos.ServerUpdateStatistics getServerUpdateStatistics() {
                return this.instance.getServerUpdateStatistics();
            }

            public SparkWebSocketProtos.PacketWrapper.Builder setServerUpdateStatistics(SparkWebSocketProtos.ServerUpdateStatistics value) {
                this.copyOnWrite();
                this.instance.setServerUpdateStatistics(value);
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder setServerUpdateStatistics(SparkWebSocketProtos.ServerUpdateStatistics.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setServerUpdateStatistics(builderForValue.build());
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder mergeServerUpdateStatistics(SparkWebSocketProtos.ServerUpdateStatistics value) {
                this.copyOnWrite();
                this.instance.mergeServerUpdateStatistics(value);
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder clearServerUpdateStatistics() {
                this.copyOnWrite();
                this.instance.clearServerUpdateStatistics();
                return this;
            }

            @Override
            public boolean hasClientPing() {
                return this.instance.hasClientPing();
            }

            @Override
            public SparkWebSocketProtos.ClientPing getClientPing() {
                return this.instance.getClientPing();
            }

            public SparkWebSocketProtos.PacketWrapper.Builder setClientPing(SparkWebSocketProtos.ClientPing value) {
                this.copyOnWrite();
                this.instance.setClientPing(value);
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder setClientPing(SparkWebSocketProtos.ClientPing.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setClientPing(builderForValue.build());
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder mergeClientPing(SparkWebSocketProtos.ClientPing value) {
                this.copyOnWrite();
                this.instance.mergeClientPing(value);
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder clearClientPing() {
                this.copyOnWrite();
                this.instance.clearClientPing();
                return this;
            }

            @Override
            public boolean hasClientConnect() {
                return this.instance.hasClientConnect();
            }

            @Override
            public SparkWebSocketProtos.ClientConnect getClientConnect() {
                return this.instance.getClientConnect();
            }

            public SparkWebSocketProtos.PacketWrapper.Builder setClientConnect(SparkWebSocketProtos.ClientConnect value) {
                this.copyOnWrite();
                this.instance.setClientConnect(value);
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder setClientConnect(SparkWebSocketProtos.ClientConnect.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setClientConnect(builderForValue.build());
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder mergeClientConnect(SparkWebSocketProtos.ClientConnect value) {
                this.copyOnWrite();
                this.instance.mergeClientConnect(value);
                return this;
            }

            public SparkWebSocketProtos.PacketWrapper.Builder clearClientConnect() {
                this.copyOnWrite();
                this.instance.clearClientConnect();
                return this;
            }
        }

        public static enum PacketCase {

            SERVER_PONG(1),
            SERVER_CONNECT_RESPONSE(2),
            SERVER_UPDATE_SAMPLER(3),
            SERVER_UPDATE_STATISTICS(4),
            CLIENT_PING(10),
            CLIENT_CONNECT(11),
            PACKET_NOT_SET(0);

            private final int value;

            private PacketCase(int value) {
                this.value = value;
            }

            @Deprecated
            public static SparkWebSocketProtos.PacketWrapper.PacketCase valueOf(int value) {
                return forNumber(value);
            }

            public static SparkWebSocketProtos.PacketWrapper.PacketCase forNumber(int value) {
                switch(value) {
                    case 0:
                        return PACKET_NOT_SET;
                    case 1:
                        return SERVER_PONG;
                    case 2:
                        return SERVER_CONNECT_RESPONSE;
                    case 3:
                        return SERVER_UPDATE_SAMPLER;
                    case 4:
                        return SERVER_UPDATE_STATISTICS;
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    default:
                        return null;
                    case 10:
                        return CLIENT_PING;
                    case 11:
                        return CLIENT_CONNECT;
                }
            }

            public int getNumber() {
                return this.value;
            }
        }
    }

    public interface PacketWrapperOrBuilder extends MessageLiteOrBuilder {

        boolean hasServerPong();

        SparkWebSocketProtos.ServerPong getServerPong();

        boolean hasServerConnectResponse();

        SparkWebSocketProtos.ServerConnectResponse getServerConnectResponse();

        boolean hasServerUpdateSampler();

        SparkWebSocketProtos.ServerUpdateSamplerData getServerUpdateSampler();

        boolean hasServerUpdateStatistics();

        SparkWebSocketProtos.ServerUpdateStatistics getServerUpdateStatistics();

        boolean hasClientPing();

        SparkWebSocketProtos.ClientPing getClientPing();

        boolean hasClientConnect();

        SparkWebSocketProtos.ClientConnect getClientConnect();

        SparkWebSocketProtos.PacketWrapper.PacketCase getPacketCase();
    }

    public static final class RawPacket extends GeneratedMessageLite<SparkWebSocketProtos.RawPacket, SparkWebSocketProtos.RawPacket.Builder> implements SparkWebSocketProtos.RawPacketOrBuilder {

        public static final int VERSION_FIELD_NUMBER = 1;

        private int version_;

        public static final int PUBLIC_KEY_FIELD_NUMBER = 2;

        private ByteString publicKey_ = ByteString.EMPTY;

        public static final int SIGNATURE_FIELD_NUMBER = 3;

        private ByteString signature_ = ByteString.EMPTY;

        public static final int MESSAGE_FIELD_NUMBER = 4;

        private ByteString message_ = ByteString.EMPTY;

        private static final SparkWebSocketProtos.RawPacket DEFAULT_INSTANCE;

        private static volatile Parser<SparkWebSocketProtos.RawPacket> PARSER;

        private RawPacket() {
        }

        @Override
        public int getVersion() {
            return this.version_;
        }

        private void setVersion(int value) {
            this.version_ = value;
        }

        private void clearVersion() {
            this.version_ = 0;
        }

        @Override
        public ByteString getPublicKey() {
            return this.publicKey_;
        }

        private void setPublicKey(ByteString value) {
            Class<?> valueClass = value.getClass();
            this.publicKey_ = value;
        }

        private void clearPublicKey() {
            this.publicKey_ = getDefaultInstance().getPublicKey();
        }

        @Override
        public ByteString getSignature() {
            return this.signature_;
        }

        private void setSignature(ByteString value) {
            Class<?> valueClass = value.getClass();
            this.signature_ = value;
        }

        private void clearSignature() {
            this.signature_ = getDefaultInstance().getSignature();
        }

        @Override
        public ByteString getMessage() {
            return this.message_;
        }

        private void setMessage(ByteString value) {
            Class<?> valueClass = value.getClass();
            this.message_ = value;
        }

        private void clearMessage() {
            this.message_ = getDefaultInstance().getMessage();
        }

        public static SparkWebSocketProtos.RawPacket parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.RawPacket parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.RawPacket parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.RawPacket parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.RawPacket parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.RawPacket parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.RawPacket parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.RawPacket parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.RawPacket parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.RawPacket parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.RawPacket parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.RawPacket parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.RawPacket.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkWebSocketProtos.RawPacket.Builder newBuilder(SparkWebSocketProtos.RawPacket prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.concurrent.ConcurrentHashMap.replaceNode(ConcurrentHashMap.java:1111)
            //   at java.base/java.util.concurrent.ConcurrentHashMap.remove(ConcurrentHashMap.java:1102)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:97)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:667)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkWebSocketProtos$RawPacket.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkWebSocketProtos$RawPacket;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkWebSocketProtos$RawPacket.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkWebSocketProtos$RawPacket$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkWebSocketProtos.RawPacket();
                case NEW_BUILDER:
                    return new SparkWebSocketProtos.RawPacket.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "version_", "publicKey_", "signature_", "message_" };
                    String info = "\u0000\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0000\u0000\u0001\u0004\u0002\n\u0003\n\u0004\n";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkWebSocketProtos.RawPacket> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkWebSocketProtos.RawPacket.class) {
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

        public static SparkWebSocketProtos.RawPacket getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkWebSocketProtos.RawPacket> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkWebSocketProtos.RawPacket defaultInstance = new SparkWebSocketProtos.RawPacket();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkWebSocketProtos.RawPacket.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkWebSocketProtos.RawPacket, SparkWebSocketProtos.RawPacket.Builder> implements SparkWebSocketProtos.RawPacketOrBuilder {

            private Builder() {
                super(SparkWebSocketProtos.RawPacket.DEFAULT_INSTANCE);
            }

            @Override
            public int getVersion() {
                return this.instance.getVersion();
            }

            public SparkWebSocketProtos.RawPacket.Builder setVersion(int value) {
                this.copyOnWrite();
                this.instance.setVersion(value);
                return this;
            }

            public SparkWebSocketProtos.RawPacket.Builder clearVersion() {
                this.copyOnWrite();
                this.instance.clearVersion();
                return this;
            }

            @Override
            public ByteString getPublicKey() {
                return this.instance.getPublicKey();
            }

            public SparkWebSocketProtos.RawPacket.Builder setPublicKey(ByteString value) {
                this.copyOnWrite();
                this.instance.setPublicKey(value);
                return this;
            }

            public SparkWebSocketProtos.RawPacket.Builder clearPublicKey() {
                this.copyOnWrite();
                this.instance.clearPublicKey();
                return this;
            }

            @Override
            public ByteString getSignature() {
                return this.instance.getSignature();
            }

            public SparkWebSocketProtos.RawPacket.Builder setSignature(ByteString value) {
                this.copyOnWrite();
                this.instance.setSignature(value);
                return this;
            }

            public SparkWebSocketProtos.RawPacket.Builder clearSignature() {
                this.copyOnWrite();
                this.instance.clearSignature();
                return this;
            }

            @Override
            public ByteString getMessage() {
                return this.instance.getMessage();
            }

            public SparkWebSocketProtos.RawPacket.Builder setMessage(ByteString value) {
                this.copyOnWrite();
                this.instance.setMessage(value);
                return this;
            }

            public SparkWebSocketProtos.RawPacket.Builder clearMessage() {
                this.copyOnWrite();
                this.instance.clearMessage();
                return this;
            }
        }
    }

    public interface RawPacketOrBuilder extends MessageLiteOrBuilder {

        int getVersion();

        ByteString getPublicKey();

        ByteString getSignature();

        ByteString getMessage();
    }

    public static final class ServerConnectResponse extends GeneratedMessageLite<SparkWebSocketProtos.ServerConnectResponse, SparkWebSocketProtos.ServerConnectResponse.Builder> implements SparkWebSocketProtos.ServerConnectResponseOrBuilder {

        public static final int CLIENT_ID_FIELD_NUMBER = 1;

        private String clientId_ = "";

        public static final int STATE_FIELD_NUMBER = 2;

        private int state_;

        public static final int SETTINGS_FIELD_NUMBER = 3;

        private SparkWebSocketProtos.ServerConnectResponse.Settings settings_;

        public static final int LAST_PAYLOAD_ID_FIELD_NUMBER = 4;

        private String lastPayloadId_ = "";

        private static final SparkWebSocketProtos.ServerConnectResponse DEFAULT_INSTANCE;

        private static volatile Parser<SparkWebSocketProtos.ServerConnectResponse> PARSER;

        private ServerConnectResponse() {
        }

        @Override
        public String getClientId() {
            return this.clientId_;
        }

        @Override
        public ByteString getClientIdBytes() {
            return ByteString.copyFromUtf8(this.clientId_);
        }

        private void setClientId(String value) {
            Class<?> valueClass = value.getClass();
            this.clientId_ = value;
        }

        private void clearClientId() {
            this.clientId_ = getDefaultInstance().getClientId();
        }

        private void setClientIdBytes(ByteString value) {
            checkByteStringIsUtf8(value);
            this.clientId_ = value.toStringUtf8();
        }

        @Override
        public int getStateValue() {
            return this.state_;
        }

        @Override
        public SparkWebSocketProtos.ServerConnectResponse.State getState() {
            SparkWebSocketProtos.ServerConnectResponse.State result = SparkWebSocketProtos.ServerConnectResponse.State.forNumber(this.state_);
            return result == null ? SparkWebSocketProtos.ServerConnectResponse.State.UNRECOGNIZED : result;
        }

        private void setStateValue(int value) {
            this.state_ = value;
        }

        private void setState(SparkWebSocketProtos.ServerConnectResponse.State value) {
            this.state_ = value.getNumber();
        }

        private void clearState() {
            this.state_ = 0;
        }

        @Override
        public boolean hasSettings() {
            return this.settings_ != null;
        }

        @Override
        public SparkWebSocketProtos.ServerConnectResponse.Settings getSettings() {
            return this.settings_ == null ? SparkWebSocketProtos.ServerConnectResponse.Settings.getDefaultInstance() : this.settings_;
        }

        private void setSettings(SparkWebSocketProtos.ServerConnectResponse.Settings value) {
            value.getClass();
            this.settings_ = value;
        }

        private void mergeSettings(SparkWebSocketProtos.ServerConnectResponse.Settings value) {
            value.getClass();
            if (this.settings_ != null && this.settings_ != SparkWebSocketProtos.ServerConnectResponse.Settings.getDefaultInstance()) {
                this.settings_ = SparkWebSocketProtos.ServerConnectResponse.Settings.newBuilder(this.settings_).mergeFrom(value).buildPartial();
            } else {
                this.settings_ = value;
            }
        }

        private void clearSettings() {
            this.settings_ = null;
        }

        @Override
        public String getLastPayloadId() {
            return this.lastPayloadId_;
        }

        @Override
        public ByteString getLastPayloadIdBytes() {
            return ByteString.copyFromUtf8(this.lastPayloadId_);
        }

        private void setLastPayloadId(String value) {
            Class<?> valueClass = value.getClass();
            this.lastPayloadId_ = value;
        }

        private void clearLastPayloadId() {
            this.lastPayloadId_ = getDefaultInstance().getLastPayloadId();
        }

        private void setLastPayloadIdBytes(ByteString value) {
            checkByteStringIsUtf8(value);
            this.lastPayloadId_ = value.toStringUtf8();
        }

        public static SparkWebSocketProtos.ServerConnectResponse parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ServerConnectResponse parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerConnectResponse parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ServerConnectResponse parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerConnectResponse parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ServerConnectResponse parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerConnectResponse parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ServerConnectResponse parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerConnectResponse parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ServerConnectResponse parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerConnectResponse parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ServerConnectResponse parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerConnectResponse.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkWebSocketProtos.ServerConnectResponse.Builder newBuilder(SparkWebSocketProtos.ServerConnectResponse prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.concurrent.ConcurrentHashMap.replaceNode(ConcurrentHashMap.java:1111)
            //   at java.base/java.util.concurrent.ConcurrentHashMap.remove(ConcurrentHashMap.java:1102)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:97)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:667)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkWebSocketProtos$ServerConnectResponse.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkWebSocketProtos$ServerConnectResponse;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkWebSocketProtos$ServerConnectResponse.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkWebSocketProtos$ServerConnectResponse$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkWebSocketProtos.ServerConnectResponse();
                case NEW_BUILDER:
                    return new SparkWebSocketProtos.ServerConnectResponse.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "clientId_", "state_", "settings_", "lastPayloadId_" };
                    String info = "\u0000\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0000\u0000\u0001Ȉ\u0002\f\u0003\t\u0004Ȉ";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkWebSocketProtos.ServerConnectResponse> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkWebSocketProtos.ServerConnectResponse.class) {
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

        public static SparkWebSocketProtos.ServerConnectResponse getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkWebSocketProtos.ServerConnectResponse> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkWebSocketProtos.ServerConnectResponse defaultInstance = new SparkWebSocketProtos.ServerConnectResponse();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkWebSocketProtos.ServerConnectResponse.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkWebSocketProtos.ServerConnectResponse, SparkWebSocketProtos.ServerConnectResponse.Builder> implements SparkWebSocketProtos.ServerConnectResponseOrBuilder {

            private Builder() {
                super(SparkWebSocketProtos.ServerConnectResponse.DEFAULT_INSTANCE);
            }

            @Override
            public String getClientId() {
                return this.instance.getClientId();
            }

            @Override
            public ByteString getClientIdBytes() {
                return this.instance.getClientIdBytes();
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder setClientId(String value) {
                this.copyOnWrite();
                this.instance.setClientId(value);
                return this;
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder clearClientId() {
                this.copyOnWrite();
                this.instance.clearClientId();
                return this;
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder setClientIdBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setClientIdBytes(value);
                return this;
            }

            @Override
            public int getStateValue() {
                return this.instance.getStateValue();
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder setStateValue(int value) {
                this.copyOnWrite();
                this.instance.setStateValue(value);
                return this;
            }

            @Override
            public SparkWebSocketProtos.ServerConnectResponse.State getState() {
                return this.instance.getState();
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder setState(SparkWebSocketProtos.ServerConnectResponse.State value) {
                this.copyOnWrite();
                this.instance.setState(value);
                return this;
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder clearState() {
                this.copyOnWrite();
                this.instance.clearState();
                return this;
            }

            @Override
            public boolean hasSettings() {
                return this.instance.hasSettings();
            }

            @Override
            public SparkWebSocketProtos.ServerConnectResponse.Settings getSettings() {
                return this.instance.getSettings();
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder setSettings(SparkWebSocketProtos.ServerConnectResponse.Settings value) {
                this.copyOnWrite();
                this.instance.setSettings(value);
                return this;
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder setSettings(SparkWebSocketProtos.ServerConnectResponse.Settings.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setSettings(builderForValue.build());
                return this;
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder mergeSettings(SparkWebSocketProtos.ServerConnectResponse.Settings value) {
                this.copyOnWrite();
                this.instance.mergeSettings(value);
                return this;
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder clearSettings() {
                this.copyOnWrite();
                this.instance.clearSettings();
                return this;
            }

            @Override
            public String getLastPayloadId() {
                return this.instance.getLastPayloadId();
            }

            @Override
            public ByteString getLastPayloadIdBytes() {
                return this.instance.getLastPayloadIdBytes();
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder setLastPayloadId(String value) {
                this.copyOnWrite();
                this.instance.setLastPayloadId(value);
                return this;
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder clearLastPayloadId() {
                this.copyOnWrite();
                this.instance.clearLastPayloadId();
                return this;
            }

            public SparkWebSocketProtos.ServerConnectResponse.Builder setLastPayloadIdBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setLastPayloadIdBytes(value);
                return this;
            }
        }

        public static final class Settings extends GeneratedMessageLite<SparkWebSocketProtos.ServerConnectResponse.Settings, SparkWebSocketProtos.ServerConnectResponse.Settings.Builder> implements SparkWebSocketProtos.ServerConnectResponse.SettingsOrBuilder {

            public static final int STATISTICS_INTERVAL_FIELD_NUMBER = 1;

            private int statisticsInterval_;

            public static final int SAMPLER_INTERVAL_FIELD_NUMBER = 2;

            private int samplerInterval_;

            private static final SparkWebSocketProtos.ServerConnectResponse.Settings DEFAULT_INSTANCE;

            private static volatile Parser<SparkWebSocketProtos.ServerConnectResponse.Settings> PARSER;

            private Settings() {
            }

            @Override
            public int getStatisticsInterval() {
                return this.statisticsInterval_;
            }

            private void setStatisticsInterval(int value) {
                this.statisticsInterval_ = value;
            }

            private void clearStatisticsInterval() {
                this.statisticsInterval_ = 0;
            }

            @Override
            public int getSamplerInterval() {
                return this.samplerInterval_;
            }

            private void setSamplerInterval(int value) {
                this.samplerInterval_ = value;
            }

            private void clearSamplerInterval() {
                this.samplerInterval_ = 0;
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkWebSocketProtos.ServerConnectResponse.Settings.Builder newBuilder(SparkWebSocketProtos.ServerConnectResponse.Settings prototype) {
                // $VF: Couldn't be decompiled
                // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                // java.lang.StackOverflowError
                //   at java.base/java.util.concurrent.ConcurrentHashMap.replaceNode(ConcurrentHashMap.java:1111)
                //   at java.base/java.util.concurrent.ConcurrentHashMap.remove(ConcurrentHashMap.java:1102)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:97)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.instanceOf(StructContext.java:282)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
                //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
                //
                // Bytecode:
                // 0: getstatic me/lucko/spark/proto/SparkWebSocketProtos$ServerConnectResponse$Settings.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkWebSocketProtos$ServerConnectResponse$Settings;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkWebSocketProtos$ServerConnectResponse$Settings.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkWebSocketProtos$ServerConnectResponse$Settings$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkWebSocketProtos.ServerConnectResponse.Settings();
                    case NEW_BUILDER:
                        return new SparkWebSocketProtos.ServerConnectResponse.Settings.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "statisticsInterval_", "samplerInterval_" };
                        String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\u0004\u0002\u0004";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkWebSocketProtos.ServerConnectResponse.Settings> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkWebSocketProtos.ServerConnectResponse.Settings.class) {
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

            public static SparkWebSocketProtos.ServerConnectResponse.Settings getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkWebSocketProtos.ServerConnectResponse.Settings> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkWebSocketProtos.ServerConnectResponse.Settings defaultInstance = new SparkWebSocketProtos.ServerConnectResponse.Settings();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkWebSocketProtos.ServerConnectResponse.Settings.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkWebSocketProtos.ServerConnectResponse.Settings, SparkWebSocketProtos.ServerConnectResponse.Settings.Builder> implements SparkWebSocketProtos.ServerConnectResponse.SettingsOrBuilder {

                private Builder() {
                    super(SparkWebSocketProtos.ServerConnectResponse.Settings.DEFAULT_INSTANCE);
                }

                @Override
                public int getStatisticsInterval() {
                    return this.instance.getStatisticsInterval();
                }

                public SparkWebSocketProtos.ServerConnectResponse.Settings.Builder setStatisticsInterval(int value) {
                    this.copyOnWrite();
                    this.instance.setStatisticsInterval(value);
                    return this;
                }

                public SparkWebSocketProtos.ServerConnectResponse.Settings.Builder clearStatisticsInterval() {
                    this.copyOnWrite();
                    this.instance.clearStatisticsInterval();
                    return this;
                }

                @Override
                public int getSamplerInterval() {
                    return this.instance.getSamplerInterval();
                }

                public SparkWebSocketProtos.ServerConnectResponse.Settings.Builder setSamplerInterval(int value) {
                    this.copyOnWrite();
                    this.instance.setSamplerInterval(value);
                    return this;
                }

                public SparkWebSocketProtos.ServerConnectResponse.Settings.Builder clearSamplerInterval() {
                    this.copyOnWrite();
                    this.instance.clearSamplerInterval();
                    return this;
                }
            }
        }

        public interface SettingsOrBuilder extends MessageLiteOrBuilder {

            int getStatisticsInterval();

            int getSamplerInterval();
        }

        public static enum State implements Internal.EnumLite {

            ACCEPTED(0), UNTRUSTED(1), REJECTED(2), UNRECOGNIZED(-1);

            public static final int ACCEPTED_VALUE = 0;

            public static final int UNTRUSTED_VALUE = 1;

            public static final int REJECTED_VALUE = 2;

            private static final Internal.EnumLiteMap<SparkWebSocketProtos.ServerConnectResponse.State> internalValueMap = new Internal.EnumLiteMap<SparkWebSocketProtos.ServerConnectResponse.State>() {

                public SparkWebSocketProtos.ServerConnectResponse.State findValueByNumber(int number) {
                    return SparkWebSocketProtos.ServerConnectResponse.State.forNumber(number);
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
            public static SparkWebSocketProtos.ServerConnectResponse.State valueOf(int value) {
                return forNumber(value);
            }

            public static SparkWebSocketProtos.ServerConnectResponse.State forNumber(int value) {
                switch(value) {
                    case 0:
                        return ACCEPTED;
                    case 1:
                        return UNTRUSTED;
                    case 2:
                        return REJECTED;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<SparkWebSocketProtos.ServerConnectResponse.State> internalGetValueMap() {
                return internalValueMap;
            }

            public static Internal.EnumVerifier internalGetVerifier() {
                return SparkWebSocketProtos.ServerConnectResponse.State.StateVerifier.INSTANCE;
            }

            private State(int value) {
                this.value = value;
            }

            private static final class StateVerifier implements Internal.EnumVerifier {

                static final Internal.EnumVerifier INSTANCE = new SparkWebSocketProtos.ServerConnectResponse.State.StateVerifier();

                @Override
                public boolean isInRange(int number) {
                    return SparkWebSocketProtos.ServerConnectResponse.State.forNumber(number) != null;
                }
            }
        }
    }

    public interface ServerConnectResponseOrBuilder extends MessageLiteOrBuilder {

        String getClientId();

        ByteString getClientIdBytes();

        int getStateValue();

        SparkWebSocketProtos.ServerConnectResponse.State getState();

        boolean hasSettings();

        SparkWebSocketProtos.ServerConnectResponse.Settings getSettings();

        String getLastPayloadId();

        ByteString getLastPayloadIdBytes();
    }

    public static final class ServerPong extends GeneratedMessageLite<SparkWebSocketProtos.ServerPong, SparkWebSocketProtos.ServerPong.Builder> implements SparkWebSocketProtos.ServerPongOrBuilder {

        public static final int OK_FIELD_NUMBER = 1;

        private boolean ok_;

        public static final int DATA_FIELD_NUMBER = 2;

        private int data_;

        private static final SparkWebSocketProtos.ServerPong DEFAULT_INSTANCE;

        private static volatile Parser<SparkWebSocketProtos.ServerPong> PARSER;

        private ServerPong() {
        }

        @Override
        public boolean getOk() {
            return this.ok_;
        }

        private void setOk(boolean value) {
            this.ok_ = value;
        }

        private void clearOk() {
            this.ok_ = false;
        }

        @Override
        public int getData() {
            return this.data_;
        }

        private void setData(int value) {
            this.data_ = value;
        }

        private void clearData() {
            this.data_ = 0;
        }

        public static SparkWebSocketProtos.ServerPong parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ServerPong parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerPong parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ServerPong parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerPong parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ServerPong parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerPong parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ServerPong parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerPong parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ServerPong parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerPong parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ServerPong parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerPong.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkWebSocketProtos.ServerPong.Builder newBuilder(SparkWebSocketProtos.ServerPong prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.concurrent.ConcurrentHashMap.replaceNode(ConcurrentHashMap.java:1111)
            //   at java.base/java.util.concurrent.ConcurrentHashMap.remove(ConcurrentHashMap.java:1102)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:97)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:667)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkWebSocketProtos$ServerPong.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkWebSocketProtos$ServerPong;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkWebSocketProtos$ServerPong.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkWebSocketProtos$ServerPong$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkWebSocketProtos.ServerPong();
                case NEW_BUILDER:
                    return new SparkWebSocketProtos.ServerPong.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "ok_", "data_" };
                    String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\u0007\u0002\u0004";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkWebSocketProtos.ServerPong> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkWebSocketProtos.ServerPong.class) {
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

        public static SparkWebSocketProtos.ServerPong getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkWebSocketProtos.ServerPong> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkWebSocketProtos.ServerPong defaultInstance = new SparkWebSocketProtos.ServerPong();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkWebSocketProtos.ServerPong.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkWebSocketProtos.ServerPong, SparkWebSocketProtos.ServerPong.Builder> implements SparkWebSocketProtos.ServerPongOrBuilder {

            private Builder() {
                super(SparkWebSocketProtos.ServerPong.DEFAULT_INSTANCE);
            }

            @Override
            public boolean getOk() {
                return this.instance.getOk();
            }

            public SparkWebSocketProtos.ServerPong.Builder setOk(boolean value) {
                this.copyOnWrite();
                this.instance.setOk(value);
                return this;
            }

            public SparkWebSocketProtos.ServerPong.Builder clearOk() {
                this.copyOnWrite();
                this.instance.clearOk();
                return this;
            }

            @Override
            public int getData() {
                return this.instance.getData();
            }

            public SparkWebSocketProtos.ServerPong.Builder setData(int value) {
                this.copyOnWrite();
                this.instance.setData(value);
                return this;
            }

            public SparkWebSocketProtos.ServerPong.Builder clearData() {
                this.copyOnWrite();
                this.instance.clearData();
                return this;
            }
        }
    }

    public interface ServerPongOrBuilder extends MessageLiteOrBuilder {

        boolean getOk();

        int getData();
    }

    public static final class ServerUpdateSamplerData extends GeneratedMessageLite<SparkWebSocketProtos.ServerUpdateSamplerData, SparkWebSocketProtos.ServerUpdateSamplerData.Builder> implements SparkWebSocketProtos.ServerUpdateSamplerDataOrBuilder {

        public static final int PAYLOAD_ID_FIELD_NUMBER = 1;

        private String payloadId_ = "";

        private static final SparkWebSocketProtos.ServerUpdateSamplerData DEFAULT_INSTANCE;

        private static volatile Parser<SparkWebSocketProtos.ServerUpdateSamplerData> PARSER;

        private ServerUpdateSamplerData() {
        }

        @Override
        public String getPayloadId() {
            return this.payloadId_;
        }

        @Override
        public ByteString getPayloadIdBytes() {
            return ByteString.copyFromUtf8(this.payloadId_);
        }

        private void setPayloadId(String value) {
            Class<?> valueClass = value.getClass();
            this.payloadId_ = value;
        }

        private void clearPayloadId() {
            this.payloadId_ = getDefaultInstance().getPayloadId();
        }

        private void setPayloadIdBytes(ByteString value) {
            checkByteStringIsUtf8(value);
            this.payloadId_ = value.toStringUtf8();
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkWebSocketProtos.ServerUpdateSamplerData.Builder newBuilder(SparkWebSocketProtos.ServerUpdateSamplerData prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.concurrent.ConcurrentHashMap.replaceNode(ConcurrentHashMap.java:1111)
            //   at java.base/java.util.concurrent.ConcurrentHashMap.remove(ConcurrentHashMap.java:1102)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:97)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:667)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkWebSocketProtos$ServerUpdateSamplerData.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkWebSocketProtos$ServerUpdateSamplerData;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkWebSocketProtos$ServerUpdateSamplerData.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkWebSocketProtos$ServerUpdateSamplerData$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkWebSocketProtos.ServerUpdateSamplerData();
                case NEW_BUILDER:
                    return new SparkWebSocketProtos.ServerUpdateSamplerData.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "payloadId_" };
                    String info = "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001Ȉ";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkWebSocketProtos.ServerUpdateSamplerData> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkWebSocketProtos.ServerUpdateSamplerData.class) {
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

        public static SparkWebSocketProtos.ServerUpdateSamplerData getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkWebSocketProtos.ServerUpdateSamplerData> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkWebSocketProtos.ServerUpdateSamplerData defaultInstance = new SparkWebSocketProtos.ServerUpdateSamplerData();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkWebSocketProtos.ServerUpdateSamplerData.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkWebSocketProtos.ServerUpdateSamplerData, SparkWebSocketProtos.ServerUpdateSamplerData.Builder> implements SparkWebSocketProtos.ServerUpdateSamplerDataOrBuilder {

            private Builder() {
                super(SparkWebSocketProtos.ServerUpdateSamplerData.DEFAULT_INSTANCE);
            }

            @Override
            public String getPayloadId() {
                return this.instance.getPayloadId();
            }

            @Override
            public ByteString getPayloadIdBytes() {
                return this.instance.getPayloadIdBytes();
            }

            public SparkWebSocketProtos.ServerUpdateSamplerData.Builder setPayloadId(String value) {
                this.copyOnWrite();
                this.instance.setPayloadId(value);
                return this;
            }

            public SparkWebSocketProtos.ServerUpdateSamplerData.Builder clearPayloadId() {
                this.copyOnWrite();
                this.instance.clearPayloadId();
                return this;
            }

            public SparkWebSocketProtos.ServerUpdateSamplerData.Builder setPayloadIdBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setPayloadIdBytes(value);
                return this;
            }
        }
    }

    public interface ServerUpdateSamplerDataOrBuilder extends MessageLiteOrBuilder {

        String getPayloadId();

        ByteString getPayloadIdBytes();
    }

    public static final class ServerUpdateStatistics extends GeneratedMessageLite<SparkWebSocketProtos.ServerUpdateStatistics, SparkWebSocketProtos.ServerUpdateStatistics.Builder> implements SparkWebSocketProtos.ServerUpdateStatisticsOrBuilder {

        public static final int PLATFORM_FIELD_NUMBER = 1;

        private SparkProtos.PlatformStatistics platform_;

        public static final int SYSTEM_FIELD_NUMBER = 2;

        private SparkProtos.SystemStatistics system_;

        private static final SparkWebSocketProtos.ServerUpdateStatistics DEFAULT_INSTANCE;

        private static volatile Parser<SparkWebSocketProtos.ServerUpdateStatistics> PARSER;

        private ServerUpdateStatistics() {
        }

        @Override
        public boolean hasPlatform() {
            return this.platform_ != null;
        }

        @Override
        public SparkProtos.PlatformStatistics getPlatform() {
            return this.platform_ == null ? SparkProtos.PlatformStatistics.getDefaultInstance() : this.platform_;
        }

        private void setPlatform(SparkProtos.PlatformStatistics value) {
            value.getClass();
            this.platform_ = value;
        }

        private void mergePlatform(SparkProtos.PlatformStatistics value) {
            value.getClass();
            if (this.platform_ != null && this.platform_ != SparkProtos.PlatformStatistics.getDefaultInstance()) {
                this.platform_ = SparkProtos.PlatformStatistics.newBuilder(this.platform_).mergeFrom(value).buildPartial();
            } else {
                this.platform_ = value;
            }
        }

        private void clearPlatform() {
            this.platform_ = null;
        }

        @Override
        public boolean hasSystem() {
            return this.system_ != null;
        }

        @Override
        public SparkProtos.SystemStatistics getSystem() {
            return this.system_ == null ? SparkProtos.SystemStatistics.getDefaultInstance() : this.system_;
        }

        private void setSystem(SparkProtos.SystemStatistics value) {
            value.getClass();
            this.system_ = value;
        }

        private void mergeSystem(SparkProtos.SystemStatistics value) {
            value.getClass();
            if (this.system_ != null && this.system_ != SparkProtos.SystemStatistics.getDefaultInstance()) {
                this.system_ = SparkProtos.SystemStatistics.newBuilder(this.system_).mergeFrom(value).buildPartial();
            } else {
                this.system_ = value;
            }
        }

        private void clearSystem() {
            this.system_ = null;
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkWebSocketProtos.ServerUpdateStatistics.Builder newBuilder(SparkWebSocketProtos.ServerUpdateStatistics prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.concurrent.ConcurrentHashMap.replaceNode(ConcurrentHashMap.java:1111)
            //   at java.base/java.util.concurrent.ConcurrentHashMap.remove(ConcurrentHashMap.java:1102)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:97)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:667)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkWebSocketProtos$ServerUpdateStatistics.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkWebSocketProtos$ServerUpdateStatistics;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkWebSocketProtos$ServerUpdateStatistics.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkWebSocketProtos$ServerUpdateStatistics$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkWebSocketProtos.ServerUpdateStatistics();
                case NEW_BUILDER:
                    return new SparkWebSocketProtos.ServerUpdateStatistics.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "platform_", "system_" };
                    String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\t\u0002\t";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkWebSocketProtos.ServerUpdateStatistics> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkWebSocketProtos.ServerUpdateStatistics.class) {
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

        public static SparkWebSocketProtos.ServerUpdateStatistics getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkWebSocketProtos.ServerUpdateStatistics> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkWebSocketProtos.ServerUpdateStatistics defaultInstance = new SparkWebSocketProtos.ServerUpdateStatistics();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkWebSocketProtos.ServerUpdateStatistics.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkWebSocketProtos.ServerUpdateStatistics, SparkWebSocketProtos.ServerUpdateStatistics.Builder> implements SparkWebSocketProtos.ServerUpdateStatisticsOrBuilder {

            private Builder() {
                super(SparkWebSocketProtos.ServerUpdateStatistics.DEFAULT_INSTANCE);
            }

            @Override
            public boolean hasPlatform() {
                return this.instance.hasPlatform();
            }

            @Override
            public SparkProtos.PlatformStatistics getPlatform() {
                return this.instance.getPlatform();
            }

            public SparkWebSocketProtos.ServerUpdateStatistics.Builder setPlatform(SparkProtos.PlatformStatistics value) {
                this.copyOnWrite();
                this.instance.setPlatform(value);
                return this;
            }

            public SparkWebSocketProtos.ServerUpdateStatistics.Builder setPlatform(SparkProtos.PlatformStatistics.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setPlatform(builderForValue.build());
                return this;
            }

            public SparkWebSocketProtos.ServerUpdateStatistics.Builder mergePlatform(SparkProtos.PlatformStatistics value) {
                this.copyOnWrite();
                this.instance.mergePlatform(value);
                return this;
            }

            public SparkWebSocketProtos.ServerUpdateStatistics.Builder clearPlatform() {
                this.copyOnWrite();
                this.instance.clearPlatform();
                return this;
            }

            @Override
            public boolean hasSystem() {
                return this.instance.hasSystem();
            }

            @Override
            public SparkProtos.SystemStatistics getSystem() {
                return this.instance.getSystem();
            }

            public SparkWebSocketProtos.ServerUpdateStatistics.Builder setSystem(SparkProtos.SystemStatistics value) {
                this.copyOnWrite();
                this.instance.setSystem(value);
                return this;
            }

            public SparkWebSocketProtos.ServerUpdateStatistics.Builder setSystem(SparkProtos.SystemStatistics.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setSystem(builderForValue.build());
                return this;
            }

            public SparkWebSocketProtos.ServerUpdateStatistics.Builder mergeSystem(SparkProtos.SystemStatistics value) {
                this.copyOnWrite();
                this.instance.mergeSystem(value);
                return this;
            }

            public SparkWebSocketProtos.ServerUpdateStatistics.Builder clearSystem() {
                this.copyOnWrite();
                this.instance.clearSystem();
                return this;
            }
        }
    }

    public interface ServerUpdateStatisticsOrBuilder extends MessageLiteOrBuilder {

        boolean hasPlatform();

        SparkProtos.PlatformStatistics getPlatform();

        boolean hasSystem();

        SparkProtos.SystemStatistics getSystem();
    }
}