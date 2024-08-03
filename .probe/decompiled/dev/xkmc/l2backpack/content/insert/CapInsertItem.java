package dev.xkmc.l2backpack.content.insert;

import dev.xkmc.l2backpack.network.DrawerInteractToServer;
import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public interface CapInsertItem extends OverlayInsertItem {

    default boolean isValidContent(ItemStack carried) {
        return true;
    }

    @Override
    default boolean clientInsert(ItemStack storage, ItemStack carried, int cid, Slot slot, boolean perform, int button, DrawerInteractToServer.Callback suppress, int limit) {
        if (carried.isEmpty()) {
            return false;
        } else if (!this.isValidContent(carried)) {
            return false;
        } else {
            if (perform) {
                this.sendInsertPacket(cid, carried, slot, suppress, limit);
            }
            return true;
        }
    }

    @Nullable
    default IItemHandler getInvCap(ItemStack storage, ServerPlayer player) {
        Optional<IItemHandler> opt = storage.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        return opt.isEmpty() ? null : (IItemHandler) opt.get();
    }

    @Override
    default void attemptInsert(ItemStack storage, ItemStack carried, ServerPlayer player) {
        if (this.isValidContent(carried)) {
            IItemHandler handler = this.getInvCap(storage, player);
            if (handler != null) {
                ItemStack remain = ItemHandlerHelper.insertItem(handler, carried.copy(), false);
                carried.setCount(remain.getCount());
            }
        }
    }
}