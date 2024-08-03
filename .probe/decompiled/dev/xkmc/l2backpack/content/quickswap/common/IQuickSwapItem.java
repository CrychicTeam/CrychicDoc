package dev.xkmc.l2backpack.content.quickswap.common;

import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IQuickSwapItem {

    @Nullable
    IQuickSwapToken<?> getTokenOfType(ItemStack var1, LivingEntity var2, QuickSwapType var3);
}