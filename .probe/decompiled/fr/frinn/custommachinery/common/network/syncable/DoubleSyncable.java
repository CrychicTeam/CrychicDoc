package fr.frinn.custommachinery.common.network.syncable;

import fr.frinn.custommachinery.common.network.data.DoubleData;
import fr.frinn.custommachinery.impl.network.AbstractSyncable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class DoubleSyncable extends AbstractSyncable<DoubleData, Double> {

    public DoubleData getData(short id) {
        return new DoubleData(id, this.get());
    }

    public static DoubleSyncable create(Supplier<Double> supplier, Consumer<Double> consumer) {
        return new DoubleSyncable() {

            public Double get() {
                return (Double) supplier.get();
            }

            public void set(Double value) {
                consumer.accept(value);
            }
        };
    }
}