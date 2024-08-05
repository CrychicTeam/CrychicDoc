package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.HullbreakerEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class HullbreakerModel extends AdvancedEntityModel<HullbreakerEntity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox dorsal;

    private final AdvancedModelBox bottomFlipper;

    private final AdvancedModelBox lflipper;

    private final AdvancedModelBox rflipper;

    private final AdvancedModelBox body2;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox tail3;

    private final AdvancedModelBox dorsal2;

    private final AdvancedModelBox rbarb;

    private final AdvancedModelBox rbarb2;

    private final AdvancedModelBox rbarbLure;

    private final AdvancedModelBox lbarb;

    private final AdvancedModelBox lbarb2;

    private final AdvancedModelBox lbarbLure;

    private final AdvancedModelBox head;

    private final AdvancedModelBox teeth1;

    private final AdvancedModelBox teeth2;

    private final AdvancedModelBox teeth3;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox teeth4;

    private final AdvancedModelBox teeth5;

    private final ModelAnimator animator;

    public boolean straighten = false;

    public HullbreakerModel() {
        this.texWidth = 512;
        this.texHeight = 512;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 0.0F, -15.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-12.0F, -41.0F, -12.0F, 24.0F, 41.0F, 53.0F, 0.0F, false);
        this.dorsal = new AdvancedModelBox(this);
        this.dorsal.setRotationPoint(0.0F, -30.5F, -8.5F);
        this.body.addChild(this.dorsal);
        this.dorsal.setTextureOffset(0, 41).addBox(0.0F, -67.5F, -10.5F, 0.0F, 75.0F, 53.0F, 0.0F, false);
        this.bottomFlipper = new AdvancedModelBox(this);
        this.bottomFlipper.setRotationPoint(0.0F, -1.5F, 32.0F);
        this.body.addChild(this.bottomFlipper);
        this.bottomFlipper.setTextureOffset(122, 245).addBox(0.0F, -3.5F, -6.0F, 0.0F, 39.0F, 32.0F, 0.0F, false);
        this.lflipper = new AdvancedModelBox(this);
        this.lflipper.setRotationPoint(12.0F, -9.5F, -2.0F);
        this.body.addChild(this.lflipper);
        this.setRotateAngle(this.lflipper, 0.3927F, -0.7854F, 0.0F);
        this.lflipper.setTextureOffset(101, 0).addBox(0.0F, -31.5F, 0.0F, 47.0F, 41.0F, 0.0F, 0.0F, false);
        this.rflipper = new AdvancedModelBox(this);
        this.rflipper.setRotationPoint(-12.0F, -9.5F, -2.0F);
        this.body.addChild(this.rflipper);
        this.setRotateAngle(this.rflipper, 0.3927F, 0.7854F, 0.0F);
        this.rflipper.setTextureOffset(101, 0).addBox(-47.0F, -31.5F, 0.0F, 47.0F, 41.0F, 0.0F, 0.0F, true);
        this.body2 = new AdvancedModelBox(this);
        this.body2.setRotationPoint(0.0F, -17.0F, 40.0F);
        this.body.addChild(this.body2);
        this.body2.setTextureOffset(210, 106).addBox(-10.0F, -14.0F, -2.0F, 20.0F, 28.0F, 46.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, 1.5F, 43.75F);
        this.body2.addChild(this.tail);
        this.tail.setTextureOffset(0, 117).addBox(0.0F, -35.5F, 6.25F, 0.0F, 67.0F, 52.0F, 0.0F, false);
        this.tail.setTextureOffset(0, 236).addBox(-6.0F, -9.5F, -2.75F, 12.0F, 19.0F, 49.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this);
        this.tail2.setRotationPoint(0.0F, 0.0F, 58.25F);
        this.tail.addChild(this.tail2);
        this.tail2.setTextureOffset(106, 109).addBox(0.0F, -35.5F, 0.0F, 0.0F, 67.0F, 52.0F, 0.0F, false);
        this.tail3 = new AdvancedModelBox(this);
        this.tail3.setRotationPoint(0.0F, 0.0F, 52.0F);
        this.tail2.addChild(this.tail3);
        this.tail3.setTextureOffset(106, 42).addBox(0.0F, -35.5F, 0.0F, 0.0F, 67.0F, 52.0F, 0.0F, false);
        this.tail3.setTextureOffset(0, 0).addBox(-3.5F, -5.5F, 44.0F, 7.0F, 7.0F, 7.0F, 0.0F, false);
        this.dorsal2 = new AdvancedModelBox(this);
        this.dorsal2.setRotationPoint(0.0F, -14.5F, -3.5F);
        this.body2.addChild(this.dorsal2);
        this.dorsal2.setTextureOffset(104, 173).addBox(0.0F, -48.5F, -2.5F, 0.0F, 49.0F, 55.0F, 0.0F, false);
        this.rbarb = new AdvancedModelBox(this);
        this.rbarb.setRotationPoint(-9.0F, -0.5F, -3.0F);
        this.body.addChild(this.rbarb);
        this.rbarb.setTextureOffset(214, 208).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 17.0F, 2.0F, 0.0F, true);
        this.rbarb2 = new AdvancedModelBox(this);
        this.rbarb2.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.rbarb.addChild(this.rbarb2);
        this.rbarb2.setTextureOffset(214, 208).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 17.0F, 2.0F, 0.0F, true);
        this.rbarbLure = new AdvancedModelBox(this);
        this.rbarbLure.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.rbarb2.addChild(this.rbarbLure);
        this.rbarbLure.setTextureOffset(214, 208).addBox(0.0F, 0.5F, -3.0F, 0.0F, 24.0F, 32.0F, 0.0F, true);
        this.rbarbLure.setTextureOffset(30, 4).addBox(-2.5F, 21.5F, 3.0F, 5.0F, 5.0F, 5.0F, 0.0F, true);
        this.lbarb = new AdvancedModelBox(this);
        this.lbarb.setRotationPoint(9.0F, -0.5F, -3.0F);
        this.body.addChild(this.lbarb);
        this.lbarb.setTextureOffset(214, 208).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 17.0F, 2.0F, 0.0F, false);
        this.lbarb2 = new AdvancedModelBox(this);
        this.lbarb2.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.lbarb.addChild(this.lbarb2);
        this.lbarb2.setTextureOffset(214, 208).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 17.0F, 2.0F, 0.0F, false);
        this.lbarbLure = new AdvancedModelBox(this);
        this.lbarbLure.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.lbarb2.addChild(this.lbarbLure);
        this.lbarbLure.setTextureOffset(214, 208).addBox(0.0F, 0.5F, -3.0F, 0.0F, 24.0F, 32.0F, 0.0F, false);
        this.lbarbLure.setTextureOffset(30, 4).addBox(-2.5F, 21.5F, 3.0F, 5.0F, 5.0F, 5.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(1.0F, -27.0F, -10.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(278, 255).addBox(-10.0F, 8.0F, -12.0F, 18.0F, 18.0F, 13.0F, -0.01F, false);
        this.head.setTextureOffset(154, 0).addBox(-11.0F, 0.0F, -52.0F, 20.0F, 26.0F, 53.0F, 0.0F, false);
        this.head.setTextureOffset(0, 304).addBox(-11.0F, 0.0F, -51.0F, 20.0F, 11.0F, 38.0F, 0.0F, false);
        this.teeth1 = new AdvancedModelBox(this);
        this.teeth1.setRotationPoint(-1.0F, 15.0F, -51.1464F);
        this.head.addChild(this.teeth1);
        this.setRotateAngle(this.teeth1, 0.0F, -0.7854F, 0.0F);
        this.teeth1.setTextureOffset(0, 304).addBox(-6.5F, -4.0F, -6.5F, 13.0F, 8.0F, 13.0F, 0.01F, false);
        this.teeth2 = new AdvancedModelBox(this);
        this.teeth2.setRotationPoint(-1.0F, 11.5F, -52.0F);
        this.head.addChild(this.teeth2);
        this.setRotateAngle(this.teeth2, 0.0F, 0.7854F, 0.0F);
        this.teeth2.setTextureOffset(278, 240).addBox(-39.0F, -11.44F, -7.0F, 32.0F, 5.0F, 10.0F, 0.07F, true);
        this.teeth3 = new AdvancedModelBox(this);
        this.teeth3.setRotationPoint(-1.0F, 11.5F, -52.0F);
        this.head.addChild(this.teeth3);
        this.setRotateAngle(this.teeth3, 0.0F, -0.7854F, 0.0F);
        this.teeth3.setTextureOffset(278, 240).addBox(7.0F, -11.44F, -7.0F, 32.0F, 5.0F, 10.0F, 0.07F, false);
        this.teeth3.setTextureOffset(247, 0).addBox(-7.0F, -11.44F, -7.0F, 14.0F, 36.0F, 14.0F, 0.07F, false);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(-1.0F, 26.0F, -12.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(0, 0).addBox(-9.0F, -19.0F, -54.0F, 0.0F, 39.0F, 14.0F, 0.0F, true);
        this.jaw.setTextureOffset(284, 136).addBox(-9.0F, 7.0F, -54.0F, 18.0F, 0.0F, 14.0F, 0.0F, true);
        this.jaw.setTextureOffset(0, 0).addBox(9.0F, -19.0F, -54.0F, 0.0F, 39.0F, 14.0F, 0.0F, false);
        this.jaw.setTextureOffset(214, 180).addBox(-9.0F, 0.0F, -40.0F, 18.0F, 7.0F, 53.0F, 0.0F, false);
        this.teeth4 = new AdvancedModelBox(this);
        this.teeth4.setRotationPoint(9.0F, 0.0F, -13.5F);
        this.jaw.addChild(this.teeth4);
        this.setRotateAngle(this.teeth4, 0.0F, 0.0F, 0.3927F);
        this.teeth4.setTextureOffset(210, 20).addBox(0.0F, -26.0F, -33.5F, 0.0F, 26.0F, 60.0F, 0.0F, false);
        this.teeth5 = new AdvancedModelBox(this);
        this.teeth5.setRotationPoint(-9.0F, 0.0F, -13.5F);
        this.jaw.addChild(this.teeth5);
        this.setRotateAngle(this.teeth5, 0.0F, 0.0F, -0.3927F);
        this.teeth5.setTextureOffset(210, 20).addBox(0.0F, -26.0F, -33.5F, 0.0F, 26.0F, 60.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.body2, this.tail, this.head, this.tail, this.tail2, this.tail3, this.dorsal, this.dorsal2, this.jaw, this.rbarb, new AdvancedModelBox[] { this.rbarb2, this.rbarbLure, this.lbarb, this.lbarb2, this.lbarbLure, this.teeth1, this.teeth2, this.teeth3, this.teeth4, this.teeth5, this.rflipper, this.lflipper });
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(HullbreakerEntity.ANIMATION_PUZZLE);
        this.animator.startKeyframe(10);
        this.animator.move(this.head, -5.0F, -5.0F, 0.0F);
        this.animator.rotate(this.head, 0.0F, (float) Math.toRadians(-5.0), (float) Math.toRadians(-20.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(20);
        this.animator.move(this.head, 5.0F, -5.0F, 0.0F);
        this.animator.rotate(this.head, 0.0F, (float) Math.toRadians(5.0), (float) Math.toRadians(20.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(20);
        this.animator.move(this.head, -5.0F, -5.0F, 0.0F);
        this.animator.rotate(this.head, 0.0F, (float) Math.toRadians(-5.0), (float) Math.toRadians(-20.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(HullbreakerEntity.ANIMATION_BITE);
        this.animator.startKeyframe(10);
        this.animator.move(this.head, 0.0F, -2.0F, 10.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(80.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, -10.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(HullbreakerEntity.ANIMATION_BASH);
        this.animator.startKeyframe(10);
        this.animator.move(this.head, 0.0F, -11.0F, -3.0F);
        this.animator.rotate(this.body, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.body2, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(50.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, -5.0F, -20.0F);
        this.animator.move(this.head, 0.0F, -9.0F, -3.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(30.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(HullbreakerEntity.ANIMATION_DIE);
        this.animator.startKeyframe(10);
        this.animator.move(this.head, 0.0F, -2.0F, 10.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(80.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, 0.0F, (float) Math.toRadians(5.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(35);
        this.animator.move(this.head, 0.0F, -2.0F, 10.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-30.0), 0.0F, (float) Math.toRadians(-20.0));
        this.animator.rotate(this.jaw, (float) Math.toRadians(40.0), 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, 0.0F, (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(HullbreakerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float bodyYRot = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTicks;
        float clampedYaw = netHeadYaw / (180.0F / (float) Math.PI);
        float fishPitchAmount = entity.getFishPitch(partialTicks) / (180.0F / (float) Math.PI);
        float headPitchAmount = headPitch / (180.0F / (float) Math.PI);
        float idleSpeed = 0.1F;
        float idleDegree = 0.5F;
        float swimSpeed = 0.45F;
        float swimDegree = 0.4F;
        float landProgress = entity.getLandProgress(partialTicks);
        float stillAmount = (1.0F - limbSwingAmount) * (1.0F - landProgress);
        this.body.rotationPointY = this.body.rotationPointY - ACMath.walkValue(ageInTicks, 1.0F, idleSpeed, 1.5F, 3.0F, true) * stillAmount;
        this.walk(this.jaw, idleSpeed, idleDegree * 0.15F, false, 2.0F, 0.2F, ageInTicks, stillAmount);
        this.flap(this.head, idleSpeed, idleDegree * 0.05F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.dorsal, idleSpeed, idleDegree * 0.1F, false, 4.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.dorsal2, idleSpeed, idleDegree * 0.1F, false, 3.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.rbarb, idleSpeed, idleDegree * 0.2F, false, -1.0F, 0.2F, ageInTicks, 1.0F);
        this.walk(this.lbarb, idleSpeed, idleDegree * 0.2F, false, -1.0F, 0.2F, ageInTicks, 1.0F);
        this.walk(this.rbarb2, idleSpeed, idleDegree * 0.2F, false, -1.0F, 0.2F, ageInTicks, 1.0F);
        this.walk(this.lbarb2, idleSpeed, idleDegree * 0.2F, false, -1.0F, 0.2F, ageInTicks, 1.0F);
        this.swing(this.rbarb, idleSpeed, idleDegree * 0.2F, true, -1.0F, 0.2F, ageInTicks, stillAmount);
        this.swing(this.lbarb, idleSpeed, idleDegree * 0.2F, false, -1.0F, 0.2F, ageInTicks, stillAmount);
        this.walk(this.rflipper, idleSpeed * 2.0F, idleDegree * 0.4F, false, 0.0F, 0.0F, ageInTicks, stillAmount);
        this.swing(this.rflipper, idleSpeed * 2.0F, idleDegree * 0.8F, false, 2.0F, -0.25F, ageInTicks, stillAmount);
        this.flap(this.rflipper, idleSpeed * 2.0F, idleDegree * 0.5F, true, 0.0F, 0.2F, ageInTicks, stillAmount);
        this.walk(this.lflipper, idleSpeed * 2.0F, idleDegree * 0.4F, true, 2.0F, 0.0F, ageInTicks, stillAmount);
        this.swing(this.lflipper, idleSpeed * 2.0F, idleDegree * 0.8F, true, 4.0F, -0.25F, ageInTicks, stillAmount);
        this.flap(this.lflipper, idleSpeed * 2.0F, idleDegree * 0.5F, false, 2.0F, 0.2F, ageInTicks, stillAmount);
        this.body.rotationPointY = this.body.rotationPointY - ACMath.walkValue(limbSwingAmount, 1.0F, swimSpeed, 1.5F, 6.0F, true);
        this.swing(this.body, swimSpeed, swimDegree * 0.05F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.body, swimSpeed, swimDegree * 0.05F, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.head, swimSpeed, swimDegree * 0.1F, true, 2.0F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(this.jaw, swimSpeed, swimDegree * 0.1F, true, 3.0F, -0.1F, limbSwing, limbSwingAmount);
        this.walk(this.dorsal, swimSpeed, swimDegree * 0.2F, true, 2.0F, 0.4F, limbSwing, limbSwingAmount);
        this.walk(this.dorsal2, swimSpeed, swimDegree * 0.2F, true, 1.0F, 0.4F, limbSwing, limbSwingAmount);
        this.walk(this.rbarb, swimSpeed, swimDegree * 0.01F, false, -1.0F, 0.9F, limbSwing, limbSwingAmount);
        this.walk(this.lbarb, swimSpeed, swimDegree * 0.01F, false, -1.0F, 0.9F, limbSwing, limbSwingAmount);
        this.swing(this.rflipper, swimSpeed, swimDegree * 1.0F, false, -1.0F, 0.3F, limbSwing, limbSwingAmount);
        this.swing(this.lflipper, swimSpeed, swimDegree * 1.0F, false, -1.0F, -0.3F, limbSwing, limbSwingAmount);
        this.flap(this.rflipper, swimSpeed, swimDegree * 0.5F, true, -2.5F, 0.2F, limbSwing, limbSwingAmount);
        this.flap(this.lflipper, swimSpeed, swimDegree * 0.5F, false, -2.5F, 0.2F, limbSwing, limbSwingAmount);
        this.progressPositionPrev(this.root, landProgress, 0.0F, 17.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.head, landProgress, 0.0F, -5.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, landProgress, (float) Math.toRadians(-10.0), 0.0F, (float) Math.toRadians(30.0), 1.0F);
        this.progressRotationPrev(this.body, landProgress, 0.0F, 0.0F, (float) Math.toRadians(-40.0), 1.0F);
        this.progressRotationPrev(this.body2, landProgress, 0.0F, 0.0F, (float) Math.toRadians(-30.0), 1.0F);
        this.progressRotationPrev(this.tail, landProgress, 0.0F, (float) Math.toRadians(-10.0), (float) Math.toRadians(-20.0), 1.0F);
        this.progressRotationPrev(this.tail2, landProgress, 0.0F, (float) Math.toRadians(5.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.rflipper, landProgress, 0.0F, (float) Math.toRadians(40.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.lflipper, landProgress, 0.0F, (float) Math.toRadians(-40.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.rbarb, landProgress, (float) Math.toRadians(20.0), 0.0F, (float) Math.toRadians(-60.0), 1.0F);
        this.progressRotationPrev(this.lbarb, landProgress, 0.0F, 0.0F, (float) Math.toRadians(-40.0), 1.0F);
        this.body.rotateAngleX += fishPitchAmount * 0.75F;
        this.head.rotateAngleX += headPitchAmount;
        this.head.rotateAngleY += clampedYaw;
        if (!this.straighten) {
            this.body2.rotateAngleX = (float) ((double) this.body2.rotateAngleX + Math.toRadians((double) entity.tail1Part.calculateAnimationAngle(partialTicks, true)));
            this.body2.rotateAngleY = (float) ((double) this.body2.rotateAngleY + Math.toRadians((double) entity.tail1Part.calculateAnimationAngle(partialTicks, false)));
            this.tail.rotateAngleX = (float) ((double) this.tail.rotateAngleX + Math.toRadians((double) entity.tail2Part.calculateAnimationAngle(partialTicks, true)));
            this.tail.rotateAngleY = (float) ((double) this.tail.rotateAngleY + Math.toRadians((double) entity.tail2Part.calculateAnimationAngle(partialTicks, false)));
            float tail2XRot = entity.tail3Part.calculateAnimationAngle(partialTicks, true);
            float tail3XRot = entity.tail4Part.calculateAnimationAngle(partialTicks, true);
            this.tail2.rotateAngleX = (float) ((double) this.tail2.rotateAngleX + Math.toRadians((double) tail2XRot));
            this.tail2.rotationPointZ = this.tail2.rotationPointZ - 5.0F * Math.abs(tail2XRot / 20.0F);
            this.tail2.rotateAngleY = (float) ((double) this.tail2.rotateAngleY + Math.toRadians((double) entity.tail3Part.calculateAnimationAngle(partialTicks, false)));
            this.tail3.rotateAngleX = (float) ((double) this.tail3.rotateAngleX + Math.toRadians((double) tail3XRot));
            this.tail3.rotationPointZ = this.tail3.rotationPointZ - 5.0F * Math.abs(tail3XRot / 20.0F);
            this.tail3.rotateAngleY = (float) ((double) this.tail3.rotateAngleY + Math.toRadians((double) entity.tail4Part.calculateAnimationAngle(partialTicks, false)));
            this.lbarb.rotateAngleY = (float) ((double) this.lbarb.rotateAngleY + Math.toRadians((double) (entity.getYawFromBuffer(2, partialTicks) - bodyYRot)));
            this.rbarb.rotateAngleY = (float) ((double) this.rbarb.rotateAngleY + Math.toRadians((double) (entity.getYawFromBuffer(2, partialTicks) - bodyYRot)));
            this.lbarb2.rotateAngleY = (float) ((double) this.lbarb2.rotateAngleY + Math.toRadians((double) (entity.getYawFromBuffer(4, partialTicks) - bodyYRot)));
            this.rbarb2.rotateAngleY = (float) ((double) this.rbarb2.rotateAngleY + Math.toRadians((double) (entity.getYawFromBuffer(4, partialTicks) - bodyYRot)));
        }
    }
}