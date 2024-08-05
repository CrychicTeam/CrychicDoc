package dev.shadowsoffire.placebo.menu;

import dev.shadowsoffire.placebo.cap.InternalItemHandler;
import java.util.function.Predicate;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class PlaceboContainerMenu extends AbstractContainerMenu implements QuickMoveHandler.IExposedContainer {

    protected final Level level;

    protected final QuickMoveHandler mover = new QuickMoveHandler();

    protected int playerInvStart = -1;

    protected int hotbarStart = -1;

    protected PlaceboContainerMenu(MenuType<?> type, int id, Inventory pInv) {
        super(type, id);
        this.level = pInv.player.m_9236_();
    }

    protected void addPlayerSlots(Inventory pInv, int x, int y) {
        this.playerInvStart = this.f_38839_.size();
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.m_38897_(new Slot(pInv, column + row * 9 + 9, x + column * 18, y + row * 18));
            }
        }
        this.hotbarStart = this.f_38839_.size();
        for (int row = 0; row < 9; row++) {
            this.m_38897_(new Slot(pInv, row, x + row * 18, y + 58));
        }
    }

    protected void registerInvShuffleRules() {
        if (this.hotbarStart != -1 && this.playerInvStart != -1) {
            this.mover.registerRule((stack, slot) -> slot >= this.hotbarStart, this.playerInvStart, this.hotbarStart);
            this.mover.registerRule((stack, slot) -> slot >= this.playerInvStart, this.hotbarStart, this.f_38839_.size());
        } else {
            throw new UnsupportedOperationException("Attempted to register inv shuffle rules with no player inv slots.");
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return this.mover.quickMoveStack(this, pPlayer, pIndex);
    }

    @Override
    public boolean moveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
        return super.moveItemStackTo(pStack, pStartIndex, pEndIndex, pReverseDirection);
    }

    @Override
    public void setData(int pId, int pData) {
        super.setData(pId, pData);
        this.m_182420_(pId, pData);
    }

    public void addDataListener(final IDataUpdateListener listener) {
        this.m_38893_(new ContainerListener() {

            @Override
            public void slotChanged(AbstractContainerMenu pContainerToSend, int pDataSlotIndex, ItemStack pStack) {
            }

            @Override
            public void dataChanged(AbstractContainerMenu pContainerMenu, int pDataSlotIndex, int pValue) {
                listener.dataUpdated(pDataSlotIndex, pValue);
            }
        });
    }

    public void addSlotListener(final SlotUpdateListener listener) {
        this.m_38893_(new ContainerListener() {

            @Override
            public void slotChanged(AbstractContainerMenu pContainerToSend, int pDataSlotIndex, ItemStack pStack) {
                listener.slotUpdated(pDataSlotIndex, pStack);
            }

            @Override
            public void dataChanged(AbstractContainerMenu pContainerMenu, int pDataSlotIndex, int pValue) {
            }
        });
    }

    protected class UpdatingSlot extends FilteredSlot {

        public UpdatingSlot(InternalItemHandler handler, int index, int x, int y, Predicate<ItemStack> filter) {
            super(handler, index, x, y, filter);
        }

        @Override
        public void setChanged() {
            super.m_6654_();
            PlaceboContainerMenu.this.m_6199_(null);
        }
    }
}