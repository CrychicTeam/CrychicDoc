package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityGiantSquid;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ModelGiantSquid extends AdvancedEntityModel<EntityGiantSquid> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox head;

    private final AdvancedModelBox beak;

    private final AdvancedModelBox left_FrontTentacle;

    private final AdvancedModelBox left_FrontTentacleEnd;

    private final AdvancedModelBox left_FrontMidTentacle;

    private final AdvancedModelBox left_FrontMidTentacleEnd;

    private final AdvancedModelBox right_FrontMidTentacle;

    private final AdvancedModelBox right_FrontMidTentacleEnd;

    private final AdvancedModelBox left_BackMidTentacle;

    private final AdvancedModelBox left_BackMidTentacleEnd;

    private final AdvancedModelBox right_BackMidTentacle;

    private final AdvancedModelBox right_BackMidTentacleEnd;

    private final AdvancedModelBox left_BackTentacle;

    private final AdvancedModelBox left_BackTentacleEnd;

    private final AdvancedModelBox right_BackTentacle;

    private final AdvancedModelBox right_BackTentacleEnd;

    private final AdvancedModelBox right_FrontTentacle;

    private final AdvancedModelBox right_tentacleEnd;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox left_arm2;

    private final AdvancedModelBox left_arm3;

    private final AdvancedModelBox left_arm4;

    private final AdvancedModelBox left_hand;

    private final AdvancedModelBox right_arm;

    private final AdvancedModelBox right_arm2;

    private final AdvancedModelBox right_arm3;

    private final AdvancedModelBox right_arm4;

    private final AdvancedModelBox right_hand;

    private final AdvancedModelBox left_eye;

    private final AdvancedModelBox left_pupil;

    private final AdvancedModelBox left_pupil_pivot;

    private final AdvancedModelBox right_pupil_pivot;

    private final AdvancedModelBox right_eye;

    private final AdvancedModelBox right_pupil;

    private final AdvancedModelBox mantle;

    private final AdvancedModelBox mantle_end;

    private final AdvancedModelBox left_membrane;

    private final AdvancedModelBox right_membrane;

    public ModelGiantSquid() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.root.addChild(this.head);
        this.head.setTextureOffset(43, 35).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 14.0F, 10.0F, 0.0F, false);
        this.beak = new AdvancedModelBox(this, "beak");
        this.beak.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.head.addChild(this.beak);
        this.beak.setTextureOffset(41, 0).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        this.left_FrontTentacle = new AdvancedModelBox(this, "left_FrontTentacle");
        this.left_FrontTentacle.setRotationPoint(1.5F, 5.0F, -4.0F);
        this.head.addChild(this.left_FrontTentacle);
        this.left_FrontTentacle.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);
        this.left_FrontTentacleEnd = new AdvancedModelBox(this, "left_FrontTentacleEnd");
        this.left_FrontTentacleEnd.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.left_FrontTentacle.addChild(this.left_FrontTentacleEnd);
        this.left_FrontTentacleEnd.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);
        this.left_FrontMidTentacle = new AdvancedModelBox(this, "left_FrontMidTentacle");
        this.left_FrontMidTentacle.setRotationPoint(4.0F, 5.0F, -2.0F);
        this.head.addChild(this.left_FrontMidTentacle);
        this.setRotationAngle(this.left_FrontMidTentacle, 0.0F, -1.5708F, 0.0F);
        this.left_FrontMidTentacle.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);
        this.left_FrontMidTentacleEnd = new AdvancedModelBox(this, "left_FrontMidTentacleEnd");
        this.left_FrontMidTentacleEnd.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.left_FrontMidTentacle.addChild(this.left_FrontMidTentacleEnd);
        this.left_FrontMidTentacleEnd.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);
        this.right_FrontMidTentacle = new AdvancedModelBox(this, "right_FrontMidTentacle");
        this.right_FrontMidTentacle.setRotationPoint(-4.0F, 5.0F, -2.0F);
        this.head.addChild(this.right_FrontMidTentacle);
        this.setRotationAngle(this.right_FrontMidTentacle, 0.0F, 1.5708F, 0.0F);
        this.right_FrontMidTentacle.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, true);
        this.right_FrontMidTentacleEnd = new AdvancedModelBox(this, "right_FrontMidTentacleEnd");
        this.right_FrontMidTentacleEnd.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.right_FrontMidTentacle.addChild(this.right_FrontMidTentacleEnd);
        this.right_FrontMidTentacleEnd.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, true);
        this.left_BackMidTentacle = new AdvancedModelBox(this, "left_BackMidTentacle");
        this.left_BackMidTentacle.setRotationPoint(4.0F, 5.0F, 2.0F);
        this.head.addChild(this.left_BackMidTentacle);
        this.setRotationAngle(this.left_BackMidTentacle, 0.0F, -1.3963F, 0.0F);
        this.left_BackMidTentacle.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);
        this.left_BackMidTentacleEnd = new AdvancedModelBox(this, "left_BackMidTentacleEnd");
        this.left_BackMidTentacleEnd.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.left_BackMidTentacle.addChild(this.left_BackMidTentacleEnd);
        this.left_BackMidTentacleEnd.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);
        this.right_BackMidTentacle = new AdvancedModelBox(this, "right_BackMidTentacle");
        this.right_BackMidTentacle.setRotationPoint(-4.0F, 5.0F, 2.0F);
        this.head.addChild(this.right_BackMidTentacle);
        this.setRotationAngle(this.right_BackMidTentacle, 0.0F, 1.3963F, 0.0F);
        this.right_BackMidTentacle.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, true);
        this.right_BackMidTentacleEnd = new AdvancedModelBox(this, "right_BackMidTentacleEnd");
        this.right_BackMidTentacleEnd.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.right_BackMidTentacle.addChild(this.right_BackMidTentacleEnd);
        this.right_BackMidTentacleEnd.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, true);
        this.left_BackTentacle = new AdvancedModelBox(this, "left_BackTentacle");
        this.left_BackTentacle.setRotationPoint(2.0F, 5.0F, 4.0F);
        this.head.addChild(this.left_BackTentacle);
        this.setRotationAngle(this.left_BackTentacle, 0.0F, 3.1416F, 0.0F);
        this.left_BackTentacle.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);
        this.left_BackTentacleEnd = new AdvancedModelBox(this, "left_BackTentacleEnd");
        this.left_BackTentacleEnd.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.left_BackTentacle.addChild(this.left_BackTentacleEnd);
        this.left_BackTentacleEnd.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);
        this.right_BackTentacle = new AdvancedModelBox(this, "right_BackTentacle");
        this.right_BackTentacle.setRotationPoint(-2.0F, 5.0F, 4.0F);
        this.head.addChild(this.right_BackTentacle);
        this.setRotationAngle(this.right_BackTentacle, 0.0F, -3.1416F, 0.0F);
        this.right_BackTentacle.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, true);
        this.right_BackTentacleEnd = new AdvancedModelBox(this, "right_BackTentacleEnd");
        this.right_BackTentacleEnd.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.right_BackTentacle.addChild(this.right_BackTentacleEnd);
        this.right_BackTentacleEnd.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, true);
        this.right_FrontTentacle = new AdvancedModelBox(this, "right_FrontTentacle");
        this.right_FrontTentacle.setRotationPoint(-1.5F, 5.0F, -4.0F);
        this.head.addChild(this.right_FrontTentacle);
        this.right_FrontTentacle.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, true);
        this.right_tentacleEnd = new AdvancedModelBox(this, "right_tentacleEnd");
        this.right_tentacleEnd.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.right_FrontTentacle.addChild(this.right_tentacleEnd);
        this.right_tentacleEnd.setTextureOffset(18, 70).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, true);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(3.0F, 5.0F, 0.0F);
        this.head.addChild(this.left_arm);
        this.setRotationAngle(this.left_arm, 0.0F, -1.5708F, 0.0F);
        this.left_arm.setTextureOffset(32, 66).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);
        this.left_arm2 = new AdvancedModelBox(this, "left_arm2");
        this.left_arm2.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.left_arm.addChild(this.left_arm2);
        this.left_arm2.setTextureOffset(45, 60).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 35.0F, 2.0F, 0.0F, false);
        this.left_arm3 = new AdvancedModelBox(this, "left_arm3");
        this.left_arm3.setRotationPoint(0.0F, 35.0F, 0.0F);
        this.left_arm2.addChild(this.left_arm3);
        this.left_arm3.setTextureOffset(45, 60).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 35.0F, 2.0F, 0.0F, false);
        this.left_arm4 = new AdvancedModelBox(this, "left_arm4");
        this.left_arm4.setRotationPoint(0.0F, 35.0F, 0.0F);
        this.left_arm3.addChild(this.left_arm4);
        this.left_arm4.setTextureOffset(45, 60).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 35.0F, 2.0F, 0.0F, false);
        this.left_hand = new AdvancedModelBox(this, "left_hand");
        this.left_hand.setRotationPoint(0.0F, 35.0F, 0.0F);
        this.left_arm4.addChild(this.left_hand);
        this.left_hand.setTextureOffset(54, 60).addBox(-3.0F, 0.0F, -1.3F, 6.0F, 14.0F, 3.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-3.0F, 5.0F, 0.0F);
        this.head.addChild(this.right_arm);
        this.setRotationAngle(this.right_arm, 0.0F, 1.5708F, 0.0F);
        this.right_arm.setTextureOffset(32, 66).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, true);
        this.right_arm2 = new AdvancedModelBox(this, "right_arm2");
        this.right_arm2.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.right_arm.addChild(this.right_arm2);
        this.right_arm2.setTextureOffset(45, 60).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 35.0F, 2.0F, 0.0F, true);
        this.right_arm3 = new AdvancedModelBox(this, "right_arm3");
        this.right_arm3.setRotationPoint(0.0F, 35.0F, 0.0F);
        this.right_arm2.addChild(this.right_arm3);
        this.right_arm3.setTextureOffset(45, 60).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 35.0F, 2.0F, 0.0F, true);
        this.right_arm4 = new AdvancedModelBox(this, "right_arm4");
        this.right_arm4.setRotationPoint(0.0F, 35.0F, 0.0F);
        this.right_arm3.addChild(this.right_arm4);
        this.right_arm4.setTextureOffset(45, 60).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 35.0F, 2.0F, 0.0F, true);
        this.right_hand = new AdvancedModelBox(this, "right_hand");
        this.right_hand.setRotationPoint(0.0F, 35.0F, 0.0F);
        this.right_arm4.addChild(this.right_hand);
        this.right_hand.setTextureOffset(54, 60).addBox(-3.0F, 0.0F, -1.3F, 6.0F, 14.0F, 3.0F, 0.0F, true);
        this.left_eye = new AdvancedModelBox(this, "left_eye");
        this.left_eye.setRotationPoint(5.9F, 0.5F, 0.0F);
        this.head.addChild(this.left_eye);
        this.left_eye.setTextureOffset(53, 19).addBox(-1.5F, -3.5F, -3.5F, 3.0F, 7.0F, 7.0F, 0.0F, false);
        this.left_pupil_pivot = new AdvancedModelBox(this, "left_pupil_pivot");
        this.left_pupil_pivot.setRotationPoint(1.55F, 0.0F, 0.0F);
        this.left_eye.addChild(this.left_pupil_pivot);
        this.left_pupil = new AdvancedModelBox(this, "left_pupil");
        this.left_pupil_pivot.addChild(this.left_pupil);
        this.left_pupil.setTextureOffset(0, 0).addBox(0.0F, -2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, false);
        this.right_eye = new AdvancedModelBox(this, "right_eye");
        this.right_eye.setRotationPoint(-5.9F, 0.5F, 0.0F);
        this.head.addChild(this.right_eye);
        this.right_eye.setTextureOffset(53, 19).addBox(-1.5F, -3.5F, -3.5F, 3.0F, 7.0F, 7.0F, 0.0F, true);
        this.right_pupil_pivot = new AdvancedModelBox(this, "right_pupil_pivot");
        this.right_pupil_pivot.setRotationPoint(-1.55F, 0.0F, 0.0F);
        this.right_eye.addChild(this.right_pupil_pivot);
        this.right_pupil = new AdvancedModelBox(this, "right_pupil");
        this.right_pupil_pivot.addChild(this.right_pupil);
        this.right_pupil.setTextureOffset(0, 0).addBox(0.0F, -2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, true);
        this.mantle = new AdvancedModelBox(this, "mantle");
        this.mantle.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.head.addChild(this.mantle);
        this.mantle.setTextureOffset(0, 0).addBox(-7.0F, -31.0F, -6.0F, 14.0F, 32.0F, 12.0F, 0.0F, false);
        this.mantle_end = new AdvancedModelBox(this, "mantle_end");
        this.mantle_end.setRotationPoint(0.0F, -31.0F, 0.0F);
        this.mantle.addChild(this.mantle_end);
        this.mantle_end.setTextureOffset(53, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);
        this.left_membrane = new AdvancedModelBox(this, "left_membrane");
        this.left_membrane.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.mantle_end.addChild(this.left_membrane);
        this.left_membrane.setTextureOffset(0, 45).addBox(0.0F, -12.0F, 0.0F, 17.0F, 24.0F, 0.0F, 0.0F, false);
        this.right_membrane = new AdvancedModelBox(this, "right_membrane");
        this.right_membrane.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.mantle_end.addChild(this.right_membrane);
        this.right_membrane.setTextureOffset(0, 45).addBox(-17.0F, -12.0F, 0.0F, 17.0F, 24.0F, 0.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setupAnim(EntityGiantSquid entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float swimSpeed = 0.2F;
        float swimDegree = 0.4F;
        float idleSpeed = 0.05F;
        float idleDegree = 0.02F;
        float flailSpeed = 0.3F;
        float flailDegree = 0.4F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float dryProgress = entity.prevDryProgress + (entity.dryProgress - entity.prevDryProgress) * partialTick;
        float capturedProgress = entity.prevCapturedProgress + (entity.capturedProgress - entity.prevCapturedProgress) * partialTick;
        float grabProgress = entity.prevGrabProgress + (entity.grabProgress - entity.prevGrabProgress) * partialTick;
        float pitch = entity.prevSquidPitch + (entity.getSquidPitch() - entity.prevSquidPitch) * partialTick;
        float f = (pitch - 90.0F) * (float) (Math.PI / 180.0);
        float f1 = (float) Math.sin((double) (swimSpeed * limbSwing)) * swimDegree * limbSwingAmount;
        float stretchy = 0.9F + 0.2F * f1;
        float stretchyXZ = 0.95F - 0.1F * f1;
        float flailAmount = capturedProgress * 0.2F;
        this.head.rotateAngleX += f;
        this.right_pupil_pivot.rotateAngleX -= f;
        this.left_pupil_pivot.rotateAngleX -= f;
        this.mantle.setScale(stretchyXZ, stretchy, stretchyXZ - dryProgress * 0.05F);
        this.mantle.setShouldScaleChildren(true);
        float contractFromGrab = 1.0F - 0.2F * grabProgress;
        this.right_arm2.rotateAngleX = this.right_arm2.rotateAngleX - contractFromGrab * this.getArmRot(entity, 2, partialTick, false);
        this.left_arm2.rotateAngleX = this.left_arm2.rotateAngleX + contractFromGrab * this.getArmRot(entity, 2, partialTick, false);
        this.right_arm2.rotateAngleX = this.right_arm2.rotateAngleX - contractFromGrab * this.getArmRot(entity, 4, partialTick, false);
        this.left_arm2.rotateAngleX = this.left_arm2.rotateAngleX + contractFromGrab * this.getArmRot(entity, 4, partialTick, false);
        this.right_arm3.rotateAngleX = this.right_arm3.rotateAngleX - contractFromGrab * this.getArmRot(entity, 8, partialTick, false);
        this.left_arm3.rotateAngleX = this.left_arm3.rotateAngleX + contractFromGrab * this.getArmRot(entity, 8, partialTick, false);
        this.right_arm4.rotateAngleX = this.right_arm4.rotateAngleX - contractFromGrab * this.getArmRot(entity, 12, partialTick, false);
        this.left_arm4.rotateAngleX = this.left_arm4.rotateAngleX + contractFromGrab * this.getArmRot(entity, 12, partialTick, false);
        this.right_arm2.rotateAngleZ = this.right_arm2.rotateAngleZ + contractFromGrab * this.getArmRot(entity, 2, partialTick, true);
        this.left_arm2.rotateAngleZ = this.left_arm2.rotateAngleZ - contractFromGrab * this.getArmRot(entity, 2, partialTick, true);
        this.right_arm2.rotateAngleZ = this.right_arm2.rotateAngleZ + contractFromGrab * this.getArmRot(entity, 4, partialTick, true);
        this.left_arm2.rotateAngleZ = this.left_arm2.rotateAngleZ - contractFromGrab * this.getArmRot(entity, 4, partialTick, true);
        this.right_arm3.rotateAngleZ = this.right_arm3.rotateAngleZ + contractFromGrab * this.getArmRot(entity, 6, partialTick, true);
        this.left_arm3.rotateAngleZ = this.left_arm3.rotateAngleZ - contractFromGrab * this.getArmRot(entity, 6, partialTick, true);
        this.right_arm4.rotateAngleZ = this.right_arm4.rotateAngleZ + contractFromGrab * this.getArmRot(entity, 8, partialTick, true);
        this.left_arm4.rotateAngleZ = this.left_arm4.rotateAngleZ - contractFromGrab * this.getArmRot(entity, 8, partialTick, true);
        this.progressRotationPrev(this.mantle_end, dryProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_membrane, dryProgress, 0.0F, Maths.rad(20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_membrane, dryProgress, 0.0F, Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.mantle, dryProgress, 0.0F, 0.0F, Maths.rad(15.0), 5.0F);
        this.progressRotationPrev(this.left_FrontTentacle, dryProgress, Maths.rad(10.0), 0.0F, Maths.rad(-25.0), 5.0F);
        this.progressRotationPrev(this.left_FrontTentacleEnd, dryProgress, Maths.rad(5.0), 0.0F, Maths.rad(-15.0), 5.0F);
        this.progressRotationPrev(this.right_FrontTentacle, dryProgress, Maths.rad(10.0), 0.0F, Maths.rad(15.0), 5.0F);
        this.progressRotationPrev(this.right_tentacleEnd, dryProgress, Maths.rad(5.0), 0.0F, Maths.rad(15.0), 5.0F);
        this.progressRotationPrev(this.left_BackTentacle, dryProgress, 0.0F, 0.0F, Maths.rad(-15.0), 5.0F);
        this.progressRotationPrev(this.left_BackTentacleEnd, dryProgress, 0.0F, 0.0F, Maths.rad(-15.0), 5.0F);
        this.progressRotationPrev(this.right_BackTentacle, dryProgress, 0.0F, 0.0F, Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.right_BackTentacleEnd, dryProgress, 0.0F, 0.0F, Maths.rad(25.0), 5.0F);
        this.progressRotationPrev(this.right_BackMidTentacle, dryProgress, Maths.rad(20.0), 0.0F, Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.left_BackMidTentacle, dryProgress, Maths.rad(15.0), 0.0F, Maths.rad(-50.0), 5.0F);
        this.progressRotationPrev(this.left_FrontMidTentacle, dryProgress, Maths.rad(35.0), Maths.rad(15.0), Maths.rad(-50.0), 5.0F);
        this.progressRotationPrev(this.left_FrontMidTentacleEnd, dryProgress, Maths.rad(35.0), Maths.rad(-15.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_FrontMidTentacle, dryProgress, Maths.rad(35.0), Maths.rad(-15.0), Maths.rad(50.0), 5.0F);
        this.progressRotationPrev(this.right_FrontMidTentacleEnd, dryProgress, Maths.rad(35.0), Maths.rad(15.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, dryProgress, Maths.rad(10.0), Maths.rad(90.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm2, dryProgress, Maths.rad(-10.0), 0.0F, Maths.rad(2.0), 5.0F);
        this.progressRotationPrev(this.left_arm3, dryProgress, 0.0F, 0.0F, Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.left_arm4, dryProgress, 0.0F, 0.0F, Maths.rad(-8.0), 5.0F);
        this.progressRotationPrev(this.right_arm, dryProgress, Maths.rad(10.0), Maths.rad(-90.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm2, dryProgress, Maths.rad(-10.0), 0.0F, Maths.rad(-4.0), 5.0F);
        this.progressRotationPrev(this.right_arm3, dryProgress, 0.0F, 0.0F, Maths.rad(-12.0), 5.0F);
        this.progressRotationPrev(this.right_arm4, dryProgress, 0.0F, 0.0F, Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.left_arm, grabProgress, Maths.rad(-110.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, grabProgress, Maths.rad(-110.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm2, grabProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm2, grabProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm3, grabProgress, Maths.rad(100.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm3, grabProgress, Maths.rad(100.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm4, grabProgress, Maths.rad(70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm4, grabProgress, Maths.rad(70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_hand, grabProgress, Maths.rad(-120.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_hand, grabProgress, Maths.rad(-120.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_arm, grabProgress, 0.0F, 2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, grabProgress, 0.0F, 2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_arm4, grabProgress, 0.0F, -2.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.right_arm4, grabProgress, 0.0F, -2.0F, 1.0F, 5.0F);
        this.progressRotationPrev(this.left_FrontTentacle, Math.max(grabProgress, capturedProgress), Maths.rad(-20.0), Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_FrontTentacle, Math.max(grabProgress, capturedProgress), Maths.rad(-20.0), Maths.rad(20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_BackTentacle, Math.max(grabProgress, capturedProgress), Maths.rad(-20.0), Maths.rad(20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_BackTentacle, Math.max(grabProgress, capturedProgress), Maths.rad(-20.0), Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_FrontMidTentacle, Math.max(grabProgress, capturedProgress), Maths.rad(-20.0), Maths.rad(20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_FrontMidTentacle, Math.max(grabProgress, capturedProgress), Maths.rad(-20.0), Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_BackMidTentacle, Math.max(grabProgress, capturedProgress), Maths.rad(-20.0), Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_BackMidTentacle, Math.max(grabProgress, capturedProgress), Maths.rad(-20.0), Maths.rad(20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.mantle, capturedProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.mantle, capturedProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, capturedProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.swing(this.right_membrane, swimSpeed, swimDegree, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.left_membrane, swimSpeed, swimDegree, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_FrontTentacle, swimSpeed, swimDegree, true, 0.0F, 0.5F, limbSwing, limbSwingAmount);
        this.flap(this.left_FrontTentacle, swimSpeed, swimDegree, true, 0.0F, 0.35F, limbSwing, limbSwingAmount);
        this.walk(this.right_FrontTentacle, swimSpeed, swimDegree, true, 0.0F, 0.5F, limbSwing, limbSwingAmount);
        this.flap(this.right_FrontTentacle, swimSpeed, swimDegree, false, 0.0F, 0.35F, limbSwing, limbSwingAmount);
        this.walk(this.left_BackTentacle, swimSpeed, swimDegree, true, 0.0F, 0.5F, limbSwing, limbSwingAmount);
        this.flap(this.left_BackTentacle, swimSpeed, swimDegree, true, 0.0F, 0.35F, limbSwing, limbSwingAmount);
        this.walk(this.right_BackTentacle, swimSpeed, swimDegree, true, 0.0F, 0.5F, limbSwing, limbSwingAmount);
        this.flap(this.right_BackTentacle, swimSpeed, swimDegree, false, 0.0F, 0.35F, limbSwing, limbSwingAmount);
        this.walk(this.right_BackMidTentacle, swimSpeed, swimDegree, true, 0.0F, 0.5F, limbSwing, limbSwingAmount);
        this.swing(this.right_BackMidTentacle, swimSpeed, swimDegree, true, 0.0F, -0.75F, limbSwing, limbSwingAmount);
        this.walk(this.right_FrontMidTentacle, swimSpeed, swimDegree, true, 0.0F, 0.5F, limbSwing, limbSwingAmount);
        this.swing(this.right_FrontMidTentacle, swimSpeed, swimDegree, false, 0.0F, -0.75F, limbSwing, limbSwingAmount);
        this.walk(this.left_BackMidTentacle, swimSpeed, swimDegree, true, 0.0F, 0.5F, limbSwing, limbSwingAmount);
        this.swing(this.left_BackMidTentacle, swimSpeed, swimDegree, false, 0.0F, -0.75F, limbSwing, limbSwingAmount);
        this.walk(this.left_FrontMidTentacle, swimSpeed, swimDegree, true, 0.0F, 0.5F, limbSwing, limbSwingAmount);
        this.swing(this.left_FrontMidTentacle, swimSpeed, swimDegree, true, 0.0F, -0.75F, limbSwing, limbSwingAmount);
        this.walk(this.left_FrontTentacleEnd, swimSpeed, swimDegree, true, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_tentacleEnd, swimSpeed, swimDegree, true, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_BackTentacleEnd, swimSpeed, swimDegree, true, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_BackTentacleEnd, swimSpeed, swimDegree, true, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_BackMidTentacleEnd, swimSpeed, swimDegree, true, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_BackMidTentacleEnd, swimSpeed, swimDegree, true, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_FrontMidTentacleEnd, swimSpeed, swimDegree, true, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_FrontMidTentacleEnd, swimSpeed, swimDegree, true, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_arm, swimSpeed, swimDegree * 0.1F, true, 0.0F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(this.right_arm, swimSpeed, swimDegree * 0.1F, true, 0.0F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(this.left_arm2, swimSpeed, swimDegree * 0.1F, false, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_arm2, swimSpeed, swimDegree * 0.1F, false, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_arm3, swimSpeed, swimDegree * 0.1F, true, -4.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_arm3, swimSpeed, swimDegree * 0.1F, true, -4.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_arm4, swimSpeed, swimDegree * 0.2F, true, -6.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_arm4, swimSpeed, swimDegree * 0.2F, true, -6.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_arm, flailSpeed, flailDegree, true, 2.0F, 0.7F, ageInTicks, flailAmount);
        this.swing(this.left_arm, flailSpeed, flailDegree, true, 6.0F, 0.7F, ageInTicks, flailAmount);
        this.walk(this.right_arm, flailSpeed, flailDegree, false, 2.0F, -0.7F, ageInTicks, flailAmount);
        this.swing(this.right_arm, flailSpeed, flailDegree, false, 6.0F, -0.7F, ageInTicks, flailAmount);
        this.walk(this.left_arm2, flailSpeed, flailDegree, true, 0.0F, 0.7F, ageInTicks, flailAmount);
        this.walk(this.right_arm2, flailSpeed, flailDegree, false, 0.0F, -0.7F, ageInTicks, flailAmount);
        this.walk(this.left_arm3, flailSpeed, flailDegree, true, 0.0F, 0.7F, ageInTicks, flailAmount);
        this.walk(this.right_arm3, flailSpeed, flailDegree, false, 0.0F, -0.7F, ageInTicks, flailAmount);
        this.walk(this.left_arm4, flailSpeed, flailDegree, true, 0.0F, 0.2F, ageInTicks, flailAmount);
        this.walk(this.right_arm4, flailSpeed, flailDegree, false, 0.0F, -0.2F, ageInTicks, flailAmount);
        this.walk(this.left_hand, flailSpeed, flailDegree, true, 0.0F, 0.2F, ageInTicks, flailAmount);
        this.walk(this.right_hand, flailSpeed, flailDegree, false, 0.0F, -0.2F, ageInTicks, flailAmount);
        this.walk(this.left_FrontTentacle, flailSpeed, flailDegree * 0.5F, false, 0.0F, -0.5F, ageInTicks, flailAmount);
        this.flap(this.left_FrontTentacle, flailSpeed, flailDegree * 0.5F, false, 0.0F, -0.15F, ageInTicks, flailAmount);
        this.walk(this.left_FrontTentacleEnd, flailSpeed, flailDegree * 0.5F, false, 0.0F, -0.85F, ageInTicks, flailAmount);
        this.walk(this.right_FrontTentacle, flailSpeed, flailDegree * 0.5F, false, 2.0F, -0.5F, ageInTicks, flailAmount);
        this.flap(this.right_FrontTentacle, flailSpeed, flailDegree * 0.5F, true, 2.0F, -0.15F, ageInTicks, flailAmount);
        this.walk(this.right_tentacleEnd, flailSpeed, flailDegree * 0.5F, false, 2.0F, -0.85F, ageInTicks, flailAmount);
        this.walk(this.left_BackTentacle, flailSpeed, flailDegree * 0.5F, false, 4.0F, -0.5F, ageInTicks, flailAmount);
        this.flap(this.left_BackTentacle, flailSpeed, flailDegree * 0.5F, false, 4.0F, -0.15F, ageInTicks, flailAmount);
        this.walk(this.left_BackTentacleEnd, flailSpeed, flailDegree * 0.5F, false, 4.0F, -0.85F, ageInTicks, flailAmount);
        this.walk(this.right_BackTentacle, flailSpeed, flailDegree * 0.5F, false, 8.0F, -0.5F, ageInTicks, flailAmount);
        this.flap(this.right_BackTentacle, flailSpeed, flailDegree * 0.5F, true, 8.0F, -0.15F, ageInTicks, flailAmount);
        this.walk(this.right_BackTentacleEnd, flailSpeed, flailDegree * 0.5F, false, 8.0F, -0.85F, ageInTicks, flailAmount);
        this.walk(this.left_BackMidTentacle, flailSpeed, flailDegree * 0.5F, false, 10.0F, -0.5F, ageInTicks, flailAmount);
        this.flap(this.left_BackMidTentacle, flailSpeed, flailDegree * 0.5F, false, 10.0F, -0.15F, ageInTicks, flailAmount);
        this.walk(this.left_BackMidTentacleEnd, flailSpeed, flailDegree * 0.5F, false, 10.0F, -0.85F, ageInTicks, flailAmount);
        this.walk(this.right_BackMidTentacle, flailSpeed, flailDegree * 0.5F, false, 12.0F, -0.5F, ageInTicks, flailAmount);
        this.flap(this.right_BackMidTentacle, flailSpeed, flailDegree * 0.5F, true, 12.0F, -0.15F, ageInTicks, flailAmount);
        this.walk(this.right_BackMidTentacleEnd, flailSpeed, flailDegree * 0.5F, false, 12.0F, -0.85F, ageInTicks, flailAmount);
        this.walk(this.left_FrontMidTentacle, flailSpeed, flailDegree * 0.5F, false, 14.0F, -0.5F, ageInTicks, flailAmount);
        this.flap(this.left_FrontMidTentacle, flailSpeed, flailDegree * 0.5F, false, 14.0F, -0.15F, ageInTicks, flailAmount);
        this.walk(this.left_FrontMidTentacleEnd, flailSpeed, flailDegree * 0.5F, false, 14.0F, -0.85F, ageInTicks, flailAmount);
        this.walk(this.right_FrontMidTentacle, flailSpeed, flailDegree * 0.5F, false, 16.0F, -0.5F, ageInTicks, flailAmount);
        this.flap(this.right_FrontMidTentacle, flailSpeed, flailDegree * 0.5F, true, 16.0F, -0.15F, ageInTicks, flailAmount);
        this.walk(this.right_FrontMidTentacleEnd, flailSpeed, flailDegree * 0.5F, false, 16.0F, -0.85F, ageInTicks, flailAmount);
        this.walk(this.left_FrontTentacle, idleSpeed, idleDegree, true, 0.0F, 0.05F, ageInTicks, 1.0F);
        this.flap(this.left_FrontTentacle, idleSpeed, idleDegree, true, 0.0F, 0.035F, ageInTicks, 1.0F);
        this.walk(this.right_FrontTentacle, idleSpeed, idleDegree, true, 0.0F, 0.05F, ageInTicks, 1.0F);
        this.flap(this.right_FrontTentacle, idleSpeed, idleDegree, false, 0.0F, 0.035F, ageInTicks, 1.0F);
        this.walk(this.left_BackTentacle, idleSpeed, idleDegree, true, 0.0F, 0.05F, ageInTicks, 1.0F);
        this.flap(this.left_BackTentacle, idleSpeed, idleDegree, true, 0.0F, 0.035F, ageInTicks, 1.0F);
        this.walk(this.right_BackTentacle, idleSpeed, idleDegree, true, 0.0F, 0.05F, ageInTicks, 1.0F);
        this.flap(this.right_BackTentacle, idleSpeed, idleDegree, false, 0.0F, 0.035F, ageInTicks, 1.0F);
        this.walk(this.right_BackMidTentacle, idleSpeed, idleDegree, true, 0.0F, 0.05F, ageInTicks, 1.0F);
        this.swing(this.right_BackMidTentacle, idleSpeed, idleDegree, true, 0.0F, -0.075F, ageInTicks, 1.0F);
        this.walk(this.right_FrontMidTentacle, idleSpeed, idleDegree, true, 0.0F, 0.05F, ageInTicks, 1.0F);
        this.swing(this.right_FrontMidTentacle, idleSpeed, idleDegree, false, 0.0F, -0.075F, ageInTicks, 1.0F);
        this.walk(this.left_BackMidTentacle, idleSpeed, idleDegree, true, 0.0F, 0.05F, ageInTicks, 1.0F);
        this.swing(this.left_BackMidTentacle, idleSpeed, idleDegree, false, 0.0F, -0.075F, ageInTicks, 1.0F);
        this.walk(this.left_FrontMidTentacle, idleSpeed, idleDegree, true, 0.0F, 0.05F, ageInTicks, 1.0F);
        this.swing(this.left_FrontMidTentacle, idleSpeed, idleDegree, true, 0.0F, -0.075F, ageInTicks, 1.0F);
        this.walk(this.left_FrontTentacleEnd, idleSpeed, idleDegree, true, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.right_tentacleEnd, idleSpeed, idleDegree, true, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.left_BackTentacleEnd, idleSpeed, idleDegree, true, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.right_BackTentacleEnd, idleSpeed, idleDegree, true, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.right_BackMidTentacleEnd, idleSpeed, idleDegree, true, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.left_BackMidTentacleEnd, idleSpeed, idleDegree, true, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.right_FrontMidTentacleEnd, idleSpeed, idleDegree, true, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.left_FrontMidTentacleEnd, idleSpeed, idleDegree, true, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.left_arm, idleSpeed, idleDegree * 0.1F, true, 0.0F, 0.01F, ageInTicks, 1.0F);
        this.walk(this.right_arm, idleSpeed, idleDegree * 0.1F, true, 0.0F, 0.01F, ageInTicks, 1.0F);
        this.walk(this.left_arm2, idleSpeed, idleDegree * 0.1F, false, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.right_arm2, idleSpeed, idleDegree * 0.1F, false, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.left_arm3, idleSpeed, idleDegree * 0.1F, true, -4.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.right_arm3, idleSpeed, idleDegree * 0.1F, true, -4.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.left_arm4, idleSpeed, idleDegree * 0.2F, true, -6.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.right_arm4, idleSpeed, idleDegree * 0.2F, true, -6.0F, 0.0F, ageInTicks, 1.0F);
        this.left_pupil.rotateAngleX += f;
        this.right_pupil.rotateAngleX += f;
        if (grabProgress >= 5.0F) {
            this.walk(this.beak, 0.7F, 0.35F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.bob(this.beak, 0.7F, 0.35F, true, ageInTicks, 1.0F);
        }
        Entity look = Minecraft.getInstance().getCameraEntity();
        if (look != null) {
            Vec3 vector3d = look.getEyePosition(partialTick);
            Vec3 vector3d1 = entity.m_20299_(partialTick);
            float dist = Mth.clamp((float) vector3d.subtract(vector3d1).length() * 0.2F, 0.4F, 1.0F);
            float eyeScale = 1.4F - dist;
            float maxEyeDist = 0.7F;
            double d0 = vector3d.y - vector3d1.y;
            Vec3 vector3d2 = entity.m_20252_(0.0F);
            vector3d2 = new Vec3(vector3d2.x, 0.0, vector3d2.z);
            Vec3 vector3d3 = new Vec3(vector3d.x - vector3d1.x, 0.0, vector3d.z - vector3d1.z).normalize();
            double d1 = vector3d2.dot(vector3d3);
            double eyeXz = (double) (Mth.sqrt((float) Math.abs(d1)) * -2.0F * (float) Math.signum(d1));
            this.left_pupil.setScale(eyeScale, eyeScale, eyeScale);
            this.left_pupil.rotationPointZ = this.left_pupil.rotationPointZ - (float) Mth.clamp(-eyeXz, (double) (-maxEyeDist / eyeScale), (double) (maxEyeDist / eyeScale));
            this.left_pupil.rotationPointY = this.left_pupil.rotationPointY + (float) Mth.clamp(-d0, (double) (-maxEyeDist / eyeScale), (double) (maxEyeDist / eyeScale));
            this.right_pupil.setScale(eyeScale, eyeScale, eyeScale);
            this.right_pupil.rotationPointZ = this.right_pupil.rotationPointZ + (float) Mth.clamp(eyeXz, (double) (-maxEyeDist / eyeScale), (double) (maxEyeDist / eyeScale));
            this.right_pupil.rotationPointY = this.right_pupil.rotationPointY + (float) Mth.clamp(-d0, (double) (-maxEyeDist / eyeScale), (double) (maxEyeDist / eyeScale));
        }
    }

    private float getArmRot(EntityGiantSquid entity, int offset, float partialTick, boolean pitch) {
        float rotWrap = Mth.wrapDegrees(entity.getRingBuffer(offset, partialTick, pitch) - entity.getRingBuffer(0, partialTick, pitch));
        return Mth.clamp(rotWrap, -50.0F, 50.0F) * 0.4F * (float) (Math.PI / 180.0);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.head, this.beak, this.right_pupil_pivot, this.left_pupil_pivot, this.left_FrontTentacle, this.left_FrontTentacleEnd, this.left_FrontMidTentacle, this.left_FrontMidTentacleEnd, this.right_FrontMidTentacle, this.right_FrontMidTentacleEnd, this.left_BackMidTentacle, new AdvancedModelBox[] { this.left_BackMidTentacleEnd, this.right_BackMidTentacle, this.right_BackMidTentacleEnd, this.left_BackTentacle, this.left_BackTentacleEnd, this.right_BackTentacle, this.right_BackTentacleEnd, this.right_FrontTentacle, this.right_tentacleEnd, this.left_arm, this.left_arm2, this.left_arm3, this.left_arm4, this.left_hand, this.right_arm, this.right_arm2, this.right_arm3, this.right_arm4, this.right_hand, this.left_eye, this.left_pupil, this.right_eye, this.right_pupil, this.mantle, this.mantle_end, this.left_membrane, this.right_membrane });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}