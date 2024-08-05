package icyllis.arc3d.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Quaternion {

    protected float x;

    protected float y;

    protected float z;

    protected float w;

    public Quaternion() {
    }

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Nonnull
    public static Quaternion copy(@Nullable Quaternion q) {
        return q == null ? identity() : q.copy();
    }

    @Nonnull
    public static Quaternion identity() {
        Quaternion q = new Quaternion();
        q.w = 1.0F;
        return q;
    }

    @Nonnull
    public static Quaternion makeEulerAngles(float rotationX, float rotationY, float rotationZ) {
        Quaternion q = new Quaternion();
        q.setFromEulerAngles(rotationX, rotationY, rotationZ);
        return q;
    }

    @Nonnull
    public static Quaternion makeAxisAngle(@Nonnull Vector3 axis, float angle) {
        Quaternion q = new Quaternion();
        q.setFromAxisAngle(axis, angle);
        return q;
    }

    @Nonnull
    public static Quaternion makeAxisAngle(float axisX, float axisY, float axisZ, float angle) {
        Quaternion q = new Quaternion();
        q.setFromAxisAngle(axisX, axisY, axisZ, angle);
        return q;
    }

    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void set(@Nonnull Quaternion q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
    }

    public void add(@Nonnull Quaternion q) {
        this.w = this.w + q.w;
        this.x = this.x + q.x;
        this.y = this.y + q.y;
        this.z = this.z + q.z;
    }

    public void subtract(@Nonnull Quaternion q) {
        this.w = this.w - q.w;
        this.x = this.x - q.x;
        this.y = this.y - q.y;
        this.z = this.z - q.z;
    }

    public void multiply(float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        this.w *= s;
    }

    public void multiply(@Nonnull Quaternion q) {
        this.set(this.w * q.x + this.x * q.w + this.y * q.z - this.z * q.y, this.w * q.y - this.x * q.z + this.y * q.w + this.z * q.x, this.w * q.z + this.x * q.y - this.y * q.x + this.z * q.w, this.w * q.w - this.x * q.x - this.y * q.y - this.z * q.z);
    }

    public float length() {
        return MathUtil.sqrt(this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public float lengthSq() {
        return this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public float dot(float x, float y, float z, float w) {
        return this.x * x + this.y * y + this.z * z + this.w * w;
    }

    public float dot(@Nonnull Quaternion q) {
        return this.x * q.x + this.y * q.y + this.z * q.z + this.w * q.w;
    }

    public void inverse() {
        float sq = this.lengthSq();
        if (MathUtil.isApproxEqual(sq, 1.0F)) {
            this.conjugate();
        } else {
            float invSq = 1.0F / sq;
            this.w *= invSq;
            this.x = -this.x * invSq;
            this.y = -this.y * invSq;
            this.z = -this.z * invSq;
        }
    }

    public boolean isNormalized() {
        return MathUtil.isApproxEqual(this.lengthSq(), 1.0F);
    }

    public void normalize() {
        float sq = this.lengthSq();
        if (sq < 1.0E-6F) {
            this.setIdentity();
        } else {
            double invNorm = 1.0 / Math.sqrt((double) sq);
            this.x = (float) ((double) this.x * invNorm);
            this.y = (float) ((double) this.y * invNorm);
            this.z = (float) ((double) this.z * invNorm);
            this.w = (float) ((double) this.w * invNorm);
        }
    }

    public void setZero() {
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
        this.w = 0.0F;
    }

    public boolean isIdentity() {
        return MathUtil.isApproxZero(this.x, this.y, this.z) && MathUtil.isApproxEqual(this.w, 1.0F);
    }

    public void setIdentity() {
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
        this.w = 1.0F;
    }

    public void conjugate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
    }

    public void slerp(@Nonnull Quaternion a, float t) {
        this.slerp(this, a, t);
    }

    public void slerp(@Nonnull Quaternion a, @Nonnull Quaternion b, float t) {
        if (t <= 0.0F) {
            this.set(a);
        } else if (t >= 1.0F) {
            this.set(b);
        } else if (MathUtil.isApproxZero(a.lengthSq())) {
            if (MathUtil.isApproxZero(b.lengthSq())) {
                this.setIdentity();
            } else {
                this.set(b);
            }
        } else if (MathUtil.isApproxZero(b.lengthSq())) {
            this.set(a);
        } else {
            float cosHalfAngle = a.dot(b);
            float s;
            if (cosHalfAngle >= 0.95F) {
                s = 1.0F - t;
            } else if (cosHalfAngle <= -0.99F) {
                t = 0.5F;
                s = 0.5F;
            } else {
                boolean negative = false;
                if (cosHalfAngle <= -1.0E-6F) {
                    cosHalfAngle = -cosHalfAngle;
                    negative = true;
                }
                float halfAngle = MathUtil.acos(cosHalfAngle);
                float sinHalfAngle = MathUtil.sin(halfAngle);
                if (Math.abs(sinHalfAngle) < 0.001F) {
                    t = 0.5F;
                    s = 0.5F;
                } else {
                    sinHalfAngle = 1.0F / sinHalfAngle;
                    s = MathUtil.sin((1.0F - t) * halfAngle) * sinHalfAngle;
                    t = MathUtil.sin(t * halfAngle) * sinHalfAngle;
                    if (negative) {
                        t = -t;
                    }
                }
            }
            this.x = a.x * s + b.x * t;
            this.y = a.y * s + b.y * t;
            this.z = a.z * s + b.z * t;
            this.w = a.w * s + b.w * t;
        }
    }

    public void rotateByAxis(@Nonnull Vector3 axis, float angle) {
        this.rotateByAxis(axis.x, axis.y, axis.z, angle);
    }

    public void rotateByAxis(float axisX, float axisY, float axisZ, float angle) {
        if (angle != 0.0F) {
            angle *= 0.5F;
            float sin = MathUtil.sin(angle);
            float qx = axisX * sin;
            float qy = axisY * sin;
            float qz = axisZ * sin;
            float qw = MathUtil.cos(angle);
            this.set(this.x * qw + this.y * qz - this.z * qy + this.w * qx, -this.x * qz + this.y * qw + this.z * qx + this.w * qy, this.x * qy - this.y * qx + this.z * qw + this.w * qz, -this.x * qx - this.y * qy - this.z * qz + this.w * qw);
        }
    }

    public void rotateX(float angle) {
        if (angle != 0.0F) {
            angle *= 0.5F;
            float sin = MathUtil.sin(angle);
            float cos = MathUtil.cos(angle);
            this.set(this.x * cos + this.w * sin, this.y * cos + this.z * sin, -this.y * sin + this.z * cos, -this.x * sin + this.w * cos);
        }
    }

    public void rotateY(float angle) {
        if (angle != 0.0F) {
            angle *= 0.5F;
            float sin = MathUtil.sin(angle);
            float cos = MathUtil.cos(angle);
            this.set(this.x * cos - this.z * sin, this.y * cos + this.w * sin, this.x * sin + this.z * cos, -this.y * sin + this.w * cos);
        }
    }

    public void rotateZ(float angle) {
        if (angle != 0.0F) {
            angle *= 0.5F;
            float sin = MathUtil.sin(angle);
            float cos = MathUtil.cos(angle);
            this.set(this.x * cos + this.y * sin, -this.x * sin + this.y * cos, this.z * cos + this.w * sin, -this.z * sin + this.w * cos);
        }
    }

    public void rotateByEuler(float rotationX, float rotationY, float rotationZ) {
        this.rotateX(rotationX);
        this.rotateY(rotationY);
        this.rotateZ(rotationZ);
    }

    public void setFromAxisAngle(@Nonnull Vector3 axis, float angle) {
        this.setFromAxisAngle(axis.x, axis.y, axis.z, angle);
    }

    public void setFromAxisAngle(float axisX, float axisY, float axisZ, float angle) {
        if (MathUtil.isApproxZero(axisX, axisY, axisZ)) {
            this.setIdentity();
        } else {
            angle *= 0.5F;
            float sin = MathUtil.sin(angle);
            this.x = axisX * sin;
            this.y = axisY * sin;
            this.z = axisZ * sin;
            this.w = MathUtil.cos(angle);
        }
    }

    public void setFromEulerAngles(float rotationX, float rotationY, float rotationZ) {
        if (MathUtil.isApproxZero(rotationX, rotationY, rotationZ)) {
            this.setIdentity();
        } else {
            rotationX *= 0.5F;
            rotationY *= 0.5F;
            rotationZ *= 0.5F;
            float sx = MathUtil.sin(rotationX);
            float cx = MathUtil.cos(rotationX);
            float sy = MathUtil.sin(rotationY);
            float cy = MathUtil.cos(rotationY);
            float sz = MathUtil.sin(rotationZ);
            float cz = MathUtil.cos(rotationZ);
            float cc = cy * cz;
            float ss = sy * sz;
            float cs = cy * sz;
            float sc = sy * cz;
            this.w = cc * cx - ss * sx;
            this.x = cc * sx + ss * cx;
            this.y = sc * cx + cs * sx;
            this.z = cs * cx - sc * sx;
        }
    }

    public float toAxisAngle(@Nonnull Vector3 axis) {
        float l = this.x * this.x + this.y * this.y + this.z * this.z;
        if (MathUtil.isApproxZero(l)) {
            axis.x = 1.0F;
            axis.y = 0.0F;
            axis.z = 0.0F;
            return 0.0F;
        } else {
            l = 1.0F / MathUtil.sqrt(l);
            axis.x = this.x * l;
            axis.y = this.y * l;
            axis.z = this.z * l;
            return MathUtil.acos(this.w) * 2.0F;
        }
    }

    public float toAxisAngle(@Nonnull float[] axis) {
        if (axis.length < 3) {
            throw new IllegalArgumentException("The array length must be at least 3");
        } else {
            float l = this.x * this.x + this.y * this.y + this.z * this.z;
            if (MathUtil.isApproxZero(l)) {
                axis[0] = 1.0F;
                axis[1] = 0.0F;
                axis[2] = 0.0F;
                return 0.0F;
            } else {
                l = 1.0F / MathUtil.sqrt(l);
                axis[0] = this.x * l;
                axis[1] = this.y * l;
                axis[2] = this.z * l;
                return MathUtil.acos(this.w) * 2.0F;
            }
        }
    }

    public void toEulerAngles(@Nonnull Vector3 result) {
        float sqx = this.x * this.x;
        float sqy = this.y * this.y;
        float sqz = this.z * this.z;
        float sqw = this.w * this.w;
        float sq = sqx + sqy + sqz + sqw;
        float f = this.x * this.y + this.z * this.w;
        if (f > 0.499F * sq) {
            result.x = 0.0F;
            result.y = 2.0F * MathUtil.atan2(this.x, this.w);
            result.z = (float) (Math.PI / 2);
        } else if (f < -0.499F * sq) {
            result.x = 0.0F;
            result.y = -2.0F * MathUtil.atan2(this.x, this.w);
            result.z = (float) (-Math.PI / 2);
        } else {
            result.x = MathUtil.atan2(2.0F * (this.x * this.w - this.y * this.z), -sqx + sqy - sqz + sqw);
            result.y = MathUtil.atan2(2.0F * (this.y * this.w - this.x * this.z), sqx - sqy - sqz + sqw);
            result.z = MathUtil.asin(2.0F * f / sq);
        }
    }

    public void toEulerAngles(@Nonnull float[] angles) {
        if (angles.length < 3) {
            throw new IllegalArgumentException("The array length must be at least 3");
        } else {
            float sqx = this.x * this.x;
            float sqy = this.y * this.y;
            float sqz = this.z * this.z;
            float sqw = this.w * this.w;
            float sq = sqx + sqy + sqz + sqw;
            float f = this.x * this.y + this.z * this.w;
            if (f > 0.499F * sq) {
                angles[0] = 0.0F;
                angles[1] = 2.0F * MathUtil.atan2(this.x, this.w);
                angles[2] = (float) (Math.PI / 2);
            } else if (f < -0.499F * sq) {
                angles[0] = 0.0F;
                angles[1] = -2.0F * MathUtil.atan2(this.x, this.w);
                angles[2] = (float) (-Math.PI / 2);
            } else {
                angles[0] = MathUtil.atan2(2.0F * (this.x * this.w - this.y * this.z), -sqx + sqy - sqz + sqw);
                angles[1] = MathUtil.atan2(2.0F * (this.y * this.w - this.x * this.z), sqx - sqy - sqz + sqw);
                angles[2] = MathUtil.asin(2.0F * f / sq);
            }
        }
    }

    @Nonnull
    public Matrix3 toMatrix3() {
        float sq = this.lengthSq();
        if (sq < 1.0E-6F) {
            return Matrix3.identity();
        } else {
            float is;
            if (MathUtil.isApproxEqual(sq, 1.0F)) {
                is = 2.0F;
            } else {
                is = 2.0F / sq;
            }
            Matrix3 mat = new Matrix3();
            float xs = is * this.x;
            float ys = is * this.y;
            float zs = is * this.z;
            float xx = this.x * xs;
            float xy = this.x * ys;
            float xz = this.x * zs;
            float xw = xs * this.w;
            float yy = this.y * ys;
            float yz = this.y * zs;
            float yw = ys * this.w;
            float zz = this.z * zs;
            float zw = zs * this.w;
            mat.m11 = 1.0F - (yy + zz);
            mat.m22 = 1.0F - (xx + zz);
            mat.m33 = 1.0F - (xx + yy);
            mat.m21 = xy - zw;
            mat.m31 = xz + yw;
            mat.m12 = xy + zw;
            mat.m32 = yz - xw;
            mat.m13 = xz - yw;
            mat.m23 = yz + xw;
            return mat;
        }
    }

    @Nonnull
    public Matrix4 toMatrix4() {
        float sq = this.lengthSq();
        if (sq < 1.0E-6F) {
            return Matrix4.identity();
        } else {
            float is;
            if (MathUtil.isApproxEqual(sq, 1.0F)) {
                is = 2.0F;
            } else {
                is = 2.0F / sq;
            }
            Matrix4 mat = new Matrix4();
            float xs = is * this.x;
            float ys = is * this.y;
            float zs = is * this.z;
            float xx = this.x * xs;
            float xy = this.x * ys;
            float xz = this.x * zs;
            float xw = xs * this.w;
            float yy = this.y * ys;
            float yz = this.y * zs;
            float yw = ys * this.w;
            float zz = this.z * zs;
            float zw = zs * this.w;
            mat.m11 = 1.0F - (yy + zz);
            mat.m22 = 1.0F - (xx + zz);
            mat.m33 = 1.0F - (xx + yy);
            mat.m21 = xy - zw;
            mat.m31 = xz + yw;
            mat.m12 = xy + zw;
            mat.m32 = yz - xw;
            mat.m13 = xz - yw;
            mat.m23 = yz + xw;
            mat.m44 = 1.0F;
            return mat;
        }
    }

    @Nonnull
    public Matrix3 toMatrix3(@Nullable Matrix3 out) {
        if (out == null) {
            return this.toMatrix3();
        } else {
            float sq = this.lengthSq();
            if (sq < 1.0E-6F) {
                out.setIdentity();
                return out;
            } else {
                float inv;
                if (MathUtil.isApproxEqual(sq, 1.0F)) {
                    inv = 2.0F;
                } else {
                    inv = 2.0F / sq;
                }
                float xs = inv * this.x;
                float ys = inv * this.y;
                float zs = inv * this.z;
                float xx = this.x * xs;
                float xy = this.x * ys;
                float xz = this.x * zs;
                float xw = xs * this.w;
                float yy = this.y * ys;
                float yz = this.y * zs;
                float yw = ys * this.w;
                float zz = this.z * zs;
                float zw = zs * this.w;
                out.m11 = 1.0F - (yy + zz);
                out.m21 = xy - zw;
                out.m31 = xz + yw;
                out.m12 = xy + zw;
                out.m22 = 1.0F - (xx + zz);
                out.m32 = yz - xw;
                out.m13 = xz - yw;
                out.m23 = yz + xw;
                out.m33 = 1.0F - (xx + yy);
                return out;
            }
        }
    }

    @Nonnull
    public Matrix4 toMatrix4(@Nullable Matrix4 out) {
        if (out == null) {
            return this.toMatrix4();
        } else {
            float sq = this.lengthSq();
            if (sq < 1.0E-6F) {
                out.setIdentity();
                return out;
            } else {
                float inv;
                if (MathUtil.isApproxEqual(sq, 1.0F)) {
                    inv = 2.0F;
                } else {
                    inv = 2.0F / sq;
                }
                float xs = inv * this.x;
                float ys = inv * this.y;
                float zs = inv * this.z;
                float xx = this.x * xs;
                float xy = this.x * ys;
                float xz = this.x * zs;
                float xw = xs * this.w;
                float yy = this.y * ys;
                float yz = this.y * zs;
                float yw = ys * this.w;
                float zz = this.z * zs;
                float zw = zs * this.w;
                out.m11 = 1.0F - (yy + zz);
                out.m21 = xy - zw;
                out.m31 = xz + yw;
                out.m41 = 0.0F;
                out.m12 = xy + zw;
                out.m22 = 1.0F - (xx + zz);
                out.m32 = yz - xw;
                out.m42 = 0.0F;
                out.m13 = xz - yw;
                out.m23 = yz + xw;
                out.m33 = 1.0F - (xx + yy);
                out.m43 = 0.0F;
                out.m14 = 0.0F;
                out.m24 = 0.0F;
                out.m34 = 0.0F;
                out.m44 = 1.0F;
                return out;
            }
        }
    }

    public boolean isApproxEqual(@Nonnull Quaternion q) {
        return this == q ? true : MathUtil.isApproxEqual(this.x, q.x) && MathUtil.isApproxEqual(this.y, q.y) && MathUtil.isApproxEqual(this.z, q.z) && MathUtil.isApproxEqual(this.w, q.w);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Quaternion quat = (Quaternion) o;
            if (Float.floatToIntBits(quat.x) != Float.floatToIntBits(this.x)) {
                return false;
            } else if (Float.floatToIntBits(quat.y) != Float.floatToIntBits(this.y)) {
                return false;
            } else {
                return Float.floatToIntBits(quat.z) != Float.floatToIntBits(this.z) ? false : Float.floatToIntBits(quat.w) == Float.floatToIntBits(this.w);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.x != 0.0F ? Float.floatToIntBits(this.x) : 0;
        result = 31 * result + (this.y != 0.0F ? Float.floatToIntBits(this.y) : 0);
        result = 31 * result + (this.z != 0.0F ? Float.floatToIntBits(this.z) : 0);
        return 31 * result + (this.w != 0.0F ? Float.floatToIntBits(this.w) : 0);
    }

    public String toString() {
        return "Quat[w: " + this.w + ", x: " + this.x + ", y: " + this.y + ", z: " + this.z + "]";
    }

    @Nonnull
    public Quaternion copy() {
        return new Quaternion(this.x, this.y, this.z, this.w);
    }
}