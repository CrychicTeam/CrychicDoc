package net.minecraft.world.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ShapelessRecipe implements CraftingRecipe {

    private final ResourceLocation id;

    final String group;

    final CraftingBookCategory category;

    final ItemStack result;

    final NonNullList<Ingredient> ingredients;

    public ShapelessRecipe(ResourceLocation resourceLocation0, String string1, CraftingBookCategory craftingBookCategory2, ItemStack itemStack3, NonNullList<Ingredient> nonNullListIngredient4) {
        this.id = resourceLocation0;
        this.group = string1;
        this.category = craftingBookCategory2;
        this.result = itemStack3;
        this.ingredients = nonNullListIngredient4;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHAPELESS_RECIPE;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess0) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        StackedContents $$2 = new StackedContents();
        int $$3 = 0;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                $$3++;
                $$2.accountStack($$5, 1);
            }
        }
        return $$3 == this.ingredients.size() && $$2.canCraft(this, null);
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 * int1 >= this.ingredients.size();
    }

    public static class Serializer implements RecipeSerializer<ShapelessRecipe> {

        public ShapelessRecipe fromJson(ResourceLocation resourceLocation0, JsonObject jsonObject1) {
            String $$2 = GsonHelper.getAsString(jsonObject1, "group", "");
            CraftingBookCategory $$3 = (CraftingBookCategory) CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject1, "category", null), CraftingBookCategory.MISC);
            NonNullList<Ingredient> $$4 = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject1, "ingredients"));
            if ($$4.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if ($$4.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            } else {
                ItemStack $$5 = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject1, "result"));
                return new ShapelessRecipe(resourceLocation0, $$2, $$3, $$5, $$4);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray0) {
            NonNullList<Ingredient> $$1 = NonNullList.create();
            for (int $$2 = 0; $$2 < jsonArray0.size(); $$2++) {
                Ingredient $$3 = Ingredient.fromJson(jsonArray0.get($$2), false);
                if (!$$3.isEmpty()) {
                    $$1.add($$3);
                }
            }
            return $$1;
        }

        public ShapelessRecipe fromNetwork(ResourceLocation resourceLocation0, FriendlyByteBuf friendlyByteBuf1) {
            String $$2 = friendlyByteBuf1.readUtf();
            CraftingBookCategory $$3 = friendlyByteBuf1.readEnum(CraftingBookCategory.class);
            int $$4 = friendlyByteBuf1.readVarInt();
            NonNullList<Ingredient> $$5 = NonNullList.withSize($$4, Ingredient.EMPTY);
            for (int $$6 = 0; $$6 < $$5.size(); $$6++) {
                $$5.set($$6, Ingredient.fromNetwork(friendlyByteBuf1));
            }
            ItemStack $$7 = friendlyByteBuf1.readItem();
            return new ShapelessRecipe(resourceLocation0, $$2, $$3, $$7, $$5);
        }

        public void toNetwork(FriendlyByteBuf friendlyByteBuf0, ShapelessRecipe shapelessRecipe1) {
            friendlyByteBuf0.writeUtf(shapelessRecipe1.group);
            friendlyByteBuf0.writeEnum(shapelessRecipe1.category);
            friendlyByteBuf0.writeVarInt(shapelessRecipe1.ingredients.size());
            for (Ingredient $$2 : shapelessRecipe1.ingredients) {
                $$2.toNetwork(friendlyByteBuf0);
            }
            friendlyByteBuf0.writeItem(shapelessRecipe1.result);
        }
    }
}