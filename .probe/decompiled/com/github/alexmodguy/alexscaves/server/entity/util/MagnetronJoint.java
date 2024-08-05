package com.github.alexmodguy.alexscaves.server.entity.util;

import com.github.alexmodguy.alexscaves.server.entity.living.MagnetronEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public enum MagnetronJoint {

    SHOULDER(new Vec3(1.0, 0.5, 0.0), false), ELBOW(new Vec3(1.5, -0.5, 0.2F), false), HAND(new Vec3(1.5, -1.5, 0.4F), false), KNEE(new Vec3(0.5, -1.5, 0.0), true), FOOT(new Vec3(0.5, -3.0, 0.0), true);

    private Vec3 basePosition;

    private boolean leg;

    private MagnetronJoint(Vec3 basePosition, boolean leg) {
        this.basePosition = basePosition;
        this.leg = leg;
    }

    public Vec3 getTargetPosition(MagnetronEntity entity, boolean left) {
        Vec3 base = this.basePosition;
        float poseProgress = entity.getAttackPoseProgress(1.0F);
        float priorPoseProgress = 1.0F - poseProgress;
        if (left) {
            base = new Vec3(-this.basePosition.x, this.basePosition.y, this.basePosition.z);
        }
        base = base.add(this.animateForPose(left, entity, entity.getPrevAttackPose(), priorPoseProgress));
        base = base.add(this.animateForPose(left, entity, entity.getAttackPose(), poseProgress));
        return base.yRot((float) Math.toRadians((double) (-entity.f_20883_)));
    }

    public Vec3 animateForPose(boolean left, MagnetronEntity entity, MagnetronEntity.AttackPose pose, float progress) {
        Vec3 add = Vec3.ZERO;
        float walkSpeed = 0.2F;
        float walkDegree = 0.4F;
        if (pose == MagnetronEntity.AttackPose.NONE) {
            float limbSwing = 0.0F;
            float limbSwingAmount = 1.0F;
            if (entity.m_6084_()) {
                limbSwingAmount = entity.f_267362_.speed() * 2.0F;
                limbSwing = entity.f_267362_.position() * 2.0F;
            }
            if (this == KNEE) {
                float up = this.calculateRotation(walkSpeed, walkDegree * 0.6F, left, 0.0F, left ? -0.3F : 0.3F, limbSwing, limbSwingAmount);
                float forwards = this.calculateRotation(walkSpeed, walkDegree * 1.5F, left, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                add = new Vec3(0.0, (double) up, (double) forwards);
            } else if (this == FOOT) {
                float up = this.calculateRotation(walkSpeed, walkDegree * 1.0F, left, 1.0F, 0.0F, limbSwing, limbSwingAmount);
                float forwards = this.calculateRotation(walkSpeed, walkDegree * 2.5F, left, 0.0F, left ? -0.2F : 0.2F, limbSwing, limbSwingAmount);
                add = new Vec3(0.0, (double) up, (double) forwards);
            } else if (this == SHOULDER) {
                float up = this.calculateRotation(walkSpeed, walkDegree * -0.2F, !left, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                float forwards = this.calculateRotation(walkSpeed, walkDegree * 0.75F, !left, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                add = new Vec3(0.0, (double) up, (double) forwards);
            } else if (this == ELBOW) {
                float up = this.calculateRotation(walkSpeed, walkDegree * -0.2F, !left, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                float forwards = this.calculateRotation(walkSpeed, walkDegree * 1.5F, !left, -1.0F, 0.0F, limbSwing, limbSwingAmount);
                add = new Vec3(0.0, (double) up, (double) forwards);
            } else if (this == HAND) {
                float up = this.calculateRotation(walkSpeed, walkDegree * 0.2F, !left, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                float forwards = this.calculateRotation(walkSpeed, walkDegree * 2.2F, !left, -1.0F, 0.0F, limbSwing, limbSwingAmount);
                add = new Vec3(0.0, (double) up, (double) forwards);
            }
        } else if (pose == MagnetronEntity.AttackPose.RIGHT_PUNCH) {
            if (this == SHOULDER) {
                if (!left) {
                    add = new Vec3(0.0, -0.5, 0.5);
                } else {
                    add = new Vec3(-0.15F, 0.0, -0.5);
                }
            } else if (this == ELBOW) {
                if (!left) {
                    add = new Vec3(-1.0, -0.1F, 1.5);
                } else {
                    add = new Vec3(-0.5, 0.0, -0.7F);
                }
            } else if (this == HAND) {
                if (!left) {
                    add = new Vec3(-2.0, 0.5, 3.0);
                } else {
                    add = new Vec3(-1.0, 0.0, -0.9F);
                }
            }
        } else if (pose == MagnetronEntity.AttackPose.LEFT_PUNCH) {
            if (this == SHOULDER) {
                if (left) {
                    add = new Vec3(0.0, -0.5, 0.5);
                } else {
                    add = new Vec3(0.15F, 0.0, -0.5);
                }
            } else if (this == ELBOW) {
                if (left) {
                    add = new Vec3(1.0, -0.1F, 1.5);
                } else {
                    add = new Vec3(0.5, 0.0, -0.7F);
                }
            } else if (this == HAND) {
                if (left) {
                    add = new Vec3(2.0, 0.5, 3.0);
                } else {
                    add = new Vec3(1.0, 0.0, -0.9F);
                }
            }
        } else if (pose == MagnetronEntity.AttackPose.SLAM) {
            float f = left ? 1.0F : -1.0F;
            float piHalf = (float) (Math.PI / 2);
            Vec3 up = Vec3.ZERO;
            if (this == SHOULDER) {
                up = new Vec3((double) (0.3F * f), -0.5, -1.0);
            } else if (this == ELBOW) {
                up = new Vec3((double) (0.5F * f), -1.0, -1.0);
            } else if (this == HAND) {
                up = new Vec3((double) (1.0F * f), -2.0, -1.5);
            }
            add = up.xRot(-piHalf - progress * 2.0F * piHalf);
        }
        return add.scale((double) progress);
    }

    private float calculateRotation(float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        float rotation = Mth.cos(f * speed + offset) * degree * f1 + weight * f1;
        return invert ? -rotation : rotation;
    }
}