package net.minecraft.world.phys;

import net.minecraft.util.Mth;

public class Vec2 {

    public static final Vec2 ZERO = new Vec2(0.0F, 0.0F);

    public static final Vec2 ONE = new Vec2(1.0F, 1.0F);

    public static final Vec2 UNIT_X = new Vec2(1.0F, 0.0F);

    public static final Vec2 NEG_UNIT_X = new Vec2(-1.0F, 0.0F);

    public static final Vec2 UNIT_Y = new Vec2(0.0F, 1.0F);

    public static final Vec2 NEG_UNIT_Y = new Vec2(0.0F, -1.0F);

    public static final Vec2 MAX = new Vec2(Float.MAX_VALUE, Float.MAX_VALUE);

    public static final Vec2 MIN = new Vec2(Float.MIN_VALUE, Float.MIN_VALUE);

    public final float x;

    public final float y;

    public Vec2(float float0, float float1) {
        this.x = float0;
        this.y = float1;
    }

    public Vec2 scale(float float0) {
        return new Vec2(this.x * float0, this.y * float0);
    }

    public float dot(Vec2 vec0) {
        return this.x * vec0.x + this.y * vec0.y;
    }

    public Vec2 add(Vec2 vec0) {
        return new Vec2(this.x + vec0.x, this.y + vec0.y);
    }

    public Vec2 add(float float0) {
        return new Vec2(this.x + float0, this.y + float0);
    }

    public boolean equals(Vec2 vec0) {
        return this.x == vec0.x && this.y == vec0.y;
    }

    public Vec2 normalized() {
        float $$0 = Mth.sqrt(this.x * this.x + this.y * this.y);
        return $$0 < 1.0E-4F ? ZERO : new Vec2(this.x / $$0, this.y / $$0);
    }

    public float length() {
        return Mth.sqrt(this.x * this.x + this.y * this.y);
    }

    public float lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    public float distanceToSqr(Vec2 vec0) {
        float $$1 = vec0.x - this.x;
        float $$2 = vec0.y - this.y;
        return $$1 * $$1 + $$2 * $$2;
    }

    public Vec2 negated() {
        return new Vec2(-this.x, -this.y);
    }
}