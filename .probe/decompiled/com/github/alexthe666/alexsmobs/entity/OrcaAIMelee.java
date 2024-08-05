package com.github.alexthe666.alexsmobs.entity;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class OrcaAIMelee extends MeleeAttackGoal {

    public OrcaAIMelee(EntityOrca orca, double v, boolean b) {
        super(orca, v, b);
    }

    @Override
    public boolean canUse() {
        return this.f_25540_.m_5448_() != null && !((EntityOrca) this.f_25540_).shouldUseJumpAttack(this.f_25540_.m_5448_()) ? super.canUse() : false;
    }
}