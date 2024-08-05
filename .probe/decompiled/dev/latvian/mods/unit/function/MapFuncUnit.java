package dev.latvian.mods.unit.function;

import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitVariables;

public class MapFuncUnit extends FuncUnit {

    public static final FunctionFactory FACTORY = FunctionFactory.of("map", 5, MapFuncUnit::new);

    public final Unit value;

    public final Unit min1;

    public final Unit max1;

    public final Unit min2;

    public final Unit max2;

    public static double map(double value, double min1, double max1, double min2, double max2) {
        return LerpFuncUnit.lerp(min2, max2, (value - min1) / (max1 - min1));
    }

    public MapFuncUnit(Unit[] args) {
        super(FACTORY);
        this.value = args[0];
        this.min1 = args[1];
        this.max1 = args[2];
        this.min2 = args[3];
        this.max2 = args[4];
    }

    @Override
    public double get(UnitVariables variables) {
        return map(this.value.get(variables), this.min1.get(variables), this.max1.get(variables), this.min2.get(variables), this.max2.get(variables));
    }
}