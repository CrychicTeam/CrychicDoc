package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;

public class ModelStymphalianBird extends ModelDragonBase<EntityStymphalianBird> {

    public AdvancedModelBox Body;

    public AdvancedModelBox LowerBody;

    public AdvancedModelBox Neck1;

    public AdvancedModelBox WingL;

    public AdvancedModelBox WingR;

    public AdvancedModelBox BackLegL1;

    public AdvancedModelBox BackLegR1;

    public AdvancedModelBox Lowerbodytilt;

    public AdvancedModelBox TailR1;

    public AdvancedModelBox TailL1;

    public AdvancedModelBox TailR2;

    public AdvancedModelBox TailL2;

    public AdvancedModelBox BackLegL2;

    public AdvancedModelBox ToeL3;

    public AdvancedModelBox ToeL2;

    public AdvancedModelBox ToeL4;

    public AdvancedModelBox ToeL1;

    public AdvancedModelBox BackLegR2;

    public AdvancedModelBox ToeR3;

    public AdvancedModelBox ToeL4_1;

    public AdvancedModelBox ToeR2;

    public AdvancedModelBox ToeR1;

    public AdvancedModelBox Neck2;

    public AdvancedModelBox HeadBase;

    public AdvancedModelBox HeadFront;

    public AdvancedModelBox Jaw;

    public AdvancedModelBox Crest1;

    public AdvancedModelBox uppernail;

    public AdvancedModelBox Crest2;

    public AdvancedModelBox Crest3;

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

    public AdvancedModelBox HeadPivot;

    public AdvancedModelBox NeckPivot;

    private ModelAnimator animator;

    public ModelStymphalianBird() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.WingL3 = new AdvancedModelBox(this, 96, 60);
        this.WingL3.mirror = true;
        this.WingL3.setPos(0.0F, 7.6F, 0.0F);
        this.WingL3.addBox(-0.7F, -0.1F, -2.0F, 1.0F, 14.0F, 10.0F, 0.0F);
        this.setRotateAngle(this.WingL3, 0.12217305F, (float) (Math.PI / 18), 0.091106184F);
        this.Jaw = new AdvancedModelBox(this, 14, 11);
        this.Jaw.setPos(0.0F, 0.0F, -2.6F);
        this.Jaw.addBox(-1.0F, -0.7F, -4.5F, 2.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.Jaw, -0.18203785F, 0.0F, 0.0F);
        this.WingL2 = new AdvancedModelBox(this, 80, 90);
        this.WingL2.mirror = true;
        this.WingL2.setPos(0.4F, 7.6F, -2.8F);
        this.WingL2.addBox(-0.6F, -2.5F, -2.1F, 1.0F, 11.0F, 11.0F, 0.0F);
        this.setRotateAngle(this.WingL2, 1.4835298F, 0.0F, (float) (-Math.PI / 18));
        this.WingR2 = new AdvancedModelBox(this, 80, 90);
        this.WingR2.setPos(-0.4F, 7.6F, -2.8F);
        this.WingR2.addBox(-0.4F, -2.5F, -2.1F, 1.0F, 11.0F, 11.0F, 0.0F);
        this.setRotateAngle(this.WingR2, 1.4835298F, 0.0F, (float) (Math.PI / 18));
        this.WingR = new AdvancedModelBox(this, 100, 107);
        this.WingR.setPos(-3.0F, -1.6F, -1.2F);
        this.WingR.addBox(-0.9F, 0.0F, -5.0F, 1.0F, 8.0F, 12.0F, 0.0F);
        this.setRotateAngle(this.WingR, 0.08726646F, 0.0F, (float) (Math.PI / 18));
        this.TailR1 = new AdvancedModelBox(this, 54, 5);
        this.TailR1.mirror = true;
        this.TailR1.setPos(0.0F, -0.2F, 6.1F);
        this.TailR1.addBox(-0.5F, 0.0F, 0.0F, 3.0F, 16.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.TailR1, (float) (Math.PI / 2), 0.034906585F, 0.0F);
        this.ToeL4_1 = new AdvancedModelBox(this, 0, 43);
        this.ToeL4_1.mirror = true;
        this.ToeL4_1.setPos(0.0F, 10.8F, 0.2F);
        this.ToeL4_1.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 4.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeL4_1, -1.5934856F, 0.61086524F, 0.0F);
        this.WingL21 = new AdvancedModelBox(this, 80, 90);
        this.WingL21.mirror = true;
        this.WingL21.setPos(-0.5F, 0.0F, 0.0F);
        this.WingL21.addBox(-0.4F, -2.5F, -2.1F, 1.0F, 11.0F, 11.0F, 0.0F);
        this.ToeR2 = new AdvancedModelBox(this, 0, 43);
        this.ToeR2.mirror = true;
        this.ToeR2.setPos(0.0F, 10.8F, 0.2F);
        this.ToeR2.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 4.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeR2, -1.5934856F, -0.61086524F, 0.0F);
        this.Crest3 = new AdvancedModelBox(this, 68, 20);
        this.Crest3.setPos(0.0F, 0.4F, 0.2F);
        this.Crest3.addBox(-0.5F, -1.21F, 0.39F, 1.0F, 3.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.Crest3, -0.31869712F, 0.0F, 0.0F);
        this.FingerL2 = new AdvancedModelBox(this, 50, 80);
        this.FingerL2.setPos(-0.1F, 11.0F, 2.0F);
        this.FingerL2.addBox(-0.8F, -0.1F, -2.0F, 1.0F, 14.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerL2, 0.2443461F, 0.0F, 0.0F);
        this.BackLegL2 = new AdvancedModelBox(this, 17, 42);
        this.BackLegL2.mirror = true;
        this.BackLegL2.setPos(0.0F, 4.4F, 0.2F);
        this.BackLegL2.addBox(-0.5F, 0.0F, -0.7F, 1.0F, 11.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.BackLegL2, -0.43633232F, 0.0F, 0.0F);
        this.FingerL1 = new AdvancedModelBox(this, 60, 80);
        this.FingerL1.setPos(0.0F, 11.0F, 0.1F);
        this.FingerL1.addBox(-0.8F, -0.1F, -2.0F, 1.0F, 11.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerL1, 0.27925268F, 0.0F, 0.0F);
        this.FingerR2 = new AdvancedModelBox(this, 50, 80);
        this.FingerR2.mirror = true;
        this.FingerR2.setPos(0.1F, 11.0F, 2.0F);
        this.FingerR2.addBox(-0.2F, -0.1F, -2.0F, 1.0F, 14.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerR2, 0.2443461F, 0.0F, 0.0F);
        this.BackLegR1 = new AdvancedModelBox(this, 20, 52);
        this.BackLegR1.setPos(-2.5F, 1.1F, 1.6F);
        this.BackLegR1.addBox(-1.0F, 0.0F, -1.5F, 2.0F, 5.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.BackLegR1, 0.5462881F, 0.0F, 0.08726646F);
        this.BackLegL1 = new AdvancedModelBox(this, 20, 52);
        this.BackLegL1.mirror = true;
        this.BackLegL1.setPos(2.5F, 1.1F, 1.6F);
        this.BackLegL1.addBox(-1.0F, 0.0F, -1.5F, 2.0F, 5.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.BackLegL1, 0.5462881F, 0.0F, -0.08726646F);
        this.HeadFront = new AdvancedModelBox(this, 0, 9);
        this.HeadFront.setPos(0.0F, -0.4F, -2.5F);
        this.HeadFront.addBox(-1.0F, -2.4F, -4.6F, 2.0F, 2.0F, 5.0F, 0.0F);
        this.setRotateAngle(this.HeadFront, 0.045553092F, 0.0F, 0.0F);
        this.WingR21 = new AdvancedModelBox(this, 80, 90);
        this.WingR21.setPos(0.5F, 0.0F, 0.0F);
        this.WingR21.addBox(-0.4F, -2.5F, -2.1F, 1.0F, 11.0F, 11.0F, 0.0F);
        this.ToeL1 = new AdvancedModelBox(this, 0, 43);
        this.ToeL1.mirror = true;
        this.ToeL1.setPos(0.0F, 11.1F, 0.2F);
        this.ToeL1.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 4.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeL1, -2.003289F, (float) Math.PI, 0.0F);
        this.ToeR3 = new AdvancedModelBox(this, 0, 43);
        this.ToeR3.mirror = true;
        this.ToeR3.setPos(0.0F, 10.8F, -0.7F);
        this.ToeR3.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 4.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeR3, -1.548107F, 0.0F, 0.0F);
        this.TailL2 = new AdvancedModelBox(this, 52, 25);
        this.TailL2.setPos(0.0F, 0.1F, 7.0F);
        this.TailL2.addBox(-3.5F, 0.0F, 0.0F, 5.0F, 12.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.TailL2, (float) (Math.PI / 2), 0.08726646F, -0.12217305F);
        this.uppernail = new AdvancedModelBox(this, 11, 4);
        this.uppernail.setPos(0.0F, -0.8F, -4.8F);
        this.uppernail.addBox(-0.5F, -0.4F, -2.0F, 1.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.uppernail, 1.6845918F, -0.0F, 0.0F);
        this.ToeR1 = new AdvancedModelBox(this, 0, 43);
        this.ToeR1.mirror = true;
        this.ToeR1.setPos(0.0F, 11.1F, 0.2F);
        this.ToeR1.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 4.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeR1, -2.003289F, (float) Math.PI, 0.0F);
        this.Neck1 = new AdvancedModelBox(this, 0, 27);
        this.Neck1.setPos(0.0F, -0.5F, -5.0F);
        this.Neck1.addBox(-2.5F, -1.8F, -3.9F, 5.0F, 5.0F, 5.0F, 0.0F);
        this.setRotateAngle(this.Neck1, -0.4553564F, 0.0F, 0.0F);
        this.Crest2 = new AdvancedModelBox(this, 68, 20);
        this.Crest2.setPos(0.0F, 0.4F, 0.2F);
        this.Crest2.addBox(-0.5F, -1.21F, 0.39F, 1.0F, 3.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.Crest2, -0.31869712F, 0.0F, 0.0F);
        this.LowerBody = new AdvancedModelBox(this, 23, 12);
        this.LowerBody.setPos(0.0F, 0.0F, 0.7F);
        this.LowerBody.addBox(-3.5F, -2.7F, -0.1F, 7.0F, 4.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.LowerBody, -0.091106184F, 0.0F, 0.0F);
        this.FingerR1 = new AdvancedModelBox(this, 60, 80);
        this.FingerR1.mirror = true;
        this.FingerR1.setPos(0.0F, 11.0F, 0.1F);
        this.FingerR1.addBox(-0.2F, -0.1F, -2.0F, 1.0F, 11.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerR1, 0.27925268F, 0.0F, 0.0F);
        this.WingL = new AdvancedModelBox(this, 100, 107);
        this.WingL.mirror = true;
        this.WingL.setPos(3.0F, -1.6F, -1.2F);
        this.WingL.addBox(-0.1F, 0.0F, -5.0F, 1.0F, 8.0F, 12.0F, 0.0F);
        this.setRotateAngle(this.WingL, 0.08726646F, 0.0F, (float) (-Math.PI / 18));
        this.BackLegR2 = new AdvancedModelBox(this, 17, 42);
        this.BackLegR2.setPos(0.0F, 4.4F, 0.2F);
        this.BackLegR2.addBox(-0.5F, 0.0F, -0.7F, 1.0F, 11.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.BackLegR2, -0.43633232F, 0.0F, 0.0F);
        this.FingerR4 = new AdvancedModelBox(this, 30, 80);
        this.FingerR4.mirror = true;
        this.FingerR4.setPos(0.0F, 11.6F, 6.6F);
        this.FingerR4.addBox(-0.1F, -0.1F, -2.0F, 1.0F, 11.0F, 3.0F, 0.0F);
        this.ToeL2 = new AdvancedModelBox(this, 0, 43);
        this.ToeL2.mirror = true;
        this.ToeL2.setPos(0.0F, 10.8F, 0.2F);
        this.ToeL2.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 4.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeL2, -1.5934856F, 0.61086524F, 0.0F);
        this.TailL1 = new AdvancedModelBox(this, 54, 5);
        this.TailL1.setPos(0.0F, -0.2F, 6.1F);
        this.TailL1.addBox(-2.5F, 0.0F, 0.0F, 3.0F, 16.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.TailL1, (float) (Math.PI / 2), -0.034906585F, 0.0F);
        this.Neck2 = new AdvancedModelBox(this, 21, 25);
        this.Neck2.setPos(0.0F, 0.0F, 0.0F);
        this.Neck2.addBox(-1.5F, -2.21F, -9.61F, 3.0F, 4.0F, 10.0F, 0.0F);
        this.NeckPivot = new AdvancedModelBox(this, 21, 25);
        this.NeckPivot.setPos(0.0F, 1.4F, -2.0F);
        this.setRotateAngle(this.NeckPivot, -0.68294734F, 0.0F, 0.0F);
        this.FingerR3 = new AdvancedModelBox(this, 40, 80);
        this.FingerR3.mirror = true;
        this.FingerR3.setPos(0.0F, 11.0F, 4.5F);
        this.FingerR3.addBox(-0.2F, -0.1F, -2.0F, 1.0F, 16.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerR3, 0.13962634F, 0.0F, 0.0F);
        this.WingR3 = new AdvancedModelBox(this, 96, 60);
        this.WingR3.setPos(0.0F, 7.6F, 0.0F);
        this.WingR3.addBox(-0.3F, -0.1F, -2.0F, 1.0F, 14.0F, 10.0F, 0.0F);
        this.setRotateAngle(this.WingR3, 0.12217305F, (float) (-Math.PI / 18), -0.08726646F);
        this.Lowerbodytilt = new AdvancedModelBox(this, 0, 54);
        this.Lowerbodytilt.setPos(0.0F, 3.2F, -3.5F);
        this.Lowerbodytilt.addBox(-3.0F, 2.4F, -0.9F, 6.0F, 8.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.Lowerbodytilt, 1.7301449F, 0.0F, 0.0F);
        this.Body = new AdvancedModelBox(this, 34, 47);
        this.Body.setPos(0.0F, 7.0F, -4.0F);
        this.Body.addBox(-4.0F, -3.0F, -6.0F, 8.0F, 7.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Body, -0.18203785F, 0.0F, 0.0F);
        this.ToeL3 = new AdvancedModelBox(this, 0, 43);
        this.ToeL3.mirror = true;
        this.ToeL3.setPos(0.0F, 10.8F, -0.7F);
        this.ToeL3.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 4.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeL3, -1.5934856F, 0.0F, 0.0F);
        this.Crest1 = new AdvancedModelBox(this, 68, 20);
        this.Crest1.setPos(0.0F, -1.6F, -2.0F);
        this.Crest1.addBox(-0.5F, -1.21F, 1.39F, 1.0F, 3.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.Crest1, 0.59184116F, 0.0F, 0.0F);
        this.HeadBase = new AdvancedModelBox(this, 0, 16);
        this.HeadBase.setPos(0.0F, 0.0F, 0.0F);
        this.HeadBase.addBox(-2.0F, -2.91F, -2.8F, 4.0F, 4.0F, 5.0F, 0.0F);
        this.HeadPivot = new AdvancedModelBox(this, 0, 16);
        this.HeadPivot.setPos(0.0F, 0.2F, -8.2F);
        this.setRotateAngle(this.HeadPivot, 1.5025539F, 0.0F, 0.0F);
        this.FingerL4 = new AdvancedModelBox(this, 30, 80);
        this.FingerL4.setPos(0.0F, 11.0F, 6.6F);
        this.FingerL4.addBox(-0.9F, -0.1F, -2.0F, 1.0F, 11.0F, 3.0F, 0.0F);
        this.TailR2 = new AdvancedModelBox(this, 52, 25);
        this.TailR2.mirror = true;
        this.TailR2.setPos(0.0F, 0.1F, 7.0F);
        this.TailR2.addBox(-1.5F, 0.0F, 0.0F, 5.0F, 12.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.TailR2, (float) (Math.PI / 2), -0.08726646F, 0.12217305F);
        this.FingerL3 = new AdvancedModelBox(this, 40, 80);
        this.FingerL3.setPos(0.0F, 11.0F, 4.4F);
        this.FingerL3.addBox(-0.8F, -0.1F, -2.0F, 1.0F, 16.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerL3, 0.13962634F, 0.0F, 0.0F);
        this.ToeL4 = new AdvancedModelBox(this, 0, 43);
        this.ToeL4.mirror = true;
        this.ToeL4.setPos(0.0F, 10.8F, 0.2F);
        this.ToeL4.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 4.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeL4, -1.5934856F, -0.61086524F, 0.0F);
        this.WingL2.addChild(this.WingL3);
        this.HeadBase.addChild(this.Jaw);
        this.WingL.addChild(this.WingL2);
        this.WingR.addChild(this.WingR2);
        this.Body.addChild(this.WingR);
        this.LowerBody.addChild(this.TailR1);
        this.BackLegR2.addChild(this.ToeL4_1);
        this.WingL2.addChild(this.WingL21);
        this.BackLegR2.addChild(this.ToeR2);
        this.Crest2.addChild(this.Crest3);
        this.WingL3.addChild(this.FingerL2);
        this.BackLegL1.addChild(this.BackLegL2);
        this.WingL3.addChild(this.FingerL1);
        this.WingR3.addChild(this.FingerR2);
        this.LowerBody.addChild(this.BackLegR1);
        this.HeadBase.addChild(this.HeadFront);
        this.WingR2.addChild(this.WingR21);
        this.BackLegL2.addChild(this.ToeL1);
        this.BackLegR2.addChild(this.ToeR3);
        this.LowerBody.addChild(this.TailL2);
        this.HeadFront.addChild(this.uppernail);
        this.BackLegR2.addChild(this.ToeR1);
        this.Body.addChild(this.Neck1);
        this.Crest1.addChild(this.Crest2);
        this.LowerBody.addChild(this.BackLegL1);
        this.Body.addChild(this.LowerBody);
        this.WingR3.addChild(this.FingerR1);
        this.Body.addChild(this.WingL);
        this.BackLegR1.addChild(this.BackLegR2);
        this.WingR3.addChild(this.FingerR4);
        this.BackLegL2.addChild(this.ToeL2);
        this.LowerBody.addChild(this.TailL1);
        this.Neck1.addChild(this.NeckPivot);
        this.NeckPivot.addChild(this.Neck2);
        this.WingR3.addChild(this.FingerR3);
        this.WingR2.addChild(this.WingR3);
        this.LowerBody.addChild(this.Lowerbodytilt);
        this.BackLegL2.addChild(this.ToeL3);
        this.HeadBase.addChild(this.Crest1);
        this.Neck2.addChild(this.HeadPivot);
        this.HeadPivot.addChild(this.HeadBase);
        this.WingL3.addChild(this.FingerL4);
        this.LowerBody.addChild(this.TailR2);
        this.WingL3.addChild(this.FingerL3);
        this.BackLegL2.addChild(this.ToeL4);
        this.HeadFront.setScale(1.01F, 1.01F, 1.01F);
        this.updateDefaultPose();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        this.animator = ModelAnimator.create();
        this.animator.update(entity);
        if (this.animator.setAnimation(EntityStymphalianBird.ANIMATION_PECK)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Neck1, -47.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.NeckPivot, 17.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HeadPivot, 46.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Jaw, 10.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Neck1, 26.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.NeckPivot, -18.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HeadPivot, 2.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Jaw, 33.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HeadFront, -20.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityStymphalianBird.ANIMATION_SPEAK)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Jaw, 35.0F, 0.0F, 0.0F);
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Jaw, 0.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
        }
        if (this.animator.setAnimation(EntityStymphalianBird.ANIMATION_SHOOT_ARROWS)) {
            this.animator.startKeyframe(20);
            this.shootPosture();
            this.animator.endKeyframe();
            this.animator.resetKeyframe(10);
        }
    }

    private void shootPosture() {
        this.rotate(this.animator, this.Body, -52.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.Neck1, 33.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.NeckPivot, -7.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.HeadPivot, 70.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.HeadFront, -15.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.Jaw, 36.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.Crest1, 40.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.Crest2, -26.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.Crest3, -33.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.BackLegR1, -25.0F, 0.0F, 15.0F);
        this.rotate(this.animator, this.BackLegL1, -25.0F, 0.0F, -15.0F);
        this.rotate(this.animator, this.WingL, 5.0F, -10.0F, -60.0F);
        this.rotate(this.animator, this.WingL2, -20.0F, 0.0F, 50.0F);
        this.rotate(this.animator, this.WingL3, 30.0F, 0.0F, 20.0F);
        this.rotate(this.animator, this.WingR, 5.0F, 10.0F, 60.0F);
        this.rotate(this.animator, this.WingR2, -20.0F, 0.0F, -50.0F);
        this.rotate(this.animator, this.WingR3, 30.0F, 0.0F, -20.0F);
        this.rotate(this.animator, this.ToeR1, -23.0F, 180.0F, 0.0F);
        this.rotate(this.animator, this.ToeR2, -75.0F, -45.0F, 0.0F);
        this.rotate(this.animator, this.ToeR3, -75.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.ToeL1, -23.0F, 180.0F, 0.0F);
        this.rotate(this.animator, this.ToeL2, -75.0F, 45.0F, 0.0F);
        this.rotate(this.animator, this.ToeL3, -75.0F, 0.0F, 0.0F);
    }

    public void setupAnim(EntityStymphalianBird entity, float f, float f1, float f2, float f3, float f4) {
        this.animate(entity, f, f1, f2, f3, f4, 1.0F);
        float speed_walk = 0.3F;
        float speed_idle = 0.05F;
        float speed_fly = 0.4F;
        float degree_walk = 0.4F;
        float degree_idle = 0.5F;
        float degree_fly = 0.7F;
        AdvancedModelBox[] NECK = new AdvancedModelBox[] { this.Neck1, this.Neck2, this.HeadBase };
        AdvancedModelBox[] FEATHERS = new AdvancedModelBox[] { this.Crest1, this.Crest2, this.Crest3 };
        AdvancedModelBox[] WING_LEFT = new AdvancedModelBox[] { this.WingL, this.WingL2, this.WingL3 };
        AdvancedModelBox[] WING_RIGHT = new AdvancedModelBox[] { this.WingR, this.WingR2, this.WingR3 };
        this.faceTarget(f3, f4, 2.0F, new AdvancedModelBox[] { this.HeadBase });
        this.faceTarget(f3, f4, 2.0F, new AdvancedModelBox[] { this.Neck2 });
        if (entity.flyProgress > 0.0F) {
            this.progressRotation(this.WingR, entity.flyProgress, 0.08726646F, 0.0F, (float) (Math.PI * 4.0 / 9.0));
            this.progressRotation(this.WingR2, entity.flyProgress, (float) (-Math.PI / 9), 0.0F, (float) (Math.PI / 18));
            this.progressRotation(this.WingR3, entity.flyProgress, (float) (Math.PI / 6), 0.0F, 0.0F);
            this.progressRotation(this.WingL, entity.flyProgress, 0.08726646F, 0.0F, (float) (-Math.PI * 4.0 / 9.0));
            this.progressRotation(this.WingL2, entity.flyProgress, (float) (-Math.PI / 9), 0.0F, (float) (-Math.PI / 18));
            this.progressRotation(this.WingL3, entity.flyProgress, (float) (Math.PI / 6), 0.0F, 0.0F);
            this.progressRotation(this.TailL1, entity.flyProgress, (float) (Math.PI / 2), -0.034906585F, 0.0F);
            this.progressRotation(this.ToeR1, entity.flyProgress, -0.4098033F, (float) Math.PI, 0.0F);
            this.progressRotation(this.TailR1, entity.flyProgress, (float) (Math.PI / 2), 0.034906585F, 0.0F);
            this.progressRotation(this.uppernail, entity.flyProgress, 1.6845918F, -0.0F, 0.0F);
            this.progressRotation(this.FingerR1, entity.flyProgress, 0.034906585F, 0.0F, 0.0F);
            this.progressRotation(this.FingerR2, entity.flyProgress, (float) (Math.PI / 20), 0.0F, 0.0F);
            this.progressRotation(this.ToeL4_1, entity.flyProgress, -0.22759093F, 0.61086524F, 0.0F);
            this.progressRotation(this.Crest3, entity.flyProgress, -0.22759093F, 0.0F, 0.0F);
            this.progressRotation(this.HeadFront, entity.flyProgress, 0.045553092F, 0.0F, 0.0F);
            this.progressRotation(this.TailR2, entity.flyProgress, (float) (Math.PI / 2), (float) (Math.PI / 9), 0.12217305F);
            this.progressRotation(this.ToeR2, entity.flyProgress, -0.22759093F, -0.61086524F, 0.0F);
            this.progressRotation(this.FingerR4, entity.flyProgress, 0.40142572F, 0.0F, 0.0F);
            this.progressRotation(this.FingerL2, entity.flyProgress, (float) (Math.PI / 20), 0.0F, 0.0F);
            this.progressRotation(this.ToeL2, entity.flyProgress, -0.22759093F, 0.61086524F, 0.0F);
            this.progressRotation(this.Crest2, entity.flyProgress, -0.22759093F, 0.0F, 0.0F);
            this.progressRotation(this.FingerL3, entity.flyProgress, (float) (Math.PI / 12), 0.0F, 0.0F);
            this.progressRotation(this.BackLegR2, entity.flyProgress, -0.18203785F, 0.0F, 0.0F);
            this.progressRotation(this.Jaw, entity.flyProgress, -0.091106184F, 0.0F, 0.0F);
            this.progressRotation(this.ToeR3, entity.flyProgress, -0.22759093F, 0.0F, 0.0F);
            this.progressRotation(this.BackLegL2, entity.flyProgress, -0.18203785F, 0.0F, 0.0F);
            this.progressRotation(this.ToeL1, entity.flyProgress, -0.4098033F, (float) Math.PI, 0.0F);
            this.progressRotation(this.TailL2, entity.flyProgress, (float) (Math.PI / 2), (float) (-Math.PI / 9), -0.12217305F);
            this.progressRotation(this.Lowerbodytilt, entity.flyProgress, 1.7301449F, 0.0F, 0.0F);
            this.progressRotation(this.FingerR3, entity.flyProgress, (float) (Math.PI / 12), 0.0F, 0.0F);
            this.progressRotation(this.LowerBody, entity.flyProgress, -0.091106184F, 0.0F, 0.0F);
            this.progressRotation(this.FingerL1, entity.flyProgress, 0.034906585F, 0.0F, 0.0F);
            this.progressRotation(this.FingerL4, entity.flyProgress, 0.40142572F, 0.0F, 0.0F);
            this.progressRotation(this.BackLegR1, entity.flyProgress, 1.6390387F, 0.0F, 0.08726646F);
            this.progressRotation(this.HeadPivot, entity.flyProgress, 0.59184116F, 0.0F, 0.0F);
            this.progressRotation(this.Crest1, entity.flyProgress, 0.18203785F, 0.0F, 0.0F);
            this.progressRotation(this.ToeL3, entity.flyProgress, -0.22759093F, 0.0F, 0.0F);
            this.progressRotation(this.Neck1, entity.flyProgress, 0.18203785F, 0.0F, 0.0F);
            this.progressRotation(this.BackLegL1, entity.flyProgress, 1.6390387F, 0.0F, -0.08726646F);
            this.progressRotation(this.NeckPivot, entity.flyProgress, -0.31869712F, 0.0F, 0.0F);
            this.progressRotation(this.ToeL4, entity.flyProgress, -0.22759093F, -0.61086524F, 0.0F);
            this.chainFlap(WING_LEFT, speed_fly + (entity.getAnimation() == EntityStymphalianBird.ANIMATION_SHOOT_ARROWS ? 0.25F : 0.0F), -degree_fly * 0.5F, 0.0, f2, 1.0F);
            this.chainFlap(WING_RIGHT, speed_fly + (entity.getAnimation() == EntityStymphalianBird.ANIMATION_SHOOT_ARROWS ? 0.25F : 0.0F), degree_fly * 0.5F, 0.0, f2, 1.0F);
            if (entity.getAnimation() != EntityStymphalianBird.ANIMATION_SHOOT_ARROWS) {
                this.chainWave(NECK, speed_fly, degree_fly * 0.15F, 4.0, f2, 1.0F);
                this.bob(this.Body, speed_fly * 0.5F, degree_fly * 2.5F, true, f2, 1.0F);
                this.walk(this.BackLegL1, speed_fly, degree_fly * 0.15F, true, 1.0F, 0.2F, f2, 1.0F);
                this.walk(this.BackLegR1, speed_fly, degree_fly * 0.15F, false, 1.0F, -0.2F, f2, 1.0F);
            }
        } else {
            this.chainWave(NECK, speed_idle, degree_idle * 0.15F, 4.0, f2, 1.0F);
            this.chainWave(FEATHERS, speed_idle, degree_idle * -0.1F, 0.0, f2, 1.0F);
            this.walk(this.LowerBody, speed_idle, degree_idle * 0.1F, false, 0.0F, 0.1F, f2, 1.0F);
            this.walk(this.Body, speed_idle, degree_idle * 0.05F, true, 1.0F, 0.0F, f2, 1.0F);
            this.walk(this.BackLegR1, speed_idle, degree_idle * -0.1F, false, 0.0F, 0.1F, f2, 1.0F);
            this.walk(this.BackLegR1, speed_idle, degree_idle * -0.05F, true, 1.0F, 0.0F, f2, 1.0F);
            this.walk(this.BackLegL1, speed_idle, degree_idle * -0.1F, false, 0.0F, 0.1F, f2, 1.0F);
            this.walk(this.BackLegL1, speed_idle, degree_idle * -0.05F, true, 1.0F, 0.0F, f2, 1.0F);
            this.chainWave(NECK, speed_walk, degree_walk * 0.5F, -3.0, f, f1);
            this.chainWave(FEATHERS, speed_walk, degree_walk * -0.1F, 0.0, f, f1);
            this.walk(this.LowerBody, speed_walk, degree_walk * 0.1F, false, 0.0F, 0.0F, f, f1);
            this.walk(this.Body, speed_walk, degree_walk * 0.25F, true, 1.0F, 0.0F, f, f1);
            this.walk(this.BackLegR1, speed_walk, degree_walk * 0.1F, false, 0.0F, 0.0F, f, f1);
            this.walk(this.BackLegR1, speed_walk, degree_walk * 0.25F, true, 1.0F, 0.0F, f, f1);
            this.walk(this.BackLegL1, speed_walk, degree_walk * 0.1F, false, 0.0F, 0.0F, f, f1);
            this.walk(this.BackLegL1, speed_walk, degree_walk * 0.25F, true, 1.0F, 0.0F, f, f1);
            this.walk(this.BackLegL1, speed_walk, degree_walk, true, 1.0F, -0.1F, f, f1);
            this.walk(this.BackLegL2, speed_walk, degree_walk, true, 1.0F, -0.1F, f, f1);
            this.walk(this.BackLegR1, speed_walk, degree_walk, false, 1.0F, 0.1F, f, f1);
            this.walk(this.BackLegR2, speed_walk, degree_walk, false, 1.0F, 0.1F, f, f1);
            this.walk(this.ToeL1, speed_walk, degree_walk * 1.25F, false, 1.0F, 0.1F, f, f1);
            this.walk(this.ToeL2, speed_walk, degree_walk * -1.75F, true, 1.0F, 0.1F, f, f1);
            this.walk(this.ToeL3, speed_walk, degree_walk * -1.75F, true, 1.0F, 0.1F, f, f1);
            this.walk(this.ToeL4, speed_walk, degree_walk * -1.75F, true, 1.0F, 0.1F, f, f1);
            this.walk(this.ToeR1, speed_walk, degree_walk * 1.25F, true, 1.0F, -0.1F, f, f1);
            this.walk(this.ToeR2, speed_walk, degree_walk * -1.75F, false, 1.0F, -0.1F, f, f1);
            this.walk(this.ToeR3, speed_walk, degree_walk * -1.75F, false, 1.0F, -0.1F, f, f1);
            this.walk(this.ToeL4_1, speed_walk, degree_walk * -1.75F, false, 1.0F, -0.1F, f, f1);
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.Body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.Body, this.LowerBody, this.Neck1, this.WingL, this.WingR, this.BackLegL1, this.BackLegR1, this.Lowerbodytilt, this.TailR1, this.TailL1, this.TailR2, this.TailL2, new AdvancedModelBox[] { this.BackLegL2, this.ToeL3, this.ToeL2, this.ToeL4, this.ToeL1, this.BackLegR2, this.ToeR3, this.ToeL4_1, this.ToeR2, this.ToeR1, this.Neck2, this.HeadBase, this.HeadFront, this.Jaw, this.Crest1, this.uppernail, this.Crest2, this.Crest3, this.WingL2, this.WingL3, this.WingL21, this.FingerL1, this.FingerL2, this.FingerL3, this.FingerL4, this.WingR2, this.WingR3, this.WingR21, this.FingerR1, this.FingerR2, this.FingerR3, this.FingerR4, this.HeadPivot, this.NeckPivot });
    }

    @Override
    public void renderStatue(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.m_7695_(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}