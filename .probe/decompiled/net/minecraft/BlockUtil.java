package net.minecraft;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntStack;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockUtil {

    public static BlockUtil.FoundRectangle getLargestRectangleAround(BlockPos blockPos0, Direction.Axis directionAxis1, int int2, Direction.Axis directionAxis3, int int4, Predicate<BlockPos> predicateBlockPos5) {
        BlockPos.MutableBlockPos $$6 = blockPos0.mutable();
        Direction $$7 = Direction.get(Direction.AxisDirection.NEGATIVE, directionAxis1);
        Direction $$8 = $$7.getOpposite();
        Direction $$9 = Direction.get(Direction.AxisDirection.NEGATIVE, directionAxis3);
        Direction $$10 = $$9.getOpposite();
        int $$11 = getLimit(predicateBlockPos5, $$6.set(blockPos0), $$7, int2);
        int $$12 = getLimit(predicateBlockPos5, $$6.set(blockPos0), $$8, int2);
        int $$13 = $$11;
        BlockUtil.IntBounds[] $$14 = new BlockUtil.IntBounds[$$11 + 1 + $$12];
        $$14[$$11] = new BlockUtil.IntBounds(getLimit(predicateBlockPos5, $$6.set(blockPos0), $$9, int4), getLimit(predicateBlockPos5, $$6.set(blockPos0), $$10, int4));
        int $$15 = $$14[$$11].min;
        for (int $$16 = 1; $$16 <= $$11; $$16++) {
            BlockUtil.IntBounds $$17 = $$14[$$13 - ($$16 - 1)];
            $$14[$$13 - $$16] = new BlockUtil.IntBounds(getLimit(predicateBlockPos5, $$6.set(blockPos0).move($$7, $$16), $$9, $$17.min), getLimit(predicateBlockPos5, $$6.set(blockPos0).move($$7, $$16), $$10, $$17.max));
        }
        for (int $$18 = 1; $$18 <= $$12; $$18++) {
            BlockUtil.IntBounds $$19 = $$14[$$13 + $$18 - 1];
            $$14[$$13 + $$18] = new BlockUtil.IntBounds(getLimit(predicateBlockPos5, $$6.set(blockPos0).move($$8, $$18), $$9, $$19.min), getLimit(predicateBlockPos5, $$6.set(blockPos0).move($$8, $$18), $$10, $$19.max));
        }
        int $$20 = 0;
        int $$21 = 0;
        int $$22 = 0;
        int $$23 = 0;
        int[] $$24 = new int[$$14.length];
        for (int $$25 = $$15; $$25 >= 0; $$25--) {
            for (int $$26 = 0; $$26 < $$14.length; $$26++) {
                BlockUtil.IntBounds $$27 = $$14[$$26];
                int $$28 = $$15 - $$27.min;
                int $$29 = $$15 + $$27.max;
                $$24[$$26] = $$25 >= $$28 && $$25 <= $$29 ? $$29 + 1 - $$25 : 0;
            }
            Pair<BlockUtil.IntBounds, Integer> $$30 = getMaxRectangleLocation($$24);
            BlockUtil.IntBounds $$31 = (BlockUtil.IntBounds) $$30.getFirst();
            int $$32 = 1 + $$31.max - $$31.min;
            int $$33 = (Integer) $$30.getSecond();
            if ($$32 * $$33 > $$22 * $$23) {
                $$20 = $$31.min;
                $$21 = $$25;
                $$22 = $$32;
                $$23 = $$33;
            }
        }
        return new BlockUtil.FoundRectangle(blockPos0.relative(directionAxis1, $$20 - $$13).relative(directionAxis3, $$21 - $$15), $$22, $$23);
    }

    private static int getLimit(Predicate<BlockPos> predicateBlockPos0, BlockPos.MutableBlockPos blockPosMutableBlockPos1, Direction direction2, int int3) {
        int $$4 = 0;
        while ($$4 < int3 && predicateBlockPos0.test(blockPosMutableBlockPos1.move(direction2))) {
            $$4++;
        }
        return $$4;
    }

    @VisibleForTesting
    static Pair<BlockUtil.IntBounds, Integer> getMaxRectangleLocation(int[] int0) {
        int $$1 = 0;
        int $$2 = 0;
        int $$3 = 0;
        IntStack $$4 = new IntArrayList();
        $$4.push(0);
        for (int $$5 = 1; $$5 <= int0.length; $$5++) {
            int $$6 = $$5 == int0.length ? 0 : int0[$$5];
            while (!$$4.isEmpty()) {
                int $$7 = int0[$$4.topInt()];
                if ($$6 >= $$7) {
                    $$4.push($$5);
                    break;
                }
                $$4.popInt();
                int $$8 = $$4.isEmpty() ? 0 : $$4.topInt() + 1;
                if ($$7 * ($$5 - $$8) > $$3 * ($$2 - $$1)) {
                    $$2 = $$5;
                    $$1 = $$8;
                    $$3 = $$7;
                }
            }
            if ($$4.isEmpty()) {
                $$4.push($$5);
            }
        }
        return new Pair(new BlockUtil.IntBounds($$1, $$2 - 1), $$3);
    }

    public static Optional<BlockPos> getTopConnectedBlock(BlockGetter blockGetter0, BlockPos blockPos1, Block block2, Direction direction3, Block block4) {
        BlockPos.MutableBlockPos $$5 = blockPos1.mutable();
        BlockState $$6;
        do {
            $$5.move(direction3);
            $$6 = blockGetter0.getBlockState($$5);
        } while ($$6.m_60713_(block2));
        return $$6.m_60713_(block4) ? Optional.of($$5) : Optional.empty();
    }

    public static class FoundRectangle {

        public final BlockPos minCorner;

        public final int axis1Size;

        public final int axis2Size;

        public FoundRectangle(BlockPos blockPos0, int int1, int int2) {
            this.minCorner = blockPos0;
            this.axis1Size = int1;
            this.axis2Size = int2;
        }
    }

    public static class IntBounds {

        public final int min;

        public final int max;

        public IntBounds(int int0, int int1) {
            this.min = int0;
            this.max = int1;
        }

        public String toString() {
            return "IntBounds{min=" + this.min + ", max=" + this.max + "}";
        }
    }
}