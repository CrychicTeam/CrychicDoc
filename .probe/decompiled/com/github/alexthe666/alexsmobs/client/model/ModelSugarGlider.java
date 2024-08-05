package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntitySugarGlider;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelSugarGlider extends AdvancedEntityModel<EntitySugarGlider> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox leftArm;

    private final AdvancedModelBox rightArm;

    private final AdvancedModelBox leftLeg;

    private final AdvancedModelBox rightLeg;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox head;

    private final AdvancedModelBox leftEar;

    private final AdvancedModelBox rightEar;

    public ModelSugarGlider() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -2.0F, -1.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 3.0F, 7.0F, 0.0F, false);
        this.leftArm = new AdvancedModelBox(this, "leftArm");
        this.leftArm.setRotationPoint(1.0F, 1.0F, -3.0F);
        this.body.addChild(this.leftArm);
        this.setRotationAngle(this.leftArm, 0.0F, 0.0F, 0.20944F);
        this.leftArm.setTextureOffset(12, 11).addBox(-1.0F, 0.0F, -2.0F, 6.0F, 0.0F, 6.0F, 0.0F, false);
        this.rightArm = new AdvancedModelBox(this, "rightArm");
        this.rightArm.setRotationPoint(-1.0F, 1.0F, -3.0F);
        this.body.addChild(this.rightArm);
        this.setRotationAngle(this.rightArm, 0.0F, 0.0F, -0.20944F);
        this.rightArm.setTextureOffset(12, 11).addBox(-5.0F, 0.0F, -2.0F, 6.0F, 0.0F, 6.0F, 0.0F, true);
        this.leftLeg = new AdvancedModelBox(this, "leftLeg");
        this.leftLeg.setRotationPoint(1.0F, 1.0F, 3.0F);
        this.body.addChild(this.leftLeg);
        this.setRotationAngle(this.leftLeg, 0.0F, 0.0F, 0.20944F);
        this.leftLeg.setTextureOffset(15, 0).addBox(-1.0F, 0.0F, -2.0F, 6.0F, 0.0F, 5.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this, "rightLeg");
        this.rightLeg.setRotationPoint(-1.0F, 1.0F, 3.0F);
        this.body.addChild(this.rightLeg);
        this.setRotationAngle(this.rightLeg, 0.0F, 0.0F, -0.20944F);
        this.rightLeg.setTextureOffset(15, 0).addBox(-5.0F, 0.0F, -2.0F, 6.0F, 0.0F, 5.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, 0.0F, 4.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 11).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, 0.0F, -3.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(17, 18).addBox(-2.5F, -2.0F, -4.0F, 5.0F, 4.0F, 4.0F, 0.0F, false);
        this.head.setTextureOffset(0, 22).addBox(-1.5F, 0.0F, -5.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        this.leftEar = new AdvancedModelBox(this, "leftEar");
        this.leftEar.setRotationPoint(2.2F, -1.6F, -2.9F);
        this.head.addChild(this.leftEar);
        this.setRotationAngle(this.leftEar, 0.0F, -0.6109F, 0.0F);
        this.leftEar.setTextureOffset(0, 0).addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        this.rightEar = new AdvancedModelBox(this, "rightEar");
        this.rightEar.setRotationPoint(-2.2F, -1.6F, -2.9F);
        this.head.addChild(this.rightEar);
        this.setRotationAngle(this.rightEar, 0.0F, 0.6109F, 0.0F);
        this.rightEar.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.leftArm, this.rightArm, this.leftEar, this.rightEar, this.tail, this.leftLeg, this.rightLeg);
    }

    public void setupAnim(EntitySugarGlider entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.1F;
        float idleDegree = 0.25F;
        float walkSpeed = 0.9F;
        float walkDegree = 0.5F;
        float glideSpeed = 1.3F;
        float glideDegree = 0.6F;
        float partialTick = ageInTicks - (float) entityIn.f_19797_;
        float glideProgress = entityIn.prevGlideProgress + (entityIn.glideProgress - entityIn.prevGlideProgress) * partialTick;
        float sitProgress = entityIn.prevSitProgress + (entityIn.sitProgress - entityIn.prevSitProgress) * partialTick;
        float forageProgress = entityIn.forageProgress + (entityIn.forageProgress - entityIn.prevForageProgress) * partialTick;
        float glideSwingAmount = glideProgress * 0.2F;
        float walkSwingAmount = (1.0F - glideSwingAmount) * limbSwingAmount;
        this.progressRotationPrev(this.body, glideProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, glideProgress, Maths.rad(12.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, glideProgress, Maths.rad(12.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftArm, glideProgress, 0.0F, 0.0F, Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.leftLeg, glideProgress, 0.0F, 0.0F, Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.rightArm, glideProgress, 0.0F, 0.0F, Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.rightLeg, glideProgress, 0.0F, 0.0F, Maths.rad(20.0), 5.0F);
        this.progressPositionPrev(this.body, glideProgress, 0.0F, -2.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.leftArm, glideProgress, 2.0F, 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.rightArm, glideProgress, -2.0F, 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leftLeg, glideProgress, 2.0F, 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.rightLeg, glideProgress, -2.0F, 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, forageProgress, Maths.rad(35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, forageProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, forageProgress, 0.0F, -1.0F, 1.0F, 5.0F);
        this.progressRotationPrev(this.body, sitProgress, Maths.rad(-170.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, sitProgress, Maths.rad(-50.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, sitProgress, Maths.rad(150.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftArm, sitProgress, 0.0F, 0.0F, Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.leftLeg, sitProgress, 0.0F, 0.0F, Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.rightArm, sitProgress, 0.0F, 0.0F, Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.rightLeg, sitProgress, 0.0F, 0.0F, Maths.rad(-20.0), 5.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 1.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.head, sitProgress, 0.0F, 2.0F, -2.0F, 5.0F);
        this.flap(this.rightEar, idleSpeed, idleDegree, false, 0.0F, -0.05F, ageInTicks, 1.0F);
        this.flap(this.leftEar, idleSpeed, idleDegree, true, 0.0F, -0.05F, ageInTicks, 1.0F);
        this.swing(this.leftArm, walkSpeed, walkDegree, false, 1.5F, -0.2F, limbSwing, walkSwingAmount);
        this.swing(this.leftLeg, walkSpeed, walkDegree, true, 1.5F, -0.2F, limbSwing, walkSwingAmount);
        this.swing(this.rightArm, walkSpeed, walkDegree, false, 1.5F, -0.2F, limbSwing, walkSwingAmount);
        this.swing(this.rightLeg, walkSpeed, walkDegree, true, 1.5F, -0.2F, limbSwing, walkSwingAmount);
        this.swing(this.tail, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, walkSwingAmount);
        this.bob(this.head, walkSpeed * 0.5F, walkDegree, true, limbSwing, walkSwingAmount);
        this.flap(this.leftArm, glideSpeed, glideDegree * 0.1F, true, 0.0F, -0.05F, ageInTicks, glideSwingAmount);
        this.flap(this.leftLeg, glideSpeed, glideDegree * 0.1F, true, 0.0F, -0.05F, ageInTicks, glideSwingAmount);
        this.flap(this.rightArm, glideSpeed, glideDegree * 0.1F, false, 0.0F, 0.05F, ageInTicks, glideSwingAmount);
        this.flap(this.rightLeg, glideSpeed, glideDegree * 0.1F, false, 0.0F, 0.05F, ageInTicks, glideSwingAmount);
        this.swing(this.head, glideSpeed * 0.2F, glideDegree * 0.4F, false, 0.0F, 0.0F, ageInTicks, glideSwingAmount);
        this.swing(this.body, glideSpeed * 0.2F, glideDegree * 0.4F, true, 1.0F, 0.0F, ageInTicks, glideSwingAmount);
        this.swing(this.tail, glideSpeed * 0.2F, glideDegree, true, -1.0F, 0.0F, ageInTicks, glideSwingAmount);
        this.bob(this.head, 1.0F, 0.6F, false, ageInTicks, forageProgress * 0.2F);
        this.swing(this.head, 0.5F, 0.6F, true, -1.0F, 0.0F, ageInTicks, forageProgress * 0.2F);
        if (forageProgress == 0.0F) {
            this.faceTarget(netHeadYaw, headPitch, 1.2F, new AdvancedModelBox[] { this.head });
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.35F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            this.head.setScale(1.0F, 1.0F, 1.0F);
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}