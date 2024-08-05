package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;

public class VallumraptorModel extends AdvancedEntityModel<VallumraptorEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox lfoot;

    private final AdvancedModelBox lclaw;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox rfoot;

    private final AdvancedModelBox rclaw;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox lhand;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox rhand;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox headquill;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tailTip;

    private final AdvancedModelBox tailQuill;

    private final AdvancedModelBox lleg2;

    private final AdvancedModelBox rleg2;

    private final AdvancedModelBox lquill;

    private final AdvancedModelBox rquill;

    private final ModelAnimator animator;

    private float alpha = 1.0F;

    public VallumraptorModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 10.5F, -1.0F);
        this.body.setTextureOffset(0, 0).addBox(-3.5F, -3.5F, -6.0F, 7.0F, 7.0F, 12.0F, 0.0F, false);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(3.0F, 0.5F, 2.5F);
        this.body.addChild(this.lleg);
        this.lleg.setTextureOffset(34, 14).addBox(-1.5F, -2.0F, -3.5F, 4.0F, 8.0F, 5.0F, 0.0F, true);
        this.lleg2 = new AdvancedModelBox(this);
        this.lleg2.setRotationPoint(0.5F, 4.5F, 1.0F);
        this.lleg.addChild(this.lleg2);
        this.lleg2.setTextureOffset(0, 48).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 9.0F, 2.0F, 0.0F, true);
        this.lfoot = new AdvancedModelBox(this);
        this.lfoot.setRotationPoint(0.0F, 8.5F, 0.5F);
        this.lleg2.addChild(this.lfoot);
        this.lfoot.setTextureOffset(20, 0).addBox(-2.5F, 0.0F, -5.0F, 5.0F, 0.0F, 6.0F, 0.0F, true);
        this.lclaw = new AdvancedModelBox(this);
        this.lclaw.setRotationPoint(-2.0F, 0.0F, -3.0F);
        this.lfoot.addChild(this.lclaw);
        this.lclaw.setTextureOffset(21, 34).addBox(0.0F, -5.0F, -4.0F, 0.0F, 5.0F, 5.0F, 0.0F, true);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-3.0F, 0.5F, 2.5F);
        this.body.addChild(this.rleg);
        this.rleg.setTextureOffset(34, 14).addBox(-2.5F, -2.0F, -3.5F, 4.0F, 8.0F, 5.0F, 0.0F, false);
        this.rleg2 = new AdvancedModelBox(this);
        this.rleg2.setRotationPoint(-0.5F, 4.75F, 1.0F);
        this.rleg.addChild(this.rleg2);
        this.rleg2.setTextureOffset(0, 48).addBox(-1.0F, -0.75F, -0.5F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.rfoot = new AdvancedModelBox(this);
        this.rfoot.setRotationPoint(0.0F, 8.25F, 0.5F);
        this.rleg2.addChild(this.rfoot);
        this.rfoot.setTextureOffset(20, 0).addBox(-2.5F, 0.0F, -5.0F, 5.0F, 0.0F, 6.0F, 0.0F, false);
        this.rclaw = new AdvancedModelBox(this);
        this.rclaw.setRotationPoint(2.0F, 0.0F, -3.0F);
        this.rfoot.addChild(this.rclaw);
        this.rclaw.setTextureOffset(21, 34).addBox(0.0F, -5.0F, -4.0F, 0.0F, 5.0F, 5.0F, 0.0F, false);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(3.0F, 2.5F, -3.0F);
        this.body.addChild(this.larm);
        this.larm.setTextureOffset(8, 48).addBox(-0.5F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, true);
        this.larm.setTextureOffset(44, 0).addBox(-0.5F, 2.0F, -4.0F, 2.0F, 3.0F, 3.0F, 0.0F, true);
        this.lhand = new AdvancedModelBox(this);
        this.lhand.setRotationPoint(1.5F, 3.5F, -4.0F);
        this.larm.addChild(this.lhand);
        this.lhand.setTextureOffset(0, 28).addBox(-4.0F, -1.5F, -3.0F, 4.0F, 3.0F, 3.0F, 0.0F, true);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(-3.0F, 2.5F, -3.0F);
        this.body.addChild(this.rarm);
        this.rarm.setTextureOffset(8, 48).addBox(-1.5F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        this.rarm.setTextureOffset(44, 0).addBox(-1.5F, 2.0F, -4.0F, 2.0F, 3.0F, 3.0F, 0.0F, false);
        this.rhand = new AdvancedModelBox(this);
        this.rhand.setRotationPoint(-1.5F, 3.5F, -4.0F);
        this.rarm.addChild(this.rhand);
        this.rhand.setTextureOffset(0, 28).addBox(0.0F, -1.5F, -3.0F, 4.0F, 3.0F, 3.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this);
        this.neck.setRotationPoint(0.0F, -1.5F, -5.0F);
        this.body.addChild(this.neck);
        this.neck.setTextureOffset(47, 22).addBox(-1.5F, -8.0F, -3.0F, 3.0F, 9.0F, 5.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -8.0F, -1.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-1.0F, -4.0F, -7.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);
        this.head.setTextureOffset(50, 8).addBox(-1.0F, -6.0F, -8.0F, 2.0F, 6.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(-9, 39).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 0.0F, 9.0F, 0.0F, false);
        this.head.setTextureOffset(46, 56).addBox(-2.5F, 0.0F, -2.0F, 5.0F, 4.0F, 4.0F, 0.0F, false);
        this.head.setTextureOffset(26, 54).addBox(-2.0F, 0.0F, -8.0F, 4.0F, 4.0F, 6.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(0.0F, 2.0F, -1.5F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(30, 4).addBox(-1.5F, 0.0F, -6.0F, 3.0F, 2.0F, 6.0F, 0.0F, false);
        this.jaw.setTextureOffset(2, 57).addBox(-1.5F, -1.0F, -6.0F, 3.0F, 1.0F, 6.0F, 0.0F, false);
        this.headquill = new AdvancedModelBox(this);
        this.headquill.setRotationPoint(0.0F, -8.0F, 2.0F);
        this.neck.addChild(this.headquill);
        this.headquill.setTextureOffset(46, 27).addBox(0.0F, -5.0F, -5.0F, 0.0F, 14.0F, 9.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, -2.25F, 5.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(16, 19).addBox(-1.5F, -1.25F, 1.0F, 3.0F, 3.0F, 12.0F, 0.0F, false);
        this.tailTip = new AdvancedModelBox(this);
        this.tailTip.setRotationPoint(0.0F, 0.25F, 13.0F);
        this.tail.addChild(this.tailTip);
        this.tailTip.setTextureOffset(0, 5).addBox(0.0F, -7.5F, 0.0F, 0.0F, 9.0F, 14.0F, 0.0F, false);
        this.tailQuill = new AdvancedModelBox(this);
        this.tailQuill.setRotationPoint(0.0F, -1.25F, 7.0F);
        this.tail.addChild(this.tailQuill);
        this.tailQuill.setTextureOffset(18, 36).addBox(0.0F, -5.0F, -6.0F, 0.0F, 6.0F, 12.0F, 0.0F, false);
        this.lquill = new AdvancedModelBox(this);
        this.lquill.setRotationPoint(0.5F, 5.0F, -0.5F);
        this.larm.addChild(this.lquill);
        this.lquill.setTextureOffset(16, 47).addBox(0.0F, 0.0F, -3.5F, 0.0F, 3.0F, 7.0F, 0.0F, true);
        this.rquill = new AdvancedModelBox(this);
        this.rquill.setRotationPoint(-0.5F, 5.0F, -0.5F);
        this.rarm.addChild(this.rquill);
        this.rquill.setTextureOffset(16, 47).addBox(0.0F, 0.0F, -3.5F, 0.0F, 3.0F, 7.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.tail, this.tailTip, this.lleg2, this.rleg2, this.lleg, this.rleg, this.neck, this.head, this.jaw, this.lhand, this.rhand, new AdvancedModelBox[] { this.lfoot, this.lclaw, this.rfoot, this.rclaw, this.larm, this.rarm, this.headquill, this.tailQuill, this.lquill, this.rquill });
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alphaIn) {
        if (this.f_102610_) {
            float f = 1.5F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alphaIn * this.alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alphaIn * this.alpha));
            matrixStackIn.popPose();
        }
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(VallumraptorEntity.ANIMATION_CALL_1);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.jaw, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(VallumraptorEntity.ANIMATION_CALL_2);
        this.animator.startKeyframe(4);
        this.animatePose(0);
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, 0.0F, (float) Math.toRadians(20.0), 0.0F);
        this.animatePose(0);
        this.animator.endKeyframe();
        this.animator.startKeyframe(8);
        this.animator.rotate(this.neck, 0.0F, (float) Math.toRadians(-20.0), 0.0F);
        this.animatePose(0);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(4);
        this.animator.setAnimation(VallumraptorEntity.ANIMATION_SCRATCH_1);
        this.animator.startKeyframe(5);
        this.animatePose(1);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animatePose(1);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animatePose(1);
        this.animator.rotate(this.rarm, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animatePose(1);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animatePose(1);
        this.animator.rotate(this.rarm, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animatePose(1);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animatePose(1);
        this.animator.rotate(this.rarm, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(VallumraptorEntity.ANIMATION_SCRATCH_2);
        this.animator.startKeyframe(5);
        this.animatePose(2);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animatePose(2);
        this.animator.rotate(this.larm, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animatePose(2);
        this.animator.rotate(this.larm, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animatePose(2);
        this.animator.rotate(this.larm, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animatePose(2);
        this.animator.rotate(this.larm, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animatePose(2);
        this.animator.rotate(this.larm, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animatePose(2);
        this.animator.rotate(this.larm, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(VallumraptorEntity.ANIMATION_STARTLEAP);
        this.animator.startKeyframe(5);
        this.animator.move(this.head, 0.0F, -0.5F, 1.0F);
        this.animator.move(this.body, 0.0F, 2.5F, 1.0F);
        this.animator.move(this.lfoot, 0.0F, -0.35F, 0.0F);
        this.animator.move(this.rfoot, 0.0F, -0.35F, 0.0F);
        this.animator.move(this.lleg, 0.0F, -0.35F, 0.0F);
        this.animator.move(this.rleg, 0.0F, -0.35F, 0.0F);
        this.animator.rotate(this.body, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.lleg, (float) Math.toRadians(-20.0), (float) Math.toRadians(-30.0), 0.0F);
        this.animator.rotate(this.rleg, (float) Math.toRadians(-20.0), (float) Math.toRadians(30.0), 0.0F);
        this.animator.rotate(this.lleg2, (float) Math.toRadians(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.rleg2, (float) Math.toRadians(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.lfoot, (float) Math.toRadians(30.0), (float) Math.toRadians(5.0), (float) Math.toRadians(25.0));
        this.animator.rotate(this.rfoot, (float) Math.toRadians(30.0), (float) Math.toRadians(-5.0), (float) Math.toRadians(-25.0));
        this.animator.rotate(this.larm, (float) Math.toRadians(-40.0), (float) Math.toRadians(-20.0), (float) Math.toRadians(-50.0));
        this.animator.rotate(this.rarm, (float) Math.toRadians(-40.0), (float) Math.toRadians(20.0), (float) Math.toRadians(50.0));
        this.animator.rotate(this.head, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(10);
        this.animator.resetKeyframe(3);
        this.animator.setAnimation(VallumraptorEntity.ANIMATION_MELEE_BITE);
        this.animator.startKeyframe(3);
        this.animator.move(this.head, 0.0F, -1.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(30.0), 0.0F, (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.startKeyframe(3);
        this.animator.move(this.head, 0.0F, -0.5F, 0.5F);
        this.animator.rotate(this.neck, (float) Math.toRadians(50.0), (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-58.0), 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.jaw, (float) Math.toRadians(50.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(7);
        this.animator.setAnimation(VallumraptorEntity.ANIMATION_MELEE_SLASH_1);
        this.animator.startKeyframe(5);
        this.animatePose(3);
        this.animator.move(this.rhand, 0.0F, 0.0F, 1.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.lhand, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-60.0), (float) Math.toRadians(60.0), 0.0F);
        this.animator.rotate(this.rhand, 0.0F, (float) Math.toRadians(40.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-30.0), 0.0F);
        this.animator.move(this.rhand, 0.0F, 0.0F, 1.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.lhand, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-20.0), (float) Math.toRadians(-20.0), 0.0F);
        this.animator.rotate(this.rhand, 0.0F, (float) Math.toRadians(40.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(VallumraptorEntity.ANIMATION_MELEE_SLASH_2);
        this.animator.startKeyframe(5);
        this.animatePose(3);
        this.animator.move(this.lhand, 0.0F, 0.0F, 1.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.rhand, 0.0F, (float) Math.toRadians(-10.0), 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-60.0), (float) Math.toRadians(-60.0), 0.0F);
        this.animator.rotate(this.lhand, 0.0F, (float) Math.toRadians(-40.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(30.0), 0.0F);
        this.animator.move(this.lhand, 0.0F, 0.0F, 1.0F);
        this.animator.rotate(this.rarm, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.rhand, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(20.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.lhand, 0.0F, (float) Math.toRadians(-40.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(VallumraptorEntity.ANIMATION_GRAB);
        this.animator.startKeyframe(5);
        this.animatePose(5);
        this.animator.rotate(this.head, 0.0F, (float) Math.toRadians(-20.0), (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(4);
        this.animator.startKeyframe(5);
        this.animatePose(5);
        this.animator.rotate(this.head, 0.0F, (float) Math.toRadians(20.0), (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animatePose(5);
        this.animator.rotate(this.rarm, (float) Math.toRadians(-40.0), (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.rhand, 0.0F, (float) Math.toRadians(40.0), 0.0F);
        this.animator.move(this.rarm, 0.0F, 0.0F, -3.0F);
        this.animator.move(this.rhand, 0.0F, 0.0F, 1.0F);
        this.animator.rotate(this.larm, (float) Math.toRadians(-40.0), (float) Math.toRadians(-10.0), 0.0F);
        this.animator.rotate(this.lhand, 0.0F, (float) Math.toRadians(-40.0), 0.0F);
        this.animator.move(this.larm, 0.0F, 0.0F, -3.0F);
        this.animator.move(this.lhand, 0.0F, 0.0F, 1.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(3);
        this.animator.resetKeyframe(5);
    }

    private void animatePose(int pose) {
        switch(pose) {
            case 0:
                this.animator.rotate(this.jaw, (float) Math.toRadians(65.0), 0.0F, 0.0F);
                this.animator.rotate(this.head, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
                this.animator.rotate(this.neck, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
                this.animator.rotate(this.body, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
                this.animator.rotate(this.lleg, (float) Math.toRadians(10.0), 0.0F, 0.0F);
                this.animator.rotate(this.rleg, (float) Math.toRadians(10.0), 0.0F, 0.0F);
                this.animator.move(this.head, 0.0F, -1.0F, 1.0F);
                this.animator.move(this.jaw, 0.0F, 1.0F, 0.0F);
                this.animator.move(this.lleg, 0.0F, -0.5F, 0.0F);
                this.animator.move(this.rleg, 0.0F, -0.5F, 0.0F);
                break;
            case 1:
                this.animator.rotate(this.head, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
                this.animator.rotate(this.neck, (float) Math.toRadians(40.0), (float) Math.toRadians(10.0), 0.0F);
                this.animator.rotate(this.larm, (float) Math.toRadians(20.0), 0.0F, 0.0F);
                this.animator.rotate(this.lhand, (float) Math.toRadians(20.0), 0.0F, 0.0F);
                this.animator.rotate(this.rarm, (float) Math.toRadians(-90.0), (float) Math.toRadians(30.0), 0.0F);
                this.animator.rotate(this.rhand, (float) Math.toRadians(-10.0), (float) Math.toRadians(10.0), 0.0F);
                this.animator.move(this.rarm, 0.0F, -1.0F, -1.0F);
                this.animator.move(this.lhand, 0.0F, 0.0F, 1.0F);
                this.animator.move(this.rhand, 0.0F, 0.0F, 1.0F);
                break;
            case 2:
                this.animator.rotate(this.head, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
                this.animator.rotate(this.neck, (float) Math.toRadians(40.0), (float) Math.toRadians(-10.0), 0.0F);
                this.animator.rotate(this.larm, (float) Math.toRadians(20.0), 0.0F, 0.0F);
                this.animator.rotate(this.rhand, (float) Math.toRadians(20.0), 0.0F, 0.0F);
                this.animator.rotate(this.larm, (float) Math.toRadians(-90.0), (float) Math.toRadians(-30.0), 0.0F);
                this.animator.rotate(this.lhand, (float) Math.toRadians(-10.0), (float) Math.toRadians(-10.0), 0.0F);
                this.animator.move(this.larm, 0.0F, -1.0F, -1.0F);
                this.animator.move(this.rhand, 0.0F, 0.0F, 1.0F);
                this.animator.move(this.lhand, 0.0F, 0.0F, 1.0F);
                break;
            case 3:
                this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(10.0), (float) Math.toRadians(10.0));
                this.animator.rotate(this.lleg, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
                this.animator.rotate(this.rleg, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
                this.animator.move(this.rleg, 0.0F, 0.25F, 0.0F);
                this.animator.move(this.lleg, 0.0F, -0.75F, 0.0F);
                break;
            case 4:
                this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-10.0), (float) Math.toRadians(-10.0));
                this.animator.rotate(this.lleg, 0.0F, 0.0F, (float) Math.toRadians(10.0));
                this.animator.rotate(this.rleg, 0.0F, 0.0F, (float) Math.toRadians(10.0));
                this.animator.move(this.lleg, 0.0F, 0.25F, 0.0F);
                this.animator.move(this.rleg, 0.0F, -0.75F, 0.0F);
                break;
            case 5:
                this.animator.rotate(this.head, (float) Math.toRadians(20.0), 0.0F, 0.0F);
                this.animator.rotate(this.neck, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
                this.animator.rotate(this.body, (float) Math.toRadians(10.0), 0.0F, 0.0F);
                this.animator.rotate(this.lleg, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
                this.animator.rotate(this.rleg, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
                this.animator.move(this.head, 0.0F, -1.0F, 0.0F);
                this.animator.move(this.body, 0.0F, 0.0F, 6.0F);
                this.animator.move(this.lleg, 0.0F, 0.35F, 0.0F);
                this.animator.move(this.rleg, 0.0F, 0.35F, 0.0F);
        }
    }

    public void setupAnim(VallumraptorEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        float walkSpeed = 0.5F;
        float walkDegree = 0.85F;
        float sprintSpeed = 0.5F;
        float sprintDegree = 0.5F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float sprintProgress = entity.getRunProgress(partialTick);
        float walkProgress = sprintProgress - 1.0F;
        float jumpProgress = entity.getLeapProgress(partialTick);
        float walkAmount = limbSwingAmount * walkProgress * (1.0F - jumpProgress);
        float sprintAmount = limbSwingAmount * sprintProgress * (1.0F - jumpProgress);
        float stillAmount = 1.0F - limbSwingAmount;
        float relaxedAmount = entity.getRelaxedProgress(partialTick);
        float sitAmount = Math.max(entity.getSitProgress(partialTick), relaxedAmount);
        float puzzleRot = entity.getPuzzledHeadRot(partialTick);
        float buryEggsAmount = entity.getBuryEggsProgress(partialTick);
        float puzzleRotRad = (float) Math.toRadians((double) puzzleRot);
        float puzzleRotPoint = puzzleRot * 0.05F;
        float yaw = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTick;
        float tailYaw = Mth.wrapDegrees(entity.getTailYaw(partialTick) - yaw) / (180.0F / (float) Math.PI);
        float danceAmount = entity.getDanceProgress(partialTick);
        float danceSpeed = 0.5F;
        if (entity.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
            this.setupAnimForAnimation(entity, entity.getAnimation(), limbSwing, limbSwingAmount, ageInTicks);
        }
        if (entity.getAnimation() != VallumraptorEntity.ANIMATION_CALL_2) {
            this.progressPositionPrev(this.head, walkAmount, 0.0F, 1.0F, -1.0F, 1.0F);
            this.progressPositionPrev(this.head, sprintAmount, 0.0F, -2.0F, 2.0F, 1.0F);
        }
        if (buryEggsAmount > 0.0F) {
            limbSwing = ageInTicks;
            walkAmount = buryEggsAmount * 0.5F;
            this.body.swing(0.25F, 0.4F, false, 0.0F, 0.0F, ageInTicks, buryEggsAmount);
            this.neck.swing(0.25F, 0.4F, true, -1.0F, 0.0F, ageInTicks, buryEggsAmount);
        }
        this.progressPositionPrev(this.body, jumpProgress, 0.0F, 0.0F, 2.0F, 1.0F);
        this.progressPositionPrev(this.larm, jumpProgress, 0.0F, 0.0F, -2.0F, 1.0F);
        this.progressPositionPrev(this.rarm, jumpProgress, 0.0F, 0.0F, -2.0F, 1.0F);
        this.progressPositionPrev(this.neck, jumpProgress, 0.0F, -1.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.body, jumpProgress, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail, jumpProgress, (float) Math.toRadians(10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tailTip, jumpProgress, (float) Math.toRadians(10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rarm, jumpProgress, (float) Math.toRadians(-40.0), (float) Math.toRadians(50.0), (float) Math.toRadians(50.0), 1.0F);
        this.progressRotationPrev(this.larm, jumpProgress, (float) Math.toRadians(-40.0), (float) Math.toRadians(-50.0), (float) Math.toRadians(-50.0), 1.0F);
        this.progressRotationPrev(this.neck, jumpProgress, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.jaw, jumpProgress, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, jumpProgress, (float) Math.toRadians(-10.0), (float) Math.toRadians(10.0), (float) Math.toRadians(10.0), 1.0F);
        this.progressRotationPrev(this.lleg, jumpProgress, (float) Math.toRadians(-10.0), (float) Math.toRadians(-10.0), (float) Math.toRadians(-10.0), 1.0F);
        this.progressRotationPrev(this.rleg2, jumpProgress, (float) Math.toRadians(-30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg2, jumpProgress, (float) Math.toRadians(-30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rfoot, jumpProgress, (float) Math.toRadians(10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lfoot, jumpProgress, (float) Math.toRadians(10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rarm, danceAmount, (float) Math.toRadians(-50.0), (float) Math.toRadians(50.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.larm, danceAmount, (float) Math.toRadians(-50.0), (float) Math.toRadians(-50.0), 0.0F, 1.0F);
        this.progressPositionPrev(this.body, sitAmount, 0.0F, 6.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.rarm, sitAmount, 0.0F, -2.0F, 1.0F, 1.0F);
        this.progressPositionPrev(this.larm, sitAmount, 0.0F, -2.0F, 1.0F, 1.0F);
        this.progressPositionPrev(this.rleg, sitAmount, 0.0F, -1.5F, 5.0F, 1.0F);
        this.progressPositionPrev(this.lleg, sitAmount, 0.0F, -1.5F, 5.0F, 1.0F);
        this.progressRotationPrev(this.rleg, sitAmount, (float) Math.toRadians(-20.0), (float) Math.toRadians(25.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg2, sitAmount, (float) Math.toRadians(-50.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rfoot, sitAmount, (float) Math.toRadians(70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, sitAmount, (float) Math.toRadians(-20.0), (float) Math.toRadians(-25.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg2, sitAmount, (float) Math.toRadians(-50.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lfoot, sitAmount, (float) Math.toRadians(70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail, sitAmount, (float) Math.toRadians(-10.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.head, relaxedAmount, 1.0F, -1.0F, 3.0F, 1.0F);
        this.progressRotationPrev(this.neck, relaxedAmount, (float) Math.toRadians(120.0), (float) Math.toRadians(30.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.head, relaxedAmount, (float) Math.toRadians(-120.0), (float) Math.toRadians(30.0), (float) Math.toRadians(-30.0), 1.0F);
        this.progressRotationPrev(this.tail, relaxedAmount, 0.0F, (float) Math.toRadians(-30.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.tailTip, relaxedAmount, (float) Math.toRadians(-10.0), (float) Math.toRadians(-30.0), 0.0F, 1.0F);
        this.swing(this.tail, 0.1F, 0.2F, false, 2.0F, 0.0F, ageInTicks, stillAmount);
        this.swing(this.tailTip, 0.1F, 0.2F, false, 1.0F, 0.0F, ageInTicks, stillAmount);
        this.walk(this.rarm, 0.1F, 0.1F, false, 3.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.larm, 0.1F, 0.1F, false, 3.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.rhand, 0.1F, 0.1F, true, 3.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.lhand, 0.1F, 0.1F, false, 3.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.neck, 0.1F, 0.05F, false, 4.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.head, 0.1F, 0.05F, true, 4.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.body, walkSpeed * 2.0F, walkDegree * 0.05F, false, -2.0F, 0.0F, limbSwing, walkAmount);
        this.bob(this.body, -walkSpeed, walkDegree * -4.0F, true, limbSwing, walkAmount);
        this.swing(this.tail, walkSpeed * 1.0F, walkDegree * 0.5F, false, 2.0F, 0.0F, limbSwing, walkAmount);
        this.swing(this.tailTip, walkSpeed * 1.0F, walkDegree * 0.5F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.lleg, walkSpeed, walkDegree * 1.0F, true, 0.0F, 0.2F, limbSwing, walkAmount);
        this.walk(this.lleg2, walkSpeed, walkDegree * 0.5F, true, -1.0F, -0.2F, limbSwing, walkAmount);
        this.swing(this.lleg2, walkSpeed, walkDegree * -0.5F, true, -1.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.lfoot, walkSpeed, walkDegree * -1.0F, true, -1.5F, 0.0F, limbSwing, walkAmount);
        this.swing(this.lfoot, walkSpeed, walkDegree * 0.5F, false, 0.0F, 0.0F, limbSwing, walkAmount);
        this.lfoot.rotationPointY = this.lfoot.rotationPointY - Math.abs((float) (Math.cos((double) (limbSwing * walkSpeed - 1.5F)) * (double) walkDegree * 1.5 * (double) walkAmount));
        this.walk(this.rleg, walkSpeed, walkDegree * 1.0F, false, 0.0F, 0.2F, limbSwing, walkAmount);
        this.walk(this.rleg2, walkSpeed, walkDegree * 0.5F, false, -1.0F, -0.2F, limbSwing, walkAmount);
        this.swing(this.rleg2, walkSpeed, walkDegree * -0.5F, false, -1.0F, 0.0F, limbSwing, walkAmount);
        this.walk(this.rfoot, walkSpeed, walkDegree * -1.0F, false, -1.5F, 0.0F, limbSwing, walkAmount);
        this.swing(this.rfoot, walkSpeed, walkDegree * 0.5F, false, 0.0F, 0.0F, limbSwing, walkAmount);
        this.rfoot.rotationPointY = this.rfoot.rotationPointY - Math.abs((float) (Math.cos((double) (limbSwing * walkSpeed - 1.5F)) * (double) walkDegree * 1.5 * (double) walkAmount));
        this.walk(this.neck, walkSpeed * 2.0F, walkDegree * 0.3F, false, -1.0F, -0.5F, limbSwing, walkAmount);
        this.walk(this.head, walkSpeed * 2.0F, walkDegree * 0.3F, true, -1.0F, -0.5F, limbSwing, walkAmount);
        this.walk(this.larm, walkSpeed * 2.0F, walkDegree * 0.3F, false, 1.0F, -0.1F, limbSwing, walkAmount);
        this.walk(this.rarm, walkSpeed * 2.0F, walkDegree * 0.3F, false, 1.0F, -0.1F, limbSwing, walkAmount);
        this.walk(this.body, sprintSpeed * 2.0F, sprintDegree * 0.05F, false, -2.0F, 0.1F, limbSwing, sprintAmount);
        this.bob(this.body, 2.0F * sprintSpeed, sprintDegree * 3.0F, false, limbSwing, sprintAmount);
        this.walk(this.lleg, sprintSpeed, sprintDegree * 1.0F, true, 0.0F, 0.2F, limbSwing, sprintAmount);
        this.walk(this.lleg2, sprintSpeed, sprintDegree * 0.9F, true, -1.0F, -0.2F, limbSwing, sprintAmount);
        this.swing(this.lleg2, sprintSpeed, sprintDegree * -0.5F, true, -1.0F, 0.0F, limbSwing, sprintAmount);
        this.walk(this.lfoot, sprintSpeed, sprintDegree * -1.0F, true, -1.5F, 0.0F, limbSwing, sprintAmount);
        this.swing(this.lfoot, sprintSpeed, sprintDegree * 1.0F, true, 0.0F, 0.0F, limbSwing, sprintAmount);
        this.lfoot.rotationPointY = this.lfoot.rotationPointY - Math.abs((float) (Math.cos((double) (limbSwing * sprintSpeed - 1.5F)) * (double) sprintDegree * 2.5 * (double) sprintAmount));
        this.walk(this.rleg, sprintSpeed, sprintDegree * 1.0F, false, 0.0F, 0.2F, limbSwing, sprintAmount);
        this.walk(this.rleg2, sprintSpeed, sprintDegree * 0.9F, false, -1.0F, -0.2F, limbSwing, sprintAmount);
        this.swing(this.rleg2, sprintSpeed, sprintDegree * -0.5F, false, -1.0F, 0.0F, limbSwing, sprintAmount);
        this.walk(this.rfoot, sprintSpeed, sprintDegree * -1.0F, false, -1.5F, 0.0F, limbSwing, sprintAmount);
        this.swing(this.rfoot, sprintSpeed, sprintDegree * -1.0F, false, 0.0F, 0.0F, limbSwing, sprintAmount);
        this.rfoot.rotationPointY = this.rfoot.rotationPointY - Math.abs((float) (Math.cos((double) (limbSwing * sprintSpeed - 1.5F)) * (double) sprintDegree * 2.5 * (double) sprintAmount));
        this.walk(this.tail, sprintSpeed * 2.0F, sprintDegree * 0.3F, false, 1.0F, -0.1F, limbSwing, sprintAmount);
        this.swing(this.tail, sprintSpeed * 1.0F, sprintDegree * 0.5F, false, 2.0F, 0.0F, limbSwing, sprintAmount);
        this.swing(this.tailTip, sprintSpeed * 1.0F, sprintDegree * 0.5F, false, 1.0F, 0.0F, limbSwing, sprintAmount);
        this.walk(this.neck, sprintSpeed * 1.5F, sprintDegree * 0.1F, false, -2.0F, 1.0F, limbSwing, sprintAmount);
        this.walk(this.head, sprintSpeed * 1.5F, sprintDegree * -0.1F, false, -1.5F, -0.9F, limbSwing, sprintAmount);
        this.larm.rotationPointZ = this.larm.rotationPointZ - (Math.abs((float) (Math.cos((double) (limbSwing * sprintSpeed + 1.5F)) * (double) sprintDegree * 2.0 * (double) sprintAmount)) + sprintAmount);
        this.walk(this.larm, sprintSpeed * 2.0F, sprintDegree * 0.3F, false, 1.0F, -0.1F, limbSwing, sprintAmount);
        this.swing(this.lhand, sprintSpeed * 2.0F, sprintDegree * 0.5F, false, 1.0F, 0.1F, limbSwing, sprintAmount);
        this.rarm.rotationPointZ = this.rarm.rotationPointZ - (Math.abs((float) (Math.cos((double) (limbSwing * sprintSpeed + 1.5F)) * (double) sprintDegree * 2.0 * (double) sprintAmount)) + sprintAmount);
        this.walk(this.rarm, sprintSpeed * 2.0F, sprintDegree * 0.3F, false, 1.0F, -0.1F, limbSwing, sprintAmount);
        this.swing(this.rhand, sprintSpeed * 2.0F, sprintDegree * 0.5F, true, 1.0F, 0.1F, limbSwing, sprintAmount);
        this.swing(this.neck, sprintSpeed * 1.5F, sprintDegree * 0.2F, false, 2.0F, 0.0F, limbSwing, sprintAmount);
        this.swing(this.head, sprintSpeed * 1.5F, sprintDegree * -0.2F, false, 2.0F, 0.0F, limbSwing, sprintAmount);
        this.swing(this.body, sprintSpeed * 1.0F, sprintDegree * 0.2F, false, 0.0F, 0.0F, limbSwing, sprintAmount);
        this.head.rotationPointX = this.head.rotationPointX + Mth.clamp(puzzleRotPoint, -1.5F, 1.5F);
        this.head.rotateAngleZ += puzzleRotRad;
        this.neck.rotateAngleZ += puzzleRotRad * -0.3F;
        this.head.rotateAngleX = this.head.rotateAngleX + Math.abs(puzzleRotRad * 0.3F);
        this.neck.rotateAngleX = this.neck.rotateAngleX - Math.abs(puzzleRotRad * 0.3F);
        this.tail.rotateAngleY += tailYaw * 0.8F;
        this.tailTip.rotateAngleY += tailYaw * 0.2F;
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.neck, this.head });
        this.swing(this.body, danceSpeed, 0.1F, false, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.flap(this.rarm, danceSpeed, 0.5F, false, 0.0F, 0.0F, ageInTicks, danceAmount);
        this.flap(this.larm, danceSpeed, 0.5F, false, 0.0F, 0.0F, ageInTicks, danceAmount);
        this.walk(this.rhand, danceSpeed, 0.5F, false, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.walk(this.lhand, danceSpeed, 0.5F, true, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.walk(this.neck, danceSpeed, 0.25F, false, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.walk(this.head, danceSpeed, 0.25F, false, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.walk(this.tail, danceSpeed, 0.15F, false, 2.0F, 0.0F, ageInTicks, danceAmount);
        this.bob(this.rarm, danceSpeed, 1.0F, false, ageInTicks, danceAmount);
        this.bob(this.larm, danceSpeed, 1.0F, false, ageInTicks, danceAmount);
    }

    private void setupAnimForAnimation(VallumraptorEntity entity, Animation animation, float limbSwing, float limbSwingAmount, float ageInTicks) {
        float partialTick = ageInTicks - (float) entity.f_19797_;
        if (animation == VallumraptorEntity.ANIMATION_SHAKE) {
            float animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 3.0F, animation, partialTick, 0);
            this.progressRotationPrev(this.neck, animationIntensity, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
            this.progressRotationPrev(this.head, animationIntensity, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
            this.progressPositionPrev(this.head, animationIntensity, 0.0F, -0.5F, 0.0F, 1.0F);
            this.swing(this.body, 0.5F, 0.2F, false, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.flap(this.body, 0.5F, 0.2F, false, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.swing(this.rleg, 0.5F, 0.2F, true, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.flap(this.rleg, 0.5F, 0.2F, true, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.rleg.rotationPointY = this.rleg.rotationPointY - ((float) (Math.cos((double) (ageInTicks * 0.5F)) * -0.5 * (double) animationIntensity) + animationIntensity * 0.2F);
            this.lleg.rotationPointY = this.lleg.rotationPointY - ((float) (Math.cos((double) (ageInTicks * 0.5F)) * 0.5 * (double) animationIntensity) + animationIntensity * 0.2F);
            this.swing(this.lleg, 0.5F, 0.2F, true, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.flap(this.lleg, 0.5F, 0.2F, true, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.flap(this.neck, 0.5F, 0.5F, false, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.flap(this.headquill, 0.5F, 0.5F, false, -1.0F, 0.0F, ageInTicks, animationIntensity);
            this.swing(this.neck, 0.5F, 0.5F, false, 2.0F, 0.0F, ageInTicks, animationIntensity);
            this.flap(this.head, 0.5F, 0.15F, false, 2.0F, 0.0F, ageInTicks, animationIntensity);
            this.swing(this.head, 0.5F, 0.5F, false, 2.0F, 0.0F, ageInTicks, animationIntensity);
            this.swing(this.tail, 0.5F, 0.5F, true, 2.0F, 0.0F, ageInTicks, animationIntensity);
            this.flap(this.tail, 0.5F, 0.5F, true, 2.0F, 0.0F, ageInTicks, animationIntensity);
            this.flap(this.rarm, 0.5F, 0.5F, false, -1.0F, 0.0F, ageInTicks, animationIntensity);
            this.flap(this.larm, 0.5F, 0.5F, false, -1.0F, 0.0F, ageInTicks, animationIntensity);
        }
    }

    public void translateToHand(PoseStack matrixStackIn, boolean left) {
        this.body.translateAndRotate(matrixStackIn);
        if (left) {
            this.larm.translateAndRotate(matrixStackIn);
        } else {
            this.rarm.translateAndRotate(matrixStackIn);
        }
    }
}