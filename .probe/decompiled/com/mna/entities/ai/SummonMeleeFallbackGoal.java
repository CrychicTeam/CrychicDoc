package com.mna.entities.ai;

import java.util.EnumSet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

public class SummonMeleeFallbackGoal extends Goal {

    protected final Mob attacker;

    private final double speedTowardsTarget;

    private final boolean longMemory;

    private Path path;

    private double targetX;

    private double targetY;

    private double targetZ;

    private int delayCounter;

    private int ticksUntilNextAttack;

    private long lastCanUseCheck;

    private int failedPathFindingPenalty = 0;

    private boolean canPenalize = false;

    public SummonMeleeFallbackGoal(Mob creature, double speedIn, boolean useLongMemory) {
        this.attacker = creature;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        long i = this.attacker.m_9236_().getGameTime();
        if (i - this.lastCanUseCheck < 20L) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.attacker.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (this.canPenalize) {
                if (--this.delayCounter <= 0) {
                    this.path = this.attacker.getNavigation().createPath(livingentity, 0);
                    this.delayCounter = 4 + this.attacker.m_217043_().nextInt(7);
                    return this.path != null;
                } else {
                    return true;
                }
            } else {
                this.path = this.attacker.getNavigation().createPath(livingentity, 0);
                return this.path != null ? true : this.getAttackReachSqr(livingentity) >= this.attacker.m_20275_(livingentity.m_20185_(), livingentity.m_20186_(), livingentity.m_20189_());
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.attacker.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!this.longMemory) {
            return !this.attacker.getNavigation().isDone();
        } else {
            return !this.attacker.isWithinRestriction(livingentity.m_20183_()) ? false : !(livingentity instanceof Player) || !livingentity.m_5833_() && !((Player) livingentity).isCreative();
        }
    }

    @Override
    public void start() {
        this.attacker.getNavigation().moveTo(this.path, this.speedTowardsTarget);
        this.attacker.setAggressive(true);
        this.delayCounter = 0;
        this.ticksUntilNextAttack = 0;
    }

    @Override
    public void stop() {
        LivingEntity livingentity = this.attacker.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.attacker.setTarget((LivingEntity) null);
        }
        this.attacker.setAggressive(false);
        this.attacker.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.attacker.getTarget();
        if (livingentity != null) {
            this.attacker.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            double d0 = this.attacker.m_20275_(livingentity.m_20185_(), livingentity.m_20186_(), livingentity.m_20189_());
            this.delayCounter = Math.max(this.delayCounter - 1, 0);
            if ((this.longMemory || this.attacker.getSensing().hasLineOfSight(livingentity)) && this.delayCounter <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || livingentity.m_20275_(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.attacker.m_217043_().nextFloat() < 0.05F)) {
                this.targetX = livingentity.m_20185_();
                this.targetY = livingentity.m_20186_();
                this.targetZ = livingentity.m_20189_();
                this.delayCounter = 4 + this.attacker.m_217043_().nextInt(7);
                if (this.canPenalize) {
                    this.delayCounter = this.delayCounter + this.failedPathFindingPenalty;
                    if (this.attacker.getNavigation().getPath() != null) {
                        Node finalPathPoint = this.attacker.getNavigation().getPath().getEndNode();
                        if (finalPathPoint != null && livingentity.m_20275_((double) finalPathPoint.x, (double) finalPathPoint.y, (double) finalPathPoint.z) < 1.0) {
                            this.failedPathFindingPenalty = 0;
                        } else {
                            this.failedPathFindingPenalty += 10;
                        }
                    } else {
                        this.failedPathFindingPenalty += 10;
                    }
                }
                if (d0 > 1024.0) {
                    this.delayCounter += 10;
                } else if (d0 > 256.0) {
                    this.delayCounter += 5;
                }
                if (!this.attacker.getNavigation().moveTo(livingentity, this.speedTowardsTarget)) {
                    this.delayCounter += 15;
                }
            }
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(livingentity, d0);
        }
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = 20;
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected int getAttackInterval() {
        return 20;
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (double) (this.attacker.m_20205_() * 2.0F * this.attacker.m_20205_() * 2.0F + attackTarget.m_20205_());
    }

    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= d0 && this.isTimeToAttack()) {
            this.resetAttackCooldown();
            this.attacker.m_6674_(InteractionHand.MAIN_HAND);
            if (!this.attacker.m_21204_().hasAttribute(Attributes.ATTACK_DAMAGE) || !this.attacker.m_21204_().hasAttribute(Attributes.ATTACK_KNOCKBACK) || !this.attacker.m_21204_().hasAttribute(Attributes.ATTACK_SPEED)) {
                enemy.hurt(this.attacker.m_269291_().mobAttack(this.attacker), 2.0F);
            } else if (!this.attacker.doHurtTarget(enemy)) {
                enemy.hurt(this.attacker.m_269291_().mobAttack(this.attacker), 2.0F);
            }
        }
    }
}