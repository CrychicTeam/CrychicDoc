package mezz.jei.api.runtime;

import java.util.Optional;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IBookmarkOverlay {

    Optional<ITypedIngredient<?>> getIngredientUnderMouse();

    @Nullable
    <T> T getIngredientUnderMouse(IIngredientType<T> var1);

    @Nullable
    default ItemStack getItemStackUnderMouse() {
        return this.getIngredientUnderMouse(VanillaTypes.ITEM_STACK);
    }
}