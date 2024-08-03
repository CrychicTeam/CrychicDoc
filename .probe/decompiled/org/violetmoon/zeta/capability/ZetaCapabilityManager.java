package org.violetmoon.zeta.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public interface ZetaCapabilityManager {

    ZetaCapabilityManager register(ZetaCapability<?> var1, Object var2);

    <T> boolean hasCapability(ZetaCapability<T> var1, ItemStack var2);

    @Nullable
    <T> T getCapability(ZetaCapability<T> var1, ItemStack var2);

    <T> boolean hasCapability(ZetaCapability<T> var1, BlockEntity var2);

    @Nullable
    <T> T getCapability(ZetaCapability<T> var1, BlockEntity var2);

    <T> boolean hasCapability(ZetaCapability<T> var1, Level var2);

    @Nullable
    <T> T getCapability(ZetaCapability<T> var1, Level var2);

    <T> void attachCapability(Object var1, ResourceLocation var2, ZetaCapability<T> var3, T var4);
}