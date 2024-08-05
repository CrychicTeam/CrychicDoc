package dev.xkmc.l2backpack.content.restore;

import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2screentracker.screen.base.LayerPopType;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import dev.xkmc.l2screentracker.screen.track.ItemBasedTrace;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BackpackTrace extends ItemBasedTrace {

    public LayerPopType restore(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack, @Nullable Component component) {
        if (stack.getItem() instanceof BaseBagItem bag) {
            bag.open(player, slot, stack);
            return LayerPopType.REMAIN;
        } else {
            return LayerPopType.FAIL;
        }
    }
}