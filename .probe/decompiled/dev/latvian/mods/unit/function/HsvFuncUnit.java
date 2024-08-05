package dev.latvian.mods.unit.function;

import dev.latvian.mods.unit.FixedNumberUnit;
import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitVariables;

public class HsvFuncUnit extends FuncUnit {

    public final Unit h;

    public final Unit s;

    public final Unit v;

    public final Unit a;

    public static final FunctionFactory FACTORY = FunctionFactory.of("hsv", 3, 4, HsvFuncUnit::new);

    public HsvFuncUnit(Unit[] args) {
        super(FACTORY);
        this.h = args[0];
        this.s = args[1];
        this.v = args[2];
        this.a = (Unit) (args.length == 4 ? args[3] : FixedNumberUnit.ONE);
    }

    @Override
    protected Unit[] getArguments() {
        return this.a == FixedNumberUnit.ONE ? new Unit[] { this.h, this.s, this.v } : new Unit[] { this.h, this.s, this.v, this.a };
    }

    @Override
    public double get(UnitVariables variables) {
        return (double) this.getInt(variables);
    }

    @Override
    public int getInt(UnitVariables variables) {
        double h = this.h.get(variables);
        double s = this.s.get(variables);
        double v = this.v.get(variables);
        int i = (int) (h * 6.0) % 6;
        double j = h * 6.0 - (double) ((float) i);
        double k = v * (1.0 - s);
        double l = v * (1.0 - j * s);
        double m = v * (1.0 - (1.0 - j) * s);
        double dr;
        double dg;
        double db;
        switch(i) {
            case 0:
                dr = v;
                dg = m;
                db = k;
                break;
            case 1:
                dr = l;
                dg = v;
                db = k;
                break;
            case 2:
                dr = k;
                dg = v;
                db = m;
                break;
            case 3:
                dr = k;
                dg = l;
                db = v;
                break;
            case 4:
                dr = m;
                dg = k;
                db = v;
                break;
            case 5:
                dr = v;
                dg = k;
                db = l;
                break;
            default:
                dr = 0.0;
                dg = 0.0;
                db = 0.0;
        }
        int cr = (int) ClampFuncUnit.clamp(dr * 255.0, 0.0, 255.0);
        int cg = (int) ClampFuncUnit.clamp(dg * 255.0, 0.0, 255.0);
        int cb = (int) ClampFuncUnit.clamp(db * 255.0, 0.0, 255.0);
        int ca = (int) ClampFuncUnit.clamp(this.a.get(variables) * 255.0, 0.0, 255.0);
        return ca << 24 | cr << 16 | cg << 8 | cb;
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        return this.a.getBoolean(variables);
    }

    @Override
    public Unit withAlpha(Unit a) {
        return a == this.a ? this : new HsvFuncUnit(new Unit[] { this.h, this.s, this.v, a });
    }
}