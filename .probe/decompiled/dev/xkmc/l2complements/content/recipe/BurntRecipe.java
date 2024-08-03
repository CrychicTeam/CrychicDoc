package dev.xkmc.l2complements.content.recipe;

import dev.xkmc.l2complements.init.registrate.LCRecipes;
import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

@SerialClass
public class BurntRecipe extends BaseRecipe<BurntRecipe, BurntRecipe, BurntRecipe.Inv> {

    @SerialField
    public Ingredient ingredient;

    @SerialField
    public ItemStack result;

    @SerialField
    public int chance;

    public BurntRecipe(ResourceLocation id) {
        super(id, (BaseRecipe.RecType<BurntRecipe, BurntRecipe, BurntRecipe.Inv>) LCRecipes.RS_BURNT.get());
    }

    public boolean matches(BurntRecipe.Inv inv, Level level) {
        return this.ingredient.test(inv.m_8020_(0));
    }

    public ItemStack assemble(BurntRecipe.Inv inv, RegistryAccess access) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return this.result;
    }

    public static class Inv extends SimpleContainer implements BaseRecipe.RecInv<BurntRecipe> {

        public Inv() {
            super(1);
        }
    }
}