package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.world.entity.HumanoidArm;

public class DeepOneModel extends AdvancedEntityModel<DeepOneEntity> implements ArmedModel {

    private final AdvancedModelBox body;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox head;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox headFins;

    private final ModelAnimator animator;

    public DeepOneModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.1667F, 2.1667F, 1.5F);
        this.body.setTextureOffset(0, 12).addBox(0.3333F, -15.1667F, -2.5F, 0.0F, 19.0F, 11.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(-6.6667F, -10.1667F, -6.5F, 13.0F, 14.0F, 9.0F, 0.0F, false);
        this.body.setTextureOffset(45, 15).addBox(-3.6667F, 3.8333F, -3.5F, 7.0F, 10.0F, 5.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(-0.1667F, 12.3333F, 1.5F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(22, 29).addBox(0.0F, -5.5F, 0.0F, 0.0F, 12.0F, 12.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(-0.1667F, -10.1667F, -3.5F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(70, 9).addBox(-3.5F, -6.0F, -8.0F, 7.0F, 5.0F, 6.0F, 0.0F, false);
        this.head.setTextureOffset(22, 23).addBox(-3.5F, -1.0F, -8.0F, 7.0F, 9.0F, 9.0F, 0.0F, false);
        this.head.setTextureOffset(18, 53).addBox(-3.5F, -6.0F, -2.0F, 7.0F, 5.0F, 3.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(0.5F, -3.0F, -4.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(-2.5F, -3.0F, -4.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        this.headFins = new AdvancedModelBox(this);
        this.headFins.setRotationPoint(-3.0F, -1.0F, -2.0F);
        this.head.addChild(this.headFins);
        this.setRotateAngle(this.headFins, -0.7854F, 0.0F, 0.0F);
        this.headFins.setTextureOffset(69, 20).addBox(-5.5F, -4.0F, 0.0F, 5.0F, 10.0F, 0.0F, 0.0F, true);
        this.headFins.setTextureOffset(69, 20).addBox(6.5F, -4.0F, 0.0F, 5.0F, 10.0F, 0.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(0.0F, 3.0F, -0.5F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(44, 0).addBox(-3.5F, -1.0F, -7.5F, 7.0F, 6.0F, 9.0F, 0.25F, false);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(-6.1667F, -4.1667F, -0.75F);
        this.body.addChild(this.rarm);
        this.rarm.setTextureOffset(0, 42).addBox(-3.5F, -2.0F, -3.75F, 4.0F, 19.0F, 5.0F, 0.0F, true);
        this.rarm.setTextureOffset(54, 30).addBox(-3.5F, 17.0F, -3.75F, 4.0F, 4.0F, 5.0F, 0.0F, true);
        this.rarm.setTextureOffset(0, 66).addBox(-3.5F, -2.0F, -3.75F, 4.0F, 19.0F, 5.0F, 0.25F, true);
        this.rarm.setTextureOffset(46, 47).addBox(-1.5F, -2.0F, 1.25F, 0.0F, 19.0F, 5.0F, 0.0F, true);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(5.8333F, -4.1667F, -2.25F);
        this.body.addChild(this.larm);
        this.larm.setTextureOffset(0, 42).addBox(-0.5F, -2.0F, -2.25F, 4.0F, 19.0F, 5.0F, 0.0F, false);
        this.larm.setTextureOffset(0, 66).addBox(-0.5F, -2.0F, -2.25F, 4.0F, 19.0F, 5.0F, 0.25F, false);
        this.larm.setTextureOffset(54, 30).addBox(-0.5F, 17.0F, -2.25F, 4.0F, 4.0F, 5.0F, 0.0F, false);
        this.larm.setTextureOffset(46, 47).addBox(1.5F, -2.0F, 2.75F, 0.0F, 19.0F, 5.0F, 0.0F, false);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-2.1667F, 13.3333F, -1.5F);
        this.body.addChild(this.rleg);
        this.rleg.setTextureOffset(56, 52).addBox(-1.5F, 0.5F, -1.0F, 3.0F, 8.0F, 3.0F, 0.0F, true);
        this.rleg.setTextureOffset(65, 43).addBox(-1.5F, 0.5F, -1.0F, 3.0F, 8.0F, 3.0F, 0.25F, true);
        this.rleg.setTextureOffset(29, 0).addBox(-4.5F, 8.5F, -4.0F, 6.0F, 0.0F, 6.0F, 0.0F, true);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(1.8333F, 13.3333F, -1.5F);
        this.body.addChild(this.lleg);
        this.lleg.setTextureOffset(56, 52).addBox(-1.5F, 0.5F, -1.0F, 3.0F, 8.0F, 3.0F, 0.0F, false);
        this.lleg.setTextureOffset(65, 43).addBox(-1.5F, 0.5F, -1.0F, 3.0F, 8.0F, 3.0F, 0.25F, false);
        this.lleg.setTextureOffset(29, 0).addBox(-1.5F, 8.5F, -4.0F, 6.0F, 0.0F, 6.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(DeepOneEntity.ANIMATION_THROW);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.jaw, (float) Math.toRadians(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(15.0), 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-15.0), (float) Math.toRadians(-15.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-65.0), (float) Math.toRadians(110.0), (float) Math.toRadians(65.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.jaw, (float) Math.toRadians(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-30.0), 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-15.0), (float) Math.toRadians(15.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-75.0), (float) Math.toRadians(10.0), (float) Math.toRadians(65.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(DeepOneEntity.ANIMATION_SCRATCH);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.jaw, (float) Math.toRadians(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-75.0), (float) Math.toRadians(30.0), (float) Math.toRadians(65.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.jaw, (float) Math.toRadians(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-10.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-75.0), (float) Math.toRadians(-30.0), (float) Math.toRadians(25.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.jaw, (float) Math.toRadians(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-10.0), 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-75.0), (float) Math.toRadians(-30.0), (float) Math.toRadians(-65.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.jaw, (float) Math.toRadians(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-75.0), (float) Math.toRadians(30.0), (float) Math.toRadians(-25.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(DeepOneEntity.ANIMATION_BITE);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.rleg, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.lleg, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(30.0), 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(30.0), 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.head, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, -1.0F, -3.0F);
        this.animator.move(this.body, 0.0F, 1.0F, -1.0F);
        this.animator.move(this.rleg, 0.0F, -1.5F, -1.0F);
        this.animator.move(this.lleg, 0.0F, -1.5F, -1.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(65.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(3);
        this.animator.setAnimation(DeepOneEntity.ANIMATION_TRADE);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.head, (float) Math.toRadians(25.0), 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-25.0), (float) Math.toRadians(-15.0), (float) Math.toRadians(-10.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-25.0), (float) Math.toRadians(15.0), (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.rotate(this.head, (float) Math.toRadians(15.0), 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-25.0), (float) Math.toRadians(-15.0), (float) Math.toRadians(-10.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-25.0), (float) Math.toRadians(15.0), (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(20);
        this.animator.rotate(this.head, (float) Math.toRadians(15.0), 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-25.0), (float) Math.toRadians(-15.0), (float) Math.toRadians(-10.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-25.0), (float) Math.toRadians(15.0), (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.larm, (float) Math.toRadians(-100.0), (float) Math.toRadians(25.0), (float) Math.toRadians(-20.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
    }

    public void setupAnim(DeepOneEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float swim = entity.m_20998_(partialTick);
        float clampedYaw = netHeadYaw / (180.0F / (float) Math.PI);
        float fishPitchAmount = entity.getFishPitch(partialTick) / (180.0F / (float) Math.PI);
        float headPitchAmount = headPitch / (180.0F / (float) Math.PI);
        float walkSpeed = 1.0F;
        float walkDegree = 1.0F;
        float swimSpeed = 0.25F;
        float swimDegree = 0.5F;
        float walkAmount = Math.min(limbSwingAmount * 2.0F, 1.0F) * (1.0F - swim);
        float swimAmount = limbSwingAmount * swim;
        this.walk(this.head, 0.1F, 0.02F, false, 0.0F, 0.02F, ageInTicks, 1.0F);
        this.walk(this.tail, 0.1F, 0.05F, false, -1.0F, 0.05F, ageInTicks, 1.0F);
        this.walk(this.larm, 0.1F, 0.05F, true, -1.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.rarm, 0.1F, 0.05F, false, -1.0F, -0.1F, ageInTicks, 1.0F);
        this.progressRotationPrev(this.body, walkAmount, (float) Math.toRadians(15.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, walkAmount, (float) Math.toRadians(-15.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, walkAmount, (float) Math.toRadians(-15.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, walkAmount, (float) Math.toRadians(-15.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.body, swim, (float) Math.toRadians(80.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, swim, (float) Math.toRadians(-70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail, swim, (float) Math.toRadians(-50.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.body, swim, 0.0F, 8.0F, 0.0F, 1.0F);
        this.flap(this.body, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.lleg, walkSpeed, walkDegree * 0.1F, true, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.rleg, walkSpeed, walkDegree * 0.1F, true, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.head, walkSpeed, walkDegree * 0.1F, true, 2.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.larm, walkSpeed, walkDegree * 0.1F, true, 0.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.rarm, walkSpeed, walkDegree * 0.1F, false, 0.0F, 0.0F, limbSwing, walkAmount);
        float bodyWalkBob = -Math.abs(ACMath.walkValue(limbSwing, walkAmount, walkSpeed, -1.5F, 1.0F, false));
        float bodySwimBob = -ACMath.walkValue(limbSwing, swimAmount, swimSpeed, 0.0F, 3.0F, false);
        this.body.rotationPointY += bodyWalkBob + bodySwimBob;
        this.lleg.rotationPointY -= bodyWalkBob;
        this.rleg.rotationPointY -= bodyWalkBob;
        this.lleg.rotationPointY = this.lleg.rotationPointY + (Math.min(0.0F, ACMath.walkValue(limbSwing, walkAmount, walkSpeed, -2.5F, 5.0F, true)) - walkAmount * 1.0F);
        this.rleg.rotationPointY = this.rleg.rotationPointY + (Math.min(0.0F, ACMath.walkValue(limbSwing, walkAmount, walkSpeed, -2.5F, 5.0F, false)) - walkAmount * 1.0F);
        this.walk(this.lleg, walkSpeed, walkDegree, false, -1.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.rleg, walkSpeed, walkDegree, true, -1.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.rarm, walkSpeed, walkDegree * 0.2F, true, -3.0F, 0.4F, limbSwing, walkAmount);
        this.walk(this.larm, walkSpeed, walkDegree * 0.2F, false, -3.0F, -0.4F, limbSwing, walkAmount);
        this.swing(this.tail, walkSpeed, walkDegree * 0.2F, false, -1.0F, -0.0F, limbSwing, walkAmount);
        this.flap(this.body, swimSpeed, swimDegree * 1.0F, true, 0.0F, 0.0F, limbSwing, swimAmount);
        this.swing(this.head, swimSpeed, swimDegree * 1.0F, false, 0.5F, 0.0F, limbSwing, swimAmount);
        this.flap(this.larm, swimSpeed, swimDegree * 2.75F, true, -0.5F, 1.5F, limbSwing, swimAmount);
        this.swing(this.larm, swimSpeed, swimDegree, true, -1.5F, 0.0F, limbSwing, swimAmount);
        this.walk(this.larm, swimSpeed, swimDegree, true, -2.0F, -0.2F, limbSwing, swimAmount);
        this.flap(this.rarm, swimSpeed, swimDegree * 2.75F, false, -3.0F, 1.5F, limbSwing, swimAmount);
        this.swing(this.rarm, swimSpeed, swimDegree, false, -1.5F, 0.0F, limbSwing, swimAmount);
        this.walk(this.rarm, swimSpeed, swimDegree, false, -4.5F, -0.2F, limbSwing, swimAmount);
        this.flap(this.tail, swimSpeed, swimDegree * 0.75F, false, -2.0F, 0.0F, limbSwing, swimAmount);
        this.walk(this.rleg, swimSpeed * 1.5F, swimDegree * 1.0F, true, 2.0F, 0.0F, limbSwing, swimAmount);
        this.walk(this.lleg, swimSpeed * 1.5F, swimDegree * 1.0F, false, 2.0F, 0.0F, limbSwing, swimAmount);
        if (entity.getAnimation() != entity.getTradingAnimation()) {
            this.body.rotateAngleX += fishPitchAmount;
            this.head.rotateAngleX += headPitchAmount;
        }
        this.head.rotateAngleY += clampedYaw;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.tail, this.head, this.jaw, this.rarm, this.larm, this.rleg, this.lleg, this.headFins);
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
        this.body.translateAndRotate(poseStack);
        if (arm == HumanoidArm.RIGHT) {
            this.rarm.translateAndRotate(poseStack);
        } else {
            this.larm.translateAndRotate(poseStack);
        }
        poseStack.translate(0.0F, 0.65F, 0.1F);
    }
}