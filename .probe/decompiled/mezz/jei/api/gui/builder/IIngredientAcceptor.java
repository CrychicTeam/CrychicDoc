package mezz.jei.api.gui.builder;

import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

public interface IIngredientAcceptor<THIS extends IIngredientAcceptor<THIS>> {

    <I> THIS addIngredients(IIngredientType<I> var1, List<I> var2);

    <I> THIS addIngredient(IIngredientType<I> var1, I var2);

    THIS addIngredientsUnsafe(List<?> var1);

    default THIS addIngredients(Ingredient ingredient) {
        return this.addIngredients(VanillaTypes.ITEM_STACK, List.of(ingredient.getItems()));
    }

    default THIS addItemStacks(List<ItemStack> itemStacks) {
        return this.addIngredients(VanillaTypes.ITEM_STACK, itemStacks);
    }

    default THIS addItemStack(ItemStack itemStack) {
        return this.addIngredient(VanillaTypes.ITEM_STACK, itemStack);
    }

    THIS addFluidStack(Fluid var1, long var2);

    THIS addFluidStack(Fluid var1, long var2, CompoundTag var4);
}