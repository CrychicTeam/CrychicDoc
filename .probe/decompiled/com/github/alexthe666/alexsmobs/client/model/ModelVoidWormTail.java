package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityVoidWormPart;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelVoidWormTail extends AdvancedEntityModel<EntityVoidWormPart> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox frillstop_left;

    private final AdvancedModelBox frillstop_right;

    private final AdvancedModelBox frillsbottom_left;

    private final AdvancedModelBox frillsbottom_right;

    public ModelVoidWormTail(float f) {
        this.texWidth = 256;
        this.texHeight = 256;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -19.0F, -16.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-8.0F, -19.0F, 0.0F, 16.0F, 38.0F, 35.0F, f, false);
        this.frillstop_left = new AdvancedModelBox(this, "frillstop_left");
        this.frillstop_left.setPos(8.0F, -19.0F, 16.0F);
        this.body.addChild(this.frillstop_left);
        this.setRotationAngle(this.frillstop_left, 0.0F, 0.0F, 0.7854F);
        this.frillstop_left.setTextureOffset(65, 36).addBox(0.0F, -14.0F, -16.0F, 0.0F, 14.0F, 38.0F, f, false);
        this.frillstop_right = new AdvancedModelBox(this, "frillstop_right");
        this.frillstop_right.setPos(-8.0F, -19.0F, 16.0F);
        this.body.addChild(this.frillstop_right);
        this.setRotationAngle(this.frillstop_right, 0.0F, 0.0F, -0.7854F);
        this.frillstop_right.setTextureOffset(65, 36).addBox(0.0F, -14.0F, -16.0F, 0.0F, 14.0F, 38.0F, f, true);
        this.frillsbottom_left = new AdvancedModelBox(this, "frillsbottom_left");
        this.frillsbottom_left.setPos(8.0F, 19.0F, 16.0F);
        this.body.addChild(this.frillsbottom_left);
        this.setRotationAngle(this.frillsbottom_left, 0.0F, 0.0F, 2.5307F);
        this.frillsbottom_left.setTextureOffset(65, 36).addBox(0.0F, -14.0F, -16.0F, 0.0F, 14.0F, 38.0F, f, false);
        this.frillsbottom_right = new AdvancedModelBox(this, "frillsbottom_right");
        this.frillsbottom_right.setPos(-8.0F, 19.0F, 16.0F);
        this.body.addChild(this.frillsbottom_right);
        this.setRotationAngle(this.frillsbottom_right, 0.0F, 0.0F, -2.5307F);
        this.frillsbottom_right.setTextureOffset(65, 36).addBox(0.0F, -14.0F, -16.0F, 0.0F, 14.0F, 38.0F, f, true);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityVoidWormPart entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float yawAmount = (entityIn.prevWormAngle + (entityIn.getWormAngle() - entityIn.prevWormAngle) * (ageInTicks - (float) entityIn.f_19797_)) / (180.0F / (float) Math.PI) * 0.5F;
        this.body.rotateAngleZ += yawAmount;
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.frillsbottom_left, this.frillsbottom_right, this.frillstop_left, this.frillstop_right);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}