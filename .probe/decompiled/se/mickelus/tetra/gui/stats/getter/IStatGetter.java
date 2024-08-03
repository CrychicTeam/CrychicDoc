package se.mickelus.tetra.gui.stats.getter;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IStatGetter {

    default boolean shouldShow(Player player, ItemStack currentStack, ItemStack previewStack) {
        double baseValue = this.getValue(player, ItemStack.EMPTY);
        return this.getValue(player, currentStack) > baseValue || this.getValue(player, previewStack) > baseValue;
    }

    double getValue(Player var1, ItemStack var2);

    double getValue(Player var1, ItemStack var2, String var3);

    double getValue(Player var1, ItemStack var2, String var3, String var4);
}