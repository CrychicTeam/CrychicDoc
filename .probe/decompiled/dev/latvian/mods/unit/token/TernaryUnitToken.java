package dev.latvian.mods.unit.token;

import dev.latvian.mods.unit.TernaryUnit;
import dev.latvian.mods.unit.Unit;

public record TernaryUnitToken(UnitToken cond, UnitToken ifTrue, UnitToken ifFalse) implements UnitToken {

    @Override
    public Unit interpret(UnitTokenStream stream) {
        return new TernaryUnit(this.cond.interpret(stream), this.ifTrue.interpret(stream), this.ifFalse.interpret(stream));
    }

    public String toString() {
        return "(" + this.cond + " ? " + this.ifTrue + " : " + this.ifFalse + ")";
    }
}