package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneKnightEntity;
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

public class DeepOneKnightModel extends AdvancedEntityModel<DeepOneKnightEntity> implements ArmedModel {

    private final AdvancedModelBox body;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox cube_r2;

    private final AdvancedModelBox cube_r3;

    private final AdvancedModelBox cube_r4;

    private final AdvancedModelBox head;

    private final AdvancedModelBox cube_r5;

    private final AdvancedModelBox cube_r6;

    private final AdvancedModelBox cube_r7;

    private final AdvancedModelBox cube_r8;

    private final AdvancedModelBox cube_r9;

    private final AdvancedModelBox cube_r10;

    private final AdvancedModelBox cube_r11;

    private final AdvancedModelBox cube_r12;

    private final AdvancedModelBox cube_r13;

    private final AdvancedModelBox cube_r14;

    private final AdvancedModelBox cube_r15;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox lfin;

    private final AdvancedModelBox rfin;

    private final AdvancedModelBox lure;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox tail;

    private final ModelAnimator animator;

    public DeepOneKnightModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body.setTextureOffset(86, 105).addBox(-6.5F, -20.0F, -4.0F, 13.0F, 11.0F, 8.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(-9.5F, -36.0F, -6.0F, 19.0F, 18.0F, 14.0F, 0.0F, false);
        this.body.setTextureOffset(0, 82).addBox(-9.5F, -36.0F, -6.0F, 19.0F, 18.0F, 14.0F, 0.25F, false);
        this.body.setTextureOffset(50, 41).addBox(0.0F, -41.0F, 0.0F, 0.0F, 23.0F, 16.0F, 0.0F, false);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(-9.5F, -27.0F, 8.0F);
        this.body.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.0F, 0.7854F, 0.0F);
        this.cube_r1.setTextureOffset(68, 105).addBox(-2.0F, -9.0F, 0.0F, 2.0F, 18.0F, 0.0F, 0.0F, false);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(9.5F, -27.0F, 8.0F);
        this.body.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, 0.0F, -0.7854F, 0.0F);
        this.cube_r2.setTextureOffset(68, 105).addBox(0.0F, -9.0F, 0.0F, 2.0F, 18.0F, 0.0F, 0.0F, true);
        this.cube_r3 = new AdvancedModelBox(this);
        this.cube_r3.setRotationPoint(9.5F, -27.0F, -6.0F);
        this.body.addChild(this.cube_r3);
        this.setRotateAngle(this.cube_r3, 0.0F, 0.7854F, 0.0F);
        this.cube_r3.setTextureOffset(68, 105).addBox(0.0F, -9.0F, 0.0F, 2.0F, 18.0F, 0.0F, 0.0F, true);
        this.cube_r4 = new AdvancedModelBox(this);
        this.cube_r4.setRotationPoint(-9.5F, -27.0F, -6.0F);
        this.body.addChild(this.cube_r4);
        this.setRotateAngle(this.cube_r4, 0.0F, -0.7854F, 0.0F);
        this.cube_r4.setTextureOffset(68, 105).addBox(-2.0F, -9.0F, 0.0F, 2.0F, 18.0F, 0.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -36.0F, -1.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 57).addBox(-6.5F, -2.0F, -12.0F, 13.0F, 13.0F, 12.0F, 0.0F, false);
        this.head.setTextureOffset(68, 88).addBox(-6.5F, -5.0F, -12.0F, 13.0F, 3.0F, 6.0F, 0.0F, false);
        this.head.setTextureOffset(98, 96).addBox(-6.5F, 0.0F, -12.0F, 13.0F, 6.0F, 2.0F, 0.25F, false);
        this.cube_r5 = new AdvancedModelBox(this);
        this.cube_r5.setRotationPoint(0.0F, 11.0F, -12.0F);
        this.head.addChild(this.cube_r5);
        this.setRotateAngle(this.cube_r5, -0.7854F, 0.0F, 0.0F);
        this.cube_r5.setTextureOffset(29, 123).addBox(-6.5F, 0.0F, 0.0F, 13.0F, 2.0F, 0.0F, 0.0F, false);
        this.cube_r6 = new AdvancedModelBox(this);
        this.cube_r6.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.head.addChild(this.cube_r6);
        this.setRotateAngle(this.cube_r6, -0.7854F, 0.0F, 0.0F);
        this.cube_r6.setTextureOffset(29, 126).addBox(-6.5F, -2.0F, 0.0F, 13.0F, 2.0F, 0.0F, 0.0F, false);
        this.cube_r7 = new AdvancedModelBox(this);
        this.cube_r7.setRotationPoint(0.0F, -2.0F, -12.0F);
        this.head.addChild(this.cube_r7);
        this.setRotateAngle(this.cube_r7, 0.7854F, 0.0F, 0.0F);
        this.cube_r7.setTextureOffset(29, 126).addBox(-6.5F, -2.0F, 0.0F, 13.0F, 2.0F, 0.0F, 0.0F, false);
        this.cube_r8 = new AdvancedModelBox(this);
        this.cube_r8.setRotationPoint(-6.5F, 3.5F, 0.0F);
        this.head.addChild(this.cube_r8);
        this.setRotateAngle(this.cube_r8, 0.0F, 0.7854F, 0.0F);
        this.cube_r8.setTextureOffset(0, 113).addBox(-2.0F, -7.5F, 0.0F, 2.0F, 15.0F, 0.0F, 0.0F, false);
        this.cube_r9 = new AdvancedModelBox(this);
        this.cube_r9.setRotationPoint(6.5F, 3.5F, 0.0F);
        this.head.addChild(this.cube_r9);
        this.setRotateAngle(this.cube_r9, 0.0F, -0.7854F, 0.0F);
        this.cube_r9.setTextureOffset(68, 108).addBox(0.0F, -7.5F, 0.0F, 2.0F, 15.0F, 0.0F, 0.0F, true);
        this.cube_r10 = new AdvancedModelBox(this);
        this.cube_r10.setRotationPoint(6.5F, 3.5F, -12.0F);
        this.head.addChild(this.cube_r10);
        this.setRotateAngle(this.cube_r10, 0.0F, 0.7854F, 0.0F);
        this.cube_r10.setTextureOffset(68, 108).addBox(0.0F, -7.5F, 0.0F, 2.0F, 15.0F, 0.0F, 0.0F, true);
        this.cube_r11 = new AdvancedModelBox(this);
        this.cube_r11.setRotationPoint(-6.5F, 3.5F, -12.0F);
        this.head.addChild(this.cube_r11);
        this.setRotateAngle(this.cube_r11, 0.0F, -0.7854F, 0.0F);
        this.cube_r11.setTextureOffset(0, 113).addBox(-2.0F, -7.5F, 0.0F, 2.0F, 15.0F, 0.0F, 0.0F, false);
        this.cube_r12 = new AdvancedModelBox(this);
        this.cube_r12.setRotationPoint(-6.5F, 11.0F, -6.0F);
        this.head.addChild(this.cube_r12);
        this.setRotateAngle(this.cube_r12, 0.0F, 0.0F, 0.7854F);
        this.cube_r12.setTextureOffset(56, 111).addBox(0.0F, 0.0F, -6.0F, 0.0F, 2.0F, 12.0F, 0.0F, true);
        this.cube_r13 = new AdvancedModelBox(this);
        this.cube_r13.setRotationPoint(6.5F, 11.0F, -6.0F);
        this.head.addChild(this.cube_r13);
        this.setRotateAngle(this.cube_r13, 0.0F, 0.0F, -0.7854F);
        this.cube_r13.setTextureOffset(56, 111).addBox(0.0F, 0.0F, -6.0F, 0.0F, 2.0F, 12.0F, 0.0F, false);
        this.cube_r14 = new AdvancedModelBox(this);
        this.cube_r14.setRotationPoint(6.5F, -2.0F, -6.0F);
        this.head.addChild(this.cube_r14);
        this.setRotateAngle(this.cube_r14, 0.0F, 0.0F, 0.7854F);
        this.cube_r14.setTextureOffset(56, 114).addBox(0.0F, -2.0F, -6.0F, 0.0F, 2.0F, 12.0F, 0.0F, false);
        this.cube_r15 = new AdvancedModelBox(this);
        this.cube_r15.setRotationPoint(-6.5F, -2.0F, -6.0F);
        this.head.addChild(this.cube_r15);
        this.setRotateAngle(this.cube_r15, 0.0F, 0.0F, -0.7854F);
        this.cube_r15.setTextureOffset(56, 114).addBox(0.0F, -2.0F, -6.0F, 0.0F, 2.0F, 12.0F, 0.0F, true);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(0.0F, 1.75F, 2.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(30, 36).addBox(-6.5F, 0.25F, -15.0F, 13.0F, 9.0F, 12.0F, 0.25F, false);
        this.lfin = new AdvancedModelBox(this);
        this.lfin.setRotationPoint(6.5F, 0.5F, -7.0F);
        this.head.addChild(this.lfin);
        this.lfin.setTextureOffset(0, 0).addBox(0.0F, -4.5F, 0.0F, 7.0F, 11.0F, 0.0F, 0.0F, true);
        this.rfin = new AdvancedModelBox(this);
        this.rfin.setRotationPoint(-6.5F, 0.5F, -7.0F);
        this.head.addChild(this.rfin);
        this.rfin.setTextureOffset(0, 0).addBox(-7.0F, -4.5F, 0.0F, 7.0F, 11.0F, 0.0F, 0.0F, false);
        this.lure = new AdvancedModelBox(this);
        this.lure.setRotationPoint(-0.25F, -2.0F, -11.5F);
        this.head.addChild(this.lure);
        this.lure.setTextureOffset(81, 54).addBox(0.0F, -11.0F, -0.5F, 0.0F, 13.0F, 5.0F, 0.0F, false);
        this.lure.setTextureOffset(86, 19).addBox(-1.0F, -10.0F, 0.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(4.5F, -9.5F, -1.0F);
        this.body.addChild(this.lleg);
        this.lleg.setTextureOffset(58, 80).addBox(-2.0F, 0.5F, -1.0F, 4.0F, 9.0F, 4.0F, -0.001F, true);
        this.lleg.setTextureOffset(44, 0).addBox(-2.0F, 9.5F, -5.0F, 8.0F, 0.0F, 8.0F, -0.001F, true);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-4.5F, -9.5F, -1.0F);
        this.body.addChild(this.rleg);
        this.rleg.setTextureOffset(58, 80).addBox(-2.0F, 0.5F, -1.0F, 4.0F, 9.0F, 4.0F, -0.001F, false);
        this.rleg.setTextureOffset(44, 0).addBox(-6.0F, 9.5F, -5.0F, 8.0F, 0.0F, 8.0F, -0.001F, false);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(-9.0F, -27.0F, 4.5F);
        this.body.addChild(this.rarm);
        this.rarm.setTextureOffset(80, 42).addBox(-5.5F, 19.0F, -4.5F, 6.0F, 4.0F, 6.0F, 0.0F, false);
        this.rarm.setTextureOffset(104, 11).addBox(-5.5F, -2.0F, -4.5F, 6.0F, 21.0F, 6.0F, 0.25F, false);
        this.rarm.setTextureOffset(0, 42).addBox(-2.5F, 4.0F, 1.5F, 0.0F, 13.0F, 6.0F, 0.0F, true);
        this.rarm.setTextureOffset(74, 72).addBox(-8.5F, -4.0F, -5.5F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.rarm.setTextureOffset(68, 17).addBox(-5.5F, -2.0F, -4.5F, 6.0F, 21.0F, 6.0F, 0.0F, false);
        this.rarm.setTextureOffset(122, 98).addBox(-8.5F, 4.0F, -1.5F, 3.0F, 15.0F, 0.0F, 0.0F, false);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(9.0F, -27.0F, 4.5F);
        this.body.addChild(this.larm);
        this.larm.setTextureOffset(80, 42).addBox(-0.5F, 19.0F, -4.5F, 6.0F, 4.0F, 6.0F, 0.0F, true);
        this.larm.setTextureOffset(0, 42).addBox(2.5F, 4.0F, 1.5F, 0.0F, 13.0F, 6.0F, 0.0F, false);
        this.larm.setTextureOffset(74, 72).addBox(0.5F, -4.0F, -5.5F, 8.0F, 8.0F, 8.0F, 0.0F, true);
        this.larm.setTextureOffset(68, 17).addBox(-0.5F, -2.0F, -4.5F, 6.0F, 21.0F, 6.0F, 0.0F, true);
        this.larm.setTextureOffset(104, 11).addBox(-0.5F, -2.0F, -4.5F, 6.0F, 21.0F, 6.0F, 0.25F, true);
        this.larm.setTextureOffset(122, 98).addBox(5.5F, 4.0F, -1.5F, 3.0F, 15.0F, 0.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, -11.0F, 2.5F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 11).addBox(0.0F, -7.0F, -4.5F, 0.0F, 16.0F, 21.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.tail, this.head, this.jaw, this.rarm, this.larm, this.rleg, this.lleg, this.lfin, this.rfin, this.lure, this.cube_r1, new AdvancedModelBox[] { this.cube_r2, this.cube_r3, this.cube_r4, this.cube_r5, this.cube_r6, this.cube_r7, this.cube_r7, this.cube_r8, this.cube_r9, this.cube_r10, this.cube_r11, this.cube_r12, this.cube_r13, this.cube_r14, this.cube_r15 });
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(DeepOneKnightEntity.ANIMATION_THROW);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.jaw, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.lure, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(15.0), 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-15.0), (float) Math.toRadians(-15.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(45.0), (float) Math.toRadians(-30.0), (float) Math.toRadians(100.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.jaw, (float) Math.toRadians(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.lure, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-30.0), 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-15.0), (float) Math.toRadians(15.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-45.0), 0.0F, (float) Math.toRadians(80.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(DeepOneKnightEntity.ANIMATION_SCRATCH);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.jaw, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.lure, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-75.0), (float) Math.toRadians(30.0), (float) Math.toRadians(65.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.jaw, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.lure, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-10.0), 0.0F);
        this.animator.move(this.rarm, 0.0F, 0.0F, -3.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-75.0), (float) Math.toRadians(-30.0), (float) Math.toRadians(25.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.jaw, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.lure, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-10.0), 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-75.0), (float) Math.toRadians(-30.0), (float) Math.toRadians(-65.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.jaw, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.lure, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.move(this.larm, 0.0F, 0.0F, -3.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-75.0), (float) Math.toRadians(30.0), (float) Math.toRadians(-25.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(DeepOneKnightEntity.ANIMATION_BITE);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.lure, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.rleg, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.lleg, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(30.0), 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(30.0), 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.head, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, -1.0F, -2.0F);
        this.animator.move(this.body, 0.0F, 1.0F, -1.0F);
        this.animator.move(this.rleg, 0.0F, -1.5F, -1.0F);
        this.animator.move(this.lleg, 0.0F, -1.5F, -1.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(55.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(3);
        this.animator.setAnimation(DeepOneKnightEntity.ANIMATION_TRADE);
        this.animator.startKeyframe(10);
        this.animator.move(this.head, 0.0F, -1.0F, -2.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(25.0), 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-35.0), (float) Math.toRadians(-20.0), (float) Math.toRadians(-10.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-45.0), (float) Math.toRadians(20.0), (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.move(this.head, 0.0F, -1.0F, -2.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(15.0), 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.lure, (float) Math.toRadians(15.0), 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-45.0), (float) Math.toRadians(-20.0), (float) Math.toRadians(-10.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-45.0), (float) Math.toRadians(20.0), (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(20);
        this.animator.move(this.head, 0.0F, -1.0F, -2.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(15.0), 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.lure, (float) Math.toRadians(15.0), 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-45.0), (float) Math.toRadians(-20.0), (float) Math.toRadians(-10.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-45.0), (float) Math.toRadians(20.0), (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.larm, (float) Math.toRadians(-100.0), (float) Math.toRadians(-10.0), (float) Math.toRadians(-20.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
    }

    public void setupAnim(DeepOneKnightEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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
        this.walk(this.head, 0.1F, 0.05F, false, 0.0F, 0.02F, ageInTicks, 1.0F);
        this.swing(this.tail, 0.1F, 0.05F, false, -2.5F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.larm, 0.1F, 0.05F, true, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.rarm, 0.1F, 0.05F, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.lure, 0.1F, 0.2F, false, 1.0F, -0.2F, ageInTicks, 1.0F);
        this.progressRotationPrev(this.body, walkAmount, (float) Math.toRadians(15.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, walkAmount, (float) Math.toRadians(-15.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, walkAmount, (float) Math.toRadians(-15.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, walkAmount, (float) Math.toRadians(-15.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.body, swim, (float) Math.toRadians(80.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, swim, (float) Math.toRadians(-70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail, swim, (float) Math.toRadians(-50.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.body, swim, 0.0F, -6.0F, 25.0F, 1.0F);
        this.progressPositionPrev(this.rarm, swim, 0.0F, -4.0F, -2.0F, 1.0F);
        this.progressPositionPrev(this.larm, swim, 0.0F, -4.0F, -2.0F, 1.0F);
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
        this.lleg.rotationPointY = this.lleg.rotationPointY + (Math.min(0.0F, ACMath.walkValue(limbSwing, walkAmount, walkSpeed, -2.6F, 5.0F, true)) - walkAmount * 2.0F);
        this.rleg.rotationPointY = this.rleg.rotationPointY + (Math.min(0.0F, ACMath.walkValue(limbSwing, walkAmount, walkSpeed, -2.6F, 5.0F, false)) - walkAmount * 2.0F);
        this.walk(this.lleg, walkSpeed, walkDegree, false, -1.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.rleg, walkSpeed, walkDegree, true, -1.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.rarm, walkSpeed, walkDegree * 0.2F, true, -3.0F, 0.4F, limbSwing, walkAmount);
        this.walk(this.larm, walkSpeed, walkDegree * 0.2F, false, -3.0F, -0.4F, limbSwing, walkAmount);
        this.swing(this.tail, walkSpeed, walkDegree * 0.2F, false, -1.0F, -0.0F, limbSwing, walkAmount);
        this.flap(this.body, swimSpeed, swimDegree * 1.0F, true, 0.0F, 0.0F, limbSwing, swimAmount);
        this.swing(this.head, swimSpeed, swimDegree * 1.0F, false, 0.5F, 0.0F, limbSwing, swimAmount);
        this.flap(this.larm, swimSpeed, swimDegree * 2.15F, true, -1.5F, 1.0F, limbSwing, swimAmount);
        this.swing(this.larm, swimSpeed, swimDegree, true, -1.5F, 0.0F, limbSwing, swimAmount);
        this.walk(this.larm, swimSpeed, swimDegree, true, -2.0F, -0.2F, limbSwing, swimAmount);
        this.flap(this.rarm, swimSpeed, swimDegree * 2.15F, false, -3.0F, 1.0F, limbSwing, swimAmount);
        this.swing(this.rarm, swimSpeed, swimDegree, false, -1.5F, 0.0F, limbSwing, swimAmount);
        this.walk(this.rarm, swimSpeed, swimDegree, false, -4.5F, -0.2F, limbSwing, swimAmount);
        this.flap(this.tail, swimSpeed, swimDegree * 0.75F, false, -2.0F, 0.0F, limbSwing, swimAmount);
        this.walk(this.lure, swimSpeed * 0.1F, swimDegree * 0.2F, true, 2.0F, 0.3F, limbSwing, swimAmount);
        this.walk(this.rleg, swimSpeed * 1.5F, swimDegree * 1.0F, true, 2.0F, 0.0F, limbSwing, swimAmount);
        this.walk(this.lleg, swimSpeed * 1.5F, swimDegree * 1.0F, false, 2.0F, 0.0F, limbSwing, swimAmount);
        if (entity.getAnimation() != entity.getTradingAnimation()) {
            this.body.rotateAngleX += fishPitchAmount;
            this.head.rotateAngleX += headPitchAmount;
        }
        this.head.rotateAngleY += clampedYaw;
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
        this.body.translateAndRotate(poseStack);
        if (arm == HumanoidArm.RIGHT) {
            this.rarm.translateAndRotate(poseStack);
            poseStack.translate(-0.1F, 0.7F, -0.2F);
        } else {
            this.larm.translateAndRotate(poseStack);
            poseStack.translate(0.1F, 0.7F, -0.2F);
        }
    }
}