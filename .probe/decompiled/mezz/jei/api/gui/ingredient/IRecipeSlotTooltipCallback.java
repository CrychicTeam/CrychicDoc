package mezz.jei.api.gui.ingredient;

import java.util.List;
import net.minecraft.network.chat.Component;

@FunctionalInterface
public interface IRecipeSlotTooltipCallback {

    void onTooltip(IRecipeSlotView var1, List<Component> var2);
}