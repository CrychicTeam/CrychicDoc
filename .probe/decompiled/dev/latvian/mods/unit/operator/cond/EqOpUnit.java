package dev.latvian.mods.unit.operator.cond;

import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitVariables;
import dev.latvian.mods.unit.token.UnitSymbol;

public class EqOpUnit extends CondOpUnit {

    public EqOpUnit(Unit left, Unit right) {
        super(UnitSymbol.EQ, left, right);
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        return this.left == this.right || Math.abs(this.left.get(variables) - this.right.get(variables)) < 1.0E-5;
    }
}