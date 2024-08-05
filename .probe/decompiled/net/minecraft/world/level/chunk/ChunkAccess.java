package net.minecraft.world.level.chunk;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListenerRegistry;
import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.lighting.ChunkSkyLightSources;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.ticks.SerializableTickContainer;
import net.minecraft.world.ticks.TickContainerAccess;
import org.slf4j.Logger;

public abstract class ChunkAccess implements BlockGetter, BiomeManager.NoiseBiomeSource, LightChunk, StructureAccess {

    public static final int NO_FILLED_SECTION = -1;

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final LongSet EMPTY_REFERENCE_SET = new LongOpenHashSet();

    protected final ShortList[] postProcessing;

    protected volatile boolean unsaved;

    private volatile boolean isLightCorrect;

    protected final ChunkPos chunkPos;

    private long inhabitedTime;

    @Nullable
    @Deprecated
    private BiomeGenerationSettings carverBiomeSettings;

    @Nullable
    protected NoiseChunk noiseChunk;

    protected final UpgradeData upgradeData;

    @Nullable
    protected BlendingData blendingData;

    protected final Map<Heightmap.Types, Heightmap> heightmaps = Maps.newEnumMap(Heightmap.Types.class);

    protected ChunkSkyLightSources skyLightSources;

    private final Map<Structure, StructureStart> structureStarts = Maps.newHashMap();

    private final Map<Structure, LongSet> structuresRefences = Maps.newHashMap();

    protected final Map<BlockPos, CompoundTag> pendingBlockEntities = Maps.newHashMap();

    protected final Map<BlockPos, BlockEntity> blockEntities = Maps.newHashMap();

    protected final LevelHeightAccessor levelHeightAccessor;

    protected final LevelChunkSection[] sections;

    public ChunkAccess(ChunkPos chunkPos0, UpgradeData upgradeData1, LevelHeightAccessor levelHeightAccessor2, Registry<Biome> registryBiome3, long long4, @Nullable LevelChunkSection[] levelChunkSection5, @Nullable BlendingData blendingData6) {
        this.chunkPos = chunkPos0;
        this.upgradeData = upgradeData1;
        this.levelHeightAccessor = levelHeightAccessor2;
        this.sections = new LevelChunkSection[levelHeightAccessor2.getSectionsCount()];
        this.inhabitedTime = long4;
        this.postProcessing = new ShortList[levelHeightAccessor2.getSectionsCount()];
        this.blendingData = blendingData6;
        this.skyLightSources = new ChunkSkyLightSources(levelHeightAccessor2);
        if (levelChunkSection5 != null) {
            if (this.sections.length == levelChunkSection5.length) {
                System.arraycopy(levelChunkSection5, 0, this.sections, 0, this.sections.length);
            } else {
                LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", levelChunkSection5.length, this.sections.length);
            }
        }
        replaceMissingSections(registryBiome3, this.sections);
    }

    private static void replaceMissingSections(Registry<Biome> registryBiome0, LevelChunkSection[] levelChunkSection1) {
        for (int $$2 = 0; $$2 < levelChunkSection1.length; $$2++) {
            if (levelChunkSection1[$$2] == null) {
                levelChunkSection1[$$2] = new LevelChunkSection(registryBiome0);
            }
        }
    }

    public GameEventListenerRegistry getListenerRegistry(int int0) {
        return GameEventListenerRegistry.NOOP;
    }

    @Nullable
    public abstract BlockState setBlockState(BlockPos var1, BlockState var2, boolean var3);

    public abstract void setBlockEntity(BlockEntity var1);

    public abstract void addEntity(Entity var1);

    public int getHighestFilledSectionIndex() {
        LevelChunkSection[] $$0 = this.getSections();
        for (int $$1 = $$0.length - 1; $$1 >= 0; $$1--) {
            LevelChunkSection $$2 = $$0[$$1];
            if (!$$2.hasOnlyAir()) {
                return $$1;
            }
        }
        return -1;
    }

    @Deprecated(forRemoval = true)
    public int getHighestSectionPosition() {
        int $$0 = this.getHighestFilledSectionIndex();
        return $$0 == -1 ? this.getMinBuildHeight() : SectionPos.sectionToBlockCoord(this.m_151568_($$0));
    }

    public Set<BlockPos> getBlockEntitiesPos() {
        Set<BlockPos> $$0 = Sets.newHashSet(this.pendingBlockEntities.keySet());
        $$0.addAll(this.blockEntities.keySet());
        return $$0;
    }

    public LevelChunkSection[] getSections() {
        return this.sections;
    }

    public LevelChunkSection getSection(int int0) {
        return this.getSections()[int0];
    }

    public Collection<Entry<Heightmap.Types, Heightmap>> getHeightmaps() {
        return Collections.unmodifiableSet(this.heightmaps.entrySet());
    }

    public void setHeightmap(Heightmap.Types heightmapTypes0, long[] long1) {
        this.getOrCreateHeightmapUnprimed(heightmapTypes0).setRawData(this, heightmapTypes0, long1);
    }

    public Heightmap getOrCreateHeightmapUnprimed(Heightmap.Types heightmapTypes0) {
        return (Heightmap) this.heightmaps.computeIfAbsent(heightmapTypes0, p_187665_ -> new Heightmap(this, p_187665_));
    }

    public boolean hasPrimedHeightmap(Heightmap.Types heightmapTypes0) {
        return this.heightmaps.get(heightmapTypes0) != null;
    }

    public int getHeight(Heightmap.Types heightmapTypes0, int int1, int int2) {
        Heightmap $$3 = (Heightmap) this.heightmaps.get(heightmapTypes0);
        if ($$3 == null) {
            if (SharedConstants.IS_RUNNING_IN_IDE && this instanceof LevelChunk) {
                LOGGER.error("Unprimed heightmap: " + heightmapTypes0 + " " + int1 + " " + int2);
            }
            Heightmap.primeHeightmaps(this, EnumSet.of(heightmapTypes0));
            $$3 = (Heightmap) this.heightmaps.get(heightmapTypes0);
        }
        return $$3.getFirstAvailable(int1 & 15, int2 & 15) - 1;
    }

    public ChunkPos getPos() {
        return this.chunkPos;
    }

    @Nullable
    @Override
    public StructureStart getStartForStructure(Structure structure0) {
        return (StructureStart) this.structureStarts.get(structure0);
    }

    @Override
    public void setStartForStructure(Structure structure0, StructureStart structureStart1) {
        this.structureStarts.put(structure0, structureStart1);
        this.unsaved = true;
    }

    public Map<Structure, StructureStart> getAllStarts() {
        return Collections.unmodifiableMap(this.structureStarts);
    }

    public void setAllStarts(Map<Structure, StructureStart> mapStructureStructureStart0) {
        this.structureStarts.clear();
        this.structureStarts.putAll(mapStructureStructureStart0);
        this.unsaved = true;
    }

    @Override
    public LongSet getReferencesForStructure(Structure structure0) {
        return (LongSet) this.structuresRefences.getOrDefault(structure0, EMPTY_REFERENCE_SET);
    }

    @Override
    public void addReferenceForStructure(Structure structure0, long long1) {
        ((LongSet) this.structuresRefences.computeIfAbsent(structure0, p_223019_ -> new LongOpenHashSet())).add(long1);
        this.unsaved = true;
    }

    @Override
    public Map<Structure, LongSet> getAllReferences() {
        return Collections.unmodifiableMap(this.structuresRefences);
    }

    @Override
    public void setAllReferences(Map<Structure, LongSet> mapStructureLongSet0) {
        this.structuresRefences.clear();
        this.structuresRefences.putAll(mapStructureLongSet0);
        this.unsaved = true;
    }

    public boolean isYSpaceEmpty(int int0, int int1) {
        if (int0 < this.getMinBuildHeight()) {
            int0 = this.getMinBuildHeight();
        }
        if (int1 >= this.m_151558_()) {
            int1 = this.m_151558_() - 1;
        }
        for (int $$2 = int0; $$2 <= int1; $$2 += 16) {
            if (!this.getSection(this.m_151564_($$2)).hasOnlyAir()) {
                return false;
            }
        }
        return true;
    }

    public void setUnsaved(boolean boolean0) {
        this.unsaved = boolean0;
    }

    public boolean isUnsaved() {
        return this.unsaved;
    }

    public abstract ChunkStatus getStatus();

    public ChunkStatus getHighestGeneratedStatus() {
        ChunkStatus $$0 = this.getStatus();
        BelowZeroRetrogen $$1 = this.getBelowZeroRetrogen();
        if ($$1 != null) {
            ChunkStatus $$2 = $$1.targetStatus();
            return $$2.isOrAfter($$0) ? $$2 : $$0;
        } else {
            return $$0;
        }
    }

    public abstract void removeBlockEntity(BlockPos var1);

    public void markPosForPostprocessing(BlockPos blockPos0) {
        LOGGER.warn("Trying to mark a block for PostProcessing @ {}, but this operation is not supported.", blockPos0);
    }

    public ShortList[] getPostProcessing() {
        return this.postProcessing;
    }

    public void addPackedPostProcess(short short0, int int1) {
        getOrCreateOffsetList(this.getPostProcessing(), int1).add(short0);
    }

    public void setBlockEntityNbt(CompoundTag compoundTag0) {
        this.pendingBlockEntities.put(BlockEntity.getPosFromTag(compoundTag0), compoundTag0);
    }

    @Nullable
    public CompoundTag getBlockEntityNbt(BlockPos blockPos0) {
        return (CompoundTag) this.pendingBlockEntities.get(blockPos0);
    }

    @Nullable
    public abstract CompoundTag getBlockEntityNbtForSaving(BlockPos var1);

    @Override
    public final void findBlockLightSources(BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState0) {
        this.findBlocks(p_284897_ -> p_284897_.m_60791_() != 0, biConsumerBlockPosBlockState0);
    }

    public void findBlocks(Predicate<BlockState> predicateBlockState0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1) {
        BlockPos.MutableBlockPos $$2 = new BlockPos.MutableBlockPos();
        for (int $$3 = this.m_151560_(); $$3 < this.m_151561_(); $$3++) {
            LevelChunkSection $$4 = this.getSection(this.m_151566_($$3));
            if ($$4.maybeHas(predicateBlockState0)) {
                BlockPos $$5 = SectionPos.of(this.chunkPos, $$3).origin();
                for (int $$6 = 0; $$6 < 16; $$6++) {
                    for (int $$7 = 0; $$7 < 16; $$7++) {
                        for (int $$8 = 0; $$8 < 16; $$8++) {
                            BlockState $$9 = $$4.getBlockState($$8, $$6, $$7);
                            if (predicateBlockState0.test($$9)) {
                                biConsumerBlockPosBlockState1.accept($$2.setWithOffset($$5, $$8, $$6, $$7), $$9);
                            }
                        }
                    }
                }
            }
        }
    }

    public abstract TickContainerAccess<Block> getBlockTicks();

    public abstract TickContainerAccess<Fluid> getFluidTicks();

    public abstract ChunkAccess.TicksToSave getTicksForSerialization();

    public UpgradeData getUpgradeData() {
        return this.upgradeData;
    }

    public boolean isOldNoiseGeneration() {
        return this.blendingData != null;
    }

    @Nullable
    public BlendingData getBlendingData() {
        return this.blendingData;
    }

    public void setBlendingData(BlendingData blendingData0) {
        this.blendingData = blendingData0;
    }

    public long getInhabitedTime() {
        return this.inhabitedTime;
    }

    public void incrementInhabitedTime(long long0) {
        this.inhabitedTime += long0;
    }

    public void setInhabitedTime(long long0) {
        this.inhabitedTime = long0;
    }

    public static ShortList getOrCreateOffsetList(ShortList[] shortList0, int int1) {
        if (shortList0[int1] == null) {
            shortList0[int1] = new ShortArrayList();
        }
        return shortList0[int1];
    }

    public boolean isLightCorrect() {
        return this.isLightCorrect;
    }

    public void setLightCorrect(boolean boolean0) {
        this.isLightCorrect = boolean0;
        this.setUnsaved(true);
    }

    @Override
    public int getMinBuildHeight() {
        return this.levelHeightAccessor.getMinBuildHeight();
    }

    @Override
    public int getHeight() {
        return this.levelHeightAccessor.getHeight();
    }

    public NoiseChunk getOrCreateNoiseChunk(Function<ChunkAccess, NoiseChunk> functionChunkAccessNoiseChunk0) {
        if (this.noiseChunk == null) {
            this.noiseChunk = (NoiseChunk) functionChunkAccessNoiseChunk0.apply(this);
        }
        return this.noiseChunk;
    }

    @Deprecated
    public BiomeGenerationSettings carverBiome(Supplier<BiomeGenerationSettings> supplierBiomeGenerationSettings0) {
        if (this.carverBiomeSettings == null) {
            this.carverBiomeSettings = (BiomeGenerationSettings) supplierBiomeGenerationSettings0.get();
        }
        return this.carverBiomeSettings;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int int0, int int1, int int2) {
        try {
            int $$3 = QuartPos.fromBlock(this.getMinBuildHeight());
            int $$4 = $$3 + QuartPos.fromBlock(this.getHeight()) - 1;
            int $$5 = Mth.clamp(int1, $$3, $$4);
            int $$6 = this.m_151564_(QuartPos.toBlock($$5));
            return this.sections[$$6].getNoiseBiome(int0 & 3, $$5 & 3, int2 & 3);
        } catch (Throwable var8) {
            CrashReport $$8 = CrashReport.forThrowable(var8, "Getting biome");
            CrashReportCategory $$9 = $$8.addCategory("Biome being got");
            $$9.setDetail("Location", (CrashReportDetail<String>) (() -> CrashReportCategory.formatLocation(this, int0, int1, int2)));
            throw new ReportedException($$8);
        }
    }

    public void fillBiomesFromNoise(BiomeResolver biomeResolver0, Climate.Sampler climateSampler1) {
        ChunkPos $$2 = this.getPos();
        int $$3 = QuartPos.fromBlock($$2.getMinBlockX());
        int $$4 = QuartPos.fromBlock($$2.getMinBlockZ());
        LevelHeightAccessor $$5 = this.getHeightAccessorForGeneration();
        for (int $$6 = $$5.getMinSection(); $$6 < $$5.getMaxSection(); $$6++) {
            LevelChunkSection $$7 = this.getSection(this.m_151566_($$6));
            int $$8 = QuartPos.fromSection($$6);
            $$7.fillBiomesFromNoise(biomeResolver0, climateSampler1, $$3, $$8, $$4);
        }
    }

    public boolean hasAnyStructureReferences() {
        return !this.getAllReferences().isEmpty();
    }

    @Nullable
    public BelowZeroRetrogen getBelowZeroRetrogen() {
        return null;
    }

    public boolean isUpgrading() {
        return this.getBelowZeroRetrogen() != null;
    }

    public LevelHeightAccessor getHeightAccessorForGeneration() {
        return this;
    }

    public void initializeLightSources() {
        this.skyLightSources.fillFrom(this);
    }

    @Override
    public ChunkSkyLightSources getSkyLightSources() {
        return this.skyLightSources;
    }

    public static record TicksToSave(SerializableTickContainer<Block> f_187680_, SerializableTickContainer<Fluid> f_187681_) {

        private final SerializableTickContainer<Block> blocks;

        private final SerializableTickContainer<Fluid> fluids;

        public TicksToSave(SerializableTickContainer<Block> f_187680_, SerializableTickContainer<Fluid> f_187681_) {
            this.blocks = f_187680_;
            this.fluids = f_187681_;
        }
    }
}