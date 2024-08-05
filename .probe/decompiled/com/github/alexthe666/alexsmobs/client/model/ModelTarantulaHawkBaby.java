package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityTarantulaHawk;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelTarantulaHawkBaby extends AdvancedEntityModel<EntityTarantulaHawk> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox head;

    public ModelTarantulaHawkBaby() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -3.0F, -7.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-4.0F, -3.0F, 0.0F, 8.0F, 6.0F, 15.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, 0.9F, 0.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 22).addBox(-3.5F, -3.0F, -3.0F, 7.0F, 5.0F, 3.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityTarantulaHawk entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 1.0F;
        float walkDegree = 0.75F;
        float stretch = (float) (Math.sin((double) (limbSwing * 0.25F)) * (double) limbSwingAmount) + limbSwingAmount;
        this.body.setScale(1.0F, 1.0F - stretch * 0.05F, 1.0F + stretch * 0.5F);
        this.body.rotationPointZ -= stretch * 4.0F;
        this.walk(this.head, 0.25F, 0.075F, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.head, walkSpeed, walkDegree * 0.1F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}