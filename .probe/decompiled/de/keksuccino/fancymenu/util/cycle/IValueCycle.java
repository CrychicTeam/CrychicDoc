package de.keksuccino.fancymenu.util.cycle;

import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public interface IValueCycle<T> {

    List<T> getValues();

    IValueCycle<T> removeValue(@NotNull T var1);

    @NotNull
    T current();

    @NotNull
    T next();

    IValueCycle<T> setCurrentValue(T var1, boolean var2);

    IValueCycle<T> setCurrentValue(T var1);

    IValueCycle<T> setCurrentValueByIndex(int var1, boolean var2);

    IValueCycle<T> setCurrentValueByIndex(int var1);

    IValueCycle<T> addCycleListener(@NotNull Consumer<T> var1);

    IValueCycle<T> clearCycleListeners();
}