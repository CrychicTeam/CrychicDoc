package dev.xkmc.modulargolems.compat.materials.cataclysm;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.special.BaseRangedAttackGoal;
import net.minecraft.world.entity.LivingEntity;

public class LeviathanBlastPortalAttackGoal extends BaseRangedAttackGoal {

    public LeviathanBlastPortalAttackGoal(AbstractGolemEntity<?, ?> golem, int lv) {
        super(100, 2, 35, golem, lv);
    }

    @Override
    protected void performAttack(LivingEntity target) {
        LeviathanBlastPortalModifier.addBeam(this.golem, target);
    }
}