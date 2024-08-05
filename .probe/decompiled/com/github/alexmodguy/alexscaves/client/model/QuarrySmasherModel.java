package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.item.QuarrySmasherEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class QuarrySmasherModel extends AdvancedEntityModel<QuarrySmasherEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox cube_r2;

    private final AdvancedModelBox cube_r3;

    private final AdvancedModelBox cube_r4;

    private final AdvancedModelBox arm2;

    private final AdvancedModelBox coil;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox rarm;

    public QuarrySmasherModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 18.0F, 0.0F);
        this.body.setTextureOffset(20, 41).addBox(4.0F, -1.5F, -3.0F, 2.0F, 7.0F, 6.0F, 0.0F, false);
        this.body.setTextureOffset(36, 41).addBox(-6.0F, -1.5F, -3.0F, 2.0F, 7.0F, 6.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(-6.0F, -10.5F, -6.0F, 12.0F, 9.0F, 12.0F, 0.0F, false);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.body.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.7854F, 0.0F, 1.5708F);
        this.cube_r1.setTextureOffset(54, 4).addBox(-1.5F, -17.5F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        this.cube_r1.setTextureOffset(48, 26).addBox(-0.5F, -13.5F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.body.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, 0.7854F, 0.0F, -1.5708F);
        this.cube_r2.setTextureOffset(54, 4).addBox(-2.5F, -17.5F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, true);
        this.cube_r2.setTextureOffset(48, 26).addBox(-1.5F, -13.5F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        this.cube_r3 = new AdvancedModelBox(this);
        this.cube_r3.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.body.addChild(this.cube_r3);
        this.setRotateAngle(this.cube_r3, -0.7854F, 0.0F, -1.5708F);
        this.cube_r3.setTextureOffset(54, 4).addBox(-2.5F, -17.5F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, true);
        this.cube_r3.setTextureOffset(48, 26).addBox(-1.5F, -13.5F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        this.cube_r4 = new AdvancedModelBox(this);
        this.cube_r4.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.body.addChild(this.cube_r4);
        this.setRotateAngle(this.cube_r4, -0.7854F, 0.0F, 1.5708F);
        this.cube_r4.setTextureOffset(54, 4).addBox(-1.5F, -17.5F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        this.cube_r4.setTextureOffset(48, 26).addBox(-0.5F, -13.5F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        this.arm2 = new AdvancedModelBox(this);
        this.arm2.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.body.addChild(this.arm2);
        this.arm2.setTextureOffset(0, 54).addBox(-20.0F, -9.5F, -4.0F, 8.0F, 13.0F, 8.0F, 0.0F, true);
        this.arm2.setTextureOffset(32, 58).addBox(-18.0F, -14.5F, -2.0F, 4.0F, 5.0F, 4.0F, 0.0F, true);
        this.arm2.setTextureOffset(42, 61).addBox(-32.0F, -11.5F, -4.0F, 14.0F, 0.0F, 6.0F, 0.0F, true);
        this.arm2.setTextureOffset(46, 48).addBox(-14.0F, -11.5F, -2.0F, 14.0F, 0.0F, 6.0F, 0.0F, true);
        this.arm2.setTextureOffset(36, 4).addBox(-12.0F, -4.5F, -2.0F, 6.0F, 5.0F, 3.0F, 0.0F, true);
        this.coil = new AdvancedModelBox(this);
        this.coil.setRotationPoint(0.0F, 2.5F, 0.0F);
        this.body.addChild(this.coil);
        this.coil.setTextureOffset(0, 21).addBox(-4.5F, -3.5F, -3.5F, 9.0F, 7.0F, 7.0F, 0.0F, false);
        this.coil.setTextureOffset(26, 29).addBox(-4.0F, -3.0F, -3.0F, 8.0F, 6.0F, 6.0F, 0.0F, false);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(5.0F, -7.5F, 5.0F);
        this.body.addChild(this.lleg);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(5.0F, -7.5F, -5.0F);
        this.body.addChild(this.larm);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-5.0F, -7.5F, 5.0F);
        this.body.addChild(this.rleg);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(-5.0F, -7.5F, -5.0F);
        this.body.addChild(this.rarm);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    public void setupAnim(QuarrySmasherEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float chainLength = entity.getChainLength(partialTick);
        float inactiveProgress = entity.getInactiveProgress(partialTick);
        float activeProgress = 1.0F - inactiveProgress;
        float wiggleActive = entity.isBeingActivated() ? (float) Math.sin((double) activeProgress * Math.PI) : 0.0F;
        float shake = Math.max((float) entity.shakeTime - partialTick, 0.0F) / 10.0F;
        this.progressRotationPrev(this.body, inactiveProgress, (float) Math.toRadians(45.0), (float) Math.toRadians(45.0), 0.0F, 1.0F);
        this.flap(this.body, 1.0F, 0.5F, true, 0.0F, 0.0F, ageInTicks, wiggleActive);
        this.swing(this.body, 1.0F, 0.35F, false, 2.0F, 0.0F, ageInTicks, wiggleActive);
        this.body.rotateAngleX = (float) ((double) this.body.rotateAngleX + Math.sin((double) (ageInTicks * 0.7F + 1.0F)) * (double) shake * 0.05F);
        this.body.rotateAngleZ = (float) ((double) this.body.rotateAngleZ + Math.sin((double) (ageInTicks * 0.7F)) * (double) shake * 0.1F);
        this.coil.rotateAngleX = (float) Math.toRadians((double) (chainLength * 260.0F));
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.cube_r1, this.cube_r2, this.cube_r3, this.cube_r4, this.lleg, this.larm, this.rleg, this.rarm, this.arm2, this.coil);
    }
}