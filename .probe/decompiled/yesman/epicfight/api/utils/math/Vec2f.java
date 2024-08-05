package yesman.epicfight.api.utils.math;

public class Vec2f {

    public float x;

    public float y;

    public Vec2f() {
        this.x = 0.0F;
        this.y = 0.0F;
    }

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2f scale(float f) {
        this.x *= f;
        this.y *= f;
        return this;
    }

    public String toString() {
        return "Vec2f[" + this.x + ", " + this.y + ", ]";
    }
}