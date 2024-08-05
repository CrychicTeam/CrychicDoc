package snownee.lychee.compat.rei.display;

import java.util.List;
import java.util.stream.Stream;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.plugin.common.displays.anvil.AnvilRecipe;
import me.shedaniel.rei.plugin.common.displays.anvil.DefaultAnvilDisplay;
import net.minecraft.world.item.ItemStack;
import snownee.lychee.anvil_crafting.AnvilCraftingRecipe;
import snownee.lychee.core.recipe.ILycheeRecipe;

public class AnvilCraftingDisplay extends DefaultAnvilDisplay implements DisplayRecipeProvider {

    private final AnvilCraftingRecipe lycheeRecipe;

    public AnvilCraftingDisplay(AnvilCraftingRecipe lycheeRecipe) {
        super(makeRecipe(lycheeRecipe));
        this.lycheeRecipe = lycheeRecipe;
    }

    private static AnvilRecipe makeRecipe(AnvilCraftingRecipe $) {
        List<ItemStack> right = Stream.of($.getRight().getItems()).map(ItemStack::m_41777_).peek($$ -> $$.setCount($.getMaterialCost())).toList();
        return new AnvilRecipe($.m_6423_(), List.of($.getLeft().getItems()), right, List.of($.getResultItem()));
    }

    @Override
    public ILycheeRecipe<?> recipe() {
        return this.lycheeRecipe;
    }

    public List<EntryIngredient> getOutputEntries() {
        List<EntryIngredient> ingredients = BaseREIDisplay.getOutputEntries(this.recipe());
        ingredients.addAll(0, super.getOutputEntries());
        return ingredients;
    }
}