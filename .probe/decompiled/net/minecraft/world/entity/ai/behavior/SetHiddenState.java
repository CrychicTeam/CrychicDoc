package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.apache.commons.lang3.mutable.MutableInt;

public class SetHiddenState {

    private static final int HIDE_TIMEOUT = 300;

    public static BehaviorControl<LivingEntity> create(int int0, int int1) {
        int $$2 = int0 * 20;
        MutableInt $$3 = new MutableInt(0);
        return BehaviorBuilder.create(p_259055_ -> p_259055_.group(p_259055_.present(MemoryModuleType.HIDING_PLACE), p_259055_.present(MemoryModuleType.HEARD_BELL_TIME)).apply(p_259055_, (p_260296_, p_260145_) -> (p_288844_, p_288845_, p_288846_) -> {
            long $$9 = p_259055_.<Long>get(p_260145_);
            boolean $$10 = $$9 + 300L <= p_288846_;
            if ($$3.getValue() <= $$2 && !$$10) {
                BlockPos $$11 = p_259055_.<GlobalPos>get(p_260296_).pos();
                if ($$11.m_123314_(p_288845_.m_20183_(), (double) int1)) {
                    $$3.increment();
                }
                return true;
            } else {
                p_260145_.erase();
                p_260296_.erase();
                p_288845_.getBrain().updateActivityFromSchedule(p_288844_.m_46468_(), p_288844_.m_46467_());
                $$3.setValue(0);
                return true;
            }
        }));
    }
}