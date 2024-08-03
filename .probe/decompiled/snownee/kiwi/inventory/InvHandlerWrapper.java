package snownee.kiwi.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class InvHandlerWrapper implements Container {

    protected final IItemHandlerModifiable handler;

    public InvHandlerWrapper(IItemHandlerModifiable handler) {
        this.handler = handler;
    }

    @Override
    public void clearContent() {
        int size = this.getContainerSize();
        for (int i = 0; i < size; i++) {
            this.removeItemNoUpdate(i);
        }
    }

    @Override
    public int getContainerSize() {
        return this.handler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        int size = this.getContainerSize();
        for (int i = 0; i < size; i++) {
            if (!this.getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.handler.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = this.getItem(index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack ret = stack.split(count);
            this.setItem(index, stack);
            return ret;
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = this.getItem(index);
        this.setItem(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.handler.setStackInSlot(index, stack);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return this.handler.isItemValid(index, stack);
    }
}