package dev.latvian.mods.unit.token;

import dev.latvian.mods.unit.FixedNumberUnit;
import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.VariableUnit;

public record StringUnitToken(String name) implements UnitToken {

    @Override
    public Unit interpret(UnitTokenStream stream) {
        Unit constant = (Unit) stream.context.constants.get(this.name);
        if (constant != null) {
            return constant;
        } else {
            try {
                return FixedNumberUnit.of(Double.parseDouble(this.name));
            } catch (Exception var4) {
                return VariableUnit.of(this.name);
            }
        }
    }

    public String toString() {
        return this.name;
    }
}