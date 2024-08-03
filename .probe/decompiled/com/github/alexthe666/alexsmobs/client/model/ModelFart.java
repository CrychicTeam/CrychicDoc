package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityFart;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelFart extends AdvancedEntityModel<EntityFart> {

    private final AdvancedModelBox main;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox cube_r2;

    public ModelFart() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.main = new AdvancedModelBox(this);
        this.main.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.main.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 11.0F, 0.0F, false);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, 0.0F, 0.5F);
        this.main.addChild(this.cube_r1);
        this.setRotationAngle(this.cube_r1, 0.0F, 0.0F, -0.7854F);
        this.cube_r1.setTextureOffset(0, 20).addBox(0.0F, -4.0F, -2.5F, 0.0F, 8.0F, 11.0F, 0.0F, true);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(0.0F, 0.0F, 0.5F);
        this.main.addChild(this.cube_r2);
        this.setRotationAngle(this.cube_r2, 0.0F, 0.0F, 0.7854F);
        this.cube_r2.setTextureOffset(0, 20).addBox(0.0F, -4.0F, -2.5F, 0.0F, 8.0F, 11.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.main, this.cube_r1, this.cube_r2);
    }

    public void setupAnim(EntityFart entity, float limbSwing, float limbSwingAmount, float partialTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float f = Math.min((float) entity.f_19797_ + partialTicks, 30.0F) / 30.0F;
        float expand = 1.5F * f;
        this.main.setScale(expand * 2.0F + 1.0F, expand * 2.0F + 1.0F, 1.0F);
        this.cube_r1.setScale(1.0F, 1.0F, expand * 1.5F + 1.0F);
        this.cube_r2.setScale(1.0F, 1.0F, expand * 1.5F + 1.0F);
        this.cube_r1.rotationPointZ += expand * 3.0F;
        this.cube_r2.rotationPointZ += expand * 3.0F;
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}