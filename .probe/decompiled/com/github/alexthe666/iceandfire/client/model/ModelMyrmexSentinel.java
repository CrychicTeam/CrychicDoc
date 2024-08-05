package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexSentinel;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;

public class ModelMyrmexSentinel extends ModelMyrmexBase {

    public AdvancedModelBox Body2;

    public AdvancedModelBox Body3;

    public AdvancedModelBox Body1;

    public AdvancedModelBox legTopR2;

    public AdvancedModelBox legTopR2_1;

    public AdvancedModelBox legTopR3;

    public AdvancedModelBox legTopR3_1;

    public AdvancedModelBox Tail1;

    public AdvancedModelBox legMidR3;

    public AdvancedModelBox legBottomR3;

    public AdvancedModelBox legMidR3_1;

    public AdvancedModelBox legBottomR3_1;

    public AdvancedModelBox Tail2;

    public AdvancedModelBox Tail3;

    public AdvancedModelBox Tail4;

    public AdvancedModelBox Tail5;

    public AdvancedModelBox Tail6;

    public AdvancedModelBox Tail7;

    public AdvancedModelBox Tail8;

    public AdvancedModelBox Tail9;

    public AdvancedModelBox Stinger;

    public AdvancedModelBox Neck1;

    public AdvancedModelBox legTopR1;

    public AdvancedModelBox legTopR1_1;

    public AdvancedModelBox HeadBase;

    public AdvancedModelBox EyeR;

    public AdvancedModelBox MandibleL;

    public AdvancedModelBox MandibleR;

    public AdvancedModelBox EyeL;

    public AdvancedModelBox legMidR1;

    public AdvancedModelBox legBottomR1;

    public AdvancedModelBox legMidR1_1;

    public AdvancedModelBox legBottomR1_1;

    public AdvancedModelBox legMidR2;

    public AdvancedModelBox legBottomR2;

    public AdvancedModelBox legMidR2_1;

    public AdvancedModelBox legBottomR2_1;

    private final ModelAnimator animator;

    public ModelMyrmexSentinel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.MandibleR = new AdvancedModelBox(this, 0, 25);
        this.MandibleR.mirror = true;
        this.MandibleR.setPos(-3.4F, 3.7F, -7.7F);
        this.MandibleR.addBox(-2.0F, -2.51F, -5.1F, 4.0F, 2.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.MandibleR, (float) (Math.PI / 18), -0.18203785F, 0.0F);
        this.legTopR2_1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR2_1.mirror = true;
        this.legTopR2_1.setPos(-3.3F, 1.0F, 1.6F);
        this.legTopR2_1.addBox(-1.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.legTopR2_1, (float) (-Math.PI / 6), (float) (-Math.PI / 6), (float) (Math.PI * 2.0 / 9.0));
        this.MandibleL = new AdvancedModelBox(this, 0, 25);
        this.MandibleL.setPos(3.4F, 3.7F, -7.7F);
        this.MandibleL.addBox(-2.0F, -2.51F, -5.1F, 4.0F, 2.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.MandibleL, (float) (Math.PI / 18), 0.18203785F, 0.0F);
        this.legTopR1 = new AdvancedModelBox(this, 0, 75);
        this.legTopR1.setPos(-3.3F, 0.0F, -8.4F);
        this.legTopR1.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 17.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.legTopR1, 1.2217305F, 0.0F, 0.18203785F);
        this.legBottomR1_1 = new AdvancedModelBox(this, 0, 98);
        this.legBottomR1_1.setPos(0.0F, 17.4F, 0.0F);
        this.legBottomR1_1.addBox(-1.5F, 0.0F, -2.9F, 3.0F, 20.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.legBottomR1_1, 2.9670596F, 0.0F, 0.0F);
        this.legMidR1_1 = new AdvancedModelBox(this, 12, 75);
        this.legMidR1_1.setPos(0.0F, 15.4F, 0.1F);
        this.legMidR1_1.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 17.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.legMidR1_1, (float) (-Math.PI * 8.0 / 9.0), 0.0F, 0.0F);
        this.legTopR1_1 = new AdvancedModelBox(this, 0, 75);
        this.legTopR1_1.mirror = true;
        this.legTopR1_1.setPos(3.3F, 0.0F, -8.4F);
        this.legTopR1_1.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 17.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.legTopR1_1, 1.2217305F, 0.0F, -0.18203785F);
        this.EyeL = new AdvancedModelBox(this, 39, 0);
        this.EyeL.setPos(4.0F, -0.3F, -3.5F);
        this.EyeL.addBox(-1.5F, -1.0F, -3.5F, 3.0F, 2.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.EyeL, 0.2268928F, -0.08726646F, (float) (Math.PI / 2));
        this.legBottomR3 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR3.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR3.addBox(-1.01F, 0.0F, -0.9F, 2.0F, 16.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.legBottomR3, 0.0F, 0.0F, -1.3203416F);
        this.legTopR3_1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR3_1.setPos(3.3F, 1.0F, 1.6F);
        this.legTopR3_1.addBox(-1.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.legTopR3_1, 0.5009095F, -0.22759093F, -0.6457718F);
        this.Body2 = new AdvancedModelBox(this, 91, 57);
        this.Body2.setPos(0.0F, 4.0F, -6.0F);
        this.Body2.addBox(-3.0F, -2.7F, -1.1F, 6.0F, 7.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.Body2, -0.045553092F, 0.0F, 0.0F);
        this.legBottomR3_1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR3_1.mirror = true;
        this.legBottomR3_1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR3_1.addBox(-1.01F, 0.0F, -0.9F, 2.0F, 16.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.legBottomR3_1, 0.0F, 0.0F, 1.3203416F);
        this.Neck1 = new AdvancedModelBox(this, 32, 22);
        this.Neck1.setPos(0.0F, -2.0F, -13.0F);
        this.Neck1.addBox(-2.5F, -2.0F, -5.5F, 5.0F, 5.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Neck1, 0.3642502F, 0.0F, 0.0F);
        this.Tail6 = new AdvancedModelBox(this, 100, 20);
        this.Tail6.setPos(0.0F, 0.7F, 3.2F);
        this.Tail6.addBox(-2.5F, -3.5F, -0.1F, 5.0F, 6.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Tail6, 0.18203785F, 0.0F, 0.0F);
        this.legMidR1 = new AdvancedModelBox(this, 12, 75);
        this.legMidR1.setPos(0.0F, 15.4F, 0.1F);
        this.legMidR1.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 17.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.legMidR1, (float) (-Math.PI * 8.0 / 9.0), 0.0F, 0.0F);
        this.legBottomR2_1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR2_1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR2_1.addBox(-1.01F, 0.0F, -0.9F, 2.0F, 16.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.legBottomR2_1, 0.0F, 0.0F, -1.3203416F);
        this.Body1 = new AdvancedModelBox(this, 34, 47);
        this.Body1.setPos(0.0F, -3.7F, -4.0F);
        this.Body1.addBox(-3.5F, -4.1F, -12.8F, 7.0F, 9.0F, 20.0F, 0.0F);
        this.setRotateAngle(this.Body1, -0.8196066F, 0.0F, 0.0F);
        this.Tail7 = new AdvancedModelBox(this, 65, 35);
        this.Tail7.setPos(0.0F, -1.0F, 6.4F);
        this.Tail7.addBox(-2.0F, -2.2F, -1.3F, 4.0F, 5.0F, 5.0F, 0.0F);
        this.setRotateAngle(this.Tail7, 0.4098033F, 0.0F, 0.0F);
        this.legBottomR1 = new AdvancedModelBox(this, 0, 98);
        this.legBottomR1.setPos(0.0F, 17.4F, 0.0F);
        this.legBottomR1.addBox(-1.02F, 0.0F, -2.9F, 3.0F, 20.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.legBottomR1, 2.9670596F, 0.0F, 0.0F);
        this.Tail2 = new AdvancedModelBox(this, 100, 20);
        this.Tail2.setPos(0.0F, 0.7F, 3.2F);
        this.Tail2.addBox(-2.5F, -3.5F, -0.1F, 5.0F, 6.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Tail2, 0.4553564F, 0.0F, 0.0F);
        this.EyeR = new AdvancedModelBox(this, 39, 0);
        this.EyeR.mirror = true;
        this.EyeR.setPos(-4.0F, -0.3F, -3.5F);
        this.EyeR.addBox(-1.5F, -1.0F, -3.5F, 3.0F, 2.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.EyeR, 0.2268928F, 0.08726646F, (float) (-Math.PI / 2));
        this.legTopR3 = new AdvancedModelBox(this, 0, 54);
        this.legTopR3.mirror = true;
        this.legTopR3.setPos(-3.3F, 1.0F, 1.6F);
        this.legTopR3.addBox(-1.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.legTopR3, 0.5009095F, 0.22759093F, 0.6457718F);
        this.Stinger = new AdvancedModelBox(this, 60, 0);
        this.Stinger.setPos(0.0F, -0.4F, 6.0F);
        this.Stinger.addBox(-1.0F, -2.7F, -1.7F, 2.0F, 10.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.Stinger, 2.8684487F, 0.0F, 0.0F);
        this.legMidR2_1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR2_1.setPos(0.0F, 8.4F, 0.1F);
        this.legMidR2_1.addBox(-1.5F, 0.0F, -1.0F, 3.0F, 12.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.legMidR2_1, 0.0F, 0.0F, 1.1383038F);
        this.legMidR3 = new AdvancedModelBox(this, 11, 50);
        this.legMidR3.setPos(0.0F, 8.4F, 0.1F);
        this.legMidR3.addBox(-1.5F, 0.0F, -1.0F, 3.0F, 12.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.legMidR3, 0.0F, 0.0F, 1.1383038F);
        this.Tail8 = new AdvancedModelBox(this, 80, 38);
        this.Tail8.setPos(0.0F, 0.6F, 1.2F);
        this.Tail8.addBox(-5.5F, -3.2F, 0.9F, 11.0F, 7.0F, 11.0F, 0.0F);
        this.setRotateAngle(this.Tail8, 0.68294734F, 0.0F, 0.0F);
        this.legTopR2 = new AdvancedModelBox(this, 0, 54);
        this.legTopR2.setPos(3.3F, 1.0F, 1.6F);
        this.legTopR2.addBox(-1.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.legTopR2, (float) (-Math.PI / 6), (float) (Math.PI / 6), (float) (-Math.PI * 2.0 / 9.0));
        this.Tail3 = new AdvancedModelBox(this, 65, 35);
        this.Tail3.setPos(0.0F, -1.0F, 6.4F);
        this.Tail3.addBox(-2.0F, -2.2F, -1.3F, 4.0F, 5.0F, 5.0F, 0.0F);
        this.setRotateAngle(this.Tail3, 0.68294734F, 0.0F, 0.0F);
        this.legBottomR2 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR2.mirror = true;
        this.legBottomR2.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR2.addBox(-1.01F, 0.0F, -0.9F, 2.0F, 16.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.legBottomR2, 0.0F, 0.0F, 1.3203416F);
        this.Tail1 = new AdvancedModelBox(this, 65, 35);
        this.Tail1.setPos(0.0F, -0.4F, 6.4F);
        this.Tail1.addBox(-2.0F, -2.2F, -0.1F, 4.0F, 5.0F, 5.0F, 0.0F);
        this.setRotateAngle(this.Tail1, -0.045553092F, 0.0F, 0.0F);
        this.HeadBase = new AdvancedModelBox(this, 0, 0);
        this.HeadBase.setPos(0.0F, -1.1F, -4.4F);
        this.HeadBase.addBox(-4.0F, -2.51F, -10.1F, 8.0F, 6.0F, 10.0F, 0.0F);
        this.setRotateAngle(this.HeadBase, 1.0927507F, 0.0F, 0.0F);
        this.Tail5 = new AdvancedModelBox(this, 65, 35);
        this.Tail5.setPos(0.0F, -1.0F, 6.4F);
        this.Tail5.addBox(-2.0F, -2.2F, -1.3F, 4.0F, 5.0F, 5.0F, 0.0F);
        this.setRotateAngle(this.Tail5, 0.4098033F, 0.0F, 0.0F);
        this.legMidR2 = new AdvancedModelBox(this, 11, 50);
        this.legMidR2.mirror = true;
        this.legMidR2.setPos(0.0F, 8.4F, 0.1F);
        this.legMidR2.addBox(-1.5F, 0.0F, -1.0F, 3.0F, 12.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.legMidR2, 0.0F, 0.0F, -1.1383038F);
        this.Tail4 = new AdvancedModelBox(this, 100, 20);
        this.Tail4.setPos(0.0F, 0.7F, 3.2F);
        this.Tail4.addBox(-2.5F, -3.5F, -0.1F, 5.0F, 6.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Tail4, 0.27314404F, 0.0F, 0.0F);
        this.legMidR3_1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR3_1.mirror = true;
        this.legMidR3_1.setPos(0.0F, 8.4F, 0.1F);
        this.legMidR3_1.addBox(-1.5F, 0.0F, -1.0F, 3.0F, 12.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.legMidR3_1, 0.0F, 0.0F, -1.1383038F);
        this.Tail9 = new AdvancedModelBox(this, 60, 17);
        this.Tail9.setPos(0.0F, -0.4F, 9.0F);
        this.Tail9.addBox(-4.0F, -2.7F, -0.1F, 8.0F, 6.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Tail9, 0.18203785F, 0.0F, 0.0F);
        this.Body3 = new AdvancedModelBox(this, 36, 76);
        this.Body3.setPos(0.0F, 0.2F, 4.1F);
        this.Body3.addBox(-4.5F, -3.4F, -1.4F, 9.0F, 9.0F, 9.0F, 0.0F);
        this.HeadBase.addChild(this.MandibleR);
        this.Body2.addChild(this.legTopR2_1);
        this.HeadBase.addChild(this.MandibleL);
        this.Body1.addChild(this.legTopR1);
        this.legMidR1_1.addChild(this.legBottomR1_1);
        this.legTopR1_1.addChild(this.legMidR1_1);
        this.Body1.addChild(this.legTopR1_1);
        this.HeadBase.addChild(this.EyeL);
        this.legMidR3.addChild(this.legBottomR3);
        this.Body3.addChild(this.legTopR3_1);
        this.legMidR3_1.addChild(this.legBottomR3_1);
        this.Body1.addChild(this.Neck1);
        this.Tail5.addChild(this.Tail6);
        this.legTopR1.addChild(this.legMidR1);
        this.legMidR2_1.addChild(this.legBottomR2_1);
        this.Body2.addChild(this.Body1);
        this.Tail6.addChild(this.Tail7);
        this.legMidR1.addChild(this.legBottomR1);
        this.Tail1.addChild(this.Tail2);
        this.HeadBase.addChild(this.EyeR);
        this.Body3.addChild(this.legTopR3);
        this.Tail9.addChild(this.Stinger);
        this.legTopR2_1.addChild(this.legMidR2_1);
        this.legTopR3.addChild(this.legMidR3);
        this.Tail7.addChild(this.Tail8);
        this.Body2.addChild(this.legTopR2);
        this.Tail2.addChild(this.Tail3);
        this.legMidR2.addChild(this.legBottomR2);
        this.Body3.addChild(this.Tail1);
        this.Neck1.addChild(this.HeadBase);
        this.Tail4.addChild(this.Tail5);
        this.legTopR2.addChild(this.legMidR2);
        this.Tail3.addChild(this.Tail4);
        this.legTopR3_1.addChild(this.legMidR3_1);
        this.Tail8.addChild(this.Tail9);
        this.Body2.addChild(this.Body3);
        this.animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.Body2);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.Body2, this.Body3, this.Body1, this.legTopR2, this.legTopR2_1, this.legTopR3, this.legTopR3_1, this.Tail1, this.legMidR3, this.legBottomR3, this.legMidR3_1, this.legBottomR3_1, new AdvancedModelBox[] { this.Tail2, this.Tail3, this.Tail4, this.Tail5, this.Tail6, this.Tail7, this.Tail8, this.Tail9, this.Stinger, this.Neck1, this.legTopR1, this.legTopR1_1, this.HeadBase, this.EyeR, this.MandibleL, this.MandibleR, this.EyeL, this.legMidR1, this.legBottomR1, this.legMidR1_1, this.legBottomR1_1, this.legMidR2, this.legBottomR2, this.legMidR2_1, this.legBottomR2_1 });
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        if (this.animator.setAnimation(EntityMyrmexSentinel.ANIMATION_GRAB)) {
            this.animator.startKeyframe(5);
            ModelUtils.rotateFrom(this.animator, this.Body1, -65.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Neck1, 0.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.legTopR1, -14.0F, 5.0F, 10.0F);
            ModelUtils.rotateFrom(this.animator, this.legTopR1_1, -14.0F, -5.0F, -10.0F);
            ModelUtils.rotateFrom(this.animator, this.legMidR1, -41.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.legMidR1_1, -41.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.legBottomR1, 67.0F, 20.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.legBottomR1_1, 67.0F, -20.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityMyrmexSentinel.ANIMATION_STING)) {
            this.animator.startKeyframe(5);
            ModelUtils.rotateFrom(this.animator, this.Body1, -36.0F, -15.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Body2, 0.0F, -31.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Body3, 0.0F, -13.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail1, 20.0F, 31.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail2, 52.0F, 15.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail3, 57.0F, 2.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail4, 18.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail5, 23.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail6, 5.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail7, 23.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail8, 0.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail9, 10.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Stinger, 107.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            ModelUtils.rotateFrom(this.animator, this.Body1, -36.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            ModelUtils.rotateFrom(this.animator, this.Body1, -36.0F, 15.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Body2, 0.0F, 31.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Body3, 0.0F, 13.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail1, 20.0F, -31.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail2, 52.0F, -15.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail3, 57.0F, -2.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail4, 18.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail5, 23.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail6, 5.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail7, 23.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail8, 0.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Tail9, 10.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Stinger, 107.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(10);
        }
        if (this.animator.setAnimation(EntityMyrmexSentinel.ANIMATION_NIBBLE)) {
            this.animator.startKeyframe(5);
            ModelUtils.rotate(this.animator, this.Neck1, -50.0F, 0.0F, 0.0F);
            ModelUtils.rotate(this.animator, this.HeadBase, 50.0F, 0.0F, 0.0F);
            ModelUtils.rotate(this.animator, this.MandibleR, 0.0F, 35.0F, 0.0F);
            ModelUtils.rotate(this.animator, this.MandibleL, 0.0F, -35.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            ModelUtils.rotate(this.animator, this.Neck1, 30.0F, 0.0F, 0.0F);
            ModelUtils.rotate(this.animator, this.HeadBase, -30.0F, 0.0F, 0.0F);
            ModelUtils.rotate(this.animator, this.MandibleR, 0.0F, -50.0F, 0.0F);
            ModelUtils.rotate(this.animator, this.MandibleL, 0.0F, 50.0F, 0.0F);
            this.animator.endKeyframe();
        }
        if (this.animator.setAnimation(EntityMyrmexSentinel.ANIMATION_SLASH)) {
            this.animator.startKeyframe(5);
            ModelUtils.rotateFrom(this.animator, this.Body1, -65.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Neck1, 0.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.legTopR1, 24.0F, 20.0F, 30.0F);
            ModelUtils.rotateFrom(this.animator, this.legMidR1, -98.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.legBottomR1, 70.0F, 20.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            ModelUtils.rotateFrom(this.animator, this.Body1, -65.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Neck1, 0.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.legTopR1_1, 24.0F, -20.0F, -30.0F);
            ModelUtils.rotateFrom(this.animator, this.legMidR1_1, -98.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.legBottomR1_1, 70.0F, -20.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            ModelUtils.rotateFrom(this.animator, this.Body1, -65.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Neck1, 0.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.legTopR1, 24.0F, 20.0F, 30.0F);
            ModelUtils.rotateFrom(this.animator, this.legMidR1, -98.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.legBottomR1, 70.0F, 20.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            ModelUtils.rotateFrom(this.animator, this.Body1, -65.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.Neck1, 0.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.legTopR1_1, 24.0F, -20.0F, -30.0F);
            ModelUtils.rotateFrom(this.animator, this.legMidR1_1, -98.0F, 0.0F, 0.0F);
            ModelUtils.rotateFrom(this.animator, this.legBottomR1_1, 70.0F, -20.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
    }

    @Override
    public void setupAnim(Entity entity, float f, float f1, float f2, float f3, float f4) {
        this.animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, 1.0F);
        EntityMyrmexSentinel myrmex = (EntityMyrmexSentinel) entity;
        AdvancedModelBox[] TAIL = new AdvancedModelBox[] { this.Tail1, this.Tail2, this.Tail3, this.Tail4, this.Tail5, this.Tail6, this.Tail7, this.Tail8, this.Tail9, this.Stinger };
        AdvancedModelBox[] NECK = new AdvancedModelBox[] { this.Neck1, this.HeadBase };
        AdvancedModelBox[] ARML1 = new AdvancedModelBox[] { this.legTopR1, this.legMidR1, this.legBottomR1 };
        AdvancedModelBox[] LEGR2 = new AdvancedModelBox[] { this.legTopR2, this.legMidR2, this.legBottomR2 };
        AdvancedModelBox[] LEGR3 = new AdvancedModelBox[] { this.legTopR3, this.legMidR3, this.legBottomR3 };
        AdvancedModelBox[] ARMR1 = new AdvancedModelBox[] { this.legTopR1_1, this.legMidR1_1, this.legBottomR1_1 };
        AdvancedModelBox[] LEGL2 = new AdvancedModelBox[] { this.legTopR2_1, this.legMidR2_1, this.legBottomR2_1 };
        AdvancedModelBox[] LEGL3 = new AdvancedModelBox[] { this.legTopR3_1, this.legMidR3_1, this.legBottomR3_1 };
        float speed_walk = 0.9F;
        float speed_idle = myrmex.isHiding() ? 0.015F : 0.035F;
        float degree_walk = 0.5F;
        float degree_idle = myrmex.isHiding() ? 0.1F : 0.25F;
        if (entity.getPassengers().isEmpty()) {
            this.faceTarget(f3, f4, 2.0F, NECK);
        }
        this.chainWave(TAIL, speed_idle, degree_idle * 0.15F, 0.0, f2, 1.0F);
        this.chainWave(NECK, speed_idle, degree_idle * -0.15F, 2.0, f2, 1.0F);
        this.swing(this.MandibleR, speed_idle * 2.0F, degree_idle * -0.75F, false, 1.0F, 0.2F, f2, 1.0F);
        this.swing(this.MandibleL, speed_idle * 2.0F, degree_idle * -0.75F, true, 1.0F, 0.2F, f2, 1.0F);
        this.animateLeg(LEGR3, speed_walk, degree_walk, false, 0.0F, 1.0F, f, f1);
        this.animateLeg(LEGR2, speed_walk, degree_walk, true, 0.0F, 1.0F, f, f1);
        this.animateLeg(LEGL3, speed_walk, degree_walk, false, 1.0F, -1.0F, f, f1);
        this.animateLeg(LEGL2, speed_walk, degree_walk, true, 1.0F, -1.0F, f, f1);
        this.bob(this.Body2, speed_walk, degree_walk, false, f, f1);
        this.chainWave(ARML1, speed_idle, degree_idle * -0.25F, 0.0, f2, 1.0F);
        this.chainWave(ARMR1, speed_idle, degree_idle * -0.25F, 0.0, f2, 1.0F);
        this.progressRotation(this.legTopR1, myrmex.holdingProgress, (float) Math.toRadians(35.0), (float) Math.toRadians(30.0), (float) Math.toRadians(10.0));
        this.progressRotation(this.legTopR1_1, myrmex.holdingProgress, (float) Math.toRadians(35.0), (float) Math.toRadians(-30.0), (float) Math.toRadians(-10.0));
        this.progressRotation(this.legMidR1, myrmex.holdingProgress, (float) Math.toRadians(-133.0), 0.0F, 0.0F);
        this.progressRotation(this.legMidR1_1, myrmex.holdingProgress, (float) Math.toRadians(-133.0), 0.0F, 0.0F);
        this.progressRotation(this.legBottomR1, myrmex.holdingProgress, (float) Math.toRadians(140.0), (float) Math.toRadians(20.0), 0.0F);
        this.progressRotation(this.legBottomR1_1, myrmex.holdingProgress, (float) Math.toRadians(140.0), (float) Math.toRadians(-20.0), 0.0F);
        this.progressRotation(this.legTopR1, myrmex.hidingProgress, (float) Math.toRadians(70.0), 0.0F, (float) Math.toRadians(70.0));
        this.progressRotation(this.legTopR1_1, myrmex.hidingProgress, (float) Math.toRadians(70.0), 0.0F, (float) Math.toRadians(-70.0));
        this.progressRotation(this.Body1, myrmex.hidingProgress, (float) Math.toRadians(-2.0), 0.0F, 0.0F);
        this.progressPosition(this.Body1, myrmex.hidingProgress, 0.0F, 1.7F, -4.0F);
        this.progressPosition(this.Body2, myrmex.hidingProgress, 0.0F, 17.0F, 0.0F);
        this.progressRotation(this.Body2, myrmex.hidingProgress, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.progressRotation(this.legTopR2, myrmex.hidingProgress, (float) Math.toRadians(55.0), (float) Math.toRadians(30.0), (float) Math.toRadians(-7.0));
        this.progressRotation(this.legTopR2_1, myrmex.hidingProgress, (float) Math.toRadians(55.0), (float) Math.toRadians(-30.0), (float) Math.toRadians(7.0));
        this.progressRotation(this.legTopR3, myrmex.hidingProgress, (float) Math.toRadians(45.0), (float) Math.toRadians(10.0), (float) Math.toRadians(40.0));
        this.progressRotation(this.legTopR3_1, myrmex.hidingProgress, (float) Math.toRadians(45.0), (float) Math.toRadians(-10.0), (float) Math.toRadians(-40.0));
        this.progressRotation(this.legMidR2, myrmex.hidingProgress, 0.0F, 0.0F, (float) Math.toRadians(-140.0));
        this.progressRotation(this.legMidR2_1, myrmex.hidingProgress, 0.0F, 0.0F, (float) Math.toRadians(140.0));
        this.progressRotation(this.legMidR3, myrmex.hidingProgress, 0.0F, 0.0F, (float) Math.toRadians(146.0));
        this.progressRotation(this.legMidR3_1, myrmex.hidingProgress, 0.0F, 0.0F, (float) Math.toRadians(-146.0));
        this.progressRotation(this.legBottomR2, myrmex.hidingProgress, 0.0F, 0.0F, (float) Math.toRadians(146.0));
        this.progressRotation(this.legBottomR2_1, myrmex.hidingProgress, 0.0F, 0.0F, (float) Math.toRadians(-146.0));
        this.progressRotation(this.legBottomR3, myrmex.hidingProgress, 0.0F, 0.0F, (float) Math.toRadians(-156.0));
        this.progressRotation(this.legBottomR3_1, myrmex.hidingProgress, 0.0F, 0.0F, (float) Math.toRadians(156.0));
        this.progressRotation(this.HeadBase, myrmex.hidingProgress, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.progressPosition(this.HeadBase, myrmex.hidingProgress, 0.0F, 0.0F, -4.4F);
        this.progressRotation(this.Tail1, myrmex.hidingProgress, (float) Math.toRadians(-20.0), 0.0F, (float) Math.toRadians(-46.0));
        this.progressRotation(this.Tail2, myrmex.hidingProgress, (float) Math.toRadians(26.0), 0.0F, 0.0F);
        this.progressRotation(this.Tail3, myrmex.hidingProgress, (float) Math.toRadians(40.0), 0.0F, (float) Math.toRadians(-30.0));
        this.progressRotation(this.Tail4, myrmex.hidingProgress, (float) Math.toRadians(25.0), 0.0F, (float) Math.toRadians(-18.0));
        this.progressRotation(this.Tail5, myrmex.hidingProgress, (float) Math.toRadians(23.0), 0.0F, 0.0F);
        this.progressRotation(this.Tail6, myrmex.hidingProgress, (float) Math.toRadians(10.0), (float) Math.toRadians(-15.0), (float) Math.toRadians(33.0));
        this.progressRotation(this.Tail7, myrmex.hidingProgress, (float) Math.toRadians(23.0), 0.0F, 0.0F);
        this.progressRotation(this.Tail8, myrmex.hidingProgress, (float) Math.toRadians(-20.0), (float) Math.toRadians(-45.0), (float) Math.toRadians(85.0));
    }

    private void animateLeg(AdvancedModelBox[] models, float speed, float degree, boolean reverse, float offset, float weight, float f, float f1) {
        this.flap(models[0], speed, degree * 0.4F, reverse, offset, weight * 0.2F, f, f1);
        this.flap(models[1], speed, degree * 2.0F, reverse, offset, weight * -0.4F, f, f1);
        this.flap(models[1], speed, -degree * 1.2F, reverse, offset, weight * 0.5F, f, f1);
        this.walk(models[0], speed, degree, reverse, offset, 0.0F, f, f1);
    }

    @Override
    public BasicModelPart[] getHeadParts() {
        return new BasicModelPart[] { this.Neck1, this.HeadBase };
    }

    @Override
    public void renderStatue(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.m_7695_(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}