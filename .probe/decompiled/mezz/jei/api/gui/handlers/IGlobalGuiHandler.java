package mezz.jei.api.gui.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.renderer.Rect2i;

public interface IGlobalGuiHandler {

    default Collection<Rect2i> getGuiExtraAreas() {
        return Collections.emptyList();
    }

    default Optional<IClickableIngredient<?>> getClickableIngredientUnderMouse(double mouseX, double mouseY) {
        return Optional.empty();
    }
}