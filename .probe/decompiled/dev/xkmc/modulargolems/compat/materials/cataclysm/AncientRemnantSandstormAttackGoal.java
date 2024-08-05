package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.github.L_Ender.cataclysm.entity.effect.Sandstorm_Entity;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.special.BaseRangedAttackGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class AncientRemnantSandstormAttackGoal extends BaseRangedAttackGoal {

    public AncientRemnantSandstormAttackGoal(AbstractGolemEntity<?, ?> golem, int lv) {
        super(100, 4, 35, golem, lv);
    }

    @Override
    protected void performAttack(LivingEntity target) {
        Vec3 diff = target.m_20182_().subtract(this.golem.m_20182_()).normalize();
        float angle = (float) Math.atan2(diff.z, diff.x);
        double sx = target.m_20185_();
        double sy = target.m_20186_();
        double sz = target.m_20189_();
        Sandstorm_Entity projectile = new Sandstorm_Entity(this.golem.m_9236_(), sx, sy, sz, 100, angle, this.golem.m_20148_());
        this.golem.m_9236_().m_7967_(projectile);
    }
}