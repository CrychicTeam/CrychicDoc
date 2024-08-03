package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityBunfungus;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelBunfungus extends AdvancedEntityModel<EntityBunfungus> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox belly;

    public final AdvancedModelBox tail;

    public final AdvancedModelBox head;

    public final AdvancedModelBox left_brow;

    public final AdvancedModelBox right_brow;

    public final AdvancedModelBox shroom_cap;

    public final AdvancedModelBox left_ear;

    public final AdvancedModelBox right_ear;

    public final AdvancedModelBox snout;

    public final AdvancedModelBox snout_r1;

    public final AdvancedModelBox left_arm;

    public final AdvancedModelBox right_arm;

    public final AdvancedModelBox left_leg;

    public final AdvancedModelBox left_foot;

    public final AdvancedModelBox right_leg;

    public final AdvancedModelBox right_foot;

    private final ModelAnimator animator;

    public ModelBunfungus() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -13.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-10.0F, -10.0F, -10.0F, 20.0F, 20.0F, 19.0F, 0.0F, false);
        this.belly = new AdvancedModelBox(this, "belly");
        this.belly.setRotationPoint(0.0F, 4.0F, -4.3F);
        this.body.addChild(this.belly);
        this.belly.setTextureOffset(64, 25).addBox(-11.0F, -7.0F, -7.5F, 22.0F, 14.0F, 15.0F, -2.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, 10.0F, 9.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(60, 0).addBox(-3.0F, -5.0F, -1.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -10.0F, -6.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 66).addBox(-6.0F, -5.0F, -9.0F, 12.0F, 8.0F, 13.0F, 0.0F, false);
        this.left_brow = new AdvancedModelBox(this, "left_brow");
        this.left_brow.setRotationPoint(3.5F, -3.5F, -9.1F);
        this.head.addChild(this.left_brow);
        this.left_brow.setTextureOffset(90, 2).addBox(-2.5F, -0.5F, 0.0F, 5.0F, 1.0F, 0.0F, 0.0F, false);
        this.right_brow = new AdvancedModelBox(this, "right_brow");
        this.right_brow.setRotationPoint(-3.5F, -3.5F, -9.1F);
        this.head.addChild(this.right_brow);
        this.right_brow.setTextureOffset(90, 2).addBox(-2.5F, -0.5F, 0.0F, 5.0F, 1.0F, 0.0F, 0.0F, true);
        this.shroom_cap = new AdvancedModelBox(this, "shroom_cap");
        this.shroom_cap.setRotationPoint(0.0F, -5.0F, -4.0F);
        this.head.addChild(this.shroom_cap);
        this.shroom_cap.setTextureOffset(0, 40).addBox(-10.0F, -5.0F, -8.0F, 20.0F, 5.0F, 20.0F, 0.0F, false);
        this.left_ear = new AdvancedModelBox(this, "left_ear");
        this.left_ear.setRotationPoint(3.0F, -4.0F, 1.0F);
        this.shroom_cap.addChild(this.left_ear);
        this.setRotationAngle(this.left_ear, 0.0F, -0.6981F, 0.2182F);
        this.left_ear.setTextureOffset(0, 0).addBox(-2.0F, -12.0F, -1.0F, 4.0F, 12.0F, 2.0F, 0.0F, false);
        this.right_ear = new AdvancedModelBox(this, "right_ear");
        this.right_ear.setRotationPoint(-3.0F, -4.0F, 1.0F);
        this.shroom_cap.addChild(this.right_ear);
        this.setRotationAngle(this.right_ear, 0.0F, 0.6981F, -0.2182F);
        this.right_ear.setTextureOffset(0, 0).addBox(-2.0F, -12.0F, -1.0F, 4.0F, 12.0F, 2.0F, 0.0F, true);
        this.snout = new AdvancedModelBox(this, "snout");
        this.snout.setRotationPoint(0.0F, 0.0F, -10.0F);
        this.head.addChild(this.snout);
        this.snout.setTextureOffset(0, 40).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 4.0F, 2.0F, 0.0F, false);
        this.snout_r1 = new AdvancedModelBox(this, "snout_r1");
        this.snout_r1.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.snout.addChild(this.snout_r1);
        this.setRotationAngle(this.snout_r1, -0.1309F, 0.0F, 0.0F);
        this.snout_r1.setTextureOffset(0, 48).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 2.0F, 0.0F, 0.0F, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(9.5F, -4.0F, -9.5F);
        this.body.addChild(this.left_arm);
        this.setRotationAngle(this.left_arm, 0.0F, 0.0F, 0.1745F);
        this.left_arm.setTextureOffset(51, 77).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 8.0F, 5.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-9.5F, -4.0F, -9.5F);
        this.body.addChild(this.right_arm);
        this.setRotationAngle(this.right_arm, 0.0F, 0.0F, -0.1745F);
        this.right_arm.setTextureOffset(51, 77).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 8.0F, 5.0F, 0.0F, true);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(6.0F, 2.0F, 0.0F);
        this.body.addChild(this.left_leg);
        this.left_foot = new AdvancedModelBox(this, "left_foot");
        this.left_foot.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.left_leg.addChild(this.left_foot);
        this.setRotationAngle(this.left_foot, 0.0F, -1.1345F, 0.0F);
        this.left_foot.setTextureOffset(64, 55).addBox(-3.0F, -1.0F, -15.0F, 6.0F, 4.0F, 17.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-6.0F, 2.0F, 0.0F);
        this.body.addChild(this.right_leg);
        this.right_foot = new AdvancedModelBox(this, "right_foot");
        this.right_foot.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.right_leg.addChild(this.right_foot);
        this.setRotationAngle(this.right_foot, 0.0F, 1.1345F, 0.0F);
        this.right_foot.setTextureOffset(64, 55).addBox(-3.0F, -1.0F, -15.0F, 6.0F, 4.0F, 17.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.animator.update(entity);
        this.animator.setAnimation(EntityBunfungus.ANIMATION_EAT);
        this.animator.startKeyframe(4);
        this.animator.rotate(this.head, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-140.0), Maths.rad(-20.0), Maths.rad(70.0));
        this.animator.rotate(this.left_arm, Maths.rad(-140.0), Maths.rad(20.0), Maths.rad(-70.0));
        this.animator.move(this.head, 0.0F, -2.0F, -1.0F);
        this.animator.move(this.right_arm, 1.0F, 2.0F, 0.0F);
        this.animator.move(this.left_arm, -1.0F, 2.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.head, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-140.0), Maths.rad(-10.0), Maths.rad(70.0));
        this.animator.rotate(this.left_arm, Maths.rad(-140.0), Maths.rad(10.0), Maths.rad(-70.0));
        this.animator.move(this.head, 0.0F, -2.0F, -1.0F);
        this.animator.move(this.right_arm, 1.0F, 2.0F, 0.0F);
        this.animator.move(this.left_arm, -1.0F, 2.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.head, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-140.0), Maths.rad(-20.0), Maths.rad(70.0));
        this.animator.rotate(this.left_arm, Maths.rad(-140.0), Maths.rad(20.0), Maths.rad(-70.0));
        this.animator.move(this.head, 0.0F, -2.0F, -1.0F);
        this.animator.move(this.right_arm, 1.0F, 2.0F, 0.0F);
        this.animator.move(this.left_arm, -1.0F, 2.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.head, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-140.0), Maths.rad(-10.0), Maths.rad(70.0));
        this.animator.rotate(this.left_arm, Maths.rad(-140.0), Maths.rad(10.0), Maths.rad(-70.0));
        this.animator.move(this.head, 0.0F, -2.0F, -1.0F);
        this.animator.move(this.right_arm, 1.0F, 2.0F, 0.0F);
        this.animator.move(this.left_arm, -1.0F, 2.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(4);
        this.animator.endKeyframe();
        this.animator.setAnimation(EntityBunfungus.ANIMATION_BELLY);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-120.0), Maths.rad(30.0), Maths.rad(-10.0));
        this.animator.rotate(this.left_arm, Maths.rad(-120.0), Maths.rad(-30.0), Maths.rad(10.0));
        this.animator.move(this.belly, 0.0F, 0.0F, -10.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityBunfungus.ANIMATION_SLAM);
        this.animator.startKeyframe(5);
        this.animator.move(this.root, 0.0F, 0.0F, -20.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.shroom_cap, this.left_ear, this.right_ear, this.left_brow, this.right_brow, this.snout, this.snout_r1, this.left_arm, this.right_arm, new AdvancedModelBox[] { this.left_leg, this.right_leg, this.tail, this.belly, this.left_foot, this.right_foot });
    }

    public void setupAnim(EntityBunfungus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float walkSpeed = 0.7F;
        float walkDegree = 2.0F;
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float sleepProgress = entity.prevSleepProgress + (entity.sleepProgress - entity.prevSleepProgress) * partialTicks;
        float fallProgress = entity.prevReboundProgress + (entity.reboundProgress - entity.prevReboundProgress) * partialTicks;
        float jumpProgress = Math.max(0.0F, entity.prevJumpProgress + (entity.jumpProgress - entity.prevJumpProgress) * partialTicks - fallProgress);
        float interestedProgress = entity.prevInterestedProgress + (entity.interestedProgress - entity.prevInterestedProgress) * partialTicks;
        float walkMod = 1.0F - Math.max(jumpProgress, fallProgress) * 0.2F;
        float limbSwingMod = Math.min(limbSwingAmount, 0.38F) * walkMod;
        this.progressRotationPrev(this.body, sleepProgress, Maths.rad(90.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, sleepProgress, Maths.rad(-70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_ear, sleepProgress, Maths.rad(50.0), Maths.rad(80.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_ear, sleepProgress, Maths.rad(50.0), Maths.rad(-80.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.head, sleepProgress, Maths.rad(-95.0), 0.0F, Maths.rad(-5.0), 5.0F);
        this.progressRotationPrev(this.left_arm, sleepProgress, Maths.rad(-170.0), Maths.rad(-10.0), Maths.rad(35.0), 5.0F);
        this.progressRotationPrev(this.right_arm, sleepProgress, Maths.rad(-170.0), Maths.rad(10.0), Maths.rad(-35.0), 5.0F);
        this.progressRotationPrev(this.left_foot, sleepProgress, Maths.rad(70.0), Maths.rad(-30.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_foot, sleepProgress, Maths.rad(70.0), Maths.rad(30.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.body, sleepProgress, 0.0F, 3.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_leg, sleepProgress, 0.0F, 0.0F, -8.0F, 5.0F);
        this.progressPositionPrev(this.right_leg, sleepProgress, 0.0F, 0.0F, -8.0F, 5.0F);
        this.progressPositionPrev(this.left_arm, sleepProgress, 0.0F, -3.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, sleepProgress, 0.0F, -3.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.head, sleepProgress, 0.0F, -3.0F, -1.0F, 5.0F);
        this.progressRotationPrev(this.left_foot, limbSwingMod, 0.0F, Maths.rad(40.0), 0.0F, 0.38F);
        this.progressRotationPrev(this.right_foot, limbSwingMod, 0.0F, Maths.rad(-40.0), 0.0F, 0.38F);
        this.progressRotationPrev(this.left_ear, limbSwingMod, Maths.rad(-30.0), Maths.rad(30.0), 0.0F, 0.38F);
        this.progressRotationPrev(this.right_ear, limbSwingMod, Maths.rad(-30.0), Maths.rad(-30.0), 0.0F, 0.38F);
        this.progressRotationPrev(this.body, jumpProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_foot, jumpProgress, Maths.rad(70.0), Maths.rad(40.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_foot, jumpProgress, Maths.rad(70.0), Maths.rad(-40.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, jumpProgress, Maths.rad(-70.0), Maths.rad(40.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, jumpProgress, Maths.rad(-70.0), Maths.rad(-40.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.body, jumpProgress, 0.0F, -3.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, jumpProgress, 0.0F, -1.0F, 3.0F, 5.0F);
        this.progressRotationPrev(this.body, fallProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_foot, fallProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_foot, fallProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, fallProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, fallProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, fallProgress, Maths.rad(-130.0), Maths.rad(20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, fallProgress, Maths.rad(-130.0), Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.body, fallProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_foot, fallProgress, 0.0F, 1.0F, -1.0F, 5.0F);
        this.progressPositionPrev(this.right_foot, fallProgress, 0.0F, 1.0F, -1.0F, 5.0F);
        this.progressRotationPrev(this.head, interestedProgress, 0.0F, Maths.rad(-20.0), Maths.rad(-10.0), 5.0F);
        this.progressRotationPrev(this.right_brow, interestedProgress, 0.0F, 0.0F, Maths.rad(10.0), 5.0F);
        this.progressPositionPrev(this.right_brow, interestedProgress, -0.5F, -0.75F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_brow, interestedProgress, 0.0F, 0.5F, 0.0F, 5.0F);
        if (sleepProgress == 0.0F) {
            this.faceTarget(netHeadYaw, headPitch, 1.3F, new AdvancedModelBox[] { this.head });
        }
        this.flap(this.left_ear, idleSpeed, idleDegree, false, 1.0F, 0.2F, ageInTicks, 1.0F);
        this.flap(this.right_ear, idleSpeed, idleDegree, true, 1.0F, 0.2F, ageInTicks, 1.0F);
        this.swing(this.left_ear, idleSpeed, idleDegree, false, 2.0F, 0.2F, ageInTicks, 1.0F);
        this.swing(this.right_ear, idleSpeed, idleDegree, true, 2.0F, 0.2F, ageInTicks, 1.0F);
        this.walk(this.tail, idleSpeed, idleDegree, false, 2.0F, 0.2F, ageInTicks, 1.0F);
        this.walk(this.right_arm, idleSpeed, idleDegree, false, -2.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.left_arm, idleSpeed, idleDegree, false, -2.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.snout_r1, idleSpeed * 8.0F, idleDegree, false, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.body, walkSpeed, walkDegree * 0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingMod);
        this.swing(this.body, walkSpeed, walkDegree * 0.5F, false, 1.0F, 0.0F, limbSwing, limbSwingMod);
        this.swing(this.right_foot, walkSpeed, walkDegree * 0.5F, false, -2.5F, 0.0F, limbSwing, limbSwingMod);
        this.swing(this.left_foot, walkSpeed, walkDegree * 0.5F, false, -2.5F, 0.0F, limbSwing, limbSwingMod);
        this.left_foot.rotateAngleX = this.left_foot.rotateAngleX - (this.left_leg.rotateAngleX + this.body.rotateAngleX);
        this.left_foot.rotateAngleZ = this.left_foot.rotateAngleZ - this.body.rotateAngleZ;
        this.right_foot.rotateAngleX = this.right_foot.rotateAngleX - (this.right_leg.rotateAngleX + this.body.rotateAngleX);
        this.right_foot.rotateAngleZ = this.right_foot.rotateAngleZ - this.body.rotateAngleZ;
        this.left_leg.rotationPointY = this.left_leg.rotationPointY + 2.0F * (float) (Math.sin((double) (limbSwing * walkSpeed) + 2.5) * (double) limbSwingMod * (double) walkDegree - (double) (limbSwingMod * walkDegree));
        this.right_leg.rotationPointY = this.right_leg.rotationPointY + 2.0F * (float) (Math.sin(-((double) (limbSwing * walkSpeed)) + 2.5) * (double) limbSwingMod * (double) walkDegree - (double) (limbSwingMod * walkDegree));
        this.flap(this.head, walkSpeed, walkDegree * 0.5F, true, 0.0F, 0.0F, limbSwing, limbSwingMod);
        this.swing(this.head, walkSpeed, walkDegree * 0.5F, true, 1.0F, 0.0F, limbSwing, limbSwingMod);
        this.flap(this.tail, walkSpeed, walkDegree * 0.5F, true, 0.0F, 0.0F, limbSwing, limbSwingMod);
        this.swing(this.tail, walkSpeed, walkDegree * 0.5F, false, 2.0F, 0.0F, limbSwing, limbSwingMod);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}