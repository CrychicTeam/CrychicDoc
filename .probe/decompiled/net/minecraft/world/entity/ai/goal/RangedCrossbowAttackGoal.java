package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RangedCrossbowAttackGoal<T extends Monster & RangedAttackMob & CrossbowAttackMob> extends Goal {

    public static final UniformInt PATHFINDING_DELAY_RANGE = TimeUtil.rangeOfSeconds(1, 2);

    private final T mob;

    private RangedCrossbowAttackGoal.CrossbowState crossbowState = RangedCrossbowAttackGoal.CrossbowState.UNCHARGED;

    private final double speedModifier;

    private final float attackRadiusSqr;

    private int seeTime;

    private int attackDelay;

    private int updatePathDelay;

    public RangedCrossbowAttackGoal(T t0, double double1, float float2) {
        this.mob = t0;
        this.speedModifier = double1;
        this.attackRadiusSqr = float2 * float2;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.isValidTarget() && this.isHoldingCrossbow();
    }

    private boolean isHoldingCrossbow() {
        return this.mob.m_21055_(Items.CROSSBOW);
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
            CrossbowItem.setCharged(this.mob.m_21211_(), false);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity $$0 = this.mob.m_5448_();
        if ($$0 != null) {
            boolean $$1 = this.mob.m_21574_().hasLineOfSight($$0);
            boolean $$2 = this.seeTime > 0;
            if ($$1 != $$2) {
                this.seeTime = 0;
            }
            if ($$1) {
                this.seeTime++;
            } else {
                this.seeTime--;
            }
            double $$3 = this.mob.m_20280_($$0);
            boolean $$4 = ($$3 > (double) this.attackRadiusSqr || this.seeTime < 5) && this.attackDelay == 0;
            if ($$4) {
                this.updatePathDelay--;
                if (this.updatePathDelay <= 0) {
                    this.mob.m_21573_().moveTo($$0, this.canRun() ? this.speedModifier : this.speedModifier * 0.5);
                    this.updatePathDelay = PATHFINDING_DELAY_RANGE.sample(this.mob.m_217043_());
                }
            } else {
                this.updatePathDelay = 0;
                this.mob.m_21573_().stop();
            }
            this.mob.m_21563_().setLookAt($$0, 30.0F, 30.0F);
            if (this.crossbowState == RangedCrossbowAttackGoal.CrossbowState.UNCHARGED) {
                if (!$$4) {
                    this.mob.m_6672_(ProjectileUtil.getWeaponHoldingHand(this.mob, Items.CROSSBOW));
                    this.crossbowState = RangedCrossbowAttackGoal.CrossbowState.CHARGING;
                    this.mob.setChargingCrossbow(true);
                }
            } else if (this.crossbowState == RangedCrossbowAttackGoal.CrossbowState.CHARGING) {
                if (!this.mob.m_6117_()) {
                    this.crossbowState = RangedCrossbowAttackGoal.CrossbowState.UNCHARGED;
                }
                int $$5 = this.mob.m_21252_();
                ItemStack $$6 = this.mob.m_21211_();
                if ($$5 >= CrossbowItem.getChargeDuration($$6)) {
                    this.mob.m_21253_();
                    this.crossbowState = RangedCrossbowAttackGoal.CrossbowState.CHARGED;
                    this.attackDelay = 20 + this.mob.m_217043_().nextInt(20);
                    this.mob.setChargingCrossbow(false);
                }
            } else if (this.crossbowState == RangedCrossbowAttackGoal.CrossbowState.CHARGED) {
                this.attackDelay--;
                if (this.attackDelay == 0) {
                    this.crossbowState = RangedCrossbowAttackGoal.CrossbowState.READY_TO_ATTACK;
                }
            } else if (this.crossbowState == RangedCrossbowAttackGoal.CrossbowState.READY_TO_ATTACK && $$1) {
                this.mob.performRangedAttack($$0, 1.0F);
                ItemStack $$7 = this.mob.m_21120_(ProjectileUtil.getWeaponHoldingHand(this.mob, Items.CROSSBOW));
                CrossbowItem.setCharged($$7, false);
                this.crossbowState = RangedCrossbowAttackGoal.CrossbowState.UNCHARGED;
            }
        }
    }

    private boolean canRun() {
        return this.crossbowState == RangedCrossbowAttackGoal.CrossbowState.UNCHARGED;
    }

    static enum CrossbowState {

        UNCHARGED, CHARGING, CHARGED, READY_TO_ATTACK
    }
}