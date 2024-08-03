package icyllis.arc3d.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.lwjgl.system.MemoryUtil;

public non-sealed class Matrix implements Matrixc, Cloneable {

    private static final int kAxisAligned_Mask = 16;

    private static final int kAxisAligned_Shift = 4;

    private static final int kOnlyPerspectiveValid_Mask = 64;

    private static final int kUnknown_Mask = 128;

    private static final Matrixc IDENTITY = new Matrix();

    protected float m11;

    protected float m12;

    protected float m14;

    protected float m21;

    protected float m22;

    protected float m24;

    protected float m41;

    protected float m42;

    protected float m44;

    private int mTypeMask;

    public Matrix() {
        this.m11 = this.m22 = this.m44 = 1.0F;
        this.mTypeMask = 16;
    }

    public Matrix(@Nonnull Matrixc m) {
        m.store(this);
    }

    public Matrix(float scaleX, float shearY, float persp0, float shearX, float scaleY, float persp1, float transX, float transY, float persp2) {
        this.set(scaleX, shearY, persp0, shearX, scaleY, persp1, transX, transY, persp2);
    }

    @Nonnull
    public static Matrixc identity() {
        return IDENTITY;
    }

    @Override
    public float m11() {
        return this.m11;
    }

    @Override
    public float m12() {
        return this.m12;
    }

    @Override
    public float m14() {
        return this.m14;
    }

    @Override
    public float m21() {
        return this.m21;
    }

    @Override
    public float m22() {
        return this.m22;
    }

    @Override
    public float m24() {
        return this.m24;
    }

    @Override
    public float m41() {
        return this.m41;
    }

    @Override
    public float m42() {
        return this.m42;
    }

    @Override
    public float m44() {
        return this.m44;
    }

    @Override
    public float getScaleX() {
        return this.m11;
    }

    @Override
    public float getScaleY() {
        return this.m22;
    }

    @Override
    public float getShearY() {
        return this.m12;
    }

    @Override
    public float getShearX() {
        return this.m21;
    }

    @Override
    public float getTranslateX() {
        return this.m41;
    }

    @Override
    public float getTranslateY() {
        return this.m42;
    }

    @Override
    public float getPerspX() {
        return this.m14;
    }

    @Override
    public float getPerspY() {
        return this.m24;
    }

    @Override
    public int getType() {
        if ((this.mTypeMask & 128) != 0) {
            this.mTypeMask = this.computeTypeMask();
        }
        return this.mTypeMask & 15;
    }

    @Override
    public boolean isIdentity() {
        return this.getType() == 0;
    }

    @Override
    public boolean isScaleTranslate() {
        return (this.getType() & -4) == 0;
    }

    @Override
    public boolean isTranslate() {
        return (this.getType() & -2) == 0;
    }

    @Override
    public boolean isAxisAligned() {
        if ((this.mTypeMask & 128) != 0) {
            this.mTypeMask = this.computeTypeMask();
        }
        return (this.mTypeMask & 16) != 0;
    }

    @Override
    public boolean preservesRightAngles() {
        int mask = this.getType();
        if (mask <= 1) {
            return true;
        } else if ((mask & 8) != 0) {
            return false;
        } else {
            assert (mask & 6) != 0;
            float mx = this.m11;
            float my = this.m22;
            float sx = this.m21;
            float sy = this.m12;
            float det22 = mx * my - sx * sy;
            return MathUtil.isApproxZero(det22) ? false : MathUtil.isApproxZero(mx * sx + sy * my);
        }
    }

    @Override
    public boolean hasPerspective() {
        if ((this.mTypeMask & 128) != 0 && (this.mTypeMask & 64) == 0) {
            this.mTypeMask = this.computePerspectiveTypeMask();
        }
        return (this.mTypeMask & 8) != 0;
    }

    @Override
    public boolean isSimilarity() {
        int mask = this.getType();
        if (mask <= 1) {
            return true;
        } else if ((mask & 8) != 0) {
            return false;
        } else {
            float mx = this.m11;
            float my = this.m22;
            if ((mask & 4) == 0) {
                return !MathUtil.isApproxZero(mx) && MathUtil.isApproxEqual(Math.abs(mx), Math.abs(my));
            } else {
                float sx = this.m21;
                float sy = this.m12;
                float det22 = mx * my - sx * sy;
                return MathUtil.isApproxZero(det22) ? false : MathUtil.isApproxEqual(mx, my) && MathUtil.isApproxEqual(sx, -sy) || MathUtil.isApproxEqual(mx, -my) && MathUtil.isApproxEqual(sx, sy);
            }
        }
    }

    public void preConcat(@Nonnull Matrixc lhs) {
        int bMask = this.getType();
        if (bMask == 0) {
            this.set(lhs);
        } else {
            int aMask = lhs.getType();
            if (aMask != 0) {
                if (((aMask | bMask) & 12) == 0) {
                    this.setScaleTranslate(lhs.m11() * this.m11, lhs.m22() * this.m22, lhs.m41() * this.m11 + this.m41, lhs.m42() * this.m22 + this.m42);
                } else {
                    float f11;
                    float f12;
                    float f14;
                    float f21;
                    float f22;
                    float f24;
                    float f41;
                    float f42;
                    float f44;
                    if (((aMask | bMask) & 8) == 0) {
                        f11 = lhs.m11() * this.m11 + lhs.m12() * this.m21;
                        f12 = lhs.m11() * this.m12 + lhs.m12() * this.m22;
                        f14 = 0.0F;
                        f21 = lhs.m21() * this.m11 + lhs.m22() * this.m21;
                        f22 = lhs.m21() * this.m12 + lhs.m22() * this.m22;
                        f24 = 0.0F;
                        f41 = lhs.m41() * this.m11 + lhs.m42() * this.m21 + this.m41;
                        f42 = lhs.m41() * this.m12 + lhs.m42() * this.m22 + this.m42;
                        f44 = 1.0F;
                        this.mTypeMask = 192;
                    } else {
                        f11 = lhs.m11() * this.m11 + lhs.m12() * this.m21 + lhs.m14() * this.m41;
                        f12 = lhs.m11() * this.m12 + lhs.m12() * this.m22 + lhs.m14() * this.m42;
                        f14 = lhs.m11() * this.m14 + lhs.m12() * this.m24 + lhs.m14() * this.m44;
                        f21 = lhs.m21() * this.m11 + lhs.m22() * this.m21 + lhs.m24() * this.m41;
                        f22 = lhs.m21() * this.m12 + lhs.m22() * this.m22 + lhs.m24() * this.m42;
                        f24 = lhs.m21() * this.m14 + lhs.m22() * this.m24 + lhs.m24() * this.m44;
                        f41 = lhs.m41() * this.m11 + lhs.m42() * this.m21 + lhs.m44() * this.m41;
                        f42 = lhs.m41() * this.m12 + lhs.m42() * this.m22 + lhs.m44() * this.m42;
                        f44 = lhs.m41() * this.m14 + lhs.m42() * this.m24 + lhs.m44() * this.m44;
                        this.mTypeMask = 128;
                    }
                    this.m11 = f11;
                    this.m12 = f12;
                    this.m14 = f14;
                    this.m21 = f21;
                    this.m22 = f22;
                    this.m24 = f24;
                    this.m41 = f41;
                    this.m42 = f42;
                    this.m44 = f44;
                }
            }
        }
    }

    public void postConcat(@Nonnull Matrixc rhs) {
        int aMask = this.getType();
        if (aMask == 0) {
            this.set(rhs);
        } else {
            int bMask = rhs.getType();
            if (bMask != 0) {
                if (((aMask | bMask) & 12) == 0) {
                    this.setScaleTranslate(this.m11 * rhs.m11(), this.m22 * rhs.m22(), this.m41 * rhs.m11() + rhs.m41(), this.m42 * rhs.m22() + rhs.m42());
                } else {
                    float f11;
                    float f12;
                    float f14;
                    float f21;
                    float f22;
                    float f24;
                    float f41;
                    float f42;
                    float f44;
                    if (((aMask | bMask) & 8) == 0) {
                        f11 = this.m11 * rhs.m11() + this.m12 * rhs.m21();
                        f12 = this.m11 * rhs.m12() + this.m12 * rhs.m22();
                        f14 = 0.0F;
                        f21 = this.m21 * rhs.m11() + this.m22 * rhs.m21();
                        f22 = this.m21 * rhs.m12() + this.m22 * rhs.m22();
                        f24 = 0.0F;
                        f41 = this.m41 * rhs.m11() + this.m42 * rhs.m21() + rhs.m41();
                        f42 = this.m41 * rhs.m12() + this.m42 * rhs.m22() + rhs.m42();
                        f44 = 1.0F;
                        this.mTypeMask = 192;
                    } else {
                        f11 = this.m11 * rhs.m11() + this.m12 * rhs.m21() + this.m14 * rhs.m41();
                        f12 = this.m11 * rhs.m12() + this.m12 * rhs.m22() + this.m14 * rhs.m42();
                        f14 = this.m11 * rhs.m14() + this.m12 * rhs.m24() + this.m14 * rhs.m44();
                        f21 = this.m21 * rhs.m11() + this.m22 * rhs.m21() + this.m24 * rhs.m41();
                        f22 = this.m21 * rhs.m12() + this.m22 * rhs.m22() + this.m24 * rhs.m42();
                        f24 = this.m21 * rhs.m14() + this.m22 * rhs.m24() + this.m24 * rhs.m44();
                        f41 = this.m41 * rhs.m11() + this.m42 * rhs.m21() + this.m44 * rhs.m41();
                        f42 = this.m41 * rhs.m12() + this.m42 * rhs.m22() + this.m44 * rhs.m42();
                        f44 = this.m41 * rhs.m14() + this.m42 * rhs.m24() + this.m44 * rhs.m44();
                        this.mTypeMask = 128;
                    }
                    this.m11 = f11;
                    this.m12 = f12;
                    this.m14 = f14;
                    this.m21 = f21;
                    this.m22 = f22;
                    this.m24 = f24;
                    this.m41 = f41;
                    this.m42 = f42;
                    this.m44 = f44;
                }
            }
        }
    }

    public void setIdentity() {
        this.m11 = 1.0F;
        this.m12 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 1.0F;
        this.m24 = 0.0F;
        this.m41 = 0.0F;
        this.m42 = 0.0F;
        this.m44 = 1.0F;
        this.mTypeMask = 16;
    }

    public void setScaleTranslate(float sx, float sy, float tx, float ty) {
        this.m11 = sx;
        this.m12 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = sy;
        this.m24 = 0.0F;
        this.m41 = tx;
        this.m42 = ty;
        this.m44 = 1.0F;
        int mask = 0;
        if (sx != 1.0F || sy != 1.0F) {
            mask |= 2;
        }
        if (tx != 0.0F || ty != 0.0F) {
            mask |= 1;
        }
        if (sx != 0.0F && sy != 0.0F) {
            mask |= 16;
        }
        this.mTypeMask = mask;
    }

    public void set(@Nonnull Matrixc m) {
        m.store(this);
    }

    public void set(float scaleX, float shearY, float persp0, float shearX, float scaleY, float persp1, float transX, float transY, float persp2) {
        this.m11 = scaleX;
        this.m12 = shearY;
        this.m14 = persp0;
        this.m21 = shearX;
        this.m22 = scaleY;
        this.m24 = persp1;
        this.m41 = transX;
        this.m42 = transY;
        this.m44 = persp2;
        this.mTypeMask = 128;
    }

    public void set(@Nonnull float[] a) {
        this.m11 = a[0];
        this.m12 = a[1];
        this.m14 = a[2];
        this.m21 = a[3];
        this.m22 = a[4];
        this.m24 = a[5];
        this.m41 = a[6];
        this.m42 = a[7];
        this.m44 = a[8];
        this.mTypeMask = 128;
    }

    public void set(@Nonnull float[] a, int offset) {
        this.m11 = a[offset];
        this.m12 = a[offset + 1];
        this.m14 = a[offset + 2];
        this.m21 = a[offset + 3];
        this.m22 = a[offset + 4];
        this.m24 = a[offset + 5];
        this.m41 = a[offset + 6];
        this.m42 = a[offset + 7];
        this.m44 = a[offset + 8];
        this.mTypeMask = 128;
    }

    public void set(@Nonnull ByteBuffer a) {
        int offset = a.position();
        this.m11 = a.getFloat(offset);
        this.m12 = a.getFloat(offset + 4);
        this.m14 = a.getFloat(offset + 8);
        this.m21 = a.getFloat(offset + 12);
        this.m22 = a.getFloat(offset + 16);
        this.m24 = a.getFloat(offset + 20);
        this.m41 = a.getFloat(offset + 24);
        this.m42 = a.getFloat(offset + 28);
        this.m44 = a.getFloat(offset + 32);
        this.mTypeMask = 128;
    }

    public void set(@Nonnull FloatBuffer a) {
        int offset = a.position();
        this.m11 = a.get(offset);
        this.m12 = a.get(offset + 1);
        this.m14 = a.get(offset + 2);
        this.m21 = a.get(offset + 3);
        this.m22 = a.get(offset + 4);
        this.m24 = a.get(offset + 5);
        this.m41 = a.get(offset + 6);
        this.m42 = a.get(offset + 7);
        this.m44 = a.get(offset + 8);
        this.mTypeMask = 128;
    }

    public void set(long p) {
        this.m11 = MemoryUtil.memGetFloat(p);
        this.m12 = MemoryUtil.memGetFloat(p + 4L);
        this.m14 = MemoryUtil.memGetFloat(p + 8L);
        this.m21 = MemoryUtil.memGetFloat(p + 12L);
        this.m22 = MemoryUtil.memGetFloat(p + 16L);
        this.m24 = MemoryUtil.memGetFloat(p + 20L);
        this.m41 = MemoryUtil.memGetFloat(p + 24L);
        this.m42 = MemoryUtil.memGetFloat(p + 28L);
        this.m44 = MemoryUtil.memGetFloat(p + 32L);
        this.mTypeMask = 128;
    }

    @Override
    public void store(@Nonnull Matrix dst) {
        dst.m11 = this.m11;
        dst.m12 = this.m12;
        dst.m14 = this.m14;
        dst.m21 = this.m21;
        dst.m22 = this.m22;
        dst.m24 = this.m24;
        dst.m41 = this.m41;
        dst.m42 = this.m42;
        dst.m44 = this.m44;
        dst.mTypeMask = this.mTypeMask;
    }

    @Override
    public void store(@Nonnull float[] a) {
        a[0] = this.m11;
        a[1] = this.m12;
        a[2] = this.m14;
        a[3] = this.m21;
        a[4] = this.m22;
        a[5] = this.m24;
        a[6] = this.m41;
        a[7] = this.m42;
        a[8] = this.m44;
    }

    @Override
    public void store(@Nonnull float[] a, int offset) {
        a[offset] = this.m11;
        a[offset + 1] = this.m12;
        a[offset + 2] = this.m14;
        a[offset + 3] = this.m21;
        a[offset + 4] = this.m22;
        a[offset + 5] = this.m24;
        a[offset + 6] = this.m41;
        a[offset + 7] = this.m42;
        a[offset + 8] = this.m44;
    }

    @Override
    public void store(@Nonnull ByteBuffer a) {
        int offset = a.position();
        a.putFloat(offset, this.m11);
        a.putFloat(offset + 4, this.m12);
        a.putFloat(offset + 8, this.m14);
        a.putFloat(offset + 12, this.m21);
        a.putFloat(offset + 16, this.m22);
        a.putFloat(offset + 20, this.m24);
        a.putFloat(offset + 24, this.m41);
        a.putFloat(offset + 28, this.m42);
        a.putFloat(offset + 32, this.m44);
    }

    @Override
    public void storeAligned(@Nonnull ByteBuffer a) {
        int offset = a.position();
        a.putFloat(offset, this.m11);
        a.putFloat(offset + 4, this.m12);
        a.putFloat(offset + 8, this.m14);
        a.putFloat(offset + 16, this.m21);
        a.putFloat(offset + 20, this.m22);
        a.putFloat(offset + 24, this.m24);
        a.putFloat(offset + 32, this.m41);
        a.putFloat(offset + 36, this.m42);
        a.putFloat(offset + 40, this.m44);
    }

    @Override
    public void store(@Nonnull FloatBuffer a) {
        int offset = a.position();
        a.put(offset, this.m11);
        a.put(offset + 1, this.m12);
        a.put(offset + 2, this.m14);
        a.put(offset + 3, this.m21);
        a.put(offset + 4, this.m22);
        a.put(offset + 5, this.m24);
        a.put(offset + 6, this.m41);
        a.put(offset + 7, this.m42);
        a.put(offset + 8, this.m44);
    }

    @Override
    public void storeAligned(@Nonnull FloatBuffer a) {
        int offset = a.position();
        a.put(offset, this.m11);
        a.put(offset + 1, this.m12);
        a.put(offset + 2, this.m14);
        a.put(offset + 4, this.m21);
        a.put(offset + 5, this.m22);
        a.put(offset + 6, this.m24);
        a.put(offset + 8, this.m41);
        a.put(offset + 9, this.m42);
        a.put(offset + 10, this.m44);
    }

    @Override
    public void store(long p) {
        MemoryUtil.memPutFloat(p, this.m11);
        MemoryUtil.memPutFloat(p + 4L, this.m12);
        MemoryUtil.memPutFloat(p + 8L, this.m14);
        MemoryUtil.memPutFloat(p + 12L, this.m21);
        MemoryUtil.memPutFloat(p + 16L, this.m22);
        MemoryUtil.memPutFloat(p + 20L, this.m24);
        MemoryUtil.memPutFloat(p + 24L, this.m41);
        MemoryUtil.memPutFloat(p + 28L, this.m42);
        MemoryUtil.memPutFloat(p + 32L, this.m44);
    }

    @Override
    public void storeAligned(long p) {
        MemoryUtil.memPutFloat(p, this.m11);
        MemoryUtil.memPutFloat(p + 4L, this.m12);
        MemoryUtil.memPutFloat(p + 8L, this.m14);
        MemoryUtil.memPutFloat(p + 16L, this.m21);
        MemoryUtil.memPutFloat(p + 20L, this.m22);
        MemoryUtil.memPutFloat(p + 24L, this.m24);
        MemoryUtil.memPutFloat(p + 32L, this.m41);
        MemoryUtil.memPutFloat(p + 36L, this.m42);
        MemoryUtil.memPutFloat(p + 40L, this.m44);
    }

    public float determinant() {
        return this.hasPerspective() ? (this.m11 * this.m22 - this.m12 * this.m21) * this.m44 + (this.m14 * this.m21 - this.m11 * this.m24) * this.m42 + (this.m12 * this.m24 - this.m14 * this.m22) * this.m41 : this.m11 * this.m22 - this.m12 * this.m21;
    }

    public float trace() {
        return this.m11 + this.m22 + this.m44;
    }

    @CheckReturnValue
    public boolean invert() {
        return this.invert(this);
    }

    @CheckReturnValue
    @Override
    public boolean invert(@Nullable Matrix dest) {
        int mask = this.getType();
        if (mask == 0) {
            if (dest != null) {
                dest.setIdentity();
            }
            return true;
        } else if ((mask & -4) == 0) {
            return this.invertScaleTranslate(mask, dest);
        } else {
            return (mask & 8) != 0 ? this.invertPerspective(dest) : this.invertAffine(dest);
        }
    }

    private boolean invertScaleTranslate(int mask, Matrix dest) {
        if ((mask & 2) != 0) {
            float invX = 1.0F / this.m11;
            float invY = 1.0F / this.m22;
            if (!Float.isFinite(invX) || !Float.isFinite(invY)) {
                return false;
            }
            float f41 = (float) ((double) (-this.m41) / (double) this.m11);
            float f42 = (float) ((double) (-this.m42) / (double) this.m22);
            if (!Float.isFinite(f41) || !Float.isFinite(f42)) {
                return false;
            }
            if (dest != null) {
                dest.m11 = invX;
                dest.m12 = 0.0F;
                dest.m14 = 0.0F;
                dest.m21 = 0.0F;
                dest.m22 = invY;
                dest.m24 = 0.0F;
                dest.m41 = f41;
                dest.m42 = f42;
                dest.m44 = 1.0F;
                dest.mTypeMask = mask | 16;
            }
        } else {
            if (!Float.isFinite(this.m41) || !Float.isFinite(this.m42)) {
                return false;
            }
            if (dest != null) {
                dest.setTranslate(-this.m41, -this.m42);
            }
        }
        return true;
    }

    private boolean invertPerspective(Matrix dest) {
        double a = (double) (this.m11 * this.m22 - this.m12 * this.m21);
        double b = (double) (this.m14 * this.m21 - this.m11 * this.m24);
        double c = (double) (this.m12 * this.m24 - this.m14 * this.m22);
        double det = a * (double) this.m44 + b * (double) this.m42 + c * (double) this.m41;
        if (det == 0.0) {
            return false;
        } else {
            det = 1.0 / det;
            float f11 = (float) ((double) (this.m22 * this.m44 - this.m42 * this.m24) * det);
            float f12 = (float) ((double) (this.m42 * this.m14 - this.m12 * this.m44) * det);
            float f21 = (float) ((double) (this.m41 * this.m24 - this.m21 * this.m44) * det);
            float f22 = (float) ((double) (this.m11 * this.m44 - this.m41 * this.m14) * det);
            float f41 = (float) ((double) (this.m21 * this.m42 - this.m41 * this.m22) * det);
            float f42 = (float) ((double) (this.m41 * this.m12 - this.m11 * this.m42) * det);
            float f14 = (float) (c * det);
            float f24 = (float) (b * det);
            float f44 = (float) (a * det);
            if (0.0F * f11 * f12 * f14 * f21 * f22 * f24 * f41 * f42 * f44 != 0.0F) {
                return false;
            } else {
                if (dest != null) {
                    dest.m11 = f11;
                    dest.m12 = f12;
                    dest.m14 = f14;
                    dest.m21 = f21;
                    dest.m22 = f22;
                    dest.m24 = f24;
                    dest.m41 = f41;
                    dest.m42 = f42;
                    dest.m44 = f44;
                    dest.mTypeMask = this.mTypeMask;
                }
                return true;
            }
        }
    }

    private boolean invertAffine(Matrix dest) {
        double det = (double) (this.m11 * this.m22 - this.m12 * this.m21);
        if (det == 0.0) {
            return false;
        } else {
            det = 1.0 / det;
            float f11 = (float) ((double) this.m22 * det);
            float f12 = (float) ((double) (-this.m12) * det);
            float f21 = (float) ((double) (-this.m21) * det);
            float f22 = (float) ((double) this.m11 * det);
            float f41 = (float) ((double) (this.m21 * this.m42 - this.m41 * this.m22) * det);
            float f42 = (float) ((double) (this.m41 * this.m12 - this.m11 * this.m42) * det);
            if (0.0F * f11 * f12 * f21 * f22 * f41 * f42 != 0.0F) {
                return false;
            } else {
                if (dest != null) {
                    dest.m11 = f11;
                    dest.m12 = f12;
                    dest.m14 = 0.0F;
                    dest.m21 = f21;
                    dest.m22 = f22;
                    dest.m24 = 0.0F;
                    dest.m41 = f41;
                    dest.m42 = f42;
                    dest.m44 = 1.0F;
                    dest.mTypeMask = this.mTypeMask;
                }
                return true;
            }
        }
    }

    public void preTranslate(float dx, float dy) {
        int mask = this.getType();
        if ((mask & 8) != 0) {
            this.m41 = this.m41 + dx * this.m11 + dy * this.m21;
            this.m42 = this.m42 + dx * this.m12 + dy * this.m22;
            this.m44 = this.m44 + dx * this.m14 + dy * this.m24;
            this.mTypeMask = 128;
        } else {
            if (mask <= 1) {
                this.m41 += dx;
                this.m42 += dy;
            } else {
                this.m41 = this.m41 + dx * this.m11 + dy * this.m21;
                this.m42 = this.m42 + dx * this.m12 + dy * this.m22;
            }
            if (this.m41 == 0.0F && this.m42 == 0.0F) {
                this.mTypeMask &= -2;
            } else {
                this.mTypeMask |= 1;
            }
        }
    }

    public void postTranslate(float dx, float dy) {
        int mask = this.getType();
        if ((mask & 8) != 0) {
            this.m11 = this.m11 + dx * this.m14;
            this.m12 = this.m12 + dy * this.m14;
            this.m21 = this.m21 + dx * this.m24;
            this.m22 = this.m22 + dy * this.m24;
            this.m41 = this.m41 + dx * this.m44;
            this.m42 = this.m42 + dy * this.m44;
            this.mTypeMask = 128;
        } else {
            this.m41 += dx;
            this.m42 += dy;
            if (this.m41 == 0.0F && this.m42 == 0.0F) {
                this.mTypeMask &= -2;
            } else {
                this.mTypeMask |= 1;
            }
        }
    }

    public void setTranslate(float dx, float dy) {
        this.m11 = 1.0F;
        this.m12 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 1.0F;
        this.m24 = 0.0F;
        this.m41 = dx;
        this.m42 = dy;
        this.m44 = 1.0F;
        if (dx == 0.0F && dy == 0.0F) {
            this.mTypeMask = 16;
        } else {
            this.mTypeMask = 17;
        }
    }

    public void preScale(float sx, float sy) {
        if (sx != 1.0F || sy != 1.0F) {
            int mask = this.getType();
            if (mask == 0) {
                this.setScale(sx, sy);
            } else if ((mask & 12) == 0) {
                this.setScaleTranslate(sx * this.m11, sy * this.m22, this.m41, this.m42);
            } else {
                this.m11 *= sx;
                this.m12 *= sx;
                this.m21 *= sy;
                this.m22 *= sy;
                if ((mask & 8) == 0) {
                    this.mTypeMask = 192;
                } else {
                    this.m14 *= sx;
                    this.m24 *= sy;
                    this.mTypeMask = 128;
                }
            }
        }
    }

    public void postScale(float sx, float sy) {
        if (sx != 1.0F || sy != 1.0F) {
            int mask = this.getType();
            if (mask == 0) {
                this.setScale(sx, sy);
            } else if ((mask & 12) == 0) {
                this.setScaleTranslate(this.m11 * sx, this.m22 * sy, this.m41 * sx, this.m42 * sy);
            } else {
                this.m11 *= sx;
                this.m21 *= sx;
                this.m41 *= sx;
                this.m12 *= sy;
                this.m22 *= sy;
                this.m42 *= sy;
                if ((mask & 8) == 0) {
                    this.mTypeMask = 192;
                } else {
                    this.mTypeMask = 128;
                }
            }
        }
    }

    public void setScale(float sx, float sy) {
        this.m11 = sx;
        this.m12 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = sy;
        this.m24 = 0.0F;
        this.m41 = 0.0F;
        this.m42 = 0.0F;
        this.m44 = 1.0F;
        if (sx == 1.0F && sy == 1.0F) {
            this.mTypeMask = 16;
        } else {
            this.mTypeMask = 2 | (sx != 0.0F && sy != 0.0F ? 16 : 0);
        }
    }

    public void setScale(float sx, float sy, float px, float py) {
        if (sx == 1.0F && sy == 1.0F) {
            this.setIdentity();
        } else {
            this.setScaleTranslate(sx, sy, px - sx * px, py - sy * py);
        }
    }

    public void preRotate(float angle) {
        if (angle != 0.0F) {
            int mask = this.getType();
            if (mask == 0) {
                this.setRotate(angle);
            } else {
                double s = Math.sin((double) angle);
                double c = Math.cos((double) angle);
                double f11 = c * (double) this.m11 + s * (double) this.m21;
                double f12 = c * (double) this.m12 + s * (double) this.m22;
                double f14 = c * (double) this.m14 + s * (double) this.m24;
                this.m21 = (float) (c * (double) this.m21 - s * (double) this.m11);
                this.m22 = (float) (c * (double) this.m22 - s * (double) this.m12);
                this.m24 = (float) (c * (double) this.m24 - s * (double) this.m14);
                this.m11 = (float) f11;
                this.m12 = (float) f12;
                this.m14 = (float) f14;
                if ((mask & 8) == 0) {
                    this.mTypeMask = 192;
                } else {
                    this.mTypeMask = 128;
                }
            }
        }
    }

    public void postRotate(float angle) {
        if (angle != 0.0F) {
            int mask = this.getType();
            if (mask == 0) {
                this.setRotate(angle);
            } else {
                double s = Math.sin((double) angle);
                double c = Math.cos((double) angle);
                double f12 = c * (double) this.m12 + s * (double) this.m11;
                double f22 = c * (double) this.m22 + s * (double) this.m21;
                double f42 = c * (double) this.m42 + s * (double) this.m41;
                this.m11 = (float) (c * (double) this.m11 - s * (double) this.m12);
                this.m21 = (float) (c * (double) this.m21 - s * (double) this.m22);
                this.m41 = (float) (c * (double) this.m41 - s * (double) this.m42);
                this.m12 = (float) f12;
                this.m22 = (float) f22;
                this.m42 = (float) f42;
                if ((mask & 8) == 0) {
                    this.mTypeMask = 192;
                } else {
                    this.mTypeMask = 128;
                }
            }
        }
    }

    public void setRotate(float angle) {
        if (angle == 0.0F) {
            this.setIdentity();
        } else {
            float s = (float) Math.sin((double) angle);
            float c = (float) Math.cos((double) angle);
            this.setSinCos(MathUtil.isApproxZero(s) ? 0.0F : s, MathUtil.isApproxZero(c) ? 0.0F : c);
        }
    }

    public void setRotate(float angle, float px, float py) {
        if (angle == 0.0F) {
            this.setIdentity();
        } else {
            float s = (float) Math.sin((double) angle);
            float c = (float) Math.cos((double) angle);
            this.setSinCos(MathUtil.isApproxZero(s) ? 0.0F : s, MathUtil.isApproxZero(c) ? 0.0F : c, px, py);
        }
    }

    public void setSinCos(float sin, float cos) {
        this.m11 = cos;
        this.m12 = sin;
        this.m14 = 0.0F;
        this.m21 = -sin;
        this.m22 = cos;
        this.m24 = 0.0F;
        this.m41 = 0.0F;
        this.m42 = 0.0F;
        this.m44 = 1.0F;
        this.mTypeMask = 192;
    }

    public void setSinCos(float sin, float cos, float px, float py) {
        double omc = (double) (1.0F - cos);
        this.m11 = cos;
        this.m12 = sin;
        this.m14 = 0.0F;
        this.m21 = -sin;
        this.m22 = cos;
        this.m24 = 0.0F;
        this.m41 = (float) (omc * (double) px + (double) (sin * py));
        this.m42 = (float) (omc * (double) py - (double) (sin * px));
        this.m44 = 1.0F;
        this.mTypeMask = 192;
    }

    public void preShear(float sx, float sy) {
        int mask = this.getType();
        if (mask == 0) {
            this.setShear(sx, sy);
        } else {
            float f11;
            float f12;
            float f14;
            float f21;
            float f22;
            float f24;
            if ((mask & 8) == 0) {
                f11 = this.m11 + sy * this.m21;
                f12 = this.m12 + sy * this.m22;
                f14 = 0.0F;
                f21 = sx * this.m11 + this.m21;
                f22 = sx * this.m12 + this.m22;
                f24 = 0.0F;
                this.mTypeMask = 192;
            } else {
                f11 = this.m11 + sy * this.m21;
                f12 = this.m12 + sy * this.m22;
                f14 = this.m14 + sy * this.m24;
                f21 = sx * this.m11 + this.m21;
                f22 = sx * this.m12 + this.m22;
                f24 = sx * this.m14 + this.m24;
                this.mTypeMask = 128;
            }
            this.m11 = f11;
            this.m12 = f12;
            this.m14 = f14;
            this.m21 = f21;
            this.m22 = f22;
            this.m24 = f24;
        }
    }

    public void postShear(float sx, float sy) {
        int mask = this.getType();
        if (mask == 0) {
            this.setShear(sx, sy);
        } else {
            float f11;
            float f12;
            float f21;
            float f22;
            float f41;
            float f42;
            if ((mask & 8) == 0) {
                f11 = this.m11 + this.m12 * sx;
                f12 = this.m11 * sy + this.m12;
                f21 = this.m21 + this.m22 * sx;
                f22 = this.m21 * sy + this.m22;
                f41 = this.m41 + this.m42 * sx;
                f42 = this.m41 * sy + this.m42;
                this.mTypeMask = 192;
            } else {
                f11 = this.m11 + this.m12 * sx;
                f12 = this.m11 * sy + this.m12;
                f21 = this.m21 + this.m22 * sx;
                f22 = this.m21 * sy + this.m22;
                f41 = this.m41 + this.m42 * sx;
                f42 = this.m41 * sy + this.m42;
                this.mTypeMask = 128;
            }
            this.m11 = f11;
            this.m12 = f12;
            this.m21 = f21;
            this.m22 = f22;
            this.m41 = f41;
            this.m42 = f42;
        }
    }

    public void setShear(float sx, float sy) {
        this.m11 = 1.0F;
        this.m12 = sy;
        this.m14 = 0.0F;
        this.m21 = sx;
        this.m22 = 1.0F;
        this.m24 = 0.0F;
        this.m41 = 0.0F;
        this.m42 = 0.0F;
        this.m44 = 1.0F;
        this.mTypeMask = 192;
    }

    public void setShear(float sx, float sy, float px, float py) {
        this.m11 = 1.0F;
        this.m12 = sy;
        this.m14 = 0.0F;
        this.m21 = sx;
        this.m22 = 1.0F;
        this.m24 = 0.0F;
        this.m41 = -sx * py;
        this.m42 = -sy * px;
        this.m44 = 1.0F;
        this.mTypeMask = 192;
    }

    @Override
    public boolean mapRect(@Nonnull Rect2fc src, @Nonnull Rect2f dst) {
        int typeMask = this.getType();
        float left = src.left();
        float top = src.top();
        float right = src.right();
        float bottom = src.bottom();
        if (typeMask <= 1) {
            dst.mLeft = left + this.m41;
            dst.mTop = top + this.m42;
            dst.mRight = right + this.m41;
            dst.mBottom = bottom + this.m42;
            return true;
        } else if ((typeMask & -4) == 0) {
            dst.mLeft = left * this.m11 + this.m41;
            dst.mTop = top * this.m22 + this.m42;
            dst.mRight = right * this.m11 + this.m41;
            dst.mBottom = bottom * this.m22 + this.m42;
            return true;
        } else {
            float x1 = this.m11 * left + this.m21 * top + this.m41;
            float y1 = this.m12 * left + this.m22 * top + this.m42;
            float x2 = this.m11 * right + this.m21 * top + this.m41;
            float y2 = this.m12 * right + this.m22 * top + this.m42;
            float x3 = this.m11 * left + this.m21 * bottom + this.m41;
            float y3 = this.m12 * left + this.m22 * bottom + this.m42;
            float x4 = this.m11 * right + this.m21 * bottom + this.m41;
            float y4 = this.m12 * right + this.m22 * bottom + this.m42;
            if ((typeMask & 8) != 0) {
                float w = 1.0F / (this.m14 * left + this.m24 * top + this.m44);
                x1 *= w;
                y1 *= w;
                w = 1.0F / (this.m14 * right + this.m24 * top + this.m44);
                x2 *= w;
                y2 *= w;
                w = 1.0F / (this.m14 * left + this.m24 * bottom + this.m44);
                x3 *= w;
                y3 *= w;
                w = 1.0F / (this.m14 * right + this.m24 * bottom + this.m44);
                x4 *= w;
                y4 *= w;
            }
            dst.mLeft = MathUtil.min(x1, x2, x3, x4);
            dst.mTop = MathUtil.min(y1, y2, y3, y4);
            dst.mRight = MathUtil.max(x1, x2, x3, x4);
            dst.mBottom = MathUtil.max(y1, y2, y3, y4);
            return (typeMask & 16) != 0;
        }
    }

    @Override
    public void mapRect(float left, float top, float right, float bottom, @Nonnull Rect2i dst) {
        int typeMask = this.getType();
        if (typeMask <= 1) {
            dst.mLeft = Math.round(left + this.m41);
            dst.mTop = Math.round(top + this.m42);
            dst.mRight = Math.round(right + this.m41);
            dst.mBottom = Math.round(bottom + this.m42);
        } else if ((typeMask & -4) == 0) {
            dst.mLeft = Math.round(left * this.m11 + this.m41);
            dst.mTop = Math.round(top * this.m22 + this.m42);
            dst.mRight = Math.round(right * this.m11 + this.m41);
            dst.mBottom = Math.round(bottom * this.m22 + this.m42);
        } else {
            float x1 = this.m11 * left + this.m21 * top + this.m41;
            float y1 = this.m12 * left + this.m22 * top + this.m42;
            float x2 = this.m11 * right + this.m21 * top + this.m41;
            float y2 = this.m12 * right + this.m22 * top + this.m42;
            float x3 = this.m11 * left + this.m21 * bottom + this.m41;
            float y3 = this.m12 * left + this.m22 * bottom + this.m42;
            float x4 = this.m11 * right + this.m21 * bottom + this.m41;
            float y4 = this.m12 * right + this.m22 * bottom + this.m42;
            if ((typeMask & 8) != 0) {
                float w = 1.0F / (this.m14 * left + this.m24 * top + this.m44);
                x1 *= w;
                y1 *= w;
                w = 1.0F / (this.m14 * right + this.m24 * top + this.m44);
                x2 *= w;
                y2 *= w;
                w = 1.0F / (this.m14 * left + this.m24 * bottom + this.m44);
                x3 *= w;
                y3 *= w;
                w = 1.0F / (this.m14 * right + this.m24 * bottom + this.m44);
                x4 *= w;
                y4 *= w;
            }
            dst.mLeft = Math.round(MathUtil.min(x1, x2, x3, x4));
            dst.mTop = Math.round(MathUtil.min(y1, y2, y3, y4));
            dst.mRight = Math.round(MathUtil.max(x1, x2, x3, x4));
            dst.mBottom = Math.round(MathUtil.max(y1, y2, y3, y4));
        }
    }

    @Override
    public void mapRectOut(float left, float top, float right, float bottom, @Nonnull Rect2i dst) {
        int typeMask = this.getType();
        if (typeMask <= 1) {
            dst.mLeft = (int) Math.floor((double) (left + this.m41));
            dst.mTop = (int) Math.floor((double) (top + this.m42));
            dst.mRight = (int) Math.ceil((double) (right + this.m41));
            dst.mBottom = (int) Math.ceil((double) (bottom + this.m42));
        } else if ((typeMask & -4) == 0) {
            dst.mLeft = (int) Math.floor((double) (left * this.m11 + this.m41));
            dst.mTop = (int) Math.floor((double) (top * this.m22 + this.m42));
            dst.mRight = (int) Math.ceil((double) (right * this.m11 + this.m41));
            dst.mBottom = (int) Math.ceil((double) (bottom * this.m22 + this.m42));
        } else {
            float x1 = this.m11 * left + this.m21 * top + this.m41;
            float y1 = this.m12 * left + this.m22 * top + this.m42;
            float x2 = this.m11 * right + this.m21 * top + this.m41;
            float y2 = this.m12 * right + this.m22 * top + this.m42;
            float x3 = this.m11 * left + this.m21 * bottom + this.m41;
            float y3 = this.m12 * left + this.m22 * bottom + this.m42;
            float x4 = this.m11 * right + this.m21 * bottom + this.m41;
            float y4 = this.m12 * right + this.m22 * bottom + this.m42;
            if ((typeMask & 8) != 0) {
                float w = 1.0F / (this.m14 * left + this.m24 * top + this.m44);
                x1 *= w;
                y1 *= w;
                w = 1.0F / (this.m14 * right + this.m24 * top + this.m44);
                x2 *= w;
                y2 *= w;
                w = 1.0F / (this.m14 * left + this.m24 * bottom + this.m44);
                x3 *= w;
                y3 *= w;
                w = 1.0F / (this.m14 * right + this.m24 * bottom + this.m44);
                x4 *= w;
                y4 *= w;
            }
            dst.mLeft = (int) Math.floor((double) MathUtil.min(x1, x2, x3, x4));
            dst.mTop = (int) Math.floor((double) MathUtil.min(y1, y2, y3, y4));
            dst.mRight = (int) Math.ceil((double) MathUtil.max(x1, x2, x3, x4));
            dst.mBottom = (int) Math.ceil((double) MathUtil.max(y1, y2, y3, y4));
        }
    }

    @Override
    public void mapPoints(float[] src, int srcPos, float[] dst, int dstPos, int count) {
        int mask = this.getType();
        if (mask == 0) {
            if (src != dst && count > 0) {
                System.arraycopy(src, srcPos, dst, dstPos, count << 1);
            }
        } else if (mask <= 1) {
            this.mapPoints1(src, srcPos, dst, dstPos, count);
        } else if (mask <= 3) {
            this.mapPoints2(src, srcPos, dst, dstPos, count);
        } else if (mask <= 7) {
            this.mapPoints4(src, srcPos, dst, dstPos, count);
        } else {
            this.mapPoints8(src, srcPos, dst, dstPos, count);
        }
    }

    private void mapPoints1(float[] src, int srcPos, float[] dst, int dstPos, int count) {
        float m41 = this.m41;
        float m42 = this.m42;
        while (count-- != 0) {
            dst[dstPos] = src[srcPos] + m41;
            dst[dstPos + 1] = src[srcPos + 1] + m42;
            srcPos += 2;
            dstPos += 2;
        }
    }

    private void mapPoints2(float[] src, int srcPos, float[] dst, int dstPos, int count) {
        float m11 = this.m11;
        float m22 = this.m22;
        float m41 = this.m41;
        float m42 = this.m42;
        while (count-- != 0) {
            dst[dstPos] = m11 * src[srcPos] + m41;
            dst[dstPos + 1] = m22 * src[srcPos + 1] + m42;
            srcPos += 2;
            dstPos += 2;
        }
    }

    private void mapPoints4(float[] src, int srcPos, float[] dst, int dstPos, int count) {
        float m11 = this.m11;
        float m12 = this.m12;
        float m21 = this.m21;
        float m22 = this.m22;
        float m41 = this.m41;
        float m42 = this.m42;
        while (count-- != 0) {
            float p0 = src[srcPos];
            float p1 = src[srcPos + 1];
            float x = m11 * p0 + m21 * p1 + m41;
            float y = m12 * p0 + m22 * p1 + m42;
            dst[dstPos] = x;
            dst[dstPos + 1] = y;
            srcPos += 2;
            dstPos += 2;
        }
    }

    private void mapPoints8(float[] src, int srcPos, float[] dst, int dstPos, int count) {
        float m11 = this.m11;
        float m12 = this.m12;
        float m14 = this.m14;
        float m21 = this.m21;
        float m22 = this.m22;
        float m24 = this.m24;
        float m41 = this.m41;
        float m42 = this.m42;
        float m44 = this.m44;
        while (count-- != 0) {
            float p0 = src[srcPos];
            float p1 = src[srcPos + 1];
            float x = m11 * p0 + m21 * p1 + m41;
            float y = m12 * p0 + m22 * p1 + m42;
            float w = m14 * p0 + m24 * p1 + m44;
            if (w != 0.0F) {
                w = 1.0F / w;
            }
            dst[dstPos] = x * w;
            dst[dstPos + 1] = y * w;
            srcPos += 2;
            dstPos += 2;
        }
    }

    @Override
    public float getMinScale() {
        int mask = this.getType();
        if (mask == 0) {
            return 1.0F;
        } else if ((mask & 8) != 0) {
            return -1.0F;
        } else if ((mask & 4) == 0) {
            return Math.min(Math.abs(this.m11), Math.abs(this.m22));
        } else {
            float a = this.m11 * this.m11 + this.m12 * this.m12;
            float b = this.m11 * this.m21 + this.m22 * this.m12;
            float c = this.m21 * this.m21 + this.m22 * this.m22;
            float result;
            if (MathUtil.isApproxZero(b)) {
                result = Math.min(a, c);
            } else {
                float amc = a - c;
                float x = (float) (Math.sqrt((double) (amc * amc + 4.0F * b * b)) * 0.5);
                result = (a + c) * 0.5F - x;
            }
            return !Float.isFinite(result) ? -1.0F : (float) Math.sqrt((double) Math.abs(result));
        }
    }

    @Override
    public float getMaxScale() {
        int mask = this.getType();
        if (mask == 0) {
            return 1.0F;
        } else if ((mask & 8) != 0) {
            return -1.0F;
        } else if ((mask & 4) == 0) {
            return Math.max(Math.abs(this.m11), Math.abs(this.m22));
        } else {
            float a = this.m11 * this.m11 + this.m12 * this.m12;
            float b = this.m11 * this.m21 + this.m22 * this.m12;
            float c = this.m21 * this.m21 + this.m22 * this.m22;
            float result;
            if (MathUtil.isApproxZero(b)) {
                result = Math.max(a, c);
            } else {
                float amc = a - c;
                float x = (float) (Math.sqrt((double) (amc * amc + 4.0F * b * b)) * 0.5);
                result = (a + c) * 0.5F + x;
            }
            return !Float.isFinite(result) ? -1.0F : (float) Math.sqrt((double) Math.abs(result));
        }
    }

    public void normalizePerspective() {
        if (this.m44 != 1.0F && this.m14 == 0.0F && this.m24 == 0.0F) {
            if (this.m44 != 0.0F) {
                double inv = 1.0 / (double) this.m44;
                this.m11 = (float) ((double) this.m11 * inv);
                this.m12 = (float) ((double) this.m12 * inv);
                this.m21 = (float) ((double) this.m21 * inv);
                this.m22 = (float) ((double) this.m22 * inv);
                this.m41 = (float) ((double) this.m41 * inv);
                this.m42 = (float) ((double) this.m42 * inv);
                this.m44 = 1.0F;
            }
            this.mTypeMask = 128;
        }
    }

    @Override
    public boolean isFinite() {
        return 0.0F * this.m11 * this.m12 * this.m14 * this.m21 * this.m22 * this.m24 * this.m41 * this.m42 * this.m44 == 0.0F;
    }

    private static int floatTo2sCompliment(float x) {
        int bits = Float.floatToRawIntBits(x);
        return bits < 0 ? -(bits & 2147483647) : bits;
    }

    private int computeTypeMask() {
        int mask = 0;
        if (this.m14 == 0.0F && this.m24 == 0.0F && this.m44 == 1.0F) {
            if (this.m41 != 0.0F || this.m42 != 0.0F) {
                mask |= 1;
            }
            int m00 = floatTo2sCompliment(this.m11);
            int m01 = floatTo2sCompliment(this.m21);
            int m10 = floatTo2sCompliment(this.m12);
            int m11 = floatTo2sCompliment(this.m22);
            if ((m01 | m10) != 0) {
                mask |= 6;
                m01 = m01 != 0 ? 1 : 0;
                m10 = m10 != 0 ? 1 : 0;
                int dp0 = (m00 | m11) == 0 ? 1 : 0;
                int ds1 = m01 & m10;
                mask |= (dp0 & ds1) << 4;
            } else {
                if ((m00 ^ 1065353216 | m11 ^ 1065353216) != 0) {
                    mask |= 2;
                }
                m00 = m00 != 0 ? 1 : 0;
                m11 = m11 != 0 ? 1 : 0;
                mask |= (m00 & m11) << 4;
            }
            return mask;
        } else {
            return 15;
        }
    }

    private int computePerspectiveTypeMask() {
        return this.m14 == 0.0F && this.m24 == 0.0F && this.m44 == 1.0F ? 192 : 15;
    }

    public boolean isApproxEqual(@Nonnull Matrix m) {
        return MathUtil.isApproxEqual(this.m11, m.m11) && MathUtil.isApproxEqual(this.m12, m.m12) && MathUtil.isApproxEqual(this.m14, m.m14) && MathUtil.isApproxEqual(this.m21, m.m21) && MathUtil.isApproxEqual(this.m22, m.m22) && MathUtil.isApproxEqual(this.m24, m.m24) && MathUtil.isApproxEqual(this.m41, m.m41) && MathUtil.isApproxEqual(this.m42, m.m42) && MathUtil.isApproxEqual(this.m44, m.m44);
    }

    public static boolean equals(@Nonnull Matrixc a, @Nonnull Matrixc b) {
        return a.m11() == b.m11() && a.m12() == b.m12() && a.m14() == b.m14() && a.m21() == b.m21() && a.m22() == b.m22() && a.m24() == b.m24() && a.m41() == b.m41() && a.m42() == b.m42() && a.m44() == b.m44();
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(this.m11);
        result = 31 * result + Float.floatToIntBits(this.m12);
        result = 31 * result + Float.floatToIntBits(this.m14);
        result = 31 * result + Float.floatToIntBits(this.m21);
        result = 31 * result + Float.floatToIntBits(this.m22);
        result = 31 * result + Float.floatToIntBits(this.m24);
        result = 31 * result + Float.floatToIntBits(this.m41);
        result = 31 * result + Float.floatToIntBits(this.m42);
        return 31 * result + Float.floatToIntBits(this.m44);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Matrixc m ? equals(this, m) : false;
    }

    @Override
    public String toString() {
        return String.format("Matrix:\n%10.6f %10.6f %10.6f\n%10.6f %10.6f %10.6f\n%10.6f %10.6f %10.6f", this.m11, this.m12, this.m14, this.m21, this.m22, this.m24, this.m41, this.m42, this.m44);
    }

    @Nonnull
    public Matrix clone() {
        try {
            return (Matrix) super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new InternalError(var2);
        }
    }
}