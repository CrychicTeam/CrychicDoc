package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class GhostAICharge extends Goal {

    private final EntityGhost ghost;

    public boolean firstPhase = true;

    public Vec3 moveToPos = null;

    public Vec3 offsetOf = Vec3.ZERO;

    public GhostAICharge(EntityGhost ghost) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.ghost = ghost;
    }

    @Override
    public boolean canUse() {
        return this.ghost.m_5448_() != null && !this.ghost.isCharging();
    }

    @Override
    public boolean canContinueToUse() {
        return this.ghost.m_5448_() != null && this.ghost.m_5448_().isAlive();
    }

    @Override
    public void start() {
        this.ghost.setCharging(true);
    }

    @Override
    public void stop() {
        this.firstPhase = true;
        this.moveToPos = null;
        this.ghost.setCharging(false);
    }

    @Override
    public void tick() {
        LivingEntity target = this.ghost.m_5448_();
        if (target != null) {
            if (this.ghost.getAnimation() == IAnimatedEntity.NO_ANIMATION && (double) this.ghost.m_20270_(target) < 1.4) {
                this.ghost.setAnimation(EntityGhost.ANIMATION_HIT);
            }
            if (this.firstPhase) {
                if (this.moveToPos == null) {
                    BlockPos moveToPos = DragonUtils.getBlockInTargetsViewGhost(this.ghost, target);
                    this.moveToPos = Vec3.atCenterOf(moveToPos);
                } else {
                    this.ghost.m_21573_().moveTo(this.moveToPos.x + 0.5, this.moveToPos.y + 0.5, this.moveToPos.z + 0.5, 1.0);
                    if (this.ghost.m_20238_(this.moveToPos.add(0.5, 0.5, 0.5)) < 9.0) {
                        if (this.ghost.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                            this.ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
                        }
                        this.firstPhase = false;
                        this.moveToPos = null;
                        this.offsetOf = target.m_20182_().subtract(this.ghost.m_20182_()).normalize();
                    }
                }
            } else {
                Vec3 fin = target.m_20182_();
                this.moveToPos = new Vec3(fin.x, target.m_20186_() + (double) (target.m_20192_() / 2.0F), fin.z);
                this.ghost.m_21573_().moveTo(target, 1.2F);
                if (this.ghost.m_20238_(this.moveToPos.add(0.5, 0.5, 0.5)) < 3.0) {
                    this.stop();
                }
            }
        }
    }
}