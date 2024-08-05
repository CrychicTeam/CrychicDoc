package org.violetmoon.zeta.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChangeSet implements IZetaConfigInternals {

    private final IZetaConfigInternals internals;

    private final Map<ValueDefinition<?>, ChangeSet.Entry<?>> changes = new HashMap();

    public ChangeSet(IZetaConfigInternals internals) {
        this.internals = internals;
    }

    @Override
    public <T> void set(ValueDefinition<T> valueDef, T nextValue) {
        T currentValue = this.internals.get(valueDef);
        if (Objects.equals(currentValue, nextValue)) {
            this.removeChange(valueDef);
        } else {
            this.changes.put(valueDef, new ChangeSet.Entry<>(valueDef, currentValue, nextValue));
        }
    }

    public void toggle(ValueDefinition<Boolean> boolDef) {
        this.set(boolDef, !this.get(boolDef));
    }

    public <T> void resetToDefault(ValueDefinition<T> valueDef) {
        this.set(valueDef, valueDef.defaultValue);
    }

    public <T> void removeChange(ValueDefinition<T> valueDef) {
        this.changes.remove(valueDef);
    }

    public void resetToDefault(SectionDefinition sectionDef) {
        sectionDef.getValues().forEach(this::resetToDefault);
        sectionDef.getSubsections().forEach(this::resetToDefault);
    }

    public void removeChange(SectionDefinition sectionDef) {
        sectionDef.getValues().forEach(this::removeChange);
        sectionDef.getSubsections().forEach(this::removeChange);
    }

    public <T> boolean isDirty(ValueDefinition<T> valueDef) {
        return this.changes.containsKey(valueDef);
    }

    public boolean isDirty(SectionDefinition sectionDefinition) {
        return sectionDefinition.getValues().stream().anyMatch(this::isDirty) || sectionDefinition.getSubsections().stream().anyMatch(this::isDirty);
    }

    public int changeCount() {
        return this.changes.size();
    }

    public void removeChange(Definition def) {
        if (def instanceof ValueDefinition<?> val) {
            this.removeChange(val);
        } else {
            this.removeChange((SectionDefinition) def);
        }
    }

    public void resetToDefault(Definition def) {
        if (def instanceof ValueDefinition<?> val) {
            this.resetToDefault(val);
        } else {
            this.resetToDefault((SectionDefinition) def);
        }
    }

    public boolean isDirty(Definition def) {
        return def instanceof ValueDefinition<?> val ? this.isDirty(val) : this.isDirty((SectionDefinition) def);
    }

    public <T> List<T> getExactSizeCopy(ValueDefinition<List<T>> def, int size, T filler) {
        List<T> value = this.get(def);
        if (value.size() > size) {
            return new ArrayList(value.subList(0, size));
        } else {
            List<T> var5 = new ArrayList(value);
            while (var5.size() < size) {
                var5.add(filler);
            }
            return var5;
        }
    }

    @Override
    public <T> T get(ValueDefinition<T> definition) {
        ChangeSet.Entry<T> entry = (ChangeSet.Entry<T>) this.changes.get(definition);
        return entry != null ? entry.nextValue : this.internals.get(definition);
    }

    public void applyAllChanges() {
        this.changes.values().forEach(this::applyOneChange);
        this.changes.clear();
        this.flush();
    }

    private <T> void applyOneChange(ChangeSet.Entry<T> entry) {
        this.internals.set(entry.valueDef, entry.nextValue);
    }

    @Override
    public void flush() {
        this.internals.flush();
    }

    private static record Entry<T>(ValueDefinition<T> valueDef, T currentValue, T nextValue) {
    }
}