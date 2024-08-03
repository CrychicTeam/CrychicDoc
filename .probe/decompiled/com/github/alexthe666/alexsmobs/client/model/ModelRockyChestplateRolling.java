package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.LivingEntity;

public class ModelRockyChestplateRolling extends AdvancedEntityModel<LivingEntity> {

    private final AdvancedModelBox Body;

    private final AdvancedModelBox LeftArm;

    private final AdvancedModelBox RightArm;

    public ModelRockyChestplateRolling() {
        this.texWidth = 64;
        this.texHeight = 64;
        float f = -6.5F;
        this.Body = new AdvancedModelBox(this, "Body");
        this.Body.setRotationPoint(0.0F, f, 0.0F);
        this.Body.setTextureOffset(0, 0).addBox(-5.0F, -0.5F, -3.0F, 10.0F, 13.0F, 9.0F, 0.0F, false);
        this.Body.setTextureOffset(0, 23).addBox(-4.0F, 0.5F, 6.0F, 8.0F, 11.0F, 4.0F, 0.0F, false);
        this.Body.setTextureOffset(25, 34).addBox(-2.0F, -0.5F, 6.0F, 4.0F, 13.0F, 4.0F, 0.0F, false);
        this.LeftArm = new AdvancedModelBox(this, "LeftArm");
        this.LeftArm.setRotationPoint(6.0F, 2.0F + f, 1.0F);
        this.setRotationAngle(this.LeftArm, 0.0F, 0.0F, 1.5708F);
        this.LeftArm.setTextureOffset(25, 23).addBox(-3.5F, -1.0F, -4.1F, 6.0F, 4.0F, 6.0F, 0.0F, false);
        this.LeftArm.setTextureOffset(0, 39).addBox(-3.0F, -3.0F, -3.1F, 7.0F, 6.0F, 4.0F, 0.0F, false);
        this.RightArm = new AdvancedModelBox(this, "RightArm");
        this.RightArm.setRotationPoint(-6.0F, 1.0F + f, 1.0F);
        this.setRotationAngle(this.RightArm, 0.0F, 0.0F, -1.5708F);
        this.RightArm.setTextureOffset(25, 23).addBox(-3.5F, -1.0F, -4.1F, 6.0F, 4.0F, 6.0F, 0.0F, true);
        this.RightArm.setTextureOffset(0, 39).addBox(-5.5F, -3.0F, -3.1F, 7.0F, 6.0F, 4.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.Body, this.RightArm, this.LeftArm);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.Body, this.RightArm, this.LeftArm);
    }

    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}