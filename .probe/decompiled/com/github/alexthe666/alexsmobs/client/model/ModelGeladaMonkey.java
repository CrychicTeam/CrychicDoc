package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityGeladaMonkey;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelGeladaMonkey extends AdvancedEntityModel<EntityGeladaMonkey> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox torso;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox mouth;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox right_arm;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox right_leg;

    public final ModelAnimator animator;

    public ModelGeladaMonkey() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -11.0F, 4.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(30, 36).addBox(-3.5F, -3.0F, -5.0F, 7.0F, 7.0F, 9.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -1.5F, 3.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, 0.5672F, 0.0F, 0.0F);
        this.tail.setTextureOffset(0, 0).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 20.0F, 0.0F, false);
        this.torso = new AdvancedModelBox(this, "torso");
        this.torso.setRotationPoint(0.0F, 0.0F, -4.0F);
        this.body.addChild(this.torso);
        this.torso.setTextureOffset(0, 24).addBox(-4.5F, -5.0F, -9.0F, 9.0F, 10.0F, 10.0F, 0.0F, false);
        this.torso.setTextureOffset(27, 0).addBox(-4.5F, 5.0F, -9.0F, 9.0F, 3.0F, 10.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setRotationPoint(0.0F, -0.9F, -8.6F);
        this.torso.addChild(this.neck);
        this.neck.setTextureOffset(39, 24).addBox(-5.0F, -5.0F, -3.0F, 10.0F, 7.0F, 4.0F, 0.0F, false);
        this.neck.setTextureOffset(50, 67).addBox(-8.0F, -5.0F, -2.0F, 16.0F, 11.0F, 0.0F, 0.0F, false);
        this.neck.setTextureOffset(25, 60).addBox(-4.0F, -5.0F, -2.4F, 8.0F, 7.0F, 3.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -1.0F, -2.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-2.5F, -2.0F, -2.0F, 5.0F, 4.0F, 3.0F, 0.0F, false);
        this.mouth = new AdvancedModelBox(this, "mouth");
        this.mouth.setRotationPoint(0.0F, 1.0F, -1.0F);
        this.head.addChild(this.mouth);
        this.setRotationAngle(this.mouth, -1.0908F, 0.0F, 0.0F);
        this.mouth.setTextureOffset(0, 8).addBox(-1.5F, -1.5F, -1.2F, 3.0F, 6.0F, 4.0F, 0.0F, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(2.0F, 3.0F, -7.0F);
        this.torso.addChild(this.left_arm);
        this.left_arm.setTextureOffset(11, 45).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-2.0F, 3.0F, -7.0F);
        this.torso.addChild(this.right_arm);
        this.right_arm.setTextureOffset(11, 45).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(2.3F, 5.0F, 2.0F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(0, 45).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 7.0F, 3.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-2.3F, 5.0F, 2.0F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(0, 45).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 7.0F, 3.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityGeladaMonkey.ANIMATION_SWIPE_L);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, Maths.rad(5.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(-5.0), 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(25.0), Maths.rad(10.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.body, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_leg, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_leg, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(-80.0), Maths.rad(-20.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityGeladaMonkey.ANIMATION_SWIPE_R);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, Maths.rad(-5.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(5.0), 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(25.0), Maths.rad(-10.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.body, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_leg, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_leg, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-80.0), Maths.rad(20.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityGeladaMonkey.ANIMATION_CHEST);
        this.animator.startKeyframe(5);
        this.standPose();
        this.animator.rotate(this.right_arm, 0.0F, 0.0F, Maths.rad(25.0));
        this.animator.rotate(this.left_arm, 0.0F, 0.0F, Maths.rad(-25.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.standPose();
        this.animator.rotate(this.neck, 0.0F, 0.0F, Maths.rad(25.0));
        this.animator.rotate(this.right_arm, 0.0F, 0.0F, Maths.rad(25.0));
        this.animator.rotate(this.left_arm, 0.0F, 0.0F, Maths.rad(-25.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.standPose();
        this.animator.rotate(this.neck, 0.0F, 0.0F, Maths.rad(-25.0));
        this.animator.rotate(this.right_arm, 0.0F, 0.0F, Maths.rad(5.0));
        this.animator.rotate(this.left_arm, 0.0F, 0.0F, Maths.rad(-5.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.standPose();
        this.animator.rotate(this.neck, 0.0F, 0.0F, Maths.rad(25.0));
        this.animator.rotate(this.right_arm, 0.0F, 0.0F, Maths.rad(25.0));
        this.animator.rotate(this.left_arm, 0.0F, 0.0F, Maths.rad(-25.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityGeladaMonkey.ANIMATION_GROOM);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.left_arm, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-90.0), Maths.rad(-10.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, Maths.rad(20.0), 0.0F, Maths.rad(20.0));
        this.animator.rotate(this.left_arm, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-90.0), Maths.rad(-10.0), 0.0F);
        this.animator.move(this.neck, 1.0F, 0.0F, -1.0F);
        this.animator.move(this.right_arm, 0.0F, 0.0F, 3.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, Maths.rad(5.0), 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.left_arm, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-130.0), Maths.rad(-10.0), 0.0F);
        this.animator.move(this.neck, 1.0F, 0.0F, -1.0F);
        this.animator.move(this.right_arm, 0.0F, 0.0F, 2.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.right_arm, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(-90.0), Maths.rad(10.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, Maths.rad(20.0), 0.0F, Maths.rad(-20.0));
        this.animator.rotate(this.right_arm, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(-90.0), Maths.rad(10.0), 0.0F);
        this.animator.move(this.neck, -1.0F, 0.0F, -1.0F);
        this.animator.move(this.right_arm, 0.0F, 0.0F, 3.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, Maths.rad(5.0), 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.right_arm, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(-130.0), Maths.rad(10.0), 0.0F);
        this.animator.move(this.neck, -1.0F, 0.0F, -1.0F);
        this.animator.move(this.right_arm, 0.0F, 0.0F, 2.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    private void standPose() {
        this.animator.rotate(this.body, Maths.rad(-65.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, Maths.rad(65.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_leg, Maths.rad(65.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_leg, Maths.rad(65.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(65.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(65.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.move(this.body, 0.0F, 0.5F, -2.0F);
        this.animator.move(this.neck, 0.0F, -1.0F, -2.0F);
        this.animator.move(this.right_leg, 0.0F, -1.0F, 1.0F);
        this.animator.move(this.left_leg, 0.0F, -1.0F, 1.0F);
        this.animator.move(this.right_arm, -1.0F, 1.0F, -1.0F);
        this.animator.move(this.left_arm, 1.0F, 1.0F, -1.0F);
    }

    public void setupAnim(EntityGeladaMonkey entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        boolean running = entity.isAggro();
        float runSpeed = 0.7F;
        float runDegree = 0.7F;
        float walkSpeed = 0.9F;
        float walkDegree = 0.6F;
        float idleSpeed = 0.15F;
        float idleDegree = 0.5F;
        float stillProgress = (1.0F - limbSwingAmount) * 5.0F;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * (ageInTicks - (float) entity.f_19797_);
        this.progressRotationPrev(this.tail, stillProgress, Maths.rad(-40.0), 0.0F, 0.0F, 5.0F);
        this.swing(this.tail, idleSpeed, idleDegree, true, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.bob(this.neck, idleSpeed * 0.5F, idleDegree * 0.25F, false, ageInTicks, 1.0F);
        if (running) {
            this.walk(this.body, runSpeed, runDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.tail, runSpeed, runDegree * 0.5F, true, 0.0F, 0.3F, limbSwing, limbSwingAmount);
            this.walk(this.neck, runSpeed, runDegree * 0.2F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.right_arm, runSpeed, runDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.left_arm, runSpeed, runDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.right_leg, runSpeed, runDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.left_leg, runSpeed, runDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.right_leg, runSpeed, runDegree * 0.2F, true, 0.0F, -0.2F, limbSwing, limbSwingAmount);
            this.flap(this.left_leg, runSpeed, runDegree * 0.2F, false, 0.0F, -0.2F, limbSwing, limbSwingAmount);
            this.bob(this.body, runSpeed, runDegree * 3.0F, false, limbSwing, limbSwingAmount);
        } else {
            this.walk(this.tail, walkSpeed, -walkDegree * 0.2F, true, 1.0F, 0.1F, limbSwing, limbSwingAmount);
            this.walk(this.body, walkSpeed, walkDegree * 0.1F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.neck, walkSpeed, -walkDegree * 0.1F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.right_arm, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.left_arm, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.right_leg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.left_leg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.bob(this.right_leg, walkSpeed, walkDegree * -1.2F, true, limbSwing, limbSwingAmount);
            this.bob(this.left_leg, walkSpeed, walkDegree * -1.2F, true, limbSwing, limbSwingAmount);
        }
        this.progressRotationPrev(this.body, sitProgress, Maths.rad(-40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, sitProgress, Maths.rad(-45.0), Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, sitProgress, Maths.rad(-45.0), Maths.rad(20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, sitProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, sitProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, sitProgress, Maths.rad(50.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.neck, sitProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 5.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_leg, sitProgress, 0.0F, -2.5F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_leg, sitProgress, 0.0F, -2.5F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_arm, sitProgress, 0.0F, 2.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, sitProgress, 0.0F, 2.0F, 2.0F, 5.0F);
        this.neck.rotateAngleY += netHeadYaw / (180.0F / (float) Math.PI) * 0.5F;
        this.neck.rotateAngleX += headPitch / (180.0F / (float) Math.PI);
        if (entity.m_6162_()) {
            this.head.setScale(1.3F, 1.3F, 1.3F);
            this.neck.setScale(1.25F, 1.25F, 1.25F);
        } else {
            this.neck.setScale(1.0F, 1.0F, 1.0F);
            this.head.setScale(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail, this.torso, this.neck, this.head, this.left_arm, this.left_leg, this.right_arm, this.right_leg, this.mouth);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}