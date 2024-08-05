package mezz.jei.gui.overlay;

import java.util.Collection;
import java.util.Set;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.core.collect.ListMultiMap;

public class ElementRenderersByType {

    private final ListMultiMap<IIngredientType<?>, ElementRenderer<?>> map = new ListMultiMap<>();

    public <T> void put(IIngredientType<T> ingredientType, ElementRenderer<T> renderer) {
        this.map.put(ingredientType, renderer);
    }

    public Set<IIngredientType<?>> getTypes() {
        return this.map.keySet();
    }

    public <T> Collection<ElementRenderer<T>> get(IIngredientType<T> ingredientType) {
        return (Collection<ElementRenderer<T>>) this.map.get(ingredientType);
    }

    public void clear() {
        this.map.clear();
    }
}