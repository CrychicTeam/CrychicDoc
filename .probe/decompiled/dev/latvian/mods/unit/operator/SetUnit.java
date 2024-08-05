package dev.latvian.mods.unit.operator;

import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitVariables;
import dev.latvian.mods.unit.VariableUnit;
import dev.latvian.mods.unit.token.UnitSymbol;

public class SetUnit extends OpUnit {

    public SetUnit(UnitSymbol symbol, Unit left, Unit right) {
        super(symbol, left, right);
    }

    @Override
    public double get(UnitVariables variables) {
        if (this.left instanceof VariableUnit var) {
            variables.getVariables().set(var.name, this.right.get(variables));
        }
        return this.right.get(variables);
    }

    @Override
    public int getInt(UnitVariables variables) {
        if (this.left instanceof VariableUnit var) {
            variables.getVariables().set(var.name, this.right.get(variables));
        }
        return this.right.getInt(variables);
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        if (this.left instanceof VariableUnit var) {
            variables.getVariables().set(var.name, this.right.get(variables));
        }
        return this.right.getBoolean(variables);
    }
}