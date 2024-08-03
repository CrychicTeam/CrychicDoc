package net.minecraft.data.recipes;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public interface RecipeBuilder {

    ResourceLocation ROOT_RECIPE_ADVANCEMENT = new ResourceLocation("recipes/root");

    RecipeBuilder unlockedBy(String var1, CriterionTriggerInstance var2);

    RecipeBuilder group(@Nullable String var1);

    Item getResult();

    void save(Consumer<FinishedRecipe> var1, ResourceLocation var2);

    default void save(Consumer<FinishedRecipe> consumerFinishedRecipe0) {
        this.save(consumerFinishedRecipe0, getDefaultRecipeId(this.getResult()));
    }

    default void save(Consumer<FinishedRecipe> consumerFinishedRecipe0, String string1) {
        ResourceLocation $$2 = getDefaultRecipeId(this.getResult());
        ResourceLocation $$3 = new ResourceLocation(string1);
        if ($$3.equals($$2)) {
            throw new IllegalStateException("Recipe " + string1 + " should remove its 'save' argument as it is equal to default one");
        } else {
            this.save(consumerFinishedRecipe0, $$3);
        }
    }

    static ResourceLocation getDefaultRecipeId(ItemLike itemLike0) {
        return BuiltInRegistries.ITEM.getKey(itemLike0.asItem());
    }
}