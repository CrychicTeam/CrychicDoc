package mezz.jei.library.plugins.vanilla.compostable;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import mezz.jei.api.recipe.vanilla.IJeiCompostingRecipe;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public class CompostingRecipeMaker {

    public static List<IJeiCompostingRecipe> getRecipes(IIngredientManager ingredientManager) {
        Object2FloatMap<ItemLike> compostables = ComposterBlock.COMPOSTABLES;
        Collection<ItemStack> allIngredients = ingredientManager.getAllItemStacks();
        return allIngredients.stream().mapMulti((itemStack, consumer) -> {
            Item item = itemStack.getItem();
            float compostValue = compostables.getOrDefault(item, 0.0F);
            if (compostValue > 0.0F) {
                CompostingRecipe recipe = new CompostingRecipe(itemStack, compostValue);
                consumer.accept(recipe);
            }
        }).limit((long) compostables.size()).sorted(Comparator.comparingDouble(IJeiCompostingRecipe::getChance)).toList();
    }
}