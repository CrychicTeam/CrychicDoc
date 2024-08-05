package dev.latvian.mods.unit.operator.cond;

import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitVariables;
import dev.latvian.mods.unit.operator.OpUnit;
import dev.latvian.mods.unit.token.UnitSymbol;

public abstract class CondOpUnit extends OpUnit {

    public CondOpUnit(UnitSymbol symbol, Unit left, Unit right) {
        super(symbol, left, right);
    }

    @Override
    public final double get(UnitVariables variables) {
        return this.getBoolean(variables) ? 1.0 : 0.0;
    }

    @Override
    public final float getFloat(UnitVariables variables) {
        return this.getBoolean(variables) ? 1.0F : 0.0F;
    }

    @Override
    public final int getInt(UnitVariables variables) {
        return this.getBoolean(variables) ? 1 : 0;
    }

    @Override
    public abstract boolean getBoolean(UnitVariables var1);
}