package dev.latvian.mods.rhino;

import java.lang.reflect.Executable;
import org.jetbrains.annotations.Nullable;

public interface WrappedExecutable {

    Object invoke(Context var1, Scriptable var2, Object var3, Object[] var4) throws Exception;

    default Object construct(Context cx, Scriptable scope, Object[] args) throws Exception {
        throw new UnsupportedOperationException();
    }

    default boolean isStatic() {
        return false;
    }

    default Class<?> getReturnType() {
        return void.class;
    }

    @Nullable
    default Executable unwrap() {
        return null;
    }
}