package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityFarseer;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.phys.Vec3;

public class ModelFarseer extends AdvancedEntityModel<EntityFarseer> {

    public final AdvancedModelBox eye;

    private final AdvancedModelBox root;

    private final AdvancedModelBox bodyCube1;

    private final AdvancedModelBox head;

    private final AdvancedModelBox leftUpperMask;

    private final AdvancedModelBox rightUpperMask;

    private final AdvancedModelBox leftLowerMask;

    private final AdvancedModelBox rightLowerMask;

    private final AdvancedModelBox bodyCube2;

    private final AdvancedModelBox leftArm;

    private final AdvancedModelBox leftElbow;

    private final AdvancedModelBox leftHand;

    private final AdvancedModelBox leftUpperRFinger;

    private final AdvancedModelBox leftLowerRFinger;

    private final AdvancedModelBox leftLowerLFinger;

    private final AdvancedModelBox leftUpperLFinger;

    private final AdvancedModelBox leftArm2;

    private final AdvancedModelBox leftElbow2;

    private final AdvancedModelBox leftHand2;

    private final AdvancedModelBox leftUpperRFinger2;

    private final AdvancedModelBox leftLowerRFinger2;

    private final AdvancedModelBox leftLowerLFinger2;

    private final AdvancedModelBox leftUpperLFinger2;

    private final AdvancedModelBox rightArm;

    private final AdvancedModelBox rightElbow;

    private final AdvancedModelBox rightHand;

    private final AdvancedModelBox rightUpperRFinger;

    private final AdvancedModelBox rightLowerRFinger2;

    private final AdvancedModelBox rightLowerLFinger;

    private final AdvancedModelBox rightUpperLFinger;

    private final AdvancedModelBox rightArm2;

    private final AdvancedModelBox rightElbow2;

    private final AdvancedModelBox rightHand2;

    private final AdvancedModelBox rightUpperRFinger2;

    private final AdvancedModelBox rightLowerRFinger3;

    private final AdvancedModelBox rightLowerLFinger2;

    private final AdvancedModelBox rightUpperLFinger2;

    private final ModelAnimator animator;

    public ModelFarseer(float scale) {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bodyCube1 = new AdvancedModelBox(this, "bodyCube1");
        this.bodyCube1.setRotationPoint(2.0F, -8.0F, 0.0F);
        this.root.addChild(this.bodyCube1);
        this.bodyCube1.setTextureOffset(0, 56).addBox(-5.0F, -2.0F, -3.0F, 10.0F, 4.0F, 5.0F, scale, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -15.0F, 0.0F);
        this.root.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-10.5F, -5.0F, -6.0F, 21.0F, 9.0F, 13.0F, scale, false);
        this.leftUpperMask = new AdvancedModelBox(this, "leftUpperMask");
        this.leftUpperMask.setRotationPoint(0.0F, -5.0F, 1.0F);
        this.head.addChild(this.leftUpperMask);
        this.leftUpperMask.setTextureOffset(0, 23).addBox(0.0F, -0.5F, -7.9F, 11.0F, 7.0F, 8.0F, scale, false);
        this.rightUpperMask = new AdvancedModelBox(this, "rightUpperMask");
        this.rightUpperMask.setRotationPoint(0.0F, -5.0F, 1.0F);
        this.head.addChild(this.rightUpperMask);
        this.rightUpperMask.setTextureOffset(0, 23).addBox(-11.0F, -0.5F, -7.9F, 11.0F, 7.0F, 8.0F, scale, true);
        this.leftLowerMask = new AdvancedModelBox(this, "leftLowerMask");
        this.leftLowerMask.setRotationPoint(0.0F, 3.0F, 1.0F);
        this.head.addChild(this.leftLowerMask);
        this.leftLowerMask.setTextureOffset(31, 31).addBox(0.0F, -2.5F, -7.9F, 11.0F, 4.0F, 8.0F, scale, false);
        this.rightLowerMask = new AdvancedModelBox(this, "rightLowerMask");
        this.rightLowerMask.setRotationPoint(0.0F, 3.0F, 1.0F);
        this.head.addChild(this.rightLowerMask);
        this.rightLowerMask.setTextureOffset(31, 31).addBox(-11.0F, -2.5F, -7.9F, 11.0F, 4.0F, 8.0F, scale, true);
        this.eye = new AdvancedModelBox(this, "eye");
        this.eye.setRotationPoint(0.0F, 4.0F, -1.0F);
        this.head.addChild(this.eye);
        this.eye.setTextureOffset(56, 0).addBox(-4.5F, -8.0F, -6.0F, 9.0F, 4.0F, 2.0F, scale, false);
        this.bodyCube2 = new AdvancedModelBox(this, "bodyCube2");
        this.bodyCube2.setRotationPoint(1.0F, -2.0F, 1.0F);
        this.root.addChild(this.bodyCube2);
        this.bodyCube2.setTextureOffset(33, 44).addBox(-6.0F, -3.0F, -3.0F, 10.0F, 5.0F, 6.0F, scale, false);
        this.leftArm = new AdvancedModelBox(this, "leftArm");
        this.leftArm.setRotationPoint(9.0F, -16.5F, 9.0F);
        this.root.addChild(this.leftArm);
        this.setRotationAngle(this.leftArm, 0.0F, 0.0F, -0.7854F);
        this.leftArm.setTextureOffset(31, 23).addBox(-1.0F, -1.5F, -2.0F, 16.0F, 3.0F, 4.0F, scale, false);
        this.leftElbow = new AdvancedModelBox(this, "leftElbow");
        this.leftElbow.setRotationPoint(15.0F, 0.0F, 0.0F);
        this.leftArm.addChild(this.leftElbow);
        this.leftElbow.setTextureOffset(0, 39).addBox(-1.0F, -1.0F, -13.0F, 2.0F, 2.0F, 14.0F, scale, false);
        this.leftHand = new AdvancedModelBox(this, "leftHand");
        this.leftHand.setRotationPoint(0.0F, 0.0F, -12.0F);
        this.leftElbow.addChild(this.leftHand);
        this.leftUpperRFinger = new AdvancedModelBox(this, "leftUpperRFinger");
        this.leftUpperRFinger.setRotationPoint(-1.4F, -1.4F, 0.0F);
        this.leftHand.addChild(this.leftUpperRFinger);
        this.setRotationAngle(this.leftUpperRFinger, 0.0F, 0.0F, -0.7854F);
        this.leftUpperRFinger.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, false);
        this.leftLowerRFinger = new AdvancedModelBox(this, "leftLowerRFinger");
        this.leftLowerRFinger.setRotationPoint(-1.4F, 1.4F, 0.0F);
        this.leftHand.addChild(this.leftLowerRFinger);
        this.setRotationAngle(this.leftLowerRFinger, 0.0F, 0.0F, -2.3562F);
        this.leftLowerRFinger.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, false);
        this.leftLowerLFinger = new AdvancedModelBox(this, "leftLowerLFinger");
        this.leftLowerLFinger.setRotationPoint(1.4F, 1.4F, 0.0F);
        this.leftHand.addChild(this.leftLowerLFinger);
        this.setRotationAngle(this.leftLowerLFinger, 0.0F, 0.0F, 2.3562F);
        this.leftLowerLFinger.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, false);
        this.leftUpperLFinger = new AdvancedModelBox(this, "leftUpperLFinger");
        this.leftUpperLFinger.setRotationPoint(1.4F, -1.4F, 0.0F);
        this.leftHand.addChild(this.leftUpperLFinger);
        this.setRotationAngle(this.leftUpperLFinger, 0.0F, 0.0F, 0.7854F);
        this.leftUpperLFinger.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, false);
        this.leftArm2 = new AdvancedModelBox(this, "leftArm2");
        this.leftArm2.setRotationPoint(6.0F, -13.5F, 9.0F);
        this.root.addChild(this.leftArm2);
        this.setRotationAngle(this.leftArm2, 0.0F, 0.0F, 0.6545F);
        this.leftArm2.setTextureOffset(31, 23).addBox(-1.0F, -1.5F, -2.0F, 16.0F, 3.0F, 4.0F, scale, false);
        this.leftElbow2 = new AdvancedModelBox(this, "leftElbow2");
        this.leftElbow2.setRotationPoint(15.0F, 0.0F, 0.0F);
        this.leftArm2.addChild(this.leftElbow2);
        this.leftElbow2.setTextureOffset(0, 39).addBox(-1.0F, -1.0F, -13.0F, 2.0F, 2.0F, 14.0F, scale, false);
        this.leftHand2 = new AdvancedModelBox(this, "leftHand2");
        this.leftHand2.setRotationPoint(0.0F, 0.0F, -12.0F);
        this.leftElbow2.addChild(this.leftHand2);
        this.leftUpperRFinger2 = new AdvancedModelBox(this, "leftUpperRFinger2");
        this.leftUpperRFinger2.setRotationPoint(-1.4F, -1.4F, 0.0F);
        this.leftHand2.addChild(this.leftUpperRFinger2);
        this.setRotationAngle(this.leftUpperRFinger2, 0.0F, 0.0F, -0.7854F);
        this.leftUpperRFinger2.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, false);
        this.leftLowerRFinger2 = new AdvancedModelBox(this, "leftLowerRFinger2");
        this.leftLowerRFinger2.setRotationPoint(-1.4F, 1.4F, 0.0F);
        this.leftHand2.addChild(this.leftLowerRFinger2);
        this.setRotationAngle(this.leftLowerRFinger2, 0.0F, 0.0F, -2.3562F);
        this.leftLowerRFinger2.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, false);
        this.leftLowerLFinger2 = new AdvancedModelBox(this, "leftLowerLFinger2");
        this.leftLowerLFinger2.setRotationPoint(1.4F, 1.4F, 0.0F);
        this.leftHand2.addChild(this.leftLowerLFinger2);
        this.setRotationAngle(this.leftLowerLFinger2, 0.0F, 0.0F, 2.3562F);
        this.leftLowerLFinger2.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, false);
        this.leftUpperLFinger2 = new AdvancedModelBox(this, "leftUpperLFinger2");
        this.leftUpperLFinger2.setRotationPoint(1.4F, -1.4F, 0.0F);
        this.leftHand2.addChild(this.leftUpperLFinger2);
        this.setRotationAngle(this.leftUpperLFinger2, 0.0F, 0.0F, 0.7854F);
        this.leftUpperLFinger2.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, false);
        this.rightArm = new AdvancedModelBox(this, "rightArm");
        this.rightArm.setRotationPoint(-9.0F, -16.5F, 9.0F);
        this.root.addChild(this.rightArm);
        this.setRotationAngle(this.rightArm, 0.0F, 0.0F, 0.7854F);
        this.rightArm.setTextureOffset(31, 23).addBox(-15.0F, -1.5F, -2.0F, 16.0F, 3.0F, 4.0F, scale, true);
        this.rightElbow = new AdvancedModelBox(this, "rightElbow");
        this.rightElbow.setRotationPoint(-15.0F, 0.0F, 0.0F);
        this.rightArm.addChild(this.rightElbow);
        this.rightElbow.setTextureOffset(0, 39).addBox(-1.0F, -1.0F, -13.0F, 2.0F, 2.0F, 14.0F, scale, true);
        this.rightHand = new AdvancedModelBox(this, "rightHand");
        this.rightHand.setRotationPoint(0.0F, 0.0F, -12.0F);
        this.rightElbow.addChild(this.rightHand);
        this.rightUpperRFinger = new AdvancedModelBox(this, "rightUpperRFinger");
        this.rightUpperRFinger.setRotationPoint(1.4F, -1.4F, 0.0F);
        this.rightHand.addChild(this.rightUpperRFinger);
        this.setRotationAngle(this.rightUpperRFinger, 0.0F, 0.0F, 0.7854F);
        this.rightUpperRFinger.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, true);
        this.rightLowerRFinger2 = new AdvancedModelBox(this, "rightLowerRFinger2");
        this.rightLowerRFinger2.setRotationPoint(1.4F, 1.4F, 0.0F);
        this.rightHand.addChild(this.rightLowerRFinger2);
        this.setRotationAngle(this.rightLowerRFinger2, 0.0F, 0.0F, 2.3562F);
        this.rightLowerRFinger2.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, true);
        this.rightLowerLFinger = new AdvancedModelBox(this, "rightLowerLFinger");
        this.rightLowerLFinger.setRotationPoint(-1.4F, 1.4F, 0.0F);
        this.rightHand.addChild(this.rightLowerLFinger);
        this.setRotationAngle(this.rightLowerLFinger, 0.0F, 0.0F, -2.3562F);
        this.rightLowerLFinger.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, true);
        this.rightUpperLFinger = new AdvancedModelBox(this, "rightUpperLFinger");
        this.rightUpperLFinger.setRotationPoint(-1.4F, -1.4F, 0.0F);
        this.rightHand.addChild(this.rightUpperLFinger);
        this.setRotationAngle(this.rightUpperLFinger, 0.0F, 0.0F, -0.7854F);
        this.rightUpperLFinger.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, true);
        this.rightArm2 = new AdvancedModelBox(this, "rightArm2");
        this.rightArm2.setRotationPoint(-6.0F, -13.5F, 9.0F);
        this.root.addChild(this.rightArm2);
        this.setRotationAngle(this.rightArm2, 0.0F, 0.0F, -0.6545F);
        this.rightArm2.setTextureOffset(31, 23).addBox(-15.0F, -1.5F, -2.0F, 16.0F, 3.0F, 4.0F, scale, true);
        this.rightElbow2 = new AdvancedModelBox(this, "rightElbow2");
        this.rightElbow2.setRotationPoint(-15.0F, 0.0F, 0.0F);
        this.rightArm2.addChild(this.rightElbow2);
        this.rightElbow2.setTextureOffset(0, 39).addBox(-1.0F, -1.0F, -13.0F, 2.0F, 2.0F, 14.0F, scale, true);
        this.rightHand2 = new AdvancedModelBox(this, "rightHand2");
        this.rightHand2.setRotationPoint(0.0F, 0.0F, -12.0F);
        this.rightElbow2.addChild(this.rightHand2);
        this.rightUpperRFinger2 = new AdvancedModelBox(this, "rightUpperRFinger2");
        this.rightUpperRFinger2.setRotationPoint(1.4F, -1.4F, 0.0F);
        this.rightHand2.addChild(this.rightUpperRFinger2);
        this.setRotationAngle(this.rightUpperRFinger2, 0.0F, 0.0F, 0.7854F);
        this.rightUpperRFinger2.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, true);
        this.rightLowerRFinger3 = new AdvancedModelBox(this, "rightLowerRFinger3");
        this.rightLowerRFinger3.setRotationPoint(1.4F, 1.4F, 0.0F);
        this.rightHand2.addChild(this.rightLowerRFinger3);
        this.setRotationAngle(this.rightLowerRFinger3, 0.0F, 0.0F, 2.3562F);
        this.rightLowerRFinger3.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, true);
        this.rightLowerLFinger2 = new AdvancedModelBox(this, "rightLowerLFinger2");
        this.rightLowerLFinger2.setRotationPoint(-1.4F, 1.4F, 0.0F);
        this.rightHand2.addChild(this.rightLowerLFinger2);
        this.setRotationAngle(this.rightLowerLFinger2, 0.0F, 0.0F, -2.3562F);
        this.rightLowerLFinger2.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, true);
        this.rightUpperLFinger2 = new AdvancedModelBox(this, "rightUpperLFinger2");
        this.rightUpperLFinger2.setRotationPoint(-1.4F, -1.4F, 0.0F);
        this.rightHand2.addChild(this.rightUpperLFinger2);
        this.setRotationAngle(this.rightUpperLFinger2, 0.0F, 0.0F, -0.7854F);
        this.rightUpperLFinger2.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -3.0F, 2.0F, 7.0F, 4.0F, scale, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.animator.update(entity);
        this.animator.setAnimation(EntityFarseer.ANIMATION_EMERGE);
        this.animator.startKeyframe(0);
        this.animator.move(this.root, 0.0F, 0.0F, 20.0F);
        this.animator.rotate(this.leftArm, 0.0F, Maths.rad(80.0), 0.0F);
        this.animator.rotate(this.leftElbow, Maths.rad(-20.0), Maths.rad(-30.0), 0.0F);
        this.animator.rotate(this.leftHand, Maths.rad(-35.0), Maths.rad(40.0), 0.0F);
        this.animator.move(this.leftArm, -1.0F, -5.0F, -6.0F);
        this.animator.rotate(this.rightArm, 0.0F, Maths.rad(-80.0), 0.0F);
        this.animator.rotate(this.rightElbow, Maths.rad(-40.0), Maths.rad(70.0), 0.0F);
        this.animator.rotate(this.rightHand, Maths.rad(5.0), Maths.rad(-60.0), 0.0F);
        this.animator.move(this.rightArm, 1.0F, -5.0F, -6.0F);
        this.animator.rotate(this.leftArm2, 0.0F, Maths.rad(60.0), 0.0F);
        this.animator.rotate(this.leftElbow2, Maths.rad(40.0), Maths.rad(-30.0), 0.0F);
        this.animator.rotate(this.leftHand2, Maths.rad(-15.0), Maths.rad(40.0), 0.0F);
        this.animator.move(this.leftArm2, 2.0F, 6.0F, -6.0F);
        this.animator.rotate(this.rightArm2, 0.0F, Maths.rad(-80.0), 0.0F);
        this.animator.rotate(this.rightElbow2, Maths.rad(20.0), Maths.rad(50.0), 0.0F);
        this.animator.rotate(this.rightHand2, Maths.rad(25.0), Maths.rad(-60.0), 0.0F);
        this.animator.move(this.rightArm2, -2.0F, 5.0F, -4.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(10);
        this.animator.startKeyframe(10);
        this.animator.move(this.root, 0.0F, 0.0F, 15.0F);
        this.animator.rotate(this.leftArm, 0.0F, Maths.rad(70.0), 0.0F);
        this.animator.rotate(this.leftElbow, Maths.rad(-20.0), Maths.rad(-30.0), 0.0F);
        this.animator.rotate(this.leftHand, Maths.rad(-5.0), Maths.rad(50.0), 0.0F);
        this.animator.move(this.leftArm, 1.0F, -5.0F, -1.0F);
        this.animator.rotate(this.rightArm, 0.0F, Maths.rad(-80.0), 0.0F);
        this.animator.rotate(this.rightElbow, Maths.rad(-40.0), Maths.rad(60.0), 0.0F);
        this.animator.rotate(this.rightHand, Maths.rad(5.0), Maths.rad(-70.0), 0.0F);
        this.animator.move(this.rightArm, -1.0F, -5.0F, -1.0F);
        this.animator.rotate(this.leftArm2, 0.0F, Maths.rad(60.0), 0.0F);
        this.animator.rotate(this.leftElbow2, Maths.rad(30.0), Maths.rad(-30.0), 0.0F);
        this.animator.rotate(this.leftHand2, Maths.rad(-5.0), Maths.rad(40.0), 0.0F);
        this.animator.move(this.leftArm2, 3.0F, 6.0F, -1.0F);
        this.animator.rotate(this.rightArm2, 0.0F, Maths.rad(-70.0), 0.0F);
        this.animator.rotate(this.rightElbow2, Maths.rad(20.0), Maths.rad(50.0), 0.0F);
        this.animator.rotate(this.rightHand2, Maths.rad(25.0), Maths.rad(-60.0), 0.0F);
        this.animator.move(this.rightArm2, -3.0F, 5.0F, 1.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.move(this.root, 0.0F, 0.0F, 10.0F);
        this.animator.rotate(this.leftArm, 0.0F, Maths.rad(60.0), 0.0F);
        this.animator.rotate(this.leftElbow, Maths.rad(-20.0), Maths.rad(-30.0), 0.0F);
        this.animator.rotate(this.leftHand, Maths.rad(-15.0), Maths.rad(50.0), 0.0F);
        this.animator.move(this.leftArm, 2.0F, -5.0F, 4.0F);
        this.animator.rotate(this.rightArm, 0.0F, Maths.rad(-72.0), 0.0F);
        this.animator.rotate(this.rightElbow, Maths.rad(-40.0), Maths.rad(60.0), 0.0F);
        this.animator.rotate(this.rightHand, Maths.rad(-5.0), Maths.rad(-75.0), 0.0F);
        this.animator.move(this.rightArm, -1.0F, -3.0F, 4.0F);
        this.animator.rotate(this.leftArm2, 0.0F, Maths.rad(55.0), 0.0F);
        this.animator.rotate(this.leftElbow2, Maths.rad(30.0), Maths.rad(-30.0), 0.0F);
        this.animator.rotate(this.leftHand2, Maths.rad(-15.0), Maths.rad(50.0), 0.0F);
        this.animator.move(this.leftArm2, 5.0F, 6.0F, 4.0F);
        this.animator.rotate(this.rightArm2, 0.0F, Maths.rad(-60.0), 0.0F);
        this.animator.rotate(this.rightElbow2, Maths.rad(20.0), Maths.rad(50.0), 0.0F);
        this.animator.rotate(this.rightHand2, Maths.rad(5.0), Maths.rad(-60.0), 0.0F);
        this.animator.move(this.rightArm2, -5.0F, 5.0F, 6.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.move(this.root, 0.0F, 0.0F, 5.0F);
        this.animator.rotate(this.leftArm, 0.0F, Maths.rad(60.0), 0.0F);
        this.animator.rotate(this.leftElbow, Maths.rad(-20.0), Maths.rad(-40.0), 0.0F);
        this.animator.rotate(this.leftHand, Maths.rad(-15.0), Maths.rad(85.0), 0.0F);
        this.animator.move(this.leftArm, 3.0F, -5.0F, 9.0F);
        this.animator.rotate(this.rightArm, 0.0F, Maths.rad(-72.0), 0.0F);
        this.animator.rotate(this.rightElbow, Maths.rad(-40.0), Maths.rad(60.0), 0.0F);
        this.animator.rotate(this.rightHand, Maths.rad(15.0), Maths.rad(-85.0), 0.0F);
        this.animator.move(this.rightArm, -5.0F, -4.0F, 9.0F);
        this.animator.rotate(this.leftArm2, 0.0F, Maths.rad(45.0), 0.0F);
        this.animator.rotate(this.leftElbow2, Maths.rad(30.0), Maths.rad(-40.0), 0.0F);
        this.animator.rotate(this.leftHand2, Maths.rad(-5.0), Maths.rad(70.0), 0.0F);
        this.animator.move(this.leftArm2, 8.0F, 4.0F, 7.0F);
        this.animator.rotate(this.rightArm2, 0.0F, Maths.rad(-50.0), 0.0F);
        this.animator.rotate(this.rightElbow2, Maths.rad(20.0), Maths.rad(50.0), 0.0F);
        this.animator.rotate(this.rightHand2, Maths.rad(25.0), Maths.rad(-70.0), 0.0F);
        this.animator.move(this.rightArm2, -5.0F, 5.0F, 11.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
    }

    public void setupAnim(EntityFarseer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float idleSpeed = 0.15F;
        float invPortalDegree = 1.0F - entity.getFacingCameraAmount(partialTick);
        float angryProgress = entity.prevAngryProgress + (entity.angryProgress - entity.prevAngryProgress) * partialTick;
        float strike1Progress = (entity.prevStrikeProgress[0] + (entity.strikeProgress[0] - entity.prevStrikeProgress[0]) * partialTick) * invPortalDegree;
        float strike2Progress = (entity.prevStrikeProgress[1] + (entity.strikeProgress[1] - entity.prevStrikeProgress[1]) * partialTick) * invPortalDegree;
        float strike3Progress = (entity.prevStrikeProgress[2] + (entity.strikeProgress[2] - entity.prevStrikeProgress[2]) * partialTick) * invPortalDegree;
        float strike4Progress = (entity.prevStrikeProgress[3] + (entity.strikeProgress[3] - entity.prevStrikeProgress[3]) * partialTick) * invPortalDegree;
        float clasp1Progress = Math.max(strike1Progress, (entity.prevClaspProgress[0] + (entity.claspProgress[0] - entity.prevClaspProgress[0]) * partialTick) * invPortalDegree);
        float clasp2Progress = Math.max(strike2Progress, (entity.prevClaspProgress[1] + (entity.claspProgress[1] - entity.prevClaspProgress[1]) * partialTick) * invPortalDegree);
        float clasp3Progress = Math.max(strike3Progress, (entity.prevClaspProgress[2] + (entity.claspProgress[2] - entity.prevClaspProgress[2]) * partialTick) * invPortalDegree);
        float clasp4Progress = Math.max(strike4Progress, (entity.prevClaspProgress[3] + (entity.claspProgress[3] - entity.prevClaspProgress[3]) * partialTick) * invPortalDegree);
        float armYaw = Maths.rad(entity.getLatencyVar(5, 3, partialTick) - entity.getLatencyVar(0, 3, partialTick));
        Vec3 topArmOffset = entity.getLatencyOffsetVec(4, partialTick).scale(-4.0);
        Vec3 bottomArmOffset = entity.getLatencyOffsetVec(8, partialTick).scale(-5.0);
        Vec3 body1Offset = entity.getLatencyOffsetVec(8, partialTick).scale(-3.0);
        Vec3 body2Offset = entity.getLatencyOffsetVec(12, partialTick).scale(-5.0);
        Vec3 angryShake = entity.angryShakeVec.scale((double) (angryProgress * 0.1F));
        this.progressRotationPrev(this.rightUpperMask, angryProgress, Maths.rad(-35.0), Maths.rad(13.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftUpperMask, angryProgress, Maths.rad(-35.0), Maths.rad(-13.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightLowerMask, angryProgress, Maths.rad(35.0), Maths.rad(13.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftLowerMask, angryProgress, Maths.rad(35.0), Maths.rad(-13.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.bodyCube1, angryProgress, 0.0F, 0.0F, 4.0F, 5.0F);
        this.progressPositionPrev(this.bodyCube2, angryProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        this.progressRotationPrev(this.leftUpperRFinger, clasp1Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftLowerRFinger, clasp1Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftLowerLFinger, clasp1Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftUpperLFinger, clasp1Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightUpperRFinger, clasp2Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightLowerRFinger2, clasp2Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightLowerLFinger, clasp2Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightUpperLFinger, clasp2Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftUpperRFinger2, clasp3Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftLowerRFinger2, clasp3Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftLowerLFinger2, clasp3Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftUpperLFinger2, clasp3Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightUpperRFinger2, clasp4Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightLowerRFinger3, clasp4Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightLowerLFinger2, clasp4Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightUpperLFinger2, clasp4Progress, Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leftArm, strike1Progress, 4.0F, -4.0F, -9.0F, 5.0F);
        this.progressRotationPrev(this.leftArm, strike1Progress, 0.0F, Maths.rad(90.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftElbow, strike1Progress, 0.0F, Maths.rad(-70.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftHand, strike1Progress, 0.0F, Maths.rad(-10.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.rightArm, strike2Progress, -4.0F, -4.0F, -9.0F, 5.0F);
        this.progressRotationPrev(this.rightArm, strike2Progress, 0.0F, Maths.rad(-90.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightElbow, strike2Progress, 0.0F, Maths.rad(70.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightHand, strike2Progress, 0.0F, Maths.rad(10.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.leftArm2, strike3Progress, 6.0F, 4.0F, -9.0F, 5.0F);
        this.progressRotationPrev(this.leftArm2, strike3Progress, 0.0F, Maths.rad(90.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftElbow2, strike3Progress, 0.0F, Maths.rad(-70.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftHand2, strike3Progress, 0.0F, Maths.rad(-10.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.rightArm2, strike4Progress, -6.0F, 4.0F, -9.0F, 5.0F);
        this.progressRotationPrev(this.rightArm2, strike4Progress, 0.0F, Maths.rad(-90.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightElbow2, strike4Progress, 0.0F, Maths.rad(70.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightHand2, strike4Progress, 0.0F, Maths.rad(10.0), 0.0F, 5.0F);
        this.leftArm.rotationPointX = (float) ((double) this.leftArm.rotationPointX + (topArmOffset.x + Math.sin((double) (ageInTicks * idleSpeed + 1.3F))) * (double) invPortalDegree);
        this.leftArm.rotationPointY = (float) ((double) this.leftArm.rotationPointY + (topArmOffset.y + Math.sin((double) (ageInTicks * idleSpeed + 1.6F))) * (double) invPortalDegree);
        this.leftArm.rotationPointZ = (float) ((double) this.leftArm.rotationPointZ + (topArmOffset.z + Math.cos((double) (ageInTicks * idleSpeed + 1.9F))) * (double) invPortalDegree);
        this.leftArm.rotateAngleY += armYaw;
        this.leftArm2.rotationPointX = (float) ((double) this.leftArm2.rotationPointX + (bottomArmOffset.x + Math.sin((double) (ageInTicks * idleSpeed + 2.3F))) * (double) invPortalDegree);
        this.leftArm2.rotationPointY = (float) ((double) this.leftArm2.rotationPointY + (bottomArmOffset.y + Math.sin((double) (ageInTicks * idleSpeed + 2.6F))) * (double) invPortalDegree);
        this.leftArm2.rotationPointZ = (float) ((double) this.leftArm2.rotationPointZ + (bottomArmOffset.z + Math.cos((double) (ageInTicks * idleSpeed + 2.9F))) * (double) invPortalDegree);
        this.leftArm2.rotateAngleY += armYaw;
        this.rightArm.rotationPointX = (float) ((double) this.rightArm.rotationPointX + (topArmOffset.x + Math.sin((double) (ageInTicks * idleSpeed + 3.3F))) * (double) invPortalDegree);
        this.rightArm.rotationPointY = (float) ((double) this.rightArm.rotationPointY + (topArmOffset.y + Math.sin((double) (ageInTicks * idleSpeed + 3.6F))) * (double) invPortalDegree);
        this.rightArm.rotationPointZ = (float) ((double) this.rightArm.rotationPointZ + (topArmOffset.z + Math.cos((double) (ageInTicks * idleSpeed + 3.9F))) * (double) invPortalDegree);
        this.rightArm.rotateAngleY += armYaw;
        this.rightArm2.rotationPointX = (float) ((double) this.rightArm2.rotationPointX + (bottomArmOffset.x + Math.sin((double) (ageInTicks * idleSpeed + 4.3F))) * (double) invPortalDegree);
        this.rightArm2.rotationPointY = (float) ((double) this.rightArm2.rotationPointY + (bottomArmOffset.y + Math.sin((double) (ageInTicks * idleSpeed + 4.6F))) * (double) invPortalDegree);
        this.rightArm2.rotationPointZ = (float) ((double) this.rightArm2.rotationPointZ + (bottomArmOffset.z + Math.cos((double) (ageInTicks * idleSpeed + 4.9F))) * (double) invPortalDegree);
        this.rightArm2.rotateAngleY += armYaw;
        this.bodyCube1.rotationPointX = (float) ((double) this.bodyCube1.rotationPointX + (body1Offset.x + Math.sin((double) (ageInTicks * idleSpeed + 7.3F))) * (double) invPortalDegree);
        this.bodyCube1.rotationPointY = (float) ((double) this.bodyCube1.rotationPointY + (body1Offset.y + Math.sin((double) (ageInTicks * idleSpeed + 7.6F))) * (double) invPortalDegree);
        this.bodyCube1.rotationPointZ = (float) ((double) this.bodyCube1.rotationPointZ + (body1Offset.z + Math.cos((double) (ageInTicks * idleSpeed + 7.9F))) * (double) invPortalDegree);
        this.bodyCube2.rotationPointX = (float) ((double) this.bodyCube2.rotationPointX + (body2Offset.x + Math.sin((double) (ageInTicks * idleSpeed + 5.3F))) * (double) invPortalDegree);
        this.bodyCube2.rotationPointY = (float) ((double) this.bodyCube2.rotationPointY + (body2Offset.y + Math.sin((double) (ageInTicks * idleSpeed + 5.6F))) * (double) invPortalDegree);
        this.bodyCube2.rotationPointZ = (float) ((double) this.bodyCube2.rotationPointZ + (body2Offset.z + Math.cos((double) (ageInTicks * idleSpeed + 5.9F))) * (double) invPortalDegree);
        this.head.rotationPointX = (float) ((double) this.head.rotationPointX + angryShake.x);
        this.head.rotationPointY = (float) ((double) this.head.rotationPointY + angryShake.y);
        this.head.rotationPointZ = (float) ((double) this.head.rotationPointZ + angryShake.z);
        this.bob(this.root, idleSpeed, invPortalDegree, false, ageInTicks, 1.0F);
        this.swing(this.leftArm, idleSpeed, invPortalDegree * 0.2F, true, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.rightArm, idleSpeed, invPortalDegree * 0.2F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.leftArm2, idleSpeed, invPortalDegree * 0.2F, true, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.rightArm2, idleSpeed, invPortalDegree * 0.2F, false, 4.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.leftUpperMask, idleSpeed * 8.0F, 0.05F, true, 1.0F, 0.2F, ageInTicks, angryProgress * 0.2F);
        this.walk(this.rightUpperMask, idleSpeed * 8.0F, 0.05F, true, 2.0F, 0.2F, ageInTicks, angryProgress * 0.2F);
        this.walk(this.rightLowerMask, idleSpeed * 8.0F, 0.05F, false, 3.0F, 0.2F, ageInTicks, angryProgress * 0.2F);
        this.walk(this.leftLowerMask, idleSpeed * 8.0F, 0.05F, false, 4.0F, 0.2F, ageInTicks, angryProgress * 0.2F);
        float headY = netHeadYaw * invPortalDegree * (float) (Math.PI / 180.0);
        float headZ = headPitch * invPortalDegree * (float) (Math.PI / 180.0);
        this.head.rotateAngleY += headY;
        this.head.rotateAngleX += headZ;
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.bodyCube1, this.head, this.leftUpperMask, this.rightUpperMask, this.leftLowerMask, this.rightLowerMask, this.eye, this.bodyCube2, this.leftArm, this.leftElbow, this.leftHand, new AdvancedModelBox[] { this.leftUpperRFinger, this.leftLowerRFinger, this.leftLowerLFinger, this.leftUpperLFinger, this.leftArm2, this.leftElbow2, this.leftHand2, this.leftUpperRFinger2, this.leftLowerRFinger2, this.leftLowerLFinger2, this.leftUpperLFinger2, this.rightArm, this.rightElbow, this.rightHand, this.rightUpperRFinger, this.rightLowerRFinger2, this.rightLowerLFinger, this.rightUpperLFinger, this.rightArm2, this.rightElbow2, this.rightHand2, this.rightUpperRFinger2, this.rightLowerRFinger3, this.rightLowerLFinger2, this.rightUpperLFinger2 });
    }

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}