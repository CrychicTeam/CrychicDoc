package mezz.jei.api.recipe.vanilla;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

public interface IJeiBrewingRecipe {

    @Unmodifiable
    List<ItemStack> getPotionInputs();

    @Unmodifiable
    List<ItemStack> getIngredients();

    ItemStack getPotionOutput();

    int getBrewingSteps();
}