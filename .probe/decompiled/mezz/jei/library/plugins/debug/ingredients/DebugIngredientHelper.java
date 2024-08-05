package mezz.jei.library.plugins.debug.ingredients;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class DebugIngredientHelper implements IIngredientHelper<DebugIngredient> {

    @Override
    public IIngredientType<DebugIngredient> getIngredientType() {
        return DebugIngredient.TYPE;
    }

    public String getDisplayName(DebugIngredient ingredient) {
        return "JEI Debug Item #" + ingredient.getNumber();
    }

    public String getUniqueId(DebugIngredient ingredient, UidContext context) {
        return "JEI_debug_" + ingredient.getNumber();
    }

    public ResourceLocation getResourceLocation(DebugIngredient ingredient) {
        return new ResourceLocation("jei", "debug_" + ingredient.getNumber());
    }

    public ItemStack getCheatItemStack(DebugIngredient ingredient) {
        return ItemStack.EMPTY;
    }

    public DebugIngredient copyIngredient(DebugIngredient ingredient) {
        return ingredient.copy();
    }

    public String getErrorInfo(@Nullable DebugIngredient ingredient) {
        return ingredient == null ? "debug ingredient: null" : this.getDisplayName(ingredient);
    }
}