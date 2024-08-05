package journeymap.client.ui.minimap;

import net.minecraft.client.gui.GuiGraphics;

public interface Selectable {

    int SELECTED_COLOR = -16711936;

    int UNSELECTED_COLOR = -65536;

    boolean mouseClicked(double var1, double var3, int var5);

    boolean mouseDragged(double var1, double var3, int var5, double var6, double var8);

    boolean mouseReleased(double var1, double var3, int var5);

    void tick();

    void renderBorder(GuiGraphics var1, int var2);
}