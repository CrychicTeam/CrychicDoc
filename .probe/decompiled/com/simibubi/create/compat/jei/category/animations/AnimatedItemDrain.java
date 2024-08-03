package com.simibubi.create.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.fluids.FluidStack;

public class AnimatedItemDrain extends AnimatedKinetics {

    private FluidStack fluid;

    public AnimatedItemDrain withFluid(FluidStack fluid) {
        this.fluid = fluid;
        return this;
    }

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((float) xOffset, (float) yOffset, 100.0F);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5F));
        int scale = 20;
        this.blockElement(AllBlocks.ITEM_DRAIN.getDefaultState()).scale((double) scale).render(graphics);
        MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        UIRenderHelper.flipForGuiRender(matrixStack);
        matrixStack.scale((float) scale, (float) scale, (float) scale);
        float from = 0.125F;
        float to = 1.0F - from;
        FluidRenderer.renderFluidBox(this.fluid, from, from, from, to, 0.75F, to, buffer, matrixStack, 15728880, false);
        buffer.endBatch();
        matrixStack.popPose();
    }
}