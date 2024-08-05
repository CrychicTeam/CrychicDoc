package fr.frinn.custommachinery.common.network.syncable;

import fr.frinn.custommachinery.common.network.data.FloatData;
import fr.frinn.custommachinery.impl.network.AbstractSyncable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class FloatSyncable extends AbstractSyncable<FloatData, Float> {

    public FloatData getData(short id) {
        return new FloatData(id, this.get());
    }

    public static FloatSyncable create(Supplier<Float> supplier, Consumer<Float> consumer) {
        return new FloatSyncable() {

            public Float get() {
                return (Float) supplier.get();
            }

            public void set(Float value) {
                consumer.accept(value);
            }
        };
    }
}