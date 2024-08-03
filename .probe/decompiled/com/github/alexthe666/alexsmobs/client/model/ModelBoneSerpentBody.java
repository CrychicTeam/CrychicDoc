package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityBoneSerpentPart;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelBoneSerpentBody extends AdvancedEntityModel<EntityBoneSerpentPart> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox middle_section;

    public ModelBoneSerpentBody() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.middle_section = new AdvancedModelBox(this, "middle_section");
        this.middle_section.setPos(0.0F, -7.75F, 0.0F);
        this.root.addChild(this.middle_section);
        this.middle_section.setTextureOffset(2, 50).addBox(-2.0F, -9.25F, -8.0F, 4.0F, 2.0F, 16.0F, 0.0F, false);
        this.middle_section.setTextureOffset(0, 0).addBox(-9.0F, -7.25F, -8.0F, 18.0F, 15.0F, 16.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityBoneSerpentPart entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.middle_section.rotateAngleX = headPitch * (float) (Math.PI / 180.0);
        this.middle_section.rotateAngleY = netHeadYaw * (float) (Math.PI / 180.0);
        float walkSpeed = 0.35F;
        float walkDegree = 3.0F;
        float idleDegree = 0.7F;
        float idleSpeed = 0.2F;
        double walkOffset = (double) (entityIn.getBodyIndex() + 1) * Math.PI * 0.5;
        this.middle_section.rotationPointY = this.middle_section.rotationPointY + (float) (Math.sin((double) (limbSwing * walkSpeed) - walkOffset) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
        this.middle_section.rotationPointY = this.middle_section.rotationPointY + (float) (Math.sin((double) (ageInTicks * idleSpeed) - walkOffset) * 1.0 * (double) idleDegree - (double) (1.0F * idleDegree));
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.middle_section);
    }
}