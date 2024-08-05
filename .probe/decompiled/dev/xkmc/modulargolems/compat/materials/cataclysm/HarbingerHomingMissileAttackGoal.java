package dev.xkmc.modulargolems.compat.materials.cataclysm;

import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemPartType;
import dev.xkmc.modulargolems.content.modifier.special.BaseRangedAttackGoal;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.world.entity.LivingEntity;

public class HarbingerHomingMissileAttackGoal extends BaseRangedAttackGoal {

    public HarbingerHomingMissileAttackGoal(AbstractGolemEntity<?, ?> golem, int lv) {
        super(100, 4, 35, golem, lv);
    }

    @Override
    protected void performAttack(LivingEntity target) {
        if (this.golem.getType() == GolemTypes.ENTITY_GOLEM.get()) {
            if (((GolemMaterial) this.golem.getMaterials().get(MetalGolemPartType.LEFT.ordinal())).modifiers().containsKey(CataCompatRegistry.HARBINGER_MISSILE.get())) {
                this.addMissile(target, 0.0F, -1.0F);
            }
            if (((GolemMaterial) this.golem.getMaterials().get(MetalGolemPartType.RIGHT.ordinal())).modifiers().containsKey(CataCompatRegistry.HARBINGER_MISSILE.get())) {
                this.addMissile(target, 0.0F, -1.0F);
            }
        } else {
            this.addMissile(target, 1.0F, 0.0F);
        }
    }

    private void addMissile(LivingEntity target, float y, float r) {
        HarbingerHomingMissileModifier.addBeam(this.golem, target, this.golem.m_20182_().add((double) r * Math.cos((double) (this.golem.f_20883_ * (float) (Math.PI / 180.0))), (double) (y + this.golem.m_20192_()), (double) r * Math.sin((double) (this.golem.f_20883_ * (float) (Math.PI / 180.0)))));
    }
}