package dev.kosmx.playerAnim.core.util;

public class Vec3d extends Vector3<Double> {

    public Vec3d(Double x, Double y, Double z) {
        super(x, y, z);
    }

    public double squaredDistanceTo(Vec3d vec3d) {
        double a = this.x - vec3d.x;
        double b = this.y - vec3d.y;
        double c = this.z - vec3d.z;
        return a * a + b * b + c * c;
    }

    public Vec3d scale(double scalar) {
        return new Vec3d(this.getX() * scalar, this.getY() * scalar, this.getZ() * scalar);
    }

    public Vec3d add(Vec3d other) {
        return new Vec3d(this.getX() + other.getX(), this.getY() + other.getY(), this.getZ() + other.getZ());
    }

    public double dotProduct(Vec3d other) {
        return this.getX() * other.getX() + this.getY() * other.getY() + this.getZ() * other.getZ();
    }

    public Vec3d crossProduct(Vec3d other) {
        return new Vec3d(this.getY() * other.getZ() - this.getZ() * other.getY(), this.getZ() * other.getX() - this.getX() * other.getZ(), this.getX() * other.getY() - this.getY() * other.getX());
    }

    public Vec3d subtract(Vec3d rhs) {
        return this.add(rhs.scale(-1.0));
    }

    public double distanceTo(Vec3d vec3d) {
        return Math.sqrt(this.squaredDistanceTo(vec3d));
    }
}