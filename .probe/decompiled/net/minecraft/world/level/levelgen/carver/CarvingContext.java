package net.minecraft.world.level.levelgen.carver;

import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.WorldGenerationContext;

public class CarvingContext extends WorldGenerationContext {

    private final RegistryAccess registryAccess;

    private final NoiseChunk noiseChunk;

    private final RandomState randomState;

    private final SurfaceRules.RuleSource surfaceRule;

    public CarvingContext(NoiseBasedChunkGenerator noiseBasedChunkGenerator0, RegistryAccess registryAccess1, LevelHeightAccessor levelHeightAccessor2, NoiseChunk noiseChunk3, RandomState randomState4, SurfaceRules.RuleSource surfaceRulesRuleSource5) {
        super(noiseBasedChunkGenerator0, levelHeightAccessor2);
        this.registryAccess = registryAccess1;
        this.noiseChunk = noiseChunk3;
        this.randomState = randomState4;
        this.surfaceRule = surfaceRulesRuleSource5;
    }

    @Deprecated
    public Optional<BlockState> topMaterial(Function<BlockPos, Holder<Biome>> functionBlockPosHolderBiome0, ChunkAccess chunkAccess1, BlockPos blockPos2, boolean boolean3) {
        return this.randomState.surfaceSystem().topMaterial(this.surfaceRule, this, functionBlockPosHolderBiome0, chunkAccess1, this.noiseChunk, blockPos2, boolean3);
    }

    @Deprecated
    public RegistryAccess registryAccess() {
        return this.registryAccess;
    }

    public RandomState randomState() {
        return this.randomState;
    }
}