package fr.frinn.custommachinery.common.network.syncable;

import fr.frinn.custommachinery.common.network.data.NbtData;
import fr.frinn.custommachinery.impl.network.AbstractSyncable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;

public abstract class NbtSyncable extends AbstractSyncable<NbtData, CompoundTag> {

    public NbtData getData(short id) {
        return new NbtData(id, this.get());
    }

    @Override
    public boolean needSync() {
        CompoundTag value = this.get();
        boolean needSync;
        if (this.lastKnownValue != null) {
            needSync = !value.equals(this.lastKnownValue);
        } else {
            needSync = true;
        }
        this.lastKnownValue = value.copy();
        return needSync;
    }

    public static NbtSyncable create(Supplier<CompoundTag> supplier, Consumer<CompoundTag> consumer) {
        return new NbtSyncable() {

            public CompoundTag get() {
                return (CompoundTag) supplier.get();
            }

            public void set(CompoundTag value) {
                consumer.accept(value);
            }
        };
    }
}