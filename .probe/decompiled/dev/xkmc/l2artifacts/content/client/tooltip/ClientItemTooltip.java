package dev.xkmc.l2artifacts.content.client.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public record ClientItemTooltip(ItemTooltip items) implements ClientTooltipComponent {

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public int getWidth(Font font) {
        return 18 * this.items.list().size();
    }

    @Override
    public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource) {
    }

    @Override
    public void renderImage(Font pFont, int pMouseX, int pMouseY, GuiGraphics g) {
        for (int i = 0; i < this.items.list().size(); i++) {
            int x = pMouseX + i * 18 + 1;
            int y = pMouseY + 1;
            ItemStack itemstack = this.items.list().get(i);
            g.renderItem(itemstack, x, y, i);
            g.renderItemDecorations(pFont, itemstack, x, y);
        }
    }
}