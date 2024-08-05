package net.minecraft.world.entity.monster.piglin;

import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class StopAdmiringIfTiredOfTryingToReachItem {

    public static BehaviorControl<LivingEntity> create(int int0, int int1) {
        return BehaviorBuilder.create(p_260320_ -> p_260320_.group(p_260320_.present(MemoryModuleType.ADMIRING_ITEM), p_260320_.present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), p_260320_.registered(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM), p_260320_.registered(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM)).apply(p_260320_, (p_260184_, p_259407_, p_259388_, p_259580_) -> (p_259044_, p_259229_, p_259125_) -> {
            if (!p_259229_.getOffhandItem().isEmpty()) {
                return false;
            } else {
                Optional<Integer> $$9 = p_260320_.tryGet(p_259388_);
                if ($$9.isEmpty()) {
                    p_259388_.set(0);
                } else {
                    int $$10 = (Integer) $$9.get();
                    if ($$10 > int0) {
                        p_260184_.erase();
                        p_259388_.erase();
                        p_259580_.setWithExpiry(true, (long) int1);
                    } else {
                        p_259388_.set($$10 + 1);
                    }
                }
                return true;
            }
        }));
    }
}