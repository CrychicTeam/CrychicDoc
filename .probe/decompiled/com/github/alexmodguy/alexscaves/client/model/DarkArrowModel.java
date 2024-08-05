package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.item.DarkArrowEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class DarkArrowModel extends AdvancedEntityModel<DarkArrowEntity> {

    private final AdvancedModelBox main;

    private final AdvancedModelBox head;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox stick;

    private final AdvancedModelBox cube_r2;

    public DarkArrowModel() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.main = new AdvancedModelBox(this);
        this.main.setRotationPoint(0.0F, 21.5F, 0.0F);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, 0.0F, -4.5F);
        this.main.addChild(this.head);
        this.head.setTextureOffset(0, -2).addBox(0.0F, -2.5F, -3.5F, 0.0F, 5.0F, 7.0F, 0.0F, false);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, 0.0F, 3.75F);
        this.head.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.0F, 0.0F, 1.5708F);
        this.cube_r1.setTextureOffset(0, -2).addBox(0.0F, -2.5F, -7.25F, 0.0F, 5.0F, 7.0F, 0.0F, false);
        this.stick = new AdvancedModelBox(this);
        this.stick.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.main.addChild(this.stick);
        this.stick.setTextureOffset(2, 2).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 10.0F, 0.0F, false);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(0.0F, 0.0F, 1.25F);
        this.stick.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, 0.0F, 0.0F, 1.5708F);
        this.cube_r2.setTextureOffset(2, 2).addBox(0.0F, -2.5F, -1.25F, 0.0F, 5.0F, 10.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.main);
    }

    public void setupAnim(DarkArrowEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float invFade = 1.0F - entity.getFadeOut(ageInTicks - (float) entity.f_19797_);
        float tickModifier = Math.min(ageInTicks / 10.0F, 1.0F) * 5.0F * invFade;
        this.stick.setScale(1.0F, 1.0F, 1.0F + tickModifier);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.main, this.head, this.cube_r1, this.cube_r2, this.stick);
    }
}