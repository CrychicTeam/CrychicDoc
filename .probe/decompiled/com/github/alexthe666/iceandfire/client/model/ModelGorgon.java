package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ModelGorgon extends ModelDragonBase<EntityGorgon> {

    private final ModelAnimator animator;

    public AdvancedModelBox Tail_1;

    public AdvancedModelBox Tail_2;

    public AdvancedModelBox Body;

    public AdvancedModelBox Tail_3;

    public AdvancedModelBox Tail_4;

    public AdvancedModelBox Tail_5;

    public AdvancedModelBox Tail_6;

    public AdvancedModelBox Tail_7;

    public AdvancedModelBox Tail_8;

    public AdvancedModelBox Tail_9;

    public AdvancedModelBox Left_Arm;

    public AdvancedModelBox Head;

    public AdvancedModelBox Right_Arm;

    public AdvancedModelBox Neck;

    public AdvancedModelBox Head_Details;

    public AdvancedModelBox SnakeBaseR2;

    public AdvancedModelBox SnakeBaseR7;

    public AdvancedModelBox SnakeBaseR6;

    public AdvancedModelBox SnakeBaseR5;

    public AdvancedModelBox SnakeBaseR4;

    public AdvancedModelBox SnakeBaseR3;

    public AdvancedModelBox SnakeBaseR1;

    public AdvancedModelBox SnakeBaseL1;

    public AdvancedModelBox SnakeBaseL2;

    public AdvancedModelBox SnakeBaseL4;

    public AdvancedModelBox SnakeBaseL3;

    public AdvancedModelBox SnakeBaseL7;

    public AdvancedModelBox SnakeBaseL6;

    public AdvancedModelBox SnakeBaseL5;

    public AdvancedModelBox SnakeBodyR2;

    public AdvancedModelBox SnakeHeadR2;

    public AdvancedModelBox SnakeJawR2;

    public AdvancedModelBox SnakeFang1R2;

    public AdvancedModelBox SnakeFang2R2;

    public AdvancedModelBox SnakeBodyR7;

    public AdvancedModelBox SnakeHeadR7;

    public AdvancedModelBox SnakeJawR7;

    public AdvancedModelBox SnakeFang1R7;

    public AdvancedModelBox SnakeFang2R7;

    public AdvancedModelBox SnakeBodyR6;

    public AdvancedModelBox SnakeHeadR6;

    public AdvancedModelBox SnakeJawR6;

    public AdvancedModelBox SnakeFang1R6;

    public AdvancedModelBox SnakeFang2R6;

    public AdvancedModelBox SnakeBodyR5;

    public AdvancedModelBox SnakeHeadR5;

    public AdvancedModelBox SnakeJawR5;

    public AdvancedModelBox SnakeFang1R5;

    public AdvancedModelBox SnakeFang2R5;

    public AdvancedModelBox SnakeBodyR4;

    public AdvancedModelBox SnakeHeadR4;

    public AdvancedModelBox SnakeJawR4;

    public AdvancedModelBox SnakeFang1R4;

    public AdvancedModelBox SnakeFang2R4;

    public AdvancedModelBox SnakeBodyR3;

    public AdvancedModelBox SnakeHeadR3;

    public AdvancedModelBox SnakeJawR3;

    public AdvancedModelBox SnakeFang1R3;

    public AdvancedModelBox SnakeFang2R3;

    public AdvancedModelBox SnakeBodyR1;

    public AdvancedModelBox SnakeHeadR1;

    public AdvancedModelBox SnakeJawR1;

    public AdvancedModelBox SnakeFang1R1;

    public AdvancedModelBox SnakeFang2R1;

    public AdvancedModelBox SnakeBodyL1;

    public AdvancedModelBox SnakeHeadL1;

    public AdvancedModelBox SnakeJawL1;

    public AdvancedModelBox SnakeFang1L1;

    public AdvancedModelBox SnakeFang2L1;

    public AdvancedModelBox SnakeBodyL2;

    public AdvancedModelBox SnakeHeadL2;

    public AdvancedModelBox SnakeJawL2;

    public AdvancedModelBox SnakeFang1L2;

    public AdvancedModelBox SnakeFang2L2;

    public AdvancedModelBox SnakeBodyL4;

    public AdvancedModelBox SnakeHeadR4_1;

    public AdvancedModelBox SnakeJawR4_1;

    public AdvancedModelBox SnakeFang1R4_1;

    public AdvancedModelBox SnakeFang2R4_1;

    public AdvancedModelBox SnakeBodyL3;

    public AdvancedModelBox SnakeHeadL3;

    public AdvancedModelBox SnakeJawL3;

    public AdvancedModelBox SnakeFang1L3;

    public AdvancedModelBox SnakeFang2L3;

    public AdvancedModelBox SnakeBodyL7;

    public AdvancedModelBox SnakeHeadL7;

    public AdvancedModelBox SnakeJawL7;

    public AdvancedModelBox SnakeFang1L7;

    public AdvancedModelBox SnakeFang2L7;

    public AdvancedModelBox snakeBodyL6;

    public AdvancedModelBox SnakeHeadL6;

    public AdvancedModelBox SnakeJawL6;

    public AdvancedModelBox SnakeFang1L6;

    public AdvancedModelBox SnakeFang2L6;

    public AdvancedModelBox SnakeBodyL5;

    public AdvancedModelBox SnakeHeadL5;

    public AdvancedModelBox SnakeJawL5;

    public AdvancedModelBox SnakeFang1L5;

    public AdvancedModelBox SnakeFang2L5;

    public ModelGorgon() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.SnakeBodyR4 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyR4.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyR4.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyR4, 1.515993F, 0.0F, 0.0F);
        this.SnakeFang2R7 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2R7.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2R7.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeJawL5 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawL5.setPos(0.0F, 0.31F, -0.3F);
        this.SnakeJawL5.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.SnakeBaseR6 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseR6.setPos(-3.0F, -6.5F, 2.3F);
        this.SnakeBaseR6.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseR6, -1.7976891F, 0.0F, -1.9896754F);
        this.Head = new AdvancedModelBox(this, 0, 0);
        this.Head.setPos(0.0F, -12.0F, 0.0F);
        this.Head.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.Head, 0.067718774F, 0.0F, 0.0F);
        this.Tail_8 = new AdvancedModelBox(this, 93, 27);
        this.Tail_8.setPos(0.0F, 8.2F, 0.4F);
        this.Tail_8.addBox(-2.0F, 0.1F, -1.9F, 4.0F, 10.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.Tail_8, 0.045553092F, 0.0F, 0.0F);
        this.SnakeHeadR2 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadR2.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadR2.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadR2, 0.7749262F, -0.0F, (float) (-Math.PI / 18));
        this.SnakeHeadR4 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadR4.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadR4.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadR4, 0.51975906F, 0.0F, 0.27925268F);
        this.SnakeFang2R4_1 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2R4_1.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2R4_1.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeBaseR1 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseR1.setPos(-3.0F, -5.5F, -2.7F);
        this.SnakeBaseR1.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseR1, -1.6046557F, 0.0F, -0.2268928F);
        this.SnakeJawR3 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawR3.setPos(0.0F, 0.11F, -0.3F);
        this.SnakeJawR3.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.SnakeBodyL1 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyL1.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyL1.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyL1, 1.6498598F, 0.0F, 0.0F);
        this.SnakeBodyL3 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyL3.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyL3.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyL3, 1.0622073F, 0.7457792F, 0.0F);
        this.SnakeJawR4 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawR4.setPos(0.0F, 0.11F, -0.3F);
        this.SnakeJawR4.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.Tail_6 = new AdvancedModelBox(this, 80, 94);
        this.Tail_6.setPos(0.0F, 13.18F, -0.09F);
        this.Tail_6.addBox(-3.0F, -1.0F, -2.4F, 6.0F, 13.0F, 6.0F, 0.0F);
        this.SnakeJawL1 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawL1.setPos(0.0F, 0.31F, -0.3F);
        this.SnakeJawL1.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.SnakeFang1R2 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1R2.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1R2.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeFang2R4 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2R4.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2R4.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.Tail_1 = new AdvancedModelBox(this, 2, 72);
        this.Tail_1.setPos(0.0F, 6.2F, -5.0F);
        this.Tail_1.addBox(-4.0F, 0.0F, -2.4F, 8.0F, 6.0F, 5.0F, 0.1F);
        this.setRotateAngle(this.Tail_1, -0.022514747F, 0.0F, 0.0F);
        this.SnakeBaseL6 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseL6.setPos(3.0F, -6.5F, 2.3F);
        this.SnakeBaseL6.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseL6, -1.7976891F, 0.0F, 1.9896754F);
        this.SnakeHeadL6 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadL6.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadL6.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadL6, 0.7679449F, 0.61086524F, -0.87266463F);
        this.SnakeBodyL5 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyL5.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyL5.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyL5, (float) (Math.PI * 2.0 / 9.0), (float) (Math.PI / 3), 0.27925268F);
        this.SnakeJawR1 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawR1.setPos(0.0F, 0.11F, -0.3F);
        this.SnakeJawR1.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.Tail_5 = new AdvancedModelBox(this, 79, 68);
        this.Tail_5.setPos(0.0F, 7.7F, -0.1F);
        this.Tail_5.addBox(-3.52F, 0.0F, -2.7F, 7.0F, 14.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Tail_5, 0.27314404F, 0.0F, 0.0F);
        this.Tail_4 = new AdvancedModelBox(this, 38, 94);
        this.Tail_4.setPos(0.0F, 5.6F, 0.1F);
        this.Tail_4.addBox(-3.52F, 0.0F, -2.7F, 7.0F, 9.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Tail_4, 0.4553564F, 0.0F, 0.0F);
        this.SnakeBodyR7 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyR7.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyR7.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyR7, 0.87266463F, -1.4690436F, 0.0F);
        this.SnakeHeadR6 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadR6.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadR6.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadR6, 0.7679449F, -0.61086524F, 0.87266463F);
        this.Tail_2 = new AdvancedModelBox(this, 38, 78);
        this.Tail_2.setPos(0.0F, 5.3F, -0.5F);
        this.Tail_2.addBox(-4.0F, 0.0F, -2.4F, 8.0F, 6.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Tail_2, 0.1129228F, 0.0F, 0.0F);
        this.Head_Details = new AdvancedModelBox(this, 32, 0);
        this.Head_Details.setPos(0.0F, 0.0F, 0.0F);
        this.Head_Details.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F);
        this.SnakeFang2R1 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2R1.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2R1.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeFang2L6 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2L6.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2L6.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.Right_Arm = new AdvancedModelBox(this, 40, 16);
        this.Right_Arm.setPos(-5.0F, -10.0F, 0.0F);
        this.Right_Arm.addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F);
        this.SnakeHeadR1 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadR1.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadR1.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadR1, -0.1129228F, -0.067718774F, 0.20333086F);
        this.Body = new AdvancedModelBox(this, 16, 16);
        this.Body.setPos(0.0F, 0.9F, 0.1F);
        this.Body.addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.Body, 0.022514747F, 0.0F, 0.0F);
        this.SnakeBodyR5 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyR5.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyR5.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyR5, (float) (Math.PI * 2.0 / 9.0), (float) (-Math.PI / 3), -0.27925268F);
        this.SnakeHeadL1 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadL1.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadL1.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadL1, -0.1129228F, 0.067718774F, -0.20333086F);
        this.SnakeJawL6 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawL6.setPos(0.0F, 0.31F, -0.3F);
        this.SnakeJawL6.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.SnakeBodyL4 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyL4.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyL4.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyL4, 1.515993F, 0.0F, 0.0F);
        this.Tail_9 = new AdvancedModelBox(this, 76, 33);
        this.Tail_9.setPos(0.0F, 8.8F, -0.1F);
        this.Tail_9.addBox(-1.5F, 0.1F, -1.3F, 3.0F, 10.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.Tail_9, 0.045553092F, 0.0F, 0.0F);
        this.SnakeHeadR7 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadR7.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadR7.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadR7, 0.8813913F, -0.429351F, 0.83618724F);
        this.SnakeBaseL1 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseL1.setPos(3.0F, -5.5F, -2.7F);
        this.SnakeBaseL1.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseL1, -1.6046557F, 0.0F, 0.2268928F);
        this.SnakeFang2R2 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2R2.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2R2.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeFang1L1 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1L1.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1L1.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeFang1L3 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1L3.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1L3.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeBaseR4 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseR4.setPos(-1.0F, -6.5F, 3.3F);
        this.SnakeBaseR4.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseR4, -2.1282544F, 0.0F, (float) (-Math.PI / 12));
        this.SnakeFang2R5 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2R5.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2R5.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeBodyR3 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyR3.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyR3.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyR3, 1.0622073F, -0.7457792F, 0.0F);
        this.SnakeJawR2 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawR2.setPos(0.0F, 0.11F, -0.3F);
        this.SnakeJawR2.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.SnakeHeadL2 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadL2.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadL2.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadL2, 0.7749262F, -0.0F, (float) (Math.PI / 18));
        this.SnakeBaseL2 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseL2.setPos(2.0F, -7.5F, -0.7F);
        this.SnakeBaseL2.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseL2, (float) (-Math.PI / 2), 0.0F, 2.268928F);
        this.SnakeJawR7 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawR7.setPos(0.0F, 0.11F, -0.3F);
        this.SnakeJawR7.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.Tail_3 = new AdvancedModelBox(this, 0, 91);
        this.Tail_3.setPos(0.0F, 4.1F, -0.9F);
        this.Tail_3.addBox(-3.5F, 0.0F, -2.4F, 7.0F, 7.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.Tail_3, 0.7398451F, 0.0F, 0.0F);
        this.SnakeBaseL5 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseL5.setPos(1.0F, -4.5F, 3.3F);
        this.SnakeBaseL5.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseL5, -2.1994638F, 0.15812683F, 2.2558382F);
        this.SnakeJawL2 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawL2.setPos(0.0F, 0.31F, -0.3F);
        this.SnakeJawL2.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.SnakeBaseR7 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseR7.setPos(-4.0F, -3.5F, 1.3F);
        this.SnakeBaseR7.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseR7, (float) (-Math.PI / 2), 0.0F, -2.2734659F);
        this.SnakeFang2L3 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2L3.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2L3.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeHeadR3 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadR3.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadR3.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadR3, 0.69429195F, -0.06981317F, (float) (Math.PI / 20));
        this.SnakeFang2L7 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2L7.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2L7.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeFang1R5 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1R5.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1R5.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeBodyL7 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyL7.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyL7.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyL7, 0.87266463F, 1.4690436F, 0.0F);
        this.SnakeFang1L7 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1L7.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1L7.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeFang2L5 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2L5.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2L5.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeJawR5 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawR5.setPos(0.0F, 0.11F, -0.3F);
        this.SnakeJawR5.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.SnakeBaseL7 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseL7.setPos(4.0F, -3.5F, 1.3F);
        this.SnakeBaseL7.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseL7, (float) (-Math.PI / 2), 0.0F, 2.2734659F);
        this.SnakeFang1R1 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1R1.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1R1.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeJawL7 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawL7.setPos(0.0F, 0.31F, -0.3F);
        this.SnakeJawL7.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.Left_Arm = new AdvancedModelBox(this, 40, 16);
        this.Left_Arm.mirror = true;
        this.Left_Arm.setPos(5.0F, -10.0F, 0.0F);
        this.Left_Arm.addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F);
        this.SnakeHeadR4_1 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadR4_1.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadR4_1.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadR4_1, 0.51975906F, 0.0F, -0.27925268F);
        this.SnakeFang2R6 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2R6.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2R6.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeBodyR1 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyR1.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyR1.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyR1, 1.6498598F, 0.0F, 0.0F);
        this.SnakeHeadL7 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadL7.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadL7.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadL7, 0.8813913F, 0.429351F, -0.83618724F);
        this.SnakeFang1R4_1 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1R4_1.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1R4_1.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeFang1R4 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1R4.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1R4.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeBodyR2 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyR2.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyR2.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyR2, 0.8651597F, -2.4586453F, 0.0F);
        this.SnakeBaseR5 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseR5.setPos(-1.0F, -4.5F, 3.3F);
        this.SnakeBaseR5.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseR5, -2.1994638F, -0.15812683F, -2.2558382F);
        this.SnakeJawL3 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawL3.setPos(0.0F, 0.31F, -0.3F);
        this.SnakeJawL3.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.SnakeBaseL4 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseL4.setPos(1.0F, -6.5F, 3.3F);
        this.SnakeBaseL4.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseL4, -2.1282544F, 0.0F, (float) (Math.PI / 12));
        this.SnakeFang2R3 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2R3.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2R3.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeHeadL3 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadL3.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadL3.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadL3, 0.69429195F, 0.06981317F, (float) (Math.PI / 20));
        this.SnakeBodyR6 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyR6.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyR6.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyR6, 1.3786355F, -1.0170033F, -0.6553711F);
        this.SnakeFang1R3 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1R3.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1R3.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeBaseR2 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseR2.setPos(-2.0F, -7.5F, -0.7F);
        this.SnakeBaseR2.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseR2, (float) (-Math.PI / 2), 0.0F, -2.268928F);
        this.SnakeBaseR3 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseR3.setPos(-3.0F, -6.5F, 1.3F);
        this.SnakeBaseR3.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseR3, -1.796642F, -0.022514747F, -0.9203121F);
        this.SnakeFang1R6 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1R6.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1R6.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeFang1L6 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1L6.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1L6.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeFang2L2 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2L2.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2L2.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.Neck = new AdvancedModelBox(this, 40, 25);
        this.Neck.setPos(0.0F, -12.0F, 0.0F);
        this.Neck.addBox(-3.0F, -3.7F, -1.0F, 6.0F, 4.0F, 1.0F, 0.0F);
        this.SnakeFang2L1 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang2L1.setPos(1.75F, -0.09F, -0.2F);
        this.SnakeFang2L1.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeFang1L2 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1L2.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1L2.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeHeadR5 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadR5.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadR5.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadR5, 0.7679449F, -1.0170033F, 0.87266463F);
        this.SnakeJawR4_1 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawR4_1.setPos(0.0F, 0.31F, -0.3F);
        this.SnakeJawR4_1.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.SnakeFang1R7 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1R7.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1R7.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.Tail_7 = new AdvancedModelBox(this, 88, 48);
        this.Tail_7.setPos(0.0F, 11.2F, -0.1F);
        this.Tail_7.addBox(-2.5F, -0.9F, -1.9F, 5.0F, 11.0F, 5.0F, 0.0F);
        this.SnakeHeadL5 = new AdvancedModelBox(this, 6, 36);
        this.SnakeHeadL5.setPos(0.0F, 0.1F, -4.3F);
        this.SnakeHeadL5.addBox(-1.0F, -1.3F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.SnakeHeadL5, 0.7679449F, 1.0170033F, -0.87266463F);
        this.SnakeFang1L5 = new AdvancedModelBox(this, 31, 37);
        this.SnakeFang1L5.setPos(0.25F, -0.09F, -0.2F);
        this.SnakeFang1L5.addBox(-1.0F, -0.3F, -2.2F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.SnakeJawR6 = new AdvancedModelBox(this, 15, 37);
        this.SnakeJawR6.setPos(0.0F, 0.11F, -0.3F);
        this.SnakeJawR6.addBox(-1.0F, -0.3F, -2.2F, 2.0F, 1.0F, 3.0F, 0.0F);
        this.SnakeBodyL2 = new AdvancedModelBox(this, 43, 52);
        this.SnakeBodyL2.setPos(0.0F, 0.4F, -5.6F);
        this.SnakeBodyL2.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBodyL2, 0.8651597F, 2.4586453F, 0.0F);
        this.snakeBodyL6 = new AdvancedModelBox(this, 43, 52);
        this.snakeBodyL6.setPos(0.0F, 0.4F, -5.6F);
        this.snakeBodyL6.addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.snakeBodyL6, 1.3786355F, 1.0170033F, 0.6553711F);
        this.SnakeBaseL3 = new AdvancedModelBox(this, 25, 50);
        this.SnakeBaseL3.setPos(3.0F, -6.5F, 1.3F);
        this.SnakeBaseL3.addBox(-0.5F, -1.0F, -5.8F, 1.0F, 1.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.SnakeBaseL3, -1.796642F, 0.022514747F, 0.9203121F);
        this.SnakeBaseR4.addChild(this.SnakeBodyR4);
        this.SnakeHeadR7.addChild(this.SnakeFang2R7);
        this.SnakeHeadL5.addChild(this.SnakeJawL5);
        this.Head_Details.addChild(this.SnakeBaseR6);
        this.Body.addChild(this.Head);
        this.Tail_7.addChild(this.Tail_8);
        this.SnakeBodyR2.addChild(this.SnakeHeadR2);
        this.SnakeBodyR4.addChild(this.SnakeHeadR4);
        this.SnakeHeadR4_1.addChild(this.SnakeFang2R4_1);
        this.Head_Details.addChild(this.SnakeBaseR1);
        this.SnakeHeadR3.addChild(this.SnakeJawR3);
        this.SnakeBaseL1.addChild(this.SnakeBodyL1);
        this.SnakeBaseL3.addChild(this.SnakeBodyL3);
        this.SnakeHeadR4.addChild(this.SnakeJawR4);
        this.Tail_5.addChild(this.Tail_6);
        this.SnakeHeadL1.addChild(this.SnakeJawL1);
        this.SnakeHeadR2.addChild(this.SnakeFang1R2);
        this.SnakeHeadR4.addChild(this.SnakeFang2R4);
        this.Head_Details.addChild(this.SnakeBaseL6);
        this.snakeBodyL6.addChild(this.SnakeHeadL6);
        this.SnakeBaseL5.addChild(this.SnakeBodyL5);
        this.SnakeHeadR1.addChild(this.SnakeJawR1);
        this.Tail_4.addChild(this.Tail_5);
        this.Tail_3.addChild(this.Tail_4);
        this.SnakeBaseR7.addChild(this.SnakeBodyR7);
        this.SnakeBodyR6.addChild(this.SnakeHeadR6);
        this.Tail_1.addChild(this.Tail_2);
        this.Head.addChild(this.Head_Details);
        this.SnakeHeadR1.addChild(this.SnakeFang2R1);
        this.SnakeHeadL6.addChild(this.SnakeFang2L6);
        this.Body.addChild(this.Right_Arm);
        this.SnakeBodyR1.addChild(this.SnakeHeadR1);
        this.Tail_1.addChild(this.Body);
        this.SnakeBaseR5.addChild(this.SnakeBodyR5);
        this.SnakeBodyL1.addChild(this.SnakeHeadL1);
        this.SnakeHeadL6.addChild(this.SnakeJawL6);
        this.SnakeBaseL4.addChild(this.SnakeBodyL4);
        this.Tail_8.addChild(this.Tail_9);
        this.SnakeBodyR7.addChild(this.SnakeHeadR7);
        this.Head_Details.addChild(this.SnakeBaseL1);
        this.SnakeHeadR2.addChild(this.SnakeFang2R2);
        this.SnakeHeadL1.addChild(this.SnakeFang1L1);
        this.SnakeHeadL3.addChild(this.SnakeFang1L3);
        this.Head_Details.addChild(this.SnakeBaseR4);
        this.SnakeHeadR5.addChild(this.SnakeFang2R5);
        this.SnakeBaseR3.addChild(this.SnakeBodyR3);
        this.SnakeHeadR2.addChild(this.SnakeJawR2);
        this.SnakeBodyL2.addChild(this.SnakeHeadL2);
        this.Head_Details.addChild(this.SnakeBaseL2);
        this.SnakeHeadR7.addChild(this.SnakeJawR7);
        this.Tail_2.addChild(this.Tail_3);
        this.Head_Details.addChild(this.SnakeBaseL5);
        this.SnakeHeadL2.addChild(this.SnakeJawL2);
        this.Head_Details.addChild(this.SnakeBaseR7);
        this.SnakeHeadL3.addChild(this.SnakeFang2L3);
        this.SnakeBodyR3.addChild(this.SnakeHeadR3);
        this.SnakeHeadL7.addChild(this.SnakeFang2L7);
        this.SnakeHeadR5.addChild(this.SnakeFang1R5);
        this.SnakeBaseL7.addChild(this.SnakeBodyL7);
        this.SnakeHeadL7.addChild(this.SnakeFang1L7);
        this.SnakeHeadL5.addChild(this.SnakeFang2L5);
        this.SnakeHeadR5.addChild(this.SnakeJawR5);
        this.Head_Details.addChild(this.SnakeBaseL7);
        this.SnakeHeadR1.addChild(this.SnakeFang1R1);
        this.SnakeHeadL7.addChild(this.SnakeJawL7);
        this.Body.addChild(this.Left_Arm);
        this.SnakeBodyL4.addChild(this.SnakeHeadR4_1);
        this.SnakeHeadR6.addChild(this.SnakeFang2R6);
        this.SnakeBaseR1.addChild(this.SnakeBodyR1);
        this.SnakeBodyL7.addChild(this.SnakeHeadL7);
        this.SnakeHeadR4_1.addChild(this.SnakeFang1R4_1);
        this.SnakeHeadR4.addChild(this.SnakeFang1R4);
        this.SnakeBaseR2.addChild(this.SnakeBodyR2);
        this.Head_Details.addChild(this.SnakeBaseR5);
        this.SnakeHeadL3.addChild(this.SnakeJawL3);
        this.Head_Details.addChild(this.SnakeBaseL4);
        this.SnakeHeadR3.addChild(this.SnakeFang2R3);
        this.SnakeBodyL3.addChild(this.SnakeHeadL3);
        this.SnakeBaseR6.addChild(this.SnakeBodyR6);
        this.SnakeHeadR3.addChild(this.SnakeFang1R3);
        this.Head_Details.addChild(this.SnakeBaseR2);
        this.Head_Details.addChild(this.SnakeBaseR3);
        this.SnakeHeadR6.addChild(this.SnakeFang1R6);
        this.SnakeHeadL6.addChild(this.SnakeFang1L6);
        this.SnakeHeadL2.addChild(this.SnakeFang2L2);
        this.Body.addChild(this.Neck);
        this.SnakeHeadL1.addChild(this.SnakeFang2L1);
        this.SnakeHeadL2.addChild(this.SnakeFang1L2);
        this.SnakeBodyR5.addChild(this.SnakeHeadR5);
        this.SnakeHeadR4_1.addChild(this.SnakeJawR4_1);
        this.SnakeHeadR7.addChild(this.SnakeFang1R7);
        this.Tail_6.addChild(this.Tail_7);
        this.SnakeBodyL5.addChild(this.SnakeHeadL5);
        this.SnakeHeadL5.addChild(this.SnakeFang1L5);
        this.SnakeHeadR6.addChild(this.SnakeJawR6);
        this.SnakeBaseL2.addChild(this.SnakeBodyL2);
        this.SnakeBaseL6.addChild(this.snakeBodyL6);
        this.Head_Details.addChild(this.SnakeBaseL3);
        this.animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        if (this.animator.setAnimation(EntityGorgon.ANIMATION_SCARE)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Head, 0.0F, 20.0F, 0.0F);
            this.rotate(this.animator, this.Left_Arm, 0.0F, -12.5F, -70.0F);
            this.rotate(this.animator, this.Right_Arm, 0.0F, 12.5F, 70.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Head, 0.0F, -20.0F, 0.0F);
            this.rotate(this.animator, this.Left_Arm, 0.0F, -25.0F, -140.0F);
            this.rotate(this.animator, this.Right_Arm, 0.0F, 25.0F, 140.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Head, 0.0F, 20.0F, 0.0F);
            this.rotate(this.animator, this.Left_Arm, 0.0F, -25.0F, -140.0F);
            this.rotate(this.animator, this.Right_Arm, 0.0F, 25.0F, 140.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Head, 0.0F, -20.0F, 0.0F);
            this.rotate(this.animator, this.Left_Arm, 0.0F, -25.0F, -140.0F);
            this.rotate(this.animator, this.Right_Arm, 0.0F, 25.0F, 140.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(10);
        }
        if (this.animator.setAnimation(EntityGorgon.ANIMATION_HIT)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.Body, 10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Left_Arm, -120.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.Right_Arm, -120.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
    }

    public void setupAnim(EntityGorgon entity, float f, float f1, float f2, float f3, float f4) {
        this.animate(entity, f, f1, f2, f3, f4, 1.0F);
        float speed_walk = 0.6F;
        float speed_idle = 0.05F;
        float degree_walk = 1.0F;
        float degree_idle = 0.5F;
        AdvancedModelBox[] TAIL = new AdvancedModelBox[] { this.Tail_4, this.Tail_5, this.Tail_6, this.Tail_7, this.Tail_8, this.Tail_9 };
        AdvancedModelBox[] SNAKEL1 = new AdvancedModelBox[] { this.SnakeBaseL1, this.SnakeBodyL1, this.SnakeHeadL1 };
        AdvancedModelBox[] SNAKEL2 = new AdvancedModelBox[] { this.SnakeBaseL2, this.SnakeBodyL2, this.SnakeHeadL2 };
        AdvancedModelBox[] SNAKEL3 = new AdvancedModelBox[] { this.SnakeBaseL3, this.SnakeBodyL3, this.SnakeHeadL3 };
        AdvancedModelBox[] SNAKEL4 = new AdvancedModelBox[] { this.SnakeBaseL4, this.SnakeBaseL4, this.SnakeBaseL4 };
        AdvancedModelBox[] SNAKEL5 = new AdvancedModelBox[] { this.SnakeBaseL5, this.SnakeBaseL5, this.SnakeBaseL5 };
        AdvancedModelBox[] SNAKEL6 = new AdvancedModelBox[] { this.SnakeBaseL6, this.SnakeBaseL6, this.SnakeBaseL6 };
        AdvancedModelBox[] SNAKEL7 = new AdvancedModelBox[] { this.SnakeBaseL7, this.SnakeBaseL7, this.SnakeBaseL7 };
        AdvancedModelBox[] SNAKER1 = new AdvancedModelBox[] { this.SnakeBaseR1, this.SnakeBodyR1, this.SnakeHeadR1 };
        AdvancedModelBox[] SNAKER2 = new AdvancedModelBox[] { this.SnakeBaseR2, this.SnakeBodyR2, this.SnakeHeadR2 };
        AdvancedModelBox[] SNAKER3 = new AdvancedModelBox[] { this.SnakeBaseR3, this.SnakeBodyR3, this.SnakeHeadR3 };
        AdvancedModelBox[] SNAKER4 = new AdvancedModelBox[] { this.SnakeBaseR4, this.SnakeBodyR4, this.SnakeHeadR4 };
        AdvancedModelBox[] SNAKER5 = new AdvancedModelBox[] { this.SnakeBaseR5, this.SnakeBaseR5, this.SnakeBaseR5 };
        AdvancedModelBox[] SNAKER6 = new AdvancedModelBox[] { this.SnakeBaseR6, this.SnakeBaseR6, this.SnakeBaseR6 };
        AdvancedModelBox[] SNAKER7 = new AdvancedModelBox[] { this.SnakeBaseR7, this.SnakeBaseR7, this.SnakeBaseR7 };
        this.chainFlap(TAIL, speed_walk, degree_walk * 0.75F, -3.0, f, f1);
        this.walk(this.Right_Arm, speed_idle * 1.5F, degree_idle * 0.4F, false, 2.0F, -0.3F, f2, 1.0F);
        this.walk(this.Left_Arm, speed_idle * 1.5F, degree_idle * 0.4F, true, 2.0F, 0.3F, f2, 1.0F);
        this.flap(this.Right_Arm, speed_idle * 1.5F, degree_idle * 0.2F, false, 2.0F, 0.2F, f2, 1.0F);
        this.flap(this.Left_Arm, speed_idle * 1.5F, degree_idle * 0.2F, true, 2.0F, 0.2F, f2, 1.0F);
        this.Right_Arm.rotateAngleX = Mth.cos(f * 0.6662F + (float) Math.PI) * 2.0F * f1 * 0.5F;
        this.Left_Arm.rotateAngleX = Mth.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
        float f12 = (float) Math.toRadians(-1.29F) + f1;
        if (f12 < 0.0F) {
            f12 = 0.0F;
        }
        if ((double) f12 > Math.toRadians(20.0)) {
            f12 = (float) Math.toRadians(20.0);
        }
        this.Tail_1.rotateAngleX = f12;
        this.Tail_3.rotateAngleX -= f12;
        this.Head.rotateAngleX -= f12;
        f12 = 0.0F;
        this.chainFlap(SNAKEL1, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKEL1, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKEL2, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKEL2, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKEL3, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKEL3, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKEL4, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKEL4, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKER1, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKER1, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKER2, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKER2, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKER3, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKER3, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKER4, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKER4, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKER5, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKER5, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKER6, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKER6, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKER7, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKER7, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKEL5, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKEL5, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKEL6, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKEL6, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainFlap(SNAKEL7, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.chainSwing(SNAKEL7, speed_idle, degree_idle * 0.75F, -3.0, f2, 1.0F);
        this.faceTarget(f3, f4, 1.0F, new AdvancedModelBox[] { this.Head });
        float deathProg = Math.min(40.0F, (float) entity.f_20919_) / 2.0F;
        this.progressRotation(this.Tail_1, deathProg, (float) Math.toRadians(5.0), (float) Math.toRadians(57.0), 0.0F);
        this.progressPosition(this.Tail_1, deathProg, -5.0F, 22.0F, -4.0F);
        this.progressRotation(this.Tail_2, deathProg, (float) Math.toRadians(18.0), (float) Math.toRadians(-54.0), 0.0F);
        this.progressRotation(this.Body, deathProg, (float) Math.toRadians(-9.0), (float) Math.toRadians(36.0), 0.0F);
        this.progressRotation(this.Right_Arm, deathProg, 0.0F, 0.0F, (float) Math.toRadians(20.0));
        this.progressRotation(this.Left_Arm, deathProg, 0.0F, 0.0F, (float) Math.toRadians(-20.0));
        this.Neck.showModel = deathProg <= 0.0F;
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.Tail_1);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.Tail_1, this.Tail_2, this.Body, this.Tail_3, this.Tail_4, this.Tail_5, this.Tail_6, this.Tail_7, this.Tail_8, this.Tail_9, this.Left_Arm, this.Head, new AdvancedModelBox[] { this.Right_Arm, this.Neck, this.Head_Details, this.SnakeBaseR2, this.SnakeBaseR7, this.SnakeBaseR6, this.SnakeBaseR5, this.SnakeBaseR4, this.SnakeBaseR3, this.SnakeBaseR1, this.SnakeBaseL1, this.SnakeBaseL2, this.SnakeBaseL4, this.SnakeBaseL3, this.SnakeBaseL7, this.SnakeBaseL6, this.SnakeBaseL5, this.SnakeBodyR2, this.SnakeHeadR2, this.SnakeJawR2, this.SnakeFang1R2, this.SnakeFang2R2, this.SnakeBodyR7, this.SnakeHeadR7, this.SnakeJawR7, this.SnakeFang1R7, this.SnakeFang2R7, this.SnakeBodyR6, this.SnakeHeadR6, this.SnakeJawR6, this.SnakeFang1R6, this.SnakeFang2R6, this.SnakeBodyR5, this.SnakeHeadR5, this.SnakeJawR5, this.SnakeFang1R5, this.SnakeFang2R5, this.SnakeBodyR4, this.SnakeHeadR4, this.SnakeJawR4, this.SnakeFang1R4, this.SnakeFang2R4, this.SnakeBodyR3, this.SnakeHeadR3, this.SnakeJawR3, this.SnakeFang1R3, this.SnakeFang2R3, this.SnakeBodyR1, this.SnakeHeadR1, this.SnakeJawR1, this.SnakeFang1R1, this.SnakeFang2R1, this.SnakeBodyL1, this.SnakeHeadL1, this.SnakeJawL1, this.SnakeFang1L1, this.SnakeFang2L1, this.SnakeBodyL2, this.SnakeHeadL2, this.SnakeJawL2, this.SnakeFang1L2, this.SnakeFang2L2, this.SnakeBodyL4, this.SnakeHeadR4_1, this.SnakeJawR4_1, this.SnakeFang1R4_1, this.SnakeFang2R4_1, this.SnakeBodyL3, this.SnakeHeadL3, this.SnakeJawL3, this.SnakeFang1L3, this.SnakeFang2L3, this.SnakeBodyL7, this.SnakeHeadL7, this.SnakeJawL7, this.SnakeFang1L7, this.SnakeFang2L7, this.snakeBodyL6, this.SnakeHeadL6, this.SnakeJawL6, this.SnakeFang1L6, this.SnakeFang2L6, this.SnakeBodyL5, this.SnakeHeadL5, this.SnakeJawL5, this.SnakeFang1L5, this.SnakeFang2L5 });
    }

    @Override
    public void renderStatue(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.m_7695_(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}