package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class DragonAIReturnToRoost extends Goal {

    private final EntityDragonBase dragon;

    public DragonAIReturnToRoost(EntityDragonBase entityIn, double movementSpeedIn) {
        this.dragon = entityIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.dragon.canMove() && this.dragon.lookingForRoostAIFlag && (this.dragon.m_5448_() == null || !this.dragon.m_5448_().isAlive()) && this.dragon.getRestrictCenter() != null && DragonUtils.isInHomeDimension(this.dragon) && this.dragon.getDistanceSquared(Vec3.atCenterOf(this.dragon.getRestrictCenter())) > this.dragon.m_20205_() * this.dragon.m_20205_();
    }

    @Override
    public void tick() {
        if (this.dragon.getRestrictCenter() != null) {
            double dist = Math.sqrt((double) this.dragon.getDistanceSquared(Vec3.atCenterOf(this.dragon.getRestrictCenter())));
            double xDist = Math.abs(this.dragon.m_20185_() - (double) this.dragon.getRestrictCenter().m_123341_() - 0.5);
            double zDist = Math.abs(this.dragon.m_20189_() - (double) this.dragon.getRestrictCenter().m_123343_() - 0.5);
            double xzDist = Math.sqrt(xDist * xDist + zDist * zDist);
            if (dist < (double) this.dragon.m_20205_()) {
                this.dragon.setFlying(false);
                this.dragon.setHovering(false);
                this.dragon.m_21573_().moveTo((double) this.dragon.getRestrictCenter().m_123341_(), (double) this.dragon.getRestrictCenter().m_123342_(), (double) this.dragon.getRestrictCenter().m_123343_(), 1.0);
            } else {
                double yAddition = (double) (15 + this.dragon.m_217043_().nextInt(3));
                if (xzDist < 40.0) {
                    yAddition = 0.0;
                    if (this.dragon.m_20096_()) {
                        this.dragon.setFlying(false);
                        this.dragon.setHovering(false);
                        this.dragon.flightManager.setFlightTarget(Vec3.upFromBottomCenterOf(this.dragon.getRestrictCenter(), yAddition));
                        this.dragon.m_21573_().moveTo((double) this.dragon.getRestrictCenter().m_123341_(), (double) this.dragon.getRestrictCenter().m_123342_(), (double) this.dragon.getRestrictCenter().m_123343_(), 1.0);
                        return;
                    }
                }
                if (!this.dragon.isFlying() && !this.dragon.isHovering() && xzDist > 40.0) {
                    this.dragon.setHovering(true);
                }
                if (this.dragon.isFlying()) {
                    this.dragon.flightManager.setFlightTarget(Vec3.upFromBottomCenterOf(this.dragon.getRestrictCenter(), yAddition));
                    this.dragon.m_21573_().moveTo((double) this.dragon.getRestrictCenter().m_123341_(), yAddition + (double) this.dragon.getRestrictCenter().m_123342_(), (double) this.dragon.getRestrictCenter().m_123343_(), 1.0);
                }
                this.dragon.flyTicks = 0;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }
}