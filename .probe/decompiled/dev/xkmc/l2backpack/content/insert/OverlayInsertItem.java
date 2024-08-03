package dev.xkmc.l2backpack.content.insert;

import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2backpack.network.DrawerInteractToServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public interface OverlayInsertItem {

    boolean clientInsert(ItemStack var1, ItemStack var2, int var3, Slot var4, boolean var5, int var6, DrawerInteractToServer.Callback var7, int var8);

    boolean mayClientTake();

    default void serverTrigger(ItemStack storage, ServerPlayer player) {
    }

    ItemStack takeItem(ItemStack var1, ServerPlayer var2);

    void attemptInsert(ItemStack var1, ItemStack var2, ServerPlayer var3);

    default void sendInsertPacket(int cid, ItemStack carried, Slot slot, DrawerInteractToServer.Callback suppress, int limit) {
        int index = cid == 0 ? slot.getSlotIndex() : slot.index;
        L2Backpack.HANDLER.toServer(new DrawerInteractToServer(DrawerInteractToServer.Type.INSERT, cid, index, carried, suppress, limit));
    }
}