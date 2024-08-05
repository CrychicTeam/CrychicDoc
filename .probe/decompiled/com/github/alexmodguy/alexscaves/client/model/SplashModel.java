package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class SplashModel extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox base;

    private final AdvancedModelBox highSplash;

    private final AdvancedModelBox ripple;

    public SplashModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.base = new AdvancedModelBox(this);
        this.base.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base.setTextureOffset(0, 28).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 8.0F, 16.0F, 0.0F, false);
        this.highSplash = new AdvancedModelBox(this);
        this.highSplash.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base.addChild(this.highSplash);
        this.highSplash.setTextureOffset(0, 52).addBox(-5.0F, -16.0F, -5.0F, 10.0F, 16.0F, 10.0F, 0.0F, false);
        this.ripple = new AdvancedModelBox(this);
        this.ripple.setRotationPoint(0.0F, -0.25F, 0.0F);
        this.base.addChild(this.ripple);
        this.ripple.setTextureOffset(0, 0).addBox(-14.0F, 0.0F, -14.0F, 28.0F, 0.0F, 28.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.base);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.base, this.ripple, this.highSplash);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float lifetime, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float progress = Math.min(ageInTicks / lifetime, 1.0F);
        this.base.setScale(1.0F + progress * 0.35F, (float) Math.sin((double) progress * Math.PI * 1.0), 1.0F + progress * 0.35F);
        this.highSplash.setScale(1.0F, Math.max((float) Math.sin((double) progress * Math.PI * 1.0 - 0.5) * 1.3F, 0.0F), 1.0F);
        this.ripple.setScale(1.0F + progress * progress, 1.0F, 1.0F + progress * progress);
    }
}