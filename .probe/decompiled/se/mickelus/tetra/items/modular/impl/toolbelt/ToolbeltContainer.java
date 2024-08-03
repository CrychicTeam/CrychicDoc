package se.mickelus.tetra.items.modular.impl.toolbelt;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.gui.DisabledSlot;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.PotionSlot;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.PotionsInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.PredicateSlot;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.QuickslotInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.QuiverInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.StorageInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.ToolbeltInventory;

@ParametersAreNonnullByDefault
public class ToolbeltContainer extends AbstractContainerMenu {

    public static RegistryObject<MenuType<ToolbeltContainer>> type;

    private final ItemStack itemStackToolbelt;

    private final QuickslotInventory quickslotInventory;

    private final StorageInventory storageInventory;

    private final PotionsInventory potionsInventory;

    private final QuiverInventory quiverInventory;

    public ToolbeltContainer(int windowId, Container playerInventory, ItemStack itemStackToolbelt, Player player) {
        super(type.get(), windowId);
        this.quickslotInventory = new QuickslotInventory(itemStackToolbelt);
        this.storageInventory = new StorageInventory(itemStackToolbelt);
        this.potionsInventory = new PotionsInventory(itemStackToolbelt);
        this.quiverInventory = new QuiverInventory(itemStackToolbelt);
        this.itemStackToolbelt = itemStackToolbelt;
        int numPotionSlots = this.potionsInventory.m_6643_();
        int numQuickslots = this.quickslotInventory.m_6643_();
        int numStorageSlots = this.storageInventory.m_6643_();
        int numQuiverSlots = this.quiverInventory.m_6643_();
        int offset = 0;
        if (numStorageSlots > 0) {
            int cols = Math.min(numStorageSlots, StorageInventory.getColumns(numStorageSlots));
            int rows = 1 + (numStorageSlots - 1) / cols;
            for (int i = 0; i < numStorageSlots; i++) {
                this.m_38897_(new PredicateSlot(this.storageInventory, i, (int) (-8.5 * (double) cols + (double) (17 * (i % cols)) + 90.0), 108 - offset - i / cols * 17, this.storageInventory::isItemValid));
            }
            offset += rows * 17 + 13;
        }
        for (int i = 0; i < numQuiverSlots; i++) {
            this.m_38897_(new PredicateSlot(this.quiverInventory, i, (int) (-8.5 * (double) numQuiverSlots + (double) (17 * i) + 90.0), 108 - offset, this.quiverInventory::isItemValid));
        }
        if (numQuiverSlots > 0) {
            offset += 30;
        }
        for (int i = 0; i < numPotionSlots; i++) {
            this.m_38897_(new PotionSlot(this.potionsInventory, i, (int) (-8.5 * (double) numPotionSlots + (double) (17 * i) + 90.0), 108 - offset));
        }
        if (numPotionSlots > 0) {
            offset += 30;
        }
        for (int i = 0; i < numQuickslots; i++) {
            this.m_38897_(new PredicateSlot(this.quickslotInventory, i, (int) (-8.5 * (double) numQuickslots + (double) (17 * i) + 90.0), 108 - offset, this.quickslotInventory::isItemValid));
        }
        if (numQuickslots > 0) {
            offset += 30;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                Slot slot;
                if (itemStackToolbelt.is(playerInventory.getItem(i * 9 + j + 9).getItem())) {
                    slot = new DisabledSlot(playerInventory, i * 9 + j + 9, j * 17 + 12, i * 17 + 142);
                } else {
                    slot = new Slot(playerInventory, i * 9 + j + 9, j * 17 + 12, i * 17 + 142);
                }
                this.m_38897_(slot);
            }
        }
        for (int i = 0; i < 9; i++) {
            Slot slot;
            if (itemStackToolbelt.is(playerInventory.getItem(i).getItem())) {
                slot = new DisabledSlot(playerInventory, i, i * 17 + 12, 197);
            } else {
                slot = new Slot(playerInventory, i, i * 17 + 12, 197);
            }
            this.m_38897_(slot);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static ToolbeltContainer create(int windowId, Inventory inv) {
        ItemStack itemStack = inv.player.m_21205_();
        if (!ModularToolbeltItem.instance.get().equals(itemStack.getItem())) {
            itemStack = inv.player.m_21206_();
        }
        if (!ModularToolbeltItem.instance.get().equals(itemStack.getItem())) {
            itemStack = ToolbeltHelper.findToolbelt(inv.player);
        }
        return new ToolbeltContainer(windowId, inv, itemStack, inv.player);
    }

    private boolean mergeItemStackExtended(ItemStack incomingStack, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            Slot slot = (Slot) this.f_38839_.get(i);
            if (slot.mayPlace(incomingStack)) {
                ItemStack slotStack = slot.getItem();
                if (ItemStack.isSameItemSameTags(slotStack, incomingStack)) {
                    if (slotStack.getCount() + incomingStack.getCount() < slot.getMaxStackSize(slotStack)) {
                        slotStack.grow(incomingStack.getCount());
                        incomingStack.setCount(0);
                        slot.setChanged();
                        return true;
                    }
                    int mergeCount = slot.getMaxStackSize(slotStack) - slotStack.getCount();
                    slotStack.grow(mergeCount);
                    incomingStack.shrink(mergeCount);
                    slot.setChanged();
                }
            }
        }
        for (int ix = startIndex; ix < endIndex; ix++) {
            Slot slot = (Slot) this.f_38839_.get(ix);
            if (slot.mayPlace(incomingStack)) {
                ItemStack slotStack = slot.getItem();
                if (slotStack.isEmpty()) {
                    slot.set(incomingStack.split(slot.getMaxStackSize(slotStack)));
                    if (incomingStack.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack = slot.getItem();
            if (itemStack.is(this.itemStackToolbelt.getItem())) {
                return ItemStack.EMPTY;
            } else {
                int numQuiverSlots = this.quiverInventory.m_6643_();
                int numPotionSlots = this.potionsInventory.m_6643_();
                int numQuickslots = this.quickslotInventory.m_6643_();
                int numStorageSlots = this.storageInventory.m_6643_();
                int playerInventoryStart = numQuickslots + numStorageSlots + numPotionSlots + numQuiverSlots;
                if (slot.container instanceof ToolbeltInventory) {
                    if (slot.container == this.potionsInventory && slot.getSlotIndex() == index) {
                        int count = slot.getItem().getCount();
                        itemStack = slot.remove(64);
                        if (!this.m_38903_(itemStack, playerInventoryStart, this.f_38839_.size(), true)) {
                            itemStack.setCount(count);
                            slot.set(itemStack);
                            slot.setChanged();
                            return ItemStack.EMPTY;
                        } else {
                            slot.setChanged();
                            return itemStack;
                        }
                    } else {
                        ItemStack copy = itemStack.copy();
                        ItemStack breakoff = copy.split(itemStack.getMaxStackSize());
                        if (this.m_38903_(breakoff, playerInventoryStart, this.f_38839_.size(), true)) {
                            if (!copy.isEmpty()) {
                                if (!breakoff.isEmpty()) {
                                    copy.grow(breakoff.getCount());
                                }
                                slot.set(copy);
                            } else {
                                slot.set(breakoff);
                            }
                            slot.setChanged();
                            return breakoff;
                        } else {
                            slot.setChanged();
                            return ItemStack.EMPTY;
                        }
                    }
                } else if (numQuiverSlots > 0 && this.m_38903_(itemStack, numStorageSlots, numStorageSlots + numQuiverSlots, false)) {
                    return itemStack;
                } else if (numPotionSlots > 0 && this.mergeItemStackExtended(itemStack, numStorageSlots + numQuiverSlots, numStorageSlots + numQuiverSlots + numPotionSlots)) {
                    return itemStack;
                } else if (numQuickslots > 0 && this.m_38903_(itemStack, numStorageSlots + numQuiverSlots + numPotionSlots, playerInventoryStart, false)) {
                    return itemStack;
                } else {
                    return numStorageSlots > 0 && this.m_38903_(itemStack, 0, numStorageSlots, false) ? itemStack : ItemStack.EMPTY;
                }
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.quickslotInventory.m_5785_(playerIn);
    }

    public QuickslotInventory getQuickslotInventory() {
        return this.quickslotInventory;
    }

    public StorageInventory getStorageInventory() {
        return this.storageInventory;
    }

    public PotionsInventory getPotionInventory() {
        return this.potionsInventory;
    }

    public QuiverInventory getQuiverInventory() {
        return this.quiverInventory;
    }
}