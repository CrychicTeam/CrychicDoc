package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneMageEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;

public class DeepOneMageModel extends AdvancedEntityModel<DeepOneMageEntity> implements ArmedModel {

    private final AdvancedModelBox body;

    private final AdvancedModelBox eye;

    private final AdvancedModelBox smTentaclefront;

    private final AdvancedModelBox smTentacleleft;

    private final AdvancedModelBox smTentacleback;

    private final AdvancedModelBox smTentacleright;

    private final AdvancedModelBox bigTentaclerightFront;

    private final AdvancedModelBox bigTentaclebottom;

    private final AdvancedModelBox bigTentacleleftFront;

    private final AdvancedModelBox bigTentaclebottom2;

    private final AdvancedModelBox bigTentacleleftBack;

    private final AdvancedModelBox bigTentaclebottom3;

    private final AdvancedModelBox bigTentaclerightBack;

    private final AdvancedModelBox bigTentaclebottom4;

    private final AdvancedModelBox bigTentaclerightArm;

    private final AdvancedModelBox bTendrilRight;

    private final AdvancedModelBox bTendrilLeft;

    private final AdvancedModelBox fin;

    private final AdvancedModelBox bigTentacleleftArm;

    private final ModelAnimator animator;

    public DeepOneMageModel() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 15.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addBox(-10.5F, -19.0F, -5.5F, 21.0F, 15.0F, 21.0F, 0.0F, false);
        this.body.setTextureOffset(43, 45).addBox(-5.5F, -4.0F, -5.5F, 11.0F, 9.0F, 11.0F, 0.0F, false);
        this.body.setTextureOffset(0, 9).addBox(0.0F, -24.0F, -11.5F, 0.0F, 20.0F, 27.0F, 0.0F, false);
        this.eye = new AdvancedModelBox(this);
        this.eye.setRotationPoint(0.0F, -11.0F, 5.0F);
        this.body.addChild(this.eye);
        this.eye.setTextureOffset(105, 33).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.0F, false);
        this.smTentaclefront = new AdvancedModelBox(this);
        this.smTentaclefront.setRotationPoint(0.0F, 5.0F, -5.5F);
        this.body.addChild(this.smTentaclefront);
        this.smTentaclefront.setTextureOffset(26, 65).addBox(-5.5F, 0.0F, 0.0F, 11.0F, 10.0F, 0.0F, 0.0F, false);
        this.smTentacleleft = new AdvancedModelBox(this);
        this.smTentacleleft.setRotationPoint(5.5F, 5.0F, 0.0F);
        this.body.addChild(this.smTentacleleft);
        this.smTentacleleft.setTextureOffset(26, 54).addBox(0.0F, 0.0F, -5.5F, 0.0F, 10.0F, 11.0F, 0.0F, true);
        this.smTentacleback = new AdvancedModelBox(this);
        this.smTentacleback.setRotationPoint(0.0F, 5.0F, 5.5F);
        this.body.addChild(this.smTentacleback);
        this.smTentacleback.setTextureOffset(26, 65).addBox(-5.5F, 0.0F, 0.0F, 11.0F, 10.0F, 0.0F, 0.0F, false);
        this.smTentacleright = new AdvancedModelBox(this);
        this.smTentacleright.setRotationPoint(-5.5F, 5.0F, 0.0F);
        this.body.addChild(this.smTentacleright);
        this.smTentacleright.setTextureOffset(26, 54).addBox(0.0F, 0.0F, -5.5F, 0.0F, 10.0F, 11.0F, 0.0F, false);
        this.bigTentaclerightFront = new AdvancedModelBox(this);
        this.bigTentaclerightFront.setRotationPoint(-4.0F, 5.0F, -3.5F);
        this.body.addChild(this.bigTentaclerightFront);
        this.setRotateAngle(this.bigTentaclerightFront, 0.0F, 0.7854F, 0.0F);
        this.bigTentaclerightFront.setTextureOffset(10, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 11.0F, 0.0F, 0.0F, false);
        this.bigTentaclebottom = new AdvancedModelBox(this);
        this.bigTentaclebottom.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.bigTentaclerightFront.addChild(this.bigTentaclebottom);
        this.bigTentaclebottom.setTextureOffset(0, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 11.0F, 0.0F, 0.0F, false);
        this.bigTentacleleftFront = new AdvancedModelBox(this);
        this.bigTentacleleftFront.setRotationPoint(4.0F, 5.0F, -3.5F);
        this.body.addChild(this.bigTentacleleftFront);
        this.setRotateAngle(this.bigTentacleleftFront, 0.0F, -0.7854F, 0.0F);
        this.bigTentacleleftFront.setTextureOffset(10, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 11.0F, 0.0F, 0.0F, true);
        this.bigTentaclebottom2 = new AdvancedModelBox(this);
        this.bigTentaclebottom2.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.bigTentacleleftFront.addChild(this.bigTentaclebottom2);
        this.bigTentaclebottom2.setTextureOffset(0, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 11.0F, 0.0F, 0.0F, true);
        this.bigTentacleleftBack = new AdvancedModelBox(this);
        this.bigTentacleleftBack.setRotationPoint(4.0F, 5.0F, 4.0F);
        this.body.addChild(this.bigTentacleleftBack);
        this.setRotateAngle(this.bigTentacleleftBack, 0.0F, 0.7854F, 0.0F);
        this.bigTentacleleftBack.setTextureOffset(10, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 11.0F, 0.0F, 0.0F, true);
        this.bigTentaclebottom3 = new AdvancedModelBox(this);
        this.bigTentaclebottom3.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.bigTentacleleftBack.addChild(this.bigTentaclebottom3);
        this.bigTentaclebottom3.setTextureOffset(0, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 11.0F, 0.0F, 0.0F, true);
        this.bigTentaclerightBack = new AdvancedModelBox(this);
        this.bigTentaclerightBack.setRotationPoint(-4.0F, 5.0F, 4.0F);
        this.body.addChild(this.bigTentaclerightBack);
        this.setRotateAngle(this.bigTentaclerightBack, 0.0F, -0.7854F, 0.0F);
        this.bigTentaclerightBack.setTextureOffset(10, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 11.0F, 0.0F, 0.0F, false);
        this.bigTentaclebottom4 = new AdvancedModelBox(this);
        this.bigTentaclebottom4.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.bigTentaclerightBack.addChild(this.bigTentaclebottom4);
        this.bigTentaclebottom4.setTextureOffset(0, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 11.0F, 0.0F, 0.0F, false);
        this.bigTentaclerightArm = new AdvancedModelBox(this);
        this.bigTentaclerightArm.setRotationPoint(-2.5F, 3.0F, -0.5F);
        this.body.addChild(this.bigTentaclerightArm);
        this.bigTentaclerightArm.setTextureOffset(63, 0).addBox(-17.0F, -10.0F, 0.0F, 19.0F, 21.0F, 0.0F, 0.0F, false);
        this.bTendrilRight = new AdvancedModelBox(this);
        this.bTendrilRight.setRotationPoint(8.5F, -4.0F, 15.5F);
        this.body.addChild(this.bTendrilRight);
        this.bTendrilRight.setTextureOffset(48, 65).addBox(-4.0F, 0.0F, 0.0F, 6.0F, 27.0F, 0.0F, 0.0F, false);
        this.bTendrilLeft = new AdvancedModelBox(this);
        this.bTendrilLeft.setRotationPoint(-8.5F, -4.0F, 15.5F);
        this.body.addChild(this.bTendrilLeft);
        this.bTendrilLeft.setTextureOffset(48, 65).addBox(-2.0F, 0.0F, 0.0F, 6.0F, 27.0F, 0.0F, 0.0F, true);
        this.fin = new AdvancedModelBox(this);
        this.fin.setRotationPoint(0.0F, -21.5F, 15.5F);
        this.body.addChild(this.fin);
        this.fin.setTextureOffset(0, 43).addBox(0.0F, -2.5F, 0.0F, 0.0F, 20.0F, 13.0F, 0.0F, false);
        this.bigTentacleleftArm = new AdvancedModelBox(this);
        this.bigTentacleleftArm.setRotationPoint(2.5F, 3.0F, -0.5F);
        this.body.addChild(this.bigTentacleleftArm);
        this.bigTentacleleftArm.setTextureOffset(63, 0).addBox(-2.0F, -10.0F, 0.0F, 19.0F, 21.0F, 0.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.eye, this.smTentacleback, this.smTentaclefront, this.smTentacleleft, this.smTentacleright, this.bigTentaclebottom, this.bigTentaclebottom2, this.bigTentaclebottom3, this.bigTentaclebottom4, this.bigTentaclerightFront, this.bigTentaclerightBack, new AdvancedModelBox[] { this.bigTentacleleftBack, this.bigTentacleleftFront, this.bigTentaclerightArm, this.bigTentacleleftArm, this.fin, this.bTendrilLeft, this.bTendrilRight });
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(DeepOneMageEntity.ANIMATION_DISAPPEAR);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(5.0), (float) Math.toRadians(-15.0));
        this.animator.rotate(this.bigTentacleleftArm, 0.0F, (float) Math.toRadians(15.0), (float) Math.toRadians(-25.0));
        this.animator.rotate(this.bigTentaclerightArm, 0.0F, (float) Math.toRadians(-35.0), (float) Math.toRadians(75.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-5.0), (float) Math.toRadians(10.0));
        this.animator.rotate(this.bigTentacleleftArm, 0.0F, (float) Math.toRadians(35.0), (float) Math.toRadians(-75.0));
        this.animator.rotate(this.bigTentaclerightArm, 0.0F, (float) Math.toRadians(-15.0), (float) Math.toRadians(25.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(5.0), (float) Math.toRadians(-15.0));
        this.animator.rotate(this.bigTentacleleftArm, 0.0F, (float) Math.toRadians(15.0), (float) Math.toRadians(-25.0));
        this.animator.rotate(this.bigTentaclerightArm, 0.0F, (float) Math.toRadians(-35.0), (float) Math.toRadians(75.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-5.0), (float) Math.toRadians(10.0));
        this.animator.rotate(this.bigTentacleleftArm, 0.0F, (float) Math.toRadians(35.0), (float) Math.toRadians(-75.0));
        this.animator.rotate(this.bigTentaclerightArm, 0.0F, (float) Math.toRadians(-15.0), (float) Math.toRadians(25.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.rotate(this.body, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.bigTentacleleftArm, 0.0F, (float) Math.toRadians(75.0), (float) Math.toRadians(-55.0));
        this.animator.rotate(this.bigTentaclerightArm, 0.0F, (float) Math.toRadians(-75.0), (float) Math.toRadians(55.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(DeepOneMageEntity.ANIMATION_ATTACK);
        this.animator.startKeyframe(10);
        this.animator.move(this.body, 0.0F, -3.0F, 0.0F);
        this.animator.rotate(this.body, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.smTentacleback, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.smTentaclefront, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.smTentacleleft, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.smTentacleright, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.bTendrilLeft, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.bTendrilRight, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.bigTentacleleftArm, (float) Math.toRadians(-10.0), (float) Math.toRadians(-10.0), (float) Math.toRadians(-85.0));
        this.animator.rotate(this.bigTentaclerightArm, (float) Math.toRadians(-10.0), (float) Math.toRadians(10.0), (float) Math.toRadians(85.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.bigTentacleleftArm, 0.0F, (float) Math.toRadians(90.0), (float) Math.toRadians(-55.0));
        this.animator.rotate(this.bigTentaclerightArm, 0.0F, (float) Math.toRadians(-90.0), (float) Math.toRadians(55.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.resetKeyframe(8);
        this.animator.setAnimation(DeepOneMageEntity.ANIMATION_SPIN);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.body, (float) Math.toRadians(-5.0), (float) Math.toRadians(-20.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(70.0), 0.0F);
        this.animatePose(0);
        this.animator.endKeyframe();
        this.animator.startKeyframe(50);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(1690.0), 0.0F);
        this.animatePose(0);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(1780.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.setAnimation(DeepOneMageEntity.ANIMATION_TRADE);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.body, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animatePose(1);
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.rotate(this.body, (float) Math.toRadians(5.0), 0.0F, (float) Math.toRadians(5.0));
        this.animatePose(1);
        this.animator.endKeyframe();
        this.animator.startKeyframe(20);
        this.animator.rotate(this.body, (float) Math.toRadians(5.0), 0.0F, (float) Math.toRadians(-5.0));
        this.animatePose(1);
        this.animator.endKeyframe();
        this.animator.startKeyframe(20);
        this.animator.rotate(this.body, (float) Math.toRadians(5.0), 0.0F, (float) Math.toRadians(5.0));
        this.animatePose(1);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, (float) Math.toRadians(-6.0), 0.0F, 0.0F);
        this.animator.rotate(this.bigTentacleleftArm, 0.0F, (float) Math.toRadians(90.0), (float) Math.toRadians(-55.0));
        this.animator.rotate(this.bigTentaclerightArm, 0.0F, (float) Math.toRadians(-90.0), (float) Math.toRadians(55.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
    }

    private void animatePose(int pose) {
        if (pose == 0) {
            this.animator.rotate(this.smTentacleback, (float) Math.toRadians(80.0), 0.0F, 0.0F);
            this.animator.rotate(this.smTentaclefront, (float) Math.toRadians(-80.0), 0.0F, 0.0F);
            this.animator.rotate(this.smTentacleleft, 0.0F, 0.0F, (float) Math.toRadians(-80.0));
            this.animator.rotate(this.smTentacleright, 0.0F, 0.0F, (float) Math.toRadians(80.0));
            this.animator.rotate(this.bigTentaclerightFront, (float) Math.toRadians(-80.0), 0.0F, 0.0F);
            this.animator.rotate(this.bigTentacleleftFront, (float) Math.toRadians(-80.0), 0.0F, 0.0F);
            this.animator.rotate(this.bigTentaclerightBack, (float) Math.toRadians(80.0), 0.0F, 0.0F);
            this.animator.rotate(this.bigTentacleleftBack, (float) Math.toRadians(80.0), 0.0F, 0.0F);
            this.animator.rotate(this.bTendrilRight, (float) Math.toRadians(30.0), 0.0F, 0.0F);
            this.animator.rotate(this.bTendrilLeft, (float) Math.toRadians(30.0), 0.0F, 0.0F);
            this.animator.rotate(this.bigTentacleleftArm, 0.0F, 0.0F, (float) Math.toRadians(15.0));
            this.animator.rotate(this.bigTentaclerightArm, 0.0F, 0.0F, (float) Math.toRadians(-15.0));
        } else if (pose == 1) {
            this.animator.move(this.bigTentacleleftArm, 0.0F, 0.0F, -3.0F);
            this.animator.move(this.bigTentaclerightArm, 0.0F, 0.0F, -3.0F);
            this.animator.rotate(this.bigTentacleleftArm, (float) Math.toRadians(50.0), (float) Math.toRadians(40.0), (float) Math.toRadians(65.0));
            this.animator.rotate(this.bigTentaclerightArm, (float) Math.toRadians(50.0), (float) Math.toRadians(-40.0), (float) Math.toRadians(-65.0));
        }
    }

    public void setupAnim(DeepOneMageEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        float walkSpeed = 0.5F;
        float walkDegree = 0.3F;
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float yBodyRot = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTicks;
        float bodyIdleBob = ACMath.walkValue(ageInTicks, 1.0F, 0.1F, 0.0F, 1.0F, false);
        float bodyWalkBob = ACMath.walkValue(ageInTicks, limbSwingAmount, walkSpeed, 1.0F, 2.0F, false);
        float handsDownAmount = 1.0F - limbSwingAmount;
        float landProgress = 1.0F - entity.m_20998_(partialTicks);
        float fishPitchAmount = entity.getFishPitch(partialTicks) / (180.0F / (float) Math.PI);
        float clampedYaw = netHeadYaw / (180.0F / (float) Math.PI);
        float headPitchAmount = headPitch / (180.0F / (float) Math.PI);
        this.body.rotationPointY += bodyIdleBob + bodyWalkBob - landProgress * 16.0F;
        this.eye.rotationPointY -= bodyIdleBob + bodyWalkBob;
        this.progressRotationPrev(this.body, limbSwingAmount, (float) Math.toRadians((double) (20.0F + 20.0F * entity.m_20998_(partialTicks))), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.bigTentacleleftArm, handsDownAmount, 2.0F, 1.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.bigTentacleleftArm, handsDownAmount, 0.0F, 0.0F, (float) Math.toRadians(45.0), 1.0F);
        this.progressPositionPrev(this.bigTentaclerightArm, handsDownAmount, -2.0F, 1.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.bigTentaclerightArm, handsDownAmount, 0.0F, 0.0F, (float) Math.toRadians(-45.0), 1.0F);
        if (entity.getAnimation() != DeepOneMageEntity.ANIMATION_TRADE) {
            this.flap(this.bigTentaclerightArm, 0.1F, 0.15F, false, 1.0F, -0.1F, ageInTicks, 1.0F);
            this.swing(this.bigTentaclerightArm, 0.1F, 0.15F, false, 1.0F, -0.1F, ageInTicks, 1.0F);
            this.flap(this.bigTentacleleftArm, 0.1F, 0.15F, true, 2.0F, 0.0F, ageInTicks, 1.0F);
            this.swing(this.bigTentacleleftArm, 0.1F, 0.15F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        }
        this.walk(this.body, 0.1F, 0.02F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.bTendrilLeft, 0.1F, 0.2F, false, 0.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.bTendrilRight, 0.1F, 0.2F, false, 0.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.smTentacleback, 0.1F, 0.1F, false, -2.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.smTentaclefront, 0.1F, 0.1F, true, -2.0F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.smTentacleleft, 0.1F, 0.1F, true, -2.0F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.smTentacleright, 0.1F, 0.1F, false, -2.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.bigTentacleleftFront, 0.1F, 0.15F, true, -3.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.bigTentaclebottom2, 0.1F, 0.1F, true, -4.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.bigTentaclerightFront, 0.1F, 0.1F, true, -3.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.bigTentaclebottom, 0.1F, 0.1F, true, -4.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.bigTentacleleftBack, 0.1F, 0.15F, false, -3.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.bigTentaclebottom4, 0.1F, 0.1F, false, -4.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.bigTentaclerightBack, 0.1F, 0.1F, false, -3.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.bigTentaclebottom3, 0.1F, 0.1F, false, -4.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.bigTentacleleftFront, walkSpeed, walkDegree, true, -3.0F, 0.1F, ageInTicks, limbSwingAmount);
        this.walk(this.bigTentaclebottom2, walkSpeed, walkDegree * 0.5F, true, -4.0F, 0.0F, ageInTicks, limbSwingAmount);
        this.walk(this.bigTentaclerightFront, walkSpeed, walkDegree, true, -3.0F, 0.1F, ageInTicks, limbSwingAmount);
        this.walk(this.bigTentaclebottom, walkSpeed, walkDegree * 0.5F, true, -4.0F, 0.0F, ageInTicks, limbSwingAmount);
        this.walk(this.bigTentacleleftBack, walkSpeed, walkDegree, false, -3.0F, 0.1F, ageInTicks, limbSwingAmount);
        this.walk(this.bigTentaclebottom4, walkSpeed, walkDegree * 0.5F, false, -4.0F, 0.0F, ageInTicks, limbSwingAmount);
        this.walk(this.bigTentaclerightBack, walkSpeed, walkDegree, false, -3.0F, 0.1F, ageInTicks, limbSwingAmount);
        this.walk(this.bigTentaclebottom3, walkSpeed, walkDegree * 0.5F, false, -4.0F, 0.0F, ageInTicks, limbSwingAmount);
        this.walk(this.smTentacleback, walkSpeed, walkDegree, false, -2.0F, 0.1F, ageInTicks, limbSwingAmount);
        this.walk(this.smTentaclefront, walkSpeed, walkDegree, true, -2.0F, 0.1F, ageInTicks, limbSwingAmount);
        this.flap(this.smTentacleleft, walkSpeed, walkDegree, true, -2.0F, 0.1F, ageInTicks, limbSwingAmount);
        this.flap(this.smTentacleright, walkSpeed, walkDegree, false, -2.0F, 0.1F, ageInTicks, limbSwingAmount);
        this.walk(this.bTendrilLeft, walkSpeed, walkDegree * 0.3F, true, 3.0F, -0.4F, ageInTicks, limbSwingAmount);
        this.walk(this.bTendrilRight, walkSpeed, walkDegree * 0.3F, false, 3.0F, 0.4F, ageInTicks, limbSwingAmount);
        this.swing(this.bigTentaclerightArm, walkSpeed, walkDegree * 0.3F, true, 5.0F, -0.1F, ageInTicks, limbSwingAmount);
        this.swing(this.bigTentacleleftArm, walkSpeed, walkDegree * 0.3F, false, 5.0F, 0.1F, ageInTicks, limbSwingAmount);
        this.body.rotateAngleX += fishPitchAmount;
        this.body.rotateAngleX += headPitchAmount;
        this.body.rotateAngleY += clampedYaw;
        Entity look = Minecraft.getInstance().getCameraEntity();
        if (look != null) {
            Vec3 cameraPosition = look.getEyePosition(partialTicks);
            Vec3 eyePosition = entity.m_20299_(partialTicks);
            double d0 = eyePosition.x - cameraPosition.x;
            double d1 = eyePosition.y - cameraPosition.y;
            double d2 = eyePosition.z - cameraPosition.z;
            double d3 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
            this.eye.rotateAngleX = (float) Math.toRadians((double) Mth.wrapDegrees((float) (Mth.atan2(d1, d3) * 180.0 / (float) Math.PI))) + this.body.rotateAngleX;
            this.eye.rotateAngleY = (float) ((double) this.eye.rotateAngleY - (Math.toRadians((double) yBodyRot) + (double) this.body.rotateAngleY));
            this.eye.rotateAngleY = (float) ((double) this.eye.rotateAngleY - Math.toRadians((double) (-((float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI)) - 90.0F)));
            this.eye.rotateAngleZ = this.eye.rotateAngleZ - this.body.rotateAngleZ;
        }
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        this.body.translateAndRotate(poseStack);
        if (humanoidArm == HumanoidArm.RIGHT) {
            this.bigTentaclerightArm.translateAndRotate(poseStack);
            poseStack.translate(-1.0, -0.6F, 0.1);
        } else {
            this.bigTentacleleftArm.translateAndRotate(poseStack);
            poseStack.translate(0.4F, -0.6F, -0.1);
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
    }
}