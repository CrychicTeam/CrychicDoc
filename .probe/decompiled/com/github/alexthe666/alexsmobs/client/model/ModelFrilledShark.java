package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityFrilledShark;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelFrilledShark extends AdvancedEntityModel<EntityFrilledShark> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox pectoralfin_left;

    private final AdvancedModelBox pectoralfin_right;

    private final AdvancedModelBox tail1;

    private final AdvancedModelBox pelvicfin_left;

    private final AdvancedModelBox pelvicfin_right;

    private final AdvancedModelBox tail2;

    private final ModelAnimator animator;

    public ModelFrilledShark() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -3.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-3.0F, -3.0F, -15.0F, 6.0F, 6.0F, 18.0F, 0.0F, false);
        this.body.setTextureOffset(66, 59).addBox(0.0F, -9.0F, -14.0F, 0.0F, 6.0F, 17.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -2.0F, -15.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(31, 0).addBox(-3.0F, -1.0F, -7.0F, 6.0F, 3.0F, 7.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this, "jaw");
        this.jaw.setPos(0.0F, 2.4F, 0.4F);
        this.head.addChild(this.jaw);
        this.setRotationAngle(this.jaw, 0.2618F, 0.0F, 0.0F);
        this.jaw.setTextureOffset(41, 25).addBox(-2.5F, 0.0F, -7.0F, 5.0F, 2.0F, 7.0F, 0.0F, false);
        this.pectoralfin_left = new AdvancedModelBox(this, "pectoralfin_left");
        this.pectoralfin_left.setPos(3.0F, 2.4F, -10.0F);
        this.body.addChild(this.pectoralfin_left);
        this.setRotationAngle(this.pectoralfin_left, 0.0F, 0.0F, 0.48F);
        this.pectoralfin_left.setTextureOffset(41, 42).addBox(0.0F, 0.0F, 0.0F, 5.0F, 0.0F, 7.0F, 0.0F, false);
        this.pectoralfin_right = new AdvancedModelBox(this, "pectoralfin_right");
        this.pectoralfin_right.setPos(-3.0F, 2.4F, -10.0F);
        this.body.addChild(this.pectoralfin_right);
        this.setRotationAngle(this.pectoralfin_right, 0.0F, 0.0F, -0.48F);
        this.pectoralfin_right.setTextureOffset(41, 42).addBox(-5.0F, 0.0F, 0.0F, 5.0F, 0.0F, 7.0F, 0.0F, true);
        this.tail1 = new AdvancedModelBox(this, "tail1");
        this.tail1.setPos(0.0F, -0.9F, 3.0F);
        this.body.addChild(this.tail1);
        this.tail1.setTextureOffset(21, 25).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 5.0F, 11.0F, 0.0F, false);
        this.tail1.setTextureOffset(0, 25).addBox(0.0F, -5.0F, 5.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);
        this.pelvicfin_left = new AdvancedModelBox(this, "pelvicfin_left");
        this.pelvicfin_left.setPos(2.0F, 3.0F, 5.0F);
        this.tail1.addChild(this.pelvicfin_left);
        this.setRotationAngle(this.pelvicfin_left, 0.0F, 0.0F, -0.9599F);
        this.pelvicfin_left.setTextureOffset(21, 25).addBox(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 5.0F, 0.0F, false);
        this.pelvicfin_right = new AdvancedModelBox(this, "pelvicfin_right");
        this.pelvicfin_right.setPos(-2.0F, 3.0F, 5.0F);
        this.tail1.addChild(this.pelvicfin_right);
        this.setRotationAngle(this.pelvicfin_right, 0.0F, 0.0F, 0.9599F);
        this.pelvicfin_right.setTextureOffset(21, 25).addBox(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 5.0F, 0.0F, true);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setPos(0.0F, 0.1F, 11.0F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(0, 25).addBox(0.0F, -6.0F, 0.0F, 0.0F, 11.0F, 20.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.animator.update(entity);
        this.animator.setAnimation(EntityFrilledShark.ANIMATION_ATTACK);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.jaw, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, 0.5F, 3.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(40.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(2);
    }

    public void setupAnim(EntityFrilledShark entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[] { this.head, this.body, this.tail1, this.tail2 };
        float idleSpeed = 0.14F;
        float idleDegree = 0.25F;
        float swimSpeed = 0.8F;
        float swimDegree = 0.75F;
        float landProgress = entityIn.prevOnLandProgress + (entityIn.onLandProgress - entityIn.prevOnLandProgress) * (ageInTicks - (float) entityIn.f_19797_);
        this.progressRotationPrev(this.body, landProgress, 0.0F, 0.0F, Maths.rad(-100.0), 5.0F);
        this.progressRotationPrev(this.pectoralfin_right, landProgress, 0.0F, 0.0F, Maths.rad(-50.0), 5.0F);
        this.progressRotationPrev(this.pectoralfin_left, landProgress, 0.0F, 0.0F, Maths.rad(50.0), 5.0F);
        this.walk(this.jaw, idleSpeed, idleDegree, true, 1.0F, -0.1F, ageInTicks, 1.0F);
        if (landProgress >= 5.0F) {
            this.chainWave(tailBoxes, idleSpeed, idleDegree * 0.9F, -3.0, ageInTicks, 1.0F);
            this.flap(this.pectoralfin_right, idleSpeed, idleDegree * 2.0F, true, 3.0F, 0.3F, ageInTicks, 1.0F);
            this.flap(this.pectoralfin_left, idleSpeed, idleDegree * -2.0F, true, 3.0F, 0.1F, ageInTicks, 1.0F);
        } else {
            this.chainSwing(tailBoxes, swimSpeed, swimDegree * 0.9F, -3.0, limbSwing, limbSwingAmount);
            this.flap(this.pectoralfin_right, swimSpeed, swimDegree, true, 1.0F, 0.3F, limbSwing, limbSwingAmount);
            this.flap(this.pectoralfin_left, swimSpeed, swimDegree, true, 1.0F, 0.3F, limbSwing, limbSwingAmount);
            this.flap(this.pelvicfin_right, swimSpeed, -swimDegree, true, 3.0F, 0.1F, limbSwing, limbSwingAmount);
            this.flap(this.pelvicfin_left, swimSpeed, -swimDegree, true, 3.0F, 0.1F, limbSwing, limbSwingAmount);
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.tail1, this.tail2, this.jaw, this.pectoralfin_left, this.pectoralfin_right, this.pelvicfin_left, this.pelvicfin_right);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}