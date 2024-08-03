package fuzs.puzzleslib.api.data.v1.recipes;

import fuzs.puzzleslib.impl.item.CopyTagRecipe;
import java.util.function.Consumer;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class CopyTagShapelessRecipeBuilder extends ShapelessRecipeBuilder {

    private Ingredient copyFrom;

    public CopyTagShapelessRecipeBuilder(RecipeCategory recipeCategory, ItemLike result, int count) {
        super(recipeCategory, result, count);
    }

    public static CopyTagShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike result) {
        return shapeless(category, result, 1);
    }

    public static CopyTagShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike result, int count) {
        return new CopyTagShapelessRecipeBuilder(category, result, count);
    }

    public CopyTagShapelessRecipeBuilder requires(TagKey<Item> tag) {
        super.requires(tag);
        return this;
    }

    public CopyTagShapelessRecipeBuilder requires(ItemLike item) {
        super.requires(item);
        return this;
    }

    public CopyTagShapelessRecipeBuilder requires(ItemLike item, int quantity) {
        super.requires(item, quantity);
        return this;
    }

    public CopyTagShapelessRecipeBuilder requires(Ingredient ingredient) {
        super.requires(ingredient);
        return this;
    }

    public CopyTagShapelessRecipeBuilder requires(Ingredient ingredient, int quantity) {
        super.requires(ingredient, quantity);
        return this;
    }

    public CopyTagShapelessRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
        super.unlockedBy(criterionName, criterionTrigger);
        return this;
    }

    public CopyTagShapelessRecipeBuilder group(@Nullable String groupName) {
        super.group(groupName);
        return this;
    }

    public CopyTagShapelessRecipeBuilder copyFrom(ItemLike copyFrom) {
        return this.copyFrom(Ingredient.of(copyFrom));
    }

    public CopyTagShapelessRecipeBuilder copyFrom(Ingredient copyFrom) {
        this.copyFrom = copyFrom;
        return this;
    }

    @Override
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation resourceLocation) {
        super.save(finishedRecipe -> {
            RecipeSerializer<?> recipeSerializer = CopyTagRecipe.getModSerializer(resourceLocation.getNamespace(), "copy_tag_shapeless_recipe");
            finishedRecipeConsumer.accept(new ForwardingFinishedRecipe(finishedRecipe, json -> json.add("copy_from", this.copyFrom.toJson()), recipeSerializer));
        }, resourceLocation);
    }
}