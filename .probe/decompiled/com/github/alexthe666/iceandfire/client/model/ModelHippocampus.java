package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;

public class ModelHippocampus extends ModelDragonBase<EntityHippocampus> {

    public AdvancedModelBox Body;

    public AdvancedModelBox FrontThighR;

    public AdvancedModelBox FrontThighL;

    public AdvancedModelBox Neck;

    public AdvancedModelBox Tail_1;

    public AdvancedModelBox FinRBack;

    public AdvancedModelBox FinLBack;

    public AdvancedModelBox Saddle;

    public AdvancedModelBox FrontLegR;

    public AdvancedModelBox FrontFootR;

    public AdvancedModelBox FinR;

    public AdvancedModelBox FrontLegL;

    public AdvancedModelBox FrontFootL;

    public AdvancedModelBox FinL;

    public AdvancedModelBox Head;

    public AdvancedModelBox Mane;

    public AdvancedModelBox TopJaw;

    public AdvancedModelBox BottomJaw;

    public AdvancedModelBox NoseBand;

    public AdvancedModelBox ReinL;

    public AdvancedModelBox ReinR;

    public AdvancedModelBox Tail_2;

    public AdvancedModelBox Fin;

    public AdvancedModelBox Tail_3;

    public AdvancedModelBox FlukeR;

    public AdvancedModelBox FlukeL;

    public AdvancedModelBox StirrupR;

    public AdvancedModelBox ChestR;

    public AdvancedModelBox ChestL;

    public AdvancedModelBox Saddleback;

    public AdvancedModelBox SaddleFront;

    public AdvancedModelBox StirrupL;

    public AdvancedModelBox StirrupIronR;

    public AdvancedModelBox StirrupIronL;

    private final ModelAnimator animator = ModelAnimator.create();

    public ModelHippocampus() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.Body = new AdvancedModelBox(this, 0, 34);
        this.Body.setPos(0.0F, 11.0F, 9.0F);
        this.Body.addBox(-5.0F, -8.0F, -19.0F, 10.0F, 10.0F, 24.0F, 0.0F);
        this.Fin = new AdvancedModelBox(this, 57, 33);
        this.Fin.setPos(0.0F, 8.2F, -0.7F);
        this.Fin.addBox(0.0F, -10.1F, 3.7F, 1.0F, 18.0F, 5.0F, 0.0F);
        this.setRotateAngle(this.Fin, -0.091106184F, 0.0F, 0.0F);
        this.BottomJaw = new AdvancedModelBox(this, 24, 27);
        this.BottomJaw.setPos(0.0F, 0.3F, -5.5F);
        this.BottomJaw.addBox(-2.0F, 0.0F, -6.5F, 4.0F, 2.0F, 5.0F, 0.0F);
        this.Saddleback = new AdvancedModelBox(this, 80, 9);
        this.Saddleback.setPos(0.0F, 0.0F, 0.0F);
        this.Saddleback.addBox(-4.0F, -1.0F, 3.0F, 8.0F, 1.0F, 2.0F, 0.0F);
        this.NoseBand = new AdvancedModelBox(this, 85, 60);
        this.NoseBand.setPos(0.0F, 7.9F, -4.5F);
        this.NoseBand.addBox(-3.0F, -11.1F, -7.0F, 6.0F, 6.0F, 12.0F, 0.0F);
        this.FlukeL = new AdvancedModelBox(this, 106, 90);
        this.FlukeL.setPos(0.0F, 16.2F, 0.1F);
        this.FlukeL.addBox(-3.5F, -0.1F, -0.5F, 7.0F, 18.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.FlukeL, -0.034906585F, -0.08726646F, (float) (-Math.PI / 6));
        this.Saddle = new AdvancedModelBox(this, 80, 0);
        this.Saddle.setPos(0.0F, -8.9F, -7.0F);
        this.Saddle.addBox(-5.0F, 0.0F, -3.0F, 10.0F, 1.0F, 8.0F, 0.0F);
        this.FinL = new AdvancedModelBox(this, 80, 43);
        this.FinL.mirror = true;
        this.FinL.setPos(-0.5F, 1.0F, -0.7F);
        this.FinL.addBox(-0.5F, -7.6F, 2.2F, 1.0F, 9.0F, 5.0F, 0.0F);
        this.StirrupR = new AdvancedModelBox(this, 80, 0);
        this.StirrupR.setPos(-5.0F, 1.0F, 0.0F);
        this.StirrupR.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F);
        this.FrontThighL = new AdvancedModelBox(this, 96, 29);
        this.FrontThighL.mirror = true;
        this.FrontThighL.setPos(4.0F, -2.0F, -16.7F);
        this.FrontThighL.addBox(-2.5F, -2.0F, -2.5F, 4.0F, 9.0F, 5.0F, 0.0F);
        this.setRotateAngle(this.FrontThighL, 0.0F, 0.0F, -0.2268928F);
        this.FrontLegL = new AdvancedModelBox(this, 96, 43);
        this.FrontLegL.mirror = true;
        this.FrontLegL.setPos(0.0F, 7.0F, 0.0F);
        this.FrontLegL.addBox(-2.0F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FrontLegL, (float) (Math.PI / 5), 0.0F, 0.0F);
        this.Tail_3 = new AdvancedModelBox(this, 44, 102);
        this.Tail_3.setPos(0.0F, 10.4F, 0.1F);
        this.Tail_3.addBox(-3.0F, 0.0F, -1.9F, 6.0F, 16.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.Tail_3, 0.22759093F, 0.0F, 0.0F);
        this.FrontFootL = new AdvancedModelBox(this, 96, 51);
        this.FrontFootL.mirror = true;
        this.FrontFootL.setPos(0.0F, 5.0F, 0.0F);
        this.FrontFootL.addBox(-2.5F, 0.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.0F);
        this.FinRBack = new AdvancedModelBox(this, 80, 43);
        this.FinRBack.setPos(-3.1F, 0.0F, -0.7F);
        this.FinRBack.addBox(-2.0F, -4.5F, 0.4F, 1.0F, 6.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.FinRBack, (float) (-Math.PI / 6), (float) (-Math.PI / 18), (float) (Math.PI / 18));
        this.Mane = new AdvancedModelBox(this, 57, 33);
        this.Mane.setPos(0.0F, -5.6F, -0.3F);
        this.Mane.addBox(0.0F, -8.6F, 3.5F, 1.0F, 18.0F, 6.0F, 0.0F);
        this.StirrupIronL = new AdvancedModelBox(this, 74, 0);
        this.StirrupIronL.setPos(0.0F, 0.0F, 0.0F);
        this.StirrupIronL.addBox(-0.5F, 6.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F);
        this.ChestL = new AdvancedModelBox(this, 0, 47);
        this.ChestL.setPos(4.5F, 1.0F, 8.0F);
        this.ChestL.addBox(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.ChestL, 0.0F, (float) (Math.PI / 2), 0.0F);
        this.Head = new AdvancedModelBox(this, 0, 0);
        this.Head.setPos(0.0F, -10.6F, 2.7F);
        this.Head.addBox(-2.5F, -2.9F, -7.0F, 5.0F, 5.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Head, -0.091106184F, 0.0F, 0.0F);
        this.Neck = new AdvancedModelBox(this, 0, 12);
        this.Neck.setPos(0.0F, -4.4F, -16.3F);
        this.Neck.addBox(-2.05F, -13.1F, -4.0F, 4.0F, 14.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.Neck, 0.68294734F, 0.0F, 0.0F);
        this.FinR = new AdvancedModelBox(this, 80, 43);
        this.FinR.setPos(0.5F, 1.0F, -0.7F);
        this.FinR.addBox(-0.5F, -7.6F, 2.2F, 1.0F, 9.0F, 5.0F, 0.0F);
        this.FrontFootR = new AdvancedModelBox(this, 96, 51);
        this.FrontFootR.setPos(0.0F, 5.0F, 0.0F);
        this.FrontFootR.addBox(-1.5F, 0.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.0F);
        this.TopJaw = new AdvancedModelBox(this, 24, 18);
        this.TopJaw.setPos(0.0F, -0.1F, -5.6F);
        this.TopJaw.addBox(-2.0F, -2.6F, -7.0F, 4.0F, 3.0F, 6.0F, 0.0F);
        this.Tail_2 = new AdvancedModelBox(this, 37, 80);
        this.Tail_2.setPos(0.0F, 11.8F, 0.1F);
        this.Tail_2.addBox(-3.5F, 0.0F, -3.0F, 7.0F, 11.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.Tail_2, -0.091106184F, 0.0F, 0.0F);
        this.FlukeR = new AdvancedModelBox(this, 106, 90);
        this.FlukeR.mirror = true;
        this.FlukeR.setPos(0.0F, 16.2F, 0.1F);
        this.FlukeR.addBox(-3.5F, -0.1F, -0.5F, 7.0F, 18.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.FlukeR, -0.034906585F, 0.08726646F, (float) (Math.PI / 6));
        this.StirrupL = new AdvancedModelBox(this, 70, 0);
        this.StirrupL.setPos(5.0F, 1.0F, 0.0F);
        this.StirrupL.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F);
        this.FrontThighR = new AdvancedModelBox(this, 96, 29);
        this.FrontThighR.setPos(-4.0F, -2.0F, -16.7F);
        this.FrontThighR.addBox(-1.5F, -2.0F, -2.5F, 4.0F, 9.0F, 5.0F, 0.0F);
        this.setRotateAngle(this.FrontThighR, 0.0F, 0.0F, 0.2268928F);
        this.FinLBack = new AdvancedModelBox(this, 80, 43);
        this.FinLBack.mirror = true;
        this.FinLBack.setPos(3.1F, 0.0F, -0.7F);
        this.FinLBack.addBox(1.0F, -4.5F, 0.4F, 1.0F, 6.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.FinLBack, (float) (-Math.PI / 6), (float) (Math.PI / 18), (float) (-Math.PI / 18));
        this.ReinL = new AdvancedModelBox(this, 46, 55);
        this.ReinL.setPos(0.0F, 0.0F, 0.0F);
        this.ReinL.addBox(3.1F, -6.3F, -3.4F, 0.0F, 3.0F, 19.0F, 0.0F);
        this.setRotateAngle(this.ReinL, -0.04363323F, 0.0F, 0.0F);
        this.ReinR = new AdvancedModelBox(this, 46, 55);
        this.ReinR.mirror = true;
        this.ReinR.setPos(0.0F, 0.0F, 0.0F);
        this.ReinR.addBox(-3.1F, -6.0F, -3.4F, 0.0F, 3.0F, 19.0F, 0.0F);
        this.setRotateAngle(this.ReinR, -0.04363323F, 0.0F, 0.0F);
        this.Tail_1 = new AdvancedModelBox(this, 0, 80);
        this.Tail_1.setPos(0.0F, -3.3F, 2.5F);
        this.Tail_1.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 13.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.Tail_1, 1.5025539F, 0.0F, 0.0F);
        this.FrontLegR = new AdvancedModelBox(this, 96, 43);
        this.FrontLegR.setPos(0.0F, 7.0F, 0.0F);
        this.FrontLegR.addBox(-1.0F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FrontLegR, (float) (Math.PI / 5), 0.0F, 0.0F);
        this.SaddleFront = new AdvancedModelBox(this, 106, 9);
        this.SaddleFront.setPos(0.0F, 0.0F, 0.0F);
        this.SaddleFront.addBox(-1.5F, -1.0F, -3.0F, 3.0F, 1.0F, 2.0F, 0.0F);
        this.StirrupIronR = new AdvancedModelBox(this, 74, 4);
        this.StirrupIronR.setPos(0.0F, 0.0F, 0.0F);
        this.StirrupIronR.addBox(-0.5F, 6.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F);
        this.ChestR = new AdvancedModelBox(this, 0, 34);
        this.ChestR.setPos(-4.5F, 1.0F, 8.0F);
        this.ChestR.addBox(-3.0F, 0.0F, -3.0F, 8.0F, 8.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.ChestR, 0.0F, (float) (Math.PI / 2), 0.0F);
        this.Tail_1.addChild(this.Fin);
        this.Head.addChild(this.BottomJaw);
        this.Saddle.addChild(this.Saddleback);
        this.Head.addChild(this.NoseBand);
        this.Tail_3.addChild(this.FlukeL);
        this.Body.addChild(this.Saddle);
        this.FrontFootL.addChild(this.FinL);
        this.Saddle.addChild(this.StirrupR);
        this.Body.addChild(this.FrontThighL);
        this.FrontThighL.addChild(this.FrontLegL);
        this.Tail_2.addChild(this.Tail_3);
        this.FrontLegL.addChild(this.FrontFootL);
        this.Body.addChild(this.FinRBack);
        this.Neck.addChild(this.Mane);
        this.StirrupL.addChild(this.StirrupIronL);
        this.Saddle.addChild(this.ChestL);
        this.Neck.addChild(this.Head);
        this.Body.addChild(this.Neck);
        this.FrontFootR.addChild(this.FinR);
        this.FrontLegR.addChild(this.FrontFootR);
        this.Head.addChild(this.TopJaw);
        this.Tail_1.addChild(this.Tail_2);
        this.Tail_3.addChild(this.FlukeR);
        this.Saddle.addChild(this.StirrupL);
        this.Body.addChild(this.FrontThighR);
        this.Body.addChild(this.FinLBack);
        this.NoseBand.addChild(this.ReinL);
        this.NoseBand.addChild(this.ReinR);
        this.Body.addChild(this.Tail_1);
        this.FrontThighR.addChild(this.FrontLegR);
        this.Saddle.addChild(this.SaddleFront);
        this.StirrupR.addChild(this.StirrupIronR);
        this.Saddle.addChild(this.ChestR);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.Body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.Body, this.FrontThighR, this.FrontThighL, this.Neck, this.Tail_1, this.FinRBack, this.FinLBack, this.Saddle, this.FrontLegR, this.FrontFootR, this.FinR, this.FrontLegL, new AdvancedModelBox[] { this.FrontFootL, this.FinL, this.Head, this.Mane, this.TopJaw, this.BottomJaw, this.NoseBand, this.ReinL, this.ReinR, this.Tail_2, this.Fin, this.Tail_3, this.FlukeR, this.FlukeL, this.StirrupR, this.ChestR, this.ChestL, this.Saddleback, this.SaddleFront, this.StirrupL, this.StirrupIronR, this.StirrupIronL });
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        if (this.animator.setAnimation(EntityHippocampus.ANIMATION_SPEAK)) {
            this.animator.startKeyframe(10);
            this.rotate(this.animator, this.Head, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BottomJaw, 20.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
    }

    public void setupAnim(EntityHippocampus entity, float f, float f1, float f2, float f3, float f4) {
        this.animate(entity, f, f1, f2, f3, f4, 1.0F);
        if (this.f_102610_) {
            this.Body.setShouldScaleChildren(true);
            this.Body.setScale(0.5F, 0.5F, 0.5F);
            this.Head.setScale(1.5F, 1.5F, 1.5F);
            this.TopJaw.setPos(0.0F, -0.1F, -7.6F);
            this.Body.setPos(0.0F, 12.5F, 4.0F);
            this.BottomJaw.setPos(0.0F, 0.3F, -7.5F);
        } else {
            this.Body.setScale(1.0F, 1.0F, 1.0F);
            this.Head.setScale(1.0F, 1.0F, 1.0F);
        }
        float speed_walk = 0.9F;
        float speed_idle = 0.05F;
        float speed_swim = 0.35F;
        float degree_walk = 1.5F;
        float degree_idle = 0.5F;
        float degree_swim = 0.75F;
        this.progressRotation(this.Body, entity.onLandProgress, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.progressRotation(this.FrontThighL, Math.max(0.0F, entity.onLandProgress), 0.0F, 0.0F, (float) Math.toRadians(-65.0));
        this.progressRotation(this.FrontThighR, Math.max(0.0F, entity.onLandProgress), 0.0F, 0.0F, (float) Math.toRadians(65.0));
        this.progressPosition(this.Body, entity.onLandProgress, 0.0F, 20.0F, 0.0F);
        this.progressRotation(this.Body, entity.sitProgress, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.progressRotation(this.Tail_1, entity.sitProgress, (float) Math.toRadians(55.0), 0.0F, 0.0F);
        this.progressRotation(this.Tail_2, entity.sitProgress, (float) Math.toRadians(-26.0), 0.0F, 0.0F);
        this.progressRotation(this.Tail_3, entity.sitProgress, (float) Math.toRadians(-33.0), 0.0F, 0.0F);
        this.progressRotation(this.FlukeR, Math.max(0.0F, entity.sitProgress), (float) Math.toRadians(-50.0), (float) Math.toRadians(5.0), (float) Math.toRadians(30.0));
        this.progressRotation(this.FlukeL, Math.max(0.0F, entity.sitProgress), (float) Math.toRadians(-50.0), (float) Math.toRadians(-5.0), (float) Math.toRadians(-30.0));
        this.progressRotation(this.Body, entity.sitProgress * entity.onLandProgress * 0.05F, (float) Math.toRadians(-5.0), (float) Math.toRadians(-5.0), (float) Math.toRadians(85.0));
        this.progressPosition(this.Body, entity.sitProgress * entity.onLandProgress * 0.05F, 0.0F, 10.0F, 0.0F);
        if (entity.m_20096_() && !entity.m_20069_()) {
            this.progressRotation(this.FrontThighL, Math.max(0.0F, entity.sitProgress), 0.0F, 0.0F, (float) Math.toRadians(60.0));
            this.progressRotation(this.FrontThighR, Math.max(0.0F, entity.sitProgress), 0.0F, 0.0F, (float) Math.toRadians(-60.0));
        }
        this.progressRotation(this.Tail_2, entity.sitProgress * entity.onLandProgress * 0.05F, (float) Math.toRadians(-7.0), (float) Math.toRadians(-25.0), (float) Math.toRadians(1.0));
        this.progressRotation(this.Tail_3, entity.sitProgress * entity.onLandProgress * 0.05F, (float) Math.toRadians(20.0), (float) Math.toRadians(-36.0), (float) Math.toRadians(36.0));
        AdvancedModelBox[] TAIL = new AdvancedModelBox[] { this.Tail_1, this.Tail_2, this.Tail_3 };
        AdvancedModelBox[] TAIL_W_BODY = new AdvancedModelBox[] { this.Body, this.Tail_1, this.Tail_2, this.Tail_3 };
        AdvancedModelBox[] LEG_L = new AdvancedModelBox[] { this.FrontThighL, this.FrontLegL };
        AdvancedModelBox[] LEG_R = new AdvancedModelBox[] { this.FrontThighR, this.FrontLegR };
        AdvancedModelBox[] NECK = new AdvancedModelBox[] { this.Neck, this.Head };
        if (entity.m_20069_()) {
            this.chainWave(NECK, speed_swim, degree_swim * 0.15F, -2.0, f, f1);
            this.chainWave(TAIL_W_BODY, speed_swim, degree_swim * 0.15F, -3.0, f, f1);
            this.walk(this.Tail_3, speed_swim, degree_swim * -0.5F, false, 0.0F, 0.0F, f, f1);
            this.chainWave(LEG_L, speed_swim, degree_swim * 0.75F, 1.0, f, f1);
            this.chainWave(LEG_R, speed_swim, degree_swim * 0.75F, 1.0, f, f1);
            this.walk(this.Tail_1, speed_idle, degree_idle * 0.15F, false, 0.0F, 0.0F, f2, 1.0F);
            this.walk(this.Tail_2, speed_idle, degree_idle * 0.25F, false, 0.0F, 0.0F, f2, 1.0F);
        } else {
            this.chainWave(LEG_L, speed_walk, degree_walk * 0.5F, 1.0, f, f1);
            this.chainWave(LEG_R, speed_walk, degree_walk * 0.5F, 1.0, f, f1);
            this.walk(this.Body, speed_walk, degree_walk * 0.05F, false, 0.0F, 0.1F, f, f1);
            this.walk(this.Tail_1, speed_walk, degree_walk * 0.05F, false, 1.0F, 0.1F, f, f1);
            this.walk(this.Tail_2, speed_walk, degree_walk * 0.05F, false, 2.0F, 0.1F, f, f1);
            this.walk(this.FrontThighL, speed_idle, degree_idle * 0.25F, false, 0.0F, -0.1F, f2, 1.0F);
            this.walk(this.FrontThighR, speed_idle, degree_idle * 0.25F, false, 0.0F, -0.1F, f2, 1.0F);
            this.walk(this.FrontLegL, speed_idle, degree_idle * 0.25F, false, 0.0F, -0.1F, f2, 1.0F);
            this.walk(this.FrontLegR, speed_idle, degree_idle * 0.25F, false, 0.0F, -0.1F, f2, 1.0F);
            this.swing(this.FinLBack, speed_idle, degree_idle * 0.25F, false, 0.0F, -0.1F, f2, 1.0F);
            this.swing(this.FinRBack, speed_idle, degree_idle * 0.25F, true, 0.0F, -0.1F, f2, 1.0F);
        }
        this.chainWave(NECK, speed_idle, degree_idle * 0.15F, -2.0, f2, 1.0F);
        if (entity.tail_buffer != null) {
            entity.tail_buffer.applyChainSwingBuffer(TAIL);
        }
    }

    @Override
    public void renderStatue(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.m_7695_(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.NoseBand.showModel = false;
        this.ReinL.showModel = false;
        this.ReinR.showModel = false;
        this.ChestL.showModel = false;
        this.ChestR.showModel = false;
        this.Saddle.showModel = false;
        this.Saddleback.showModel = false;
        this.StirrupIronL.showModel = false;
        this.StirrupIronR.showModel = false;
        this.SaddleFront.showModel = false;
        this.StirrupL.showModel = false;
        this.StirrupR.showModel = false;
    }
}