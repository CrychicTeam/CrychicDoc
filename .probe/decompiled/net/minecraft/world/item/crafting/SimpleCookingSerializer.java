package net.minecraft.world.item.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class SimpleCookingSerializer<T extends AbstractCookingRecipe> implements RecipeSerializer<T> {

    private final int defaultCookingTime;

    private final SimpleCookingSerializer.CookieBaker<T> factory;

    public SimpleCookingSerializer(SimpleCookingSerializer.CookieBaker<T> simpleCookingSerializerCookieBakerT0, int int1) {
        this.defaultCookingTime = int1;
        this.factory = simpleCookingSerializerCookieBakerT0;
    }

    public T fromJson(ResourceLocation resourceLocation0, JsonObject jsonObject1) {
        String $$2 = GsonHelper.getAsString(jsonObject1, "group", "");
        CookingBookCategory $$3 = (CookingBookCategory) CookingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject1, "category", null), CookingBookCategory.MISC);
        JsonElement $$4 = (JsonElement) (GsonHelper.isArrayNode(jsonObject1, "ingredient") ? GsonHelper.getAsJsonArray(jsonObject1, "ingredient") : GsonHelper.getAsJsonObject(jsonObject1, "ingredient"));
        Ingredient $$5 = Ingredient.fromJson($$4, false);
        String $$6 = GsonHelper.getAsString(jsonObject1, "result");
        ResourceLocation $$7 = new ResourceLocation($$6);
        ItemStack $$8 = new ItemStack((ItemLike) BuiltInRegistries.ITEM.m_6612_($$7).orElseThrow(() -> new IllegalStateException("Item: " + $$6 + " does not exist")));
        float $$9 = GsonHelper.getAsFloat(jsonObject1, "experience", 0.0F);
        int $$10 = GsonHelper.getAsInt(jsonObject1, "cookingtime", this.defaultCookingTime);
        return this.factory.create(resourceLocation0, $$2, $$3, $$5, $$8, $$9, $$10);
    }

    public T fromNetwork(ResourceLocation resourceLocation0, FriendlyByteBuf friendlyByteBuf1) {
        String $$2 = friendlyByteBuf1.readUtf();
        CookingBookCategory $$3 = friendlyByteBuf1.readEnum(CookingBookCategory.class);
        Ingredient $$4 = Ingredient.fromNetwork(friendlyByteBuf1);
        ItemStack $$5 = friendlyByteBuf1.readItem();
        float $$6 = friendlyByteBuf1.readFloat();
        int $$7 = friendlyByteBuf1.readVarInt();
        return this.factory.create(resourceLocation0, $$2, $$3, $$4, $$5, $$6, $$7);
    }

    public void toNetwork(FriendlyByteBuf friendlyByteBuf0, T t1) {
        friendlyByteBuf0.writeUtf(t1.group);
        friendlyByteBuf0.writeEnum(t1.category());
        t1.ingredient.toNetwork(friendlyByteBuf0);
        friendlyByteBuf0.writeItem(t1.result);
        friendlyByteBuf0.writeFloat(t1.experience);
        friendlyByteBuf0.writeVarInt(t1.cookingTime);
    }

    interface CookieBaker<T extends AbstractCookingRecipe> {

        T create(ResourceLocation var1, String var2, CookingBookCategory var3, Ingredient var4, ItemStack var5, float var6, int var7);
    }
}