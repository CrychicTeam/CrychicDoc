package mezz.jei.api.gui.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;

public interface IGuiContainerHandler<T extends AbstractContainerScreen<?>> {

    default List<Rect2i> getGuiExtraAreas(T containerScreen) {
        return Collections.emptyList();
    }

    default Optional<IClickableIngredient<?>> getClickableIngredientUnderMouse(T containerScreen, double mouseX, double mouseY) {
        return Optional.empty();
    }

    default Collection<IGuiClickableArea> getGuiClickableAreas(T containerScreen, double guiMouseX, double guiMouseY) {
        return Collections.emptyList();
    }
}