package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class GalenaGauntletModel extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox base;

    private final AdvancedModelBox fingers;

    private final AdvancedModelBox thumb;

    private final AdvancedModelBox finger;

    private final AdvancedModelBox finger3;

    private final AdvancedModelBox finger2;

    private final boolean left;

    public GalenaGauntletModel(boolean left) {
        this.texWidth = 128;
        this.texHeight = 128;
        this.left = left;
        this.base = new AdvancedModelBox(this);
        this.base.setRotationPoint(0.0F, 6.75F, -0.5F);
        this.base.setTextureOffset(27, 31).addBox(-4.5F, 0.25F, -4.5F, 9.0F, 11.0F, 9.0F, 0.01F, false);
        this.base.setTextureOffset(0, 0).addBox(-4.5F, 0.25F, -4.5F, 9.0F, 11.0F, 9.0F, 0.26F, false);
        this.fingers = new AdvancedModelBox(this);
        this.fingers.setRotationPoint(0.0F, 9.25F, 0.5F);
        this.base.addChild(this.fingers);
        this.thumb = new AdvancedModelBox(this);
        if (!left) {
            this.thumb.setRotationPoint(4.0F, 0.5F, 2.0F);
            this.setRotateAngle(this.thumb, 0.0F, 3.1416F, 0.0F);
            this.thumb.setTextureOffset(27, 0).addBox(-4.5F, -1.5F, -2.0F, 5.0F, 3.0F, 4.0F, 0.25F, false);
            this.thumb.setTextureOffset(36, 23).addBox(-4.5F, -1.5F, -2.0F, 5.0F, 3.0F, 4.0F, 0.0F, false);
        } else {
            this.thumb.setRotationPoint(-5.0F, 0.5F, 2.0F);
            this.thumb.setTextureOffset(27, 0).addBox(-4.5F, -1.5F, -2.0F, 5.0F, 3.0F, 4.0F, 0.25F, false);
            this.thumb.setTextureOffset(36, 23).addBox(-4.5F, -1.5F, -2.0F, 5.0F, 3.0F, 4.0F, 0.0F, false);
        }
        this.fingers.addChild(this.thumb);
        this.finger = new AdvancedModelBox(this);
        this.finger.setRotationPoint(-4.5F, 0.5F, -5.0F);
        this.fingers.addChild(this.finger);
        this.finger.setTextureOffset(30, 54).addBox(-2.0F, -1.75F, -6.0F, 4.0F, 3.0F, 6.0F, 0.25F, true);
        this.finger.setTextureOffset(56, 54).addBox(-2.0F, -1.75F, -6.0F, 4.0F, 3.0F, 6.0F, 0.0F, false);
        this.finger3 = new AdvancedModelBox(this);
        this.finger3.setRotationPoint(4.5F, 0.25F, -5.0F);
        this.fingers.addChild(this.finger3);
        this.finger3.setTextureOffset(30, 54).addBox(-2.0F, -1.5F, -6.0F, 4.0F, 3.0F, 6.0F, 0.25F, true);
        this.finger3.setTextureOffset(56, 54).addBox(-2.0F, -1.5F, -6.0F, 4.0F, 3.0F, 6.0F, 0.0F, true);
        this.finger2 = new AdvancedModelBox(this);
        this.finger2.setRotationPoint(0.0F, 0.25F, -5.0F);
        this.fingers.addChild(this.finger2);
        this.finger2.setTextureOffset(30, 54).addBox(-2.0F, -1.5F, -6.0F, 4.0F, 3.0F, 6.0F, 0.25F, true);
        this.finger2.setTextureOffset(56, 54).addBox(-2.0F, -1.5F, -6.0F, 4.0F, 3.0F, 6.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public void setupAnim(Entity entity, float openAmount, float switchProgress, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float closeAmount = 1.0F - openAmount;
        float leftOff = this.left ? -1.0F : 1.0F;
        if (this.left) {
            this.progressRotationPrev(this.base, closeAmount, 0.0F, (float) Math.toRadians(-90.0), 0.0F, 1.0F);
            this.progressRotationPrev(this.thumb, closeAmount, 0.0F, 0.0F, (float) Math.toRadians(-90.0), 1.0F);
        } else {
            this.progressRotationPrev(this.base, closeAmount, 0.0F, (float) Math.toRadians(90.0), 0.0F, 1.0F);
            this.progressRotationPrev(this.thumb, closeAmount, 0.0F, 0.0F, (float) Math.toRadians(90.0), 1.0F);
        }
        this.progressRotationPrev(this.finger, closeAmount, (float) Math.toRadians(180.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.finger2, closeAmount, (float) Math.toRadians(180.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.finger3, closeAmount, (float) Math.toRadians(180.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.finger, closeAmount, 1.0F, 2.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.finger2, closeAmount, 0.0F, 3.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.finger3, closeAmount, -1.0F, 2.5F, 0.0F, 1.0F);
        this.progressPositionPrev(this.thumb, closeAmount, 0.0F, -2.5F, 0.0F, 1.0F);
        this.walk(this.finger, 0.1F, 0.3F, true, 3.0F * leftOff, -0.4F, ageInTicks, openAmount);
        this.swing(this.finger, 0.1F, 0.1F, this.left, 5.0F * leftOff, 0.1F * leftOff, ageInTicks, openAmount);
        this.walk(this.finger2, 0.1F, 0.3F, true, 2.0F * leftOff, -0.4F, ageInTicks, openAmount);
        this.walk(this.finger3, 0.1F, 0.3F, true, 1.0F * leftOff, -0.4F, ageInTicks, openAmount);
        this.swing(this.finger3, 0.1F, -0.1F, this.left, 2.0F * leftOff, -0.1F * leftOff, ageInTicks, openAmount);
        this.flap(this.thumb, 0.1F, 0.3F * leftOff, true, -1.0F, -0.2F * leftOff, ageInTicks, openAmount);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.base, this.fingers, this.thumb, this.finger, this.finger2, this.finger3);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.base);
    }
}