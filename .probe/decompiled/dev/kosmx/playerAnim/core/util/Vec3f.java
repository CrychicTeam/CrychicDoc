package dev.kosmx.playerAnim.core.util;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Vec3f extends Vector3<Float> {

    public static final Vec3f ZERO = new Vec3f(0.0F, 0.0F, 0.0F);

    public Vec3f(float x, float y, float z) {
        super(x, y, z);
    }

    public double squaredDistanceTo(Vec3d vec3d) {
        double a = (double) this.x.floatValue() - vec3d.x;
        double b = (double) this.y.floatValue() - vec3d.y;
        double c = (double) this.z.floatValue() - vec3d.z;
        return a * a + b * b + c * c;
    }

    public Vec3f scale(float scalar) {
        return new Vec3f(this.getX() * scalar, this.getY() * scalar, this.getZ() * scalar);
    }

    public Vec3f add(Vec3f other) {
        return new Vec3f(this.getX() + other.getX(), this.getY() + other.getY(), this.getZ() + other.getZ());
    }

    public float dotProduct(Vec3f other) {
        return this.getX() * other.getX() + this.getY() * other.getY() + this.getZ() * other.getZ();
    }

    public Vec3f crossProduct(Vec3f other) {
        return new Vec3f(this.getY() * other.getZ() - this.getZ() * other.getY(), this.getZ() * other.getX() - this.getX() * other.getZ(), this.getX() * other.getY() - this.getY() * other.getX());
    }

    public Vec3f subtract(Vec3f rhs) {
        return this.add(rhs.scale(-1.0F));
    }

    public double distanceTo(Vec3d vec3d) {
        return Math.sqrt(this.squaredDistanceTo(vec3d));
    }
}