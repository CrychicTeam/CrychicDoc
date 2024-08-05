package ca.fxco.memoryleakfix.mixinextras.sugar.ref;

public interface LocalRef<T> {

    T get();

    void set(T var1);
}