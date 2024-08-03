package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class UnderzealotModel extends AdvancedEntityModel<UnderzealotEntity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox head;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox nose;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox lleg;

    private final ModelAnimator animator;

    public boolean noBurrowing;

    public UnderzealotModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, -8.0F, 1.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 37).addBox(-6.5F, -7.0F, -5.0F, 13.0F, 11.0F, 10.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(-6.5F, -7.0F, -5.0F, 13.0F, 14.0F, 10.0F, 0.25F, false);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(-6.25F, -6.0F, -0.5F);
        this.body.addChild(this.rarm);
        this.rarm.setTextureOffset(34, 58).addBox(-2.75F, -1.0F, -2.5F, 2.0F, 9.0F, 5.0F, 0.0F, true);
        this.rarm.setTextureOffset(59, 62).addBox(-2.75F, 8.0F, -2.5F, 2.0F, 3.0F, 5.0F, 0.0F, true);
        this.rarm.setTextureOffset(20, 58).addBox(-2.75F, -1.0F, -2.5F, 2.0F, 9.0F, 5.0F, 0.25F, true);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(6.25F, -6.0F, -0.5F);
        this.body.addChild(this.larm);
        this.larm.setTextureOffset(34, 58).addBox(0.75F, -1.0F, -2.5F, 2.0F, 9.0F, 5.0F, 0.0F, false);
        this.larm.setTextureOffset(59, 62).addBox(0.75F, 8.0F, -2.5F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        this.larm.setTextureOffset(20, 58).addBox(0.75F, -1.0F, -2.5F, 2.0F, 9.0F, 5.0F, 0.25F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -7.0F, -1.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(42, 14).addBox(-6.5F, -7.25F, -4.0F, 13.0F, 7.0F, 10.0F, 0.24F, false);
        this.head.setTextureOffset(46, 31).addBox(-6.5F, -7.0F, -4.0F, 13.0F, 7.0F, 10.0F, 0.0F, false);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(2.99F, -7.49F, 6.24F);
        this.head.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, -0.7854F, 0.0F, 0.0F);
        this.cube_r1.setTextureOffset(46, 0).addBox(-6.5F, 0.25F, 0.25F, 7.0F, 4.0F, 7.0F, 0.25F, false);
        this.nose = new AdvancedModelBox(this);
        this.nose.setRotationPoint(0.0F, -5.0F, -3.75F);
        this.head.addChild(this.nose);
        this.nose.setTextureOffset(42, 31).addBox(-2.5F, -1.0F, -2.25F, 5.0F, 2.0F, 2.0F, 0.0F, false);
        this.nose.setTextureOffset(46, 48).addBox(-7.0F, -5.0F, -1.25F, 14.0F, 11.0F, 0.0F, 0.0F, false);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-3.5F, -4.0F, 1.0F);
        this.root.addChild(this.rleg);
        this.rleg.setTextureOffset(74, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, true);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(3.5F, -4.0F, 1.0F);
        this.root.addChild(this.lleg);
        this.lleg.setTextureOffset(74, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.nose, this.cube_r1, this.rleg, this.lleg, this.rarm, this.larm);
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(UnderzealotEntity.ANIMATION_ATTACK_0);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-100.0), 0.0F, (float) Math.toRadians(-40.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-100.0), 0.0F, (float) Math.toRadians(40.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-145.0), (float) Math.toRadians(40.0), (float) Math.toRadians(10.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-145.0), (float) Math.toRadians(-40.0), (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(UnderzealotEntity.ANIMATION_ATTACK_1);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, (float) Math.toRadians(-10.0), (float) Math.toRadians(30.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-70.0), (float) Math.toRadians(70.0), (float) Math.toRadians(-10.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-90.0), (float) Math.toRadians(-30.0), (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, (float) Math.toRadians(-10.0), (float) Math.toRadians(-60.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-90.0), (float) Math.toRadians(30.0), (float) Math.toRadians(10.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-70.0), (float) Math.toRadians(-20.0), (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(UnderzealotEntity.ANIMATION_BREAKTORCH);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, (float) Math.toRadians(10.0), (float) Math.toRadians(-20.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-30.0), (float) Math.toRadians(10.0), (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-150.0), (float) Math.toRadians(10.0), (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(UnderzealotEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float buriedProgress = this.noBurrowing ? 0.0F : entity.getBuriedProgress(partialTick);
        float carryingProgress = entity.getCarryingProgress(partialTick);
        float prayingProgress = entity.getPrayingProgress(partialTick);
        float buriedStrength = (float) Math.sin((double) buriedProgress * Math.PI);
        float walkSpeed = 0.5F;
        float walkDegree = 1.0F;
        float digSpeed = 0.9F;
        float digDegree = 1.0F;
        float praySpeed = 0.2F;
        float prayDegree = 1.0F;
        float armFreedom = 1.0F - carryingProgress;
        this.progressPositionPrev(this.root, buriedProgress, 0.0F, 28.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.rarm, buriedProgress, -2.0F, -1.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.larm, buriedProgress, 2.0F, -1.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.rarm, carryingProgress, 0.0F, -2.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.larm, carryingProgress, 0.0F, -2.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rarm, buriedProgress, 0.0F, 0.0F, (float) Math.toRadians(160.0), 1.0F);
        this.progressRotationPrev(this.larm, buriedProgress, 0.0F, 0.0F, (float) Math.toRadians(-160.0), 1.0F);
        this.progressRotationPrev(this.rarm, carryingProgress, (float) Math.toRadians(-180.0), 0.0F, (float) Math.toRadians(20.0), 1.0F);
        this.progressRotationPrev(this.larm, carryingProgress, (float) Math.toRadians(-180.0), 0.0F, (float) Math.toRadians(-20.0), 1.0F);
        this.walk(this.nose, 0.4F, 0.1F, false, 1.0F, -0.05F, ageInTicks, 1.0F);
        this.swing(this.nose, 0.3F, 0.1F, false, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.rarm, 0.1F, 0.05F, false, 4.0F, 0.05F, ageInTicks, 1.0F);
        this.flap(this.larm, 0.1F, 0.05F, true, 4.0F, 0.05F, ageInTicks, 1.0F);
        this.swing(this.body, walkSpeed, walkDegree * 0.1F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.head, walkSpeed, walkDegree * 0.1F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.lleg, walkSpeed, walkDegree * 0.5F, false, 1.0F, -0.2F, limbSwing, limbSwingAmount);
        this.walk(this.rleg, walkSpeed, walkDegree * 0.5F, true, 1.0F, 0.2F, limbSwing, limbSwingAmount);
        this.lleg.rotationPointY = this.lleg.rotationPointY + Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 2.0F, true));
        this.rleg.rotationPointY = this.rleg.rotationPointY + Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 2.0F, false));
        this.flap(this.rarm, walkSpeed, walkDegree * 0.1F, false, 4.0F, 0.25F, limbSwing, limbSwingAmount * armFreedom);
        this.flap(this.larm, walkSpeed, walkDegree * 0.1F, true, 4.0F, 0.25F, limbSwing, limbSwingAmount * armFreedom);
        this.swing(this.root, digSpeed, digDegree * 0.1F, false, 2.0F, 0.0F, ageInTicks, buriedStrength);
        this.walk(this.rarm, digSpeed, digDegree, false, 4.0F, 0.0F, ageInTicks, buriedStrength);
        this.walk(this.larm, digSpeed, digDegree, true, 4.0F, 0.0F, ageInTicks, buriedStrength);
        this.walk(this.head, digSpeed, digDegree * 0.1F, true, 4.0F, 0.5F, ageInTicks, buriedStrength);
        this.progressPositionPrev(this.root, prayingProgress, 0.0F, -2.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.body, prayingProgress, 0.0F, 0.0F, 2.0F, 1.0F);
        this.progressRotationPrev(this.root, prayingProgress, (float) Math.toRadians(-30.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.lleg, prayingProgress, 0.0F, 4.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, prayingProgress, (float) Math.toRadians(120.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.rleg, prayingProgress, 0.0F, 4.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, prayingProgress, (float) Math.toRadians(120.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rarm, prayingProgress * armFreedom, (float) Math.toRadians(-160.0), (float) Math.toRadians(60.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.larm, prayingProgress * armFreedom, (float) Math.toRadians(-160.0), (float) Math.toRadians(-60.0), 0.0F, 1.0F);
        float bodyForwards = -Math.max(0.0F, ACMath.walkValue(ageInTicks, prayingProgress, praySpeed, 4.0F, 6.0F, false));
        this.body.rotationPointY -= bodyForwards * 0.5F;
        this.body.rotationPointZ += bodyForwards;
        this.walk(this.body, praySpeed, prayDegree * 0.5F, false, 4.0F, 0.4F, ageInTicks, prayingProgress);
        this.walk(this.head, praySpeed, prayDegree * 0.5F, false, 3.0F, 0.1F, ageInTicks, prayingProgress);
        this.walk(this.larm, praySpeed, prayDegree, false, 4.0F, 1.0F, ageInTicks, prayingProgress * armFreedom);
        this.swing(this.larm, praySpeed, prayDegree, false, 4.0F, 0.2F, ageInTicks, prayingProgress * armFreedom);
        this.walk(this.rarm, praySpeed, prayDegree, false, 4.0F, 1.0F, ageInTicks, prayingProgress * armFreedom);
        this.swing(this.rarm, praySpeed, prayDegree, true, 4.0F, 0.2F, ageInTicks, prayingProgress * armFreedom);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
    }
}