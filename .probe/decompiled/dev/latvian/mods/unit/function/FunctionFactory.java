package dev.latvian.mods.unit.function;

import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.token.UnitInterpretException;
import java.util.function.Supplier;

public record FunctionFactory(String name, int minArgs, int maxArgs, FunctionFactory.FuncSupplier supplier) {

    public static FunctionFactory of(String name, int minArgs, int maxArgs, FunctionFactory.FuncSupplier supplier) {
        return new FunctionFactory(name, minArgs, maxArgs, supplier);
    }

    public static FunctionFactory of(String name, int args, FunctionFactory.FuncSupplier supplier) {
        return of(name, args, args, supplier);
    }

    public static FunctionFactory of0(String name, Supplier<Unit> supplier) {
        return of(name, 0, new FunctionFactory.Arg0(supplier));
    }

    public static FunctionFactory of1(String name, FunctionFactory.Arg1 supplier) {
        return of(name, 1, supplier);
    }

    public static FunctionFactory of2(String name, FunctionFactory.Arg2 supplier) {
        return of(name, 2, supplier);
    }

    public static FunctionFactory of3(String name, FunctionFactory.Arg3 supplier) {
        return of(name, 3, supplier);
    }

    public Unit create(Unit[] args) {
        if (args.length >= this.minArgs && args.length <= this.maxArgs) {
            return this.supplier.create(args);
        } else {
            throw new UnitInterpretException("Invalid number of arguments for function '" + this.name + "'. Expected " + (this.minArgs == this.maxArgs ? String.valueOf(this.minArgs) : this.minArgs + "-" + this.maxArgs) + " but got " + args.length);
        }
    }

    public static final class Arg0 implements FunctionFactory.FuncSupplier {

        private final Supplier<Unit> unit;

        private Unit cachedUnit;

        public Arg0(Supplier<Unit> unit) {
            this.unit = unit;
        }

        @Override
        public Unit create(Unit[] args) {
            if (this.cachedUnit == null) {
                this.cachedUnit = (Unit) this.unit.get();
            }
            return this.cachedUnit;
        }
    }

    @FunctionalInterface
    public interface Arg1 extends FunctionFactory.FuncSupplier {

        Unit createArg(Unit var1);

        @Override
        default Unit create(Unit[] args) {
            return this.createArg(args[0]);
        }
    }

    @FunctionalInterface
    public interface Arg2 extends FunctionFactory.FuncSupplier {

        Unit createArg(Unit var1, Unit var2);

        @Override
        default Unit create(Unit[] args) {
            return this.createArg(args[0], args[1]);
        }
    }

    @FunctionalInterface
    public interface Arg3 extends FunctionFactory.FuncSupplier {

        Unit createArg(Unit var1, Unit var2, Unit var3);

        @Override
        default Unit create(Unit[] args) {
            return this.createArg(args[0], args[1], args[2]);
        }
    }

    @FunctionalInterface
    public interface FuncSupplier {

        Unit create(Unit[] var1);
    }
}