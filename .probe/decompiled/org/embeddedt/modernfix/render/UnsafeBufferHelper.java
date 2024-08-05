package org.embeddedt.modernfix.render;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import org.embeddedt.modernfix.ModernFix;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.MemoryUtil.MemoryAllocator;
import sun.misc.Unsafe;

public class UnsafeBufferHelper {

    private static final MemoryAllocator ALLOCATOR = MemoryUtil.getAllocator(false);

    private static Unsafe UNSAFE = null;

    private static long ADDRESS = -1L;

    public static void init() {
    }

    public static void free(ByteBuffer buf) {
        if (UNSAFE != null && ADDRESS >= 0L) {
            long address = UNSAFE.getAndSetLong(buf, ADDRESS, 0L);
            if (address != 0L) {
                ALLOCATOR.free(address);
            }
        } else {
            ALLOCATOR.free(MemoryUtil.memAddress0(buf));
        }
    }

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
            Field addressField = MemoryUtil.class.getDeclaredField("ADDRESS");
            addressField.setAccessible(true);
            ADDRESS = addressField.getLong(null);
        } catch (Throwable var2) {
            ModernFix.LOGGER.error("Could load unsafe/buffer address", var2);
        }
    }
}