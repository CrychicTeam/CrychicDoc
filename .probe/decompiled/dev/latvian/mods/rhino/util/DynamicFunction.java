package dev.latvian.mods.rhino.util;

import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;
import org.jetbrains.annotations.Nullable;

public class DynamicFunction extends BaseFunction {

    private final DynamicFunction.Callback function;

    public DynamicFunction(DynamicFunction.Callback f) {
        this.function = f;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return this.function.call(args);
    }

    @FunctionalInterface
    public interface Callback {

        @Nullable
        Object call(Object[] var1);
    }
}