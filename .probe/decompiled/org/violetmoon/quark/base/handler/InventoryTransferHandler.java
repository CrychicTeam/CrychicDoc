package org.violetmoon.quark.base.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.api.IQuarkButtonAllowed;
import org.violetmoon.quark.api.ITransferManager;
import org.violetmoon.quark.api.QuarkCapabilities;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.config.QuarkGeneralConfig;
import org.violetmoon.quark.content.management.module.EasyTransferingModule;

public class InventoryTransferHandler {

    public static void transfer(Player player, boolean isRestock, boolean smart) {
        if (Quark.ZETA.modules.isEnabled(EasyTransferingModule.class) && !player.isSpectator() && accepts(player.containerMenu, player)) {
            InventoryTransferHandler.Transfer transfer = (InventoryTransferHandler.Transfer) (isRestock ? new InventoryTransferHandler.Restock(player, smart) : new InventoryTransferHandler.Transfer(player, smart));
            transfer.execute();
        }
    }

    private static boolean hasProvider(Object te) {
        if (te instanceof BlockEntity be && Quark.ZETA.capabilityManager.hasCapability(QuarkCapabilities.TRANSFER, be)) {
            return true;
        }
        return false;
    }

    private static ITransferManager getProvider(Object te) {
        return Quark.ZETA.capabilityManager.getCapability(QuarkCapabilities.TRANSFER, (BlockEntity) te);
    }

    public static boolean accepts(AbstractContainerMenu container, Player player) {
        if (!(container instanceof CraftingMenu) && (!player.m_9236_().isClientSide() || !(container instanceof CreativeModeInventoryScreen.ItemPickerMenu))) {
            return hasProvider(container) ? getProvider(container).acceptsTransfer(player) : container instanceof IQuarkButtonAllowed || container.slots.size() - player.getInventory().items.size() >= QuarkGeneralConfig.chestButtonSlotTarget;
        } else {
            return false;
        }
    }

    public static class ContainerWrapper extends InvWrapper {

        private final AbstractContainerMenu container;

        public static IItemHandler provideWrapper(Slot slot, AbstractContainerMenu container) {
            if (slot instanceof SlotItemHandler slotItemHandler) {
                IItemHandler handler = slotItemHandler.getItemHandler();
                return InventoryTransferHandler.hasProvider(handler) ? InventoryTransferHandler.getProvider(handler).getTransferItemHandler(() -> handler) : handler;
            } else {
                return provideWrapper(slot.container, container);
            }
        }

        public static IItemHandler provideWrapper(Container inv, AbstractContainerMenu container) {
            return (IItemHandler) (InventoryTransferHandler.hasProvider(inv) ? InventoryTransferHandler.getProvider(inv).getTransferItemHandler(() -> new InventoryTransferHandler.ContainerWrapper(inv, container)) : new InventoryTransferHandler.ContainerWrapper(inv, container));
        }

        private ContainerWrapper(Container inv, AbstractContainerMenu container) {
            super(inv);
            this.container = container;
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            Slot containerSlot = this.getSlot(slot);
            return containerSlot != null && containerSlot.mayPlace(stack) ? super.insertItem(slot, stack, simulate) : stack;
        }

        private Slot getSlot(int slotId) {
            Container inv = this.getInv();
            for (Slot slot : this.container.slots) {
                if (slot.container == inv && slot.getSlotIndex() == slotId) {
                    return slot;
                }
            }
            return null;
        }
    }

    public static class PlayerInvWrapper extends InvWrapper {

        public PlayerInvWrapper(Container inv) {
            super(inv);
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) {
                stack = stack.copy();
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public int getSlots() {
            return super.getSlots() - 5;
        }
    }

    public static class Restock extends InventoryTransferHandler.Transfer {

        public Restock(Player player, boolean filtered) {
            super(player, filtered);
        }

        @Override
        public void transfer(InventoryTransferHandler.TransferPredicate predicate) {
            IItemHandler inv = (IItemHandler) ((Pair) this.itemHandlers.get(0)).getLeft();
            IItemHandler playerInv = new InventoryTransferHandler.PlayerInvWrapper(this.player.getInventory());
            for (int i = inv.getSlots() - 1; i >= 0; i--) {
                ItemStack stackAt = inv.extractItem(i, inv.getStackInSlot(i).getCount(), true);
                if (!stackAt.isEmpty()) {
                    ItemStack copy = stackAt.copy();
                    ItemStack ret = this.insertInHandler(playerInv, copy, predicate);
                    if (!ItemStack.matches(stackAt, ret)) {
                        inv.extractItem(i, stackAt.getCount() - ret.getCount(), false);
                    }
                }
            }
        }
    }

    public static class Transfer {

        public final Player player;

        public final boolean smart;

        public final List<Pair<IItemHandler, Double>> itemHandlers = new ArrayList();

        public Transfer(Player player, boolean smart) {
            this.player = player;
            this.smart = smart;
        }

        public void execute() {
            this.locateItemHandlers();
            if (!this.itemHandlers.isEmpty()) {
                if (this.smart) {
                    this.smartTransfer();
                } else {
                    this.roughTransfer();
                }
                this.player.inventoryMenu.m_38946_();
                this.player.containerMenu.broadcastChanges();
            }
        }

        public void smartTransfer() {
            this.transfer((stack, handler) -> {
                int slots = handler.getSlots();
                for (int i = 0; i < slots; i++) {
                    ItemStack stackAt = handler.getStackInSlot(i);
                    if (!stackAt.isEmpty()) {
                        boolean itemEqual = stack.getItem() == stackAt.getItem();
                        boolean damageEqual = stack.getDamageValue() == stackAt.getDamageValue();
                        boolean nbtEqual = ItemStack.isSameItemSameTags(stackAt, stack);
                        if (itemEqual && damageEqual && nbtEqual) {
                            return true;
                        }
                        if (stack.isDamageableItem() && stack.getMaxStackSize() == 1 && itemEqual && nbtEqual) {
                            return true;
                        }
                    }
                }
                return false;
            });
        }

        public void roughTransfer() {
            this.transfer((stack, handler) -> true);
        }

        public void locateItemHandlers() {
            AbstractContainerMenu c = this.player.containerMenu;
            for (Slot s : c.slots) {
                Container inv = s.container;
                if (inv != this.player.getInventory()) {
                    this.itemHandlers.add(Pair.of(InventoryTransferHandler.ContainerWrapper.provideWrapper(s, c), 0.0));
                    break;
                }
            }
        }

        public void transfer(InventoryTransferHandler.TransferPredicate predicate) {
            Inventory inv = this.player.getInventory();
            for (int i = Inventory.getSelectionSize(); i < inv.items.size(); i++) {
                ItemStack stackAt = inv.getItem(i);
                if (!stackAt.isEmpty()) {
                    ItemStack ret = this.insert(stackAt, predicate);
                    if (!ItemStack.matches(stackAt, ret)) {
                        inv.setItem(i, ret);
                    }
                }
            }
        }

        public ItemStack insert(ItemStack stack, InventoryTransferHandler.TransferPredicate predicate) {
            ItemStack ret = stack.copy();
            for (Pair<IItemHandler, Double> pair : this.itemHandlers) {
                IItemHandler handler = (IItemHandler) pair.getLeft();
                ret = this.insertInHandler(handler, ret, predicate);
                if (ret.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
            return ret;
        }

        public ItemStack insertInHandler(IItemHandler handler, ItemStack stack, InventoryTransferHandler.TransferPredicate predicate) {
            if (predicate.test(stack, handler)) {
                ItemStack retStack = ItemHandlerHelper.insertItemStacked(handler, stack, false);
                return !retStack.isEmpty() ? retStack.copy() : retStack;
            } else {
                return stack;
            }
        }
    }

    public interface TransferPredicate extends BiPredicate<ItemStack, IItemHandler> {
    }
}