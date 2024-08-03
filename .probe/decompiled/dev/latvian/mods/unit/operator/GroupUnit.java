package dev.latvian.mods.unit.operator;

import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitVariables;

public class GroupUnit extends Unit {

    public final Unit[] units;

    public GroupUnit(Unit[] units) {
        this.units = units;
    }

    @Override
    public double get(UnitVariables variables) {
        double value = 0.0;
        for (Unit unit : this.units) {
            value = unit.get(variables);
        }
        return value;
    }

    @Override
    public int getInt(UnitVariables variables) {
        int value = 0;
        for (Unit unit : this.units) {
            value = unit.getInt(variables);
        }
        return value;
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        boolean value = false;
        for (Unit unit : this.units) {
            value = unit.getBoolean(variables);
        }
        return value;
    }

    @Override
    public void toString(StringBuilder builder) {
        for (int i = 0; i < this.units.length; i++) {
            if (i > 0) {
                builder.append(';');
                builder.append(' ');
            }
            this.units[i].toString(builder);
        }
    }
}