package mezz.jei.api.ingredients;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

public interface IIngredientRenderer<T> {

    void render(GuiGraphics var1, T var2);

    List<Component> getTooltip(T var1, TooltipFlag var2);

    default Font getFontRenderer(Minecraft minecraft, T ingredient) {
        return minecraft.font;
    }

    default int getWidth() {
        return 16;
    }

    default int getHeight() {
        return 16;
    }
}