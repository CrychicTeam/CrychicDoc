package org.violetmoon.quark.base.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.IShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class ExclusionRecipe implements CraftingRecipe {

    public static final ExclusionRecipe.Serializer SERIALIZER = new ExclusionRecipe.Serializer();

    protected final CraftingRecipe parent;

    private final List<ResourceLocation> excluded;

    public ExclusionRecipe(CraftingRecipe parent, List<ResourceLocation> excluded) {
        this.parent = parent;
        this.excluded = excluded;
    }

    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level worldIn) {
        for (ResourceLocation recipeLoc : this.excluded) {
            Optional<? extends Recipe<?>> recipeHolder = worldIn.getRecipeManager().byKey(recipeLoc);
            if (recipeHolder.isPresent()) {
                Recipe<?> recipe = (Recipe<?>) recipeHolder.get();
                if (recipe instanceof CraftingRecipe craftingRecipe && craftingRecipe.m_5818_(inv, worldIn)) {
                    return false;
                }
            }
        }
        return this.parent.m_5818_(inv, worldIn);
    }

    @NotNull
    public ItemStack assemble(@NotNull CraftingContainer inv, RegistryAccess gaming) {
        return this.parent.m_5874_(inv, gaming);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return this.parent.m_8004_(width, height);
    }

    @NotNull
    @Override
    public ItemStack getResultItem(RegistryAccess acc) {
        return this.parent.m_8043_(acc);
    }

    @NotNull
    @Override
    public ResourceLocation getId() {
        return this.parent.m_6423_();
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @NotNull
    @Override
    public RecipeType<?> getType() {
        return this.parent.getType();
    }

    @NotNull
    public NonNullList<ItemStack> getRemainingItems(@NotNull CraftingContainer inv) {
        return this.parent.m_7457_(inv);
    }

    @NotNull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.parent.m_7527_();
    }

    @Override
    public boolean isSpecial() {
        return this.parent.m_5598_();
    }

    @NotNull
    @Override
    public String getGroup() {
        return this.parent.m_6076_();
    }

    @NotNull
    @Override
    public ItemStack getToastSymbol() {
        return this.parent.m_8042_();
    }

    @Override
    public CraftingBookCategory category() {
        return this.parent.category();
    }

    public static class Serializer implements RecipeSerializer<ExclusionRecipe> {

        @NotNull
        public ExclusionRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            String trueType = GsonHelper.getAsString(json, "true_type");
            if (trueType.equals("quark:exclusion")) {
                throw new JsonSyntaxException("Recipe type circularity");
            } else {
                JsonArray excluded = GsonHelper.getAsJsonArray(json, "exclusions");
                List<ResourceLocation> excludedRecipes = new ArrayList();
                for (JsonElement el : excluded) {
                    ResourceLocation loc = new ResourceLocation(el.getAsString());
                    if (!loc.equals(recipeId)) {
                        excludedRecipes.add(loc);
                    }
                }
                RecipeSerializer<?> serializer = BuiltInRegistries.RECIPE_SERIALIZER.get(new ResourceLocation(trueType));
                if (serializer == null) {
                    throw new JsonSyntaxException("Invalid or unsupported recipe type '" + trueType + "'");
                } else {
                    Recipe<?> parent = serializer.fromJson(recipeId, json);
                    if (!(parent instanceof CraftingRecipe)) {
                        throw new JsonSyntaxException("Type '" + trueType + "' is not a crafting recipe");
                    } else {
                        return (ExclusionRecipe) (parent instanceof IShapedRecipe ? new ExclusionRecipe.ShapedExclusionRecipe((CraftingRecipe) parent, excludedRecipes) : new ExclusionRecipe((CraftingRecipe) parent, excludedRecipes));
                    }
                }
            }
        }

        @NotNull
        public ExclusionRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            int exclusions = buffer.readVarInt();
            List<ResourceLocation> excludedRecipes = new ArrayList();
            for (int i = 0; i < exclusions; i++) {
                ResourceLocation loc = new ResourceLocation(buffer.readUtf(32767));
                if (!loc.equals(recipeId)) {
                    excludedRecipes.add(loc);
                }
            }
            String trueType = buffer.readUtf(32767);
            RecipeSerializer<?> serializer = BuiltInRegistries.RECIPE_SERIALIZER.get(new ResourceLocation(trueType));
            if (serializer == null) {
                throw new IllegalArgumentException("Invalid or unsupported recipe type '" + trueType + "'");
            } else {
                Recipe<?> parent = serializer.fromNetwork(recipeId, buffer);
                if (parent instanceof CraftingRecipe craftingRecipe) {
                    return (ExclusionRecipe) (parent instanceof IShapedRecipe ? new ExclusionRecipe.ShapedExclusionRecipe(craftingRecipe, excludedRecipes) : new ExclusionRecipe(craftingRecipe, excludedRecipes));
                } else {
                    throw new IllegalArgumentException("Type '" + trueType + "' is not a crafting recipe");
                }
            }
        }

        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull ExclusionRecipe recipe) {
            buffer.writeVarInt(recipe.excluded.size());
            for (ResourceLocation loc : recipe.excluded) {
                buffer.writeUtf(loc.toString(), 32767);
            }
            buffer.writeUtf(Objects.toString(BuiltInRegistries.RECIPE_SERIALIZER.getKey(recipe.parent.m_7707_())), 32767);
            recipe.parent.m_7707_().toNetwork(buffer, recipe.parent);
        }
    }

    private static class ShapedExclusionRecipe extends ExclusionRecipe implements IShapedRecipe<CraftingContainer> {

        private final IShapedRecipe<CraftingContainer> shapedParent;

        public ShapedExclusionRecipe(CraftingRecipe shapedParent, List<ResourceLocation> excluded) {
            super(shapedParent, excluded);
            this.shapedParent = (IShapedRecipe<CraftingContainer>) shapedParent;
        }

        @Override
        public int getRecipeWidth() {
            return this.shapedParent.getRecipeWidth();
        }

        @Override
        public int getRecipeHeight() {
            return this.shapedParent.getRecipeHeight();
        }
    }
}