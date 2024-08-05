package net.minecraft.world.entity.ai.village.poi;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.SectionTracker;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.storage.SectionStorage;

public class PoiManager extends SectionStorage<PoiSection> {

    public static final int MAX_VILLAGE_DISTANCE = 6;

    public static final int VILLAGE_SECTION_SIZE = 1;

    private final PoiManager.DistanceTracker distanceTracker;

    private final LongSet loadedChunks = new LongOpenHashSet();

    public PoiManager(Path path0, DataFixer dataFixer1, boolean boolean2, RegistryAccess registryAccess3, LevelHeightAccessor levelHeightAccessor4) {
        super(path0, PoiSection::m_27295_, PoiSection::new, dataFixer1, DataFixTypes.POI_CHUNK, boolean2, registryAccess3, levelHeightAccessor4);
        this.distanceTracker = new PoiManager.DistanceTracker();
    }

    public void add(BlockPos blockPos0, Holder<PoiType> holderPoiType1) {
        ((PoiSection) this.m_63827_(SectionPos.asLong(blockPos0))).add(blockPos0, holderPoiType1);
    }

    public void remove(BlockPos blockPos0) {
        this.m_63823_(SectionPos.asLong(blockPos0)).ifPresent(p_148657_ -> p_148657_.remove(blockPos0));
    }

    public long getCountInRange(Predicate<Holder<PoiType>> predicateHolderPoiType0, BlockPos blockPos1, int int2, PoiManager.Occupancy poiManagerOccupancy3) {
        return this.getInRange(predicateHolderPoiType0, blockPos1, int2, poiManagerOccupancy3).count();
    }

    public boolean existsAtPosition(ResourceKey<PoiType> resourceKeyPoiType0, BlockPos blockPos1) {
        return this.exists(blockPos1, p_217879_ -> p_217879_.is(resourceKeyPoiType0));
    }

    public Stream<PoiRecord> getInSquare(Predicate<Holder<PoiType>> predicateHolderPoiType0, BlockPos blockPos1, int int2, PoiManager.Occupancy poiManagerOccupancy3) {
        int $$4 = Math.floorDiv(int2, 16) + 1;
        return ChunkPos.rangeClosed(new ChunkPos(blockPos1), $$4).flatMap(p_217938_ -> this.getInChunk(predicateHolderPoiType0, p_217938_, poiManagerOccupancy3)).filter(p_217971_ -> {
            BlockPos $$3 = p_217971_.getPos();
            return Math.abs($$3.m_123341_() - blockPos1.m_123341_()) <= int2 && Math.abs($$3.m_123343_() - blockPos1.m_123343_()) <= int2;
        });
    }

    public Stream<PoiRecord> getInRange(Predicate<Holder<PoiType>> predicateHolderPoiType0, BlockPos blockPos1, int int2, PoiManager.Occupancy poiManagerOccupancy3) {
        int $$4 = int2 * int2;
        return this.getInSquare(predicateHolderPoiType0, blockPos1, int2, poiManagerOccupancy3).filter(p_217906_ -> p_217906_.getPos().m_123331_(blockPos1) <= (double) $$4);
    }

    @VisibleForDebug
    public Stream<PoiRecord> getInChunk(Predicate<Holder<PoiType>> predicateHolderPoiType0, ChunkPos chunkPos1, PoiManager.Occupancy poiManagerOccupancy2) {
        return IntStream.range(this.f_156618_.getMinSection(), this.f_156618_.getMaxSection()).boxed().map(p_217886_ -> this.m_63823_(SectionPos.of(chunkPos1, p_217886_).asLong())).filter(Optional::isPresent).flatMap(p_217942_ -> ((PoiSection) p_217942_.get()).getRecords(predicateHolderPoiType0, poiManagerOccupancy2));
    }

    public Stream<BlockPos> findAll(Predicate<Holder<PoiType>> predicateHolderPoiType0, Predicate<BlockPos> predicateBlockPos1, BlockPos blockPos2, int int3, PoiManager.Occupancy poiManagerOccupancy4) {
        return this.getInRange(predicateHolderPoiType0, blockPos2, int3, poiManagerOccupancy4).map(PoiRecord::m_27257_).filter(predicateBlockPos1);
    }

    public Stream<Pair<Holder<PoiType>, BlockPos>> findAllWithType(Predicate<Holder<PoiType>> predicateHolderPoiType0, Predicate<BlockPos> predicateBlockPos1, BlockPos blockPos2, int int3, PoiManager.Occupancy poiManagerOccupancy4) {
        return this.getInRange(predicateHolderPoiType0, blockPos2, int3, poiManagerOccupancy4).filter(p_217982_ -> predicateBlockPos1.test(p_217982_.getPos())).map(p_217990_ -> Pair.of(p_217990_.getPoiType(), p_217990_.getPos()));
    }

    public Stream<Pair<Holder<PoiType>, BlockPos>> findAllClosestFirstWithType(Predicate<Holder<PoiType>> predicateHolderPoiType0, Predicate<BlockPos> predicateBlockPos1, BlockPos blockPos2, int int3, PoiManager.Occupancy poiManagerOccupancy4) {
        return this.findAllWithType(predicateHolderPoiType0, predicateBlockPos1, blockPos2, int3, poiManagerOccupancy4).sorted(Comparator.comparingDouble(p_217915_ -> ((BlockPos) p_217915_.getSecond()).m_123331_(blockPos2)));
    }

    public Optional<BlockPos> find(Predicate<Holder<PoiType>> predicateHolderPoiType0, Predicate<BlockPos> predicateBlockPos1, BlockPos blockPos2, int int3, PoiManager.Occupancy poiManagerOccupancy4) {
        return this.findAll(predicateHolderPoiType0, predicateBlockPos1, blockPos2, int3, poiManagerOccupancy4).findFirst();
    }

    public Optional<BlockPos> findClosest(Predicate<Holder<PoiType>> predicateHolderPoiType0, BlockPos blockPos1, int int2, PoiManager.Occupancy poiManagerOccupancy3) {
        return this.getInRange(predicateHolderPoiType0, blockPos1, int2, poiManagerOccupancy3).map(PoiRecord::m_27257_).min(Comparator.comparingDouble(p_217977_ -> p_217977_.m_123331_(blockPos1)));
    }

    public Optional<Pair<Holder<PoiType>, BlockPos>> findClosestWithType(Predicate<Holder<PoiType>> predicateHolderPoiType0, BlockPos blockPos1, int int2, PoiManager.Occupancy poiManagerOccupancy3) {
        return this.getInRange(predicateHolderPoiType0, blockPos1, int2, poiManagerOccupancy3).min(Comparator.comparingDouble(p_217909_ -> p_217909_.getPos().m_123331_(blockPos1))).map(p_217959_ -> Pair.of(p_217959_.getPoiType(), p_217959_.getPos()));
    }

    public Optional<BlockPos> findClosest(Predicate<Holder<PoiType>> predicateHolderPoiType0, Predicate<BlockPos> predicateBlockPos1, BlockPos blockPos2, int int3, PoiManager.Occupancy poiManagerOccupancy4) {
        return this.getInRange(predicateHolderPoiType0, blockPos2, int3, poiManagerOccupancy4).map(PoiRecord::m_27257_).filter(predicateBlockPos1).min(Comparator.comparingDouble(p_217918_ -> p_217918_.m_123331_(blockPos2)));
    }

    public Optional<BlockPos> take(Predicate<Holder<PoiType>> predicateHolderPoiType0, BiPredicate<Holder<PoiType>, BlockPos> biPredicateHolderPoiTypeBlockPos1, BlockPos blockPos2, int int3) {
        return this.getInRange(predicateHolderPoiType0, blockPos2, int3, PoiManager.Occupancy.HAS_SPACE).filter(p_217934_ -> biPredicateHolderPoiTypeBlockPos1.test(p_217934_.getPoiType(), p_217934_.getPos())).findFirst().map(p_217881_ -> {
            p_217881_.acquireTicket();
            return p_217881_.getPos();
        });
    }

    public Optional<BlockPos> getRandom(Predicate<Holder<PoiType>> predicateHolderPoiType0, Predicate<BlockPos> predicateBlockPos1, PoiManager.Occupancy poiManagerOccupancy2, BlockPos blockPos3, int int4, RandomSource randomSource5) {
        List<PoiRecord> $$6 = Util.toShuffledList(this.getInRange(predicateHolderPoiType0, blockPos3, int4, poiManagerOccupancy2), randomSource5);
        return $$6.stream().filter(p_217945_ -> predicateBlockPos1.test(p_217945_.getPos())).findFirst().map(PoiRecord::m_27257_);
    }

    public boolean release(BlockPos blockPos0) {
        return (Boolean) this.m_63823_(SectionPos.asLong(blockPos0)).map(p_217993_ -> p_217993_.release(blockPos0)).orElseThrow(() -> Util.pauseInIde(new IllegalStateException("POI never registered at " + blockPos0)));
    }

    public boolean exists(BlockPos blockPos0, Predicate<Holder<PoiType>> predicateHolderPoiType1) {
        return (Boolean) this.m_63823_(SectionPos.asLong(blockPos0)).map(p_217925_ -> p_217925_.exists(blockPos0, predicateHolderPoiType1)).orElse(false);
    }

    public Optional<Holder<PoiType>> getType(BlockPos blockPos0) {
        return this.m_63823_(SectionPos.asLong(blockPos0)).flatMap(p_217974_ -> p_217974_.getType(blockPos0));
    }

    @Deprecated
    @VisibleForDebug
    public int getFreeTickets(BlockPos blockPos0) {
        return (Integer) this.m_63823_(SectionPos.asLong(blockPos0)).map(p_217912_ -> p_217912_.getFreeTickets(blockPos0)).orElse(0);
    }

    public int sectionsToVillage(SectionPos sectionPos0) {
        this.distanceTracker.runAllUpdates();
        return this.distanceTracker.getLevel(sectionPos0.asLong());
    }

    boolean isVillageCenter(long long0) {
        Optional<PoiSection> $$1 = this.m_63818_(long0);
        return $$1 == null ? false : (Boolean) $$1.map(p_217883_ -> p_217883_.getRecords(p_217927_ -> p_217927_.is(PoiTypeTags.VILLAGE), PoiManager.Occupancy.IS_OCCUPIED).findAny().isPresent()).orElse(false);
    }

    @Override
    public void tick(BooleanSupplier booleanSupplier0) {
        super.tick(booleanSupplier0);
        this.distanceTracker.runAllUpdates();
    }

    @Override
    protected void setDirty(long long0) {
        super.setDirty(long0);
        this.distanceTracker.m_8288_(long0, this.distanceTracker.getLevelFromSource(long0), false);
    }

    @Override
    protected void onSectionLoad(long long0) {
        this.distanceTracker.m_8288_(long0, this.distanceTracker.getLevelFromSource(long0), false);
    }

    public void checkConsistencyWithBlocks(SectionPos sectionPos0, LevelChunkSection levelChunkSection1) {
        Util.ifElse(this.m_63823_(sectionPos0.asLong()), p_217898_ -> p_217898_.refresh(p_217967_ -> {
            if (mayHavePoi(levelChunkSection1)) {
                this.updateFromSection(levelChunkSection1, sectionPos0, p_217967_);
            }
        }), () -> {
            if (mayHavePoi(levelChunkSection1)) {
                PoiSection $$2 = (PoiSection) this.m_63827_(sectionPos0.asLong());
                this.updateFromSection(levelChunkSection1, sectionPos0, $$2::m_218021_);
            }
        });
    }

    private static boolean mayHavePoi(LevelChunkSection levelChunkSection0) {
        return levelChunkSection0.maybeHas(PoiTypes::m_252831_);
    }

    private void updateFromSection(LevelChunkSection levelChunkSection0, SectionPos sectionPos1, BiConsumer<BlockPos, Holder<PoiType>> biConsumerBlockPosHolderPoiType2) {
        sectionPos1.blocksInside().forEach(p_217902_ -> {
            BlockState $$3 = levelChunkSection0.getBlockState(SectionPos.sectionRelative(p_217902_.m_123341_()), SectionPos.sectionRelative(p_217902_.m_123342_()), SectionPos.sectionRelative(p_217902_.m_123343_()));
            PoiTypes.forState($$3).ifPresent(p_217931_ -> biConsumerBlockPosHolderPoiType2.accept(p_217902_, p_217931_));
        });
    }

    public void ensureLoadedAndValid(LevelReader levelReader0, BlockPos blockPos1, int int2) {
        SectionPos.aroundChunk(new ChunkPos(blockPos1), Math.floorDiv(int2, 16), this.f_156618_.getMinSection(), this.f_156618_.getMaxSection()).map(p_217979_ -> Pair.of(p_217979_, this.m_63823_(p_217979_.asLong()))).filter(p_217963_ -> !(Boolean) ((Optional) p_217963_.getSecond()).map(PoiSection::m_27272_).orElse(false)).map(p_217891_ -> ((SectionPos) p_217891_.getFirst()).chunk()).filter(p_217961_ -> this.loadedChunks.add(p_217961_.toLong())).forEach(p_217889_ -> levelReader0.getChunk(p_217889_.x, p_217889_.z, ChunkStatus.EMPTY));
    }

    final class DistanceTracker extends SectionTracker {

        private final Long2ByteMap levels = new Long2ByteOpenHashMap();

        protected DistanceTracker() {
            super(7, 16, 256);
            this.levels.defaultReturnValue((byte) 7);
        }

        @Override
        protected int getLevelFromSource(long long0) {
            return PoiManager.this.isVillageCenter(long0) ? 0 : 7;
        }

        @Override
        protected int getLevel(long long0) {
            return this.levels.get(long0);
        }

        @Override
        protected void setLevel(long long0, int int1) {
            if (int1 > 6) {
                this.levels.remove(long0);
            } else {
                this.levels.put(long0, (byte) int1);
            }
        }

        public void runAllUpdates() {
            super.m_75588_(Integer.MAX_VALUE);
        }
    }

    public static enum Occupancy {

        HAS_SPACE(PoiRecord::m_27253_), IS_OCCUPIED(PoiRecord::m_27254_), ANY(p_27223_ -> true);

        private final Predicate<? super PoiRecord> test;

        private Occupancy(Predicate<? super PoiRecord> p_27220_) {
            this.test = p_27220_;
        }

        public Predicate<? super PoiRecord> getTest() {
            return this.test;
        }
    }
}