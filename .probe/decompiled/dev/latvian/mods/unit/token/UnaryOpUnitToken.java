package dev.latvian.mods.unit.token;

import dev.latvian.mods.unit.Unit;

public record UnaryOpUnitToken(UnitSymbol operator, UnitToken token) implements UnitToken {

    @Override
    public Unit interpret(UnitTokenStream stream) {
        Unit unit = this.token.interpret(stream);
        return this.operator.unaryOp.create(unit);
    }

    public String toString() {
        return "(" + this.operator + this.token + ")";
    }
}