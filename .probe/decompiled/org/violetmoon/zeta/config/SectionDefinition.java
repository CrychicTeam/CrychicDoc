package org.violetmoon.zeta.config;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SectionDefinition extends Definition {

    @NotNull
    public final Map<String, SectionDefinition> subsections;

    @NotNull
    public final Map<String, ValueDefinition<?>> values;

    public SectionDefinition(SectionDefinition.Builder builder) {
        super(builder);
        this.subsections = builder.subsections;
        this.values = builder.values;
    }

    public void finish() {
        this.getSubsections().removeIf(sub -> !sub.getAllChildren().iterator().hasNext());
        this.getAllChildren().forEach(child -> child.setParent(this));
        this.getSubsections().forEach(SectionDefinition::finish);
    }

    @Nullable
    public ValueDefinition<?> getValue(String name) {
        return (ValueDefinition<?>) this.values.get(name);
    }

    @Nullable
    public <T> ValueDefinition<T> getValue(String name, Class<T> type) {
        ValueDefinition<?> value = this.getValue(name);
        return value == null ? null : value.downcast(type);
    }

    @Nullable
    public <T> ValueDefinition<T> getValueErased(String name, Class<?> type) {
        return this.getValue(name, (Class<T>) type);
    }

    public Collection<SectionDefinition> getSubsections() {
        return this.subsections.values();
    }

    public Collection<ValueDefinition<?>> getValues() {
        return this.values.values();
    }

    public Iterable<Definition> getAllChildren() {
        return Iterables.concat(this.getSubsections(), this.getValues());
    }

    public String toString() {
        return "SectionDefinition{" + this.name + " (" + this.subsections.size() + " subsections, " + this.values.size() + " values)}";
    }

    public static class Builder extends Definition.Builder<SectionDefinition.Builder, SectionDefinition> {

        protected final Map<String, SectionDefinition> subsections = new LinkedHashMap();

        protected final Map<String, ValueDefinition<?>> values = new LinkedHashMap();

        public SectionDefinition build() {
            return new SectionDefinition(this);
        }

        public SectionDefinition.Builder addSubsection(SectionDefinition value) {
            this.subsections.put(value.name, value);
            return this;
        }

        public SectionDefinition.Builder addValue(ValueDefinition<?> value) {
            this.values.put(value.name, value);
            return this;
        }

        public SectionDefinition addSubsection(Consumer<SectionDefinition.Builder> buildAction) {
            SectionDefinition.Builder childBuilder = new SectionDefinition.Builder();
            buildAction.accept(childBuilder);
            SectionDefinition child = childBuilder.build();
            this.addSubsection(child);
            return child;
        }

        public <T> ValueDefinition<T> addValue(Consumer<ValueDefinition.Builder<T>> buildAction) {
            ValueDefinition.Builder<T> childBuilder = new ValueDefinition.Builder<>();
            buildAction.accept(childBuilder);
            ValueDefinition<T> child = childBuilder.build();
            this.addValue(child);
            return child;
        }
    }
}