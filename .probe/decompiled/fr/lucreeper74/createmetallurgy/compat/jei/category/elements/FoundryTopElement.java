package fr.lucreeper74.createmetallurgy.compat.jei.category.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import fr.lucreeper74.createmetallurgy.registries.CMBlocks;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;

public class FoundryTopElement implements IDrawable {

    @Override
    public int getWidth() {
        return 50;
    }

    @Override
    public int getHeight() {
        return 50;
    }

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate((float) xOffset, (float) yOffset, 200.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(-15.5F));
        poseStack.mulPose(Axis.YP.rotationDegrees(22.5F));
        int scale = 23;
        GuiGameElement.of(CMBlocks.FOUNDRY_LID_BLOCK.getDefaultState()).atLocal(0.0, 0.65, 0.0).scale((double) scale).render(graphics);
        GuiGameElement.of(CMBlocks.FOUNDRY_BASIN_BLOCK.getDefaultState()).atLocal(0.0, 1.65, 0.0).scale((double) scale).render(graphics);
        poseStack.popPose();
    }
}