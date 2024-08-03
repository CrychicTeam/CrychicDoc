package net.minecraft.world.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public class SmoothSwimmingLookControl extends LookControl {

    private final int maxYRotFromCenter;

    private static final int HEAD_TILT_X = 10;

    private static final int HEAD_TILT_Y = 20;

    public SmoothSwimmingLookControl(Mob mob0, int int1) {
        super(mob0);
        this.maxYRotFromCenter = int1;
    }

    @Override
    public void tick() {
        if (this.f_186068_ > 0) {
            this.f_186068_--;
            this.m_180896_().ifPresent(p_287449_ -> this.f_24937_.f_20885_ = this.m_24956_(this.f_24937_.f_20885_, p_287449_ + 20.0F, this.f_24938_));
            this.m_180897_().ifPresent(p_289401_ -> this.f_24937_.m_146926_(this.m_24956_(this.f_24937_.m_146909_(), p_289401_ + 10.0F, this.f_24939_)));
        } else {
            if (this.f_24937_.getNavigation().isDone()) {
                this.f_24937_.m_146926_(this.m_24956_(this.f_24937_.m_146909_(), 0.0F, 5.0F));
            }
            this.f_24937_.f_20885_ = this.m_24956_(this.f_24937_.f_20885_, this.f_24937_.f_20883_, this.f_24938_);
        }
        float $$0 = Mth.wrapDegrees(this.f_24937_.f_20885_ - this.f_24937_.f_20883_);
        if ($$0 < (float) (-this.maxYRotFromCenter)) {
            this.f_24937_.f_20883_ -= 4.0F;
        } else if ($$0 > (float) this.maxYRotFromCenter) {
            this.f_24937_.f_20883_ += 4.0F;
        }
    }
}