package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntitySkreecher;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelSkreecher extends AdvancedEntityModel<EntitySkreecher> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox upperJaw;

    private final AdvancedModelBox leftEye;

    private final AdvancedModelBox rightEye;

    private final AdvancedModelBox leftLeg;

    private final AdvancedModelBox leftFoot;

    private final AdvancedModelBox rightLeg;

    private final AdvancedModelBox rightFoot;

    private final AdvancedModelBox leftArm;

    private final AdvancedModelBox leftArmPivot;

    private final AdvancedModelBox leftHand;

    private final AdvancedModelBox rightArm;

    private final AdvancedModelBox rightArmPivot;

    private final AdvancedModelBox rightHand;

    public ModelSkreecher() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -14.0F, 0.0F);
        this.root.addChild(this.body);
        this.setRotationAngle(this.body, -0.3927F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 26).addBox(-3.0F, -5.0F, -1.5F, 6.0F, 6.0F, 3.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -5.0F, -1.0F);
        this.body.addChild(this.head);
        this.setRotationAngle(this.head, 0.5236F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 13).addBox(-5.0F, -7.0F, -4.2F, 10.0F, 7.0F, 5.0F, -0.1F, false);
        this.upperJaw = new AdvancedModelBox(this, "upperJaw");
        this.upperJaw.setRotationPoint(0.0F, -7.0F, 0.8F);
        this.head.addChild(this.upperJaw);
        this.upperJaw.setTextureOffset(0, 0).addBox(-6.0F, 0.0F, -4.8F, 12.0F, 7.0F, 5.0F, 0.0F, false);
        this.leftEye = new AdvancedModelBox(this, "leftEye");
        this.leftEye.setRotationPoint(3.0F, 1.6F, -3.8F);
        this.upperJaw.addChild(this.leftEye);
        this.leftEye.setTextureOffset(34, 16).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        this.rightEye = new AdvancedModelBox(this, "rightEye");
        this.rightEye.setRotationPoint(-3.0F, 1.6F, -3.8F);
        this.upperJaw.addChild(this.rightEye);
        this.rightEye.setTextureOffset(34, 16).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 4.0F, 0.0F, true);
        this.leftLeg = new AdvancedModelBox(this, "leftLeg");
        this.leftLeg.setRotationPoint(2.0F, 2.0F, 0.0F);
        this.body.addChild(this.leftLeg);
        this.setRotationAngle(this.leftLeg, 0.0F, -0.3054F, 0.0F);
        this.leftLeg.setTextureOffset(27, 25).addBox(0.0F, -1.0F, -5.0F, 0.0F, 6.0F, 7.0F, 0.0F, false);
        this.leftFoot = new AdvancedModelBox(this, "leftFoot");
        this.leftFoot.setRotationPoint(0.0F, 2.0F, -4.0F);
        this.leftLeg.addChild(this.leftFoot);
        this.leftFoot.setTextureOffset(1, 48).addBox(-1.0F, 0.0F, -4.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this, "rightLeg");
        this.rightLeg.setRotationPoint(-2.0F, 2.0F, 0.0F);
        this.body.addChild(this.rightLeg);
        this.setRotationAngle(this.rightLeg, 0.0F, 0.3054F, 0.0F);
        this.rightLeg.setTextureOffset(27, 25).addBox(0.0F, -1.0F, -5.0F, 0.0F, 6.0F, 7.0F, 0.0F, true);
        this.rightFoot = new AdvancedModelBox(this, "rightFoot");
        this.rightFoot.setRotationPoint(0.0F, 2.0F, -4.0F);
        this.rightLeg.addChild(this.rightFoot);
        this.rightFoot.setTextureOffset(1, 48).addBox(0.0F, 0.0F, -4.0F, 1.0F, 6.0F, 6.0F, 0.0F, true);
        this.leftArmPivot = new AdvancedModelBox(this, "leftArmPivot");
        this.leftArmPivot.setRotationPoint(4.0F, -3.0F, 0.0F);
        this.body.addChild(this.leftArmPivot);
        this.leftArm = new AdvancedModelBox(this, "leftArm");
        this.leftArmPivot.addChild(this.leftArm);
        this.setRotationAngle(this.leftArm, 1.5708F, -1.1781F, -1.5708F);
        this.leftArm.setTextureOffset(17, 34).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 16.0F, 2.0F, 0.0F, false);
        this.leftHand = new AdvancedModelBox(this, "leftHand");
        this.leftHand.setRotationPoint(0.0F, 15.0F, 0.0F);
        this.leftArm.addChild(this.leftHand);
        this.leftHand.setTextureOffset(31, 9).addBox(-4.0F, -0.1F, -2.0F, 8.0F, 3.0F, 4.0F, 0.0F, false);
        this.rightArmPivot = new AdvancedModelBox(this, "rightArmPivot");
        this.rightArmPivot.setRotationPoint(-4.0F, -3.0F, 0.0F);
        this.body.addChild(this.rightArmPivot);
        this.rightArm = new AdvancedModelBox(this, "rightArm");
        this.rightArmPivot.addChild(this.rightArm);
        this.setRotationAngle(this.rightArm, 1.5708F, 1.1781F, 1.5708F);
        this.rightArm.setTextureOffset(17, 34).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 16.0F, 2.0F, 0.0F, true);
        this.rightHand = new AdvancedModelBox(this, "rightHand");
        this.rightHand.setRotationPoint(0.0F, 15.0F, 0.0F);
        this.rightArm.addChild(this.rightHand);
        this.rightHand.setTextureOffset(31, 9).addBox(-4.0F, -0.1F, -2.0F, 8.0F, 3.0F, 4.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.upperJaw, this.leftEye, this.rightEye, this.leftLeg, this.leftArmPivot, this.leftArm, this.leftFoot, this.rightLeg, this.rightArmPivot, new AdvancedModelBox[] { this.rightArm, this.rightFoot, this.leftHand, this.rightHand });
    }

    public void setupAnim(EntitySkreecher entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float clingProgress = entity.prevClingProgress + (entity.clingProgress - entity.prevClingProgress) * partialTick;
        float groundProgress = 5.0F - clingProgress;
        float clapProgress = entity.prevClapProgress + (entity.clapProgress - entity.prevClapProgress) * partialTick;
        float distanceToCeiling = entity.prevDistanceToCeiling + (entity.getDistanceToCeiling() - entity.prevDistanceToCeiling) * partialTick;
        float armScale = 0.3F + distanceToCeiling * clingProgress * 0.2F + 0.7F * groundProgress * 0.2F;
        float armDegree = (5.0F - distanceToCeiling) * 0.2F;
        float walkSpeed = 1.0F;
        float walkDegree = 0.6F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float groundSpeed = groundProgress * 0.2F;
        float feetStill = (5.0F - clapProgress) * 0.2F;
        float clingSpeed = clingProgress * 0.2F;
        this.rightArm.setShouldScaleChildren(true);
        this.rightArm.setScale(1.0F, armScale, 1.0F);
        this.leftArm.setShouldScaleChildren(true);
        this.leftArm.setScale(1.0F, armScale, 1.0F);
        this.progressRotationPrev(this.rightArmPivot, clingProgress, Maths.rad(-210.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftArmPivot, clingProgress, Maths.rad(-210.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, clingProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, clingProgress, Maths.rad(-30.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, clingProgress, 0.0F, 8.5F, 0.0F, 5.0F);
        this.progressPositionPrev(this.rightArmPivot, clingProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leftArmPivot, clingProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, clingProgress, 0.0F, 3.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightLeg, clapProgress, Maths.rad(-10.0), Maths.rad(-25.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftLeg, clapProgress, Maths.rad(-10.0), Maths.rad(25.0), 0.0F, 5.0F);
        this.walk(this.body, idleSpeed, idleDegree, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.rightLeg, idleSpeed, idleDegree, false, -1.0F, 0.0F, ageInTicks, feetStill);
        this.walk(this.leftLeg, idleSpeed, idleDegree, false, -1.0F, 0.0F, ageInTicks, feetStill);
        this.walk(this.leftArmPivot, idleSpeed, idleDegree, true, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.rightArmPivot, idleSpeed, idleDegree, true, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.bob(this.head, idleSpeed, -idleDegree * 2.0F, false, limbSwing, 1.0F);
        this.walk(this.body, walkSpeed, walkDegree, false, 0.0F, 0.3F, limbSwing, limbSwingAmount * groundSpeed);
        this.walk(this.head, walkSpeed, walkDegree * 0.5F, true, 0.3F, 0.3F, limbSwing, limbSwingAmount * groundSpeed);
        this.swing(this.leftArm, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount * groundSpeed);
        this.swing(this.rightArm, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount * groundSpeed);
        this.walk(this.rightLeg, walkSpeed, walkDegree, false, 1.0F, 0.3F, limbSwing, limbSwingAmount * groundSpeed * feetStill);
        this.walk(this.leftLeg, walkSpeed, walkDegree, false, 1.0F, 0.3F, limbSwing, limbSwingAmount * groundSpeed * feetStill);
        this.swing(this.leftArm, walkSpeed, walkDegree, false, -2.0F, -0.2F, limbSwing, limbSwingAmount * groundSpeed);
        this.swing(this.rightArm, walkSpeed, walkDegree, false, -2.0F, 0.2F, limbSwing, limbSwingAmount * groundSpeed);
        this.swing(this.leftArm, walkSpeed, armDegree * walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount * clingSpeed);
        this.swing(this.rightArm, walkSpeed, armDegree * walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount * clingSpeed);
        this.walk(this.leftArm, walkSpeed, armDegree * walkDegree * 0.2F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount * clingSpeed);
        this.walk(this.rightArm, walkSpeed, armDegree * walkDegree * 0.2F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount * clingSpeed);
        this.walk(this.rightLeg, walkSpeed, walkDegree, false, 2.0F, 0.4F, limbSwing, limbSwingAmount * clingSpeed * feetStill);
        this.walk(this.leftLeg, walkSpeed, walkDegree, true, 2.0F, -0.4F, limbSwing, limbSwingAmount * clingSpeed * feetStill);
        this.swing(this.body, walkSpeed, walkDegree * 0.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount * clingSpeed);
        this.swing(this.head, walkSpeed, walkDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount * clingSpeed);
        this.swing(this.head, 10.0F, 0.05F, false, 0.0F, 0.0F, ageInTicks, clapProgress * 0.2F);
        this.flap(this.leftLeg, 0.8F, 0.5F, false, 0.0F, -0.45F, ageInTicks, clapProgress * 0.2F);
        this.flap(this.rightLeg, 0.8F, 0.5F, true, 0.0F, -0.45F, ageInTicks, clapProgress * 0.2F);
        this.swing(this.leftLeg, 0.8F, 0.35F, false, 0.0F, -0.15F, ageInTicks, clapProgress * 0.2F);
        this.swing(this.rightLeg, 0.8F, 0.35F, true, 0.0F, -0.15F, ageInTicks, clapProgress * 0.2F);
        this.bob(this.body, idleSpeed * 3.0F, -idleDegree * 5.0F, false, ageInTicks, clingSpeed);
        this.bob(this.rightArmPivot, idleSpeed * 3.0F, idleDegree * 8.0F, false, ageInTicks, clingSpeed);
        this.bob(this.leftArmPivot, idleSpeed * 3.0F, idleDegree * 8.0F, false, ageInTicks, clingSpeed);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}