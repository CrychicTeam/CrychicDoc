package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityGust;
import com.github.alexthe666.alexsmobs.entity.EntityGuster;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelGuster extends AdvancedEntityModel<EntityGuster> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox tornado;

    private final AdvancedModelBox tornado2;

    private final AdvancedModelBox tornadomid;

    private final AdvancedModelBox tornado3;

    private final AdvancedModelBox tornado4;

    private final AdvancedModelBox eyes;

    private final AdvancedModelBox eye_left;

    private final AdvancedModelBox eye_right;

    public ModelGuster() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.tornado = new AdvancedModelBox(this, "tornado");
        this.tornado.setPos(0.0F, -4.0F, 0.0F);
        this.root.addChild(this.tornado);
        this.tornado.setTextureOffset(65, 72).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.tornado2 = new AdvancedModelBox(this, "tornado2");
        this.tornado2.setPos(0.0F, -8.0F, 0.0F);
        this.tornado.addChild(this.tornado2);
        this.tornado2.setTextureOffset(0, 72).addBox(-8.0F, -4.0F, -8.0F, 16.0F, 8.0F, 16.0F, 0.0F, false);
        this.tornadomid = new AdvancedModelBox(this, "tornadomid");
        this.tornadomid.setPos(0.0F, 6.0F, 0.0F);
        this.tornado2.addChild(this.tornadomid);
        this.tornadomid.setTextureOffset(16, 96).addBox(-14.0F, -4.0F, -14.0F, 28.0F, 4.0F, 28.0F, 0.0F, false);
        this.tornado3 = new AdvancedModelBox(this, "tornado3");
        this.tornado3.setPos(0.0F, -8.0F, 0.0F);
        this.tornado2.addChild(this.tornado3);
        this.tornado3.setTextureOffset(0, 39).addBox(-12.0F, -4.0F, -12.0F, 24.0F, 8.0F, 24.0F, 0.0F, false);
        this.tornado4 = new AdvancedModelBox(this, "tornado4");
        this.tornado4.setPos(0.0F, -8.0F, 0.0F);
        this.tornado3.addChild(this.tornado4);
        this.tornado4.setTextureOffset(0, 0).addBox(-15.0F, -4.0F, -15.0F, 30.0F, 8.0F, 30.0F, 0.0F, false);
        this.eyes = new AdvancedModelBox(this, "eyes");
        this.eyes.setPos(0.0F, -18.0F, -15.0F);
        this.root.addChild(this.eyes);
        this.eye_left = new AdvancedModelBox(this, "eye_left");
        this.eye_left.setPos(4.0F, 0.0F, 0.0F);
        this.eyes.addChild(this.eye_left);
        this.eye_left.setTextureOffset(8, 13).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 8.0F, 0.0F, 0.0F, false);
        this.eye_right = new AdvancedModelBox(this, "eye_right");
        this.eye_right.setPos(-4.0F, 0.0F, 0.0F);
        this.eyes.addChild(this.eye_right);
        this.eye_right.setTextureOffset(8, 13).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 8.0F, 0.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.eyes, this.eye_left, this.eye_right, this.tornado, this.tornado2, this.tornado3, this.tornado4, this.tornadomid);
    }

    public void setupAnim(EntityGuster entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        AdvancedModelBox[] tornadoBoxes = new AdvancedModelBox[] { this.tornado, this.tornado2, this.tornado3, this.tornado4 };
        float idleSpeed = 0.1F;
        float idleDegree = 1.0F;
        float walkSpeed = 0.5F;
        float walkDegree = 1.0F;
        this.bob(this.root, walkSpeed, walkDegree * 3.0F, false, limbSwing, limbSwingAmount);
        this.chainFlap(tornadoBoxes, walkSpeed, walkDegree * 0.1F, -2.0, limbSwing, limbSwingAmount);
        this.bob(this.root, idleSpeed, idleDegree * 3.0F, false, ageInTicks, 1.0F);
        if (entity.isGooglyEyes()) {
            this.eye_left.rotationPointY = this.eye_left.rotationPointY + (float) (Math.sin((double) ageInTicks * 0.7 - 2.0) * 1.9);
            this.eye_right.rotationPointY = this.eye_right.rotationPointY + (float) (Math.sin((double) ageInTicks * 0.7 + 2.0) * 1.9);
        } else {
            this.eye_left.rotationPointY = this.eye_left.rotationPointY + (float) (Math.sin((double) ageInTicks * 0.1 - 2.0) * 0.9);
            this.eye_right.rotationPointY = this.eye_right.rotationPointY + (float) (Math.sin((double) ageInTicks * 0.1 + 2.0) * 0.9);
        }
        this.bob(this.eyes, idleSpeed, idleDegree * -3.2F, false, ageInTicks, 1.0F);
        this.eyes.rotateAngleY += netHeadYaw * 0.5F * (float) (Math.PI / 180.0);
        this.eyes.rotateAngleX += headPitch * 0.8F * (float) (Math.PI / 180.0);
        this.tornado.rotationPointX = (float) ((double) this.tornado.rotationPointX + Math.cos((double) (ageInTicks * 0.7F)) * 4.0);
        this.tornado.rotationPointZ = (float) ((double) this.tornado.rotationPointZ + Math.sin((double) (ageInTicks * 0.7F)) * 4.0);
        this.tornado.rotationPointX = (float) ((double) this.tornado.rotationPointX + (Math.cos((double) (ageInTicks * 0.3F)) * 2.0 - (double) this.tornado.rotationPointX));
        this.tornado.rotationPointZ = (float) ((double) this.tornado.rotationPointZ + (Math.sin((double) (ageInTicks * 0.3F)) * 2.0 - (double) this.tornado.rotationPointZ));
        this.tornadomid.rotateAngleZ = (float) ((double) this.tornadomid.rotateAngleZ + Math.sin((double) (ageInTicks * 0.2F)) * 0.1);
        this.tornado.rotateAngleY -= ageInTicks * 1.0F;
        this.tornado2.rotateAngleY = this.tornado2.rotateAngleY - (this.tornado.rotateAngleY + ageInTicks * 0.3F);
        this.tornado3.rotateAngleY = this.tornado3.rotateAngleY - (this.tornado.rotateAngleY + this.tornado2.rotateAngleY + ageInTicks * 0.2F);
        this.tornado4.rotateAngleY = this.tornado4.rotateAngleY - (this.tornado.rotateAngleY + this.tornado2.rotateAngleY + this.tornado3.rotateAngleY + ageInTicks * 0.34F);
        this.tornadomid.rotateAngleY += ageInTicks * 0.5F;
        this.eyes.rotationPointZ = (float) ((double) this.eyes.rotationPointZ - (2.0 + Math.cos((double) this.tornado3.rotateAngleY)));
    }

    public void animateGust(EntityGust entity, float limbSwing, float limbSwingAmount, float ageInTicks) {
        this.resetToDefaultPose();
        AdvancedModelBox[] tornadoBoxes = new AdvancedModelBox[] { this.tornado, this.tornado2, this.tornado3, this.tornado4 };
        float idleSpeed = 0.1F;
        float idleDegree = 1.0F;
        float walkSpeed = 0.5F;
        float walkDegree = 1.0F;
        this.bob(this.root, walkSpeed, walkDegree * 3.0F, false, limbSwing, limbSwingAmount);
        this.chainFlap(tornadoBoxes, walkSpeed, walkDegree * 0.1F, -2.0, limbSwing, limbSwingAmount);
        this.bob(this.root, idleSpeed, idleDegree * 3.0F, false, ageInTicks, 1.0F);
        this.bob(this.eyes, idleSpeed, idleDegree * -3.2F, false, ageInTicks, 1.0F);
        this.tornado.rotationPointX = (float) ((double) this.tornado.rotationPointX + Math.cos((double) (ageInTicks * 0.7F)) * 4.0);
        this.tornado.rotationPointZ = (float) ((double) this.tornado.rotationPointZ + Math.sin((double) (ageInTicks * 0.7F)) * 4.0);
        this.tornado.rotationPointX = (float) ((double) this.tornado.rotationPointX + (Math.cos((double) (ageInTicks * 0.3F)) * 2.0 - (double) this.tornado.rotationPointX));
        this.tornado.rotationPointZ = (float) ((double) this.tornado.rotationPointZ + (Math.sin((double) (ageInTicks * 0.3F)) * 2.0 - (double) this.tornado.rotationPointZ));
        this.tornadomid.rotateAngleZ = (float) ((double) this.tornadomid.rotateAngleZ + Math.sin((double) (ageInTicks * 0.2F)) * 0.1);
        this.tornado.rotateAngleY -= ageInTicks * 1.0F;
        this.tornado2.rotateAngleY = this.tornado2.rotateAngleY - (this.tornado.rotateAngleY + ageInTicks * 0.3F);
        this.tornado3.rotateAngleY = this.tornado3.rotateAngleY - (this.tornado.rotateAngleY + this.tornado2.rotateAngleY + ageInTicks * 0.2F);
        this.tornado4.rotateAngleY = this.tornado4.rotateAngleY - (this.tornado.rotateAngleY + this.tornado2.rotateAngleY + this.tornado3.rotateAngleY + ageInTicks * 0.34F);
        this.tornadomid.rotateAngleY += ageInTicks * 0.5F;
        this.eyes.rotationPointZ = (float) ((double) this.eyes.rotationPointZ - (2.0 + Math.cos((double) this.tornado3.rotateAngleY)));
    }

    public void hideEyes() {
        this.eyes.showModel = false;
        this.eye_left.showModel = false;
        this.eye_right.showModel = false;
    }

    public void showEyes() {
        this.eyes.showModel = true;
        this.eye_left.showModel = true;
        this.eye_right.showModel = true;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}