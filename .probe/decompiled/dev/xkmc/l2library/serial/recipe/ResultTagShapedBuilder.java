package dev.xkmc.l2library.serial.recipe;

import com.google.gson.JsonObject;
import dev.xkmc.l2serial.serialization.custom_handler.StackHelper;
import java.util.function.Consumer;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ResultTagShapedBuilder extends ShapedRecipeBuilder implements IExtendedRecipe {

    private final ItemStack stack;

    public ResultTagShapedBuilder(ItemStack stack) {
        super(RecipeCategory.MISC, stack.getItem(), stack.getCount());
        this.stack = stack;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pvd, ResourceLocation id) {
        this.m_126143_(id);
        this.f_126110_.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        pvd.accept(new ExtendedRecipeResult(new ShapedRecipeBuilder.Result(id, this.f_126106_, this.f_126107_, this.f_126111_ == null ? "" : this.f_126111_, CraftingBookCategory.MISC, this.f_126108_, this.f_126109_, this.f_126110_, new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath()), false), this));
    }

    @Override
    public void addAdditional(JsonObject json) {
        json.add("result", StackHelper.serializeForgeItemStack(this.stack));
    }

    @Override
    public RecipeSerializer<?> getType() {
        return RecipeSerializer.SHAPED_RECIPE;
    }
}