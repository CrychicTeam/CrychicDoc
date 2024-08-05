package com.mojang.blaze3d.vertex;

import com.google.common.collect.Queues;
import java.util.Deque;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class PoseStack {

    private final Deque<PoseStack.Pose> poseStack = Util.make(Queues.newArrayDeque(), p_85848_ -> {
        Matrix4f $$1 = new Matrix4f();
        Matrix3f $$2 = new Matrix3f();
        p_85848_.add(new PoseStack.Pose($$1, $$2));
    });

    public void translate(double double0, double double1, double double2) {
        this.translate((float) double0, (float) double1, (float) double2);
    }

    public void translate(float float0, float float1, float float2) {
        PoseStack.Pose $$3 = (PoseStack.Pose) this.poseStack.getLast();
        $$3.pose.translate(float0, float1, float2);
    }

    public void scale(float float0, float float1, float float2) {
        PoseStack.Pose $$3 = (PoseStack.Pose) this.poseStack.getLast();
        $$3.pose.scale(float0, float1, float2);
        if (float0 == float1 && float1 == float2) {
            if (float0 > 0.0F) {
                return;
            }
            $$3.normal.scale(-1.0F);
        }
        float $$4 = 1.0F / float0;
        float $$5 = 1.0F / float1;
        float $$6 = 1.0F / float2;
        float $$7 = Mth.fastInvCubeRoot($$4 * $$5 * $$6);
        $$3.normal.scale($$7 * $$4, $$7 * $$5, $$7 * $$6);
    }

    public void mulPose(Quaternionf quaternionf0) {
        PoseStack.Pose $$1 = (PoseStack.Pose) this.poseStack.getLast();
        $$1.pose.rotate(quaternionf0);
        $$1.normal.rotate(quaternionf0);
    }

    public void rotateAround(Quaternionf quaternionf0, float float1, float float2, float float3) {
        PoseStack.Pose $$4 = (PoseStack.Pose) this.poseStack.getLast();
        $$4.pose.rotateAround(quaternionf0, float1, float2, float3);
        $$4.normal.rotate(quaternionf0);
    }

    public void pushPose() {
        PoseStack.Pose $$0 = (PoseStack.Pose) this.poseStack.getLast();
        this.poseStack.addLast(new PoseStack.Pose(new Matrix4f($$0.pose), new Matrix3f($$0.normal)));
    }

    public void popPose() {
        this.poseStack.removeLast();
    }

    public PoseStack.Pose last() {
        return (PoseStack.Pose) this.poseStack.getLast();
    }

    public boolean clear() {
        return this.poseStack.size() == 1;
    }

    public void setIdentity() {
        PoseStack.Pose $$0 = (PoseStack.Pose) this.poseStack.getLast();
        $$0.pose.identity();
        $$0.normal.identity();
    }

    public void mulPoseMatrix(Matrix4f matrixF0) {
        ((PoseStack.Pose) this.poseStack.getLast()).pose.mul(matrixF0);
    }

    public static final class Pose {

        final Matrix4f pose;

        final Matrix3f normal;

        Pose(Matrix4f matrixF0, Matrix3f matrixF1) {
            this.pose = matrixF0;
            this.normal = matrixF1;
        }

        public Matrix4f pose() {
            return this.pose;
        }

        public Matrix3f normal() {
            return this.normal;
        }
    }
}