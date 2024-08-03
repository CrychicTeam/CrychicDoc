package me.jellysquid.mods.sodium.client.gui.options.binding;

public interface OptionBinding<S, T> {

    void setValue(S var1, T var2);

    T getValue(S var1);
}