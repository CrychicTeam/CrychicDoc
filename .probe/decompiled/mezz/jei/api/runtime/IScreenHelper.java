package mezz.jei.api.runtime;

import java.util.Optional;
import java.util.stream.Stream;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiProperties;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;

public interface IScreenHelper {

    Stream<IClickableIngredient<?>> getClickableIngredientUnderMouse(Screen var1, double var2, double var4);

    <T extends Screen> Optional<IGuiProperties> getGuiProperties(T var1);

    Stream<IGuiClickableArea> getGuiClickableArea(AbstractContainerScreen<?> var1, double var2, double var4);

    Stream<Rect2i> getGuiExclusionAreas(Screen var1);

    <T extends Screen> Optional<IGhostIngredientHandler<T>> getGhostIngredientHandler(T var1);
}