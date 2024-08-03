package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class CyclopsAIAttackMelee extends MeleeAttackGoal {

    public CyclopsAIAttackMelee(EntityCyclops creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity entity, double distance) {
        double d0 = this.m_6639_(entity);
        if (this.isCyclopsBlinded() && distance >= 36.0) {
            this.m_8041_();
        } else {
            if (distance <= d0) {
                this.f_25540_.m_6674_(InteractionHand.MAIN_HAND);
                this.f_25540_.m_7327_(entity);
            }
        }
    }

    private boolean isCyclopsBlinded() {
        return this.f_25540_ instanceof EntityCyclops && ((EntityCyclops) this.f_25540_).isBlinded();
    }
}