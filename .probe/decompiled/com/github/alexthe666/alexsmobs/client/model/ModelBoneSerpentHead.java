package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityBoneSerpent;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelBoneSerpentHead extends AdvancedEntityModel<EntityBoneSerpent> {

    private final AdvancedModelBox head;

    private final AdvancedModelBox hornL;

    private final AdvancedModelBox hornR;

    private final AdvancedModelBox middlehorn;

    private final AdvancedModelBox headtop;

    private final AdvancedModelBox jaw;

    public ModelBoneSerpentHead() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, 16.0F, 8.0F);
        this.hornL = new AdvancedModelBox(this, "hornL");
        this.hornL.setPos(4.1F, -7.0F, -1.0F);
        this.head.addChild(this.hornL);
        this.setRotationAngle(this.hornL, 0.6545F, 0.3927F, 0.0F);
        this.hornL.setTextureOffset(61, 42).addBox(-0.1F, -3.0F, -5.0F, 5.0F, 5.0F, 16.0F, 0.0F, false);
        this.hornR = new AdvancedModelBox(this, "hornR");
        this.hornR.setPos(-4.1F, -7.0F, -1.0F);
        this.head.addChild(this.hornR);
        this.setRotationAngle(this.hornR, 0.6545F, -0.3927F, 0.0F);
        this.hornR.setTextureOffset(61, 42).addBox(-4.9F, -3.0F, -5.0F, 5.0F, 5.0F, 16.0F, 0.0F, true);
        this.middlehorn = new AdvancedModelBox(this, "middlehorn");
        this.middlehorn.setPos(-2.9F, -6.0F, -1.0F);
        this.head.addChild(this.middlehorn);
        this.setRotationAngle(this.middlehorn, 0.6545F, 0.0F, 0.0F);
        this.middlehorn.setTextureOffset(67, 67).addBox(-0.1F, -4.0F, -5.0F, 6.0F, 6.0F, 22.0F, 0.0F, false);
        this.headtop = new AdvancedModelBox(this, "headtop");
        this.headtop.setPos(0.0F, -2.5F, 0.0F);
        this.head.addChild(this.headtop);
        this.headtop.setTextureOffset(0, 0).addBox(-9.0F, -4.5F, -30.0F, 18.0F, 11.0F, 30.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this, "jaw");
        this.jaw.setPos(0.0F, 2.5F, -2.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(0, 42).addBox(-8.0F, -1.5F, -26.0F, 16.0F, 7.0F, 28.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityBoneSerpent entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.head.rotateAngleX = headPitch * (float) (Math.PI / 180.0);
        float walkSpeed = 0.35F;
        float walkDegree = 3.0F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.2F;
        double walkOffset = 0.0;
        this.walk(this.jaw, idleSpeed * 0.5F, idleDegree * 0.3F, false, -1.2F, 0.0F, ageInTicks, 1.0F);
        this.head.rotationPointY = this.head.rotationPointY + (float) (Math.sin((double) (limbSwing * walkSpeed) + walkOffset) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
        this.walk(this.jaw, walkSpeed * 1.0F, walkDegree * 0.03F, false, -1.2F, 0.0F, limbSwing, limbSwingAmount);
        this.head.rotationPointY = this.head.rotationPointY + (float) (Math.sin((double) (ageInTicks * idleSpeed) + walkOffset) * 1.0 * (double) idleDegree - (double) (1.0F * idleDegree));
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.head);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.head, this.hornL, this.hornR, this.middlehorn, this.headtop, this.jaw);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}