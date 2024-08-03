package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.item.FloaterEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class FloaterModel extends AdvancedEntityModel<FloaterEntity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox main;

    private final AdvancedModelBox propellor;

    private final AdvancedModelBox cube_r1;

    public FloaterModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.main = new AdvancedModelBox(this);
        this.main.setRotationPoint(0.0F, -9.0F, 0.0F);
        this.root.addChild(this.main);
        this.main.setTextureOffset(0, 16).addBox(-6.0F, -5.0F, -6.0F, 12.0F, 10.0F, 12.0F, 0.0F, false);
        this.propellor = new AdvancedModelBox(this);
        this.propellor.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.main.addChild(this.propellor);
        this.propellor.setTextureOffset(0, 0).addBox(-8.0F, 4.0F, -8.0F, 16.0F, 0.0F, 16.0F, 0.0F, false);
        this.propellor.setTextureOffset(36, 18).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 10.0F, 0.0F, 0.0F, false);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.propellor.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.0F, -1.5708F, 0.0F);
        this.cube_r1.setTextureOffset(36, 18).addBox(-6.0F, -18.0F, 0.0F, 12.0F, 10.0F, 0.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.cube_r1, this.propellor);
    }

    public void setupAnim(FloaterEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.propellor.rotateAngleY += ageInTicks * 0.5F;
    }
}