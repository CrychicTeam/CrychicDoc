package net.minecraftforge.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;

public interface IIngredientSerializer<T extends Ingredient> {

    T parse(FriendlyByteBuf var1);

    T parse(JsonObject var1);

    void write(FriendlyByteBuf var1, T var2);
}