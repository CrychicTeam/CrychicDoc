package icyllis.arc3d.core;

import java.util.function.LongConsumer;
import javax.annotation.Nullable;
import org.lwjgl.system.NativeType;
import org.lwjgl.system.jni.JNINativeInterface;

public class PixelRef extends RefCnt {

    protected final int mWidth;

    protected final int mHeight;

    protected final Object mBase;

    protected final long mAddress;

    protected final int mRowStride;

    protected final LongConsumer mFreeFn;

    protected boolean mImmutable;

    public PixelRef(int width, int height, @Nullable Object base, @NativeType("void *") long address, int rowStride, @Nullable LongConsumer freeFn) {
        this.mWidth = width;
        this.mHeight = height;
        this.mBase = base;
        this.mAddress = address;
        this.mRowStride = rowStride;
        this.mFreeFn = freeFn;
    }

    @Override
    protected void deallocate() {
        if (this.mFreeFn != null) {
            this.mFreeFn.accept(this.mAddress);
        }
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    @Nullable
    public Object getBase() {
        return this.mBase;
    }

    public long getAddress() {
        return this.mAddress;
    }

    public int getRowStride() {
        return this.mRowStride;
    }

    public boolean isImmutable() {
        return this.mImmutable;
    }

    public void setImmutable() {
        this.mImmutable = true;
    }

    public String toString() {
        return "PixelRef{mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + ", mBase=" + this.mBase + ", mAddress=0x" + Long.toHexString(this.mAddress) + ", mRowStride=" + this.mRowStride + ", mImmutable=" + this.mImmutable + "}";
    }

    public static long getBaseElements(Object base) {
        Class<?> clazz = base.getClass();
        long elems;
        if (clazz == byte[].class) {
            elems = JNINativeInterface.nGetByteArrayElements((byte[]) base, 0L);
        } else if (clazz == short[].class) {
            elems = JNINativeInterface.nGetShortArrayElements((short[]) base, 0L);
        } else {
            if (clazz != int[].class) {
                throw new AssertionError(clazz);
            }
            elems = JNINativeInterface.nGetIntArrayElements((int[]) base, 0L);
        }
        return elems;
    }

    public static void releaseBaseElements(Object base, long elems, boolean write) {
        Class<?> clazz = base.getClass();
        int mode = write ? 0 : 2;
        if (clazz == byte[].class) {
            JNINativeInterface.nReleaseByteArrayElements((byte[]) base, elems, mode);
        } else if (clazz == short[].class) {
            JNINativeInterface.nReleaseShortArrayElements((short[]) base, elems, mode);
        } else {
            if (clazz != int[].class) {
                throw new AssertionError(clazz);
            }
            JNINativeInterface.nReleaseIntArrayElements((int[]) base, elems, mode);
        }
    }

    public static void getBaseRegion(Object base, int start, int len, long buf) {
        Class<?> clazz = base.getClass();
        if (clazz == byte[].class) {
            JNINativeInterface.nGetByteArrayRegion((byte[]) base, start, len, buf);
        } else if (clazz == short[].class) {
            JNINativeInterface.nGetShortArrayRegion((short[]) base, start, len, buf);
        } else {
            if (clazz != int[].class) {
                throw new AssertionError(clazz);
            }
            JNINativeInterface.nGetIntArrayRegion((int[]) base, start, len, buf);
        }
    }
}