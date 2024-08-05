package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityBison;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelBisonBaby extends AdvancedEntityModel<EntityBison> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox torso;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox right_arm;

    private final AdvancedModelBox head;

    private final AdvancedModelBox left_ear;

    private final AdvancedModelBox right_ear;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox right_leg;

    private final ModelAnimator animator;

    public ModelBisonBaby() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -13.5F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 19).addBox(-3.0F, -3.5F, -1.0F, 6.0F, 8.0F, 10.0F, 0.0F, false);
        this.torso = new AdvancedModelBox(this, "torso");
        this.torso.setRotationPoint(0.0F, -0.4F, 0.0F);
        this.body.addChild(this.torso);
        this.torso.setTextureOffset(0, 0).addBox(-3.5F, -4.0F, -9.0F, 7.0F, 9.0F, 9.0F, 0.0F, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(2.4F, 3.9F, -6.5F);
        this.torso.addChild(this.left_arm);
        this.left_arm.setTextureOffset(0, 38).addBox(-1.0F, 1.0F, -1.5F, 2.0F, 9.0F, 3.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-2.4F, 3.9F, -6.5F);
        this.torso.addChild(this.right_arm);
        this.right_arm.setTextureOffset(0, 38).addBox(-1.0F, 1.0F, -1.5F, 2.0F, 9.0F, 3.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -2.1F, -9.0F);
        this.torso.addChild(this.head);
        this.setRotationAngle(this.head, 0.6981F, 0.0F, 0.0F);
        this.head.setTextureOffset(24, 10).addBox(-2.5F, -3.0F, -8.0F, 5.0F, 6.0F, 9.0F, 0.0F, false);
        this.left_ear = new AdvancedModelBox(this, "left_ear");
        this.left_ear.setRotationPoint(2.5F, -2.6F, -0.3F);
        this.head.addChild(this.left_ear);
        this.setRotationAngle(this.left_ear, -0.8378F, -1.3875F, 0.8727F);
        this.left_ear.setTextureOffset(24, 0).addBox(0.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        this.right_ear = new AdvancedModelBox(this, "right_ear");
        this.right_ear.setRotationPoint(-2.5F, -2.6F, -0.3F);
        this.head.addChild(this.right_ear);
        this.setRotationAngle(this.right_ear, -0.8378F, 1.3875F, -0.8727F);
        this.right_ear.setTextureOffset(24, 0).addBox(-4.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -1.5F, 9.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, 0.2618F, 0.0F, 0.0F);
        this.tail.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 6.0F, 1.0F, 0.0F, false);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(1.8F, 3.5F, 7.5F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(33, 26).addBox(-1.0F, 1.0F, -1.5F, 2.0F, 9.0F, 3.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-1.8F, 3.5F, 7.5F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(33, 26).addBox(-1.0F, 1.0F, -1.5F, 2.0F, 9.0F, 3.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
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
    }

    private void eatPose() {
        this.animator.rotate(this.head, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.torso, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.move(this.torso, 0.0F, 0.0F, 2.0F);
        this.animator.rotate(this.left_arm, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.move(this.left_arm, 0.0F, -2.5F, 0.0F);
        this.animator.move(this.right_arm, 0.0F, -2.5F, 0.0F);
        this.animator.move(this.head, 0.0F, 4.0F, 0.0F);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.torso, this.head, this.left_ear, this.right_ear, this.left_leg, this.right_leg, this.tail, this.right_arm, this.left_arm);
    }

    public void setupAnim(EntityBison entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 0.4F;
        float walkDegree = 0.7F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        this.walk(this.right_arm, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_arm, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_leg, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_leg, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.tail, walkSpeed, walkDegree * 0.1F, true, 1.0F, -0.6F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree, true, limbSwing, limbSwingAmount);
        this.bob(this.head, walkSpeed, -walkDegree, false, limbSwing, limbSwingAmount);
        this.swing(this.left_ear, idleSpeed, idleDegree * 0.5F, true, 3.0F, -0.2F, ageInTicks, 1.0F);
        this.swing(this.right_ear, idleSpeed, idleDegree * 0.5F, true, 3.0F, 0.2F, ageInTicks, 1.0F);
        this.walk(this.tail, idleSpeed, idleDegree, false, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.bob(this.head, idleSpeed, idleDegree, false, ageInTicks, 1.0F);
        this.head.rotateAngleY += netHeadYaw * 0.75F * (float) (Math.PI / 180.0);
        this.head.rotateAngleX += headPitch * (float) (Math.PI / 180.0);
    }
}