package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityCaiman;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class CaimanAIBellow extends Goal {

    private final EntityCaiman caiman;

    private int bellowTime = 0;

    public CaimanAIBellow(EntityCaiman caiman) {
        this.caiman = caiman;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.caiman.m_5448_() == null && this.caiman.bellowCooldown <= 0 && this.caiman.m_20072_() && !this.caiman.shouldFollow();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.bellowTime < 60;
    }

    @Override
    public void stop() {
        this.bellowTime = 0;
        this.caiman.bellowCooldown = 1000 + this.caiman.m_217043_().nextInt(1000);
        this.caiman.setBellowing(false);
    }

    @Override
    public void tick() {
        if (this.caiman.m_20072_()) {
            double d1 = this.caiman.getFluidTypeHeight(ForgeMod.WATER_TYPE.get());
            this.caiman.m_21573_().stop();
            if (d1 > 0.3F) {
                double d2 = Math.pow(d1 - 0.3F, 2.0);
                this.caiman.m_20256_(new Vec3(this.caiman.m_20184_().x, Math.min(d2 * 0.08F, 0.04F), this.caiman.m_20184_().z));
            } else {
                this.caiman.m_20256_(new Vec3(this.caiman.m_20184_().x, -0.02F, this.caiman.m_20184_().z));
            }
            if (d1 > 0.19F && d1 < 0.5) {
                this.bellowTime++;
                this.caiman.m_5496_(AMSoundRegistry.CAIMAN_SPLASH.get(), 1.0F, this.caiman.m_6100_());
                this.caiman.setBellowing(true);
            }
        }
    }
}