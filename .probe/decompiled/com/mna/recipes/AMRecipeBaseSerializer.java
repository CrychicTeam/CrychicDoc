package com.mna.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public abstract class AMRecipeBaseSerializer<T extends AMRecipeBase> implements RecipeSerializer<T> {

    public final T fromJson(ResourceLocation recipeId, JsonObject json) {
        return this.readExtra(recipeId, json);
    }

    public final T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        T inst = this.readExtra(recipeId, buffer);
        inst.tier = buffer.readInt();
        return inst;
    }

    public final void toNetwork(FriendlyByteBuf buffer, T recipe) {
        this.writeExtra(buffer, recipe);
        buffer.writeInt(recipe.getTier());
    }

    protected abstract T readExtra(ResourceLocation var1, JsonObject var2);

    protected abstract T readExtra(ResourceLocation var1, FriendlyByteBuf var2);

    protected abstract void writeExtra(FriendlyByteBuf var1, T var2);
}