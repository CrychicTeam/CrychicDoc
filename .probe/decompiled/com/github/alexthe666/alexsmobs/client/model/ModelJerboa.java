package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityJerboa;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelJerboa extends AdvancedEntityModel<EntityJerboa> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox leftEar;

    private final AdvancedModelBox rightEar;

    private final AdvancedModelBox leftArm;

    private final AdvancedModelBox rightArm;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox legs;

    private final AdvancedModelBox feet;

    public ModelJerboa() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 19).addBox(-2.5F, -2.0F, -4.0F, 5.0F, 4.0F, 8.0F, 0.0F, false);
        this.leftEar = new AdvancedModelBox(this, "leftEar");
        this.leftEar.setRotationPoint(2.0F, -1.0F, -2.0F);
        this.body.addChild(this.leftEar);
        this.setRotationAngle(this.leftEar, -0.6981F, -0.6545F, 1.0036F);
        this.leftEar.setTextureOffset(0, 0).addBox(-2.0F, -5.0F, 0.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
        this.rightEar = new AdvancedModelBox(this, "rightEar");
        this.rightEar.setRotationPoint(-2.0F, -1.0F, -2.0F);
        this.body.addChild(this.rightEar);
        this.setRotationAngle(this.rightEar, -0.6981F, 0.6545F, -1.0036F);
        this.rightEar.setTextureOffset(0, 0).addBox(-2.0F, -5.0F, 0.0F, 4.0F, 5.0F, 0.0F, 0.0F, true);
        this.leftArm = new AdvancedModelBox(this, "leftArm");
        this.leftArm.setRotationPoint(1.0F, 2.0F, -2.0F);
        this.body.addChild(this.leftArm);
        this.setRotationAngle(this.leftArm, 1.309F, 0.0F, 0.0F);
        this.leftArm.setTextureOffset(0, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
        this.rightArm = new AdvancedModelBox(this, "rightArm");
        this.rightArm.setRotationPoint(-1.0F, 2.0F, -2.0F);
        this.body.addChild(this.rightArm);
        this.setRotationAngle(this.rightArm, 1.309F, 0.0F, 0.0F);
        this.rightArm.setTextureOffset(0, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, 1.0F, 4.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, -0.3491F, 0.0F, 0.0F);
        this.tail.setTextureOffset(0, 0).addBox(-1.5F, -6.0F, 0.0F, 3.0F, 6.0F, 12.0F, 0.0F, false);
        this.legs = new AdvancedModelBox(this, "legs");
        this.legs.setRotationPoint(0.0F, 1.9F, 2.8F);
        this.body.addChild(this.legs);
        this.setRotationAngle(this.legs, 0.5236F, 0.0F, 0.0F);
        this.legs.setTextureOffset(19, 0).addBox(-2.0F, 0.0F, -5.0F, 4.0F, 3.0F, 5.0F, 0.0F, false);
        this.feet = new AdvancedModelBox(this, "feet");
        this.feet.setRotationPoint(0.0F, 3.0F, -5.0F);
        this.legs.addChild(this.feet);
        this.setRotationAngle(this.feet, -0.5236F, 0.0F, 0.0F);
        this.feet.setTextureOffset(19, 9).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 0.0F, 2.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.leftEar, this.rightEar, this.leftArm, this.rightArm, this.tail, this.legs, this.feet);
    }

    public void setupAnim(EntityJerboa entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float idleSpeed = 0.1F;
        float idleDegree = 0.2F;
        float sleepProgress = entity.prevSleepProgress + (entity.sleepProgress - entity.prevSleepProgress) * partialTicks;
        float reboundProgress = entity.prevReboundProgress + (entity.reboundProgress - entity.prevReboundProgress) * partialTicks;
        float jumpProgress = Math.max(0.0F, entity.prevJumpProgress + (entity.jumpProgress - entity.prevJumpProgress) * partialTicks - reboundProgress);
        float begProgress = entity.prevBegProgress + (entity.begProgress - entity.prevBegProgress) * partialTicks;
        this.walk(this.leftArm, idleSpeed, idleDegree, true, 2.0F, 0.3F, ageInTicks, 1.0F);
        this.walk(this.rightArm, idleSpeed, idleDegree, true, 2.0F, 0.3F, ageInTicks, 1.0F);
        this.walk(this.tail, idleSpeed, idleDegree * 0.5F, true, 1.0F, -0.05F, ageInTicks, 1.0F);
        this.walk(this.leftEar, idleSpeed, idleDegree * 0.5F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.rightEar, idleSpeed, idleDegree * 0.5F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.progressRotationPrev(this.legs, jumpProgress, Maths.rad(65.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, jumpProgress, Maths.rad(-5.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, jumpProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, jumpProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legs, reboundProgress, Maths.rad(-30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, reboundProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, reboundProgress, Maths.rad(35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftEar, reboundProgress, 0.0F, Maths.rad(35.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightEar, reboundProgress, 0.0F, Maths.rad(-35.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.body, reboundProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, sleepProgress, 0.0F, 5.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.legs, sleepProgress, 0.0F, -2.2F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legs, sleepProgress, Maths.rad(-30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, sleepProgress, Maths.rad(50.0), 0.0F, Maths.rad(90.0), 5.0F);
        this.progressRotationPrev(this.leftEar, sleepProgress, 0.0F, Maths.rad(35.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightEar, sleepProgress, 0.0F, Maths.rad(-35.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftArm, begProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightArm, begProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        if (begProgress > 0.0F) {
            float f = this.body.rotateAngleX;
            this.walk(this.body, 0.7F, 0.1F, false, 2.0F, -0.7F, ageInTicks, begProgress * 0.2F);
            float f1 = this.body.rotateAngleX - f;
            this.legs.rotateAngleX -= f1;
            this.tail.rotateAngleX -= f1;
            this.legs.rotationPointY += f1 * 3.0F;
            this.walk(this.rightArm, 0.7F, 1.2F, false, 0.0F, -1.0F, ageInTicks, begProgress * 0.2F);
            this.flap(this.rightArm, 0.7F, 0.25F, false, -1.0F, 0.2F, ageInTicks, begProgress * 0.2F);
            this.walk(this.leftArm, 0.7F, 1.2F, false, 0.0F, -1.0F, ageInTicks, begProgress * 0.2F);
            this.flap(this.leftArm, 0.7F, 0.25F, true, -1.0F, 0.2F, ageInTicks, begProgress * 0.2F);
        }
        float headY = netHeadYaw * 0.35F * (float) (Math.PI / 180.0);
        float headZ = headPitch * 0.65F * (float) (Math.PI / 180.0);
        if (Math.max(sleepProgress, begProgress) == 0.0F) {
            this.body.rotateAngleY += headY;
            this.legs.rotateAngleY -= headY * 0.6F;
            this.body.rotateAngleX += headZ;
            this.legs.rotateAngleX -= headZ;
            this.tail.rotateAngleX -= headZ * 1.2F;
            if (headPitch > 0.0F) {
                this.body.rotationPointY = this.body.rotationPointY + Math.abs(headPitch) * 0.015F;
                this.legs.rotationPointZ = this.legs.rotationPointZ - Math.abs(headPitch) * 0.02F;
            } else {
                this.legs.rotationPointY = this.legs.rotationPointY - Math.abs(headPitch) * 0.0225F;
                this.legs.rotationPointZ = this.legs.rotationPointZ - Math.abs(headPitch) * 0.015F;
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.75F;
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.65F, 0.65F, 0.65F);
            matrixStackIn.translate(0.0, 0.815, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}