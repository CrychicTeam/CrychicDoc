package net.mehvahdjukaar.supplementaries.common.inventories;

import net.mehvahdjukaar.moonlight.api.misc.IContainerProvider;
import net.mehvahdjukaar.supplementaries.common.block.tiles.CannonBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CannonContainerMenu extends AbstractContainerMenu implements IContainerProvider {

    protected final CannonBlockTile inventory;

    public CannonBlockTile getContainer() {
        return this.inventory;
    }

    public CannonContainerMenu(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this((MenuType) ModMenuTypes.CANNON.get(), id, playerInventory, (CannonBlockTile) playerInventory.player.m_9236_().getBlockEntity(packetBuffer.readBlockPos()));
    }

    public <T extends CannonContainerMenu> CannonContainerMenu(int id, Inventory playerInventory, CannonBlockTile inventory) {
        this((MenuType<T>) ModMenuTypes.CANNON.get(), id, playerInventory, inventory);
    }

    public <T extends CannonContainerMenu> CannonContainerMenu(MenuType<T> type, int id, Inventory playerInventory, CannonBlockTile inventory) {
        super(type, id);
        this.inventory = inventory;
        m_38869_(this.inventory, 2);
        this.inventory.m_5856_(playerInventory.player);
        this.m_38897_(new DelegatingSlot(this.inventory, 0, 20, 20, this));
        this.m_38897_(new DelegatingSlot(this.inventory, 1, 60, 40, this));
        for (int si = 0; si < 3; si++) {
            for (int sj = 0; sj < 9; sj++) {
                this.m_38897_(new Slot(playerInventory, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
            }
        }
        for (int si = 0; si < 9; si++) {
            this.m_38897_(new Slot(playerInventory, si, 8 + si * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.inventory.m_6542_(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemCopy = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot.hasItem()) {
            ItemStack item = slot.getItem();
            itemCopy = item.copy();
            if (index < this.inventory.m_6643_()) {
                if (!this.m_38903_(item, this.inventory.m_6643_(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.m_38903_(item, 0, this.inventory.m_6643_(), false)) {
                return ItemStack.EMPTY;
            }
            if (item.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (item.getCount() == itemCopy.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, item);
        }
        return itemCopy;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.inventory.m_5785_(playerIn);
    }
}