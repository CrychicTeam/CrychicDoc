package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class StymphalianBirdAIAirTarget extends Goal {

    private final EntityStymphalianBird bird;

    public StymphalianBirdAIAirTarget(EntityStymphalianBird bird) {
        this.bird = bird;
    }

    public static BlockPos getNearbyAirTarget(EntityStymphalianBird bird) {
        if (bird.m_5448_() == null) {
            BlockPos pos = DragonUtils.getBlockInViewStymphalian(bird);
            if (pos != null && bird.m_9236_().getBlockState(pos).m_60795_()) {
                return pos;
            } else {
                if (bird.flock != null && bird.flock.isLeader(bird)) {
                    bird.flock.setTarget(bird.airTarget);
                }
                return bird.m_20183_();
            }
        } else {
            return BlockPos.containing((double) bird.m_5448_().m_146903_(), bird.m_5448_().m_20186_() + (double) bird.m_5448_().m_20192_(), (double) bird.m_5448_().m_146907_());
        }
    }

    @Override
    public boolean canUse() {
        if (this.bird != null) {
            if (!this.bird.isFlying()) {
                return false;
            } else if (!this.bird.m_6162_() && !this.bird.doesWantToLand()) {
                if (this.bird.airTarget != null && this.bird.isTargetBlocked(Vec3.atCenterOf(this.bird.airTarget))) {
                    this.bird.airTarget = null;
                }
                if (this.bird.airTarget != null) {
                    return false;
                } else {
                    Vec3 vec = this.findAirTarget();
                    if (vec == null) {
                        return false;
                    } else {
                        this.bird.airTarget = BlockPos.containing(vec);
                        return true;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.bird.isFlying()) {
            return false;
        } else {
            return this.bird.m_6162_() ? false : this.bird.airTarget != null;
        }
    }

    public Vec3 findAirTarget() {
        return Vec3.atCenterOf(getNearbyAirTarget(this.bird));
    }
}