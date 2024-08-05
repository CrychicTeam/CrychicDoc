package dev.latvian.mods.unit;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class VariableSet implements UnitVariables {

    private final Map<String, Unit> variables = new HashMap();

    public VariableSet set(String name, Unit value) {
        this.variables.put(name, value);
        return this;
    }

    public VariableSet set(String name, double value) {
        return this.set(name, FixedNumberUnit.of(value));
    }

    public MutableNumberUnit setMutable(String name, double initialValue) {
        MutableNumberUnit unit = new MutableNumberUnit(initialValue);
        this.set(name, unit);
        return unit;
    }

    @Nullable
    public Unit get(String entry) {
        return (Unit) this.variables.get(entry);
    }

    public VariableSet createSubset() {
        return new VariableSubset(this);
    }

    @Override
    public final VariableSet getVariables() {
        return this;
    }
}