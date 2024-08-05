package dev.latvian.mods.unit;

import java.util.HashMap;
import java.util.Map;

public class VariableUnit extends Unit {

    private static final Object CACHE_LOCK = new Object();

    private static final Map<String, VariableUnit> CACHE = new HashMap();

    public final String name;

    public static VariableUnit of(String name) {
        synchronized (CACHE_LOCK) {
            return (VariableUnit) CACHE.computeIfAbsent(name, VariableUnit::new);
        }
    }

    private VariableUnit(String n) {
        this.name = n;
    }

    @Override
    public double get(UnitVariables variables) {
        Unit var = variables.getVariables().get(this.name);
        if (var == null) {
            throw new IllegalStateException("Variable " + this.name + " is not defined!");
        } else {
            return var.get(variables);
        }
    }

    @Override
    public void toString(StringBuilder builder) {
        builder.append(this.name);
    }
}