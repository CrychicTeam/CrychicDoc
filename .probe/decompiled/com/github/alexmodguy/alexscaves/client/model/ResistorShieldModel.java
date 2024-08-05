package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class ResistorShieldModel extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox base;

    private final AdvancedModelBox rotationBolt;

    public ResistorShieldModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 2.0F);
        this.setRotateAngle(this.root, 0.0F, 1.5708F, 0.0F);
        this.base = new AdvancedModelBox(this);
        this.base.setRotationPoint(4.0F, -20.0F, 0.0F);
        this.root.addChild(this.base);
        this.setRotateAngle(this.base, 0.0F, -0.0436F, 0.0F);
        this.base.setTextureOffset(16, 36).addBox(-9.0F, -3.0F, -1.0F, 6.0F, 6.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(28, 28).addBox(-3.0F, -8.0F, -8.0F, 6.0F, 4.0F, 16.0F, 0.0F, false);
        this.base.setTextureOffset(40, 60).addBox(-3.0F, -4.0F, -8.0F, 6.0F, 8.0F, 4.0F, 0.0F, false);
        this.base.setTextureOffset(56, 22).addBox(-3.0F, -4.0F, 4.0F, 6.0F, 8.0F, 4.0F, 0.0F, false);
        this.base.setTextureOffset(16, 20).addBox(-3.0F, -4.0F, -4.0F, 6.0F, 8.0F, 8.0F, 0.0F, false);
        this.base.setTextureOffset(0, 44).addBox(-3.0F, 4.0F, -8.0F, 6.0F, 4.0F, 16.0F, 0.0F, false);
        this.rotationBolt = new AdvancedModelBox(this);
        this.rotationBolt.setRotationPoint(1.0F, 0.0F, 0.0F);
        this.base.addChild(this.rotationBolt);
        this.rotationBolt.setTextureOffset(54, 66).addBox(-3.0F, 4.0F, -3.0F, 6.0F, 13.0F, 6.0F, 0.0F, false);
        this.rotationBolt.setTextureOffset(22, 66).addBox(-3.0F, -17.0F, -3.0F, 6.0F, 13.0F, 6.0F, 0.0F, false);
        this.rotationBolt.setTextureOffset(1, 0).addBox(-3.0F, -4.0F, -4.0F, 7.0F, 8.0F, 8.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public void setupAnim(Entity entity, float useProgress, float switchProgress, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.rotationBolt.rotateAngleX = (float) Math.toRadians((double) (360.0F * useProgress + switchProgress * 180.0F));
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.rotationBolt, this.base);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }
}