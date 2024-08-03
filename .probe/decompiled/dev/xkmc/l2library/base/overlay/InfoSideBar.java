package dev.xkmc.l2library.base.overlay;

import dev.xkmc.l2library.init.L2LibraryConfig;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public abstract class InfoSideBar<S extends SideBar.Signature<S>> extends SideBar<S> implements IGuiOverlay {

    public InfoSideBar(float duration, float ease) {
        super(duration, ease);
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics g, float partialTick, int width, int height) {
        if (this.ease((float) gui.m_93079_() + partialTick)) {
            List<Component> text = this.getText();
            if (!text.isEmpty()) {
                int anchor = L2LibraryConfig.CLIENT.infoAnchor.get();
                int y = height * anchor / 2;
                int w = (int) ((double) width * L2LibraryConfig.CLIENT.infoMaxWidth.get());
                new TextBox(g, 0, anchor, this.getXOffset(width), y, w).renderLongText(Minecraft.getInstance().font, text);
            }
        }
    }

    protected abstract List<Component> getText();

    @Override
    protected int getXOffset(int width) {
        float progress = (this.max_ease - this.ease_time) / this.max_ease;
        return Math.round(-progress * (float) width / 2.0F + 8.0F);
    }
}