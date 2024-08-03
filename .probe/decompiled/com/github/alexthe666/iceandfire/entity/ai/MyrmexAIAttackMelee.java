package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class MyrmexAIAttackMelee extends Goal {

    protected EntityMyrmexBase myrmex;

    private int attackTick;

    private final double speedTowardsTarget;

    private final boolean longMemory;

    private int delayCounter;

    private double targetX;

    private double targetY;

    private double targetZ;

    private int failedPathFindingPenalty = 0;

    private final boolean canPenalize = false;

    private PathResult attackPath;

    public MyrmexAIAttackMelee(EntityMyrmexBase dragon, double speedIn, boolean useLongMemory) {
        this.myrmex = dragon;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity LivingEntity = this.myrmex.m_5448_();
        if (!(this.myrmex.m_21573_() instanceof AdvancedPathNavigate)) {
            return false;
        } else if (LivingEntity instanceof Player && this.myrmex.getHive() != null && !this.myrmex.getHive().isPlayerReputationLowEnoughToFight(LivingEntity.m_20148_())) {
            return false;
        } else if (LivingEntity == null) {
            return false;
        } else if (!LivingEntity.isAlive()) {
            return false;
        } else if (!this.myrmex.canMove()) {
            return false;
        } else {
            this.attackPath = ((AdvancedPathNavigate) this.myrmex.m_21573_()).moveToLivingEntity(LivingEntity, this.speedTowardsTarget);
            return this.attackPath != null ? true : this.getAttackReachSqr(LivingEntity) >= this.myrmex.m_20275_(LivingEntity.m_20185_(), LivingEntity.m_20191_().minY, LivingEntity.m_20189_());
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity LivingEntity = this.myrmex.m_5448_();
        if (this.myrmex.m_21214_() != null && this.myrmex.m_21214_().isAlive()) {
            LivingEntity = this.myrmex.m_21214_();
        }
        if ((LivingEntity == null || LivingEntity.isAlive()) && this.myrmex.m_21573_() instanceof AdvancedPathNavigate) {
            return LivingEntity != null && LivingEntity.isAlive() && (!(LivingEntity instanceof Player) || !LivingEntity.m_5833_() && !((Player) LivingEntity).isCreative());
        } else {
            this.stop();
            return false;
        }
    }

    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        LivingEntity LivingEntity = this.myrmex.m_5448_();
        if (LivingEntity instanceof Player && (LivingEntity.m_5833_() || ((Player) LivingEntity).isCreative())) {
            this.myrmex.m_6710_(null);
            this.myrmex.m_21335_(null);
        }
    }

    @Override
    public void tick() {
        LivingEntity entity = this.myrmex.m_5448_();
        if (entity != null) {
            this.myrmex.m_21573_().moveTo(entity, this.speedTowardsTarget);
            double d0 = this.myrmex.m_20275_(entity.m_20185_(), entity.m_20191_().minY, entity.m_20189_());
            double d1 = this.getAttackReachSqr(entity);
            this.delayCounter--;
            if ((this.longMemory || this.myrmex.m_21574_().hasLineOfSight(entity)) && this.delayCounter <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || entity.m_20275_(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.myrmex.m_217043_().nextFloat() < 0.05F)) {
                this.targetX = entity.m_20185_();
                this.targetY = entity.m_20191_().minY;
                this.targetZ = entity.m_20189_();
                this.delayCounter = 4 + this.myrmex.m_217043_().nextInt(7);
                Objects.requireNonNull(this);
                if (d0 > 1024.0) {
                    this.delayCounter += 10;
                } else if (d0 > 256.0) {
                    this.delayCounter += 5;
                }
                if (this.myrmex.canMove()) {
                    this.delayCounter += 15;
                }
            }
            this.attackTick = Math.max(this.attackTick - 1, 0);
            if (d0 <= d1 && this.attackTick <= 0) {
                this.attackTick = 20;
                this.myrmex.m_6674_(InteractionHand.MAIN_HAND);
                this.myrmex.m_7327_(entity);
            }
        }
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (double) (this.myrmex.m_20205_() * 2.0F * this.myrmex.m_20205_() * 2.0F + attackTarget.m_20205_());
    }
}