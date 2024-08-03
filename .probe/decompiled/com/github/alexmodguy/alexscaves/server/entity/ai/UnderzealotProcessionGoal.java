package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class UnderzealotProcessionGoal extends Goal {

    public final UnderzealotEntity entity;

    private double speedModifier;

    private int attemptToFollowTicks = 0;

    public UnderzealotProcessionGoal(UnderzealotEntity underzealot, double speed) {
        this.entity = underzealot;
        this.speedModifier = speed;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.entity.m_217043_().nextInt(20) == 0 && this.entity.isPackFollower() && this.entity.getPriorPackMember() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.isPackFollower() && this.entity.getPriorPackMember() != null;
    }

    @Override
    public void stop() {
        this.entity.m_21573_().stop();
        this.attemptToFollowTicks = 0;
    }

    @Override
    public void tick() {
        if (!this.entity.isDiggingInProgress() && !this.entity.isBuried()) {
            if (this.entity.getPriorPackMember() != null) {
                UnderzealotEntity priorPackMember = (UnderzealotEntity) this.entity.getPriorPackMember();
                double distanceTo = (double) this.entity.m_20270_(priorPackMember);
                if (distanceTo > (double) (this.entity.m_20205_() + 0.5F)) {
                    Vec3 vec3 = new Vec3(priorPackMember.m_20185_() - this.entity.m_20185_(), priorPackMember.m_20186_() - this.entity.m_20186_(), priorPackMember.m_20189_() - this.entity.m_20189_()).normalize().scale(Math.max(distanceTo - 1.0, 0.0));
                    this.entity.m_21573_().moveTo(this.entity.m_20185_() + vec3.x, this.entity.m_20186_() + vec3.y, this.entity.m_20189_() + vec3.z, this.speedModifier);
                }
                if (distanceTo > 8.0) {
                    this.attemptToFollowTicks++;
                    if (this.entity.m_21573_().isStuck() || this.attemptToFollowTicks > 60) {
                        this.entity.setBuried(true);
                        this.entity.reemergeAt(priorPackMember.m_20183_(), 20 + this.entity.m_217043_().nextInt(20));
                    }
                } else {
                    this.attemptToFollowTicks = 0;
                }
            }
        } else {
            this.entity.setPraying(false);
        }
    }
}