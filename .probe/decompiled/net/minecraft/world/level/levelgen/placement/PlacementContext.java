package net.minecraft.world.level.levelgen.placement;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;

public class PlacementContext extends WorldGenerationContext {

    private final WorldGenLevel level;

    private final ChunkGenerator generator;

    private final Optional<PlacedFeature> topFeature;

    public PlacementContext(WorldGenLevel worldGenLevel0, ChunkGenerator chunkGenerator1, Optional<PlacedFeature> optionalPlacedFeature2) {
        super(chunkGenerator1, worldGenLevel0);
        this.level = worldGenLevel0;
        this.generator = chunkGenerator1;
        this.topFeature = optionalPlacedFeature2;
    }

    public int getHeight(Heightmap.Types heightmapTypes0, int int1, int int2) {
        return this.level.m_6924_(heightmapTypes0, int1, int2);
    }

    public CarvingMask getCarvingMask(ChunkPos chunkPos0, GenerationStep.Carving generationStepCarving1) {
        return ((ProtoChunk) this.level.m_6325_(chunkPos0.x, chunkPos0.z)).getOrCreateCarvingMask(generationStepCarving1);
    }

    public BlockState getBlockState(BlockPos blockPos0) {
        return this.level.m_8055_(blockPos0);
    }

    public int getMinBuildHeight() {
        return this.level.m_141937_();
    }

    public WorldGenLevel getLevel() {
        return this.level;
    }

    public Optional<PlacedFeature> topFeature() {
        return this.topFeature;
    }

    public ChunkGenerator generator() {
        return this.generator;
    }
}