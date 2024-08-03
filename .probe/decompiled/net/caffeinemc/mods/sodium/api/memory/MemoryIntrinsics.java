package net.caffeinemc.mods.sodium.api.memory;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class MemoryIntrinsics {

    private static final Unsafe UNSAFE;

    public static void copyMemory(long src, long dst, int length) {
        UNSAFE.copyMemory(src, dst, (long) length);
    }

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException var1) {
            throw new RuntimeException("Couldn't obtain reference to sun.misc.Unsafe", var1);
        }
    }
}