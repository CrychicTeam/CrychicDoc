package fr.frinn.custommachinery.impl.network;

import fr.frinn.custommachinery.api.network.IData;
import fr.frinn.custommachinery.api.network.ISyncable;

public abstract class AbstractSyncable<D extends IData<?>, T> implements ISyncable<D, T> {

    public T lastKnownValue;

    @Override
    public boolean needSync() {
        T value = this.get();
        boolean needSync = !value.equals(this.lastKnownValue);
        this.lastKnownValue = value;
        return needSync;
    }
}