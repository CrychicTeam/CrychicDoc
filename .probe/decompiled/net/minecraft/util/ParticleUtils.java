package net.minecraft.util;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParticleUtils {

    public static void spawnParticlesOnBlockFaces(Level level0, BlockPos blockPos1, ParticleOptions particleOptions2, IntProvider intProvider3) {
        for (Direction $$4 : Direction.values()) {
            spawnParticlesOnBlockFace(level0, blockPos1, particleOptions2, intProvider3, $$4, () -> getRandomSpeedRanges(level0.random), 0.55);
        }
    }

    public static void spawnParticlesOnBlockFace(Level level0, BlockPos blockPos1, ParticleOptions particleOptions2, IntProvider intProvider3, Direction direction4, Supplier<Vec3> supplierVec5, double double6) {
        int $$7 = intProvider3.sample(level0.random);
        for (int $$8 = 0; $$8 < $$7; $$8++) {
            spawnParticleOnFace(level0, blockPos1, direction4, particleOptions2, (Vec3) supplierVec5.get(), double6);
        }
    }

    private static Vec3 getRandomSpeedRanges(RandomSource randomSource0) {
        return new Vec3(Mth.nextDouble(randomSource0, -0.5, 0.5), Mth.nextDouble(randomSource0, -0.5, 0.5), Mth.nextDouble(randomSource0, -0.5, 0.5));
    }

    public static void spawnParticlesAlongAxis(Direction.Axis directionAxis0, Level level1, BlockPos blockPos2, double double3, ParticleOptions particleOptions4, UniformInt uniformInt5) {
        Vec3 $$6 = Vec3.atCenterOf(blockPos2);
        boolean $$7 = directionAxis0 == Direction.Axis.X;
        boolean $$8 = directionAxis0 == Direction.Axis.Y;
        boolean $$9 = directionAxis0 == Direction.Axis.Z;
        int $$10 = uniformInt5.sample(level1.random);
        for (int $$11 = 0; $$11 < $$10; $$11++) {
            double $$12 = $$6.x + Mth.nextDouble(level1.random, -1.0, 1.0) * ($$7 ? 0.5 : double3);
            double $$13 = $$6.y + Mth.nextDouble(level1.random, -1.0, 1.0) * ($$8 ? 0.5 : double3);
            double $$14 = $$6.z + Mth.nextDouble(level1.random, -1.0, 1.0) * ($$9 ? 0.5 : double3);
            double $$15 = $$7 ? Mth.nextDouble(level1.random, -1.0, 1.0) : 0.0;
            double $$16 = $$8 ? Mth.nextDouble(level1.random, -1.0, 1.0) : 0.0;
            double $$17 = $$9 ? Mth.nextDouble(level1.random, -1.0, 1.0) : 0.0;
            level1.addParticle(particleOptions4, $$12, $$13, $$14, $$15, $$16, $$17);
        }
    }

    public static void spawnParticleOnFace(Level level0, BlockPos blockPos1, Direction direction2, ParticleOptions particleOptions3, Vec3 vec4, double double5) {
        Vec3 $$6 = Vec3.atCenterOf(blockPos1);
        int $$7 = direction2.getStepX();
        int $$8 = direction2.getStepY();
        int $$9 = direction2.getStepZ();
        double $$10 = $$6.x + ($$7 == 0 ? Mth.nextDouble(level0.random, -0.5, 0.5) : (double) $$7 * double5);
        double $$11 = $$6.y + ($$8 == 0 ? Mth.nextDouble(level0.random, -0.5, 0.5) : (double) $$8 * double5);
        double $$12 = $$6.z + ($$9 == 0 ? Mth.nextDouble(level0.random, -0.5, 0.5) : (double) $$9 * double5);
        double $$13 = $$7 == 0 ? vec4.x() : 0.0;
        double $$14 = $$8 == 0 ? vec4.y() : 0.0;
        double $$15 = $$9 == 0 ? vec4.z() : 0.0;
        level0.addParticle(particleOptions3, $$10, $$11, $$12, $$13, $$14, $$15);
    }

    public static void spawnParticleBelow(Level level0, BlockPos blockPos1, RandomSource randomSource2, ParticleOptions particleOptions3) {
        double $$4 = (double) blockPos1.m_123341_() + randomSource2.nextDouble();
        double $$5 = (double) blockPos1.m_123342_() - 0.05;
        double $$6 = (double) blockPos1.m_123343_() + randomSource2.nextDouble();
        level0.addParticle(particleOptions3, $$4, $$5, $$6, 0.0, 0.0, 0.0);
    }
}