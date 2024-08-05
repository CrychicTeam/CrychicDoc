package mezz.jei.api.runtime;

import java.util.Collection;
import java.util.Optional;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

public interface IIngredientManager {

    @Unmodifiable
    default Collection<ItemStack> getAllItemStacks() {
        return this.getAllIngredients(VanillaTypes.ITEM_STACK);
    }

    @Unmodifiable
    <V> Collection<V> getAllIngredients(IIngredientType<V> var1);

    <V> IIngredientHelper<V> getIngredientHelper(V var1);

    <V> IIngredientHelper<V> getIngredientHelper(IIngredientType<V> var1);

    <V> IIngredientRenderer<V> getIngredientRenderer(V var1);

    <V> IIngredientRenderer<V> getIngredientRenderer(IIngredientType<V> var1);

    @Unmodifiable
    Collection<IIngredientType<?>> getRegisteredIngredientTypes();

    <V> void addIngredientsAtRuntime(IIngredientType<V> var1, Collection<V> var2);

    <V> void removeIngredientsAtRuntime(IIngredientType<V> var1, Collection<V> var2);

    <V> Optional<IIngredientType<V>> getIngredientTypeChecked(V var1);

    <V> Optional<IIngredientType<V>> getIngredientTypeChecked(Class<? extends V> var1);

    <V> Optional<ITypedIngredient<V>> createTypedIngredient(IIngredientType<V> var1, V var2);

    default <V> Optional<ITypedIngredient<V>> createTypedIngredient(V ingredient) {
        return this.getIngredientTypeChecked(ingredient).flatMap(ingredientType -> this.createTypedIngredient(ingredientType, ingredient));
    }

    <V> Optional<V> getIngredientByUid(IIngredientType<V> var1, String var2);

    void registerIngredientListener(IIngredientManager.IIngredientListener var1);

    public interface IIngredientListener {

        <V> void onIngredientsAdded(IIngredientHelper<V> var1, Collection<ITypedIngredient<V>> var2);

        <V> void onIngredientsRemoved(IIngredientHelper<V> var1, Collection<ITypedIngredient<V>> var2);
    }
}