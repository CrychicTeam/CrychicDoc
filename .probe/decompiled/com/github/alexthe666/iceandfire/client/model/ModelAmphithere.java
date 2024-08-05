package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;

public class ModelAmphithere extends ModelDragonBase<EntityAmphithere> {

    public AdvancedModelBox BodyUpper;

    public AdvancedModelBox BodyLower;

    public AdvancedModelBox Neck1;

    public AdvancedModelBox WingL;

    public AdvancedModelBox WingR;

    public AdvancedModelBox Tail1;

    public AdvancedModelBox Tail2;

    public AdvancedModelBox Tail3;

    public AdvancedModelBox Tail4;

    public AdvancedModelBox Club;

    public AdvancedModelBox TailR1;

    public AdvancedModelBox TailL1;

    public AdvancedModelBox TailL2;

    public AdvancedModelBox TailR2;

    public AdvancedModelBox Neck2;

    public AdvancedModelBox Neck3;

    public AdvancedModelBox Head;

    public AdvancedModelBox HeadFront;

    public AdvancedModelBox Jaw;

    public AdvancedModelBox CrestL1;

    public AdvancedModelBox CrestL2;

    public AdvancedModelBox CrestR2;

    public AdvancedModelBox CrestR1;

    public AdvancedModelBox CrestR3;

    public AdvancedModelBox CrestL3;

    public AdvancedModelBox CrestRB;

    public AdvancedModelBox CrestLB;

    public AdvancedModelBox Beak;

    public AdvancedModelBox Teeth2;

    public AdvancedModelBox Teeth1;

    public AdvancedModelBox WingL2;

    public AdvancedModelBox WingL3;

    public AdvancedModelBox WingL21;

    public AdvancedModelBox FingerL1;

    public AdvancedModelBox FingerL2;

    public AdvancedModelBox FingerL3;

    public AdvancedModelBox FingerL4;

    public AdvancedModelBox WingR2;

    public AdvancedModelBox WingR3;

    public AdvancedModelBox WingR21;

    public AdvancedModelBox FingerR1;

    public AdvancedModelBox FingerR2;

    public AdvancedModelBox FingerR3;

    public AdvancedModelBox FingerR4;

    private final ModelAnimator animator;

    public ModelAmphithere() {
        this.texWidth = 256;
        this.texHeight = 128;
        this.FingerR3 = new AdvancedModelBox(this, 40, 80);
        this.FingerR3.mirror = true;
        this.FingerR3.setPos(0.0F, 11.0F, 4.5F);
        this.FingerR3.addBox(-0.2F, -0.1F, -2.0F, 1.0F, 16.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerR3, 0.13962634F, 0.0F, 0.0F);
        this.TailR1 = new AdvancedModelBox(this, 120, 7);
        this.TailR1.mirror = true;
        this.TailR1.setPos(0.0F, 0.8F, 8.0F);
        this.TailR1.addBox(-0.5F, 0.0F, 0.0F, 3.0F, 13.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.TailR1, (float) (Math.PI * 4.0 / 9.0), 0.06981317F, 0.0F);
        this.Teeth2 = new AdvancedModelBox(this, 6, 16);
        this.Teeth2.mirror = true;
        this.Teeth2.setPos(0.0F, -0.4F, -3.5F);
        this.Teeth2.addBox(-0.6F, 0.5F, -4.62F, 2.0F, 1.0F, 5.0F, 0.0F);
        this.HeadFront = new AdvancedModelBox(this, 8, 32);
        this.HeadFront.setPos(0.0F, 0.7F, 0.0F);
        this.HeadFront.addBox(-1.5F, -2.8F, -8.8F, 3.0F, 3.0F, 5.0F, 0.0F);
        this.setRotateAngle(this.HeadFront, -0.22759093F, -0.0F, 0.0F);
        this.Head = new AdvancedModelBox(this, 6, 54);
        this.Head.setPos(0.0F, 1.7F, -5.5F);
        this.Head.addBox(-2.0F, -3.0F, -4.0F, 4.0F, 4.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.Head, 0.5462881F, -0.0F, 0.0F);
        this.CrestL3 = new AdvancedModelBox(this, 134, 12);
        this.CrestL3.mirror = true;
        this.CrestL3.setPos(1.1F, -1.3F, -1.0F);
        this.CrestL3.addBox(-1.0F, -1.3F, 0.0F, 2.0F, 5.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.CrestL3, (float) (Math.PI * 5.0 / 9.0), 0.87266463F, (float) (Math.PI / 4));
        this.TailL2 = new AdvancedModelBox(this, 100, 8);
        this.TailL2.mirror = true;
        this.TailL2.setPos(0.0F, 0.7F, 1.7F);
        this.TailL2.addBox(-1.0F, 0.0F, 0.0F, 4.0F, 14.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.TailL2, (float) (Math.PI * 4.0 / 9.0), 0.31869712F, 0.0F);
        this.FingerL2 = new AdvancedModelBox(this, 50, 80);
        this.FingerL2.setPos(-0.1F, 11.0F, 2.0F);
        this.FingerL2.addBox(-0.8F, -0.1F, -2.0F, 1.0F, 14.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerL2, 0.06981317F, 0.0F, 0.0F);
        this.BodyUpper = new AdvancedModelBox(this, 67, 47);
        this.BodyUpper.setPos(0.0F, 15.8F, -10.0F);
        this.BodyUpper.addBox(-3.5F, -2.1F, 0.0F, 7.0F, 8.0F, 7.0F, 0.0F);
        this.CrestLB = new AdvancedModelBox(this, 134, 12);
        this.CrestLB.mirror = true;
        this.CrestLB.setPos(1.1F, -1.4F, 1.2F);
        this.CrestLB.addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.CrestLB, (float) (Math.PI * 5.0 / 9.0), 0.27314404F, 0.0F);
        this.TailL1 = new AdvancedModelBox(this, 120, 7);
        this.TailL1.setPos(0.0F, 0.8F, 8.0F);
        this.TailL1.addBox(-2.5F, 0.0F, 0.0F, 3.0F, 13.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.TailL1, (float) (Math.PI * 4.0 / 9.0), -0.06981317F, 0.0F);
        this.CrestR3 = new AdvancedModelBox(this, 134, 12);
        this.CrestR3.setPos(-1.1F, -1.3F, -1.0F);
        this.CrestR3.addBox(-1.0F, -1.3F, 0.0F, 2.0F, 5.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.CrestR3, (float) (Math.PI * 5.0 / 9.0), -0.87266463F, -0.7285004F);
        this.Jaw = new AdvancedModelBox(this, 34, 56);
        this.Jaw.setPos(0.0F, 0.7F, -3.1F);
        this.Jaw.addBox(-1.5F, -0.5F, -5.5F, 3.0F, 1.0F, 5.0F, 0.0F);
        this.setRotateAngle(this.Jaw, (float) (-Math.PI / 10), -0.0F, 0.0F);
        this.CrestR2 = new AdvancedModelBox(this, 134, 12);
        this.CrestR2.setPos(-1.1F, -1.3F, -1.0F);
        this.CrestR2.addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.CrestR2, (float) (Math.PI * 5.0 / 9.0), -0.61086524F, (float) (-Math.PI / 10));
        this.Teeth1 = new AdvancedModelBox(this, 6, 16);
        this.Teeth1.setPos(0.0F, -0.4F, -3.5F);
        this.Teeth1.addBox(-1.2F, 0.5F, -4.62F, 2.0F, 1.0F, 5.0F, 0.0F);
        this.WingL3 = new AdvancedModelBox(this, 96, 67);
        this.WingL3.mirror = true;
        this.WingL3.setPos(0.0F, 7.6F, 0.0F);
        this.WingL3.addBox(-0.7F, -0.1F, -2.0F, 1.0F, 14.0F, 10.0F, 0.0F);
        this.setRotateAngle(this.WingL3, (float) (Math.PI / 6), 0.0F, 0.0F);
        this.WingR21 = new AdvancedModelBox(this, 80, 90);
        this.WingR21.setPos(0.5F, 0.0F, 0.0F);
        this.WingR21.addBox(-0.78F, -2.5F, -2.1F, 1.0F, 11.0F, 11.0F, 0.0F);
        this.FingerR1 = new AdvancedModelBox(this, 60, 80);
        this.FingerR1.mirror = true;
        this.FingerR1.setPos(0.0F, 11.0F, 0.1F);
        this.FingerR1.addBox(-0.2F, -0.1F, -2.0F, 1.0F, 11.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerR1, 0.034906585F, 0.0F, 0.0F);
        this.BodyLower = new AdvancedModelBox(this, 95, 47);
        this.BodyLower.setPos(0.0F, 0.3F, 6.7F);
        this.BodyLower.addBox(-3.0F, -1.5F, -1.6F, 6.0F, 6.0F, 10.0F, 0.0F);
        this.setRotateAngle(this.BodyLower, 0.045553092F, 0.0F, 0.0F);
        this.Tail1 = new AdvancedModelBox(this, 69, 34);
        this.Tail1.setPos(0.0F, 1.1F, 7.0F);
        this.Tail1.addBox(-2.0F, -2.1F, -0.4F, 4.0F, 5.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.Tail1, -0.045553092F, 0.0F, 0.0F);
        this.FingerR4 = new AdvancedModelBox(this, 30, 80);
        this.FingerR4.mirror = true;
        this.FingerR4.setPos(0.0F, 11.6F, 6.6F);
        this.FingerR4.addBox(-0.1F, -0.1F, -2.0F, 1.0F, 11.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerR4, (float) (Math.PI / 12), 0.0F, 0.0F);
        this.WingR2 = new AdvancedModelBox(this, 80, 90);
        this.WingR2.setPos(-0.4F, 7.6F, -2.8F);
        this.WingR2.addBox(-0.4F, -2.5F, -2.1F, 1.0F, 11.0F, 11.0F, 0.0F);
        this.setRotateAngle(this.WingR2, (float) (-Math.PI / 9), 0.0F, 0.0F);
        this.WingR = new AdvancedModelBox(this, 100, 107);
        this.WingR.setPos(-3.0F, -0.5F, 5.0F);
        this.WingR.addBox(-0.9F, 0.0F, -5.0F, 1.0F, 8.0F, 12.0F, 0.0F);
        this.setRotateAngle(this.WingR, 0.08726646F, 0.0F, (float) (Math.PI * 4.0 / 9.0));
        this.WingL2 = new AdvancedModelBox(this, 80, 90);
        this.WingL2.mirror = true;
        this.WingL2.setPos(0.4F, 7.6F, -2.8F);
        this.WingL2.addBox(-0.6F, -2.5F, -2.1F, 1.0F, 11.0F, 11.0F, 0.0F);
        this.setRotateAngle(this.WingL2, (float) (-Math.PI / 9), 0.0F, 0.0F);
        this.CrestL2 = new AdvancedModelBox(this, 134, 12);
        this.CrestL2.mirror = true;
        this.CrestL2.setPos(1.1F, -1.3F, -1.0F);
        this.CrestL2.addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.CrestL2, (float) (Math.PI * 5.0 / 9.0), 0.61086524F, (float) (Math.PI / 10));
        this.FingerL3 = new AdvancedModelBox(this, 40, 80);
        this.FingerL3.setPos(0.0F, 11.0F, 4.4F);
        this.FingerL3.addBox(-0.8F, -0.1F, -2.0F, 1.0F, 16.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerL3, 0.13962634F, 0.0F, 0.0F);
        this.FingerR2 = new AdvancedModelBox(this, 50, 80);
        this.FingerR2.mirror = true;
        this.FingerR2.setPos(0.1F, 11.0F, 2.0F);
        this.FingerR2.addBox(-0.2F, -0.1F, -2.0F, 1.0F, 14.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerR2, 0.06981317F, 0.0F, 0.0F);
        this.FingerL1 = new AdvancedModelBox(this, 60, 80);
        this.FingerL1.setPos(0.0F, 11.0F, 0.1F);
        this.FingerL1.addBox(-0.8F, -0.1F, -2.0F, 1.0F, 11.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerL1, 0.034906585F, 0.0F, 0.0F);
        this.Beak = new AdvancedModelBox(this, 7, 45);
        this.Beak.setPos(0.0F, -1.7F, -7.7F);
        this.Beak.addBox(-1.0F, -1.4F, -3.0F, 2.0F, 3.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.Beak, (float) (Math.PI * 2.0 / 9.0), -0.0F, 0.0F);
        this.WingL = new AdvancedModelBox(this, 100, 107);
        this.WingL.mirror = true;
        this.WingL.setPos(3.0F, -0.5F, 5.0F);
        this.WingL.addBox(-0.1F, 0.0F, -5.0F, 1.0F, 8.0F, 12.0F, 0.0F);
        this.setRotateAngle(this.WingL, 0.08726646F, 0.0F, (float) (-Math.PI * 4.0 / 9.0));
        this.CrestL1 = new AdvancedModelBox(this, 134, 12);
        this.CrestL1.setPos(0.9F, -2.0F, -1.0F);
        this.CrestL1.addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.CrestL1, (float) (Math.PI * 5.0 / 9.0), 0.08726646F, 0.0F);
        this.Tail3 = new AdvancedModelBox(this, 70, 21);
        this.Tail3.setPos(0.0F, -0.1F, 7.7F);
        this.Tail3.addBox(-1.0F, -1.3F, -0.2F, 2.0F, 3.0F, 9.0F, 0.0F);
        this.setRotateAngle(this.Tail3, 0.091106184F, 0.0F, 0.0F);
        this.Club = new AdvancedModelBox(this, 42, 17);
        this.Club.setPos(0.0F, -0.7F, 7.5F);
        this.Club.addBox(-2.0F, -0.4F, 0.3F, 4.0F, 2.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.Club, 0.091106184F, 0.0F, 0.0F);
        this.Neck2 = new AdvancedModelBox(this, 37, 44);
        this.Neck2.setPos(0.0F, -0.1F, -3.8F);
        this.Neck2.addBox(-1.5F, -1.7F, -6.4F, 3.0F, 5.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Neck2, -0.091106184F, -0.0F, 0.0F);
        this.CrestR1 = new AdvancedModelBox(this, 134, 12);
        this.CrestR1.mirror = true;
        this.CrestR1.setPos(-0.9F, -2.0F, -1.0F);
        this.CrestR1.addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.CrestR1, (float) (Math.PI * 5.0 / 9.0), -0.08726646F, 0.0F);
        this.WingL21 = new AdvancedModelBox(this, 80, 90);
        this.WingL21.mirror = true;
        this.WingL21.setPos(-0.5F, 0.0F, 0.0F);
        this.WingL21.addBox(-0.22F, -2.5F, -2.1F, 1.0F, 11.0F, 11.0F, 0.0F);
        this.Neck1 = new AdvancedModelBox(this, 57, 64);
        this.Neck1.setPos(0.0F, 0.6F, 1.8F);
        this.Neck1.addBox(-2.0F, -2.3F, -5.0F, 4.0F, 6.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.Neck1, 0.13665928F, -0.0F, -0.045553092F);
        this.FingerL4 = new AdvancedModelBox(this, 30, 80);
        this.FingerL4.setPos(0.0F, 11.0F, 6.6F);
        this.FingerL4.addBox(-0.9F, -0.1F, -2.0F, 1.0F, 11.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerL4, (float) (Math.PI / 12), 0.0F, 0.0F);
        this.Tail2 = new AdvancedModelBox(this, 95, 34);
        this.Tail2.setPos(0.0F, -0.1F, 6.6F);
        this.Tail2.addBox(-1.51F, -1.7F, -0.1F, 3.0F, 4.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.Tail2, -0.091106184F, 0.0F, 0.0F);
        this.Neck3 = new AdvancedModelBox(this, 25, 64);
        this.Neck3.setPos(0.0F, 0.0F, -5.1F);
        this.Neck3.addBox(-1.5F, -1.1F, -7.0F, 3.0F, 4.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.Neck3, -0.18203785F, -0.0F, 0.0F);
        this.Tail4 = new AdvancedModelBox(this, 70, 22);
        this.Tail4.setPos(0.0F, 0.3F, 9.0F);
        this.Tail4.addBox(-1.02F, -1.3F, -0.6F, 2.0F, 2.0F, 9.0F, 0.0F);
        this.setRotateAngle(this.Tail4, -0.045553092F, 0.0F, 0.0F);
        this.TailR2 = new AdvancedModelBox(this, 100, 8);
        this.TailR2.setPos(-2.0F, 0.7F, 1.7F);
        this.TailR2.addBox(-1.0F, 0.0F, 0.0F, 4.0F, 14.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.TailR2, (float) (Math.PI * 4.0 / 9.0), -0.31869712F, 0.0F);
        this.CrestRB = new AdvancedModelBox(this, 134, 12);
        this.CrestRB.setPos(-1.1F, -1.4F, 1.2F);
        this.CrestRB.addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.CrestRB, (float) (Math.PI * 5.0 / 9.0), -0.27314404F, 0.0F);
        this.WingR3 = new AdvancedModelBox(this, 96, 67);
        this.WingR3.setPos(0.0F, 7.6F, 0.0F);
        this.WingR3.addBox(-0.3F, -0.1F, -2.0F, 1.0F, 14.0F, 10.0F, 0.0F);
        this.setRotateAngle(this.WingR3, (float) (Math.PI / 6), 0.0F, 0.0F);
        this.WingR3.addChild(this.FingerR3);
        this.Club.addChild(this.TailR1);
        this.HeadFront.addChild(this.Teeth2);
        this.Head.addChild(this.HeadFront);
        this.Neck3.addChild(this.Head);
        this.Head.addChild(this.CrestL3);
        this.Club.addChild(this.TailL2);
        this.WingL3.addChild(this.FingerL2);
        this.Head.addChild(this.CrestLB);
        this.Club.addChild(this.TailL1);
        this.Head.addChild(this.CrestR3);
        this.Head.addChild(this.Jaw);
        this.Head.addChild(this.CrestR2);
        this.HeadFront.addChild(this.Teeth1);
        this.WingL2.addChild(this.WingL3);
        this.WingR2.addChild(this.WingR21);
        this.WingR3.addChild(this.FingerR1);
        this.BodyUpper.addChild(this.BodyLower);
        this.BodyLower.addChild(this.Tail1);
        this.WingR3.addChild(this.FingerR4);
        this.WingR.addChild(this.WingR2);
        this.BodyUpper.addChild(this.WingR);
        this.WingL.addChild(this.WingL2);
        this.Head.addChild(this.CrestL2);
        this.WingL3.addChild(this.FingerL3);
        this.WingR3.addChild(this.FingerR2);
        this.WingL3.addChild(this.FingerL1);
        this.HeadFront.addChild(this.Beak);
        this.BodyUpper.addChild(this.WingL);
        this.Head.addChild(this.CrestL1);
        this.Tail2.addChild(this.Tail3);
        this.Tail4.addChild(this.Club);
        this.Neck1.addChild(this.Neck2);
        this.Head.addChild(this.CrestR1);
        this.WingL2.addChild(this.WingL21);
        this.BodyUpper.addChild(this.Neck1);
        this.WingL3.addChild(this.FingerL4);
        this.Tail1.addChild(this.Tail2);
        this.Neck2.addChild(this.Neck3);
        this.Tail3.addChild(this.Tail4);
        this.Club.addChild(this.TailR2);
        this.Head.addChild(this.CrestRB);
        this.WingR2.addChild(this.WingR3);
        this.Jaw.setScale(0.99F, 0.99F, 0.99F);
        this.Neck3.setScale(0.99F, 0.99F, 0.99F);
        this.animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.BodyUpper);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.BodyUpper, this.BodyLower, this.Neck1, this.WingL, this.WingR, this.Tail1, this.Tail2, this.Tail3, this.Tail4, this.Club, this.TailR1, this.TailL1, new AdvancedModelBox[] { this.TailL2, this.TailR2, this.Neck2, this.Neck3, this.Head, this.HeadFront, this.Jaw, this.CrestL1, this.CrestL2, this.CrestR2, this.CrestR1, this.CrestR3, this.CrestL3, this.CrestRB, this.CrestLB, this.Beak, this.Teeth2, this.Teeth1, this.WingL2, this.WingL3, this.WingL21, this.FingerL1, this.FingerL2, this.FingerL3, this.FingerL4, this.WingR2, this.WingR3, this.WingR21, this.FingerR1, this.FingerR2, this.FingerR3, this.FingerR4 });
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.animator.update(entity);
        if (this.animator.setAnimation(EntityAmphithere.ANIMATION_BITE)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Neck1, -39.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck2, -18.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck3, -11.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Head, 60.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Jaw, 41.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Neck1, -19.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck2, 23.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck3, 7.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Head, 20.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityAmphithere.ANIMATION_BITE_RIDER)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Neck1, 8.0F, 0.0F, 63.0F);
            this.rotate(this.animator, this.Neck2, -5.0F, 35.0F, 13.0F);
            this.rotate(this.animator, this.Neck3, -10.0F, 70.0F, 0.0F);
            this.rotate(this.animator, this.Head, 20.0F, 30.0F, 95.0F);
            this.rotate(this.animator, this.Jaw, 40.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Neck1, 8.0F, 0.0F, 63.0F);
            this.rotate(this.animator, this.Neck2, -5.0F, 35.0F, 13.0F);
            this.rotate(this.animator, this.Neck3, -10.0F, 70.0F, 0.0F);
            this.rotate(this.animator, this.Head, 50.0F, 40.0F, 95.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityAmphithere.ANIMATION_WING_BLAST)) {
            this.animator.startKeyframe(5);
            this.wingBlastPose();
            this.rotateMinus(this.animator, this.WingR, 32.0F, 0.0F, 170.0F);
            this.rotateMinus(this.animator, this.WingL, 32.0F, 0.0F, -170.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.wingBlastPose();
            this.rotateMinus(this.animator, this.WingR, 15.0F, 0.0F, 45.0F);
            this.rotateMinus(this.animator, this.WingL, 15.0F, 0.0F, -45.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.wingBlastPose();
            this.rotateMinus(this.animator, this.WingR, 32.0F, 0.0F, 170.0F);
            this.rotateMinus(this.animator, this.WingL, 32.0F, 0.0F, -170.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.wingBlastPose();
            this.rotateMinus(this.animator, this.WingR, 15.0F, 0.0F, 45.0F);
            this.rotateMinus(this.animator, this.WingL, 15.0F, 0.0F, -45.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(10);
        }
        if (this.animator.setAnimation(EntityAmphithere.ANIMATION_TAIL_WHIP)) {
            this.animator.startKeyframe(10);
            this.rotate(this.animator, this.BodyUpper, 0.0F, 30.0F, 0.0F);
            this.rotate(this.animator, this.BodyLower, 0.0F, 10.0F, 0.0F);
            this.rotate(this.animator, this.Tail1, 0.0F, 15.0F, 0.0F);
            this.rotate(this.animator, this.Tail2, 0.0F, 10.0F, 0.0F);
            this.rotate(this.animator, this.Tail2, 0.0F, 10.0F, 0.0F);
            this.rotate(this.animator, this.Tail3, 0.0F, 20.0F, 0.0F);
            this.rotate(this.animator, this.Tail4, 0.0F, 15.0F, -20.0F);
            this.rotate(this.animator, this.Neck1, 0.0F, -18.0F, -20.0F);
            this.rotate(this.animator, this.Neck2, 0.0F, -29.0F, 0.0F);
            this.rotate(this.animator, this.Neck3, 0.0F, -44.0F, 0.0F);
            this.rotate(this.animator, this.Head, 31.0F, -28.0F, 0.0F);
            this.rotate(this.animator, this.WingL, 0.0F, 18.0F, 0.0F);
            this.rotate(this.animator, this.WingR, 0.0F, -18.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.BodyUpper, 0.0F, -214.0F, 0.0F);
            this.rotate(this.animator, this.BodyLower, 0.0F, -10.0F, 0.0F);
            this.rotate(this.animator, this.Tail1, 0.0F, -15.0F, 0.0F);
            this.rotate(this.animator, this.Tail2, 0.0F, -7.0F, 0.0F);
            this.rotate(this.animator, this.Tail2, 0.0F, -7.0F, 0.0F);
            this.rotate(this.animator, this.Tail3, 0.0F, -7.0F, 0.0F);
            this.rotate(this.animator, this.Tail4, 0.0F, -15.0F, -20.0F);
            this.rotate(this.animator, this.Neck1, 0.0F, 18.0F, -20.0F);
            this.rotate(this.animator, this.Neck2, 0.0F, 29.0F, 0.0F);
            this.rotate(this.animator, this.Neck3, 0.0F, 44.0F, 0.0F);
            this.rotate(this.animator, this.Head, 31.0F, -28.0F, 0.0F);
            this.rotate(this.animator, this.WingL, 0.0F, 18.0F, 0.0F);
            this.rotate(this.animator, this.WingR, 0.0F, -18.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(3);
            this.animator.resetKeyframe(7);
            this.animator.endKeyframe();
        }
        if (this.animator.setAnimation(EntityAmphithere.ANIMATION_SPEAK)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Jaw, 31.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
    }

    private void wingBlastPose() {
        this.animator.move(this.BodyUpper, 0.0F, -36.0F, 20.0F);
        this.rotateMinus(this.animator, this.BodyUpper, -80.0F, 0.0F, 0.0F);
        this.rotateMinus(this.animator, this.BodyLower, -10.0F, 0.0F, 0.0F);
        this.rotateMinus(this.animator, this.Tail1, -10.0F, 0.0F, 0.0F);
        this.rotateMinus(this.animator, this.Tail1, -10.0F, 0.0F, 0.0F);
        this.rotateMinus(this.animator, this.Tail2, -10.0F, 0.0F, 0.0F);
        this.rotateMinus(this.animator, this.Tail3, -10.0F, 0.0F, 0.0F);
        this.rotateMinus(this.animator, this.Tail4, -10.0F, 0.0F, 0.0F);
        this.rotateMinus(this.animator, this.Club, -25.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.Neck1, 20.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.Neck2, 32.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.Neck3, 2.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.Head, 80.0F, 0.0F, 0.0F);
        this.rotateMinus(this.animator, this.WingR2, -20.0F, 0.0F, -20.0F);
        this.rotateMinus(this.animator, this.WingL2, -20.0F, 0.0F, 20.0F);
        this.rotateMinus(this.animator, this.WingR3, 16.0F, 0.0F, 0.0F);
        this.rotateMinus(this.animator, this.WingL3, 16.0F, 0.0F, 0.0F);
    }

    public void setupAnim(EntityAmphithere amphithere, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(amphithere, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.0F);
        if (this.f_102610_) {
            this.BodyUpper.setShouldScaleChildren(true);
            this.HeadFront.setShouldScaleChildren(true);
            this.Jaw.setShouldScaleChildren(true);
            this.BodyUpper.setScale(0.5F, 0.5F, 0.5F);
            this.Head.setScale(1.5F, 1.5F, 1.5F);
            this.HeadFront.setScale(1.0F, 1.0F, 1.0F);
            this.HeadFront.offsetZ = -2.0F;
            this.Jaw.offsetZ = -4.5F;
        } else {
            this.BodyUpper.setScale(1.0F, 1.0F, 1.0F);
            this.Head.setScale(1.0F, 1.0F, 1.0F);
            this.HeadFront.setScale(1.0F, 1.0F, 1.0F);
        }
        float speed_walk = 0.4F;
        float speed_idle = 0.05F;
        float speed_fly = 0.2F;
        float degree_walk = 0.5F;
        float degree_idle = 0.5F;
        float degree_flap = 0.5F * (amphithere.flapProgress / 10.0F);
        AdvancedModelBox[] TAIL = new AdvancedModelBox[] { this.Tail1, this.Tail2, this.Tail3, this.Tail4 };
        AdvancedModelBox[] ENTIRE_BODY = new AdvancedModelBox[] { this.BodyUpper, this.BodyLower, this.Tail1, this.Tail2, this.Tail3, this.Tail4 };
        AdvancedModelBox[] NECK = new AdvancedModelBox[] { this.Neck1, this.Neck2, this.Neck3 };
        if (amphithere.groundProgress >= 10.0F) {
            this.chainSwing(ENTIRE_BODY, speed_walk, 0.125F, 2.0, limbSwing, limbSwingAmount);
            this.chainSwing(NECK, speed_walk, -degree_walk, 4.0, limbSwing, limbSwingAmount);
        }
        this.chainWave(NECK, speed_idle, degree_idle * 0.15F, 4.0, ageInTicks, 1.0F);
        this.chainSwing(TAIL, speed_idle, degree_idle * 0.1F, 2.0, ageInTicks, 1.0F);
        this.flap(this.WingL, speed_fly, degree_flap, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.WingR, speed_fly, -degree_flap, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.WingL2, speed_fly, degree_flap, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.WingR2, speed_fly, -degree_flap, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        float sitProgress = amphithere.diveProgress;
        this.progressRotation(this.FingerR4, sitProgress, (float) (Math.PI / 12), 0.0F, 0.0F);
        this.progressRotation(this.WingL2, sitProgress, (float) (-Math.PI / 9), 0.0F, (float) (Math.PI / 9));
        this.progressRotation(this.FingerR1, sitProgress, 0.034906585F, 0.0F, 0.0F);
        this.progressRotation(this.FingerL3, sitProgress, 0.13962634F, 0.0F, 0.0F);
        this.progressRotation(this.WingR21, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.FingerL1, sitProgress, 0.034906585F, 0.0F, 0.0F);
        this.progressRotation(this.WingR, sitProgress, 0.55850536F, 0.0F, 1.6580628F);
        this.progressRotation(this.FingerL4, sitProgress, (float) (Math.PI / 12), 0.0F, 0.0F);
        this.progressRotation(this.WingR3, sitProgress, 1.4835298F, 0.0F, 0.0F);
        this.progressRotation(this.WingR2, sitProgress, (float) (-Math.PI / 9), 0.0F, (float) (-Math.PI / 9));
        this.progressRotation(this.WingL3, sitProgress, 1.4835298F, 0.0F, 0.0F);
        this.progressRotation(this.FingerL2, sitProgress, 0.06981317F, 0.0F, 0.0F);
        this.progressRotation(this.FingerR3, sitProgress, 0.13962634F, 0.0F, 0.0F);
        this.progressRotation(this.FingerR2, sitProgress, 0.06981317F, 0.0F, 0.0F);
        this.progressRotation(this.WingL, sitProgress, 0.55850536F, 0.0F, -1.6580628F);
        sitProgress = amphithere.groundProgress;
        this.progressRotation(this.Tail1, sitProgress, -0.045553092F, 0.0F, 0.0F);
        this.progressRotation(this.CrestR2, sitProgress, (float) (Math.PI * 5.0 / 9.0), -0.61086524F, (float) (-Math.PI / 10));
        this.progressRotation(this.FingerR4, sitProgress, (float) (-Math.PI / 12), 0.0F, 0.0F);
        this.progressRotation(this.TailL1, sitProgress, (float) (Math.PI * 4.0 / 9.0), -0.06981317F, 0.0F);
        this.progressRotation(this.Neck2, sitProgress, -0.4553564F, -0.0F, 0.0F);
        this.progressRotation(this.FingerR3, sitProgress, -0.13962634F, 0.0F, 0.0F);
        this.progressRotation(this.FingerL1, sitProgress, 0.13962634F, 0.0F, 0.0F);
        this.progressRotation(this.WingR, sitProgress, 0.08726646F, 0.0F, 1.134464F);
        this.progressRotation(this.FingerL4, sitProgress, (float) (-Math.PI / 12), 0.0F, 0.0F);
        this.progressRotation(this.Teeth2, sitProgress, 0.0F, -0.0F, 0.0F);
        this.progressRotation(this.FingerR2, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Beak, sitProgress, (float) (Math.PI * 2.0 / 9.0), -0.0F, 0.0F);
        this.progressRotation(this.BodyUpper, sitProgress, 0.0F, -0.0F, 0.0F);
        this.progressRotation(this.Club, sitProgress, 0.18203785F, 0.0F, 0.0F);
        this.progressRotation(this.CrestLB, sitProgress, (float) (Math.PI * 5.0 / 9.0), 0.27314404F, 0.0F);
        this.progressRotation(this.FingerR1, sitProgress, 0.13962634F, 0.0F, 0.0F);
        this.progressRotation(this.HeadFront, sitProgress, -0.22759093F, -0.0F, 0.0F);
        this.progressRotation(this.Neck1, sitProgress, -0.18203785F, -0.0F, -0.045553092F);
        this.progressRotation(this.FingerL2, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.WingR3, sitProgress, 0.12217305F, (float) (-Math.PI / 18), -0.12217305F);
        this.progressRotation(this.Teeth1, sitProgress, 0.0F, -0.0F, 0.0F);
        this.progressRotation(this.Tail4, sitProgress, -0.045553092F, 0.0F, 0.0F);
        this.progressRotation(this.WingL3, sitProgress, 0.12217305F, (float) (Math.PI / 18), 0.12217305F);
        this.progressRotation(this.Jaw, sitProgress, -0.31869712F, -0.0F, 0.0F);
        this.progressRotation(this.Tail2, sitProgress, 0.091106184F, 0.0F, 0.0F);
        this.progressRotation(this.CrestRB, sitProgress, (float) (Math.PI * 5.0 / 9.0), -0.27314404F, 0.0F);
        this.progressRotation(this.BodyLower, sitProgress, -0.13665928F, 0.0F, 0.0F);
        this.progressRotation(this.CrestR3, sitProgress, (float) (Math.PI * 5.0 / 9.0), -0.87266463F, -0.7285004F);
        this.progressRotation(this.WingL21, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Neck3, sitProgress, 0.18203785F, -0.0F, 0.0F);
        this.progressRotation(this.TailL2, sitProgress, (float) (Math.PI * 4.0 / 9.0), 0.31869712F, 0.0F);
        this.progressRotation(this.FingerL3, sitProgress, -0.13962634F, 0.0F, 0.0F);
        this.progressRotation(this.CrestL3, sitProgress, (float) (Math.PI * 5.0 / 9.0), 0.87266463F, (float) (Math.PI / 4));
        this.progressRotation(this.CrestL2, sitProgress, (float) (Math.PI * 5.0 / 9.0), 0.61086524F, (float) (Math.PI / 10));
        this.progressRotation(this.WingR21, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.TailR1, sitProgress, (float) (Math.PI * 4.0 / 9.0), 0.06981317F, 0.0F);
        this.progressRotation(this.Head, sitProgress, 0.8196066F, -0.0F, 0.0F);
        this.progressRotation(this.WingR2, sitProgress, 1.4835298F, 0.0F, (float) (Math.PI / 18));
        this.progressRotation(this.WingL, sitProgress, 0.08726646F, 0.0F, -1.134464F);
        this.progressRotation(this.CrestR1, sitProgress, (float) (Math.PI * 5.0 / 9.0), -0.08726646F, 0.0F);
        this.progressRotation(this.WingL2, sitProgress, 1.4835298F, 0.0F, (float) (-Math.PI / 18));
        this.progressRotation(this.Tail3, sitProgress, 0.045553092F, 0.0F, 0.0F);
        this.progressRotation(this.TailR2, sitProgress, (float) (Math.PI * 4.0 / 9.0), -0.31869712F, 0.0F);
        this.progressRotation(this.CrestL1, sitProgress, (float) (Math.PI * 5.0 / 9.0), 0.08726646F, 0.0F);
        this.progressPosition(this.BodyUpper, sitProgress, 0.0F, 18.0F, 0.0F);
        sitProgress = amphithere.sitProgress;
        this.progressRotation(this.CrestLB, sitProgress, (float) (Math.PI * 5.0 / 9.0), 0.27314404F, 0.0F);
        this.progressRotation(this.CrestR1, sitProgress, (float) (Math.PI * 5.0 / 9.0), -0.08726646F, 0.0F);
        this.progressRotation(this.TailL1, sitProgress, (float) (Math.PI * 4.0 / 9.0), -0.06981317F, 0.0F);
        this.progressRotation(this.HeadFront, sitProgress, -0.22759093F, -0.0F, 0.0F);
        this.progressRotation(this.CrestL1, sitProgress, (float) (Math.PI * 5.0 / 9.0), 0.08726646F, 0.0F);
        this.progressRotation(this.CrestR3, sitProgress, (float) (Math.PI * 5.0 / 9.0), -0.87266463F, -0.7285004F);
        this.progressRotation(this.TailR2, sitProgress, (float) (Math.PI * 4.0 / 9.0), -0.31869712F, 0.0F);
        this.progressRotation(this.Neck2, sitProgress, -0.4553564F, -0.0F, 0.0F);
        this.progressRotation(this.Head, sitProgress, 1.3658947F, -0.0F, 0.0F);
        this.progressRotation(this.Tail1, sitProgress, 0.0F, 0.7285004F, 0.045553092F);
        this.progressRotation(this.CrestRB, sitProgress, (float) (Math.PI * 5.0 / 9.0), -0.27314404F, 0.0F);
        this.progressRotation(this.Jaw, sitProgress, -0.31869712F, -0.0F, 0.0F);
        this.progressRotation(this.Tail2, sitProgress, 0.091106184F, 0.3642502F, 0.0F);
        this.progressRotation(this.Tail3, sitProgress, 0.045553092F, 0.3642502F, 0.0F);
        this.progressRotation(this.BodyLower, sitProgress, -0.13665928F, 0.0F, 0.13665928F);
        this.progressRotation(this.TailR1, sitProgress, (float) (Math.PI * 4.0 / 9.0), 0.06981317F, 0.0F);
        this.progressRotation(this.Club, sitProgress, 0.2678395F, 0.7285004F, 0.08580165F);
        this.progressRotation(this.TailL2, sitProgress, (float) (Math.PI * 4.0 / 9.0), 0.31869712F, 0.0F);
        this.progressRotation(this.CrestL3, sitProgress, (float) (Math.PI * 5.0 / 9.0), 0.87266463F, (float) (Math.PI / 4));
        this.progressRotation(this.CrestL2, sitProgress, (float) (Math.PI * 5.0 / 9.0), 0.61086524F, (float) (Math.PI / 10));
        this.progressRotation(this.Neck1, sitProgress, -0.5462881F, -0.0F, -0.045553092F);
        this.progressRotation(this.Tail4, sitProgress, -0.045553092F, 0.5009095F, 0.0F);
        this.progressRotation(this.Neck3, sitProgress, 0.18203785F, -0.0F, 0.0F);
        if (amphithere.groundProgress <= 0.0F && amphithere.getAnimation() != EntityAmphithere.ANIMATION_WING_BLAST && !amphithere.m_20096_()) {
            amphithere.roll_buffer.applyChainFlapBuffer(this.BodyUpper);
            amphithere.pitch_buffer.applyChainWaveBuffer(this.BodyUpper);
            amphithere.tail_buffer.applyChainSwingBuffer(TAIL);
        }
    }

    @Override
    public void renderStatue(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.m_7695_(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}