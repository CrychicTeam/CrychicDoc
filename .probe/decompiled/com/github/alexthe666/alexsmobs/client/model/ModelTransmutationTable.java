package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.tileentity.TileEntityTransmutationTable;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class ModelTransmutationTable extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox base;

    private final AdvancedModelBox star;

    private final AdvancedModelBox portal;

    private final AdvancedModelBox leftArm;

    private final AdvancedModelBox leftElbow;

    private final AdvancedModelBox leftHand;

    private final AdvancedModelBox rightArm;

    private final AdvancedModelBox rightElbow;

    private final AdvancedModelBox rightHand;

    public ModelTransmutationTable(float scale) {
        this.texWidth = 64;
        this.texHeight = 64;
        this.base = new AdvancedModelBox(this, "base");
        this.base.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.base.setTextureOffset(0, 0).addBox(-7.0F, -5.0F, -7.0F, 14.0F, 5.0F, 14.0F, scale, false);
        this.star = new AdvancedModelBox(this, "star");
        this.star.setRotationPoint(0.0F, -12.5F, 0.0F);
        this.base.addChild(this.star);
        this.star.setTextureOffset(0, 20).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, scale, false);
        this.portal = new AdvancedModelBox(this, "portal");
        this.portal.setRotationPoint(0.0F, -12.5F, 0.0F);
        this.base.addChild(this.portal);
        this.portal.setTextureOffset(21, 20).addBox(-4.1F, -4.5F, 0.0F, 9.0F, 9.0F, 0.0F, scale, false);
        this.leftArm = new AdvancedModelBox(this, "leftArm");
        this.leftArm.setRotationPoint(6.6F, -3.3F, 0.0F);
        this.base.addChild(this.leftArm);
        this.setRotationAngle(this.leftArm, 0.0F, 0.0F, 0.3054F);
        this.leftArm.setTextureOffset(13, 39).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, scale, false);
        this.leftElbow = new AdvancedModelBox(this, "leftElbow");
        this.leftElbow.setRotationPoint(-1.0F, -6.0F, 0.0F);
        this.leftArm.addChild(this.leftElbow);
        this.setRotationAngle(this.leftElbow, 0.0F, 0.0F, 0.4363F);
        this.leftElbow.setTextureOffset(0, 36).addBox(-3.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F, scale - 0.1F, false);
        this.leftHand = new AdvancedModelBox(this, "leftHand");
        this.leftHand.setRotationPoint(-3.8F, -0.8F, 0.0F);
        this.leftElbow.addChild(this.leftHand);
        this.setRotationAngle(this.leftHand, 0.0F, 0.0F, -0.7418F);
        this.leftHand.setTextureOffset(31, 39).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, scale, false);
        this.leftHand.setTextureOffset(32, 30).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 6.0F, scale, false);
        this.rightArm = new AdvancedModelBox(this, "rightArm");
        this.rightArm.setRotationPoint(-6.6F, -3.3F, 0.0F);
        this.base.addChild(this.rightArm);
        this.setRotationAngle(this.rightArm, 0.0F, 0.0F, -0.3054F);
        this.rightArm.setTextureOffset(13, 39).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, scale, true);
        this.rightElbow = new AdvancedModelBox(this, "rightElbow");
        this.rightElbow.setRotationPoint(1.0F, -6.0F, 0.0F);
        this.rightArm.addChild(this.rightElbow);
        this.setRotationAngle(this.rightElbow, 0.0F, 0.0F, -0.4363F);
        this.rightElbow.setTextureOffset(0, 36).addBox(-2.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F, scale - 0.1F, true);
        this.rightHand = new AdvancedModelBox(this, "rightHand");
        this.rightHand.setRotationPoint(3.8F, -0.8F, 0.0F);
        this.rightElbow.addChild(this.rightHand);
        this.setRotationAngle(this.rightHand, 0.0F, 0.0F, 0.7418F);
        this.rightHand.setTextureOffset(31, 39).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, scale, true);
        this.rightHand.setTextureOffset(32, 30).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 6.0F, scale, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.base);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.base, this.star, this.portal, this.leftArm, this.rightArm, this.leftElbow, this.rightElbow, this.leftHand, this.rightHand);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
    }

    public void animate(TileEntityTransmutationTable beak, float partialTick) {
        this.resetToDefaultPose();
        float ageInTicks = (float) beak.ticksExisted + partialTick;
        this.flap(this.leftArm, 0.5F, 0.2F, false, 0.0F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.rightArm, 0.5F, -0.2F, false, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.leftElbow, 0.5F, 0.1F, true, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.rightElbow, 0.5F, -0.1F, true, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.leftHand, 0.5F, 0.1F, true, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.rightHand, 0.5F, -0.1F, true, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.portal, 0.5F, -0.1F, true, -3.0F, 0.0F, ageInTicks, 1.0F);
        this.bob(this.star, 0.25F, 1.0F, false, ageInTicks, 1.0F);
        this.bob(this.portal, 0.25F, 1.0F, false, ageInTicks, 1.0F);
        this.portal.setScale(0.35F * (float) Math.sin((double) (ageInTicks * 0.5F + 1.0F)) + 1.35F, 0.1F * (float) Math.cos((double) (ageInTicks * 0.5F + 1.0F)) + 1.1F, 1.0F);
        this.star.rotateAngleY = ageInTicks * 0.1F;
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}