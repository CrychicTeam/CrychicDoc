package noppes.npcs.shared.client.gui.listeners;

import net.minecraft.client.gui.GuiGraphics;

public interface IGui {

    int getID();

    void render(GuiGraphics var1, int var2, int var3);

    void tick();

    boolean isActive();
}