package net.minecraft.world.entity.ai.behavior;

import java.util.function.BiPredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.GameRules;

public class StartCelebratingIfTargetDead {

    public static BehaviorControl<LivingEntity> create(int int0, BiPredicate<LivingEntity, LivingEntity> biPredicateLivingEntityLivingEntity1) {
        return BehaviorBuilder.create(p_259600_ -> p_259600_.group(p_259600_.present(MemoryModuleType.ATTACK_TARGET), p_259600_.registered(MemoryModuleType.ANGRY_AT), p_259600_.absent(MemoryModuleType.CELEBRATE_LOCATION), p_259600_.registered(MemoryModuleType.DANCING)).apply(p_259600_, (p_259049_, p_259067_, p_259031_, p_259141_) -> (p_259956_, p_259611_, p_259619_) -> {
            LivingEntity $$10 = p_259600_.get(p_259049_);
            if (!$$10.isDeadOrDying()) {
                return false;
            } else {
                if (biPredicateLivingEntityLivingEntity1.test(p_259611_, $$10)) {
                    p_259141_.setWithExpiry(true, (long) int0);
                }
                p_259031_.setWithExpiry($$10.m_20183_(), (long) int0);
                if ($$10.m_6095_() != EntityType.PLAYER || p_259956_.m_46469_().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
                    p_259049_.erase();
                    p_259067_.erase();
                }
                return true;
            }
        }));
    }
}