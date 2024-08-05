package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class SirenLightModel extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox main;

    private final AdvancedModelBox siren;

    public SirenLightModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.main = new AdvancedModelBox(this);
        this.main.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.main.setTextureOffset(0, 12).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.main.setTextureOffset(0, 0).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 2.0F, 10.0F, 0.0F, false);
        this.siren = new AdvancedModelBox(this);
        this.siren.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.main.addChild(this.siren);
        this.siren.setTextureOffset(0, 28).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.main);
    }

    @Override
    public void setupAnim(Entity entity, float rotation, float f, float f1, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.siren.rotateAngleY = (float) ((double) this.siren.rotateAngleY + Math.toRadians((double) rotation));
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.main, this.siren);
    }
}