package net.minecraft.world.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FlyingMoveControl extends MoveControl {

    private final int maxTurn;

    private final boolean hoversInPlace;

    public FlyingMoveControl(Mob mob0, int int1, boolean boolean2) {
        super(mob0);
        this.maxTurn = int1;
        this.hoversInPlace = boolean2;
    }

    @Override
    public void tick() {
        if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
            this.f_24981_ = MoveControl.Operation.WAIT;
            this.f_24974_.m_20242_(true);
            double $$0 = this.f_24975_ - this.f_24974_.m_20185_();
            double $$1 = this.f_24976_ - this.f_24974_.m_20186_();
            double $$2 = this.f_24977_ - this.f_24974_.m_20189_();
            double $$3 = $$0 * $$0 + $$1 * $$1 + $$2 * $$2;
            if ($$3 < 2.5000003E-7F) {
                this.f_24974_.setYya(0.0F);
                this.f_24974_.setZza(0.0F);
                return;
            }
            float $$4 = (float) (Mth.atan2($$2, $$0) * 180.0F / (float) Math.PI) - 90.0F;
            this.f_24974_.m_146922_(this.m_24991_(this.f_24974_.m_146908_(), $$4, 90.0F));
            float $$5;
            if (this.f_24974_.m_20096_()) {
                $$5 = (float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED));
            } else {
                $$5 = (float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.FLYING_SPEED));
            }
            this.f_24974_.setSpeed($$5);
            double $$7 = Math.sqrt($$0 * $$0 + $$2 * $$2);
            if (Math.abs($$1) > 1.0E-5F || Math.abs($$7) > 1.0E-5F) {
                float $$8 = (float) (-(Mth.atan2($$1, $$7) * 180.0F / (float) Math.PI));
                this.f_24974_.m_146926_(this.m_24991_(this.f_24974_.m_146909_(), $$8, (float) this.maxTurn));
                this.f_24974_.setYya($$1 > 0.0 ? $$5 : -$$5);
            }
        } else {
            if (!this.hoversInPlace) {
                this.f_24974_.m_20242_(false);
            }
            this.f_24974_.setYya(0.0F);
            this.f_24974_.setZza(0.0F);
        }
    }
}