package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class TeslaBulbModel extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox bulb;

    private final AdvancedModelBox baseRing;

    private final AdvancedModelBox midRing;

    private final AdvancedModelBox topRing;

    private final AdvancedModelBox outer;

    private final AdvancedModelBox center;

    public TeslaBulbModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.bulb = new AdvancedModelBox(this);
        this.bulb.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.baseRing = new AdvancedModelBox(this);
        this.baseRing.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.bulb.addChild(this.baseRing);
        this.baseRing.setTextureOffset(0, 32).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 0.0F, 16.0F, 0.0F, false);
        this.midRing = new AdvancedModelBox(this);
        this.midRing.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.baseRing.addChild(this.midRing);
        this.midRing.setTextureOffset(0, 0).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 0.0F, 16.0F, 0.0F, false);
        this.topRing = new AdvancedModelBox(this);
        this.topRing.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.midRing.addChild(this.topRing);
        this.topRing.setTextureOffset(0, 16).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 0.0F, 16.0F, 0.0F, false);
        this.outer = new AdvancedModelBox(this);
        this.outer.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.topRing.addChild(this.outer);
        this.outer.setTextureOffset(40, 40).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.center = new AdvancedModelBox(this);
        this.center.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.outer.addChild(this.center);
        this.center.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.bulb);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.bulb, this.baseRing, this.center, this.outer, this.midRing, this.topRing);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float explode, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float intensity = 1.0F + explode;
        float zoom = (float) ((Math.sin((double) (ageInTicks * 0.5F * intensity)) + 1.0) * 0.5) * 0.1F + (float) Math.sin((double) explode * Math.PI);
        this.outer.setScale(1.0F + zoom, 1.0F + zoom, 1.0F + zoom);
        this.bulb.rotationPointY = (float) ((double) this.bulb.rotationPointY + -1.0 + Math.sin((double) (ageInTicks * 0.045F * intensity)) * 1.0);
        this.baseRing.rotationPointY = (float) ((double) this.baseRing.rotationPointY - (-1.0 + Math.sin((double) (ageInTicks * 0.045F * intensity)) * 1.0));
        this.baseRing.rotationPointY = (float) ((double) this.baseRing.rotationPointY + -2.5 + Math.sin((double) (ageInTicks * 0.045F * intensity + 1.0F)) * 0.5);
        this.midRing.rotationPointY = (float) ((double) this.midRing.rotationPointY + Math.sin((double) (ageInTicks * 0.045F * intensity + 2.0F)) * 0.25);
        this.topRing.rotationPointY = (float) ((double) this.topRing.rotationPointY - Math.sin((double) (ageInTicks * 0.045F * intensity + 3.0F)) * 0.15F);
        this.bulb.rotationPointY = (float) ((double) this.bulb.rotationPointY + Math.sin((double) (ageInTicks * 0.045F)) * 1.0);
        this.bulb.rotationPointY = (float) ((double) this.bulb.rotationPointY + Math.sin((double) (ageInTicks * 0.045F)) * 1.0);
        this.center.rotateAngleX += ageInTicks * 0.1F * intensity;
        this.center.rotateAngleY += ageInTicks * 0.2F * intensity;
        this.outer.rotateAngleZ += ageInTicks * 0.05F * intensity;
        this.outer.rotateAngleX -= ageInTicks * 0.1F * intensity;
        this.outer.rotateAngleY -= ageInTicks * 0.2F * intensity;
        this.outer.rotateAngleZ -= ageInTicks * 0.05F * intensity;
        this.baseRing.rotateAngleY += ageInTicks * 0.1F * intensity;
        this.midRing.rotateAngleY += ageInTicks * 0.1F * intensity;
        this.topRing.rotateAngleY += ageInTicks * 0.1F * intensity;
    }
}