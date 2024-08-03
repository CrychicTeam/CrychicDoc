package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityRhinoceros;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelRhinoceros extends AdvancedEntityModel<EntityRhinoceros> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox leftLeg;

    private final AdvancedModelBox rightLeg;

    private final AdvancedModelBox chest;

    private final AdvancedModelBox head;

    private final AdvancedModelBox horns;

    private final AdvancedModelBox leftEar;

    private final AdvancedModelBox rightEar;

    private final AdvancedModelBox leftArm;

    private final AdvancedModelBox rightArm;

    private final ModelAnimator animator;

    public ModelRhinoceros() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -19.0F, 4.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 44).addBox(-9.0F, -10.0F, -6.0F, 18.0F, 20.0F, 21.0F, 0.0F, false);
        this.leftLeg = new AdvancedModelBox(this, "leftLeg");
        this.leftLeg.setRotationPoint(6.0F, 9.0F, 12.0F);
        this.body.addChild(this.leftLeg);
        this.leftLeg.setTextureOffset(70, 77).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 11.0F, 9.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this, "rightLeg");
        this.rightLeg.setRotationPoint(-6.0F, 9.0F, 12.0F);
        this.body.addChild(this.rightLeg);
        this.rightLeg.setTextureOffset(70, 77).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 11.0F, 9.0F, 0.0F, true);
        this.chest = new AdvancedModelBox(this, "chest");
        this.chest.setRotationPoint(0.0F, -4.0F, -10.0F);
        this.body.addChild(this.chest);
        this.chest.setTextureOffset(0, 0).addBox(-11.0F, -10.0F, -14.0F, 22.0F, 23.0F, 20.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, 3.0F, -14.0F);
        this.chest.addChild(this.head);
        this.setRotationAngle(this.head, 0.3927F, 0.0F, 0.0F);
        this.head.setTextureOffset(76, 35).addBox(-6.0F, -6.0F, -8.0F, 12.0F, 14.0F, 9.0F, 0.0F, false);
        this.head.setTextureOffset(65, 0).addBox(-4.0F, 0.0F, -18.0F, 8.0F, 8.0F, 10.0F, 0.0F, false);
        this.horns = new AdvancedModelBox(this, "horns");
        this.horns.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addChild(this.horns);
        this.horns.setTextureOffset(0, 0).addBox(-2.0F, -12.0F, -18.0F, 4.0F, 12.0F, 5.0F, 0.0F, false);
        this.horns.setTextureOffset(0, 44).addBox(-2.0F, -4.0F, -13.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        this.leftEar = new AdvancedModelBox(this, "leftEar");
        this.leftEar.setRotationPoint(6.0F, -5.0F, -4.0F);
        this.head.addChild(this.leftEar);
        this.setRotationAngle(this.leftEar, -0.2443F, -0.2443F, 0.7679F);
        this.leftEar.setTextureOffset(0, 53).addBox(-1.0F, -5.0F, 0.0F, 3.0F, 6.0F, 1.0F, 0.0F, false);
        this.rightEar = new AdvancedModelBox(this, "rightEar");
        this.rightEar.setRotationPoint(-6.0F, -5.0F, -4.0F);
        this.head.addChild(this.rightEar);
        this.setRotationAngle(this.rightEar, -0.2443F, 0.2443F, -0.7679F);
        this.rightEar.setTextureOffset(0, 53).addBox(-2.0F, -5.0F, 0.0F, 3.0F, 6.0F, 1.0F, 0.0F, true);
        this.leftArm = new AdvancedModelBox(this, "leftArm");
        this.leftArm.setRotationPoint(7.3F, 11.0F, -8.0F);
        this.chest.addChild(this.leftArm);
        this.leftArm.setTextureOffset(79, 59).addBox(-4.0F, 2.0F, -4.0F, 7.0F, 10.0F, 7.0F, 0.0F, false);
        this.rightArm = new AdvancedModelBox(this, "rightArm");
        this.rightArm.setRotationPoint(-7.3F, 11.0F, -8.0F);
        this.chest.addChild(this.rightArm);
        this.rightArm.setTextureOffset(79, 59).addBox(-3.0F, 2.0F, -4.0F, 7.0F, 10.0F, 7.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityRhinoceros.ANIMATION_FLICK_EARS);
        this.animator.startKeyframe(2);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.rightEar, 0.0F, Maths.rad(25.0), Maths.rad(40.0));
        this.animator.rotate(this.leftEar, 0.0F, Maths.rad(-25.0), Maths.rad(-40.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.rightEar, 0.0F, 0.0F, 0.0F);
        this.animator.rotate(this.leftEar, 0.0F, 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.rightEar, 0.0F, Maths.rad(5.0), Maths.rad(-40.0));
        this.animator.rotate(this.leftEar, 0.0F, Maths.rad(-5.0), Maths.rad(40.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.rightEar, 0.0F, Maths.rad(25.0), Maths.rad(40.0));
        this.animator.rotate(this.leftEar, 0.0F, Maths.rad(-25.0), Maths.rad(-40.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.rightEar, 0.0F, 0.0F, 0.0F);
        this.animator.rotate(this.leftEar, 0.0F, 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.head, 0.0F, 0.0F, 0.0F);
        this.animator.rotate(this.rightEar, 0.0F, Maths.rad(5.0), Maths.rad(-40.0));
        this.animator.rotate(this.leftEar, 0.0F, Maths.rad(-5.0), Maths.rad(40.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(7);
        this.animator.setAnimation(EntityRhinoceros.ANIMATION_EAT_GRASS);
        this.animator.startKeyframe(5);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.eatPose();
        this.animator.move(this.head, 0.0F, 1.0F, 1.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.eatPose();
        this.animator.move(this.head, 0.0F, 1.0F, 1.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.eatPose();
        this.animator.move(this.head, 0.0F, 1.0F, 1.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityRhinoceros.ANIMATION_FLING);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 1.0F, -2.0F);
        this.animator.move(this.leftArm, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.rightArm, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.rightLeg, 0.0F, 1.0F, 0.0F);
        this.animator.move(this.leftLeg, 0.0F, 1.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.leftLeg, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.rightLeg, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.rightArm, Maths.rad(-50.0), 0.0F, Maths.rad(5.0));
        this.animator.rotate(this.leftArm, Maths.rad(-50.0), 0.0F, Maths.rad(-5.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.head, Maths.rad(-60.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityRhinoceros.ANIMATION_SLASH);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, 5.0F);
        this.animator.rotate(this.rightLeg, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.leftLeg, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.rightArm, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.leftArm, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, 0.0F, -4.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(45.0), Maths.rad(70.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, -2.0F);
        this.animator.move(this.head, 0.0F, 0.0F, -2.0F);
        this.animator.rotate(this.head, Maths.rad(-20.0), Maths.rad(-45.0), Maths.rad(-50.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.head, 0.0F, 0.0F, -4.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(-45.0), Maths.rad(-70.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, -2.0F);
        this.animator.move(this.head, 0.0F, 0.0F, -2.0F);
        this.animator.rotate(this.head, Maths.rad(-20.0), Maths.rad(45.0), Maths.rad(50.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
    }

    private void eatPose() {
        this.animator.rotate(this.body, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.rightLeg, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.leftLeg, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.rightArm, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.leftArm, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, -4.0F, -2.0F);
        this.animator.move(this.rightLeg, 0.0F, 1.8F, -2.0F);
        this.animator.move(this.leftLeg, 0.0F, 1.8F, -2.0F);
        this.animator.move(this.rightArm, 0.0F, -3.0F, 0.0F);
        this.animator.move(this.leftArm, 0.0F, -3.0F, 0.0F);
    }

    public void setupAnim(EntityRhinoceros entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 0.7F;
        float walkDegree = 0.6F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        this.walk(this.leftArm, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightArm, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree * 2.0F, true, limbSwing, limbSwingAmount);
        this.bob(this.head, walkSpeed, walkDegree * 2.0F, true, limbSwing, limbSwingAmount);
        this.walk(this.head, idleSpeed, idleDegree * 0.5F, false, 0.0F, 0.05F, ageInTicks, 1.0F);
        this.flap(this.leftEar, idleSpeed, idleDegree, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.rightEar, idleSpeed, idleDegree, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.head.rotateAngleY += netHeadYaw * 0.8F * (float) (Math.PI / 180.0);
        this.head.rotateAngleX += headPitch * (float) (Math.PI / 180.0);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.chest, this.leftArm, this.leftEar, this.leftLeg, this.rightArm, this.rightEar, this.rightLeg, this.horns);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.35F;
            float feet = 1.3F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            this.horns.showModel = false;
            this.leftArm.setScale(1.0F, feet, 1.0F);
            this.rightArm.setScale(1.0F, feet, 1.0F);
            this.leftLeg.setScale(1.0F, feet, 1.0F);
            this.rightLeg.setScale(1.0F, feet, 1.0F);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.3, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
            this.leftArm.setScale(1.0F, 1.0F, 1.0F);
            this.rightArm.setScale(1.0F, 1.0F, 1.0F);
            this.leftLeg.setScale(1.0F, 1.0F, 1.0F);
            this.rightLeg.setScale(1.0F, 1.0F, 1.0F);
            this.horns.showModel = true;
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }
}