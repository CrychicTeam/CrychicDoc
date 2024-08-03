package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCombJelly;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;

public class ModelCombJelly extends AdvancedEntityModel<EntityCombJelly> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox inner_body;

    public ModelCombJelly(float f) {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -13.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 15.0F, 10.0F, f, false);
        this.inner_body = new AdvancedModelBox(this, "inner_body");
        this.inner_body.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.body.addChild(this.inner_body);
        this.inner_body.setTextureOffset(40, 6).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 13.0F, 6.0F, f, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.inner_body);
    }

    public void setupAnim(EntityCombJelly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = Minecraft.getInstance().getFrameTime();
        float birdPitch = entity.prevjellyPitch + (entity.getJellyPitch() - entity.prevjellyPitch) * partialTick;
        float landProgress = entity.prevOnLandProgress + (entity.onLandProgress - entity.prevOnLandProgress) * partialTick;
        float girateSpeed = 0.1F * ageInTicks * (1.0F - landProgress * 0.2F);
        float widthScale = 0.95F + (float) Math.sin((double) girateSpeed) * 0.1F;
        float heightScale = 0.95F + (float) Math.cos((double) girateSpeed) * 0.1F;
        float squishedScale = widthScale - 0.1F * landProgress;
        this.body.setScale(squishedScale, heightScale, widthScale);
        this.inner_body.setScale(squishedScale, heightScale, widthScale);
        this.body.rotateAngleX = birdPitch * (float) (Math.PI / 180.0) * (1.0F - landProgress * 0.2F);
        this.body.rotateAngleZ = landProgress * 0.2F * (float) (Math.PI / 2);
        this.body.rotationPointY += landProgress * 1.85F;
        this.body.rotationPointY = this.body.rotationPointY + Math.abs(birdPitch * 0.07F);
        this.body.rotationPointX += landProgress;
        this.bob(this.body, 0.1F, 1.0F, false, ageInTicks, 1.0F - landProgress * 0.2F);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}