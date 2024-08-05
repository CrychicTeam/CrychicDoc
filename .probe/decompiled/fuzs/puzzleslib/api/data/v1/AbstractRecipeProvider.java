package fuzs.puzzleslib.api.data.v1;

import com.google.common.base.Preconditions;
import fuzs.puzzleslib.api.data.v1.recipes.ForwardingFinishedRecipe;
import fuzs.puzzleslib.api.item.v2.LegacySmithingTransformRecipe;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.data.event.GatherDataEvent;

public abstract class AbstractRecipeProvider extends RecipeProvider {

    public AbstractRecipeProvider(GatherDataEvent evt, String modId) {
        this(evt.getGenerator().getPackOutput());
    }

    public AbstractRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected abstract void buildRecipes(Consumer<FinishedRecipe> var1);

    @Deprecated(forRemoval = true)
    protected static void legacyNetheriteSmithing(String modId, Consumer<FinishedRecipe> exporter, Item base, RecipeCategory recipeCategory, Item result) {
        legacyNetheriteSmithing(modId, exporter, base, Items.NETHERITE_INGOT, recipeCategory, result);
    }

    @Deprecated(forRemoval = true)
    protected static void legacyNetheriteSmithing(String modId, Consumer<FinishedRecipe> exporter, Item base, Item addition, RecipeCategory recipeCategory, Item result) {
        new SmithingTransformRecipeBuilder(LegacySmithingTransformRecipe.getModSerializer(modId), Ingredient.of(), Ingredient.of(base), Ingredient.of(addition), recipeCategory, result).unlocks(m_176602_(addition), m_125977_(addition)).save(finishedRecipe -> exporter.accept(new ForwardingFinishedRecipe(finishedRecipe, json -> json.remove("template"))), new ResourceLocation(modId, m_176632_(result) + "_crafting_transform"));
    }

    protected static String getHasName(ItemLike... items) {
        Preconditions.checkPositionIndex(0, items.length - 1, "items is empty");
        return "has_" + (String) Stream.of(items).map(x$0 -> RecipeProvider.getItemName(x$0)).collect(Collectors.joining("_and_"));
    }

    protected static InventoryChangeTrigger.TriggerInstance has(ItemLike... items) {
        Preconditions.checkPositionIndex(0, items.length - 1, "items is empty");
        return m_126011_(new ItemPredicate[] { ItemPredicate.Builder.item().of(items).build() });
    }

    protected static void stonecutterResultFromBase(String modId, Consumer<FinishedRecipe> exporter, RecipeCategory category, ItemLike result, ItemLike ingredient) {
        stonecutterResultFromBase(modId, exporter, category, result, ingredient, 1);
    }

    protected static void stonecutterResultFromBase(String modId, Consumer<FinishedRecipe> exporter, RecipeCategory category, ItemLike result, ItemLike ingredient, int resultCount) {
        String recipeId = m_176517_(result, ingredient) + "_stonecutting";
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ingredient), category, result, resultCount).unlockedBy(m_176602_(ingredient), m_125977_(ingredient)).save(exporter, new ResourceLocation(modId, recipeId));
    }
}