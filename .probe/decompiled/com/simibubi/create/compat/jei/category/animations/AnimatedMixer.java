package com.simibubi.create.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class AnimatedMixer extends AnimatedKinetics {

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((float) xOffset, (float) yOffset, 200.0F);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5F));
        int scale = 23;
        this.blockElement(this.cogwheel()).rotateBlock(0.0, (double) (getCurrentAngle() * 2.0F), 0.0).atLocal(0.0, 0.0, 0.0).scale((double) scale).render(graphics);
        this.blockElement(AllBlocks.MECHANICAL_MIXER.getDefaultState()).atLocal(0.0, 0.0, 0.0).scale((double) scale).render(graphics);
        float animation = (Mth.sin(AnimationTickHolder.getRenderTime() / 32.0F) + 1.0F) / 5.0F + 0.5F;
        this.blockElement(AllPartialModels.MECHANICAL_MIXER_POLE).atLocal(0.0, (double) animation, 0.0).scale((double) scale).render(graphics);
        this.blockElement(AllPartialModels.MECHANICAL_MIXER_HEAD).rotateBlock(0.0, (double) (getCurrentAngle() * 4.0F), 0.0).atLocal(0.0, (double) animation, 0.0).scale((double) scale).render(graphics);
        this.blockElement(AllBlocks.BASIN.getDefaultState()).atLocal(0.0, 1.65, 0.0).scale((double) scale).render(graphics);
        matrixStack.popPose();
    }
}