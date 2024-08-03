package io.github.lightman314.lightmanscurrency.common.menus.traderinterface.item;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.trader_interface.menu.TraderInterfaceClientTab;
import io.github.lightman314.lightmanscurrency.api.trader_interface.menu.TraderInterfaceTab;
import io.github.lightman314.lightmanscurrency.api.upgrades.slot.UpgradeInputSlot;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderInterfaceScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderinterface.item.ItemStorageClientTab;
import io.github.lightman314.lightmanscurrency.common.blockentity.ItemTraderInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.common.menus.TraderInterfaceMenu;
import io.github.lightman314.lightmanscurrency.common.menus.slots.SimpleSlot;
import io.github.lightman314.lightmanscurrency.common.traders.item.TraderItemStorage;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemStorageTab extends TraderInterfaceTab {

    List<SimpleSlot> slots = new ArrayList();

    public ItemStorageTab(TraderInterfaceMenu menu) {
        super(menu);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public TraderInterfaceClientTab<?> createClientTab(TraderInterfaceScreen screen) {
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
    public void onTabOpen() {
        SimpleSlot.SetActive(this.slots);
    }

    @Override
    public void onTabClose() {
        SimpleSlot.SetInactive(this.slots);
    }

    @Override
    public void addStorageMenuSlots(Function<Slot, Slot> addSlot) {
        for (int i = 0; i < this.menu.getBE().getUpgradeInventory().getContainerSize(); i++) {
            SimpleSlot upgradeSlot = new UpgradeInputSlot(this.menu.getBE().getUpgradeInventory(), i, 176, 18 + 18 * i, this.menu.getBE(), this::onUpgradeModified);
            upgradeSlot.active = false;
            addSlot.apply(upgradeSlot);
            this.slots.add(upgradeSlot);
        }
    }

    private void onUpgradeModified() {
        this.menu.getBE().setUpgradeSlotsDirty();
    }

    @Override
    public boolean quickMoveStack(ItemStack stack) {
        if (this.menu.getBE() instanceof ItemTraderInterfaceBlockEntity be) {
            TraderItemStorage storage = be.getItemBuffer();
            if (storage.getFittableAmount(stack) > 0) {
                storage.tryAddItem(stack);
                be.setItemBufferDirty();
                return true;
            }
        }
        return super.quickMoveStack(stack);
    }

    public void clickedOnSlot(int storageSlot, boolean isShiftHeld, boolean leftClick) {
        if (this.menu.getBE().canAccess(this.menu.player) && this.menu.getBE() instanceof ItemTraderInterfaceBlockEntity be) {
            TraderItemStorage storage = be.getItemBuffer();
            ItemStack heldItem = this.menu.m_142621_();
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
                        this.menu.player.getInventory().add(stackToRemove);
                        removedAmount = tempAmount - stackToRemove.getCount();
                    } else {
                        this.menu.m_142503_(stackToRemove);
                        removedAmount = tempAmount;
                    }
                    if (removedAmount > 0) {
                        removeStack.setCount(removedAmount);
                        storage.removeItem(removeStack);
                        be.setItemBufferDirty();
                    }
                }
            } else if (leftClick) {
                storage.tryAddItem(heldItem);
                be.setItemBufferDirty();
            } else {
                ItemStack addItem = heldItem.copy();
                addItem.setCount(1);
                if (storage.addItem(addItem)) {
                    heldItem.shrink(1);
                    if (heldItem.isEmpty()) {
                        this.menu.m_142503_(ItemStack.EMPTY);
                    }
                }
                be.setItemBufferDirty();
            }
            if (this.menu.isClient()) {
                this.menu.SendMessage(LazyPacketData.builder().setInt("ClickedSlot", storageSlot).setBoolean("HeldShift", isShiftHeld).setBoolean("LeftClick", leftClick));
            }
        }
    }

    public void quickTransfer(int type) {
        if (this.menu.getBE().canAccess(this.menu.player) && this.menu.getBE() instanceof ItemTraderInterfaceBlockEntity be) {
            TraderItemStorage storage = be.getItemBuffer();
            Inventory inv = this.menu.player.getInventory();
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
                be.setItemBufferDirty();
            }
            if (this.menu.isClient()) {
                this.menu.SendMessage(LazyPacketData.simpleInt("QuickTransfer", type));
            }
        }
    }

    public void toggleInputSlot(Direction side) {
        if (this.menu.getBE().canAccess(this.menu.player) && this.menu.getBE() instanceof ItemTraderInterfaceBlockEntity be) {
            be.getItemHandler().toggleInputSide(side);
            be.setHandlerDirty(be.getItemHandler());
        }
    }

    public void toggleOutputSlot(Direction side) {
        if (this.menu.getBE().canAccess(this.menu.player) && this.menu.getBE() instanceof ItemTraderInterfaceBlockEntity be) {
            be.getItemHandler().toggleOutputSide(side);
            be.setHandlerDirty(be.getItemHandler());
        }
    }

    @Override
    public void handleMessage(@Nonnull LazyPacketData message) {
        if (message.contains("ClickedSlot")) {
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