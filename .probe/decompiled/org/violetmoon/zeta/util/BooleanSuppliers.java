package org.violetmoon.zeta.util;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class BooleanSuppliers {

    public static final BooleanSupplier TRUE = () -> true;

    public static final BooleanSupplier FALSE = () -> false;

    private static final Supplier<Boolean> BOXED_TRUE = () -> true;

    private static final Supplier<Boolean> BOXED_FALSE = () -> false;

    private BooleanSuppliers() {
    }

    public static BooleanSupplier and(BooleanSupplier a, BooleanSupplier b) {
        if (a == FALSE || b == FALSE) {
            return FALSE;
        } else if (a == TRUE) {
            return b;
        } else {
            return b == TRUE ? a : () -> a.getAsBoolean() && b.getAsBoolean();
        }
    }

    public static BooleanSupplier or(BooleanSupplier a, BooleanSupplier b) {
        if (a == TRUE || b == TRUE) {
            return TRUE;
        } else if (a == FALSE) {
            return b;
        } else {
            return b == FALSE ? a : () -> a.getAsBoolean() || b.getAsBoolean();
        }
    }

    public static BooleanSupplier not(BooleanSupplier x) {
        if (x == TRUE) {
            return FALSE;
        } else {
            return x == FALSE ? TRUE : () -> !x.getAsBoolean();
        }
    }

    public static Supplier<Boolean> boxed(BooleanSupplier x) {
        if (x == TRUE) {
            return BOXED_TRUE;
        } else {
            return x == FALSE ? BOXED_FALSE : x::getAsBoolean;
        }
    }
}