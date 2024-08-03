package net.minecraft.network.protocol.game;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.VisibleForTesting;

public class VecDeltaCodec {

    private static final double TRUNCATION_STEPS = 4096.0;

    private Vec3 base = Vec3.ZERO;

    @VisibleForTesting
    static long encode(double double0) {
        return Math.round(double0 * 4096.0);
    }

    @VisibleForTesting
    static double decode(long long0) {
        return (double) long0 / 4096.0;
    }

    public Vec3 decode(long long0, long long1, long long2) {
        if (long0 == 0L && long1 == 0L && long2 == 0L) {
            return this.base;
        } else {
            double $$3 = long0 == 0L ? this.base.x : decode(encode(this.base.x) + long0);
            double $$4 = long1 == 0L ? this.base.y : decode(encode(this.base.y) + long1);
            double $$5 = long2 == 0L ? this.base.z : decode(encode(this.base.z) + long2);
            return new Vec3($$3, $$4, $$5);
        }
    }

    public long encodeX(Vec3 vec0) {
        return encode(vec0.x) - encode(this.base.x);
    }

    public long encodeY(Vec3 vec0) {
        return encode(vec0.y) - encode(this.base.y);
    }

    public long encodeZ(Vec3 vec0) {
        return encode(vec0.z) - encode(this.base.z);
    }

    public Vec3 delta(Vec3 vec0) {
        return vec0.subtract(this.base);
    }

    public void setBase(Vec3 vec0) {
        this.base = vec0;
    }
}