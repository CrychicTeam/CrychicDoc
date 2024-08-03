package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class ChainBuffer {

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

    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, LivingEntity entity) {
        this.prevYawVariation = this.yawVariation;
        if (entity.yBodyRot != entity.yBodyRotO && Mth.abs(entity.yBodyRotO - entity.yBodyRot) < 0.1F && Mth.abs(this.yawVariation) < maxAngle) {
            this.yawVariation = this.yawVariation + (entity.yBodyRotO - entity.yBodyRot) / divisor;
        }
        if (this.yawVariation > 0.7F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation -= angleDecrement;
                if (Mth.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        } else if (this.yawVariation < -0.7F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation += angleDecrement;
                if (Mth.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        }
    }

    public void calculateChainWaveBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, LivingEntity entity) {
        this.prevPitchVariation = this.pitchVariation;
        if (entity.m_146909_() != entity.f_19860_ && Mth.abs(this.pitchVariation) < maxAngle) {
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

    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, LivingEntity entity) {
        this.calculateChainSwingBuffer(maxAngle, bufferTime, angleDecrement, 1.0F, entity);
    }

    public void calculateChainWaveBuffer(float maxAngle, int bufferTime, float angleDecrement, LivingEntity entity) {
        this.calculateChainWaveBuffer(maxAngle, bufferTime, angleDecrement, 1.0F, entity);
    }

    public void applyChainSwingBuffer(BasicModelPart... boxes) {
        float rotateAmount = (float) (Math.PI / 180.0) * Mth.lerp(this.getPartialTicks(), this.prevYawVariation, this.yawVariation) / (float) boxes.length;
        for (BasicModelPart box : boxes) {
            box.rotateAngleY += rotateAmount;
        }
    }

    private float getPartialTicks() {
        return Minecraft.getInstance().getFrameTime();
    }

    public void applyChainWaveBuffer(BasicModelPart... boxes) {
        float rotateAmount = (float) (Math.PI / 180.0) * Mth.lerp(this.getPartialTicks(), this.prevYawVariation, this.yawVariation) / (float) boxes.length;
        for (BasicModelPart box : boxes) {
            box.rotateAngleX += rotateAmount;
        }
    }
}