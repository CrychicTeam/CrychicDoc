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
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class CustomShapelessBuilder<T extends AbstractShapelessRecipe<T>> extends ShapelessRecipeBuilder implements IExtendedRecipe {

    private final RegistryEntry<AbstractShapelessRecipe.Serializer<T>> serializer;

    public CustomShapelessBuilder(RegistryEntry<AbstractShapelessRecipe.Serializer<T>> serializer, ItemLike result, int count) {
        super(RecipeCategory.MISC, result, count);
        this.serializer = serializer;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pvd, ResourceLocation id) {
        this.m_126207_(id);
        this.f_126176_.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        pvd.accept(new ExtendedRecipeResult(new ShapelessRecipeBuilder.Result(id, this.f_126173_, this.f_126174_, this.f_126177_ == null ? "" : this.f_126177_, CraftingBookCategory.MISC, this.f_126175_, this.f_126176_, new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath())), this));
    }

    public CustomShapelessBuilder<T> unlockedBy(RegistrateRecipeProvider pvd, ItemLike item) {
        this.f_126176_.addCriterion("has_" + pvd.safeName(item.asItem()), DataIngredient.items(item.asItem(), new Item[0]).getCritereon(pvd));
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