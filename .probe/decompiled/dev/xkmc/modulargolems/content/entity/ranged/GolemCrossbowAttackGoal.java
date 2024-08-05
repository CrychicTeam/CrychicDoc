package dev.xkmc.modulargolems.content.entity.ranged;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import java.util.EnumSet;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

public class GolemCrossbowAttackGoal extends Goal {

    public static final UniformInt PATHFINDING_DELAY_RANGE = TimeUtil.rangeOfSeconds(1, 2);

    private final HumanoidGolemEntity mob;

    private GolemCrossbowAttackGoal.CrossbowState crossbowState = GolemCrossbowAttackGoal.CrossbowState.UNCHARGED;

    private final double speedModifier;

    private final float attackRadiusSqr;

    private int seeTime;

    private int attackDelay;

    private int updatePathDelay;

    public GolemCrossbowAttackGoal(HumanoidGolemEntity pMob, double pSpeedModifier, float pAttackRadius) {
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.attackRadiusSqr = pAttackRadius * pAttackRadius;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.isValidTarget() && this.isHoldingCrossbow();
    }

    private boolean isHoldingCrossbow() {
        return this.mob.m_21093_(is -> is.getItem() instanceof CrossbowItem);
    }

    @Override
    public boolean canContinueToUse() {
        return this.isValidTarget() && (this.canUse() || !this.mob.m_21573_().isDone()) && this.isHoldingCrossbow();
    }

    private boolean isValidTarget() {
        return this.mob.m_5448_() != null && this.mob.m_5448_().isAlive();
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.m_21561_(false);
        this.mob.m_6710_(null);
        this.seeTime = 0;
        if (this.mob.m_6117_()) {
            this.mob.m_5810_();
            this.mob.setChargingCrossbow(false);
            CrossbowItem.setCharged(this.mob.getUseItem(), false);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.mob.m_5448_();
        if (livingentity != null) {
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
            double d0 = this.mob.m_20280_(livingentity);
            boolean flag2 = (d0 > (double) this.attackRadiusSqr || this.seeTime < 5) && this.attackDelay == 0;
            if (flag2) {
                this.updatePathDelay--;
                if (this.updatePathDelay <= 0) {
                    this.mob.m_21573_().moveTo(livingentity, this.canRun() ? this.speedModifier : this.speedModifier * 0.5);
                    this.updatePathDelay = PATHFINDING_DELAY_RANGE.sample(this.mob.m_217043_());
                }
            } else {
                this.updatePathDelay = 0;
                this.mob.m_21573_().stop();
            }
            this.mob.m_21563_().setLookAt(livingentity, 30.0F, 30.0F);
            if (this.crossbowState == GolemCrossbowAttackGoal.CrossbowState.UNCHARGED) {
                if (!flag2 && !this.mob.getProjectile(this.mob.m_21120_(this.mob.getWeaponHand())).isEmpty()) {
                    this.mob.m_6672_(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem));
                    this.crossbowState = GolemCrossbowAttackGoal.CrossbowState.CHARGING;
                    this.mob.setChargingCrossbow(true);
                }
            } else if (this.crossbowState == GolemCrossbowAttackGoal.CrossbowState.CHARGING) {
                if (!this.mob.m_6117_()) {
                    this.crossbowState = GolemCrossbowAttackGoal.CrossbowState.UNCHARGED;
                }
                int i = this.mob.m_21252_();
                ItemStack itemstack = this.mob.getUseItem();
                if (i >= CrossbowItem.getChargeDuration(itemstack)) {
                    this.mob.m_21253_();
                    this.crossbowState = GolemCrossbowAttackGoal.CrossbowState.CHARGED;
                    this.attackDelay = 20 + this.mob.m_217043_().nextInt(20);
                    this.mob.setChargingCrossbow(false);
                }
            } else if (this.crossbowState == GolemCrossbowAttackGoal.CrossbowState.CHARGED) {
                this.attackDelay--;
                if (this.attackDelay == 0) {
                    this.crossbowState = GolemCrossbowAttackGoal.CrossbowState.READY_TO_ATTACK;
                }
            } else if (this.crossbowState == GolemCrossbowAttackGoal.CrossbowState.READY_TO_ATTACK && flag) {
                this.mob.performRangedAttack(livingentity, 1.0F);
                ItemStack itemstack1 = this.mob.m_21120_(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem));
                CrossbowItem.setCharged(itemstack1, false);
                this.crossbowState = GolemCrossbowAttackGoal.CrossbowState.UNCHARGED;
            }
        }
    }

    private boolean canRun() {
        return this.crossbowState == GolemCrossbowAttackGoal.CrossbowState.UNCHARGED;
    }

    static enum CrossbowState {

        UNCHARGED, CHARGING, CHARGED, READY_TO_ATTACK
    }
}