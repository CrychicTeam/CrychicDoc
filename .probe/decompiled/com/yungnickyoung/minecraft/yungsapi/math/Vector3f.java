package com.yungnickyoung.minecraft.yungsapi.math;

public class Vector3f {

    public float x;

    public float y;

    public float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(float[] v) {
        this(v[0], v[1], v[2]);
    }

    public Vector3f(Vector3f v1) {
        this(v1.x, v1.y, v1.z);
    }

    public final float dot(Vector3f v1) {
        return this.x * v1.x + this.y * v1.y + this.z * v1.z;
    }

    public final float length() {
        return (float) Math.sqrt((double) (this.x * this.x + this.y * this.y + this.z * this.z));
    }

    public final float lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
}