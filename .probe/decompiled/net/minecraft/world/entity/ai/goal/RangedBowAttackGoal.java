package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;

public class RangedBowAttackGoal<T extends Monster & RangedAttackMob> extends Goal {

    private final T mob;

    private final double speedModifier;

    private int attackIntervalMin;

    private final float attackRadiusSqr;

    private int attackTime = -1;

    private int seeTime;

    private boolean strafingClockwise;

    private boolean strafingBackwards;

    private int strafingTime = -1;

    public RangedBowAttackGoal(T t0, double double1, int int2, float float3) {
        this.mob = t0;
        this.speedModifier = double1;
        this.attackIntervalMin = int2;
        this.attackRadiusSqr = float3 * float3;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public void setMinAttackInterval(int int0) {
        this.attackIntervalMin = int0;
    }

    @Override
    public boolean canUse() {
        return this.mob.m_5448_() == null ? false : this.isHoldingBow();
    }

    protected boolean isHoldingBow() {
        return this.mob.m_21055_(Items.BOW);
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
        LivingEntity $$0 = this.mob.m_5448_();
        if ($$0 != null) {
            double $$1 = this.mob.m_20275_($$0.m_20185_(), $$0.m_20186_(), $$0.m_20189_());
            boolean $$2 = this.mob.m_21574_().hasLineOfSight($$0);
            boolean $$3 = this.seeTime > 0;
            if ($$2 != $$3) {
                this.seeTime = 0;
            }
            if ($$2) {
                this.seeTime++;
            } else {
                this.seeTime--;
            }
            if (!($$1 > (double) this.attackRadiusSqr) && this.seeTime >= 20) {
                this.mob.m_21573_().stop();
                this.strafingTime++;
            } else {
                this.mob.m_21573_().moveTo($$0, this.speedModifier);
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
                if ($$1 > (double) (this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if ($$1 < (double) (this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }
                this.mob.m_21566_().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                if (this.mob.m_275832_() instanceof Mob $$4) {
                    $$4.lookAt($$0, 30.0F, 30.0F);
                }
                this.mob.m_21391_($$0, 30.0F, 30.0F);
            } else {
                this.mob.m_21563_().setLookAt($$0, 30.0F, 30.0F);
            }
            if (this.mob.m_6117_()) {
                if (!$$2 && this.seeTime < -60) {
                    this.mob.m_5810_();
                } else if ($$2) {
                    int $$5 = this.mob.m_21252_();
                    if ($$5 >= 20) {
                        this.mob.m_5810_();
                        this.mob.performRangedAttack($$0, BowItem.getPowerForTime($$5));
                        this.attackTime = this.attackIntervalMin;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.mob.m_6672_(ProjectileUtil.getWeaponHoldingHand(this.mob, Items.BOW));
            }
        }
    }
}