package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.WatcherEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class WatcherModel extends AdvancedEntityModel<WatcherEntity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox head;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox rwing;

    private final AdvancedModelBox cube_r2;

    private final AdvancedModelBox lwing;

    private final AdvancedModelBox cube_r3;

    private final AdvancedModelBox lhorn;

    private final AdvancedModelBox rhorn;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox lleg;

    private final ModelAnimator animator;

    public WatcherModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 5.125F, -0.5F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(67, 61).addBox(-4.0F, -5.125F, -2.5F, 8.0F, 12.0F, 5.0F, 0.0F, false);
        this.body.setTextureOffset(27, 22).addBox(-5.0F, -5.875F, -3.5F, 10.0F, 22.0F, 7.0F, 0.25F, false);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(-5.1667F, -4.4583F, 0.0F);
        this.body.addChild(this.larm);
        this.larm.setTextureOffset(16, 61).addBox(-4.3333F, -0.6667F, -3.0F, 4.0F, 5.0F, 6.0F, 0.25F, false);
        this.larm.setTextureOffset(56, 46).addBox(-3.3333F, 0.3333F, -2.5F, 3.0F, 15.0F, 5.0F, 0.24F, false);
        this.larm.setTextureOffset(36, 65).addBox(-3.3333F, 15.3333F, -2.5F, 3.0F, 4.0F, 5.0F, 0.0F, false);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(5.1667F, -4.4583F, 0.0F);
        this.body.addChild(this.rarm);
        this.rarm.setTextureOffset(16, 61).addBox(0.3333F, -0.6667F, -3.0F, 4.0F, 5.0F, 6.0F, 0.25F, true);
        this.rarm.setTextureOffset(56, 46).addBox(0.3333F, 0.3333F, -2.5F, 3.0F, 15.0F, 5.0F, 0.24F, true);
        this.rarm.setTextureOffset(36, 65).addBox(0.3333F, 15.3333F, -2.5F, 3.0F, 4.0F, 5.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -5.125F, 0.5F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(72, 45).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 9.0F, 7.0F, 0.0F, false);
        this.head.setTextureOffset(34, 0).addBox(-5.0F, -10.0F, -4.0F, 10.0F, 9.0F, 7.0F, 0.26F, false);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, -10.25F, -4.25F);
        this.head.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.7854F, 0.0F, 0.0F);
        this.cube_r1.setTextureOffset(54, 16).addBox(-2.0F, 0.25F, -7.25F, 4.0F, 4.0F, 7.0F, 0.25F, false);
        this.rwing = new AdvancedModelBox(this);
        this.rwing.setRotationPoint(0.0F, -5.0F, 3.0F);
        this.head.addChild(this.rwing);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rwing.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, 0.0F, 1.1781F, 0.0F);
        this.cube_r2.setTextureOffset(0, 18).addBox(0.0F, -9.0F, 0.0F, 0.0F, 17.0F, 11.0F, 0.0F, false);
        this.lwing = new AdvancedModelBox(this);
        this.lwing.setRotationPoint(0.0F, -5.0F, 3.0F);
        this.head.addChild(this.lwing);
        this.cube_r3 = new AdvancedModelBox(this);
        this.cube_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lwing.addChild(this.cube_r3);
        this.setRotateAngle(this.cube_r3, 0.0F, -1.1781F, 0.0F);
        this.cube_r3.setTextureOffset(0, 18).addBox(0.0F, -9.0F, 0.0F, 0.0F, 17.0F, 11.0F, 0.0F, true);
        this.lhorn = new AdvancedModelBox(this);
        this.lhorn.setRotationPoint(5.25F, -6.0F, 0.0F);
        this.head.addChild(this.lhorn);
        this.lhorn.setTextureOffset(0, 61).addBox(0.0F, -11.0F, 0.0F, 8.0F, 15.0F, 0.0F, 0.0F, true);
        this.rhorn = new AdvancedModelBox(this);
        this.rhorn.setRotationPoint(-5.25F, -6.0F, 0.0F);
        this.head.addChild(this.rhorn);
        this.rhorn.setTextureOffset(0, 61).addBox(-8.0F, -11.0F, 0.0F, 8.0F, 15.0F, 0.0F, 0.0F, false);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(2.5F, 11.5F, -0.5F);
        this.root.addChild(this.rleg);
        this.rleg.setTextureOffset(61, 27).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 15.0F, 3.0F, 0.0F, true);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(-2.5F, 11.5F, -0.5F);
        this.root.addChild(this.lleg);
        this.lleg.setTextureOffset(61, 27).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 15.0F, 3.0F, 0.0F, false);
        this.root.rotateAngleY = (float) Math.PI;
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void setupAnim(WatcherEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float runProgress = entity.getRunAmount(partialTick);
        float shadeAmount = entity.getShadeAmount(partialTick);
        float groundAmount = 1.0F - shadeAmount;
        float walkAmount = limbSwingAmount * (1.0F - runProgress);
        float runAmount = limbSwingAmount * runProgress;
        float walkSpeed = 0.5F;
        float walkDegree = 1.0F;
        float runSpeed = 0.5F;
        float runDegree = 1.0F;
        float twitchinessAmount = ACMath.smin((float) Math.sin((double) (ageInTicks * 0.03F)) + 0.5F, 0.0F, 0.3F);
        this.progressRotationPrev(this.body, walkAmount, (float) Math.toRadians(-5.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, walkAmount, (float) Math.toRadians(5.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.body, runAmount, (float) Math.toRadians(-15.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, runAmount, (float) Math.toRadians(15.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rarm, runProgress, (float) Math.toRadians(75.0), (float) Math.toRadians(35.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.larm, runProgress, (float) Math.toRadians(75.0), (float) Math.toRadians(-35.0), 0.0F, 1.0F);
        this.progressPositionPrev(this.body, runAmount * groundAmount, 0.0F, -6.5F, 4.0F, 1.0F);
        this.progressPositionPrev(this.head, runAmount * groundAmount, 0.0F, 1.5F, 2.0F, 1.0F);
        this.swing(this.lwing, 0.2F, 0.25F, false, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.rwing, 0.2F, 0.25F, true, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.head, 4.0F, 0.1F, false, 0.0F, 0.0F, ageInTicks, twitchinessAmount);
        this.flap(this.larm, 0.1F, 0.1F, false, -0.5F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.rarm, 0.1F, 0.1F, true, -0.5F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.larm, 0.1F, 0.1F, true, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.rarm, 0.1F, 0.1F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.lleg, walkSpeed, walkDegree * 0.5F, false, 1.0F, -0.1F, limbSwing, walkAmount * groundAmount);
        this.walk(this.rleg, walkSpeed, walkDegree * 0.5F, true, 1.0F, 0.1F, limbSwing, walkAmount * groundAmount);
        this.walk(this.body, walkSpeed * 0.5F, walkDegree * 0.1F, false, -2.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.head, walkSpeed * 0.5F, walkDegree * 0.1F, false, -1.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.larm, walkSpeed * 0.5F, walkDegree * 0.25F, false, -0.5F, 0.1F, limbSwing, walkAmount);
        this.walk(this.rarm, walkSpeed * 0.5F, walkDegree * 0.25F, false, 0.5F, 0.1F, limbSwing, walkAmount);
        this.walk(this.head, runSpeed * 0.5F, runDegree * 0.1F, false, -1.0F, 0.0F, limbSwing, runAmount);
        this.walk(this.lleg, runSpeed, runDegree * 0.5F, false, 4.0F, -0.1F, limbSwing, runAmount * groundAmount);
        this.walk(this.rleg, runSpeed, runDegree * 0.5F, true, 4.0F, 0.1F, limbSwing, runAmount * groundAmount);
        this.walk(this.rarm, runSpeed, runDegree * 0.15F, false, 2.0F, 0.4F, limbSwing, runAmount);
        this.walk(this.larm, runSpeed, runDegree * 0.15F, true, 2.0F, -0.4F, limbSwing, runAmount);
        this.swing(this.root, runSpeed, runDegree * 0.35F, true, 3.0F, 0.0F, limbSwing, runAmount * groundAmount);
        this.swing(this.body, runSpeed, runDegree * 0.25F, false, 3.0F, 0.0F, limbSwing, runAmount);
        this.larm.setScale(1.0F, 1.0F + runAmount * 0.4F, 1.0F);
        this.rarm.setScale(1.0F, 1.0F + runAmount * 0.4F, 1.0F);
        this.walk(this.lleg, walkSpeed, walkDegree * 0.3F, false, -1.0F, -0.1F, limbSwing, limbSwingAmount * shadeAmount);
        this.walk(this.rleg, walkSpeed, walkDegree * 0.3F, false, -2.0F, -0.1F, limbSwing, limbSwingAmount * shadeAmount);
        this.head.rotateAngleY += netHeadYaw / (180.0F / (float) Math.PI);
        this.head.rotateAngleX -= headPitch / (180.0F / (float) Math.PI);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.cube_r1, this.cube_r2, this.cube_r3, this.body, this.larm, this.lleg, this.rarm, this.rleg, this.lhorn, this.rhorn, this.head, new AdvancedModelBox[] { this.rwing, this.lwing });
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(WatcherEntity.ANIMATION_ATTACK_0);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(30.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-35.0), (float) Math.toRadians(-60.0), (float) Math.toRadians(-25.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(25.0), 0.0F, (float) Math.toRadians(40.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-30.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(25.0), 0.0F, (float) Math.toRadians(-40.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-35.0), (float) Math.toRadians(60.0), (float) Math.toRadians(25.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(WatcherEntity.ANIMATION_ATTACK_1);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, -5.0F);
        this.animator.rotate(this.body, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-10.0), (float) Math.toRadians(75.0), (float) Math.toRadians(25.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-10.0), (float) Math.toRadians(-75.0), (float) Math.toRadians(-25.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.rarm, (float) Math.toRadians(0.0), (float) Math.toRadians(-45.0), (float) Math.toRadians(25.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(0.0), (float) Math.toRadians(45.0), (float) Math.toRadians(-25.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void positionForParticle(float partialTick, float v) {
        this.resetToDefaultPose();
    }
}