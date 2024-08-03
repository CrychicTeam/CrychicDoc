package de.keksuccino.fancymenu.util.cycle;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ValueCycle<T> implements IValueCycle<T> {

    private static final Logger LOGGER = LogManager.getLogger();

    protected List<T> values = new ArrayList();

    protected int currentIndex = 0;

    protected List<Consumer<T>> cycleListeners = new ArrayList();

    public static <T> ValueCycle<T> fromList(@NotNull List<T> values) {
        Objects.requireNonNull(values);
        if (values.size() < 2) {
            throw new InvalidParameterException("Failed to create ValueCycle! Value list size too small (<2)!");
        } else {
            ValueCycle<T> valueCycle = new ValueCycle<>();
            valueCycle.values.addAll(values);
            return valueCycle;
        }
    }

    @SafeVarargs
    public static <T> ValueCycle<T> fromArray(@NotNull T... values) {
        Objects.requireNonNull(values);
        return fromList(Arrays.asList(values));
    }

    protected ValueCycle() {
    }

    @Override
    public List<T> getValues() {
        return new ArrayList(this.values);
    }

    public ValueCycle<T> removeValue(@NotNull T value) {
        if (this.values.size() == 2) {
            LOGGER.error("Unable to remove value! At least 2 values needed!");
            return this;
        } else {
            this.values.remove(value);
            this.setCurrentValueByIndex(0, false);
            return this;
        }
    }

    @NotNull
    @Override
    public T current() {
        return (T) this.values.get(this.currentIndex);
    }

    @NotNull
    @Override
    public T next() {
        if (this.currentIndex >= this.values.size() - 1) {
            this.currentIndex = 0;
        } else {
            this.currentIndex++;
        }
        this.notifyListeners();
        return this.current();
    }

    public ValueCycle<T> setCurrentValue(T value, boolean notifyListeners) {
        int i = this.values.indexOf(value);
        if (i != -1) {
            this.currentIndex = i;
            if (notifyListeners) {
                this.notifyListeners();
            }
        }
        return this;
    }

    public ValueCycle<T> setCurrentValue(T value) {
        return this.setCurrentValue(value, true);
    }

    public ValueCycle<T> setCurrentValueByIndex(int index, boolean notifyListeners) {
        if (index > 0 && index < this.values.size()) {
            this.currentIndex = index;
            if (notifyListeners) {
                this.notifyListeners();
            }
        }
        return this;
    }

    public ValueCycle<T> setCurrentValueByIndex(int index) {
        return this.setCurrentValueByIndex(index, true);
    }

    public ValueCycle<T> addCycleListener(@NotNull Consumer<T> listener) {
        this.cycleListeners.add(listener);
        return this;
    }

    public ValueCycle<T> clearCycleListeners() {
        this.cycleListeners.clear();
        return this;
    }

    protected void notifyListeners() {
        for (Consumer<T> listener : this.cycleListeners) {
            listener.accept(this.current());
        }
    }
}