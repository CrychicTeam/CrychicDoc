package dev.xkmc.l2backpack.content.common;

import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface TooltipInvItem {

    default int getRowSize() {
        return 9;
    }

    int getInvSize(ItemStack var1);

    List<ItemStack> getInvItems(ItemStack var1, Player var2);
}