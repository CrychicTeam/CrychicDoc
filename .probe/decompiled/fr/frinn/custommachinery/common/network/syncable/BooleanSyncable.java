package fr.frinn.custommachinery.common.network.syncable;

import fr.frinn.custommachinery.common.network.data.BooleanData;
import fr.frinn.custommachinery.impl.network.AbstractSyncable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class BooleanSyncable extends AbstractSyncable<BooleanData, Boolean> {

    public BooleanData getData(short id) {
        return new BooleanData(id, this.get());
    }

    public static BooleanSyncable create(Supplier<Boolean> supplier, Consumer<Boolean> consumer) {
        return new BooleanSyncable() {

            public Boolean get() {
                return (Boolean) supplier.get();
            }

            public void set(Boolean value) {
                consumer.accept(value);
            }
        };
    }
}