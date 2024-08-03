package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;
import com.github.alexthe666.iceandfire.entity.EntityDreadGhoul;

public class ModelDreadGhoul extends ModelBipedBase<EntityDreadGhoul> {

    public AdvancedModelBox head2;

    public AdvancedModelBox clawsRight;

    public AdvancedModelBox clawsLeft;

    public ModelDreadGhoul(float modelScale) {
        this.texWidth = 128;
        this.texHeight = 64;
        this.head = new HideableModelRenderer(this, 0, 0);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -7.4F, -4.0F, 8.0F, 8.0F, 8.0F, modelScale);
        this.setRotateAngle(this.head, 0.045553092F, 0.0F, 0.0F);
        this.legLeft = new HideableModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.setPos(1.9F, 12.0F, 0.1F);
        this.legLeft.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelScale);
        this.setRotateAngle(this.legLeft, -0.045553092F, 0.0F, 0.0F);
        this.armRight = new HideableModelRenderer(this, 40, 16);
        this.armRight.setPos(-4.0F, 2.0F, 0.0F);
        this.armRight.addBox(-3.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, modelScale);
        this.setRotateAngle(this.armRight, -0.13665928F, 0.091106184F, 0.22759093F);
        this.armLeft = new HideableModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.setPos(4.0F, 2.0F, -0.0F);
        this.armLeft.addBox(0.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, modelScale);
        this.setRotateAngle(this.armLeft, -0.13665928F, 0.091106184F, -0.22759093F);
        this.head2 = new AdvancedModelBox(this, 32, 0);
        this.head2.setPos(0.0F, 0.4F, 0.0F);
        this.head2.addBox(-4.5F, -6.4F, -4.1F, 9.0F, 8.0F, 8.0F, modelScale);
        this.clawsLeft = new AdvancedModelBox(this, 56, 25);
        this.clawsLeft.mirror = true;
        this.clawsLeft.setPos(-0.5F, 11.0F, 0.0F);
        this.clawsLeft.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, modelScale);
        this.setRotateAngle(this.clawsLeft, -0.0F, 0.0F, 0.28972465F);
        this.body = new HideableModelRenderer(this, 16, 16);
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, modelScale);
        this.setRotateAngle(this.body, 0.045553092F, 0.0F, 0.0F);
        this.legRight = new HideableModelRenderer(this, 0, 16);
        this.legRight.setPos(-1.9F, 12.0F, 0.1F);
        this.legRight.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelScale);
        this.setRotateAngle(this.legRight, -0.045553092F, 0.0F, 0.0F);
        this.clawsRight = new AdvancedModelBox(this, 56, 25);
        this.clawsRight.setPos(0.5F, 11.0F, 0.0F);
        this.clawsRight.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, modelScale);
        this.setRotateAngle(this.clawsRight, 0.0F, 0.0F, -0.28972465F);
        this.body.addChild(this.head);
        this.body.addChild(this.legRight);
        this.body.addChild(this.legLeft);
        this.body.addChild(this.armRight);
        this.body.addChild(this.armLeft);
        this.head.addChild(this.head2);
        this.armLeft.addChild(this.clawsLeft);
        this.armRight.addChild(this.clawsRight);
        this.animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    public void setupAnim(EntityDreadGhoul thrall, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
        this.animate(thrall, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.0F);
        float speed_walk = 0.6F;
        float speed_idle = 0.05F;
        float degree_walk = 1.0F;
        if (thrall.getAnimation() == EntityDreadGhoul.ANIMATION_SPAWN && thrall.getAnimationTick() < 30) {
            this.swing(this.armRight, 0.5F, 0.5F, false, 2.0F, -0.7F, (float) thrall.f_19797_, 1.0F);
            this.swing(this.armLeft, 0.5F, 0.5F, true, 2.0F, -0.7F, (float) thrall.f_19797_, 1.0F);
            this.flap(this.armRight, 0.5F, 0.5F, true, 1.0F, 0.0F, (float) thrall.f_19797_, 1.0F);
            this.flap(this.armLeft, 0.5F, 0.5F, true, 1.0F, 0.0F, (float) thrall.f_19797_, 1.0F);
        }
        this.flap(this.armLeft, speed_idle, 0.15F, false, 2.0F, -0.1F, (float) thrall.f_19797_, 1.0F);
        this.flap(this.armRight, speed_idle, 0.15F, true, 2.0F, -0.1F, (float) thrall.f_19797_, 1.0F);
        this.walk(this.head, speed_idle, 0.1F, true, 1.0F, -0.05F, (float) thrall.f_19797_, 1.0F);
        this.walk(this.legRight, speed_walk, degree_walk, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.legLeft, speed_walk, degree_walk, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.legRight, speed_walk, degree_walk * 0.1F, true, 3.0F, -0.05F, limbSwing, limbSwingAmount);
        this.flap(this.legLeft, speed_walk, degree_walk * 0.1F, true, 3.0F, 0.05F, limbSwing, limbSwingAmount);
        this.flap(this.body, speed_walk, degree_walk * 0.1F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.armRight, speed_walk, degree_walk, true, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.armLeft, speed_walk, degree_walk, false, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.armRight, speed_walk, degree_walk * 0.8F, true, -2.0F, -0.1F, limbSwing, limbSwingAmount);
        this.flap(this.armLeft, speed_walk, degree_walk * 0.8F, true, -2.0F, 0.1F, limbSwing, limbSwingAmount);
        this.flap(this.head, speed_walk, degree_walk * 0.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
    }

    void animate(EntityDreadGhoul entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float f) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        if (this.animator.setAnimation(EntityDreadGhoul.ANIMATION_SLASH)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.armRight, 20.0F, 45.0F, 80.0F);
            this.rotate(this.animator, this.body, 0.0F, 30.0F, 0.0F);
            this.rotate(this.animator, this.head, 0.0F, -20.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.armRight, -80.0F, -15.0F, 10.0F);
            this.rotate(this.animator, this.body, 0.0F, -70.0F, 0.0F);
            this.rotate(this.animator, this.head, 0.0F, 60.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.armLeft, 20.0F, -45.0F, -80.0F);
            this.rotate(this.animator, this.body, 0.0F, -30.0F, 0.0F);
            this.rotate(this.animator, this.head, 0.0F, 20.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.armLeft, -80.0F, 15.0F, -10.0F);
            this.rotate(this.animator, this.body, 0.0F, 70.0F, 0.0F);
            this.rotate(this.animator, this.head, 0.0F, -60.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityDreadGhoul.ANIMATION_SPAWN)) {
            this.animator.startKeyframe(0);
            this.animator.move(this.body, 0.0F, 35.0F, 0.0F);
            this.rotateMinus(this.animator, this.armLeft, -180.0F, -90.0F, 50.0F);
            this.rotateMinus(this.animator, this.head, -60.0F, 0.0F, 0.0F);
            this.rotateMinus(this.animator, this.armRight, -180.0F, 90.0F, -50.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(30);
            this.animator.move(this.body, 0.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.armLeft, -30.0F, -90.0F, 0.0F);
            this.rotate(this.animator, this.armRight, -30.0F, 90.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
    }
}