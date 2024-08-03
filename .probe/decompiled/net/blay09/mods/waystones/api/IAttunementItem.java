package net.blay09.mods.waystones.api;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IAttunementItem {

    @Nullable
    IWaystone getWaystoneAttunedTo(@Nullable MinecraftServer var1, ItemStack var2);

    void setWaystoneAttunedTo(ItemStack var1, @Nullable IWaystone var2);
}