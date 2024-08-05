package dev.latvian.mods.unit.operator.cond;

import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitVariables;
import dev.latvian.mods.unit.token.UnitSymbol;

public class NeqOpUnit extends CondOpUnit {

    public NeqOpUnit(Unit left, Unit right) {
        super(UnitSymbol.NEQ, left, right);
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        return this.left != this.right && Math.abs(this.left.get(variables) - this.right.get(variables)) >= 1.0E-5;
    }
}