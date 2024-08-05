package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCaiman;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelCaiman extends AdvancedEntityModel<EntityCaiman> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox leftLeg;

    private final AdvancedModelBox leftFoot;

    private final AdvancedModelBox rightLeg;

    private final AdvancedModelBox rightFoot;

    private final AdvancedModelBox leftArm;

    private final AdvancedModelBox leftHand;

    private final AdvancedModelBox rightArm;

    private final AdvancedModelBox rightHand;

    private final AdvancedModelBox tail1;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox tail3;

    private final AdvancedModelBox head;

    private final AdvancedModelBox bottomJaw;

    private final AdvancedModelBox topJaw;

    public ModelCaiman() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-4.0F, -3.0F, -8.0F, 8.0F, 6.0F, 16.0F, 0.0F, false);
        this.leftLeg = new AdvancedModelBox(this);
        this.leftLeg.setRotationPoint(4.0F, 1.0F, 6.0F);
        this.body.addChild(this.leftLeg);
        this.leftLeg.setTextureOffset(56, 13).addBox(-1.0F, -1.0F, -4.0F, 3.0F, 4.0F, 5.0F, 0.0F, false);
        this.leftFoot = new AdvancedModelBox(this);
        this.leftFoot.setRotationPoint(1.0F, 3.0F, -3.0F);
        this.leftLeg.addChild(this.leftFoot);
        this.leftFoot.setTextureOffset(26, 55).addBox(-2.0F, -0.01F, -4.0F, 5.0F, 0.0F, 5.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this);
        this.rightLeg.setRotationPoint(-4.0F, 1.0F, 6.0F);
        this.body.addChild(this.rightLeg);
        this.rightLeg.setTextureOffset(56, 13).addBox(-2.0F, -1.0F, -4.0F, 3.0F, 4.0F, 5.0F, 0.0F, true);
        this.rightFoot = new AdvancedModelBox(this);
        this.rightFoot.setRotationPoint(-1.0F, 3.0F, -3.0F);
        this.rightLeg.addChild(this.rightFoot);
        this.rightFoot.setTextureOffset(26, 55).addBox(-3.0F, -0.01F, -4.0F, 5.0F, 0.0F, 5.0F, 0.0F, true);
        this.leftArm = new AdvancedModelBox(this);
        this.leftArm.setRotationPoint(4.2F, 0.2F, -5.5F);
        this.body.addChild(this.leftArm);
        this.leftArm.setTextureOffset(0, 0).addBox(-1.2F, -1.2F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, false);
        this.leftHand = new AdvancedModelBox(this);
        this.leftHand.setRotationPoint(0.3F, 3.8F, -0.5F);
        this.leftArm.addChild(this.leftHand);
        this.leftHand.setTextureOffset(55, 39).addBox(-2.5F, -0.01F, -4.0F, 5.0F, 0.0F, 5.0F, 0.0F, false);
        this.rightArm = new AdvancedModelBox(this);
        this.rightArm.setRotationPoint(-4.2F, 0.2F, -5.5F);
        this.body.addChild(this.rightArm);
        this.rightArm.setTextureOffset(0, 0).addBox(-1.8F, -1.2F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, true);
        this.rightHand = new AdvancedModelBox(this);
        this.rightHand.setRotationPoint(-0.3F, 3.8F, -0.5F);
        this.rightArm.addChild(this.rightHand);
        this.rightHand.setTextureOffset(55, 39).addBox(-2.5F, -0.01F, -4.0F, 5.0F, 0.0F, 5.0F, 0.0F, true);
        this.tail1 = new AdvancedModelBox(this);
        this.tail1.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.body.addChild(this.tail1);
        this.tail1.setTextureOffset(0, 23).addBox(-3.0F, -2.0F, 1.0F, 6.0F, 5.0F, 11.0F, 0.0F, false);
        this.tail1.setTextureOffset(24, 23).addBox(-2.0F, -3.0F, 7.0F, 4.0F, 1.0F, 5.0F, 0.0F, false);
        this.tail1.setTextureOffset(50, 28).addBox(-4.0F, -2.0F, 1.0F, 8.0F, 0.0F, 6.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this);
        this.tail2.setRotationPoint(0.0F, 1.0F, 12.0F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(39, 13).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 10.0F, 0.0F, false);
        this.tail2.setTextureOffset(43, 45).addBox(-1.5F, -4.0F, 0.0F, 3.0F, 2.0F, 10.0F, 0.0F, false);
        this.tail3 = new AdvancedModelBox(this);
        this.tail3.setRotationPoint(0.0F, 0.0F, 10.0F);
        this.tail2.addChild(this.tail3);
        this.tail3.setTextureOffset(15, 55).addBox(0.0F, -3.0F, 0.0F, 0.0F, 5.0F, 10.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, 0.0F, -10.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 52).addBox(-3.5F, -5.0F, -3.0F, 7.0F, 7.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(58, 0).addBox(-2.5F, -6.0F, -3.0F, 5.0F, 1.0F, 3.0F, 0.0F, false);
        this.bottomJaw = new AdvancedModelBox(this);
        this.bottomJaw.setRotationPoint(0.0F, -3.0F, -2.0F);
        this.head.addChild(this.bottomJaw);
        this.bottomJaw.setTextureOffset(0, 40).addBox(-3.0F, 0.0F, -10.0F, 6.0F, 2.0F, 9.0F, 0.0F, false);
        this.bottomJaw.setTextureOffset(22, 44).addBox(-3.0F, -1.0F, -10.0F, 6.0F, 1.0F, 9.0F, 0.0F, false);
        this.topJaw = new AdvancedModelBox(this);
        this.topJaw.setRotationPoint(0.0F, -4.0F, -3.0F);
        this.head.addChild(this.topJaw);
        this.topJaw.setTextureOffset(33, 0).addBox(-3.5F, -1.0F, -10.0F, 7.0F, 2.0F, 10.0F, 0.0F, false);
        this.topJaw.setTextureOffset(25, 30).addBox(-3.5F, 1.0F, -10.0F, 7.0F, 3.0F, 10.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.bottomJaw, this.head, this.topJaw, this.tail1, this.tail2, this.tail3, this.leftLeg, this.leftFoot, this.rightLeg, this.rightFoot, new AdvancedModelBox[] { this.rightArm, this.rightHand, this.leftArm, this.leftHand });
    }

    public void setupAnim(EntityCaiman entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.05F;
        float idleDegree = 0.1F;
        float walkSpeed = 1.0F;
        float walkDegree = 1.0F;
        float swimSpeed = 0.65F;
        float swimDegree = 0.65F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float swimProgress = (entity.prevSwimProgress + (entity.swimProgress - entity.prevSwimProgress) * partialTick) * 0.2F;
        float grabProgress = entity.prevHoldProgress + (entity.holdProgress - entity.prevHoldProgress) * partialTick;
        float vibrateProgress = (entity.prevVibrateProgress + (entity.vibrateProgress - entity.prevVibrateProgress) * partialTick) * 0.2F;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTick;
        float walkAmount = (1.0F - swimProgress) * limbSwingAmount;
        float swimAmount = swimProgress * limbSwingAmount;
        this.progressRotationPrev(this.rightArm, swimProgress, Maths.rad(75.0), 0.0F, Maths.rad(60.0), 1.0F);
        this.progressRotationPrev(this.leftArm, swimProgress, Maths.rad(75.0), 0.0F, Maths.rad(-60.0), 1.0F);
        this.progressRotationPrev(this.rightLeg, swimProgress, Maths.rad(75.0), 0.0F, Maths.rad(60.0), 1.0F);
        this.progressRotationPrev(this.leftLeg, swimProgress, Maths.rad(75.0), 0.0F, Maths.rad(-60.0), 1.0F);
        this.progressPositionPrev(this.head, swimAmount, 0.0F, 2.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.head, entity.holdProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.bottomJaw, grabProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.topJaw, grabProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.bottomJaw, grabProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, sitProgress, Maths.rad(10.0), Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.body, sitProgress, 0.0F, Maths.rad(10.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.tail1, sitProgress, -1.0F, 0.0F, -1.0F, 5.0F);
        this.progressRotationPrev(this.tail1, sitProgress, 0.0F, Maths.rad(40.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.tail2, sitProgress, 0.0F, Maths.rad(40.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.tail3, sitProgress, 0.0F, Maths.rad(50.0), 0.0F, 5.0F);
        this.bob(this.head, idleSpeed, idleDegree * 5.0F, false, ageInTicks, 1.0F);
        this.bob(this.body, 20.0F, 0.5F, false, ageInTicks, vibrateProgress);
        this.swing(this.body, 20.0F, 0.04F, false, 3.0F, 0.0F, ageInTicks, vibrateProgress);
        this.swing(this.head, 0.5F, 0.4F, true, 2.0F, 0.0F, ageInTicks, grabProgress * 0.2F);
        this.swing(this.body, 0.5F, 0.4F, false, 2.0F, 0.0F, ageInTicks, grabProgress * 0.2F);
        this.swing(this.tail1, 0.5F, 0.4F, false, 4.0F, 0.0F, ageInTicks, grabProgress * 0.2F);
        this.swing(this.tail2, 0.5F, 0.4F, false, 3.0F, 0.0F, ageInTicks, grabProgress * 0.2F);
        this.head.rotationPointX = this.head.rotationPointX + this.walkValue(ageInTicks, grabProgress * 0.2F, 0.5F, 2.0F, 2.0F, false);
        this.swing(this.tail1, idleSpeed, idleDegree, false, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail2, idleSpeed, idleDegree, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail3, idleSpeed, idleDegree, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.body, walkSpeed, walkDegree * 0.1F, true, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.head, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.leftLeg, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.rightLeg, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.leftArm, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.rightArm, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.tail1, walkSpeed, walkDegree * 0.1F, true, -1.0F, 0.0F, limbSwing, walkAmount);
        this.swing(this.tail1, walkSpeed, walkDegree * 0.3F, false, 0.0F, 0.0F, limbSwing, walkAmount);
        this.swing(this.tail2, walkSpeed, walkDegree * 0.3F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.swing(this.tail3, walkSpeed, walkDegree * 0.3F, false, -1.0F, 0.0F, limbSwing, walkAmount);
        this.bob(this.head, walkSpeed, walkDegree * -1.0F, false, limbSwing, walkAmount);
        float bodyBob = this.walkValue(limbSwing, walkAmount, walkSpeed, 0.5F, 1.0F, true) - walkAmount * 2.0F;
        this.body.rotationPointY += bodyBob;
        this.walk(this.leftArm, walkSpeed, walkDegree * 0.4F, true, 0.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.leftHand, walkSpeed, walkDegree * 0.2F, true, -3.0F, 0.1F, limbSwing, walkAmount);
        this.leftArm.rotationPointY = this.leftArm.rotationPointY + (Math.min(0.0F, this.walkValue(limbSwing, walkAmount, walkSpeed, -1.5F, 3.0F, false)) - bodyBob);
        this.leftArm.rotationPointZ = this.leftArm.rotationPointZ + this.walkValue(limbSwing, walkAmount, walkSpeed, -1.5F, walkDegree * 3.0F, false);
        this.leftHand.rotationPointY = this.leftHand.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, walkAmount, walkSpeed, -2.5F, walkDegree * 1.0F, true));
        this.walk(this.rightArm, walkSpeed, walkDegree * 0.4F, false, 0.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.rightHand, walkSpeed, walkDegree * 0.2F, false, -3.0F, -0.1F, limbSwing, walkAmount);
        this.rightArm.rotationPointY = this.rightArm.rotationPointY + (Math.min(0.0F, this.walkValue(limbSwing, walkAmount, walkSpeed, -1.5F, 3.0F, true)) - bodyBob);
        this.rightArm.rotationPointZ = this.rightArm.rotationPointZ + this.walkValue(limbSwing, walkAmount, walkSpeed, -1.5F, walkDegree * 3.0F, true);
        this.rightHand.rotationPointY = this.rightHand.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, walkAmount, walkSpeed, -2.5F, walkDegree * 1.0F, false));
        this.walk(this.leftLeg, walkSpeed, walkDegree * 0.3F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.leftFoot, walkSpeed, walkDegree * 0.2F, false, -2.0F, -0.1F, limbSwing, walkAmount);
        this.leftLeg.rotationPointY = this.leftLeg.rotationPointY + (Math.min(0.0F, this.walkValue(limbSwing, walkAmount, walkSpeed, -0.5F, 3.0F, true)) - bodyBob);
        this.leftLeg.rotationPointZ = this.leftLeg.rotationPointZ + this.walkValue(limbSwing, walkAmount, walkSpeed, -0.5F, walkDegree * 3.0F, true);
        this.leftLeg.rotationPointY = this.leftLeg.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, walkAmount, walkSpeed, -2.0F, walkDegree * 0.5F, false));
        this.walk(this.rightLeg, walkSpeed, walkDegree * 0.3F, true, 1.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.rightFoot, walkSpeed, walkDegree * 0.2F, true, -2.0F, 0.1F, limbSwing, walkAmount);
        this.rightLeg.rotationPointY = this.rightLeg.rotationPointY + (Math.min(0.0F, this.walkValue(limbSwing, walkAmount, walkSpeed, -0.5F, 3.0F, false)) - bodyBob);
        this.rightLeg.rotationPointZ = this.rightLeg.rotationPointZ + this.walkValue(limbSwing, walkAmount, walkSpeed, -0.5F, walkDegree * 3.0F, false);
        this.rightFoot.rotationPointY = this.rightFoot.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, walkAmount, walkSpeed, -2.0F, walkDegree * 0.5F, true));
        this.walk(this.rightArm, swimSpeed, swimDegree, false, 0.0F, -0.25F, limbSwing, swimAmount);
        this.walk(this.leftArm, swimSpeed, swimDegree, false, 0.0F, -0.25F, limbSwing, swimAmount);
        this.walk(this.rightLeg, swimSpeed, swimDegree, true, 0.0F, 0.25F, limbSwing, swimAmount);
        this.walk(this.leftLeg, swimSpeed, swimDegree, true, 0.0F, 0.25F, limbSwing, swimAmount);
        this.swing(this.body, swimSpeed, swimDegree * 0.4F, false, 1.5F, 0.0F, limbSwing, swimAmount);
        this.swing(this.head, swimSpeed, swimDegree * 0.1F, true, 2.0F, 0.0F, limbSwing, swimAmount);
        this.chainSwing(new AdvancedModelBox[] { this.tail1, this.tail2, this.tail3 }, swimSpeed, swimDegree * 1.0F, -2.5, limbSwing, swimAmount);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.25F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.25F, 0.25F, 0.25F);
            matrixStackIn.translate(0.0, 4.5, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    private float walkValue(float limbSwing, float limbSwingAmount, float speed, float offset, float degree, boolean inverse) {
        return (float) (Math.cos((double) (limbSwing * speed + offset)) * (double) degree * (double) limbSwingAmount * (double) (inverse ? -1 : 1));
    }
}