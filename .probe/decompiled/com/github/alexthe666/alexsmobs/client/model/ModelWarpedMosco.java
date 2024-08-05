package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityWarpedMosco;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelWarpedMosco extends AdvancedEntityModel<EntityWarpedMosco> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox back;

    private final AdvancedModelBox legfront_left;

    private final AdvancedModelBox kneefront_left;

    private final AdvancedModelBox legfront_right;

    private final AdvancedModelBox kneefront_right;

    private final AdvancedModelBox legback_left;

    private final AdvancedModelBox kneeback_left;

    private final AdvancedModelBox legback_right;

    private final AdvancedModelBox kneeback_right;

    private final AdvancedModelBox chest;

    private final AdvancedModelBox wingtop_left;

    private final AdvancedModelBox wingtop_right;

    private final AdvancedModelBox wingbottom_left;

    private final AdvancedModelBox wingbottom_left_r1;

    private final AdvancedModelBox wingbottom_right;

    private final AdvancedModelBox wingbottom_right_r1;

    private final AdvancedModelBox shoulder_left;

    private final AdvancedModelBox shoulderspikes_left;

    private final AdvancedModelBox arm_left;

    private final AdvancedModelBox hand_left;

    private final AdvancedModelBox shoulder_right;

    private final AdvancedModelBox shoulderspikes_right;

    private final AdvancedModelBox arm_right;

    private final AdvancedModelBox hand_right;

    private final AdvancedModelBox head;

    private final AdvancedModelBox antenna_left;

    private final AdvancedModelBox antenna_right;

    private final AdvancedModelBox proboscis;

    private final AdvancedModelBox proboscis_r1;

    private ModelAnimator animator;

    public ModelWarpedMosco() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -24.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 31).addBox(-8.0F, -10.0F, -7.0F, 16.0F, 14.0F, 14.0F, 0.0F, false);
        this.back = new AdvancedModelBox(this, "back");
        this.back.setPos(0.0F, -4.0F, 7.0F);
        this.body.addChild(this.back);
        this.setRotationAngle(this.back, -0.4363F, 0.0F, 0.0F);
        this.back.setTextureOffset(44, 44).addBox(-5.0F, -1.0F, 0.0F, 10.0F, 10.0F, 17.0F, 0.0F, false);
        this.legfront_left = new AdvancedModelBox(this, "legfront_left");
        this.legfront_left.setPos(5.5F, 2.0F, -4.5F);
        this.body.addChild(this.legfront_left);
        this.setRotationAngle(this.legfront_left, 0.0F, -0.5236F, 0.0F);
        this.legfront_left.setTextureOffset(72, 86).addBox(-3.5F, -2.0F, -3.5F, 7.0F, 12.0F, 7.0F, 0.0F, false);
        this.kneefront_left = new AdvancedModelBox(this, "kneefront_left");
        this.kneefront_left.setPos(0.0F, 10.0F, 0.0F);
        this.legfront_left.addChild(this.kneefront_left);
        this.kneefront_left.setTextureOffset(101, 81).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 12.0F, 5.0F, 0.0F, false);
        this.legfront_right = new AdvancedModelBox(this, "legfront_right");
        this.legfront_right.setPos(-5.5F, 2.0F, -4.5F);
        this.body.addChild(this.legfront_right);
        this.setRotationAngle(this.legfront_right, 0.0F, 0.5236F, 0.0F);
        this.legfront_right.setTextureOffset(72, 86).addBox(-3.5F, -2.0F, -3.5F, 7.0F, 12.0F, 7.0F, 0.0F, true);
        this.kneefront_right = new AdvancedModelBox(this, "kneefront_right");
        this.kneefront_right.setPos(0.0F, 10.0F, 0.0F);
        this.legfront_right.addChild(this.kneefront_right);
        this.kneefront_right.setTextureOffset(101, 81).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 12.0F, 5.0F, 0.0F, true);
        this.legback_left = new AdvancedModelBox(this, "legback_left");
        this.legback_left.setPos(5.5F, 2.0F, 5.5F);
        this.body.addChild(this.legback_left);
        this.setRotationAngle(this.legback_left, 0.0F, 0.6981F, 0.0F);
        this.legback_left.setTextureOffset(72, 86).addBox(-3.5F, -2.0F, -3.5F, 7.0F, 12.0F, 7.0F, 0.0F, false);
        this.kneeback_left = new AdvancedModelBox(this, "kneeback_left");
        this.kneeback_left.setPos(0.0F, 10.0F, 0.0F);
        this.legback_left.addChild(this.kneeback_left);
        this.kneeback_left.setTextureOffset(101, 81).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 12.0F, 5.0F, 0.0F, false);
        this.legback_right = new AdvancedModelBox(this, "legback_right");
        this.legback_right.setPos(-5.5F, 2.0F, 5.5F);
        this.body.addChild(this.legback_right);
        this.setRotationAngle(this.legback_right, 0.0F, -0.6981F, 0.0F);
        this.legback_right.setTextureOffset(72, 86).addBox(-3.5F, -2.0F, -3.5F, 7.0F, 12.0F, 7.0F, 0.0F, true);
        this.kneeback_right = new AdvancedModelBox(this, "kneeback_right");
        this.kneeback_right.setPos(0.0F, 10.0F, 0.0F);
        this.legback_right.addChild(this.kneeback_right);
        this.kneeback_right.setTextureOffset(101, 81).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 12.0F, 5.0F, 0.0F, true);
        this.chest = new AdvancedModelBox(this, "chest");
        this.chest.setPos(0.0F, -10.0F, 0.0F);
        this.body.addChild(this.chest);
        this.chest.setTextureOffset(0, 0).addBox(-12.0F, -14.0F, -8.0F, 24.0F, 14.0F, 16.0F, 0.0F, false);
        this.wingtop_left = new AdvancedModelBox(this, "wingtop_left");
        this.wingtop_left.setPos(0.5F, -7.0F, 8.0F);
        this.chest.addChild(this.wingtop_left);
        this.setRotationAngle(this.wingtop_left, 0.0F, -0.0873F, -0.2182F);
        this.wingtop_left.setTextureOffset(24, 109).addBox(0.0F, -11.0F, 0.0F, 33.0F, 19.0F, 0.0F, 0.0F, false);
        this.wingtop_right = new AdvancedModelBox(this, "wingtop_right");
        this.wingtop_right.setPos(-0.5F, -7.0F, 8.0F);
        this.chest.addChild(this.wingtop_right);
        this.setRotationAngle(this.wingtop_right, 0.0F, 0.0873F, 0.2182F);
        this.wingtop_right.setTextureOffset(24, 109).addBox(-33.0F, -11.0F, 0.0F, 33.0F, 19.0F, 0.0F, 0.0F, true);
        this.wingbottom_left = new AdvancedModelBox(this, "wingbottom_left");
        this.wingbottom_left.setPos(0.5F, -6.0F, 8.0F);
        this.chest.addChild(this.wingbottom_left);
        this.setRotationAngle(this.wingbottom_left, 0.0F, -0.0873F, -0.2182F);
        this.wingbottom_left_r1 = new AdvancedModelBox(this, "wingbottom_left_r1");
        this.wingbottom_left_r1.setPos(0.0F, 0.0F, 0.0F);
        this.wingbottom_left.addChild(this.wingbottom_left_r1);
        this.setRotationAngle(this.wingbottom_left_r1, 0.0436F, 0.0F, 0.829F);
        this.wingbottom_left_r1.setTextureOffset(24, 109).addBox(0.0F, -11.0F, 0.0F, 33.0F, 19.0F, 0.0F, 0.0F, false);
        this.wingbottom_right = new AdvancedModelBox(this, "wingbottom_right");
        this.wingbottom_right.setPos(-0.5F, -6.0F, 8.0F);
        this.chest.addChild(this.wingbottom_right);
        this.setRotationAngle(this.wingbottom_right, 0.0F, 0.0873F, 0.2182F);
        this.wingbottom_right_r1 = new AdvancedModelBox(this, "wingbottom_right_r1");
        this.wingbottom_right_r1.setPos(0.0F, 0.0F, 0.0F);
        this.wingbottom_right.addChild(this.wingbottom_right_r1);
        this.setRotationAngle(this.wingbottom_right_r1, 0.0436F, 0.0F, -0.829F);
        this.wingbottom_right_r1.setTextureOffset(24, 109).addBox(-33.0F, -11.0F, 0.0F, 33.0F, 19.0F, 0.0F, 0.0F, true);
        this.shoulder_left = new AdvancedModelBox(this, "shoulder_left");
        this.shoulder_left.setPos(16.0F, -11.0F, 0.0F);
        this.chest.addChild(this.shoulder_left);
        this.shoulder_left.setTextureOffset(0, 60).addBox(-4.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, 0.0F, false);
        this.shoulderspikes_left = new AdvancedModelBox(this, "shoulderspikes_left");
        this.shoulderspikes_left.setPos(6.5F, -4.0F, 0.0F);
        this.shoulder_left.addChild(this.shoulderspikes_left);
        this.shoulderspikes_left.setTextureOffset(101, 101).addBox(-6.5F, -8.0F, 0.0F, 13.0F, 16.0F, 0.0F, 0.0F, false);
        this.arm_left = new AdvancedModelBox(this, "arm_left");
        this.arm_left.setPos(1.1F, 6.0F, 2.0F);
        this.shoulder_left.addChild(this.arm_left);
        this.arm_left.setTextureOffset(71, 21).addBox(-5.0F, 0.0F, -7.0F, 10.0F, 11.0F, 10.0F, 0.0F, false);
        this.hand_left = new AdvancedModelBox(this, "hand_left");
        this.hand_left.setPos(0.0F, 11.0F, 0.0F);
        this.arm_left.addChild(this.hand_left);
        this.hand_left.setTextureOffset(0, 85).addBox(-4.0F, 0.0F, -7.0F, 8.0F, 12.0F, 10.0F, 0.0F, false);
        this.shoulder_right = new AdvancedModelBox(this, "shoulder_right");
        this.shoulder_right.setPos(-16.0F, -11.0F, 0.0F);
        this.chest.addChild(this.shoulder_right);
        this.shoulder_right.setTextureOffset(0, 60).addBox(-8.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, 0.0F, true);
        this.shoulderspikes_right = new AdvancedModelBox(this, "shoulderspikes_right");
        this.shoulderspikes_right.setPos(-6.5F, -4.0F, 0.0F);
        this.shoulder_right.addChild(this.shoulderspikes_right);
        this.shoulderspikes_right.setTextureOffset(101, 101).addBox(-6.5F, -8.0F, 0.0F, 13.0F, 16.0F, 0.0F, 0.0F, true);
        this.arm_right = new AdvancedModelBox(this, "arm_right");
        this.arm_right.setPos(-1.2F, 6.0F, 2.0F);
        this.shoulder_right.addChild(this.arm_right);
        this.arm_right.setTextureOffset(71, 21).addBox(-5.0F, 0.0F, -7.0F, 10.0F, 11.0F, 10.0F, 0.0F, true);
        this.hand_right = new AdvancedModelBox(this, "hand_right");
        this.hand_right.setPos(0.0F, 11.0F, 0.0F);
        this.arm_right.addChild(this.hand_right);
        this.hand_right.setTextureOffset(0, 85).addBox(-4.0F, 0.0F, -7.0F, 8.0F, 12.0F, 10.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -14.0F, -1.0F);
        this.chest.addChild(this.head);
        this.head.setTextureOffset(82, 43).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 5.0F, 10.0F, 0.0F, false);
        this.antenna_left = new AdvancedModelBox(this, "antenna_left");
        this.antenna_left.setPos(2.0F, -5.0F, -5.0F);
        this.head.addChild(this.antenna_left);
        this.setRotationAngle(this.antenna_left, -0.829F, 0.0F, 0.0F);
        this.antenna_left.setTextureOffset(102, 59).addBox(-1.0F, -20.0F, 0.0F, 6.0F, 20.0F, 0.0F, 0.0F, false);
        this.antenna_right = new AdvancedModelBox(this, "antenna_right");
        this.antenna_right.setPos(-2.0F, -5.0F, -5.0F);
        this.head.addChild(this.antenna_right);
        this.setRotationAngle(this.antenna_right, -0.829F, 0.0F, 0.0F);
        this.antenna_right.setTextureOffset(102, 59).addBox(-5.0F, -20.0F, 0.0F, 6.0F, 20.0F, 0.0F, 0.0F, true);
        this.proboscis = new AdvancedModelBox(this, "proboscis");
        this.proboscis.setPos(0.0F, -1.0F, -6.0F);
        this.head.addChild(this.proboscis);
        this.proboscis_r1 = new AdvancedModelBox(this, "proboscis_r1");
        this.proboscis_r1.setPos(0.0F, -1.0F, 1.0F);
        this.proboscis.addChild(this.proboscis_r1);
        this.setRotationAngle(this.proboscis_r1, 0.3054F, 0.0F, 0.0F);
        this.proboscis_r1.setTextureOffset(37, 86).addBox(-1.0F, 0.0F, -15.0F, 2.0F, 1.0F, 15.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityWarpedMosco.ANIMATION_PUNCH_R);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.chest, 0.0F, Maths.rad(40.0), 0.0F);
        this.animator.rotate(this.shoulder_right, Maths.rad(40.0), Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-80.0), 0.0F, 0.0F);
        this.animator.rotate(this.hand_right, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.shoulder_left, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.chest, 0.0F, Maths.rad(-40.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(30.0), 0.0F);
        this.animator.rotate(this.shoulder_right, Maths.rad(-20.0), Maths.rad(20.0), Maths.rad(20.0));
        this.animator.rotate(this.arm_right, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.hand_right, Maths.rad(-30.0), Maths.rad(-30.0), 0.0F);
        this.animator.rotate(this.shoulder_left, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityWarpedMosco.ANIMATION_PUNCH_L);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.chest, 0.0F, Maths.rad(-40.0), 0.0F);
        this.animator.rotate(this.shoulder_left, Maths.rad(40.0), Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-80.0), 0.0F, 0.0F);
        this.animator.rotate(this.hand_right, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.shoulder_right, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.chest, 0.0F, Maths.rad(40.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(-30.0), 0.0F);
        this.animator.rotate(this.shoulder_left, Maths.rad(-20.0), Maths.rad(-20.0), Maths.rad(-20.0));
        this.animator.rotate(this.arm_left, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.hand_left, Maths.rad(-30.0), Maths.rad(30.0), 0.0F);
        this.animator.rotate(this.shoulder_right, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityWarpedMosco.ANIMATION_SLAM);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.chest, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.shoulder_left, Maths.rad(-160.0), Maths.rad(20.0), Maths.rad(-10.0));
        this.animator.rotate(this.shoulder_right, Maths.rad(-160.0), Maths.rad(-20.0), Maths.rad(10.0));
        this.animator.rotate(this.arm_right, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, Maths.rad(60.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(40.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-60.0), 0.0F, 0.0F);
        this.animator.rotate(this.legfront_left, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.legfront_right, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.legback_left, Maths.rad(-40.0), 0.0F, Maths.rad(-30.0));
        this.animator.rotate(this.legback_right, Maths.rad(-40.0), 0.0F, Maths.rad(30.0));
        this.animator.rotate(this.shoulder_left, Maths.rad(-100.0), Maths.rad(20.0), Maths.rad(-10.0));
        this.animator.rotate(this.shoulder_right, Maths.rad(-100.0), Maths.rad(-20.0), Maths.rad(10.0));
        this.animator.rotate(this.arm_right, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.move(this.legfront_left, 0.0F, -3.0F, 0.0F);
        this.animator.move(this.legfront_right, 0.0F, -3.0F, 0.0F);
        this.animator.move(this.legback_left, 0.0F, 6.0F, 0.0F);
        this.animator.move(this.legback_right, 0.0F, 6.0F, 0.0F);
        this.animator.move(this.shoulder_left, 0.0F, -3.0F, 0.0F);
        this.animator.move(this.shoulder_right, 0.0F, -3.0F, 0.0F);
        this.animator.move(this.body, 0.0F, 0.0F, 2.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(EntityWarpedMosco.ANIMATION_SUCK);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.chest, Maths.rad(60.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(40.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-60.0), 0.0F, 0.0F);
        this.animator.rotate(this.legfront_left, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.legfront_right, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.legback_left, Maths.rad(-40.0), 0.0F, Maths.rad(-30.0));
        this.animator.rotate(this.legback_right, Maths.rad(-40.0), 0.0F, Maths.rad(30.0));
        this.animator.rotate(this.shoulder_left, Maths.rad(-100.0), Maths.rad(20.0), Maths.rad(-10.0));
        this.animator.rotate(this.shoulder_right, Maths.rad(-100.0), Maths.rad(-20.0), Maths.rad(10.0));
        this.animator.rotate(this.arm_right, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.move(this.legfront_left, 0.0F, -3.0F, 0.0F);
        this.animator.move(this.legfront_right, 0.0F, -3.0F, 0.0F);
        this.animator.move(this.legback_left, 0.0F, 6.0F, 0.0F);
        this.animator.move(this.legback_right, 0.0F, 6.0F, 0.0F);
        this.animator.move(this.shoulder_left, 0.0F, -3.0F, 0.0F);
        this.animator.move(this.shoulder_right, 0.0F, -3.0F, 0.0F);
        this.animator.move(this.body, 0.0F, 0.0F, 2.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(15);
        this.suckPose();
        this.animator.endKeyframe();
        for (int i = 0; i < 5; i++) {
            this.animator.startKeyframe(5);
            this.suckPose();
            this.animator.move(this.proboscis, 0.0F, i % 2 == 0 ? -1.0F : 0.0F, i % 2 == 0 ? 4.0F : 0.0F);
            this.animator.endKeyframe();
        }
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(EntityWarpedMosco.ANIMATION_SPIT);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.back, this.legfront_left, this.kneefront_left, this.legfront_right, this.kneefront_right, this.legback_left, this.kneeback_left, this.legback_right, this.kneeback_right, this.chest, new AdvancedModelBox[] { this.wingtop_left, this.wingtop_right, this.wingbottom_left, this.wingbottom_left_r1, this.wingbottom_right, this.wingbottom_right_r1, this.shoulder_left, this.shoulderspikes_left, this.arm_left, this.hand_left, this.shoulder_right, this.shoulderspikes_right, this.arm_right, this.hand_right, this.head, this.antenna_left, this.antenna_right, this.proboscis, this.proboscis_r1 });
    }

    private void suckPose() {
        this.animator.rotate(this.chest, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-60.0), 0.0F, 0.0F);
        this.animator.rotate(this.shoulder_left, Maths.rad(-100.0), Maths.rad(20.0), Maths.rad(-10.0));
        this.animator.rotate(this.shoulder_right, Maths.rad(-100.0), Maths.rad(-20.0), Maths.rad(10.0));
        this.animator.rotate(this.arm_right, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-20.0), 0.0F, 0.0F);
    }

    public void setupAnim(EntityWarpedMosco entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float flySpeed = 0.5F;
        float flyDegree = 0.5F;
        float walkSpeed = 0.5F;
        float walkDegree = 0.6F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float flyLeftProgress = entity.prevLeftFlyProgress + (entity.flyLeftProgress - entity.prevLeftFlyProgress) * partialTicks;
        float flyRightProgress = entity.prevFlyRightProgress + (entity.flyRightProgress - entity.prevFlyRightProgress) * partialTicks;
        float flyProgress = Math.max(flyLeftProgress, flyRightProgress);
        float walkProgress = (5.0F - flyProgress) * limbSwingAmount;
        this.walk(this.antenna_left, idleSpeed, idleDegree, false, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.antenna_right, idleSpeed, idleDegree, false, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.shoulder_left, idleSpeed, idleDegree, false, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.shoulder_right, idleSpeed, idleDegree, false, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.shoulder_left, idleSpeed, idleDegree * 0.5F, false, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.shoulder_right, idleSpeed, idleDegree * 0.5F, true, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.hand_left, idleSpeed, idleDegree, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.hand_right, idleSpeed, idleDegree, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.head, idleSpeed, idleDegree * 0.25F, false, -1.0F, 0.05F, ageInTicks, 1.0F);
        this.walk(this.chest, idleSpeed, idleDegree * 0.15F, false, -2.0F, 0.05F, ageInTicks, 1.0F);
        this.bob(this.body, idleSpeed, idleDegree * 5.0F, false, ageInTicks, 1.0F);
        this.walk(this.legfront_right, idleSpeed, idleDegree, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.kneefront_right, idleSpeed, idleDegree, true, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.legfront_left, idleSpeed, idleDegree, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.kneefront_left, idleSpeed, idleDegree, true, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.legback_right, idleSpeed, idleDegree, true, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.kneeback_right, idleSpeed, idleDegree, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.legback_left, idleSpeed, idleDegree, true, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.kneeback_left, idleSpeed, idleDegree, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.progressRotationPrev(this.chest, walkProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.wingbottom_left, walkProgress, 0.0F, Maths.rad(-50.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.wingbottom_right, walkProgress, 0.0F, Maths.rad(50.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.wingtop_left, walkProgress, 0.0F, Maths.rad(-50.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.wingtop_right, walkProgress, 0.0F, Maths.rad(50.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.head, walkProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.back, walkProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, flyProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, flyProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legback_left, flyProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legback_right, flyProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legfront_left, flyProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legfront_right, flyProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.chest, flyLeftProgress, 0.0F, Maths.rad(30.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.head, flyLeftProgress, 0.0F, Maths.rad(-30.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.shoulder_left, flyLeftProgress, Maths.rad(-30.0), Maths.rad(-30.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_left, flyLeftProgress, Maths.rad(-40.0), Maths.rad(10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.hand_left, flyLeftProgress, Maths.rad(-30.0), Maths.rad(60.0), Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.chest, flyRightProgress, 0.0F, Maths.rad(-30.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.head, flyRightProgress, 0.0F, Maths.rad(30.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.shoulder_right, flyRightProgress, Maths.rad(-30.0), Maths.rad(30.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_right, flyRightProgress, Maths.rad(-40.0), Maths.rad(-10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.hand_right, flyRightProgress, Maths.rad(-30.0), Maths.rad(-60.0), Maths.rad(-20.0), 5.0F);
        if (flyProgress <= 0.0F) {
            this.walk(this.kneefront_left, walkSpeed, walkDegree * 0.4F, true, 0.0F, -0.1F, limbSwing, limbSwingAmount);
            this.walk(this.legfront_left, walkSpeed, walkDegree * 0.8F, false, 2.0F, -0.3F, limbSwing, limbSwingAmount);
            this.walk(this.kneefront_right, walkSpeed, walkDegree * 0.4F, false, 0.0F, 0.1F, limbSwing, limbSwingAmount);
            this.walk(this.legfront_right, walkSpeed, walkDegree * 0.8F, true, 2.0F, 0.3F, limbSwing, limbSwingAmount);
            this.walk(this.kneeback_left, walkSpeed, walkDegree * 0.4F, false, 0.0F, -0.1F, limbSwing, limbSwingAmount);
            this.walk(this.legback_left, walkSpeed, walkDegree * 0.8F, true, 2.0F, -0.3F, limbSwing, limbSwingAmount);
            this.walk(this.kneeback_right, walkSpeed, walkDegree * 0.4F, true, 0.0F, 0.1F, limbSwing, limbSwingAmount);
            this.walk(this.legback_right, walkSpeed, walkDegree * 0.8F, false, 2.0F, 0.3F, limbSwing, limbSwingAmount);
            this.swing(this.chest, walkSpeed, walkDegree * 0.3F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.shoulder_left, walkSpeed, walkDegree * 0.6F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.shoulder_right, walkSpeed, walkDegree * 0.6F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.arm_left, walkSpeed, walkDegree * 0.6F, true, 0.6F, 0.9F, limbSwing, limbSwingAmount);
            this.walk(this.arm_right, walkSpeed, walkDegree * 0.6F, false, 0.6F, -0.9F, limbSwing, limbSwingAmount);
            this.walk(this.hand_left, walkSpeed, walkDegree * 0.6F, true, 0.6F, 0.3F, limbSwing, limbSwingAmount);
            this.walk(this.hand_right, walkSpeed, walkDegree * 0.6F, false, 0.6F, -0.3F, limbSwing, limbSwingAmount);
            this.swing(this.legfront_left, walkSpeed, walkDegree * 0.4F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.legfront_right, walkSpeed, walkDegree * 0.4F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.legback_left, walkSpeed, walkDegree * 0.4F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.legback_right, walkSpeed, walkDegree * 0.4F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.bob(this.body, walkSpeed, walkDegree * 5.0F, false, limbSwing, limbSwingAmount);
        } else {
            this.swing(this.wingbottom_left, flySpeed * 3.3F, flyDegree, true, 0.0F, 0.2F, ageInTicks, 1.0F);
            this.swing(this.wingbottom_right, flySpeed * 3.3F, flyDegree, false, 0.0F, 0.2F, ageInTicks, 1.0F);
            this.swing(this.wingtop_left, flySpeed * 3.3F, flyDegree * 1.3F, true, 1.0F, 0.5F, ageInTicks, 1.0F);
            this.swing(this.wingtop_right, flySpeed * 3.3F, flyDegree * 1.3F, false, 1.0F, 0.5F, ageInTicks, 1.0F);
            this.bob(this.body, flySpeed, flyDegree * 5.0F, false, ageInTicks, 1.0F);
        }
        if (entity.getAnimation() != EntityWarpedMosco.ANIMATION_SUCK) {
            this.head.rotateAngleY += netHeadYaw * 0.6F * (float) (Math.PI / 180.0);
            this.head.rotateAngleX += headPitch * 0.9F * (float) (Math.PI / 180.0);
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}