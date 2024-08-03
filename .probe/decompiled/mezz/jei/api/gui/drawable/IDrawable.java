package mezz.jei.api.gui.drawable;

import net.minecraft.client.gui.GuiGraphics;

public interface IDrawable {

    int getWidth();

    int getHeight();

    default void draw(GuiGraphics guiGraphics) {
        this.draw(guiGraphics, 0, 0);
    }

    void draw(GuiGraphics var1, int var2, int var3);
}