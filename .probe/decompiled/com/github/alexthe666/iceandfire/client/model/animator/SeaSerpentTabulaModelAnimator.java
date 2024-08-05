package com.github.alexthe666.iceandfire.client.model.animator;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.client.model.util.EnumSeaSerpentAnimations;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.client.Minecraft;

public class SeaSerpentTabulaModelAnimator extends IceAndFireTabulaModelAnimator implements ITabulaModelAnimator<EntitySeaSerpent> {

    public TabulaModel[] swimPose = new TabulaModel[] { EnumSeaSerpentAnimations.SWIM1.seaserpent_model, EnumSeaSerpentAnimations.SWIM3.seaserpent_model, EnumSeaSerpentAnimations.SWIM4.seaserpent_model, EnumSeaSerpentAnimations.SWIM6.seaserpent_model };

    public SeaSerpentTabulaModelAnimator() {
        super(EnumSeaSerpentAnimations.T_POSE.seaserpent_model);
    }

    public void setRotationAngles(TabulaModel model, EntitySeaSerpent entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        model.resetToDefaultPose();
        model.getCube("BodyUpper").rotationPointY += 9.0F;
        model.llibAnimator.update(entity);
        this.animate(model, entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale);
        int currentIndex = entity.swimCycle / 10;
        int prevIndex = currentIndex - 1;
        if (prevIndex < 0) {
            prevIndex = 3;
        }
        TabulaModel prevPosition = this.swimPose[prevIndex];
        TabulaModel currentPosition = this.swimPose[currentIndex];
        float partialTicks = Minecraft.getInstance().getFrameTime();
        float delta = (float) entity.swimCycle / 10.0F % 1.0F + partialTicks / 10.0F;
        for (AdvancedModelBox cube : model.getCubes().values()) {
            if (entity.jumpProgress > 0.0F && !this.isRotationEqual(cube, EnumSeaSerpentAnimations.JUMPING2.seaserpent_model.getCube(cube.boxName))) {
                this.transitionTo(cube, EnumSeaSerpentAnimations.JUMPING2.seaserpent_model.getCube(cube.boxName), entity.jumpProgress, 5.0F, false);
            }
            if (entity.wantJumpProgress > 0.0F && !this.isRotationEqual(cube, EnumSeaSerpentAnimations.JUMPING1.seaserpent_model.getCube(cube.boxName))) {
                this.transitionTo(cube, EnumSeaSerpentAnimations.JUMPING1.seaserpent_model.getCube(cube.boxName), entity.wantJumpProgress, 10.0F, false);
            }
            AdvancedModelBox prevPositionCube = prevPosition.getCube(cube.boxName);
            AdvancedModelBox currPositionCube = currentPosition.getCube(cube.boxName);
            float prevX = prevPositionCube.rotateAngleX;
            float prevY = prevPositionCube.rotateAngleY;
            float prevZ = prevPositionCube.rotateAngleZ;
            float x = currPositionCube.rotateAngleX;
            float y = currPositionCube.rotateAngleY;
            float z = currPositionCube.rotateAngleZ;
            this.addToRotateAngle(cube, limbSwingAmount, prevX + delta * this.distance(prevX, x), prevY + delta * this.distance(prevY, y), prevZ + delta * this.distance(prevZ, z));
        }
        if (entity.breathProgress > 0.0F) {
            this.progressRotation(model.getCube("Head"), entity.breathProgress, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
            this.progressRotation(model.getCube("HeadFront"), entity.breathProgress, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
            this.progressRotation(model.getCube("Jaw"), entity.breathProgress, (float) Math.toRadians(60.0), 0.0F, 0.0F);
        }
        if (entity.jumpRot > 0.0F) {
            float jumpRot = entity.prevJumpRot + (entity.jumpRot - entity.prevJumpRot) * partialTicks;
            float turn = (float) entity.m_20184_().y * -4.0F;
            AdvancedModelBox var28 = model.getCube("BodyUpper");
            var28.rotateAngleX = var28.rotateAngleX + (float) Math.toRadians((double) (22.5F * turn)) * jumpRot;
            var28 = model.getCube("Tail1");
            var28.rotateAngleX = var28.rotateAngleX - (float) Math.toRadians((double) turn) * jumpRot;
            var28 = model.getCube("Tail2");
            var28.rotateAngleX = var28.rotateAngleX - (float) Math.toRadians((double) turn) * jumpRot;
            var28 = model.getCube("Tail3");
            var28.rotateAngleX = var28.rotateAngleX - (float) Math.toRadians((double) turn) * jumpRot;
            var28 = model.getCube("Tail4");
            var28.rotateAngleX = var28.rotateAngleX - (float) Math.toRadians((double) turn) * jumpRot;
        }
        float prevRenderOffset = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTicks;
        AdvancedModelBox var33 = model.getCube("Tail1");
        var33.rotateAngleY = var33.rotateAngleY + (entity.getPieceYaw(1, partialTicks) - prevRenderOffset) * (float) (Math.PI / 180.0);
        var33 = model.getCube("Tail2");
        var33.rotateAngleY = var33.rotateAngleY + (entity.getPieceYaw(2, partialTicks) - prevRenderOffset) * (float) (Math.PI / 180.0);
        var33 = model.getCube("Tail3");
        var33.rotateAngleY = var33.rotateAngleY + (entity.getPieceYaw(3, partialTicks) - prevRenderOffset) * (float) (Math.PI / 180.0);
        var33 = model.getCube("Tail4");
        var33.rotateAngleY = var33.rotateAngleY + (entity.getPieceYaw(4, partialTicks) - prevRenderOffset) * (float) (Math.PI / 180.0);
        model.getCube("BodyUpper").rotateAngleX -= rotationPitch * (float) (Math.PI / 180.0);
        if (!entity.isJumpingOutOfWater() || entity.m_20069_()) {
            var33 = model.getCube("Tail1");
            var33.rotateAngleX = var33.rotateAngleX - (entity.getPiecePitch(1, partialTicks) - 0.0F) * (float) (Math.PI / 180.0);
            var33 = model.getCube("Tail2");
            var33.rotateAngleX = var33.rotateAngleX - (entity.getPiecePitch(2, partialTicks) - 0.0F) * (float) (Math.PI / 180.0);
            var33 = model.getCube("Tail3");
            var33.rotateAngleX = var33.rotateAngleX - (entity.getPiecePitch(3, partialTicks) - 0.0F) * (float) (Math.PI / 180.0);
            var33 = model.getCube("Tail4");
            var33.rotateAngleX = var33.rotateAngleX - (entity.getPiecePitch(4, partialTicks) - 0.0F) * (float) (Math.PI / 180.0);
        }
    }

    public void progressRotation(AdvancedModelBox model, float progress, float rotX, float rotY, float rotZ) {
        model.rotateAngleX = model.rotateAngleX + progress * (rotX - model.defaultRotationX) / 20.0F;
        model.rotateAngleY = model.rotateAngleY + progress * (rotY - model.defaultRotationY) / 20.0F;
        model.rotateAngleZ = model.rotateAngleZ + progress * (rotZ - model.defaultRotationZ) / 20.0F;
    }

    private void animate(TabulaModel model, EntitySeaSerpent entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        if (model.llibAnimator.setAnimation(EntitySeaSerpent.ANIMATION_SPEAK)) {
            model.llibAnimator.startKeyframe(5);
            this.rotate(model.llibAnimator, model.getCube("Jaw"), 25.0F, 0.0F, 0.0F);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.setStaticKeyframe(5);
            model.llibAnimator.resetKeyframe(5);
        }
        if (model.llibAnimator.setAnimation(EntitySeaSerpent.ANIMATION_BITE)) {
            model.llibAnimator.startKeyframe(5);
            this.moveToPose(model, EnumSeaSerpentAnimations.BITE1.seaserpent_model);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(5);
            this.moveToPose(model, EnumSeaSerpentAnimations.BITE2.seaserpent_model);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.setStaticKeyframe(2);
            model.llibAnimator.resetKeyframe(3);
        }
        if (model.llibAnimator.setAnimation(EntitySeaSerpent.ANIMATION_ROAR)) {
            model.llibAnimator.startKeyframe(10);
            this.moveToPose(model, EnumSeaSerpentAnimations.ROAR1.seaserpent_model);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            this.moveToPose(model, EnumSeaSerpentAnimations.ROAR2.seaserpent_model);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            this.moveToPose(model, EnumSeaSerpentAnimations.ROAR3.seaserpent_model);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.resetKeyframe(10);
        }
    }
}