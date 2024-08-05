package dev.latvian.mods.unit.operator.cond;

import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitVariables;
import dev.latvian.mods.unit.token.UnitSymbol;

public class GteOpUnit extends CondOpUnit {

    public GteOpUnit(Unit left, Unit right) {
        super(UnitSymbol.GTE, left, right);
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        return this.left.get(variables) >= this.right.get(variables);
    }
}