package se.mickelus.tetra.gui.stats.getter;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.Tooltips;

public interface ITooltipGetter {

    default String getTooltip(Player player, ItemStack itemStack) {
        return this.hasExtendedTooltip(player, itemStack) ? this.getTooltipBase(player, itemStack) + "\n \n" + Tooltips.expand.getString() : this.getTooltipBase(player, itemStack);
    }

    default String getTooltipExtended(Player player, ItemStack itemStack) {
        return this.hasExtendedTooltip(player, itemStack) ? this.getTooltipBase(player, itemStack) + "\n \n" + Tooltips.expanded.getString() + "\n" + ChatFormatting.GRAY + this.getTooltipExtension(player, itemStack) : this.getTooltip(player, itemStack);
    }

    String getTooltipBase(Player var1, ItemStack var2);

    default boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
        return false;
    }

    default String getTooltipExtension(Player player, ItemStack itemStack) {
        return null;
    }
}