package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntitySkunk;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelSkunk extends AdvancedEntityModel<EntitySkunk> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox leftLeg;

    private final AdvancedModelBox rightLeg;

    private final AdvancedModelBox leftArm;

    private final AdvancedModelBox rightArm;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox head;

    public ModelSkunk() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-3.5F, -4.0F, -4.5F, 7.0F, 6.0F, 9.0F, 0.0F, false);
        this.leftLeg = new AdvancedModelBox(this, "leftLeg");
        this.leftLeg.setRotationPoint(4.0F, 2.0F, 4.0F);
        this.body.addChild(this.leftLeg);
        this.setRotationAngle(this.leftLeg, 0.0F, -0.7418F, 0.0F);
        this.leftLeg.setTextureOffset(0, 33).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this, "rightLeg");
        this.rightLeg.setRotationPoint(-4.0F, 2.0F, 4.0F);
        this.body.addChild(this.rightLeg);
        this.setRotationAngle(this.rightLeg, 0.0F, 0.7418F, 0.0F);
        this.rightLeg.setTextureOffset(0, 33).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 4.0F, 0.0F, true);
        this.leftArm = new AdvancedModelBox(this, "leftArm");
        this.leftArm.setRotationPoint(3.5F, 2.0F, -3.0F);
        this.body.addChild(this.leftArm);
        this.setRotationAngle(this.leftArm, 0.0F, -0.5672F, 0.0F);
        this.leftArm.setTextureOffset(32, 31).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        this.rightArm = new AdvancedModelBox(this, "rightArm");
        this.rightArm.setRotationPoint(-3.5F, 2.0F, -3.0F);
        this.body.addChild(this.rightArm);
        this.setRotationAngle(this.rightArm, 0.0F, 0.5672F, 0.0F);
        this.rightArm.setTextureOffset(32, 31).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 4.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -1.0F, 4.5F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 16).addBox(-3.0F, -10.0F, 0.0F, 6.0F, 12.0F, 4.0F, 0.0F, false);
        this.tail.setTextureOffset(21, 16).addBox(-3.0F, -10.0F, 4.0F, 6.0F, 7.0F, 5.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, 0.0F, -5.5F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(24, 0).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 4.0F, 0.0F, false);
        this.head.setTextureOffset(21, 29).addBox(-2.0F, 0.0F, -6.0F, 4.0F, 2.0F, 3.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setupAnim(EntitySkunk entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.1F;
        float idleDegree = 0.15F;
        float walkSpeed = 1.25F;
        float walkDegree = 0.5F;
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float sprayProgress = entity.prevSprayProgress + (entity.sprayProgress - entity.prevSprayProgress) * partialTicks;
        float legsStill = Math.max(sprayProgress * 0.2F, limbSwingAmount);
        this.progressRotationPrev(this.leftArm, sprayProgress, Maths.rad(80.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightArm, sprayProgress, Maths.rad(80.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftLeg, sprayProgress, Maths.rad(100.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightLeg, sprayProgress, Maths.rad(100.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, sprayProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, sprayProgress, 0.0F, -2.4F, 0.0F, 5.0F);
        this.progressPositionPrev(this.tail, sprayProgress, 0.0F, -2.0F, -1.0F, 5.0F);
        this.walk(this.body, 0.5F, 0.2F, true, 4.0F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.swing(this.body, 0.5F, 0.2F, true, 1.5F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.walk(this.head, 0.5F, 0.2F, false, 4.0F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.swing(this.head, 0.5F, 0.2F, false, 1.5F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.walk(this.leftArm, 0.5F, 0.2F, false, 4.0F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.swing(this.leftArm, 0.5F, 0.2F, false, 1.5F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.walk(this.rightArm, 0.5F, 0.2F, false, 4.0F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.swing(this.rightArm, 0.5F, 0.2F, false, 1.5F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.walk(this.leftLeg, 0.5F, 0.2F, false, 4.0F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.swing(this.leftLeg, 0.5F, 0.2F, false, 1.5F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.walk(this.rightLeg, 0.5F, 0.2F, false, 4.0F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.swing(this.rightLeg, 0.5F, 0.2F, false, 1.5F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.flap(this.tail, 0.5F, 0.5F, false, 2.5F, 0.0F, ageInTicks, sprayProgress * 0.2F);
        this.walk(this.tail, idleSpeed, idleDegree, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.progressRotationPrev(this.leftArm, Math.min(legsStill, 0.5F), 0.0F, Maths.rad(30.0), 0.0F, 0.5F);
        this.progressRotationPrev(this.rightArm, Math.min(legsStill, 0.5F), 0.0F, Maths.rad(-30.0), 0.0F, 0.5F);
        this.progressRotationPrev(this.leftLeg, Math.min(legsStill, 0.5F), 0.0F, Maths.rad(40.0), 0.0F, 0.5F);
        this.progressRotationPrev(this.rightLeg, Math.min(legsStill, 0.5F), 0.0F, Maths.rad(-40.0), 0.0F, 0.5F);
        this.progressPositionPrev(this.head, Math.min(legsStill, 0.5F), 0.0F, -1.0F, 0.0F, 0.5F);
        this.swing(this.body, walkSpeed, walkDegree * 0.5F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.head, walkSpeed, walkDegree * 0.5F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail, walkSpeed, walkDegree * 0.5F, false, 4.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.tail, walkSpeed, walkDegree * 0.2F, true, 2.0F, 0.3F, limbSwing, limbSwingAmount);
        this.walk(this.leftArm, walkSpeed, walkDegree * 1.2F, true, -2.5F, -0.2F, limbSwing, limbSwingAmount);
        this.walk(this.rightArm, walkSpeed, walkDegree * 1.2F, false, -2.5F, 0.2F, limbSwing, limbSwingAmount);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 1.2F, true, -2.5F, -0.2F, limbSwing, limbSwingAmount);
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, false, -2.5F, 0.2F, limbSwing, limbSwingAmount);
        this.flap(this.body, walkSpeed, walkSpeed * 0.3F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.rightLeg, walkSpeed, walkSpeed * 0.3F, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.leftLeg, walkSpeed, walkSpeed * 0.3F, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.rightArm, walkSpeed, walkSpeed * 0.3F, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.leftArm, walkSpeed, walkSpeed * 0.3F, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.head, walkSpeed, walkSpeed * 0.3F, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.tail, walkSpeed, walkSpeed * 0.2F, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.faceTarget(netHeadYaw, headPitch, 1.2F, new AdvancedModelBox[] { this.head });
        float leftLegS = (float) (Math.sin((double) (limbSwing * walkSpeed) - 2.5) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
        float rightLegS = (float) (Math.sin(-((double) (limbSwing * walkSpeed)) + 2.5) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
        this.rightArm.rotationPointY += 3.0F * leftLegS;
        this.leftArm.rotationPointY += 3.0F * rightLegS;
        this.rightArm.rotationPointZ += 1.0F * leftLegS;
        this.leftArm.rotationPointZ += 1.0F * rightLegS;
        this.leftLeg.rotationPointY += 3.0F * leftLegS;
        this.rightLeg.rotationPointY += 3.0F * rightLegS;
        this.leftLeg.rotationPointZ += 1.0F * leftLegS;
        this.rightLeg.rotationPointZ += 1.0F * rightLegS;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg, this.tail, this.head);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            this.head.setScale(1.5F, 1.5F, 1.5F);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.65F, 0.65F, 0.65F);
            matrixStackIn.translate(0.0, 0.815, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        } else {
            this.head.setScale(1.0F, 1.0F, 1.0F);
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }
}