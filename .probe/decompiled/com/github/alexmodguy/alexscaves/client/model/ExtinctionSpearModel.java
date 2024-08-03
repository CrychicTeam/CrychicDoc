package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.item.ExtinctionSpearEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ExtinctionSpearModel extends AdvancedEntityModel<ExtinctionSpearEntity> {

    private final AdvancedModelBox spear;

    private final AdvancedModelBox spear2;

    public ExtinctionSpearModel() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.spear = new AdvancedModelBox(this);
        this.spear.setRotationPoint(0.0F, 15.0F, 0.0F);
        this.spear.setTextureOffset(0, 0).addBox(-0.5F, -20.0F, -0.5F, 1.0F, 29.0F, 1.0F, 0.0F, false);
        this.spear.setTextureOffset(12, 0).addBox(-1.0F, -18.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.spear.setTextureOffset(4, 0).addBox(-1.0F, -15.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.spear.setTextureOffset(4, 19).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 12.0F, 1.0F, 0.25F, false);
        this.spear.setTextureOffset(8, 10).addBox(0.0F, -18.0F, -5.5F, 0.0F, 2.0F, 5.0F, 0.0F, false);
        this.spear.setTextureOffset(14, 1).addBox(0.0F, -15.0F, -3.5F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        this.spear.setTextureOffset(4, 0).addBox(0.0F, -18.0F, 0.5F, 0.0F, 2.0F, 5.0F, 0.0F, false);
        this.spear.setTextureOffset(8, 14).addBox(0.0F, -15.0F, 0.5F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        this.spear.setTextureOffset(18, 9).addBox(0.0F, -35.0F, -3.5F, 0.0F, 16.0F, 7.0F, 0.0F, false);
        this.spear2 = new AdvancedModelBox(this);
        this.spear2.setRotationPoint(0.0F, -23.0F, 0.0F);
        this.spear.addChild(this.spear2);
        this.setRotateAngle(this.spear2, 0.0F, -1.5708F, 0.0F);
        this.spear2.setTextureOffset(18, 9).addBox(0.0F, -12.0F, -3.5F, 0.0F, 16.0F, 7.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.spear);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.spear, this.spear2);
    }

    public void setupAnim(ExtinctionSpearEntity entity, float limbSwing, float explode, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        if (entity.isWiggling()) {
            this.swing(this.spear, 2.0F, 0.2F, false, -1.5F, 0.0F, ageInTicks, 1.0F);
        }
    }
}