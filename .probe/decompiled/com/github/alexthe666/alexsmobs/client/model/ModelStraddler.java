package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityStraddler;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelStraddler extends AdvancedEntityModel<EntityStraddler> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox hair;

    public final AdvancedModelBox horn_left;

    public final AdvancedModelBox hair_left;

    public final AdvancedModelBox horn_right;

    public final AdvancedModelBox hair_right;

    public final AdvancedModelBox leg_left;

    public final AdvancedModelBox leg_right;

    private ModelAnimator animator;

    public ModelStraddler() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -14.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-14.0F, -12.0F, -7.0F, 28.0F, 11.0F, 14.0F, 0.0F, false);
        this.hair = new AdvancedModelBox(this, "hair");
        this.hair.setPos(0.0F, -13.0F, 0.0F);
        this.body.addChild(this.hair);
        this.hair.setTextureOffset(23, 26).addBox(-6.0F, -4.0F, 0.0F, 12.0F, 5.0F, 0.0F, 0.0F, false);
        this.horn_left = new AdvancedModelBox(this, "horn_left");
        this.horn_left.setPos(9.5F, -12.0F, -4.0F);
        this.body.addChild(this.horn_left);
        this.horn_left.setTextureOffset(0, 26).addBox(-2.5F, -18.0F, 0.0F, 6.0F, 18.0F, 10.0F, 0.0F, false);
        this.hair_left = new AdvancedModelBox(this, "hair_left");
        this.hair_left.setPos(-2.5F, -17.0F, 10.0F);
        this.horn_left.addChild(this.hair_left);
        this.setRotationAngle(this.hair_left, -0.5672F, -0.2618F, 0.0F);
        this.hair_left.setTextureOffset(33, 33).addBox(0.0F, 0.0F, 0.0F, 0.0F, 6.0F, 16.0F, 0.0F, false);
        this.horn_right = new AdvancedModelBox(this, "horn_right");
        this.horn_right.setPos(-9.5F, -12.0F, -4.0F);
        this.body.addChild(this.horn_right);
        this.horn_right.setTextureOffset(0, 26).addBox(-3.5F, -18.0F, 0.0F, 6.0F, 18.0F, 10.0F, 0.0F, true);
        this.hair_right = new AdvancedModelBox(this, "hair_right");
        this.hair_right.setPos(2.5F, -17.0F, 10.0F);
        this.horn_right.addChild(this.hair_right);
        this.setRotationAngle(this.hair_right, -0.5672F, 0.2618F, 0.0F);
        this.hair_right.setTextureOffset(33, 33).addBox(0.0F, 0.0F, 0.0F, 0.0F, 6.0F, 16.0F, 0.0F, true);
        this.leg_left = new AdvancedModelBox(this, "leg_left");
        this.leg_left.setPos(7.0F, -0.5F, 0.0F);
        this.body.addChild(this.leg_left);
        this.leg_left.setTextureOffset(50, 26).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 15.0F, 6.0F, 0.0F, false);
        this.leg_right = new AdvancedModelBox(this, "leg_right");
        this.leg_right.setPos(-7.0F, -0.5F, 0.0F);
        this.body.addChild(this.leg_right);
        this.leg_right.setTextureOffset(50, 26).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 15.0F, 6.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityStraddler.ANIMATION_LAUNCH);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, Maths.rad(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.horn_right, 0.0F, 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.horn_left, 0.0F, 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.hair_left, 0.0F, Maths.rad(-70.0), 0.0F);
        this.animator.rotate(this.hair_right, 0.0F, Maths.rad(70.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.body, Maths.rad(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.horn_right, Maths.rad(-30.0), 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.horn_left, Maths.rad(-30.0), 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.hair_left, Maths.rad(20.0), Maths.rad(-15.0), 0.0F);
        this.animator.rotate(this.hair_right, Maths.rad(20.0), Maths.rad(15.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.horn_right, Maths.rad(20.0), 0.0F, Maths.rad(0.0));
        this.animator.rotate(this.horn_left, Maths.rad(20.0), 0.0F, Maths.rad(0.0));
        this.animator.rotate(this.hair_left, Maths.rad(20.0), Maths.rad(-160.0), 0.0F);
        this.animator.rotate(this.hair_right, Maths.rad(20.0), Maths.rad(160.0), 0.0F);
        this.animator.move(this.horn_left, 0.0F, 2.4F, 0.0F);
        this.animator.move(this.horn_right, 0.0F, 2.4F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.hair, this.hair_left, this.hair_right, this.horn_left, this.horn_right, this.leg_left, this.leg_right);
    }

    public void setupAnim(EntityStraddler entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 0.5F;
        float walkDegree = 0.6F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        this.walk(this.hair_left, idleSpeed, idleDegree, false, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.swing(this.hair_left, idleSpeed, idleDegree, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.hair_right, idleSpeed, idleDegree, false, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.swing(this.hair_right, idleSpeed, idleDegree, true, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.hair, idleSpeed, idleDegree, false, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.leg_right, walkSpeed, walkDegree * 1.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leg_left, walkSpeed, walkDegree * 1.5F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.body, walkSpeed, walkDegree * 0.3F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.body, walkSpeed, walkDegree * 0.3F, false, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.body, walkSpeed, walkDegree * 0.3F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.hair_left, walkSpeed, walkDegree * 0.8F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.hair_right, walkSpeed, walkDegree * 0.8F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        if (entity.m_20197_().size() <= 0) {
            this.body.rotateAngleX += headPitch * 0.5F * (float) (Math.PI / 180.0);
            this.leg_right.rotateAngleX -= headPitch * 0.5F * (float) (Math.PI / 180.0);
            this.leg_left.rotateAngleX -= headPitch * 0.5F * (float) (Math.PI / 180.0);
        }
    }

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}