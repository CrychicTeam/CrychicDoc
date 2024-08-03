package com.simibubi.create.compat.jei.category.animations;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;

public class AnimatedBlazeBurner extends AnimatedKinetics {

    private BlazeBurnerBlock.HeatLevel heatLevel;

    public AnimatedBlazeBurner withHeat(BlazeBurnerBlock.HeatLevel heatLevel) {
        this.heatLevel = heatLevel;
        return this;
    }

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((float) xOffset, (float) yOffset, 200.0F);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5F));
        int scale = 23;
        float offset = (Mth.sin(AnimationTickHolder.getRenderTime() / 16.0F) + 0.5F) / 16.0F;
        this.blockElement(AllBlocks.BLAZE_BURNER.getDefaultState()).atLocal(0.0, 1.65, 0.0).scale((double) scale).render(graphics);
        PartialModel blaze = this.heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? AllPartialModels.BLAZE_SUPER : AllPartialModels.BLAZE_ACTIVE;
        PartialModel rods2 = this.heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? AllPartialModels.BLAZE_BURNER_SUPER_RODS_2 : AllPartialModels.BLAZE_BURNER_RODS_2;
        this.blockElement(blaze).atLocal(1.0, 1.8, 1.0).rotate(0.0, 180.0, 0.0).scale((double) scale).render(graphics);
        this.blockElement(rods2).atLocal(1.0, 1.7 + (double) offset, 1.0).rotate(0.0, 180.0, 0.0).scale((double) scale).render(graphics);
        matrixStack.scale((float) scale, (float) (-scale), (float) scale);
        matrixStack.translate(0.0, -1.8, 0.0);
        SpriteShiftEntry spriteShift = this.heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? AllSpriteShifts.SUPER_BURNER_FLAME : AllSpriteShifts.BURNER_FLAME;
        float spriteWidth = spriteShift.getTarget().getU1() - spriteShift.getTarget().getU0();
        float spriteHeight = spriteShift.getTarget().getV1() - spriteShift.getTarget().getV0();
        float time = AnimationTickHolder.getRenderTime(Minecraft.getInstance().level);
        float speed = 0.03125F + 0.015625F * (float) this.heatLevel.ordinal();
        double vScroll = (double) (speed * time);
        vScroll -= Math.floor(vScroll);
        vScroll = vScroll * (double) spriteHeight / 2.0;
        double uScroll = (double) (speed * time / 2.0F);
        uScroll -= Math.floor(uScroll);
        uScroll = uScroll * (double) spriteWidth / 2.0;
        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
        CachedBufferer.partial(AllPartialModels.BLAZE_BURNER_FLAME, Blocks.AIR.defaultBlockState()).shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll).light(15728880).renderInto(matrixStack, vb);
        matrixStack.popPose();
    }
}