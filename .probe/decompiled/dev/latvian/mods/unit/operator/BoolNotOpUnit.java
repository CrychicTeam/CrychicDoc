package dev.latvian.mods.unit.operator;

import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitVariables;
import dev.latvian.mods.unit.token.UnitSymbol;

public class BoolNotOpUnit extends UnaryOpUnit {

    public BoolNotOpUnit(Unit unit) {
        super(UnitSymbol.BOOL_NOT, unit);
    }

    @Override
    public double get(UnitVariables variables) {
        return this.getBoolean(variables) ? 1.0 : 0.0;
    }

    @Override
    public int getInt(UnitVariables variables) {
        return this.getBoolean(variables) ? 1 : 0;
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        return !this.unit.getBoolean(variables);
    }
}