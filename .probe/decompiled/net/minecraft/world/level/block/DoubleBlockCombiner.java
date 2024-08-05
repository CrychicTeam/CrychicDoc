package net.minecraft.world.level.block;

import java.util.function.BiPredicate;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class DoubleBlockCombiner {

    public static <S extends BlockEntity> DoubleBlockCombiner.NeighborCombineResult<S> combineWithNeigbour(BlockEntityType<S> blockEntityTypeS0, Function<BlockState, DoubleBlockCombiner.BlockType> functionBlockStateDoubleBlockCombinerBlockType1, Function<BlockState, Direction> functionBlockStateDirection2, DirectionProperty directionProperty3, BlockState blockState4, LevelAccessor levelAccessor5, BlockPos blockPos6, BiPredicate<LevelAccessor, BlockPos> biPredicateLevelAccessorBlockPos7) {
        S $$8 = blockEntityTypeS0.getBlockEntity(levelAccessor5, blockPos6);
        if ($$8 == null) {
            return DoubleBlockCombiner.Combiner::m_6502_;
        } else if (biPredicateLevelAccessorBlockPos7.test(levelAccessor5, blockPos6)) {
            return DoubleBlockCombiner.Combiner::m_6502_;
        } else {
            DoubleBlockCombiner.BlockType $$9 = (DoubleBlockCombiner.BlockType) functionBlockStateDoubleBlockCombinerBlockType1.apply(blockState4);
            boolean $$10 = $$9 == DoubleBlockCombiner.BlockType.SINGLE;
            boolean $$11 = $$9 == DoubleBlockCombiner.BlockType.FIRST;
            if ($$10) {
                return new DoubleBlockCombiner.NeighborCombineResult.Single<>($$8);
            } else {
                BlockPos $$12 = blockPos6.relative((Direction) functionBlockStateDirection2.apply(blockState4));
                BlockState $$13 = levelAccessor5.m_8055_($$12);
                if ($$13.m_60713_(blockState4.m_60734_())) {
                    DoubleBlockCombiner.BlockType $$14 = (DoubleBlockCombiner.BlockType) functionBlockStateDoubleBlockCombinerBlockType1.apply($$13);
                    if ($$14 != DoubleBlockCombiner.BlockType.SINGLE && $$9 != $$14 && $$13.m_61143_(directionProperty3) == blockState4.m_61143_(directionProperty3)) {
                        if (biPredicateLevelAccessorBlockPos7.test(levelAccessor5, $$12)) {
                            return DoubleBlockCombiner.Combiner::m_6502_;
                        }
                        S $$15 = blockEntityTypeS0.getBlockEntity(levelAccessor5, $$12);
                        if ($$15 != null) {
                            S $$16 = $$11 ? $$8 : $$15;
                            S $$17 = $$11 ? $$15 : $$8;
                            return new DoubleBlockCombiner.NeighborCombineResult.Double<>($$16, $$17);
                        }
                    }
                }
                return new DoubleBlockCombiner.NeighborCombineResult.Single<>($$8);
            }
        }
    }

    public static enum BlockType {

        SINGLE, FIRST, SECOND
    }

    public interface Combiner<S, T> {

        T acceptDouble(S var1, S var2);

        T acceptSingle(S var1);

        T acceptNone();
    }

    public interface NeighborCombineResult<S> {

        <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> var1);

        public static final class Double<S> implements DoubleBlockCombiner.NeighborCombineResult<S> {

            private final S first;

            private final S second;

            public Double(S s0, S s1) {
                this.first = s0;
                this.second = s1;
            }

            @Override
            public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> doubleBlockCombinerCombinerSuperST0) {
                return doubleBlockCombinerCombinerSuperST0.acceptDouble(this.first, this.second);
            }
        }

        public static final class Single<S> implements DoubleBlockCombiner.NeighborCombineResult<S> {

            private final S single;

            public Single(S s0) {
                this.single = s0;
            }

            @Override
            public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> doubleBlockCombinerCombinerSuperST0) {
                return doubleBlockCombinerCombinerSuperST0.acceptSingle(this.single);
            }
        }
    }
}