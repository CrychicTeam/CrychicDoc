package dev.ftb.mods.ftblibrary.util;

import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface ChainedBooleanSupplier extends BooleanSupplier {

    ChainedBooleanSupplier TRUE = () -> true;

    ChainedBooleanSupplier FALSE = () -> false;

    default ChainedBooleanSupplier not() {
        return () -> !this.getAsBoolean();
    }

    default ChainedBooleanSupplier or(BooleanSupplier supplier) {
        return () -> this.getAsBoolean() || supplier.getAsBoolean();
    }

    default ChainedBooleanSupplier and(BooleanSupplier supplier) {
        return () -> this.getAsBoolean() && supplier.getAsBoolean();
    }

    default ChainedBooleanSupplier xor(BooleanSupplier supplier) {
        return () -> this.getAsBoolean() != supplier.getAsBoolean();
    }
}