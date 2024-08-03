package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityStraddleboard;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelStraddleboard extends AdvancedEntityModel<EntityStraddleboard> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox hair_left;

    private final AdvancedModelBox hair_right;

    private final AdvancedModelBox spikes;

    private final AdvancedModelBox front;

    private final AdvancedModelBox spikes_front;

    public ModelStraddleboard() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-6.0F, -2.0F, -10.0F, 12.0F, 2.0F, 26.0F, 0.0F, false);
        this.hair_left = new AdvancedModelBox(this, "hair_left");
        this.hair_left.setPos(6.0F, -2.0F, 0.0F);
        this.body.addChild(this.hair_left);
        this.setRotationAngle(this.hair_left, 0.0F, 0.0F, 0.8727F);
        this.hair_left.setTextureOffset(0, 29).addBox(0.0F, -10.0F, -2.0F, 0.0F, 10.0F, 24.0F, 0.0F, false);
        this.hair_right = new AdvancedModelBox(this, "hair_right");
        this.hair_right.setPos(-6.0F, -2.0F, 0.0F);
        this.body.addChild(this.hair_right);
        this.setRotationAngle(this.hair_right, 0.0F, 0.0F, -0.8727F);
        this.hair_right.setTextureOffset(0, 29).addBox(0.0F, -10.0F, -2.0F, 0.0F, 10.0F, 24.0F, 0.0F, true);
        this.spikes = new AdvancedModelBox(this, "spikes");
        this.spikes.setPos(0.0F, -1.5F, 13.5F);
        this.body.addChild(this.spikes);
        this.spikes.setTextureOffset(25, 29).addBox(-4.0F, -5.5F, -3.5F, 8.0F, 11.0F, 7.0F, 0.0F, false);
        this.front = new AdvancedModelBox(this, "front");
        this.front.setPos(0.0F, 0.0F, -10.0F);
        this.body.addChild(this.front);
        this.setRotationAngle(this.front, -0.1309F, 0.0F, 0.0F);
        this.front.setTextureOffset(48, 40).addBox(-5.0F, -2.0F, -8.0F, 10.0F, 2.0F, 8.0F, 0.0F, false);
        this.spikes_front = new AdvancedModelBox(this, "spikes_front");
        this.spikes_front.setPos(0.0F, -4.0F, -3.5F);
        this.front.addChild(this.spikes_front);
        this.spikes_front.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 4.0F, 5.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.hair_left, this.hair_right, this.spikes, this.spikes_front, this.front);
    }

    public void setupAnim(EntityStraddleboard entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
    }

    public void animateBoard(EntityStraddleboard board, float ageInTicks) {
        this.resetToDefaultPose();
        this.walk(this.hair_right, 0.1F, 0.01F, false, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.hair_left, 0.1F, 0.01F, false, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.hair_right, 0.1F, 0.1F, false, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.hair_left, 0.1F, 0.1F, true, 0.0F, -0.1F, ageInTicks, 1.0F);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}