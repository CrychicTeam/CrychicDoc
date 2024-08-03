package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.github.L_Ender.cataclysm.entity.projectile.Death_Laser_Beam_Entity;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.special.BaseRangedAttackGoal;
import net.minecraft.world.entity.LivingEntity;

public class HarbingerDeathBeamAttackGoal extends BaseRangedAttackGoal {

    private Death_Laser_Beam_Entity beam;

    public HarbingerDeathBeamAttackGoal(AbstractGolemEntity<?, ?> golem, int lv) {
        super(100, 4, 35, golem, lv);
    }

    @Override
    public boolean canContinueToUse() {
        return this.beam != null ? true : super.m_8045_();
    }

    @Override
    public void tick() {
        if (this.beam != null) {
            if (this.beam.m_213877_()) {
                this.beam = null;
            } else {
                this.beam.m_20343_(this.golem.m_20185_(), this.golem.m_20188_(), this.golem.m_20189_());
            }
        }
        super.tick();
    }

    @Override
    protected void performAttack(LivingEntity target) {
        this.beam = HarbingerDeathBeamModifier.addBeam(this.golem);
        this.golem.m_21573_().stop();
    }
}