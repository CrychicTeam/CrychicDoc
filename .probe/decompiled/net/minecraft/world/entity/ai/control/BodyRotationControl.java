package net.minecraft.world.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public class BodyRotationControl implements Control {

    private final Mob mob;

    private static final int HEAD_STABLE_ANGLE = 15;

    private static final int DELAY_UNTIL_STARTING_TO_FACE_FORWARD = 10;

    private static final int HOW_LONG_IT_TAKES_TO_FACE_FORWARD = 10;

    private int headStableTime;

    private float lastStableYHeadRot;

    public BodyRotationControl(Mob mob0) {
        this.mob = mob0;
    }

    public void clientTick() {
        if (this.isMoving()) {
            this.mob.f_20883_ = this.mob.m_146908_();
            this.rotateHeadIfNecessary();
            this.lastStableYHeadRot = this.mob.f_20885_;
            this.headStableTime = 0;
        } else {
            if (this.notCarryingMobPassengers()) {
                if (Math.abs(this.mob.f_20885_ - this.lastStableYHeadRot) > 15.0F) {
                    this.headStableTime = 0;
                    this.lastStableYHeadRot = this.mob.f_20885_;
                    this.rotateBodyIfNecessary();
                } else {
                    this.headStableTime++;
                    if (this.headStableTime > 10) {
                        this.rotateHeadTowardsFront();
                    }
                }
            }
        }
    }

    private void rotateBodyIfNecessary() {
        this.mob.f_20883_ = Mth.rotateIfNecessary(this.mob.f_20883_, this.mob.f_20885_, (float) this.mob.getMaxHeadYRot());
    }

    private void rotateHeadIfNecessary() {
        this.mob.f_20885_ = Mth.rotateIfNecessary(this.mob.f_20885_, this.mob.f_20883_, (float) this.mob.getMaxHeadYRot());
    }

    private void rotateHeadTowardsFront() {
        int $$0 = this.headStableTime - 10;
        float $$1 = Mth.clamp((float) $$0 / 10.0F, 0.0F, 1.0F);
        float $$2 = (float) this.mob.getMaxHeadYRot() * (1.0F - $$1);
        this.mob.f_20883_ = Mth.rotateIfNecessary(this.mob.f_20883_, this.mob.f_20885_, $$2);
    }

    private boolean notCarryingMobPassengers() {
        return !(this.mob.m_146895_() instanceof Mob);
    }

    private boolean isMoving() {
        double $$0 = this.mob.m_20185_() - this.mob.f_19854_;
        double $$1 = this.mob.m_20189_() - this.mob.f_19856_;
        return $$0 * $$0 + $$1 * $$1 > 2.5000003E-7F;
    }
}