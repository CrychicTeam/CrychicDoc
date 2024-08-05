package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

public class SeaSerpentAIAttackMelee extends Goal {

    protected final int attackInterval = 20;

    protected EntitySeaSerpent attacker;

    protected int attackTick;

    Level world;

    double speedTowardsTarget;

    boolean longMemory;

    Path path;

    private int delayCounter;

    private double targetX;

    private double targetY;

    private double targetZ;

    private int failedPathFindingPenalty = 0;

    private final boolean canPenalize = false;

    public SeaSerpentAIAttackMelee(EntitySeaSerpent amphithere, double speedIn, boolean useLongMemory) {
        this.attacker = amphithere;
        this.world = amphithere.m_9236_();
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity LivingEntity = this.attacker.m_5448_();
        if (LivingEntity != null && (!this.attacker.shouldUseJumpAttack(LivingEntity) || this.attacker.jumpCooldown > 0)) {
            if (!LivingEntity.isAlive()) {
                return false;
            } else {
                this.path = this.attacker.m_21573_().createPath(LivingEntity, 0);
                return this.path != null ? true : this.getAttackReachSqr(LivingEntity) >= this.attacker.m_20275_(LivingEntity.m_20185_(), LivingEntity.m_20191_().minY, LivingEntity.m_20189_());
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity LivingEntity = this.attacker.m_5448_();
        if (LivingEntity == null) {
            return false;
        } else if (!LivingEntity.isAlive()) {
            return false;
        } else if (!this.longMemory) {
            return !this.attacker.m_21573_().isDone();
        } else {
            return !this.attacker.m_21444_(LivingEntity.m_20183_()) ? false : !(LivingEntity instanceof Player) || !LivingEntity.m_5833_() && !((Player) LivingEntity).isCreative();
        }
    }

    @Override
    public void start() {
        if (this.attacker.m_20069_()) {
            this.attacker.m_21566_().setWantedPosition(this.targetX, this.targetY, this.targetZ, 0.1F);
        } else {
            this.attacker.m_21573_().moveTo(this.path, this.speedTowardsTarget);
        }
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        LivingEntity LivingEntity = this.attacker.m_5448_();
        if (LivingEntity instanceof Player && (LivingEntity.m_5833_() || ((Player) LivingEntity).isCreative())) {
            this.attacker.m_6710_(null);
        }
        this.attacker.m_21573_().stop();
    }

    @Override
    public void tick() {
        LivingEntity LivingEntity = this.attacker.m_5448_();
        if (LivingEntity != null) {
            if (this.attacker.m_20069_()) {
                this.attacker.m_21566_().setWantedPosition(LivingEntity.m_20185_(), LivingEntity.m_20186_() + (double) LivingEntity.m_20192_(), LivingEntity.m_20189_(), 0.1);
            }
            this.attacker.m_21563_().setLookAt(LivingEntity, 30.0F, 30.0F);
            this.delayCounter--;
            if ((this.longMemory || this.attacker.m_21574_().hasLineOfSight(LivingEntity)) && this.delayCounter <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || LivingEntity.m_20275_(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.attacker.m_217043_().nextFloat() < 0.05F)) {
                double d0 = this.attacker.m_20275_(LivingEntity.m_20185_(), LivingEntity.m_20191_().minY, LivingEntity.m_20189_());
                this.targetX = LivingEntity.m_20185_();
                this.targetY = LivingEntity.m_20191_().minY;
                this.targetZ = LivingEntity.m_20189_();
                this.delayCounter = 4 + this.attacker.m_217043_().nextInt(7);
                Objects.requireNonNull(this);
                if (d0 > 1024.0) {
                    this.delayCounter += 10;
                } else if (d0 > 256.0) {
                    this.delayCounter += 5;
                }
                if (!this.attacker.m_21573_().moveTo(LivingEntity, this.speedTowardsTarget)) {
                    this.delayCounter += 15;
                }
            }
            this.attackTick = Math.max(this.attackTick - 1, 0);
            this.checkAndPerformAttack(LivingEntity);
        }
    }

    protected void checkAndPerformAttack(LivingEntity enemy) {
        if (this.attacker.isTouchingMob(enemy)) {
            this.attackTick = 20;
            this.attacker.m_6674_(InteractionHand.MAIN_HAND);
            this.attacker.doHurtTarget(enemy);
        }
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (double) (this.attacker.m_20205_() * 2.0F * this.attacker.m_20205_() * 2.0F + attackTarget.m_20205_());
    }
}