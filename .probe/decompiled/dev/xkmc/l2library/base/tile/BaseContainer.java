package dev.xkmc.l2library.base.tile;

import dev.xkmc.l2serial.serialization.codec.AliasCollection;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class BaseContainer<T extends BaseContainer<T>> extends SimpleContainer implements AliasCollection<ItemStack> {

    private int max = 64;

    private Predicate<ItemStack> predicate = e -> true;

    public BaseContainer(int size) {
        super(size);
    }

    public T setMax(int max) {
        this.max = Mth.clamp(max, 1, 64);
        return this.getThis();
    }

    public T setPredicate(Predicate<ItemStack> predicate) {
        this.predicate = predicate;
        return this.getThis();
    }

    public T add(BaseContainerListener t) {
        this.m_19164_(t);
        return this.getThis();
    }

    public boolean canAddWhileHaveSpace(ItemStack add, int space) {
        int ans = 0;
        for (ItemStack stack : this.f_19147_) {
            if (stack.isEmpty()) {
                ans++;
            } else if (ItemStack.isSameItemSameTags(stack, add) && stack.getCount() + add.getCount() <= Math.min(stack.getMaxStackSize(), this.getMaxStackSize())) {
                return true;
            }
        }
        return ans - 1 >= space;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.predicate.test(stack);
    }

    @Override
    public boolean canAddItem(ItemStack stack) {
        return this.predicate.test(stack) && super.canAddItem(stack);
    }

    public boolean canRecipeAddItem(ItemStack stack) {
        stack = stack.copy();
        for (ItemStack slot : this.f_19147_) {
            if (slot.isEmpty() || ItemStack.isSameItemSameTags(slot, stack)) {
                int cap = Math.min(this.getMaxStackSize(), slot.getMaxStackSize());
                int amount = Math.min(stack.getCount(), cap - slot.getCount());
                if (amount > 0) {
                    stack.shrink(amount);
                    if (stack.getCount() == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return this.max;
    }

    public List<ItemStack> getAsList() {
        return this.f_19147_;
    }

    public void clear() {
        super.clearContent();
    }

    public void set(int n, int i, ItemStack elem) {
        this.f_19147_.set(i, elem);
    }

    public Class<ItemStack> getElemClass() {
        return ItemStack.class;
    }

    public T getThis() {
        return (T) this;
    }
}