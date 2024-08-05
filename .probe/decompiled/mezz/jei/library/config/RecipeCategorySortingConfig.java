package mezz.jei.library.config;

import java.nio.file.Path;
import java.util.Comparator;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.common.config.sorting.MappedSortingConfig;
import mezz.jei.common.config.sorting.serializers.SortingSerializers;

public class RecipeCategorySortingConfig extends MappedSortingConfig<RecipeType<?>, String> {

    public RecipeCategorySortingConfig(Path path) {
        super(path, SortingSerializers.STRING, r -> r.getUid().toString());
    }

    @Override
    protected Comparator<String> getDefaultSortOrder() {
        Comparator<String> minecraftCraftingFirst = Comparator.comparing(s -> {
            String vanillaCrafting = RecipeTypes.CRAFTING.getUid().toString();
            return s.equals(vanillaCrafting);
        }).reversed();
        Comparator<String> minecraftFirst = Comparator.comparing(s -> s.startsWith("minecraft")).reversed();
        Comparator<String> naturalOrder = Comparator.naturalOrder();
        return minecraftCraftingFirst.thenComparing(minecraftFirst).thenComparing(naturalOrder);
    }
}