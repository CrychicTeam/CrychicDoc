package net.minecraft.core;

import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.stream.IntStream;
import javax.annotation.concurrent.Immutable;
import net.minecraft.Util;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;

@Immutable
public class Vec3i implements Comparable<Vec3i> {

    public static final Codec<Vec3i> CODEC = Codec.INT_STREAM.comapFlatMap(p_123318_ -> Util.fixedSize(p_123318_, 3).map(p_175586_ -> new Vec3i(p_175586_[0], p_175586_[1], p_175586_[2])), p_123313_ -> IntStream.of(new int[] { p_123313_.getX(), p_123313_.getY(), p_123313_.getZ() }));

    public static final Vec3i ZERO = new Vec3i(0, 0, 0);

    private int x;

    private int y;

    private int z;

    public static Codec<Vec3i> offsetCodec(int int0) {
        return ExtraCodecs.validate(CODEC, p_274739_ -> Math.abs(p_274739_.getX()) < int0 && Math.abs(p_274739_.getY()) < int0 && Math.abs(p_274739_.getZ()) < int0 ? DataResult.success(p_274739_) : DataResult.error(() -> "Position out of range, expected at most " + int0 + ": " + p_274739_));
    }

    public Vec3i(int int0, int int1, int int2) {
        this.x = int0;
        this.y = int1;
        this.z = int2;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (!(object0 instanceof Vec3i $$1)) {
            return false;
        } else if (this.getX() != $$1.getX()) {
            return false;
        } else {
            return this.getY() != $$1.getY() ? false : this.getZ() == $$1.getZ();
        }
    }

    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public int compareTo(Vec3i vecI0) {
        if (this.getY() == vecI0.getY()) {
            return this.getZ() == vecI0.getZ() ? this.getX() - vecI0.getX() : this.getZ() - vecI0.getZ();
        } else {
            return this.getY() - vecI0.getY();
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    protected Vec3i setX(int int0) {
        this.x = int0;
        return this;
    }

    protected Vec3i setY(int int0) {
        this.y = int0;
        return this;
    }

    protected Vec3i setZ(int int0) {
        this.z = int0;
        return this;
    }

    public Vec3i offset(int int0, int int1, int int2) {
        return int0 == 0 && int1 == 0 && int2 == 0 ? this : new Vec3i(this.getX() + int0, this.getY() + int1, this.getZ() + int2);
    }

    public Vec3i offset(Vec3i vecI0) {
        return this.offset(vecI0.getX(), vecI0.getY(), vecI0.getZ());
    }

    public Vec3i subtract(Vec3i vecI0) {
        return this.offset(-vecI0.getX(), -vecI0.getY(), -vecI0.getZ());
    }

    public Vec3i multiply(int int0) {
        if (int0 == 1) {
            return this;
        } else {
            return int0 == 0 ? ZERO : new Vec3i(this.getX() * int0, this.getY() * int0, this.getZ() * int0);
        }
    }

    public Vec3i above() {
        return this.above(1);
    }

    public Vec3i above(int int0) {
        return this.relative(Direction.UP, int0);
    }

    public Vec3i below() {
        return this.below(1);
    }

    public Vec3i below(int int0) {
        return this.relative(Direction.DOWN, int0);
    }

    public Vec3i north() {
        return this.north(1);
    }

    public Vec3i north(int int0) {
        return this.relative(Direction.NORTH, int0);
    }

    public Vec3i south() {
        return this.south(1);
    }

    public Vec3i south(int int0) {
        return this.relative(Direction.SOUTH, int0);
    }

    public Vec3i west() {
        return this.west(1);
    }

    public Vec3i west(int int0) {
        return this.relative(Direction.WEST, int0);
    }

    public Vec3i east() {
        return this.east(1);
    }

    public Vec3i east(int int0) {
        return this.relative(Direction.EAST, int0);
    }

    public Vec3i relative(Direction direction0) {
        return this.relative(direction0, 1);
    }

    public Vec3i relative(Direction direction0, int int1) {
        return int1 == 0 ? this : new Vec3i(this.getX() + direction0.getStepX() * int1, this.getY() + direction0.getStepY() * int1, this.getZ() + direction0.getStepZ() * int1);
    }

    public Vec3i relative(Direction.Axis directionAxis0, int int1) {
        if (int1 == 0) {
            return this;
        } else {
            int $$2 = directionAxis0 == Direction.Axis.X ? int1 : 0;
            int $$3 = directionAxis0 == Direction.Axis.Y ? int1 : 0;
            int $$4 = directionAxis0 == Direction.Axis.Z ? int1 : 0;
            return new Vec3i(this.getX() + $$2, this.getY() + $$3, this.getZ() + $$4);
        }
    }

    public Vec3i cross(Vec3i vecI0) {
        return new Vec3i(this.getY() * vecI0.getZ() - this.getZ() * vecI0.getY(), this.getZ() * vecI0.getX() - this.getX() * vecI0.getZ(), this.getX() * vecI0.getY() - this.getY() * vecI0.getX());
    }

    public boolean closerThan(Vec3i vecI0, double double1) {
        return this.distSqr(vecI0) < Mth.square(double1);
    }

    public boolean closerToCenterThan(Position position0, double double1) {
        return this.distToCenterSqr(position0) < Mth.square(double1);
    }

    public double distSqr(Vec3i vecI0) {
        return this.distToLowCornerSqr((double) vecI0.getX(), (double) vecI0.getY(), (double) vecI0.getZ());
    }

    public double distToCenterSqr(Position position0) {
        return this.distToCenterSqr(position0.x(), position0.y(), position0.z());
    }

    public double distToCenterSqr(double double0, double double1, double double2) {
        double $$3 = (double) this.getX() + 0.5 - double0;
        double $$4 = (double) this.getY() + 0.5 - double1;
        double $$5 = (double) this.getZ() + 0.5 - double2;
        return $$3 * $$3 + $$4 * $$4 + $$5 * $$5;
    }

    public double distToLowCornerSqr(double double0, double double1, double double2) {
        double $$3 = (double) this.getX() - double0;
        double $$4 = (double) this.getY() - double1;
        double $$5 = (double) this.getZ() - double2;
        return $$3 * $$3 + $$4 * $$4 + $$5 * $$5;
    }

    public int distManhattan(Vec3i vecI0) {
        float $$1 = (float) Math.abs(vecI0.getX() - this.getX());
        float $$2 = (float) Math.abs(vecI0.getY() - this.getY());
        float $$3 = (float) Math.abs(vecI0.getZ() - this.getZ());
        return (int) ($$1 + $$2 + $$3);
    }

    public int get(Direction.Axis directionAxis0) {
        return directionAxis0.choose(this.x, this.y, this.z);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }

    public String toShortString() {
        return this.getX() + ", " + this.getY() + ", " + this.getZ();
    }
}