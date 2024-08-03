package com.simibubi.create.foundation.utility;

import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class CameraAngleAnimationService {

    private static final LerpedFloat yRotation = LerpedFloat.angular().startWithValue(0.0);

    private static final LerpedFloat xRotation = LerpedFloat.angular().startWithValue(0.0);

    private static CameraAngleAnimationService.Mode animationMode = CameraAngleAnimationService.Mode.LINEAR;

    private static float animationSpeed = -1.0F;

    public static void tick() {
        yRotation.tickChaser();
        xRotation.tickChaser();
        if (Minecraft.getInstance().player != null) {
            if (!yRotation.settled()) {
                Minecraft.getInstance().player.m_146922_(yRotation.getValue(1.0F));
            }
            if (!xRotation.settled()) {
                Minecraft.getInstance().player.m_146926_(xRotation.getValue(1.0F));
            }
        }
    }

    public static boolean isYawAnimating() {
        return !yRotation.settled();
    }

    public static boolean isPitchAnimating() {
        return !xRotation.settled();
    }

    public static float getYaw(float partialTicks) {
        return yRotation.getValue(partialTicks);
    }

    public static float getPitch(float partialTicks) {
        return xRotation.getValue(partialTicks);
    }

    public static void setAnimationMode(CameraAngleAnimationService.Mode mode) {
        animationMode = mode;
    }

    public static void setAnimationSpeed(float speed) {
        animationSpeed = speed;
    }

    public static void setYawTarget(float yaw) {
        float currentYaw = getCurrentYaw();
        yRotation.startWithValue((double) currentYaw);
        setupChaser(yRotation, currentYaw + AngleHelper.getShortestAngleDiff((double) currentYaw, (double) Mth.wrapDegrees(yaw)));
    }

    public static void setPitchTarget(float pitch) {
        float currentPitch = getCurrentPitch();
        xRotation.startWithValue((double) currentPitch);
        setupChaser(xRotation, currentPitch + AngleHelper.getShortestAngleDiff((double) currentPitch, (double) Mth.wrapDegrees(pitch)));
    }

    private static float getCurrentYaw() {
        return Minecraft.getInstance().player == null ? 0.0F : Mth.wrapDegrees(Minecraft.getInstance().player.m_146908_());
    }

    private static float getCurrentPitch() {
        return Minecraft.getInstance().player == null ? 0.0F : Mth.wrapDegrees(Minecraft.getInstance().player.m_146909_());
    }

    private static void setupChaser(LerpedFloat rotation, float target) {
        if (animationMode == CameraAngleAnimationService.Mode.LINEAR) {
            rotation.chase((double) target, animationSpeed > 0.0F ? (double) animationSpeed : 2.0, LerpedFloat.Chaser.LINEAR);
        } else if (animationMode == CameraAngleAnimationService.Mode.EXPONENTIAL) {
            rotation.chase((double) target, animationSpeed > 0.0F ? (double) animationSpeed : 0.25, LerpedFloat.Chaser.EXP);
        }
    }

    public static enum Mode {

        LINEAR, EXPONENTIAL
    }
}