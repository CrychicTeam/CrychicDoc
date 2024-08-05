package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDreadLich;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class DreadLichAIStrife extends Goal {

    private final EntityDreadLich entity;

    private final double moveSpeedAmp;

    private final float maxAttackDistance;

    private int attackCooldown;

    private int seeTime;

    private boolean strafingClockwise;

    private boolean strafingBackwards;

    private int strafingTime = -1;

    public DreadLichAIStrife(EntityDreadLich mob, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn) {
        this.entity = mob;
        this.moveSpeedAmp = moveSpeedAmpIn;
        this.attackCooldown = attackCooldownIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    public void setAttackCooldown(int attackCooldownIn) {
        this.attackCooldown = attackCooldownIn;
    }

    @Override
    public boolean canUse() {
        return this.entity.m_5448_() != null && this.isStaffInHand();
    }

    protected boolean isStaffInHand() {
        return !this.entity.m_21205_().isEmpty() && this.entity.m_21205_().getItem() == IafItemRegistry.LICH_STAFF.get();
    }

    @Override
    public boolean canContinueToUse() {
        return (this.canUse() || !this.entity.m_21573_().isDone()) && this.isStaffInHand();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        this.seeTime = 0;
        this.entity.m_5810_();
    }

    @Override
    public void tick() {
        LivingEntity LivingEntity = this.entity.m_5448_();
        if (LivingEntity != null) {
            double d0 = this.entity.m_20275_(LivingEntity.m_20185_(), LivingEntity.m_20191_().minY, LivingEntity.m_20189_());
            boolean flag = this.entity.m_21574_().hasLineOfSight(LivingEntity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }
            if (flag) {
                this.seeTime++;
            } else {
                this.seeTime--;
            }
            if (d0 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
                this.entity.m_21573_().stop();
                this.strafingTime++;
            } else {
                this.entity.m_21573_().moveTo(LivingEntity, this.moveSpeedAmp);
                this.strafingTime = -1;
            }
            if (this.strafingTime >= 20) {
                if ((double) this.entity.m_217043_().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if ((double) this.entity.m_217043_().nextFloat() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }
            if (this.strafingTime > -1) {
                if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }
                this.entity.m_21566_().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entity.m_21391_(LivingEntity, 30.0F, 30.0F);
            } else {
                this.entity.m_21563_().setLookAt(LivingEntity, 30.0F, 30.0F);
            }
            if (!flag && this.seeTime < -60) {
                this.entity.m_5810_();
            } else if (flag) {
                this.entity.m_5810_();
                this.entity.performRangedAttack(LivingEntity, 0.0F);
            }
        }
    }
}