package com.github.alexmodguy.alexscaves.server.level.carver;

import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class WaterBubbleCarver extends CaveWorldCarver {

    public WaterBubbleCarver(Codec<CaveCarverConfiguration> codecCaveCarverConfiguration0) {
        super(codecCaveCarverConfiguration0);
    }

    @Override
    protected int getCaveBound() {
        return 15;
    }

    @Override
    protected float getThickness(RandomSource randomSource) {
        return randomSource.nextFloat() * 4.0F + 2.0F;
    }

    @Override
    protected double getYScale() {
        return 1.0;
    }

    @Override
    public boolean carve(CarvingContext carvingContext0, CaveCarverConfiguration caveCarverConfiguration1, ChunkAccess chunkAccess2, Function<BlockPos, Holder<Biome>> functionBlockPosHolderBiome3, RandomSource randomSource4, Aquifer aquifer5, ChunkPos chunkPos6, CarvingMask carvingMask7) {
        int i = SectionPos.sectionToBlockCoord(this.m_65073_() * 2 - 1);
        int j = randomSource4.nextInt(randomSource4.nextInt(randomSource4.nextInt(this.getCaveBound()) + 1) + 1);
        for (int k = 0; k < j; k++) {
            double d0 = (double) chunkPos6.getBlockX(randomSource4.nextInt(16));
            double d1 = (double) caveCarverConfiguration1.f_159088_.sample(randomSource4, carvingContext0);
            double d2 = (double) chunkPos6.getBlockZ(randomSource4.nextInt(16));
            double d3 = (double) caveCarverConfiguration1.horizontalRadiusMultiplier.m_214084_(randomSource4);
            double d4 = (double) caveCarverConfiguration1.verticalRadiusMultiplier.m_214084_(randomSource4);
            double d5 = (double) (-0.4F - randomSource4.nextFloat() * 0.6F);
            WorldCarver.CarveSkipChecker worldcarver$carveskipchecker = (p_159202_, p_159203_, p_159204_, p_159205_, p_159206_) -> shouldSkip(p_159203_, p_159204_, p_159205_, d5);
            double d6 = (double) caveCarverConfiguration1.f_159089_.m_214084_(randomSource4);
            float f1 = 3.0F;
            this.m_190690_(carvingContext0, caveCarverConfiguration1, chunkAccess2, functionBlockPosHolderBiome3, aquifer5, d0, d1, d2, f1, d6, carvingMask7, worldcarver$carveskipchecker);
        }
        return true;
    }

    private static boolean shouldSkip(double double0, double double1, double double2, double double3) {
        return double1 <= double3 ? true : double0 * double0 + double1 * double1 + double2 * double2 >= 1.0;
    }

    protected boolean carveBlock(CarvingContext context, CaveCarverConfiguration carverConfiguration, ChunkAccess access, Function<BlockPos, Holder<Biome>> biomeGetter, CarvingMask mask, BlockPos.MutableBlockPos mutableBlockPos, BlockPos.MutableBlockPos blockPos, Aquifer aquifer, MutableBoolean mutableBoolean) {
        if (this.m_224910_(carverConfiguration, access.m_8055_(mutableBlockPos))) {
            BlockState blockstate = f_64981_.createLegacyBlock();
            access.setBlockState(mutableBlockPos, blockstate, false);
            return true;
        } else {
            return false;
        }
    }
}