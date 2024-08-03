package dev.latvian.mods.rhino.mod.util;

@FunctionalInterface
public interface ChangeListener<T> {

    void onChanged(T var1);
}