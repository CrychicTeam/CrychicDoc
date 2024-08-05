package com.github.alexthe666.alexsmobs.entity.util;

import com.github.alexthe666.alexsmobs.entity.EntityLaviathan;
import net.minecraft.util.Mth;

public class LaviathanNeckSolver {

    private int yawTimer;

    private float yawVariation;

    private int pitchTimer;

    private float pitchVariation;

    private float prevYawVariation;

    private float prevPitchVariation;

    public void resetRotations() {
        this.yawVariation = 0.0F;
        this.pitchVariation = 0.0F;
        this.prevYawVariation = 0.0F;
        this.prevPitchVariation = 0.0F;
    }

    public void calcYaw(float maxAngle, int bufferTime, float angleDecrement, float divisor, EntityLaviathan entity) {
        this.prevYawVariation = this.yawVariation;
        if (entity.f_20883_ != entity.f_20884_ && Math.abs(this.yawVariation) < maxAngle) {
            this.yawVariation = this.yawVariation + (entity.f_20884_ - entity.f_20883_) / divisor;
        }
        if (this.yawVariation > 0.7F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation -= angleDecrement;
                if (Math.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        } else if (this.yawVariation < -0.7F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation += angleDecrement;
                if (Math.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        }
    }

    public void calcPitch(float maxAngle, int bufferTime, float angleDecrement, float divisor, EntityLaviathan entity) {
        this.prevPitchVariation = this.pitchVariation;
        if (entity.m_146909_() != entity.f_19860_ && Math.abs(this.pitchVariation) < maxAngle) {
            this.pitchVariation = this.pitchVariation + (entity.f_19860_ - entity.m_146909_()) / divisor;
        }
        if (this.pitchVariation > 0.7F * angleDecrement) {
            if (this.pitchTimer > bufferTime) {
                this.pitchVariation -= angleDecrement;
                if (Mth.abs(this.pitchVariation) < angleDecrement) {
                    this.pitchVariation = 0.0F;
                    this.pitchTimer = 0;
                }
            } else {
                this.pitchTimer++;
            }
        } else if (this.pitchVariation < -0.7F * angleDecrement) {
            if (this.pitchTimer > bufferTime) {
                this.pitchVariation += angleDecrement;
                if (Mth.abs(this.pitchVariation) < angleDecrement) {
                    this.pitchVariation = 0.0F;
                    this.pitchTimer = 0;
                }
            } else {
                this.pitchTimer++;
            }
        }
    }

    public float getYawVariation(float partialTick) {
        return partialTick == 0.0F ? this.yawVariation : Mth.lerp(partialTick, this.prevYawVariation, this.yawVariation);
    }

    public float getPitchVariation(float partialTick) {
        return partialTick == 0.0F ? this.pitchVariation : Mth.lerp(partialTick, this.prevPitchVariation, this.pitchVariation);
    }
}