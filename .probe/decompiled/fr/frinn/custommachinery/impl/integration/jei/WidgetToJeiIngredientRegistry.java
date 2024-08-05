package fr.frinn.custommachinery.impl.integration.jei;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.integration.jei.RegisterWidgetToJeiIngredientGetterEvent;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import java.util.Map;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.runtime.IClickableIngredient;
import org.jetbrains.annotations.Nullable;

public class WidgetToJeiIngredientRegistry {

    private static Map<GuiElementType<?>, WidgetToJeiIngredientRegistry.IngredientGetter<?>> registry;

    private static boolean init = false;

    public static void init() {
        if (!init) {
            RegisterWidgetToJeiIngredientGetterEvent event = new RegisterWidgetToJeiIngredientGetterEvent();
            RegisterWidgetToJeiIngredientGetterEvent.EVENT.invoker().registerIngredientGetter(event);
            registry = event.getIngredientGetters();
            init = true;
        }
    }

    public static IClickableIngredient<?> getIngredient(AbstractGuiElementWidget<?> widget, double mouseX, double mouseY, IJeiHelpers helpers) {
        WidgetToJeiIngredientRegistry.IngredientGetter getter = (WidgetToJeiIngredientRegistry.IngredientGetter) registry.get(widget.getElement().getType());
        return getter == null ? null : getter.getIngredient(widget, mouseX, mouseY, helpers);
    }

    public interface IngredientGetter<E extends IGuiElement> {

        @Nullable
        <T> IClickableIngredient<T> getIngredient(AbstractGuiElementWidget<E> var1, double var2, double var4, IJeiHelpers var6);
    }
}