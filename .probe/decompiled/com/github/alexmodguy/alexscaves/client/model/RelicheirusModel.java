package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.RelicheirusEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.animation.LegSolverQuadruped;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;

public class RelicheirusModel extends AdvancedEntityModel<RelicheirusEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox chest;

    private final AdvancedModelBox chestFeathers;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox larmFeathers;

    private final AdvancedModelBox lhand;

    private final AdvancedModelBox lhandClaw1;

    private final AdvancedModelBox lhandClaw2;

    private final AdvancedModelBox lhandClaw3;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox rarmFeathers;

    private final AdvancedModelBox rhand;

    private final AdvancedModelBox rhandClaw1;

    private final AdvancedModelBox rhandClaw2;

    private final AdvancedModelBox rhandClaw3;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox neckFeathers;

    private final AdvancedModelBox head;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox lwattle;

    private final AdvancedModelBox rwattle;

    private final AdvancedModelBox hips;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox lleg2;

    private final AdvancedModelBox lfoot;

    private final AdvancedModelBox claw1;

    private final AdvancedModelBox claw2;

    private final AdvancedModelBox claw3;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox rleg2;

    private final AdvancedModelBox rfoot;

    private final AdvancedModelBox rclaw1;

    private final AdvancedModelBox rclaw2;

    private final AdvancedModelBox rclaw3;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox tailFeathers;

    private final ModelAnimator animator;

    public RelicheirusModel() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, -15.0F, 0.0F);
        this.chest = new AdvancedModelBox(this);
        this.chest.setRotationPoint(0.0F, -10.5F, 3.0F);
        this.body.addChild(this.chest);
        this.chest.setTextureOffset(34, 98).addBox(-15.0F, 28.5F, -28.0F, 30.0F, 4.0F, 28.0F, 0.0F, false);
        this.chest.setTextureOffset(0, 0).addBox(-15.0F, -8.5F, -28.0F, 30.0F, 37.0F, 28.0F, 0.0F, false);
        this.chest.setTextureOffset(0, 96).addBox(-13.5F, -1.5F, -2.0F, 27.0F, 18.0F, 11.0F, 0.0F, false);
        this.chestFeathers = new AdvancedModelBox(this);
        this.chestFeathers.setRotationPoint(0.0F, -8.5F, -9.5F);
        this.chest.addChild(this.chestFeathers);
        this.chestFeathers.setTextureOffset(0, 113).addBox(0.0F, -6.0F, -18.5F, 0.0F, 14.0F, 33.0F, 0.0F, false);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(15.0F, 20.5F, -21.0F);
        this.chest.addChild(this.larm);
        this.larm.setTextureOffset(40, 160).addBox(-2.5F, 4.0F, 2.5F, 7.0F, 21.0F, 15.0F, 0.0F, false);
        this.larm.setTextureOffset(84, 170).addBox(-2.0F, 9.0F, 3.0F, 6.0F, 8.0F, 14.0F, 0.0F, false);
        this.larm.setTextureOffset(130, 138).addBox(-2.5F, -4.0F, -2.5F, 7.0F, 8.0F, 20.0F, 0.0F, false);
        this.larmFeathers = new AdvancedModelBox(this);
        this.larmFeathers.setRotationPoint(2.5F, 7.5F, 17.5F);
        this.larm.addChild(this.larmFeathers);
        this.larmFeathers.setTextureOffset(154, 221).addBox(0.0F, -11.5F, 0.0F, 0.0F, 23.0F, 6.0F, 0.0F, false);
        this.lhand = new AdvancedModelBox(this);
        this.lhand.setRotationPoint(0.0F, 17.5F, 10.0F);
        this.larm.addChild(this.lhand);
        this.lhand.setTextureOffset(84, 170).addBox(-2.0F, -0.5F, -7.0F, 6.0F, 8.0F, 14.0F, 0.0F, false);
        this.lhandClaw1 = new AdvancedModelBox(this);
        this.lhandClaw1.setRotationPoint(3.0F, 7.5F, -5.0F);
        this.lhand.addChild(this.lhandClaw1);
        this.lhandClaw1.setTextureOffset(151, 169).addBox(-12.0F, -4.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        this.lhandClaw1.setTextureOffset(0, 88).addBox(-12.0F, 0.0F, -2.0F, 13.0F, 4.0F, 4.0F, 0.0F, false);
        this.lhandClaw2 = new AdvancedModelBox(this);
        this.lhandClaw2.setRotationPoint(1.0F, 7.5F, 0.0F);
        this.lhand.addChild(this.lhandClaw2);
        this.lhandClaw2.setTextureOffset(151, 169).addBox(-10.0F, -4.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        this.lhandClaw2.setTextureOffset(0, 88).addBox(-10.0F, 0.0F, -2.0F, 13.0F, 4.0F, 4.0F, 0.0F, false);
        this.lhandClaw3 = new AdvancedModelBox(this);
        this.lhandClaw3.setRotationPoint(1.0F, 7.5F, 5.0F);
        this.lhand.addChild(this.lhandClaw3);
        this.lhandClaw3.setTextureOffset(151, 169).addBox(-10.0F, -4.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        this.lhandClaw3.setTextureOffset(0, 88).addBox(-10.0F, 0.0F, -2.0F, 13.0F, 4.0F, 4.0F, 0.0F, false);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(-15.0F, 20.5F, -21.5F);
        this.chest.addChild(this.rarm);
        this.rarm.setTextureOffset(84, 170).addBox(-4.0F, 9.0F, 3.5F, 6.0F, 8.0F, 14.0F, 0.0F, true);
        this.rarm.setTextureOffset(40, 160).addBox(-4.5F, 4.0F, 3.0F, 7.0F, 21.0F, 15.0F, 0.0F, true);
        this.rarm.setTextureOffset(130, 138).addBox(-4.5F, -4.0F, -2.0F, 7.0F, 8.0F, 20.0F, 0.0F, true);
        this.rarmFeathers = new AdvancedModelBox(this);
        this.rarmFeathers.setRotationPoint(-2.5F, 7.5F, 18.0F);
        this.rarm.addChild(this.rarmFeathers);
        this.rarmFeathers.setTextureOffset(154, 221).addBox(0.0F, -11.5F, 0.0F, 0.0F, 23.0F, 6.0F, 0.0F, true);
        this.rhand = new AdvancedModelBox(this);
        this.rhand.setRotationPoint(0.0F, 17.5F, 10.5F);
        this.rarm.addChild(this.rhand);
        this.rhand.setTextureOffset(84, 170).addBox(-4.0F, -0.5F, -7.0F, 6.0F, 8.0F, 14.0F, 0.0F, true);
        this.rhandClaw1 = new AdvancedModelBox(this);
        this.rhandClaw1.setRotationPoint(-3.0F, 7.5F, -5.0F);
        this.rhand.addChild(this.rhandClaw1);
        this.rhandClaw1.setTextureOffset(151, 169).addBox(11.0F, -4.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, true);
        this.rhandClaw1.setTextureOffset(0, 88).addBox(-1.0F, 0.0F, -2.0F, 13.0F, 4.0F, 4.0F, 0.0F, true);
        this.rhandClaw2 = new AdvancedModelBox(this);
        this.rhandClaw2.setRotationPoint(-1.0F, 7.5F, 0.0F);
        this.rhand.addChild(this.rhandClaw2);
        this.rhandClaw2.setTextureOffset(151, 169).addBox(9.0F, -4.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, true);
        this.rhandClaw2.setTextureOffset(0, 88).addBox(-3.0F, 0.0F, -2.0F, 13.0F, 4.0F, 4.0F, 0.0F, true);
        this.rhandClaw3 = new AdvancedModelBox(this);
        this.rhandClaw3.setRotationPoint(-1.0F, 7.5F, 5.0F);
        this.rhand.addChild(this.rhandClaw3);
        this.rhandClaw3.setTextureOffset(151, 169).addBox(9.0F, -4.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, true);
        this.rhandClaw3.setTextureOffset(0, 88).addBox(-3.0F, 0.0F, -2.0F, 13.0F, 4.0F, 4.0F, 0.0F, true);
        this.neck = new AdvancedModelBox(this);
        this.neck.setRotationPoint(0.0F, 4.5F, -23.0F);
        this.chest.addChild(this.neck);
        this.neck.setTextureOffset(116, 0).addBox(-8.0F, -28.0F, -11.0F, 16.0F, 30.0F, 16.0F, 0.0F, false);
        this.neck.setTextureOffset(66, 138).addBox(-8.0F, 2.0F, -11.0F, 16.0F, 6.0F, 16.0F, 0.0F, false);
        this.neckFeathers = new AdvancedModelBox(this);
        this.neckFeathers.setRotationPoint(0.0F, -28.0F, -7.5F);
        this.neck.addChild(this.neckFeathers);
        this.neckFeathers.setTextureOffset(0, 49).addBox(0.0F, -3.0F, -9.5F, 0.0F, 22.0F, 17.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -25.0F, -1.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(0, 160).addBox(-5.0F, -27.0F, -5.0F, 10.0F, 31.0F, 10.0F, 0.0F, false);
        this.head.setTextureOffset(168, 46).addBox(-3.0F, -27.0F, -12.0F, 6.0F, 2.0F, 7.0F, 0.0F, false);
        this.head.setTextureOffset(168, 56).addBox(-4.5F, -27.0F, -17.0F, 9.0F, 4.0F, 5.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(0.0F, -25.0F, -5.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(88, 14).addBox(-3.5F, 0.0F, -7.0F, 7.0F, 4.0F, 7.0F, 0.0F, false);
        this.jaw.setTextureOffset(0, 19).addBox(-4.5F, 2.0F, -12.0F, 9.0F, 2.0F, 5.0F, 0.0F, false);
        this.lwattle = new AdvancedModelBox(this);
        this.lwattle.setRotationPoint(3.0F, 4.0F, -1.0F);
        this.jaw.addChild(this.lwattle);
        this.lwattle.setTextureOffset(30, 164).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        this.rwattle = new AdvancedModelBox(this);
        this.rwattle.setRotationPoint(-3.0F, 4.0F, -1.0F);
        this.jaw.addChild(this.rwattle);
        this.rwattle.setTextureOffset(30, 164).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        this.hips = new AdvancedModelBox(this);
        this.hips.setRotationPoint(0.0F, -10.5F, 3.0F);
        this.body.addChild(this.hips);
        this.hips.setTextureOffset(97, 46).addBox(-13.0F, -0.75F, -5.0F, 26.0F, 29.0F, 19.0F, 0.0F, false);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(12.0F, 23.5F, 9.5F);
        this.hips.addChild(this.lleg);
        this.lleg.setTextureOffset(124, 166).addBox(-4.0F, -3.0F, -7.5F, 8.0F, 18.0F, 11.0F, 0.0F, false);
        this.lleg2 = new AdvancedModelBox(this);
        this.lleg2.setRotationPoint(-0.5F, 13.0F, 1.0F);
        this.lleg.addChild(this.lleg2);
        this.lleg2.setTextureOffset(0, 0).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 12.0F, 7.0F, 0.0F, false);
        this.lfoot = new AdvancedModelBox(this);
        this.lfoot.setRotationPoint(0.0F, 9.5F, 1.0F);
        this.lleg2.addChild(this.lfoot);
        this.lfoot.setTextureOffset(88, 0).addBox(-3.5F, 0.5F, -7.5F, 7.0F, 3.0F, 11.0F, 0.0F, false);
        this.claw1 = new AdvancedModelBox(this);
        this.claw1.setRotationPoint(-2.5F, 0.5F, -7.5F);
        this.lfoot.addChild(this.claw1);
        this.claw1.setTextureOffset(88, 0).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 3.0F, 0.0F, false);
        this.claw2 = new AdvancedModelBox(this);
        this.claw2.setRotationPoint(0.0F, 0.5F, -7.5F);
        this.lfoot.addChild(this.claw2);
        this.claw2.setTextureOffset(88, 0).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 3.0F, 0.0F, false);
        this.claw3 = new AdvancedModelBox(this);
        this.claw3.setRotationPoint(2.5F, 0.5F, -7.5F);
        this.lfoot.addChild(this.claw3);
        this.claw3.setTextureOffset(88, 0).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 3.0F, 0.0F, false);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-12.0F, 23.5F, 9.5F);
        this.hips.addChild(this.rleg);
        this.rleg.setTextureOffset(124, 166).addBox(-4.0F, -3.0F, -7.5F, 8.0F, 18.0F, 11.0F, 0.0F, true);
        this.rleg2 = new AdvancedModelBox(this);
        this.rleg2.setRotationPoint(0.5F, 13.0F, 1.0F);
        this.rleg.addChild(this.rleg2);
        this.rleg2.setTextureOffset(0, 0).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 12.0F, 7.0F, 0.0F, true);
        this.rfoot = new AdvancedModelBox(this);
        this.rfoot.setRotationPoint(0.0F, 9.5F, 1.0F);
        this.rleg2.addChild(this.rfoot);
        this.rfoot.setTextureOffset(88, 0).addBox(-3.5F, 0.5F, -7.5F, 7.0F, 3.0F, 11.0F, 0.0F, true);
        this.rclaw1 = new AdvancedModelBox(this);
        this.rclaw1.setRotationPoint(2.5F, 0.5F, -7.5F);
        this.rfoot.addChild(this.rclaw1);
        this.rclaw1.setTextureOffset(88, 0).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 3.0F, 0.0F, true);
        this.rclaw2 = new AdvancedModelBox(this);
        this.rclaw2.setRotationPoint(0.0F, 0.5F, -7.5F);
        this.rfoot.addChild(this.rclaw2);
        this.rclaw2.setTextureOffset(88, 0).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 3.0F, 0.0F, true);
        this.rclaw3 = new AdvancedModelBox(this);
        this.rclaw3.setRotationPoint(-2.5F, 0.5F, -7.5F);
        this.rfoot.addChild(this.rclaw3);
        this.rclaw3.setTextureOffset(88, 0).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 3.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, 10.5F, 13.0F);
        this.hips.addChild(this.tail);
        this.tail.setTextureOffset(165, 100).addBox(-8.0F, -5.0F, -2.0F, 16.0F, 16.0F, 22.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this);
        this.tail2.setRotationPoint(0.0F, 1.0F, 19.5F);
        this.tail.addChild(this.tail2);
        this.tail2.setTextureOffset(88, 225).addBox(-4.0F, -2.0F, -2.5F, 8.0F, 8.0F, 23.0F, 0.0F, false);
        this.tailFeathers = new AdvancedModelBox(this);
        this.tailFeathers.setRotationPoint(0.0F, 1.0F, 17.5F);
        this.tail2.addChild(this.tailFeathers);
        this.tailFeathers.setTextureOffset(127, 167).addBox(-12.0F, 0.0F, -14.0F, 24.0F, 18.0F, 38.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.5F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.25F, 0.25F, 0.25F);
            matrixStackIn.translate(0.0, 4.5, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.chest, this.chestFeathers, this.tail, this.tail2, this.tailFeathers, this.neck, this.neckFeathers, this.head, this.jaw, this.hips, this.lwattle, new AdvancedModelBox[] { this.rwattle, this.lleg, this.lleg2, this.rleg, this.rleg2, this.rfoot, this.lfoot, this.rarm, this.rarmFeathers, this.larm, this.larmFeathers, this.rhand, this.rhandClaw1, this.rhandClaw2, this.rhandClaw3, this.lhand, this.lhandClaw1, this.lhandClaw2, this.lhandClaw3, this.rclaw1, this.rclaw2, this.rclaw3, this.claw1, this.claw2, this.claw3 });
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(RelicheirusEntity.ANIMATION_SPEAK_1);
        this.animator.startKeyframe(5);
        this.animatePose(0);
        this.animator.rotate(this.neck, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(3);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(RelicheirusEntity.ANIMATION_SPEAK_2);
        this.animator.startKeyframe(5);
        this.animatePose(0);
        this.animator.rotate(this.neck, (float) Math.toRadians(5.0), (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-15.0), (float) Math.toRadians(10.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animatePose(0);
        this.animator.rotate(this.neck, (float) Math.toRadians(5.0), (float) Math.toRadians(-10.0), 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-15.0), (float) Math.toRadians(-10.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(RelicheirusEntity.ANIMATION_EAT_TREE);
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.rotate(this.neck, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.rwattle, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.lwattle, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.lhand, 0.0F, 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.rhand, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.rotate(this.lhand, 0.0F, 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.rhand, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.rotate(this.neck, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.rwattle, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.lwattle, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.lhand, 0.0F, 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.rhand, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.rotate(this.lhand, 0.0F, 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.rhand, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.rotate(this.neck, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.rwattle, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.lwattle, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.lhand, 0.0F, 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.rhand, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(RelicheirusEntity.ANIMATION_EAT_TRILOCARIS);
        this.animator.startKeyframe(10);
        this.animatePose(2);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(10);
        this.animatePose(2);
        this.animator.rotate(this.neck, (float) Math.toRadians(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.rwattle, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.lwattle, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(15);
        this.animatePose(1);
        this.animator.rotate(this.larm, (float) Math.toRadians(55.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(55.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.rwattle, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.lwattle, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(RelicheirusEntity.ANIMATION_PUSH_TREE);
        this.animator.startKeyframe(15);
        this.animatePose(1);
        this.animator.rotate(this.lhand, 0.0F, 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.rhand, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animatePose(1);
        this.animator.rotate(this.neck, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.rarm, 0.0F, 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.lhand, 0.0F, 0.0F, (float) Math.toRadians(-20.0));
        this.animator.rotate(this.rhand, 0.0F, 0.0F, (float) Math.toRadians(20.0));
        this.animator.move(this.body, 0.0F, 0.0F, 5.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.rotate(this.neck, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, 0.0F, 0.0F, (float) Math.toRadians(20.0));
        this.animator.rotate(this.rarm, 0.0F, 0.0F, (float) Math.toRadians(-20.0));
        this.animator.move(this.body, 0.0F, 0.0F, -5.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(RelicheirusEntity.ANIMATION_SCRATCH_1);
        this.animator.startKeyframe(10);
        this.animatePose(1);
        this.animator.move(this.rarm, -2.0F, -4.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(65.0), 0.0F, (float) Math.toRadians(5.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-155.0), 0.0F, (float) Math.toRadians(-15.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.rhandClaw1, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rhandClaw2, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rhandClaw3, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.move(this.rarm, -2.0F, -4.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(65.0), 0.0F, (float) Math.toRadians(5.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-115.0), 0.0F, (float) Math.toRadians(5.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(-10.0), (float) Math.toRadians(-30.0), (float) Math.toRadians(-15.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.rhandClaw1, 0.0F, 0.0F, (float) Math.toRadians(55.0));
        this.animator.rotate(this.rhandClaw2, 0.0F, 0.0F, (float) Math.toRadians(55.0));
        this.animator.rotate(this.rhandClaw3, 0.0F, 0.0F, (float) Math.toRadians(55.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.move(this.rarm, -2.0F, -4.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(65.0), 0.0F, (float) Math.toRadians(5.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-155.0), 0.0F, (float) Math.toRadians(-15.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.rhandClaw1, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rhandClaw2, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rhandClaw3, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.move(this.rarm, -2.0F, -4.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(65.0), 0.0F, (float) Math.toRadians(5.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-115.0), 0.0F, (float) Math.toRadians(5.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(-10.0), (float) Math.toRadians(-30.0), (float) Math.toRadians(-15.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.rhandClaw1, 0.0F, 0.0F, (float) Math.toRadians(55.0));
        this.animator.rotate(this.rhandClaw2, 0.0F, 0.0F, (float) Math.toRadians(55.0));
        this.animator.rotate(this.rhandClaw3, 0.0F, 0.0F, (float) Math.toRadians(55.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.move(this.rarm, -2.0F, -4.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(65.0), 0.0F, (float) Math.toRadians(5.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-155.0), 0.0F, (float) Math.toRadians(-15.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.rhandClaw1, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rhandClaw2, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rhandClaw3, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(RelicheirusEntity.ANIMATION_SCRATCH_2);
        this.animator.startKeyframe(10);
        this.animatePose(1);
        this.animator.move(this.larm, 2.0F, -4.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(65.0), 0.0F, (float) Math.toRadians(-5.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-135.0), 0.0F, (float) Math.toRadians(15.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.lhandClaw1, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.lhandClaw2, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.lhandClaw3, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.move(this.larm, 2.0F, -4.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(65.0), 0.0F, (float) Math.toRadians(-5.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-115.0), 0.0F, (float) Math.toRadians(-5.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(-10.0), (float) Math.toRadians(30.0), (float) Math.toRadians(15.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.lhandClaw1, 0.0F, 0.0F, (float) Math.toRadians(-55.0));
        this.animator.rotate(this.lhandClaw2, 0.0F, 0.0F, (float) Math.toRadians(-55.0));
        this.animator.rotate(this.lhandClaw3, 0.0F, 0.0F, (float) Math.toRadians(-55.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.move(this.larm, 2.0F, -4.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(65.0), 0.0F, (float) Math.toRadians(-5.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-155.0), 0.0F, (float) Math.toRadians(15.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.lhandClaw1, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.lhandClaw2, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.lhandClaw3, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.move(this.larm, 2.0F, -4.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(65.0), 0.0F, (float) Math.toRadians(-5.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-115.0), 0.0F, (float) Math.toRadians(-5.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(-10.0), (float) Math.toRadians(30.0), (float) Math.toRadians(15.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.lhandClaw1, 0.0F, 0.0F, (float) Math.toRadians(-55.0));
        this.animator.rotate(this.lhandClaw2, 0.0F, 0.0F, (float) Math.toRadians(-55.0));
        this.animator.rotate(this.lhandClaw3, 0.0F, 0.0F, (float) Math.toRadians(-55.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.move(this.larm, 2.0F, -4.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(65.0), 0.0F, (float) Math.toRadians(-5.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-155.0), 0.0F, (float) Math.toRadians(15.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.lhandClaw1, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.lhandClaw2, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.lhandClaw3, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(RelicheirusEntity.ANIMATION_MELEE_SLASH_1);
        this.animator.startKeyframe(7);
        this.animatePose(1);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(15.0), 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(75.0), 0.0F, (float) Math.toRadians(5.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(15.0), (float) Math.toRadians(50.0), (float) Math.toRadians(75.0));
        this.animator.rotate(this.rhand, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animatePose(1);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-15.0), 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(75.0), 0.0F, (float) Math.toRadians(5.0));
        this.animator.rotate(this.lhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-15.0), (float) Math.toRadians(-20.0), (float) Math.toRadians(15.0));
        this.animator.rotate(this.rhand, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rhandClaw1, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rhandClaw2, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.rotate(this.rhandClaw3, 0.0F, 0.0F, (float) Math.toRadians(35.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(RelicheirusEntity.ANIMATION_MELEE_SLASH_2);
        this.animator.startKeyframe(7);
        this.animatePose(1);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-15.0), 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(75.0), 0.0F, (float) Math.toRadians(-5.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(15.0), (float) Math.toRadians(-50.0), (float) Math.toRadians(-75.0));
        this.animator.rotate(this.lhand, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animatePose(1);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(15.0), 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(75.0), 0.0F, (float) Math.toRadians(-5.0));
        this.animator.rotate(this.rhand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-15.0), (float) Math.toRadians(20.0), (float) Math.toRadians(-15.0));
        this.animator.rotate(this.lhand, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.lhandClaw1, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.lhandClaw2, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.rotate(this.lhandClaw3, 0.0F, 0.0F, (float) Math.toRadians(-35.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
    }

    private void animatePose(int pose) {
        switch(pose) {
            case 0:
                this.animator.move(this.lwattle, 0.0F, 0.0F, -1.0F);
                this.animator.move(this.rwattle, 0.0F, 0.0F, -1.0F);
                this.animator.rotate(this.lwattle, (float) Math.toRadians(-35.0), 0.0F, 0.0F);
                this.animator.rotate(this.rwattle, (float) Math.toRadians(-35.0), 0.0F, 0.0F);
                this.animator.rotate(this.jaw, (float) Math.toRadians(30.0), 0.0F, 0.0F);
                break;
            case 1:
                this.animator.move(this.body, 0.0F, 1.0F, 5.0F);
                this.animator.move(this.rleg, 0.0F, -6.0F, -1.0F);
                this.animator.move(this.lleg, 0.0F, -6.0F, -1.0F);
                this.animator.rotate(this.body, (float) Math.toRadians(-35.0), 0.0F, 0.0F);
                this.animator.rotate(this.neck, (float) Math.toRadians(35.0), 0.0F, 0.0F);
                this.animator.rotate(this.lleg, (float) Math.toRadians(35.0), 0.0F, 0.0F);
                this.animator.rotate(this.rleg, (float) Math.toRadians(35.0), 0.0F, 0.0F);
                this.animator.rotate(this.tail, (float) Math.toRadians(15.0), 0.0F, 0.0F);
                this.animator.rotate(this.tail2, (float) Math.toRadians(15.0), 0.0F, 0.0F);
                this.animator.rotate(this.chest, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
                this.animator.rotate(this.larm, (float) Math.toRadians(-55.0), 0.0F, 0.0F);
                this.animator.rotate(this.rarm, (float) Math.toRadians(-55.0), 0.0F, 0.0F);
                break;
            case 2:
                this.animator.move(this.body, 0.0F, 7.0F, -2.0F);
                this.animator.move(this.rleg, 0.0F, 2.5F, -1.5F);
                this.animator.move(this.lleg, 0.0F, 2.5F, -1.5F);
                this.animator.move(this.rarm, 0.0F, -4.0F, 1.5F);
                this.animator.move(this.larm, 0.0F, -4.0F, 1.5F);
                this.animator.rotate(this.body, (float) Math.toRadians(35.0), 0.0F, 0.0F);
                this.animator.rotate(this.tail, (float) Math.toRadians(-25.0), 0.0F, 0.0F);
                this.animator.rotate(this.rleg, (float) Math.toRadians(-35.0), 0.0F, 0.0F);
                this.animator.rotate(this.lleg, (float) Math.toRadians(-35.0), 0.0F, 0.0F);
                this.animator.rotate(this.rarm, (float) Math.toRadians(-125.0), 0.0F, 0.0F);
                this.animator.rotate(this.larm, (float) Math.toRadians(-125.0), 0.0F, 0.0F);
        }
    }

    private void setupAnimForAnimation(RelicheirusEntity entity, Animation animation, float limbSwing, float bodyDown, float ageInTicks) {
        float partialTick = ageInTicks - (float) entity.f_19797_;
        if (animation == RelicheirusEntity.ANIMATION_SHAKE) {
            float animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 3.0F, animation, partialTick, 0);
            this.swing(this.neck, 0.5F, 0.2F, false, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.swing(this.head, 0.5F, 0.2F, false, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.flap(this.head, 0.5F, 0.1F, false, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.flap(this.lwattle, 0.5F, 0.7F, false, -1.0F, 0.0F, ageInTicks, animationIntensity);
            this.flap(this.rwattle, 0.5F, 0.7F, false, -1.0F, 0.0F, ageInTicks, animationIntensity);
            this.walk(this.neck, 0.5F, 0.05F, false, 3.0F, 0.3F, ageInTicks, animationIntensity);
            this.walk(this.head, 0.5F, 0.05F, false, 2.0F, 0.1F, ageInTicks, animationIntensity);
        }
        if (animation == RelicheirusEntity.ANIMATION_EAT_TREE || animation == RelicheirusEntity.ANIMATION_EAT_TRILOCARIS) {
            float animationIntensity;
            if (animation == RelicheirusEntity.ANIMATION_EAT_TRILOCARIS) {
                animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 2.0F, animation, partialTick, 15, 30);
            } else {
                animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 3.0F, animation, partialTick, 0);
            }
            float peckY = (float) ((double) entity.getPeckY() - (entity.m_20299_(partialTick).y - (double) (bodyDown / 16.0F)));
            float peckYPixel = Mth.clamp(peckY, -2.0F, 2.0F) * animationIntensity * entity.getScale();
            this.neck.rotateAngleX -= peckYPixel * 0.2F;
            this.head.rotateAngleX -= peckYPixel * 0.2F;
            this.neck.rotationPointY -= peckYPixel * 16.0F * 0.15F;
            this.neck.rotationPointZ = this.neck.rotationPointZ + Math.abs(peckYPixel * 16.0F * 0.35F);
        }
    }

    public void setupAnim(RelicheirusEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float buryEggsAmount = entity.getBuryEggsProgress(partialTick);
        float walkSpeed = 0.8F;
        float walkDegree = 2.0F;
        float danceAmount = entity.getDanceProgress(partialTick);
        float raisedArmsAmount = Math.max(danceAmount, entity.getRaiseArmsAmount(partialTick));
        float armsWalkAmount = 1.0F - raisedArmsAmount;
        float danceSpeed = 0.3F;
        float f = this.articulateLegs(entity.legSolver, armsWalkAmount, partialTick);
        if (entity.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
            this.setupAnimForAnimation(entity, entity.getAnimation(), limbSwing, f, ageInTicks);
        }
        if (buryEggsAmount > 0.0F) {
            limbSwing = ageInTicks;
            limbSwingAmount = buryEggsAmount * 0.5F;
            this.body.swing(0.25F, 0.4F, false, 0.0F, 0.0F, ageInTicks, buryEggsAmount);
            this.neck.swing(0.25F, 0.4F, true, -1.0F, 0.0F, ageInTicks, buryEggsAmount);
        }
        this.walk(this.neck, 0.1F, 0.03F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.head, 0.1F, 0.03F, false, -0.5F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.lwattle, 0.1F, 0.1F, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.rwattle, 0.1F, 0.1F, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail, 0.1F, 0.05F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail2, 0.1F, 0.1F, true, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.neck, walkSpeed, walkDegree * 0.1F, false, 2.0F, 0.3F, limbSwing, limbSwingAmount);
        this.walk(this.head, walkSpeed, walkDegree * -0.1F, false, 1.0F, -0.2F, limbSwing, limbSwingAmount);
        this.swing(this.tail, walkSpeed, walkDegree * 0.2F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail2, walkSpeed, walkDegree * 0.2F, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        float bodyWalkBob = this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -1.5F, 4.0F, false);
        this.body.rotationPointY += bodyWalkBob;
        this.lleg.rotationPointY -= bodyWalkBob;
        this.rleg.rotationPointY -= bodyWalkBob;
        this.larm.rotationPointY -= bodyWalkBob * armsWalkAmount;
        this.rarm.rotationPointY -= bodyWalkBob * armsWalkAmount;
        this.walk(this.lleg, walkSpeed, walkDegree * 0.4F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.lleg2, walkSpeed, walkDegree * 0.3F, false, 1.5F, -0.1F, limbSwing, limbSwingAmount);
        this.walk(this.lfoot, walkSpeed, walkDegree * 0.8F, false, -1.5F, 0.4F, limbSwing, limbSwingAmount);
        this.lleg.rotationPointY = this.lleg.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, walkDegree * 10.0F, true));
        this.walk(this.rleg, walkSpeed, walkDegree * 0.4F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rleg2, walkSpeed, walkDegree * 0.3F, true, 1.5F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(this.rfoot, walkSpeed, walkDegree * 0.8F, true, -1.5F, -0.4F, limbSwing, limbSwingAmount);
        this.rleg.rotationPointY = this.rleg.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, walkDegree * 10.0F, false));
        this.walk(this.rarm, walkSpeed, walkDegree * 0.4F, false, 1.0F, -0.6F, limbSwing, limbSwingAmount * armsWalkAmount);
        this.walk(this.rhand, walkSpeed, walkDegree * 0.4F, false, 2.5F, 0.2F, limbSwing, limbSwingAmount * armsWalkAmount);
        this.rarm.rotationPointY = this.rarm.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, walkDegree * 10.0F, true)) * armsWalkAmount;
        this.rhand.rotationPointY = this.rhand.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, 1.5F, walkDegree * 4.0F, true)) * armsWalkAmount;
        this.walk(this.larm, walkSpeed, walkDegree * 0.4F, true, 1.0F, 0.6F, limbSwing, limbSwingAmount * armsWalkAmount);
        this.walk(this.lhand, walkSpeed, walkDegree * 0.4F, true, 2.5F, -0.2F, limbSwing, limbSwingAmount * armsWalkAmount);
        this.larm.rotationPointY = this.larm.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, walkDegree * 10.0F, false)) * armsWalkAmount;
        this.lhand.rotationPointY = this.lhand.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, 1.5F, walkDegree * 4.0F, false)) * armsWalkAmount;
        float yawAmount = netHeadYaw / (180.0F / (float) Math.PI);
        float pitchAmount = headPitch / (180.0F / (float) Math.PI);
        this.neck.rotateAngleX += pitchAmount * 0.5F;
        this.head.rotateAngleX += pitchAmount * 0.5F;
        this.neck.rotateAngleY += yawAmount * 0.5F;
        this.head.rotateAngleY += yawAmount * 0.5F;
        this.progressPositionPrev(this.body, danceAmount, 0.0F, -3.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rarm, danceAmount, 0.0F, (float) Math.toRadians(30.0), (float) Math.toRadians(30.0), 1.0F);
        this.progressRotationPrev(this.larm, danceAmount, 0.0F, (float) Math.toRadians(-30.0), (float) Math.toRadians(-30.0), 1.0F);
        this.progressRotationPrev(this.body, danceAmount, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, danceAmount, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, danceAmount, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
        this.walk(this.tail, danceSpeed, 0.2F, false, -0.0F, 0.2F, ageInTicks, danceAmount);
        this.walk(this.tail2, danceSpeed, 0.2F, false, -0.0F, 0.2F, ageInTicks, danceAmount);
        this.flap(this.neck, danceSpeed, 0.2F, false, -0.0F, 0.0F, ageInTicks, danceAmount);
        this.flap(this.head, danceSpeed, 0.2F, false, -0.0F, 0.0F, ageInTicks, danceAmount);
        this.flap(this.rarm, danceSpeed, 0.6F, false, -1.0F, 1.2F, ageInTicks, danceAmount);
        this.flap(this.larm, danceSpeed, 0.6F, true, 1.0F, 1.2F, ageInTicks, danceAmount);
        this.flap(this.rhand, danceSpeed, 0.6F, false, -2.0F, 0.2F, ageInTicks, danceAmount);
        this.flap(this.lhand, danceSpeed, 0.6F, true, 2.0F, 0.2F, ageInTicks, danceAmount);
        this.swing(this.body, danceSpeed, 0.1F, false, 1.0F, 0.0F, ageInTicks, danceAmount);
    }

    private float walkValue(float limbSwing, float limbSwingAmount, float speed, float offset, float degree, boolean inverse) {
        return (float) (Math.cos((double) (limbSwing * speed + offset)) * (double) degree * (double) limbSwingAmount * (double) (inverse ? -1 : 1));
    }

    public void translateToMouth(PoseStack matrixStackIn) {
        this.body.translateAndRotate(matrixStackIn);
        this.chest.translateAndRotate(matrixStackIn);
        this.neck.translateAndRotate(matrixStackIn);
        this.head.translateAndRotate(matrixStackIn);
    }

    private float articulateLegs(LegSolverQuadruped legs, float armsWalkAmount, float partialTick) {
        float heightBackLeft = legs.backLeft.getHeight(partialTick);
        float heightBackRight = legs.backRight.getHeight(partialTick);
        float heightFrontLeft = legs.frontLeft.getHeight(partialTick);
        float heightFrontRight = legs.frontRight.getHeight(partialTick);
        float max = Math.max(Math.max(heightBackLeft, heightBackRight), armsWalkAmount * Math.max(heightFrontLeft, heightFrontRight)) * 0.8F;
        this.body.rotationPointY += max * 16.0F;
        this.rarm.rotationPointY += (heightFrontRight - max) * armsWalkAmount * 16.0F;
        this.larm.rotationPointY += (heightFrontLeft - max) * armsWalkAmount * 16.0F;
        this.rleg.rotationPointY += (heightBackRight - max) * 16.0F;
        this.lleg.rotationPointY += (heightBackLeft - max) * 16.0F;
        return max * 16.0F;
    }
}