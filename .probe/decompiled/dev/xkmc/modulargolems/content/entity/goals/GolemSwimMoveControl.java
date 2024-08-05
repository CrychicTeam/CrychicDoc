package dev.xkmc.modulargolems.content.entity.goals;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class GolemSwimMoveControl extends MoveControl {

    private final AbstractGolemEntity<?, ?> golem;

    public GolemSwimMoveControl(AbstractGolemEntity<?, ?> pDrowned) {
        super(pDrowned);
        this.golem = pDrowned;
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.golem.m_5448_();
        if (this.golem.m_20069_()) {
            if (livingentity != null && livingentity.m_20186_() > this.golem.m_20186_()) {
                this.golem.m_20256_(this.golem.m_20184_().add(0.0, 0.002, 0.0));
            }
            if (this.f_24981_ != MoveControl.Operation.MOVE_TO || this.golem.m_21573_().isDone()) {
                this.golem.m_7910_(0.0F);
                return;
            }
            double d0 = this.f_24975_ - this.golem.m_20185_();
            double d1 = this.f_24976_ - this.golem.m_20186_();
            double d2 = this.f_24977_ - this.golem.m_20189_();
            double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            d1 /= d3;
            float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
            this.golem.m_146922_(this.m_24991_(this.golem.m_146908_(), f, 90.0F));
            this.golem.f_20883_ = this.golem.m_146908_();
            float f1 = (float) (this.f_24978_ * this.golem.m_21133_(Attributes.MOVEMENT_SPEED));
            float f2 = Mth.lerp(0.125F, this.golem.m_6113_(), f1);
            this.golem.m_7910_(f2);
            this.golem.m_20256_(this.golem.m_20184_().add((double) f2 * d0 * 0.005, (double) f2 * d1 * 0.1, (double) f2 * d2 * 0.005));
        } else {
            if (!this.golem.m_20096_()) {
                this.golem.m_20256_(this.golem.m_20184_().add(0.0, -0.008, 0.0));
            }
            super.tick();
        }
    }
}