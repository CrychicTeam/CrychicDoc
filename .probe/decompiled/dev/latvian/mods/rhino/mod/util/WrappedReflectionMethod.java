package dev.latvian.mods.rhino.mod.util;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.WrappedExecutable;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.jetbrains.annotations.Nullable;

public record WrappedReflectionMethod(Method method) implements WrappedExecutable {

    public static WrappedExecutable of(Method method) {
        return method == null ? null : new WrappedReflectionMethod(method);
    }

    @Override
    public Object invoke(Context cx, Scriptable scope, Object self, Object[] args) throws Exception {
        return this.method.invoke(self, args);
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(this.method.getModifiers());
    }

    @Override
    public Class<?> getReturnType() {
        return this.method.getReturnType();
    }

    @Nullable
    @Override
    public Executable unwrap() {
        return this.method;
    }
}