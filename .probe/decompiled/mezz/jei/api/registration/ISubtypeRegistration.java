package mezz.jei.api.registration;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public interface ISubtypeRegistration {

    <B, I> void registerSubtypeInterpreter(IIngredientTypeWithSubtypes<B, I> var1, B var2, IIngredientSubtypeInterpreter<I> var3);

    default void registerSubtypeInterpreter(Item item, IIngredientSubtypeInterpreter<ItemStack> interpreter) {
        this.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, interpreter);
    }

    void useNbtForSubtypes(Item... var1);

    void useNbtForSubtypes(Fluid... var1);
}