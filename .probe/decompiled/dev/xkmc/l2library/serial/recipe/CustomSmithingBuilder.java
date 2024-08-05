package dev.xkmc.l2library.serial.recipe;

import com.google.gson.JsonObject;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.RegistryEntry;
import java.util.function.Consumer;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class CustomSmithingBuilder<T extends AbstractSmithingRecipe<T>> extends SmithingTransformRecipeBuilder implements IExtendedRecipe {

    private final RegistryEntry<AbstractSmithingRecipe.Serializer<T>> serializer;

    public CustomSmithingBuilder(RegistryEntry<AbstractSmithingRecipe.Serializer<T>> serializer, Ingredient left, Ingredient right, Item result) {
        super((RecipeSerializer<?>) serializer.get(), AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER, left, right, RecipeCategory.MISC, result);
        this.serializer = serializer;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pvd, ResourceLocation id) {
        this.m_266305_(id);
        this.f_266090_.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        pvd.accept(new ExtendedRecipeResult(new SmithingTransformRecipeBuilder.Result(id, this.getType(), AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER, this.f_265893_, this.f_265959_, this.f_266005_, this.f_266090_, new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath())), this));
    }

    public CustomSmithingBuilder<T> unlockedBy(RegistrateRecipeProvider pvd, ItemLike item) {
        this.f_266090_.addCriterion("has_" + pvd.safeName(item.asItem()), DataIngredient.items(item.asItem(), new Item[0]).getCritereon(pvd));
        return this;
    }

    @Override
    public void addAdditional(JsonObject obj) {
    }

    @Override
    public RecipeSerializer<?> getType() {
        return (RecipeSerializer<?>) this.serializer.get();
    }
}