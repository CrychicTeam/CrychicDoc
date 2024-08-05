package fr.frinn.custommachinery.common.network.syncable;

import dev.architectury.fluid.FluidStack;
import fr.frinn.custommachinery.common.network.data.FluidStackData;
import fr.frinn.custommachinery.impl.network.AbstractSyncable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class FluidStackSyncable extends AbstractSyncable<FluidStackData, FluidStack> {

    public FluidStackData getData(short id) {
        return new FluidStackData(id, this.get());
    }

    @Override
    public boolean needSync() {
        FluidStack value = this.get();
        boolean needSync;
        if (this.lastKnownValue != null) {
            needSync = !value.isFluidStackEqual(this.lastKnownValue);
        } else {
            needSync = true;
        }
        this.lastKnownValue = value.copy();
        return needSync;
    }

    public static FluidStackSyncable create(Supplier<FluidStack> supplier, Consumer<FluidStack> consumer) {
        return new FluidStackSyncable() {

            public FluidStack get() {
                return (FluidStack) supplier.get();
            }

            public void set(FluidStack value) {
                consumer.accept(value);
            }
        };
    }
}