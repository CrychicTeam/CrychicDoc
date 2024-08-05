package dev.latvian.mods.unit.function;

import dev.latvian.mods.unit.UnitVariables;

public class RoundedTimeUnit extends FuncUnit {

    public static final FunctionFactory FACTORY = FunctionFactory.of0("roundedTime", RoundedTimeUnit::new);

    public static long time() {
        return Math.round((double) System.currentTimeMillis() / 1000.0);
    }

    private RoundedTimeUnit() {
        super(FACTORY);
    }

    @Override
    public double get(UnitVariables variables) {
        return (double) time();
    }
}