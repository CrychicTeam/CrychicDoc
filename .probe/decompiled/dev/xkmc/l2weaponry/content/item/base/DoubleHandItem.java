package dev.xkmc.l2weaponry.content.item.base;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface DoubleHandItem {

    default boolean disableOffHand(Player player, ItemStack stack) {
        return true;
    }
}