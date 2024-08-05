package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.LuxtructosaurusLegSolver;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public abstract class SauropodBaseModel<T extends SauropodBaseEntity> extends AdvancedEntityModel<T> {

    protected final AdvancedModelBox root;

    protected final AdvancedModelBox body;

    protected final AdvancedModelBox hips;

    protected final AdvancedModelBox tail;

    protected final AdvancedModelBox tail2;

    protected final AdvancedModelBox tail3;

    protected final AdvancedModelBox left_Leg;

    protected final AdvancedModelBox left_Foot;

    protected final AdvancedModelBox right_Leg;

    protected final AdvancedModelBox right_Foot;

    protected final AdvancedModelBox chest;

    protected final AdvancedModelBox right_Arm;

    protected final AdvancedModelBox right_Hand;

    protected final AdvancedModelBox left_Arm;

    protected final AdvancedModelBox left_Hand;

    protected final AdvancedModelBox neck;

    protected final AdvancedModelBox neck2;

    protected final AdvancedModelBox head;

    protected final AdvancedModelBox jaw;

    protected final AdvancedModelBox dewlap;

    protected final ModelAnimator animator;

    public boolean straighten = false;

    public SauropodBaseModel() {
        this.texWidth = 512;
        this.texHeight = 512;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.root.addChild(this.body);
        this.hips = new AdvancedModelBox(this);
        this.hips.setRotationPoint(0.0F, -65.0F, 0.5F);
        this.body.addChild(this.hips);
        this.hips.setTextureOffset(230, 149).addBox(-19.0F, -24.0F, -3.5F, 38.0F, 48.0F, 41.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, 6.5F, 33.0F);
        this.hips.addChild(this.tail);
        this.tail.setTextureOffset(0, 246).addBox(-12.0F, -14.5F, 2.5F, 24.0F, 29.0F, 49.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this);
        this.tail2.setRotationPoint(0.0F, 1.5F, 49.0F);
        this.tail.addChild(this.tail2);
        this.tail2.setTextureOffset(245, 238).addBox(-8.0F, -10.0F, -6.5F, 16.0F, 20.0F, 57.0F, 0.0F, false);
        this.tail3 = new AdvancedModelBox(this);
        this.tail3.setRotationPoint(0.0F, 0.5F, 48.5F);
        this.tail2.addChild(this.tail3);
        this.tail3.setTextureOffset(138, 174).addBox(-5.0F, -6.5F, -14.0F, 10.0F, 13.0F, 72.0F, 0.0F, false);
        this.left_Leg = new AdvancedModelBox(this);
        this.left_Leg.setRotationPoint(18.0F, 12.0F, 22.5F);
        this.hips.addChild(this.left_Leg);
        this.left_Leg.setTextureOffset(139, 0).addBox(-9.5F, -7.0F, -15.0F, 19.0F, 35.0F, 27.0F, 0.0F, false);
        this.left_Foot = new AdvancedModelBox(this);
        this.left_Foot.setRotationPoint(0.0F, 23.0F, 5.0F);
        this.left_Leg.addChild(this.left_Foot);
        this.left_Foot.setTextureOffset(270, 315).addBox(-6.5F, -5.0F, -5.0F, 13.0F, 35.0F, 17.0F, 0.0F, false);
        this.left_Foot.setTextureOffset(153, 149).addBox(-6.5F, 26.0F, -9.0F, 13.0F, 4.0F, 4.0F, 0.25F, false);
        this.left_Foot.setTextureOffset(153, 157).addBox(-6.5F, 26.0F, -9.0F, 13.0F, 4.0F, 4.0F, 0.0F, false);
        this.right_Leg = new AdvancedModelBox(this);
        this.right_Leg.setRotationPoint(-18.0F, 12.0F, 22.5F);
        this.hips.addChild(this.right_Leg);
        this.right_Leg.setTextureOffset(139, 0).addBox(-9.5F, -7.0F, -15.0F, 19.0F, 35.0F, 27.0F, 0.0F, true);
        this.right_Foot = new AdvancedModelBox(this);
        this.right_Foot.setRotationPoint(0.0F, 23.0F, 5.0F);
        this.right_Leg.addChild(this.right_Foot);
        this.right_Foot.setTextureOffset(270, 315).addBox(-6.5F, -5.0F, -5.0F, 13.0F, 35.0F, 17.0F, 0.0F, true);
        this.right_Foot.setTextureOffset(153, 149).addBox(-6.5F, 26.0F, -9.0F, 13.0F, 4.0F, 4.0F, 0.25F, true);
        this.right_Foot.setTextureOffset(153, 157).addBox(-6.5F, 26.0F, -9.0F, 13.0F, 4.0F, 4.0F, 0.0F, true);
        this.chest = new AdvancedModelBox(this);
        this.chest.setRotationPoint(0.0F, -9.0F, 0.0F);
        this.hips.addChild(this.chest);
        this.chest.setTextureOffset(0, 123).addBox(-24.0F, -33.0F, -56.5F, 48.0F, 66.0F, 57.0F, 0.01F, false);
        this.right_Arm = new AdvancedModelBox(this);
        this.right_Arm.setRotationPoint(-23.0F, -3.0F, -37.5F);
        this.chest.addChild(this.right_Arm);
        this.right_Arm.setTextureOffset(0, 0).addBox(-13.0F, -10.0F, -11.0F, 14.0F, 44.0F, 21.0F, 0.0F, true);
        this.right_Hand = new AdvancedModelBox(this);
        this.right_Hand.setRotationPoint(-3.0F, 32.0F, -9.0F);
        this.right_Arm.addChild(this.right_Hand);
        this.right_Hand.setTextureOffset(264, 0).addBox(-15.0F, -2.0F, -2.75F, 24.0F, 47.0F, 29.0F, 0.0F, true);
        this.right_Hand.setTextureOffset(20, 238).addBox(-8.0F, -2.0F, -12.75F, 0.0F, 47.0F, 10.0F, 0.0F, true);
        this.right_Hand.setTextureOffset(20, 238).addBox(2.0F, -2.0F, -12.75F, 0.0F, 47.0F, 10.0F, 0.0F, true);
        this.right_Hand.setTextureOffset(49, 0).addBox(9.0F, 37.0F, 13.25F, 8.0F, 8.0F, 8.0F, 0.0F, true);
        this.left_Arm = new AdvancedModelBox(this);
        this.left_Arm.setRotationPoint(23.0F, -3.0F, -37.5F);
        this.chest.addChild(this.left_Arm);
        this.left_Arm.setTextureOffset(0, 0).addBox(-1.0F, -10.0F, -11.0F, 14.0F, 44.0F, 21.0F, 0.0F, false);
        this.left_Hand = new AdvancedModelBox(this);
        this.left_Hand.setRotationPoint(3.0F, 32.0F, -9.0F);
        this.left_Arm.addChild(this.left_Hand);
        this.left_Hand.setTextureOffset(264, 0).addBox(-9.0F, -2.0F, -2.75F, 24.0F, 47.0F, 29.0F, 0.0F, false);
        this.left_Hand.setTextureOffset(20, 238).addBox(8.0F, -2.0F, -12.75F, 0.0F, 47.0F, 10.0F, 0.0F, false);
        this.left_Hand.setTextureOffset(20, 238).addBox(-2.0F, -2.0F, -12.75F, 0.0F, 47.0F, 10.0F, 0.0F, false);
        this.left_Hand.setTextureOffset(49, 0).addBox(-17.0F, 37.0F, 13.25F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this);
        this.neck.setRotationPoint(0.5F, -31.0F, -33.0F);
        this.chest.addChild(this.neck);
        this.neck.setTextureOffset(0, 0).addBox(-13.5F, -14.0F, -76.5F, 26.0F, 36.0F, 87.0F, 0.0F, false);
        this.neck2 = new AdvancedModelBox(this);
        this.neck2.setRotationPoint(-0.5F, -6.0F, -75.5F);
        this.neck.addChild(this.neck2);
        this.neck2.setTextureOffset(153, 44).addBox(-8.0F, -2.0F, -80.0F, 16.0F, 26.0F, 79.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.8F, 8.0F, -75.0F);
        this.neck2.addChild(this.head);
        this.head.setTextureOffset(198, 315).addBox(-9.8F, -4.0F, -15.0F, 18.0F, 22.0F, 18.0F, 0.0F, false);
        this.head.setTextureOffset(0, 324).addBox(-5.8F, -13.0F, -23.0F, 10.0F, 20.0F, 20.0F, 0.0F, false);
        this.head.setTextureOffset(264, 76).addBox(-11.8F, 6.0F, -28.0F, 22.0F, 7.0F, 21.0F, 0.0F, false);
        this.head.setTextureOffset(0, 65).addBox(-11.3F, 11.0F, -27.5F, 21.0F, 6.0F, 13.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(-0.8F, 10.5F, -8.5F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(331, 83).addBox(-11.0F, -1.5F, -19.5F, 22.0F, 9.0F, 21.0F, -0.01F, false);
        this.jaw.setTextureOffset(360, 0).addBox(-11.0F, 3.0F, -19.5F, 22.0F, 2.0F, 17.0F, -0.001F, false);
        this.dewlap = new AdvancedModelBox(this);
        this.dewlap.setRotationPoint(0.0F, 24.0F, -57.5F);
        this.neck2.addChild(this.dewlap);
        this.dewlap.setTextureOffset(97, 194).addBox(0.0F, -4.0F, -32.5F, 0.0F, 26.0F, 65.0F, 0.0F, false);
        this.left_Foot.setShouldScaleChildren(true);
        this.right_Foot.setShouldScaleChildren(true);
        this.left_Hand.setShouldScaleChildren(true);
        this.right_Hand.setShouldScaleChildren(true);
        this.jaw.setScale(0.99F, 0.99F, 0.99F);
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(SauropodBaseEntity.ANIMATION_SPEAK);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(7);
        this.animator.rotate(this.head, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(40.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(SauropodBaseEntity.ANIMATION_ROAR);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, (float) Math.toRadians(-50.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(40.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(40);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(SauropodBaseEntity.ANIMATION_EPIC_DEATH);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, (float) Math.toRadians(-50.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck2, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(40.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(100);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(SauropodBaseEntity.ANIMATION_SUMMON);
        this.animator.startKeyframe(0);
        this.animator.move(this.body, 0.0F, 200.0F, 0.0F);
        this.animator.move(this.neck2, 0.0F, -5.0F, -5.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(-50.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck2, (float) Math.toRadians(120.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(50.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(120);
        this.animator.setAnimation(SauropodBaseEntity.ANIMATION_STOMP);
        this.animator.startKeyframe(20);
        this.animator.move(this.body, 0.0F, -25.0F, -23.0F);
        this.animator.move(this.chest, 0.0F, 2.0F, 4.0F);
        this.animator.move(this.head, 0.0F, 5.0F, -5.0F);
        this.animator.move(this.left_Leg, 0.0F, 0.0F, -5.0F);
        this.animator.move(this.right_Leg, 0.0F, 0.0F, -5.0F);
        this.animator.move(this.left_Arm, 0.0F, 10.0F, -5.0F);
        this.animator.move(this.right_Arm, 0.0F, 10.0F, -5.0F);
        this.animator.rotate(this.body, (float) Math.toRadians(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck2, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail3, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_Leg, (float) Math.toRadians(40.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_Leg, (float) Math.toRadians(40.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_Arm, (float) Math.toRadians(-20.0), (float) Math.toRadians(-20.0), (float) Math.toRadians(-20.0));
        this.animator.rotate(this.right_Arm, (float) Math.toRadians(-20.0), (float) Math.toRadians(20.0), (float) Math.toRadians(20.0));
        this.animator.rotate(this.left_Hand, (float) Math.toRadians(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_Hand, (float) Math.toRadians(50.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 10.0F, 0.0F);
        this.animator.move(this.right_Leg, 0.0F, -10.0F, 0.0F);
        this.animator.move(this.left_Leg, 0.0F, -10.0F, 0.0F);
        this.animator.move(this.right_Arm, -2.0F, -10.0F, -7.0F);
        this.animator.move(this.left_Arm, 2.0F, -10.0F, -7.0F);
        this.animator.rotate(this.left_Arm, 0.0F, 0.0F, (float) Math.toRadians(-20.0));
        this.animator.rotate(this.right_Arm, 0.0F, 0.0F, (float) Math.toRadians(20.0));
        this.animator.rotate(this.left_Hand, 0.0F, 0.0F, (float) Math.toRadians(20.0));
        this.animator.rotate(this.right_Hand, 0.0F, 0.0F, (float) Math.toRadians(-20.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(10);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(SauropodBaseEntity.ANIMATION_SPEW_FLAMES);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(60.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(60);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(SauropodBaseEntity.ANIMATION_JUMP);
        this.animator.startKeyframe(10);
        this.animator.move(this.body, 0.0F, 10.0F, 0.0F);
        this.animator.move(this.right_Leg, 0.0F, -9.0F, 0.0F);
        this.animator.move(this.left_Leg, 0.0F, -9.0F, 0.0F);
        this.animator.move(this.right_Arm, -2.0F, -6.0F, -4.0F);
        this.animator.move(this.left_Arm, 2.0F, -6.0F, -4.0F);
        this.animator.rotate(this.left_Arm, 0.0F, 0.0F, (float) Math.toRadians(-20.0));
        this.animator.rotate(this.right_Arm, 0.0F, 0.0F, (float) Math.toRadians(20.0));
        this.animator.rotate(this.left_Hand, 0.0F, 0.0F, (float) Math.toRadians(20.0));
        this.animator.rotate(this.right_Hand, 0.0F, 0.0F, (float) Math.toRadians(-20.0));
        this.animator.rotate(this.left_Leg, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.right_Leg, 0.0F, 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.left_Foot, 0.0F, 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.right_Foot, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.neck, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck2, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(10);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, -10.0F, 0.0F);
        this.animator.rotate(this.left_Arm, (float) Math.toRadians(-30.0), 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.right_Arm, (float) Math.toRadians(-30.0), 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.right_Leg, (float) Math.toRadians(30.0), 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.left_Leg, (float) Math.toRadians(30.0), 0.0F, (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 10.0F, 0.0F);
        this.animator.move(this.right_Leg, 0.0F, -9.0F, 0.0F);
        this.animator.move(this.left_Leg, 0.0F, -9.0F, 0.0F);
        this.animator.move(this.right_Arm, -2.0F, -6.0F, -4.0F);
        this.animator.move(this.left_Arm, 2.0F, -6.0F, -4.0F);
        this.animator.rotate(this.left_Arm, 0.0F, 0.0F, (float) Math.toRadians(-20.0));
        this.animator.rotate(this.right_Arm, 0.0F, 0.0F, (float) Math.toRadians(20.0));
        this.animator.rotate(this.left_Hand, 0.0F, 0.0F, (float) Math.toRadians(20.0));
        this.animator.rotate(this.right_Hand, 0.0F, 0.0F, (float) Math.toRadians(-20.0));
        this.animator.rotate(this.left_Leg, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.right_Leg, 0.0F, 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.left_Foot, 0.0F, 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.right_Foot, 0.0F, 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.neck, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck2, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(SauropodBaseEntity.ANIMATION_LEFT_KICK);
        this.animator.startKeyframe(4);
        this.animator.move(this.left_Arm, 3.0F, 3.0F, -3.0F);
        this.animator.rotate(this.left_Arm, (float) Math.toRadians(-30.0), (float) Math.toRadians(-40.0), 0.0F);
        this.animator.rotate(this.left_Hand, (float) Math.toRadians(40.0), 0.0F, (float) Math.toRadians(10.0));
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.tail, 0.0F, (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.neck, 0.0F, (float) Math.toRadians(-5.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(6);
        this.animator.move(this.left_Arm, 0.0F, -5.0F, -3.0F);
        this.animator.rotate(this.left_Arm, (float) Math.toRadians(-80.0), (float) Math.toRadians(0.0), 0.0F);
        this.animator.rotate(this.left_Hand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(SauropodBaseEntity.ANIMATION_RIGHT_KICK);
        this.animator.startKeyframe(5);
        this.animator.move(this.right_Arm, -3.0F, 3.0F, -3.0F);
        this.animator.rotate(this.right_Arm, (float) Math.toRadians(-30.0), (float) Math.toRadians(40.0), 0.0F);
        this.animator.rotate(this.right_Hand, (float) Math.toRadians(40.0), 0.0F, (float) Math.toRadians(-10.0));
        this.animator.rotate(this.body, 0.0F, (float) Math.toRadians(-10.0), 0.0F);
        this.animator.rotate(this.tail, 0.0F, (float) Math.toRadians(-10.0), 0.0F);
        this.animator.rotate(this.neck, 0.0F, (float) Math.toRadians(5.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.move(this.right_Arm, 0.0F, -5.0F, -3.0F);
        this.animator.rotate(this.right_Arm, (float) Math.toRadians(-80.0), (float) Math.toRadians(0.0), 0.0F);
        this.animator.rotate(this.right_Hand, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(6);
        this.animator.setAnimation(SauropodBaseEntity.ANIMATION_EAT_LEAVES);
        this.animator.startKeyframe(15);
        this.animator.rotate(this.neck, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck2, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(40.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(10);
        this.animator.rotate(this.head, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.move(this.jaw, 0.0F, 0.0F, 1.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
    }

    public void setupAnim(SauropodBaseEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        if (entity.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
            this.setupAnimForAnimation(entity, entity.getAnimation(), limbSwing, limbSwingAmount, ageInTicks);
        }
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float idleSpeed = 0.05F;
        float walkSpeed = 0.05F;
        float walkDegree = 3.0F;
        float walk = entity.getWalkAnimPosition(partialTicks);
        float walkAmount = Math.min(entity.getWalkAnimSpeed(partialTicks), 1.0F);
        float raiseArmsAmount = entity.getRaiseArmsAmount(partialTicks);
        float legBack = entity.getLegBackAmount(partialTicks);
        float danceAmount = entity.getDanceProgress(partialTicks);
        float buryEggsAmount = entity.getBuryEggsProgress(partialTicks);
        this.positionNeckAndTail(entity, netHeadYaw, headPitch, partialTicks);
        this.articulateLegs(entity.legSolver, raiseArmsAmount, partialTicks);
        if (buryEggsAmount > 0.0F) {
            limbSwingAmount = buryEggsAmount * 0.5F;
        }
        this.walk(this.neck, idleSpeed, 0.03F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.neck2, idleSpeed, 0.02F, true, -1.0F, -0.03F, ageInTicks, 1.0F);
        this.walk(this.head, idleSpeed, 0.01F, true, -2.0F, 0.02F, ageInTicks, 1.0F);
        this.flap(this.dewlap, 0.14F, 0.1F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.dewlap, 0.14F, 0.05F, true, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.tail, idleSpeed, 0.03F, true, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.tail2, idleSpeed, 0.03F, true, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.tail3, idleSpeed, 0.03F, true, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail, idleSpeed, 0.03F, true, 4.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail2, idleSpeed, 0.03F, true, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail3, idleSpeed, 0.03F, true, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.dewlap.rotationPointY = this.dewlap.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F, -1.5F, 1.0F, false);
        float legAnimSeperation = 0.5F;
        this.animateLegWalking(this.right_Arm, this.right_Hand, legAnimSeperation * 3.0F, walkSpeed, walkDegree, walk, walkAmount, true, false, legBack);
        this.animateLegWalking(this.right_Leg, this.right_Foot, legAnimSeperation * 2.0F, walkSpeed, walkDegree, walk, walkAmount, false, false, legBack);
        this.animateLegWalking(this.left_Arm, this.left_Hand, legAnimSeperation, walkSpeed, walkDegree, walk, walkAmount, true, true, legBack);
        this.animateLegWalking(this.left_Leg, this.left_Foot, 0.0F, walkSpeed, walkDegree, walk, walkAmount, false, true, legBack);
        this.animateDancing(entity, danceAmount, ageInTicks);
    }

    private void setupAnimForAnimation(SauropodBaseEntity entity, Animation animation, float limbSwing, float limbSwingAmount, float ageInTicks) {
        float partialTick = ageInTicks - (float) entity.f_19797_;
        if (entity.getAnimation() == SauropodBaseEntity.ANIMATION_ROAR) {
            float animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 1.0F, animation, partialTick, 5, 50);
            this.head.swing(1.0F, 0.1F, false, -1.0F, 0.0F, ageInTicks, animationIntensity);
            this.jaw.walk(2.0F, 0.1F, false, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.dewlap.flap(2.0F, 0.1F, false, 2.0F, 0.0F, ageInTicks, animationIntensity);
            this.neck.flap(0.5F, 0.1F, false, -3.0F, 0.0F, ageInTicks, animationIntensity);
            this.neck2.flap(0.5F, 0.1F, false, -2.0F, 0.0F, ageInTicks, animationIntensity);
        }
        if (entity.getAnimation() == SauropodBaseEntity.ANIMATION_EPIC_DEATH) {
            float animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 1.0F, animation, partialTick, 5, 110);
            this.head.swing(0.4F, 0.1F, false, -1.0F, 0.0F, ageInTicks, animationIntensity);
            this.jaw.walk(1.0F, 0.1F, false, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.dewlap.flap(1.0F, 0.1F, false, 2.0F, 0.0F, ageInTicks, animationIntensity);
            this.neck.swing(0.1F, 0.2F, false, -1.0F, 0.0F, ageInTicks, animationIntensity);
            this.neck.flap(0.25F, 0.1F, false, -3.0F, 0.0F, ageInTicks, animationIntensity);
            this.neck2.swing(0.1F, 0.2F, false, -1.0F, 0.0F, ageInTicks, animationIntensity);
            this.neck2.flap(0.25F, 0.1F, false, -2.0F, 0.0F, ageInTicks, animationIntensity);
        }
        if (entity.getAnimation() == SauropodBaseEntity.ANIMATION_SPEW_FLAMES) {
            float animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 1.0F, animation, partialTick, 5, 70);
            this.head.walk(2.0F, 0.05F, false, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.head.swing(2.0F, 0.05F, false, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.dewlap.flap(2.0F, 0.1F, false, 2.0F, 0.0F, ageInTicks, animationIntensity);
            this.neck.flap(1.0F, 0.05F, false, -3.0F, 0.0F, ageInTicks, animationIntensity);
            this.neck2.flap(1.0F, 0.05F, false, -2.0F, 0.0F, ageInTicks, animationIntensity);
        }
        if (entity.getAnimation() == SauropodBaseEntity.ANIMATION_EAT_LEAVES) {
            float animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 3.0F, animation, partialTick, 35);
            float jawDown = Math.min(0.0F, ACMath.walkValue(ageInTicks, animationIntensity, 0.4F, 2.0F, 1.0F, true));
            this.jaw.walk(0.5F, 0.1F, false, 1.0F, 0.1F, ageInTicks, animationIntensity);
            this.head.rotateAngleX = this.head.rotateAngleX + ACMath.walkValue(ageInTicks, animationIntensity, 0.4F, 2.0F, 0.05F, false);
            this.jaw.rotationPointZ = this.jaw.rotationPointZ + animationIntensity * 2.0F + ACMath.walkValue(ageInTicks, animationIntensity, 0.4F, 0.5F, 1.0F, false);
        }
    }

    private void positionNeckAndTail(SauropodBaseEntity entity, float netHeadYaw, float headPitch, float partialTicks) {
        if (!this.straighten && !entity.isFakeEntity()) {
            float neckPart1Pitch = (float) Math.toRadians((double) entity.neckPart1.calculateAnimationAngle(partialTicks, true)) * 0.5F;
            float neckPart2Pitch = (float) Math.toRadians((double) entity.neckPart2.calculateAnimationAngle(partialTicks, true)) * 0.5F;
            float neckPart3Pitch = (float) Math.toRadians((double) entity.neckPart3.calculateAnimationAngle(partialTicks, true)) * 0.5F;
            float tailPart1Pitch = (float) Math.toRadians((double) entity.tailPart1.calculateAnimationAngle(partialTicks, true)) + 0.141F;
            float tailPart2Pitch = (float) Math.toRadians((double) entity.tailPart2.calculateAnimationAngle(partialTicks, true)) + 0.076F;
            float tailPart3Pitch = (float) Math.toRadians((double) entity.tailPart3.calculateAnimationAngle(partialTicks, true)) * 0.5F;
            float neckPart2Yaw = entity.neckPart2.calculateAnimationAngle(partialTicks, false);
            float pitchAmount = entity.getAnimation() == SauropodBaseEntity.ANIMATION_SPEW_FLAMES ? 0.0F : Mth.clamp(headPitch, -30.0F, 30.0F) / (180.0F / (float) Math.PI);
            float headApproach = Mth.approachDegrees(neckPart2Yaw, entity.headPart.calculateAnimationAngle(partialTicks, false), 45.0F) - neckPart2Yaw;
            this.neck.rotateAngleX -= neckPart1Pitch + neckPart2Pitch;
            this.neck.rotateAngleY = (float) ((double) this.neck.rotateAngleY + (Math.toRadians((double) (180.0F + entity.neckPart1.calculateAnimationAngle(partialTicks, false))) - (double) this.chest.rotateAngleY - (double) this.body.rotateAngleY - (double) this.root.rotateAngleY));
            this.neck2.rotateAngleX -= neckPart2Pitch;
            this.neck2.rotateAngleY = (float) ((double) this.neck2.rotateAngleY + Math.toRadians((double) (180.0F + neckPart2Yaw)));
            this.head.rotateAngleX = this.head.rotateAngleX + (pitchAmount + neckPart1Pitch + neckPart2Pitch + neckPart3Pitch - (float) Math.toRadians((double) entity.headPart.calculateAnimationAngle(partialTicks, true)) * 0.2F);
            this.head.rotateAngleY = (float) ((double) this.head.rotateAngleY + Math.toRadians((double) headApproach));
            if (neckPart2Pitch > 0.0F) {
                this.neck2.rotationPointZ = this.neck2.rotationPointZ + Math.min(neckPart2Pitch * 50.0F, 50.0F);
            }
            this.tail.rotateAngleY = (float) ((double) this.tail.rotateAngleY + Math.toRadians((double) entity.tailPart1.calculateAnimationAngle(partialTicks, false)));
            this.tail2.rotateAngleY = (float) ((double) this.tail2.rotateAngleY + Math.toRadians((double) entity.tailPart2.calculateAnimationAngle(partialTicks, false)));
            this.tail3.rotateAngleY = (float) ((double) this.tail3.rotateAngleY + Math.toRadians((double) (entity.tailPart3.calculateAnimationAngle(partialTicks, false) - entity.tailPart2.calculateAnimationAngle(partialTicks, false))));
            this.tail.rotateAngleX += tailPart1Pitch;
            this.tail2.rotateAngleX += tailPart2Pitch;
            this.tail3.rotateAngleX += tailPart3Pitch;
        }
    }

    private void animateLegWalking(AdvancedModelBox leg, AdvancedModelBox foot, float offset, float speed, float degree, float limbSwing, float limbSwingAmount, boolean front, boolean left, float legBack) {
        float leg1 = Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmount, speed, (float) Math.PI * (offset + 0.3333F), 1.0F, true) + 0.75F) * 4.0F;
        float leg1Delayed = Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmount, speed, (float) Math.PI * offset, 1.0F, true) + 0.75F) * 4.0F;
        float leg1Prev = Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmount, speed, (float) Math.PI * (offset + 0.6666F), 1.0F, true) + 0.75F) * 4.0F;
        float leg1Squish = 1.0F - 0.15F * (float) Math.pow((double) Math.min(leg1Delayed - leg1, 0.0F), 3.0);
        float legInactivityAmount = 1.0F - Math.abs(leg1);
        this.walk(leg, speed, degree * 0.3F, false, (float) Math.PI * offset + 1.0F, 0.0F, limbSwing, leg1);
        this.walk(foot, speed, degree * 0.2F, false, (float) Math.PI * offset - 2.0F, -0.25F, limbSwing, leg1);
        if (front) {
            this.swing(leg, speed, degree * -0.2F, left, (float) Math.PI * offset + 1.0F, 0.0F, limbSwing, leg1Prev);
            leg.rotationPointZ += leg1Prev * 8.0F;
        }
        leg.rotationPointY += leg1 * 10.0F;
        leg.rotationPointZ += leg1Delayed * 16.0F;
        leg.rotationPointZ += legBack * legInactivityAmount * 16.0F;
        float raisedBody = leg1 * 8.0F;
        this.body.rotationPointY += raisedBody;
        this.left_Leg.rotationPointY -= raisedBody;
        this.right_Leg.rotationPointY -= raisedBody;
        this.left_Arm.rotationPointY -= raisedBody;
        this.right_Arm.rotationPointY -= raisedBody;
        this.tail.rotationPointY -= raisedBody * 0.5F;
        this.neck.rotationPointY -= raisedBody * 0.5F;
        float squish2 = 2.0F - leg1Squish;
        foot.setScale(leg1Squish * leg1Squish, squish2, leg1Squish);
        leg.rotationPointY -= (squish2 - 1.0F) * 30.0F;
    }

    private float articulateLegs(LuxtructosaurusLegSolver legs, float raiseArmsAmount, float partialTick) {
        float armsArticulateAmount = 1.0F - raiseArmsAmount;
        float heightBackLeft = legs.backLeft.getHeight(partialTick);
        float heightBackRight = legs.backRight.getHeight(partialTick);
        float heightFrontLeft = legs.frontLeft.getHeight(partialTick);
        float heightFrontRight = legs.frontRight.getHeight(partialTick);
        float max = Math.max(Math.max(heightBackLeft, heightBackRight), armsArticulateAmount * Math.max(heightFrontLeft, heightFrontRight)) * 0.75F;
        this.body.rotationPointY += max * 16.0F;
        this.right_Arm.rotationPointY += (heightFrontRight - max) * armsArticulateAmount * 16.0F;
        this.left_Arm.rotationPointY += (heightFrontLeft - max) * armsArticulateAmount * 16.0F;
        this.right_Leg.rotationPointY += (heightBackRight - max) * 16.0F;
        this.left_Leg.rotationPointY += (heightBackLeft - max) * 16.0F;
        return max * 16.0F;
    }

    private void animateDancing(SauropodBaseEntity entity, float danceAmount, float ageInTicks) {
        float ageSine = Mth.clamp((float) Math.sin((double) (ageInTicks * 0.08F)) * 2.0F, 0.0F, 1.0F);
        float gangnam1 = danceAmount * ageSine;
        float gangnam2 = danceAmount * (1.0F - ageSine);
        float gangnamSpeed = 0.65F;
        this.progressPositionPrev(this.body, danceAmount, 0.0F, -37.0F, -23.0F, 1.0F);
        this.progressRotationPrev(this.body, danceAmount, (float) Math.toRadians(-50.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.left_Leg, danceAmount, (float) Math.toRadians(50.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.right_Leg, danceAmount, (float) Math.toRadians(50.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail, danceAmount, (float) Math.toRadians(50.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.neck, danceAmount, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.neck2, danceAmount, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.left_Arm, gangnam1, 2.0F, 0.0F, -5.0F, 1.0F);
        this.progressPositionPrev(this.right_Arm, gangnam1, -2.0F, 0.0F, -5.0F, 1.0F);
        this.progressRotationPrev(this.left_Arm, gangnam1, (float) Math.toRadians(-5.0), 0.0F, (float) Math.toRadians(10.0), 1.0F);
        this.progressRotationPrev(this.right_Arm, gangnam1, (float) Math.toRadians(-20.0), 0.0F, (float) Math.toRadians(-10.0), 1.0F);
        this.progressRotationPrev(this.left_Hand, gangnam1, (float) Math.toRadians(10.0), 0.0F, (float) Math.toRadians(30.0), 1.0F);
        this.progressRotationPrev(this.right_Hand, gangnam1, (float) Math.toRadians(-20.0), 0.0F, (float) Math.toRadians(-30.0), 1.0F);
        this.body.swing(gangnamSpeed, 0.05F, false, 0.0F, 0.0F, ageInTicks, danceAmount);
        this.tail.flap(gangnamSpeed, 0.1F, true, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.tail2.swing(gangnamSpeed, 0.1F, true, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.tail3.swing(gangnamSpeed, 0.1F, true, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.left_Arm.walk(gangnamSpeed, 0.2F, false, 2.0F, -0.3F, ageInTicks, danceAmount);
        this.right_Arm.walk(gangnamSpeed, 0.2F, false, 2.0F, -0.3F, ageInTicks, gangnam1);
        this.left_Hand.walk(gangnamSpeed, 0.1F, false, 1.0F, -0.1F, ageInTicks, danceAmount);
        this.right_Hand.walk(gangnamSpeed, 0.1F, false, 1.0F, -0.1F, ageInTicks, gangnam1);
        this.left_Leg.walk(gangnamSpeed, 0.3F, false, 1.0F, -0.1F, ageInTicks, danceAmount);
        this.right_Leg.walk(gangnamSpeed, 0.3F, true, 1.0F, -0.1F, ageInTicks, danceAmount);
        this.body.bob(gangnamSpeed, 10.0F, false, ageInTicks, danceAmount);
        this.progressPositionPrev(this.left_Arm, gangnam2, 2.0F, 20.0F, -5.0F, 1.0F);
        this.progressPositionPrev(this.left_Hand, gangnam2, 0.0F, -4.0F, -4.0F, 1.0F);
        this.progressPositionPrev(this.right_Arm, gangnam2, 2.0F, 0.0F, -10.0F, 1.0F);
        this.progressPositionPrev(this.right_Hand, gangnam2, 3.0F, -3.0F, 3.0F, 1.0F);
        this.progressRotationPrev(this.left_Arm, gangnam2, (float) Math.toRadians(-10.0), 0.0F, (float) Math.toRadians(-30.0), 1.0F);
        this.progressRotationPrev(this.left_Hand, gangnam2, (float) Math.toRadians(-10.0), 0.0F, (float) Math.toRadians(90.0), 1.0F);
        this.progressRotationPrev(this.right_Arm, gangnam2, (float) Math.toRadians(-80.0), (float) Math.toRadians(40.0), (float) Math.toRadians(-20.0), 1.0F);
        this.progressRotationPrev(this.right_Hand, gangnam2, (float) Math.toRadians(-40.0), (float) Math.toRadians(-40.0), (float) Math.toRadians(20.0), 1.0F);
        this.right_Arm.flap(gangnamSpeed, 0.5F, false, 1.0F, 0.0F, ageInTicks, gangnam2);
        this.right_Arm.swing(gangnamSpeed, 0.5F, false, 0.0F, 0.0F, ageInTicks, gangnam2);
        this.right_Hand.flap(gangnamSpeed, 0.2F, false, 3.0F, -0.1F, ageInTicks, gangnam2);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public Vec3 getMouthPosition(Vec3 offsetIn) {
        PoseStack translationStack = new PoseStack();
        translationStack.pushPose();
        this.root.translateAndRotate(translationStack);
        this.body.translateAndRotate(translationStack);
        this.chest.translateAndRotate(translationStack);
        this.neck.translateAndRotate(translationStack);
        this.neck2.translateAndRotate(translationStack);
        this.head.translateAndRotate(translationStack);
        this.jaw.translateAndRotate(translationStack);
        Vector4f armOffsetVec = new Vector4f((float) offsetIn.x, (float) offsetIn.y, (float) offsetIn.z, 1.0F);
        armOffsetVec.mul(translationStack.last().pose());
        Vec3 vec3 = new Vec3((double) (-armOffsetVec.x()), (double) (-armOffsetVec.y()), (double) armOffsetVec.z());
        translationStack.popPose();
        return vec3.add(0.0, 5.0, -1.0);
    }
}