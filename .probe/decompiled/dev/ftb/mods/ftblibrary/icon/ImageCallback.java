package dev.ftb.mods.ftblibrary.icon;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ImageCallback<T> {

    void imageLoaded(boolean var1, @Nullable T var2);
}