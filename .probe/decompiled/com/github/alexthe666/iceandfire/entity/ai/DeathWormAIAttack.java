package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class DeathWormAIAttack extends Goal {

    private final EntityDeathWorm worm;

    private int jumpCooldown = 0;

    public DeathWormAIAttack(EntityDeathWorm worm) {
        this.worm = worm;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.jumpCooldown > 0) {
            this.jumpCooldown--;
        }
        return this.worm.m_5448_() != null && !this.worm.m_20160_() && (this.worm.m_20096_() || this.worm.isInSandStrict()) && this.jumpCooldown <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.worm.m_5448_() != null && this.worm.m_5448_().isAlive();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        LivingEntity target = this.worm.m_5448_();
        if (target != null) {
            if (this.worm.isInSand()) {
                BlockPos topSand = this.worm.m_20183_();
                while (this.worm.m_9236_().getBlockState(topSand.above()).m_204336_(BlockTags.SAND)) {
                    topSand = topSand.above();
                }
                this.worm.m_6034_(this.worm.m_20185_(), (double) ((float) topSand.m_123342_() + 0.5F), this.worm.m_20189_());
            }
            if (this.shouldJump()) {
                this.jumpAttack();
            } else {
                this.worm.m_21573_().moveTo(target, 1.0);
            }
        }
    }

    public boolean shouldJump() {
        LivingEntity target = this.worm.m_5448_();
        if (target != null) {
            double distanceXZ = this.worm.m_20275_(target.m_20185_(), this.worm.m_20186_(), target.m_20189_());
            float distanceXZSqrt = (float) Math.sqrt(distanceXZ);
            double d0 = this.worm.m_20184_().y;
            if (distanceXZSqrt < 12.0F && distanceXZSqrt > 2.0F) {
                return this.jumpCooldown <= 0 && (d0 * d0 >= 0.03F || this.worm.m_146909_() == 0.0F || Math.abs(this.worm.m_146909_()) >= 10.0F || !this.worm.m_20069_()) && !this.worm.m_20096_();
            }
        }
        return false;
    }

    public void jumpAttack() {
        LivingEntity target = this.worm.m_5448_();
        if (target != null) {
            this.worm.m_21391_(target, 260.0F, 30.0F);
            double smoothX = Mth.clamp(Math.abs(target.m_20185_() - this.worm.m_20185_()), 0.0, 1.0);
            double smoothZ = Mth.clamp(Math.abs(target.m_20189_() - this.worm.m_20189_()), 0.0, 1.0);
            double d0 = (target.m_20185_() - this.worm.m_20185_()) * 0.2 * smoothX;
            double d2 = (target.m_20189_() - this.worm.m_20189_()) * 0.2 * smoothZ;
            float up = (this.worm.getScale() > 3.0F ? 0.8F : 0.5F) + this.worm.m_217043_().nextFloat() * 0.5F;
            this.worm.m_20256_(this.worm.m_20184_().add(d0 * 0.3, (double) up, d2 * 0.3));
            this.worm.m_21573_().stop();
            this.worm.setWormJumping(20);
            this.jumpCooldown = this.worm.m_217043_().nextInt(32) + 64;
        }
    }

    @Override
    public void stop() {
        this.worm.m_146926_(0.0F);
    }

    @Override
    public void tick() {
        if (this.jumpCooldown > 0) {
            this.jumpCooldown--;
        }
        LivingEntity target = this.worm.m_5448_();
        if (target != null && this.worm.m_142582_(target) && this.worm.m_20270_(target) < 3.0F) {
            this.worm.doHurtTarget(target);
        }
        Vec3 vector3d = this.worm.m_20184_();
        if (vector3d.y * vector3d.y < 0.1F && this.worm.m_146909_() != 0.0F) {
            this.worm.m_146926_(Mth.rotLerp(this.worm.m_146909_(), 0.0F, 0.2F));
        } else {
            double d0 = vector3d.horizontalDistance();
            double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * 180.0F / (float) Math.PI;
            this.worm.m_146926_((float) d1);
        }
        if (this.shouldJump()) {
            this.jumpAttack();
        } else if (this.worm.m_21573_().isDone()) {
            this.worm.m_21573_().moveTo(target, 1.0);
        }
    }
}