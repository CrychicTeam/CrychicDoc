package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.SeaPigEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;

public class SeaPigModel extends AdvancedEntityModel<SeaPigEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox rleg1;

    private final AdvancedModelBox rleg2;

    private final AdvancedModelBox rleg3;

    private final AdvancedModelBox rleg4;

    private final AdvancedModelBox lleg1;

    private final AdvancedModelBox lleg2;

    private final AdvancedModelBox lleg3;

    private final AdvancedModelBox lleg4;

    private final AdvancedModelBox head;

    private final AdvancedModelBox lfrontAntennae;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox rfrontAntennae;

    private final AdvancedModelBox cube_r2;

    private final AdvancedModelBox lbackAntennae;

    private final AdvancedModelBox cube_r3;

    private final AdvancedModelBox rbackAntennae;

    private final AdvancedModelBox cube_r4;

    public SeaPigModel() {
        this.texWidth = 48;
        this.texHeight = 48;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 20.0F, 0.5F);
        this.body.setTextureOffset(0, 0).addBox(-2.5F, -3.0F, -4.5F, 5.0F, 6.0F, 9.0F, 0.0F, false);
        this.body.setTextureOffset(0, 29).addBox(-2.5F, -2.0F, -4.5F, 5.0F, 3.0F, 9.0F, -0.25F, false);
        this.rleg1 = new AdvancedModelBox(this);
        this.rleg1.setRotationPoint(-2.0F, 2.75F, -3.5F);
        this.body.addChild(this.rleg1);
        this.rleg1.setTextureOffset(0, 0).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.rleg2 = new AdvancedModelBox(this);
        this.rleg2.setRotationPoint(-2.0F, 2.75F, -1.0F);
        this.body.addChild(this.rleg2);
        this.rleg2.setTextureOffset(0, 0).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.rleg3 = new AdvancedModelBox(this);
        this.rleg3.setRotationPoint(-2.0F, 2.75F, 1.5F);
        this.body.addChild(this.rleg3);
        this.rleg3.setTextureOffset(0, 0).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.rleg4 = new AdvancedModelBox(this);
        this.rleg4.setRotationPoint(-2.0F, 2.75F, 4.0F);
        this.body.addChild(this.rleg4);
        this.rleg4.setTextureOffset(0, 0).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.lleg1 = new AdvancedModelBox(this);
        this.lleg1.setRotationPoint(2.0F, 2.75F, -3.5F);
        this.body.addChild(this.lleg1);
        this.lleg1.setTextureOffset(0, 0).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        this.lleg2 = new AdvancedModelBox(this);
        this.lleg2.setRotationPoint(2.0F, 2.75F, -1.0F);
        this.body.addChild(this.lleg2);
        this.lleg2.setTextureOffset(0, 0).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        this.lleg3 = new AdvancedModelBox(this);
        this.lleg3.setRotationPoint(2.0F, 2.75F, 1.5F);
        this.body.addChild(this.lleg3);
        this.lleg3.setTextureOffset(0, 0).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        this.lleg4 = new AdvancedModelBox(this);
        this.lleg4.setRotationPoint(2.0F, 2.75F, 4.0F);
        this.body.addChild(this.lleg4);
        this.lleg4.setTextureOffset(0, 0).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -0.5F, -4.5F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(16, 6).addBox(-2.0F, 3.0F, -2.0F, 4.0F, 0.0F, 3.0F, 0.0F, false);
        this.head.setTextureOffset(0, 15).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 3.0F, 0.0F, false);
        this.lfrontAntennae = new AdvancedModelBox(this);
        this.lfrontAntennae.setRotationPoint(1.75F, -2.5F, -4.5F);
        this.body.addChild(this.lfrontAntennae);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lfrontAntennae.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.3927F, -0.3927F, 0.0F);
        this.cube_r1.setTextureOffset(0, 14).addBox(0.0F, -6.5F, -8.0F, 0.0F, 7.0F, 8.0F, 0.0F, false);
        this.rfrontAntennae = new AdvancedModelBox(this);
        this.rfrontAntennae.setRotationPoint(-1.75F, -2.5F, -4.5F);
        this.body.addChild(this.rfrontAntennae);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rfrontAntennae.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, 0.3927F, 0.3927F, 0.0F);
        this.cube_r2.setTextureOffset(0, 14).addBox(0.0F, -6.5F, -8.0F, 0.0F, 7.0F, 8.0F, 0.0F, true);
        this.lbackAntennae = new AdvancedModelBox(this);
        this.lbackAntennae.setRotationPoint(2.0F, -3.0F, 2.0F);
        this.body.addChild(this.lbackAntennae);
        this.cube_r3 = new AdvancedModelBox(this);
        this.cube_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lbackAntennae.addChild(this.cube_r3);
        this.setRotateAngle(this.cube_r3, -0.3927F, 0.0F, 0.7854F);
        this.cube_r3.setTextureOffset(20, 9).addBox(0.0F, -10.0F, -0.5F, 0.0F, 11.0F, 6.0F, 0.0F, false);
        this.rbackAntennae = new AdvancedModelBox(this);
        this.rbackAntennae.setRotationPoint(-2.0F, -3.0F, 2.0F);
        this.body.addChild(this.rbackAntennae);
        this.cube_r4 = new AdvancedModelBox(this);
        this.cube_r4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rbackAntennae.addChild(this.cube_r4);
        this.setRotateAngle(this.cube_r4, -0.3927F, 0.0F, -0.7854F);
        this.cube_r4.setTextureOffset(20, 9).addBox(0.0F, -10.0F, -0.5F, 0.0F, 11.0F, 6.0F, 0.0F, true);
        this.updateDefaultPose();
        this.body.scaleChildren = true;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.rleg1, this.rleg2, this.rleg3, this.rleg4, this.lleg1, this.lleg2, this.lleg3, this.lleg4, this.head, this.lfrontAntennae, this.lbackAntennae, new AdvancedModelBox[] { this.rfrontAntennae, this.rbackAntennae, this.cube_r1, this.cube_r2, this.cube_r3, this.cube_r4 });
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    public void translateToBody(PoseStack poseStack) {
        this.body.translateAndRotate(poseStack);
    }

    public void setupAnim(SeaPigEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float squish = entity.getSquishProgress(ageInTicks - (float) entity.f_19797_);
        float walkSpeed = 1.0F;
        float walkDegree = 1.3F;
        this.body.rotationPointY += squish * 3.0F;
        this.body.setScale(1.0F, 1.0F - squish * 0.7F, 1.0F);
        this.walk(this.rleg1, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.rleg1.rotationPointY = this.rleg1.rotationPointY - ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, 1.5F, 0.2F, true);
        this.walk(this.rleg2, walkSpeed, walkDegree, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.rleg2.rotationPointY = this.rleg2.rotationPointY - ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, 3.5F, 0.2F, true);
        this.walk(this.rleg3, walkSpeed, walkDegree, false, 4.0F, 0.0F, limbSwing, limbSwingAmount);
        this.rleg3.rotationPointY = this.rleg3.rotationPointY - ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, 5.5F, 0.2F, true);
        this.walk(this.rleg4, walkSpeed, walkDegree, false, 6.0F, 0.0F, limbSwing, limbSwingAmount);
        this.rleg4.rotationPointY = this.rleg4.rotationPointY - ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, 7.5F, 0.2F, true);
        this.walk(this.lleg1, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.lleg1.rotationPointY = this.lleg1.rotationPointY - ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, 1.5F, 0.2F, true);
        this.walk(this.lleg2, walkSpeed, walkDegree, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.lleg2.rotationPointY = this.lleg2.rotationPointY - ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, 3.5F, 0.2F, true);
        this.walk(this.lleg3, walkSpeed, walkDegree, false, 4.0F, 0.0F, limbSwing, limbSwingAmount);
        this.lleg3.rotationPointY = this.lleg3.rotationPointY - ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, 5.5F, 0.2F, true);
        this.walk(this.lleg4, walkSpeed, walkDegree, false, 6.0F, 0.0F, limbSwing, limbSwingAmount);
        this.lleg4.rotationPointY = this.lleg4.rotationPointY - ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, 7.5F, 0.2F, true);
        this.walk(this.body, walkSpeed, walkDegree * 0.05F, false, -0.5F, 0.0F, limbSwing, limbSwingAmount);
        this.body.rotationPointY = this.body.rotationPointY - ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, 0.5F, 0.2F, true);
        this.walk(this.rfrontAntennae, 0.1F, 0.1F, true, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.lfrontAntennae, 0.1F, 0.1F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.rbackAntennae, 0.1F, 0.1F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.lbackAntennae, 0.1F, 0.1F, true, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.head, 0.2F, 0.03F, true, 3.0F, 0.0F, ageInTicks, 1.0F);
    }
}