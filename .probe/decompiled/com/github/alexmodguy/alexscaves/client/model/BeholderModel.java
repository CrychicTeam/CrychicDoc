package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class BeholderModel extends AdvancedEntityModel {

    private final AdvancedModelBox main;

    private final AdvancedModelBox stand;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox eye;

    public BeholderModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.main = new AdvancedModelBox(this);
        this.main.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.stand = new AdvancedModelBox(this);
        this.stand.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.main.addChild(this.stand);
        this.stand.setTextureOffset(20, 8).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        this.stand.setTextureOffset(0, 0).addBox(0.0F, -16.0F, -8.0F, 0.0F, 14.0F, 16.0F, 0.0F, false);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.stand.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.0F, -1.5708F, 0.0F);
        this.cube_r1.setTextureOffset(0, 0).addBox(0.0F, -6.0F, -8.0F, 0.0F, 14.0F, 16.0F, 0.0F, false);
        this.eye = new AdvancedModelBox(this);
        this.eye.setRotationPoint(0.0F, -9.5F, 0.0F);
        this.stand.addChild(this.eye);
        this.eye.setTextureOffset(1, 1).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.main);
    }

    @Override
    public void setupAnim(Entity entity, float eyeXRot, float eyeYRot, float ageInTicks, float unused0, float unused1) {
        this.resetToDefaultPose();
        this.eye.rotationPointY = (float) ((double) this.eye.rotationPointY + Math.sin((double) (ageInTicks * 0.2F)) * 0.5);
        this.eye.rotateAngleY = (float) Math.toRadians((double) eyeYRot);
        this.eye.rotateAngleX = (float) Math.toRadians((double) eyeXRot);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.main, this.stand, this.cube_r1, this.eye);
    }

    public void hideEye(boolean firstPersonView) {
        this.eye.showModel = !firstPersonView;
    }
}