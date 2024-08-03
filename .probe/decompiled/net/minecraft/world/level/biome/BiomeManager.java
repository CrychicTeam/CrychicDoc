package net.minecraft.world.level.biome;

import com.google.common.hash.Hashing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.util.Mth;

public class BiomeManager {

    public static final int CHUNK_CENTER_QUART = QuartPos.fromBlock(8);

    private static final int ZOOM_BITS = 2;

    private static final int ZOOM = 4;

    private static final int ZOOM_MASK = 3;

    private final BiomeManager.NoiseBiomeSource noiseBiomeSource;

    private final long biomeZoomSeed;

    public BiomeManager(BiomeManager.NoiseBiomeSource biomeManagerNoiseBiomeSource0, long long1) {
        this.noiseBiomeSource = biomeManagerNoiseBiomeSource0;
        this.biomeZoomSeed = long1;
    }

    public static long obfuscateSeed(long long0) {
        return Hashing.sha256().hashLong(long0).asLong();
    }

    public BiomeManager withDifferentSource(BiomeManager.NoiseBiomeSource biomeManagerNoiseBiomeSource0) {
        return new BiomeManager(biomeManagerNoiseBiomeSource0, this.biomeZoomSeed);
    }

    public Holder<Biome> getBiome(BlockPos blockPos0) {
        int $$1 = blockPos0.m_123341_() - 2;
        int $$2 = blockPos0.m_123342_() - 2;
        int $$3 = blockPos0.m_123343_() - 2;
        int $$4 = $$1 >> 2;
        int $$5 = $$2 >> 2;
        int $$6 = $$3 >> 2;
        double $$7 = (double) ($$1 & 3) / 4.0;
        double $$8 = (double) ($$2 & 3) / 4.0;
        double $$9 = (double) ($$3 & 3) / 4.0;
        int $$10 = 0;
        double $$11 = Double.POSITIVE_INFINITY;
        for (int $$12 = 0; $$12 < 8; $$12++) {
            boolean $$13 = ($$12 & 4) == 0;
            boolean $$14 = ($$12 & 2) == 0;
            boolean $$15 = ($$12 & 1) == 0;
            int $$16 = $$13 ? $$4 : $$4 + 1;
            int $$17 = $$14 ? $$5 : $$5 + 1;
            int $$18 = $$15 ? $$6 : $$6 + 1;
            double $$19 = $$13 ? $$7 : $$7 - 1.0;
            double $$20 = $$14 ? $$8 : $$8 - 1.0;
            double $$21 = $$15 ? $$9 : $$9 - 1.0;
            double $$22 = getFiddledDistance(this.biomeZoomSeed, $$16, $$17, $$18, $$19, $$20, $$21);
            if ($$11 > $$22) {
                $$10 = $$12;
                $$11 = $$22;
            }
        }
        int $$23 = ($$10 & 4) == 0 ? $$4 : $$4 + 1;
        int $$24 = ($$10 & 2) == 0 ? $$5 : $$5 + 1;
        int $$25 = ($$10 & 1) == 0 ? $$6 : $$6 + 1;
        return this.noiseBiomeSource.getNoiseBiome($$23, $$24, $$25);
    }

    public Holder<Biome> getNoiseBiomeAtPosition(double double0, double double1, double double2) {
        int $$3 = QuartPos.fromBlock(Mth.floor(double0));
        int $$4 = QuartPos.fromBlock(Mth.floor(double1));
        int $$5 = QuartPos.fromBlock(Mth.floor(double2));
        return this.getNoiseBiomeAtQuart($$3, $$4, $$5);
    }

    public Holder<Biome> getNoiseBiomeAtPosition(BlockPos blockPos0) {
        int $$1 = QuartPos.fromBlock(blockPos0.m_123341_());
        int $$2 = QuartPos.fromBlock(blockPos0.m_123342_());
        int $$3 = QuartPos.fromBlock(blockPos0.m_123343_());
        return this.getNoiseBiomeAtQuart($$1, $$2, $$3);
    }

    public Holder<Biome> getNoiseBiomeAtQuart(int int0, int int1, int int2) {
        return this.noiseBiomeSource.getNoiseBiome(int0, int1, int2);
    }

    private static double getFiddledDistance(long long0, int int1, int int2, int int3, double double4, double double5, double double6) {
        long $$7 = LinearCongruentialGenerator.next(long0, (long) int1);
        $$7 = LinearCongruentialGenerator.next($$7, (long) int2);
        $$7 = LinearCongruentialGenerator.next($$7, (long) int3);
        $$7 = LinearCongruentialGenerator.next($$7, (long) int1);
        $$7 = LinearCongruentialGenerator.next($$7, (long) int2);
        $$7 = LinearCongruentialGenerator.next($$7, (long) int3);
        double $$8 = getFiddle($$7);
        $$7 = LinearCongruentialGenerator.next($$7, long0);
        double $$9 = getFiddle($$7);
        $$7 = LinearCongruentialGenerator.next($$7, long0);
        double $$10 = getFiddle($$7);
        return Mth.square(double6 + $$10) + Mth.square(double5 + $$9) + Mth.square(double4 + $$8);
    }

    private static double getFiddle(long long0) {
        double $$1 = (double) Math.floorMod(long0 >> 24, 1024) / 1024.0;
        return ($$1 - 0.5) * 0.9;
    }

    public interface NoiseBiomeSource {

        Holder<Biome> getNoiseBiome(int var1, int var2, int var3);
    }
}