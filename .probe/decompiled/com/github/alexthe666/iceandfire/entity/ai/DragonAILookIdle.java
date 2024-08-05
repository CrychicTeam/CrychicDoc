package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;

public class DragonAILookIdle extends Goal {

    private final EntityDragonBase dragon;

    private double lookX;

    private double lookZ;

    private int idleTime;

    public DragonAILookIdle(EntityDragonBase prehistoric) {
        this.dragon = prehistoric;
        this.m_7021_(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.dragon.canMove() && this.dragon.getAnimation() != EntityDragonBase.ANIMATION_SHAKEPREY && !this.dragon.isFuelingForge() ? this.dragon.m_217043_().nextFloat() < 0.02F : false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.idleTime >= 0 && this.dragon.canMove();
    }

    @Override
    public void start() {
        double d0 = (Math.PI * 2) * this.dragon.m_217043_().nextDouble();
        this.lookX = (double) Mth.cos((float) d0);
        this.lookZ = (double) Mth.sin((float) d0);
        this.idleTime = 20 + this.dragon.m_217043_().nextInt(20);
    }

    @Override
    public void tick() {
        if (this.idleTime > 0) {
            this.idleTime--;
        }
        this.dragon.m_21563_().setLookAt(this.dragon.m_20185_() + this.lookX, this.dragon.m_20186_() + (double) this.dragon.m_20192_(), this.dragon.m_20189_() + this.lookZ, (float) this.dragon.getMaxHeadYRot(), (float) this.dragon.m_8132_());
    }
}