package org.violetmoon.zeta.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.violetmoon.zeta.Zeta;

public interface IZetaIngredientSerializer<T extends Ingredient> {

    T parse(FriendlyByteBuf var1);

    T parse(JsonObject var1);

    void write(FriendlyByteBuf var1, T var2);

    Zeta getZeta();

    default ResourceLocation getID() {
        return this.getZeta().craftingExtensions.getID(this);
    }
}