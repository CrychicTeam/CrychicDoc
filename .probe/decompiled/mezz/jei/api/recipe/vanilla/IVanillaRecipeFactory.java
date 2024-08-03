package mezz.jei.api.recipe.vanilla;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public interface IVanillaRecipeFactory {

    IJeiAnvilRecipe createAnvilRecipe(ItemStack var1, List<ItemStack> var2, List<ItemStack> var3);

    IJeiAnvilRecipe createAnvilRecipe(List<ItemStack> var1, List<ItemStack> var2, List<ItemStack> var3);

    IJeiBrewingRecipe createBrewingRecipe(List<ItemStack> var1, ItemStack var2, ItemStack var3);

    IJeiBrewingRecipe createBrewingRecipe(List<ItemStack> var1, List<ItemStack> var2, ItemStack var3);
}