package net.minecraft.world.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class SimpleCraftingRecipeSerializer<T extends CraftingRecipe> implements RecipeSerializer<T> {

    private final SimpleCraftingRecipeSerializer.Factory<T> constructor;

    public SimpleCraftingRecipeSerializer(SimpleCraftingRecipeSerializer.Factory<T> simpleCraftingRecipeSerializerFactoryT0) {
        this.constructor = simpleCraftingRecipeSerializerFactoryT0;
    }

    public T fromJson(ResourceLocation resourceLocation0, JsonObject jsonObject1) {
        CraftingBookCategory $$2 = (CraftingBookCategory) CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject1, "category", null), CraftingBookCategory.MISC);
        return this.constructor.create(resourceLocation0, $$2);
    }

    public T fromNetwork(ResourceLocation resourceLocation0, FriendlyByteBuf friendlyByteBuf1) {
        CraftingBookCategory $$2 = friendlyByteBuf1.readEnum(CraftingBookCategory.class);
        return this.constructor.create(resourceLocation0, $$2);
    }

    public void toNetwork(FriendlyByteBuf friendlyByteBuf0, T t1) {
        friendlyByteBuf0.writeEnum(t1.category());
    }

    @FunctionalInterface
    public interface Factory<T extends CraftingRecipe> {

        T create(ResourceLocation var1, CraftingBookCategory var2);
    }
}