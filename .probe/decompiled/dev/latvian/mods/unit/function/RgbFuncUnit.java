package dev.latvian.mods.unit.function;

import dev.latvian.mods.unit.EmptyVariableSet;
import dev.latvian.mods.unit.FixedColorUnit;
import dev.latvian.mods.unit.FixedNumberUnit;
import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitVariables;

public class RgbFuncUnit extends FuncUnit {

    public static final FunctionFactory FACTORY = FunctionFactory.of("rgb", 1, 4, args -> {
        if (args.length == 1 && args[0] instanceof FixedColorUnit) {
            return args[0];
        } else {
            RgbFuncUnit c = new RgbFuncUnit();
            c.a = FixedNumberUnit.ONE;
            if (args.length == 3 || args.length == 4) {
                c.r = args[0];
                c.g = args[1];
                c.b = args[2];
                if (args.length == 4) {
                    c.a = args[3];
                }
            } else if (args.length == 2) {
                if (args[0] instanceof FixedColorUnit u) {
                    if (args[1].isFixed()) {
                        return u.withAlpha(args[1]);
                    }
                    c.r = FixedNumberUnit.of((double) (u.color >> 16 & 0xFF) / 255.0);
                    c.g = FixedNumberUnit.of((double) (u.color >> 8 & 0xFF) / 255.0);
                    c.b = FixedNumberUnit.of((double) (u.color >> 0 & 0xFF) / 255.0);
                    c.a = args[1];
                } else {
                    c.r = c.g = c.b = args[0];
                    c.a = args[1];
                }
            } else if (args.length == 1) {
                c.r = c.g = c.b = args[0];
            }
            return (Unit) (c.r.isFixed() && c.g.isFixed() && c.b.isFixed() && c.a.isFixed() ? FixedColorUnit.of(c.getInt(EmptyVariableSet.INSTANCE), true) : c);
        }
    });

    public Unit r;

    public Unit g;

    public Unit b;

    public Unit a;

    private static int c(UnitVariables variables, Unit u) {
        return (int) Math.min(Math.max(0.0, u.get(variables) * 255.0), 255.0);
    }

    private RgbFuncUnit() {
        super(FACTORY);
    }

    @Override
    protected Unit[] getArguments() {
        return this.a == FixedNumberUnit.ONE ? new Unit[] { this.r, this.g, this.b } : new Unit[] { this.r, this.g, this.b, this.a };
    }

    @Override
    public double get(UnitVariables variables) {
        return (double) this.getInt(variables);
    }

    @Override
    public int getInt(UnitVariables variables) {
        return c(variables, this.r) << 16 | c(variables, this.g) << 8 | c(variables, this.b) | c(variables, this.a) << 24;
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        return this.a.getBoolean(variables);
    }
}