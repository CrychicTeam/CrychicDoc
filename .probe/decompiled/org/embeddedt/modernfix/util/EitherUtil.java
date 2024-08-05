package org.embeddedt.modernfix.util;

import com.mojang.datafixers.util.Either;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public class EitherUtil {

    private static final Class<?> LEFT;

    private static final Class<?> RIGHT;

    private static final MethodHandle LEFT_VAL;

    private static final MethodHandle RIGHT_VAL;

    public static <L, R> L leftOrNull(Either<L, R> either) {
        if (either.getClass() == LEFT) {
            try {
                return (L) (Object) LEFT_VAL.invokeExact(either);
            } catch (Throwable var2) {
                throw new RuntimeException(var2);
            }
        } else {
            return null;
        }
    }

    public static <L, R> R rightOrNull(Either<L, R> either) {
        if (either.getClass() == RIGHT) {
            try {
                return (R) (Object) RIGHT_VAL.invokeExact(either);
            } catch (Throwable var2) {
                throw new RuntimeException(var2);
            }
        } else {
            return null;
        }
    }

    static {
        try {
            LEFT = Class.forName("com.mojang.datafixers.util.Either$Left");
            RIGHT = Class.forName("com.mojang.datafixers.util.Either$Right");
            Field lvalue = LEFT.getDeclaredField("value");
            lvalue.setAccessible(true);
            Field rvalue = RIGHT.getDeclaredField("value");
            rvalue.setAccessible(true);
            LEFT_VAL = MethodHandles.publicLookup().unreflectGetter(lvalue).asType(MethodType.methodType(Object.class, Either.class));
            RIGHT_VAL = MethodHandles.publicLookup().unreflectGetter(rvalue).asType(MethodType.methodType(Object.class, Either.class));
        } catch (ReflectiveOperationException var2) {
            throw new AssertionError("Failed to hook DFU Either", var2);
        }
    }
}