package dev.xkmc.l2complements.compat.ars;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;

public record ArsRecipeBuilder(ResourceLocation type, Enchantment enchantment, int level, int sourceCost, ArrayList<ArsRecipeBuilder.WrappedIngredient> pedestalItems) {

    public static ArsRecipeBuilder of(Enchantment ench, int lv, int cost, List<Ingredient> ings) {
        ArrayList<ArsRecipeBuilder.WrappedIngredient> list = new ArrayList();
        for (Ingredient e : ings) {
            list.add(new ArsRecipeBuilder.WrappedIngredient(e));
        }
        return new ArsRecipeBuilder(new ResourceLocation("ars_nouveau:enchantment"), ench, lv, cost, list);
    }

    public static record WrappedIngredient(Ingredient item) {
    }
}