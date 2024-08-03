package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntitySeaBear;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelSeaBear extends AdvancedEntityModel<EntitySeaBear> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox snout;

    private final AdvancedModelBox right_ear;

    private final AdvancedModelBox left_ear;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox right_arm;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox right_leg;

    private ModelAnimator animator;

    public ModelSeaBear() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -15.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-8.0F, -13.0F, -20.0F, 16.0F, 28.0F, 41.0F, 0.0F, false);
        this.body.setTextureOffset(0, 70).addBox(0.0F, -22.0F, -19.0F, 0.0F, 9.0F, 39.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, 6.2F, -22.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-5.0F, -5.0F, -6.0F, 10.0F, 10.0F, 8.0F, 0.0F, false);
        this.snout = new AdvancedModelBox(this, "snout");
        this.snout.setRotationPoint(0.0F, 0.0F, -6.0F);
        this.head.addChild(this.snout);
        this.snout.setTextureOffset(21, 19).addBox(-2.0F, 0.0F, -5.0F, 4.0F, 5.0F, 5.0F, 0.0F, false);
        this.right_ear = new AdvancedModelBox(this, "right_ear");
        this.right_ear.setRotationPoint(-3.5F, -5.0F, -3.0F);
        this.head.addChild(this.right_ear);
        this.right_ear.setTextureOffset(11, 19).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        this.left_ear = new AdvancedModelBox(this, "left_ear");
        this.left_ear.setRotationPoint(3.5F, -5.0F, -3.0F);
        this.head.addChild(this.left_ear);
        this.left_ear.setTextureOffset(11, 19).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, 1.0F, 21.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(79, 70).addBox(0.0F, -13.0F, 0.0F, 0.0F, 25.0F, 17.0F, 0.0F, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(6.6F, 15.0F, -10.0F);
        this.body.addChild(this.left_arm);
        this.left_arm.setTextureOffset(0, 70).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 13.0F, 9.0F, 0.0F, false);
        this.left_arm.setTextureOffset(0, 19).addBox(0.0F, 13.0F, -5.0F, 0.0F, 1.0F, 9.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-6.6F, 15.0F, -10.0F);
        this.body.addChild(this.right_arm);
        this.right_arm.setTextureOffset(0, 70).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 13.0F, 9.0F, 0.0F, true);
        this.right_arm.setTextureOffset(0, 19).addBox(0.0F, 13.0F, -5.0F, 0.0F, 1.0F, 9.0F, 0.0F, true);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(7.7F, 15.0F, 16.0F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(40, 70).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 8.0F, 6.0F, 0.0F, false);
        this.left_leg.setTextureOffset(15, 30).addBox(0.0F, 8.0F, -3.0F, 0.0F, 1.0F, 6.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-7.7F, 15.0F, 16.0F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(40, 70).addBox(0.0F, 0.0F, -3.0F, 1.0F, 8.0F, 6.0F, 0.0F, true);
        this.right_leg.setTextureOffset(15, 30).addBox(0.0F, 8.0F, -3.0F, 0.0F, 1.0F, 6.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.animator.update(entity);
        this.animator.setAnimation(EntitySeaBear.ANIMATION_POINT);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.head, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, Maths.rad(-10.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.tail, 0.0F, Maths.rad(10.0), 0.0F);
        this.animator.move(this.right_arm, 1.0F, 0.0F, -5.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-110.0), Maths.rad(10.0), Maths.rad(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, Maths.rad(-10.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.tail, 0.0F, Maths.rad(10.0), 0.0F);
        this.animator.move(this.right_arm, 1.0F, 0.0F, -7.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-90.0), Maths.rad(10.0), Maths.rad(-10.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntitySeaBear.ANIMATION_ATTACK);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.body, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.tail, 0.0F, Maths.rad(20.0), 0.0F);
        this.animator.move(this.right_arm, 1.0F, 0.0F, -5.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-110.0), Maths.rad(10.0), Maths.rad(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.body, 0.0F, Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.tail, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.move(this.left_arm, -1.0F, 0.0F, -5.0F);
        this.animator.rotate(this.left_arm, Maths.rad(-110.0), Maths.rad(-10.0), Maths.rad(10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.body, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.tail, 0.0F, Maths.rad(20.0), 0.0F);
        this.animator.move(this.right_arm, 1.0F, 0.0F, -5.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-110.0), Maths.rad(10.0), Maths.rad(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.body, 0.0F, Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.tail, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.move(this.left_arm, -1.0F, 0.0F, -5.0F);
        this.animator.rotate(this.left_arm, Maths.rad(-110.0), Maths.rad(-10.0), Maths.rad(10.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setupAnim(EntitySeaBear entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float idleSpeed = 0.14F;
        float idleDegree = 0.25F;
        float swimSpeed = 0.8F;
        float swimDegree = 0.75F;
        float landProgress = entity.prevOnLandProgress + (entity.onLandProgress - entity.prevOnLandProgress) * (ageInTicks - (float) entity.f_19797_);
        this.progressRotationPrev(this.body, landProgress, 0.0F, 0.0F, Maths.rad(-90.0), 5.0F);
        this.progressPositionPrev(this.body, landProgress, 0.0F, 8.0F, 0.0F, 5.0F);
        this.flap(this.left_arm, idleSpeed, idleDegree, true, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.right_arm, idleSpeed, idleDegree, false, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.left_leg, idleSpeed, idleDegree, true, 3.0F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.right_leg, idleSpeed, idleDegree, false, 3.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.tail, idleSpeed, idleDegree, true, 5.0F, 0.0F, ageInTicks, 1.0F);
        this.bob(this.body, idleSpeed, idleDegree * 2.0F, false, ageInTicks, 1.0F);
        this.walk(this.body, swimSpeed, swimDegree * 0.1F, false, -3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.body, swimSpeed, swimDegree * 0.2F, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.head, swimSpeed, swimDegree * 0.2F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_arm, swimSpeed, swimDegree * 0.3F, false, -3.0F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(this.right_arm, swimSpeed, swimDegree * 0.3F, false, -3.0F, 0.1F, limbSwing, limbSwingAmount);
        this.flap(this.left_arm, swimSpeed, swimDegree, true, 0.0F, 0.4F, limbSwing, limbSwingAmount);
        this.flap(this.right_arm, swimSpeed, swimDegree, false, 0.0F, 0.4F, limbSwing, limbSwingAmount);
        this.flap(this.left_leg, swimSpeed, swimDegree, true, 2.0F, 0.2F, limbSwing, limbSwingAmount);
        this.flap(this.right_leg, swimSpeed, swimDegree, false, 2.0F, 0.2F, limbSwing, limbSwingAmount);
        this.swing(this.tail, swimSpeed, swimDegree * 1.2F, true, 4.0F, 0.0F, limbSwing, limbSwingAmount);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.snout, this.right_arm, this.right_leg, this.right_ear, this.left_arm, this.left_leg, this.left_ear, this.tail);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}