package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.item.DinosaurSpiritEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GrottoceratopsEntity;
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

public class GrottoceratopsModel extends AdvancedEntityModel<GrottoceratopsEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox bodySpikes;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox rleg2;

    private final AdvancedModelBox rfoot;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox lleg2;

    private final AdvancedModelBox lfoot;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tailSpike;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox tail2Spike;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox grassBunch;

    private final AdvancedModelBox grass;

    private final AdvancedModelBox grass2;

    private final AdvancedModelBox grassBunch2;

    private final AdvancedModelBox grass3;

    private final AdvancedModelBox grass4;

    private final AdvancedModelBox grass5;

    private final AdvancedModelBox grass6;

    private final ModelAnimator animator;

    public GrottoceratopsModel() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addBox(-11.0F, -14.0F, -17.5F, 22.0F, 30.0F, 35.0F, 0.0F, false);
        this.bodySpikes = new AdvancedModelBox(this);
        this.bodySpikes.setRotationPoint(0.0F, -2.5F, 0.0F);
        this.body.addChild(this.bodySpikes);
        this.bodySpikes.setTextureOffset(0, 22).addBox(0.0F, -15.5F, -21.5F, 0.0F, 31.0F, 43.0F, 0.0F, false);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-11.0F, 5.0F, 9.5F);
        this.body.addChild(this.rleg);
        this.rleg.setTextureOffset(117, 17).addBox(-5.0F, -2.0F, -8.0F, 9.0F, 15.0F, 12.0F, 0.0F, true);
        this.rleg2 = new AdvancedModelBox(this);
        this.rleg2.setRotationPoint(-0.5F, 8.5F, 2.0F);
        this.rleg.addChild(this.rleg2);
        this.rleg2.setTextureOffset(126, 44).addBox(-3.0F, -1.5F, -4.0F, 6.0F, 13.0F, 8.0F, 0.0F, true);
        this.rfoot = new AdvancedModelBox(this);
        this.rfoot.setRotationPoint(0.0F, 11.5F, -0.5F);
        this.rleg2.addChild(this.rfoot);
        this.rfoot.setTextureOffset(114, 0).addBox(-4.0F, 0.0F, -6.5F, 8.0F, 2.0F, 11.0F, 0.0F, true);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(11.0F, 5.0F, 9.5F);
        this.body.addChild(this.lleg);
        this.lleg.setTextureOffset(117, 17).addBox(-4.0F, -2.0F, -8.0F, 9.0F, 15.0F, 12.0F, 0.0F, false);
        this.lleg2 = new AdvancedModelBox(this);
        this.lleg2.setRotationPoint(0.5F, 8.5F, 2.0F);
        this.lleg.addChild(this.lleg2);
        this.lleg2.setTextureOffset(126, 44).addBox(-3.0F, -1.5F, -4.0F, 6.0F, 13.0F, 8.0F, 0.0F, false);
        this.lfoot = new AdvancedModelBox(this);
        this.lfoot.setRotationPoint(0.5F, 20.0F, 1.5F);
        this.lleg.addChild(this.lfoot);
        this.lfoot.setTextureOffset(114, 0).addBox(-4.0F, 0.0F, -6.5F, 8.0F, 2.0F, 11.0F, 0.0F, false);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(-10.0F, 4.0F, -12.0F);
        this.body.addChild(this.rarm);
        this.rarm.setTextureOffset(0, 0).addBox(-4.0F, -2.0F, -4.5F, 6.0F, 25.0F, 9.0F, 0.0F, true);
        this.rarm.setTextureOffset(79, 7).addBox(-1.0F, 17.0F, -7.5F, 3.0F, 3.0F, 3.0F, 0.0F, true);
        this.rarm.setTextureOffset(105, 31).addBox(-1.0F, 20.0F, -7.5F, 3.0F, 2.0F, 2.0F, 0.0F, true);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(10.0F, 4.0F, -12.0F);
        this.body.addChild(this.larm);
        this.larm.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, -4.5F, 6.0F, 25.0F, 9.0F, 0.0F, false);
        this.larm.setTextureOffset(79, 7).addBox(-2.0F, 17.0F, -7.5F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        this.larm.setTextureOffset(105, 31).addBox(-2.0F, 20.0F, -7.5F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, -1.0F, 16.5F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(65, 75).addBox(-6.0F, -5.0F, -1.0F, 12.0F, 12.0F, 21.0F, 0.0F, false);
        this.tailSpike = new AdvancedModelBox(this);
        this.tailSpike.setRotationPoint(0.0F, -5.0F, 10.5F);
        this.tail.addChild(this.tailSpike);
        this.tailSpike.setTextureOffset(0, 98).addBox(0.0F, -4.0F, -9.5F, 0.0F, 12.0F, 19.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this);
        this.tail2.setRotationPoint(0.0F, 0.0F, 19.0F);
        this.tail.addChild(this.tail2);
        this.tail2.setTextureOffset(26, 129).addBox(-10.0F, -3.0F, 14.0F, 7.0F, 3.0F, 3.0F, 0.0F, true);
        this.tail2.setTextureOffset(95, 46).addBox(-3.0F, -3.0F, -1.0F, 6.0F, 7.0F, 19.0F, 0.0F, false);
        this.tail2.setTextureOffset(26, 129).addBox(3.0F, -3.0F, 8.0F, 7.0F, 3.0F, 3.0F, 0.0F, false);
        this.tail2.setTextureOffset(26, 129).addBox(3.0F, -3.0F, 14.0F, 7.0F, 3.0F, 3.0F, 0.0F, false);
        this.tail2.setTextureOffset(26, 129).addBox(-10.0F, -3.0F, 8.0F, 7.0F, 3.0F, 3.0F, 0.0F, true);
        this.tail2Spike = new AdvancedModelBox(this);
        this.tail2Spike.setRotationPoint(0.0F, -3.0F, 9.5F);
        this.tail2.addChild(this.tail2Spike);
        this.tail2Spike.setTextureOffset(131, 83).addBox(0.0F, -4.0F, -8.5F, 0.0F, 8.0F, 17.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this);
        this.neck.setRotationPoint(0.0F, 5.0F, -16.5F);
        this.body.addChild(this.neck);
        this.neck.setTextureOffset(100, 129).addBox(-5.0F, -7.0F, -16.0F, 10.0F, 14.0F, 20.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -6.0F, -14.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(110, 72).addBox(-8.0F, -17.0F, -17.0F, 16.0F, 12.0F, 8.0F, 0.0F, false);
        this.head.setTextureOffset(79, 29).addBox(-4.0F, 10.0F, -17.0F, 8.0F, 2.0F, 4.0F, 0.0F, false);
        this.head.setTextureOffset(46, 108).addBox(-7.0F, -5.0F, -7.0F, 14.0F, 15.0F, 10.0F, 0.0F, false);
        this.head.setTextureOffset(94, 108).addBox(-11.0F, -17.0F, 0.0F, 22.0F, 18.0F, 3.0F, 0.0F, false);
        this.head.setTextureOffset(0, 96).addBox(-14.0F, -20.0F, 0.0F, 28.0F, 21.0F, 0.0F, 0.0F, false);
        this.head.setTextureOffset(79, 0).addBox(-7.0F, -9.0F, -7.0F, 3.0F, 4.0F, 3.0F, 0.0F, false);
        this.head.setTextureOffset(21, 0).addBox(4.0F, -9.0F, -7.0F, 3.0F, 4.0F, 3.0F, 0.0F, false);
        this.head.setTextureOffset(0, 129).addBox(-4.0F, -5.0F, -17.0F, 8.0F, 7.0F, 10.0F, 0.0F, false);
        this.head.setTextureOffset(36, 142).addBox(-4.0F, 2.0F, -17.0F, 8.0F, 8.0F, 4.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(0.0F, 2.0F, -6.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(65, 140).addBox(-4.0F, 0.0F, -7.0F, 8.0F, 8.0F, 6.0F, 0.0F, false);
        this.grassBunch = new AdvancedModelBox(this);
        this.grassBunch.setRotationPoint(2.0F, 0.0F, -3.5F);
        this.jaw.addChild(this.grassBunch);
        this.grass = new AdvancedModelBox(this);
        this.grass.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.grassBunch.addChild(this.grass);
        this.grass5 = new AdvancedModelBox(this);
        this.grass5.setRotationPoint(2.4246F, -0.4275F, 0.0F);
        this.grass.addChild(this.grass5);
        this.setRotateAngle(this.grass5, 0.0F, 0.0F, -0.3491F);
        this.grass5.setTextureOffset(24, 165).addBox(-2.4246F, -0.4275F, -2.5F, 5.0F, 0.0F, 5.0F, 0.0F, false);
        this.grass2 = new AdvancedModelBox(this);
        this.grass2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.grassBunch.addChild(this.grass2);
        this.grass2.setTextureOffset(1, 165).addBox(0.0F, 0.0F, -2.5F, 5.0F, 0.0F, 5.0F, 0.0F, false);
        this.grassBunch2 = new AdvancedModelBox(this);
        this.grassBunch2.setRotationPoint(-2.0F, 0.0F, -3.5F);
        this.jaw.addChild(this.grassBunch2);
        this.grass3 = new AdvancedModelBox(this);
        this.grass3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.grassBunch2.addChild(this.grass3);
        this.grass6 = new AdvancedModelBox(this);
        this.grass6.setRotationPoint(-2.4246F, -0.4275F, 0.0F);
        this.grass3.addChild(this.grass6);
        this.setRotateAngle(this.grass6, 0.0F, 0.0F, 0.3491F);
        this.grass6.setTextureOffset(24, 165).addBox(-2.5754F, -0.4275F, -2.5F, 5.0F, 0.0F, 5.0F, 0.0F, true);
        this.grass4 = new AdvancedModelBox(this);
        this.grass4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.grassBunch2.addChild(this.grass4);
        this.grass4.setTextureOffset(1, 165).addBox(-5.0F, 0.0F, -2.5F, 5.0F, 0.0F, 5.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.bodySpikes, this.tail, this.tail2, this.tailSpike, this.tail2Spike, this.neck, this.head, this.jaw, this.larm, this.rarm, this.lleg, new AdvancedModelBox[] { this.lleg2, this.lfoot, this.rleg, this.rleg2, this.rfoot, this.grassBunch, this.grassBunch2, this.grass, this.grass2, this.grass3, this.grass4, this.grass5, this.grass6 });
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(GrottoceratopsEntity.ANIMATION_SPEAK_1);
        this.animator.startKeyframe(5);
        this.animator.move(this.jaw, 0.0F, 1.0F, -1.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(GrottoceratopsEntity.ANIMATION_SPEAK_2);
        this.animator.startKeyframe(5);
        this.animator.move(this.jaw, 0.0F, 1.0F, -0.5F);
        this.animator.move(this.neck, 0.0F, 0.0F, -4.0F);
        this.animator.move(this.head, 0.0F, 2.0F, -2.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), (float) Math.toRadians(-20.0), 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(-20.0), (float) Math.toRadians(-10.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.move(this.jaw, 0.0F, 1.0F, -0.5F);
        this.animator.move(this.neck, 0.0F, 0.0F, -4.0F);
        this.animator.move(this.head, 0.0F, 2.0F, -2.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(-20.0), (float) Math.toRadians(10.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(GrottoceratopsEntity.ANIMATION_CHEW_FROM_GROUND);
        this.animator.startKeyframe(5);
        this.animator.move(this.neck, 0.0F, 0.0F, -4.0F);
        this.animator.move(this.head, 0.0F, 3.0F, -2.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.neck, 0.0F, 0.0F, -1.0F);
        this.animator.move(this.head, 0.0F, 3.0F, -2.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.neck, 0.0F, 0.0F, -4.0F);
        this.animator.move(this.head, 0.0F, 3.0F, -2.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(GrottoceratopsEntity.ANIMATION_MELEE_RAM);
        this.animator.startKeyframe(5);
        this.animator.move(this.neck, 0.0F, 0.0F, 2.0F);
        this.animator.move(this.head, 0.0F, 3.0F, -2.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(2);
        this.animator.move(this.neck, 0.0F, 0.0F, -4.0F);
        this.animator.move(this.head, 0.0F, 3.0F, -2.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(-25.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(3);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(GrottoceratopsEntity.ANIMATION_MELEE_TAIL_1);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, 0.0F, (float) Math.toRadians(-10.0), 0.0F);
        this.animator.rotate(this.tail, (float) Math.toRadians(20.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.tail2, (float) Math.toRadians(20.0), (float) Math.toRadians(20.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.tail, (float) Math.toRadians(-20.0), (float) Math.toRadians(-50.0), 0.0F);
        this.animator.rotate(this.tail2, (float) Math.toRadians(-20.0), (float) Math.toRadians(-20.0), (float) Math.toRadians(20.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(3);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(GrottoceratopsEntity.ANIMATION_MELEE_TAIL_2);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.tail, (float) Math.toRadians(20.0), (float) Math.toRadians(-20.0), 0.0F);
        this.animator.rotate(this.tail2, (float) Math.toRadians(20.0), (float) Math.toRadians(-20.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, 0.0F, (float) Math.toRadians(-10.0), 0.0F);
        this.animator.rotate(this.tail, (float) Math.toRadians(-20.0), (float) Math.toRadians(50.0), 0.0F);
        this.animator.rotate(this.tail2, (float) Math.toRadians(-20.0), (float) Math.toRadians(20.0), (float) Math.toRadians(-20.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(3);
        this.animator.resetKeyframe(5);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.5F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    private void setupAnimForAnimation(GrottoceratopsEntity entity, Animation animation, float limbSwing, float limbSwingAmount, float ageInTicks) {
        float partialTick = ageInTicks - (float) entity.f_19797_;
        boolean chewing = animation == GrottoceratopsEntity.ANIMATION_CHEW_FROM_GROUND || animation == GrottoceratopsEntity.ANIMATION_CHEW;
        if (chewing) {
            float animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 3.0F, animation, partialTick, animation == GrottoceratopsEntity.ANIMATION_CHEW_FROM_GROUND ? 15 : 0);
            float jawDown = Math.min(0.0F, ACMath.walkValue(ageInTicks, animationIntensity, 0.4F, 2.0F, 1.0F, true));
            this.head.rotateAngleX = this.head.rotateAngleX + ACMath.walkValue(ageInTicks, animationIntensity, 0.4F, 2.0F, 0.05F, false);
            this.jaw.rotationPointX = this.jaw.rotationPointX + ACMath.walkValue(ageInTicks, animationIntensity, 0.4F, 0.5F, 2.0F, true);
            this.jaw.rotationPointY -= jawDown;
            this.grassBunch.rotationPointY -= jawDown * 0.5F;
            this.grassBunch2.rotationPointY -= jawDown * 0.5F;
            this.grassBunch.rotateAngleZ = this.grassBunch.rotateAngleZ - ACMath.walkValue(ageInTicks, animationIntensity, 0.4F, 2.0F, 0.3F, true);
            this.grassBunch2.rotateAngleZ = this.grassBunch2.rotateAngleZ - ACMath.walkValue(ageInTicks, animationIntensity, 0.4F, 2.0F, 0.3F, false);
        }
    }

    public void setupAnim(GrottoceratopsEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        float walkSpeed = 0.5F;
        float walkDegree = 1.0F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float buryEggsAmount = entity.getBuryEggsProgress(partialTick);
        float stillAmount = 1.0F - limbSwingAmount;
        float danceAmount = entity.getDanceProgress(partialTick);
        float danceSpeed = 0.5F;
        boolean showGrass = entity.getAnimation() == GrottoceratopsEntity.ANIMATION_CHEW_FROM_GROUND && entity.getAnimationTick() > 15 || entity.getAnimation() == GrottoceratopsEntity.ANIMATION_CHEW;
        if (entity.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
            this.setupAnimForAnimation(entity, entity.getAnimation(), limbSwing, limbSwingAmount, ageInTicks);
        }
        float tailSwingYaw = Mth.wrapDegrees(entity.getTailSwingRot(partialTick)) / (180.0F / (float) Math.PI);
        this.body.rotateAngleY += tailSwingYaw;
        this.grassBunch.showModel = showGrass;
        this.grassBunch2.showModel = showGrass;
        if (buryEggsAmount > 0.0F) {
            limbSwing = ageInTicks;
            limbSwingAmount = buryEggsAmount * 0.5F;
            this.body.swing(0.25F, 0.4F, false, 0.0F, 0.0F, ageInTicks, buryEggsAmount);
            this.neck.swing(0.25F, 0.4F, true, -1.0F, 0.0F, ageInTicks, buryEggsAmount);
        }
        this.progressRotationPrev(this.tail, stillAmount, (float) Math.toRadians(-10.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.neck, 1.0F, 0.0F, 0.0F, 5.0F, 1.0F);
        this.walk(this.tail, 0.1F, 0.1F, false, 0.0F, 0.0F, ageInTicks, stillAmount);
        this.swing(this.tail, 0.1F, 0.2F, false, 2.0F, 0.0F, ageInTicks, stillAmount);
        this.swing(this.tail2, 0.1F, 0.1F, false, 1.0F, 0.0F, ageInTicks, stillAmount);
        this.walk(this.neck, 0.1F, 0.03F, false, 2.0F, 0.0F, ageInTicks, stillAmount);
        this.walk(this.head, 0.1F, 0.03F, true, 1.0F, 0.0F, ageInTicks, stillAmount);
        this.flap(this.body, walkSpeed, walkDegree * 0.1F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.lleg, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.rleg, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.larm, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.rarm, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.neck, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail, walkSpeed, walkDegree * 0.4F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail2, walkSpeed, walkDegree * 0.2F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.articulateLegs(entity.legSolver, partialTick);
        float bodyBob = ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed * 1.5F, 0.5F, 2.4F, true);
        this.body.rotationPointY += bodyBob;
        this.walk(this.larm, walkSpeed, walkDegree * 0.4F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.larm.rotationPointY = this.larm.rotationPointY + (Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, -1.5F, 5.0F, false)) - bodyBob);
        this.larm.rotationPointZ = this.larm.rotationPointZ + ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, -1.5F, 1.0F, false);
        this.walk(this.rarm, walkSpeed, walkDegree * 0.4F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.rarm.rotationPointY = this.rarm.rotationPointY + (Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, -1.5F, 5.0F, true)) - bodyBob);
        this.rarm.rotationPointZ = this.rarm.rotationPointZ + ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, -1.5F, 1.0F, true);
        this.walk(this.lleg, walkSpeed, walkDegree * 0.3F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.lfoot, walkSpeed, walkDegree * 0.2F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.lleg.rotationPointY = this.lleg.rotationPointY + (Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 5.0F, true)) - bodyBob);
        this.lleg.rotationPointZ = this.lleg.rotationPointZ + ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 1.0F, true);
        this.walk(this.rleg, walkSpeed, walkDegree * 0.3F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rfoot, walkSpeed, walkDegree * 0.2F, true, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.rleg.rotationPointY = this.rleg.rotationPointY + (Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 5.0F, false)) - bodyBob);
        this.rleg.rotationPointZ = this.rleg.rotationPointZ + ACMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 1.0F, false);
        if (entity.getAnimation() != GrottoceratopsEntity.ANIMATION_MELEE_TAIL_1 && entity.getAnimation() != GrottoceratopsEntity.ANIMATION_MELEE_TAIL_2) {
            float yawAmount = netHeadYaw / (180.0F / (float) Math.PI);
            float pitchAmount = headPitch / (180.0F / (float) Math.PI);
            this.neck.rotateAngleX += pitchAmount * 0.1F;
            this.head.rotateAngleX += pitchAmount * 0.2F;
            this.neck.rotateAngleY += yawAmount * 0.3F;
            this.head.rotateAngleY += yawAmount * 0.4F;
        }
        this.neck.rotationPointY = this.neck.rotationPointY + ACMath.walkValue(ageInTicks, danceAmount, danceSpeed * 1.5F, 1.0F, 1.5F, false);
        this.neck.rotationPointX = this.neck.rotationPointX + ACMath.walkValue(ageInTicks, danceAmount, danceSpeed * 1.5F, 0.0F, 3.0F, false);
        this.swing(this.body, danceSpeed, 0.1F, false, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.swing(this.tail, danceSpeed, 0.5F, false, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.swing(this.tail2, danceSpeed, 0.5F, false, 1.0F, 0.0F, ageInTicks, danceAmount);
    }

    private void articulateLegs(LegSolverQuadruped legs, float partialTick) {
        float heightBackLeft = legs.backLeft.getHeight(partialTick);
        float heightBackRight = legs.backRight.getHeight(partialTick);
        float heightFrontLeft = legs.frontLeft.getHeight(partialTick);
        float heightFrontRight = legs.frontRight.getHeight(partialTick);
        float max = Math.max(Math.max(heightBackLeft, heightBackRight), Math.max(heightFrontLeft, heightFrontRight)) * 0.8F;
        this.body.rotationPointY += max * 16.0F;
        this.rarm.rotationPointY += (heightFrontRight - max) * 16.0F;
        this.larm.rotationPointY += (heightFrontLeft - max) * 16.0F;
        this.rleg.rotationPointY += (heightBackRight - max) * 16.0F;
        this.lleg.rotationPointY += (heightBackLeft - max) * 16.0F;
    }

    public void animateSpirit(DinosaurSpiritEntity entityIn, float partialTicks) {
        this.resetToDefaultPose();
    }

    public void renderSpiritToBuffer(PoseStack poseStack, VertexConsumer ivertexbuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 1.3F, 1.0F);
        this.head.render(poseStack, ivertexbuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        poseStack.popPose();
    }
}