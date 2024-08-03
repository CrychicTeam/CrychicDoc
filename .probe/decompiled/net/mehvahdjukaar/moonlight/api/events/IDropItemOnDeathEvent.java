package net.mehvahdjukaar.moonlight.api.events;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.moonlight.api.events.forge.IDropItemOnDeathEventImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IDropItemOnDeathEvent extends SimpleEvent {

    @ExpectPlatform
    @Transformed
    static IDropItemOnDeathEvent create(ItemStack itemStack, Player player, boolean beforeDrop) {
        return IDropItemOnDeathEventImpl.create(itemStack, player, beforeDrop);
    }

    boolean isBeforeDrop();

    Player getPlayer();

    ItemStack getItemStack();

    void setCanceled(boolean var1);

    boolean isCanceled();

    void setReturnItemStack(ItemStack var1);

    ItemStack getReturnItemStack();
}