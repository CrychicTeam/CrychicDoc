package com.simibubi.create.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class AnimatedCrushingWheels extends AnimatedKinetics {

    private final BlockState wheel = (BlockState) AllBlocks.CRUSHING_WHEEL.getDefaultState().m_61124_(BlockStateProperties.AXIS, Direction.Axis.X);

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((float) xOffset, (float) yOffset, 100.0F);
        matrixStack.mulPose(Axis.YP.rotationDegrees(-22.5F));
        int scale = 22;
        this.blockElement(this.wheel).rotateBlock(0.0, 90.0, (double) (-getCurrentAngle())).scale((double) scale).render(graphics);
        this.blockElement(this.wheel).rotateBlock(0.0, 90.0, (double) getCurrentAngle()).atLocal(2.0, 0.0, 0.0).scale((double) scale).render(graphics);
        matrixStack.popPose();
    }
}