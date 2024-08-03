package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class RingBell {

    private static final float BELL_RING_CHANCE = 0.95F;

    public static final int RING_BELL_FROM_DISTANCE = 3;

    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create(p_259094_ -> p_259094_.group(p_259094_.present(MemoryModuleType.MEETING_POINT)).apply(p_259094_, p_259028_ -> (p_259026_, p_260317_, p_260205_) -> {
            if (p_259026_.f_46441_.nextFloat() <= 0.95F) {
                return false;
            } else {
                BlockPos $$5 = p_259094_.<GlobalPos>get(p_259028_).pos();
                if ($$5.m_123314_(p_260317_.m_20183_(), 3.0)) {
                    BlockState $$6 = p_259026_.m_8055_($$5);
                    if ($$6.m_60713_(Blocks.BELL)) {
                        BellBlock $$7 = (BellBlock) $$6.m_60734_();
                        $$7.attemptToRing(p_260317_, p_259026_, $$5, null);
                    }
                }
                return true;
            }
        }));
    }
}