package com.github.alexthe666.alexsmobs.entity.ai;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class AnimalAIMeleeNearby extends Goal {

    private final Mob entity;

    private final int range;

    private final double speed;

    private BlockPos fightStartPos = null;

    public AnimalAIMeleeNearby(Mob entity, int range, double speed) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.entity = entity;
        this.range = range;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null && this.entity.getTarget().isAlive() && !this.entity.m_20160_();
    }

    @Override
    public void start() {
        this.fightStartPos = this.entity.m_20097_();
    }

    @Override
    public void stop() {
        this.entity.getNavigation().stop();
        this.fightStartPos = null;
    }

    @Override
    public void tick() {
        if (this.entity.m_20270_(this.entity.getTarget()) < 3.0F + this.entity.m_20205_() + this.entity.getTarget().m_20205_()) {
            this.entity.doHurtTarget(this.entity.getTarget());
            this.entity.lookAt(this.entity.getTarget(), 180.0F, 180.0F);
        } else if (this.fightStartPos != null) {
            if (this.entity.m_20238_(Vec3.atCenterOf(this.fightStartPos)) < (double) (this.range * this.range)) {
                this.entity.getNavigation().moveTo(this.entity.getTarget(), this.speed);
            } else {
                this.entity.getNavigation().moveTo((double) ((float) this.fightStartPos.m_123341_() + 0.5F), (double) ((float) this.fightStartPos.m_123342_() + 0.5F), (double) ((float) this.fightStartPos.m_123343_() + 0.5F), 0.4F + this.speed);
            }
        }
    }
}