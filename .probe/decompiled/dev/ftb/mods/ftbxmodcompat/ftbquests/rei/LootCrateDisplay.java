package dev.ftb.mods.ftbxmodcompat.ftbquests.rei;

import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.WrappedLootCrate;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;

public class LootCrateDisplay extends BasicDisplay {

    private final WrappedLootCrate wrappedLootCrate;

    public LootCrateDisplay(WrappedLootCrate wrappedLootCrate) {
        super(EntryIngredients.ofIngredients(wrappedLootCrate.inputIngredients()), EntryIngredients.ofIngredients(wrappedLootCrate.outputIngredients()));
        this.wrappedLootCrate = wrappedLootCrate;
    }

    public CategoryIdentifier<?> getCategoryIdentifier() {
        return REICategories.LOOT_CRATE;
    }

    public WrappedLootCrate getWrappedLootCrate() {
        return this.wrappedLootCrate;
    }
}