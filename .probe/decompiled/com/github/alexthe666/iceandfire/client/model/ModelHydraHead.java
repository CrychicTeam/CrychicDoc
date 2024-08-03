package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ModelHydraHead extends ModelDragonBase<EntityHydra> {

    public AdvancedModelBox Neck1;

    public AdvancedModelBox Neck2;

    public AdvancedModelBox Neck3;

    public AdvancedModelBox Neck4;

    public AdvancedModelBox Head1;

    public AdvancedModelBox HeadPivot;

    public AdvancedModelBox neckSpike1;

    public AdvancedModelBox neckSpike2;

    public AdvancedModelBox UpperJaw1;

    public AdvancedModelBox LowerJaw1;

    public AdvancedModelBox TeethTR1;

    public AdvancedModelBox TeethL1;

    public AdvancedModelBox TeethR1;

    public AdvancedModelBox TeethTL1;

    private final ModelAnimator animator;

    private int headIndex = 0;

    public ModelHydraHead(int headIndex) {
        this.headIndex = headIndex;
        this.texWidth = 256;
        this.texHeight = 128;
        this.Neck3 = new AdvancedModelBox(this, 25, 90);
        this.Neck3.setPos(0.0F, -0.5F, -6.7F);
        this.Neck3.addBox(-1.92F, -2.0F, -8.0F, 4.0F, 4.0F, 9.0F, 0.0F);
        this.setRotateAngle(this.Neck3, -0.12217305F, -0.0F, 0.0F);
        this.TeethR1 = new AdvancedModelBox(this, 6, 44);
        this.TeethR1.mirror = true;
        this.TeethR1.setPos(0.0F, 0.0F, 0.0F);
        this.TeethR1.addBox(-0.2F, 0.8F, -4.1F, 2.0F, 2.0F, 6.0F, 0.0F);
        this.neckSpike2 = new AdvancedModelBox(this, 0, 0);
        this.neckSpike2.setPos(0.0F, -0.9F, -3.7F);
        this.neckSpike2.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.neckSpike2, 0.68294734F, -0.0F, 0.0F);
        this.Head1 = new AdvancedModelBox(this, 6, 77);
        this.Head1.addBox(-3.0F, -1.3F, -2.5F, 6.0F, 3.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.Head1, (float) (Math.PI / 4), -0.0F, 0.0F);
        this.HeadPivot = new AdvancedModelBox(this, 6, 77);
        this.HeadPivot.setPos(0.0F, -0.2F, -8.3F);
        this.LowerJaw1 = new AdvancedModelBox(this, 6, 63);
        this.LowerJaw1.setPos(0.0F, 1.9F, -0.8F);
        this.LowerJaw1.addBox(-2.0F, -0.6F, -7.0F, 4.0F, 1.0F, 7.0F, 0.0F);
        this.Neck2 = new AdvancedModelBox(this, 85, 72);
        this.Neck2.setPos(0.0F, 0.2F, -6.8F);
        this.Neck2.addBox(-2.0F, -2.8F, -7.0F, 4.0F, 5.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.Neck2, (float) (-Math.PI / 6), 0.0F, 0.0F);
        this.Neck1 = new AdvancedModelBox(this, 56, 80);
        this.Neck1.setPos(-0.0F, 1.5F, -0.2F);
        this.Neck1.addBox(-2.5F, -3.0F, -7.0F, 5.0F, 6.0F, 9.0F, 0.0F);
        this.setRotateAngle(this.Neck1, (float) (-Math.PI / 9), 0.0F, 0.0F);
        this.TeethL1 = new AdvancedModelBox(this, 6, 44);
        this.TeethL1.setPos(0.0F, 0.9F, -2.8F);
        this.TeethL1.addBox(-1.9F, 0.8F, -4.1F, 2.0F, 2.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.TeethL1, 0.045553092F, -0.0F, (float) -Math.PI);
        this.Neck4 = new AdvancedModelBox(this, 35, 70);
        this.Neck4.mirror = true;
        this.Neck4.setPos(0.0F, 0.0F, -7.4F);
        this.Neck4.addBox(-2.0F, -2.0F, -8.2F, 4.0F, 4.0F, 9.0F, 0.0F);
        this.setRotateAngle(this.Neck4, 0.4712389F, 0.0F, 0.0F);
        this.neckSpike1 = new AdvancedModelBox(this, 40, 0);
        this.neckSpike1.setPos(0.0F, -1.2F, -6.0F);
        this.neckSpike1.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.neckSpike1, 0.7740535F, 0.0F, 0.0F);
        this.TeethTL1 = new AdvancedModelBox(this, 6, 44);
        this.TeethTL1.mirror = true;
        this.TeethTL1.setPos(0.0F, 0.0F, 0.0F);
        this.TeethTL1.addBox(-0.2F, 0.8F, -4.1F, 2.0F, 2.0F, 6.0F, 0.0F);
        this.UpperJaw1 = new AdvancedModelBox(this, 6, 54);
        this.UpperJaw1.setPos(0.0F, 0.0F, -2.4F);
        this.UpperJaw1.addBox(-2.5F, -1.7F, -5.8F, 5.0F, 3.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.UpperJaw1, 0.091106184F, -0.0F, 0.0F);
        this.TeethTR1 = new AdvancedModelBox(this, 6, 44);
        this.TeethTR1.setPos(0.0F, -0.4F, -3.6F);
        this.setRotateAngle(this.TeethTR1, -0.091106184F, -0.0F, 0.0F);
        this.TeethTR1.addBox(-1.9F, 0.8F, -4.1F, 2.0F, 2.0F, 6.0F, 0.0F);
        this.Neck2.addChild(this.Neck3);
        this.TeethL1.addChild(this.TeethR1);
        this.Neck4.addChild(this.neckSpike2);
        this.Neck4.addChild(this.HeadPivot);
        this.HeadPivot.addChild(this.Head1);
        this.Head1.addChild(this.LowerJaw1);
        this.Neck1.addChild(this.Neck2);
        this.LowerJaw1.addChild(this.TeethL1);
        this.Neck3.addChild(this.Neck4);
        this.Head1.addChild(this.TeethTR1);
        this.Neck4.addChild(this.neckSpike1);
        this.TeethTR1.addChild(this.TeethTL1);
        this.Head1.addChild(this.UpperJaw1);
        this.animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        this.animator.update(entity);
    }

    public void setupAnim(EntityHydra entity, float f, float f1, float f2, float f3, float f4) {
        this.animate(entity, f, f1, f2, f3, f4, 1.0F);
        float speed_walk = 0.6F;
        float speed_idle = 0.05F;
        float degree_walk = 0.2F;
        float degree_idle = 0.5F;
        if (!EntityGorgon.isStoneMob(entity)) {
            float partialTicks = Minecraft.getInstance().getFrameTime();
            AdvancedModelBox[] ENTIRE_HEAD = new AdvancedModelBox[] { this.Neck1, this.Neck2, this.Neck3, this.Neck4 };
            this.chainFlap(ENTIRE_HEAD, speed_idle, degree_idle * 0.15F, (double) (-3 + this.headIndex % 4), f2, 1.0F);
            this.chainSwing(ENTIRE_HEAD, speed_idle, degree_idle * 0.05F, (double) (-3 + this.headIndex % 3), f2, 1.0F);
            this.chainWave(ENTIRE_HEAD, speed_idle * 1.5F, degree_idle * 0.05F, (double) (-2 + this.headIndex % 3), f2, 1.0F);
            this.faceTarget(f3, f4, 1.0F, new AdvancedModelBox[] { this.Head1 });
            this.walk(this.neckSpike1, speed_idle * 1.5F, degree_idle * 0.4F, false, 2.0F, -0.1F, f2, 1.0F);
            this.walk(this.neckSpike2, speed_idle * 1.5F, degree_idle * 0.4F, false, 3.0F, -0.1F, f2, 1.0F);
            this.chainSwing(ENTIRE_HEAD, speed_walk, degree_walk * 0.75F, -3.0, f, f1);
            float speakProgress = entity.prevSpeakingProgress[this.headIndex] + partialTicks * (entity.speakingProgress[this.headIndex] - entity.prevSpeakingProgress[this.headIndex]);
            this.progressRotationInterp(this.LowerJaw1, Mth.sin((float) ((double) speakProgress * Math.PI)) * 10.0F, (float) Math.toRadians(25.0), 0.0F, 0.0F, 10.0F);
            float strikeProgress = entity.prevStrikeProgress[this.headIndex] + partialTicks * (entity.strikingProgress[this.headIndex] - entity.prevStrikeProgress[this.headIndex]);
            this.progressRotationInterp(this.Neck2, strikeProgress, (float) Math.toRadians(5.0), 0.0F, 0.0F, 10.0F);
            this.progressRotationInterp(this.Neck3, strikeProgress, (float) Math.toRadians(5.0), 0.0F, 0.0F, 10.0F);
            this.progressRotationInterp(this.Neck4, strikeProgress, (float) Math.toRadians(5.0), 0.0F, 0.0F, 10.0F);
            this.progressRotationInterp(this.Head1, strikeProgress, (float) Math.toRadians(-15.0), 0.0F, 0.0F, 10.0F);
            this.progressRotationInterp(this.LowerJaw1, strikeProgress, (float) Math.toRadians(45.0), 0.0F, 0.0F, 10.0F);
            this.progressPositionInterp(this.TeethTR1, strikeProgress, 0.5F, 0.0F, 0.0F, 10.0F);
            float breathProgress = entity.prevBreathProgress[this.headIndex] + partialTicks * (entity.breathProgress[this.headIndex] - entity.prevBreathProgress[this.headIndex]);
            this.progressRotationInterp(this.Neck4, breathProgress, (float) Math.toRadians(15.0), 0.0F, 0.0F, 10.0F);
            this.progressRotationInterp(this.Neck3, breathProgress, (float) Math.toRadians(15.0), 0.0F, 0.0F, 10.0F);
            this.progressPositionInterp(this.TeethTR1, breathProgress, 0.5F, 0.0F, 0.0F, 10.0F);
            this.progressRotationInterp(this.Head1, breathProgress, (float) Math.toRadians(15.0), 0.0F, 0.0F, 10.0F);
            this.progressRotationInterp(this.UpperJaw1, breathProgress, (float) Math.toRadians(-10.0), 0.0F, 0.0F, 10.0F);
            this.progressRotationInterp(this.LowerJaw1, breathProgress, (float) Math.toRadians(50.0), 0.0F, 0.0F, 10.0F);
            this.Neck2.showModel = entity.getSeveredHead() != this.headIndex && entity.m_6084_();
        }
    }

    @Override
    public void renderStatue(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.m_7695_(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.Neck1);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.Neck1, this.Neck2, this.Neck3, this.Neck4, this.Head1, this.HeadPivot, this.neckSpike1, this.neckSpike2, this.UpperJaw1, this.LowerJaw1, this.TeethTR1, this.TeethL1, new AdvancedModelBox[] { this.TeethR1, this.TeethTL1 });
    }
}