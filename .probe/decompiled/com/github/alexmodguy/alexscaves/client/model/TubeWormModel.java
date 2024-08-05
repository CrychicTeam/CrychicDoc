package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class TubeWormModel extends AdvancedEntityModel {

    private final AdvancedModelBox worm;

    public TubeWormModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.worm = new AdvancedModelBox(this);
        this.worm.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.worm.setTextureOffset(18, 23).addBox(-3.0F, -18.0F, -3.0F, 6.0F, 3.0F, 6.0F, 0.0F, false);
        this.worm.setTextureOffset(0, 0).addBox(-3.0F, -15.0F, -3.0F, 6.0F, 15.0F, 6.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.worm);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.worm);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
    }

    public void animateParticle(float age, float tuckAmount, float animationOffset, float yRot, float partialTicks) {
        this.resetToDefaultPose();
        float ageInTicks = age + partialTicks;
        float invTuckAmount = 1.0F - tuckAmount;
        float bob = (float) (1.0 + Math.sin((double) (ageInTicks * 0.3F + animationOffset))) * 2.0F;
        this.worm.rotationPointY -= invTuckAmount * 10.0F - invTuckAmount * bob + 5.0F;
        this.worm.rotateAngleY = (float) ((double) this.worm.rotateAngleY + Math.toRadians((double) yRot));
        this.walk(this.worm, 0.35F, 0.15F, false, animationOffset - 1.0F, 0.0F, ageInTicks, invTuckAmount);
        this.flap(this.worm, 0.35F, 0.2F, false, animationOffset + 2.0F, 0.0F, ageInTicks, invTuckAmount);
        this.worm.setScale(1.0F, Math.min(0.5F + invTuckAmount, 1.0F), 1.0F);
    }
}