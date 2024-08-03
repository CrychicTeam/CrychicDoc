package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.phys.Vec3;

public class DeathWormAIJump extends JumpGoal {

    private static final int[] JUMP_DISTANCES = new int[] { 0, 1, 4, 5, 6, 7 };

    private final EntityDeathWorm dolphin;

    private final int chance;

    private boolean inWater;

    private int jumpCooldown;

    public DeathWormAIJump(EntityDeathWorm dolphin, int p_i50329_2_) {
        this.dolphin = dolphin;
        this.chance = p_i50329_2_;
    }

    @Override
    public boolean canUse() {
        if (this.jumpCooldown > 0) {
            this.jumpCooldown--;
        }
        if (this.dolphin.m_217043_().nextInt(this.chance) == 0 && !this.dolphin.m_20160_() && this.dolphin.m_5448_() == null) {
            Direction direction = this.dolphin.m_6374_();
            int i = direction.getStepX();
            int j = direction.getStepZ();
            BlockPos blockpos = this.dolphin.m_20183_();
            for (int k : JUMP_DISTANCES) {
                if (!this.canJumpTo(blockpos, i, j, k) || !this.isAirAbove(blockpos, i, j, k)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean canJumpTo(BlockPos pos, int dx, int dz, int scale) {
        BlockPos blockpos = pos.offset(dx * scale, 0, dz * scale);
        return this.dolphin.m_9236_().getBlockState(blockpos).m_204336_(BlockTags.SAND);
    }

    private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
        return this.dolphin.m_9236_().getBlockState(pos.offset(dx * scale, 1, dz * scale)).m_60795_() && this.dolphin.m_9236_().getBlockState(pos.offset(dx * scale, 2, dz * scale)).m_60795_();
    }

    @Override
    public boolean canContinueToUse() {
        double d0 = this.dolphin.m_20184_().y;
        return this.jumpCooldown > 0 && (d0 * d0 >= 0.03F || this.dolphin.m_146909_() == 0.0F || Math.abs(this.dolphin.m_146909_()) >= 10.0F || !this.dolphin.isInSand()) && !this.dolphin.m_20096_();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        Direction direction = this.dolphin.m_6374_();
        float up = (this.dolphin.getScale() > 3.0F ? 0.7F : 0.4F) + this.dolphin.m_217043_().nextFloat() * 0.4F;
        this.dolphin.m_20256_(this.dolphin.m_20184_().add((double) direction.getStepX() * 0.6, (double) up, (double) direction.getStepZ() * 0.6));
        this.dolphin.m_21573_().stop();
        this.dolphin.setWormJumping(30);
        this.jumpCooldown = this.dolphin.m_217043_().nextInt(65) + 32;
    }

    @Override
    public void stop() {
        this.dolphin.m_146926_(0.0F);
    }

    @Override
    public void tick() {
        boolean flag = this.inWater;
        if (!flag) {
            this.inWater = this.dolphin.m_9236_().getBlockState(this.dolphin.m_20183_()).m_204336_(BlockTags.SAND);
        }
        Vec3 vector3d = this.dolphin.m_20184_();
        if (vector3d.y * vector3d.y < 0.1F && this.dolphin.m_146909_() != 0.0F) {
            this.dolphin.m_146926_(Mth.rotLerp(this.dolphin.m_146909_(), 0.0F, 0.2F));
        } else {
            double d0 = vector3d.horizontalDistance();
            double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * 180.0F / (float) Math.PI;
            this.dolphin.m_146926_((float) d1);
        }
    }
}