package mezz.jei.api.ingredients;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IIngredientHelper<V> {

    IIngredientType<V> getIngredientType();

    String getDisplayName(V var1);

    String getUniqueId(V var1, UidContext var2);

    default String getWildcardId(V ingredient) {
        return this.getUniqueId(ingredient, UidContext.Ingredient);
    }

    default String getDisplayModId(V ingredient) {
        return this.getResourceLocation(ingredient).getNamespace();
    }

    default Iterable<Integer> getColors(V ingredient) {
        return Collections.emptyList();
    }

    ResourceLocation getResourceLocation(V var1);

    default ItemStack getCheatItemStack(V ingredient) {
        return ItemStack.EMPTY;
    }

    V copyIngredient(V var1);

    default V normalizeIngredient(V ingredient) {
        return this.copyIngredient(ingredient);
    }

    default boolean isValidIngredient(V ingredient) {
        return true;
    }

    default boolean isIngredientOnServer(V ingredient) {
        return true;
    }

    default Stream<ResourceLocation> getTagStream(V ingredient) {
        return Stream.empty();
    }

    String getErrorInfo(@Nullable V var1);

    default Optional<ResourceLocation> getTagEquivalent(Collection<V> ingredients) {
        return Optional.empty();
    }
}