package dev.latvian.mods.unit.function;

import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitVariables;

public class BoolFuncUnit extends Func1Unit {

    public static final FunctionFactory FACTORY = FunctionFactory.of1("bool", Unit::bool);

    public BoolFuncUnit(Unit a) {
        super(FACTORY, a);
    }

    @Override
    public double get(UnitVariables variables) {
        return this.getBoolean(variables) ? 1.0 : 0.0;
    }

    @Override
    public float getFloat(UnitVariables variables) {
        return this.getBoolean(variables) ? 1.0F : 0.0F;
    }

    @Override
    public int getInt(UnitVariables variables) {
        return this.getBoolean(variables) ? 1 : 0;
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        return this.a.getBoolean(variables);
    }
}