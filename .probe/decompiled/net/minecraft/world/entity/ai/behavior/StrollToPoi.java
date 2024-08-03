package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.apache.commons.lang3.mutable.MutableLong;

public class StrollToPoi {

    public static BehaviorControl<PathfinderMob> create(MemoryModuleType<GlobalPos> memoryModuleTypeGlobalPos0, float float1, int int2, int int3) {
        MutableLong $$4 = new MutableLong(0L);
        return BehaviorBuilder.create(p_258859_ -> p_258859_.group(p_258859_.registered(MemoryModuleType.WALK_TARGET), p_258859_.present(memoryModuleTypeGlobalPos0)).apply(p_258859_, (p_258842_, p_258843_) -> (p_258851_, p_258852_, p_258853_) -> {
            GlobalPos $$10 = p_258859_.get(p_258843_);
            if (p_258851_.m_46472_() != $$10.dimension() || !$$10.pos().m_203195_(p_258852_.m_20182_(), (double) int3)) {
                return false;
            } else if (p_258853_ <= $$4.getValue()) {
                return true;
            } else {
                p_258842_.set(new WalkTarget($$10.pos(), float1, int2));
                $$4.setValue(p_258853_ + 80L);
                return true;
            }
        }));
    }
}