package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityDropBear;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelDropBear extends AdvancedEntityModel<EntityDropBear> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox leg_left;

    private final AdvancedModelBox leg_right;

    private final AdvancedModelBox front_body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox nose;

    private final AdvancedModelBox ear_left;

    private final AdvancedModelBox ear_right;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox arm_left;

    private final AdvancedModelBox claws_left;

    private final AdvancedModelBox arm_right;

    private final AdvancedModelBox claws_right;

    private final ModelAnimator animator;

    public ModelDropBear() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -13.0F, 8.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 31).addBox(-6.0F, -8.0F, -7.0F, 12.0F, 13.0F, 13.0F, 0.0F, false);
        this.leg_left = new AdvancedModelBox(this, "leg_left");
        this.leg_left.setPos(3.4F, 5.0F, 2.5F);
        this.body.addChild(this.leg_left);
        this.leg_left.setTextureOffset(0, 58).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, 0.0F, false);
        this.leg_right = new AdvancedModelBox(this, "leg_right");
        this.leg_right.setPos(-3.4F, 5.0F, 2.5F);
        this.body.addChild(this.leg_right);
        this.leg_right.setTextureOffset(0, 58).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, 0.0F, true);
        this.front_body = new AdvancedModelBox(this, "front_body");
        this.front_body.setPos(0.0F, -2.0F, -7.0F);
        this.body.addChild(this.front_body);
        this.front_body.setTextureOffset(0, 0).addBox(-8.0F, -8.0F, -14.0F, 16.0F, 16.0F, 14.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -1.0F, -14.0F);
        this.front_body.addChild(this.head);
        this.head.setTextureOffset(42, 49).addBox(-5.0F, -5.0F, -9.0F, 10.0F, 8.0F, 9.0F, 0.0F, false);
        this.nose = new AdvancedModelBox(this, "nose");
        this.nose.setPos(0.0F, -0.5F, -9.5F);
        this.head.addChild(this.nose);
        this.nose.setTextureOffset(0, 7).addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        this.ear_left = new AdvancedModelBox(this, "ear_left");
        this.ear_left.setPos(2.75F, -2.75F, -3.5F);
        this.head.addChild(this.ear_left);
        this.ear_left.setTextureOffset(21, 58).addBox(-0.75F, -5.25F, -1.5F, 6.0F, 6.0F, 3.0F, 0.0F, false);
        this.ear_left.setTextureOffset(0, 0).addBox(2.25F, 0.75F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        this.ear_right = new AdvancedModelBox(this, "ear_right");
        this.ear_right.setPos(-2.75F, -2.75F, -3.5F);
        this.head.addChild(this.ear_right);
        this.ear_right.setTextureOffset(21, 58).addBox(-5.25F, -5.25F, -1.5F, 6.0F, 6.0F, 3.0F, 0.0F, true);
        this.ear_right.setTextureOffset(0, 0).addBox(-5.25F, 0.75F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, true);
        this.jaw = new AdvancedModelBox(this, "jaw");
        this.jaw.setPos(0.0F, 1.0F, 0.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(47, 0).addBox(-5.0F, 0.0F, -9.0F, 10.0F, 4.0F, 9.0F, 0.0F, false);
        this.arm_left = new AdvancedModelBox(this, "arm_left");
        this.arm_left.setPos(6.75F, 3.0F, -8.75F);
        this.front_body.addChild(this.arm_left);
        this.arm_left.setTextureOffset(56, 26).addBox(-1.75F, -3.0F, -2.25F, 5.0F, 14.0F, 5.0F, 0.0F, false);
        this.claws_left = new AdvancedModelBox(this, "claws_left");
        this.claws_left.setPos(0.25F, 11.0F, -2.25F);
        this.arm_left.addChild(this.claws_left);
        this.claws_left.setTextureOffset(61, 14).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 2.0F, 5.0F, 0.0F, false);
        this.arm_right = new AdvancedModelBox(this, "arm_right");
        this.arm_right.setPos(-6.75F, 3.0F, -8.75F);
        this.front_body.addChild(this.arm_right);
        this.arm_right.setTextureOffset(56, 26).addBox(-3.25F, -3.0F, -2.25F, 5.0F, 14.0F, 5.0F, 0.0F, true);
        this.claws_right = new AdvancedModelBox(this, "claws_right");
        this.claws_right.setPos(-0.25F, 11.0F, -2.25F);
        this.arm_right.addChild(this.claws_right);
        this.claws_right.setTextureOffset(61, 14).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 2.0F, 5.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityDropBear.ANIMATION_BITE);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(80.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, 0.0F, 2.0F);
        this.animator.move(this.ear_left, 0.0F, 0.0F, -2.0F);
        this.animator.move(this.ear_right, 0.0F, 0.0F, -2.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(1);
        this.animator.startKeyframe(2);
        this.animator.rotate(this.head, Maths.rad(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(1);
        this.animator.setAnimation(EntityDropBear.ANIMATION_SWIPE_L);
        this.animator.startKeyframe(7);
        this.animator.rotate(this.front_body, 0.0F, Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.arm_left, Maths.rad(65.0), 0.0F, Maths.rad(-100.0));
        this.animator.rotate(this.arm_right, Maths.rad(-15.0), 0.0F, Maths.rad(10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.front_body, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-90.0), 0.0F, Maths.rad(20.0));
        this.animator.rotate(this.arm_right, Maths.rad(-15.0), 0.0F, Maths.rad(20.0));
        this.animator.move(this.arm_left, 0.0F, 0.0F, -6.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(3);
        this.animator.setAnimation(EntityDropBear.ANIMATION_SWIPE_R);
        this.animator.startKeyframe(7);
        this.animator.rotate(this.front_body, 0.0F, Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.arm_right, Maths.rad(65.0), 0.0F, Maths.rad(100.0));
        this.animator.rotate(this.arm_left, Maths.rad(-15.0), 0.0F, Maths.rad(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.front_body, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-90.0), 0.0F, Maths.rad(-20.0));
        this.animator.rotate(this.arm_left, Maths.rad(-15.0), 0.0F, Maths.rad(-20.0));
        this.animator.move(this.arm_right, 0.0F, 0.0F, -6.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(3);
        this.animator.setAnimation(EntityDropBear.ANIMATION_JUMPUP);
        this.animator.startKeyframe(10);
        this.animator.move(this.body, 0.0F, 5.0F, 0.0F);
        this.animator.rotate(this.arm_right, 0.0F, 0.0F, Maths.rad(40.0));
        this.animator.rotate(this.arm_left, 0.0F, 0.0F, Maths.rad(-40.0));
        this.animator.rotate(this.leg_right, 0.0F, 0.0F, Maths.rad(40.0));
        this.animator.rotate(this.leg_left, 0.0F, 0.0F, Maths.rad(-40.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(EntityDropBear entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float upsideDownProgress = entityIn.prevUpsideDownProgress + (entityIn.upsideDownProgress - entityIn.prevUpsideDownProgress) * (ageInTicks - (float) entityIn.f_19797_);
        float walkSpeed = 0.7F;
        float walkDegree = 0.7F;
        float idleSpeed = 0.2F;
        float idleDegree = 0.1F;
        float invert = upsideDownProgress > 0.0F ? -1.0F : 1.0F;
        this.progressPositionPrev(this.body, upsideDownProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, upsideDownProgress, 0.0F, 0.0F, Maths.rad(180.0) * (entityIn.fallRotation ? -1.0F : 1.0F), 5.0F);
        this.walk(this.leg_left, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.leg_left, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.leg_right, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.leg_right, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.arm_right, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.arm_left, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.arm_left, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.bob(this.arm_right, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.flap(this.front_body, walkSpeed, walkDegree * 0.2F, false, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.head, walkSpeed, walkDegree * 0.2F, true, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.flap(this.ear_right, walkSpeed, walkDegree * 0.2F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.ear_left, walkSpeed, walkDegree * 0.2F, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.ear_right, idleSpeed, idleDegree, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.ear_left, idleSpeed, idleDegree, true, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.nose, idleSpeed * 0.5F, idleDegree, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.head.rotateAngleY += netHeadYaw * 0.9F * invert * (float) (Math.PI / 180.0);
        this.head.rotateAngleX += headPitch * 0.9F * invert * (float) (Math.PI / 180.0);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.leg_left, this.leg_right, this.arm_left, this.arm_right, this.head, this.ear_left, this.ear_right, this.nose, this.jaw, this.front_body, new AdvancedModelBox[] { this.claws_left, this.claws_right });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}