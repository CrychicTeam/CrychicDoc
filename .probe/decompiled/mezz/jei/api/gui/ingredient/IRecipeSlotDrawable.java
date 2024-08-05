package mezz.jei.api.gui.ingredient;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

public interface IRecipeSlotDrawable extends IRecipeSlotView {

    Rect2i getRect();

    void draw(GuiGraphics var1);

    void drawHoverOverlays(GuiGraphics var1);

    List<Component> getTooltip();

    void addTooltipCallback(IRecipeSlotTooltipCallback var1);
}