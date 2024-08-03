package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.GammaroachEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class GammaroachModel extends AdvancedEntityModel<GammaroachEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox bodyThing2;

    private final AdvancedModelBox bodyThing;

    private final AdvancedModelBox ltail;

    private final AdvancedModelBox rtail;

    private final AdvancedModelBox mtail;

    private final AdvancedModelBox carapace;

    private final AdvancedModelBox head;

    private final AdvancedModelBox lantennae;

    private final AdvancedModelBox antenna1Thing;

    private final AdvancedModelBox rantennae;

    private final AdvancedModelBox atenna2Thing;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox cube_r5;

    private final AdvancedModelBox rleg2;

    private final AdvancedModelBox rleg3;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox lleg2;

    private final AdvancedModelBox lleg3;

    private final ModelAnimator animator;

    public GammaroachModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 14.5F, 0.0F);
        this.body.setTextureOffset(33, 22).addBox(0.0F, -6.5F, 2.0F, 0.0F, 5.0F, 11.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(-5.0F, -2.5F, -6.0F, 10.0F, 5.0F, 19.0F, 0.0F, false);
        this.bodyThing2 = new AdvancedModelBox(this);
        this.bodyThing2.setRotationPoint(-5.0F, -2.5F, 7.5F);
        this.body.addChild(this.bodyThing2);
        this.setRotateAngle(this.bodyThing2, 0.0F, 0.0F, -0.7854F);
        this.bodyThing2.setTextureOffset(22, 36).addBox(0.0F, -4.0F, -5.5F, 0.0F, 5.0F, 11.0F, 0.0F, true);
        this.bodyThing = new AdvancedModelBox(this);
        this.bodyThing.setRotationPoint(5.0F, -2.5F, 7.5F);
        this.body.addChild(this.bodyThing);
        this.setRotateAngle(this.bodyThing, 0.0F, 0.0F, 0.7854F);
        this.bodyThing.setTextureOffset(22, 36).addBox(0.0F, -4.0F, -5.5F, 0.0F, 5.0F, 11.0F, 0.0F, false);
        this.ltail = new AdvancedModelBox(this);
        this.ltail.setRotationPoint(3.5F, 1.5F, 13.0F);
        this.body.addChild(this.ltail);
        this.ltail.setTextureOffset(39, 24).addBox(-3.5F, 0.0F, 0.0F, 8.0F, 0.0F, 4.0F, 0.0F, false);
        this.rtail = new AdvancedModelBox(this);
        this.rtail.setRotationPoint(-3.5F, 1.5F, 13.0F);
        this.body.addChild(this.rtail);
        this.rtail.setTextureOffset(39, 24).addBox(-4.5F, 0.0F, 0.0F, 8.0F, 0.0F, 4.0F, 0.0F, true);
        this.mtail = new AdvancedModelBox(this);
        this.mtail.setRotationPoint(0.0F, 0.5F, 13.0F);
        this.body.addChild(this.mtail);
        this.mtail.setTextureOffset(30, 7).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 0.0F, 9.0F, 0.0F, false);
        this.carapace = new AdvancedModelBox(this);
        this.carapace.setRotationPoint(0.0F, -2.5F, -6.0F);
        this.body.addChild(this.carapace);
        this.carapace.setTextureOffset(0, 41).addBox(0.0F, -4.0F, -2.5F, 0.0F, 5.0F, 11.0F, 0.0F, false);
        this.carapace.setTextureOffset(0, 33).addBox(-6.0F, -1.0F, -0.5F, 12.0F, 5.0F, 9.0F, 0.0F, false);
        this.carapace.setTextureOffset(6, 8).addBox(4.0F, -3.0F, -0.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.carapace.setTextureOffset(0, 6).addBox(-6.0F, -3.0F, -0.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, 2.0F, 0.5F);
        this.carapace.addChild(this.head);
        this.head.setTextureOffset(42, 38).addBox(-3.5F, -1.5F, -4.0F, 7.0F, 4.0F, 4.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(-3.0F, 0.0F, -3.5F, 6.0F, 3.0F, 3.0F, 0.0F, false);
        this.lantennae = new AdvancedModelBox(this);
        this.lantennae.setRotationPoint(2.5F, 0.75F, -3.0F);
        this.head.addChild(this.lantennae);
        this.antenna1Thing = new AdvancedModelBox(this);
        this.antenna1Thing.setRotationPoint(-0.5F, 0.0F, 0.0F);
        this.lantennae.addChild(this.antenna1Thing);
        this.setRotateAngle(this.antenna1Thing, 0.0F, 0.0F, -0.7854F);
        this.antenna1Thing.setTextureOffset(0, 24).addBox(-2.0F, 0.0F, -7.5F, 17.0F, 0.0F, 9.0F, 0.0F, false);
        this.rantennae = new AdvancedModelBox(this);
        this.rantennae.setRotationPoint(-2.5F, 0.75F, -3.0F);
        this.head.addChild(this.rantennae);
        this.atenna2Thing = new AdvancedModelBox(this);
        this.atenna2Thing.setRotationPoint(0.5F, 0.0F, 0.0F);
        this.rantennae.addChild(this.atenna2Thing);
        this.setRotateAngle(this.atenna2Thing, 0.0F, 0.0F, 0.7854F);
        this.atenna2Thing.setTextureOffset(0, 24).addBox(-15.0F, 0.0F, -7.5F, 17.0F, 0.0F, 9.0F, 0.0F, true);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-1.5F, 2.0F, -5.0F);
        this.body.addChild(this.rleg);
        this.cube_r5 = new AdvancedModelBox(this);
        this.cube_r5.setRotationPoint(0.0F, 0.5F, 0.0F);
        this.rleg.addChild(this.cube_r5);
        this.setRotateAngle(this.cube_r5, 0.0F, -0.7854F, 0.0F);
        this.cube_r5.setTextureOffset(58, 12).addBox(-11.5F, -1.0F, 0.0F, 12.0F, 8.0F, 0.0F, 0.0F, true);
        this.rleg2 = new AdvancedModelBox(this);
        this.rleg2.setRotationPoint(-1.5F, 2.0F, -1.5F);
        this.body.addChild(this.rleg2);
        this.rleg2.setTextureOffset(49, 0).addBox(-11.5F, -3.5F, 0.0F, 12.0F, 11.0F, 0.0F, 0.0F, true);
        this.rleg3 = new AdvancedModelBox(this);
        this.rleg3.setRotationPoint(-1.5F, 2.0F, 2.0F);
        this.body.addChild(this.rleg3);
        this.setRotateAngle(this.rleg3, 0.0F, 0.7854F, 0.0F);
        this.rleg3.setTextureOffset(0, 57).addBox(-20.5F, -3.5F, 0.0F, 21.0F, 11.0F, 0.0F, 0.0F, true);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(1.5F, 2.0F, -5.0F);
        this.body.addChild(this.lleg);
        this.setRotateAngle(this.lleg, 0.0F, 0.7854F, 0.0F);
        this.lleg.setTextureOffset(58, 12).addBox(-0.5F, -0.5F, 0.0F, 12.0F, 8.0F, 0.0F, 0.0F, false);
        this.lleg2 = new AdvancedModelBox(this);
        this.lleg2.setRotationPoint(1.5F, 2.0F, -1.5F);
        this.body.addChild(this.lleg2);
        this.lleg2.setTextureOffset(49, 0).addBox(-0.5F, -3.5F, 0.0F, 12.0F, 11.0F, 0.0F, 0.0F, false);
        this.lleg3 = new AdvancedModelBox(this);
        this.lleg3.setRotationPoint(1.5F, 2.0F, 2.0F);
        this.body.addChild(this.lleg3);
        this.setRotateAngle(this.lleg3, 0.0F, -0.7854F, 0.0F);
        this.lleg3.setTextureOffset(0, 57).addBox(-0.5F, -3.5F, 0.0F, 21.0F, 11.0F, 0.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.head, this.lantennae, this.rantennae, this.carapace, this.mtail, this.rtail, this.ltail, this.bodyThing2, this.bodyThing, this.antenna1Thing, this.atenna2Thing, new AdvancedModelBox[] { this.cube_r5, this.rleg, this.rleg2, this.rleg3, this.lleg, this.lleg2, this.lleg3 });
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(GammaroachEntity.ANIMATION_SPRAY);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.body, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.carapace, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.lleg, (float) Math.toRadians(-10.0), (float) Math.toRadians(-30.0), 0.0F);
        this.animator.rotate(this.rleg, (float) Math.toRadians(-10.0), (float) Math.toRadians(30.0), 0.0F);
        this.animator.rotate(this.lleg2, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.rleg2, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.lleg3, (float) Math.toRadians(-10.0), (float) Math.toRadians(40.0), (float) Math.toRadians(-20.0));
        this.animator.rotate(this.rleg3, (float) Math.toRadians(-10.0), (float) Math.toRadians(-40.0), (float) Math.toRadians(20.0));
        this.animator.move(this.lleg, 0.0F, -2.0F, 1.0F);
        this.animator.move(this.rleg, 0.0F, -2.0F, 1.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(20);
        this.animator.resetKeyframe(10);
        this.animator.endKeyframe();
        this.animator.setAnimation(GammaroachEntity.ANIMATION_RAM);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, 6.0F);
        this.animator.rotate(this.lleg, 0.0F, (float) Math.toRadians(30.0), 0.0F);
        this.animator.rotate(this.rleg, 0.0F, (float) Math.toRadians(-30.0), 0.0F);
        this.animator.rotate(this.lleg2, 0.0F, (float) Math.toRadians(40.0), 0.0F);
        this.animator.rotate(this.rleg2, 0.0F, (float) Math.toRadians(-40.0), 0.0F);
        this.animator.rotate(this.lleg3, 0.0F, (float) Math.toRadians(50.0), 0.0F);
        this.animator.rotate(this.rleg3, 0.0F, (float) Math.toRadians(-50.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.startKeyframe(3);
        this.animator.move(this.body, 0.0F, 0.0F, -9.0F);
        this.animator.move(this.carapace, 0.0F, 1.0F, -2.0F);
        this.animator.rotate(this.carapace, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.lleg, 0.0F, (float) Math.toRadians(-30.0), 0.0F);
        this.animator.rotate(this.rleg, 0.0F, (float) Math.toRadians(30.0), 0.0F);
        this.animator.rotate(this.lleg2, 0.0F, (float) Math.toRadians(-40.0), 0.0F);
        this.animator.rotate(this.rleg2, 0.0F, (float) Math.toRadians(40.0), 0.0F);
        this.animator.rotate(this.lleg3, 0.0F, (float) Math.toRadians(-20.0), 0.0F);
        this.animator.rotate(this.rleg3, 0.0F, (float) Math.toRadians(20.0), 0.0F);
        this.animator.rotate(this.rantennae, (float) Math.toRadians(-80.0), 0.0F, 0.0F);
        this.animator.rotate(this.lantennae, (float) Math.toRadians(-80.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(10);
    }

    private void setupAnimForAnimation(GammaroachEntity entity, Animation animation, float limbSwing, float limbSwingAmount, float ageInTicks) {
        float partialTick = ageInTicks - (float) entity.f_19797_;
        if (animation == GammaroachEntity.ANIMATION_SPRAY) {
            float animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 3.0F, animation, partialTick, 10, 30);
            this.swing(this.body, 1.0F, 0.2F, false, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.walk(this.body, 1.0F, 0.2F, false, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.swing(this.lleg, 1.0F, 0.2F, true, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.walk(this.lleg, 1.0F, 0.2F, true, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.swing(this.rleg, 1.0F, 0.2F, true, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.walk(this.rleg, 1.0F, 0.2F, true, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.swing(this.lleg2, 1.0F, 0.2F, true, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.walk(this.lleg2, 1.0F, 0.2F, true, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.swing(this.rleg2, 1.0F, 0.2F, true, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.walk(this.rleg2, 1.0F, 0.2F, true, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.swing(this.lleg3, 1.0F, 0.2F, true, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.walk(this.lleg3, 1.0F, 0.2F, true, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.swing(this.rleg3, 1.0F, 0.2F, true, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.walk(this.rleg3, 1.0F, 0.2F, true, 1.0F, 0.0F, ageInTicks, animationIntensity);
        }
    }

    public void setupAnim(GammaroachEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        float walkSpeed = 1.0F;
        float walkDegree = 0.9F;
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float deathAmount = ((float) entity.f_20919_ + partialTicks) / 20.0F;
        this.progressRotationPrev(this.rantennae, limbSwingAmount, (float) Math.toRadians(-80.0), 0.0F, (float) Math.toRadians(20.0), 1.0F);
        this.progressRotationPrev(this.lantennae, limbSwingAmount, (float) Math.toRadians(-80.0), 0.0F, (float) Math.toRadians(-20.0), 1.0F);
        if ((double) entity.f_20919_ > 0.0) {
            limbSwing = ageInTicks;
            limbSwingAmount = 1.0F;
        }
        this.progressPositionPrev(this.body, deathAmount * deathAmount, 0.0F, 16.0F, 0.0F, 1.0F);
        this.walk(this.head, 0.1F, 0.1F, true, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.rantennae, 0.1F, 0.15F, true, 1.0F, -0.3F, ageInTicks, 1.0F);
        this.walk(this.lantennae, 0.1F, 0.15F, true, 1.0F, -0.3F, ageInTicks, 1.0F);
        this.walk(this.mtail, 0.1F, 0.05F, true, 2.0F, -0.05F, ageInTicks, 1.0F);
        this.swing(this.rtail, 0.1F, 0.05F, true, 3.0F, -0.05F, ageInTicks, 1.0F);
        this.swing(this.ltail, 0.1F, 0.05F, false, 3.0F, -0.05F, ageInTicks, 1.0F);
        float offset = 0.0F;
        float offsetleft = 2.0F;
        float offsetUp = 0.3F;
        this.swing(this.rleg, walkSpeed, walkDegree * 1.2F, false, offset, -0.3F, limbSwing, limbSwingAmount);
        this.flap(this.rleg, walkSpeed, walkDegree * 0.3F, false, offset - 1.5F, offsetUp, limbSwing, limbSwingAmount);
        this.swing(this.lleg, walkSpeed, -walkDegree * 1.2F, false, offset + offsetleft, 0.3F, limbSwing, limbSwingAmount);
        this.flap(this.lleg, walkSpeed, walkDegree * 0.3F, false, offset + offsetleft + 1.5F, -offsetUp, limbSwing, limbSwingAmount);
        this.swing(this.rleg2, walkSpeed, -walkDegree, false, ++offset + offsetleft, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.rleg2, walkSpeed, walkDegree * 0.3F, false, offset + offsetleft - 1.5F, offsetUp, limbSwing, limbSwingAmount);
        this.swing(this.lleg2, walkSpeed, walkDegree, false, offset, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.lleg2, walkSpeed, walkDegree * 0.3F, false, offset - 1.5F, -offsetUp, limbSwing, limbSwingAmount);
        this.swing(this.rleg3, walkSpeed, walkDegree * 0.5F, false, ++offset, -0.1F, limbSwing, limbSwingAmount);
        this.flap(this.rleg3, walkSpeed, walkDegree * 0.3F, false, offset - 1.5F, offsetUp, limbSwing, limbSwingAmount);
        this.swing(this.lleg3, walkSpeed, -walkDegree * 0.5F, false, offset + offsetleft, 0.1F, limbSwing, limbSwingAmount);
        this.flap(this.lleg3, walkSpeed, walkDegree * 0.3F, false, offset + offsetleft - 1.5F, -offsetUp, limbSwing, limbSwingAmount);
        this.swing(this.body, walkSpeed, walkDegree * 0.2F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree * -3.0F, true, limbSwing, limbSwingAmount);
        if (entity.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
            this.setupAnimForAnimation(entity, entity.getAnimation(), limbSwing, limbSwingAmount, ageInTicks);
        }
    }
}