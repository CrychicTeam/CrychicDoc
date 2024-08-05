package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityBison;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelBison extends AdvancedEntityModel<EntityBison> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox right_leg;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail_r1;

    private final AdvancedModelBox torso;

    private final AdvancedModelBox head;

    private final AdvancedModelBox horn_r1;

    private final AdvancedModelBox left_ear;

    private final AdvancedModelBox right_ear;

    private final AdvancedModelBox beard;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox right_arm;

    private final ModelAnimator animator;

    public ModelBison() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -23.0F, 4.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 54).addBox(-9.0F, -11.0F, -1.0F, 18.0F, 20.0F, 19.0F, 0.0F, false);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(5.8F, 5.0F, 14.0F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(75, 80).addBox(-3.0F, 4.0F, -3.0F, 6.0F, 14.0F, 7.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-5.8F, 5.0F, 14.0F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(75, 80).addBox(-3.0F, 4.0F, -3.0F, 6.0F, 14.0F, 7.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -6.0F, 18.0F);
        this.body.addChild(this.tail);
        this.tail_r1 = new AdvancedModelBox(this, "tail_r1");
        this.tail_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.tail.addChild(this.tail_r1);
        this.setRotationAngle(this.tail_r1, 0.0436F, 0.0F, 0.0F);
        this.tail_r1.setTextureOffset(0, 54).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 14.0F, 0.0F, 0.0F, false);
        this.torso = new AdvancedModelBox(this, "torso");
        this.torso.setRotationPoint(0.0F, -3.0F, 1.0F);
        this.body.addChild(this.torso);
        this.torso.setTextureOffset(0, 0).addBox(-10.0F, -14.0F, -28.0F, 20.0F, 27.0F, 26.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, 0.0F, -27.0F);
        this.torso.addChild(this.head);
        this.setRotationAngle(this.head, -0.2618F, 0.0F, 0.0F);
        this.head.setTextureOffset(76, 54).addBox(-4.0F, 0.0F, -8.0F, 8.0F, 15.0F, 10.0F, 0.0F, false);
        this.head.setTextureOffset(67, 0).addBox(-6.0F, -6.0F, -9.9F, 12.0F, 10.0F, 12.0F, 0.0F, false);
        this.horn_r1 = new AdvancedModelBox(this, "horn_r1");
        this.horn_r1.setRotationPoint(-7.0F, 0.5F, -5.0F);
        this.head.addChild(this.horn_r1);
        this.setRotationAngle(this.horn_r1, 0.3927F, 0.0F, 0.0F);
        this.horn_r1.setTextureOffset(0, 0).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        this.horn_r1.setTextureOffset(11, 1).addBox(1.0F, -0.5F, -1.0F, 3.0F, 3.0F, 2.0F, 0.0F, true);
        this.horn_r1.setTextureOffset(11, 1).addBox(10.0F, -0.5F, -1.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);
        this.horn_r1.setTextureOffset(0, 0).addBox(13.0F, -4.5F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        this.left_ear = new AdvancedModelBox(this, "left_ear");
        this.left_ear.setRotationPoint(4.0F, 3.0F, -3.0F);
        this.head.addChild(this.left_ear);
        this.setRotationAngle(this.left_ear, 0.0F, -0.6981F, 0.4363F);
        this.left_ear.setTextureOffset(0, 23).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 1.0F, 0.0F, false);
        this.right_ear = new AdvancedModelBox(this, "right_ear");
        this.right_ear.setRotationPoint(-4.0F, 3.0F, -3.0F);
        this.head.addChild(this.right_ear);
        this.setRotationAngle(this.right_ear, 0.0F, 0.6981F, -0.4363F);
        this.right_ear.setTextureOffset(0, 23).addBox(-5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 1.0F, 0.0F, true);
        this.beard = new AdvancedModelBox(this, "beard");
        this.beard.setRotationPoint(0.0F, 15.0F, 1.0F);
        this.head.addChild(this.beard);
        this.setRotationAngle(this.beard, 0.2182F, 0.0F, 0.0F);
        this.beard.setTextureOffset(0, 0).addBox(0.0F, -5.0F, -5.0F, 0.0F, 13.0F, 10.0F, 0.0F, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(7.8F, 10.0F, -15.0F);
        this.torso.addChild(this.left_arm);
        this.left_arm.setTextureOffset(93, 23).addBox(-3.0F, 3.0F, -3.0F, 5.0F, 13.0F, 5.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-7.8F, 10.0F, -15.0F);
        this.torso.addChild(this.right_arm);
        this.right_arm.setTextureOffset(93, 23).addBox(-2.0F, 3.0F, -3.0F, 5.0F, 13.0F, 5.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityBison.ANIMATION_PREPARE_CHARGE);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.torso, 0.0F, Maths.rad(15.0), 0.0F);
        this.animator.rotate(this.body, 0.0F, Maths.rad(5.0), 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, 0.0F, Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.torso, 0.0F, Maths.rad(-15.0), 0.0F);
        this.animator.rotate(this.body, 0.0F, Maths.rad(-5.0), 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.torso, 0.0F, Maths.rad(15.0), 0.0F);
        this.animator.rotate(this.body, 0.0F, Maths.rad(5.0), 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(40.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.torso, 0.0F, Maths.rad(-15.0), 0.0F);
        this.animator.rotate(this.body, 0.0F, Maths.rad(-5.0), 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityBison.ANIMATION_EAT);
        this.animator.startKeyframe(5);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.eatPose();
        this.animator.move(this.head, 0.0F, 1.0F, 1.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.eatPose();
        this.animator.move(this.head, 0.0F, 1.0F, 1.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.eatPose();
        this.animator.move(this.head, 0.0F, 1.0F, 1.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityBison.ANIMATION_ATTACK);
        this.animator.startKeyframe(5);
        this.animator.move(this.head, 0.0F, 2.0F, 1.0F);
        this.animator.rotate(this.head, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, -2.0F, 0.0F);
        this.animator.move(this.left_arm, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.right_arm, 0.0F, -1.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_leg, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_leg, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    private void eatPose() {
        this.animator.rotate(this.head, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.torso, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.move(this.torso, 0.0F, 0.0F, 2.0F);
        this.animator.rotate(this.left_arm, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.beard, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.move(this.left_arm, 0.0F, -5.0F, -1.0F);
        this.animator.move(this.right_arm, 0.0F, -5.0F, -1.0F);
        this.animator.move(this.head, 0.0F, 4.0F, 1.0F);
    }

    public void setupAnim(EntityBison entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 0.7F;
        float walkDegree = 0.6F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float runProgress = entity.prevChargeProgress + (entity.chargeProgress - entity.prevChargeProgress) * partialTick;
        this.progressPositionPrev(this.head, runProgress, 0.0F, 1.0F, -3.5F, 5.0F);
        this.progressRotationPrev(this.head, runProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        if (runProgress > 0.0F) {
            this.walk(this.right_arm, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.left_arm, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.right_arm, walkSpeed, walkDegree * 0.25F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.left_arm, walkSpeed, walkDegree * 0.25F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.right_leg, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.left_leg, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.right_leg, walkSpeed, walkDegree * 0.25F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.left_leg, walkSpeed, walkDegree * 0.25F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.tail, walkSpeed, walkDegree * 0.2F, true, 1.0F, -0.6F, limbSwing, limbSwingAmount);
            this.bob(this.body, walkSpeed * 0.5F, walkDegree * 5.0F, true, limbSwing, limbSwingAmount);
            this.bob(this.head, walkSpeed * 0.5F, -walkDegree * 2.0F, false, limbSwing, limbSwingAmount);
        } else {
            this.walk(this.right_arm, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.left_arm, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.right_leg, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.left_leg, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.tail, walkSpeed, walkDegree * 0.1F, true, 1.0F, -0.6F, limbSwing, limbSwingAmount);
            this.bob(this.body, walkSpeed, walkDegree, true, limbSwing, limbSwingAmount);
            this.bob(this.head, walkSpeed, -walkDegree, false, limbSwing, limbSwingAmount);
        }
        this.flap(this.beard, idleSpeed, idleDegree, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.left_ear, idleSpeed, idleDegree * 0.5F, true, 3.0F, -0.2F, ageInTicks, 1.0F);
        this.swing(this.right_ear, idleSpeed, idleDegree * 0.5F, true, 3.0F, 0.2F, ageInTicks, 1.0F);
        this.walk(this.tail, idleSpeed, idleDegree, false, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.bob(this.head, idleSpeed, idleDegree, false, ageInTicks, 1.0F);
        this.head.rotateAngleY += netHeadYaw * 0.35F * (float) (Math.PI / 180.0);
        this.head.rotateAngleX += headPitch * (float) (Math.PI / 180.0);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.left_arm, this.right_arm, this.head, this.tail, this.tail_r1, this.horn_r1, this.beard, this.left_leg, this.right_leg, this.left_ear, new AdvancedModelBox[] { this.right_ear, this.torso });
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }
}