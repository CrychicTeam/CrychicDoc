package io.github.lightman314.lightmanscurrency.common.villager_merchant.listings.mods;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class VillagerTradeMod {

    @Nonnull
    public abstract ItemStack modifyCost(@Nullable Entity var1, @Nonnull ItemStack var2);

    @Nonnull
    public abstract ItemStack modifyResult(@Nullable Entity var1, @Nonnull ItemStack var2);

    protected final ItemStack copyWithNewItem(@Nonnull ItemStack stack, @Nullable Item replacement) {
        if (replacement == null) {
            return stack;
        } else {
            ItemStack copy = new ItemStack(replacement);
            copy.setCount(stack.getCount());
            if (stack.hasTag()) {
                copy.setTag(stack.getTag().copy());
            }
            return copy;
        }
    }
}