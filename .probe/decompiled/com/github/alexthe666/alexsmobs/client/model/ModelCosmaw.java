package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCosmaw;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class ModelCosmaw extends AdvancedEntityModel<EntityCosmaw> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox leftArm;

    public final AdvancedModelBox rightArm;

    public final AdvancedModelBox leftFin;

    public final AdvancedModelBox rightFin;

    public final AdvancedModelBox mouthArm1;

    public final AdvancedModelBox mouthArm2;

    public final AdvancedModelBox mouth;

    public final AdvancedModelBox topJaw;

    public final AdvancedModelBox lowerJaw;

    public final AdvancedModelBox eyesBase;

    public final AdvancedModelBox leftEye;

    public final AdvancedModelBox rightEye;

    public final AdvancedModelBox tail;

    public final AdvancedModelBox leftLeg;

    public final AdvancedModelBox rightLeg;

    public final AdvancedModelBox tailFin;

    public ModelCosmaw() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-6.5F, -7.0F, -20.0F, 13.0F, 14.0F, 32.0F, 0.0F, false);
        this.leftArm = new AdvancedModelBox(this, "leftArm");
        this.leftArm.setRotationPoint(5.0F, 7.0F, -12.2F);
        this.body.addChild(this.leftArm);
        this.leftArm.setTextureOffset(17, 47).addBox(-1.0F, 0.0F, -0.8F, 2.0F, 4.0F, 3.0F, 0.0F, false);
        this.leftArm.setTextureOffset(0, 13).addBox(0.0F, 2.0F, 1.2F, 0.0F, 4.0F, 3.0F, 0.0F, false);
        this.rightArm = new AdvancedModelBox(this, "rightArm");
        this.rightArm.setRotationPoint(-5.0F, 7.0F, -12.2F);
        this.body.addChild(this.rightArm);
        this.rightArm.setTextureOffset(17, 47).addBox(-1.0F, 0.0F, -0.8F, 2.0F, 4.0F, 3.0F, 0.0F, true);
        this.rightArm.setTextureOffset(0, 13).addBox(0.0F, 2.0F, 1.2F, 0.0F, 4.0F, 3.0F, 0.0F, true);
        this.leftFin = new AdvancedModelBox(this, "leftFin");
        this.leftFin.setRotationPoint(4.5F, -6.0F, 0.0F);
        this.body.addChild(this.leftFin);
        this.leftFin.setTextureOffset(33, 47).addBox(0.0F, 0.0F, -5.0F, 9.0F, 0.0F, 27.0F, 0.0F, false);
        this.rightFin = new AdvancedModelBox(this, "rightFin");
        this.rightFin.setRotationPoint(-4.5F, -6.0F, 0.0F);
        this.body.addChild(this.rightFin);
        this.rightFin.setTextureOffset(33, 47).addBox(-9.0F, 0.0F, -5.0F, 9.0F, 0.0F, 27.0F, 0.0F, true);
        this.mouthArm1 = new AdvancedModelBox(this, "mouthArm1");
        this.mouthArm1.setRotationPoint(0.0F, -3.0F, -20.0F);
        this.body.addChild(this.mouthArm1);
        this.setRotationAngle(this.mouthArm1, 1.0908F, 0.0F, 0.0F);
        this.mouthArm1.setTextureOffset(65, 75).addBox(-2.0F, -1.0F, -20.0F, 4.0F, 4.0F, 22.0F, 0.0F, false);
        this.mouthArm2 = new AdvancedModelBox(this, "mouthArm2");
        this.mouthArm2.setRotationPoint(0.0F, 3.0F, -20.0F);
        this.mouthArm1.addChild(this.mouthArm2);
        this.setRotationAngle(this.mouthArm2, -1.0472F, 0.0F, 0.0F);
        this.mouthArm2.setTextureOffset(79, 32).addBox(-2.0F, -4.0F, -17.0F, 4.0F, 4.0F, 17.0F, -0.1F, false);
        this.mouth = new AdvancedModelBox(this, "mouth");
        this.mouth.setRotationPoint(0.0F, -1.4F, -16.7F);
        this.mouthArm2.addChild(this.mouth);
        this.topJaw = new AdvancedModelBox(this, "topJaw");
        this.topJaw.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.mouth.addChild(this.topJaw);
        this.topJaw.setTextureOffset(0, 13).addBox(-2.5F, -3.0F, -7.0F, 5.0F, 3.0F, 8.0F, 0.0F, false);
        this.lowerJaw = new AdvancedModelBox(this, "lowerJaw");
        this.lowerJaw.setRotationPoint(0.0F, -1.0F, 0.3F);
        this.mouth.addChild(this.lowerJaw);
        this.lowerJaw.setTextureOffset(0, 0).addBox(-3.0F, 0.0F, -9.0F, 6.0F, 3.0F, 9.0F, 0.0F, false);
        this.eyesBase = new AdvancedModelBox(this, "eyesBase");
        this.eyesBase.setRotationPoint(0.0F, -7.0F, -11.0F);
        this.body.addChild(this.eyesBase);
        this.eyesBase.setTextureOffset(3, 69).addBox(-11.0F, -1.0F, -1.0F, 23.0F, 2.0F, 2.0F, 0.0F, false);
        this.leftEye = new AdvancedModelBox(this, "leftEye");
        this.leftEye.setRotationPoint(13.0F, 0.0F, 0.0F);
        this.eyesBase.addChild(this.leftEye);
        this.leftEye.setTextureOffset(0, 47).addBox(-1.0F, -3.0F, -3.0F, 2.0F, 6.0F, 6.0F, 0.0F, false);
        this.rightEye = new AdvancedModelBox(this, "rightEye");
        this.rightEye.setRotationPoint(-12.0F, 0.0F, 1.0F);
        this.eyesBase.addChild(this.rightEye);
        this.rightEye.setTextureOffset(0, 47).addBox(-1.0F, -3.0F, -4.0F, 2.0F, 6.0F, 6.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -1.8F, 11.6F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(59, 0).addBox(-4.5F, -5.0F, 0.0F, 9.0F, 11.0F, 20.0F, 0.0F, false);
        this.leftLeg = new AdvancedModelBox(this, "leftLeg");
        this.leftLeg.setRotationPoint(3.0F, 5.8F, 3.2F);
        this.tail.addChild(this.leftLeg);
        this.leftLeg.setTextureOffset(19, 13).addBox(-1.0F, 0.0F, -0.8F, 2.0F, 4.0F, 3.0F, 0.0F, false);
        this.leftLeg.setTextureOffset(0, 0).addBox(0.0F, 2.0F, 1.2F, 0.0F, 4.0F, 3.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this, "rightLeg");
        this.rightLeg.setRotationPoint(-3.0F, 5.8F, 3.2F);
        this.tail.addChild(this.rightLeg);
        this.rightLeg.setTextureOffset(19, 13).addBox(-1.0F, 0.0F, -0.8F, 2.0F, 4.0F, 3.0F, 0.0F, true);
        this.rightLeg.setTextureOffset(0, 0).addBox(0.0F, 2.0F, 1.2F, 0.0F, 4.0F, 3.0F, 0.0F, true);
        this.tailFin = new AdvancedModelBox(this, "tailFin");
        this.tailFin.setRotationPoint(0.0F, 1.0F, 7.0F);
        this.tail.addChild(this.tailFin);
        this.tailFin.setTextureOffset(0, 47).addBox(0.0F, -10.0F, -3.0F, 0.0F, 19.0F, 32.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.tail, this.tailFin, this.body, this.leftLeg, this.rightLeg, this.eyesBase, this.leftEye, this.rightEye, this.leftArm, this.leftFin, this.rightArm, new AdvancedModelBox[] { this.rightFin, this.mouth, this.mouthArm1, this.mouthArm2, this.lowerJaw, this.topJaw });
    }

    public void setupAnim(EntityCosmaw entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 0.7F;
        float walkDegree = 0.4F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.2F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float clutchProgress = entity.prevClutchProgress + (entity.clutchProgress - entity.prevClutchProgress) * partialTick;
        float biteProgress = entity.prevBiteProgress + (entity.biteProgress - entity.prevBiteProgress) * partialTick;
        float openProgress = Math.max(Math.max(entity.prevOpenProgress + (entity.openProgress - entity.prevOpenProgress) * partialTick, clutchProgress), biteProgress);
        float cosmawPitch = (float) (Math.toRadians((double) entity.getClampedCosmawPitch(partialTick)) * (double) (5.0F - clutchProgress) * 0.2F);
        float cosmawPitchPos = entity.getClampedCosmawPitch(partialTick) / 90.0F * (5.0F - clutchProgress) * 0.2F;
        this.body.rotateAngleX += cosmawPitch;
        this.eyesBase.rotateAngleX -= cosmawPitch;
        this.mouthArm1.rotateAngleX -= cosmawPitch * 0.2F;
        this.mouthArm2.rotateAngleX -= cosmawPitch * 1.7F;
        this.lowerJaw.rotateAngleX -= cosmawPitch * 0.3F;
        this.topJaw.rotateAngleX -= cosmawPitch * 0.3F;
        if (cosmawPitchPos > 0.0F) {
            this.mouthArm2.rotationPointY = this.mouthArm2.rotationPointY - Math.min(cosmawPitchPos * 6.0F, 3.0F);
        } else {
            this.mouthArm2.rotationPointZ -= cosmawPitchPos * 3.0F;
            this.mouthArm2.rotationPointY += cosmawPitchPos;
        }
        this.walk(this.body, idleSpeed, idleDegree * 0.1F, false, -1.0F, 0.05F, ageInTicks, 1.0F);
        this.walk(this.tail, idleSpeed, idleDegree * -0.15F, true, -1.0F, 0.05F, ageInTicks, 1.0F);
        this.swing(this.leftFin, idleSpeed, idleDegree * 0.22F, false, -2.0F, 0.05F, ageInTicks, 1.0F);
        this.swing(this.rightFin, idleSpeed, idleDegree * 0.22F, true, -2.0F, 0.05F, ageInTicks, 1.0F);
        this.walk(this.eyesBase, idleSpeed, idleDegree * 0.1F, true, -1.0F, 0.05F, ageInTicks, 1.0F);
        this.walk(this.lowerJaw, idleSpeed, idleDegree * 0.2F, true, 1.0F, -0.05F, ageInTicks, 1.0F);
        this.walk(this.topJaw, idleSpeed, idleDegree * 0.2F, true, 1.0F, 0.05F, ageInTicks, 1.0F);
        this.walk(this.mouthArm1, idleSpeed, idleDegree * 0.4F, false, 2.0F, 0.05F, ageInTicks, 1.0F);
        this.walk(this.mouthArm2, idleSpeed, idleDegree * 0.6F, true, 2.0F, 0.05F, ageInTicks, 1.0F);
        this.bob(this.body, idleSpeed * 2.0F, idleDegree * 4.0F, false, ageInTicks, 1.0F);
        this.walk(this.leftArm, idleSpeed, idleDegree * 0.3F, false, -2.0F, -0.05F, ageInTicks, 1.0F);
        this.walk(this.rightArm, idleSpeed, idleDegree * 0.3F, false, -2.0F, -0.05F, ageInTicks, 1.0F);
        this.walk(this.leftLeg, idleSpeed, idleDegree * 0.3F, false, -3.0F, -0.05F, ageInTicks, 1.0F);
        this.walk(this.rightLeg, idleSpeed, idleDegree * 0.3F, false, -3.0F, -0.05F, ageInTicks, 1.0F);
        this.swing(this.tail, walkSpeed, walkDegree * 0.5F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tailFin, walkSpeed, walkDegree * 0.25F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.leftFin, walkSpeed, walkDegree * 0.7F, false, -2.0F, 0.05F, limbSwing, limbSwingAmount);
        this.flap(this.rightFin, walkSpeed, walkDegree * 0.7F, true, -2.0F, 0.05F, limbSwing, limbSwingAmount);
        this.walk(this.leftArm, walkSpeed, walkDegree * 0.3F, false, -2.0F, -0.05F, limbSwing, limbSwingAmount);
        this.walk(this.rightArm, walkSpeed, walkDegree * 0.3F, false, -2.0F, -0.05F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree * 4.0F, false, limbSwing, limbSwingAmount);
        this.progressRotationPrev(this.topJaw, openProgress, Maths.rad(-30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.lowerJaw, openProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, clutchProgress, Maths.rad(-30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.eyesBase, clutchProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.mouthArm1, clutchProgress, Maths.rad(-5.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.mouthArm2, clutchProgress, Maths.rad(120.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.mouthArm2, clutchProgress, 0.0F, -2.0F, 3.0F, 5.0F);
        this.progressPositionPrev(this.body, clutchProgress, 0.0F, -10.0F, 33.0F, 5.0F);
        this.progressPositionPrev(this.body, biteProgress, 0.0F, 0.0F, 20.0F, 5.0F);
        this.progressRotationPrev(this.mouthArm1, biteProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.mouthArm2, biteProgress, Maths.rad(50.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftArm, biteProgress, Maths.rad(10.0), 0.0F, Maths.rad(-30.0), 5.0F);
        this.progressRotationPrev(this.rightArm, biteProgress, Maths.rad(10.0), 0.0F, Maths.rad(30.0), 5.0F);
        float eyeYaw = Mth.clamp(netHeadYaw, -40.0F, 40.0F) / (180.0F / (float) Math.PI);
        this.eyesBase.rotateAngleY += eyeYaw * 0.35F;
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.5F;
            this.eyesBase.setScale(f, f, f);
            this.eyesBase.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.eyesBase.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }
}