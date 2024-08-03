package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.TripodfishEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class TripodfishModel extends AdvancedEntityModel<TripodfishEntity> {

    private final AdvancedModelBox mainBody;

    private final AdvancedModelBox dorsalfin;

    private final AdvancedModelBox bfin;

    private final AdvancedModelBox rpectoralFin;

    private final AdvancedModelBox lpectoralFin;

    private final AdvancedModelBox lpelvicFin;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox rpelvicFin;

    private final AdvancedModelBox cube_r2;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox ttailFin;

    private final AdvancedModelBox btailFin;

    public TripodfishModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.mainBody = new AdvancedModelBox(this);
        this.mainBody.setRotationPoint(0.0F, 22.0F, -2.25F);
        this.mainBody.setTextureOffset(0, 0).addBox(-1.5F, -3.0F, -3.75F, 3.0F, 5.0F, 11.0F, 0.0F, false);
        this.mainBody.setTextureOffset(26, 24).addBox(-1.5F, -1.0F, -7.75F, 3.0F, 3.0F, 4.0F, 0.0F, false);
        this.dorsalfin = new AdvancedModelBox(this);
        this.dorsalfin.setRotationPoint(0.0F, -3.0F, 1.25F);
        this.mainBody.addChild(this.dorsalfin);
        this.dorsalfin.setTextureOffset(0, 7).addBox(0.0F, -5.0F, -1.0F, 0.0F, 6.0F, 9.0F, 0.0F, false);
        this.bfin = new AdvancedModelBox(this);
        this.bfin.setRotationPoint(0.0F, 2.0F, 3.75F);
        this.mainBody.addChild(this.bfin);
        this.bfin.setTextureOffset(22, 10).addBox(0.0F, -1.0F, -1.5F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        this.rpectoralFin = new AdvancedModelBox(this);
        this.rpectoralFin.setRotationPoint(-1.5F, -2.0F, -0.75F);
        this.mainBody.addChild(this.rpectoralFin);
        this.rpectoralFin.setTextureOffset(17, 0).addBox(-7.0F, -9.0F, 0.0F, 7.0F, 11.0F, 0.0F, 0.0F, true);
        this.lpectoralFin = new AdvancedModelBox(this);
        this.lpectoralFin.setRotationPoint(1.5F, -2.0F, -0.75F);
        this.mainBody.addChild(this.lpectoralFin);
        this.lpectoralFin.setTextureOffset(17, 0).addBox(0.0F, -9.0F, 0.0F, 7.0F, 11.0F, 0.0F, 0.0F, false);
        this.lpelvicFin = new AdvancedModelBox(this);
        this.lpelvicFin.setRotationPoint(1.5F, 2.0F, -0.25F);
        this.mainBody.addChild(this.lpelvicFin);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, 0.0F, 1.25F);
        this.lpelvicFin.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.0F, 0.0F, -0.4363F);
        this.cube_r1.setTextureOffset(0, 18).addBox(0.0F, -2.0F, -1.75F, 0.0F, 19.0F, 4.0F, 0.0F, false);
        this.rpelvicFin = new AdvancedModelBox(this);
        this.rpelvicFin.setRotationPoint(-1.5F, 2.0F, -0.25F);
        this.mainBody.addChild(this.rpelvicFin);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(0.0F, 0.0F, 1.25F);
        this.rpelvicFin.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, 0.0F, 0.0F, 0.4363F);
        this.cube_r2.setTextureOffset(0, 18).addBox(0.0F, -2.0F, -1.75F, 0.0F, 19.0F, 4.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, -0.5F, 6.75F);
        this.mainBody.addChild(this.tail);
        this.tail.setTextureOffset(10, 16).addBox(-1.0F, -2.0F, 0.5F, 2.0F, 4.0F, 8.0F, 0.0F, false);
        this.ttailFin = new AdvancedModelBox(this);
        this.ttailFin.setRotationPoint(0.0F, -1.5F, 8.5F);
        this.tail.addChild(this.ttailFin);
        this.ttailFin.setTextureOffset(16, 24).addBox(0.0F, -10.0F, 0.0F, 0.0F, 12.0F, 4.0F, 0.0F, false);
        this.btailFin = new AdvancedModelBox(this);
        this.btailFin.setRotationPoint(0.0F, 1.5F, 8.5F);
        this.tail.addChild(this.btailFin);
        this.btailFin.setTextureOffset(8, 24).addBox(-0.01F, -1.0F, 0.0F, 0.0F, 17.0F, 4.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.mainBody, this.bfin, this.btailFin, this.tail, this.dorsalfin, this.ttailFin, this.rpectoralFin, this.lpectoralFin, this.lpelvicFin, this.rpelvicFin, this.cube_r1, this.cube_r2, new AdvancedModelBox[0]);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.mainBody);
    }

    public void setupAnim(TripodfishEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float landProgress = entity.getLandProgress(partialTicks);
        float standProgress = entity.getStandProgress(partialTicks) * (1.0F - landProgress);
        float swimProgress = 1.0F - standProgress;
        float fishPitchAmount = entity.getFishPitch(partialTicks) / (180.0F / (float) Math.PI) * swimProgress;
        float swimSpeed = 0.8F;
        float swimDegree = 0.6F;
        this.progressRotationPrev(this.mainBody, landProgress, 0.0F, 0.0F, (float) Math.toRadians(85.0), 1.0F);
        this.progressPositionPrev(this.mainBody, standProgress, 0.0F, -15.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.mainBody, landProgress, 0.0F, -1.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.rpelvicFin, swimProgress, 0.0F, 0.0F, 1.0F, 1.0F);
        this.progressPositionPrev(this.lpelvicFin, swimProgress, 0.0F, 0.0F, 1.0F, 1.0F);
        this.progressRotationPrev(this.rpelvicFin, swimProgress, (float) Math.toRadians(70.0), 0.0F, (float) Math.toRadians(-30.0), 1.0F);
        this.progressRotationPrev(this.lpelvicFin, swimProgress, (float) Math.toRadians(70.0), 0.0F, (float) Math.toRadians(30.0), 1.0F);
        this.progressRotationPrev(this.ttailFin, swimProgress, (float) Math.toRadians(-10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.btailFin, swimProgress, (float) Math.toRadians(40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rpectoralFin, swimProgress, (float) Math.toRadians(-30.0), (float) Math.toRadians(-10.0), (float) Math.toRadians(-70.0), 1.0F);
        this.progressRotationPrev(this.lpectoralFin, swimProgress, (float) Math.toRadians(-30.0), (float) Math.toRadians(10.0), (float) Math.toRadians(70.0), 1.0F);
        this.walk(this.rpectoralFin, 0.15F, 0.3F, false, 1.0F, 0.1F, ageInTicks, swimProgress);
        this.walk(this.lpectoralFin, 0.15F, 0.3F, false, 1.0F, -0.1F, ageInTicks, swimProgress);
        this.bob(this.mainBody, 0.1F, 0.5F, false, ageInTicks, swimProgress);
        this.swing(this.mainBody, swimSpeed, swimDegree * 0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount * swimProgress);
        this.swing(this.tail, swimSpeed, swimDegree * 0.75F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount * swimProgress);
        this.swing(this.ttailFin, swimSpeed, swimDegree * 0.75F, false, -2.0F, 0.0F, limbSwing, limbSwingAmount * swimProgress);
        this.swing(this.btailFin, swimSpeed, swimDegree * 0.75F, false, -2.0F, 0.0F, limbSwing, limbSwingAmount * swimProgress);
        this.walk(this.rpectoralFin, swimSpeed, swimDegree * 0.4F, false, 1.0F, -0.1F, limbSwing, limbSwingAmount * swimProgress);
        this.walk(this.lpectoralFin, swimSpeed, swimDegree * 0.4F, false, 1.0F, 0.1F, limbSwing, limbSwingAmount * swimProgress);
        this.walk(this.rpectoralFin, 0.05F, 0.1F, false, 1.0F, 0.0F, ageInTicks, standProgress);
        this.walk(this.lpectoralFin, 0.05F, 0.1F, false, 1.0F, 0.0F, ageInTicks, standProgress);
        this.walk(this.ttailFin, 0.05F, 0.1F, false, -1.0F, 0.0F, ageInTicks, standProgress);
        this.walk(this.dorsalfin, 0.05F, 0.1F, false, 0.0F, 0.0F, ageInTicks, standProgress);
        this.mainBody.rotateAngleX += fishPitchAmount * 0.9F;
    }
}