package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityTendonSegment;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelTendonClaw extends AdvancedEntityModel<EntityTendonSegment> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox claw1;

    private final AdvancedModelBox claw2;

    private final AdvancedModelBox claw3;

    public ModelTendonClaw() {
        this.texWidth = 16;
        this.texHeight = 16;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.claw1 = new AdvancedModelBox(this, "claw1");
        this.claw1.setRotationPoint(0.0F, 0.0F, 0.7F);
        this.root.addChild(this.claw1);
        this.setRotationAngle(this.claw1, 0.2618F, 0.0F, 0.0F);
        this.claw1.setTextureOffset(0, 0).addBox(-1.5F, -8.0F, -2.0F, 3.0F, 8.0F, 2.0F, 0.0F, false);
        this.claw2 = new AdvancedModelBox(this, "claw2");
        this.claw2.setRotationPoint(0.5F, 0.0F, 1.0F);
        this.root.addChild(this.claw2);
        this.setRotationAngle(this.claw2, 0.2618F, 0.0F, 2.1817F);
        this.claw2.setTextureOffset(0, 0).addBox(-1.5F, -8.0F, -2.0F, 3.0F, 8.0F, 2.0F, 0.0F, false);
        this.claw3 = new AdvancedModelBox(this, "claw3");
        this.claw3.setRotationPoint(-0.5F, 0.0F, 1.0F);
        this.root.addChild(this.claw3);
        this.setRotationAngle(this.claw3, 0.2618F, 0.0F, -2.1817F);
        this.claw3.setTextureOffset(0, 0).addBox(-1.5F, -8.0F, -2.0F, 3.0F, 8.0F, 2.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.claw1, this.claw2, this.claw3);
    }

    public void setupAnim(EntityTendonSegment entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
    }

    public void setAttributes(float rotX, float rotY, float open) {
        this.resetToDefaultPose();
        this.resetToDefaultPose();
        this.root.rotateAngleX = Maths.rad((double) rotX);
        this.root.rotateAngleY = Maths.rad((double) rotY);
        this.progressRotationPrev(this.claw1, open, Maths.rad(45.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.claw2, open, Maths.rad(45.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.claw3, open, Maths.rad(45.0), 0.0F, 0.0F, 1.0F);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}