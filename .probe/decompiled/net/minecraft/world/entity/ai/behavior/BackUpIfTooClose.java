package net.minecraft.world.entity.ai.behavior;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;

public class BackUpIfTooClose {

    public static OneShot<Mob> create(int int0, float float1) {
        return BehaviorBuilder.create(p_260278_ -> p_260278_.group(p_260278_.absent(MemoryModuleType.WALK_TARGET), p_260278_.registered(MemoryModuleType.LOOK_TARGET), p_260278_.present(MemoryModuleType.ATTACK_TARGET), p_260278_.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(p_260278_, (p_260206_, p_259953_, p_259993_, p_259209_) -> (p_259617_, p_260038_, p_259374_) -> {
            LivingEntity $$9 = p_260278_.get(p_259993_);
            if ($$9.m_19950_(p_260038_, (double) int0) && p_260278_.<NearestVisibleLivingEntities>get(p_259209_).contains($$9)) {
                p_259953_.set(new EntityTracker($$9, true));
                p_260038_.getMoveControl().strafe(-float1, 0.0F);
                p_260038_.m_146922_(Mth.rotateIfNecessary(p_260038_.m_146908_(), p_260038_.f_20885_, 0.0F));
                return true;
            } else {
                return false;
            }
        }));
    }
}