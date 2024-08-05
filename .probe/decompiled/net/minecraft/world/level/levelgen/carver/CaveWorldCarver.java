package net.minecraft.world.level.levelgen.carver;

import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;

public class CaveWorldCarver extends WorldCarver<CaveCarverConfiguration> {

    public CaveWorldCarver(Codec<CaveCarverConfiguration> codecCaveCarverConfiguration0) {
        super(codecCaveCarverConfiguration0);
    }

    public boolean isStartChunk(CaveCarverConfiguration caveCarverConfiguration0, RandomSource randomSource1) {
        return randomSource1.nextFloat() <= caveCarverConfiguration0.f_67859_;
    }

    public boolean carve(CarvingContext carvingContext0, CaveCarverConfiguration caveCarverConfiguration1, ChunkAccess chunkAccess2, Function<BlockPos, Holder<Biome>> functionBlockPosHolderBiome3, RandomSource randomSource4, Aquifer aquifer5, ChunkPos chunkPos6, CarvingMask carvingMask7) {
        int $$8 = SectionPos.sectionToBlockCoord(this.m_65073_() * 2 - 1);
        int $$9 = randomSource4.nextInt(randomSource4.nextInt(randomSource4.nextInt(this.getCaveBound()) + 1) + 1);
        for (int $$10 = 0; $$10 < $$9; $$10++) {
            double $$11 = (double) chunkPos6.getBlockX(randomSource4.nextInt(16));
            double $$12 = (double) caveCarverConfiguration1.f_159088_.sample(randomSource4, carvingContext0);
            double $$13 = (double) chunkPos6.getBlockZ(randomSource4.nextInt(16));
            double $$14 = (double) caveCarverConfiguration1.horizontalRadiusMultiplier.m_214084_(randomSource4);
            double $$15 = (double) caveCarverConfiguration1.verticalRadiusMultiplier.m_214084_(randomSource4);
            double $$16 = (double) caveCarverConfiguration1.floorLevel.m_214084_(randomSource4);
            WorldCarver.CarveSkipChecker $$17 = (p_159202_, p_159203_, p_159204_, p_159205_, p_159206_) -> shouldSkip(p_159203_, p_159204_, p_159205_, $$16);
            int $$18 = 1;
            if (randomSource4.nextInt(4) == 0) {
                double $$19 = (double) caveCarverConfiguration1.f_159089_.m_214084_(randomSource4);
                float $$20 = 1.0F + randomSource4.nextFloat() * 6.0F;
                this.createRoom(carvingContext0, caveCarverConfiguration1, chunkAccess2, functionBlockPosHolderBiome3, aquifer5, $$11, $$12, $$13, $$20, $$19, carvingMask7, $$17);
                $$18 += randomSource4.nextInt(4);
            }
            for (int $$21 = 0; $$21 < $$18; $$21++) {
                float $$22 = randomSource4.nextFloat() * (float) (Math.PI * 2);
                float $$23 = (randomSource4.nextFloat() - 0.5F) / 4.0F;
                float $$24 = this.getThickness(randomSource4);
                int $$25 = $$8 - randomSource4.nextInt($$8 / 4);
                int $$26 = 0;
                this.createTunnel(carvingContext0, caveCarverConfiguration1, chunkAccess2, functionBlockPosHolderBiome3, randomSource4.nextLong(), aquifer5, $$11, $$12, $$13, $$14, $$15, $$24, $$22, $$23, 0, $$25, this.getYScale(), carvingMask7, $$17);
            }
        }
        return true;
    }

    protected int getCaveBound() {
        return 15;
    }

    protected float getThickness(RandomSource randomSource0) {
        float $$1 = randomSource0.nextFloat() * 2.0F + randomSource0.nextFloat();
        if (randomSource0.nextInt(10) == 0) {
            $$1 *= randomSource0.nextFloat() * randomSource0.nextFloat() * 3.0F + 1.0F;
        }
        return $$1;
    }

    protected double getYScale() {
        return 1.0;
    }

    protected void createRoom(CarvingContext carvingContext0, CaveCarverConfiguration caveCarverConfiguration1, ChunkAccess chunkAccess2, Function<BlockPos, Holder<Biome>> functionBlockPosHolderBiome3, Aquifer aquifer4, double double5, double double6, double double7, float float8, double double9, CarvingMask carvingMask10, WorldCarver.CarveSkipChecker worldCarverCarveSkipChecker11) {
        double $$12 = 1.5 + (double) (Mth.sin((float) (Math.PI / 2)) * float8);
        double $$13 = $$12 * double9;
        this.m_190753_(carvingContext0, caveCarverConfiguration1, chunkAccess2, functionBlockPosHolderBiome3, aquifer4, double5 + 1.0, double6, double7, $$12, $$13, carvingMask10, worldCarverCarveSkipChecker11);
    }

    protected void createTunnel(CarvingContext carvingContext0, CaveCarverConfiguration caveCarverConfiguration1, ChunkAccess chunkAccess2, Function<BlockPos, Holder<Biome>> functionBlockPosHolderBiome3, long long4, Aquifer aquifer5, double double6, double double7, double double8, double double9, double double10, float float11, float float12, float float13, int int14, int int15, double double16, CarvingMask carvingMask17, WorldCarver.CarveSkipChecker worldCarverCarveSkipChecker18) {
        RandomSource $$19 = RandomSource.create(long4);
        int $$20 = $$19.nextInt(int15 / 2) + int15 / 4;
        boolean $$21 = $$19.nextInt(6) == 0;
        float $$22 = 0.0F;
        float $$23 = 0.0F;
        for (int $$24 = int14; $$24 < int15; $$24++) {
            double $$25 = 1.5 + (double) (Mth.sin((float) Math.PI * (float) $$24 / (float) int15) * float11);
            double $$26 = $$25 * double16;
            float $$27 = Mth.cos(float13);
            double6 += (double) (Mth.cos(float12) * $$27);
            double7 += (double) Mth.sin(float13);
            double8 += (double) (Mth.sin(float12) * $$27);
            float13 *= $$21 ? 0.92F : 0.7F;
            float13 += $$23 * 0.1F;
            float12 += $$22 * 0.1F;
            $$23 *= 0.9F;
            $$22 *= 0.75F;
            $$23 += ($$19.nextFloat() - $$19.nextFloat()) * $$19.nextFloat() * 2.0F;
            $$22 += ($$19.nextFloat() - $$19.nextFloat()) * $$19.nextFloat() * 4.0F;
            if ($$24 == $$20 && float11 > 1.0F) {
                this.createTunnel(carvingContext0, caveCarverConfiguration1, chunkAccess2, functionBlockPosHolderBiome3, $$19.nextLong(), aquifer5, double6, double7, double8, double9, double10, $$19.nextFloat() * 0.5F + 0.5F, float12 - (float) (Math.PI / 2), float13 / 3.0F, $$24, int15, 1.0, carvingMask17, worldCarverCarveSkipChecker18);
                this.createTunnel(carvingContext0, caveCarverConfiguration1, chunkAccess2, functionBlockPosHolderBiome3, $$19.nextLong(), aquifer5, double6, double7, double8, double9, double10, $$19.nextFloat() * 0.5F + 0.5F, float12 + (float) (Math.PI / 2), float13 / 3.0F, $$24, int15, 1.0, carvingMask17, worldCarverCarveSkipChecker18);
                return;
            }
            if ($$19.nextInt(4) != 0) {
                if (!m_159367_(chunkAccess2.getPos(), double6, double8, $$24, int15, float11)) {
                    return;
                }
                this.m_190753_(carvingContext0, caveCarverConfiguration1, chunkAccess2, functionBlockPosHolderBiome3, aquifer5, double6, double7, double8, $$25 * double9, $$26 * double10, carvingMask17, worldCarverCarveSkipChecker18);
            }
        }
    }

    private static boolean shouldSkip(double double0, double double1, double double2, double double3) {
        return double1 <= double3 ? true : double0 * double0 + double1 * double1 + double2 * double2 >= 1.0;
    }
}