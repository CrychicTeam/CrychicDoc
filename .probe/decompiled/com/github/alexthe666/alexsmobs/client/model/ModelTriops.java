package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityTriops;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;

public class ModelTriops extends AdvancedEntityModel<EntityTriops> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox leftAntenna;

    private final AdvancedModelBox rightAntenna;

    private final AdvancedModelBox leftLegs;

    private final AdvancedModelBox rightLegs;

    private final AdvancedModelBox tail1;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox leftTailFlipper;

    private final AdvancedModelBox rightTailFlipper;

    public ModelTriops() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, -2.0F, -2.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-3.5F, -1.1F, -3.5F, 7.0F, 3.0F, 7.0F, 0.0F, false);
        this.leftAntenna = new AdvancedModelBox(this);
        this.leftAntenna.setRotationPoint(3.5F, 2.0F, -2.0F);
        this.body.addChild(this.leftAntenna);
        this.leftAntenna.setTextureOffset(15, 21).addBox(-1.0F, -1.0F, 0.0F, 4.0F, 1.0F, 3.0F, 0.0F, false);
        this.rightAntenna = new AdvancedModelBox(this);
        this.rightAntenna.setRotationPoint(-3.5F, 2.0F, -2.0F);
        this.body.addChild(this.rightAntenna);
        this.rightAntenna.setTextureOffset(15, 21).addBox(-3.0F, -1.0F, 0.0F, 4.0F, 1.0F, 3.0F, 0.0F, true);
        this.leftLegs = new AdvancedModelBox(this);
        this.leftLegs.setRotationPoint(0.0F, 1.9F, 0.0F);
        this.body.addChild(this.leftLegs);
        this.setRotateAngle(this.leftLegs, 0.0F, 0.0F, 0.0873F);
        this.leftLegs.setTextureOffset(22, 0).addBox(0.0F, 0.0F, -2.0F, 3.0F, 0.0F, 4.0F, 0.0F, false);
        this.rightLegs = new AdvancedModelBox(this);
        this.rightLegs.setRotationPoint(0.0F, 1.9F, 0.0F);
        this.body.addChild(this.rightLegs);
        this.setRotateAngle(this.rightLegs, 0.0F, 0.0F, -0.0873F);
        this.rightLegs.setTextureOffset(22, 0).addBox(-3.0F, 0.0F, -2.0F, 3.0F, 0.0F, 4.0F, 0.0F, true);
        this.tail1 = new AdvancedModelBox(this);
        this.tail1.setRotationPoint(0.0F, 1.0F, 2.3F);
        this.body.addChild(this.tail1);
        this.tail1.setTextureOffset(0, 18).addBox(-1.5F, -1.0F, -0.8F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        this.tail1.setTextureOffset(22, 11).addBox(1.5F, 1.0F, -0.8F, 2.0F, 0.0F, 4.0F, 0.0F, false);
        this.tail1.setTextureOffset(22, 11).addBox(-3.5F, 1.0F, -0.8F, 2.0F, 0.0F, 4.0F, 0.0F, true);
        this.tail2 = new AdvancedModelBox(this);
        this.tail2.setRotationPoint(0.0F, 0.2F, 3.2F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(11, 14).addBox(-1.5F, -1.2F, 0.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        this.leftTailFlipper = new AdvancedModelBox(this);
        this.leftTailFlipper.setRotationPoint(0.7F, -1.0F, 4.8F);
        this.tail2.addChild(this.leftTailFlipper);
        this.setRotateAngle(this.leftTailFlipper, 0.2618F, 0.2618F, 0.0F);
        this.leftTailFlipper.setTextureOffset(0, 11).addBox(0.0F, 0.0F, -1.0F, 1.0F, 0.0F, 6.0F, 0.0F, false);
        this.rightTailFlipper = new AdvancedModelBox(this);
        this.rightTailFlipper.setRotationPoint(-0.7F, -1.0F, 4.8F);
        this.tail2.addChild(this.rightTailFlipper);
        this.setRotateAngle(this.rightTailFlipper, 0.2618F, -0.2618F, 0.0F);
        this.rightTailFlipper.setTextureOffset(0, 11).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 6.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.leftAntenna, this.rightAntenna, this.leftLegs, this.rightLegs, this.tail1, this.tail2, this.leftTailFlipper, this.rightTailFlipper);
    }

    public void setupAnim(EntityTriops entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.5F;
        float idleDegree = 0.2F;
        float swimSpeed = 0.65F;
        float swimDegree = 0.25F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float landProgress = entity.prevOnLandProgress + (entity.onLandProgress - entity.prevOnLandProgress) * partialTick;
        float swimAmount = 1.0F - landProgress * 0.2F;
        float swimRot = swimAmount * (entity.prevSwimRot + (entity.swimRot - entity.prevSwimRot) * partialTick);
        float yaw = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTick;
        float tail1Rot = Mth.wrapDegrees(entity.prevTail1Yaw + (entity.tail1Yaw - entity.prevTail1Yaw) * partialTick - yaw) * 0.35F;
        float tail2Rot = Mth.wrapDegrees(entity.prevTail2Yaw + (entity.tail2Yaw - entity.prevTail2Yaw) * partialTick - yaw) * 0.35F;
        this.progressRotationPrev(this.body, landProgress, 0.0F, 0.0F, Maths.rad(-180.0), 5.0F);
        this.progressPositionPrev(this.body, limbSwingAmount, 0.0F, -3.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.leftAntenna, 1.0F - limbSwingAmount, 0.0F, Maths.rad(20.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.rightAntenna, 1.0F - limbSwingAmount, 0.0F, Maths.rad(-20.0), 0.0F, 1.0F);
        this.body.rotateAngleX += headPitch * (float) (Math.PI / 180.0) * swimAmount;
        this.body.rotateAngleZ = this.body.rotateAngleZ + Maths.rad((double) swimRot);
        this.swing(this.rightAntenna, idleSpeed, idleDegree, false, 1.0F, -0.2F, ageInTicks, 1.0F);
        this.swing(this.leftAntenna, idleSpeed, idleDegree, true, 1.0F, -0.2F, ageInTicks, 1.0F);
        this.walk(this.leftTailFlipper, idleSpeed, idleDegree, false, 3.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.rightTailFlipper, idleSpeed, idleDegree, false, 3.0F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.leftLegs, idleSpeed * 3.0F, idleDegree, true, 1.0F, -0.2F, ageInTicks, 1.0F);
        this.flap(this.rightLegs, idleSpeed * 3.0F, idleDegree, false, 1.0F, -0.2F, ageInTicks, 1.0F);
        this.walk(this.body, swimSpeed, swimDegree, false, 2.5F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.tail1, swimSpeed, swimDegree, false, 1.5F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.tail2, swimSpeed, swimDegree * 1.5F, false, 0.5F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftTailFlipper, swimSpeed, swimDegree, false, 0.0F, -0.1F, limbSwing, limbSwingAmount);
        this.walk(this.rightTailFlipper, swimSpeed, swimDegree, false, 0.0F, -0.1F, limbSwing, limbSwingAmount);
        this.walk(this.tail1, idleSpeed, idleDegree, false, 0.0F, -0.1F, ageInTicks, landProgress * 0.2F);
        this.walk(this.tail2, idleSpeed, idleDegree * 1.5F, false, 0.0F, -0.3F, ageInTicks, landProgress * 0.2F);
        this.walk(this.leftTailFlipper, idleSpeed, idleDegree, false, 0.0F, -0.3F, ageInTicks, landProgress * 0.2F);
        this.walk(this.rightTailFlipper, idleSpeed, idleDegree, false, 0.0F, -0.3F, ageInTicks, landProgress * 0.2F);
        this.tail1.rotateAngleY = this.tail1.rotateAngleY + Maths.rad((double) tail1Rot);
        this.tail2.rotateAngleY = this.tail2.rotateAngleY + Maths.rad((double) tail2Rot);
    }
}