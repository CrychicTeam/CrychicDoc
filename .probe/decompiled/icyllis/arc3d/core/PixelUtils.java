package icyllis.arc3d.core;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.libc.LibCString;
import sun.misc.Unsafe;

public class PixelUtils {

    static final Unsafe UNSAFE = getUnsafe();

    public static final boolean NATIVE_BIG_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;

    private static Unsafe getUnsafe() {
        try {
            Field field = MemoryUtil.class.getDeclaredField("UNSAFE");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception var1) {
            throw new AssertionError("No MemoryUtil.UNSAFE", var1);
        }
    }

    public static void copyImage(long src, long srcRowBytes, long dst, long dstRowBytes, long minRowBytes, int rows) {
        if (srcRowBytes >= minRowBytes && dstRowBytes >= minRowBytes) {
            if (srcRowBytes == minRowBytes && dstRowBytes == minRowBytes) {
                LibCString.nmemcpy(dst, src, minRowBytes * (long) rows);
            } else {
                while (rows-- != 0) {
                    LibCString.nmemcpy(dst, src, minRowBytes);
                    src += srcRowBytes;
                    dst += dstRowBytes;
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static void setPixel8(Object base, long addr, byte value, int count) {
        long wideValue = (long) value << 8 | (long) value;
        wideValue |= wideValue << 16;
        wideValue |= wideValue << 32;
        while (count >= 8) {
            UNSAFE.putLong(base, addr, wideValue);
            addr += 8L;
            count -= 8;
        }
        while (count-- != 0) {
            UNSAFE.putByte(base, addr, value);
            addr++;
        }
    }

    public static void setPixel16(Object base, long addr, short value, int count) {
        if (NATIVE_BIG_ENDIAN) {
            value = Short.reverseBytes(value);
        }
        long wideValue = (long) value << 16 | (long) value;
        wideValue |= wideValue << 32;
        while (count >= 4) {
            UNSAFE.putLong(base, addr, wideValue);
            addr += 8L;
            count -= 4;
        }
        while (count-- != 0) {
            UNSAFE.putShort(base, addr, value);
            addr += 2L;
        }
    }

    public static void setPixel32(Object base, long addr, int value, int count) {
        if (NATIVE_BIG_ENDIAN) {
            value = Integer.reverseBytes(value);
        }
        long wideValue = (long) value << 32 | (long) value;
        while (count >= 2) {
            UNSAFE.putLong(base, addr, wideValue);
            addr += 8L;
            count -= 2;
        }
        if (count != 0) {
            assert count == 1;
            UNSAFE.putInt(base, addr, value);
        }
    }

    public static void setPixel64(Object base, long addr, long value, int count) {
        if (NATIVE_BIG_ENDIAN) {
            value = Long.reverseBytes(value);
        }
        while (count-- != 0) {
            UNSAFE.putLong(base, addr, value);
            addr += 8L;
        }
    }
}