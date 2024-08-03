package com.simibubi.create.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import net.minecraft.client.gui.GuiGraphics;

public class AnimatedMillstone extends AnimatedKinetics {

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((float) xOffset, (float) yOffset, 0.0F);
        AllGuiTextures.JEI_SHADOW.render(graphics, -16, 13);
        matrixStack.translate(-2.0F, 18.0F, 0.0F);
        int scale = 22;
        this.blockElement(AllPartialModels.MILLSTONE_COG).rotateBlock(22.5, (double) (getCurrentAngle() * 2.0F), 0.0).scale((double) scale).render(graphics);
        this.blockElement(AllBlocks.MILLSTONE.getDefaultState()).rotateBlock(22.5, 22.5, 0.0).scale((double) scale).render(graphics);
        matrixStack.popPose();
    }
}