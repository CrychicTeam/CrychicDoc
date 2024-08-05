package net.minecraft.world.level.block;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public interface ChangeOverTimeBlock<T extends Enum<T>> {

    int SCAN_DISTANCE = 4;

    Optional<BlockState> getNext(BlockState var1);

    float getChanceModifier();

    default void onRandomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        float $$4 = 0.05688889F;
        if (randomSource3.nextFloat() < 0.05688889F) {
            this.applyChangeOverTime(blockState0, serverLevel1, blockPos2, randomSource3);
        }
    }

    T getAge();

    default void applyChangeOverTime(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        int $$4 = this.getAge().ordinal();
        int $$5 = 0;
        int $$6 = 0;
        for (BlockPos $$7 : BlockPos.withinManhattan(blockPos2, 4, 4, 4)) {
            int $$8 = $$7.m_123333_(blockPos2);
            if ($$8 > 4) {
                break;
            }
            if (!$$7.equals(blockPos2)) {
                BlockState $$9 = serverLevel1.m_8055_($$7);
                Block $$10 = $$9.m_60734_();
                if ($$10 instanceof ChangeOverTimeBlock) {
                    Enum<?> $$11 = ((ChangeOverTimeBlock) $$10).getAge();
                    if (this.getAge().getClass() == $$11.getClass()) {
                        int $$12 = $$11.ordinal();
                        if ($$12 < $$4) {
                            return;
                        }
                        if ($$12 > $$4) {
                            $$6++;
                        } else {
                            $$5++;
                        }
                    }
                }
            }
        }
        float $$13 = (float) ($$6 + 1) / (float) ($$6 + $$5 + 1);
        float $$14 = $$13 * $$13 * this.getChanceModifier();
        if (randomSource3.nextFloat() < $$14) {
            this.getNext(blockState0).ifPresent(p_153039_ -> serverLevel1.m_46597_(blockPos2, p_153039_));
        }
    }
}