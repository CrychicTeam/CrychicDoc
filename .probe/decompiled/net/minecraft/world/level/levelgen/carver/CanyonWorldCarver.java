package net.minecraft.world.level.levelgen.carver;

import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;

public class CanyonWorldCarver extends WorldCarver<CanyonCarverConfiguration> {

    public CanyonWorldCarver(Codec<CanyonCarverConfiguration> codecCanyonCarverConfiguration0) {
        super(codecCanyonCarverConfiguration0);
    }

    public boolean isStartChunk(CanyonCarverConfiguration canyonCarverConfiguration0, RandomSource randomSource1) {
        return randomSource1.nextFloat() <= canyonCarverConfiguration0.f_67859_;
    }

    public boolean carve(CarvingContext carvingContext0, CanyonCarverConfiguration canyonCarverConfiguration1, ChunkAccess chunkAccess2, Function<BlockPos, Holder<Biome>> functionBlockPosHolderBiome3, RandomSource randomSource4, Aquifer aquifer5, ChunkPos chunkPos6, CarvingMask carvingMask7) {
        int $$8 = (this.m_65073_() * 2 - 1) * 16;
        double $$9 = (double) chunkPos6.getBlockX(randomSource4.nextInt(16));
        int $$10 = canyonCarverConfiguration1.f_159088_.sample(randomSource4, carvingContext0);
        double $$11 = (double) chunkPos6.getBlockZ(randomSource4.nextInt(16));
        float $$12 = randomSource4.nextFloat() * (float) (Math.PI * 2);
        float $$13 = canyonCarverConfiguration1.verticalRotation.m_214084_(randomSource4);
        double $$14 = (double) canyonCarverConfiguration1.f_159089_.m_214084_(randomSource4);
        float $$15 = canyonCarverConfiguration1.shape.thickness.m_214084_(randomSource4);
        int $$16 = (int) ((float) $$8 * canyonCarverConfiguration1.shape.distanceFactor.m_214084_(randomSource4));
        int $$17 = 0;
        this.doCarve(carvingContext0, canyonCarverConfiguration1, chunkAccess2, functionBlockPosHolderBiome3, randomSource4.nextLong(), aquifer5, $$9, (double) $$10, $$11, $$15, $$12, $$13, 0, $$16, $$14, carvingMask7);
        return true;
    }

    private void doCarve(CarvingContext carvingContext0, CanyonCarverConfiguration canyonCarverConfiguration1, ChunkAccess chunkAccess2, Function<BlockPos, Holder<Biome>> functionBlockPosHolderBiome3, long long4, Aquifer aquifer5, double double6, double double7, double double8, float float9, float float10, float float11, int int12, int int13, double double14, CarvingMask carvingMask15) {
        RandomSource $$16 = RandomSource.create(long4);
        float[] $$17 = this.initWidthFactors(carvingContext0, canyonCarverConfiguration1, $$16);
        float $$18 = 0.0F;
        float $$19 = 0.0F;
        for (int $$20 = int12; $$20 < int13; $$20++) {
            double $$21 = 1.5 + (double) (Mth.sin((float) $$20 * (float) Math.PI / (float) int13) * float9);
            double $$22 = $$21 * double14;
            $$21 *= (double) canyonCarverConfiguration1.shape.horizontalRadiusFactor.m_214084_($$16);
            $$22 = this.updateVerticalRadius(canyonCarverConfiguration1, $$16, $$22, (float) int13, (float) $$20);
            float $$23 = Mth.cos(float11);
            float $$24 = Mth.sin(float11);
            double6 += (double) (Mth.cos(float10) * $$23);
            double7 += (double) $$24;
            double8 += (double) (Mth.sin(float10) * $$23);
            float11 *= 0.7F;
            float11 += $$19 * 0.05F;
            float10 += $$18 * 0.05F;
            $$19 *= 0.8F;
            $$18 *= 0.5F;
            $$19 += ($$16.nextFloat() - $$16.nextFloat()) * $$16.nextFloat() * 2.0F;
            $$18 += ($$16.nextFloat() - $$16.nextFloat()) * $$16.nextFloat() * 4.0F;
            if ($$16.nextInt(4) != 0) {
                if (!m_159367_(chunkAccess2.getPos(), double6, double8, $$20, int13, float9)) {
                    return;
                }
                this.m_190753_(carvingContext0, canyonCarverConfiguration1, chunkAccess2, functionBlockPosHolderBiome3, aquifer5, double6, double7, double8, $$21, $$22, carvingMask15, (p_159082_, p_159083_, p_159084_, p_159085_, p_159086_) -> this.shouldSkip(p_159082_, $$17, p_159083_, p_159084_, p_159085_, p_159086_));
            }
        }
    }

    private float[] initWidthFactors(CarvingContext carvingContext0, CanyonCarverConfiguration canyonCarverConfiguration1, RandomSource randomSource2) {
        int $$3 = carvingContext0.m_142208_();
        float[] $$4 = new float[$$3];
        float $$5 = 1.0F;
        for (int $$6 = 0; $$6 < $$3; $$6++) {
            if ($$6 == 0 || randomSource2.nextInt(canyonCarverConfiguration1.shape.widthSmoothness) == 0) {
                $$5 = 1.0F + randomSource2.nextFloat() * randomSource2.nextFloat();
            }
            $$4[$$6] = $$5 * $$5;
        }
        return $$4;
    }

    private double updateVerticalRadius(CanyonCarverConfiguration canyonCarverConfiguration0, RandomSource randomSource1, double double2, float float3, float float4) {
        float $$5 = 1.0F - Mth.abs(0.5F - float4 / float3) * 2.0F;
        float $$6 = canyonCarverConfiguration0.shape.verticalRadiusDefaultFactor + canyonCarverConfiguration0.shape.verticalRadiusCenterFactor * $$5;
        return (double) $$6 * double2 * (double) Mth.randomBetween(randomSource1, 0.75F, 1.0F);
    }

    private boolean shouldSkip(CarvingContext carvingContext0, float[] float1, double double2, double double3, double double4, int int5) {
        int $$6 = int5 - carvingContext0.m_142201_();
        return (double2 * double2 + double4 * double4) * (double) float1[$$6 - 1] + double3 * double3 / 6.0 >= 1.0;
    }
}