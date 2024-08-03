package fr.frinn.custommachinery.common.network.syncable;

import fr.frinn.custommachinery.common.network.data.ItemStackData;
import fr.frinn.custommachinery.impl.network.AbstractSyncable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;

public abstract class ItemStackSyncable extends AbstractSyncable<ItemStackData, ItemStack> {

    public ItemStackData getData(short id) {
        return new ItemStackData(id, this.get());
    }

    @Override
    public boolean needSync() {
        ItemStack value = this.get();
        boolean needSync;
        if (this.lastKnownValue != null) {
            needSync = !ItemStack.matches(value, this.lastKnownValue);
        } else {
            needSync = true;
        }
        this.lastKnownValue = value.copy();
        return needSync;
    }

    public static ItemStackSyncable create(Supplier<ItemStack> supplier, Consumer<ItemStack> consumer) {
        return new ItemStackSyncable() {

            public ItemStack get() {
                return (ItemStack) supplier.get();
            }

            public void set(ItemStack value) {
                consumer.accept(value);
            }
        };
    }
}