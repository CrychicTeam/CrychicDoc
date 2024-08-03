package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityMimicube;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class ModelMimicube extends AdvancedEntityModel<EntityMimicube> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox innerbody;

    public final AdvancedModelBox mouth;

    public final AdvancedModelBox eye_left;

    public final AdvancedModelBox eye_right;

    public ModelMimicube() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-8.0F, -14.0F, -8.0F, 16.0F, 14.0F, 16.0F, 0.0F, false);
        this.innerbody = new AdvancedModelBox(this, "innerbody");
        this.innerbody.setPos(0.0F, -7.0F, 0.0F);
        this.root.addChild(this.innerbody);
        this.innerbody.setTextureOffset(0, 31).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.mouth = new AdvancedModelBox(this, "mouth");
        this.mouth.setPos(2.0F, 4.0F, -5.0F);
        this.innerbody.addChild(this.mouth);
        this.mouth.setTextureOffset(0, 12).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        this.eye_left = new AdvancedModelBox(this, "eye_left");
        this.eye_left.setPos(3.5F, -1.5F, -4.0F);
        this.innerbody.addChild(this.eye_left);
        this.setRotationAngle(this.eye_left, 0.0F, 0.0F, 0.3054F);
        this.eye_left.setTextureOffset(0, 6).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);
        this.eye_right = new AdvancedModelBox(this, "eye_right");
        this.eye_right.setPos(-3.5F, -0.5F, -4.0F);
        this.innerbody.addChild(this.eye_right);
        this.setRotationAngle(this.eye_right, 0.0F, 0.0F, -0.3927F);
        this.eye_right.setTextureOffset(0, 0).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.innerbody, this.eye_left, this.eye_right, this.mouth);
    }

    public void setupAnim(EntityMimicube entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.1F;
        float idleDegree = 1.0F;
        this.bob(this.innerbody, idleDegree, idleSpeed, false, limbSwing, limbSwingAmount);
        this.flap(this.innerbody, idleSpeed * 1.3F, idleDegree * 0.05F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        float lvt_6_1_ = Mth.lerp(Minecraft.getInstance().getFrameTime(), entity.prevSquishFactor, entity.squishFactor);
        float lvt_7_1_ = 1.0F / (lvt_6_1_ + 1.0F);
        float squishScale = 1.0F / lvt_7_1_;
        this.innerbody.rotationPointY += lvt_6_1_ * -5.0F;
        this.body.setScale(1.0F, squishScale, 1.0F);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}