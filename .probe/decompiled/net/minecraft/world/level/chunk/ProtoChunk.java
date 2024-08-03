package net.minecraft.world.level.chunk;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.ProtoChunkTicks;
import net.minecraft.world.ticks.TickContainerAccess;

public class ProtoChunk extends ChunkAccess {

    @Nullable
    private volatile LevelLightEngine lightEngine;

    private volatile ChunkStatus status = ChunkStatus.EMPTY;

    private final List<CompoundTag> entities = Lists.newArrayList();

    private final Map<GenerationStep.Carving, CarvingMask> carvingMasks = new Object2ObjectArrayMap();

    @Nullable
    private BelowZeroRetrogen belowZeroRetrogen;

    private final ProtoChunkTicks<Block> blockTicks;

    private final ProtoChunkTicks<Fluid> fluidTicks;

    public ProtoChunk(ChunkPos chunkPos0, UpgradeData upgradeData1, LevelHeightAccessor levelHeightAccessor2, Registry<Biome> registryBiome3, @Nullable BlendingData blendingData4) {
        this(chunkPos0, upgradeData1, null, new ProtoChunkTicks<>(), new ProtoChunkTicks<>(), levelHeightAccessor2, registryBiome3, blendingData4);
    }

    public ProtoChunk(ChunkPos chunkPos0, UpgradeData upgradeData1, @Nullable LevelChunkSection[] levelChunkSection2, ProtoChunkTicks<Block> protoChunkTicksBlock3, ProtoChunkTicks<Fluid> protoChunkTicksFluid4, LevelHeightAccessor levelHeightAccessor5, Registry<Biome> registryBiome6, @Nullable BlendingData blendingData7) {
        super(chunkPos0, upgradeData1, levelHeightAccessor5, registryBiome6, 0L, levelChunkSection2, blendingData7);
        this.blockTicks = protoChunkTicksBlock3;
        this.fluidTicks = protoChunkTicksFluid4;
    }

    @Override
    public TickContainerAccess<Block> getBlockTicks() {
        return this.blockTicks;
    }

    @Override
    public TickContainerAccess<Fluid> getFluidTicks() {
        return this.fluidTicks;
    }

    @Override
    public ChunkAccess.TicksToSave getTicksForSerialization() {
        return new ChunkAccess.TicksToSave(this.blockTicks, this.fluidTicks);
    }

    @Override
    public BlockState getBlockState(BlockPos blockPos0) {
        int $$1 = blockPos0.m_123342_();
        if (this.m_151562_($$1)) {
            return Blocks.VOID_AIR.defaultBlockState();
        } else {
            LevelChunkSection $$2 = this.m_183278_(this.m_151564_($$1));
            return $$2.hasOnlyAir() ? Blocks.AIR.defaultBlockState() : $$2.getBlockState(blockPos0.m_123341_() & 15, $$1 & 15, blockPos0.m_123343_() & 15);
        }
    }

    @Override
    public FluidState getFluidState(BlockPos blockPos0) {
        int $$1 = blockPos0.m_123342_();
        if (this.m_151562_($$1)) {
            return Fluids.EMPTY.defaultFluidState();
        } else {
            LevelChunkSection $$2 = this.m_183278_(this.m_151564_($$1));
            return $$2.hasOnlyAir() ? Fluids.EMPTY.defaultFluidState() : $$2.getFluidState(blockPos0.m_123341_() & 15, $$1 & 15, blockPos0.m_123343_() & 15);
        }
    }

    @Nullable
    @Override
    public BlockState setBlockState(BlockPos blockPos0, BlockState blockState1, boolean boolean2) {
        int $$3 = blockPos0.m_123341_();
        int $$4 = blockPos0.m_123342_();
        int $$5 = blockPos0.m_123343_();
        if ($$4 >= this.m_141937_() && $$4 < this.m_151558_()) {
            int $$6 = this.m_151564_($$4);
            LevelChunkSection $$7 = this.m_183278_($$6);
            boolean $$8 = $$7.hasOnlyAir();
            if ($$8 && blockState1.m_60713_(Blocks.AIR)) {
                return blockState1;
            } else {
                int $$9 = SectionPos.sectionRelative($$3);
                int $$10 = SectionPos.sectionRelative($$4);
                int $$11 = SectionPos.sectionRelative($$5);
                BlockState $$12 = $$7.setBlockState($$9, $$10, $$11, blockState1);
                if (this.status.isOrAfter(ChunkStatus.INITIALIZE_LIGHT)) {
                    boolean $$13 = $$7.hasOnlyAir();
                    if ($$13 != $$8) {
                        this.lightEngine.m_75834_(blockPos0, $$13);
                    }
                    if (LightEngine.hasDifferentLightProperties(this, blockPos0, $$12, blockState1)) {
                        this.f_283754_.update(this, $$9, $$4, $$11);
                        this.lightEngine.checkBlock(blockPos0);
                    }
                }
                EnumSet<Heightmap.Types> $$14 = this.getStatus().heightmapsAfter();
                EnumSet<Heightmap.Types> $$15 = null;
                for (Heightmap.Types $$16 : $$14) {
                    Heightmap $$17 = (Heightmap) this.f_187608_.get($$16);
                    if ($$17 == null) {
                        if ($$15 == null) {
                            $$15 = EnumSet.noneOf(Heightmap.Types.class);
                        }
                        $$15.add($$16);
                    }
                }
                if ($$15 != null) {
                    Heightmap.primeHeightmaps(this, $$15);
                }
                for (Heightmap.Types $$18 : $$14) {
                    ((Heightmap) this.f_187608_.get($$18)).update($$9, $$4, $$11, blockState1);
                }
                return $$12;
            }
        } else {
            return Blocks.VOID_AIR.defaultBlockState();
        }
    }

    @Override
    public void setBlockEntity(BlockEntity blockEntity0) {
        this.f_187610_.put(blockEntity0.getBlockPos(), blockEntity0);
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos blockPos0) {
        return (BlockEntity) this.f_187610_.get(blockPos0);
    }

    public Map<BlockPos, BlockEntity> getBlockEntities() {
        return this.f_187610_;
    }

    public void addEntity(CompoundTag compoundTag0) {
        this.entities.add(compoundTag0);
    }

    @Override
    public void addEntity(Entity entity0) {
        if (!entity0.isPassenger()) {
            CompoundTag $$1 = new CompoundTag();
            entity0.save($$1);
            this.addEntity($$1);
        }
    }

    @Override
    public void setStartForStructure(Structure structure0, StructureStart structureStart1) {
        BelowZeroRetrogen $$2 = this.getBelowZeroRetrogen();
        if ($$2 != null && structureStart1.isValid()) {
            BoundingBox $$3 = structureStart1.getBoundingBox();
            LevelHeightAccessor $$4 = this.getHeightAccessorForGeneration();
            if ($$3.minY() < $$4.getMinBuildHeight() || $$3.maxY() >= $$4.getMaxBuildHeight()) {
                return;
            }
        }
        super.setStartForStructure(structure0, structureStart1);
    }

    public List<CompoundTag> getEntities() {
        return this.entities;
    }

    @Override
    public ChunkStatus getStatus() {
        return this.status;
    }

    public void setStatus(ChunkStatus chunkStatus0) {
        this.status = chunkStatus0;
        if (this.belowZeroRetrogen != null && chunkStatus0.isOrAfter(this.belowZeroRetrogen.targetStatus())) {
            this.setBelowZeroRetrogen(null);
        }
        this.m_8092_(true);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int int0, int int1, int int2) {
        if (this.m_284331_().isOrAfter(ChunkStatus.BIOMES)) {
            return super.getNoiseBiome(int0, int1, int2);
        } else {
            throw new IllegalStateException("Asking for biomes before we have biomes");
        }
    }

    public static short packOffsetCoordinates(BlockPos blockPos0) {
        int $$1 = blockPos0.m_123341_();
        int $$2 = blockPos0.m_123342_();
        int $$3 = blockPos0.m_123343_();
        int $$4 = $$1 & 15;
        int $$5 = $$2 & 15;
        int $$6 = $$3 & 15;
        return (short) ($$4 | $$5 << 4 | $$6 << 8);
    }

    public static BlockPos unpackOffsetCoordinates(short short0, int int1, ChunkPos chunkPos2) {
        int $$3 = SectionPos.sectionToBlockCoord(chunkPos2.x, short0 & 15);
        int $$4 = SectionPos.sectionToBlockCoord(int1, short0 >>> 4 & 15);
        int $$5 = SectionPos.sectionToBlockCoord(chunkPos2.z, short0 >>> 8 & 15);
        return new BlockPos($$3, $$4, $$5);
    }

    @Override
    public void markPosForPostprocessing(BlockPos blockPos0) {
        if (!this.m_151570_(blockPos0)) {
            ChunkAccess.getOrCreateOffsetList(this.f_187602_, this.m_151564_(blockPos0.m_123342_())).add(packOffsetCoordinates(blockPos0));
        }
    }

    @Override
    public void addPackedPostProcess(short short0, int int1) {
        ChunkAccess.getOrCreateOffsetList(this.f_187602_, int1).add(short0);
    }

    public Map<BlockPos, CompoundTag> getBlockEntityNbts() {
        return Collections.unmodifiableMap(this.f_187609_);
    }

    @Nullable
    @Override
    public CompoundTag getBlockEntityNbtForSaving(BlockPos blockPos0) {
        BlockEntity $$1 = this.getBlockEntity(blockPos0);
        return $$1 != null ? $$1.saveWithFullMetadata() : (CompoundTag) this.f_187609_.get(blockPos0);
    }

    @Override
    public void removeBlockEntity(BlockPos blockPos0) {
        this.f_187610_.remove(blockPos0);
        this.f_187609_.remove(blockPos0);
    }

    @Nullable
    public CarvingMask getCarvingMask(GenerationStep.Carving generationStepCarving0) {
        return (CarvingMask) this.carvingMasks.get(generationStepCarving0);
    }

    public CarvingMask getOrCreateCarvingMask(GenerationStep.Carving generationStepCarving0) {
        return (CarvingMask) this.carvingMasks.computeIfAbsent(generationStepCarving0, p_289528_ -> new CarvingMask(this.m_141928_(), this.m_141937_()));
    }

    public void setCarvingMask(GenerationStep.Carving generationStepCarving0, CarvingMask carvingMask1) {
        this.carvingMasks.put(generationStepCarving0, carvingMask1);
    }

    public void setLightEngine(LevelLightEngine levelLightEngine0) {
        this.lightEngine = levelLightEngine0;
    }

    public void setBelowZeroRetrogen(@Nullable BelowZeroRetrogen belowZeroRetrogen0) {
        this.belowZeroRetrogen = belowZeroRetrogen0;
    }

    @Nullable
    @Override
    public BelowZeroRetrogen getBelowZeroRetrogen() {
        return this.belowZeroRetrogen;
    }

    private static <T> LevelChunkTicks<T> unpackTicks(ProtoChunkTicks<T> protoChunkTicksT0) {
        return new LevelChunkTicks<>(protoChunkTicksT0.scheduledTicks());
    }

    public LevelChunkTicks<Block> unpackBlockTicks() {
        return unpackTicks(this.blockTicks);
    }

    public LevelChunkTicks<Fluid> unpackFluidTicks() {
        return unpackTicks(this.fluidTicks);
    }

    @Override
    public LevelHeightAccessor getHeightAccessorForGeneration() {
        return (LevelHeightAccessor) (this.m_187679_() ? BelowZeroRetrogen.UPGRADE_HEIGHT_ACCESSOR : this);
    }
}