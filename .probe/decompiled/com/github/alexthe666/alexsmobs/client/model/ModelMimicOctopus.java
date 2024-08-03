package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityMimicOctopus;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;

public class ModelMimicOctopus extends AdvancedEntityModel<EntityMimicOctopus> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox leftEye;

    private final AdvancedModelBox leftEyeSpike;

    private final AdvancedModelBox rightEye;

    private final AdvancedModelBox rightEyeSpike;

    private final AdvancedModelBox leftFrontArm1;

    private final AdvancedModelBox rightFrontArm1;

    private final AdvancedModelBox leftFrontArm2;

    private final AdvancedModelBox rightFrontArm2;

    private final AdvancedModelBox leftBackArm1;

    private final AdvancedModelBox rightBackArm1;

    private final AdvancedModelBox leftBackArm2;

    private final AdvancedModelBox rightBackArm2;

    private final AdvancedModelBox mantle;

    private final AdvancedModelBox creeperPivots1;

    private final AdvancedModelBox creeperPivots2;

    private final AdvancedModelBox creeperPivots3;

    private final AdvancedModelBox creeperPivots4;

    public ModelMimicOctopus() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(30, 24).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 4.0F, 7.0F, 0.0F, false);
        this.leftEye = new AdvancedModelBox(this, "leftEye");
        this.leftEye.setRotationPoint(2.0F, -2.0F, -2.5F);
        this.body.addChild(this.leftEye);
        this.leftEye.setTextureOffset(35, 18).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        this.leftEyeSpike = new AdvancedModelBox(this, "leftEyeSpike");
        this.leftEyeSpike.setRotationPoint(-1.0F, -1.0F, -1.5F);
        this.leftEye.addChild(this.leftEyeSpike);
        this.leftEyeSpike.setTextureOffset(0, 0).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, false);
        this.rightEye = new AdvancedModelBox(this, "rightEye");
        this.rightEye.setRotationPoint(-2.0F, -2.0F, -2.5F);
        this.body.addChild(this.rightEye);
        this.rightEye.setTextureOffset(35, 18).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 1.0F, 3.0F, 0.0F, true);
        this.rightEyeSpike = new AdvancedModelBox(this, "rightEyeSpike");
        this.rightEyeSpike.setRotationPoint(1.0F, -1.0F, -1.5F);
        this.rightEye.addChild(this.rightEyeSpike);
        this.rightEyeSpike.setTextureOffset(0, 0).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, true);
        this.leftFrontArm1 = new AdvancedModelBox(this, "leftFrontArm1");
        this.leftFrontArm1.setRotationPoint(2.0F, 2.0F, -4.0F);
        this.setRotationAngle(this.leftFrontArm1, 0.0F, 0.6109F, 0.0F);
        this.leftFrontArm1.setTextureOffset(26, 0).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, 0.0F, false);
        this.leftFrontArm1.setTextureOffset(35, 11).addBox(11.0F, -4.0F, -1.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
        this.rightFrontArm1 = new AdvancedModelBox(this, "rightFrontArm1");
        this.rightFrontArm1.setRotationPoint(-2.0F, 2.0F, -4.0F);
        this.setRotationAngle(this.rightFrontArm1, 0.0F, -0.6109F, 0.0F);
        this.rightFrontArm1.setTextureOffset(26, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, 0.0F, true);
        this.rightFrontArm1.setTextureOffset(35, 11).addBox(-15.0F, -4.0F, -1.0F, 4.0F, 3.0F, 2.0F, 0.0F, true);
        this.leftFrontArm2 = new AdvancedModelBox(this, "leftFrontArm2");
        this.leftFrontArm2.setRotationPoint(2.0F, 2.0F, -2.3F);
        this.setRotationAngle(this.leftFrontArm2, 0.0F, 0.3054F, 0.0F);
        this.leftFrontArm2.setTextureOffset(0, 26).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, 0.0F, false);
        this.leftFrontArm2.setTextureOffset(35, 5).addBox(11.0F, -4.0F, -1.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
        this.rightFrontArm2 = new AdvancedModelBox(this, "rightFrontArm2");
        this.rightFrontArm2.setRotationPoint(-2.0F, 2.0F, -2.3F);
        this.setRotationAngle(this.rightFrontArm2, 0.0F, -0.3054F, 0.0F);
        this.rightFrontArm2.setTextureOffset(0, 26).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, 0.0F, true);
        this.rightFrontArm2.setTextureOffset(35, 5).addBox(-15.0F, -4.0F, -1.0F, 4.0F, 3.0F, 2.0F, 0.0F, true);
        this.creeperPivots1 = new AdvancedModelBox(this, "creeperPivots1");
        this.creeperPivots2 = new AdvancedModelBox(this, "creeperPivots2");
        this.creeperPivots3 = new AdvancedModelBox(this, "creeperPivots3");
        this.creeperPivots4 = new AdvancedModelBox(this, "creeperPivots4");
        this.body.addChild(this.creeperPivots1);
        this.body.addChild(this.creeperPivots2);
        this.body.addChild(this.creeperPivots3);
        this.body.addChild(this.creeperPivots4);
        this.leftBackArm1 = new AdvancedModelBox(this, "leftBackArm1");
        this.leftBackArm1.setRotationPoint(2.0F, 2.0F, -1.0F);
        this.setRotationAngle(this.leftBackArm1, 0.0F, -0.2182F, 0.0F);
        this.leftBackArm1.setTextureOffset(0, 21).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, 0.0F, false);
        this.leftBackArm1.setTextureOffset(13, 31).addBox(11.0F, -4.0F, -1.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
        this.rightBackArm1 = new AdvancedModelBox(this, "rightBackArm1");
        this.rightBackArm1.setRotationPoint(-2.0F, 2.0F, -1.0F);
        this.setRotationAngle(this.rightBackArm1, 0.0F, 0.2182F, 0.0F);
        this.rightBackArm1.setTextureOffset(0, 21).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, 0.0F, true);
        this.rightBackArm1.setTextureOffset(13, 31).addBox(-15.0F, -4.0F, -1.0F, 4.0F, 3.0F, 2.0F, 0.0F, true);
        this.leftBackArm2 = new AdvancedModelBox(this, "leftBackArm2");
        this.leftBackArm2.setRotationPoint(2.0F, 2.0F, 1.0F);
        this.setRotationAngle(this.leftBackArm2, 0.0F, -0.6981F, 0.0F);
        this.leftBackArm2.setTextureOffset(0, 16).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, 0.0F, false);
        this.leftBackArm2.setTextureOffset(0, 31).addBox(11.0F, -4.0F, -1.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
        this.rightBackArm2 = new AdvancedModelBox(this, "rightBackArm2");
        this.rightBackArm2.setRotationPoint(-2.0F, 2.0F, 1.0F);
        this.setRotationAngle(this.rightBackArm2, 0.0F, 0.6981F, 0.0F);
        this.rightBackArm2.setTextureOffset(0, 16).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, 0.0F, true);
        this.rightBackArm2.setTextureOffset(0, 31).addBox(-15.0F, -4.0F, -1.0F, 4.0F, 3.0F, 2.0F, 0.0F, true);
        this.mantle = new AdvancedModelBox(this, "mantle");
        this.mantle.setRotationPoint(0.0F, -1.0F, 2.0F);
        this.body.addChild(this.mantle);
        this.mantle.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -2.0F, 8.0F, 6.0F, 9.0F, 0.0F, false);
        this.creeperPivots1.addChild(this.leftFrontArm1);
        this.creeperPivots1.addChild(this.leftFrontArm2);
        this.creeperPivots2.addChild(this.leftBackArm1);
        this.creeperPivots2.addChild(this.leftBackArm2);
        this.creeperPivots3.addChild(this.rightFrontArm1);
        this.creeperPivots3.addChild(this.rightFrontArm2);
        this.creeperPivots4.addChild(this.rightBackArm1);
        this.creeperPivots4.addChild(this.rightBackArm2);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityMimicOctopus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float transProgress = entity.prevTransProgress + (entity.transProgress - entity.prevTransProgress) * partialTicks;
        float groundProgress = entity.prevGroundProgress + (entity.groundProgress - entity.prevGroundProgress) * partialTicks;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTicks;
        float notSitProgress = 1.0F - sitProgress * 0.2F;
        float swimProgress = (5.0F - groundProgress) * notSitProgress;
        float groundProgressNorm = groundProgress * 0.2F * notSitProgress;
        if (entity.getPrevMimicState() != null) {
            float progress = notSitProgress * (5.0F - transProgress);
            this.animateForMimicGround(entity.getPrevMimicState(), entity, limbSwing, limbSwingAmount, ageInTicks, progress * groundProgressNorm);
            if (sitProgress == 0.0F) {
                this.animateForMimicWater(entity.getPrevMimicState(), entity, limbSwing, limbSwingAmount, ageInTicks, progress * (1.0F - groundProgressNorm));
            }
        }
        this.animateForMimicGround(entity.getMimicState(), entity, limbSwing, limbSwingAmount, ageInTicks, notSitProgress * transProgress * groundProgressNorm);
        this.animateForMimicWater(entity.getMimicState(), entity, limbSwing, limbSwingAmount, ageInTicks, notSitProgress * transProgress * (1.0F - groundProgressNorm));
        if (swimProgress > 0.0F) {
            float rot = headPitch * (float) (Math.PI / 180.0);
            this.body.rotationPointY = this.body.rotationPointY + Math.abs(rot) * -7.0F;
            this.body.rotateAngleX -= rot;
        }
        this.progressRotationPrev(this.mantle, sitProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftFrontArm1, sitProgress, 0.0F, Maths.rad(10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftFrontArm2, sitProgress, 0.0F, Maths.rad(-5.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftBackArm1, sitProgress, 0.0F, Maths.rad(-10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftBackArm2, sitProgress, 0.0F, Maths.rad(-15.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightFrontArm1, sitProgress, 0.0F, Maths.rad(-10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightFrontArm2, sitProgress, 0.0F, Maths.rad(5.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightBackArm1, sitProgress, 0.0F, Maths.rad(10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightBackArm2, sitProgress, 0.0F, Maths.rad(15.0), 0.0F, 5.0F);
    }

    public void animateForMimicWater(EntityMimicOctopus.MimicState state, EntityMimicOctopus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float swimProgress) {
        limbSwingAmount = limbSwingAmount * swimProgress * 0.2F;
        this.progressRotationPrev(this.body, swimProgress, 0.0F, Maths.rad(-180.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftFrontArm1, swimProgress, Maths.rad(-90.0), Maths.rad(10.0), Maths.rad(-50.0), 5.0F);
        this.progressRotationPrev(this.leftFrontArm2, swimProgress, Maths.rad(-90.0), Maths.rad(20.0), Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.leftBackArm1, swimProgress, Maths.rad(-90.0), Maths.rad(50.0), Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.leftBackArm2, swimProgress, Maths.rad(-90.0), Maths.rad(70.0), Maths.rad(50.0), 5.0F);
        this.progressPositionPrev(this.leftFrontArm1, swimProgress, -1.0F, -1.0F, 1.0F, 5.0F);
        this.progressPosition(this.leftFrontArm2, swimProgress, this.leftFrontArm1.rotationPointX, this.leftFrontArm1.rotationPointY, this.leftFrontArm1.rotationPointZ, 5.0F);
        this.progressPosition(this.leftBackArm1, swimProgress, this.leftFrontArm1.rotationPointX, this.leftFrontArm1.rotationPointY, this.leftFrontArm1.rotationPointZ, 5.0F);
        this.progressPosition(this.leftBackArm2, swimProgress, this.leftFrontArm1.rotationPointX, this.leftFrontArm1.rotationPointY, this.leftFrontArm1.rotationPointZ, 5.0F);
        this.progressRotationPrev(this.rightFrontArm1, swimProgress, Maths.rad(-90.0), Maths.rad(-10.0), Maths.rad(50.0), 5.0F);
        this.progressRotationPrev(this.rightFrontArm2, swimProgress, Maths.rad(-90.0), Maths.rad(-20.0), Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.rightBackArm1, swimProgress, Maths.rad(-90.0), Maths.rad(-50.0), Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.rightBackArm2, swimProgress, Maths.rad(-90.0), Maths.rad(-70.0), Maths.rad(-50.0), 5.0F);
        this.progressPositionPrev(this.rightFrontArm1, swimProgress, 1.0F, -1.0F, 1.0F, 5.0F);
        this.progressPosition(this.rightFrontArm2, swimProgress, this.rightFrontArm1.rotationPointX, this.rightFrontArm1.rotationPointY, this.rightFrontArm1.rotationPointZ, 5.0F);
        this.progressPosition(this.rightBackArm1, swimProgress, this.rightFrontArm1.rotationPointX, this.rightFrontArm1.rotationPointY, this.rightFrontArm1.rotationPointZ, 5.0F);
        this.progressPosition(this.rightBackArm2, swimProgress, this.rightFrontArm1.rotationPointX, this.rightFrontArm1.rotationPointY, this.rightFrontArm1.rotationPointZ, 5.0F);
        if (state == EntityMimicOctopus.MimicState.GUARDIAN) {
            float degree = 1.6F;
            float speed = 0.5F;
            this.progressPositionPrev(this.body, swimProgress, 0.0F, -4.0F, 5.0F, 5.0F);
            this.progressPositionPrev(this.mantle, swimProgress, 0.0F, 2.0F, 0.0F, 5.0F);
            if (swimProgress > 0.0F) {
                this.mantle.setScale(1.0F + swimProgress * 0.1F, 1.0F + swimProgress * 0.1F, 1.0F + swimProgress * 0.1F);
            }
            this.progressRotationPrev(this.leftFrontArm1, swimProgress, Maths.rad(90.0), Maths.rad(45.0), Maths.rad(50.0), 5.0F);
            this.progressRotationPrev(this.rightFrontArm1, swimProgress, Maths.rad(-90.0), Maths.rad(-45.0), Maths.rad(-50.0), 5.0F);
            this.progressPositionPrev(this.leftFrontArm1, swimProgress, -1.0F, -1.0F, 0.0F, 5.0F);
            this.progressPositionPrev(this.rightFrontArm1, swimProgress, 1.0F, 1.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.leftFrontArm2, swimProgress, Maths.rad(90.0), Maths.rad(-40.0), Maths.rad(-15.0), 5.0F);
            this.progressPositionPrev(this.leftFrontArm2, swimProgress, 0.0F, 1.0F, 5.0F, 5.0F);
            this.progressRotationPrev(this.leftBackArm1, swimProgress, Maths.rad(90.0), Maths.rad(-20.0), Maths.rad(10.0), 5.0F);
            this.progressPositionPrev(this.leftBackArm1, swimProgress, -1.0F, 0.0F, 2.0F, 5.0F);
            this.progressRotationPrev(this.leftBackArm2, swimProgress, Maths.rad(90.0), Maths.rad(-70.0), Maths.rad(-15.0), 5.0F);
            this.progressPositionPrev(this.leftBackArm2, swimProgress, 0.0F, 1.0F, 5.0F, 5.0F);
            this.progressRotationPrev(this.rightFrontArm2, swimProgress, Maths.rad(90.0), Maths.rad(40.0), Maths.rad(15.0), 5.0F);
            this.progressPositionPrev(this.rightFrontArm2, swimProgress, 0.0F, 1.0F, 5.0F, 5.0F);
            this.progressRotationPrev(this.rightBackArm1, swimProgress, Maths.rad(90.0), Maths.rad(20.0), Maths.rad(-10.0), 5.0F);
            this.progressPositionPrev(this.rightBackArm1, swimProgress, 1.0F, 0.0F, 2.0F, 5.0F);
            this.progressRotationPrev(this.rightBackArm2, swimProgress, Maths.rad(90.0), Maths.rad(70.0), Maths.rad(15.0), 5.0F);
            this.progressPositionPrev(this.rightBackArm2, swimProgress, 0.0F, 1.0F, 5.0F, 5.0F);
            this.swing(this.leftFrontArm1, speed, degree * 0.25F, true, 0.0F, 0.1F, limbSwing, limbSwingAmount);
            this.swing(this.rightFrontArm1, speed, degree * 0.25F, true, 0.0F, 0.1F, limbSwing, limbSwingAmount);
        } else {
            float f = 1.0F;
            if (state == EntityMimicOctopus.MimicState.CREEPER) {
                this.progressPositionPrev(this.body, swimProgress, 0.0F, -3.0F, -2.0F, 5.0F);
                this.progressPositionPrev(this.mantle, swimProgress, 0.0F, -2.0F, 1.0F, 5.0F);
                this.progressRotationPrev(this.mantle, swimProgress, Maths.rad(-60.0), 0.0F, 0.0F, 5.0F);
                f = 0.5F;
            }
            float degree = 1.6F;
            float speed = 0.5F;
            this.bob(this.body, speed, degree * 2.0F, false, limbSwing, limbSwingAmount);
            if (swimProgress > 0.0F) {
                if (state == EntityMimicOctopus.MimicState.PUFFERFISH) {
                    float f2 = 1.4F + 0.15F * Mth.sin(speed * limbSwing - 2.0F) * swimProgress * 0.2F;
                    this.progressPositionPrev(this.mantle, swimProgress, 0.0F, 1.0F, 0.0F, 5.0F);
                    this.mantle.setScale(f2, f2, f2);
                } else {
                    float scale = 1.1F + 0.3F * f * Mth.sin(speed * limbSwing - 2.0F);
                    this.mantle.setScale((scale - 1.0F) * swimProgress * 0.05F + 1.0F, (scale - 1.0F) * swimProgress * 0.05F + 1.0F, (scale - 1.0F) * swimProgress * 0.2F + 1.0F);
                }
            }
            this.walk(this.rightEyeSpike, speed, degree * 0.2F, true, -2.0F, 0.1F, limbSwing, limbSwingAmount);
            this.walk(this.leftEyeSpike, speed, degree * 0.2F, true, -2.0F, 0.1F, limbSwing, limbSwingAmount);
            this.swing(this.leftFrontArm1, speed, degree * 0.35F, true, 0.0F, -0.1F, limbSwing, limbSwingAmount);
            this.swing(this.leftFrontArm2, speed, degree * 0.35F, true, 0.0F, -0.1F, limbSwing, limbSwingAmount);
            this.swing(this.leftBackArm1, speed, degree * 0.35F, true, 0.0F, -0.1F, limbSwing, limbSwingAmount);
            this.swing(this.leftBackArm2, speed, degree * 0.35F, true, 0.0F, -0.1F, limbSwing, limbSwingAmount);
            this.swing(this.rightFrontArm1, speed, degree * 0.35F, false, 0.0F, -0.1F, limbSwing, limbSwingAmount);
            this.swing(this.rightFrontArm2, speed, degree * 0.35F, false, 0.0F, -0.1F, limbSwing, limbSwingAmount);
            this.swing(this.rightBackArm1, speed, degree * 0.35F, false, 0.0F, -0.1F, limbSwing, limbSwingAmount);
            this.swing(this.rightBackArm2, speed, degree * 0.35F, false, 0.0F, -0.1F, limbSwing, limbSwingAmount);
        }
    }

    public void animateForMimicGround(EntityMimicOctopus.MimicState state, EntityMimicOctopus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float groundProgress) {
        this.mantle.setScale(1.0F, 1.0F, 1.0F);
        limbSwingAmount = limbSwingAmount * groundProgress * 0.2F;
        float degree = 0.8F;
        float speed = 0.8F;
        if (state == EntityMimicOctopus.MimicState.CREEPER) {
            this.progressRotationPrev(this.body, groundProgress, 0.0F, Maths.rad(-180.0), 0.0F, 5.0F);
            this.progressRotationPrev(this.mantle, groundProgress, Maths.rad(-80.0), 0.0F, 0.0F, 5.0F);
            this.progressPositionPrev(this.mantle, groundProgress, 0.0F, -3.0F, -1.0F, 5.0F);
            this.progressPositionPrev(this.body, groundProgress, 0.0F, -13.0F, -2.0F, 5.0F);
            this.progressRotationPrev(this.leftFrontArm1, groundProgress, Maths.rad(-20.0), Maths.rad(55.0), Maths.rad(-20.0), 5.0F);
            this.progressPositionPrev(this.leftFrontArm1, groundProgress, -1.0F, 0.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.rightFrontArm1, groundProgress, Maths.rad(-20.0), Maths.rad(-55.0), Maths.rad(20.0), 5.0F);
            this.progressPositionPrev(this.rightFrontArm1, groundProgress, 1.0F, 0.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.leftFrontArm2, groundProgress, Maths.rad(-20.0), Maths.rad(73.0), Maths.rad(-20.0), 5.0F);
            this.progressPositionPrev(this.leftFrontArm2, groundProgress, 1.0F, 0.0F, -1.65F, 5.0F);
            this.progressRotationPrev(this.rightFrontArm2, groundProgress, Maths.rad(-20.0), Maths.rad(-73.0), Maths.rad(20.0), 5.0F);
            this.progressPositionPrev(this.rightFrontArm2, groundProgress, -1.0F, 0.0F, -1.65F, 5.0F);
            this.progressRotationPrev(this.leftBackArm1, groundProgress, Maths.rad(-20.0), Maths.rad(-78.0), Maths.rad(20.0), 5.0F);
            this.progressPositionPrev(this.leftBackArm1, groundProgress, -1.0F, 0.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.rightBackArm1, groundProgress, Maths.rad(-20.0), Maths.rad(78.0), Maths.rad(-20.0), 5.0F);
            this.progressPositionPrev(this.rightBackArm1, groundProgress, 1.0F, 0.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.leftBackArm2, groundProgress, Maths.rad(-20.0), Maths.rad(-51.0), Maths.rad(20.0), 5.0F);
            this.progressPositionPrev(this.leftBackArm2, groundProgress, 1.0F, 0.0F, -2.0F, 5.0F);
            this.progressRotationPrev(this.rightBackArm2, groundProgress, Maths.rad(-20.0), Maths.rad(51.0), Maths.rad(-20.0), 5.0F);
            this.progressPositionPrev(this.rightBackArm2, groundProgress, -1.0F, 0.0F, -2.0F, 5.0F);
            this.progressRotationPrev(this.creeperPivots1, groundProgress, Maths.rad(90.0), 0.0F, 0.0F, 5.0F);
            this.progressPositionPrev(this.creeperPivots1, groundProgress, 0.0F, -2.0F, -5.0F, 5.0F);
            this.progressRotationPrev(this.creeperPivots3, groundProgress, Maths.rad(90.0), 0.0F, 0.0F, 5.0F);
            this.progressPositionPrev(this.creeperPivots3, groundProgress, 0.0F, -2.0F, -5.0F, 5.0F);
            this.progressRotationPrev(this.creeperPivots2, groundProgress, Maths.rad(-90.0), 0.0F, 0.0F, 5.0F);
            this.progressPositionPrev(this.creeperPivots2, groundProgress, 0.0F, 3.0F, 2.0F, 5.0F);
            this.progressRotationPrev(this.creeperPivots4, groundProgress, Maths.rad(-90.0), 0.0F, 0.0F, 5.0F);
            this.progressPositionPrev(this.creeperPivots4, groundProgress, 0.0F, 3.0F, 2.0F, 5.0F);
            this.walk(this.creeperPivots1, speed, degree * 0.25F, true, 1.0F, 0.1F, limbSwing, limbSwingAmount);
            this.walk(this.creeperPivots4, speed, degree * 0.25F, true, 1.0F, -0.1F, limbSwing, limbSwingAmount);
            this.walk(this.creeperPivots2, speed, degree * 0.25F, false, 1.0F, 0.1F, limbSwing, limbSwingAmount);
            this.walk(this.creeperPivots3, speed, degree * 0.25F, false, 1.0F, -0.1F, limbSwing, limbSwingAmount);
            this.flap(this.mantle, speed, degree * 0.25F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        } else {
            float idleDegree = 0.02F;
            float idleSpeed = 0.05F;
            this.swing(this.leftFrontArm1, idleSpeed, idleDegree, true, 0.0F, -0.05F, ageInTicks, groundProgress);
            this.swing(this.rightFrontArm1, idleSpeed, idleDegree, false, 0.0F, -0.05F, ageInTicks, groundProgress);
            this.swing(this.leftFrontArm2, idleSpeed, idleDegree, true, -1.0F, -0.02F, ageInTicks, groundProgress);
            this.swing(this.rightFrontArm2, idleSpeed, idleDegree, false, -1.0F, -0.02F, ageInTicks, groundProgress);
            this.swing(this.leftBackArm1, idleSpeed, idleDegree, true, -2.0F, 0.02F, ageInTicks, groundProgress);
            this.swing(this.rightBackArm1, idleSpeed, idleDegree, false, -2.0F, -0.02F, ageInTicks, groundProgress);
            this.swing(this.leftBackArm2, idleSpeed, idleDegree, true, -3.0F, 0.05F, ageInTicks, groundProgress);
            this.swing(this.rightBackArm2, idleSpeed, idleDegree, false, -3.0F, -0.05F, ageInTicks, groundProgress);
            this.flap(this.leftEyeSpike, idleSpeed, idleDegree, false, -5.0F, 0.1F, ageInTicks, groundProgress);
            this.flap(this.rightEyeSpike, idleSpeed, idleDegree, true, -5.0F, 0.1F, ageInTicks, groundProgress);
            this.walk(this.mantle, speed, degree * 0.15F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.mantle, speed, degree * 0.15F, true, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.body, speed, degree * 0.2F, true, -3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.leftFrontArm1, speed, degree * 0.3F, true, -1.0F, -0.3F, limbSwing, limbSwingAmount);
            this.swing(this.leftFrontArm2, speed, degree * 0.3F, true, -2.0F, -0.1F, limbSwing, limbSwingAmount);
            this.swing(this.leftBackArm1, speed, degree * 0.3F, true, -3.0F, -0.1F, limbSwing, limbSwingAmount);
            this.swing(this.leftBackArm2, speed, degree * 0.3F, true, -4.0F, -0.2F, limbSwing, limbSwingAmount);
            this.swing(this.rightFrontArm1, speed, degree * 0.3F, false, -1.0F, -0.3F, limbSwing, limbSwingAmount);
            this.swing(this.rightFrontArm2, speed, degree * 0.3F, false, -2.0F, -0.1F, limbSwing, limbSwingAmount);
            this.swing(this.rightBackArm1, speed, degree * 0.3F, false, -3.0F, -0.1F, limbSwing, limbSwingAmount);
            this.swing(this.rightBackArm2, speed, degree * 0.3F, false, -4.0F, 0.2F, limbSwing, limbSwingAmount);
            if (entity.hasGuardianLaser()) {
                this.progressRotationPrev(this.body, groundProgress, 0.0F, Maths.rad(180.0), 0.0F, 5.0F);
            }
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.mantle, this.rightEye, this.rightEyeSpike, this.leftEye, this.leftEyeSpike, this.body, this.leftFrontArm1, this.leftFrontArm2, this.rightFrontArm1, this.rightFrontArm2, this.leftBackArm1, new AdvancedModelBox[] { this.leftBackArm2, this.rightBackArm1, this.rightBackArm2, this.creeperPivots1, this.creeperPivots2, this.creeperPivots3, this.creeperPivots4 });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}