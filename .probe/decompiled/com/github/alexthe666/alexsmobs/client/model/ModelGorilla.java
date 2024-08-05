package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityGorilla;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class ModelGorilla extends AdvancedEntityModel<EntityGorilla> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox chest;

    public final AdvancedModelBox head;

    public final AdvancedModelBox foreheadDK_r1;

    public final AdvancedModelBox mouth;

    public final AdvancedModelBox leftArm;

    public final AdvancedModelBox rightArm;

    public final AdvancedModelBox leftLeg;

    public final AdvancedModelBox rightLeg;

    public final ModelAnimator animator;

    public ModelGorilla() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -14.0F, 3.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 23).addBox(-4.5F, -4.0F, -3.0F, 9.0F, 9.0F, 7.0F, 0.0F, false);
        this.chest = new AdvancedModelBox(this, "chest");
        this.chest.setRotationPoint(0.0F, 4.0F, -3.0F);
        this.body.addChild(this.chest);
        this.chest.setTextureOffset(0, 0).addBox(-6.5F, -8.0F, -11.0F, 13.0F, 11.0F, 11.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -8.0F, -11.0F);
        this.chest.addChild(this.head);
        this.head.setTextureOffset(22, 48).addBox(-3.0F, -5.0F, -4.0F, 6.0F, 7.0F, 7.0F, 0.0F, false);
        this.head.setTextureOffset(38, 0).addBox(-3.0F, -6.0F, -4.0F, 6.0F, 1.0F, 7.0F, 0.0F, false);
        this.head.setTextureOffset(0, 40).addBox(-3.0F, -8.0F, -5.0F, 6.0F, 4.0F, 8.0F, 0.0F, false);
        this.foreheadDK_r1 = new AdvancedModelBox(this, "foreheadDK_r1");
        this.foreheadDK_r1.setRotationPoint(0.0F, -6.0F, -2.0F);
        this.head.addChild(this.foreheadDK_r1);
        this.setRotationAngle(this.foreheadDK_r1, 0.0F, 0.2182F, 0.0F);
        this.foreheadDK_r1.setTextureOffset(0, 56).addBox(0.0F, -6.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, false);
        this.mouth = new AdvancedModelBox(this, "mouth");
        this.mouth.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.head.addChild(this.mouth);
        this.mouth.setTextureOffset(49, 9).addBox(-2.0F, -2.0F, -5.0F, 4.0F, 5.0F, 4.0F, 0.0F, false);
        this.leftArm = new AdvancedModelBox(this, "leftArm");
        this.leftArm.setRotationPoint(6.0F, -6.5F, -9.5F);
        this.chest.addChild(this.leftArm);
        this.leftArm.setTextureOffset(33, 23).addBox(-3.0F, -2.5F, -2.5F, 6.0F, 19.0F, 5.0F, 0.0F, false);
        this.rightArm = new AdvancedModelBox(this, "rightArm");
        this.rightArm.setRotationPoint(-6.0F, -6.5F, -9.5F);
        this.chest.addChild(this.rightArm);
        this.rightArm.setTextureOffset(33, 23).addBox(-3.0F, -2.5F, -2.5F, 6.0F, 19.0F, 5.0F, 0.0F, true);
        this.leftLeg = new AdvancedModelBox(this, "leftLeg");
        this.leftLeg.setRotationPoint(3.0F, 4.5F, 2.5F);
        this.body.addChild(this.leftLeg);
        this.leftLeg.setTextureOffset(49, 48).addBox(-2.5F, 0.5F, -2.5F, 5.0F, 9.0F, 5.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this, "rightLeg");
        this.rightLeg.setRotationPoint(-3.0F, 4.5F, 2.5F);
        this.body.addChild(this.rightLeg);
        this.rightLeg.setTextureOffset(49, 48).addBox(-2.5F, 0.5F, -2.5F, 5.0F, 9.0F, 5.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityGorilla.ANIMATION_BREAKBLOCK_R);
        this.animator.startKeyframe(7);
        this.animator.rotate(this.chest, 0.0F, Maths.rad(-10.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.rightArm, Maths.rad(65.0), Maths.rad(-20.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, 0.0F, Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(0.0));
        this.animator.rotate(this.rightArm, Maths.rad(-80.0), Maths.rad(-30.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(6);
        this.animator.setAnimation(EntityGorilla.ANIMATION_BREAKBLOCK_L);
        this.animator.startKeyframe(7);
        this.animator.rotate(this.chest, 0.0F, Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.leftArm, Maths.rad(65.0), Maths.rad(20.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(0.0));
        this.animator.rotate(this.leftArm, Maths.rad(-80.0), Maths.rad(30.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(6);
        this.animator.setAnimation(EntityGorilla.ANIMATION_POUNDCHEST);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.rightArm, Maths.rad(-60.0), 0.0F, Maths.rad(20.0));
        this.animator.rotate(this.leftArm, Maths.rad(-60.0), 0.0F, Maths.rad(-20.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.rightArm, 0.0F, 5.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(0.0), Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.chest, 0.0F, 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.rightArm, Maths.rad(-60.0), 0.0F, Maths.rad(-80.0));
        this.animator.rotate(this.leftArm, Maths.rad(-60.0), 0.0F, Maths.rad(-40.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.leftArm, 0.0F, 5.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(0.0), Maths.rad(-10.0), 0.0F);
        this.animator.rotate(this.chest, 0.0F, 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.rightArm, Maths.rad(-60.0), 0.0F, Maths.rad(20.0));
        this.animator.rotate(this.leftArm, Maths.rad(-60.0), 0.0F, Maths.rad(60.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.rightArm, 0.0F, 5.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(0.0), Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.rightArm, Maths.rad(-60.0), 0.0F, Maths.rad(-80.0));
        this.animator.rotate(this.leftArm, Maths.rad(-60.0), 0.0F, Maths.rad(-40.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.leftArm, 0.0F, 5.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(0.0), Maths.rad(-10.0), 0.0F);
        this.animator.rotate(this.chest, 0.0F, 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.rightArm, Maths.rad(-60.0), 0.0F, Maths.rad(20.0));
        this.animator.rotate(this.leftArm, Maths.rad(-60.0), 0.0F, Maths.rad(60.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.rightArm, 0.0F, 5.0F, 0.0F);
        this.animator.rotate(this.chest, 0.0F, 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.head, Maths.rad(0.0), Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.rightArm, Maths.rad(-60.0), 0.0F, Maths.rad(-80.0));
        this.animator.rotate(this.leftArm, Maths.rad(-60.0), 0.0F, Maths.rad(-40.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.leftArm, 0.0F, 5.0F, 0.0F);
        this.animator.rotate(this.chest, 0.0F, 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.head, Maths.rad(0.0), Maths.rad(-10.0), 0.0F);
        this.animator.rotate(this.rightArm, Maths.rad(-60.0), 0.0F, Maths.rad(20.0));
        this.animator.rotate(this.leftArm, Maths.rad(-60.0), 0.0F, Maths.rad(60.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityGorilla.ANIMATION_ATTACK);
        this.animator.startKeyframe(7);
        this.animator.rotate(this.leftArm, Maths.rad(65.0), Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.rightArm, Maths.rad(65.0), Maths.rad(-10.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.leftArm, Maths.rad(-90.0), Maths.rad(30.0), 0.0F);
        this.animator.rotate(this.rightArm, Maths.rad(-90.0), Maths.rad(-30.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(6);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.35F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setupAnim(EntityGorilla entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 0.7F;
        float walkDegree = 0.5F;
        float eatSpeed = 0.8F;
        float eatDegree = 0.3F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float sitProgress = entityIn.prevSitProgress + (entityIn.sitProgress - entityIn.prevSitProgress) * partialTick;
        float standProgress = entityIn.prevStandProgress + (entityIn.standProgress - entityIn.prevStandProgress) * partialTick;
        float rideProgress = entityIn.m_20159_() && entityIn.m_6162_() ? 5.0F : 0.0F;
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
        this.progressRotationPrev(this.leftArm, rideProgress, Maths.rad(-20.0), Maths.rad(-20.0), Maths.rad(-40.0), 5.0F);
        this.progressRotationPrev(this.rightArm, rideProgress, Maths.rad(-20.0), Maths.rad(20.0), Maths.rad(40.0), 5.0F);
        this.progressRotationPrev(this.leftLeg, rideProgress, Maths.rad(-20.0), 0.0F, Maths.rad(-80.0), 5.0F);
        this.progressRotationPrev(this.rightLeg, rideProgress, Maths.rad(-20.0), 0.0F, Maths.rad(80.0), 5.0F);
        this.progressRotationPrev(this.head, rideProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, rideProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, rideProgress, 0.0F, 5.0F, 3.0F, 5.0F);
        this.progressRotationPrev(this.body, sitProgress, Maths.rad(-80.0), 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.rightLeg, sitProgress, Maths.rad(-10.0), Maths.rad(-30.0), Maths.rad(30.0), 10.0F);
        this.progressRotationPrev(this.leftLeg, sitProgress, Maths.rad(-10.0), Maths.rad(30.0), Maths.rad(-30.0), 10.0F);
        this.progressRotationPrev(this.head, sitProgress, Maths.rad(80.0), 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.leftArm, sitProgress, Maths.rad(20.0), 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.rightArm, sitProgress, Maths.rad(20.0), 0.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 8.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.head, sitProgress, 0.0F, 4.0F, -2.0F, 10.0F);
        this.progressPositionPrev(this.leftArm, sitProgress, 0.0F, 0.0F, 2.0F, 10.0F);
        this.progressPositionPrev(this.rightArm, sitProgress, 0.0F, 0.0F, 2.0F, 10.0F);
        this.progressRotationPrev(this.body, standProgress, Maths.rad(-80.0), 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.rightLeg, standProgress, Maths.rad(80.0), 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.leftLeg, standProgress, Maths.rad(80.0), 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.head, standProgress, Maths.rad(80.0), 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.leftArm, standProgress, Maths.rad(80.0), 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.rightArm, standProgress, Maths.rad(80.0), 0.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.body, standProgress, 0.0F, 1.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.rightLeg, standProgress, -1.0F, -3.0F, 1.2F, 10.0F);
        this.progressPositionPrev(this.leftLeg, standProgress, 1.0F, -3.0F, 1.2F, 10.0F);
        this.progressPositionPrev(this.leftArm, standProgress, 2.0F, 1.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.rightArm, standProgress, -2.0F, 1.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.head, standProgress, 0.0F, 4.0F, -2.0F, 10.0F);
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftArm, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightArm, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.body, walkSpeed, walkDegree * 0.2F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        if (entityIn.isEating()) {
            this.walk(this.rightArm, eatSpeed, eatDegree, false, 1.0F, -0.3F, ageInTicks, 1.0F);
            this.walk(this.leftArm, eatSpeed, eatDegree, false, 1.0F, -0.3F, ageInTicks, 1.0F);
            this.walk(this.chest, eatSpeed, eatDegree * 0.1F, false, 2.0F, 0.3F, ageInTicks, 1.0F);
            this.walk(this.head, eatSpeed, eatDegree * 0.3F, true, 1.0F, 0.3F, ageInTicks, 1.0F);
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.chest, this.head, this.foreheadDK_r1, this.mouth, this.leftArm, this.rightArm, this.rightLeg, this.leftLeg);
    }
}