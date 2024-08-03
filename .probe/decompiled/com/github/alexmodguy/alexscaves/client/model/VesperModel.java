package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class VesperModel extends AdvancedEntityModel<VesperEntity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox torso;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox lfoot;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox cube_r2;

    private final AdvancedModelBox rfoot;

    private final AdvancedModelBox head;

    private final AdvancedModelBox cube_r3;

    private final AdvancedModelBox cube_r4;

    private final AdvancedModelBox cube_r5;

    private final AdvancedModelBox cube_r6;

    private final AdvancedModelBox cube_r7;

    private final AdvancedModelBox cube_r8;

    private final AdvancedModelBox cube_r9;

    private final AdvancedModelBox cube_r10;

    private final AdvancedModelBox nose;

    private final AdvancedModelBox cube_r11;

    private final AdvancedModelBox cube_r12;

    private final AdvancedModelBox cube_r13;

    private final AdvancedModelBox cube_r14;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox lear;

    private final AdvancedModelBox rear;

    private final AdvancedModelBox lwing;

    private final AdvancedModelBox lfinger;

    private final AdvancedModelBox lwingTip;

    private final AdvancedModelBox rwing;

    private final AdvancedModelBox rfinger;

    private final AdvancedModelBox rwingTip;

    private final AdvancedModelBox tail;

    private final ModelAnimator animator;

    public VesperModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.torso = new AdvancedModelBox(this);
        this.torso.setRotationPoint(0.0F, -10.0F, 0.5F);
        this.root.addChild(this.torso);
        this.torso.setTextureOffset(30, 44).addBox(-3.5F, -4.0F, -2.5F, 7.0F, 8.0F, 5.0F, 0.0F, false);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(2.5F, -6.3431F, -2.0F);
        this.root.addChild(this.lleg);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, 0.3431F, 0.0F);
        this.lleg.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.7854F, 0.0F, 0.0F);
        this.cube_r1.setTextureOffset(68, 32).addBox(-2.0F, 0.0F, -4.0F, 3.0F, 4.0F, 4.0F, 0.0F, true);
        this.lfoot = new AdvancedModelBox(this);
        this.lfoot.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.lleg.addChild(this.lfoot);
        this.lfoot.setTextureOffset(68, 26).addBox(-1.5F, 0.0F, -5.0F, 3.0F, 1.0F, 5.0F, 0.0F, true);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-2.5F, -6.3431F, -2.0F);
        this.root.addChild(this.rleg);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(0.0F, 0.3431F, 0.0F);
        this.rleg.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, 0.7854F, 0.0F, 0.0F);
        this.cube_r2.setTextureOffset(68, 32).addBox(-1.0F, 0.0F, -4.0F, 3.0F, 4.0F, 4.0F, 0.0F, false);
        this.rfoot = new AdvancedModelBox(this);
        this.rfoot.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.rleg.addChild(this.rfoot);
        this.rfoot.setTextureOffset(68, 26).addBox(-1.5F, 0.0F, -5.0F, 3.0F, 1.0F, 5.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -12.5F, 1.0F);
        this.root.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-6.5F, -12.5F, -10.0F, 13.0F, 13.0F, 13.0F, 0.0F, false);
        this.head.setTextureOffset(0, 44).addBox(-6.5F, -12.5F, 3.0F, 13.0F, 13.0F, 2.0F, 0.0F, false);
        this.cube_r3 = new AdvancedModelBox(this);
        this.cube_r3.setRotationPoint(-6.5F, -6.0F, -10.0F);
        this.head.addChild(this.cube_r3);
        this.setRotateAngle(this.cube_r3, 0.0F, 1.309F, 0.0F);
        this.cube_r3.setTextureOffset(32, 67).addBox(-5.0F, -6.5F, 0.0F, 5.0F, 13.0F, 0.0F, 0.0F, true);
        this.cube_r4 = new AdvancedModelBox(this);
        this.cube_r4.setRotationPoint(-6.5F, -6.0F, -3.0F);
        this.head.addChild(this.cube_r4);
        this.setRotateAngle(this.cube_r4, 0.0F, 1.309F, 0.0F);
        this.cube_r4.setTextureOffset(32, 67).addBox(-5.0F, -6.5F, 0.0F, 5.0F, 13.0F, 0.0F, 0.0F, true);
        this.cube_r5 = new AdvancedModelBox(this);
        this.cube_r5.setRotationPoint(0.0F, 0.5F, -3.0F);
        this.head.addChild(this.cube_r5);
        this.setRotateAngle(this.cube_r5, 1.309F, 0.0F, 0.0F);
        this.cube_r5.setTextureOffset(52, 17).addBox(-6.5F, 0.0F, 0.0F, 13.0F, 5.0F, 0.0F, 0.0F, false);
        this.cube_r6 = new AdvancedModelBox(this);
        this.cube_r6.setRotationPoint(0.0F, 0.5F, -10.0F);
        this.head.addChild(this.cube_r6);
        this.setRotateAngle(this.cube_r6, 1.309F, 0.0F, 0.0F);
        this.cube_r6.setTextureOffset(52, 17).addBox(-6.5F, 0.0F, 0.0F, 13.0F, 5.0F, 0.0F, 0.0F, false);
        this.cube_r7 = new AdvancedModelBox(this);
        this.cube_r7.setRotationPoint(6.5F, -6.0F, -3.0F);
        this.head.addChild(this.cube_r7);
        this.setRotateAngle(this.cube_r7, 0.0F, -1.309F, 0.0F);
        this.cube_r7.setTextureOffset(32, 67).addBox(0.0F, -6.5F, 0.0F, 5.0F, 13.0F, 0.0F, 0.0F, false);
        this.cube_r8 = new AdvancedModelBox(this);
        this.cube_r8.setRotationPoint(6.5F, -6.0F, -10.0F);
        this.head.addChild(this.cube_r8);
        this.setRotateAngle(this.cube_r8, 0.0F, -1.309F, 0.0F);
        this.cube_r8.setTextureOffset(32, 67).addBox(0.0F, -6.5F, 0.0F, 5.0F, 13.0F, 0.0F, 0.0F, false);
        this.cube_r9 = new AdvancedModelBox(this);
        this.cube_r9.setRotationPoint(0.0F, -12.5F, -10.0F);
        this.head.addChild(this.cube_r9);
        this.setRotateAngle(this.cube_r9, -1.309F, 0.0F, 0.0F);
        this.cube_r9.setTextureOffset(26, 62).addBox(-6.5F, -5.0F, 0.0F, 13.0F, 5.0F, 0.0F, 0.0F, false);
        this.cube_r10 = new AdvancedModelBox(this);
        this.cube_r10.setRotationPoint(0.0F, -12.5F, -3.0F);
        this.head.addChild(this.cube_r10);
        this.setRotateAngle(this.cube_r10, -1.309F, 0.0F, 0.0F);
        this.cube_r10.setTextureOffset(26, 62).addBox(-6.5F, -5.0F, 0.0F, 13.0F, 5.0F, 0.0F, 0.0F, false);
        this.nose = new AdvancedModelBox(this);
        this.nose.setRotationPoint(0.0F, -4.0F, -9.5F);
        this.head.addChild(this.nose);
        this.nose.setTextureOffset(49, 52).addBox(-3.5F, -2.75F, -2.5F, 7.0F, 5.0F, 5.0F, 0.0F, false);
        this.nose.setTextureOffset(66, 62).addBox(-3.5F, -13.75F, -2.5F, 7.0F, 11.0F, 0.0F, 0.0F, false);
        this.nose.setTextureOffset(66, 8).addBox(-2.5F, 2.25F, -2.24F, 5.0F, 2.0F, 5.0F, 0.0F, false);
        this.cube_r11 = new AdvancedModelBox(this);
        this.cube_r11.setRotationPoint(-7.1955F, 4.25F, -4.0307F);
        this.nose.addChild(this.cube_r11);
        this.setRotateAngle(this.cube_r11, 0.0F, -0.3927F, 0.0F);
        this.cube_r11.setTextureOffset(57, 22).addBox(0.0F, -2.0F, 0.0F, 6.0F, 4.0F, 0.0F, 0.0F, true);
        this.cube_r12 = new AdvancedModelBox(this);
        this.cube_r12.setRotationPoint(7.1955F, 4.25F, -4.0307F);
        this.nose.addChild(this.cube_r12);
        this.setRotateAngle(this.cube_r12, 0.0F, 0.3927F, 0.0F);
        this.cube_r12.setTextureOffset(57, 22).addBox(-6.0F, -2.0F, 0.0F, 6.0F, 4.0F, 0.0F, 0.0F, false);
        this.cube_r13 = new AdvancedModelBox(this);
        this.cube_r13.setRotationPoint(3.5F, -5.75F, -2.5F);
        this.nose.addChild(this.cube_r13);
        this.setRotateAngle(this.cube_r13, 0.0F, 0.3927F, 0.0F);
        this.cube_r13.setTextureOffset(0, 64).addBox(0.0F, -8.0F, 0.0F, 7.0F, 16.0F, 0.0F, 0.0F, false);
        this.cube_r14 = new AdvancedModelBox(this);
        this.cube_r14.setRotationPoint(-3.5F, -5.75F, -2.5F);
        this.nose.addChild(this.cube_r14);
        this.setRotateAngle(this.cube_r14, 0.0F, -0.3927F, 0.0F);
        this.cube_r14.setTextureOffset(0, 64).addBox(-7.0F, -8.0F, 0.0F, 7.0F, 16.0F, 0.0F, 0.0F, true);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(0.0F, 2.25F, -0.245F);
        this.nose.addChild(this.jaw);
        this.jaw.setTextureOffset(14, 67).addBox(-3.0F, -1.0F, -2.005F, 6.0F, 3.0F, 3.0F, 0.0F, false);
        this.lear = new AdvancedModelBox(this);
        this.lear.setRotationPoint(5.5F, -9.5F, -10.0F);
        this.head.addChild(this.lear);
        this.lear.setTextureOffset(52, 62).addBox(-3.0F, -13.0F, -1.0F, 6.0F, 15.0F, 1.0F, 0.0F, true);
        this.rear = new AdvancedModelBox(this);
        this.rear.setRotationPoint(-5.5F, -9.5F, -10.0F);
        this.head.addChild(this.rear);
        this.rear.setTextureOffset(52, 62).addBox(-3.0F, -13.0F, -1.0F, 6.0F, 15.0F, 1.0F, 0.0F, false);
        this.lwing = new AdvancedModelBox(this);
        this.lwing.setRotationPoint(3.0F, -10.75F, 0.5F);
        this.root.addChild(this.lwing);
        this.lwing.setTextureOffset(0, 26).addBox(-2.5F, 0.75F, 0.0F, 25.0F, 18.0F, 0.0F, 0.0F, false);
        this.lwing.setTextureOffset(39, 4).addBox(0.5F, -1.25F, -1.0F, 17.0F, 2.0F, 2.0F, 0.0F, false);
        this.lfinger = new AdvancedModelBox(this);
        this.lfinger.setRotationPoint(16.5F, -0.75F, 0.0F);
        this.lwing.addChild(this.lfinger);
        this.lfinger.setTextureOffset(42, 67).addBox(-1.0F, -4.5F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);
        this.lfinger.setTextureOffset(39, 8).addBox(-1.0F, -7.5F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        this.lwingTip = new AdvancedModelBox(this);
        this.lwingTip.setRotationPoint(17.5F, -0.25F, 0.005F);
        this.lwing.addChild(this.lwingTip);
        this.lwingTip.setTextureOffset(39, 0).addBox(0.0F, -1.0F, -1.005F, 17.0F, 2.0F, 2.0F, 0.0F, false);
        this.lwingTip.setTextureOffset(0, 80).addBox(-11.0F, -1.0F, 0.005F, 43.0F, 28.0F, 0.0F, 0.0F, false);
        this.lwingTip.setTextureOffset(0, 80).addBox(-11.0F, -1.0F, -0.015F, 43.0F, 28.0F, 0.0F, 0.0F, false);
        this.rwing = new AdvancedModelBox(this);
        this.rwing.setRotationPoint(-3.0F, -10.75F, 0.5F);
        this.root.addChild(this.rwing);
        this.rwing.setTextureOffset(0, 26).addBox(-22.5F, 0.75F, 0.0F, 25.0F, 18.0F, 0.0F, 0.0F, true);
        this.rwing.setTextureOffset(39, 4).addBox(-17.5F, -1.25F, -1.0F, 17.0F, 2.0F, 2.0F, 0.0F, true);
        this.rfinger = new AdvancedModelBox(this);
        this.rfinger.setRotationPoint(-16.5F, -0.75F, 0.0F);
        this.rwing.addChild(this.rfinger);
        this.rfinger.setTextureOffset(42, 67).addBox(-1.0F, -4.5F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, true);
        this.rfinger.setTextureOffset(39, 8).addBox(-1.0F, -7.5F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, true);
        this.rwingTip = new AdvancedModelBox(this);
        this.rwingTip.setRotationPoint(-17.5F, -0.25F, 0.005F);
        this.rwing.addChild(this.rwingTip);
        this.rwingTip.setTextureOffset(39, 0).addBox(-17.0F, -1.0F, -1.005F, 17.0F, 2.0F, 2.0F, 0.0F, true);
        this.rwingTip.setTextureOffset(0, 80).addBox(-32.0F, -1.0F, 0.005F, 43.0F, 28.0F, 0.0F, 0.0F, true);
        this.rwingTip.setTextureOffset(0, 80).addBox(-32.0F, -1.0F, -0.015F, 43.0F, 28.0F, 0.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, -6.0F, 3.0F);
        this.root.addChild(this.tail);
        this.tail.setTextureOffset(43, 8).addBox(-3.5F, 0.0F, 0.0F, 7.0F, 0.0F, 9.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.head, this.nose, this.jaw, this.tail, this.torso, this.lleg, this.rleg, this.lfoot, this.rfoot, this.lear, this.rear, new AdvancedModelBox[] { this.lwing, this.lwingTip, this.lfinger, this.rwing, this.rwingTip, this.rfinger, this.cube_r1, this.cube_r2, this.cube_r3, this.cube_r4, this.cube_r5, this.cube_r6, this.cube_r7, this.cube_r8, this.cube_r9, this.cube_r10, this.cube_r11, this.cube_r12, this.cube_r13, this.cube_r14 });
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(VesperEntity.ANIMATION_BITE);
        this.animator.startKeyframe(7);
        this.animator.move(this.head, 0.0F, 0.0F, 1.0F);
        this.animator.move(this.jaw, 0.0F, 1.0F, 1.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.nose, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(65.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.move(this.head, 0.0F, 0.0F, -1.0F);
        this.animator.move(this.jaw, 0.0F, 1.0F, 1.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(VesperEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float capturedProgress = entity.getCapturedProgress(partialTick);
        float sleepProgress = entity.getSleepProgress(partialTick);
        float foldWingsProgress = Math.max(capturedProgress, sleepProgress);
        float flyProgress = entity.getFlyProgress(partialTick);
        float groundProgress = (1.0F - Math.max(flyProgress, sleepProgress)) * (1.0F - capturedProgress);
        float groundMove = groundProgress * limbSwingAmount;
        float walkSpeed = 1.0F;
        float walkDegree = 1.0F;
        float rollAmount = entity.getFlightRoll(partialTick) / (180.0F / (float) Math.PI) * flyProgress;
        float flightPitchAmount = entity.getFlightPitch(partialTick) / (180.0F / (float) Math.PI) * flyProgress;
        this.progressPositionPrev(this.root, sleepProgress, 0.0F, -24.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.head, sleepProgress, 0.0F, -2.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.rwing, sleepProgress, 0.0F, -3.0F, 1.0F, 1.0F);
        this.progressPositionPrev(this.lwing, sleepProgress, 0.0F, -3.0F, 1.0F, 1.0F);
        this.progressPositionPrev(this.rleg, sleepProgress, 0.0F, -1.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.lleg, sleepProgress, 0.0F, -1.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rwing, foldWingsProgress, (float) Math.toRadians(-35.0), (float) Math.toRadians(-30.0), (float) Math.toRadians(35.0), 1.0F);
        this.progressRotationPrev(this.rwingTip, foldWingsProgress, 0.0F, (float) Math.toRadians(-135.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.lwing, foldWingsProgress, (float) Math.toRadians(-35.0), (float) Math.toRadians(30.0), (float) Math.toRadians(-35.0), 1.0F);
        this.progressRotationPrev(this.lwingTip, foldWingsProgress, 0.0F, (float) Math.toRadians(145.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, sleepProgress, 0.0F, (float) Math.toRadians(-45.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, sleepProgress, 0.0F, (float) Math.toRadians(45.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.head, sleepProgress, (float) Math.toRadians(-65.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail, sleepProgress, (float) Math.toRadians(-45.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.root, sleepProgress, 0.0F, 0.0F, (float) Math.toRadians((double) (180 * (entity.m_19879_() % 2 == 0 ? -1 : 1))), 1.0F);
        this.progressPositionPrev(this.head, groundProgress, 0.0F, 3.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.rwing, groundProgress, 0.0F, -1.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.lwing, groundProgress, 0.0F, -1.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.lleg, groundProgress, 0.0F, 0.0F, 3.0F, 1.0F);
        this.progressPositionPrev(this.rleg, groundProgress, 0.0F, 0.0F, 3.0F, 1.0F);
        this.progressRotationPrev(this.rwing, groundProgress, (float) Math.toRadians(90.0), 0.0F, (float) Math.toRadians(-40.0), 1.0F);
        this.progressRotationPrev(this.rwingTip, groundProgress, 0.0F, (float) Math.toRadians(30.0), (float) Math.toRadians(-70.0), 1.0F);
        this.progressRotationPrev(this.rfinger, groundProgress, 0.0F, (float) Math.toRadians(-50.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.lwing, groundProgress, (float) Math.toRadians(90.0), 0.0F, (float) Math.toRadians(40.0), 1.0F);
        this.progressRotationPrev(this.lwingTip, groundProgress, 0.0F, (float) Math.toRadians(-30.0), (float) Math.toRadians(70.0), 1.0F);
        this.progressRotationPrev(this.lfinger, groundProgress, 0.0F, (float) Math.toRadians(50.0), 0.0F, 1.0F);
        this.progressPositionPrev(this.root, flyProgress, 0.0F, -7.0F, 7.0F, 1.0F);
        this.progressPositionPrev(this.head, flyProgress, 0.0F, -2.0F, -4.0F, 1.0F);
        this.progressPositionPrev(this.lleg, flyProgress, 0.0F, -1.0F, 2.0F, 1.0F);
        this.progressPositionPrev(this.rleg, flyProgress, 0.0F, -1.0F, 2.0F, 1.0F);
        this.progressRotationPrev(this.root, flyProgress, (float) Math.toRadians(90.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, flyProgress, (float) Math.toRadians(-70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail, flyProgress, (float) Math.toRadians(-90.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lfoot, flyProgress, (float) Math.toRadians(70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rfoot, flyProgress, (float) Math.toRadians(70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rwing, flyProgress, 0.0F, (float) Math.toRadians(30.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.rwingTip, flyProgress, 0.0F, (float) Math.toRadians(-40.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.lwing, flyProgress, 0.0F, (float) Math.toRadians(-30.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.lwingTip, flyProgress, 0.0F, (float) Math.toRadians(40.0), 0.0F, 1.0F);
        this.progressPositionPrev(this.root, capturedProgress, 0.0F, -1.0F, -7.0F, 1.0F);
        this.progressRotationPrev(this.root, capturedProgress, (float) Math.toRadians(-40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, capturedProgress, (float) Math.toRadians(-40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, capturedProgress, (float) Math.toRadians(-30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, capturedProgress, (float) Math.toRadians(-30.0), 0.0F, 0.0F, 1.0F);
        this.lleg.setScale(1.0F, 1.0F + sleepProgress * 0.5F, 1.0F);
        this.lleg.scaleChildren = true;
        this.rleg.setScale(1.0F, 1.0F + sleepProgress * 0.5F, 1.0F);
        this.rleg.scaleChildren = true;
        float hopForwards = -Math.max(0.0F, ACMath.walkValue(limbSwing, groundMove, walkSpeed, -4.0F, 8.0F, false));
        this.walk(this.head, 0.2F, 0.03F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.nose, 0.9F, 0.05F, false, 1.0F, 0.02F, ageInTicks, 1.0F);
        this.flap(this.lear, 0.1F, 0.05F, false, 4.0F, 0.05F, ageInTicks, 1.0F);
        this.flap(this.rear, 0.1F, 0.05F, true, 4.0F, 0.05F, ageInTicks, 1.0F);
        this.bob(this.head, 0.1F, 0.5F, false, ageInTicks, 1.0F);
        this.swing(this.lwing, walkSpeed, walkDegree, false, 0.0F, 0.2F, limbSwing, groundMove);
        this.flap(this.lfinger, walkSpeed, walkDegree, false, 0.0F, 0.2F, limbSwing, groundMove);
        this.swing(this.rwing, walkSpeed, walkDegree, true, 0.0F, 0.2F, limbSwing, groundMove);
        this.flap(this.rfinger, walkSpeed, walkDegree, true, 0.0F, 0.2F, limbSwing, groundMove);
        this.walk(this.rleg, walkSpeed, walkDegree, true, -1.5F, 0.4F, limbSwing, groundMove);
        this.walk(this.rfoot, walkSpeed, walkDegree, false, -3.5F, 0.4F, limbSwing, groundMove);
        this.walk(this.lleg, walkSpeed, walkDegree, true, -1.5F, 0.4F, limbSwing, groundMove);
        this.walk(this.lfoot, walkSpeed, walkDegree, false, -3.5F, 0.4F, limbSwing, groundMove);
        this.root.rotationPointZ += hopForwards * 1.5F;
        this.root.rotationPointY += hopForwards;
        this.walk(this.lleg, 0.3F, 0.2F, false, 2.0F, -0.1F, ageInTicks, flyProgress);
        this.walk(this.rleg, 0.3F, 0.2F, false, 2.0F, -0.1F, ageInTicks, flyProgress);
        this.walk(this.head, 0.3F, 0.1F, false, 1.0F, 0.0F, ageInTicks, flyProgress);
        this.swing(this.rwing, 0.5F, 1.0F, false, 1.0F, -0.5F, ageInTicks, flyProgress);
        this.swing(this.lwing, 0.5F, 1.0F, true, 1.0F, -0.5F, ageInTicks, flyProgress);
        this.swing(this.rwingTip, 0.5F, 0.4F, false, 0.0F, 0.0F, ageInTicks, flyProgress);
        this.swing(this.lwingTip, 0.5F, 0.4F, true, 0.0F, 0.0F, ageInTicks, flyProgress);
        this.bob(this.root, 0.5F, 4.0F, false, ageInTicks, flyProgress);
        this.bob(this.head, 0.5F, -1.0F, false, ageInTicks, flyProgress);
        this.walk(this.lleg, 0.3F, 0.5F, true, 0.0F, -0.1F, ageInTicks, capturedProgress);
        this.walk(this.rleg, 0.3F, 0.5F, false, 0.0F, -0.1F, ageInTicks, capturedProgress);
        this.swing(this.head, 0.3F, 0.9F, false, 2.0F, 0.0F, ageInTicks, capturedProgress);
        float yawAmount = netHeadYaw / (180.0F / (float) Math.PI);
        float pitchAmount = headPitch / (180.0F / (float) Math.PI);
        this.head.rotateAngleX += pitchAmount;
        this.head.rotateAngleZ += yawAmount * flyProgress;
        this.head.rotateAngleY += yawAmount * (1.0F - flyProgress);
        this.root.rotateAngleX += flightPitchAmount;
        this.root.rotateAngleZ += rollAmount;
    }
}