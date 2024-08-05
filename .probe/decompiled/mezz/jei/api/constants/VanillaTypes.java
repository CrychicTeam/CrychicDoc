package mezz.jei.api.constants;

import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class VanillaTypes {

    public static final IIngredientTypeWithSubtypes<Item, ItemStack> ITEM_STACK = new IIngredientTypeWithSubtypes<Item, ItemStack>() {

        @Override
        public Class<? extends ItemStack> getIngredientClass() {
            return ItemStack.class;
        }

        @Override
        public Class<? extends Item> getIngredientBaseClass() {
            return Item.class;
        }

        public Item getBase(ItemStack ingredient) {
            return ingredient.getItem();
        }
    };

    private VanillaTypes() {
    }
}