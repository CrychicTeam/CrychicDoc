package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class BrainiacModel extends AdvancedEntityModel<BrainiacEntity> {

    private final AdvancedModelBox torso;

    private final AdvancedModelBox chest;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox handMaw;

    private final AdvancedModelBox handMaw3;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox head;

    private final AdvancedModelBox brain;

    private final AdvancedModelBox tongue;

    private final AdvancedModelBox tongue2;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox rleg;

    private final ModelAnimator animator;

    public BrainiacModel() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.torso = new AdvancedModelBox(this);
        this.torso.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.torso.setTextureOffset(0, 68).addBox(-5.5F, -4.0F, -4.0F, 11.0F, 14.0F, 8.0F, 0.0F, false);
        this.torso.setTextureOffset(0, 107).addBox(-5.5F, -3.0F, -4.0F, 11.0F, 13.0F, 8.0F, 0.25F, false);
        this.chest = new AdvancedModelBox(this);
        this.chest.setRotationPoint(0.0F, 0.0F, 4.0F);
        this.torso.addChild(this.chest);
        this.chest.setTextureOffset(0, 42).addBox(-8.5F, -14.0F, -11.0F, 17.0F, 14.0F, 12.0F, 0.0F, false);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(8.1667F, -8.1667F, -5.0833F);
        this.chest.addChild(this.larm);
        this.larm.setTextureOffset(0, 0).addBox(0.3333F, -1.8333F, -4.1667F, 8.0F, 23.0F, 9.0F, 0.0F, false);
        this.larm.setTextureOffset(70, 79).addBox(1.3333F, -2.8333F, -6.1667F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.larm.setTextureOffset(35, 98).addBox(1.3333F, -2.8333F, -6.1667F, 8.0F, 8.0F, 8.0F, -0.25F, false);
        this.larm.setTextureOffset(35, 98).addBox(1.3333F, 9.1667F, -2.1667F, 8.0F, 8.0F, 8.0F, -0.25F, false);
        this.larm.setTextureOffset(38, 79).addBox(1.3333F, 9.1667F, -2.1667F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.handMaw = new AdvancedModelBox(this);
        this.handMaw.setRotationPoint(8.3333F, 21.1667F, 0.3333F);
        this.larm.addChild(this.handMaw);
        this.handMaw.setTextureOffset(0, 90).addBox(-4.0F, 0.0F, -4.5F, 4.0F, 8.0F, 9.0F, 0.0F, false);
        this.handMaw3 = new AdvancedModelBox(this);
        this.handMaw3.setRotationPoint(-8.0F, 0.0F, 0.0F);
        this.handMaw.addChild(this.handMaw3);
        this.handMaw3.setTextureOffset(92, 0).addBox(0.0F, 0.0F, -4.5F, 4.0F, 8.0F, 9.0F, 0.0F, false);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(-7.25F, -12.25F, -6.0F);
        this.chest.addChild(this.rarm);
        this.rarm.setTextureOffset(56, 0).addBox(-8.25F, -3.75F, -3.25F, 9.0F, 37.0F, 9.0F, 0.0F, false);
        this.rarm.setTextureOffset(84, 38).addBox(-10.25F, 15.25F, -5.25F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.rarm.setTextureOffset(35, 98).addBox(-10.25F, 15.25F, -5.25F, 8.0F, 8.0F, 8.0F, -0.25F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -7.0F, -11.25F);
        this.chest.addChild(this.head);
        this.head.setTextureOffset(100, 54).addBox(-3.5F, -3.0F, -6.25F, 7.0F, 10.0F, 7.0F, 0.0F, false);
        this.brain = new AdvancedModelBox(this);
        this.brain.setRotationPoint(0.0F, 28.0F, 7.25F);
        this.head.addChild(this.brain);
        this.brain.setTextureOffset(46, 56).addBox(-6.5F, -41.0F, -15.0F, 13.0F, 11.0F, 12.0F, 0.0F, false);
        this.tongue = new AdvancedModelBox(this);
        this.tongue.setRotationPoint(0.0F, 3.5F, -1.75F);
        this.head.addChild(this.tongue);
        this.tongue.setTextureOffset(73, 106).addBox(-2.5F, -0.5F, -21.0F, 5.0F, 1.0F, 21.0F, 0.0F, false);
        this.tongue2 = new AdvancedModelBox(this);
        this.tongue2.setRotationPoint(0.0F, 0.0F, -21.0F);
        this.tongue.addChild(this.tongue2);
        this.tongue2.setTextureOffset(84, 84).addBox(-2.5F, -0.5F, -21.0F, 5.0F, 1.0F, 21.0F, 0.0F, false);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(3.5F, 9.5F, 0.0F);
        this.torso.addChild(this.lleg);
        this.lleg.setTextureOffset(26, 91).addBox(-2.0F, 0.5F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F, true);
        this.lleg.setTextureOffset(67, 95).addBox(-2.0F, 0.5F, -2.0F, 4.0F, 11.0F, 4.0F, 0.25F, true);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-3.5F, 9.5F, 0.0F);
        this.torso.addChild(this.rleg);
        this.rleg.setTextureOffset(26, 91).addBox(-2.0F, 0.5F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F, false);
        this.rleg.setTextureOffset(67, 95).addBox(-2.0F, 0.5F, -2.0F, 4.0F, 11.0F, 4.0F, 0.25F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(BrainiacEntity.ANIMATION_THROW_BARREL);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.torso, 0.0F, (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-270.0), (float) Math.toRadians(-10.0), (float) Math.toRadians(-10.0));
        this.animator.rotate(this.handMaw, 0.0F, 0.0F, (float) Math.toRadians(20.0));
        this.animator.rotate(this.handMaw3, 0.0F, 0.0F, (float) Math.toRadians(40.0));
        this.animator.move(this.handMaw3, 0.0F, -4.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.larm, (float) Math.toRadians(-80.0), 0.0F, 0.0F);
        this.animator.rotate(this.handMaw, 0.0F, 0.0F, (float) Math.toRadians(-90.0));
        this.animator.rotate(this.handMaw3, 0.0F, 0.0F, (float) Math.toRadians(90.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(BrainiacEntity.ANIMATION_DRINK_BARREL);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.torso, 0.0F, (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-270.0), (float) Math.toRadians(-10.0), (float) Math.toRadians(-10.0));
        this.animator.rotate(this.handMaw, 0.0F, 0.0F, (float) Math.toRadians(20.0));
        this.animator.rotate(this.handMaw3, 0.0F, 0.0F, (float) Math.toRadians(40.0));
        this.animator.move(this.handMaw3, 0.0F, -4.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(10);
        this.animator.move(this.larm, 1.0F, 6.0F, 10.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-80.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.handMaw, 0.0F, 0.0F, (float) Math.toRadians(-90.0));
        this.animator.rotate(this.handMaw3, 0.0F, 0.0F, (float) Math.toRadians(90.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.move(this.larm, 1.0F, 5.0F, 10.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-70.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.handMaw, 0.0F, 0.0F, (float) Math.toRadians(-90.0));
        this.animator.rotate(this.handMaw3, 0.0F, 0.0F, (float) Math.toRadians(90.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.move(this.larm, 1.0F, 6.0F, 10.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-80.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.handMaw, 0.0F, 0.0F, (float) Math.toRadians(-90.0));
        this.animator.rotate(this.handMaw3, 0.0F, 0.0F, (float) Math.toRadians(90.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.move(this.larm, 1.0F, 5.0F, 10.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-70.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.handMaw, 0.0F, 0.0F, (float) Math.toRadians(-90.0));
        this.animator.rotate(this.handMaw3, 0.0F, 0.0F, (float) Math.toRadians(90.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.move(this.larm, 1.0F, 6.0F, 10.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-80.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.handMaw, 0.0F, 0.0F, (float) Math.toRadians(-90.0));
        this.animator.rotate(this.handMaw3, 0.0F, 0.0F, (float) Math.toRadians(90.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.move(this.larm, 1.0F, 5.0F, 10.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-70.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.handMaw, 0.0F, 0.0F, (float) Math.toRadians(-90.0));
        this.animator.rotate(this.handMaw3, 0.0F, 0.0F, (float) Math.toRadians(90.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.move(this.larm, 1.0F, 6.0F, 10.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-80.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.handMaw, 0.0F, 0.0F, (float) Math.toRadians(-90.0));
        this.animator.rotate(this.handMaw3, 0.0F, 0.0F, (float) Math.toRadians(90.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(BrainiacEntity.ANIMATION_BITE);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.torso, 0.0F, (float) Math.toRadians(-10.0), 0.0F);
        this.animator.rotate(this.chest, 0.0F, (float) Math.toRadians(-10.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.torso, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.chest, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-80.0), (float) Math.toRadians(-20.0), 0.0F);
        this.animator.rotate(this.handMaw, 0.0F, 0.0F, (float) Math.toRadians(-90.0));
        this.animator.rotate(this.handMaw3, 0.0F, 0.0F, (float) Math.toRadians(90.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.torso, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.chest, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-70.0), (float) Math.toRadians(-20.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(BrainiacEntity.ANIMATION_SMASH);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.chest, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-130.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-130.0), (float) Math.toRadians(-20.0), 0.0F);
        this.animator.rotate(this.handMaw, 0.0F, 0.0F, (float) Math.toRadians(-20.0));
        this.animator.rotate(this.handMaw3, 0.0F, 0.0F, (float) Math.toRadians(20.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-20.0), (float) Math.toRadians(-20.0), 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-20.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.torso);
    }

    public void translateToArmOrChest(PoseStack poseStack, boolean arm) {
        this.torso.translateAndRotate(poseStack);
        this.chest.translateAndRotate(poseStack);
        if (arm) {
            this.larm.translateAndRotate(poseStack);
        }
    }

    public void setupAnim(BrainiacEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        float walkSpeed = 0.5F;
        float walkDegree = 1.0F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float tongueLaunch = entity.getShootTongueAmount(partialTick);
        float tongueLength = entity.getLastTongueDistance(partialTick) * tongueLaunch;
        float tongueWiggle = (float) Math.sin((double) tongueLaunch * Math.PI);
        Entity tongueTarget = entity.getTongueTarget();
        if (tongueTarget != null && tongueLaunch > 0.0F) {
            float yBodyRot = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTick;
            Vec3 cameraPosition = tongueTarget.getEyePosition(partialTick);
            Vec3 eyePosition = entity.m_20299_(partialTick);
            double d0 = eyePosition.x - cameraPosition.x;
            double d1 = eyePosition.y - cameraPosition.y;
            double d2 = eyePosition.z - cameraPosition.z;
            double d3 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
            this.tongue.rotateAngleY = (float) ((double) this.tongue.rotateAngleY - (Math.toRadians((double) yBodyRot) + (double) this.head.rotateAngleY + (double) this.chest.rotateAngleY + (double) this.torso.rotateAngleY));
            this.tongue.rotateAngleY = this.tongue.rotateAngleY - (float) Math.toRadians((double) (-((float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI)) - 90.0F));
            this.tongue.rotateAngleX = this.tongue.rotateAngleX + (float) Math.toRadians((double) Mth.wrapDegrees((float) (Mth.atan2(d1, d3) * 180.0 / (float) Math.PI)));
        }
        if (tongueLength <= 0.0F) {
            this.tongue.showModel = false;
        } else {
            this.tongue.showModel = true;
        }
        float hunchAmount = limbSwingAmount * (1.0F - entity.getRaiseArmsAmount(partialTick));
        float hunchLeftAmount = hunchAmount * (1.0F - entity.getRaiseLeftArmAmount(partialTick));
        float zoomedHunch = Math.min(1.0F, hunchAmount * 3.0F);
        this.progressRotationPrev(this.chest, zoomedHunch, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.larm, zoomedHunch, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rarm, zoomedHunch, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, zoomedHunch, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.rarm, zoomedHunch, 0.0F, -3.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.larm, zoomedHunch, 0.0F, -2.5F, 0.0F, 1.0F);
        this.progressPositionPrev(this.head, zoomedHunch, 0.0F, -2.0F, 0.0F, 1.0F);
        this.tongue.setScale(1.0F, 1.0F, tongueLength);
        this.tongue.scaleChildren = true;
        this.brain.setScale((1.0F + (float) Math.sin((double) (ageInTicks * 0.5F + 2.0F))) * 0.1F + 1.0F, 1.0F, (1.0F + (float) Math.sin((double) (ageInTicks * 0.5F))) * 0.1F + 1.0F);
        this.flap(this.head, 0.15F, 0.1F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.head, 0.55F, 0.1F, false, 2.0F, 0.0F, ageInTicks, 0.5F + 0.5F * (float) Math.sin((double) (ageInTicks * 0.5F)));
        this.walk(this.tongue, 0.4F, 0.1F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.tongue2, 0.4F, 0.2F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tongue, 1.5F, 0.4F, false, 0.0F, 0.0F, ageInTicks, tongueWiggle);
        this.swing(this.tongue2, 1.5F, 0.4F, false, -1.0F, 0.0F, ageInTicks, tongueWiggle);
        this.tongue.rotateAngleZ = this.tongue.rotateAngleZ - this.head.rotateAngleZ;
        float bodyBob = this.walkValue(limbSwing, limbSwingAmount, walkSpeed * 1.5F, 0.5F, 1.0F, true);
        this.torso.rotationPointY += bodyBob;
        this.flap(this.chest, walkSpeed, walkDegree * 0.2F, false, 2.5F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.head, walkSpeed, walkDegree * -0.1F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.torso, walkSpeed, walkDegree * 0.1F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rleg, walkSpeed, walkDegree, true, 0.0F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(this.lleg, walkSpeed, walkDegree, false, 0.0F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(this.rarm, walkSpeed, walkDegree * 1.3F, false, 1.0F, 0.0F, limbSwing, hunchAmount);
        this.walk(this.larm, walkSpeed, walkDegree * 1.3F, false, -1.0F, 0.0F, limbSwing, hunchLeftAmount);
        this.walk(this.torso, walkSpeed, walkDegree * 0.1F, false, -1.0F, 0.0F, limbSwing, hunchLeftAmount);
        this.rarm.rotationPointY = this.rarm.rotationPointY + (Math.min(0.0F, this.walkValue(limbSwing, hunchAmount, walkSpeed, -0.75F, 1.0F, false)) - bodyBob);
        this.rarm.rotationPointZ = this.rarm.rotationPointZ + this.walkValue(limbSwing, hunchAmount, walkSpeed, -0.75F, -3.0F, true);
        this.larm.rotationPointY = this.larm.rotationPointY + (Math.min(0.0F, this.walkValue(limbSwing, hunchLeftAmount, walkSpeed, 0.75F, 1.0F, false)) - bodyBob);
        this.larm.rotationPointZ = this.larm.rotationPointZ + this.walkValue(limbSwing, hunchLeftAmount, walkSpeed, 0.75F, -3.0F, true);
        this.rarm.rotateAngleZ = this.rarm.rotateAngleZ - this.torso.rotateAngleZ;
        this.larm.rotateAngleZ = this.larm.rotateAngleZ - this.torso.rotateAngleZ;
        float yawAmount = netHeadYaw / (180.0F / (float) Math.PI);
        float pitchAmount = headPitch / (180.0F / (float) Math.PI);
        this.head.rotateAngleX += pitchAmount * 0.25F;
        this.head.rotateAngleY += yawAmount * 0.5F;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.torso, this.chest, this.lleg, this.rleg, this.rarm, this.larm, this.handMaw, this.handMaw3, this.head, this.brain, this.tongue, this.tongue2, new AdvancedModelBox[0]);
    }

    private float walkValue(float limbSwing, float limbSwingAmount, float speed, float offset, float degree, boolean inverse) {
        return (float) (Math.cos((double) (limbSwing * speed + offset)) * (double) degree * (double) limbSwingAmount * (double) (inverse ? -1 : 1));
    }
}