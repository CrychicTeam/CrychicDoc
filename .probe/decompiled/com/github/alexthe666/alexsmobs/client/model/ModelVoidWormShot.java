package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityVoidWormShot;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class ModelVoidWormShot extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox glass;

    private final AdvancedModelBox cube;

    public ModelVoidWormShot() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.glass = new AdvancedModelBox(this, "glass");
        this.glass.setPos(0.0F, -5.0F, 0.0F);
        this.root.addChild(this.glass);
        this.glass.setTextureOffset(0, 21).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.cube = new AdvancedModelBox(this, "cube");
        this.cube.setPos(0.0F, -5.0F, 0.0F);
        this.root.addChild(this.cube);
        this.cube.setTextureOffset(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.cube, this.glass);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    public void animate(EntityVoidWormShot entityIn, float ageInTicks) {
        this.resetToDefaultPose();
        float innerScale = (float) (1.0 + 0.25 * Math.abs(Math.sin((double) (ageInTicks * 0.6F))));
        float outerScale = (float) (1.0 + 0.5 * Math.abs(Math.cos((double) (ageInTicks * 0.2F))));
        this.glass.setScale(innerScale, innerScale, innerScale);
        this.glass.rotateAngleX += ageInTicks * 0.25F;
        this.cube.rotateAngleX += ageInTicks * 0.5F;
        this.glass.setShouldScaleChildren(false);
        this.cube.setScale(outerScale, outerScale, outerScale);
    }
}