package fr.frinn.custommachinery.api.network;

public interface ISyncable<D extends IData<?>, T> {

    T get();

    void set(T var1);

    boolean needSync();

    D getData(short var1);
}