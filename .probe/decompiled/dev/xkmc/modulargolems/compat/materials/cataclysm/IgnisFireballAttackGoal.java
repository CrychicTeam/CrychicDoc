package dev.xkmc.modulargolems.compat.materials.cataclysm;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.special.BaseRangedAttackGoal;
import net.minecraft.world.entity.LivingEntity;

public class IgnisFireballAttackGoal extends BaseRangedAttackGoal {

    public IgnisFireballAttackGoal(AbstractGolemEntity<?, ?> golem, int lv) {
        super(200, 4, 25, golem, lv);
    }

    @Override
    protected void performAttack(LivingEntity target) {
        IgnisFireballModifier.addFireball(this.golem, this.lv);
    }
}