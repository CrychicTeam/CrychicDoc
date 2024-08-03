package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class BoundroidModel extends AdvancedEntityModel<BoundroidEntity> {

    private final AdvancedModelBox head;

    private final AdvancedModelBox bump1;

    private final AdvancedModelBox bump2;

    public BoundroidModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.head.setTextureOffset(0, 27).addBox(-6.0F, 1.0F, -6.0F, 12.0F, 5.0F, 12.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(-11.0F, 6.0F, -11.0F, 22.0F, 5.0F, 22.0F, 0.0F, false);
        this.bump1 = new AdvancedModelBox(this);
        this.bump1.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.head.addChild(this.bump1);
        this.setRotateAngle(this.bump1, 0.0F, -0.7854F, 0.0F);
        this.bump1.setTextureOffset(0, 0).addBox(-4.0F, -2.5F, 0.0F, 8.0F, 5.0F, 0.0F, 0.0F, false);
        this.bump2 = new AdvancedModelBox(this);
        this.bump2.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.head.addChild(this.bump2);
        this.setRotateAngle(this.bump2, 0.0F, 0.7854F, 0.0F);
        this.bump2.setTextureOffset(0, 0).addBox(-4.0F, -2.5F, 0.0F, 8.0F, 5.0F, 0.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.head);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.head, this.bump2, this.bump1);
    }

    public void showChains() {
        this.bump1.showModel = true;
        this.bump2.showModel = true;
    }

    public void hideChains() {
        this.bump1.showModel = false;
        this.bump2.showModel = false;
    }

    public void setupAnim(BoundroidEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float offGroundAmount = 1.0F - entity.getGroundProgress(ageInTicks - (float) entity.f_19797_);
        float yawAmount = netHeadYaw / (180.0F / (float) Math.PI);
        this.head.rotateAngleY += yawAmount;
        this.walk(this.head, 0.15F, 0.2F, false, -1.0F, 0.0F, ageInTicks, offGroundAmount);
        this.flap(this.head, 0.15F, 0.2F, false, 1.0F, 0.0F, ageInTicks, offGroundAmount);
    }

    public void animateForQuarry(float ageInTicks, float slamAmount) {
        this.resetToDefaultPose();
        float offGroundAmount = 1.0F - slamAmount;
        this.walk(this.head, 0.15F, 0.2F, false, -1.0F, 0.0F, ageInTicks, offGroundAmount);
        this.flap(this.head, 0.15F, 0.2F, false, 1.0F, 0.0F, ageInTicks, offGroundAmount);
    }
}