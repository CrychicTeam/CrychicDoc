package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class CorrodentDigRandomlyGoal extends Goal {

    private CorrodentEntity entity;

    private double x;

    private double y;

    private double z;

    private boolean surface = false;

    public CorrodentDigRandomlyGoal(CorrodentEntity entity) {
        this.entity = entity;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.entity.m_20160_() && (this.entity.m_5448_() == null || !this.entity.m_5448_().isAlive()) && !this.entity.m_20159_() && (this.entity.isDigging() || this.entity.m_20096_() || this.entity.m_5830_())) {
            if (!this.entity.isDigging() && !this.entity.m_5830_() && this.entity.m_217043_().nextInt(20) != 0) {
                return false;
            } else {
                if (this.entity.isDigging() && this.entity.timeDigging > 300) {
                    this.surface = true;
                }
                Vec3 target = this.generatePosition();
                if (target == null) {
                    return false;
                } else {
                    this.x = target.x;
                    this.y = target.y;
                    this.z = target.z;
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.entity.setDigging(true);
        this.entity.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.m_21573_().isDone() && !this.entity.m_21573_().isStuck() && this.entity.isDigging();
    }

    @Override
    public void tick() {
        if (this.surface && this.entity.m_20275_(this.x, this.y, this.z) < 4.0) {
            this.entity.setDigging(false);
        }
    }

    @Override
    public void stop() {
        this.surface = false;
    }

    private Vec3 generatePosition() {
        BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos();
        for (int i = 0; i < 20; i++) {
            check.move(this.entity.m_20183_());
            check.move(this.entity.m_217043_().nextInt(32) - 16, this.entity.m_217043_().nextInt(32) - 16, this.entity.m_217043_().nextInt(32) - 16);
            if (check.m_123342_() < this.entity.m_9236_().m_141937_() || !this.entity.m_9236_().isLoaded(check)) {
                break;
            }
            if (this.surface) {
                while (!this.entity.m_9236_().m_46859_(check) && check.m_123342_() < this.entity.m_9236_().m_151558_()) {
                    check.move(0, 1, 0);
                }
                if (this.entity.m_9236_().m_46859_(check)) {
                    return check.immutable().getCenter();
                }
            } else {
                while (this.entity.m_9236_().m_46859_(check) && check.m_123342_() > this.entity.m_9236_().m_141937_() - 1) {
                    check.move(0, -1, 0);
                }
                if (CorrodentEntity.isSafeDig(this.entity.m_9236_(), check.immutable()) && this.entity.canReach(check)) {
                    return Vec3.atCenterOf(check.immutable());
                }
            }
        }
        return null;
    }
}