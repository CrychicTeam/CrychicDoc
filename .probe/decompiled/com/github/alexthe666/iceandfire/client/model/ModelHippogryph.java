package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;

public class ModelHippogryph extends ModelDragonBase<EntityHippogryph> {

    public AdvancedModelBox Body;

    public AdvancedModelBox Neck;

    public AdvancedModelBox HindThighR;

    public AdvancedModelBox Tail1;

    public AdvancedModelBox HindThighL;

    public AdvancedModelBox BackLegR1;

    public AdvancedModelBox BackLegR1_1;

    public AdvancedModelBox WingL;

    public AdvancedModelBox WingR;

    public AdvancedModelBox Saddle;

    public AdvancedModelBox Neck2;

    public AdvancedModelBox Crest1;

    public AdvancedModelBox Head;

    public AdvancedModelBox HeadPivot;

    public AdvancedModelBox Jaw;

    public AdvancedModelBox Beak;

    public AdvancedModelBox Quill_R;

    public AdvancedModelBox Quill_L;

    public AdvancedModelBox Crest1_1;

    public AdvancedModelBox NoseBand;

    public AdvancedModelBox BeakTip;

    public AdvancedModelBox Beak2;

    public AdvancedModelBox ReinL;

    public AdvancedModelBox ReinR;

    public AdvancedModelBox HindLegR;

    public AdvancedModelBox HindFootR;

    public AdvancedModelBox Tail2;

    public AdvancedModelBox Tail3;

    public AdvancedModelBox HindLegL;

    public AdvancedModelBox HindFootL;

    public AdvancedModelBox BackLegR2;

    public AdvancedModelBox ToeR3;

    public AdvancedModelBox ToeL4;

    public AdvancedModelBox ToeR2;

    public AdvancedModelBox ToeR1;

    public AdvancedModelBox BackLegR2_1;

    public AdvancedModelBox ToeR3_1;

    public AdvancedModelBox ToeL4_1;

    public AdvancedModelBox ToeR2_1;

    public AdvancedModelBox ToeR1_1;

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

    public AdvancedModelBox ChestR;

    public AdvancedModelBox ChestL;

    public AdvancedModelBox Saddleback;

    public AdvancedModelBox SaddleFront;

    public AdvancedModelBox StirrupL;

    public AdvancedModelBox StirrupR;

    public AdvancedModelBox StirrupIronL;

    public AdvancedModelBox StirrupIronR;

    private final ModelAnimator animator;

    public ModelHippogryph() {
        this.texWidth = 256;
        this.texHeight = 128;
        this.ChestR = new AdvancedModelBox(this, 0, 34);
        this.ChestR.setPos(-4.5F, 1.0F, 8.0F);
        this.ChestR.addBox(-3.0F, 0.0F, -3.0F, 8.0F, 8.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.ChestR, 0.0F, (float) (Math.PI / 2), 0.0F);
        this.StirrupIronL = new AdvancedModelBox(this, 74, 0);
        this.StirrupIronL.setPos(0.0F, 0.0F, 0.0F);
        this.StirrupIronL.addBox(-0.5F, 6.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F);
        this.WingR2 = new AdvancedModelBox(this, 80, 90);
        this.WingR2.setPos(-0.4F, 7.6F, -2.8F);
        this.WingR2.addBox(-0.4F, -2.5F, -2.1F, 1.0F, 11.0F, 11.0F, 0.0F);
        this.setRotateAngle(this.WingR2, 1.548107F, 0.0F, (float) (Math.PI / 18));
        this.FingerL3 = new AdvancedModelBox(this, 40, 80);
        this.FingerL3.setPos(0.0F, 15.0F, 4.4F);
        this.FingerL3.addBox(-0.8F, -0.1F, -2.0F, 1.0F, 16.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerL3, 0.08726646F, 0.0F, 0.0F);
        this.HindFootR = new AdvancedModelBox(this, 96, 51);
        this.HindFootR.setPos(0.0F, 5.0F, 0.0F);
        this.HindFootR.addBox(-1.5F, 0.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.0F);
        this.FingerR1 = new AdvancedModelBox(this, 60, 80);
        this.FingerR1.mirror = true;
        this.FingerR1.setPos(0.0F, 15.0F, 0.1F);
        this.FingerR1.addBox(-0.2F, -0.1F, -2.0F, 1.0F, 11.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerR1, 0.12217305F, 0.0F, 0.0F);
        this.BackLegR2_1 = new AdvancedModelBox(this, 81, 42);
        this.BackLegR2_1.mirror = true;
        this.BackLegR2_1.setPos(0.0F, 6.9F, 0.8F);
        this.BackLegR2_1.addBox(-1.0F, 0.0F, -0.7F, 2.0F, 10.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.BackLegR2_1, -0.31869712F, 0.0F, 0.0F);
        this.HindLegR = new AdvancedModelBox(this, 96, 43);
        this.HindLegR.setPos(0.0F, 7.0F, 0.0F);
        this.HindLegR.addBox(-1.0F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F);
        this.FingerL1 = new AdvancedModelBox(this, 60, 80);
        this.FingerL1.setPos(0.0F, 15.0F, 0.1F);
        this.FingerL1.addBox(-0.8F, -0.1F, -2.0F, 1.0F, 11.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerL1, 0.12217305F, 0.0F, 0.0F);
        this.FingerL4 = new AdvancedModelBox(this, 30, 80);
        this.FingerL4.setPos(0.0F, 15.0F, 6.6F);
        this.FingerL4.addBox(-0.9F, -0.1F, -2.0F, 1.0F, 11.0F, 3.0F, 0.0F);
        this.Saddleback = new AdvancedModelBox(this, 80, 9);
        this.Saddleback.setPos(0.0F, 0.0F, 0.0F);
        this.Saddleback.addBox(-4.0F, -1.0F, 3.0F, 8.0F, 1.0F, 2.0F, 0.0F);
        this.Tail3 = new AdvancedModelBox(this, 24, 3);
        this.Tail3.setPos(0.0F, -0.2F, 6.3F);
        this.Tail3.addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Tail3, (float) (-Math.PI / 9), 0.0F, 0.0F);
        this.WingR21 = new AdvancedModelBox(this, 80, 90);
        this.WingR21.setPos(0.5F, 0.0F, 0.0F);
        this.WingR21.addBox(-0.6F, -2.5F, -2.1F, 1.0F, 11.0F, 11.0F, 0.0F);
        this.Tail2 = new AdvancedModelBox(this, 38, 7);
        this.Tail2.setPos(0.0F, 0.0F, 2.6F);
        this.Tail2.addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 7.0F, 0.0F);
        this.ReinL = new AdvancedModelBox(this, 46, 55);
        this.ReinL.setPos(0.0F, 0.0F, 0.0F);
        this.ReinL.addBox(3.1F, -6.3F, -3.4F, 0.0F, 3.0F, 19.0F, 0.0F);
        this.setRotateAngle(this.ReinL, -0.04363323F, 0.0F, 0.0F);
        this.Tail1 = new AdvancedModelBox(this, 44, 0);
        this.Tail1.setPos(0.0F, -8.1F, 5.0F);
        this.Tail1.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.Tail1, -1.134464F, 0.0F, 0.0F);
        this.BackLegR1 = new AdvancedModelBox(this, 66, 40);
        this.BackLegR1.setPos(-4.2F, -3.9F, -17.0F);
        this.BackLegR1.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 8.0F, 4.0F, 0.0F);
        this.ChestL = new AdvancedModelBox(this, 0, 47);
        this.ChestL.setPos(4.5F, 1.0F, 8.0F);
        this.ChestL.addBox(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.ChestL, 0.0F, (float) (Math.PI / 2), 0.0F);
        this.ToeL4 = new AdvancedModelBox(this, 51, 43);
        this.ToeL4.mirror = true;
        this.ToeL4.setPos(-0.6F, 9.8F, 0.2F);
        this.ToeL4.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 5.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeL4, -1.2292354F, 0.95609134F, 0.0F);
        this.ToeR3_1 = new AdvancedModelBox(this, 51, 43);
        this.ToeR3_1.mirror = true;
        this.ToeR3_1.setPos(0.0F, 9.8F, -0.7F);
        this.ToeR3_1.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 5.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeR3_1, -1.0927507F, 0.0F, 0.0F);
        this.ToeR2_1 = new AdvancedModelBox(this, 51, 43);
        this.ToeR2_1.mirror = true;
        this.ToeR2_1.setPos(0.6F, 9.8F, 0.2F);
        this.ToeR2_1.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 5.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeR2_1, -1.1838568F, -0.95609134F, 0.0F);
        this.Quill_R = new AdvancedModelBox(this, 22, 99);
        this.Quill_R.setPos(-2.0F, -4.2F, 1.9F);
        this.Quill_R.addBox(-0.5F, -4.5F, -0.6F, 1.0F, 5.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.Quill_R, -1.1838568F, (float) (-Math.PI / 18), 0.0F);
        this.WingR3 = new AdvancedModelBox(this, 124, 86);
        this.WingR3.setPos(0.0F, 7.6F, 0.0F);
        this.WingR3.addBox(-0.3F, -0.1F, -2.0F, 1.0F, 18.0F, 10.0F, 0.0F);
        this.ToeR1_1 = new AdvancedModelBox(this, 51, 43);
        this.ToeR1_1.mirror = true;
        this.ToeR1_1.setPos(0.0F, 9.8F, 0.9F);
        this.ToeR1_1.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 5.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeR1_1, -1.821251F, (float) Math.PI, 0.0F);
        this.Beak = new AdvancedModelBox(this, 0, 84);
        this.Beak.setPos(0.0F, -0.6F, -4.8F);
        this.Beak.addBox(-2.01F, -3.2F, -4.0F, 4.0F, 4.0F, 5.0F, 0.0F);
        this.FingerR3 = new AdvancedModelBox(this, 40, 80);
        this.FingerR3.mirror = true;
        this.FingerR3.setPos(0.0F, 15.0F, 4.5F);
        this.FingerR3.addBox(-0.2F, -0.1F, -2.0F, 1.0F, 16.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerR3, 0.08726646F, 0.0F, 0.0F);
        this.FingerL2 = new AdvancedModelBox(this, 50, 80);
        this.FingerL2.setPos(-0.1F, 15.0F, 2.0F);
        this.FingerL2.addBox(-0.8F, -0.1F, -2.0F, 1.0F, 14.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerL2, 0.10471976F, 0.0F, 0.0F);
        this.BeakTip = new AdvancedModelBox(this, 14, 99);
        this.BeakTip.setPos(0.0F, 0.6F, -2.7F);
        this.BeakTip.addBox(-1.0F, -2.8F, -1.7F, 2.0F, 5.0F, 1.0F, 0.0F);
        this.Crest1_1 = new AdvancedModelBox(this, 30, 100);
        this.Crest1_1.setPos(0.0F, -5.4F, 3.1F);
        this.Crest1_1.addBox(0.0F, -8.0F, 0.1F, 1.0F, 8.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.Crest1_1, -2.2310543F, 0.0F, 0.0F);
        this.ToeR3 = new AdvancedModelBox(this, 51, 43);
        this.ToeR3.mirror = true;
        this.ToeR3.setPos(0.0F, 9.8F, -0.7F);
        this.ToeR3.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 5.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeR3, -1.0927507F, 0.0F, 0.0F);
        this.HindFootL = new AdvancedModelBox(this, 96, 51);
        this.HindFootL.mirror = true;
        this.HindFootL.setPos(0.0F, 5.0F, 0.0F);
        this.HindFootL.addBox(-2.5F, 0.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.0F);
        this.FingerR4 = new AdvancedModelBox(this, 30, 80);
        this.FingerR4.mirror = true;
        this.FingerR4.setPos(0.0F, 15.6F, 6.6F);
        this.FingerR4.addBox(-0.1F, -0.1F, -2.0F, 1.0F, 11.0F, 3.0F, 0.0F);
        this.BackLegR1_1 = new AdvancedModelBox(this, 66, 40);
        this.BackLegR1_1.mirror = true;
        this.BackLegR1_1.setPos(4.2F, -3.9F, -17.0F);
        this.BackLegR1_1.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 8.0F, 4.0F, 0.0F);
        this.WingL3 = new AdvancedModelBox(this, 124, 86);
        this.WingL3.mirror = true;
        this.WingL3.setPos(0.0F, 7.6F, 0.0F);
        this.WingL3.addBox(-0.7F, -0.1F, -2.0F, 1.0F, 18.0F, 10.0F, 0.0F);
        this.Jaw = new AdvancedModelBox(this, 24, 68);
        this.Jaw.setPos(0.0F, 0.4F, -3.0F);
        this.Jaw.addBox(-2.0F, -0.3F, -5.4F, 4.0F, 1.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Jaw, -0.045553092F, 0.0F, 0.0F);
        this.ToeL4_1 = new AdvancedModelBox(this, 51, 43);
        this.ToeL4_1.mirror = true;
        this.ToeL4_1.setPos(-0.6F, 9.8F, 0.2F);
        this.ToeL4_1.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 5.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeL4_1, -1.2292354F, 0.61086524F, 0.0F);
        this.HindLegL = new AdvancedModelBox(this, 96, 43);
        this.HindLegL.mirror = true;
        this.HindLegL.setPos(0.0F, 7.0F, 0.0F);
        this.HindLegL.addBox(-2.0F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F);
        this.ToeR1 = new AdvancedModelBox(this, 51, 43);
        this.ToeR1.mirror = true;
        this.ToeR1.setPos(0.0F, 9.8F, 0.9F);
        this.ToeR1.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 5.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeR1, -1.821251F, (float) Math.PI, 0.0F);
        this.Body = new AdvancedModelBox(this, 0, 34);
        this.Body.setPos(0.0F, 11.0F, 9.0F);
        this.Body.addBox(-5.0F, -8.0F, -19.0F, 10.0F, 10.0F, 24.0F, 0.0F);
        this.Saddle = new AdvancedModelBox(this, 80, 0);
        this.Saddle.setPos(0.0F, -8.9F, -7.0F);
        this.Saddle.addBox(-5.0F, 0.0F, -3.0F, 10.0F, 1.0F, 8.0F, 0.0F);
        this.WingL = new AdvancedModelBox(this, 100, 107);
        this.WingL.mirror = true;
        this.WingL.setPos(4.2F, -6.6F, -13.2F);
        this.WingL.addBox(-0.1F, 0.0F, -5.0F, 1.0F, 8.0F, 12.0F, 0.0F);
        this.setRotateAngle(this.WingL, 0.12217305F, 0.38397244F, (float) (-Math.PI * 2.0 / 9.0));
        this.WingR = new AdvancedModelBox(this, 100, 107);
        this.WingR.setPos(-4.2F, -6.6F, -13.2F);
        this.WingR.addBox(-0.9F, 0.0F, -5.0F, 1.0F, 8.0F, 12.0F, 0.0F);
        this.setRotateAngle(this.WingR, 0.12217305F, -0.38397244F, (float) (Math.PI * 2.0 / 9.0));
        this.WingL2 = new AdvancedModelBox(this, 80, 90);
        this.WingL2.mirror = true;
        this.WingL2.setPos(0.4F, 7.6F, -2.8F);
        this.WingL2.addBox(-0.6F, -2.5F, -2.1F, 1.0F, 11.0F, 11.0F, 0.0F);
        this.setRotateAngle(this.WingL2, 1.548107F, 0.0F, (float) (-Math.PI / 18));
        this.WingL21 = new AdvancedModelBox(this, 80, 90);
        this.WingL21.mirror = true;
        this.WingL21.setPos(-0.5F, 0.0F, 0.0F);
        this.WingL21.addBox(-0.4F, -2.5F, -2.1F, 1.0F, 11.0F, 11.0F, 0.0F);
        this.Crest1 = new AdvancedModelBox(this, 30, 100);
        this.Crest1.setPos(0.0F, -10.4F, 6.1F);
        this.Crest1.addBox(0.0F, -8.0F, 0.1F, 1.0F, 8.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.Crest1, -2.4586453F, 0.0F, 0.0F);
        this.Neck = new AdvancedModelBox(this, 1, 109);
        this.Neck.setPos(0.0F, -1.1F, -18.2F);
        this.Neck.addBox(-3.0F, -6.6F, -2.2F, 6.0F, 9.0F, 9.0F, 0.0F);
        this.setRotateAngle(this.Neck, 0.7740535F, 0.0F, 0.0F);
        this.Beak2 = new AdvancedModelBox(this, 0, 84);
        this.Beak2.setPos(0.0F, 0.1F, 0.0F);
        this.Beak2.addBox(-1.99F, -3.2F, -4.0F, 4.0F, 4.0F, 5.0F, 0.0F);
        this.StirrupIronR = new AdvancedModelBox(this, 74, 4);
        this.StirrupIronR.setPos(0.0F, 0.0F, 0.0F);
        this.StirrupIronR.addBox(-0.5F, 6.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F);
        this.HindThighR = new AdvancedModelBox(this, 96, 29);
        this.HindThighR.setPos(-4.0F, -1.0F, 2.0F);
        this.HindThighR.addBox(-1.5F, -2.0F, -2.5F, 4.0F, 9.0F, 5.0F, 0.0F);
        this.NoseBand = new AdvancedModelBox(this, 85, 60);
        this.NoseBand.setPos(0.0F, 6.5F, -2.2F);
        this.NoseBand.addBox(-3.0F, -11.1F, -7.0F, 6.0F, 6.0F, 12.0F, 0.0F);
        this.setRotateAngle(this.NoseBand, 0.091106184F, 0.0F, 0.0F);
        this.ToeR2 = new AdvancedModelBox(this, 51, 43);
        this.ToeR2.mirror = true;
        this.ToeR2.setPos(0.6F, 9.8F, 0.2F);
        this.ToeR2.addBox(-0.5F, -0.5F, -0.7F, 1.0F, 5.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.ToeR2, -1.1838568F, -0.61086524F, 0.0F);
        this.FingerR2 = new AdvancedModelBox(this, 50, 80);
        this.FingerR2.mirror = true;
        this.FingerR2.setPos(0.1F, 15.0F, 2.0F);
        this.FingerR2.addBox(-0.2F, -0.1F, -2.0F, 1.0F, 14.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.FingerR2, 0.10471976F, 0.0F, 0.0F);
        this.StirrupR = new AdvancedModelBox(this, 80, 0);
        this.StirrupR.setPos(-5.0F, 1.0F, 0.0F);
        this.StirrupR.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F);
        this.Head = new AdvancedModelBox(this, 0, 68);
        this.Head.addBox(-2.5F, -4.7F, -3.9F, 5.0F, 6.0F, 8.0F, 0.0F);
        this.HeadPivot = new AdvancedModelBox(this, 0, 68);
        this.HeadPivot.setPos(0.0F, -7.8F, 1.2F);
        this.setRotateAngle(this.HeadPivot, 0.3642502F, 0.0F, 0.0F);
        this.SaddleFront = new AdvancedModelBox(this, 106, 9);
        this.SaddleFront.setPos(0.0F, 0.0F, 0.0F);
        this.SaddleFront.addBox(-1.5F, -1.0F, -3.0F, 3.0F, 1.0F, 2.0F, 0.0F);
        this.StirrupL = new AdvancedModelBox(this, 70, 0);
        this.StirrupL.setPos(5.0F, 1.0F, 0.0F);
        this.StirrupL.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F);
        this.Quill_L = new AdvancedModelBox(this, 22, 99);
        this.Quill_L.mirror = true;
        this.Quill_L.setPos(2.0F, -4.3F, 1.9F);
        this.Quill_L.addBox(-0.5F, -4.5F, -0.6F, 1.0F, 5.0F, 2.0F, 0.0F);
        this.setRotateAngle(this.Quill_L, -1.1838568F, (float) (Math.PI / 18), 0.0F);
        this.BackLegR2 = new AdvancedModelBox(this, 81, 42);
        this.BackLegR2.setPos(0.0F, 6.9F, 0.8F);
        this.BackLegR2.addBox(-1.0F, 0.0F, -0.7F, 2.0F, 10.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.BackLegR2, -0.31869712F, 0.0F, 0.0F);
        this.HindThighL = new AdvancedModelBox(this, 96, 29);
        this.HindThighL.mirror = true;
        this.HindThighL.setPos(4.0F, -1.0F, 2.0F);
        this.HindThighL.addBox(-2.5F, -2.0F, -2.5F, 4.0F, 9.0F, 5.0F, 0.0F);
        this.Neck2 = new AdvancedModelBox(this, 36, 108);
        this.Neck2.setPos(0.0F, -6.8F, 0.2F);
        this.Neck2.addBox(-2.02F, -8.5F, -1.6F, 4.0F, 10.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Neck2, -0.68294734F, 0.0F, 0.0F);
        this.ReinR = new AdvancedModelBox(this, 46, 55);
        this.ReinR.mirror = true;
        this.ReinR.setPos(0.0F, 0.0F, 0.0F);
        this.ReinR.addBox(-3.1F, -6.0F, -3.4F, 0.0F, 3.0F, 19.0F, 0.0F);
        this.setRotateAngle(this.ReinR, -0.04363323F, 0.0F, 0.0F);
        this.BackLegR2.addChild(this.ToeR1);
        this.Tail1.addChild(this.Tail2);
        this.Saddle.addChild(this.StirrupL);
        this.BackLegR2.addChild(this.ToeL4);
        this.Head.addChild(this.NoseBand);
        this.WingL2.addChild(this.WingL21);
        this.WingL3.addChild(this.FingerL2);
        this.HindLegR.addChild(this.HindFootR);
        this.Neck.addChild(this.Crest1);
        this.BackLegR1.addChild(this.BackLegR2);
        this.Body.addChild(this.HindThighL);
        this.HindThighL.addChild(this.HindLegL);
        this.Body.addChild(this.BackLegR1_1);
        this.WingR3.addChild(this.FingerR3);
        this.Head.addChild(this.Jaw);
        this.Head.addChild(this.Quill_L);
        this.Head.addChild(this.Crest1_1);
        this.BackLegR2_1.addChild(this.ToeL4_1);
        this.WingL3.addChild(this.FingerL3);
        this.BackLegR2_1.addChild(this.ToeR3_1);
        this.WingR3.addChild(this.FingerR2);
        this.Body.addChild(this.WingL);
        this.Saddle.addChild(this.Saddleback);
        this.Beak.addChild(this.BeakTip);
        this.Saddle.addChild(this.SaddleFront);
        this.StirrupR.addChild(this.StirrupIronR);
        this.Neck.addChild(this.Neck2);
        this.Beak.addChild(this.Beak2);
        this.Body.addChild(this.Saddle);
        this.NoseBand.addChild(this.ReinR);
        this.BackLegR1_1.addChild(this.BackLegR2_1);
        this.WingL.addChild(this.WingL2);
        this.HindThighR.addChild(this.HindLegR);
        this.WingR2.addChild(this.WingR21);
        this.StirrupL.addChild(this.StirrupIronL);
        this.Body.addChild(this.Neck);
        this.NoseBand.addChild(this.ReinL);
        this.BackLegR2_1.addChild(this.ToeR1_1);
        this.WingL2.addChild(this.WingL3);
        this.WingR3.addChild(this.FingerR4);
        this.Saddle.addChild(this.StirrupR);
        this.WingL3.addChild(this.FingerL1);
        this.WingR.addChild(this.WingR2);
        this.BackLegR2_1.addChild(this.ToeR2_1);
        this.WingR3.addChild(this.FingerR1);
        this.Head.addChild(this.Beak);
        this.WingR2.addChild(this.WingR3);
        this.Neck2.addChild(this.HeadPivot);
        this.HeadPivot.addChild(this.Head);
        this.WingL3.addChild(this.FingerL4);
        this.Tail2.addChild(this.Tail3);
        this.BackLegR2.addChild(this.ToeR3);
        this.HindLegL.addChild(this.HindFootL);
        this.Body.addChild(this.Tail1);
        this.BackLegR2.addChild(this.ToeR2);
        this.Body.addChild(this.HindThighR);
        this.Body.addChild(this.BackLegR1);
        this.Saddle.addChild(this.ChestR);
        this.Body.addChild(this.WingR);
        this.Saddle.addChild(this.ChestL);
        this.Head.addChild(this.Quill_R);
        this.animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public void renderStatue(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.m_7695_(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (this.f_102610_) {
            this.Body.setShouldScaleChildren(true);
            this.Head.setShouldScaleChildren(false);
            this.Body.setScale(0.5F, 0.5F, 0.5F);
            this.Head.setScale(1.5F, 1.5F, 1.5F);
            this.Beak.setScale(0.75F, 0.75F, 0.75F);
            this.Quill_L.setScale(2.0F, 2.0F, 2.0F);
            this.Quill_R.setScale(2.0F, 2.0F, 2.0F);
            this.Body.setPos(0.0F, 18.0F, 4.0F);
        } else {
            this.Body.setScale(1.0F, 1.0F, 1.0F);
            this.Head.setScale(1.0F, 1.0F, 1.0F);
        }
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

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        if (this.animator.setAnimation(EntityHippogryph.ANIMATION_SPEAK)) {
            this.animator.startKeyframe(10);
            this.rotate(this.animator, this.Head, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Jaw, 20.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityHippogryph.ANIMATION_EAT)) {
            this.animator.startKeyframe(10);
            this.rotate(this.animator, this.Body, 10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck, 45.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck2, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Head, -50.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighR, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighL, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1_1, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Jaw, 20.0F, 0.0F, 0.0F);
            this.animator.move(this.BackLegR1, 0.0F, -2.0F, 0.5F);
            this.animator.move(this.BackLegR1_1, 0.0F, -2.0F, 0.5F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Body, 10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck, 45.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck2, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Head, -50.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighR, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighL, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1_1, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Jaw, 0.0F, 0.0F, 0.0F);
            this.animator.move(this.BackLegR1, 0.0F, -2.0F, 0.5F);
            this.animator.move(this.BackLegR1_1, 0.0F, -2.0F, 0.5F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Body, 10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck, 45.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck2, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Head, -50.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighR, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighL, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1_1, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Jaw, 20.0F, 0.0F, 0.0F);
            this.animator.move(this.BackLegR1, 0.0F, -2.0F, 0.5F);
            this.animator.move(this.BackLegR1_1, 0.0F, -2.0F, 0.5F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityHippogryph.ANIMATION_BITE)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Neck, -15.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck2, -15.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Head, 23.0F, 0.0F, 0.0F);
            this.animator.move(this.HeadPivot, 0.0F, -3.0F, -0.5F);
            this.rotate(this.animator, this.Jaw, 20.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Neck, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck2, 10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Head, -55.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Jaw, 45.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Neck, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck2, 10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Head, -75.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Jaw, 5.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityHippogryph.ANIMATION_SCRATCH)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Body, -35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighR, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighL, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck, 10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck2, 20.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Head, 5.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1, -50.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1_1, 50.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Body, -35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighR, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighL, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck, 10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck2, 20.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Head, 5.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1, 50.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1_1, -50.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Body, -35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighR, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighL, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck, 10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck2, 20.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Head, 5.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1, -50.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1_1, 50.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Body, -35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighR, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.HindThighL, 35.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck, 10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Neck2, 20.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Head, 5.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1, 50.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.BackLegR1_1, -50.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
    }

    public void setupAnim(EntityHippogryph entity, float f, float f1, float f2, float f3, float f4) {
        this.animate(entity, f, f1, f2, f3, f4, 1.0F);
        if (this.f_102610_) {
            this.Body.setShouldScaleChildren(true);
            this.Head.setShouldScaleChildren(false);
            this.Body.setScale(0.5F, 0.5F, 0.5F);
            this.Head.setScale(1.5F, 1.5F, 1.5F);
            this.Beak.setScale(0.75F, 0.75F, 0.75F);
            this.Quill_L.setScale(2.0F, 2.0F, 2.0F);
            this.Quill_R.setScale(2.0F, 2.0F, 2.0F);
            this.Body.setPos(0.0F, 18.0F, 4.0F);
        } else {
            this.Body.setScale(1.0F, 1.0F, 1.0F);
            this.Head.setScale(1.0F, 1.0F, 1.0F);
            this.Quill_L.setScale(1.0F, 1.0F, 1.0F);
            this.Quill_R.setScale(1.0F, 1.0F, 1.0F);
        }
        if (this.f_102610_) {
            this.progressPosition(this.Body, entity.sitProgress, 0.0F, 16.0F, 0.0F);
        } else {
            this.progressPosition(this.Body, entity.sitProgress, 0.0F, 18.0F, 0.0F);
        }
        float sitProgress = Math.max(entity.hoverProgress, entity.flyProgress);
        this.progressRotation(this.Beak, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.HindLegR, sitProgress, (float) (-Math.PI / 18), 0.0F, 0.0F);
        this.progressRotation(this.FingerR3, sitProgress, 0.40142572F, 0.0F, 0.0F);
        this.progressRotation(this.FingerL1, sitProgress, 0.034906585F, 0.0F, 0.0F);
        this.progressRotation(this.SaddleFront, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Jaw, sitProgress, -0.045553092F, 0.0F, 0.0F);
        this.progressRotation(this.FingerL4, sitProgress, (float) (Math.PI * 2.0 / 9.0), 0.0F, 0.0F);
        this.progressRotation(this.WingL, sitProgress, 0.08726646F, 0.0F, (float) (-Math.PI * 4.0 / 9.0));
        this.progressRotation(this.ToeR2_1, sitProgress, 0.4537856F, -0.4537856F, -0.4537856F);
        this.progressRotation(this.FingerR2, sitProgress, 0.2268928F, 0.0F, 0.0F);
        this.progressRotation(this.ToeR3_1, sitProgress, 0.4537856F, 0.0F, 0.0F);
        this.progressRotation(this.FingerR1, sitProgress, 0.034906585F, 0.0F, 0.0F);
        this.progressRotation(this.StirrupR, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.BackLegR1_1, sitProgress, (float) (Math.PI * 2.0 / 9.0), 0.0F, 0.0F);
        this.progressRotation(this.Crest1, sitProgress, -2.4586453F, 0.0F, 0.0F);
        this.progressRotation(this.ChestL, sitProgress, 0.0F, (float) (Math.PI / 2), 0.0F);
        this.progressRotation(this.Crest1_1, sitProgress, -2.2310543F, 0.0F, 0.0F);
        this.progressRotation(this.WingL3, sitProgress, (float) (Math.PI / 6), 0.0F, 0.0F);
        this.progressRotation(this.BackLegR1, sitProgress, (float) (Math.PI * 2.0 / 9.0), 0.0F, 0.0F);
        this.progressRotation(this.HindLegL, sitProgress, (float) (-Math.PI / 18), 0.0F, 0.0F);
        this.progressRotation(this.HindThighR, sitProgress, 0.7679449F, 0.0F, 0.0F);
        this.progressRotation(this.Body, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Quill_R, sitProgress, -1.1838568F, (float) (-Math.PI / 18), 0.0F);
        this.progressRotation(this.Neck2, sitProgress, -0.59184116F, 0.0F, 0.0F);
        this.progressRotation(this.WingL21, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.ReinL, sitProgress, -0.04363323F, 0.0F, 0.0F);
        this.progressRotation(this.FingerL3, sitProgress, 0.40142572F, 0.0F, 0.0F);
        this.progressRotation(this.ToeR1, sitProgress, -0.7679449F, (float) Math.PI, 0.0F);
        this.progressRotation(this.Saddleback, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.WingR2, sitProgress, (float) (-Math.PI / 9), 0.0F, (float) (Math.PI / 18));
        this.progressRotation(this.ToeL4, sitProgress, 0.4537856F, 0.4537856F, 0.4537856F);
        this.progressRotation(this.WingL2, sitProgress, (float) (-Math.PI / 9), 0.0F, (float) (-Math.PI / 18));
        this.progressRotation(this.ReinR, sitProgress, -0.04363323F, 0.0F, 0.0F);
        this.progressRotation(this.ToeR3, sitProgress, 0.4537856F, 0.0F, 0.0F);
        this.progressRotation(this.FingerR4, sitProgress, (float) (Math.PI * 2.0 / 9.0), 0.0F, 0.0F);
        this.progressRotation(this.Beak2, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.WingR, sitProgress, 0.08726646F, 0.0F, (float) (Math.PI * 4.0 / 9.0));
        this.progressRotation(this.ChestR, sitProgress, 0.0F, (float) (Math.PI / 2), 0.0F);
        this.progressRotation(this.ToeL4_1, sitProgress, 0.4537856F, 0.4537856F, 0.4537856F);
        this.progressRotation(this.Saddle, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.NoseBand, sitProgress, 0.091106184F, 0.0F, 0.0F);
        this.progressRotation(this.StirrupIronR, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Tail3, sitProgress, 0.27314404F, 0.0F, 0.0F);
        this.progressRotation(this.FingerL2, sitProgress, 0.2268928F, 0.0F, 0.0F);
        this.progressRotation(this.WingR3, sitProgress, (float) (Math.PI / 6), 0.0F, 0.0F);
        this.progressRotation(this.ToeR1_1, sitProgress, -0.7679449F, (float) Math.PI, 0.0F);
        this.progressRotation(this.StirrupL, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Tail1, sitProgress, -0.7285004F, 0.0F, 0.0F);
        this.progressRotation(this.BackLegR2_1, sitProgress, (float) (-Math.PI * 4.0 / 9.0), 0.0F, 0.0F);
        this.progressRotation(this.StirrupIronL, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.ToeR2, sitProgress, 0.4537856F, -0.4537856F, -0.4537856F);
        this.progressRotation(this.HindFootR, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.HindThighL, sitProgress, 0.7679449F, 0.0F, 0.0F);
        this.progressRotation(this.BackLegR2, sitProgress, (float) (-Math.PI * 4.0 / 9.0), 0.0F, 0.0F);
        this.progressRotation(this.WingR21, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Neck, sitProgress, 1.1838568F, 0.0F, 0.0F);
        this.progressRotation(this.BeakTip, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Tail2, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.HeadPivot, sitProgress, -0.13665928F, 0.0F, 0.0F);
        this.progressRotation(this.Quill_L, sitProgress, -1.1838568F, (float) (Math.PI / 18), 0.0F);
        this.progressRotation(this.HindFootL, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressPositionPrev(this.HindThighL, sitProgress, 0.0F, -0.75F, 0.0F);
        this.progressPositionPrev(this.HindThighR, sitProgress, 0.0F, -0.75F, 0.0F);
        sitProgress = entity.sitProgress;
        this.progressRotation(this.HeadPivot, sitProgress, -0.13665928F, 0.0F, 0.0F);
        this.progressRotation(this.HindLegR, sitProgress, 1.548107F, 0.0F, 0.0F);
        this.progressRotation(this.FingerL4, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.StirrupL, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.ToeR1_1, sitProgress, (float) (-Math.PI * 7.0 / 9.0), (float) Math.PI, 0.0F);
        this.progressRotation(this.Jaw, sitProgress, -0.045553092F, 0.0F, 0.0F);
        this.progressRotation(this.BackLegR2_1, sitProgress, -2.1399481F, 0.0F, 0.0F);
        this.progressRotation(this.ToeR2_1, sitProgress, -0.2268928F, -0.2268928F, (float) (-Math.PI / 9));
        this.progressRotation(this.ReinR, sitProgress, -0.04363323F, 0.0F, 0.0F);
        this.progressRotation(this.StirrupR, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.ToeR1, sitProgress, (float) (-Math.PI * 7.0 / 9.0), (float) Math.PI, 0.0F);
        this.progressRotation(this.ToeR3, sitProgress, (float) (-Math.PI / 12), 0.0F, 0.0F);
        this.progressRotation(this.BackLegR1_1, sitProgress, 1.1383038F, 0.0F, 0.0F);
        this.progressRotation(this.WingL21, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.BeakTip, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Tail2, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.FingerR3, sitProgress, 0.08726646F, 0.0F, 0.0F);
        this.progressRotation(this.Head, sitProgress, 0.3642502F, 0.0F, 0.0F);
        this.progressRotation(this.BackLegR1, sitProgress, 1.1838568F, 0.0F, 0.0F);
        this.progressRotation(this.HindFootR, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.FingerL1, sitProgress, 0.12217305F, 0.0F, 0.0F);
        this.progressRotation(this.FingerL3, sitProgress, 0.08726646F, 0.0F, 0.0F);
        this.progressRotation(this.WingR21, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.ToeL4, sitProgress, -0.2268928F, 0.2268928F, (float) (Math.PI / 9));
        this.progressRotation(this.HindLegL, sitProgress, 1.548107F, 0.0F, 0.0F);
        this.progressRotation(this.Saddleback, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.FingerR1, sitProgress, 0.12217305F, 0.0F, 0.0F);
        this.progressRotation(this.WingR, sitProgress, (float) (-Math.PI / 12), (float) (-Math.PI / 12), 0.4537856F);
        this.progressRotation(this.WingR2, sitProgress, 1.548107F, 0.0F, (float) (Math.PI / 18));
        this.progressRotation(this.NoseBand, sitProgress, 0.091106184F, 0.0F, 0.0F);
        this.progressRotation(this.WingL, sitProgress, (float) (-Math.PI / 12), (float) (Math.PI / 12), -0.4537856F);
        this.progressRotation(this.FingerL2, sitProgress, 0.10471976F, 0.0F, 0.0F);
        this.progressRotation(this.ReinL, sitProgress, -0.04363323F, 0.0F, 0.0F);
        this.progressRotation(this.WingL3, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Tail3, sitProgress, (float) (-Math.PI / 9), 0.0F, 0.0F);
        this.progressRotation(this.Saddle, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.FingerR2, sitProgress, 0.10471976F, 0.0F, 0.0F);
        this.progressRotation(this.HindThighL, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.WingR3, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Neck2, sitProgress, -0.68294734F, 0.0F, 0.0F);
        this.progressRotation(this.StirrupIronR, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Body, sitProgress, 0.045553092F, 0.0F, 0.0F);
        this.progressRotation(this.Neck, sitProgress, 0.7740535F, 0.0F, 0.0F);
        this.progressRotation(this.Tail1, sitProgress, -1.134464F, 0.0F, 0.0F);
        this.progressRotation(this.ToeR3_1, sitProgress, (float) (-Math.PI / 12), 0.0F, 0.0F);
        this.progressRotation(this.ChestR, sitProgress, 0.0F, (float) (Math.PI / 2), 0.0F);
        this.progressRotation(this.SaddleFront, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.ToeL4_1, sitProgress, -0.2268928F, 0.2268928F, (float) (Math.PI / 9));
        this.progressRotation(this.WingL2, sitProgress, 1.548107F, 0.0F, (float) (-Math.PI / 18));
        this.progressRotation(this.Crest1_1, sitProgress, -2.2310543F, 0.0F, 0.0F);
        this.progressRotation(this.ChestL, sitProgress, 0.0F, (float) (Math.PI / 2), 0.0F);
        this.progressRotation(this.Quill_L, sitProgress, -1.1838568F, (float) (Math.PI / 18), 0.0F);
        this.progressRotation(this.Quill_R, sitProgress, -1.1838568F, (float) (-Math.PI / 18), 0.0F);
        this.progressRotation(this.StirrupIronL, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.BackLegR2, sitProgress, -2.1399481F, 0.0F, 0.0F);
        this.progressRotation(this.Beak2, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.Crest1, sitProgress, -2.4586453F, 0.0F, 0.0F);
        this.progressRotation(this.Beak, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.FingerR4, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.HindFootL, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.HindThighR, sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.ToeR2, sitProgress, -0.2268928F, -0.2268928F, (float) (-Math.PI / 9));
        this.progressPositionPrev(this.HindThighL, sitProgress, 0.0F, -0.75F, 0.0F);
        this.progressPositionPrev(this.HindThighR, sitProgress, 0.0F, -0.75F, 0.0F);
        sitProgress = 0.4F;
        float speed_idle = 0.05F;
        float speed_fly = 0.35F + (entity.getEnumVariant() == EnumHippogryphTypes.DODO ? 0.2F : 0.0F);
        float degree_walk = 0.5F;
        float degree_idle = 0.5F;
        float degree_fly = 0.5F + (entity.getEnumVariant() == EnumHippogryphTypes.DODO ? 1.0F : 0.0F);
        this.bob(this.Body, speed_idle, degree_idle, false, f2, 1.0F);
        this.bob(this.BackLegR1, -speed_idle, degree_idle, false, f2, 1.0F);
        this.bob(this.BackLegR1_1, -speed_idle, degree_idle, false, f2, 1.0F);
        this.bob(this.HindThighR, -speed_idle, degree_idle, false, f2, 1.0F);
        this.bob(this.HindThighL, -speed_idle, degree_idle, false, f2, 1.0F);
        AdvancedModelBox[] NECK = new AdvancedModelBox[] { this.Neck, this.Neck2, this.Head };
        this.chainWave(NECK, speed_idle, degree_idle * 0.15F, -2.0, f2, 1.0F);
        if (!entity.isFlying() && entity.airBorneCounter <= 50 && !entity.isHovering()) {
            this.faceTarget(f3, f4, 3.0F, NECK);
            this.bob(this.Body, sitProgress, degree_walk, false, f, f1);
            this.bob(this.BackLegR1, -sitProgress, degree_walk, false, f, f1);
            this.bob(this.BackLegR1_1, -sitProgress, degree_walk, false, f, f1);
            this.bob(this.HindThighR, -sitProgress, degree_walk, false, f, f1);
            this.bob(this.HindThighL, -sitProgress, degree_walk, false, f, f1);
            this.chainWave(NECK, sitProgress, degree_walk * 0.15F, -2.0, f, f1);
            this.walk(this.BackLegR1, sitProgress, degree_walk * -0.75F, true, 0.0F, 0.0F, f, f1);
            this.walk(this.BackLegR1_1, sitProgress, degree_walk * -0.75F, false, 0.0F, 0.0F, f, f1);
            this.walk(this.BackLegR2, sitProgress, degree_walk * 0.5F, false, 0.0F, 0.0F, f, f1);
            this.walk(this.BackLegR2_1, sitProgress, degree_walk * 0.5F, true, 0.0F, 0.0F, f, f1);
            this.walk(this.HindThighR, sitProgress, degree_walk * -0.75F, false, 0.0F, 0.0F, f, f1);
            this.walk(this.HindThighL, sitProgress, degree_walk * -0.75F, true, 0.0F, 0.0F, f, f1);
            this.walk(this.HindLegR, sitProgress, degree_walk * 0.5F, true, 0.0F, 0.0F, f, f1);
            this.walk(this.HindLegL, sitProgress, degree_walk * 0.5F, false, 0.0F, 0.0F, f, f1);
            this.walk(this.HindFootR, -sitProgress, degree_walk, true, 2.75F, 0.0F, f, f1);
            this.walk(this.HindFootL, -sitProgress, degree_walk, false, 2.75F, 0.0F, f, f1);
        } else {
            this.flap(this.WingL, speed_fly, degree_fly, false, 0.0F, 0.0F, f2, 1.0F);
            this.flap(this.WingR, speed_fly, -degree_fly, false, 0.0F, 0.0F, f2, 1.0F);
            this.flap(this.WingL2, speed_fly, degree_fly, false, 0.0F, 0.0F, f2, 1.0F);
            this.flap(this.WingR2, speed_fly, -degree_fly, false, 0.0F, 0.0F, f2, 1.0F);
        }
        float f12 = -1.134464F + f1;
        if (f12 > 0.0F) {
            f12 = 0.0F;
        }
        if ((double) f12 < Math.toRadians(-80.0)) {
            f12 = (float) Math.toRadians(-80.0);
        }
        this.Tail1.rotateAngleX = f12;
        f12 = 0.0F;
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.Body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.Body, this.Neck, this.HindThighR, this.Tail1, this.HindThighL, this.BackLegR1, this.BackLegR1_1, this.WingL, this.WingR, this.Saddle, this.Neck2, this.Crest1, new AdvancedModelBox[] { this.Head, this.HeadPivot, this.Jaw, this.Beak, this.Quill_R, this.Quill_L, this.Crest1_1, this.NoseBand, this.BeakTip, this.Beak2, this.ReinL, this.ReinR, this.HindLegR, this.HindFootR, this.Tail2, this.Tail3, this.HindLegL, this.HindFootL, this.BackLegR2, this.ToeR3, this.ToeL4, this.ToeR2, this.ToeR1, this.BackLegR2_1, this.ToeR3_1, this.ToeL4_1, this.ToeR2_1, this.ToeR1_1, this.WingL2, this.WingL3, this.WingL21, this.FingerL1, this.FingerL2, this.FingerL3, this.FingerL4, this.WingR2, this.WingR3, this.WingR21, this.FingerR1, this.FingerR2, this.FingerR3, this.FingerR4, this.ChestR, this.ChestL, this.Saddleback, this.SaddleFront, this.StirrupL, this.StirrupR, this.StirrupIronL, this.StirrupIronR });
    }
}