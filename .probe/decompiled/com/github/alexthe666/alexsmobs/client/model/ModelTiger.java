package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityTiger;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelTiger extends AdvancedEntityModel<EntityTiger> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox head;

    private final AdvancedModelBox earleft;

    private final AdvancedModelBox earright;

    private final AdvancedModelBox snout;

    private final AdvancedModelBox legleft;

    private final AdvancedModelBox legright;

    private final AdvancedModelBox armleft;

    private final AdvancedModelBox armright;

    private final ModelAnimator animator;

    public ModelTiger() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -14.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-5.0F, -6.0F, -12.0F, 10.0F, 11.0F, 22.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, -4.0F, 8.6F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, 0.0873F, 0.0F, 0.0F);
        this.tail.setTextureOffset(46, 34).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 9.0F, 3.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setPos(0.0F, 7.9F, 0.0F);
        this.tail.addChild(this.tail2);
        this.setRotationAngle(this.tail2, 0.2182F, 0.0F, 0.0F);
        this.tail2.setTextureOffset(43, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 9.0F, 3.0F, -0.1F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -4.0F, -12.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 34).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 7.0F, 6.0F, 0.0F, false);
        this.head.setTextureOffset(9, 15).addBox(4.0F, -1.0F, -5.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        this.head.setTextureOffset(9, 15).addBox(-5.0F, -1.0F, -5.0F, 1.0F, 4.0F, 2.0F, 0.0F, true);
        this.earleft = new AdvancedModelBox(this, "earleft");
        this.earleft.setPos(3.0F, -4.0F, -2.0F);
        this.head.addChild(this.earleft);
        this.earleft.setTextureOffset(0, 15).addBox(0.0F, -2.0F, -2.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        this.earright = new AdvancedModelBox(this, "earright");
        this.earright.setPos(-3.0F, -4.0F, -2.0F);
        this.head.addChild(this.earright);
        this.earright.setTextureOffset(0, 15).addBox(-1.0F, -2.0F, -2.0F, 1.0F, 2.0F, 3.0F, 0.0F, true);
        this.snout = new AdvancedModelBox(this, "snout");
        this.snout.setPos(0.0F, -1.0F, -6.0F);
        this.head.addChild(this.snout);
        this.setRotationAngle(this.snout, 0.1745F, 0.0F, 0.0F);
        this.snout.setTextureOffset(43, 13).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 4.0F, 3.0F, 0.0F, false);
        this.legleft = new AdvancedModelBox(this, "legleft");
        this.legleft.setPos(2.9F, 5.0F, 7.9F);
        this.body.addChild(this.legleft);
        this.legleft.setTextureOffset(0, 48).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 11.0F, 5.0F, 0.0F, false);
        this.legright = new AdvancedModelBox(this, "legright");
        this.legright.setPos(-2.9F, 5.0F, 7.9F);
        this.body.addChild(this.legright);
        this.legright.setTextureOffset(0, 48).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 11.0F, 5.0F, 0.0F, true);
        this.armleft = new AdvancedModelBox(this, "armleft");
        this.armleft.setPos(3.5F, -1.5F, -9.0F);
        this.body.addChild(this.armleft);
        this.armleft.setTextureOffset(29, 34).addBox(-2.0F, -5.5F, -2.0F, 4.0F, 21.0F, 4.0F, 0.0F, false);
        this.armright = new AdvancedModelBox(this, "armright");
        this.armright.setPos(-3.5F, -1.5F, -9.0F);
        this.body.addChild(this.armright);
        this.armright.setTextureOffset(29, 34).addBox(-2.0F, -5.5F, -2.0F, 4.0F, 21.0F, 4.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityTiger.ANIMATION_PAW_R);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.armleft, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.armright, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.move(this.body, 0.0F, 0.0F, 3.0F);
        this.animator.move(this.armright, 0.0F, 1.0F, 0.0F);
        this.animator.move(this.armleft, 0.0F, 1.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.head, Maths.rad(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.armleft, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.armright, Maths.rad(-70.0), 0.0F, Maths.rad(20.0));
        this.animator.rotate(this.tail, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail2, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.move(this.body, 0.0F, -3.0F, -3.0F);
        this.animator.rotate(this.body, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.legleft, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.legright, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.head, Maths.rad(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.armleft, Maths.rad(10.0), 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.armright, Maths.rad(-40.0), 0.0F, Maths.rad(-20.0));
        this.animator.rotate(this.tail, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail2, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.move(this.body, 0.0F, -3.0F, -3.0F);
        this.animator.move(this.armright, -1.0F, 0.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.legleft, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.legright, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityTiger.ANIMATION_PAW_L);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.armleft, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.armright, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.move(this.body, 0.0F, 0.0F, 3.0F);
        this.animator.move(this.armright, 0.0F, 1.0F, 0.0F);
        this.animator.move(this.armleft, 0.0F, 1.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.head, Maths.rad(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.armleft, Maths.rad(-70.0), 0.0F, Maths.rad(-20.0));
        this.animator.rotate(this.armright, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail2, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.move(this.body, 0.0F, -3.0F, -3.0F);
        this.animator.rotate(this.body, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.legleft, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.legright, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.head, Maths.rad(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.armleft, Maths.rad(-40.0), 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.armright, Maths.rad(10.0), 0.0F, Maths.rad(20.0));
        this.animator.rotate(this.tail, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail2, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.move(this.body, 0.0F, -3.0F, -3.0F);
        this.animator.move(this.armleft, 1.0F, 0.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.legleft, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.legright, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityTiger.ANIMATION_TAIL_FLICK);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.tail, Maths.rad(10.0), 0.0F, Maths.rad(30.0));
        this.animator.rotate(this.tail2, Maths.rad(10.0), 0.0F, Maths.rad(20.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.rotate(this.tail, Maths.rad(10.0), 0.0F, Maths.rad(-30.0));
        this.animator.rotate(this.tail2, Maths.rad(10.0), 0.0F, Maths.rad(-20.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.rotate(this.tail, Maths.rad(10.0), 0.0F, Maths.rad(30.0));
        this.animator.rotate(this.tail2, Maths.rad(10.0), 0.0F, Maths.rad(20.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityTiger.ANIMATION_LEAP);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 1.0F, 3.0F);
        this.animator.move(this.head, 0.0F, 2.0F, 0.0F);
        this.animator.move(this.armright, 0.0F, 1.0F, 2.0F);
        this.animator.move(this.armleft, 0.0F, 1.0F, 2.0F);
        this.animator.rotate(this.head, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.armleft, Maths.rad(-50.0), 0.0F, 0.0F);
        this.animator.rotate(this.armright, Maths.rad(-50.0), 0.0F, 0.0F);
        this.animator.rotate(this.legright, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.legleft, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.tail, Maths.rad(90.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail2, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.legleft, Maths.rad(30.0), 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.legright, Maths.rad(30.0), 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.armright, Maths.rad(-60.0), 0.0F, Maths.rad(30.0));
        this.animator.rotate(this.armleft, Maths.rad(-60.0), 0.0F, Maths.rad(-30.0));
        this.animator.move(this.armright, -1.0F, -1.0F, 1.0F);
        this.animator.move(this.armleft, 1.0F, -1.0F, 1.0F);
        this.animator.move(this.legright, 0.0F, -4.0F, -1.0F);
        this.animator.move(this.legleft, 0.0F, -4.0F, -1.0F);
        this.animator.move(this.body, 0.0F, -5.0F, 4.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(EntityTiger entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 0.7F;
        float walkDegree = 0.8F;
        float runSpeed = 1.0F;
        float runDegree = 0.8F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float moveProgress = 5.0F * limbSwingAmount;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTick;
        float holdProgress = entity.prevHoldProgress + (entity.holdProgress - entity.prevHoldProgress) * partialTick;
        float sleepProgress = entity.prevSleepProgress + (entity.sleepProgress - entity.prevSleepProgress) * partialTick;
        boolean leftSleep = entity.m_19879_() % 2 == 0;
        this.walk(this.tail, idleSpeed, idleDegree * 1.0F, false, -2.0F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.tail, idleSpeed, idleDegree * 1.2F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.tail2, idleSpeed, idleDegree * 2.0F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[] { this.tail, this.tail2 };
        this.progressRotationPrev(this.tail, moveProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, Math.min(moveProgress * 2.0F, 5.0F), 0.0F, 2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.tail, moveProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, moveProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.armleft, moveProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.armright, moveProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.legleft, moveProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.legright, moveProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        if (entity.isRunning()) {
            this.chainFlap(tailBoxes, runSpeed, runDegree * 0.5F, -1.0, limbSwing, limbSwingAmount);
            this.bob(this.body, runSpeed, runDegree * 2.0F, false, limbSwing, limbSwingAmount);
            this.bob(this.head, runSpeed, runDegree * -1.0F, false, limbSwing, limbSwingAmount);
            this.walk(this.armleft, runSpeed, runDegree * 0.75F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.armright, runSpeed, runDegree * 0.75F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.legright, runSpeed, runDegree * 1.0F, false, 0.5F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.legleft, runSpeed, runDegree * 1.0F, false, 0.5F, 0.0F, limbSwing, limbSwingAmount);
        } else {
            this.chainFlap(tailBoxes, walkSpeed, walkDegree * 1.0F, -1.0, limbSwing, limbSwingAmount);
            this.flap(this.body, walkSpeed, walkDegree * 0.3F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.head, walkSpeed, -walkDegree * 0.3F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.armleft, walkSpeed, -walkDegree * 0.1F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.armright, walkSpeed, -walkDegree * 0.1F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.legleft, walkSpeed, -walkDegree * 0.3F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.legright, walkSpeed, -walkDegree * 0.3F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.armright, walkSpeed, walkDegree * 0.5F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.bob(this.armright, walkSpeed, -walkDegree, false, limbSwing, limbSwingAmount);
            this.walk(this.armleft, walkSpeed, walkDegree * 0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.bob(this.armleft, walkSpeed, -walkDegree, false, limbSwing, limbSwingAmount);
            this.walk(this.legright, walkSpeed, walkDegree * 0.8F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.bob(this.legright, walkSpeed, -walkDegree, false, limbSwing, limbSwingAmount);
            this.walk(this.legleft, walkSpeed, walkDegree * 0.8F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.bob(this.legleft, walkSpeed, -walkDegree, false, limbSwing, limbSwingAmount);
            this.bob(this.body, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        }
        this.progressRotationPrev(this.legleft, sitProgress, Maths.rad(-90.0), Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.legright, sitProgress, Maths.rad(-90.0), Maths.rad(20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.armleft, sitProgress, Maths.rad(-50.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.armright, sitProgress, Maths.rad(-50.0), 0.0F, 0.0F, 5.0F);
        float tailAngle = entity.m_19879_() % 2 == 0 ? 1.0F : -1.0F;
        this.progressRotationPrev(this.tail, sitProgress, Maths.rad(20.0), Maths.rad((double) (tailAngle * -15.0F)), Maths.rad((double) (tailAngle * 15.0F)), 5.0F);
        this.progressRotationPrev(this.tail2, sitProgress, Maths.rad(20.0), Maths.rad((double) (tailAngle * -30.0F)), Maths.rad((double) (tailAngle * 30.0F)), 5.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 5.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.tail, sitProgress, 0.0F, 2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.tail2, sitProgress, tailAngle, 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.armright, sitProgress, 0.0F, -1.0F, 4.0F, 5.0F);
        this.progressPositionPrev(this.armleft, sitProgress, 0.0F, -1.0F, 4.0F, 5.0F);
        this.progressPositionPrev(this.legright, sitProgress, 0.0F, 2.8F, -0.5F, 5.0F);
        this.progressPositionPrev(this.legleft, sitProgress, 0.0F, 2.8F, -0.5F, 5.0F);
        if (leftSleep) {
            this.progressRotationPrev(this.body, sleepProgress, 0.0F, 0.0F, Maths.rad(-90.0), 5.0F);
            this.progressRotationPrev(this.head, sleepProgress, 0.0F, 0.0F, Maths.rad(73.0), 5.0F);
            this.progressRotationPrev(this.tail, sleepProgress, 0.0F, 0.0F, Maths.rad(20.0), 5.0F);
            this.progressRotationPrev(this.tail2, sleepProgress, 0.0F, 0.0F, Maths.rad(-20.0), 5.0F);
            this.progressRotationPrev(this.armleft, sleepProgress, Maths.rad(-10.0), 0.0F, Maths.rad(10.0), 5.0F);
            this.progressRotationPrev(this.armright, sleepProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.legright, sleepProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.legleft, sleepProgress, Maths.rad(10.0), 0.0F, Maths.rad(20.0), 5.0F);
            this.progressPositionPrev(this.armleft, sleepProgress, 1.0F, -1.0F, 0.0F, 5.0F);
            this.progressPositionPrev(this.armright, sleepProgress, 0.5F, -1.0F, 1.0F, 5.0F);
            this.progressPositionPrev(this.body, sleepProgress, 0.0F, 9.0F, 0.0F, 5.0F);
            this.progressPositionPrev(this.head, sleepProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        } else {
            this.progressRotationPrev(this.body, sleepProgress, 0.0F, 0.0F, Maths.rad(90.0), 5.0F);
            this.progressRotationPrev(this.head, sleepProgress, 0.0F, 0.0F, Maths.rad(-73.0), 5.0F);
            this.progressRotationPrev(this.tail, sleepProgress, 0.0F, 0.0F, Maths.rad(-20.0), 5.0F);
            this.progressRotationPrev(this.tail2, sleepProgress, 0.0F, 0.0F, Maths.rad(20.0), 5.0F);
            this.progressRotationPrev(this.armright, sleepProgress, Maths.rad(-10.0), 0.0F, Maths.rad(-10.0), 5.0F);
            this.progressRotationPrev(this.armleft, sleepProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.legleft, sleepProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.legright, sleepProgress, Maths.rad(10.0), 0.0F, Maths.rad(-20.0), 5.0F);
            this.progressPositionPrev(this.armright, sleepProgress, -1.0F, -1.0F, 0.0F, 5.0F);
            this.progressPositionPrev(this.armleft, sleepProgress, -0.5F, -1.0F, 1.0F, 5.0F);
            this.progressPositionPrev(this.body, sleepProgress, 0.0F, 9.0F, 0.0F, 5.0F);
            this.progressPositionPrev(this.head, sleepProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        }
        this.progressRotationPrev(this.body, holdProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, holdProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legleft, holdProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legright, holdProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.armright, holdProgress, Maths.rad(-60.0), Maths.rad(-5.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.armleft, holdProgress, Maths.rad(-60.0), Maths.rad(5.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.body, holdProgress, 0.0F, 3.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, holdProgress, 0.0F, -1.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.armleft, holdProgress, 2.0F, -2.0F, -1.0F, 5.0F);
        this.progressPositionPrev(this.armright, holdProgress, -2.0F, -2.0F, -1.0F, 5.0F);
        this.flap(this.head, 0.85F, 0.3F, false, 0.0F, 0.0F, ageInTicks, holdProgress * 0.2F);
        this.flap(this.tail, 0.85F, 0.3F, false, 0.0F, 0.0F, ageInTicks, holdProgress * 0.2F);
        this.flap(this.tail2, 0.85F, 0.3F, false, 0.0F, 0.0F, ageInTicks, holdProgress * 0.2F);
        this.flap(this.earleft, 0.85F, 0.3F, false, -1.0F, 0.0F, ageInTicks, holdProgress * 0.2F);
        this.flap(this.earright, 0.85F, 0.3F, false, -1.0F, 0.0F, ageInTicks, holdProgress * 0.2F);
        if (sleepProgress == 0.0F) {
            this.faceTarget(netHeadYaw, headPitch, 1.2F, new AdvancedModelBox[] { this.head });
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.tail, this.tail2, this.snout, this.earleft, this.earright, this.legleft, this.legright, this.armleft, this.armright, new AdvancedModelBox[0]);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.5F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}