package dev.xkmc.l2archery.content.client;

import dev.xkmc.l2archery.init.data.ArcheryConfig;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ArrowDisplayOverlay implements IGuiOverlay {

    @Override
    public void render(ForgeGui gui, GuiGraphics g, float partialTick, int width, int height) {
        LocalPlayer player = Proxy.getClientPlayer();
        if (player != null) {
            if (ArcheryConfig.CLIENT.showArrow.get()) {
                ItemStack bowStack = player.m_21205_();
                if (bowStack.getItem() instanceof BowItem) {
                    ItemStack arrowStack = player.m_6298_(bowStack);
                    gui.setupOverlayRenderState(true, false);
                    int x = gui.f_92977_ / 2 + 16;
                    int y = gui.f_92978_ / 2 - 8;
                    g.renderItem(arrowStack, x, y);
                    g.renderItemDecorations(gui.m_93082_(), arrowStack, x, y);
                }
            }
        }
    }
}