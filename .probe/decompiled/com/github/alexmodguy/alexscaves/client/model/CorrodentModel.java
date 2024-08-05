package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;

public class CorrodentModel extends AdvancedEntityModel<CorrodentEntity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox chest;

    private final AdvancedModelBox head;

    private final AdvancedModelBox rwhisker_S;

    private final AdvancedModelBox rwhisker;

    private final AdvancedModelBox lwhisker_S;

    private final AdvancedModelBox lwhisker;

    private final AdvancedModelBox snout;

    private final AdvancedModelBox nose;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox rarmPivot;

    private final AdvancedModelBox larmPivot;

    private final AdvancedModelBox hipsPivot;

    private final AdvancedModelBox hips;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox rlegPivot;

    private final AdvancedModelBox llegPivot;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox lleg;

    private final ModelAnimator animator;

    public CorrodentModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.chest = new AdvancedModelBox(this);
        this.chest.setRotationPoint(0.0F, -7.0F, 5.0F);
        this.root.addChild(this.chest);
        this.chest.setTextureOffset(0, 46).addBox(-1.0F, -10.0F, -7.0F, 2.0F, 5.0F, 14.0F, 0.0F, false);
        this.chest.setTextureOffset(0, 23).addBox(-4.0F, -5.0F, -7.0F, 8.0F, 9.0F, 14.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, 0.0F, -6.5F);
        this.chest.addChild(this.head);
        this.head.setTextureOffset(30, 28).addBox(-5.0F, -4.0F, -3.5F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        this.head.setTextureOffset(50, 0).addBox(-5.0F, -3.0F, -7.5F, 10.0F, 6.0F, 7.0F, 0.0F, false);
        this.head.setTextureOffset(30, 28).addBox(4.0F, -4.0F, -3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.rwhisker_S = new AdvancedModelBox(this);
        this.rwhisker_S.setRotationPoint(4.5F, -8.0F, -2.5F);
        this.head.addChild(this.rwhisker_S);
        this.rwhisker_S.setTextureOffset(0, 62).addBox(0.0F, -5.0F, -3.0F, 0.0F, 10.0F, 6.0F, 0.0F, false);
        this.rwhisker = new AdvancedModelBox(this);
        this.rwhisker.setRotationPoint(4.0F, -3.0F, -7.0F);
        this.head.addChild(this.rwhisker);
        this.rwhisker.setTextureOffset(0, 17).addBox(0.0F, -10.0F, -0.5F, 0.0F, 10.0F, 6.0F, 0.0F, false);
        this.lwhisker_S = new AdvancedModelBox(this);
        this.lwhisker_S.setRotationPoint(-4.5F, -8.0F, -2.5F);
        this.head.addChild(this.lwhisker_S);
        this.lwhisker_S.setTextureOffset(0, 62).addBox(0.0F, -5.0F, -3.0F, 0.0F, 10.0F, 6.0F, 0.0F, true);
        this.lwhisker = new AdvancedModelBox(this);
        this.lwhisker.setRotationPoint(-4.0F, -3.0F, -7.0F);
        this.head.addChild(this.lwhisker);
        this.lwhisker.setTextureOffset(0, 17).addBox(0.0F, -10.0F, -0.5F, 0.0F, 10.0F, 6.0F, 0.0F, true);
        this.snout = new AdvancedModelBox(this);
        this.snout.setRotationPoint(0.0F, -3.0F, -6.0F);
        this.head.addChild(this.snout);
        this.snout.setTextureOffset(30, 7).addBox(-2.0F, 3.0F, -9.5F, 4.0F, 7.0F, 0.0F, 0.0F, false);
        this.snout.setTextureOffset(48, 28).addBox(-3.0F, -2.0F, -9.5F, 6.0F, 5.0F, 10.0F, 0.0F, false);
        this.nose = new AdvancedModelBox(this);
        this.nose.setRotationPoint(0.0F, -1.5F, -9.25F);
        this.snout.addChild(this.nose);
        this.nose.setTextureOffset(0, 9).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(0.0F, 0.0F, -7.5F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(53, 43).addBox(-3.0F, 0.0F, -8.0F, 6.0F, 3.0F, 9.0F, -0.01F, false);
        this.jaw.setTextureOffset(32, 51).addBox(-3.0F, -2.99F, -8.0F, 6.0F, 3.0F, 9.0F, -0.01F, false);
        this.jaw.setScale(0.99F, 0.99F, 0.99F);
        this.rarmPivot = new AdvancedModelBox(this);
        this.rarmPivot.setRotationPoint(4.0F, 2.25F, -3.5F);
        this.chest.addChild(this.rarmPivot);
        this.rarm = new AdvancedModelBox(this);
        this.rarmPivot.addChild(this.rarm);
        this.rarm.setTextureOffset(0, 0).addBox(-1.0F, -1.25F, -1.5F, 3.0F, 6.0F, 3.0F, 0.0F, true);
        this.rarm.setTextureOffset(23, 0).addBox(-1.0F, 4.75F, -5.5F, 7.0F, 0.0F, 7.0F, 0.0F, true);
        this.larmPivot = new AdvancedModelBox(this);
        this.larmPivot.setRotationPoint(-4.0F, 2.25F, -3.5F);
        this.chest.addChild(this.larmPivot);
        this.larm = new AdvancedModelBox(this);
        this.larmPivot.addChild(this.larm);
        this.larm.setTextureOffset(0, 0).addBox(-2.0F, -1.25F, -1.5F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        this.larm.setTextureOffset(23, 0).addBox(-6.0F, 4.75F, -5.5F, 7.0F, 0.0F, 7.0F, 0.0F, false);
        this.hipsPivot = new AdvancedModelBox(this);
        this.hipsPivot.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.chest.addChild(this.hipsPivot);
        this.hips = new AdvancedModelBox(this);
        this.hipsPivot.addChild(this.hips);
        this.hips.setTextureOffset(30, 32).addBox(-1.0F, -10.0F, 0.0F, 2.0F, 5.0F, 14.0F, -0.01F, false);
        this.hips.setTextureOffset(0, 0).addBox(-4.0F, -5.0F, 0.0F, 8.0F, 9.0F, 14.0F, -0.01F, false);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, -5.0F, 14.0F);
        this.hips.addChild(this.tail);
        this.tail.setTextureOffset(70, 8).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 5.0F, 14.0F, 0.0F, false);
        this.rlegPivot = new AdvancedModelBox(this);
        this.rlegPivot.setRotationPoint(4.0F, 2.25F, 11.5F);
        this.hips.addChild(this.rlegPivot);
        this.rleg = new AdvancedModelBox(this);
        this.rlegPivot.addChild(this.rleg);
        this.rleg.setTextureOffset(0, 0).addBox(-1.0F, -1.25F, -1.5F, 3.0F, 6.0F, 3.0F, 0.0F, true);
        this.rleg.setTextureOffset(23, 0).addBox(-1.0F, 4.75F, -5.5F, 7.0F, 0.0F, 7.0F, 0.0F, true);
        this.llegPivot = new AdvancedModelBox(this);
        this.llegPivot.setRotationPoint(-4.0F, 2.25F, 11.5F);
        this.hips.addChild(this.llegPivot);
        this.lleg = new AdvancedModelBox(this);
        this.llegPivot.addChild(this.lleg);
        this.lleg.setTextureOffset(0, 0).addBox(-2.0F, -1.25F, -1.5F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        this.lleg.setTextureOffset(23, 0).addBox(-6.0F, 4.75F, -5.5F, 7.0F, 0.0F, 7.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.rarmPivot, this.larmPivot, this.rarm, this.larm, this.chest, this.hips, this.hipsPivot, this.llegPivot, this.rlegPivot, this.lleg, this.rleg, new AdvancedModelBox[] { this.head, this.snout, this.nose, this.tail, this.lwhisker, this.lwhisker_S, this.rwhisker, this.rwhisker_S, this.jaw });
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(CorrodentEntity.ANIMATION_BITE);
        this.animator.startKeyframe(5);
        this.animator.move(this.head, 0.0F, 0.0F, 4.0F);
        this.animator.move(this.chest, 0.0F, 0.0F, 4.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-5.0), 0.0F, (float) Math.toRadians(-5.0));
        this.animator.rotate(this.snout, (float) Math.toRadians(-35.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail, (float) Math.toRadians(25.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.chest, 0.0F, 0.0F, -2.0F);
        this.animator.move(this.jaw, 0.0F, 0.0F, 1.0F);
        this.animator.rotate(this.snout, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail, (float) Math.toRadians(45.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(CorrodentEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float walkSpeed = 1.0F;
        float walkDegree = 1.0F;
        float digSpeed = 1.0F;
        float digDegree = 1.0F;
        float stillAmount = 1.0F - limbSwingAmount;
        float digAmount = entity.getDigAmount(partialTicks);
        float afraidAmount = entity.getAfraidAmount(partialTicks);
        float walkAmount = (1.0F - digAmount) * limbSwingAmount * (1.0F + afraidAmount);
        float digLimbAmount = limbSwingAmount * digAmount;
        float twitchinessAmount = ACMath.smin((float) Math.sin((double) (ageInTicks * 0.1F)) + 0.5F, 0.0F, 0.3F);
        float digPitch = entity.getDigPitch(partialTicks);
        this.progressRotationPrev(this.tail, stillAmount, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.chest, afraidAmount, 0.0F, -2.5F, 0.0F, 1.0F);
        this.progressPositionPrev(this.hips, afraidAmount, 0.0F, -2.45F, -3.0F, 1.0F);
        this.progressPositionPrev(this.head, afraidAmount, 0.0F, -1.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.chest, afraidAmount, (float) Math.toRadians(40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, afraidAmount, (float) Math.toRadians(-40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.larm, afraidAmount, (float) Math.toRadians(-40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rarm, afraidAmount, (float) Math.toRadians(-40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.hips, afraidAmount, (float) Math.toRadians(-80.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, afraidAmount, (float) Math.toRadians(40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, afraidAmount, (float) Math.toRadians(40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail, afraidAmount, (float) Math.toRadians(70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.snout, afraidAmount, (float) Math.toRadians(-10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.jaw, afraidAmount, (float) Math.toRadians(40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rarm, digAmount, (float) Math.toRadians(-35.0), (float) Math.toRadians(-45.0), (float) Math.toRadians(-45.0), 1.0F);
        this.progressRotationPrev(this.larm, digAmount, (float) Math.toRadians(-35.0), (float) Math.toRadians(45.0), (float) Math.toRadians(45.0), 1.0F);
        this.progressRotationPrev(this.rleg, digAmount, (float) Math.toRadians(-35.0), (float) Math.toRadians(-45.0), (float) Math.toRadians(-45.0), 1.0F);
        this.progressRotationPrev(this.lleg, digAmount, (float) Math.toRadians(-35.0), (float) Math.toRadians(45.0), (float) Math.toRadians(45.0), 1.0F);
        this.progressPositionPrev(this.rarm, digAmount, 0.0F, -2.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.larm, digAmount, 0.0F, -2.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.rleg, digAmount, 0.0F, -2.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.lleg, digAmount, 0.0F, -2.0F, 0.0F, 1.0F);
        this.walk(this.head, 0.1F, 0.05F, true, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.tail, 0.1F, 0.05F, true, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.snout, 1.0F, 0.1F, true, 1.0F, -0.3F, ageInTicks, twitchinessAmount);
        this.walk(this.jaw, 1.0F, 0.1F, true, 1.0F, -0.2F, ageInTicks, twitchinessAmount);
        this.walk(this.nose, 2.5F, 0.1F, true, 1.0F, -0.4F, ageInTicks, twitchinessAmount);
        this.flap(this.lwhisker, 1.5F, 0.3F, false, 1.0F, 1.0F, ageInTicks, twitchinessAmount);
        this.flap(this.rwhisker, 1.5F, 0.3F, true, 1.0F, 1.0F, ageInTicks, twitchinessAmount);
        this.flap(this.head, 0.3F, 0.3F, true, 1.0F, 0.0F, ageInTicks, afraidAmount);
        this.swing(this.head, 0.3F, 0.2F, true, 2.0F, 0.0F, ageInTicks, afraidAmount);
        this.flap(this.lwhisker, 0.15F, 0.4F, false, 1.0F, -1.0F, ageInTicks, afraidAmount);
        this.flap(this.rwhisker, 0.15F, 0.4F, true, 1.0F, -1.0F, ageInTicks, afraidAmount);
        this.walk(this.head, walkSpeed, walkDegree * 0.05F, true, 1.0F, 0.1F, limbSwing, walkAmount);
        this.flap(this.chest, walkSpeed, walkDegree * 0.1F, true, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.hips, walkSpeed, walkDegree * 0.1F, true, 2.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.lleg, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.rleg, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.larm, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.rarm, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.flap(this.head, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, walkAmount);
        this.swing(this.tail, walkSpeed, walkDegree * 0.4F, false, -1.0F, 0.0F, limbSwing, walkAmount);
        float bodyBob = ACMath.walkValue(limbSwing, walkAmount, walkSpeed * 1.5F, 0.5F, 1.0F, true);
        this.chest.rotationPointY += bodyBob;
        this.walk(this.rarm, walkSpeed, walkDegree, false, -1.5F, 0.0F, limbSwing, walkAmount);
        this.rarm.rotateAngleZ = this.rarm.rotateAngleZ - this.chest.rotateAngleZ * walkAmount;
        this.rarm.rotationPointY = this.rarm.rotationPointY - (bodyBob - Math.min(0.0F, ACMath.walkValue(limbSwing, walkAmount, walkSpeed, -1.5F, 4.0F, true)) + walkAmount);
        this.walk(this.larm, walkSpeed, walkDegree, true, -1.5F, 0.0F, limbSwing, walkAmount);
        this.larm.rotateAngleZ = this.larm.rotateAngleZ - this.chest.rotateAngleZ * walkAmount;
        this.larm.rotationPointY = this.larm.rotationPointY - (bodyBob - Math.min(0.0F, ACMath.walkValue(limbSwing, walkAmount, walkSpeed, -1.5F, 4.0F, false)) + walkAmount);
        this.walk(this.rleg, walkSpeed, walkDegree, true, -2.5F, 0.0F, limbSwing, walkAmount);
        this.rleg.rotateAngleZ = this.rleg.rotateAngleZ - (this.chest.rotateAngleZ + this.hips.rotateAngleZ) * walkAmount;
        this.rleg.rotationPointY = this.rleg.rotationPointY - (bodyBob - Math.min(0.0F, ACMath.walkValue(limbSwing, walkAmount, walkSpeed, -2.5F, 4.0F, false)) + walkAmount);
        this.walk(this.lleg, walkSpeed, walkDegree, false, -2.5F, 0.0F, limbSwing, walkAmount);
        this.lleg.rotateAngleZ = this.lleg.rotateAngleZ - (this.chest.rotateAngleZ + this.hips.rotateAngleZ) * walkAmount;
        this.lleg.rotationPointY = this.lleg.rotationPointY - (bodyBob - Math.min(0.0F, ACMath.walkValue(limbSwing, walkAmount, walkSpeed, -2.5F, 4.0F, true)) + walkAmount);
        this.swing(this.chest, digSpeed * 0.5F, digDegree * 0.1F, false, 2.0F, 0.0F, limbSwing, digLimbAmount);
        this.swing(this.hips, digSpeed * 0.5F, digDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, digLimbAmount);
        this.swing(this.tail, digSpeed * 0.5F, digDegree * 0.1F, false, 0.0F, 0.0F, limbSwing, digLimbAmount);
        this.swing(this.rarmPivot, digSpeed, digDegree, false, 1.0F, -0.3F, limbSwing, digLimbAmount);
        this.walk(this.rarmPivot, digSpeed, digDegree * 0.5F, false, 2.0F, 0.0F, limbSwing, digLimbAmount);
        this.flap(this.rarmPivot, digSpeed, digDegree, false, 3.0F, 0.0F, limbSwing, digLimbAmount);
        this.swing(this.larmPivot, digSpeed, digDegree, false, 1.0F, 0.3F, limbSwing, digLimbAmount);
        this.walk(this.larmPivot, digSpeed, digDegree * 0.5F, true, 2.0F, 0.0F, limbSwing, digLimbAmount);
        this.flap(this.larmPivot, digSpeed, digDegree, false, 3.0F, 0.0F, limbSwing, digLimbAmount);
        this.swing(this.llegPivot, digSpeed, digDegree, false, -1.0F, 0.3F, limbSwing, digLimbAmount);
        this.walk(this.llegPivot, digSpeed, digDegree * 0.5F, false, -2.0F, 0.0F, limbSwing, digLimbAmount);
        this.flap(this.llegPivot, digSpeed, digDegree, false, -2.0F, 0.0F, limbSwing, digLimbAmount);
        this.swing(this.rlegPivot, digSpeed, digDegree, false, -1.0F, -0.3F, limbSwing, digLimbAmount);
        this.walk(this.rlegPivot, digSpeed, digDegree * 0.5F, true, -2.0F, 0.0F, limbSwing, digLimbAmount);
        this.flap(this.rlegPivot, digSpeed, digDegree, false, 3.0F, 0.0F, limbSwing, digLimbAmount);
        this.walk(this.head, digSpeed * 4.0F, digDegree * 0.15F, true, 4.0F, -0.05F, limbSwing, digLimbAmount);
        this.walk(this.jaw, digSpeed * 4.0F, digDegree * 0.15F, true, 4.0F, -0.25F, limbSwing, digLimbAmount);
        float headPitchAmount = headPitch / (180.0F / (float) Math.PI);
        this.head.rotateAngleX += headPitchAmount;
        double defaultX = (double) Mth.wrapDegrees(digPitch);
        double defaultY = (double) Mth.wrapDegrees(entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTicks);
        double tailX = (double) entity.getTrailTransformation(5, 0, partialTicks) - defaultX;
        double tail1Y = (double) entity.getTrailTransformation(5, 1, partialTicks) - defaultY;
        double tail2Y = (double) entity.getTrailTransformation(10, 1, partialTicks) - defaultY;
        this.chest.rotateAngleX = (float) ((double) this.chest.rotateAngleX + Math.toRadians((double) digPitch));
        this.hipsPivot.rotateAngleY = (float) ((double) this.hipsPivot.rotateAngleY + Math.toRadians(Mth.wrapDegrees(tail1Y)) * (double) (1.0F - afraidAmount));
        this.tail.rotateAngleY = (float) ((double) this.tail.rotateAngleY + Math.toRadians(Mth.wrapDegrees(tail2Y)));
        this.hipsPivot.rotateAngleX = (float) ((double) this.hipsPivot.rotateAngleX + Math.toRadians(Mth.wrapDegrees(tailX)) * (double) (1.0F - afraidAmount));
        this.head.rotateAngleY = (float) ((double) this.head.rotateAngleY + Math.toRadians((double) Mth.approachDegrees((float) defaultY, (float) ((double) netHeadYaw + defaultY), 20.0F) - defaultY));
    }
}