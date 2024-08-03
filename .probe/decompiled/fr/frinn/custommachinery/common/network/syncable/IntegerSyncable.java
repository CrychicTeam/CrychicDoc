package fr.frinn.custommachinery.common.network.syncable;

import fr.frinn.custommachinery.common.network.data.IntegerData;
import fr.frinn.custommachinery.impl.network.AbstractSyncable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class IntegerSyncable extends AbstractSyncable<IntegerData, Integer> {

    public IntegerData getData(short id) {
        return new IntegerData(id, this.get());
    }

    public static IntegerSyncable create(Supplier<Integer> supplier, Consumer<Integer> consumer) {
        return new IntegerSyncable() {

            public Integer get() {
                return (Integer) supplier.get();
            }

            public void set(Integer value) {
                consumer.accept(value);
            }
        };
    }
}