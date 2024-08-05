package dev.xkmc.modulargolems.compat.materials.cataclysm;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.special.BaseRangedAttackGoal;
import net.minecraft.world.entity.LivingEntity;

public class EnderGuardianVoidRuneAttackGoal extends BaseRangedAttackGoal {

    public EnderGuardianVoidRuneAttackGoal(AbstractGolemEntity<?, ?> golem, int lv) {
        super(100, 0, 15, golem, lv);
    }

    @Override
    protected void performAttack(LivingEntity target) {
        EnderGuardianVoidRuneModifier.addRune(this.golem, target, this.lv);
    }
}