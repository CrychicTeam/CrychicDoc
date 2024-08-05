package com.mna.interop.jei.ingredients;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class ManaweavePatternIngredientSerializer implements IIngredientSerializer<ManaweavePatternIngredient> {

    public ManaweavePatternIngredient parse(FriendlyByteBuf buffer) {
        return new ManaweavePatternIngredient(buffer.readResourceLocation());
    }

    public ManaweavePatternIngredient parse(JsonObject json) {
        return null;
    }

    public void write(FriendlyByteBuf buffer, ManaweavePatternIngredient ingredient) {
        buffer.writeResourceLocation(ingredient.getWeaveId());
    }
}