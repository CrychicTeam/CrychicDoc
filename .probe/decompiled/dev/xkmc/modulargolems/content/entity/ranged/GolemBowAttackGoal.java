package dev.xkmc.modulargolems.content.entity.ranged;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;

public class GolemBowAttackGoal extends Goal {

    private final HumanoidGolemEntity mob;

    private final double speedModifier;

    private int attackIntervalMin;

    private final float attackRadiusSqr;

    private int attackTime = -1;

    private int seeTime;

    private boolean strafingClockwise;

    private boolean strafingBackwards;

    private int strafingTime = -1;

    public GolemBowAttackGoal(HumanoidGolemEntity pMob, double pSpeedModifier, int pAttackIntervalMin, float pAttackRadius) {
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.attackIntervalMin = pAttackIntervalMin;
        this.attackRadiusSqr = pAttackRadius * pAttackRadius;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public void setMinAttackInterval(int pAttackCooldown) {
        this.attackIntervalMin = pAttackCooldown;
    }

    @Override
    public boolean canUse() {
        return this.mob.m_5448_() != null && this.isHoldingBow() && !this.mob.getProjectile(this.mob.m_21120_(this.mob.getWeaponHand())).isEmpty();
    }

    protected boolean isHoldingBow() {
        return this.mob.m_21093_(is -> is.getItem() instanceof BowItem);
    }

    @Override
    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.m_21573_().isDone()) && this.isHoldingBow();
    }

    @Override
    public void start() {
        super.start();
        this.mob.m_21561_(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.m_21561_(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.mob.m_5810_();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.mob.m_5448_();
        if (livingentity != null) {
            double d0 = this.mob.m_20275_(livingentity.m_20185_(), livingentity.m_20186_(), livingentity.m_20189_());
            boolean flag = this.mob.m_21574_().hasLineOfSight(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }
            if (flag) {
                this.seeTime++;
            } else {
                this.seeTime--;
            }
            if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 20) {
                this.mob.m_21573_().stop();
                this.strafingTime++;
            } else {
                this.mob.m_21573_().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }
            if (this.strafingTime >= 20) {
                if ((double) this.mob.m_217043_().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if ((double) this.mob.m_217043_().nextFloat() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }
            if (this.strafingTime > -1) {
                if (d0 > (double) (this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double) (this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }
                this.mob.m_21566_().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.mob.m_21391_(livingentity, 30.0F, 30.0F);
            } else {
                this.mob.m_21563_().setLookAt(livingentity, 30.0F, 30.0F);
            }
            if (this.mob.m_6117_()) {
                if (!flag && this.seeTime < -60) {
                    this.mob.m_5810_();
                } else if (flag) {
                    int i = this.mob.m_21252_();
                    if (i >= 20) {
                        this.mob.m_5810_();
                        this.mob.performRangedAttack(livingentity, BowItem.getPowerForTime(i));
                        this.attackTime = this.attackIntervalMin;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.mob.m_6672_(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof BowItem));
            }
        }
    }
}