package net.minecraft.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public interface Recipe<C extends Container> {

    boolean matches(C var1, Level var2);

    ItemStack assemble(C var1, RegistryAccess var2);

    boolean canCraftInDimensions(int var1, int var2);

    ItemStack getResultItem(RegistryAccess var1);

    default NonNullList<ItemStack> getRemainingItems(C c0) {
        NonNullList<ItemStack> $$1 = NonNullList.withSize(c0.getContainerSize(), ItemStack.EMPTY);
        for (int $$2 = 0; $$2 < $$1.size(); $$2++) {
            Item $$3 = c0.getItem($$2).getItem();
            if ($$3.hasCraftingRemainingItem()) {
                $$1.set($$2, new ItemStack($$3.getCraftingRemainingItem()));
            }
        }
        return $$1;
    }

    default NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }

    default boolean isSpecial() {
        return false;
    }

    default boolean showNotification() {
        return true;
    }

    default String getGroup() {
        return "";
    }

    default ItemStack getToastSymbol() {
        return new ItemStack(Blocks.CRAFTING_TABLE);
    }

    ResourceLocation getId();

    RecipeSerializer<?> getSerializer();

    RecipeType<?> getType();

    default boolean isIncomplete() {
        NonNullList<Ingredient> $$0 = this.getIngredients();
        return $$0.isEmpty() || $$0.stream().anyMatch(p_151268_ -> p_151268_.getItems().length == 0);
    }
}