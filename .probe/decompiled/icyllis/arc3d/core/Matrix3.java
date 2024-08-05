package icyllis.arc3d.core;

import javax.annotation.Nonnull;
import org.lwjgl.system.MemoryUtil;

public class Matrix3 {

    public float m11;

    public float m12;

    public float m13;

    public float m21;

    public float m22;

    public float m23;

    public float m31;

    public float m32;

    public float m33;

    @Nonnull
    public static Matrix3 identity() {
        Matrix3 m = new Matrix3();
        m.m11 = m.m22 = m.m33 = 1.0F;
        return m;
    }

    public void setIdentity() {
        this.m11 = 1.0F;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 1.0F;
        this.m23 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public void store(long p) {
        MemoryUtil.memPutFloat(p, this.m11);
        MemoryUtil.memPutFloat(p + 4L, this.m12);
        MemoryUtil.memPutFloat(p + 8L, this.m13);
        MemoryUtil.memPutFloat(p + 12L, this.m21);
        MemoryUtil.memPutFloat(p + 16L, this.m22);
        MemoryUtil.memPutFloat(p + 20L, this.m23);
        MemoryUtil.memPutFloat(p + 24L, this.m31);
        MemoryUtil.memPutFloat(p + 28L, this.m32);
        MemoryUtil.memPutFloat(p + 32L, this.m33);
    }

    public void storeAligned(long p) {
        MemoryUtil.memPutFloat(p, this.m11);
        MemoryUtil.memPutFloat(p + 4L, this.m12);
        MemoryUtil.memPutFloat(p + 8L, this.m13);
        MemoryUtil.memPutFloat(p + 16L, this.m21);
        MemoryUtil.memPutFloat(p + 20L, this.m22);
        MemoryUtil.memPutFloat(p + 24L, this.m23);
        MemoryUtil.memPutFloat(p + 32L, this.m31);
        MemoryUtil.memPutFloat(p + 36L, this.m32);
        MemoryUtil.memPutFloat(p + 40L, this.m33);
    }
}