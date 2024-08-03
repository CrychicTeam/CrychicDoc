package dev.xkmc.l2backpack.content.common;

import dev.xkmc.l2backpack.content.render.DrawerCountDeco;
import dev.xkmc.l2library.util.Proxy;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record InvClientTooltip(InvTooltip inv) implements ClientTooltipComponent {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/gui/container/bundle.png");

    @Override
    public int getHeight() {
        return this.inv.item().getInvSize(this.inv.stack()) / this.inv.item().getRowSize() * 18 + 2;
    }

    @Override
    public int getWidth(Font font) {
        return 18 * this.inv.item().getRowSize();
    }

    @Override
    public void renderImage(Font font, int mx, int my, GuiGraphics g) {
        List<ItemStack> list = this.inv.item().getInvItems(this.inv.stack(), Proxy.getClientPlayer());
        for (int i = 0; i < list.size(); i++) {
            this.renderSlot(font, mx + i % this.inv.item().getRowSize() * 18, my + i / this.inv.item().getRowSize() * 18, g, (ItemStack) list.get(i));
        }
    }

    private void renderSlot(Font font, int x, int y, GuiGraphics g, ItemStack stack) {
        this.blit(g, x, y);
        if (!stack.isEmpty()) {
            DrawerCountDeco.startTooltipRendering();
            g.renderItem(stack, x + 1, y + 1, 0);
            g.renderItemDecorations(font, stack, x + 1, y + 1);
            DrawerCountDeco.stopTooltipRendering();
        }
    }

    private void blit(GuiGraphics g, int x, int y) {
        g.blit(TEXTURE_LOCATION, x, y, 0, 0.0F, 0.0F, 18, 18, 128, 128);
    }
}