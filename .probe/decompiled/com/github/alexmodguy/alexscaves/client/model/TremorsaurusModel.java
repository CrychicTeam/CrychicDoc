package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.item.DinosaurSpiritEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.animation.LegSolver;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class TremorsaurusModel extends AdvancedEntityModel<TremorsaurusEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox rleg2;

    private final AdvancedModelBox rfoot;

    private final AdvancedModelBox rclaw1;

    private final AdvancedModelBox rclaw2;

    private final AdvancedModelBox rclaw3;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox lleg2;

    private final AdvancedModelBox lfoot;

    private final AdvancedModelBox lclaw1;

    private final AdvancedModelBox lclaw2;

    private final AdvancedModelBox lclaw3;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox glasses;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tailTip;

    private final ModelAnimator animator;

    public TremorsaurusModel() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, -17.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addBox(-13.0F, -15.0F, -20.0F, 26.0F, 30.0F, 39.0F, 0.0F, false);
        this.body.setTextureOffset(91, 0).addBox(-1.0F, -18.0F, -9.0F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        this.body.setTextureOffset(91, 0).addBox(-1.0F, -18.0F, -18.0F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        this.body.setTextureOffset(91, 0).addBox(-1.0F, -18.0F, -2.0F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        this.body.setTextureOffset(91, 0).addBox(-1.0F, -18.0F, 5.0F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        this.body.setTextureOffset(91, 0).addBox(-1.0F, -18.0F, 12.0F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(-13.0F, 11.0F, -13.5F);
        this.body.addChild(this.rarm);
        this.setRotateAngle(this.rarm, 0.0F, 0.0F, -1.2654F);
        this.rarm.setTextureOffset(16, 24).addBox(-8.0F, 0.0F, -1.5F, 8.0F, 3.0F, 3.0F, 0.0F, true);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(13.0F, 11.0F, -13.5F);
        this.body.addChild(this.larm);
        this.setRotateAngle(this.larm, 0.0F, 0.0F, 1.2654F);
        this.larm.setTextureOffset(16, 24).addBox(0.0F, 0.0F, -1.5F, 8.0F, 3.0F, 3.0F, 0.0F, false);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-11.5F, 2.0F, 6.5F);
        this.body.addChild(this.rleg);
        this.rleg.setTextureOffset(61, 106).addBox(-6.5F, -2.0F, -9.5F, 13.0F, 26.0F, 19.0F, 0.0F, true);
        this.rleg2 = new AdvancedModelBox(this);
        this.rleg2.setRotationPoint(0.0F, 21.5F, 7.0F);
        this.rleg.addChild(this.rleg2);
        this.rleg2.setTextureOffset(0, 0).addBox(-3.5F, -1.5F, -3.5F, 7.0F, 15.0F, 9.0F, 0.0F, true);
        this.rfoot = new AdvancedModelBox(this);
        this.rfoot.setRotationPoint(0.5F, 13.5F, 1.0F);
        this.rleg2.addChild(this.rfoot);
        this.rfoot.setTextureOffset(125, 127).addBox(-6.0F, 0.0F, -12.5F, 11.0F, 4.0F, 17.0F, 0.0F, true);
        this.rclaw1 = new AdvancedModelBox(this);
        this.rclaw1.setRotationPoint(4.0F, 0.0F, -12.5F);
        this.rfoot.addChild(this.rclaw1);
        this.rclaw1.setTextureOffset(91, 8).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 4.0F, 4.0F, 0.0F, true);
        this.rclaw2 = new AdvancedModelBox(this);
        this.rclaw2.setRotationPoint(-0.5F, 0.0F, -12.5F);
        this.rfoot.addChild(this.rclaw2);
        this.rclaw2.setTextureOffset(91, 8).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 4.0F, 4.0F, 0.0F, true);
        this.rclaw3 = new AdvancedModelBox(this);
        this.rclaw3.setRotationPoint(-5.0F, 0.0F, -12.5F);
        this.rfoot.addChild(this.rclaw3);
        this.rclaw3.setTextureOffset(91, 8).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 4.0F, 4.0F, 0.0F, true);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(11.5F, 2.0F, 6.5F);
        this.body.addChild(this.lleg);
        this.lleg.setTextureOffset(61, 106).addBox(-6.5F, -2.0F, -9.5F, 13.0F, 26.0F, 19.0F, 0.0F, false);
        this.lleg2 = new AdvancedModelBox(this);
        this.lleg2.setRotationPoint(0.0F, 21.5F, 7.0F);
        this.lleg.addChild(this.lleg2);
        this.lleg2.setTextureOffset(0, 0).addBox(-3.5F, -1.5F, -3.5F, 7.0F, 15.0F, 9.0F, 0.0F, false);
        this.lfoot = new AdvancedModelBox(this);
        this.lfoot.setRotationPoint(-0.5F, 13.5F, 1.0F);
        this.lleg2.addChild(this.lfoot);
        this.lfoot.setTextureOffset(125, 127).addBox(-5.0F, 0.0F, -12.5F, 11.0F, 4.0F, 17.0F, 0.0F, false);
        this.lclaw1 = new AdvancedModelBox(this);
        this.lclaw1.setRotationPoint(-4.0F, 0.0F, -12.5F);
        this.lfoot.addChild(this.lclaw1);
        this.lclaw1.setTextureOffset(91, 8).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);
        this.lclaw2 = new AdvancedModelBox(this);
        this.lclaw2.setRotationPoint(0.5F, 0.0F, -12.5F);
        this.lfoot.addChild(this.lclaw2);
        this.lclaw2.setTextureOffset(91, 8).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);
        this.lclaw3 = new AdvancedModelBox(this);
        this.lclaw3.setRotationPoint(5.0F, 0.0F, -12.5F);
        this.lfoot.addChild(this.lclaw3);
        this.lclaw3.setTextureOffset(91, 8).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this);
        this.neck.setRotationPoint(0.0F, -5.0F, -14.0F);
        this.body.addChild(this.neck);
        this.setRotateAngle(this.neck, 0.7854F, 0.0F, 0.0F);
        this.neck.setTextureOffset(44, 111).addBox(-1.0F, -19.0F, 3.0F, 2.0F, 5.0F, 3.0F, 0.0F, false);
        this.neck.setTextureOffset(0, 69).addBox(-1.0F, -26.0F, 1.0F, 2.0F, 5.0F, 5.0F, 0.0F, false);
        this.neck.setTextureOffset(91, 0).addBox(-1.0F, -26.0F, -6.0F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        this.neck.setTextureOffset(0, 111).addBox(-7.0F, -23.0F, -13.0F, 14.0F, 26.0F, 16.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -17.0F, -8.0F);
        this.neck.addChild(this.head);
        this.setRotateAngle(this.head, -0.5672F, 0.0F, 0.0F);
        this.head.setTextureOffset(91, 0).addBox(-7.5F, -6.1F, -21.0F, 15.0F, 9.0F, 21.0F, 0.0F, false);
        this.head.setTextureOffset(130, 59).addBox(-7.5F, -0.1F, -7.0F, 15.0F, 3.0F, 7.0F, 0.0F, false);
        this.head.setTextureOffset(0, 25).addBox(2.5F, -9.1F, -7.0F, 5.0F, 3.0F, 6.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(5.5F, -11.1F, -7.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(-7.5F, -11.1F, -7.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        this.head.setTextureOffset(0, 25).addBox(-7.5F, -9.1F, -7.0F, 5.0F, 3.0F, 6.0F, 0.0F, true);
        this.head.setTextureOffset(0, 153).addBox(-7.5F, 2.9F, -21.0F, 15.0F, 5.0F, 21.0F, 0.0F, false);
        this.glasses = new AdvancedModelBox(this);
        this.glasses.setRotationPoint(0.0F, -7.6F, -2.5F);
        this.head.addChild(this.glasses);
        this.glasses.setTextureOffset(91, 30).addBox(-8.0F, -1.5F, -5.0F, 16.0F, 3.0F, 6.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(0.0F, -0.25F, -2.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(124, 73).addBox(-8.5F, 3.15F, -20.0F, 17.0F, 7.0F, 15.0F, 0.0F, false);
        this.jaw.setTextureOffset(125, 106).addBox(-8.5F, -3.85F, -20.0F, 17.0F, 7.0F, 14.0F, 0.0F, false);
        this.jaw.setTextureOffset(57, 70).addBox(-8.5F, 0.15F, -5.0F, 17.0F, 10.0F, 8.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, -4.0F, 18.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(135, 2).addBox(-8.0F, -9.0F, -3.0F, 16.0F, 18.0F, 28.0F, 0.0F, false);
        this.tail.setTextureOffset(91, 0).addBox(-1.0F, -12.0F, 3.0F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        this.tail.setTextureOffset(91, 0).addBox(-1.0F, -12.0F, 10.0F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        this.tail.setTextureOffset(0, 111).addBox(-1.0F, -11.0F, 17.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        this.tailTip = new AdvancedModelBox(this);
        this.tailTip.setRotationPoint(0.0F, -2.5F, 24.0F);
        this.tail.addChild(this.tailTip);
        this.tailTip.setTextureOffset(0, 1).addBox(0.0F, -5.5F, 20.0F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        this.tailTip.setTextureOffset(0, 2).addBox(0.0F, -5.5F, 15.0F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        this.tailTip.setTextureOffset(157, 64).addBox(-4.0F, -4.5F, -2.0F, 8.0F, 9.0F, 31.0F, 0.0F, false);
        this.tailTip.setTextureOffset(0, 111).addBox(-1.0F, -6.5F, 9.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        this.tailTip.setTextureOffset(0, 111).addBox(-1.0F, -6.5F, 3.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
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

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(TremorsaurusEntity.ANIMATION_SNIFF);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(TremorsaurusEntity.ANIMATION_SPEAK);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.neck, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(7);
        this.animator.rotate(this.neck, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(TremorsaurusEntity.ANIMATION_ROAR);
        this.animator.startKeyframe(5);
        this.animator.move(this.lleg, 0.0F, -1.5F, 0.0F);
        this.animator.move(this.rleg, 0.0F, -1.5F, 0.0F);
        this.animator.rotate(this.body, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.lleg, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.rleg, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animatePose(0);
        this.animator.rotate(this.neck, 0.0F, (float) Math.toRadians(30.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, (float) Math.toRadians(20.0), (float) Math.toRadians(-20.0));
        this.animator.rotate(this.jaw, (float) Math.toRadians(60.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(20);
        this.animatePose(0);
        this.animator.rotate(this.neck, 0.0F, (float) Math.toRadians(-30.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, (float) Math.toRadians(-20.0), (float) Math.toRadians(20.0));
        this.animator.rotate(this.jaw, (float) Math.toRadians(60.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(10);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(TremorsaurusEntity.ANIMATION_BITE);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.neck, 0.0F, 1.0F, -4.0F);
        this.animator.move(this.head, 0.0F, -3.0F, 1.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-45.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(70.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(TremorsaurusEntity.ANIMATION_SHAKE_PREY);
        this.animator.startKeyframe(5);
        this.animatePose(0);
        this.animator.rotate(this.jaw, (float) Math.toRadians(40.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.jaw, (float) Math.toRadians(40.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(25);
        this.animator.resetKeyframe(5);
    }

    private void animatePose(int pose) {
        switch(pose) {
            case 0:
                this.animator.move(this.body, 0.0F, 2.0F, -5.0F);
                this.animator.move(this.neck, 0.0F, 3.0F, 0.0F);
                this.animator.move(this.head, 0.0F, 0.0F, -5.0F);
                this.animator.move(this.lleg, 0.0F, 3.0F, 3.0F);
                this.animator.move(this.rleg, 0.0F, 3.0F, 3.0F);
                this.animator.rotate(this.body, (float) Math.toRadians(30.0), 0.0F, 0.0F);
                this.animator.rotate(this.lleg, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
                this.animator.rotate(this.rleg, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
                this.animator.rotate(this.neck, (float) Math.toRadians(-25.0), 0.0F, 0.0F);
                this.animator.rotate(this.head, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
                this.animator.rotate(this.tail, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
                this.animator.rotate(this.tailTip, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        }
    }

    private void setupAnimForAnimation(TremorsaurusEntity entity, Animation animation, float limbSwing, float limbSwingAmount, float ageInTicks) {
        float partialTick = ageInTicks - (float) entity.f_19797_;
        if (entity.getAnimation() == TremorsaurusEntity.ANIMATION_ROAR) {
            float animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 1.0F, animation, partialTick, 5, 40);
            this.head.swing(1.0F, 0.1F, false, -1.0F, 0.0F, ageInTicks, animationIntensity);
            this.jaw.walk(2.0F, 0.1F, false, 1.0F, 0.0F, ageInTicks, animationIntensity);
        }
        if (entity.getAnimation() == TremorsaurusEntity.ANIMATION_SHAKE_PREY) {
            float animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 1.0F, animation, partialTick, 5, 30);
            this.body.swing(0.6F, 0.8F, false, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.lleg.swing(0.6F, 0.8F, true, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.rleg.swing(0.6F, 0.8F, true, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.neck.swing(0.6F, 0.8F, false, -1.0F, 0.0F, ageInTicks, animationIntensity);
            this.head.swing(0.6F, 0.8F, false, -2.0F, 0.0F, ageInTicks, animationIntensity);
            this.tail.swing(0.6F, 0.8F, true, 2.0F, 0.0F, ageInTicks, animationIntensity);
            this.tailTip.swing(0.6F, 0.8F, true, 1.0F, 0.0F, ageInTicks, animationIntensity);
        }
    }

    public void setupAnim(TremorsaurusEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        if (entity.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
            this.setupAnimForAnimation(entity, entity.getAnimation(), limbSwing, limbSwingAmount, ageInTicks);
        }
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float danceAmount = entity.getDanceProgress(partialTicks);
        float sitAmount = entity.getSitProgress(partialTicks);
        float buryEggsAmount = entity.getBuryEggsProgress(partialTicks);
        float danceSpeed = 0.5F;
        this.glasses.showModel = danceAmount > 0.0F;
        float walkSpeed = 0.8F;
        float walkDegree = 1.0F;
        this.progressPositionPrev(this.body, sitAmount, 0.0F, 18.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.rleg, sitAmount, 0.0F, -11.0F, 12.0F, 1.0F);
        this.progressPositionPrev(this.lleg, sitAmount, 0.0F, -11.0F, 12.0F, 1.0F);
        this.progressRotationPrev(this.rleg, sitAmount, (float) Math.toRadians(-20.0), (float) Math.toRadians(15.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg2, sitAmount, (float) Math.toRadians(-50.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rfoot, sitAmount, (float) Math.toRadians(70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, sitAmount, (float) Math.toRadians(-20.0), (float) Math.toRadians(-15.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg2, sitAmount, (float) Math.toRadians(-50.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lfoot, sitAmount, (float) Math.toRadians(70.0), 0.0F, 0.0F, 1.0F);
        if (buryEggsAmount > 0.0F) {
            limbSwing = ageInTicks;
            limbSwingAmount = buryEggsAmount * 0.5F;
            this.body.swing(0.25F, 0.4F, false, 0.0F, 0.0F, ageInTicks, buryEggsAmount);
            this.neck.swing(0.25F, 0.4F, true, -1.0F, 0.0F, ageInTicks, buryEggsAmount);
        }
        float bodyIdleBob = this.walkValue(ageInTicks, 1.0F, 0.1F, -1.0F, 1.0F, false);
        this.walk(this.neck, 0.1F, 0.03F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.head, 0.1F, 0.03F, true, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.jaw, 0.1F, 0.03F, true, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.rarm, 0.1F, 0.05F, false, 3.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.larm, 0.1F, 0.05F, true, 3.0F, -0.1F, ageInTicks, 1.0F);
        this.swing(this.rarm, 0.1F, 0.1F, true, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.larm, 0.1F, 0.1F, true, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail, 0.1F, 0.15F, true, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tailTip, 0.1F, 0.15F, true, -2.0F, 0.0F, ageInTicks, 1.0F);
        float bodyWalkBob = -Math.abs(this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -1.5F, 4.0F, false));
        this.body.rotationPointY += bodyIdleBob + bodyWalkBob;
        this.lleg.rotationPointY -= bodyIdleBob + bodyWalkBob;
        this.rleg.rotationPointY -= bodyIdleBob + bodyWalkBob;
        this.swing(this.body, walkSpeed, walkDegree * 0.3F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail, walkSpeed, walkDegree * 0.5F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tailTip, walkSpeed, walkDegree * 0.5F, false, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.body, walkSpeed, walkDegree * 0.3F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.neck, walkSpeed, walkDegree * 0.3F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.lleg, walkSpeed, walkDegree * 0.3F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.rleg, walkSpeed, walkDegree * 0.3F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.body, walkSpeed, walkDegree * 0.5F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.tail, walkSpeed, walkDegree * 0.5F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.lleg, walkSpeed, walkDegree * 0.5F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.rleg, walkSpeed, walkDegree * 0.5F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.neck, walkSpeed, walkDegree * 0.5F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.lleg, walkSpeed, walkDegree * 0.6F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.lleg2, walkSpeed, walkDegree * 0.6F, false, 1.5F, -0.1F, limbSwing, limbSwingAmount);
        this.walk(this.lfoot, walkSpeed, walkDegree * 1.0F, false, -1.5F, 0.35F, limbSwing, limbSwingAmount);
        this.lleg.rotationPointY = this.lleg.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 30.0F, true));
        this.lfoot.rotationPointY = this.lfoot.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -1.5F, walkDegree * 2.0F, false));
        this.walk(this.rleg, walkSpeed, walkDegree * 0.6F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rleg2, walkSpeed, walkDegree * 0.6F, true, 1.5F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(this.rfoot, walkSpeed, walkDegree * 1.0F, true, -1.5F, -0.35F, limbSwing, limbSwingAmount);
        this.rleg.rotationPointY = this.rleg.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 30.0F, false));
        this.rfoot.rotationPointY = this.rfoot.rotationPointY + Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -1.5F, walkDegree * 2.0F, true));
        this.flap(this.rarm, walkSpeed, walkDegree * 0.1F, false, -1.5F, 0.2F, limbSwing, limbSwingAmount);
        this.flap(this.larm, walkSpeed, walkDegree * 0.1F, true, -1.5F, 0.2F, limbSwing, limbSwingAmount);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.neck, this.head });
        this.articulateLegs(entity.legSolver, partialTicks);
        this.walk(this.neck, danceSpeed, 0.5F, false, 1.0F, 0.2F, ageInTicks, danceAmount);
        this.swing(this.neck, danceSpeed, 0.35F, false, 0.0F, 0.0F, ageInTicks, danceAmount);
        this.walk(this.head, danceSpeed, 0.5F, true, 1.0F, 0.2F, ageInTicks, danceAmount);
        this.swing(this.head, danceSpeed, 0.35F, true, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.swing(this.larm, danceSpeed, 0.6F, false, 1.0F, -0.3F, ageInTicks, danceAmount);
        this.swing(this.rarm, danceSpeed, 0.6F, false, 1.0F, -0.3F, ageInTicks, danceAmount);
        this.flap(this.larm, danceSpeed, 0.3F, false, 1.0F, -0.3F, ageInTicks, danceAmount);
        this.flap(this.rarm, danceSpeed, 0.3F, false, 1.0F, 0.3F, ageInTicks, danceAmount);
        this.swing(this.body, danceSpeed, 0.2F, false, -1.0F, 0.0F, ageInTicks, danceAmount);
    }

    private void articulateLegs(LegSolver legs, float partialTick) {
        float heightBackLeft = legs.legs[0].getHeight(partialTick);
        float heightBackRight = legs.legs[1].getHeight(partialTick);
        float max = (1.0F - ACMath.smin(1.0F - heightBackLeft, 1.0F - heightBackRight, 0.1F)) * 0.8F;
        this.body.rotationPointY += max * 16.0F;
        this.rleg.rotationPointY += (heightBackRight - max) * 16.0F;
        this.lleg.rotationPointY += (heightBackLeft - max) * 16.0F;
    }

    private float walkValue(float limbSwing, float limbSwingAmount, float speed, float offset, float degree, boolean inverse) {
        return (float) (Math.cos((double) (limbSwing * speed + offset)) * (double) degree * (double) limbSwingAmount * (double) (inverse ? -1 : 1));
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.neck, this.head, this.jaw, this.glasses, this.tail, this.tailTip, this.rarm, this.larm, this.rleg, this.rleg2, this.rfoot, new AdvancedModelBox[] { this.rclaw1, this.rclaw2, this.rclaw3, this.lleg, this.lleg2, this.lfoot, this.lclaw1, this.lclaw2, this.lclaw3 });
    }

    public void translateToMouth(PoseStack matrixStackIn) {
        this.body.translateAndRotate(matrixStackIn);
        this.neck.translateAndRotate(matrixStackIn);
        this.head.translateAndRotate(matrixStackIn);
    }

    public Vec3 getRiderPosition(Vec3 offsetIn) {
        PoseStack translationStack = new PoseStack();
        translationStack.pushPose();
        this.body.translateAndRotate(translationStack);
        Vector4f armOffsetVec = new Vector4f((float) offsetIn.x, (float) offsetIn.y, (float) offsetIn.z, 1.0F);
        armOffsetVec.mul(translationStack.last().pose());
        Vec3 vec3 = new Vec3((double) armOffsetVec.x(), (double) armOffsetVec.y(), (double) armOffsetVec.z());
        translationStack.popPose();
        return vec3;
    }

    public void animateSpirit(DinosaurSpiritEntity entityIn, float partialTicks) {
        this.resetToDefaultPose();
        float abilityProgress = entityIn.getAbilityProgress(partialTicks);
        float middleProgress = (float) Math.sin((double) abilityProgress * Math.PI);
        this.progressRotationPrev(this.neck, middleProgress, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, middleProgress, (float) Math.toRadians(-70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.jaw, middleProgress, (float) Math.toRadians(70.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.neck, abilityProgress, 0.0F, -4.0F, -9.0F, 1.0F);
    }

    public void renderSpiritToBuffer(PoseStack poseStack, VertexConsumer ivertexbuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 1.3F, 1.0F);
        this.neck.render(poseStack, ivertexbuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        poseStack.popPose();
    }
}