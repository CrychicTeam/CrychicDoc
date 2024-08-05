package dev.latvian.mods.unit.token;

import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.function.FunctionFactory;
import java.util.List;

public record FunctionUnitToken(String name, List<UnitToken> args) implements UnitToken {

    @Override
    public Unit interpret(UnitTokenStream stream) {
        FunctionFactory factory = stream.context.getFunctionFactory(this.name);
        if (factory == null) {
            throw new IllegalStateException("Unknown function '" + this.name + "'!");
        } else if (this.args.isEmpty()) {
            return factory.create(Unit.EMPTY_ARRAY);
        } else {
            Unit[] newArgs = new Unit[this.args.size()];
            for (int i = 0; i < this.args.size(); i++) {
                newArgs[i] = ((UnitToken) this.args.get(i)).interpret(stream);
            }
            return factory.create(newArgs);
        }
    }
}