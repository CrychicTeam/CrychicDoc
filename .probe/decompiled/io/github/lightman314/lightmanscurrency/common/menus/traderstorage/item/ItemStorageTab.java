package io.github.lightman314.lightmanscurrency.common.menus.traderstorage.item;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageMenu;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageTab;
import io.github.lightman314.lightmanscurrency.api.upgrades.slot.UpgradeInputSlot;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.item.ItemStorageClientTab;
import io.github.lightman314.lightmanscurrency.common.menus.slots.SimpleSlot;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.item.TraderItemStorage;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemStorageTab extends TraderStorageTab {

    List<SimpleSlot> slots = new ArrayList();

    public ItemStorageTab(@Nonnull ITraderStorageMenu menu) {
        super(menu);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Object createClientTab(Object screen) {
        return new ItemStorageClientTab(screen, this);
    }

    @Override
    public boolean canOpen(Player player) {
        return true;
    }

    public List<? extends Slot> getSlots() {
        return this.slots;
    }

    @Override
    public void addStorageMenuSlots(Function<Slot, Slot> addSlot) {
        if (this.menu.getTrader() instanceof ItemTraderData trader && !trader.isPersistent()) {
            Container upgrades = trader.getUpgrades();
            for (int i = 0; i < upgrades.getContainerSize(); i++) {
                SimpleSlot upgradeSlot = new UpgradeInputSlot(upgrades, i, 176, 18 + 18 * i, trader);
                upgradeSlot.active = false;
                addSlot.apply(upgradeSlot);
                this.slots.add(upgradeSlot);
            }
        }
    }

    @Override
    public void onTabOpen() {
        SimpleSlot.SetActive(this.slots);
    }

    @Override
    public void onTabClose() {
        SimpleSlot.SetInactive(this.slots);
    }

    @Override
    public boolean quickMoveStack(ItemStack stack) {
        if (this.menu.getTrader() instanceof ItemTraderData trader) {
            if (trader.isPersistent()) {
                return false;
            }
            TraderItemStorage storage = trader.getStorage();
            if (storage.getFittableAmount(stack) > 0) {
                storage.tryAddItem(stack);
                trader.markStorageDirty();
                return true;
            }
        }
        return super.quickMoveStack(stack);
    }

    public void clickedOnSlot(int storageSlot, boolean isShiftHeld, boolean leftClick) {
        if (this.menu.getTrader() instanceof ItemTraderData trader) {
            if (trader.isPersistent()) {
                return;
            }
            TraderItemStorage storage = trader.getStorage();
            ItemStack heldItem = this.menu.getHeldItem();
            if (heldItem.isEmpty()) {
                List<ItemStack> storageContents = storage.getContents();
                if (storageSlot >= 0 && storageSlot < storageContents.size()) {
                    ItemStack stackToRemove = ((ItemStack) storageContents.get(storageSlot)).copy();
                    ItemStack removeStack = stackToRemove.copy();
                    int tempAmount = Math.min(stackToRemove.getMaxStackSize(), stackToRemove.getCount());
                    stackToRemove.setCount(tempAmount);
                    if (!leftClick) {
                        if (tempAmount > 1) {
                            tempAmount /= 2;
                        }
                        stackToRemove.setCount(tempAmount);
                    }
                    int removedAmount;
                    if (isShiftHeld) {
                        this.menu.getPlayer().getInventory().add(stackToRemove);
                        removedAmount = tempAmount - stackToRemove.getCount();
                    } else {
                        this.menu.setHeldItem(stackToRemove);
                        removedAmount = tempAmount;
                    }
                    if (removedAmount > 0) {
                        removeStack.setCount(removedAmount);
                        storage.removeItem(removeStack);
                        trader.markStorageDirty();
                    }
                }
            } else if (leftClick) {
                storage.tryAddItem(heldItem);
                trader.markStorageDirty();
            } else {
                ItemStack addItem = heldItem.copy();
                addItem.setCount(1);
                if (storage.addItem(addItem)) {
                    heldItem.shrink(1);
                    if (heldItem.isEmpty()) {
                        this.menu.setHeldItem(ItemStack.EMPTY);
                    }
                }
                trader.markStorageDirty();
            }
            if (this.menu.isClient()) {
                this.sendStorageClickMessage(storageSlot, isShiftHeld, leftClick);
            }
        }
    }

    private void sendStorageClickMessage(int storageSlot, boolean isShiftHeld, boolean leftClick) {
        this.menu.SendMessage(LazyPacketData.builder().setInt("ClickedSlot", storageSlot).setBoolean("HeldShift", isShiftHeld).setBoolean("LeftClick", leftClick));
    }

    public void quickTransfer(int type) {
        if (this.menu.getTrader() instanceof ItemTraderData trader) {
            if (trader.isPersistent()) {
                return;
            }
            TraderItemStorage storage = trader.getStorage();
            Inventory inv = this.menu.getPlayer().getInventory();
            boolean changed = false;
            if (type == 0) {
                for (int i = 0; i < 36; i++) {
                    ItemStack stack = inv.getItem(i);
                    int fillAmount = storage.getFittableAmount(stack);
                    if (fillAmount > 0) {
                        ItemStack fillStack = inv.removeItem(i, fillAmount);
                        storage.forceAddItem(fillStack);
                    }
                }
            } else if (type == 1) {
                for (ItemStack stack : InventoryUtil.copyList(storage.getContents())) {
                    boolean keepTrying = true;
                    while (storage.getItemCount(stack) > 0 && keepTrying) {
                        ItemStack transferStack = stack.copy();
                        int transferCount = Math.min(storage.getItemCount(stack), stack.getMaxStackSize());
                        transferStack.setCount(transferCount);
                        int removedCount = InventoryUtil.safeGiveToPlayer(inv, transferStack);
                        if (removedCount > 0) {
                            changed = true;
                            ItemStack removeStack = stack.copy();
                            removeStack.setCount(removedCount);
                            storage.removeItem(removeStack);
                        } else {
                            keepTrying = false;
                        }
                    }
                }
            }
            if (changed) {
                trader.markStorageDirty();
            }
            if (this.menu.isClient()) {
                this.menu.SendMessage(LazyPacketData.simpleInt("QuickTransfer", type));
            }
        }
    }

    @Override
    public void receiveMessage(LazyPacketData message) {
        if (message.contains("ClickedSlot", (byte) 2)) {
            int storageSlot = message.getInt("ClickedSlot");
            boolean isShiftHeld = message.getBoolean("HeldShift");
            boolean leftClick = message.getBoolean("LeftClick");
            this.clickedOnSlot(storageSlot, isShiftHeld, leftClick);
        }
        if (message.contains("QuickTransfer")) {
            this.quickTransfer(message.getInt("QuickTransfer"));
        }
    }
}