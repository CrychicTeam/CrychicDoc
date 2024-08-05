package net.minecraft.world.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public abstract class SingleItemRecipe implements Recipe<Container> {

    protected final Ingredient ingredient;

    protected final ItemStack result;

    private final RecipeType<?> type;

    private final RecipeSerializer<?> serializer;

    protected final ResourceLocation id;

    protected final String group;

    public SingleItemRecipe(RecipeType<?> recipeType0, RecipeSerializer<?> recipeSerializer1, ResourceLocation resourceLocation2, String string3, Ingredient ingredient4, ItemStack itemStack5) {
        this.type = recipeType0;
        this.serializer = recipeSerializer1;
        this.id = resourceLocation2;
        this.group = string3;
        this.ingredient = ingredient4;
        this.result = itemStack5;
    }

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess0) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> $$0 = NonNullList.create();
        $$0.add(this.ingredient);
        return $$0;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return true;
    }

    @Override
    public ItemStack assemble(Container container0, RegistryAccess registryAccess1) {
        return this.result.copy();
    }

    public static class Serializer<T extends SingleItemRecipe> implements RecipeSerializer<T> {

        final SingleItemRecipe.Serializer.SingleItemMaker<T> factory;

        protected Serializer(SingleItemRecipe.Serializer.SingleItemMaker<T> singleItemRecipeSerializerSingleItemMakerT0) {
            this.factory = singleItemRecipeSerializerSingleItemMakerT0;
        }

        public T fromJson(ResourceLocation resourceLocation0, JsonObject jsonObject1) {
            String $$2 = GsonHelper.getAsString(jsonObject1, "group", "");
            Ingredient $$3;
            if (GsonHelper.isArrayNode(jsonObject1, "ingredient")) {
                $$3 = Ingredient.fromJson(GsonHelper.getAsJsonArray(jsonObject1, "ingredient"), false);
            } else {
                $$3 = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject1, "ingredient"), false);
            }
            String $$5 = GsonHelper.getAsString(jsonObject1, "result");
            int $$6 = GsonHelper.getAsInt(jsonObject1, "count");
            ItemStack $$7 = new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation($$5)), $$6);
            return this.factory.create(resourceLocation0, $$2, $$3, $$7);
        }

        public T fromNetwork(ResourceLocation resourceLocation0, FriendlyByteBuf friendlyByteBuf1) {
            String $$2 = friendlyByteBuf1.readUtf();
            Ingredient $$3 = Ingredient.fromNetwork(friendlyByteBuf1);
            ItemStack $$4 = friendlyByteBuf1.readItem();
            return this.factory.create(resourceLocation0, $$2, $$3, $$4);
        }

        public void toNetwork(FriendlyByteBuf friendlyByteBuf0, T t1) {
            friendlyByteBuf0.writeUtf(t1.group);
            t1.ingredient.toNetwork(friendlyByteBuf0);
            friendlyByteBuf0.writeItem(t1.result);
        }

        interface SingleItemMaker<T extends SingleItemRecipe> {

            T create(ResourceLocation var1, String var2, Ingredient var3, ItemStack var4);
        }
    }
}