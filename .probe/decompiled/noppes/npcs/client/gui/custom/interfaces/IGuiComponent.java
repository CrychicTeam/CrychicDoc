package noppes.npcs.client.gui.custom.interfaces;

import net.minecraft.client.gui.GuiGraphics;
import noppes.npcs.api.gui.ICustomGuiComponent;

public interface IGuiComponent {

    int getID();

    void onRender(GuiGraphics var1, int var2, int var3, float var4);

    default void onRenderPost(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    }

    void init();

    ICustomGuiComponent component();
}