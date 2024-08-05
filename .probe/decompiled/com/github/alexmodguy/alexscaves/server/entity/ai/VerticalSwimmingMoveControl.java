package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class VerticalSwimmingMoveControl extends MoveControl {

    private final Mob mob;

    private float secondSpeedModifier;

    private float maxRotChange;

    public VerticalSwimmingMoveControl(Mob mob, float secondSpeedModifier, float maxRotChange) {
        super(mob);
        this.mob = mob;
        this.secondSpeedModifier = secondSpeedModifier;
        this.maxRotChange = maxRotChange;
    }

    @Override
    public void tick() {
        if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
            double d0 = this.f_24975_ - this.mob.m_20185_();
            double d1 = this.f_24976_ - this.mob.m_20186_();
            double d2 = this.f_24977_ - this.mob.m_20189_();
            double d3 = (double) Mth.sqrt((float) (d0 * d0 + d1 * d1 + d2 * d2));
            double d4 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
            d1 /= d3;
            this.mob.f_20883_ = this.mob.m_146908_();
            float f1 = (float) (this.f_24978_ * this.mob.m_21133_(Attributes.MOVEMENT_SPEED) * (double) this.secondSpeedModifier);
            float rotBy = this.maxRotChange;
            this.mob.m_20256_(this.mob.m_20184_().add(0.0, (double) f1 * d1 * 0.4, 0.0));
            if (d4 < (double) (this.mob.m_20205_() + 1.4F)) {
                f1 *= 0.7F;
                if (d4 < 0.3F) {
                    rotBy = 0.0F;
                } else {
                    rotBy = Math.max(40.0F, this.maxRotChange);
                }
            }
            float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
            this.mob.m_146922_(this.m_24991_(this.mob.m_146908_(), f, rotBy));
            if (d3 > 0.3) {
                this.mob.setSpeed(f1);
            } else {
                this.mob.setSpeed(0.0F);
            }
        } else {
            this.mob.setSpeed(0.0F);
            if (this.mob instanceof DeepOneBaseEntity deepOne) {
                this.mob.m_20256_(this.mob.m_20184_().add(0.0, -0.1, 0.0));
            }
        }
    }
}