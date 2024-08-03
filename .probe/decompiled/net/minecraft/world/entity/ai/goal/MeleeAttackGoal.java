package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

public class MeleeAttackGoal extends Goal {

    protected final PathfinderMob mob;

    private final double speedModifier;

    private final boolean followingTargetEvenIfNotSeen;

    private Path path;

    private double pathedTargetX;

    private double pathedTargetY;

    private double pathedTargetZ;

    private int ticksUntilNextPathRecalculation;

    private int ticksUntilNextAttack;

    private final int attackInterval = 20;

    private long lastCanUseCheck;

    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;

    public MeleeAttackGoal(PathfinderMob pathfinderMob0, double double1, boolean boolean2) {
        this.mob = pathfinderMob0;
        this.speedModifier = double1;
        this.followingTargetEvenIfNotSeen = boolean2;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        long $$0 = this.mob.m_9236_().getGameTime();
        if ($$0 - this.lastCanUseCheck < 20L) {
            return false;
        } else {
            this.lastCanUseCheck = $$0;
            LivingEntity $$1 = this.mob.m_5448_();
            if ($$1 == null) {
                return false;
            } else if (!$$1.isAlive()) {
                return false;
            } else {
                this.path = this.mob.m_21573_().createPath($$1, 0);
                return this.path != null ? true : this.getAttackReachSqr($$1) >= this.mob.m_20275_($$1.m_20185_(), $$1.m_20186_(), $$1.m_20189_());
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity $$0 = this.mob.m_5448_();
        if ($$0 == null) {
            return false;
        } else if (!$$0.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.mob.m_21573_().isDone();
        } else {
            return !this.mob.m_21444_($$0.m_20183_()) ? false : !($$0 instanceof Player) || !$$0.m_5833_() && !((Player) $$0).isCreative();
        }
    }

    @Override
    public void start() {
        this.mob.m_21573_().moveTo(this.path, this.speedModifier);
        this.mob.m_21561_(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    @Override
    public void stop() {
        LivingEntity $$0 = this.mob.m_5448_();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test($$0)) {
            this.mob.m_6710_(null);
        }
        this.mob.m_21561_(false);
        this.mob.m_21573_().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity $$0 = this.mob.m_5448_();
        if ($$0 != null) {
            this.mob.m_21563_().setLookAt($$0, 30.0F, 30.0F);
            double $$1 = this.mob.m_262793_($$0);
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if ((this.followingTargetEvenIfNotSeen || this.mob.m_21574_().hasLineOfSight($$0)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || $$0.m_20275_(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.mob.m_217043_().nextFloat() < 0.05F)) {
                this.pathedTargetX = $$0.m_20185_();
                this.pathedTargetY = $$0.m_20186_();
                this.pathedTargetZ = $$0.m_20189_();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.m_217043_().nextInt(7);
                if ($$1 > 1024.0) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if ($$1 > 256.0) {
                    this.ticksUntilNextPathRecalculation += 5;
                }
                if (!this.mob.m_21573_().moveTo($$0, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }
                this.ticksUntilNextPathRecalculation = this.m_183277_(this.ticksUntilNextPathRecalculation);
            }
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack($$0, $$1);
        }
    }

    protected void checkAndPerformAttack(LivingEntity livingEntity0, double double1) {
        double $$2 = this.getAttackReachSqr(livingEntity0);
        if (double1 <= $$2 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.mob.m_6674_(InteractionHand.MAIN_HAND);
            this.mob.m_7327_(livingEntity0);
        }
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.m_183277_(20);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected int getAttackInterval() {
        return this.m_183277_(20);
    }

    protected double getAttackReachSqr(LivingEntity livingEntity0) {
        return (double) (this.mob.m_20205_() * 2.0F * this.mob.m_20205_() * 2.0F + livingEntity0.m_20205_());
    }
}