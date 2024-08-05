package dev.latvian.mods.unit.operator.op;

import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitVariables;
import dev.latvian.mods.unit.operator.OpUnit;
import dev.latvian.mods.unit.token.UnitSymbol;

public class XorOpUnit extends OpUnit {

    public XorOpUnit(Unit left, Unit right) {
        super(UnitSymbol.XOR, left, right);
    }

    @Override
    public double get(UnitVariables variables) {
        return (double) this.getInt(variables);
    }

    @Override
    public int getInt(UnitVariables variables) {
        return this.left.getInt(variables) ^ this.right.getInt(variables);
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        return this.left.getBoolean(variables) ^ this.right.getBoolean(variables);
    }
}