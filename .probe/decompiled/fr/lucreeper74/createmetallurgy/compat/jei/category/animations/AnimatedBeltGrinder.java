package fr.lucreeper74.createmetallurgy.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import fr.lucreeper74.createmetallurgy.content.belt_grinder.BeltGrinderBlock;
import fr.lucreeper74.createmetallurgy.registries.CMBlocks;
import fr.lucreeper74.createmetallurgy.registries.CMPartialModels;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class AnimatedBeltGrinder extends AnimatedKinetics {

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((float) xOffset, (float) yOffset, 0.0F);
        matrixStack.translate(0.0F, 0.0F, 200.0F);
        matrixStack.translate(2.0F, 22.0F, 0.0F);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(112.5F));
        int scale = 25;
        this.blockElement(this.shaft(Direction.Axis.X)).rotateBlock((double) (-getCurrentAngle()), 0.0, 0.0).scale((double) scale).render(graphics);
        this.blockElement((BlockState) CMBlocks.BELT_GRINDER_BLOCK.getDefaultState().m_61124_(BeltGrinderBlock.HORIZONTAL_FACING, Direction.WEST)).rotateBlock(0.0, 0.0, 0.0).scale((double) scale).render(graphics);
        this.blockElement(CMPartialModels.GRINDER_BELT).rotateBlock(0.0, -90.0, 0.0).scale((double) scale).render(graphics);
        matrixStack.popPose();
    }
}