package dev.latvian.mods.rhino.util;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;

@FunctionalInterface
public interface CustomJavaToJsWrapper {

    Scriptable convertJavaToJs(Context var1, Scriptable var2, Class<?> var3);
}