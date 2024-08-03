package dev.xkmc.l2backpack.content.insert;

import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2backpack.init.data.BackpackConfig;
import dev.xkmc.l2backpack.network.DrawerInteractToServer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface InsertOnlyItem extends CapInsertItem {

    @Override
    default boolean clientInsert(ItemStack storage, ItemStack carried, int cid, Slot slot, boolean perform, int button, DrawerInteractToServer.Callback suppress, int limit) {
        return !BackpackConfig.CLIENT.allowBackpackInsert(button) ? false : CapInsertItem.super.clientInsert(storage, carried, cid, slot, perform, button, suppress, limit);
    }

    @Override
    default void sendInsertPacket(int cid, ItemStack carried, Slot slot, DrawerInteractToServer.Callback suppress, int limit) {
        int index = cid == 0 ? slot.getSlotIndex() : slot.index;
        DrawerInteractToServer.Type method = Screen.hasAltDown() ? DrawerInteractToServer.Type.PICKUP : DrawerInteractToServer.Type.INSERT;
        L2Backpack.HANDLER.toServer(new DrawerInteractToServer(method, cid, index, carried, suppress, limit));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    default boolean mayClientTake() {
        return false;
    }

    @Override
    default ItemStack takeItem(ItemStack storage, ServerPlayer player) {
        return ItemStack.EMPTY;
    }
}