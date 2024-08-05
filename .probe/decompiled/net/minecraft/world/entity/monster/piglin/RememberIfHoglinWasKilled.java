package net.minecraft.world.entity.monster.piglin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class RememberIfHoglinWasKilled {

    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create(p_260168_ -> p_260168_.group(p_260168_.present(MemoryModuleType.ATTACK_TARGET), p_260168_.registered(MemoryModuleType.HUNTED_RECENTLY)).apply(p_260168_, (p_259214_, p_260031_) -> (p_289478_, p_289479_, p_289480_) -> {
            LivingEntity $$6 = p_260168_.get(p_259214_);
            if ($$6.m_6095_() == EntityType.HOGLIN && $$6.isDeadOrDying()) {
                p_260031_.setWithExpiry(true, (long) PiglinAi.TIME_BETWEEN_HUNTS.sample(p_289479_.m_9236_().random));
            }
            return true;
        }));
    }
}