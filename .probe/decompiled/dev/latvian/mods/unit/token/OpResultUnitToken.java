package dev.latvian.mods.unit.token;

import dev.latvian.mods.unit.Unit;

public record OpResultUnitToken(UnitSymbol operator, UnitToken left, UnitToken right) implements UnitToken {

    @Override
    public Unit interpret(UnitTokenStream stream) {
        Unit uleft = this.left.interpret(stream);
        Unit uright = this.right.interpret(stream);
        return this.operator.op.create(uleft, uright);
    }

    public String toString() {
        return "(" + this.left + " " + this.operator + " " + this.right + ")";
    }
}