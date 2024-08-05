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

public final class SparkSamplerProtos {

    private SparkSamplerProtos() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static final class SamplerData extends GeneratedMessageLite<SparkSamplerProtos.SamplerData, SparkSamplerProtos.SamplerData.Builder> implements SparkSamplerProtos.SamplerDataOrBuilder {

        public static final int METADATA_FIELD_NUMBER = 1;

        private SparkSamplerProtos.SamplerMetadata metadata_;

        public static final int THREADS_FIELD_NUMBER = 2;

        private Internal.ProtobufList<SparkSamplerProtos.ThreadNode> threads_;

        public static final int CLASS_SOURCES_FIELD_NUMBER = 3;

        private MapFieldLite<String, String> classSources_ = MapFieldLite.emptyMapField();

        public static final int METHOD_SOURCES_FIELD_NUMBER = 4;

        private MapFieldLite<String, String> methodSources_ = MapFieldLite.emptyMapField();

        public static final int LINE_SOURCES_FIELD_NUMBER = 5;

        private MapFieldLite<String, String> lineSources_ = MapFieldLite.emptyMapField();

        public static final int TIME_WINDOWS_FIELD_NUMBER = 6;

        private Internal.IntList timeWindows_;

        private int timeWindowsMemoizedSerializedSize = -1;

        public static final int TIME_WINDOW_STATISTICS_FIELD_NUMBER = 7;

        private MapFieldLite<Integer, SparkProtos.WindowStatistics> timeWindowStatistics_ = MapFieldLite.emptyMapField();

        public static final int CHANNEL_INFO_FIELD_NUMBER = 8;

        private SparkSamplerProtos.SocketChannelInfo channelInfo_;

        private static final SparkSamplerProtos.SamplerData DEFAULT_INSTANCE;

        private static volatile Parser<SparkSamplerProtos.SamplerData> PARSER;

        private SamplerData() {
            this.threads_ = emptyProtobufList();
            this.timeWindows_ = emptyIntList();
        }

        @Override
        public boolean hasMetadata() {
            return this.metadata_ != null;
        }

        @Override
        public SparkSamplerProtos.SamplerMetadata getMetadata() {
            return this.metadata_ == null ? SparkSamplerProtos.SamplerMetadata.getDefaultInstance() : this.metadata_;
        }

        private void setMetadata(SparkSamplerProtos.SamplerMetadata value) {
            value.getClass();
            this.metadata_ = value;
        }

        private void mergeMetadata(SparkSamplerProtos.SamplerMetadata value) {
            value.getClass();
            if (this.metadata_ != null && this.metadata_ != SparkSamplerProtos.SamplerMetadata.getDefaultInstance()) {
                this.metadata_ = SparkSamplerProtos.SamplerMetadata.newBuilder(this.metadata_).mergeFrom(value).buildPartial();
            } else {
                this.metadata_ = value;
            }
        }

        private void clearMetadata() {
            this.metadata_ = null;
        }

        @Override
        public List<SparkSamplerProtos.ThreadNode> getThreadsList() {
            return this.threads_;
        }

        public List<? extends SparkSamplerProtos.ThreadNodeOrBuilder> getThreadsOrBuilderList() {
            return this.threads_;
        }

        @Override
        public int getThreadsCount() {
            return this.threads_.size();
        }

        @Override
        public SparkSamplerProtos.ThreadNode getThreads(int index) {
            return (SparkSamplerProtos.ThreadNode) this.threads_.get(index);
        }

        public SparkSamplerProtos.ThreadNodeOrBuilder getThreadsOrBuilder(int index) {
            return (SparkSamplerProtos.ThreadNodeOrBuilder) this.threads_.get(index);
        }

        private void ensureThreadsIsMutable() {
            Internal.ProtobufList<SparkSamplerProtos.ThreadNode> tmp = this.threads_;
            if (!tmp.isModifiable()) {
                this.threads_ = GeneratedMessageLite.mutableCopy(tmp);
            }
        }

        private void setThreads(int index, SparkSamplerProtos.ThreadNode value) {
            value.getClass();
            this.ensureThreadsIsMutable();
            this.threads_.set(index, value);
        }

        private void addThreads(SparkSamplerProtos.ThreadNode value) {
            value.getClass();
            this.ensureThreadsIsMutable();
            this.threads_.add(value);
        }

        private void addThreads(int index, SparkSamplerProtos.ThreadNode value) {
            value.getClass();
            this.ensureThreadsIsMutable();
            this.threads_.add(index, value);
        }

        private void addAllThreads(Iterable<? extends SparkSamplerProtos.ThreadNode> values) {
            this.ensureThreadsIsMutable();
            AbstractMessageLite.addAll(values, this.threads_);
        }

        private void clearThreads() {
            this.threads_ = emptyProtobufList();
        }

        private void removeThreads(int index) {
            this.ensureThreadsIsMutable();
            this.threads_.remove(index);
        }

        private MapFieldLite<String, String> internalGetClassSources() {
            return this.classSources_;
        }

        private MapFieldLite<String, String> internalGetMutableClassSources() {
            if (!this.classSources_.isMutable()) {
                this.classSources_ = this.classSources_.mutableCopy();
            }
            return this.classSources_;
        }

        @Override
        public int getClassSourcesCount() {
            return this.internalGetClassSources().size();
        }

        @Override
        public boolean containsClassSources(String key) {
            Class<?> keyClass = key.getClass();
            return this.internalGetClassSources().containsKey(key);
        }

        @Deprecated
        @Override
        public Map<String, String> getClassSources() {
            return this.getClassSourcesMap();
        }

        @Override
        public Map<String, String> getClassSourcesMap() {
            return Collections.unmodifiableMap(this.internalGetClassSources());
        }

        @Override
        public String getClassSourcesOrDefault(String key, String defaultValue) {
            Class<?> keyClass = key.getClass();
            Map<String, String> map = this.internalGetClassSources();
            return map.containsKey(key) ? (String) map.get(key) : defaultValue;
        }

        @Override
        public String getClassSourcesOrThrow(String key) {
            Class<?> keyClass = key.getClass();
            Map<String, String> map = this.internalGetClassSources();
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException();
            } else {
                return (String) map.get(key);
            }
        }

        private Map<String, String> getMutableClassSourcesMap() {
            return this.internalGetMutableClassSources();
        }

        private MapFieldLite<String, String> internalGetMethodSources() {
            return this.methodSources_;
        }

        private MapFieldLite<String, String> internalGetMutableMethodSources() {
            if (!this.methodSources_.isMutable()) {
                this.methodSources_ = this.methodSources_.mutableCopy();
            }
            return this.methodSources_;
        }

        @Override
        public int getMethodSourcesCount() {
            return this.internalGetMethodSources().size();
        }

        @Override
        public boolean containsMethodSources(String key) {
            Class<?> keyClass = key.getClass();
            return this.internalGetMethodSources().containsKey(key);
        }

        @Deprecated
        @Override
        public Map<String, String> getMethodSources() {
            return this.getMethodSourcesMap();
        }

        @Override
        public Map<String, String> getMethodSourcesMap() {
            return Collections.unmodifiableMap(this.internalGetMethodSources());
        }

        @Override
        public String getMethodSourcesOrDefault(String key, String defaultValue) {
            Class<?> keyClass = key.getClass();
            Map<String, String> map = this.internalGetMethodSources();
            return map.containsKey(key) ? (String) map.get(key) : defaultValue;
        }

        @Override
        public String getMethodSourcesOrThrow(String key) {
            Class<?> keyClass = key.getClass();
            Map<String, String> map = this.internalGetMethodSources();
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException();
            } else {
                return (String) map.get(key);
            }
        }

        private Map<String, String> getMutableMethodSourcesMap() {
            return this.internalGetMutableMethodSources();
        }

        private MapFieldLite<String, String> internalGetLineSources() {
            return this.lineSources_;
        }

        private MapFieldLite<String, String> internalGetMutableLineSources() {
            if (!this.lineSources_.isMutable()) {
                this.lineSources_ = this.lineSources_.mutableCopy();
            }
            return this.lineSources_;
        }

        @Override
        public int getLineSourcesCount() {
            return this.internalGetLineSources().size();
        }

        @Override
        public boolean containsLineSources(String key) {
            Class<?> keyClass = key.getClass();
            return this.internalGetLineSources().containsKey(key);
        }

        @Deprecated
        @Override
        public Map<String, String> getLineSources() {
            return this.getLineSourcesMap();
        }

        @Override
        public Map<String, String> getLineSourcesMap() {
            return Collections.unmodifiableMap(this.internalGetLineSources());
        }

        @Override
        public String getLineSourcesOrDefault(String key, String defaultValue) {
            Class<?> keyClass = key.getClass();
            Map<String, String> map = this.internalGetLineSources();
            return map.containsKey(key) ? (String) map.get(key) : defaultValue;
        }

        @Override
        public String getLineSourcesOrThrow(String key) {
            Class<?> keyClass = key.getClass();
            Map<String, String> map = this.internalGetLineSources();
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException();
            } else {
                return (String) map.get(key);
            }
        }

        private Map<String, String> getMutableLineSourcesMap() {
            return this.internalGetMutableLineSources();
        }

        @Override
        public List<Integer> getTimeWindowsList() {
            return this.timeWindows_;
        }

        @Override
        public int getTimeWindowsCount() {
            return this.timeWindows_.size();
        }

        @Override
        public int getTimeWindows(int index) {
            return this.timeWindows_.getInt(index);
        }

        private void ensureTimeWindowsIsMutable() {
            Internal.IntList tmp = this.timeWindows_;
            if (!tmp.isModifiable()) {
                this.timeWindows_ = GeneratedMessageLite.mutableCopy(tmp);
            }
        }

        private void setTimeWindows(int index, int value) {
            this.ensureTimeWindowsIsMutable();
            this.timeWindows_.setInt(index, value);
        }

        private void addTimeWindows(int value) {
            this.ensureTimeWindowsIsMutable();
            this.timeWindows_.addInt(value);
        }

        private void addAllTimeWindows(Iterable<? extends Integer> values) {
            this.ensureTimeWindowsIsMutable();
            AbstractMessageLite.addAll(values, this.timeWindows_);
        }

        private void clearTimeWindows() {
            this.timeWindows_ = emptyIntList();
        }

        private MapFieldLite<Integer, SparkProtos.WindowStatistics> internalGetTimeWindowStatistics() {
            return this.timeWindowStatistics_;
        }

        private MapFieldLite<Integer, SparkProtos.WindowStatistics> internalGetMutableTimeWindowStatistics() {
            if (!this.timeWindowStatistics_.isMutable()) {
                this.timeWindowStatistics_ = this.timeWindowStatistics_.mutableCopy();
            }
            return this.timeWindowStatistics_;
        }

        @Override
        public int getTimeWindowStatisticsCount() {
            return this.internalGetTimeWindowStatistics().size();
        }

        @Override
        public boolean containsTimeWindowStatistics(int key) {
            return this.internalGetTimeWindowStatistics().containsKey(key);
        }

        @Deprecated
        @Override
        public Map<Integer, SparkProtos.WindowStatistics> getTimeWindowStatistics() {
            return this.getTimeWindowStatisticsMap();
        }

        @Override
        public Map<Integer, SparkProtos.WindowStatistics> getTimeWindowStatisticsMap() {
            return Collections.unmodifiableMap(this.internalGetTimeWindowStatistics());
        }

        @Override
        public SparkProtos.WindowStatistics getTimeWindowStatisticsOrDefault(int key, SparkProtos.WindowStatistics defaultValue) {
            Map<Integer, SparkProtos.WindowStatistics> map = this.internalGetTimeWindowStatistics();
            return map.containsKey(key) ? (SparkProtos.WindowStatistics) map.get(key) : defaultValue;
        }

        @Override
        public SparkProtos.WindowStatistics getTimeWindowStatisticsOrThrow(int key) {
            Map<Integer, SparkProtos.WindowStatistics> map = this.internalGetTimeWindowStatistics();
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException();
            } else {
                return (SparkProtos.WindowStatistics) map.get(key);
            }
        }

        private Map<Integer, SparkProtos.WindowStatistics> getMutableTimeWindowStatisticsMap() {
            return this.internalGetMutableTimeWindowStatistics();
        }

        @Override
        public boolean hasChannelInfo() {
            return this.channelInfo_ != null;
        }

        @Override
        public SparkSamplerProtos.SocketChannelInfo getChannelInfo() {
            return this.channelInfo_ == null ? SparkSamplerProtos.SocketChannelInfo.getDefaultInstance() : this.channelInfo_;
        }

        private void setChannelInfo(SparkSamplerProtos.SocketChannelInfo value) {
            value.getClass();
            this.channelInfo_ = value;
        }

        private void mergeChannelInfo(SparkSamplerProtos.SocketChannelInfo value) {
            value.getClass();
            if (this.channelInfo_ != null && this.channelInfo_ != SparkSamplerProtos.SocketChannelInfo.getDefaultInstance()) {
                this.channelInfo_ = SparkSamplerProtos.SocketChannelInfo.newBuilder(this.channelInfo_).mergeFrom(value).buildPartial();
            } else {
                this.channelInfo_ = value;
            }
        }

        private void clearChannelInfo() {
            this.channelInfo_ = null;
        }

        public static SparkSamplerProtos.SamplerData parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.SamplerData parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.SamplerData parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.SamplerData parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.SamplerData parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.SamplerData parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.SamplerData parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.SamplerData parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.SamplerData parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.SamplerData parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.SamplerData parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.SamplerData parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.SamplerData.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkSamplerProtos.SamplerData.Builder newBuilder(SparkSamplerProtos.SamplerData prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.HashMap.hash(HashMap.java:338)
            //   at java.base/java.util.HashMap.getNode(HashMap.java:568)
            //   at java.base/java.util.HashMap.containsKey(HashMap.java:594)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.VarType.remap(VarType.java:428)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.remap(GenericType.java:350)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:693)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkSamplerProtos$SamplerData.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkSamplerProtos$SamplerData;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkSamplerProtos$SamplerData.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkSamplerProtos$SamplerData$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkSamplerProtos.SamplerData();
                case NEW_BUILDER:
                    return new SparkSamplerProtos.SamplerData.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "metadata_", "threads_", SparkSamplerProtos.ThreadNode.class, "classSources_", SparkSamplerProtos.SamplerData.ClassSourcesDefaultEntryHolder.defaultEntry, "methodSources_", SparkSamplerProtos.SamplerData.MethodSourcesDefaultEntryHolder.defaultEntry, "lineSources_", SparkSamplerProtos.SamplerData.LineSourcesDefaultEntryHolder.defaultEntry, "timeWindows_", "timeWindowStatistics_", SparkSamplerProtos.SamplerData.TimeWindowStatisticsDefaultEntryHolder.defaultEntry, "channelInfo_" };
                    String info = "\u0000\b\u0000\u0000\u0001\b\b\u0004\u0002\u0000\u0001\t\u0002\u001b\u00032\u00042\u00052\u0006'\u00072\b\t";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkSamplerProtos.SamplerData> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkSamplerProtos.SamplerData.class) {
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

        public static SparkSamplerProtos.SamplerData getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkSamplerProtos.SamplerData> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkSamplerProtos.SamplerData defaultInstance = new SparkSamplerProtos.SamplerData();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkSamplerProtos.SamplerData.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkSamplerProtos.SamplerData, SparkSamplerProtos.SamplerData.Builder> implements SparkSamplerProtos.SamplerDataOrBuilder {

            private Builder() {
                super(SparkSamplerProtos.SamplerData.DEFAULT_INSTANCE);
            }

            @Override
            public boolean hasMetadata() {
                return this.instance.hasMetadata();
            }

            @Override
            public SparkSamplerProtos.SamplerMetadata getMetadata() {
                return this.instance.getMetadata();
            }

            public SparkSamplerProtos.SamplerData.Builder setMetadata(SparkSamplerProtos.SamplerMetadata value) {
                this.copyOnWrite();
                this.instance.setMetadata(value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder setMetadata(SparkSamplerProtos.SamplerMetadata.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setMetadata(builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder mergeMetadata(SparkSamplerProtos.SamplerMetadata value) {
                this.copyOnWrite();
                this.instance.mergeMetadata(value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder clearMetadata() {
                this.copyOnWrite();
                this.instance.clearMetadata();
                return this;
            }

            @Override
            public List<SparkSamplerProtos.ThreadNode> getThreadsList() {
                return Collections.unmodifiableList(this.instance.getThreadsList());
            }

            @Override
            public int getThreadsCount() {
                return this.instance.getThreadsCount();
            }

            @Override
            public SparkSamplerProtos.ThreadNode getThreads(int index) {
                return this.instance.getThreads(index);
            }

            public SparkSamplerProtos.SamplerData.Builder setThreads(int index, SparkSamplerProtos.ThreadNode value) {
                this.copyOnWrite();
                this.instance.setThreads(index, value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder setThreads(int index, SparkSamplerProtos.ThreadNode.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setThreads(index, builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder addThreads(SparkSamplerProtos.ThreadNode value) {
                this.copyOnWrite();
                this.instance.addThreads(value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder addThreads(int index, SparkSamplerProtos.ThreadNode value) {
                this.copyOnWrite();
                this.instance.addThreads(index, value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder addThreads(SparkSamplerProtos.ThreadNode.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.addThreads(builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder addThreads(int index, SparkSamplerProtos.ThreadNode.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.addThreads(index, builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder addAllThreads(Iterable<? extends SparkSamplerProtos.ThreadNode> values) {
                this.copyOnWrite();
                this.instance.addAllThreads(values);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder clearThreads() {
                this.copyOnWrite();
                this.instance.clearThreads();
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder removeThreads(int index) {
                this.copyOnWrite();
                this.instance.removeThreads(index);
                return this;
            }

            @Override
            public int getClassSourcesCount() {
                return this.instance.getClassSourcesMap().size();
            }

            @Override
            public boolean containsClassSources(String key) {
                Class<?> keyClass = key.getClass();
                return this.instance.getClassSourcesMap().containsKey(key);
            }

            public SparkSamplerProtos.SamplerData.Builder clearClassSources() {
                this.copyOnWrite();
                this.instance.getMutableClassSourcesMap().clear();
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder removeClassSources(String key) {
                Class<?> keyClass = key.getClass();
                this.copyOnWrite();
                this.instance.getMutableClassSourcesMap().remove(key);
                return this;
            }

            @Deprecated
            @Override
            public Map<String, String> getClassSources() {
                return this.getClassSourcesMap();
            }

            @Override
            public Map<String, String> getClassSourcesMap() {
                return Collections.unmodifiableMap(this.instance.getClassSourcesMap());
            }

            @Override
            public String getClassSourcesOrDefault(String key, String defaultValue) {
                Class<?> keyClass = key.getClass();
                Map<String, String> map = this.instance.getClassSourcesMap();
                return map.containsKey(key) ? (String) map.get(key) : defaultValue;
            }

            @Override
            public String getClassSourcesOrThrow(String key) {
                Class<?> keyClass = key.getClass();
                Map<String, String> map = this.instance.getClassSourcesMap();
                if (!map.containsKey(key)) {
                    throw new IllegalArgumentException();
                } else {
                    return (String) map.get(key);
                }
            }

            public SparkSamplerProtos.SamplerData.Builder putClassSources(String key, String value) {
                Class<?> keyClass = key.getClass();
                Class<?> valueClass = value.getClass();
                this.copyOnWrite();
                this.instance.getMutableClassSourcesMap().put(key, value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder putAllClassSources(Map<String, String> values) {
                this.copyOnWrite();
                this.instance.getMutableClassSourcesMap().putAll(values);
                return this;
            }

            @Override
            public int getMethodSourcesCount() {
                return this.instance.getMethodSourcesMap().size();
            }

            @Override
            public boolean containsMethodSources(String key) {
                Class<?> keyClass = key.getClass();
                return this.instance.getMethodSourcesMap().containsKey(key);
            }

            public SparkSamplerProtos.SamplerData.Builder clearMethodSources() {
                this.copyOnWrite();
                this.instance.getMutableMethodSourcesMap().clear();
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder removeMethodSources(String key) {
                Class<?> keyClass = key.getClass();
                this.copyOnWrite();
                this.instance.getMutableMethodSourcesMap().remove(key);
                return this;
            }

            @Deprecated
            @Override
            public Map<String, String> getMethodSources() {
                return this.getMethodSourcesMap();
            }

            @Override
            public Map<String, String> getMethodSourcesMap() {
                return Collections.unmodifiableMap(this.instance.getMethodSourcesMap());
            }

            @Override
            public String getMethodSourcesOrDefault(String key, String defaultValue) {
                Class<?> keyClass = key.getClass();
                Map<String, String> map = this.instance.getMethodSourcesMap();
                return map.containsKey(key) ? (String) map.get(key) : defaultValue;
            }

            @Override
            public String getMethodSourcesOrThrow(String key) {
                Class<?> keyClass = key.getClass();
                Map<String, String> map = this.instance.getMethodSourcesMap();
                if (!map.containsKey(key)) {
                    throw new IllegalArgumentException();
                } else {
                    return (String) map.get(key);
                }
            }

            public SparkSamplerProtos.SamplerData.Builder putMethodSources(String key, String value) {
                Class<?> keyClass = key.getClass();
                Class<?> valueClass = value.getClass();
                this.copyOnWrite();
                this.instance.getMutableMethodSourcesMap().put(key, value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder putAllMethodSources(Map<String, String> values) {
                this.copyOnWrite();
                this.instance.getMutableMethodSourcesMap().putAll(values);
                return this;
            }

            @Override
            public int getLineSourcesCount() {
                return this.instance.getLineSourcesMap().size();
            }

            @Override
            public boolean containsLineSources(String key) {
                Class<?> keyClass = key.getClass();
                return this.instance.getLineSourcesMap().containsKey(key);
            }

            public SparkSamplerProtos.SamplerData.Builder clearLineSources() {
                this.copyOnWrite();
                this.instance.getMutableLineSourcesMap().clear();
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder removeLineSources(String key) {
                Class<?> keyClass = key.getClass();
                this.copyOnWrite();
                this.instance.getMutableLineSourcesMap().remove(key);
                return this;
            }

            @Deprecated
            @Override
            public Map<String, String> getLineSources() {
                return this.getLineSourcesMap();
            }

            @Override
            public Map<String, String> getLineSourcesMap() {
                return Collections.unmodifiableMap(this.instance.getLineSourcesMap());
            }

            @Override
            public String getLineSourcesOrDefault(String key, String defaultValue) {
                Class<?> keyClass = key.getClass();
                Map<String, String> map = this.instance.getLineSourcesMap();
                return map.containsKey(key) ? (String) map.get(key) : defaultValue;
            }

            @Override
            public String getLineSourcesOrThrow(String key) {
                Class<?> keyClass = key.getClass();
                Map<String, String> map = this.instance.getLineSourcesMap();
                if (!map.containsKey(key)) {
                    throw new IllegalArgumentException();
                } else {
                    return (String) map.get(key);
                }
            }

            public SparkSamplerProtos.SamplerData.Builder putLineSources(String key, String value) {
                Class<?> keyClass = key.getClass();
                Class<?> valueClass = value.getClass();
                this.copyOnWrite();
                this.instance.getMutableLineSourcesMap().put(key, value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder putAllLineSources(Map<String, String> values) {
                this.copyOnWrite();
                this.instance.getMutableLineSourcesMap().putAll(values);
                return this;
            }

            @Override
            public List<Integer> getTimeWindowsList() {
                return Collections.unmodifiableList(this.instance.getTimeWindowsList());
            }

            @Override
            public int getTimeWindowsCount() {
                return this.instance.getTimeWindowsCount();
            }

            @Override
            public int getTimeWindows(int index) {
                return this.instance.getTimeWindows(index);
            }

            public SparkSamplerProtos.SamplerData.Builder setTimeWindows(int index, int value) {
                this.copyOnWrite();
                this.instance.setTimeWindows(index, value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder addTimeWindows(int value) {
                this.copyOnWrite();
                this.instance.addTimeWindows(value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder addAllTimeWindows(Iterable<? extends Integer> values) {
                this.copyOnWrite();
                this.instance.addAllTimeWindows(values);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder clearTimeWindows() {
                this.copyOnWrite();
                this.instance.clearTimeWindows();
                return this;
            }

            @Override
            public int getTimeWindowStatisticsCount() {
                return this.instance.getTimeWindowStatisticsMap().size();
            }

            @Override
            public boolean containsTimeWindowStatistics(int key) {
                return this.instance.getTimeWindowStatisticsMap().containsKey(key);
            }

            public SparkSamplerProtos.SamplerData.Builder clearTimeWindowStatistics() {
                this.copyOnWrite();
                this.instance.getMutableTimeWindowStatisticsMap().clear();
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder removeTimeWindowStatistics(int key) {
                this.copyOnWrite();
                this.instance.getMutableTimeWindowStatisticsMap().remove(key);
                return this;
            }

            @Deprecated
            @Override
            public Map<Integer, SparkProtos.WindowStatistics> getTimeWindowStatistics() {
                return this.getTimeWindowStatisticsMap();
            }

            @Override
            public Map<Integer, SparkProtos.WindowStatistics> getTimeWindowStatisticsMap() {
                return Collections.unmodifiableMap(this.instance.getTimeWindowStatisticsMap());
            }

            @Override
            public SparkProtos.WindowStatistics getTimeWindowStatisticsOrDefault(int key, SparkProtos.WindowStatistics defaultValue) {
                Map<Integer, SparkProtos.WindowStatistics> map = this.instance.getTimeWindowStatisticsMap();
                return map.containsKey(key) ? (SparkProtos.WindowStatistics) map.get(key) : defaultValue;
            }

            @Override
            public SparkProtos.WindowStatistics getTimeWindowStatisticsOrThrow(int key) {
                Map<Integer, SparkProtos.WindowStatistics> map = this.instance.getTimeWindowStatisticsMap();
                if (!map.containsKey(key)) {
                    throw new IllegalArgumentException();
                } else {
                    return (SparkProtos.WindowStatistics) map.get(key);
                }
            }

            public SparkSamplerProtos.SamplerData.Builder putTimeWindowStatistics(int key, SparkProtos.WindowStatistics value) {
                Class<?> valueClass = value.getClass();
                this.copyOnWrite();
                this.instance.getMutableTimeWindowStatisticsMap().put(key, value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder putAllTimeWindowStatistics(Map<Integer, SparkProtos.WindowStatistics> values) {
                this.copyOnWrite();
                this.instance.getMutableTimeWindowStatisticsMap().putAll(values);
                return this;
            }

            @Override
            public boolean hasChannelInfo() {
                return this.instance.hasChannelInfo();
            }

            @Override
            public SparkSamplerProtos.SocketChannelInfo getChannelInfo() {
                return this.instance.getChannelInfo();
            }

            public SparkSamplerProtos.SamplerData.Builder setChannelInfo(SparkSamplerProtos.SocketChannelInfo value) {
                this.copyOnWrite();
                this.instance.setChannelInfo(value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder setChannelInfo(SparkSamplerProtos.SocketChannelInfo.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setChannelInfo(builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder mergeChannelInfo(SparkSamplerProtos.SocketChannelInfo value) {
                this.copyOnWrite();
                this.instance.mergeChannelInfo(value);
                return this;
            }

            public SparkSamplerProtos.SamplerData.Builder clearChannelInfo() {
                this.copyOnWrite();
                this.instance.clearChannelInfo();
                return this;
            }
        }

        private static final class ClassSourcesDefaultEntryHolder {

            static final MapEntryLite<String, String> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.STRING, "", WireFormat.FieldType.STRING, "");
        }

        private static final class LineSourcesDefaultEntryHolder {

            static final MapEntryLite<String, String> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.STRING, "", WireFormat.FieldType.STRING, "");
        }

        private static final class MethodSourcesDefaultEntryHolder {

            static final MapEntryLite<String, String> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.STRING, "", WireFormat.FieldType.STRING, "");
        }

        private static final class TimeWindowStatisticsDefaultEntryHolder {

            static final MapEntryLite<Integer, SparkProtos.WindowStatistics> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.INT32, 0, WireFormat.FieldType.MESSAGE, SparkProtos.WindowStatistics.getDefaultInstance());
        }
    }

    public interface SamplerDataOrBuilder extends MessageLiteOrBuilder {

        boolean hasMetadata();

        SparkSamplerProtos.SamplerMetadata getMetadata();

        List<SparkSamplerProtos.ThreadNode> getThreadsList();

        SparkSamplerProtos.ThreadNode getThreads(int var1);

        int getThreadsCount();

        int getClassSourcesCount();

        boolean containsClassSources(String var1);

        @Deprecated
        Map<String, String> getClassSources();

        Map<String, String> getClassSourcesMap();

        String getClassSourcesOrDefault(String var1, String var2);

        String getClassSourcesOrThrow(String var1);

        int getMethodSourcesCount();

        boolean containsMethodSources(String var1);

        @Deprecated
        Map<String, String> getMethodSources();

        Map<String, String> getMethodSourcesMap();

        String getMethodSourcesOrDefault(String var1, String var2);

        String getMethodSourcesOrThrow(String var1);

        int getLineSourcesCount();

        boolean containsLineSources(String var1);

        @Deprecated
        Map<String, String> getLineSources();

        Map<String, String> getLineSourcesMap();

        String getLineSourcesOrDefault(String var1, String var2);

        String getLineSourcesOrThrow(String var1);

        List<Integer> getTimeWindowsList();

        int getTimeWindowsCount();

        int getTimeWindows(int var1);

        int getTimeWindowStatisticsCount();

        boolean containsTimeWindowStatistics(int var1);

        @Deprecated
        Map<Integer, SparkProtos.WindowStatistics> getTimeWindowStatistics();

        Map<Integer, SparkProtos.WindowStatistics> getTimeWindowStatisticsMap();

        SparkProtos.WindowStatistics getTimeWindowStatisticsOrDefault(int var1, SparkProtos.WindowStatistics var2);

        SparkProtos.WindowStatistics getTimeWindowStatisticsOrThrow(int var1);

        boolean hasChannelInfo();

        SparkSamplerProtos.SocketChannelInfo getChannelInfo();
    }

    public static final class SamplerMetadata extends GeneratedMessageLite<SparkSamplerProtos.SamplerMetadata, SparkSamplerProtos.SamplerMetadata.Builder> implements SparkSamplerProtos.SamplerMetadataOrBuilder {

        public static final int CREATOR_FIELD_NUMBER = 1;

        private SparkProtos.CommandSenderMetadata creator_;

        public static final int START_TIME_FIELD_NUMBER = 2;

        private long startTime_;

        public static final int INTERVAL_FIELD_NUMBER = 3;

        private int interval_;

        public static final int THREAD_DUMPER_FIELD_NUMBER = 4;

        private SparkSamplerProtos.SamplerMetadata.ThreadDumper threadDumper_;

        public static final int DATA_AGGREGATOR_FIELD_NUMBER = 5;

        private SparkSamplerProtos.SamplerMetadata.DataAggregator dataAggregator_;

        public static final int COMMENT_FIELD_NUMBER = 6;

        private String comment_;

        public static final int PLATFORM_METADATA_FIELD_NUMBER = 7;

        private SparkProtos.PlatformMetadata platformMetadata_;

        public static final int PLATFORM_STATISTICS_FIELD_NUMBER = 8;

        private SparkProtos.PlatformStatistics platformStatistics_;

        public static final int SYSTEM_STATISTICS_FIELD_NUMBER = 9;

        private SparkProtos.SystemStatistics systemStatistics_;

        public static final int SERVER_CONFIGURATIONS_FIELD_NUMBER = 10;

        private MapFieldLite<String, String> serverConfigurations_ = MapFieldLite.emptyMapField();

        public static final int END_TIME_FIELD_NUMBER = 11;

        private long endTime_;

        public static final int NUMBER_OF_TICKS_FIELD_NUMBER = 12;

        private int numberOfTicks_;

        public static final int SOURCES_FIELD_NUMBER = 13;

        private MapFieldLite<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> sources_ = MapFieldLite.emptyMapField();

        public static final int EXTRA_PLATFORM_METADATA_FIELD_NUMBER = 14;

        private MapFieldLite<String, String> extraPlatformMetadata_ = MapFieldLite.emptyMapField();

        public static final int SAMPLER_MODE_FIELD_NUMBER = 15;

        private int samplerMode_;

        private static final SparkSamplerProtos.SamplerMetadata DEFAULT_INSTANCE;

        private static volatile Parser<SparkSamplerProtos.SamplerMetadata> PARSER;

        private SamplerMetadata() {
            this.comment_ = "";
        }

        @Override
        public boolean hasCreator() {
            return this.creator_ != null;
        }

        @Override
        public SparkProtos.CommandSenderMetadata getCreator() {
            return this.creator_ == null ? SparkProtos.CommandSenderMetadata.getDefaultInstance() : this.creator_;
        }

        private void setCreator(SparkProtos.CommandSenderMetadata value) {
            value.getClass();
            this.creator_ = value;
        }

        private void mergeCreator(SparkProtos.CommandSenderMetadata value) {
            value.getClass();
            if (this.creator_ != null && this.creator_ != SparkProtos.CommandSenderMetadata.getDefaultInstance()) {
                this.creator_ = SparkProtos.CommandSenderMetadata.newBuilder(this.creator_).mergeFrom(value).buildPartial();
            } else {
                this.creator_ = value;
            }
        }

        private void clearCreator() {
            this.creator_ = null;
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
        public int getInterval() {
            return this.interval_;
        }

        private void setInterval(int value) {
            this.interval_ = value;
        }

        private void clearInterval() {
            this.interval_ = 0;
        }

        @Override
        public boolean hasThreadDumper() {
            return this.threadDumper_ != null;
        }

        @Override
        public SparkSamplerProtos.SamplerMetadata.ThreadDumper getThreadDumper() {
            return this.threadDumper_ == null ? SparkSamplerProtos.SamplerMetadata.ThreadDumper.getDefaultInstance() : this.threadDumper_;
        }

        private void setThreadDumper(SparkSamplerProtos.SamplerMetadata.ThreadDumper value) {
            value.getClass();
            this.threadDumper_ = value;
        }

        private void mergeThreadDumper(SparkSamplerProtos.SamplerMetadata.ThreadDumper value) {
            value.getClass();
            if (this.threadDumper_ != null && this.threadDumper_ != SparkSamplerProtos.SamplerMetadata.ThreadDumper.getDefaultInstance()) {
                this.threadDumper_ = SparkSamplerProtos.SamplerMetadata.ThreadDumper.newBuilder(this.threadDumper_).mergeFrom(value).buildPartial();
            } else {
                this.threadDumper_ = value;
            }
        }

        private void clearThreadDumper() {
            this.threadDumper_ = null;
        }

        @Override
        public boolean hasDataAggregator() {
            return this.dataAggregator_ != null;
        }

        @Override
        public SparkSamplerProtos.SamplerMetadata.DataAggregator getDataAggregator() {
            return this.dataAggregator_ == null ? SparkSamplerProtos.SamplerMetadata.DataAggregator.getDefaultInstance() : this.dataAggregator_;
        }

        private void setDataAggregator(SparkSamplerProtos.SamplerMetadata.DataAggregator value) {
            value.getClass();
            this.dataAggregator_ = value;
        }

        private void mergeDataAggregator(SparkSamplerProtos.SamplerMetadata.DataAggregator value) {
            value.getClass();
            if (this.dataAggregator_ != null && this.dataAggregator_ != SparkSamplerProtos.SamplerMetadata.DataAggregator.getDefaultInstance()) {
                this.dataAggregator_ = SparkSamplerProtos.SamplerMetadata.DataAggregator.newBuilder(this.dataAggregator_).mergeFrom(value).buildPartial();
            } else {
                this.dataAggregator_ = value;
            }
        }

        private void clearDataAggregator() {
            this.dataAggregator_ = null;
        }

        @Override
        public String getComment() {
            return this.comment_;
        }

        @Override
        public ByteString getCommentBytes() {
            return ByteString.copyFromUtf8(this.comment_);
        }

        private void setComment(String value) {
            Class<?> valueClass = value.getClass();
            this.comment_ = value;
        }

        private void clearComment() {
            this.comment_ = getDefaultInstance().getComment();
        }

        private void setCommentBytes(ByteString value) {
            checkByteStringIsUtf8(value);
            this.comment_ = value.toStringUtf8();
        }

        @Override
        public boolean hasPlatformMetadata() {
            return this.platformMetadata_ != null;
        }

        @Override
        public SparkProtos.PlatformMetadata getPlatformMetadata() {
            return this.platformMetadata_ == null ? SparkProtos.PlatformMetadata.getDefaultInstance() : this.platformMetadata_;
        }

        private void setPlatformMetadata(SparkProtos.PlatformMetadata value) {
            value.getClass();
            this.platformMetadata_ = value;
        }

        private void mergePlatformMetadata(SparkProtos.PlatformMetadata value) {
            value.getClass();
            if (this.platformMetadata_ != null && this.platformMetadata_ != SparkProtos.PlatformMetadata.getDefaultInstance()) {
                this.platformMetadata_ = SparkProtos.PlatformMetadata.newBuilder(this.platformMetadata_).mergeFrom(value).buildPartial();
            } else {
                this.platformMetadata_ = value;
            }
        }

        private void clearPlatformMetadata() {
            this.platformMetadata_ = null;
        }

        @Override
        public boolean hasPlatformStatistics() {
            return this.platformStatistics_ != null;
        }

        @Override
        public SparkProtos.PlatformStatistics getPlatformStatistics() {
            return this.platformStatistics_ == null ? SparkProtos.PlatformStatistics.getDefaultInstance() : this.platformStatistics_;
        }

        private void setPlatformStatistics(SparkProtos.PlatformStatistics value) {
            value.getClass();
            this.platformStatistics_ = value;
        }

        private void mergePlatformStatistics(SparkProtos.PlatformStatistics value) {
            value.getClass();
            if (this.platformStatistics_ != null && this.platformStatistics_ != SparkProtos.PlatformStatistics.getDefaultInstance()) {
                this.platformStatistics_ = SparkProtos.PlatformStatistics.newBuilder(this.platformStatistics_).mergeFrom(value).buildPartial();
            } else {
                this.platformStatistics_ = value;
            }
        }

        private void clearPlatformStatistics() {
            this.platformStatistics_ = null;
        }

        @Override
        public boolean hasSystemStatistics() {
            return this.systemStatistics_ != null;
        }

        @Override
        public SparkProtos.SystemStatistics getSystemStatistics() {
            return this.systemStatistics_ == null ? SparkProtos.SystemStatistics.getDefaultInstance() : this.systemStatistics_;
        }

        private void setSystemStatistics(SparkProtos.SystemStatistics value) {
            value.getClass();
            this.systemStatistics_ = value;
        }

        private void mergeSystemStatistics(SparkProtos.SystemStatistics value) {
            value.getClass();
            if (this.systemStatistics_ != null && this.systemStatistics_ != SparkProtos.SystemStatistics.getDefaultInstance()) {
                this.systemStatistics_ = SparkProtos.SystemStatistics.newBuilder(this.systemStatistics_).mergeFrom(value).buildPartial();
            } else {
                this.systemStatistics_ = value;
            }
        }

        private void clearSystemStatistics() {
            this.systemStatistics_ = null;
        }

        private MapFieldLite<String, String> internalGetServerConfigurations() {
            return this.serverConfigurations_;
        }

        private MapFieldLite<String, String> internalGetMutableServerConfigurations() {
            if (!this.serverConfigurations_.isMutable()) {
                this.serverConfigurations_ = this.serverConfigurations_.mutableCopy();
            }
            return this.serverConfigurations_;
        }

        @Override
        public int getServerConfigurationsCount() {
            return this.internalGetServerConfigurations().size();
        }

        @Override
        public boolean containsServerConfigurations(String key) {
            Class<?> keyClass = key.getClass();
            return this.internalGetServerConfigurations().containsKey(key);
        }

        @Deprecated
        @Override
        public Map<String, String> getServerConfigurations() {
            return this.getServerConfigurationsMap();
        }

        @Override
        public Map<String, String> getServerConfigurationsMap() {
            return Collections.unmodifiableMap(this.internalGetServerConfigurations());
        }

        @Override
        public String getServerConfigurationsOrDefault(String key, String defaultValue) {
            Class<?> keyClass = key.getClass();
            Map<String, String> map = this.internalGetServerConfigurations();
            return map.containsKey(key) ? (String) map.get(key) : defaultValue;
        }

        @Override
        public String getServerConfigurationsOrThrow(String key) {
            Class<?> keyClass = key.getClass();
            Map<String, String> map = this.internalGetServerConfigurations();
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException();
            } else {
                return (String) map.get(key);
            }
        }

        private Map<String, String> getMutableServerConfigurationsMap() {
            return this.internalGetMutableServerConfigurations();
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
        public int getNumberOfTicks() {
            return this.numberOfTicks_;
        }

        private void setNumberOfTicks(int value) {
            this.numberOfTicks_ = value;
        }

        private void clearNumberOfTicks() {
            this.numberOfTicks_ = 0;
        }

        private MapFieldLite<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> internalGetSources() {
            return this.sources_;
        }

        private MapFieldLite<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> internalGetMutableSources() {
            if (!this.sources_.isMutable()) {
                this.sources_ = this.sources_.mutableCopy();
            }
            return this.sources_;
        }

        @Override
        public int getSourcesCount() {
            return this.internalGetSources().size();
        }

        @Override
        public boolean containsSources(String key) {
            Class<?> keyClass = key.getClass();
            return this.internalGetSources().containsKey(key);
        }

        @Deprecated
        @Override
        public Map<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> getSources() {
            return this.getSourcesMap();
        }

        @Override
        public Map<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> getSourcesMap() {
            return Collections.unmodifiableMap(this.internalGetSources());
        }

        @Override
        public SparkSamplerProtos.SamplerMetadata.SourceMetadata getSourcesOrDefault(String key, SparkSamplerProtos.SamplerMetadata.SourceMetadata defaultValue) {
            Class<?> keyClass = key.getClass();
            Map<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> map = this.internalGetSources();
            return map.containsKey(key) ? (SparkSamplerProtos.SamplerMetadata.SourceMetadata) map.get(key) : defaultValue;
        }

        @Override
        public SparkSamplerProtos.SamplerMetadata.SourceMetadata getSourcesOrThrow(String key) {
            Class<?> keyClass = key.getClass();
            Map<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> map = this.internalGetSources();
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException();
            } else {
                return (SparkSamplerProtos.SamplerMetadata.SourceMetadata) map.get(key);
            }
        }

        private Map<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> getMutableSourcesMap() {
            return this.internalGetMutableSources();
        }

        private MapFieldLite<String, String> internalGetExtraPlatformMetadata() {
            return this.extraPlatformMetadata_;
        }

        private MapFieldLite<String, String> internalGetMutableExtraPlatformMetadata() {
            if (!this.extraPlatformMetadata_.isMutable()) {
                this.extraPlatformMetadata_ = this.extraPlatformMetadata_.mutableCopy();
            }
            return this.extraPlatformMetadata_;
        }

        @Override
        public int getExtraPlatformMetadataCount() {
            return this.internalGetExtraPlatformMetadata().size();
        }

        @Override
        public boolean containsExtraPlatformMetadata(String key) {
            Class<?> keyClass = key.getClass();
            return this.internalGetExtraPlatformMetadata().containsKey(key);
        }

        @Deprecated
        @Override
        public Map<String, String> getExtraPlatformMetadata() {
            return this.getExtraPlatformMetadataMap();
        }

        @Override
        public Map<String, String> getExtraPlatformMetadataMap() {
            return Collections.unmodifiableMap(this.internalGetExtraPlatformMetadata());
        }

        @Override
        public String getExtraPlatformMetadataOrDefault(String key, String defaultValue) {
            Class<?> keyClass = key.getClass();
            Map<String, String> map = this.internalGetExtraPlatformMetadata();
            return map.containsKey(key) ? (String) map.get(key) : defaultValue;
        }

        @Override
        public String getExtraPlatformMetadataOrThrow(String key) {
            Class<?> keyClass = key.getClass();
            Map<String, String> map = this.internalGetExtraPlatformMetadata();
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException();
            } else {
                return (String) map.get(key);
            }
        }

        private Map<String, String> getMutableExtraPlatformMetadataMap() {
            return this.internalGetMutableExtraPlatformMetadata();
        }

        @Override
        public int getSamplerModeValue() {
            return this.samplerMode_;
        }

        @Override
        public SparkSamplerProtos.SamplerMetadata.SamplerMode getSamplerMode() {
            SparkSamplerProtos.SamplerMetadata.SamplerMode result = SparkSamplerProtos.SamplerMetadata.SamplerMode.forNumber(this.samplerMode_);
            return result == null ? SparkSamplerProtos.SamplerMetadata.SamplerMode.UNRECOGNIZED : result;
        }

        private void setSamplerModeValue(int value) {
            this.samplerMode_ = value;
        }

        private void setSamplerMode(SparkSamplerProtos.SamplerMetadata.SamplerMode value) {
            this.samplerMode_ = value.getNumber();
        }

        private void clearSamplerMode() {
            this.samplerMode_ = 0;
        }

        public static SparkSamplerProtos.SamplerMetadata parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.SamplerMetadata parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.SamplerMetadata parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.SamplerMetadata parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.SamplerMetadata parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.SamplerMetadata parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.SamplerMetadata parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.SamplerMetadata parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.SamplerMetadata parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.SamplerMetadata parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.SamplerMetadata parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.SamplerMetadata parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.SamplerMetadata.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkSamplerProtos.SamplerMetadata.Builder newBuilder(SparkSamplerProtos.SamplerMetadata prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.HashMap.hash(HashMap.java:338)
            //   at java.base/java.util.HashMap.getNode(HashMap.java:568)
            //   at java.base/java.util.HashMap.containsKey(HashMap.java:594)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.VarType.remap(VarType.java:428)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.remap(GenericType.java:350)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:693)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkSamplerProtos.SamplerMetadata();
                case NEW_BUILDER:
                    return new SparkSamplerProtos.SamplerMetadata.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "creator_", "startTime_", "interval_", "threadDumper_", "dataAggregator_", "comment_", "platformMetadata_", "platformStatistics_", "systemStatistics_", "serverConfigurations_", SparkSamplerProtos.SamplerMetadata.ServerConfigurationsDefaultEntryHolder.defaultEntry, "endTime_", "numberOfTicks_", "sources_", SparkSamplerProtos.SamplerMetadata.SourcesDefaultEntryHolder.defaultEntry, "extraPlatformMetadata_", SparkSamplerProtos.SamplerMetadata.ExtraPlatformMetadataDefaultEntryHolder.defaultEntry, "samplerMode_" };
                    String info = "\u0000\u000f\u0000\u0000\u0001\u000f\u000f\u0003\u0000\u0000\u0001\t\u0002\u0002\u0003\u0004\u0004\t\u0005\t\u0006Ȉ\u0007\t\b\t\t\t\n2\u000b\u0002\f\u0004\r2\u000e2\u000f\f";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkSamplerProtos.SamplerMetadata> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkSamplerProtos.SamplerMetadata.class) {
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

        public static SparkSamplerProtos.SamplerMetadata getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkSamplerProtos.SamplerMetadata> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkSamplerProtos.SamplerMetadata defaultInstance = new SparkSamplerProtos.SamplerMetadata();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkSamplerProtos.SamplerMetadata.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkSamplerProtos.SamplerMetadata, SparkSamplerProtos.SamplerMetadata.Builder> implements SparkSamplerProtos.SamplerMetadataOrBuilder {

            private Builder() {
                super(SparkSamplerProtos.SamplerMetadata.DEFAULT_INSTANCE);
            }

            @Override
            public boolean hasCreator() {
                return this.instance.hasCreator();
            }

            @Override
            public SparkProtos.CommandSenderMetadata getCreator() {
                return this.instance.getCreator();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setCreator(SparkProtos.CommandSenderMetadata value) {
                this.copyOnWrite();
                this.instance.setCreator(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setCreator(SparkProtos.CommandSenderMetadata.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setCreator(builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder mergeCreator(SparkProtos.CommandSenderMetadata value) {
                this.copyOnWrite();
                this.instance.mergeCreator(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearCreator() {
                this.copyOnWrite();
                this.instance.clearCreator();
                return this;
            }

            @Override
            public long getStartTime() {
                return this.instance.getStartTime();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setStartTime(long value) {
                this.copyOnWrite();
                this.instance.setStartTime(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearStartTime() {
                this.copyOnWrite();
                this.instance.clearStartTime();
                return this;
            }

            @Override
            public int getInterval() {
                return this.instance.getInterval();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setInterval(int value) {
                this.copyOnWrite();
                this.instance.setInterval(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearInterval() {
                this.copyOnWrite();
                this.instance.clearInterval();
                return this;
            }

            @Override
            public boolean hasThreadDumper() {
                return this.instance.hasThreadDumper();
            }

            @Override
            public SparkSamplerProtos.SamplerMetadata.ThreadDumper getThreadDumper() {
                return this.instance.getThreadDumper();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setThreadDumper(SparkSamplerProtos.SamplerMetadata.ThreadDumper value) {
                this.copyOnWrite();
                this.instance.setThreadDumper(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setThreadDumper(SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setThreadDumper(builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder mergeThreadDumper(SparkSamplerProtos.SamplerMetadata.ThreadDumper value) {
                this.copyOnWrite();
                this.instance.mergeThreadDumper(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearThreadDumper() {
                this.copyOnWrite();
                this.instance.clearThreadDumper();
                return this;
            }

            @Override
            public boolean hasDataAggregator() {
                return this.instance.hasDataAggregator();
            }

            @Override
            public SparkSamplerProtos.SamplerMetadata.DataAggregator getDataAggregator() {
                return this.instance.getDataAggregator();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setDataAggregator(SparkSamplerProtos.SamplerMetadata.DataAggregator value) {
                this.copyOnWrite();
                this.instance.setDataAggregator(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setDataAggregator(SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setDataAggregator(builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder mergeDataAggregator(SparkSamplerProtos.SamplerMetadata.DataAggregator value) {
                this.copyOnWrite();
                this.instance.mergeDataAggregator(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearDataAggregator() {
                this.copyOnWrite();
                this.instance.clearDataAggregator();
                return this;
            }

            @Override
            public String getComment() {
                return this.instance.getComment();
            }

            @Override
            public ByteString getCommentBytes() {
                return this.instance.getCommentBytes();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setComment(String value) {
                this.copyOnWrite();
                this.instance.setComment(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearComment() {
                this.copyOnWrite();
                this.instance.clearComment();
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setCommentBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setCommentBytes(value);
                return this;
            }

            @Override
            public boolean hasPlatformMetadata() {
                return this.instance.hasPlatformMetadata();
            }

            @Override
            public SparkProtos.PlatformMetadata getPlatformMetadata() {
                return this.instance.getPlatformMetadata();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setPlatformMetadata(SparkProtos.PlatformMetadata value) {
                this.copyOnWrite();
                this.instance.setPlatformMetadata(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setPlatformMetadata(SparkProtos.PlatformMetadata.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setPlatformMetadata(builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder mergePlatformMetadata(SparkProtos.PlatformMetadata value) {
                this.copyOnWrite();
                this.instance.mergePlatformMetadata(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearPlatformMetadata() {
                this.copyOnWrite();
                this.instance.clearPlatformMetadata();
                return this;
            }

            @Override
            public boolean hasPlatformStatistics() {
                return this.instance.hasPlatformStatistics();
            }

            @Override
            public SparkProtos.PlatformStatistics getPlatformStatistics() {
                return this.instance.getPlatformStatistics();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setPlatformStatistics(SparkProtos.PlatformStatistics value) {
                this.copyOnWrite();
                this.instance.setPlatformStatistics(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setPlatformStatistics(SparkProtos.PlatformStatistics.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setPlatformStatistics(builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder mergePlatformStatistics(SparkProtos.PlatformStatistics value) {
                this.copyOnWrite();
                this.instance.mergePlatformStatistics(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearPlatformStatistics() {
                this.copyOnWrite();
                this.instance.clearPlatformStatistics();
                return this;
            }

            @Override
            public boolean hasSystemStatistics() {
                return this.instance.hasSystemStatistics();
            }

            @Override
            public SparkProtos.SystemStatistics getSystemStatistics() {
                return this.instance.getSystemStatistics();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setSystemStatistics(SparkProtos.SystemStatistics value) {
                this.copyOnWrite();
                this.instance.setSystemStatistics(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setSystemStatistics(SparkProtos.SystemStatistics.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setSystemStatistics(builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder mergeSystemStatistics(SparkProtos.SystemStatistics value) {
                this.copyOnWrite();
                this.instance.mergeSystemStatistics(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearSystemStatistics() {
                this.copyOnWrite();
                this.instance.clearSystemStatistics();
                return this;
            }

            @Override
            public int getServerConfigurationsCount() {
                return this.instance.getServerConfigurationsMap().size();
            }

            @Override
            public boolean containsServerConfigurations(String key) {
                Class<?> keyClass = key.getClass();
                return this.instance.getServerConfigurationsMap().containsKey(key);
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearServerConfigurations() {
                this.copyOnWrite();
                this.instance.getMutableServerConfigurationsMap().clear();
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder removeServerConfigurations(String key) {
                Class<?> keyClass = key.getClass();
                this.copyOnWrite();
                this.instance.getMutableServerConfigurationsMap().remove(key);
                return this;
            }

            @Deprecated
            @Override
            public Map<String, String> getServerConfigurations() {
                return this.getServerConfigurationsMap();
            }

            @Override
            public Map<String, String> getServerConfigurationsMap() {
                return Collections.unmodifiableMap(this.instance.getServerConfigurationsMap());
            }

            @Override
            public String getServerConfigurationsOrDefault(String key, String defaultValue) {
                Class<?> keyClass = key.getClass();
                Map<String, String> map = this.instance.getServerConfigurationsMap();
                return map.containsKey(key) ? (String) map.get(key) : defaultValue;
            }

            @Override
            public String getServerConfigurationsOrThrow(String key) {
                Class<?> keyClass = key.getClass();
                Map<String, String> map = this.instance.getServerConfigurationsMap();
                if (!map.containsKey(key)) {
                    throw new IllegalArgumentException();
                } else {
                    return (String) map.get(key);
                }
            }

            public SparkSamplerProtos.SamplerMetadata.Builder putServerConfigurations(String key, String value) {
                Class<?> keyClass = key.getClass();
                Class<?> valueClass = value.getClass();
                this.copyOnWrite();
                this.instance.getMutableServerConfigurationsMap().put(key, value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder putAllServerConfigurations(Map<String, String> values) {
                this.copyOnWrite();
                this.instance.getMutableServerConfigurationsMap().putAll(values);
                return this;
            }

            @Override
            public long getEndTime() {
                return this.instance.getEndTime();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setEndTime(long value) {
                this.copyOnWrite();
                this.instance.setEndTime(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearEndTime() {
                this.copyOnWrite();
                this.instance.clearEndTime();
                return this;
            }

            @Override
            public int getNumberOfTicks() {
                return this.instance.getNumberOfTicks();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setNumberOfTicks(int value) {
                this.copyOnWrite();
                this.instance.setNumberOfTicks(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearNumberOfTicks() {
                this.copyOnWrite();
                this.instance.clearNumberOfTicks();
                return this;
            }

            @Override
            public int getSourcesCount() {
                return this.instance.getSourcesMap().size();
            }

            @Override
            public boolean containsSources(String key) {
                Class<?> keyClass = key.getClass();
                return this.instance.getSourcesMap().containsKey(key);
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearSources() {
                this.copyOnWrite();
                this.instance.getMutableSourcesMap().clear();
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder removeSources(String key) {
                Class<?> keyClass = key.getClass();
                this.copyOnWrite();
                this.instance.getMutableSourcesMap().remove(key);
                return this;
            }

            @Deprecated
            @Override
            public Map<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> getSources() {
                return this.getSourcesMap();
            }

            @Override
            public Map<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> getSourcesMap() {
                return Collections.unmodifiableMap(this.instance.getSourcesMap());
            }

            @Override
            public SparkSamplerProtos.SamplerMetadata.SourceMetadata getSourcesOrDefault(String key, SparkSamplerProtos.SamplerMetadata.SourceMetadata defaultValue) {
                Class<?> keyClass = key.getClass();
                Map<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> map = this.instance.getSourcesMap();
                return map.containsKey(key) ? (SparkSamplerProtos.SamplerMetadata.SourceMetadata) map.get(key) : defaultValue;
            }

            @Override
            public SparkSamplerProtos.SamplerMetadata.SourceMetadata getSourcesOrThrow(String key) {
                Class<?> keyClass = key.getClass();
                Map<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> map = this.instance.getSourcesMap();
                if (!map.containsKey(key)) {
                    throw new IllegalArgumentException();
                } else {
                    return (SparkSamplerProtos.SamplerMetadata.SourceMetadata) map.get(key);
                }
            }

            public SparkSamplerProtos.SamplerMetadata.Builder putSources(String key, SparkSamplerProtos.SamplerMetadata.SourceMetadata value) {
                Class<?> keyClass = key.getClass();
                Class<?> valueClass = value.getClass();
                this.copyOnWrite();
                this.instance.getMutableSourcesMap().put(key, value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder putAllSources(Map<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> values) {
                this.copyOnWrite();
                this.instance.getMutableSourcesMap().putAll(values);
                return this;
            }

            @Override
            public int getExtraPlatformMetadataCount() {
                return this.instance.getExtraPlatformMetadataMap().size();
            }

            @Override
            public boolean containsExtraPlatformMetadata(String key) {
                Class<?> keyClass = key.getClass();
                return this.instance.getExtraPlatformMetadataMap().containsKey(key);
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearExtraPlatformMetadata() {
                this.copyOnWrite();
                this.instance.getMutableExtraPlatformMetadataMap().clear();
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder removeExtraPlatformMetadata(String key) {
                Class<?> keyClass = key.getClass();
                this.copyOnWrite();
                this.instance.getMutableExtraPlatformMetadataMap().remove(key);
                return this;
            }

            @Deprecated
            @Override
            public Map<String, String> getExtraPlatformMetadata() {
                return this.getExtraPlatformMetadataMap();
            }

            @Override
            public Map<String, String> getExtraPlatformMetadataMap() {
                return Collections.unmodifiableMap(this.instance.getExtraPlatformMetadataMap());
            }

            @Override
            public String getExtraPlatformMetadataOrDefault(String key, String defaultValue) {
                Class<?> keyClass = key.getClass();
                Map<String, String> map = this.instance.getExtraPlatformMetadataMap();
                return map.containsKey(key) ? (String) map.get(key) : defaultValue;
            }

            @Override
            public String getExtraPlatformMetadataOrThrow(String key) {
                Class<?> keyClass = key.getClass();
                Map<String, String> map = this.instance.getExtraPlatformMetadataMap();
                if (!map.containsKey(key)) {
                    throw new IllegalArgumentException();
                } else {
                    return (String) map.get(key);
                }
            }

            public SparkSamplerProtos.SamplerMetadata.Builder putExtraPlatformMetadata(String key, String value) {
                Class<?> keyClass = key.getClass();
                Class<?> valueClass = value.getClass();
                this.copyOnWrite();
                this.instance.getMutableExtraPlatformMetadataMap().put(key, value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder putAllExtraPlatformMetadata(Map<String, String> values) {
                this.copyOnWrite();
                this.instance.getMutableExtraPlatformMetadataMap().putAll(values);
                return this;
            }

            @Override
            public int getSamplerModeValue() {
                return this.instance.getSamplerModeValue();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setSamplerModeValue(int value) {
                this.copyOnWrite();
                this.instance.setSamplerModeValue(value);
                return this;
            }

            @Override
            public SparkSamplerProtos.SamplerMetadata.SamplerMode getSamplerMode() {
                return this.instance.getSamplerMode();
            }

            public SparkSamplerProtos.SamplerMetadata.Builder setSamplerMode(SparkSamplerProtos.SamplerMetadata.SamplerMode value) {
                this.copyOnWrite();
                this.instance.setSamplerMode(value);
                return this;
            }

            public SparkSamplerProtos.SamplerMetadata.Builder clearSamplerMode() {
                this.copyOnWrite();
                this.instance.clearSamplerMode();
                return this;
            }
        }

        public static final class DataAggregator extends GeneratedMessageLite<SparkSamplerProtos.SamplerMetadata.DataAggregator, SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder> implements SparkSamplerProtos.SamplerMetadata.DataAggregatorOrBuilder {

            public static final int TYPE_FIELD_NUMBER = 1;

            private int type_;

            public static final int THREAD_GROUPER_FIELD_NUMBER = 2;

            private int threadGrouper_;

            public static final int TICK_LENGTH_THRESHOLD_FIELD_NUMBER = 3;

            private long tickLengthThreshold_;

            public static final int NUMBER_OF_INCLUDED_TICKS_FIELD_NUMBER = 4;

            private int numberOfIncludedTicks_;

            private static final SparkSamplerProtos.SamplerMetadata.DataAggregator DEFAULT_INSTANCE;

            private static volatile Parser<SparkSamplerProtos.SamplerMetadata.DataAggregator> PARSER;

            private DataAggregator() {
            }

            @Override
            public int getTypeValue() {
                return this.type_;
            }

            @Override
            public SparkSamplerProtos.SamplerMetadata.DataAggregator.Type getType() {
                SparkSamplerProtos.SamplerMetadata.DataAggregator.Type result = SparkSamplerProtos.SamplerMetadata.DataAggregator.Type.forNumber(this.type_);
                return result == null ? SparkSamplerProtos.SamplerMetadata.DataAggregator.Type.UNRECOGNIZED : result;
            }

            private void setTypeValue(int value) {
                this.type_ = value;
            }

            private void setType(SparkSamplerProtos.SamplerMetadata.DataAggregator.Type value) {
                this.type_ = value.getNumber();
            }

            private void clearType() {
                this.type_ = 0;
            }

            @Override
            public int getThreadGrouperValue() {
                return this.threadGrouper_;
            }

            @Override
            public SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper getThreadGrouper() {
                SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper result = SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper.forNumber(this.threadGrouper_);
                return result == null ? SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper.UNRECOGNIZED : result;
            }

            private void setThreadGrouperValue(int value) {
                this.threadGrouper_ = value;
            }

            private void setThreadGrouper(SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper value) {
                this.threadGrouper_ = value.getNumber();
            }

            private void clearThreadGrouper() {
                this.threadGrouper_ = 0;
            }

            @Override
            public long getTickLengthThreshold() {
                return this.tickLengthThreshold_;
            }

            private void setTickLengthThreshold(long value) {
                this.tickLengthThreshold_ = value;
            }

            private void clearTickLengthThreshold() {
                this.tickLengthThreshold_ = 0L;
            }

            @Override
            public int getNumberOfIncludedTicks() {
                return this.numberOfIncludedTicks_;
            }

            private void setNumberOfIncludedTicks(int value) {
                this.numberOfIncludedTicks_ = value;
            }

            private void clearNumberOfIncludedTicks() {
                this.numberOfIncludedTicks_ = 0;
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder newBuilder(SparkSamplerProtos.SamplerMetadata.DataAggregator prototype) {
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
                // 0: getstatic me/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$DataAggregator.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$DataAggregator;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$DataAggregator.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$DataAggregator$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkSamplerProtos.SamplerMetadata.DataAggregator();
                    case NEW_BUILDER:
                        return new SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "type_", "threadGrouper_", "tickLengthThreshold_", "numberOfIncludedTicks_" };
                        String info = "\u0000\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0000\u0000\u0001\f\u0002\f\u0003\u0002\u0004\u0004";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkSamplerProtos.SamplerMetadata.DataAggregator> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkSamplerProtos.SamplerMetadata.DataAggregator.class) {
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

            public static SparkSamplerProtos.SamplerMetadata.DataAggregator getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkSamplerProtos.SamplerMetadata.DataAggregator> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkSamplerProtos.SamplerMetadata.DataAggregator defaultInstance = new SparkSamplerProtos.SamplerMetadata.DataAggregator();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkSamplerProtos.SamplerMetadata.DataAggregator.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkSamplerProtos.SamplerMetadata.DataAggregator, SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder> implements SparkSamplerProtos.SamplerMetadata.DataAggregatorOrBuilder {

                private Builder() {
                    super(SparkSamplerProtos.SamplerMetadata.DataAggregator.DEFAULT_INSTANCE);
                }

                @Override
                public int getTypeValue() {
                    return this.instance.getTypeValue();
                }

                public SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder setTypeValue(int value) {
                    this.copyOnWrite();
                    this.instance.setTypeValue(value);
                    return this;
                }

                @Override
                public SparkSamplerProtos.SamplerMetadata.DataAggregator.Type getType() {
                    return this.instance.getType();
                }

                public SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder setType(SparkSamplerProtos.SamplerMetadata.DataAggregator.Type value) {
                    this.copyOnWrite();
                    this.instance.setType(value);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder clearType() {
                    this.copyOnWrite();
                    this.instance.clearType();
                    return this;
                }

                @Override
                public int getThreadGrouperValue() {
                    return this.instance.getThreadGrouperValue();
                }

                public SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder setThreadGrouperValue(int value) {
                    this.copyOnWrite();
                    this.instance.setThreadGrouperValue(value);
                    return this;
                }

                @Override
                public SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper getThreadGrouper() {
                    return this.instance.getThreadGrouper();
                }

                public SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder setThreadGrouper(SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper value) {
                    this.copyOnWrite();
                    this.instance.setThreadGrouper(value);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder clearThreadGrouper() {
                    this.copyOnWrite();
                    this.instance.clearThreadGrouper();
                    return this;
                }

                @Override
                public long getTickLengthThreshold() {
                    return this.instance.getTickLengthThreshold();
                }

                public SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder setTickLengthThreshold(long value) {
                    this.copyOnWrite();
                    this.instance.setTickLengthThreshold(value);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder clearTickLengthThreshold() {
                    this.copyOnWrite();
                    this.instance.clearTickLengthThreshold();
                    return this;
                }

                @Override
                public int getNumberOfIncludedTicks() {
                    return this.instance.getNumberOfIncludedTicks();
                }

                public SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder setNumberOfIncludedTicks(int value) {
                    this.copyOnWrite();
                    this.instance.setNumberOfIncludedTicks(value);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.DataAggregator.Builder clearNumberOfIncludedTicks() {
                    this.copyOnWrite();
                    this.instance.clearNumberOfIncludedTicks();
                    return this;
                }
            }

            public static enum ThreadGrouper implements Internal.EnumLite {

                BY_NAME(0), BY_POOL(1), AS_ONE(2), UNRECOGNIZED(-1);

                public static final int BY_NAME_VALUE = 0;

                public static final int BY_POOL_VALUE = 1;

                public static final int AS_ONE_VALUE = 2;

                private static final Internal.EnumLiteMap<SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper> internalValueMap = new Internal.EnumLiteMap<SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper>() {

                    public SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper findValueByNumber(int number) {
                        return SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper.forNumber(number);
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
                public static SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper valueOf(int value) {
                    return forNumber(value);
                }

                public static SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper forNumber(int value) {
                    switch(value) {
                        case 0:
                            return BY_NAME;
                        case 1:
                            return BY_POOL;
                        case 2:
                            return AS_ONE;
                        default:
                            return null;
                    }
                }

                public static Internal.EnumLiteMap<SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper> internalGetValueMap() {
                    return internalValueMap;
                }

                public static Internal.EnumVerifier internalGetVerifier() {
                    return SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper.ThreadGrouperVerifier.INSTANCE;
                }

                private ThreadGrouper(int value) {
                    this.value = value;
                }

                private static final class ThreadGrouperVerifier implements Internal.EnumVerifier {

                    static final Internal.EnumVerifier INSTANCE = new SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper.ThreadGrouperVerifier();

                    @Override
                    public boolean isInRange(int number) {
                        return SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper.forNumber(number) != null;
                    }
                }
            }

            public static enum Type implements Internal.EnumLite {

                SIMPLE(0), TICKED(1), UNRECOGNIZED(-1);

                public static final int SIMPLE_VALUE = 0;

                public static final int TICKED_VALUE = 1;

                private static final Internal.EnumLiteMap<SparkSamplerProtos.SamplerMetadata.DataAggregator.Type> internalValueMap = new Internal.EnumLiteMap<SparkSamplerProtos.SamplerMetadata.DataAggregator.Type>() {

                    public SparkSamplerProtos.SamplerMetadata.DataAggregator.Type findValueByNumber(int number) {
                        return SparkSamplerProtos.SamplerMetadata.DataAggregator.Type.forNumber(number);
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
                public static SparkSamplerProtos.SamplerMetadata.DataAggregator.Type valueOf(int value) {
                    return forNumber(value);
                }

                public static SparkSamplerProtos.SamplerMetadata.DataAggregator.Type forNumber(int value) {
                    switch(value) {
                        case 0:
                            return SIMPLE;
                        case 1:
                            return TICKED;
                        default:
                            return null;
                    }
                }

                public static Internal.EnumLiteMap<SparkSamplerProtos.SamplerMetadata.DataAggregator.Type> internalGetValueMap() {
                    return internalValueMap;
                }

                public static Internal.EnumVerifier internalGetVerifier() {
                    return SparkSamplerProtos.SamplerMetadata.DataAggregator.Type.TypeVerifier.INSTANCE;
                }

                private Type(int value) {
                    this.value = value;
                }

                private static final class TypeVerifier implements Internal.EnumVerifier {

                    static final Internal.EnumVerifier INSTANCE = new SparkSamplerProtos.SamplerMetadata.DataAggregator.Type.TypeVerifier();

                    @Override
                    public boolean isInRange(int number) {
                        return SparkSamplerProtos.SamplerMetadata.DataAggregator.Type.forNumber(number) != null;
                    }
                }
            }
        }

        public interface DataAggregatorOrBuilder extends MessageLiteOrBuilder {

            int getTypeValue();

            SparkSamplerProtos.SamplerMetadata.DataAggregator.Type getType();

            int getThreadGrouperValue();

            SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper getThreadGrouper();

            long getTickLengthThreshold();

            int getNumberOfIncludedTicks();
        }

        private static final class ExtraPlatformMetadataDefaultEntryHolder {

            static final MapEntryLite<String, String> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.STRING, "", WireFormat.FieldType.STRING, "");
        }

        public static enum SamplerMode implements Internal.EnumLite {

            EXECUTION(0), ALLOCATION(1), UNRECOGNIZED(-1);

            public static final int EXECUTION_VALUE = 0;

            public static final int ALLOCATION_VALUE = 1;

            private static final Internal.EnumLiteMap<SparkSamplerProtos.SamplerMetadata.SamplerMode> internalValueMap = new Internal.EnumLiteMap<SparkSamplerProtos.SamplerMetadata.SamplerMode>() {

                public SparkSamplerProtos.SamplerMetadata.SamplerMode findValueByNumber(int number) {
                    return SparkSamplerProtos.SamplerMetadata.SamplerMode.forNumber(number);
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
            public static SparkSamplerProtos.SamplerMetadata.SamplerMode valueOf(int value) {
                return forNumber(value);
            }

            public static SparkSamplerProtos.SamplerMetadata.SamplerMode forNumber(int value) {
                switch(value) {
                    case 0:
                        return EXECUTION;
                    case 1:
                        return ALLOCATION;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<SparkSamplerProtos.SamplerMetadata.SamplerMode> internalGetValueMap() {
                return internalValueMap;
            }

            public static Internal.EnumVerifier internalGetVerifier() {
                return SparkSamplerProtos.SamplerMetadata.SamplerMode.SamplerModeVerifier.INSTANCE;
            }

            private SamplerMode(int value) {
                this.value = value;
            }

            private static final class SamplerModeVerifier implements Internal.EnumVerifier {

                static final Internal.EnumVerifier INSTANCE = new SparkSamplerProtos.SamplerMetadata.SamplerMode.SamplerModeVerifier();

                @Override
                public boolean isInRange(int number) {
                    return SparkSamplerProtos.SamplerMetadata.SamplerMode.forNumber(number) != null;
                }
            }
        }

        private static final class ServerConfigurationsDefaultEntryHolder {

            static final MapEntryLite<String, String> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.STRING, "", WireFormat.FieldType.STRING, "");
        }

        public static final class SourceMetadata extends GeneratedMessageLite<SparkSamplerProtos.SamplerMetadata.SourceMetadata, SparkSamplerProtos.SamplerMetadata.SourceMetadata.Builder> implements SparkSamplerProtos.SamplerMetadata.SourceMetadataOrBuilder {

            public static final int NAME_FIELD_NUMBER = 1;

            private String name_ = "";

            public static final int VERSION_FIELD_NUMBER = 2;

            private String version_ = "";

            private static final SparkSamplerProtos.SamplerMetadata.SourceMetadata DEFAULT_INSTANCE;

            private static volatile Parser<SparkSamplerProtos.SamplerMetadata.SourceMetadata> PARSER;

            private SourceMetadata() {
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

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata.Builder newBuilder(SparkSamplerProtos.SamplerMetadata.SourceMetadata prototype) {
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
                // 0: getstatic me/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$SourceMetadata.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$SourceMetadata;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$SourceMetadata.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$SourceMetadata$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkSamplerProtos.SamplerMetadata.SourceMetadata();
                    case NEW_BUILDER:
                        return new SparkSamplerProtos.SamplerMetadata.SourceMetadata.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "name_", "version_" };
                        String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001Ȉ\u0002Ȉ";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkSamplerProtos.SamplerMetadata.SourceMetadata> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkSamplerProtos.SamplerMetadata.SourceMetadata.class) {
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

            public static SparkSamplerProtos.SamplerMetadata.SourceMetadata getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkSamplerProtos.SamplerMetadata.SourceMetadata> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkSamplerProtos.SamplerMetadata.SourceMetadata defaultInstance = new SparkSamplerProtos.SamplerMetadata.SourceMetadata();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkSamplerProtos.SamplerMetadata.SourceMetadata.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkSamplerProtos.SamplerMetadata.SourceMetadata, SparkSamplerProtos.SamplerMetadata.SourceMetadata.Builder> implements SparkSamplerProtos.SamplerMetadata.SourceMetadataOrBuilder {

                private Builder() {
                    super(SparkSamplerProtos.SamplerMetadata.SourceMetadata.DEFAULT_INSTANCE);
                }

                @Override
                public String getName() {
                    return this.instance.getName();
                }

                @Override
                public ByteString getNameBytes() {
                    return this.instance.getNameBytes();
                }

                public SparkSamplerProtos.SamplerMetadata.SourceMetadata.Builder setName(String value) {
                    this.copyOnWrite();
                    this.instance.setName(value);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.SourceMetadata.Builder clearName() {
                    this.copyOnWrite();
                    this.instance.clearName();
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.SourceMetadata.Builder setNameBytes(ByteString value) {
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

                public SparkSamplerProtos.SamplerMetadata.SourceMetadata.Builder setVersion(String value) {
                    this.copyOnWrite();
                    this.instance.setVersion(value);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.SourceMetadata.Builder clearVersion() {
                    this.copyOnWrite();
                    this.instance.clearVersion();
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.SourceMetadata.Builder setVersionBytes(ByteString value) {
                    this.copyOnWrite();
                    this.instance.setVersionBytes(value);
                    return this;
                }
            }
        }

        public interface SourceMetadataOrBuilder extends MessageLiteOrBuilder {

            String getName();

            ByteString getNameBytes();

            String getVersion();

            ByteString getVersionBytes();
        }

        private static final class SourcesDefaultEntryHolder {

            static final MapEntryLite<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> defaultEntry = MapEntryLite.newDefaultInstance(WireFormat.FieldType.STRING, "", WireFormat.FieldType.MESSAGE, SparkSamplerProtos.SamplerMetadata.SourceMetadata.getDefaultInstance());
        }

        public static final class ThreadDumper extends GeneratedMessageLite<SparkSamplerProtos.SamplerMetadata.ThreadDumper, SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder> implements SparkSamplerProtos.SamplerMetadata.ThreadDumperOrBuilder {

            public static final int TYPE_FIELD_NUMBER = 1;

            private int type_;

            public static final int IDS_FIELD_NUMBER = 2;

            private Internal.LongList ids_;

            private int idsMemoizedSerializedSize = -1;

            public static final int PATTERNS_FIELD_NUMBER = 3;

            private Internal.ProtobufList<String> patterns_;

            private static final SparkSamplerProtos.SamplerMetadata.ThreadDumper DEFAULT_INSTANCE;

            private static volatile Parser<SparkSamplerProtos.SamplerMetadata.ThreadDumper> PARSER;

            private ThreadDumper() {
                this.ids_ = emptyLongList();
                this.patterns_ = GeneratedMessageLite.emptyProtobufList();
            }

            @Override
            public int getTypeValue() {
                return this.type_;
            }

            @Override
            public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type getType() {
                SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type result = SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type.forNumber(this.type_);
                return result == null ? SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type.UNRECOGNIZED : result;
            }

            private void setTypeValue(int value) {
                this.type_ = value;
            }

            private void setType(SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type value) {
                this.type_ = value.getNumber();
            }

            private void clearType() {
                this.type_ = 0;
            }

            @Override
            public List<Long> getIdsList() {
                return this.ids_;
            }

            @Override
            public int getIdsCount() {
                return this.ids_.size();
            }

            @Override
            public long getIds(int index) {
                return this.ids_.getLong(index);
            }

            private void ensureIdsIsMutable() {
                Internal.LongList tmp = this.ids_;
                if (!tmp.isModifiable()) {
                    this.ids_ = GeneratedMessageLite.mutableCopy(tmp);
                }
            }

            private void setIds(int index, long value) {
                this.ensureIdsIsMutable();
                this.ids_.setLong(index, value);
            }

            private void addIds(long value) {
                this.ensureIdsIsMutable();
                this.ids_.addLong(value);
            }

            private void addAllIds(Iterable<? extends Long> values) {
                this.ensureIdsIsMutable();
                AbstractMessageLite.addAll(values, this.ids_);
            }

            private void clearIds() {
                this.ids_ = emptyLongList();
            }

            @Override
            public List<String> getPatternsList() {
                return this.patterns_;
            }

            @Override
            public int getPatternsCount() {
                return this.patterns_.size();
            }

            @Override
            public String getPatterns(int index) {
                return (String) this.patterns_.get(index);
            }

            @Override
            public ByteString getPatternsBytes(int index) {
                return ByteString.copyFromUtf8((String) this.patterns_.get(index));
            }

            private void ensurePatternsIsMutable() {
                Internal.ProtobufList<String> tmp = this.patterns_;
                if (!tmp.isModifiable()) {
                    this.patterns_ = GeneratedMessageLite.mutableCopy(tmp);
                }
            }

            private void setPatterns(int index, String value) {
                Class<?> valueClass = value.getClass();
                this.ensurePatternsIsMutable();
                this.patterns_.set(index, value);
            }

            private void addPatterns(String value) {
                Class<?> valueClass = value.getClass();
                this.ensurePatternsIsMutable();
                this.patterns_.add(value);
            }

            private void addAllPatterns(Iterable<String> values) {
                this.ensurePatternsIsMutable();
                AbstractMessageLite.addAll(values, this.patterns_);
            }

            private void clearPatterns() {
                this.patterns_ = GeneratedMessageLite.emptyProtobufList();
            }

            private void addPatternsBytes(ByteString value) {
                checkByteStringIsUtf8(value);
                this.ensurePatternsIsMutable();
                this.patterns_.add(value.toStringUtf8());
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper parseFrom(InputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper parseDelimitedFrom(InputStream input) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper parseFrom(CodedInputStream input) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder newBuilder(SparkSamplerProtos.SamplerMetadata.ThreadDumper prototype) {
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
                // 0: getstatic me/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$ThreadDumper.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$ThreadDumper;
                // 3: aload 0
                // 4: invokevirtual me/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$ThreadDumper.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
                // 7: checkcast me/lucko/spark/proto/SparkSamplerProtos$SamplerMetadata$ThreadDumper$Builder
                // a: areturn
            }

            @Override
            protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
                switch(method) {
                    case NEW_MUTABLE_INSTANCE:
                        return new SparkSamplerProtos.SamplerMetadata.ThreadDumper();
                    case NEW_BUILDER:
                        return new SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder();
                    case BUILD_MESSAGE_INFO:
                        Object[] objects = new Object[] { "type_", "ids_", "patterns_" };
                        String info = "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0002\u0000\u0001\f\u0002%\u0003Ț";
                        return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                    case GET_DEFAULT_INSTANCE:
                        return DEFAULT_INSTANCE;
                    case GET_PARSER:
                        Parser<SparkSamplerProtos.SamplerMetadata.ThreadDumper> parser = PARSER;
                        if (parser == null) {
                            synchronized (SparkSamplerProtos.SamplerMetadata.ThreadDumper.class) {
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

            public static SparkSamplerProtos.SamplerMetadata.ThreadDumper getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<SparkSamplerProtos.SamplerMetadata.ThreadDumper> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }

            static {
                SparkSamplerProtos.SamplerMetadata.ThreadDumper defaultInstance = new SparkSamplerProtos.SamplerMetadata.ThreadDumper();
                DEFAULT_INSTANCE = defaultInstance;
                GeneratedMessageLite.registerDefaultInstance(SparkSamplerProtos.SamplerMetadata.ThreadDumper.class, defaultInstance);
            }

            public static final class Builder extends GeneratedMessageLite.Builder<SparkSamplerProtos.SamplerMetadata.ThreadDumper, SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder> implements SparkSamplerProtos.SamplerMetadata.ThreadDumperOrBuilder {

                private Builder() {
                    super(SparkSamplerProtos.SamplerMetadata.ThreadDumper.DEFAULT_INSTANCE);
                }

                @Override
                public int getTypeValue() {
                    return this.instance.getTypeValue();
                }

                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder setTypeValue(int value) {
                    this.copyOnWrite();
                    this.instance.setTypeValue(value);
                    return this;
                }

                @Override
                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type getType() {
                    return this.instance.getType();
                }

                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder setType(SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type value) {
                    this.copyOnWrite();
                    this.instance.setType(value);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder clearType() {
                    this.copyOnWrite();
                    this.instance.clearType();
                    return this;
                }

                @Override
                public List<Long> getIdsList() {
                    return Collections.unmodifiableList(this.instance.getIdsList());
                }

                @Override
                public int getIdsCount() {
                    return this.instance.getIdsCount();
                }

                @Override
                public long getIds(int index) {
                    return this.instance.getIds(index);
                }

                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder setIds(int index, long value) {
                    this.copyOnWrite();
                    this.instance.setIds(index, value);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder addIds(long value) {
                    this.copyOnWrite();
                    this.instance.addIds(value);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder addAllIds(Iterable<? extends Long> values) {
                    this.copyOnWrite();
                    this.instance.addAllIds(values);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder clearIds() {
                    this.copyOnWrite();
                    this.instance.clearIds();
                    return this;
                }

                @Override
                public List<String> getPatternsList() {
                    return Collections.unmodifiableList(this.instance.getPatternsList());
                }

                @Override
                public int getPatternsCount() {
                    return this.instance.getPatternsCount();
                }

                @Override
                public String getPatterns(int index) {
                    return this.instance.getPatterns(index);
                }

                @Override
                public ByteString getPatternsBytes(int index) {
                    return this.instance.getPatternsBytes(index);
                }

                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder setPatterns(int index, String value) {
                    this.copyOnWrite();
                    this.instance.setPatterns(index, value);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder addPatterns(String value) {
                    this.copyOnWrite();
                    this.instance.addPatterns(value);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder addAllPatterns(Iterable<String> values) {
                    this.copyOnWrite();
                    this.instance.addAllPatterns(values);
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder clearPatterns() {
                    this.copyOnWrite();
                    this.instance.clearPatterns();
                    return this;
                }

                public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Builder addPatternsBytes(ByteString value) {
                    this.copyOnWrite();
                    this.instance.addPatternsBytes(value);
                    return this;
                }
            }

            public static enum Type implements Internal.EnumLite {

                ALL(0), SPECIFIC(1), REGEX(2), UNRECOGNIZED(-1);

                public static final int ALL_VALUE = 0;

                public static final int SPECIFIC_VALUE = 1;

                public static final int REGEX_VALUE = 2;

                private static final Internal.EnumLiteMap<SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type> internalValueMap = new Internal.EnumLiteMap<SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type>() {

                    public SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type findValueByNumber(int number) {
                        return SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type.forNumber(number);
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
                public static SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type valueOf(int value) {
                    return forNumber(value);
                }

                public static SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type forNumber(int value) {
                    switch(value) {
                        case 0:
                            return ALL;
                        case 1:
                            return SPECIFIC;
                        case 2:
                            return REGEX;
                        default:
                            return null;
                    }
                }

                public static Internal.EnumLiteMap<SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type> internalGetValueMap() {
                    return internalValueMap;
                }

                public static Internal.EnumVerifier internalGetVerifier() {
                    return SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type.TypeVerifier.INSTANCE;
                }

                private Type(int value) {
                    this.value = value;
                }

                private static final class TypeVerifier implements Internal.EnumVerifier {

                    static final Internal.EnumVerifier INSTANCE = new SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type.TypeVerifier();

                    @Override
                    public boolean isInRange(int number) {
                        return SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type.forNumber(number) != null;
                    }
                }
            }
        }

        public interface ThreadDumperOrBuilder extends MessageLiteOrBuilder {

            int getTypeValue();

            SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type getType();

            List<Long> getIdsList();

            int getIdsCount();

            long getIds(int var1);

            List<String> getPatternsList();

            int getPatternsCount();

            String getPatterns(int var1);

            ByteString getPatternsBytes(int var1);
        }
    }

    public interface SamplerMetadataOrBuilder extends MessageLiteOrBuilder {

        boolean hasCreator();

        SparkProtos.CommandSenderMetadata getCreator();

        long getStartTime();

        int getInterval();

        boolean hasThreadDumper();

        SparkSamplerProtos.SamplerMetadata.ThreadDumper getThreadDumper();

        boolean hasDataAggregator();

        SparkSamplerProtos.SamplerMetadata.DataAggregator getDataAggregator();

        String getComment();

        ByteString getCommentBytes();

        boolean hasPlatformMetadata();

        SparkProtos.PlatformMetadata getPlatformMetadata();

        boolean hasPlatformStatistics();

        SparkProtos.PlatformStatistics getPlatformStatistics();

        boolean hasSystemStatistics();

        SparkProtos.SystemStatistics getSystemStatistics();

        int getServerConfigurationsCount();

        boolean containsServerConfigurations(String var1);

        @Deprecated
        Map<String, String> getServerConfigurations();

        Map<String, String> getServerConfigurationsMap();

        String getServerConfigurationsOrDefault(String var1, String var2);

        String getServerConfigurationsOrThrow(String var1);

        long getEndTime();

        int getNumberOfTicks();

        int getSourcesCount();

        boolean containsSources(String var1);

        @Deprecated
        Map<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> getSources();

        Map<String, SparkSamplerProtos.SamplerMetadata.SourceMetadata> getSourcesMap();

        SparkSamplerProtos.SamplerMetadata.SourceMetadata getSourcesOrDefault(String var1, SparkSamplerProtos.SamplerMetadata.SourceMetadata var2);

        SparkSamplerProtos.SamplerMetadata.SourceMetadata getSourcesOrThrow(String var1);

        int getExtraPlatformMetadataCount();

        boolean containsExtraPlatformMetadata(String var1);

        @Deprecated
        Map<String, String> getExtraPlatformMetadata();

        Map<String, String> getExtraPlatformMetadataMap();

        String getExtraPlatformMetadataOrDefault(String var1, String var2);

        String getExtraPlatformMetadataOrThrow(String var1);

        int getSamplerModeValue();

        SparkSamplerProtos.SamplerMetadata.SamplerMode getSamplerMode();
    }

    public static final class SocketChannelInfo extends GeneratedMessageLite<SparkSamplerProtos.SocketChannelInfo, SparkSamplerProtos.SocketChannelInfo.Builder> implements SparkSamplerProtos.SocketChannelInfoOrBuilder {

        public static final int CHANNEL_ID_FIELD_NUMBER = 1;

        private String channelId_ = "";

        public static final int PUBLIC_KEY_FIELD_NUMBER = 2;

        private ByteString publicKey_ = ByteString.EMPTY;

        private static final SparkSamplerProtos.SocketChannelInfo DEFAULT_INSTANCE;

        private static volatile Parser<SparkSamplerProtos.SocketChannelInfo> PARSER;

        private SocketChannelInfo() {
        }

        @Override
        public String getChannelId() {
            return this.channelId_;
        }

        @Override
        public ByteString getChannelIdBytes() {
            return ByteString.copyFromUtf8(this.channelId_);
        }

        private void setChannelId(String value) {
            Class<?> valueClass = value.getClass();
            this.channelId_ = value;
        }

        private void clearChannelId() {
            this.channelId_ = getDefaultInstance().getChannelId();
        }

        private void setChannelIdBytes(ByteString value) {
            checkByteStringIsUtf8(value);
            this.channelId_ = value.toStringUtf8();
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

        public static SparkSamplerProtos.SocketChannelInfo parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.SocketChannelInfo parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.SocketChannelInfo parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.SocketChannelInfo parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.SocketChannelInfo parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.SocketChannelInfo parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.SocketChannelInfo parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.SocketChannelInfo parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.SocketChannelInfo parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.SocketChannelInfo parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.SocketChannelInfo parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.SocketChannelInfo parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.SocketChannelInfo.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkSamplerProtos.SocketChannelInfo.Builder newBuilder(SparkSamplerProtos.SocketChannelInfo prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.HashMap.hash(HashMap.java:338)
            //   at java.base/java.util.HashMap.getNode(HashMap.java:568)
            //   at java.base/java.util.HashMap.containsKey(HashMap.java:594)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.VarType.remap(VarType.java:428)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.remap(GenericType.java:350)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:693)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkSamplerProtos$SocketChannelInfo.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkSamplerProtos$SocketChannelInfo;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkSamplerProtos$SocketChannelInfo.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkSamplerProtos$SocketChannelInfo$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkSamplerProtos.SocketChannelInfo();
                case NEW_BUILDER:
                    return new SparkSamplerProtos.SocketChannelInfo.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "channelId_", "publicKey_" };
                    String info = "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001Ȉ\u0002\n";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkSamplerProtos.SocketChannelInfo> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkSamplerProtos.SocketChannelInfo.class) {
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

        public static SparkSamplerProtos.SocketChannelInfo getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkSamplerProtos.SocketChannelInfo> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkSamplerProtos.SocketChannelInfo defaultInstance = new SparkSamplerProtos.SocketChannelInfo();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkSamplerProtos.SocketChannelInfo.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkSamplerProtos.SocketChannelInfo, SparkSamplerProtos.SocketChannelInfo.Builder> implements SparkSamplerProtos.SocketChannelInfoOrBuilder {

            private Builder() {
                super(SparkSamplerProtos.SocketChannelInfo.DEFAULT_INSTANCE);
            }

            @Override
            public String getChannelId() {
                return this.instance.getChannelId();
            }

            @Override
            public ByteString getChannelIdBytes() {
                return this.instance.getChannelIdBytes();
            }

            public SparkSamplerProtos.SocketChannelInfo.Builder setChannelId(String value) {
                this.copyOnWrite();
                this.instance.setChannelId(value);
                return this;
            }

            public SparkSamplerProtos.SocketChannelInfo.Builder clearChannelId() {
                this.copyOnWrite();
                this.instance.clearChannelId();
                return this;
            }

            public SparkSamplerProtos.SocketChannelInfo.Builder setChannelIdBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setChannelIdBytes(value);
                return this;
            }

            @Override
            public ByteString getPublicKey() {
                return this.instance.getPublicKey();
            }

            public SparkSamplerProtos.SocketChannelInfo.Builder setPublicKey(ByteString value) {
                this.copyOnWrite();
                this.instance.setPublicKey(value);
                return this;
            }

            public SparkSamplerProtos.SocketChannelInfo.Builder clearPublicKey() {
                this.copyOnWrite();
                this.instance.clearPublicKey();
                return this;
            }
        }
    }

    public interface SocketChannelInfoOrBuilder extends MessageLiteOrBuilder {

        String getChannelId();

        ByteString getChannelIdBytes();

        ByteString getPublicKey();
    }

    public static final class StackTraceNode extends GeneratedMessageLite<SparkSamplerProtos.StackTraceNode, SparkSamplerProtos.StackTraceNode.Builder> implements SparkSamplerProtos.StackTraceNodeOrBuilder {

        public static final int CLASS_NAME_FIELD_NUMBER = 3;

        private String className_;

        public static final int METHOD_NAME_FIELD_NUMBER = 4;

        private String methodName_;

        public static final int PARENT_LINE_NUMBER_FIELD_NUMBER = 5;

        private int parentLineNumber_;

        public static final int LINE_NUMBER_FIELD_NUMBER = 6;

        private int lineNumber_;

        public static final int METHOD_DESC_FIELD_NUMBER = 7;

        private String methodDesc_;

        public static final int TIMES_FIELD_NUMBER = 8;

        private Internal.DoubleList times_;

        private int timesMemoizedSerializedSize = -1;

        public static final int CHILDREN_REFS_FIELD_NUMBER = 9;

        private Internal.IntList childrenRefs_;

        private int childrenRefsMemoizedSerializedSize = -1;

        private static final SparkSamplerProtos.StackTraceNode DEFAULT_INSTANCE;

        private static volatile Parser<SparkSamplerProtos.StackTraceNode> PARSER;

        private StackTraceNode() {
            this.className_ = "";
            this.methodName_ = "";
            this.methodDesc_ = "";
            this.times_ = emptyDoubleList();
            this.childrenRefs_ = emptyIntList();
        }

        @Override
        public String getClassName() {
            return this.className_;
        }

        @Override
        public ByteString getClassNameBytes() {
            return ByteString.copyFromUtf8(this.className_);
        }

        private void setClassName(String value) {
            Class<?> valueClass = value.getClass();
            this.className_ = value;
        }

        private void clearClassName() {
            this.className_ = getDefaultInstance().getClassName();
        }

        private void setClassNameBytes(ByteString value) {
            checkByteStringIsUtf8(value);
            this.className_ = value.toStringUtf8();
        }

        @Override
        public String getMethodName() {
            return this.methodName_;
        }

        @Override
        public ByteString getMethodNameBytes() {
            return ByteString.copyFromUtf8(this.methodName_);
        }

        private void setMethodName(String value) {
            Class<?> valueClass = value.getClass();
            this.methodName_ = value;
        }

        private void clearMethodName() {
            this.methodName_ = getDefaultInstance().getMethodName();
        }

        private void setMethodNameBytes(ByteString value) {
            checkByteStringIsUtf8(value);
            this.methodName_ = value.toStringUtf8();
        }

        @Override
        public int getParentLineNumber() {
            return this.parentLineNumber_;
        }

        private void setParentLineNumber(int value) {
            this.parentLineNumber_ = value;
        }

        private void clearParentLineNumber() {
            this.parentLineNumber_ = 0;
        }

        @Override
        public int getLineNumber() {
            return this.lineNumber_;
        }

        private void setLineNumber(int value) {
            this.lineNumber_ = value;
        }

        private void clearLineNumber() {
            this.lineNumber_ = 0;
        }

        @Override
        public String getMethodDesc() {
            return this.methodDesc_;
        }

        @Override
        public ByteString getMethodDescBytes() {
            return ByteString.copyFromUtf8(this.methodDesc_);
        }

        private void setMethodDesc(String value) {
            Class<?> valueClass = value.getClass();
            this.methodDesc_ = value;
        }

        private void clearMethodDesc() {
            this.methodDesc_ = getDefaultInstance().getMethodDesc();
        }

        private void setMethodDescBytes(ByteString value) {
            checkByteStringIsUtf8(value);
            this.methodDesc_ = value.toStringUtf8();
        }

        @Override
        public List<Double> getTimesList() {
            return this.times_;
        }

        @Override
        public int getTimesCount() {
            return this.times_.size();
        }

        @Override
        public double getTimes(int index) {
            return this.times_.getDouble(index);
        }

        private void ensureTimesIsMutable() {
            Internal.DoubleList tmp = this.times_;
            if (!tmp.isModifiable()) {
                this.times_ = GeneratedMessageLite.mutableCopy(tmp);
            }
        }

        private void setTimes(int index, double value) {
            this.ensureTimesIsMutable();
            this.times_.setDouble(index, value);
        }

        private void addTimes(double value) {
            this.ensureTimesIsMutable();
            this.times_.addDouble(value);
        }

        private void addAllTimes(Iterable<? extends Double> values) {
            this.ensureTimesIsMutable();
            AbstractMessageLite.addAll(values, this.times_);
        }

        private void clearTimes() {
            this.times_ = emptyDoubleList();
        }

        @Override
        public List<Integer> getChildrenRefsList() {
            return this.childrenRefs_;
        }

        @Override
        public int getChildrenRefsCount() {
            return this.childrenRefs_.size();
        }

        @Override
        public int getChildrenRefs(int index) {
            return this.childrenRefs_.getInt(index);
        }

        private void ensureChildrenRefsIsMutable() {
            Internal.IntList tmp = this.childrenRefs_;
            if (!tmp.isModifiable()) {
                this.childrenRefs_ = GeneratedMessageLite.mutableCopy(tmp);
            }
        }

        private void setChildrenRefs(int index, int value) {
            this.ensureChildrenRefsIsMutable();
            this.childrenRefs_.setInt(index, value);
        }

        private void addChildrenRefs(int value) {
            this.ensureChildrenRefsIsMutable();
            this.childrenRefs_.addInt(value);
        }

        private void addAllChildrenRefs(Iterable<? extends Integer> values) {
            this.ensureChildrenRefsIsMutable();
            AbstractMessageLite.addAll(values, this.childrenRefs_);
        }

        private void clearChildrenRefs() {
            this.childrenRefs_ = emptyIntList();
        }

        public static SparkSamplerProtos.StackTraceNode parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.StackTraceNode parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.StackTraceNode parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.StackTraceNode parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.StackTraceNode parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.StackTraceNode parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.StackTraceNode parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.StackTraceNode parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.StackTraceNode parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.StackTraceNode parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.StackTraceNode parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.StackTraceNode parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.StackTraceNode.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkSamplerProtos.StackTraceNode.Builder newBuilder(SparkSamplerProtos.StackTraceNode prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.HashMap.hash(HashMap.java:338)
            //   at java.base/java.util.HashMap.getNode(HashMap.java:568)
            //   at java.base/java.util.HashMap.containsKey(HashMap.java:594)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.VarType.remap(VarType.java:428)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.remap(GenericType.java:350)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:693)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkSamplerProtos$StackTraceNode.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkSamplerProtos$StackTraceNode;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkSamplerProtos$StackTraceNode.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkSamplerProtos$StackTraceNode$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkSamplerProtos.StackTraceNode();
                case NEW_BUILDER:
                    return new SparkSamplerProtos.StackTraceNode.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "className_", "methodName_", "parentLineNumber_", "lineNumber_", "methodDesc_", "times_", "childrenRefs_" };
                    String info = "\u0000\u0007\u0000\u0000\u0003\t\u0007\u0000\u0002\u0000\u0003Ȉ\u0004Ȉ\u0005\u0004\u0006\u0004\u0007Ȉ\b#\t'";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkSamplerProtos.StackTraceNode> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkSamplerProtos.StackTraceNode.class) {
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

        public static SparkSamplerProtos.StackTraceNode getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkSamplerProtos.StackTraceNode> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkSamplerProtos.StackTraceNode defaultInstance = new SparkSamplerProtos.StackTraceNode();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkSamplerProtos.StackTraceNode.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkSamplerProtos.StackTraceNode, SparkSamplerProtos.StackTraceNode.Builder> implements SparkSamplerProtos.StackTraceNodeOrBuilder {

            private Builder() {
                super(SparkSamplerProtos.StackTraceNode.DEFAULT_INSTANCE);
            }

            @Override
            public String getClassName() {
                return this.instance.getClassName();
            }

            @Override
            public ByteString getClassNameBytes() {
                return this.instance.getClassNameBytes();
            }

            public SparkSamplerProtos.StackTraceNode.Builder setClassName(String value) {
                this.copyOnWrite();
                this.instance.setClassName(value);
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder clearClassName() {
                this.copyOnWrite();
                this.instance.clearClassName();
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder setClassNameBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setClassNameBytes(value);
                return this;
            }

            @Override
            public String getMethodName() {
                return this.instance.getMethodName();
            }

            @Override
            public ByteString getMethodNameBytes() {
                return this.instance.getMethodNameBytes();
            }

            public SparkSamplerProtos.StackTraceNode.Builder setMethodName(String value) {
                this.copyOnWrite();
                this.instance.setMethodName(value);
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder clearMethodName() {
                this.copyOnWrite();
                this.instance.clearMethodName();
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder setMethodNameBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setMethodNameBytes(value);
                return this;
            }

            @Override
            public int getParentLineNumber() {
                return this.instance.getParentLineNumber();
            }

            public SparkSamplerProtos.StackTraceNode.Builder setParentLineNumber(int value) {
                this.copyOnWrite();
                this.instance.setParentLineNumber(value);
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder clearParentLineNumber() {
                this.copyOnWrite();
                this.instance.clearParentLineNumber();
                return this;
            }

            @Override
            public int getLineNumber() {
                return this.instance.getLineNumber();
            }

            public SparkSamplerProtos.StackTraceNode.Builder setLineNumber(int value) {
                this.copyOnWrite();
                this.instance.setLineNumber(value);
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder clearLineNumber() {
                this.copyOnWrite();
                this.instance.clearLineNumber();
                return this;
            }

            @Override
            public String getMethodDesc() {
                return this.instance.getMethodDesc();
            }

            @Override
            public ByteString getMethodDescBytes() {
                return this.instance.getMethodDescBytes();
            }

            public SparkSamplerProtos.StackTraceNode.Builder setMethodDesc(String value) {
                this.copyOnWrite();
                this.instance.setMethodDesc(value);
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder clearMethodDesc() {
                this.copyOnWrite();
                this.instance.clearMethodDesc();
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder setMethodDescBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setMethodDescBytes(value);
                return this;
            }

            @Override
            public List<Double> getTimesList() {
                return Collections.unmodifiableList(this.instance.getTimesList());
            }

            @Override
            public int getTimesCount() {
                return this.instance.getTimesCount();
            }

            @Override
            public double getTimes(int index) {
                return this.instance.getTimes(index);
            }

            public SparkSamplerProtos.StackTraceNode.Builder setTimes(int index, double value) {
                this.copyOnWrite();
                this.instance.setTimes(index, value);
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder addTimes(double value) {
                this.copyOnWrite();
                this.instance.addTimes(value);
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder addAllTimes(Iterable<? extends Double> values) {
                this.copyOnWrite();
                this.instance.addAllTimes(values);
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder clearTimes() {
                this.copyOnWrite();
                this.instance.clearTimes();
                return this;
            }

            @Override
            public List<Integer> getChildrenRefsList() {
                return Collections.unmodifiableList(this.instance.getChildrenRefsList());
            }

            @Override
            public int getChildrenRefsCount() {
                return this.instance.getChildrenRefsCount();
            }

            @Override
            public int getChildrenRefs(int index) {
                return this.instance.getChildrenRefs(index);
            }

            public SparkSamplerProtos.StackTraceNode.Builder setChildrenRefs(int index, int value) {
                this.copyOnWrite();
                this.instance.setChildrenRefs(index, value);
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder addChildrenRefs(int value) {
                this.copyOnWrite();
                this.instance.addChildrenRefs(value);
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder addAllChildrenRefs(Iterable<? extends Integer> values) {
                this.copyOnWrite();
                this.instance.addAllChildrenRefs(values);
                return this;
            }

            public SparkSamplerProtos.StackTraceNode.Builder clearChildrenRefs() {
                this.copyOnWrite();
                this.instance.clearChildrenRefs();
                return this;
            }
        }
    }

    public interface StackTraceNodeOrBuilder extends MessageLiteOrBuilder {

        String getClassName();

        ByteString getClassNameBytes();

        String getMethodName();

        ByteString getMethodNameBytes();

        int getParentLineNumber();

        int getLineNumber();

        String getMethodDesc();

        ByteString getMethodDescBytes();

        List<Double> getTimesList();

        int getTimesCount();

        double getTimes(int var1);

        List<Integer> getChildrenRefsList();

        int getChildrenRefsCount();

        int getChildrenRefs(int var1);
    }

    public static final class ThreadNode extends GeneratedMessageLite<SparkSamplerProtos.ThreadNode, SparkSamplerProtos.ThreadNode.Builder> implements SparkSamplerProtos.ThreadNodeOrBuilder {

        public static final int NAME_FIELD_NUMBER = 1;

        private String name_;

        public static final int CHILDREN_FIELD_NUMBER = 3;

        private Internal.ProtobufList<SparkSamplerProtos.StackTraceNode> children_;

        public static final int TIMES_FIELD_NUMBER = 4;

        private Internal.DoubleList times_;

        private int timesMemoizedSerializedSize = -1;

        public static final int CHILDREN_REFS_FIELD_NUMBER = 5;

        private Internal.IntList childrenRefs_;

        private int childrenRefsMemoizedSerializedSize = -1;

        private static final SparkSamplerProtos.ThreadNode DEFAULT_INSTANCE;

        private static volatile Parser<SparkSamplerProtos.ThreadNode> PARSER;

        private ThreadNode() {
            this.name_ = "";
            this.children_ = emptyProtobufList();
            this.times_ = emptyDoubleList();
            this.childrenRefs_ = emptyIntList();
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
        public List<SparkSamplerProtos.StackTraceNode> getChildrenList() {
            return this.children_;
        }

        public List<? extends SparkSamplerProtos.StackTraceNodeOrBuilder> getChildrenOrBuilderList() {
            return this.children_;
        }

        @Override
        public int getChildrenCount() {
            return this.children_.size();
        }

        @Override
        public SparkSamplerProtos.StackTraceNode getChildren(int index) {
            return (SparkSamplerProtos.StackTraceNode) this.children_.get(index);
        }

        public SparkSamplerProtos.StackTraceNodeOrBuilder getChildrenOrBuilder(int index) {
            return (SparkSamplerProtos.StackTraceNodeOrBuilder) this.children_.get(index);
        }

        private void ensureChildrenIsMutable() {
            Internal.ProtobufList<SparkSamplerProtos.StackTraceNode> tmp = this.children_;
            if (!tmp.isModifiable()) {
                this.children_ = GeneratedMessageLite.mutableCopy(tmp);
            }
        }

        private void setChildren(int index, SparkSamplerProtos.StackTraceNode value) {
            value.getClass();
            this.ensureChildrenIsMutable();
            this.children_.set(index, value);
        }

        private void addChildren(SparkSamplerProtos.StackTraceNode value) {
            value.getClass();
            this.ensureChildrenIsMutable();
            this.children_.add(value);
        }

        private void addChildren(int index, SparkSamplerProtos.StackTraceNode value) {
            value.getClass();
            this.ensureChildrenIsMutable();
            this.children_.add(index, value);
        }

        private void addAllChildren(Iterable<? extends SparkSamplerProtos.StackTraceNode> values) {
            this.ensureChildrenIsMutable();
            AbstractMessageLite.addAll(values, this.children_);
        }

        private void clearChildren() {
            this.children_ = emptyProtobufList();
        }

        private void removeChildren(int index) {
            this.ensureChildrenIsMutable();
            this.children_.remove(index);
        }

        @Override
        public List<Double> getTimesList() {
            return this.times_;
        }

        @Override
        public int getTimesCount() {
            return this.times_.size();
        }

        @Override
        public double getTimes(int index) {
            return this.times_.getDouble(index);
        }

        private void ensureTimesIsMutable() {
            Internal.DoubleList tmp = this.times_;
            if (!tmp.isModifiable()) {
                this.times_ = GeneratedMessageLite.mutableCopy(tmp);
            }
        }

        private void setTimes(int index, double value) {
            this.ensureTimesIsMutable();
            this.times_.setDouble(index, value);
        }

        private void addTimes(double value) {
            this.ensureTimesIsMutable();
            this.times_.addDouble(value);
        }

        private void addAllTimes(Iterable<? extends Double> values) {
            this.ensureTimesIsMutable();
            AbstractMessageLite.addAll(values, this.times_);
        }

        private void clearTimes() {
            this.times_ = emptyDoubleList();
        }

        @Override
        public List<Integer> getChildrenRefsList() {
            return this.childrenRefs_;
        }

        @Override
        public int getChildrenRefsCount() {
            return this.childrenRefs_.size();
        }

        @Override
        public int getChildrenRefs(int index) {
            return this.childrenRefs_.getInt(index);
        }

        private void ensureChildrenRefsIsMutable() {
            Internal.IntList tmp = this.childrenRefs_;
            if (!tmp.isModifiable()) {
                this.childrenRefs_ = GeneratedMessageLite.mutableCopy(tmp);
            }
        }

        private void setChildrenRefs(int index, int value) {
            this.ensureChildrenRefsIsMutable();
            this.childrenRefs_.setInt(index, value);
        }

        private void addChildrenRefs(int value) {
            this.ensureChildrenRefsIsMutable();
            this.childrenRefs_.addInt(value);
        }

        private void addAllChildrenRefs(Iterable<? extends Integer> values) {
            this.ensureChildrenRefsIsMutable();
            AbstractMessageLite.addAll(values, this.childrenRefs_);
        }

        private void clearChildrenRefs() {
            this.childrenRefs_ = emptyIntList();
        }

        public static SparkSamplerProtos.ThreadNode parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.ThreadNode parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.ThreadNode parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.ThreadNode parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.ThreadNode parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static SparkSamplerProtos.ThreadNode parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static SparkSamplerProtos.ThreadNode parseFrom(InputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.ThreadNode parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.ThreadNode parseDelimitedFrom(InputStream input) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.ThreadNode parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.ThreadNode parseFrom(CodedInputStream input) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static SparkSamplerProtos.ThreadNode parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static SparkSamplerProtos.ThreadNode.Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static SparkSamplerProtos.ThreadNode.Builder newBuilder(SparkSamplerProtos.ThreadNode prototype) {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.StackOverflowError
            //   at java.base/java.util.HashMap.hash(HashMap.java:338)
            //   at java.base/java.util.HashMap.getNode(HashMap.java:568)
            //   at java.base/java.util.HashMap.containsKey(HashMap.java:594)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.VarType.remap(VarType.java:428)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.remap(GenericType.java:350)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.gen.generics.GenericType.getGenericSuperType(GenericType.java:693)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1608)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
            //
            // Bytecode:
            // 0: getstatic me/lucko/spark/proto/SparkSamplerProtos$ThreadNode.DEFAULT_INSTANCE Lme/lucko/spark/proto/SparkSamplerProtos$ThreadNode;
            // 3: aload 0
            // 4: invokevirtual me/lucko/spark/proto/SparkSamplerProtos$ThreadNode.createBuilder (Lme/lucko/spark/lib/protobuf/GeneratedMessageLite;)Lme/lucko/spark/lib/protobuf/GeneratedMessageLite$Builder;
            // 7: checkcast me/lucko/spark/proto/SparkSamplerProtos$ThreadNode$Builder
            // a: areturn
        }

        @Override
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch(method) {
                case NEW_MUTABLE_INSTANCE:
                    return new SparkSamplerProtos.ThreadNode();
                case NEW_BUILDER:
                    return new SparkSamplerProtos.ThreadNode.Builder();
                case BUILD_MESSAGE_INFO:
                    Object[] objects = new Object[] { "name_", "children_", SparkSamplerProtos.StackTraceNode.class, "times_", "childrenRefs_" };
                    String info = "\u0000\u0004\u0000\u0000\u0001\u0005\u0004\u0000\u0003\u0000\u0001Ȉ\u0003\u001b\u0004#\u0005'";
                    return newMessageInfo(DEFAULT_INSTANCE, info, objects);
                case GET_DEFAULT_INSTANCE:
                    return DEFAULT_INSTANCE;
                case GET_PARSER:
                    Parser<SparkSamplerProtos.ThreadNode> parser = PARSER;
                    if (parser == null) {
                        synchronized (SparkSamplerProtos.ThreadNode.class) {
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

        public static SparkSamplerProtos.ThreadNode getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SparkSamplerProtos.ThreadNode> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        static {
            SparkSamplerProtos.ThreadNode defaultInstance = new SparkSamplerProtos.ThreadNode();
            DEFAULT_INSTANCE = defaultInstance;
            GeneratedMessageLite.registerDefaultInstance(SparkSamplerProtos.ThreadNode.class, defaultInstance);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SparkSamplerProtos.ThreadNode, SparkSamplerProtos.ThreadNode.Builder> implements SparkSamplerProtos.ThreadNodeOrBuilder {

            private Builder() {
                super(SparkSamplerProtos.ThreadNode.DEFAULT_INSTANCE);
            }

            @Override
            public String getName() {
                return this.instance.getName();
            }

            @Override
            public ByteString getNameBytes() {
                return this.instance.getNameBytes();
            }

            public SparkSamplerProtos.ThreadNode.Builder setName(String value) {
                this.copyOnWrite();
                this.instance.setName(value);
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder clearName() {
                this.copyOnWrite();
                this.instance.clearName();
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder setNameBytes(ByteString value) {
                this.copyOnWrite();
                this.instance.setNameBytes(value);
                return this;
            }

            @Override
            public List<SparkSamplerProtos.StackTraceNode> getChildrenList() {
                return Collections.unmodifiableList(this.instance.getChildrenList());
            }

            @Override
            public int getChildrenCount() {
                return this.instance.getChildrenCount();
            }

            @Override
            public SparkSamplerProtos.StackTraceNode getChildren(int index) {
                return this.instance.getChildren(index);
            }

            public SparkSamplerProtos.ThreadNode.Builder setChildren(int index, SparkSamplerProtos.StackTraceNode value) {
                this.copyOnWrite();
                this.instance.setChildren(index, value);
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder setChildren(int index, SparkSamplerProtos.StackTraceNode.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.setChildren(index, builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder addChildren(SparkSamplerProtos.StackTraceNode value) {
                this.copyOnWrite();
                this.instance.addChildren(value);
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder addChildren(int index, SparkSamplerProtos.StackTraceNode value) {
                this.copyOnWrite();
                this.instance.addChildren(index, value);
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder addChildren(SparkSamplerProtos.StackTraceNode.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.addChildren(builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder addChildren(int index, SparkSamplerProtos.StackTraceNode.Builder builderForValue) {
                this.copyOnWrite();
                this.instance.addChildren(index, builderForValue.build());
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder addAllChildren(Iterable<? extends SparkSamplerProtos.StackTraceNode> values) {
                this.copyOnWrite();
                this.instance.addAllChildren(values);
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder clearChildren() {
                this.copyOnWrite();
                this.instance.clearChildren();
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder removeChildren(int index) {
                this.copyOnWrite();
                this.instance.removeChildren(index);
                return this;
            }

            @Override
            public List<Double> getTimesList() {
                return Collections.unmodifiableList(this.instance.getTimesList());
            }

            @Override
            public int getTimesCount() {
                return this.instance.getTimesCount();
            }

            @Override
            public double getTimes(int index) {
                return this.instance.getTimes(index);
            }

            public SparkSamplerProtos.ThreadNode.Builder setTimes(int index, double value) {
                this.copyOnWrite();
                this.instance.setTimes(index, value);
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder addTimes(double value) {
                this.copyOnWrite();
                this.instance.addTimes(value);
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder addAllTimes(Iterable<? extends Double> values) {
                this.copyOnWrite();
                this.instance.addAllTimes(values);
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder clearTimes() {
                this.copyOnWrite();
                this.instance.clearTimes();
                return this;
            }

            @Override
            public List<Integer> getChildrenRefsList() {
                return Collections.unmodifiableList(this.instance.getChildrenRefsList());
            }

            @Override
            public int getChildrenRefsCount() {
                return this.instance.getChildrenRefsCount();
            }

            @Override
            public int getChildrenRefs(int index) {
                return this.instance.getChildrenRefs(index);
            }

            public SparkSamplerProtos.ThreadNode.Builder setChildrenRefs(int index, int value) {
                this.copyOnWrite();
                this.instance.setChildrenRefs(index, value);
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder addChildrenRefs(int value) {
                this.copyOnWrite();
                this.instance.addChildrenRefs(value);
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder addAllChildrenRefs(Iterable<? extends Integer> values) {
                this.copyOnWrite();
                this.instance.addAllChildrenRefs(values);
                return this;
            }

            public SparkSamplerProtos.ThreadNode.Builder clearChildrenRefs() {
                this.copyOnWrite();
                this.instance.clearChildrenRefs();
                return this;
            }
        }
    }

    public interface ThreadNodeOrBuilder extends MessageLiteOrBuilder {

        String getName();

        ByteString getNameBytes();

        List<SparkSamplerProtos.StackTraceNode> getChildrenList();

        SparkSamplerProtos.StackTraceNode getChildren(int var1);

        int getChildrenCount();

        List<Double> getTimesList();

        int getTimesCount();

        double getTimes(int var1);

        List<Integer> getChildrenRefsList();

        int getChildrenRefsCount();

        int getChildrenRefs(int var1);
    }
}