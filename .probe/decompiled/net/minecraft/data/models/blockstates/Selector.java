package net.minecraft.data.models.blockstates;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.state.properties.Property;

public final class Selector {

    private static final Selector EMPTY = new Selector(ImmutableList.of());

    private static final Comparator<Property.Value<?>> COMPARE_BY_NAME = Comparator.comparing(p_125494_ -> p_125494_.property().getName());

    private final List<Property.Value<?>> values;

    public Selector extend(Property.Value<?> propertyValue0) {
        return new Selector(ImmutableList.builder().addAll(this.values).add(propertyValue0).build());
    }

    public Selector extend(Selector selector0) {
        return new Selector(ImmutableList.builder().addAll(this.values).addAll(selector0.values).build());
    }

    private Selector(List<Property.Value<?>> listPropertyValue0) {
        this.values = listPropertyValue0;
    }

    public static Selector empty() {
        return EMPTY;
    }

    public static Selector of(Property.Value<?>... propertyValue0) {
        return new Selector(ImmutableList.copyOf(propertyValue0));
    }

    public boolean equals(Object object0) {
        return this == object0 || object0 instanceof Selector && this.values.equals(((Selector) object0).values);
    }

    public int hashCode() {
        return this.values.hashCode();
    }

    public String getKey() {
        return (String) this.values.stream().sorted(COMPARE_BY_NAME).map(Property.Value::toString).collect(Collectors.joining(","));
    }

    public String toString() {
        return this.getKey();
    }
}