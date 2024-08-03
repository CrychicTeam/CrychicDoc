package org.violetmoon.zeta.world.generator;

import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import org.violetmoon.zeta.config.type.DimensionConfig;
import org.violetmoon.zeta.util.BooleanSuppliers;

public abstract class Generator {

    public final DimensionConfig dimConfig;

    private final BooleanSupplier condition;

    public Generator(DimensionConfig dimConfig) {
        this(dimConfig, BooleanSuppliers.TRUE);
    }

    public Generator(DimensionConfig dimConfig, BooleanSupplier condition) {
        this.dimConfig = dimConfig;
        this.condition = condition;
    }

    public final int generate(int seedIncrement, long seed, GenerationStep.Decoration stage, WorldGenRegion worldIn, ChunkGenerator generator, WorldgenRandom rand, BlockPos pos) {
        rand.setFeatureSeed(seed, seedIncrement, stage.ordinal());
        this.generateChunk(worldIn, generator, rand, pos);
        return seedIncrement + 1;
    }

    public abstract void generateChunk(WorldGenRegion var1, ChunkGenerator var2, RandomSource var3, BlockPos var4);

    public boolean canGenerate(ServerLevelAccessor world) {
        return this.condition.getAsBoolean() && this.dimConfig.canSpawnHere(world.getLevel());
    }

    public Holder<Biome> getBiome(LevelAccessor world, BlockPos pos, boolean offset) {
        BlockPos testPos = offset ? new BlockPos(pos.m_123341_(), world.m_151558_() - 1, pos.m_123343_()) : pos;
        return world.m_7062_().getBiome(testPos);
    }

    protected boolean isNether(LevelAccessor world) {
        return world.m_6042_().ultraWarm();
    }
}