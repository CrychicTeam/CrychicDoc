package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityBoneSerpent;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class BoneSerpentAIMeleeJump extends JumpGoal {

    private final EntityBoneSerpent dolphin;

    private int attackCooldown = 0;

    private boolean inWater;

    public BoneSerpentAIMeleeJump(EntityBoneSerpent dolphin) {
        this.dolphin = dolphin;
    }

    @Override
    public boolean canUse() {
        if (this.dolphin.m_5448_() != null && !this.dolphin.m_20096_() && (this.dolphin.m_20077_() || this.dolphin.m_20069_()) && this.dolphin.jumpCooldown <= 0) {
            BlockPos blockpos = this.dolphin.m_20183_();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        double d0 = this.dolphin.m_20184_().y;
        return this.dolphin.m_5448_() != null && this.dolphin.jumpCooldown > 0 && (!(d0 * d0 < 0.03F) || this.dolphin.m_146909_() == 0.0F || !(Math.abs(this.dolphin.m_146909_()) < 10.0F) || !this.dolphin.m_20069_()) && !this.dolphin.m_20096_();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        LivingEntity target = this.dolphin.m_5448_();
        if (target != null) {
            double distanceXZ = this.dolphin.m_20275_(target.m_20185_(), this.dolphin.m_20186_(), target.m_20189_());
            if (distanceXZ < 150.0) {
                this.dolphin.m_21391_(target, 260.0F, 30.0F);
                double smoothX = Mth.clamp(Math.abs(target.m_20185_() - this.dolphin.m_20185_()), 0.0, 1.0);
                double smoothZ = Mth.clamp(Math.abs(target.m_20189_() - this.dolphin.m_20189_()), 0.0, 1.0);
                double d0 = (target.m_20185_() - this.dolphin.m_20185_()) * 0.3 * smoothX;
                double d2 = (target.m_20189_() - this.dolphin.m_20189_()) * 0.3 * smoothZ;
                float up = 1.0F + this.dolphin.m_217043_().nextFloat() * 0.8F;
                this.dolphin.m_20256_(this.dolphin.m_20184_().add(d0 * 0.3, (double) up, d2 * 0.3));
                this.dolphin.m_21573_().stop();
                this.dolphin.jumpCooldown = this.dolphin.m_217043_().nextInt(32) + 64;
            } else {
                this.dolphin.m_21573_().moveTo(target, 1.0);
            }
        }
    }

    @Override
    public void stop() {
        this.dolphin.m_146926_(0.0F);
        this.attackCooldown = 0;
    }

    @Override
    public void tick() {
        boolean flag = this.inWater;
        if (!flag) {
            FluidState fluidstate = this.dolphin.m_9236_().getFluidState(this.dolphin.m_20183_());
            this.inWater = fluidstate.is(FluidTags.LAVA) || fluidstate.is(FluidTags.WATER);
        }
        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }
        if (this.inWater && !flag) {
            this.dolphin.m_5496_(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
        }
        LivingEntity target = this.dolphin.m_5448_();
        if (target != null && this.dolphin.m_20270_(target) < 3.0F && this.attackCooldown <= 0) {
            this.dolphin.m_7327_(target);
            this.attackCooldown = 20;
        }
        Vec3 vector3d = this.dolphin.m_20184_();
        if (vector3d.y * vector3d.y < 0.1F && this.dolphin.m_146909_() != 0.0F) {
            this.dolphin.m_146926_(Mth.rotLerp(this.dolphin.m_146909_(), 0.0F, 0.2F));
        } else {
            double d0 = Math.sqrt(vector3d.horizontalDistanceSqr());
            double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * 180.0F / (float) Math.PI;
            this.dolphin.m_146926_((float) d1);
        }
    }
}