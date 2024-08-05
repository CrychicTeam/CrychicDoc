package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.LanternfishEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class LanternfishModel extends AdvancedEntityModel<LanternfishEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox rightfin;

    private final AdvancedModelBox leftfin;

    public LanternfishModel() {
        this.texWidth = 16;
        this.texHeight = 16;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 22.0F, -1.5F);
        this.body.setTextureOffset(2, 9).addBox(-1.0F, -1.0F, -2.5F, 2.0F, 2.0F, 5.0F, 0.0F, false);
        this.body.setTextureOffset(8, 3).addBox(0.0F, 1.0F, -1.25F, 0.0F, 2.0F, 4.0F, 0.0F, false);
        this.body.setTextureOffset(7, -1).addBox(0.0F, -2.5F, -1.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, 0.0F, 2.5F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 3).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 0.0F, false);
        this.rightfin = new AdvancedModelBox(this);
        this.rightfin.setRotationPoint(1.0F, 0.5F, -0.5F);
        this.body.addChild(this.rightfin);
        this.rightfin.setTextureOffset(0, 0).addBox(0.0F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        this.leftfin = new AdvancedModelBox(this);
        this.leftfin.setRotationPoint(-1.0F, 0.5F, -0.5F);
        this.body.addChild(this.leftfin);
        this.leftfin.setTextureOffset(0, 0).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.tail, this.rightfin, this.leftfin);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    public void setupAnim(LanternfishEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float swimSpeed = 0.9F;
        float swimDegree = 0.9F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float landProgress = entity.getLandProgress(partialTick);
        float pitchAmount = entity.getFishPitch(partialTick) / (180.0F / (float) Math.PI);
        this.progressRotationPrev(this.body, landProgress, 0.0F, 0.0F, (float) Math.toRadians(-90.0), 1.0F);
        this.walk(this.body, 0.1F, 0.05F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.bob(this.body, 0.1F, 0.3F, false, ageInTicks, 1.0F);
        this.swing(this.body, swimSpeed, swimDegree * 0.4F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail, swimSpeed, swimDegree * 0.8F, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.rightfin, swimSpeed, swimDegree * 1.1F, true, 0.0F, 0.4F, limbSwing, limbSwingAmount);
        this.swing(this.leftfin, swimSpeed, swimDegree * 1.1F, false, 0.0F, 0.4F, limbSwing, limbSwingAmount);
        this.body.rotateAngleX += pitchAmount;
    }
}