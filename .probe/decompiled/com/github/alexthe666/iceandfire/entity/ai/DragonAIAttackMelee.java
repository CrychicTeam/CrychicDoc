package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class DragonAIAttackMelee extends Goal {

    protected EntityDragonBase dragon;

    private int attackTick;

    private final boolean longMemory;

    private int delayCounter;

    private double targetX;

    private double targetY;

    private double targetZ;

    private int failedPathFindingPenalty = 0;

    private final boolean canPenalize = false;

    private final double speedTowardsTarget;

    public DragonAIAttackMelee(EntityDragonBase dragon, double speedIn, boolean useLongMemory) {
        this.dragon = dragon;
        this.longMemory = useLongMemory;
        this.speedTowardsTarget = speedIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.dragon.m_5448_();
        if (!(this.dragon.m_21573_() instanceof AdvancedPathNavigate)) {
            return false;
        } else if (livingEntity == null) {
            return false;
        } else if (!livingEntity.isAlive()) {
            return false;
        } else if (this.dragon.canMove() && !this.dragon.isHovering() && !this.dragon.isFlying()) {
            ((AdvancedPathNavigate) this.dragon.m_21573_()).moveToLivingEntity(livingEntity, this.speedTowardsTarget);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!(this.dragon.m_21573_() instanceof AdvancedPathNavigate)) {
            return false;
        } else {
            LivingEntity livingEntity = this.dragon.m_5448_();
            if (livingEntity != null && !livingEntity.isAlive()) {
                this.stop();
                return false;
            } else {
                return livingEntity != null && livingEntity.isAlive() && !this.dragon.isFlying() && !this.dragon.isHovering();
            }
        }
    }

    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        LivingEntity LivingEntity = this.dragon.m_5448_();
        if (LivingEntity instanceof Player && (LivingEntity.m_5833_() || ((Player) LivingEntity).isCreative())) {
            this.dragon.setTarget(null);
        }
        this.dragon.m_21573_().stop();
    }

    @Override
    public void tick() {
        LivingEntity entity = this.dragon.m_5448_();
        if (this.delayCounter > 0) {
            this.delayCounter--;
        }
        if (entity != null) {
            if (this.dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY) {
                this.stop();
                return;
            }
            ((AdvancedPathNavigate) this.dragon.m_21573_()).moveToLivingEntity(entity, this.speedTowardsTarget);
            double d0 = this.dragon.m_20275_(entity.m_20185_(), entity.m_20191_().minY, entity.m_20189_());
            double d1 = this.getAttackReachSqr(entity);
            this.delayCounter--;
            if ((this.longMemory || this.dragon.m_21574_().hasLineOfSight(entity)) && this.delayCounter <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || entity.m_20275_(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.dragon.m_217043_().nextFloat() < 0.05F)) {
                this.targetX = entity.m_20185_();
                this.targetY = entity.m_20191_().minY;
                this.targetZ = entity.m_20189_();
                this.delayCounter = 4 + this.dragon.m_217043_().nextInt(7);
                Objects.requireNonNull(this);
                if (d0 > 1024.0) {
                    this.delayCounter += 10;
                } else if (d0 > 256.0) {
                    this.delayCounter += 5;
                }
                if (this.dragon.canMove()) {
                    this.delayCounter += 15;
                }
            }
            this.attackTick = Math.max(this.attackTick - 1, 0);
            if (d0 <= d1 && this.attackTick <= 0) {
                this.attackTick = 20;
                this.dragon.m_6674_(InteractionHand.MAIN_HAND);
                this.dragon.doHurtTarget(entity);
            }
        }
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (double) (this.dragon.m_20205_() * 2.0F * this.dragon.m_20205_() * 2.0F + attackTarget.m_20205_());
    }
}