package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class LimestoneSpearModel extends AdvancedEntityModel {

    private final AdvancedModelBox spear;

    private final AdvancedModelBox spear2;

    public LimestoneSpearModel() {
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
        this.spear.setTextureOffset(4, 0).addBox(0.0F, -27.0F, -3.5F, 0.0F, 8.0F, 7.0F, 0.0F, false);
        this.spear2 = new AdvancedModelBox(this);
        this.spear2.setRotationPoint(0.0F, -23.0F, 0.0F);
        this.spear.addChild(this.spear2);
        this.setRotateAngle(this.spear2, 0.0F, -1.5708F, 0.0F);
        this.spear2.setTextureOffset(4, 0).addBox(0.0F, -4.0F, -3.5F, 0.0F, 8.0F, 7.0F, 0.0F, false);
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

    @Override
    public void setupAnim(Entity entity, float limbSwing, float explode, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
    }
}