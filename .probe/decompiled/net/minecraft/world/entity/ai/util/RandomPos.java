package net.minecraft.world.entity.ai.util;

import com.google.common.annotations.VisibleForTesting;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class RandomPos {

    private static final int RANDOM_POS_ATTEMPTS = 10;

    public static BlockPos generateRandomDirection(RandomSource randomSource0, int int1, int int2) {
        int $$3 = randomSource0.nextInt(2 * int1 + 1) - int1;
        int $$4 = randomSource0.nextInt(2 * int2 + 1) - int2;
        int $$5 = randomSource0.nextInt(2 * int1 + 1) - int1;
        return new BlockPos($$3, $$4, $$5);
    }

    @Nullable
    public static BlockPos generateRandomDirectionWithinRadians(RandomSource randomSource0, int int1, int int2, int int3, double double4, double double5, double double6) {
        double $$7 = Mth.atan2(double5, double4) - (float) (Math.PI / 2);
        double $$8 = $$7 + (double) (2.0F * randomSource0.nextFloat() - 1.0F) * double6;
        double $$9 = Math.sqrt(randomSource0.nextDouble()) * (double) Mth.SQRT_OF_TWO * (double) int1;
        double $$10 = -$$9 * Math.sin($$8);
        double $$11 = $$9 * Math.cos($$8);
        if (!(Math.abs($$10) > (double) int1) && !(Math.abs($$11) > (double) int1)) {
            int $$12 = randomSource0.nextInt(2 * int2 + 1) - int2 + int3;
            return BlockPos.containing($$10, (double) $$12, $$11);
        } else {
            return null;
        }
    }

    @VisibleForTesting
    public static BlockPos moveUpOutOfSolid(BlockPos blockPos0, int int1, Predicate<BlockPos> predicateBlockPos2) {
        if (!predicateBlockPos2.test(blockPos0)) {
            return blockPos0;
        } else {
            BlockPos $$3 = blockPos0.above();
            while ($$3.m_123342_() < int1 && predicateBlockPos2.test($$3)) {
                $$3 = $$3.above();
            }
            return $$3;
        }
    }

    @VisibleForTesting
    public static BlockPos moveUpToAboveSolid(BlockPos blockPos0, int int1, int int2, Predicate<BlockPos> predicateBlockPos3) {
        if (int1 < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + int1 + ", expected >= 0");
        } else if (!predicateBlockPos3.test(blockPos0)) {
            return blockPos0;
        } else {
            BlockPos $$4 = blockPos0.above();
            while ($$4.m_123342_() < int2 && predicateBlockPos3.test($$4)) {
                $$4 = $$4.above();
            }
            BlockPos $$5 = $$4;
            while ($$5.m_123342_() < int2 && $$5.m_123342_() - $$4.m_123342_() < int1) {
                BlockPos $$6 = $$5.above();
                if (predicateBlockPos3.test($$6)) {
                    break;
                }
                $$5 = $$6;
            }
            return $$5;
        }
    }

    @Nullable
    public static Vec3 generateRandomPos(PathfinderMob pathfinderMob0, Supplier<BlockPos> supplierBlockPos1) {
        return generateRandomPos(supplierBlockPos1, pathfinderMob0::m_21692_);
    }

    @Nullable
    public static Vec3 generateRandomPos(Supplier<BlockPos> supplierBlockPos0, ToDoubleFunction<BlockPos> toDoubleFunctionBlockPos1) {
        double $$2 = Double.NEGATIVE_INFINITY;
        BlockPos $$3 = null;
        for (int $$4 = 0; $$4 < 10; $$4++) {
            BlockPos $$5 = (BlockPos) supplierBlockPos0.get();
            if ($$5 != null) {
                double $$6 = toDoubleFunctionBlockPos1.applyAsDouble($$5);
                if ($$6 > $$2) {
                    $$2 = $$6;
                    $$3 = $$5;
                }
            }
        }
        return $$3 != null ? Vec3.atBottomCenterOf($$3) : null;
    }

    public static BlockPos generateRandomPosTowardDirection(PathfinderMob pathfinderMob0, int int1, RandomSource randomSource2, BlockPos blockPos3) {
        int $$4 = blockPos3.m_123341_();
        int $$5 = blockPos3.m_123343_();
        if (pathfinderMob0.m_21536_() && int1 > 1) {
            BlockPos $$6 = pathfinderMob0.m_21534_();
            if (pathfinderMob0.m_20185_() > (double) $$6.m_123341_()) {
                $$4 -= randomSource2.nextInt(int1 / 2);
            } else {
                $$4 += randomSource2.nextInt(int1 / 2);
            }
            if (pathfinderMob0.m_20189_() > (double) $$6.m_123343_()) {
                $$5 -= randomSource2.nextInt(int1 / 2);
            } else {
                $$5 += randomSource2.nextInt(int1 / 2);
            }
        }
        return BlockPos.containing((double) $$4 + pathfinderMob0.m_20185_(), (double) blockPos3.m_123342_() + pathfinderMob0.m_20186_(), (double) $$5 + pathfinderMob0.m_20189_());
    }
}