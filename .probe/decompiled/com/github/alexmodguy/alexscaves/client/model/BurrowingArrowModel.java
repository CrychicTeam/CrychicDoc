package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.item.BurrowingArrowEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class BurrowingArrowModel extends AdvancedEntityModel<BurrowingArrowEntity> {

    private final AdvancedModelBox main;

    private final AdvancedModelBox arrow;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox arrow_head;

    private final AdvancedModelBox ljaw;

    private final AdvancedModelBox cube_r2;

    private final AdvancedModelBox tjaw;

    private final AdvancedModelBox cube_r3;

    public BurrowingArrowModel() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.main = new AdvancedModelBox(this);
        this.main.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.arrow = new AdvancedModelBox(this);
        this.arrow.setRotationPoint(0.0F, -2.5F, 2.0F);
        this.main.addChild(this.arrow);
        this.arrow.setTextureOffset(0, 5).addBox(0.0F, -2.5F, -6.0F, 0.0F, 5.0F, 12.0F, 0.0F, false);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.arrow.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.0F, 0.0F, 1.5708F);
        this.cube_r1.setTextureOffset(0, 0).addBox(0.0F, -2.5F, -6.0F, 0.0F, 5.0F, 12.0F, 0.0F, false);
        this.arrow_head = new AdvancedModelBox(this);
        this.arrow_head.setRotationPoint(0.0F, 0.0F, -6.0F);
        this.arrow.addChild(this.arrow_head);
        this.ljaw = new AdvancedModelBox(this);
        this.ljaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.arrow_head.addChild(this.ljaw);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ljaw.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, -0.6981F, 0.0F, 0.0F);
        this.cube_r2.setTextureOffset(0, 7).addBox(-2.0F, 0.0F, 0.5F, 4.0F, 3.0F, 2.0F, 0.0F, false);
        this.cube_r2.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, -3.5F, 4.0F, 3.0F, 4.0F, 0.0F, false);
        this.tjaw = new AdvancedModelBox(this);
        this.tjaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.arrow_head.addChild(this.tjaw);
        this.cube_r3 = new AdvancedModelBox(this);
        this.cube_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.tjaw.addChild(this.cube_r3);
        this.setRotateAngle(this.cube_r3, 0.6981F, 0.0F, 0.0F);
        this.cube_r3.setTextureOffset(12, 3).addBox(-2.0F, -3.0F, -3.5F, 4.0F, 3.0F, 4.0F, 0.01F, false);
        this.cube_r3.setTextureOffset(0, 22).addBox(-2.0F, -3.0F, 0.5F, 4.0F, 3.0F, 2.0F, 0.01F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.main);
    }

    public void setupAnim(BurrowingArrowEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float burrow = entity.getDiggingAmount(ageInTicks - (float) entity.f_19797_);
        this.main.rotationPointZ = this.main.rotationPointZ + Math.abs((float) (Math.cos((double) (ageInTicks * 0.5F)) * 3.0 * (double) burrow));
        this.walk(this.tjaw, 1.0F, 0.6F, false, 1.0F, -0.6F, ageInTicks, burrow);
        this.walk(this.ljaw, 1.0F, 0.6F, true, 1.0F, -0.6F, ageInTicks, burrow);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.main, this.arrow, this.cube_r1, this.cube_r2, this.cube_r3, this.tjaw, this.ljaw, this.arrow_head);
    }
}