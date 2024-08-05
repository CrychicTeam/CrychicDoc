package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityVoidWorm;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;

public class ModelVoidWorm extends AdvancedEntityModel<EntityVoidWorm> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox topfrills_left;

    private final AdvancedModelBox topfrills_right;

    private final AdvancedModelBox bottomfrills_left;

    private final AdvancedModelBox bottomfrills_right;

    private final AdvancedModelBox head;

    private final AdvancedModelBox eye_bottom_r1;

    private final AdvancedModelBox eye_top_r1;

    private final AdvancedModelBox topjaw;

    private final AdvancedModelBox bottomjaw;

    public ModelVoidWorm(float f) {
        this.texWidth = 256;
        this.texHeight = 256;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setPos(0.0F, -10.0F, 20.0F);
        this.root.addChild(this.neck);
        this.neck.setTextureOffset(0, 53).addBox(-10.0F, -10.0F, -28.0F, 20.0F, 20.0F, 28.0F, f, false);
        this.topfrills_left = new AdvancedModelBox(this, "topfrills_left");
        this.topfrills_left.setPos(10.0F, -10.0F, -20.0F);
        this.neck.addChild(this.topfrills_left);
        this.setRotationAngle(this.topfrills_left, 0.0F, 0.0F, 0.7854F);
        this.topfrills_left.setTextureOffset(71, 76).addBox(0.0F, -9.0F, -7.0F, 0.0F, 9.0F, 26.0F, f, false);
        this.topfrills_right = new AdvancedModelBox(this, "topfrills_right");
        this.topfrills_right.setPos(-10.0F, -10.0F, -20.0F);
        this.neck.addChild(this.topfrills_right);
        this.setRotationAngle(this.topfrills_right, 0.0F, 0.0F, -0.7854F);
        this.topfrills_right.setTextureOffset(71, 76).addBox(0.0F, -9.0F, -7.0F, 0.0F, 9.0F, 26.0F, f, true);
        this.bottomfrills_left = new AdvancedModelBox(this, "bottomfrills_left");
        this.bottomfrills_left.setPos(10.0F, 10.0F, -20.0F);
        this.neck.addChild(this.bottomfrills_left);
        this.setRotationAngle(this.bottomfrills_left, 0.0F, 0.0F, 2.3562F);
        this.bottomfrills_left.setTextureOffset(71, 76).addBox(0.0F, -9.0F, -7.0F, 0.0F, 9.0F, 26.0F, f, false);
        this.bottomfrills_right = new AdvancedModelBox(this, "bottomfrills_right");
        this.bottomfrills_right.setPos(-10.0F, 10.0F, -20.0F);
        this.neck.addChild(this.bottomfrills_right);
        this.setRotationAngle(this.bottomfrills_right, 0.0F, 0.0F, -2.3562F);
        this.bottomfrills_right.setTextureOffset(71, 76).addBox(0.0F, -9.0F, -7.0F, 0.0F, 9.0F, 26.0F, f, true);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, 0.0F, -28.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-17.0F, -17.0F, -18.0F, 34.0F, 34.0F, 18.0F, f, false);
        this.head.setTextureOffset(25, 102).addBox(17.0F, -5.0F, -14.0F, 2.0F, 10.0F, 10.0F, f, false);
        this.head.setTextureOffset(0, 102).addBox(-19.0F, -5.0F, -14.0F, 2.0F, 10.0F, 10.0F, f, false);
        this.eye_bottom_r1 = new AdvancedModelBox(this, "eye_bottom_r1");
        this.eye_bottom_r1.setPos(0.0F, 18.0F, -9.0F);
        this.head.addChild(this.eye_bottom_r1);
        this.setRotationAngle(this.eye_bottom_r1, 0.0F, 0.0F, 1.5708F);
        this.eye_bottom_r1.setTextureOffset(0, 53).addBox(-1.0F, -5.0F, -5.0F, 2.0F, 10.0F, 10.0F, f, false);
        this.eye_top_r1 = new AdvancedModelBox(this, "eye_top_r1");
        this.eye_top_r1.setPos(0.0F, -18.0F, -9.0F);
        this.head.addChild(this.eye_top_r1);
        this.setRotationAngle(this.eye_top_r1, 0.0F, 0.0F, 1.5708F);
        this.eye_top_r1.setTextureOffset(69, 54).addBox(-1.0F, -5.0F, -5.0F, 2.0F, 10.0F, 10.0F, f, false);
        this.topjaw = new AdvancedModelBox(this, "topjaw");
        this.topjaw.setPos(0.0F, 3.0F, -18.0F);
        this.head.addChild(this.topjaw);
        this.topjaw.setTextureOffset(98, 64).addBox(-5.0F, -10.0F, -16.0F, 10.0F, 10.0F, 16.0F, f, false);
        this.bottomjaw = new AdvancedModelBox(this, "bottomjaw");
        this.bottomjaw.setPos(0.0F, -3.0F, -17.9F);
        this.head.addChild(this.bottomjaw);
        this.bottomjaw.setTextureOffset(89, 37).addBox(-5.0F, 0.0F, -16.0F, 10.0F, 10.0F, 16.0F, f - 0.1F, false);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityVoidWorm entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.root.rotateAngleX += headPitch * (float) (Math.PI / 180.0);
        this.root.rotationPointY = this.root.rotationPointY + (-3.0F - Mth.clamp(headPitch * -0.125F, -10.0F, 10.0F) * 0.3F);
        this.root.rotationPointZ += -15.0F + limbSwingAmount * 10.0F;
        float yawAmount = (entityIn.prevWormAngle + (entityIn.getWormAngle() - entityIn.prevWormAngle) * (ageInTicks - (float) entityIn.f_19797_)) / (180.0F / (float) Math.PI) * 0.5F;
        this.neck.rotateAngleZ += yawAmount;
        float jawProgress = entityIn.prevJawProgress + (entityIn.jawProgress - entityIn.prevJawProgress) * (ageInTicks - (float) entityIn.f_19797_);
        this.progressRotationPrev(this.bottomjaw, jawProgress, Maths.rad(60.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.topjaw, jawProgress, Maths.rad(-60.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.bottomjaw, jawProgress, 0.0F, 2.0F, -5.0F, 5.0F);
        this.progressPositionPrev(this.topjaw, jawProgress, 0.0F, -2.0F, -5.0F, 5.0F);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.neck, this.head, this.bottomfrills_left, this.bottomfrills_right, this.eye_bottom_r1, this.eye_top_r1, this.topfrills_left, this.topfrills_right, this.topjaw, this.bottomjaw);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}