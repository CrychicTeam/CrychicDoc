package net.mehvahdjukaar.supplementaries.common.inventories;

import net.mehvahdjukaar.moonlight.api.misc.IContainerProvider;
import net.mehvahdjukaar.supplementaries.common.block.tiles.AbstractPresentBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PresentContainerMenu extends AbstractContainerMenu implements IContainerProvider {

    @Nullable
    protected final AbstractPresentBlockTile inventory;

    public AbstractPresentBlockTile getContainer() {
        return this.inventory;
    }

    public static PresentContainerMenu create(Integer integer, Inventory inventory, FriendlyByteBuf buf) {
        if (buf != null) {
            BlockPos pos = buf.readBlockPos();
            if (inventory.player.m_9236_().getBlockEntity(pos) instanceof AbstractPresentBlockTile tile) {
                return new PresentContainerMenu(integer, inventory, tile);
            }
        }
        return new PresentContainerMenu(integer, inventory, null);
    }

    public <T extends PresentContainerMenu> PresentContainerMenu(int id, Inventory playerInventory, AbstractPresentBlockTile inventory) {
        this((MenuType<T>) ModMenuTypes.PRESENT_BLOCK.get(), id, playerInventory, inventory);
    }

    public <T extends PresentContainerMenu> PresentContainerMenu(MenuType<T> type, int id, Inventory playerInventory, AbstractPresentBlockTile inventory) {
        super(type, id);
        this.inventory = inventory;
        if (inventory != null) {
            m_38869_(this.inventory, 1);
            this.inventory.m_5856_(playerInventory.player);
            this.m_38897_(new DelegatingSlot(this.inventory, 0, this.getSlotX(), this.getSlotY(), this));
            for (int si = 0; si < 3; si++) {
                for (int sj = 0; sj < 9; sj++) {
                    this.m_38897_(new Slot(playerInventory, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
                }
            }
            for (int si = 0; si < 9; si++) {
                this.m_38897_(new Slot(playerInventory, si, 8 + si * 18, 142));
            }
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.inventory != null && this.inventory.m_6542_(playerIn);
    }

    protected int getSlotY() {
        return 20;
    }

    protected int getSlotX() {
        return 17;
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
        if (!this.inventory.canHoldItems() && !this.inventory.m_58901_()) {
            this.m_150411_(playerIn, this.inventory);
        }
    }
}