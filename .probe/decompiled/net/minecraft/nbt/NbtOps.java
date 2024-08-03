package net.minecraft.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.RecordBuilder.AbstractStringBuilder;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class NbtOps implements DynamicOps<Tag> {

    public static final NbtOps INSTANCE = new NbtOps();

    private static final String WRAPPER_MARKER = "";

    protected NbtOps() {
    }

    public Tag empty() {
        return EndTag.INSTANCE;
    }

    public <U> U convertTo(DynamicOps<U> dynamicOpsU0, Tag tag1) {
        switch(tag1.getId()) {
            case 0:
                return (U) dynamicOpsU0.empty();
            case 1:
                return (U) dynamicOpsU0.createByte(((NumericTag) tag1).getAsByte());
            case 2:
                return (U) dynamicOpsU0.createShort(((NumericTag) tag1).getAsShort());
            case 3:
                return (U) dynamicOpsU0.createInt(((NumericTag) tag1).getAsInt());
            case 4:
                return (U) dynamicOpsU0.createLong(((NumericTag) tag1).getAsLong());
            case 5:
                return (U) dynamicOpsU0.createFloat(((NumericTag) tag1).getAsFloat());
            case 6:
                return (U) dynamicOpsU0.createDouble(((NumericTag) tag1).getAsDouble());
            case 7:
                return (U) dynamicOpsU0.createByteList(ByteBuffer.wrap(((ByteArrayTag) tag1).getAsByteArray()));
            case 8:
                return (U) dynamicOpsU0.createString(tag1.getAsString());
            case 9:
                return (U) this.convertList(dynamicOpsU0, tag1);
            case 10:
                return (U) this.convertMap(dynamicOpsU0, tag1);
            case 11:
                return (U) dynamicOpsU0.createIntList(Arrays.stream(((IntArrayTag) tag1).getAsIntArray()));
            case 12:
                return (U) dynamicOpsU0.createLongList(Arrays.stream(((LongArrayTag) tag1).getAsLongArray()));
            default:
                throw new IllegalStateException("Unknown tag type: " + tag1);
        }
    }

    public DataResult<Number> getNumberValue(Tag tag0) {
        return tag0 instanceof NumericTag $$1 ? DataResult.success($$1.getAsNumber()) : DataResult.error(() -> "Not a number");
    }

    public Tag createNumeric(Number number0) {
        return DoubleTag.valueOf(number0.doubleValue());
    }

    public Tag createByte(byte byte0) {
        return ByteTag.valueOf(byte0);
    }

    public Tag createShort(short short0) {
        return ShortTag.valueOf(short0);
    }

    public Tag createInt(int int0) {
        return IntTag.valueOf(int0);
    }

    public Tag createLong(long long0) {
        return LongTag.valueOf(long0);
    }

    public Tag createFloat(float float0) {
        return FloatTag.valueOf(float0);
    }

    public Tag createDouble(double double0) {
        return DoubleTag.valueOf(double0);
    }

    public Tag createBoolean(boolean boolean0) {
        return ByteTag.valueOf(boolean0);
    }

    public DataResult<String> getStringValue(Tag tag0) {
        return tag0 instanceof StringTag $$1 ? DataResult.success($$1.getAsString()) : DataResult.error(() -> "Not a string");
    }

    public Tag createString(String string0) {
        return StringTag.valueOf(string0);
    }

    public DataResult<Tag> mergeToList(Tag tag0, Tag tag1) {
        return (DataResult<Tag>) createCollector(tag0).map(p_248053_ -> DataResult.success(p_248053_.accept(tag1).result())).orElseGet(() -> DataResult.error(() -> "mergeToList called with not a list: " + tag0, tag0));
    }

    public DataResult<Tag> mergeToList(Tag tag0, List<Tag> listTag1) {
        return (DataResult<Tag>) createCollector(tag0).map(p_248048_ -> DataResult.success(p_248048_.acceptAll(listTag1).result())).orElseGet(() -> DataResult.error(() -> "mergeToList called with not a list: " + tag0, tag0));
    }

    public DataResult<Tag> mergeToMap(Tag tag0, Tag tag1, Tag tag2) {
        if (!(tag0 instanceof CompoundTag) && !(tag0 instanceof EndTag)) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + tag0, tag0);
        } else if (!(tag1 instanceof StringTag)) {
            return DataResult.error(() -> "key is not a string: " + tag1, tag0);
        } else {
            CompoundTag $$3 = new CompoundTag();
            if (tag0 instanceof CompoundTag $$4) {
                $$4.getAllKeys().forEach(p_129068_ -> $$3.put(p_129068_, $$4.get(p_129068_)));
            }
            $$3.put(tag1.getAsString(), tag2);
            return DataResult.success($$3);
        }
    }

    public DataResult<Tag> mergeToMap(Tag tag0, MapLike<Tag> mapLikeTag1) {
        if (!(tag0 instanceof CompoundTag) && !(tag0 instanceof EndTag)) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + tag0, tag0);
        } else {
            CompoundTag $$2 = new CompoundTag();
            if (tag0 instanceof CompoundTag $$3) {
                $$3.getAllKeys().forEach(p_129059_ -> $$2.put(p_129059_, $$3.get(p_129059_)));
            }
            List<Tag> $$4 = Lists.newArrayList();
            mapLikeTag1.entries().forEach(p_128994_ -> {
                Tag $$3 = (Tag) p_128994_.getFirst();
                if (!($$3 instanceof StringTag)) {
                    $$4.add($$3);
                } else {
                    $$2.put($$3.getAsString(), (Tag) p_128994_.getSecond());
                }
            });
            return !$$4.isEmpty() ? DataResult.error(() -> "some keys are not strings: " + $$4, $$2) : DataResult.success($$2);
        }
    }

    public DataResult<Stream<Pair<Tag, Tag>>> getMapValues(Tag tag0) {
        return tag0 instanceof CompoundTag $$1 ? DataResult.success($$1.getAllKeys().stream().map(p_129021_ -> Pair.of(this.createString(p_129021_), $$1.get(p_129021_)))) : DataResult.error(() -> "Not a map: " + tag0);
    }

    public DataResult<Consumer<BiConsumer<Tag, Tag>>> getMapEntries(Tag tag0) {
        return tag0 instanceof CompoundTag $$1 ? DataResult.success((Consumer) p_129024_ -> $$1.getAllKeys().forEach(p_178006_ -> p_129024_.accept(this.createString(p_178006_), $$1.get(p_178006_)))) : DataResult.error(() -> "Not a map: " + tag0);
    }

    public DataResult<MapLike<Tag>> getMap(Tag tag0) {
        return tag0 instanceof CompoundTag $$1 ? DataResult.success(new MapLike<Tag>() {

            @Nullable
            public Tag get(Tag p_129174_) {
                return $$1.get(p_129174_.getAsString());
            }

            @Nullable
            public Tag get(String p_129169_) {
                return $$1.get(p_129169_);
            }

            public Stream<Pair<Tag, Tag>> entries() {
                return $$1.getAllKeys().stream().map(p_129172_ -> Pair.of(NbtOps.this.createString(p_129172_), $$1.get(p_129172_)));
            }

            public String toString() {
                return "MapLike[" + $$1 + "]";
            }
        }) : DataResult.error(() -> "Not a map: " + tag0);
    }

    public Tag createMap(Stream<Pair<Tag, Tag>> streamPairTagTag0) {
        CompoundTag $$1 = new CompoundTag();
        streamPairTagTag0.forEach(p_129018_ -> $$1.put(((Tag) p_129018_.getFirst()).getAsString(), (Tag) p_129018_.getSecond()));
        return $$1;
    }

    private static Tag tryUnwrap(CompoundTag compoundTag0) {
        if (compoundTag0.size() == 1) {
            Tag $$1 = compoundTag0.get("");
            if ($$1 != null) {
                return $$1;
            }
        }
        return compoundTag0;
    }

    public DataResult<Stream<Tag>> getStream(Tag tag0) {
        if (tag0 instanceof ListTag $$1) {
            return $$1.getElementType() == 10 ? DataResult.success($$1.stream().map(p_248049_ -> tryUnwrap((CompoundTag) p_248049_))) : DataResult.success($$1.stream());
        } else {
            return tag0 instanceof CollectionTag<?> $$2 ? DataResult.success($$2.stream().map(p_129158_ -> p_129158_)) : DataResult.error(() -> "Not a list");
        }
    }

    public DataResult<Consumer<Consumer<Tag>>> getList(Tag tag0) {
        if (tag0 instanceof ListTag $$1) {
            return $$1.getElementType() == 10 ? DataResult.success((Consumer) p_248055_ -> $$1.forEach(p_248051_ -> p_248055_.accept(tryUnwrap((CompoundTag) p_248051_)))) : DataResult.success($$1::forEach);
        } else {
            return tag0 instanceof CollectionTag<?> $$2 ? DataResult.success($$2::forEach) : DataResult.error(() -> "Not a list: " + tag0);
        }
    }

    public DataResult<ByteBuffer> getByteBuffer(Tag tag0) {
        return tag0 instanceof ByteArrayTag $$1 ? DataResult.success(ByteBuffer.wrap($$1.getAsByteArray())) : super.getByteBuffer(tag0);
    }

    public Tag createByteList(ByteBuffer byteBuffer0) {
        return new ByteArrayTag(DataFixUtils.toArray(byteBuffer0));
    }

    public DataResult<IntStream> getIntStream(Tag tag0) {
        return tag0 instanceof IntArrayTag $$1 ? DataResult.success(Arrays.stream($$1.getAsIntArray())) : super.getIntStream(tag0);
    }

    public Tag createIntList(IntStream intStream0) {
        return new IntArrayTag(intStream0.toArray());
    }

    public DataResult<LongStream> getLongStream(Tag tag0) {
        return tag0 instanceof LongArrayTag $$1 ? DataResult.success(Arrays.stream($$1.getAsLongArray())) : super.getLongStream(tag0);
    }

    public Tag createLongList(LongStream longStream0) {
        return new LongArrayTag(longStream0.toArray());
    }

    public Tag createList(Stream<Tag> streamTag0) {
        return NbtOps.InitialListCollector.INSTANCE.m_246922_(streamTag0).result();
    }

    public Tag remove(Tag tag0, String string1) {
        if (tag0 instanceof CompoundTag $$2) {
            CompoundTag $$3 = new CompoundTag();
            $$2.getAllKeys().stream().filter(p_128988_ -> !Objects.equals(p_128988_, string1)).forEach(p_129028_ -> $$3.put(p_129028_, $$2.get(p_129028_)));
            return $$3;
        } else {
            return tag0;
        }
    }

    public String toString() {
        return "NBT";
    }

    public RecordBuilder<Tag> mapBuilder() {
        return new NbtOps.NbtRecordBuilder();
    }

    private static Optional<NbtOps.ListCollector> createCollector(Tag tag0) {
        if (tag0 instanceof EndTag) {
            return Optional.of(NbtOps.InitialListCollector.INSTANCE);
        } else {
            if (tag0 instanceof CollectionTag<?> $$1) {
                if ($$1.isEmpty()) {
                    return Optional.of(NbtOps.InitialListCollector.INSTANCE);
                }
                if ($$1 instanceof ListTag $$2) {
                    return switch($$2.getElementType()) {
                        case 0 ->
                            Optional.of(NbtOps.InitialListCollector.INSTANCE);
                        case 10 ->
                            Optional.of(new NbtOps.HeterogenousListCollector($$2));
                        default ->
                            Optional.of(new NbtOps.HomogenousListCollector($$2));
                    };
                }
                if ($$1 instanceof ByteArrayTag $$3) {
                    return Optional.of(new NbtOps.ByteListCollector($$3.getAsByteArray()));
                }
                if ($$1 instanceof IntArrayTag $$4) {
                    return Optional.of(new NbtOps.IntListCollector($$4.getAsIntArray()));
                }
                if ($$1 instanceof LongArrayTag $$5) {
                    return Optional.of(new NbtOps.LongListCollector($$5.getAsLongArray()));
                }
            }
            return Optional.empty();
        }
    }

    static class ByteListCollector implements NbtOps.ListCollector {

        private final ByteArrayList values = new ByteArrayList();

        public ByteListCollector(byte byte0) {
            this.values.add(byte0);
        }

        public ByteListCollector(byte[] byte0) {
            this.values.addElements(0, byte0);
        }

        @Override
        public NbtOps.ListCollector accept(Tag tag0) {
            if (tag0 instanceof ByteTag $$1) {
                this.values.add($$1.getAsByte());
                return this;
            } else {
                return new NbtOps.HeterogenousListCollector(this.values).accept(tag0);
            }
        }

        @Override
        public Tag result() {
            return new ByteArrayTag(this.values.toByteArray());
        }
    }

    static class HeterogenousListCollector implements NbtOps.ListCollector {

        private final ListTag result = new ListTag();

        public HeterogenousListCollector() {
        }

        public HeterogenousListCollector(Collection<Tag> collectionTag0) {
            this.result.addAll(collectionTag0);
        }

        public HeterogenousListCollector(IntArrayList intArrayList0) {
            intArrayList0.forEach(p_249166_ -> this.result.add(wrapElement(IntTag.valueOf(p_249166_))));
        }

        public HeterogenousListCollector(ByteArrayList byteArrayList0) {
            byteArrayList0.forEach(p_249160_ -> this.result.add(wrapElement(ByteTag.valueOf(p_249160_))));
        }

        public HeterogenousListCollector(LongArrayList longArrayList0) {
            longArrayList0.forEach(p_249754_ -> this.result.add(wrapElement(LongTag.valueOf(p_249754_))));
        }

        private static boolean isWrapper(CompoundTag compoundTag0) {
            return compoundTag0.size() == 1 && compoundTag0.contains("");
        }

        private static Tag wrapIfNeeded(Tag tag0) {
            if (tag0 instanceof CompoundTag $$1 && !isWrapper($$1)) {
                return $$1;
            }
            return wrapElement(tag0);
        }

        private static CompoundTag wrapElement(Tag tag0) {
            CompoundTag $$1 = new CompoundTag();
            $$1.put("", tag0);
            return $$1;
        }

        @Override
        public NbtOps.ListCollector accept(Tag tag0) {
            this.result.add(wrapIfNeeded(tag0));
            return this;
        }

        @Override
        public Tag result() {
            return this.result;
        }
    }

    static class HomogenousListCollector implements NbtOps.ListCollector {

        private final ListTag result = new ListTag();

        HomogenousListCollector(Tag tag0) {
            this.result.add(tag0);
        }

        HomogenousListCollector(ListTag listTag0) {
            this.result.addAll(listTag0);
        }

        @Override
        public NbtOps.ListCollector accept(Tag tag0) {
            if (tag0.getId() != this.result.getElementType()) {
                return new NbtOps.HeterogenousListCollector().m_246277_(this.result).accept(tag0);
            } else {
                this.result.add(tag0);
                return this;
            }
        }

        @Override
        public Tag result() {
            return this.result;
        }
    }

    static class InitialListCollector implements NbtOps.ListCollector {

        public static final NbtOps.InitialListCollector INSTANCE = new NbtOps.InitialListCollector();

        private InitialListCollector() {
        }

        @Override
        public NbtOps.ListCollector accept(Tag tag0) {
            if (tag0 instanceof CompoundTag $$1) {
                return new NbtOps.HeterogenousListCollector().accept($$1);
            } else if (tag0 instanceof ByteTag $$2) {
                return new NbtOps.ByteListCollector($$2.getAsByte());
            } else if (tag0 instanceof IntTag $$3) {
                return new NbtOps.IntListCollector($$3.getAsInt());
            } else {
                return (NbtOps.ListCollector) (tag0 instanceof LongTag $$4 ? new NbtOps.LongListCollector($$4.getAsLong()) : new NbtOps.HomogenousListCollector(tag0));
            }
        }

        @Override
        public Tag result() {
            return new ListTag();
        }
    }

    static class IntListCollector implements NbtOps.ListCollector {

        private final IntArrayList values = new IntArrayList();

        public IntListCollector(int int0) {
            this.values.add(int0);
        }

        public IntListCollector(int[] int0) {
            this.values.addElements(0, int0);
        }

        @Override
        public NbtOps.ListCollector accept(Tag tag0) {
            if (tag0 instanceof IntTag $$1) {
                this.values.add($$1.getAsInt());
                return this;
            } else {
                return new NbtOps.HeterogenousListCollector(this.values).accept(tag0);
            }
        }

        @Override
        public Tag result() {
            return new IntArrayTag(this.values.toIntArray());
        }
    }

    interface ListCollector {

        NbtOps.ListCollector accept(Tag var1);

        default NbtOps.ListCollector acceptAll(Iterable<Tag> iterableTag0) {
            NbtOps.ListCollector $$1 = this;
            for (Tag $$2 : iterableTag0) {
                $$1 = $$1.accept($$2);
            }
            return $$1;
        }

        default NbtOps.ListCollector acceptAll(Stream<Tag> streamTag0) {
            return this.acceptAll(streamTag0::iterator);
        }

        Tag result();
    }

    static class LongListCollector implements NbtOps.ListCollector {

        private final LongArrayList values = new LongArrayList();

        public LongListCollector(long long0) {
            this.values.add(long0);
        }

        public LongListCollector(long[] long0) {
            this.values.addElements(0, long0);
        }

        @Override
        public NbtOps.ListCollector accept(Tag tag0) {
            if (tag0 instanceof LongTag $$1) {
                this.values.add($$1.getAsLong());
                return this;
            } else {
                return new NbtOps.HeterogenousListCollector(this.values).accept(tag0);
            }
        }

        @Override
        public Tag result() {
            return new LongArrayTag(this.values.toLongArray());
        }
    }

    class NbtRecordBuilder extends AbstractStringBuilder<Tag, CompoundTag> {

        protected NbtRecordBuilder() {
            super(NbtOps.this);
        }

        protected CompoundTag initBuilder() {
            return new CompoundTag();
        }

        protected CompoundTag append(String string0, Tag tag1, CompoundTag compoundTag2) {
            compoundTag2.put(string0, tag1);
            return compoundTag2;
        }

        protected DataResult<Tag> build(CompoundTag compoundTag0, Tag tag1) {
            if (tag1 == null || tag1 == EndTag.INSTANCE) {
                return DataResult.success(compoundTag0);
            } else if (!(tag1 instanceof CompoundTag $$2)) {
                return DataResult.error(() -> "mergeToMap called with not a map: " + tag1, tag1);
            } else {
                CompoundTag $$3 = new CompoundTag(Maps.newHashMap($$2.entries()));
                for (Entry<String, Tag> $$4 : compoundTag0.entries().entrySet()) {
                    $$3.put((String) $$4.getKey(), (Tag) $$4.getValue());
                }
                return DataResult.success($$3);
            }
        }
    }
}