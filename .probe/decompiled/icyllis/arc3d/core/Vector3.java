package icyllis.arc3d.core;

import javax.annotation.Nonnull;

public class Vector3 {

    public float x;

    public float y;

    public float z;

    public Vector3() {
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(@Nonnull Vector3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public void add(@Nonnull Vector3 v) {
        this.x = this.x + v.x;
        this.y = this.y + v.y;
        this.z = this.z + v.z;
    }

    public void subtract(@Nonnull Vector3 v) {
        this.x = this.x - v.x;
        this.y = this.y - v.y;
        this.z = this.z - v.z;
    }

    public void multiply(float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
    }

    public void multiply(float mx, float my, float mz) {
        this.x *= mx;
        this.y *= my;
        this.z *= mz;
    }

    public void multiply(@Nonnull Vector3 v) {
        this.x = this.x * v.x;
        this.y = this.y * v.y;
        this.z = this.z * v.z;
    }

    public float length() {
        return MathUtil.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public float lengthSq() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public float dot(float x, float y, float z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public float dot(@Nonnull Vector3 v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public void cross(float x, float y, float z) {
        float f = this.y * z - this.z * y;
        float g = this.z * x - this.x * z;
        float h = this.x * y - this.y * x;
        this.x = f;
        this.y = g;
        this.z = h;
    }

    public void cross(@Nonnull Vector3 v) {
        if (this == v) {
            this.setZero();
        } else {
            float x = this.y * v.z - this.z * v.y;
            float y = this.z * v.x - this.x * v.z;
            float z = this.x * v.y - this.y * v.x;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public void minComponent(@Nonnull Vector3 v) {
        this.x = Math.min(this.x, v.x);
        this.y = Math.min(this.y, v.y);
        this.z = Math.min(this.z, v.z);
    }

    public void maxComponent(@Nonnull Vector3 v) {
        this.x = Math.max(this.x, v.x);
        this.y = Math.max(this.y, v.y);
        this.z = Math.max(this.z, v.z);
    }

    public boolean isNormalized() {
        return MathUtil.isApproxEqual(this.lengthSq(), 1.0F);
    }

    public void normalize() {
        float sq = this.lengthSq();
        if (sq < 1.0E-6F) {
            this.x = 1.0F;
            this.y = 0.0F;
            this.z = 0.0F;
        } else {
            double invNorm = 1.0 / Math.sqrt((double) sq);
            this.x = (float) ((double) this.x * invNorm);
            this.y = (float) ((double) this.y * invNorm);
            this.z = (float) ((double) this.z * invNorm);
        }
    }

    public void setZero() {
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
    }

    public void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public float sum() {
        return this.x + this.y + this.z;
    }

    public float product() {
        return this.x * this.y * this.z;
    }

    public void perpendicular() {
        if (Math.abs(this.x) >= Math.abs(this.y)) {
            float l = 1.0F / MathUtil.sqrt(this.x * this.x + this.z * this.z);
            float t = this.x;
            this.x = -this.z * l;
            this.y = 0.0F;
            this.z = t * l;
        } else {
            float l = 1.0F / MathUtil.sqrt(this.y * this.y + this.z * this.z);
            float t = this.y;
            this.x = 0.0F;
            this.y = this.z * l;
            this.z = -t * l;
        }
    }

    public void projection(@Nonnull Vector3 v) {
        float sq = this.lengthSq();
        if (sq < 1.0E-6F) {
            this.setZero();
        } else {
            float c = this.dot(v) / MathUtil.sqrt(sq);
            this.x = v.x * c;
            this.y = v.y * c;
            this.z = v.z * c;
        }
    }

    @Nonnull
    public Quaternion rotation(float angle) {
        return Quaternion.makeAxisAngle(this, angle);
    }

    public void transform(@Nonnull Matrix4 mat) {
        mat.preTransform(this);
    }

    public void preTransform(@Nonnull Matrix4 mat) {
        mat.postTransform(this);
    }

    public void transform(@Nonnull Quaternion q) {
        float f = q.y * this.z - q.z * this.y + q.w * this.x;
        float g = q.z * this.x - q.x * this.z + q.w * this.y;
        float h = q.x * this.y - q.y * this.x + q.w * this.z;
        this.x = this.x + (q.y * h - q.z * g) * 2.0F;
        this.y = this.y + (q.z * f - q.x * h) * 2.0F;
        this.z = this.z + (q.x * g - q.y * f) * 2.0F;
    }

    public void sort() {
        if (this.x > this.y) {
            float t = this.x;
            this.x = this.y;
            this.y = t;
        }
        if (this.y > this.z) {
            float t = this.y;
            this.y = this.z;
            this.z = t;
        }
        if (this.x > this.y) {
            float t = this.x;
            this.x = this.y;
            this.y = t;
        }
    }

    public void reverse() {
        float t = this.x;
        this.x = this.z;
        this.z = t;
    }

    public boolean equivalent(@Nonnull Vector3 v) {
        return this == v ? true : MathUtil.isApproxEqual(this.x, v.x) && MathUtil.isApproxEqual(this.y, v.y) && MathUtil.isApproxEqual(this.z, v.z);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Vector3 vec = (Vector3) o;
            if (Float.floatToIntBits(vec.x) != Float.floatToIntBits(this.x)) {
                return false;
            } else {
                return Float.floatToIntBits(vec.y) != Float.floatToIntBits(this.y) ? false : Float.floatToIntBits(vec.z) == Float.floatToIntBits(this.z);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.x != 0.0F ? Float.floatToIntBits(this.x) : 0;
        result = 31 * result + (this.y != 0.0F ? Float.floatToIntBits(this.y) : 0);
        return 31 * result + (this.z != 0.0F ? Float.floatToIntBits(this.z) : 0);
    }

    public String toString() {
        return "Vector3(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    @Nonnull
    public Vector3 copy() {
        return new Vector3(this.x, this.y, this.z);
    }
}