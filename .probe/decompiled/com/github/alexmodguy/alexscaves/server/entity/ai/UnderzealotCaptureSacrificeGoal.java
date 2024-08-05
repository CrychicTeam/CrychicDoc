package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class UnderzealotCaptureSacrificeGoal extends Goal {

    private UnderzealotEntity entity;

    private LivingEntity sacrifice;

    private int validTimeCheck = 0;

    public UnderzealotCaptureSacrificeGoal(UnderzealotEntity underzealot) {
        this.entity = underzealot;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.m_5448_();
        long worldTime = this.entity.m_9236_().getGameTime() % 10L;
        if (!this.entity.isCarrying() && !this.entity.isPackFollower() && this.entity.sacrificeCooldown <= 0) {
            if (worldTime == 0L && this.entity.m_217043_().nextInt(3) == 0 || target != null && target.isAlive()) {
                AABB aabb = this.entity.m_20191_().inflate(20.0);
                List<LivingEntity> list = this.entity.m_9236_().m_6443_(LivingEntity.class, aabb, this::isFirstValidSacrifice);
                if (list.isEmpty()) {
                    return false;
                } else {
                    LivingEntity closest = null;
                    for (LivingEntity mob : list) {
                        if ((closest == null || mob.m_20280_(this.entity) < closest.m_20280_(this.entity)) && this.entity.m_142582_(mob)) {
                            closest = mob;
                        }
                    }
                    this.sacrifice = closest;
                    return this.sacrifice != null;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isFirstValidSacrifice(LivingEntity entity) {
        return this.isValidSacrifice(entity) && (entity instanceof VesperEntity || entity.getRandom().nextInt(4) == 0);
    }

    private boolean isValidSacrifice(LivingEntity entity) {
        if (entity instanceof UnderzealotSacrifice sacrifice && !entity.m_20159_() && sacrifice.isValidSacrifice(this.getDistanceToGround(entity))) {
            return true;
        }
        return false;
    }

    private int getDistanceToGround(LivingEntity entity) {
        int downBy = 0;
        for (BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(entity.m_146903_(), entity.m_146904_(), entity.m_146907_()); entity.m_9236_().m_46859_(pos) && pos.m_123342_() > entity.m_9236_().m_141937_(); downBy++) {
            pos.move(0, -1, 0);
        }
        return downBy;
    }

    @Override
    public boolean canContinueToUse() {
        return this.sacrifice != null && this.sacrifice.isAlive() && !this.sacrifice.m_20159_() && !this.entity.isPackFollower() && this.entity.m_20270_(this.sacrifice) < 32.0F && this.entity.sacrificeCooldown <= 0;
    }

    @Override
    public void stop() {
        this.entity.m_21573_().stop();
    }

    @Override
    public void start() {
        this.validTimeCheck = 0;
    }

    @Override
    public void tick() {
        double distance = (double) this.entity.m_20270_(this.sacrifice);
        this.entity.m_21573_().moveTo(this.sacrifice, 1.0);
        if (distance < 1.4F) {
            this.sacrifice.m_20329_(this.entity);
        }
        Vec3 sub = this.sacrifice.m_20182_().subtract(this.entity.m_20182_());
        if (!this.entity.isBuried() && sub.y > 0.5 && sub.horizontalDistance() < 2.0 && this.entity.m_20096_()) {
            this.entity.jumpFromGround();
        }
        this.validTimeCheck++;
        if (this.validTimeCheck % 100 == 0 && !this.isValidSacrifice(this.sacrifice)) {
            this.sacrifice = null;
        }
    }
}