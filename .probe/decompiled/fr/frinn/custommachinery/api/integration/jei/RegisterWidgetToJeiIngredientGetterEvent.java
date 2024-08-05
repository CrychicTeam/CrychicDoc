package fr.frinn.custommachinery.api.integration.jei;

import com.google.common.collect.ImmutableMap;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.impl.integration.jei.WidgetToJeiIngredientRegistry;
import java.util.HashMap;
import java.util.Map;

public class RegisterWidgetToJeiIngredientGetterEvent {

    public static final Event<RegisterWidgetToJeiIngredientGetterEvent.Register> EVENT = EventFactory.createLoop();

    private final Map<GuiElementType<?>, WidgetToJeiIngredientRegistry.IngredientGetter<?>> registry = new HashMap();

    public <E extends IGuiElement> void register(GuiElementType<E> type, WidgetToJeiIngredientRegistry.IngredientGetter<E> getter) {
        if (this.registry.containsKey(type)) {
            throw new IllegalArgumentException("An ingredient getter for GuiElementType: " + type + " is already registered !");
        } else {
            this.registry.put(type, getter);
        }
    }

    public Map<GuiElementType<?>, WidgetToJeiIngredientRegistry.IngredientGetter<?>> getIngredientGetters() {
        return ImmutableMap.copyOf(this.registry);
    }

    public interface Register {

        void registerIngredientGetter(RegisterWidgetToJeiIngredientGetterEvent var1);
    }
}