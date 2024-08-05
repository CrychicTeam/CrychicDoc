package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityCrocodile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class CrocodileAIMelee extends MeleeAttackGoal {

    private final EntityCrocodile crocodile;

    public CrocodileAIMelee(EntityCrocodile crocodile, double speedIn, boolean useLongMemory) {
        super(crocodile, speedIn, useLongMemory);
        this.crocodile = crocodile;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.crocodile.m_20197_().isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.crocodile.m_20197_().isEmpty();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.m_6639_(enemy);
        if (distToEnemySqr <= d0) {
            this.m_25563_();
            this.f_25540_.m_6674_(InteractionHand.MAIN_HAND);
            this.f_25540_.m_7327_(enemy);
        }
    }
}