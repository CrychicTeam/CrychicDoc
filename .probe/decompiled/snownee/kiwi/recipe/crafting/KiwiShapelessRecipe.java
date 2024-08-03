package snownee.kiwi.recipe.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import snownee.kiwi.data.DataModule;

public class KiwiShapelessRecipe extends ShapelessRecipe {

    private boolean noContainers;

    private boolean trimmed;

    public KiwiShapelessRecipe(ShapelessRecipe rawRecipe, boolean noContainers) {
        super(rawRecipe.getId(), rawRecipe.getGroup(), rawRecipe.category(), rawRecipe.getResultItem(null), rawRecipe.getIngredients());
        this.noContainers = noContainers;
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        this.trim();
        return super.matches(craftingContainer, level);
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        this.trim();
        return super.canCraftInDimensions(i, j);
    }

    private void trim() {
        if (!this.trimmed) {
            this.trimmed = true;
            this.m_7527_().removeIf(Ingredient::m_43947_);
        }
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        return this.noContainers ? NonNullList.withSize(inv.m_6643_(), ItemStack.EMPTY) : super.m_7457_(inv);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DataModule.SHAPELESS.get();
    }

    public static class Serializer implements RecipeSerializer<KiwiShapelessRecipe> {

        public KiwiShapelessRecipe fromJson(ResourceLocation id, JsonObject o) {
            String s = GsonHelper.getAsString(o, "group", "");
            NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(o, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else {
                CraftingBookCategory category = (CraftingBookCategory) CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(o, "category", null), CraftingBookCategory.MISC);
                ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(o, "result"));
                return new KiwiShapelessRecipe(new ShapelessRecipe(id, s, category, itemstack, nonnulllist), GsonHelper.getAsBoolean(o, "no_containers", false));
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray a) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();
            for (int i = 0; i < a.size(); i++) {
                Ingredient ingredient = Ingredient.fromJson(a.get(i));
                if (ingredient != Ingredient.EMPTY) {
                    nonnulllist.add(ingredient);
                }
            }
            return nonnulllist;
        }

        public KiwiShapelessRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return new KiwiShapelessRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(recipeId, buffer), buffer.readBoolean());
        }

        public void toNetwork(FriendlyByteBuf buffer, KiwiShapelessRecipe recipe) {
            RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buffer, recipe);
            buffer.writeBoolean(recipe.noContainers);
        }
    }
}