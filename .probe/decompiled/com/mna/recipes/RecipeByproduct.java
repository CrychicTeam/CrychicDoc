package com.mna.recipes;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeByproduct {

    public final ItemStack stack;

    public final float chance;

    @Nullable
    public static RecipeByproduct FromJSON(JsonObject json) {
        if (json.has("item") && json.has("chance")) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("item").getAsString()));
            if (item != null) {
                ItemStack stack = new ItemStack(item);
                if (json.has("count")) {
                    stack.setCount(json.get("count").getAsInt());
                }
                return new RecipeByproduct(stack, json.get("chance").getAsFloat());
            }
        }
        return null;
    }

    private RecipeByproduct(ItemStack stack, float chance) {
        this.stack = stack;
        this.chance = chance;
    }
}