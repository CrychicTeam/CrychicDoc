package fr.frinn.custommachinery.common.network.syncable;

import fr.frinn.custommachinery.common.network.data.StringData;
import fr.frinn.custommachinery.impl.network.AbstractSyncable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class StringSyncable extends AbstractSyncable<StringData, String> {

    public StringData getData(short id) {
        return new StringData(id, this.get());
    }

    @Override
    public boolean needSync() {
        String value = this.get();
        boolean needSync = !value.equals(this.lastKnownValue);
        this.lastKnownValue = value;
        return needSync;
    }

    public static StringSyncable create(Supplier<String> supplier, Consumer<String> consumer) {
        return new StringSyncable() {

            public String get() {
                return (String) supplier.get();
            }

            public void set(String value) {
                consumer.accept(value);
            }
        };
    }
}