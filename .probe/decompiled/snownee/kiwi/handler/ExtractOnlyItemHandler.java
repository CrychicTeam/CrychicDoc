package snownee.kiwi.handler;

import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ExtractOnlyItemHandler<T extends IItemHandler> implements IItemHandler, Supplier<T> {

    private final T handler;

    public ExtractOnlyItemHandler(T handler) {
        this.handler = handler;
    }

    @Override
    public int getSlots() {
        return this.handler.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.handler.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.handler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.handler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return false;
    }

    public T get() {
        return this.handler;
    }
}