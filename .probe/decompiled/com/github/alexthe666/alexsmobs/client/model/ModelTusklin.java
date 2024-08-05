package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityTusklin;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelTusklin extends AdvancedEntityModel<EntityTusklin> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox leg_left;

    private final AdvancedModelBox leg_right;

    private final AdvancedModelBox torso;

    private final AdvancedModelBox arm_left;

    private final AdvancedModelBox arm_right;

    private final AdvancedModelBox head;

    private final AdvancedModelBox tusk_left;

    private final AdvancedModelBox tusk_right;

    private final AdvancedModelBox ear_left;

    private final AdvancedModelBox earLeft_r1;

    private final AdvancedModelBox ear_right;

    private final AdvancedModelBox earLeft_r2;

    private ModelAnimator animator;

    public ModelTusklin() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -18.0F, 1.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(52, 56).addBox(-8.0F, -7.0F, 0.0F, 16.0F, 15.0F, 15.0F, 0.0F, false);
        this.leg_left = new AdvancedModelBox(this, "leg_left");
        this.leg_left.setRotationPoint(6.0F, 8.0F, 13.0F);
        this.body.addChild(this.leg_left);
        this.leg_left.setTextureOffset(58, 0).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 11.0F, 7.0F, 0.0F, false);
        this.leg_right = new AdvancedModelBox(this, "leg_right");
        this.leg_right.setRotationPoint(-6.0F, 8.0F, 13.0F);
        this.body.addChild(this.leg_right);
        this.leg_right.setTextureOffset(58, 0).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 11.0F, 7.0F, 0.0F, true);
        this.torso = new AdvancedModelBox(this, "torso");
        this.torso.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.body.addChild(this.torso);
        this.torso.setTextureOffset(0, 0).addBox(-9.0F, -11.0F, -20.0F, 18.0F, 20.0F, 21.0F, 0.0F, false);
        this.torso.setTextureOffset(0, 0).addBox(0.0F, -16.0F, -20.0F, 0.0F, 5.0F, 10.0F, 0.0F, false);
        this.arm_left = new AdvancedModelBox(this, "arm_left");
        this.arm_left.setRotationPoint(5.5F, 10.0F, -14.0F);
        this.torso.addChild(this.arm_left);
        this.arm_left.setTextureOffset(0, 71).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 9.0F, 6.0F, 0.0F, false);
        this.arm_right = new AdvancedModelBox(this, "arm_right");
        this.arm_right.setRotationPoint(-5.5F, 10.0F, -14.0F);
        this.torso.addChild(this.arm_right);
        this.arm_right.setTextureOffset(0, 71).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 9.0F, 6.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -5.0F, -22.0F);
        this.torso.addChild(this.head);
        this.setRotationAngle(this.head, 0.5236F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 42).addBox(-7.0F, -3.0F, -17.0F, 14.0F, 9.0F, 19.0F, 0.0F, false);
        this.head.setTextureOffset(52, 50).addBox(0.0F, 6.0F, -16.0F, 0.0F, 3.0F, 7.0F, 0.0F, false);
        this.head.setTextureOffset(0, 42).addBox(0.0F, -10.0F, -5.0F, 0.0F, 7.0F, 7.0F, 0.0F, false);
        this.tusk_left = new AdvancedModelBox(this, "tusk_left");
        this.tusk_left.setRotationPoint(8.0F, 2.0F, -13.5F);
        this.head.addChild(this.tusk_left);
        this.tusk_left.setTextureOffset(48, 42).addBox(-1.0F, -11.0F, -1.5F, 2.0F, 11.0F, 3.0F, 0.0F, false);
        this.tusk_left.setTextureOffset(59, 42).addBox(-1.0F, -11.0F, 1.5F, 2.0F, 3.0F, 4.0F, 0.0F, false);
        this.tusk_right = new AdvancedModelBox(this, "tusk_right");
        this.tusk_right.setRotationPoint(-8.0F, 2.0F, -13.5F);
        this.head.addChild(this.tusk_right);
        this.tusk_right.setTextureOffset(48, 42).addBox(-1.0F, -11.0F, -1.5F, 2.0F, 11.0F, 3.0F, 0.0F, true);
        this.tusk_right.setTextureOffset(59, 42).addBox(-1.0F, -11.0F, 1.5F, 2.0F, 3.0F, 4.0F, 0.0F, true);
        this.ear_left = new AdvancedModelBox(this, "ear_left");
        this.ear_left.setRotationPoint(7.0F, 0.0F, -1.0F);
        this.head.addChild(this.ear_left);
        this.setRotationAngle(this.ear_left, 0.0F, 0.0F, -0.48F);
        this.earLeft_r1 = new AdvancedModelBox(this, "earLeft_r1");
        this.earLeft_r1.setRotationPoint(1.0F, 0.0F, 2.0F);
        this.ear_left.addChild(this.earLeft_r1);
        this.setRotationAngle(this.earLeft_r1, -0.3927F, 0.0F, 0.0F);
        this.earLeft_r1.setTextureOffset(68, 46).addBox(-1.0F, -1.0F, -3.0F, 1.0F, 5.0F, 4.0F, 0.0F, false);
        this.ear_right = new AdvancedModelBox(this, "ear_right");
        this.ear_right.setRotationPoint(-7.0F, 0.0F, -1.0F);
        this.head.addChild(this.ear_right);
        this.setRotationAngle(this.ear_right, 0.0F, 0.0F, 0.48F);
        this.earLeft_r2 = new AdvancedModelBox(this, "earLeft_r2");
        this.earLeft_r2.setRotationPoint(-1.0F, 0.0F, 2.0F);
        this.ear_right.addChild(this.earLeft_r2);
        this.setRotationAngle(this.earLeft_r2, -0.3927F, 0.0F, 0.0F);
        this.earLeft_r2.setTextureOffset(68, 46).addBox(0.0F, -1.0F, -3.0F, 1.0F, 5.0F, 4.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.6F;
            float f1 = 2.2F;
            float f2 = 1.4F;
            this.ear_left.setScale(f1, f1, f1);
            this.ear_right.setScale(f1, f1, f1);
            this.head.setScale(f, f, f);
            this.arm_left.setScale(1.0F, f2, 1.0F);
            this.arm_right.setScale(1.0F, f2, 1.0F);
            this.leg_left.setScale(1.0F, f2, 1.0F);
            this.leg_right.setScale(1.0F, f2, 1.0F);
            this.head.setShouldScaleChildren(true);
            this.tusk_left.showModel = false;
            this.tusk_right.showModel = false;
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.45F, 0.45F, 0.45F);
            matrixStackIn.translate(0.0, 1.6, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
            this.ear_left.setScale(1.0F, 1.0F, 1.0F);
            this.ear_right.setScale(1.0F, 1.0F, 1.0F);
            this.arm_left.setScale(1.0F, 1.0F, 1.0F);
            this.arm_right.setScale(1.0F, 1.0F, 1.0F);
            this.leg_left.setScale(1.0F, 1.0F, 1.0F);
            this.leg_right.setScale(1.0F, 1.0F, 1.0F);
        } else {
            this.tusk_left.showModel = true;
            this.tusk_right.showModel = true;
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.leg_left, this.leg_right, this.torso, this.arm_left, this.arm_right, this.head, this.tusk_left, this.tusk_right, this.earLeft_r1, this.ear_left, new AdvancedModelBox[] { this.ear_right, this.earLeft_r2 });
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.animator.update(entity);
        this.animator.setAnimation(EntityTusklin.ANIMATION_RUT);
        this.animator.startKeyframe(4);
        this.animator.move(this.head, 0.0F, 4.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.ear_left, 0.0F, 0.0F, Maths.rad(-30.0));
        this.animator.rotate(this.ear_right, 0.0F, 0.0F, Maths.rad(30.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.move(this.head, -1.0F, 3.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(30.0), 0.0F);
        this.animator.rotate(this.ear_left, 0.0F, 0.0F, Maths.rad(-30.0));
        this.animator.rotate(this.ear_right, 0.0F, 0.0F, Maths.rad(30.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.move(this.head, 1.0F, 3.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(-30.0), 0.0F);
        this.animator.rotate(this.ear_left, 0.0F, 0.0F, Maths.rad(-30.0));
        this.animator.rotate(this.ear_right, 0.0F, 0.0F, Maths.rad(30.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.move(this.head, -1.0F, 3.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(30.0), 0.0F);
        this.animator.rotate(this.ear_left, 0.0F, 0.0F, Maths.rad(-30.0));
        this.animator.rotate(this.ear_right, 0.0F, 0.0F, Maths.rad(30.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.move(this.head, 1.0F, 3.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(-30.0), 0.0F);
        this.animator.rotate(this.ear_left, 0.0F, 0.0F, Maths.rad(-30.0));
        this.animator.rotate(this.ear_right, 0.0F, 0.0F, Maths.rad(30.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.move(this.head, -1.0F, 3.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(30.0), 0.0F);
        this.animator.rotate(this.ear_left, 0.0F, 0.0F, Maths.rad(-30.0));
        this.animator.rotate(this.ear_right, 0.0F, 0.0F, Maths.rad(30.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(4);
        this.animator.setAnimation(EntityTusklin.ANIMATION_GORE_L);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, 5.0F);
        this.animator.rotate(this.leg_right, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, 2.0F, -2.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(-45.0), Maths.rad(-90.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, -2.0F);
        this.animator.rotate(this.head, Maths.rad(-20.0), Maths.rad(45.0), Maths.rad(60.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityTusklin.ANIMATION_GORE_R);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, 5.0F);
        this.animator.rotate(this.leg_right, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, 2.0F, -2.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(45.0), Maths.rad(90.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, -2.0F);
        this.animator.rotate(this.head, Maths.rad(-20.0), Maths.rad(-45.0), Maths.rad(-60.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityTusklin.ANIMATION_FLING);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 1.0F, -2.0F);
        this.animator.move(this.arm_left, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.arm_right, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.leg_right, 0.0F, 1.0F, 0.0F);
        this.animator.move(this.leg_left, 0.0F, 1.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-50.0), 0.0F, Maths.rad(20.0));
        this.animator.rotate(this.arm_left, Maths.rad(-50.0), 0.0F, Maths.rad(-20.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.head, Maths.rad(-60.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityTusklin.ANIMATION_BUCK);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, -5.0F, 3.0F);
        this.animator.move(this.arm_left, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.arm_right, 0.0F, -1.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(30.0), 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.arm_left, Maths.rad(30.0), 0.0F, Maths.rad(10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, -5.0F, -2.0F);
        this.animator.move(this.arm_left, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.arm_right, 0.0F, -1.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-60.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-20.0), 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.arm_left, Maths.rad(-20.0), 0.0F, Maths.rad(10.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(EntityTusklin entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 1.0F;
        float walkDegree = 0.8F;
        float idleSpeed = 0.125F;
        float idleDegree = 0.5F;
        if (this.f_102610_) {
            this.head.rotationPointY -= 4.0F;
            this.head.rotationPointZ += 2.0F;
        }
        this.walk(this.head, idleSpeed * 0.4F, idleDegree * 0.2F, true, 1.0F, -0.01F, ageInTicks, 1.0F);
        this.flap(this.ear_right, idleSpeed * 0.7F, idleDegree * 0.2F, false, 0.0F, -0.01F, ageInTicks, 1.0F);
        this.flap(this.ear_left, idleSpeed * 0.7F, idleDegree * 0.2F, true, 0.0F, -0.01F, ageInTicks, 1.0F);
        this.walk(this.leg_left, walkSpeed, walkDegree * 1.85F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leg_right, walkSpeed, walkDegree * 1.85F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.arm_left, walkSpeed, walkDegree * 1.85F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.arm_right, walkSpeed, walkDegree * 1.85F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree * 4.0F, true, limbSwing, limbSwingAmount);
        this.bob(this.head, walkSpeed * 0.6F, walkDegree * 2.0F, true, limbSwing, limbSwingAmount);
        this.bob(this.ear_right, walkSpeed, walkDegree * -0.75F, true, limbSwing, limbSwingAmount);
        this.bob(this.ear_left, walkSpeed, walkDegree * -0.75F, true, limbSwing, limbSwingAmount);
        if (entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
        }
    }

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}