package dev.ftb.mods.ftbteams.api.property;

import java.util.function.Consumer;

public final class TeamPropertyValue<T> {

    private final TeamProperty<T> property;

    T value;

    final Consumer<T> consumer;

    public TeamPropertyValue(TeamProperty<T> property, T value) {
        this.property = property;
        this.value = value;
        this.consumer = val -> this.value = (T) val;
    }

    public TeamPropertyValue(TeamProperty<T> k) {
        this(k, k.getDefaultValue());
    }

    public TeamProperty<T> getProperty() {
        return this.property;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public TeamPropertyValue<T> copy() {
        return new TeamPropertyValue<>(this.property, this.value);
    }

    public String toString() {
        return this.property.id + ":" + this.value;
    }
}