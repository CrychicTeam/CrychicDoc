package icyllis.arc3d.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.lwjgl.system.MemoryUtil;

public class Matrix4 implements Cloneable {

    public float m11;

    public float m12;

    public float m13;

    public float m14;

    public float m21;

    public float m22;

    public float m23;

    public float m24;

    public float m31;

    public float m32;

    public float m33;

    public float m34;

    public float m41;

    public float m42;

    public float m43;

    public float m44;

    public Matrix4() {
    }

    public Matrix4(@Nonnull Matrix4 m) {
        this.m11 = m.m11;
        this.m12 = m.m12;
        this.m13 = m.m13;
        this.m14 = m.m14;
        this.m21 = m.m21;
        this.m22 = m.m22;
        this.m23 = m.m23;
        this.m24 = m.m24;
        this.m31 = m.m31;
        this.m32 = m.m32;
        this.m33 = m.m33;
        this.m34 = m.m34;
        this.m41 = m.m41;
        this.m42 = m.m42;
        this.m43 = m.m43;
        this.m44 = m.m44;
    }

    public Matrix4(@Nonnull float... a) {
        this.set(a);
    }

    @Nonnull
    public static Matrix4 copy(@Nullable Matrix4 m) {
        return m == null ? identity() : m.clone();
    }

    @Nonnull
    public static Matrix4 identity() {
        Matrix4 m = new Matrix4();
        m.m11 = m.m22 = m.m33 = m.m44 = 1.0F;
        return m;
    }

    @Nonnull
    public static Matrix4 makeTranslate(float x, float y, float z) {
        Matrix4 m = new Matrix4();
        m.m11 = 1.0F;
        m.m22 = 1.0F;
        m.m33 = 1.0F;
        m.m41 = x;
        m.m42 = y;
        m.m43 = z;
        m.m44 = 1.0F;
        return m;
    }

    @Nonnull
    public static Matrix4 makeScale(float x, float y, float z) {
        Matrix4 m = new Matrix4();
        m.m11 = x;
        m.m22 = y;
        m.m33 = z;
        m.m44 = 1.0F;
        return m;
    }

    @Nonnull
    public static Matrix4 makeOrthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4 mat = new Matrix4();
        float invRL = 1.0F / (right - left);
        float invTB = 1.0F / (top - bottom);
        float invNF = 1.0F / (near - far);
        mat.m11 = 2.0F * invRL;
        mat.m22 = 2.0F * invTB;
        mat.m33 = 2.0F * invNF;
        mat.m41 = -(right + left) * invRL;
        mat.m42 = -(top + bottom) * invTB;
        mat.m43 = (near + far) * invNF;
        mat.m44 = 1.0F;
        return mat;
    }

    @Nonnull
    public static Matrix4 makeOrthographic(float width, float height, float near, float far, boolean flipY) {
        Matrix4 mat = new Matrix4();
        float invNF = 1.0F / (near - far);
        mat.m11 = 2.0F / width;
        mat.m22 = flipY ? -2.0F / height : 2.0F / height;
        mat.m33 = 2.0F * invNF;
        mat.m41 = -1.0F;
        mat.m42 = flipY ? 1.0F : -1.0F;
        mat.m43 = (near + far) * invNF;
        mat.m44 = 1.0F;
        return mat;
    }

    @Nonnull
    public static Matrix4 makePerspective(float left, float right, float bottom, float top, float near, float far) {
        Matrix4 mat = new Matrix4();
        float invRL = 1.0F / (right - left);
        float invTB = 1.0F / (top - bottom);
        float invNF = 1.0F / (near - far);
        float tNear = 2.0F * near;
        mat.m11 = tNear * invRL;
        mat.m22 = tNear * invTB;
        mat.m31 = (right + left) * invRL;
        mat.m32 = (top + bottom) * invTB;
        mat.m33 = (near + far) * invNF;
        mat.m34 = -1.0F;
        mat.m43 = tNear * far * invNF;
        return mat;
    }

    @Nonnull
    public static Matrix4 makePerspective(float fov, float aspect, float near, float far) {
        Matrix4 mat = new Matrix4();
        float y = (float) (1.0 / Math.tan((double) fov * 0.5));
        float invNF = 1.0F / (near - far);
        mat.m11 = y / aspect;
        mat.m22 = y;
        mat.m33 = (near + far) * invNF;
        mat.m34 = -1.0F;
        mat.m43 = 2.0F * far * near * invNF;
        return mat;
    }

    public void add(@Nonnull Matrix4 m) {
        this.m11 = this.m11 + m.m11;
        this.m12 = this.m12 + m.m12;
        this.m13 = this.m13 + m.m13;
        this.m14 = this.m14 + m.m14;
        this.m21 = this.m21 + m.m21;
        this.m22 = this.m22 + m.m22;
        this.m23 = this.m23 + m.m23;
        this.m24 = this.m24 + m.m24;
        this.m31 = this.m31 + m.m31;
        this.m32 = this.m32 + m.m32;
        this.m33 = this.m33 + m.m33;
        this.m34 = this.m34 + m.m34;
        this.m41 = this.m41 + m.m41;
        this.m42 = this.m42 + m.m42;
        this.m43 = this.m43 + m.m43;
        this.m44 = this.m44 + m.m44;
    }

    public void subtract(@Nonnull Matrix4 m) {
        this.m11 = this.m11 - m.m11;
        this.m12 = this.m12 - m.m12;
        this.m13 = this.m13 - m.m13;
        this.m14 = this.m14 - m.m14;
        this.m21 = this.m21 - m.m21;
        this.m22 = this.m22 - m.m22;
        this.m23 = this.m23 - m.m23;
        this.m24 = this.m24 - m.m24;
        this.m31 = this.m31 - m.m31;
        this.m32 = this.m32 - m.m32;
        this.m33 = this.m33 - m.m33;
        this.m34 = this.m34 - m.m34;
        this.m41 = this.m41 - m.m41;
        this.m42 = this.m42 - m.m42;
        this.m43 = this.m43 - m.m43;
        this.m44 = this.m44 - m.m44;
    }

    public void preConcat(@Nonnull Matrix4 lhs) {
        float f11 = lhs.m11 * this.m11 + lhs.m12 * this.m21 + lhs.m13 * this.m31 + lhs.m14 * this.m41;
        float f12 = lhs.m11 * this.m12 + lhs.m12 * this.m22 + lhs.m13 * this.m32 + lhs.m14 * this.m42;
        float f13 = lhs.m11 * this.m13 + lhs.m12 * this.m23 + lhs.m13 * this.m33 + lhs.m14 * this.m43;
        float f14 = lhs.m11 * this.m14 + lhs.m12 * this.m24 + lhs.m13 * this.m34 + lhs.m14 * this.m44;
        float f21 = lhs.m21 * this.m11 + lhs.m22 * this.m21 + lhs.m23 * this.m31 + lhs.m24 * this.m41;
        float f22 = lhs.m21 * this.m12 + lhs.m22 * this.m22 + lhs.m23 * this.m32 + lhs.m24 * this.m42;
        float f23 = lhs.m21 * this.m13 + lhs.m22 * this.m23 + lhs.m23 * this.m33 + lhs.m24 * this.m43;
        float f24 = lhs.m21 * this.m14 + lhs.m22 * this.m24 + lhs.m23 * this.m34 + lhs.m24 * this.m44;
        float f31 = lhs.m31 * this.m11 + lhs.m32 * this.m21 + lhs.m33 * this.m31 + lhs.m34 * this.m41;
        float f32 = lhs.m31 * this.m12 + lhs.m32 * this.m22 + lhs.m33 * this.m32 + lhs.m34 * this.m42;
        float f33 = lhs.m31 * this.m13 + lhs.m32 * this.m23 + lhs.m33 * this.m33 + lhs.m34 * this.m43;
        float f34 = lhs.m31 * this.m14 + lhs.m32 * this.m24 + lhs.m33 * this.m34 + lhs.m34 * this.m44;
        float f41 = lhs.m41 * this.m11 + lhs.m42 * this.m21 + lhs.m43 * this.m31 + lhs.m44 * this.m41;
        float f42 = lhs.m41 * this.m12 + lhs.m42 * this.m22 + lhs.m43 * this.m32 + lhs.m44 * this.m42;
        float f43 = lhs.m41 * this.m13 + lhs.m42 * this.m23 + lhs.m43 * this.m33 + lhs.m44 * this.m43;
        float f44 = lhs.m41 * this.m14 + lhs.m42 * this.m24 + lhs.m43 * this.m34 + lhs.m44 * this.m44;
        this.m11 = f11;
        this.m12 = f12;
        this.m13 = f13;
        this.m14 = f14;
        this.m21 = f21;
        this.m22 = f22;
        this.m23 = f23;
        this.m24 = f24;
        this.m31 = f31;
        this.m32 = f32;
        this.m33 = f33;
        this.m34 = f34;
        this.m41 = f41;
        this.m42 = f42;
        this.m43 = f43;
        this.m44 = f44;
    }

    public void preConcat(float l11, float l12, float l13, float l14, float l21, float l22, float l23, float l24, float l31, float l32, float l33, float l34, float l41, float l42, float l43, float l44) {
        float f11 = l11 * this.m11 + l12 * this.m21 + l13 * this.m31 + l14 * this.m41;
        float f12 = l11 * this.m12 + l12 * this.m22 + l13 * this.m32 + l14 * this.m42;
        float f13 = l11 * this.m13 + l12 * this.m23 + l13 * this.m33 + l14 * this.m43;
        float f14 = l11 * this.m14 + l12 * this.m24 + l13 * this.m34 + l14 * this.m44;
        float f21 = l21 * this.m11 + l22 * this.m21 + l23 * this.m31 + l24 * this.m41;
        float f22 = l21 * this.m12 + l22 * this.m22 + l23 * this.m32 + l24 * this.m42;
        float f23 = l21 * this.m13 + l22 * this.m23 + l23 * this.m33 + l24 * this.m43;
        float f24 = l21 * this.m14 + l22 * this.m24 + l23 * this.m34 + l24 * this.m44;
        float f31 = l31 * this.m11 + l32 * this.m21 + l33 * this.m31 + l34 * this.m41;
        float f32 = l31 * this.m12 + l32 * this.m22 + l33 * this.m32 + l34 * this.m42;
        float f33 = l31 * this.m13 + l32 * this.m23 + l33 * this.m33 + l34 * this.m43;
        float f34 = l31 * this.m14 + l32 * this.m24 + l33 * this.m34 + l34 * this.m44;
        float f41 = l41 * this.m11 + l42 * this.m21 + l43 * this.m31 + l44 * this.m41;
        float f42 = l41 * this.m12 + l42 * this.m22 + l43 * this.m32 + l44 * this.m42;
        float f43 = l41 * this.m13 + l42 * this.m23 + l43 * this.m33 + l44 * this.m43;
        float f44 = l41 * this.m14 + l42 * this.m24 + l43 * this.m34 + l44 * this.m44;
        this.m11 = f11;
        this.m12 = f12;
        this.m13 = f13;
        this.m14 = f14;
        this.m21 = f21;
        this.m22 = f22;
        this.m23 = f23;
        this.m24 = f24;
        this.m31 = f31;
        this.m32 = f32;
        this.m33 = f33;
        this.m34 = f34;
        this.m41 = f41;
        this.m42 = f42;
        this.m43 = f43;
        this.m44 = f44;
    }

    public void postConcat(@Nonnull Matrix4 rhs) {
        float f11 = this.m11 * rhs.m11 + this.m12 * rhs.m21 + this.m13 * rhs.m31 + this.m14 * rhs.m41;
        float f12 = this.m11 * rhs.m12 + this.m12 * rhs.m22 + this.m13 * rhs.m32 + this.m14 * rhs.m42;
        float f13 = this.m11 * rhs.m13 + this.m12 * rhs.m23 + this.m13 * rhs.m33 + this.m14 * rhs.m43;
        float f14 = this.m11 * rhs.m14 + this.m12 * rhs.m24 + this.m13 * rhs.m34 + this.m14 * rhs.m44;
        float f21 = this.m21 * rhs.m11 + this.m22 * rhs.m21 + this.m23 * rhs.m31 + this.m24 * rhs.m41;
        float f22 = this.m21 * rhs.m12 + this.m22 * rhs.m22 + this.m23 * rhs.m32 + this.m24 * rhs.m42;
        float f23 = this.m21 * rhs.m13 + this.m22 * rhs.m23 + this.m23 * rhs.m33 + this.m24 * rhs.m43;
        float f24 = this.m21 * rhs.m14 + this.m22 * rhs.m24 + this.m23 * rhs.m34 + this.m24 * rhs.m44;
        float f31 = this.m31 * rhs.m11 + this.m32 * rhs.m21 + this.m33 * rhs.m31 + this.m34 * rhs.m41;
        float f32 = this.m31 * rhs.m12 + this.m32 * rhs.m22 + this.m33 * rhs.m32 + this.m34 * rhs.m42;
        float f33 = this.m31 * rhs.m13 + this.m32 * rhs.m23 + this.m33 * rhs.m33 + this.m34 * rhs.m43;
        float f34 = this.m31 * rhs.m14 + this.m32 * rhs.m24 + this.m33 * rhs.m34 + this.m34 * rhs.m44;
        float f41 = this.m41 * rhs.m11 + this.m42 * rhs.m21 + this.m43 * rhs.m31 + this.m44 * rhs.m41;
        float f42 = this.m41 * rhs.m12 + this.m42 * rhs.m22 + this.m43 * rhs.m32 + this.m44 * rhs.m42;
        float f43 = this.m41 * rhs.m13 + this.m42 * rhs.m23 + this.m43 * rhs.m33 + this.m44 * rhs.m43;
        float f44 = this.m41 * rhs.m14 + this.m42 * rhs.m24 + this.m43 * rhs.m34 + this.m44 * rhs.m44;
        this.m11 = f11;
        this.m12 = f12;
        this.m13 = f13;
        this.m14 = f14;
        this.m21 = f21;
        this.m22 = f22;
        this.m23 = f23;
        this.m24 = f24;
        this.m31 = f31;
        this.m32 = f32;
        this.m33 = f33;
        this.m34 = f34;
        this.m41 = f41;
        this.m42 = f42;
        this.m43 = f43;
        this.m44 = f44;
    }

    public void postConcat(float r11, float r12, float r13, float r14, float r21, float r22, float r23, float r24, float r31, float r32, float r33, float r34, float r41, float r42, float r43, float r44) {
        float f11 = this.m11 * r11 + this.m12 * r21 + this.m13 * r31 + this.m14 * r41;
        float f12 = this.m11 * r12 + this.m12 * r22 + this.m13 * r32 + this.m14 * r42;
        float f13 = this.m11 * r13 + this.m12 * r23 + this.m13 * r33 + this.m14 * r43;
        float f14 = this.m11 * r14 + this.m12 * r24 + this.m13 * r34 + this.m14 * r44;
        float f21 = this.m21 * r11 + this.m22 * r21 + this.m23 * r31 + this.m24 * r41;
        float f22 = this.m21 * r12 + this.m22 * r22 + this.m23 * r32 + this.m24 * r42;
        float f23 = this.m21 * r13 + this.m22 * r23 + this.m23 * r33 + this.m24 * r43;
        float f24 = this.m21 * r14 + this.m22 * r24 + this.m23 * r34 + this.m24 * r44;
        float f31 = this.m31 * r11 + this.m32 * r21 + this.m33 * r31 + this.m34 * r41;
        float f32 = this.m31 * r12 + this.m32 * r22 + this.m33 * r32 + this.m34 * r42;
        float f33 = this.m31 * r13 + this.m32 * r23 + this.m33 * r33 + this.m34 * r43;
        float f34 = this.m31 * r14 + this.m32 * r24 + this.m33 * r34 + this.m34 * r44;
        float f41 = this.m41 * r11 + this.m42 * r21 + this.m43 * r31 + this.m44 * r41;
        float f42 = this.m41 * r12 + this.m42 * r22 + this.m43 * r32 + this.m44 * r42;
        float f43 = this.m41 * r13 + this.m42 * r23 + this.m43 * r33 + this.m44 * r43;
        float f44 = this.m41 * r14 + this.m42 * r24 + this.m43 * r34 + this.m44 * r44;
        this.m11 = f11;
        this.m12 = f12;
        this.m13 = f13;
        this.m14 = f14;
        this.m21 = f21;
        this.m22 = f22;
        this.m23 = f23;
        this.m24 = f24;
        this.m31 = f31;
        this.m32 = f32;
        this.m33 = f33;
        this.m34 = f34;
        this.m41 = f41;
        this.m42 = f42;
        this.m43 = f43;
        this.m44 = f44;
    }

    public void preConcat2D(@Nonnull Matrixc lhs) {
        float f11 = lhs.m11() * this.m11 + lhs.m12() * this.m21 + lhs.m14() * this.m41;
        float f12 = lhs.m11() * this.m12 + lhs.m12() * this.m22 + lhs.m14() * this.m42;
        float f13 = lhs.m11() * this.m13 + lhs.m12() * this.m23 + lhs.m14() * this.m43;
        float f14 = lhs.m11() * this.m14 + lhs.m12() * this.m24 + lhs.m14() * this.m44;
        float f21 = lhs.m21() * this.m11 + lhs.m22() * this.m21 + lhs.m24() * this.m41;
        float f22 = lhs.m21() * this.m12 + lhs.m22() * this.m22 + lhs.m24() * this.m42;
        float f23 = lhs.m21() * this.m13 + lhs.m22() * this.m23 + lhs.m24() * this.m43;
        float f24 = lhs.m21() * this.m14 + lhs.m22() * this.m24 + lhs.m24() * this.m44;
        float f41 = lhs.m41() * this.m11 + lhs.m42() * this.m21 + lhs.m44() * this.m41;
        float f42 = lhs.m41() * this.m12 + lhs.m42() * this.m22 + lhs.m44() * this.m42;
        float f43 = lhs.m41() * this.m13 + lhs.m42() * this.m23 + lhs.m44() * this.m43;
        float f44 = lhs.m41() * this.m14 + lhs.m42() * this.m24 + lhs.m44() * this.m44;
        this.m11 = f11;
        this.m12 = f12;
        this.m13 = f13;
        this.m14 = f14;
        this.m21 = f21;
        this.m22 = f22;
        this.m23 = f23;
        this.m24 = f24;
        this.m41 = f41;
        this.m42 = f42;
        this.m43 = f43;
        this.m44 = f44;
    }

    public void postConcat2D(@Nonnull Matrixc rhs) {
        float f11 = this.m11 * rhs.m11() + this.m12 * rhs.m21() + this.m14 * rhs.m41();
        float f12 = this.m11 * rhs.m12() + this.m12 * rhs.m22() + this.m14 * rhs.m42();
        float f14 = this.m11 * rhs.m14() + this.m12 * rhs.m24() + this.m14 * rhs.m44();
        float f21 = this.m21 * rhs.m11() + this.m22 * rhs.m21() + this.m24 * rhs.m41();
        float f22 = this.m21 * rhs.m12() + this.m22 * rhs.m22() + this.m24 * rhs.m42();
        float f24 = this.m21 * rhs.m14() + this.m22 * rhs.m24() + this.m24 * rhs.m44();
        float f31 = this.m31 * rhs.m11() + this.m32 * rhs.m21() + this.m34 * rhs.m41();
        float f32 = this.m31 * rhs.m12() + this.m32 * rhs.m22() + this.m34 * rhs.m42();
        float f34 = this.m31 * rhs.m14() + this.m32 * rhs.m24() + this.m34 * rhs.m44();
        float f41 = this.m41 * rhs.m11() + this.m42 * rhs.m21() + this.m44 * rhs.m41();
        float f42 = this.m41 * rhs.m12() + this.m42 * rhs.m22() + this.m44 * rhs.m42();
        float f44 = this.m41 * rhs.m14() + this.m42 * rhs.m24() + this.m44 * rhs.m44();
        this.m11 = f11;
        this.m12 = f12;
        this.m14 = f14;
        this.m21 = f21;
        this.m22 = f22;
        this.m24 = f24;
        this.m31 = f31;
        this.m32 = f32;
        this.m34 = f34;
        this.m41 = f41;
        this.m42 = f42;
        this.m44 = f44;
    }

    public void preConcat(@Nonnull Matrix3 lhs) {
        float f11 = lhs.m11 * this.m11 + lhs.m12 * this.m21 + lhs.m13 * this.m31;
        float f12 = lhs.m11 * this.m12 + lhs.m12 * this.m22 + lhs.m13 * this.m32;
        float f13 = lhs.m11 * this.m13 + lhs.m12 * this.m23 + lhs.m13 * this.m33;
        float f14 = lhs.m11 * this.m14 + lhs.m12 * this.m24 + lhs.m13 * this.m34;
        float f21 = lhs.m21 * this.m11 + lhs.m22 * this.m21 + lhs.m23 * this.m31;
        float f22 = lhs.m21 * this.m12 + lhs.m22 * this.m22 + lhs.m23 * this.m32;
        float f23 = lhs.m21 * this.m13 + lhs.m22 * this.m23 + lhs.m23 * this.m33;
        float f24 = lhs.m21 * this.m14 + lhs.m22 * this.m24 + lhs.m23 * this.m34;
        float f31 = lhs.m31 * this.m11 + lhs.m32 * this.m21 + lhs.m33 * this.m31;
        float f32 = lhs.m31 * this.m12 + lhs.m32 * this.m22 + lhs.m33 * this.m32;
        float f33 = lhs.m31 * this.m13 + lhs.m32 * this.m23 + lhs.m33 * this.m33;
        float f34 = lhs.m31 * this.m14 + lhs.m32 * this.m24 + lhs.m33 * this.m34;
        this.m11 = f11;
        this.m12 = f12;
        this.m13 = f13;
        this.m14 = f14;
        this.m21 = f21;
        this.m22 = f22;
        this.m23 = f23;
        this.m24 = f24;
        this.m31 = f31;
        this.m32 = f32;
        this.m33 = f33;
        this.m34 = f34;
    }

    public void postConcat(@Nonnull Matrix3 rhs) {
        float f11 = this.m11 * rhs.m11 + this.m12 * rhs.m21 + this.m13 * rhs.m31;
        float f12 = this.m11 * rhs.m12 + this.m12 * rhs.m22 + this.m13 * rhs.m32;
        float f13 = this.m11 * rhs.m13 + this.m12 * rhs.m23 + this.m13 * rhs.m33;
        float f21 = this.m21 * rhs.m11 + this.m22 * rhs.m21 + this.m23 * rhs.m31;
        float f22 = this.m21 * rhs.m12 + this.m22 * rhs.m22 + this.m23 * rhs.m32;
        float f23 = this.m21 * rhs.m13 + this.m22 * rhs.m23 + this.m23 * rhs.m33;
        float f31 = this.m31 * rhs.m11 + this.m32 * rhs.m21 + this.m33 * rhs.m31;
        float f32 = this.m31 * rhs.m12 + this.m32 * rhs.m22 + this.m33 * rhs.m32;
        float f33 = this.m31 * rhs.m13 + this.m32 * rhs.m23 + this.m33 * rhs.m33;
        float f41 = this.m41 * rhs.m11 + this.m42 * rhs.m21 + this.m43 * rhs.m31;
        float f42 = this.m41 * rhs.m12 + this.m42 * rhs.m22 + this.m43 * rhs.m32;
        float f43 = this.m41 * rhs.m13 + this.m42 * rhs.m23 + this.m43 * rhs.m33;
        this.m11 = f11;
        this.m12 = f12;
        this.m13 = f13;
        this.m21 = f21;
        this.m22 = f22;
        this.m23 = f23;
        this.m31 = f31;
        this.m32 = f32;
        this.m33 = f33;
        this.m41 = f41;
        this.m42 = f42;
        this.m43 = f43;
    }

    public void setZero() {
        this.m11 = 0.0F;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 0.0F;
        this.m23 = 0.0F;
        this.m24 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 0.0F;
        this.m34 = 0.0F;
        this.m41 = 0.0F;
        this.m42 = 0.0F;
        this.m43 = 0.0F;
        this.m44 = 0.0F;
    }

    public void setIdentity() {
        this.m11 = 1.0F;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 1.0F;
        this.m23 = 0.0F;
        this.m24 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
        this.m34 = 0.0F;
        this.m41 = 0.0F;
        this.m42 = 0.0F;
        this.m43 = 0.0F;
        this.m44 = 1.0F;
    }

    public void set(@Nonnull Matrix4 m) {
        this.m11 = m.m11;
        this.m12 = m.m12;
        this.m13 = m.m13;
        this.m14 = m.m14;
        this.m21 = m.m21;
        this.m22 = m.m22;
        this.m23 = m.m23;
        this.m24 = m.m24;
        this.m31 = m.m31;
        this.m32 = m.m32;
        this.m33 = m.m33;
        this.m34 = m.m34;
        this.m41 = m.m41;
        this.m42 = m.m42;
        this.m43 = m.m43;
        this.m44 = m.m44;
    }

    public void set(float m11, float m12, float m13, float m14, float m21, float m22, float m23, float m24, float m31, float m32, float m33, float m34, float m41, float m42, float m43, float m44) {
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m14 = m14;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m24 = m24;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
        this.m34 = m34;
        this.m41 = m41;
        this.m42 = m42;
        this.m43 = m43;
        this.m44 = m44;
    }

    public void set(@Nonnull float[] a) {
        this.m11 = a[0];
        this.m12 = a[1];
        this.m13 = a[2];
        this.m14 = a[3];
        this.m21 = a[4];
        this.m22 = a[5];
        this.m23 = a[6];
        this.m24 = a[7];
        this.m31 = a[8];
        this.m32 = a[9];
        this.m33 = a[10];
        this.m34 = a[11];
        this.m41 = a[12];
        this.m42 = a[13];
        this.m43 = a[14];
        this.m44 = a[15];
    }

    public void set(@Nonnull float[] a, int offset) {
        this.m11 = a[offset];
        this.m12 = a[offset + 1];
        this.m13 = a[offset + 2];
        this.m14 = a[offset + 3];
        this.m21 = a[offset + 4];
        this.m22 = a[offset + 5];
        this.m23 = a[offset + 6];
        this.m24 = a[offset + 7];
        this.m31 = a[offset + 8];
        this.m32 = a[offset + 9];
        this.m33 = a[offset + 10];
        this.m34 = a[offset + 11];
        this.m41 = a[offset + 12];
        this.m42 = a[offset + 13];
        this.m43 = a[offset + 14];
        this.m44 = a[offset + 15];
    }

    public void set(@Nonnull ByteBuffer a) {
        int offset = a.position();
        this.m11 = a.getFloat(offset);
        this.m12 = a.getFloat(offset + 4);
        this.m13 = a.getFloat(offset + 8);
        this.m14 = a.getFloat(offset + 12);
        this.m21 = a.getFloat(offset + 16);
        this.m22 = a.getFloat(offset + 20);
        this.m23 = a.getFloat(offset + 24);
        this.m24 = a.getFloat(offset + 28);
        this.m31 = a.getFloat(offset + 32);
        this.m32 = a.getFloat(offset + 36);
        this.m33 = a.getFloat(offset + 40);
        this.m34 = a.getFloat(offset + 44);
        this.m41 = a.getFloat(offset + 48);
        this.m42 = a.getFloat(offset + 52);
        this.m43 = a.getFloat(offset + 56);
        this.m44 = a.getFloat(offset + 60);
    }

    public void set(@Nonnull FloatBuffer a) {
        int offset = a.position();
        this.m11 = a.get(offset);
        this.m12 = a.get(offset + 1);
        this.m13 = a.get(offset + 2);
        this.m14 = a.get(offset + 3);
        this.m21 = a.get(offset + 4);
        this.m22 = a.get(offset + 5);
        this.m23 = a.get(offset + 6);
        this.m24 = a.get(offset + 7);
        this.m31 = a.get(offset + 8);
        this.m32 = a.get(offset + 9);
        this.m33 = a.get(offset + 10);
        this.m34 = a.get(offset + 11);
        this.m41 = a.get(offset + 12);
        this.m42 = a.get(offset + 13);
        this.m43 = a.get(offset + 14);
        this.m44 = a.get(offset + 15);
    }

    public void set(long p) {
        this.m11 = MemoryUtil.memGetFloat(p);
        this.m12 = MemoryUtil.memGetFloat(p + 4L);
        this.m13 = MemoryUtil.memGetFloat(p + 8L);
        this.m14 = MemoryUtil.memGetFloat(p + 12L);
        this.m21 = MemoryUtil.memGetFloat(p + 16L);
        this.m22 = MemoryUtil.memGetFloat(p + 20L);
        this.m23 = MemoryUtil.memGetFloat(p + 24L);
        this.m24 = MemoryUtil.memGetFloat(p + 28L);
        this.m31 = MemoryUtil.memGetFloat(p + 32L);
        this.m32 = MemoryUtil.memGetFloat(p + 36L);
        this.m33 = MemoryUtil.memGetFloat(p + 40L);
        this.m34 = MemoryUtil.memGetFloat(p + 44L);
        this.m41 = MemoryUtil.memGetFloat(p + 48L);
        this.m42 = MemoryUtil.memGetFloat(p + 52L);
        this.m43 = MemoryUtil.memGetFloat(p + 56L);
        this.m44 = MemoryUtil.memGetFloat(p + 60L);
    }

    public void store(@Nonnull Matrix4 m) {
        m.m11 = this.m11;
        m.m12 = this.m12;
        m.m13 = this.m13;
        m.m14 = this.m14;
        m.m21 = this.m21;
        m.m22 = this.m22;
        m.m23 = this.m23;
        m.m24 = this.m24;
        m.m31 = this.m31;
        m.m32 = this.m32;
        m.m33 = this.m33;
        m.m34 = this.m34;
        m.m41 = this.m41;
        m.m42 = this.m42;
        m.m43 = this.m43;
        m.m44 = this.m44;
    }

    public void store(@Nonnull float[] a) {
        a[0] = this.m11;
        a[1] = this.m12;
        a[2] = this.m13;
        a[3] = this.m14;
        a[4] = this.m21;
        a[5] = this.m22;
        a[6] = this.m23;
        a[7] = this.m24;
        a[8] = this.m31;
        a[9] = this.m32;
        a[10] = this.m33;
        a[11] = this.m34;
        a[12] = this.m41;
        a[13] = this.m42;
        a[14] = this.m43;
        a[15] = this.m44;
    }

    public void store(@Nonnull float[] a, int offset) {
        a[offset] = this.m11;
        a[offset + 1] = this.m12;
        a[offset + 2] = this.m13;
        a[offset + 3] = this.m14;
        a[offset + 4] = this.m21;
        a[offset + 5] = this.m22;
        a[offset + 6] = this.m23;
        a[offset + 7] = this.m24;
        a[offset + 8] = this.m31;
        a[offset + 9] = this.m32;
        a[offset + 10] = this.m33;
        a[offset + 11] = this.m34;
        a[offset + 12] = this.m41;
        a[offset + 13] = this.m42;
        a[offset + 14] = this.m43;
        a[offset + 15] = this.m44;
    }

    public void store(@Nonnull ByteBuffer a) {
        int offset = a.position();
        a.putFloat(offset, this.m11);
        a.putFloat(offset + 4, this.m12);
        a.putFloat(offset + 8, this.m13);
        a.putFloat(offset + 12, this.m14);
        a.putFloat(offset + 16, this.m21);
        a.putFloat(offset + 20, this.m22);
        a.putFloat(offset + 24, this.m23);
        a.putFloat(offset + 28, this.m24);
        a.putFloat(offset + 32, this.m31);
        a.putFloat(offset + 36, this.m32);
        a.putFloat(offset + 40, this.m33);
        a.putFloat(offset + 44, this.m34);
        a.putFloat(offset + 48, this.m41);
        a.putFloat(offset + 52, this.m42);
        a.putFloat(offset + 56, this.m43);
        a.putFloat(offset + 60, this.m44);
    }

    public void store(@Nonnull FloatBuffer a) {
        int offset = a.position();
        a.put(offset, this.m11);
        a.put(offset + 1, this.m12);
        a.put(offset + 2, this.m13);
        a.put(offset + 3, this.m14);
        a.put(offset + 4, this.m21);
        a.put(offset + 5, this.m22);
        a.put(offset + 6, this.m23);
        a.put(offset + 7, this.m24);
        a.put(offset + 8, this.m31);
        a.put(offset + 9, this.m32);
        a.put(offset + 10, this.m33);
        a.put(offset + 11, this.m34);
        a.put(offset + 12, this.m41);
        a.put(offset + 13, this.m42);
        a.put(offset + 14, this.m43);
        a.put(offset + 15, this.m44);
    }

    public void store(long p) {
        MemoryUtil.memPutFloat(p, this.m11);
        MemoryUtil.memPutFloat(p + 1L, this.m12);
        MemoryUtil.memPutFloat(p + 2L, this.m13);
        MemoryUtil.memPutFloat(p + 3L, this.m14);
        MemoryUtil.memPutFloat(p + 4L, this.m21);
        MemoryUtil.memPutFloat(p + 5L, this.m22);
        MemoryUtil.memPutFloat(p + 6L, this.m23);
        MemoryUtil.memPutFloat(p + 7L, this.m24);
        MemoryUtil.memPutFloat(p + 8L, this.m31);
        MemoryUtil.memPutFloat(p + 9L, this.m32);
        MemoryUtil.memPutFloat(p + 10L, this.m33);
        MemoryUtil.memPutFloat(p + 11L, this.m34);
        MemoryUtil.memPutFloat(p + 12L, this.m41);
        MemoryUtil.memPutFloat(p + 13L, this.m42);
        MemoryUtil.memPutFloat(p + 14L, this.m43);
        MemoryUtil.memPutFloat(p + 15L, this.m44);
    }

    public float determinant() {
        return (this.m11 * this.m22 - this.m12 * this.m21) * (this.m33 * this.m44 - this.m34 * this.m43) - (this.m11 * this.m23 - this.m13 * this.m21) * (this.m32 * this.m44 - this.m34 * this.m42) + (this.m11 * this.m24 - this.m14 * this.m21) * (this.m32 * this.m43 - this.m33 * this.m42) + (this.m12 * this.m23 - this.m13 * this.m22) * (this.m31 * this.m44 - this.m34 * this.m41) - (this.m12 * this.m24 - this.m14 * this.m22) * (this.m31 * this.m43 - this.m33 * this.m41) + (this.m13 * this.m24 - this.m14 * this.m23) * (this.m31 * this.m42 - this.m32 * this.m41);
    }

    public float trace() {
        return this.m11 + this.m22 + this.m33 + this.m44;
    }

    public void transpose() {
        float f12 = this.m21;
        float f13 = this.m31;
        float f14 = this.m41;
        float f21 = this.m12;
        float f23 = this.m32;
        float f24 = this.m42;
        float f31 = this.m13;
        float f32 = this.m23;
        float f34 = this.m43;
        float f41 = this.m14;
        float f42 = this.m24;
        float f43 = this.m34;
        this.m12 = f12;
        this.m13 = f13;
        this.m14 = f14;
        this.m21 = f21;
        this.m23 = f23;
        this.m24 = f24;
        this.m31 = f31;
        this.m32 = f32;
        this.m34 = f34;
        this.m41 = f41;
        this.m42 = f42;
        this.m43 = f43;
    }

    public boolean invert() {
        return this.invert(this);
    }

    public boolean invert(@Nonnull Matrix4 dest) {
        float b00 = this.m11 * this.m22 - this.m12 * this.m21;
        float b01 = this.m11 * this.m23 - this.m13 * this.m21;
        float b02 = this.m11 * this.m24 - this.m14 * this.m21;
        float b03 = this.m12 * this.m23 - this.m13 * this.m22;
        float b04 = this.m12 * this.m24 - this.m14 * this.m22;
        float b05 = this.m13 * this.m24 - this.m14 * this.m23;
        float b06 = this.m31 * this.m42 - this.m32 * this.m41;
        float b07 = this.m31 * this.m43 - this.m33 * this.m41;
        float b08 = this.m31 * this.m44 - this.m34 * this.m41;
        float b09 = this.m32 * this.m43 - this.m33 * this.m42;
        float b10 = this.m32 * this.m44 - this.m34 * this.m42;
        float b11 = this.m33 * this.m44 - this.m34 * this.m43;
        float det = b00 * b11 - b01 * b10 + b02 * b09 + b03 * b08 - b04 * b07 + b05 * b06;
        if (MathUtil.isApproxZero(det)) {
            return false;
        } else {
            det = 1.0F / det;
            float f11 = (this.m22 * b11 - this.m23 * b10 + this.m24 * b09) * det;
            float f12 = (this.m23 * b08 - this.m21 * b11 - this.m24 * b07) * det;
            float f13 = (this.m21 * b10 - this.m22 * b08 + this.m24 * b06) * det;
            float f14 = (this.m22 * b07 - this.m21 * b09 - this.m23 * b06) * det;
            float f21 = (this.m13 * b10 - this.m12 * b11 - this.m14 * b09) * det;
            float f22 = (this.m11 * b11 - this.m13 * b08 + this.m14 * b07) * det;
            float f23 = (this.m12 * b08 - this.m11 * b10 - this.m14 * b06) * det;
            float f24 = (this.m11 * b09 - this.m12 * b07 + this.m13 * b06) * det;
            float f31 = (this.m42 * b05 - this.m43 * b04 + this.m44 * b03) * det;
            float f32 = (this.m43 * b02 - this.m41 * b05 - this.m44 * b01) * det;
            float f33 = (this.m41 * b04 - this.m42 * b02 + this.m44 * b00) * det;
            float f34 = (this.m42 * b01 - this.m41 * b03 - this.m43 * b00) * det;
            float f41 = (this.m33 * b04 - this.m32 * b05 - this.m34 * b03) * det;
            float f42 = (this.m31 * b05 - this.m33 * b02 + this.m34 * b01) * det;
            float f43 = (this.m32 * b02 - this.m31 * b04 - this.m34 * b00) * det;
            float f44 = (this.m31 * b03 - this.m32 * b01 + this.m33 * b00) * det;
            dest.m11 = f11;
            dest.m21 = f12;
            dest.m31 = f13;
            dest.m41 = f14;
            dest.m12 = f21;
            dest.m22 = f22;
            dest.m32 = f23;
            dest.m42 = f24;
            dest.m13 = f31;
            dest.m23 = f32;
            dest.m33 = f33;
            dest.m43 = f34;
            dest.m14 = f41;
            dest.m24 = f42;
            dest.m34 = f43;
            dest.m44 = f44;
            return true;
        }
    }

    @Nonnull
    public Matrix4 setOrthographic(float left, float right, float bottom, float top, float near, float far) {
        float invRL = 1.0F / (right - left);
        float invTB = 1.0F / (top - bottom);
        float invNF = 1.0F / (near - far);
        this.m11 = 2.0F * invRL;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 2.0F * invTB;
        this.m23 = 0.0F;
        this.m24 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 2.0F * invNF;
        this.m34 = 0.0F;
        this.m41 = -(right + left) * invRL;
        this.m42 = -(top + bottom) * invTB;
        this.m43 = (near + far) * invNF;
        this.m44 = 1.0F;
        return this;
    }

    @Nonnull
    public Matrix4 setOrthographic(float width, float height, float near, float far, boolean flipY) {
        float invNF = 1.0F / (near - far);
        this.m11 = 2.0F / width;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = flipY ? -2.0F / height : 2.0F / height;
        this.m23 = 0.0F;
        this.m24 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 2.0F * invNF;
        this.m34 = 0.0F;
        this.m41 = -1.0F;
        this.m42 = flipY ? 1.0F : -1.0F;
        this.m43 = (near + far) * invNF;
        this.m44 = 1.0F;
        return this;
    }

    public void setOrthographic(float left, float right, float bottom, float top, float near, float far, boolean signed) {
        this.m11 = 2.0F / (right - left);
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 2.0F / (top - bottom);
        this.m23 = 0.0F;
        this.m24 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = (signed ? 2.0F : 1.0F) / (near - far);
        this.m34 = 0.0F;
        this.m41 = (right + left) / (left - right);
        this.m42 = (top + bottom) / (bottom - top);
        this.m43 = (signed ? far + near : near) / (near - far);
        this.m44 = 1.0F;
    }

    public void setOrthographicLH(float left, float right, float bottom, float top, float near, float far, boolean signed) {
        this.m11 = 2.0F / (right - left);
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 2.0F / (top - bottom);
        this.m23 = 0.0F;
        this.m24 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = (signed ? 2.0F : 1.0F) / (far - near);
        this.m34 = 0.0F;
        this.m41 = (right + left) / (left - right);
        this.m42 = (top + bottom) / (bottom - top);
        this.m43 = (signed ? far + near : near) / (near - far);
        this.m44 = 1.0F;
    }

    @Nonnull
    public Matrix4 setPerspective(float left, float right, float bottom, float top, float near, float far) {
        float invRL = 1.0F / (right - left);
        float invTB = 1.0F / (top - bottom);
        float invNF = 1.0F / (near - far);
        float tNear = 2.0F * near;
        this.m11 = tNear * invRL;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = tNear * invTB;
        this.m23 = 0.0F;
        this.m24 = 0.0F;
        this.m31 = (right + left) * invRL;
        this.m32 = (top + bottom) * invTB;
        this.m33 = (near + far) * invNF;
        this.m34 = -1.0F;
        this.m41 = 0.0F;
        this.m42 = 0.0F;
        this.m43 = tNear * far * invNF;
        this.m44 = 0.0F;
        return this;
    }

    @Nonnull
    public Matrix4 setPerspective(float fov, float aspect, float near, float far) {
        float y = 1.0F / MathUtil.tan(fov * 0.5F);
        float invNF = 1.0F / (near - far);
        this.m11 = y / aspect;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = y;
        this.m23 = 0.0F;
        this.m24 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = (near + far) * invNF;
        this.m34 = -1.0F;
        this.m41 = 0.0F;
        this.m42 = 0.0F;
        this.m43 = 2.0F * far * near * invNF;
        this.m44 = 0.0F;
        return this;
    }

    public void setPerspective(double fov, double aspect, float near, float far, boolean signed) {
        double h = Math.tan(fov * 0.5);
        this.m11 = (float) (1.0 / (h * aspect));
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = (float) (1.0 / h);
        this.m23 = 0.0F;
        this.m24 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        if (far == Float.POSITIVE_INFINITY) {
            this.m33 = -0.99999F;
            this.m43 = (1.0E-5F - (signed ? 2.0F : 1.0F)) * near;
        } else if (near == Float.POSITIVE_INFINITY) {
            this.m33 = (signed ? 1.0F : 0.0F) - 1.0E-5F;
            this.m43 = ((signed ? 2.0F : 1.0F) - 1.0E-5F) * far;
        } else {
            this.m33 = (signed ? far + near : far) / (near - far);
            this.m43 = (signed ? far + far : far) * near / (near - far);
        }
        this.m34 = -1.0F;
        this.m41 = 0.0F;
        this.m42 = 0.0F;
        this.m44 = 0.0F;
    }

    public void setPerspectiveLH(double fov, double aspect, float near, float far, boolean signed) {
        double h = Math.tan(fov * 0.5);
        this.m11 = (float) (1.0 / (h * aspect));
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = (float) (1.0 / h);
        this.m23 = 0.0F;
        this.m24 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        if (far == Float.POSITIVE_INFINITY) {
            this.m33 = -0.99999F;
            this.m43 = (1.0E-5F - (signed ? 2.0F : 1.0F)) * near;
        } else if (near == Float.POSITIVE_INFINITY) {
            this.m33 = (signed ? 1.0F : 0.0F) - 1.0E-5F;
            this.m43 = ((signed ? 2.0F : 1.0F) - 1.0E-5F) * far;
        } else {
            this.m33 = (signed ? far + near : far) / (far - near);
            this.m43 = (signed ? far + far : far) * near / (near - far);
        }
        this.m34 = 1.0F;
        this.m41 = 0.0F;
        this.m42 = 0.0F;
        this.m44 = 0.0F;
    }

    public void preTranslateX(float dx) {
        this.m41 = this.m41 + dx * this.m11;
        this.m42 = this.m42 + dx * this.m12;
        this.m43 = this.m43 + dx * this.m13;
        this.m44 = this.m44 + dx * this.m14;
    }

    public void postTranslateX(float dx) {
        this.m11 = this.m11 + dx * this.m14;
        this.m21 = this.m21 + dx * this.m24;
        this.m31 = this.m31 + dx * this.m34;
        this.m41 = this.m41 + dx * this.m44;
    }

    public void preTranslateY(float dy) {
        this.m41 = this.m41 + dy * this.m21;
        this.m42 = this.m42 + dy * this.m22;
        this.m43 = this.m43 + dy * this.m23;
        this.m44 = this.m44 + dy * this.m24;
    }

    public void postTranslateY(float dy) {
        this.m12 = this.m12 + dy * this.m14;
        this.m22 = this.m22 + dy * this.m24;
        this.m32 = this.m32 + dy * this.m34;
        this.m42 = this.m42 + dy * this.m44;
    }

    public void preTranslateZ(float dz) {
        this.m41 = this.m41 + dz * this.m31;
        this.m42 = this.m42 + dz * this.m32;
        this.m43 = this.m43 + dz * this.m33;
        this.m44 = this.m44 + dz * this.m34;
    }

    public void postTranslateZ(float dz) {
        this.m13 = this.m13 + dz * this.m14;
        this.m23 = this.m23 + dz * this.m24;
        this.m33 = this.m33 + dz * this.m34;
        this.m43 = this.m43 + dz * this.m44;
    }

    public void preTranslate(@Nonnull Vector3 t) {
        this.preTranslate(t.x, t.y, t.z);
    }

    public void preTranslate(float dx, float dy, float dz) {
        this.m41 = this.m41 + dx * this.m11 + dy * this.m21 + dz * this.m31;
        this.m42 = this.m42 + dx * this.m12 + dy * this.m22 + dz * this.m32;
        this.m43 = this.m43 + dx * this.m13 + dy * this.m23 + dz * this.m33;
        this.m44 = this.m44 + dx * this.m14 + dy * this.m24 + dz * this.m34;
    }

    public void preTranslate(float dx, float dy) {
        this.m41 = this.m41 + dx * this.m11 + dy * this.m21;
        this.m42 = this.m42 + dx * this.m12 + dy * this.m22;
        this.m43 = this.m43 + dx * this.m13 + dy * this.m23;
        this.m44 = this.m44 + dx * this.m14 + dy * this.m24;
    }

    public void postTranslate(@Nonnull Vector3 t) {
        this.postTranslate(t.x, t.y, t.z);
    }

    public void postTranslate(float dx, float dy, float dz) {
        this.m11 = this.m11 + dx * this.m14;
        this.m12 = this.m12 + dy * this.m14;
        this.m13 = this.m13 + dz * this.m14;
        this.m21 = this.m21 + dx * this.m24;
        this.m22 = this.m22 + dy * this.m24;
        this.m23 = this.m23 + dz * this.m24;
        this.m31 = this.m31 + dx * this.m34;
        this.m32 = this.m32 + dy * this.m34;
        this.m33 = this.m33 + dz * this.m34;
        this.m41 = this.m41 + dx * this.m44;
        this.m42 = this.m42 + dy * this.m44;
        this.m43 = this.m43 + dz * this.m44;
    }

    public void postTranslate(float dx, float dy) {
        this.m11 = this.m11 + dx * this.m14;
        this.m12 = this.m12 + dy * this.m14;
        this.m21 = this.m21 + dx * this.m24;
        this.m22 = this.m22 + dy * this.m24;
        this.m31 = this.m31 + dx * this.m34;
        this.m32 = this.m32 + dy * this.m34;
        this.m41 = this.m41 + dx * this.m44;
        this.m42 = this.m42 + dy * this.m44;
    }

    public void setTranslate(@Nonnull Vector3 t) {
        this.setTranslate(t.x, t.y, t.z);
    }

    public void setTranslate(float x, float y, float z) {
        this.m11 = 1.0F;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 1.0F;
        this.m23 = 0.0F;
        this.m24 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
        this.m34 = 0.0F;
        this.m41 = x;
        this.m42 = y;
        this.m43 = z;
        this.m44 = 1.0F;
    }

    public void preScaleX(float s) {
        this.m11 *= s;
        this.m12 *= s;
        this.m13 *= s;
        this.m14 *= s;
    }

    public void postScaleX(float s) {
        this.m11 *= s;
        this.m21 *= s;
        this.m31 *= s;
        this.m41 *= s;
    }

    public void preScaleY(float s) {
        this.m21 *= s;
        this.m22 *= s;
        this.m23 *= s;
        this.m24 *= s;
    }

    public void postScaleY(float s) {
        this.m12 *= s;
        this.m22 *= s;
        this.m32 *= s;
        this.m42 *= s;
    }

    public void preScaleZ(float s) {
        this.m31 *= s;
        this.m32 *= s;
        this.m33 *= s;
        this.m34 *= s;
    }

    public void postScaleZ(float s) {
        this.m13 *= s;
        this.m23 *= s;
        this.m33 *= s;
        this.m43 *= s;
    }

    public void preScale(@Nonnull Vector3 s) {
        this.preScale(s.x, s.y, s.z);
    }

    public void preScale(float sx, float sy, float sz) {
        this.m11 *= sx;
        this.m12 *= sx;
        this.m13 *= sx;
        this.m14 *= sx;
        this.m21 *= sy;
        this.m22 *= sy;
        this.m23 *= sy;
        this.m24 *= sy;
        this.m31 *= sz;
        this.m32 *= sz;
        this.m33 *= sz;
        this.m34 *= sz;
    }

    public void preScale(float sx, float sy) {
        this.m11 *= sx;
        this.m12 *= sx;
        this.m13 *= sx;
        this.m14 *= sx;
        this.m21 *= sy;
        this.m22 *= sy;
        this.m23 *= sy;
        this.m24 *= sy;
    }

    public void postScale(@Nonnull Vector3 s) {
        this.postScale(s.x, s.y, s.z);
    }

    public void postScale(float sx, float sy, float sz) {
        this.m11 *= sx;
        this.m21 *= sx;
        this.m31 *= sx;
        this.m41 *= sx;
        this.m12 *= sy;
        this.m22 *= sy;
        this.m32 *= sy;
        this.m42 *= sy;
        this.m13 *= sz;
        this.m23 *= sz;
        this.m33 *= sz;
        this.m43 *= sz;
    }

    public void postScale(float sx, float sy) {
        this.m11 *= sx;
        this.m21 *= sx;
        this.m31 *= sx;
        this.m41 *= sx;
        this.m12 *= sy;
        this.m22 *= sy;
        this.m32 *= sy;
        this.m42 *= sy;
    }

    public void setScale(@Nonnull Vector3 s) {
        this.setScale(s.x, s.y, s.z);
    }

    public void setScale(float x, float y, float z) {
        this.m11 = x;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m14 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = y;
        this.m23 = 0.0F;
        this.m24 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = z;
        this.m34 = 0.0F;
        this.m41 = 0.0F;
        this.m42 = 0.0F;
        this.m43 = 0.0F;
        this.m44 = 1.0F;
    }

    public void preShear(float sxy, float sxz, float syx, float syz, float szx, float szy) {
        float f11 = this.m11 + sxy * this.m21 + sxz * this.m31;
        float f12 = this.m12 + sxy * this.m22 + sxz * this.m32;
        float f13 = this.m13 + sxy * this.m23 + sxz * this.m33;
        float f14 = this.m14 + sxy * this.m24 + sxz * this.m34;
        float f21 = syx * this.m11 + this.m21 + syz * this.m31;
        float f22 = syx * this.m12 + this.m22 + syz * this.m32;
        float f23 = syx * this.m13 + this.m23 + syz * this.m33;
        float f24 = syx * this.m14 + this.m24 + syz * this.m34;
        float f31 = szx * this.m11 + szy * this.m21 + this.m31;
        float f32 = szx * this.m12 + szy * this.m22 + this.m32;
        float f33 = szx * this.m13 + szy * this.m23 + this.m33;
        float f34 = szx * this.m14 + szy * this.m24 + this.m34;
        this.m11 = f11;
        this.m12 = f12;
        this.m13 = f13;
        this.m14 = f14;
        this.m21 = f21;
        this.m22 = f22;
        this.m23 = f23;
        this.m24 = f24;
        this.m31 = f31;
        this.m32 = f32;
        this.m33 = f33;
        this.m34 = f34;
    }

    public void postShear(float sxy, float sxz, float syx, float syz, float szx, float szy) {
        float f11 = this.m11 + this.m12 * syx + this.m13 * szx;
        float f12 = this.m11 * sxy + this.m12 + this.m13 * szy;
        float f13 = this.m11 * sxz + this.m12 * syz + this.m13;
        float f21 = this.m21 + this.m22 * syx + this.m23 * szx;
        float f22 = this.m21 * sxy + this.m22 + this.m23 * szy;
        float f23 = this.m21 * sxz + this.m22 * syz + this.m23;
        float f31 = this.m31 + this.m32 * syx + this.m33 * szx;
        float f32 = this.m31 * sxy + this.m32 + this.m33 * szy;
        float f33 = this.m31 * sxz + this.m32 * syz + this.m33;
        float f41 = this.m41 + this.m42 * syx + this.m43 * szx;
        float f42 = this.m41 * sxy + this.m42 + this.m43 * szy;
        float f43 = this.m41 * sxz + this.m42 * syz + this.m43;
        this.m11 = f11;
        this.m12 = f12;
        this.m13 = f13;
        this.m21 = f21;
        this.m22 = f22;
        this.m23 = f23;
        this.m31 = f31;
        this.m32 = f32;
        this.m33 = f33;
        this.m41 = f41;
        this.m42 = f42;
        this.m43 = f43;
    }

    public void preShear2D(float sx, float sy) {
        float f11 = this.m11 + sy * this.m21;
        float f12 = this.m12 + sy * this.m22;
        float f13 = this.m13 + sy * this.m23;
        float f14 = this.m14 + sy * this.m24;
        float f21 = sx * this.m11 + this.m21;
        float f22 = sx * this.m12 + this.m22;
        float f23 = sx * this.m13 + this.m23;
        float f24 = sx * this.m14 + this.m24;
        this.m11 = f11;
        this.m12 = f12;
        this.m13 = f13;
        this.m14 = f14;
        this.m21 = f21;
        this.m22 = f22;
        this.m23 = f23;
        this.m24 = f24;
    }

    public void postShear2D(float sx, float sy) {
        float f11 = this.m11 + this.m12 * sx;
        float f12 = this.m11 * sy + this.m12;
        float f21 = this.m21 + this.m22 * sx;
        float f22 = this.m21 * sy + this.m22;
        float f31 = this.m31 + this.m32 * sx;
        float f32 = this.m31 * sy + this.m32;
        float f41 = this.m41 + this.m42 * sx;
        float f42 = this.m41 * sy + this.m42;
        this.m11 = f11;
        this.m12 = f12;
        this.m21 = f21;
        this.m22 = f22;
        this.m31 = f31;
        this.m32 = f32;
        this.m41 = f41;
        this.m42 = f42;
    }

    public void setShear(float sxy, float sxz, float syx, float syz, float szx, float szy) {
        this.m11 = 1.0F;
        this.m12 = sxy;
        this.m13 = sxz;
        this.m14 = 0.0F;
        this.m21 = syx;
        this.m22 = 1.0F;
        this.m23 = syz;
        this.m24 = 0.0F;
        this.m31 = szx;
        this.m32 = szy;
        this.m33 = 1.0F;
        this.m34 = 0.0F;
        this.m41 = 0.0F;
        this.m42 = 0.0F;
        this.m43 = 0.0F;
        this.m44 = 1.0F;
    }

    public void preRotateX(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double f21 = c * (double) this.m21 + s * (double) this.m31;
        double f22 = c * (double) this.m22 + s * (double) this.m32;
        double f23 = c * (double) this.m23 + s * (double) this.m33;
        double f24 = c * (double) this.m24 + s * (double) this.m34;
        this.m31 = (float) (c * (double) this.m31 - s * (double) this.m21);
        this.m32 = (float) (c * (double) this.m32 - s * (double) this.m22);
        this.m33 = (float) (c * (double) this.m33 - s * (double) this.m23);
        this.m34 = (float) (c * (double) this.m34 - s * (double) this.m24);
        this.m21 = (float) f21;
        this.m22 = (float) f22;
        this.m23 = (float) f23;
        this.m24 = (float) f24;
    }

    public void postRotateX(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double f13 = c * (double) this.m13 + s * (double) this.m12;
        double f23 = c * (double) this.m23 + s * (double) this.m22;
        double f33 = c * (double) this.m33 + s * (double) this.m32;
        double f43 = c * (double) this.m43 + s * (double) this.m42;
        this.m12 = (float) (c * (double) this.m12 - s * (double) this.m13);
        this.m22 = (float) (c * (double) this.m22 - s * (double) this.m23);
        this.m32 = (float) (c * (double) this.m32 - s * (double) this.m33);
        this.m42 = (float) (c * (double) this.m42 - s * (double) this.m43);
        this.m13 = (float) f13;
        this.m23 = (float) f23;
        this.m33 = (float) f33;
        this.m43 = (float) f43;
    }

    public void preRotateY(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double f11 = c * (double) this.m11 - s * (double) this.m31;
        double f12 = c * (double) this.m12 - s * (double) this.m32;
        double f13 = c * (double) this.m13 - s * (double) this.m33;
        double f14 = c * (double) this.m14 - s * (double) this.m34;
        this.m31 = (float) (s * (double) this.m11 + c * (double) this.m31);
        this.m32 = (float) (s * (double) this.m12 + c * (double) this.m32);
        this.m33 = (float) (s * (double) this.m13 + c * (double) this.m33);
        this.m34 = (float) (s * (double) this.m14 + c * (double) this.m34);
        this.m11 = (float) f11;
        this.m12 = (float) f12;
        this.m13 = (float) f13;
        this.m14 = (float) f14;
    }

    public void postRotateY(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double f13 = c * (double) this.m13 - s * (double) this.m11;
        double f23 = c * (double) this.m23 - s * (double) this.m21;
        double f33 = c * (double) this.m33 - s * (double) this.m31;
        double f43 = c * (double) this.m43 - s * (double) this.m41;
        this.m11 = (float) (s * (double) this.m13 + c * (double) this.m11);
        this.m21 = (float) (s * (double) this.m23 + c * (double) this.m21);
        this.m31 = (float) (s * (double) this.m33 + c * (double) this.m31);
        this.m41 = (float) (s * (double) this.m43 + c * (double) this.m41);
        this.m13 = (float) f13;
        this.m23 = (float) f23;
        this.m33 = (float) f33;
        this.m43 = (float) f43;
    }

    public void preRotateZ(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double f11 = c * (double) this.m11 + s * (double) this.m21;
        double f12 = c * (double) this.m12 + s * (double) this.m22;
        double f13 = c * (double) this.m13 + s * (double) this.m23;
        double f14 = c * (double) this.m14 + s * (double) this.m24;
        this.m21 = (float) (c * (double) this.m21 - s * (double) this.m11);
        this.m22 = (float) (c * (double) this.m22 - s * (double) this.m12);
        this.m23 = (float) (c * (double) this.m23 - s * (double) this.m13);
        this.m24 = (float) (c * (double) this.m24 - s * (double) this.m14);
        this.m11 = (float) f11;
        this.m12 = (float) f12;
        this.m13 = (float) f13;
        this.m14 = (float) f14;
    }

    public void postRotateZ(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double f12 = c * (double) this.m12 + s * (double) this.m11;
        double f22 = c * (double) this.m22 + s * (double) this.m21;
        double f32 = c * (double) this.m32 + s * (double) this.m31;
        double f42 = c * (double) this.m42 + s * (double) this.m41;
        this.m11 = (float) (c * (double) this.m11 - s * (double) this.m12);
        this.m21 = (float) (c * (double) this.m21 - s * (double) this.m22);
        this.m31 = (float) (c * (double) this.m31 - s * (double) this.m32);
        this.m41 = (float) (c * (double) this.m41 - s * (double) this.m42);
        this.m12 = (float) f12;
        this.m22 = (float) f22;
        this.m32 = (float) f32;
        this.m42 = (float) f42;
    }

    public void preRotate(double angleX, double angleY, double angleZ) {
        this.preRotateX(angleX);
        this.preRotateY(angleY);
        this.preRotateZ(angleZ);
    }

    public void postRotate(double angleX, double angleY, double angleZ) {
        this.postRotateX(angleX);
        this.postRotateY(angleY);
        this.postRotateZ(angleZ);
    }

    public void preRotate(@Nonnull Vector3 axis, float angle) {
        this.preRotate((double) axis.x, (double) axis.y, (double) axis.z, (double) angle);
    }

    public void preRotate(double x, double y, double z, double angle) {
        if (angle != 0.0) {
            angle *= 0.5;
            double s = Math.sin(angle);
            double c = Math.cos(angle);
            x *= s;
            y *= s;
            z *= s;
            double xs = 2.0 * x;
            double ys = 2.0 * y;
            double zs = 2.0 * z;
            double xx = x * xs;
            double xy = x * ys;
            double xz = x * zs;
            double xw = xs * c;
            double yy = y * ys;
            double yz = y * zs;
            double yw = ys * c;
            double zz = z * zs;
            double zw = zs * c;
            x = 1.0 - (yy + zz);
            y = xy + zw;
            z = xz - yw;
            double f11 = x * (double) this.m11 + y * (double) this.m21 + z * (double) this.m31;
            double f12 = x * (double) this.m12 + y * (double) this.m22 + z * (double) this.m32;
            double f13 = x * (double) this.m13 + y * (double) this.m23 + z * (double) this.m33;
            double f14 = x * (double) this.m14 + y * (double) this.m24 + z * (double) this.m34;
            x = xy - zw;
            y = 1.0 - (xx + zz);
            z = yz + xw;
            double f21 = x * (double) this.m11 + y * (double) this.m21 + z * (double) this.m31;
            double f22 = x * (double) this.m12 + y * (double) this.m22 + z * (double) this.m32;
            double f23 = x * (double) this.m13 + y * (double) this.m23 + z * (double) this.m33;
            double f24 = x * (double) this.m14 + y * (double) this.m24 + z * (double) this.m34;
            x = xz + yw;
            y = yz - xw;
            z = 1.0 - (xx + yy);
            double f31 = x * (double) this.m11 + y * (double) this.m21 + z * (double) this.m31;
            double f32 = x * (double) this.m12 + y * (double) this.m22 + z * (double) this.m32;
            double f33 = x * (double) this.m13 + y * (double) this.m23 + z * (double) this.m33;
            double f34 = x * (double) this.m14 + y * (double) this.m24 + z * (double) this.m34;
            this.m11 = (float) f11;
            this.m12 = (float) f12;
            this.m13 = (float) f13;
            this.m14 = (float) f14;
            this.m21 = (float) f21;
            this.m22 = (float) f22;
            this.m23 = (float) f23;
            this.m24 = (float) f24;
            this.m31 = (float) f31;
            this.m32 = (float) f32;
            this.m33 = (float) f33;
            this.m34 = (float) f34;
        }
    }

    public void preRotate(@Nonnull Quaternion q) {
        float sq = q.lengthSq();
        if (!(sq < 1.0E-6F)) {
            float is;
            if (MathUtil.isApproxEqual(sq, 1.0F)) {
                is = 2.0F;
            } else {
                is = 2.0F / sq;
            }
            float xs = is * q.x;
            float ys = is * q.y;
            float zs = is * q.z;
            float xx = q.x * xs;
            float xy = q.x * ys;
            float xz = q.x * zs;
            float xw = xs * q.w;
            float yy = q.y * ys;
            float yz = q.y * zs;
            float yw = ys * q.w;
            float zz = q.z * zs;
            float zw = zs * q.w;
            xs = 1.0F - (yy + zz);
            ys = xy + zw;
            zs = xz - yw;
            float f11 = xs * this.m11 + ys * this.m21 + zs * this.m31;
            float f12 = xs * this.m12 + ys * this.m22 + zs * this.m32;
            float f13 = xs * this.m13 + ys * this.m23 + zs * this.m33;
            float f14 = xs * this.m14 + ys * this.m24 + zs * this.m34;
            xs = xy - zw;
            ys = 1.0F - (xx + zz);
            zs = yz + xw;
            float f21 = xs * this.m11 + ys * this.m21 + zs * this.m31;
            float f22 = xs * this.m12 + ys * this.m22 + zs * this.m32;
            float f23 = xs * this.m13 + ys * this.m23 + zs * this.m33;
            float f24 = xs * this.m14 + ys * this.m24 + zs * this.m34;
            xs = xz + yw;
            ys = yz - xw;
            zs = 1.0F - (xx + yy);
            float f31 = xs * this.m11 + ys * this.m21 + zs * this.m31;
            float f32 = xs * this.m12 + ys * this.m22 + zs * this.m32;
            float f33 = xs * this.m13 + ys * this.m23 + zs * this.m33;
            float f34 = xs * this.m14 + ys * this.m24 + zs * this.m34;
            this.m11 = f11;
            this.m12 = f12;
            this.m13 = f13;
            this.m14 = f14;
            this.m21 = f21;
            this.m22 = f22;
            this.m23 = f23;
            this.m24 = f24;
            this.m31 = f31;
            this.m32 = f32;
            this.m33 = f33;
            this.m34 = f34;
        }
    }

    public void postRotate(double x, double y, double z, double angle) {
        if (angle != 0.0) {
            angle *= 0.5;
            double s = Math.sin(angle);
            double c = Math.cos(angle);
            x *= s;
            y *= s;
            z *= s;
            double xs = 2.0 * x;
            double ys = 2.0 * y;
            double zs = 2.0 * z;
            double xx = x * xs;
            double xy = x * ys;
            double xz = x * zs;
            double xw = xs * c;
            double yy = y * ys;
            double yz = y * zs;
            double yw = ys * c;
            double zz = z * zs;
            double zw = zs * c;
            double f11 = 1.0 - (yy + zz);
            double f12 = xy + zw;
            double f13 = xz - yw;
            double f21 = xy - zw;
            double f22 = 1.0 - (xx + zz);
            double f23 = yz + xw;
            double f31 = xz + yw;
            double f32 = yz - xw;
            double f33 = 1.0 - (xx + yy);
            x = (double) this.m11 * f11 + (double) this.m12 * f21 + (double) this.m13 * f31;
            y = (double) this.m11 * f12 + (double) this.m12 * f22 + (double) this.m13 * f32;
            z = (double) this.m11 * f13 + (double) this.m12 * f23 + (double) this.m13 * f33;
            this.m11 = (float) x;
            this.m12 = (float) y;
            this.m13 = (float) z;
            x = (double) this.m21 * f11 + (double) this.m22 * f21 + (double) this.m23 * f31;
            y = (double) this.m21 * f12 + (double) this.m22 * f22 + (double) this.m23 * f32;
            z = (double) this.m21 * f13 + (double) this.m22 * f23 + (double) this.m23 * f33;
            this.m21 = (float) x;
            this.m22 = (float) y;
            this.m23 = (float) z;
            x = (double) this.m31 * f11 + (double) this.m32 * f21 + (double) this.m33 * f31;
            y = (double) this.m31 * f12 + (double) this.m32 * f22 + (double) this.m33 * f32;
            z = (double) this.m31 * f13 + (double) this.m32 * f23 + (double) this.m33 * f33;
            this.m31 = (float) x;
            this.m32 = (float) y;
            this.m33 = (float) z;
            x = (double) this.m41 * f11 + (double) this.m42 * f21 + (double) this.m43 * f31;
            y = (double) this.m41 * f12 + (double) this.m42 * f22 + (double) this.m43 * f32;
            z = (double) this.m41 * f13 + (double) this.m42 * f23 + (double) this.m43 * f33;
            this.m41 = (float) x;
            this.m42 = (float) y;
            this.m43 = (float) z;
        }
    }

    public void setRotation(@Nonnull Quaternion q) {
        q.toMatrix4(this);
    }

    public void preTransform(@Nonnull Vector4 vec) {
        float x = this.m11 * vec.x + this.m21 * vec.y + this.m31 * vec.z + this.m41 * vec.w;
        float y = this.m12 * vec.x + this.m22 * vec.y + this.m32 * vec.z + this.m42 * vec.w;
        float z = this.m13 * vec.x + this.m23 * vec.y + this.m33 * vec.z + this.m43 * vec.w;
        float w = this.m14 * vec.x + this.m24 * vec.y + this.m34 * vec.z + this.m44 * vec.w;
        vec.x = x;
        vec.y = y;
        vec.z = z;
        vec.w = w;
    }

    public void postTransform(@Nonnull Vector4 vec) {
        float x = this.m11 * vec.x + this.m12 * vec.y + this.m13 * vec.z + this.m14 * vec.w;
        float y = this.m21 * vec.x + this.m22 * vec.y + this.m23 * vec.z + this.m24 * vec.w;
        float z = this.m31 * vec.x + this.m32 * vec.y + this.m33 * vec.z + this.m34 * vec.w;
        float w = this.m41 * vec.x + this.m42 * vec.y + this.m43 * vec.z + this.m44 * vec.w;
        vec.x = x;
        vec.y = y;
        vec.z = z;
        vec.w = w;
    }

    public void preTransform(@Nonnull Vector3 vec) {
        float x = this.m11 * vec.x + this.m21 * vec.y + this.m31 * vec.z + this.m41;
        float y = this.m12 * vec.x + this.m22 * vec.y + this.m32 * vec.z + this.m42;
        float z = this.m13 * vec.x + this.m23 * vec.y + this.m33 * vec.z + this.m43;
        if (this.isAffine()) {
            vec.x = x;
            vec.y = y;
            vec.z = z;
        } else {
            float w = 1.0F / (this.m14 * vec.x + this.m24 * vec.y + this.m34 * vec.z + this.m44);
            vec.x = x * w;
            vec.y = y * w;
            vec.z = z * w;
        }
    }

    public void postTransform(@Nonnull Vector3 vec) {
        float x = this.m11 * vec.x + this.m12 * vec.y + this.m13 * vec.z + this.m14;
        float y = this.m21 * vec.x + this.m22 * vec.y + this.m23 * vec.z + this.m24;
        float z = this.m31 * vec.x + this.m32 * vec.y + this.m33 * vec.z + this.m34;
        if (!this.hasTranslation()) {
            vec.x = x;
            vec.y = y;
            vec.z = z;
        } else {
            float w = 1.0F / (this.m41 * vec.x + this.m42 * vec.y + this.m43 * vec.z + this.m44);
            vec.x = x * w;
            vec.y = y * w;
            vec.z = z * w;
        }
    }

    public void mapRect(@Nonnull Rect2f r) {
        this.mapRect(r.mLeft, r.mTop, r.mRight, r.mBottom, r);
    }

    public void mapRect(@Nonnull Rect2fc r, @Nonnull Rect2f dest) {
        this.mapRect(r.left(), r.top(), r.right(), r.bottom(), dest);
    }

    public void mapRect(float left, float top, float right, float bottom, @Nonnull Rect2f dest) {
        float x1 = this.m11 * left + this.m21 * top + this.m41;
        float y1 = this.m12 * left + this.m22 * top + this.m42;
        float x2 = this.m11 * right + this.m21 * top + this.m41;
        float y2 = this.m12 * right + this.m22 * top + this.m42;
        float x3 = this.m11 * left + this.m21 * bottom + this.m41;
        float y3 = this.m12 * left + this.m22 * bottom + this.m42;
        float x4 = this.m11 * right + this.m21 * bottom + this.m41;
        float y4 = this.m12 * right + this.m22 * bottom + this.m42;
        if (this.hasPerspective()) {
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
        dest.mLeft = MathUtil.min(x1, x2, x3, x4);
        dest.mTop = MathUtil.min(y1, y2, y3, y4);
        dest.mRight = MathUtil.max(x1, x2, x3, x4);
        dest.mBottom = MathUtil.max(y1, y2, y3, y4);
    }

    public void mapRect(@Nonnull Rect2fc r, @Nonnull Rect2i dest) {
        this.mapRect(r.left(), r.top(), r.right(), r.bottom(), dest);
    }

    public void mapRect(@Nonnull Rect2ic r, @Nonnull Rect2i dest) {
        this.mapRect((float) r.left(), (float) r.top(), (float) r.right(), (float) r.bottom(), dest);
    }

    public void mapRect(float left, float top, float right, float bottom, @Nonnull Rect2i dest) {
        float x1 = this.m11 * left + this.m21 * top + this.m41;
        float y1 = this.m12 * left + this.m22 * top + this.m42;
        float x2 = this.m11 * right + this.m21 * top + this.m41;
        float y2 = this.m12 * right + this.m22 * top + this.m42;
        float x3 = this.m11 * left + this.m21 * bottom + this.m41;
        float y3 = this.m12 * left + this.m22 * bottom + this.m42;
        float x4 = this.m11 * right + this.m21 * bottom + this.m41;
        float y4 = this.m12 * right + this.m22 * bottom + this.m42;
        if (this.hasPerspective()) {
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
        dest.mLeft = Math.round(MathUtil.min(x1, x2, x3, x4));
        dest.mTop = Math.round(MathUtil.min(y1, y2, y3, y4));
        dest.mRight = Math.round(MathUtil.max(x1, x2, x3, x4));
        dest.mBottom = Math.round(MathUtil.max(y1, y2, y3, y4));
    }

    public void mapRectOut(@Nonnull Rect2ic r, @Nonnull Rect2i dest) {
        this.mapRectOut((float) r.left(), (float) r.top(), (float) r.right(), (float) r.bottom(), dest);
    }

    public void mapRectOut(@Nonnull Rect2fc r, @Nonnull Rect2i dest) {
        this.mapRectOut(r.left(), r.top(), r.right(), r.bottom(), dest);
    }

    public void mapRectOut(float left, float top, float right, float bottom, @Nonnull Rect2i dest) {
        float x1 = this.m11 * left + this.m21 * top + this.m41;
        float y1 = this.m12 * left + this.m22 * top + this.m42;
        float x2 = this.m11 * right + this.m21 * top + this.m41;
        float y2 = this.m12 * right + this.m22 * top + this.m42;
        float x3 = this.m11 * left + this.m21 * bottom + this.m41;
        float y3 = this.m12 * left + this.m22 * bottom + this.m42;
        float x4 = this.m11 * right + this.m21 * bottom + this.m41;
        float y4 = this.m12 * right + this.m22 * bottom + this.m42;
        if (this.hasPerspective()) {
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
        dest.mLeft = (int) Math.floor((double) MathUtil.min(x1, x2, x3, x4));
        dest.mTop = (int) Math.floor((double) MathUtil.min(y1, y2, y3, y4));
        dest.mRight = (int) Math.ceil((double) MathUtil.max(x1, x2, x3, x4));
        dest.mBottom = (int) Math.ceil((double) MathUtil.max(y1, y2, y3, y4));
    }

    public void mapPoint(@Nonnull float[] p) {
        if (this.isAffine()) {
            float x = this.m11 * p[0] + this.m21 * p[1] + this.m41;
            float y = this.m12 * p[0] + this.m22 * p[1] + this.m42;
            p[0] = x;
            p[1] = y;
        } else {
            float x = this.m11 * p[0] + this.m21 * p[1] + this.m41;
            float y = this.m12 * p[0] + this.m22 * p[1] + this.m42;
            float w = 1.0F / (this.m14 * p[0] + this.m24 * p[1] + this.m44);
            p[0] = x * w;
            p[1] = y * w;
        }
    }

    public float mapPointX(float x, float y) {
        if (this.isAffine()) {
            return this.m11 * x + this.m21 * y + this.m41;
        } else {
            float f = this.m11 * x + this.m21 * y + this.m41;
            float w = 1.0F / (this.m14 * x + this.m24 * y + this.m44);
            return f * w;
        }
    }

    public float mapPointY(float x, float y) {
        if (this.isAffine()) {
            return this.m12 * x + this.m22 * y + this.m42;
        } else {
            float f = this.m12 * x + this.m22 * y + this.m42;
            float w = 1.0F / (this.m14 * x + this.m24 * y + this.m44);
            return f * w;
        }
    }

    public void mapVec3(float[] vec) {
        float x = this.m11 * vec[0] + this.m21 * vec[1] + this.m41 * vec[2];
        float y = this.m12 * vec[0] + this.m22 * vec[1] + this.m42 * vec[2];
        float w = this.m14 * vec[0] + this.m24 * vec[1] + this.m44 * vec[2];
        vec[0] = x;
        vec[1] = y;
        vec[2] = w;
    }

    public boolean isAffine() {
        return MathUtil.isApproxZero(this.m14, this.m24, this.m34) && MathUtil.isApproxEqual(this.m44, 1.0F);
    }

    public boolean isScaleTranslate() {
        return this.isAffine() && MathUtil.isApproxZero(this.m12, this.m13, this.m21) && MathUtil.isApproxZero(this.m23, this.m31, this.m32);
    }

    public boolean isAxisAligned() {
        return this.isAffine() && (MathUtil.isApproxZero(this.m11) && MathUtil.isApproxZero(this.m22) && !MathUtil.isApproxZero(this.m12) && !MathUtil.isApproxZero(this.m21) || MathUtil.isApproxZero(this.m12) && MathUtil.isApproxZero(this.m21) && !MathUtil.isApproxZero(this.m11) && !MathUtil.isApproxZero(this.m22));
    }

    public void normalizePerspective() {
        if (this.m44 != 1.0F && this.m44 != 0.0F && this.m14 == 0.0F && this.m24 == 0.0F && this.m34 == 0.0F) {
            float inv = 1.0F / this.m44;
            this.m11 *= inv;
            this.m12 *= inv;
            this.m13 *= inv;
            this.m21 *= inv;
            this.m22 *= inv;
            this.m23 *= inv;
            this.m31 *= inv;
            this.m32 *= inv;
            this.m33 *= inv;
            this.m41 *= inv;
            this.m42 *= inv;
            this.m43 *= inv;
            this.m44 = 1.0F;
        }
    }

    public boolean hasPerspective() {
        return !this.isAffine();
    }

    public boolean hasTranslation() {
        return !MathUtil.isApproxZero(this.m41, this.m42, this.m43) || !MathUtil.isApproxEqual(this.m44, 1.0F);
    }

    public boolean isIdentity() {
        return MathUtil.isApproxZero(this.m12, this.m13, this.m14) && MathUtil.isApproxZero(this.m21, this.m23, this.m24) && MathUtil.isApproxZero(this.m31, this.m32, this.m34) && MathUtil.isApproxZero(this.m41, this.m42, this.m43) && MathUtil.isApproxEqual(this.m11, this.m22, this.m33, this.m44, 1.0F);
    }

    public void toMatrix(@Nonnull Matrix dest) {
        dest.set(this.m11, this.m12, this.m14, this.m21, this.m22, this.m24, this.m41, this.m42, this.m44);
    }

    @Nonnull
    public Matrix toMatrix() {
        return new Matrix(this.m11, this.m12, this.m14, this.m21, this.m22, this.m24, this.m41, this.m42, this.m44);
    }

    public void toMatrix3(@Nonnull Matrix3 dest) {
        dest.m11 = this.m11;
        dest.m12 = this.m12;
        dest.m13 = this.m13;
        dest.m21 = this.m21;
        dest.m22 = this.m22;
        dest.m23 = this.m23;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.m33 = this.m33;
    }

    @Nonnull
    public Matrix3 toMatrix3() {
        Matrix3 m = new Matrix3();
        this.toMatrix3(m);
        return m;
    }

    public boolean isApproxEqual(@Nonnull Matrix4 m) {
        return MathUtil.isApproxEqual(this.m11, m.m11) && MathUtil.isApproxEqual(this.m12, m.m12) && MathUtil.isApproxEqual(this.m13, m.m13) && MathUtil.isApproxEqual(this.m14, m.m14) && MathUtil.isApproxEqual(this.m21, m.m21) && MathUtil.isApproxEqual(this.m22, m.m22) && MathUtil.isApproxEqual(this.m23, m.m23) && MathUtil.isApproxEqual(this.m24, m.m24) && MathUtil.isApproxEqual(this.m31, m.m31) && MathUtil.isApproxEqual(this.m32, m.m32) && MathUtil.isApproxEqual(this.m33, m.m33) && MathUtil.isApproxEqual(this.m34, m.m34) && MathUtil.isApproxEqual(this.m41, m.m41) && MathUtil.isApproxEqual(this.m42, m.m42) && MathUtil.isApproxEqual(this.m43, m.m43) && MathUtil.isApproxEqual(this.m44, m.m44);
    }

    public int hashCode() {
        int result = Float.hashCode(this.m11);
        result = 31 * result + Float.hashCode(this.m12);
        result = 31 * result + Float.hashCode(this.m13);
        result = 31 * result + Float.hashCode(this.m14);
        result = 31 * result + Float.hashCode(this.m21);
        result = 31 * result + Float.hashCode(this.m22);
        result = 31 * result + Float.hashCode(this.m23);
        result = 31 * result + Float.hashCode(this.m24);
        result = 31 * result + Float.hashCode(this.m31);
        result = 31 * result + Float.hashCode(this.m32);
        result = 31 * result + Float.hashCode(this.m33);
        result = 31 * result + Float.hashCode(this.m34);
        result = 31 * result + Float.hashCode(this.m41);
        result = 31 * result + Float.hashCode(this.m42);
        result = 31 * result + Float.hashCode(this.m43);
        return 31 * result + Float.hashCode(this.m44);
    }

    public boolean equals(Object o) {
        return !(o instanceof Matrix4 m) ? false : this.m11 == m.m11 && this.m12 == m.m12 && this.m13 == m.m13 && this.m14 == m.m14 && this.m21 == m.m21 && this.m22 == m.m22 && this.m23 == m.m23 && this.m24 == m.m24 && this.m31 == m.m31 && this.m32 == m.m32 && this.m33 == m.m33 && this.m34 == m.m34 && this.m41 == m.m41 && this.m42 == m.m42 && this.m43 == m.m43 && this.m44 == m.m44;
    }

    public String toString() {
        return String.format("Matrix4:\n%10.6f %10.6f %10.6f %10.6f\n%10.6f %10.6f %10.6f %10.6f\n%10.6f %10.6f %10.6f %10.6f\n%10.6f %10.6f %10.6f %10.6f", this.m11, this.m12, this.m13, this.m14, this.m21, this.m22, this.m23, this.m24, this.m31, this.m32, this.m33, this.m34, this.m41, this.m42, this.m43, this.m44);
    }

    @Nonnull
    public Matrix4 clone() {
        try {
            return (Matrix4) super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new InternalError(var2);
        }
    }

    private static void mulMatrix(@Nonnull float[] a, @Nonnull float[] b) {
        float var4 = b[0];
        float var5 = b[1];
        float var6 = b[2];
        float var7 = b[3];
        float var8 = b[4];
        float var9 = b[5];
        float var10 = b[6];
        float var11 = b[7];
        float var12 = b[8];
        float var13 = b[9];
        float var14 = b[10];
        float var15 = b[11];
        float var16 = b[12];
        float var17 = b[13];
        float var18 = b[14];
        float var19 = b[15];
        float var20 = a[0];
        float var21 = a[4];
        float var22 = a[8];
        float var23 = a[12];
        a[0] = var20 * var4 + var21 * var5 + var22 * var6 + var23 * var7;
        a[4] = var20 * var8 + var21 * var9 + var22 * var10 + var23 * var11;
        a[8] = var20 * var12 + var21 * var13 + var22 * var14 + var23 * var15;
        a[12] = var20 * var16 + var21 * var17 + var22 * var18 + var23 * var19;
        var20 = a[1];
        var21 = a[5];
        var22 = a[9];
        var23 = a[13];
        a[1] = var20 * var4 + var21 * var5 + var22 * var6 + var23 * var7;
        a[5] = var20 * var8 + var21 * var9 + var22 * var10 + var23 * var11;
        a[9] = var20 * var12 + var21 * var13 + var22 * var14 + var23 * var15;
        a[13] = var20 * var16 + var21 * var17 + var22 * var18 + var23 * var19;
        var20 = a[2];
        var21 = a[6];
        var22 = a[10];
        var23 = a[14];
        a[2] = var20 * var4 + var21 * var5 + var22 * var6 + var23 * var7;
        a[6] = var20 * var8 + var21 * var9 + var22 * var10 + var23 * var11;
        a[10] = var20 * var12 + var21 * var13 + var22 * var14 + var23 * var15;
        a[14] = var20 * var16 + var21 * var17 + var22 * var18 + var23 * var19;
        var20 = a[3];
        var21 = a[7];
        var22 = a[11];
        var23 = a[15];
        a[3] = var20 * var4 + var21 * var5 + var22 * var6 + var23 * var7;
        a[7] = var20 * var8 + var21 * var9 + var22 * var10 + var23 * var11;
        a[11] = var20 * var12 + var21 * var13 + var22 * var14 + var23 * var15;
        a[15] = var20 * var16 + var21 * var17 + var22 * var18 + var23 * var19;
    }

    private static void multiply(@Nonnull float[] a, @Nonnull float[] b, @Nonnull float[] out) {
        float[] temp = new float[28];
        float f11 = a[0] + a[10];
        float f12 = a[1] + a[11];
        float f21 = a[4] + a[14];
        float f22 = a[5] + a[15];
        float g11 = b[0] + b[10];
        float g12 = b[1] + b[11];
        float g21 = b[4] + b[14];
        float g22 = b[5] + b[15];
        float x1 = (f11 + f22) * (g11 + g22);
        float x2 = (f21 + f22) * g11;
        float x3 = f11 * (g12 - g22);
        float x4 = f22 * (g21 - g11);
        float x5 = (f11 + f12) * g22;
        float x6 = (f21 - f11) * (g11 + g12);
        float x7 = (f12 - f22) * (g21 + g22);
        temp[0] = x1 + x4 - x5 + x7;
        temp[1] = x3 + x5;
        temp[2] = x2 + x4;
        temp[3] = x1 - x2 + x3 + x6;
        f11 = a[8] + a[10];
        f12 = a[9] + a[11];
        f21 = a[12] + a[14];
        f22 = a[13] + a[15];
        g11 = b[0];
        g12 = b[1];
        g21 = b[4];
        g22 = b[5];
        x1 = (f11 + f22) * (g11 + g22);
        x2 = (f21 + f22) * g11;
        x3 = f11 * (g12 - g22);
        x4 = f22 * (g21 - g11);
        x5 = (f11 + f12) * g22;
        x6 = (f21 - f11) * (g11 + g12);
        x7 = (f12 - f22) * (g21 + g22);
        temp[4] = x1 + x4 - x5 + x7;
        temp[5] = x3 + x5;
        temp[6] = x2 + x4;
        temp[7] = x1 - x2 + x3 + x6;
        f11 = a[0];
        f12 = a[1];
        f21 = a[4];
        f22 = a[5];
        g11 = b[2] - b[10];
        g12 = b[3] - b[11];
        g21 = b[6] - b[14];
        g22 = b[7] - b[15];
        x1 = (f11 + f22) * (g11 + g22);
        x2 = (f21 + f22) * g11;
        x3 = f11 * (g12 - g22);
        x4 = f22 * (g21 - g11);
        x5 = (f11 + f12) * g22;
        x6 = (f21 - f11) * (g11 + g12);
        x7 = (f12 - f22) * (g21 + g22);
        temp[8] = x1 + x4 - x5 + x7;
        temp[9] = x3 + x5;
        temp[10] = x2 + x4;
        temp[11] = x1 - x2 + x3 + x6;
        f11 = a[10];
        f12 = a[11];
        f21 = a[14];
        f22 = a[15];
        g11 = b[10] - b[0];
        g12 = b[11] - b[1];
        g21 = b[12] - b[4];
        g22 = b[13] - b[5];
        x1 = (f11 + f22) * (g11 + g22);
        x2 = (f21 + f22) * g11;
        x3 = f11 * (g12 - g22);
        x4 = f22 * (g21 - g11);
        x5 = (f11 + f12) * g22;
        x6 = (f21 - f11) * (g11 + g12);
        x7 = (f12 - f22) * (g21 + g22);
        temp[12] = x1 + x4 - x5 + x7;
        temp[13] = x3 + x5;
        temp[14] = x2 + x4;
        temp[15] = x1 - x2 + x3 + x6;
        f11 = a[0] + a[2];
        f12 = a[1] + a[3];
        f21 = a[4] + a[6];
        f22 = a[5] + a[7];
        g11 = b[10];
        g12 = b[11];
        g21 = b[14];
        g22 = b[15];
        x1 = (f11 + f22) * (g11 + g22);
        x2 = (f21 + f22) * g11;
        x3 = f11 * (g12 - g22);
        x4 = f22 * (g21 - g11);
        x5 = (f11 + f12) * g22;
        x6 = (f21 - f11) * (g11 + g12);
        x7 = (f12 - f22) * (g21 + g22);
        temp[16] = x1 + x4 - x5 + x7;
        temp[17] = x3 + x5;
        temp[18] = x2 + x4;
        temp[19] = x1 - x2 + x3 + x6;
        f11 = a[10] - a[0];
        f12 = a[11] - a[1];
        f21 = a[12] - a[4];
        f22 = a[13] - a[5];
        g11 = b[0] + b[2];
        g12 = b[1] + b[3];
        g21 = b[4] + b[6];
        g22 = b[5] + b[7];
        x1 = (f11 + f22) * (g11 + g22);
        x2 = (f21 + f22) * g11;
        x3 = f11 * (g12 - g22);
        x4 = f22 * (g21 - g11);
        x5 = (f11 + f12) * g22;
        x6 = (f21 - f11) * (g11 + g12);
        x7 = (f12 - f22) * (g21 + g22);
        temp[20] = x1 + x4 - x5 + x7;
        temp[21] = x3 + x5;
        temp[22] = x2 + x4;
        temp[23] = x1 - x2 + x3 + x6;
        f11 = a[2] - a[10];
        f12 = a[3] - a[11];
        f21 = a[6] - a[14];
        f22 = a[7] - a[15];
        g11 = b[8] + b[10];
        g12 = b[9] + b[11];
        g21 = b[12] + b[14];
        g22 = b[13] + b[15];
        x1 = (f11 + f22) * (g11 + g22);
        x2 = (f21 + f22) * g11;
        x3 = f11 * (g12 - g22);
        x4 = f22 * (g21 - g11);
        x5 = (f11 + f12) * g22;
        x6 = (f21 - f11) * (g11 + g12);
        x7 = (f12 - f22) * (g21 + g22);
        temp[24] = x1 + x4 - x5 + x7;
        temp[25] = x3 + x5;
        temp[26] = x2 + x4;
        temp[27] = x1 - x2 + x3 + x6;
        out[0] = temp[0] + temp[12] - temp[16] + temp[24];
        out[1] = temp[1] + temp[13] - temp[17] + temp[25];
        out[4] = temp[2] + temp[14] - temp[18] + temp[26];
        out[5] = temp[3] + temp[15] - temp[19] + temp[27];
        out[2] = temp[8] + temp[16];
        out[3] = temp[9] + temp[17];
        out[6] = temp[10] + temp[18];
        out[7] = temp[11] + temp[19];
        out[8] = temp[4] + temp[12];
        out[9] = temp[5] + temp[13];
        out[12] = temp[6] + temp[14];
        out[13] = temp[7] + temp[15];
        out[10] = temp[0] - temp[4] + temp[8] + temp[20];
        out[11] = temp[1] - temp[5] + temp[9] + temp[21];
        out[14] = temp[2] - temp[6] + temp[10] + temp[22];
        out[15] = temp[3] - temp[7] + temp[11] + temp[23];
    }
}