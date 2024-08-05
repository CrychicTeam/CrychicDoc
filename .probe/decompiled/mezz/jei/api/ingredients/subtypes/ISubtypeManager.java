package mezz.jei.api.ingredients.subtypes;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import net.minecraft.world.item.ItemStack;

public interface ISubtypeManager {

    default String getSubtypeInfo(ItemStack ingredient, UidContext context) {
        return this.getSubtypeInfo(VanillaTypes.ITEM_STACK, ingredient, context);
    }

    <T> String getSubtypeInfo(IIngredientTypeWithSubtypes<?, T> var1, T var2, UidContext var3);
}