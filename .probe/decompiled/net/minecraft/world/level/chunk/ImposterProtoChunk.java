package net.minecraft.world.level.chunk;

import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.lighting.ChunkSkyLightSources;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.ticks.BlackholeTickAccess;
import net.minecraft.world.ticks.TickContainerAccess;

public class ImposterProtoChunk extends ProtoChunk {

    private final LevelChunk wrapped;

    private final boolean allowWrites;

    public ImposterProtoChunk(LevelChunk levelChunk0, boolean boolean1) {
        super(levelChunk0.m_7697_(), UpgradeData.EMPTY, levelChunk0.f_187611_, levelChunk0.getLevel().registryAccess().registryOrThrow(Registries.BIOME), levelChunk0.m_183407_());
        this.wrapped = levelChunk0;
        this.allowWrites = boolean1;
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos blockPos0) {
        return this.wrapped.getBlockEntity(blockPos0);
    }

    @Override
    public BlockState getBlockState(BlockPos blockPos0) {
        return this.wrapped.getBlockState(blockPos0);
    }

    @Override
    public FluidState getFluidState(BlockPos blockPos0) {
        return this.wrapped.getFluidState(blockPos0);
    }

    @Override
    public int getMaxLightLevel() {
        return this.wrapped.m_7469_();
    }

    @Override
    public LevelChunkSection getSection(int int0) {
        return this.allowWrites ? this.wrapped.m_183278_(int0) : super.m_183278_(int0);
    }

    @Nullable
    @Override
    public BlockState setBlockState(BlockPos blockPos0, BlockState blockState1, boolean boolean2) {
        return this.allowWrites ? this.wrapped.setBlockState(blockPos0, blockState1, boolean2) : null;
    }

    @Override
    public void setBlockEntity(BlockEntity blockEntity0) {
        if (this.allowWrites) {
            this.wrapped.setBlockEntity(blockEntity0);
        }
    }

    @Override
    public void addEntity(Entity entity0) {
        if (this.allowWrites) {
            this.wrapped.addEntity(entity0);
        }
    }

    @Override
    public void setStatus(ChunkStatus chunkStatus0) {
        if (this.allowWrites) {
            super.setStatus(chunkStatus0);
        }
    }

    @Override
    public LevelChunkSection[] getSections() {
        return this.wrapped.m_7103_();
    }

    @Override
    public void setHeightmap(Heightmap.Types heightmapTypes0, long[] long1) {
    }

    private Heightmap.Types fixType(Heightmap.Types heightmapTypes0) {
        if (heightmapTypes0 == Heightmap.Types.WORLD_SURFACE_WG) {
            return Heightmap.Types.WORLD_SURFACE;
        } else {
            return heightmapTypes0 == Heightmap.Types.OCEAN_FLOOR_WG ? Heightmap.Types.OCEAN_FLOOR : heightmapTypes0;
        }
    }

    @Override
    public Heightmap getOrCreateHeightmapUnprimed(Heightmap.Types heightmapTypes0) {
        return this.wrapped.m_6005_(heightmapTypes0);
    }

    @Override
    public int getHeight(Heightmap.Types heightmapTypes0, int int1, int int2) {
        return this.wrapped.m_5885_(this.fixType(heightmapTypes0), int1, int2);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int int0, int int1, int int2) {
        return this.wrapped.m_203495_(int0, int1, int2);
    }

    @Override
    public ChunkPos getPos() {
        return this.wrapped.m_7697_();
    }

    @Nullable
    @Override
    public StructureStart getStartForStructure(Structure structure0) {
        return this.wrapped.m_213652_(structure0);
    }

    @Override
    public void setStartForStructure(Structure structure0, StructureStart structureStart1) {
    }

    @Override
    public Map<Structure, StructureStart> getAllStarts() {
        return this.wrapped.m_6633_();
    }

    @Override
    public void setAllStarts(Map<Structure, StructureStart> mapStructureStructureStart0) {
    }

    @Override
    public LongSet getReferencesForStructure(Structure structure0) {
        return this.wrapped.m_213649_(structure0);
    }

    @Override
    public void addReferenceForStructure(Structure structure0, long long1) {
    }

    @Override
    public Map<Structure, LongSet> getAllReferences() {
        return this.wrapped.m_62769_();
    }

    @Override
    public void setAllReferences(Map<Structure, LongSet> mapStructureLongSet0) {
    }

    @Override
    public void setUnsaved(boolean boolean0) {
        this.wrapped.m_8092_(boolean0);
    }

    @Override
    public boolean isUnsaved() {
        return false;
    }

    @Override
    public ChunkStatus getStatus() {
        return this.wrapped.getStatus();
    }

    @Override
    public void removeBlockEntity(BlockPos blockPos0) {
    }

    @Override
    public void markPosForPostprocessing(BlockPos blockPos0) {
    }

    @Override
    public void setBlockEntityNbt(CompoundTag compoundTag0) {
    }

    @Nullable
    @Override
    public CompoundTag getBlockEntityNbt(BlockPos blockPos0) {
        return this.wrapped.m_8049_(blockPos0);
    }

    @Nullable
    @Override
    public CompoundTag getBlockEntityNbtForSaving(BlockPos blockPos0) {
        return this.wrapped.getBlockEntityNbtForSaving(blockPos0);
    }

    @Override
    public void findBlocks(Predicate<BlockState> predicateBlockState0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1) {
        this.wrapped.m_284478_(predicateBlockState0, biConsumerBlockPosBlockState1);
    }

    @Override
    public TickContainerAccess<Block> getBlockTicks() {
        return this.allowWrites ? this.wrapped.getBlockTicks() : BlackholeTickAccess.emptyContainer();
    }

    @Override
    public TickContainerAccess<Fluid> getFluidTicks() {
        return this.allowWrites ? this.wrapped.getFluidTicks() : BlackholeTickAccess.emptyContainer();
    }

    @Override
    public ChunkAccess.TicksToSave getTicksForSerialization() {
        return this.wrapped.getTicksForSerialization();
    }

    @Nullable
    @Override
    public BlendingData getBlendingData() {
        return this.wrapped.m_183407_();
    }

    @Override
    public void setBlendingData(BlendingData blendingData0) {
        this.wrapped.m_183400_(blendingData0);
    }

    @Override
    public CarvingMask getCarvingMask(GenerationStep.Carving generationStepCarving0) {
        if (this.allowWrites) {
            return super.getCarvingMask(generationStepCarving0);
        } else {
            throw (UnsupportedOperationException) Util.pauseInIde(new UnsupportedOperationException("Meaningless in this context"));
        }
    }

    @Override
    public CarvingMask getOrCreateCarvingMask(GenerationStep.Carving generationStepCarving0) {
        if (this.allowWrites) {
            return super.getOrCreateCarvingMask(generationStepCarving0);
        } else {
            throw (UnsupportedOperationException) Util.pauseInIde(new UnsupportedOperationException("Meaningless in this context"));
        }
    }

    public LevelChunk getWrapped() {
        return this.wrapped;
    }

    @Override
    public boolean isLightCorrect() {
        return this.wrapped.m_6332_();
    }

    @Override
    public void setLightCorrect(boolean boolean0) {
        this.wrapped.m_8094_(boolean0);
    }

    @Override
    public void fillBiomesFromNoise(BiomeResolver biomeResolver0, Climate.Sampler climateSampler1) {
        if (this.allowWrites) {
            this.wrapped.m_183442_(biomeResolver0, climateSampler1);
        }
    }

    @Override
    public void initializeLightSources() {
        this.wrapped.m_284190_();
    }

    @Override
    public ChunkSkyLightSources getSkyLightSources() {
        return this.wrapped.m_284400_();
    }
}