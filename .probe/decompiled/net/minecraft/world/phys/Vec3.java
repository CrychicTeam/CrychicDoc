package net.minecraft.world.phys;

import com.mojang.serialization.Codec;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

public class Vec3 implements Position {

    public static final Codec<Vec3> CODEC = Codec.DOUBLE.listOf().comapFlatMap(p_231079_ -> Util.fixedSize(p_231079_, 3).map(p_231081_ -> new Vec3((Double) p_231081_.get(0), (Double) p_231081_.get(1), (Double) p_231081_.get(2))), p_231083_ -> List.of(p_231083_.x(), p_231083_.y(), p_231083_.z()));

    public static final Vec3 ZERO = new Vec3(0.0, 0.0, 0.0);

    public final double x;

    public final double y;

    public final double z;

    public static Vec3 fromRGB24(int int0) {
        double $$1 = (double) (int0 >> 16 & 0xFF) / 255.0;
        double $$2 = (double) (int0 >> 8 & 0xFF) / 255.0;
        double $$3 = (double) (int0 & 0xFF) / 255.0;
        return new Vec3($$1, $$2, $$3);
    }

    public static Vec3 atLowerCornerOf(Vec3i vecI0) {
        return new Vec3((double) vecI0.getX(), (double) vecI0.getY(), (double) vecI0.getZ());
    }

    public static Vec3 atLowerCornerWithOffset(Vec3i vecI0, double double1, double double2, double double3) {
        return new Vec3((double) vecI0.getX() + double1, (double) vecI0.getY() + double2, (double) vecI0.getZ() + double3);
    }

    public static Vec3 atCenterOf(Vec3i vecI0) {
        return atLowerCornerWithOffset(vecI0, 0.5, 0.5, 0.5);
    }

    public static Vec3 atBottomCenterOf(Vec3i vecI0) {
        return atLowerCornerWithOffset(vecI0, 0.5, 0.0, 0.5);
    }

    public static Vec3 upFromBottomCenterOf(Vec3i vecI0, double double1) {
        return atLowerCornerWithOffset(vecI0, 0.5, double1, 0.5);
    }

    public Vec3(double double0, double double1, double double2) {
        this.x = double0;
        this.y = double1;
        this.z = double2;
    }

    public Vec3(Vector3f vectorF0) {
        this((double) vectorF0.x(), (double) vectorF0.y(), (double) vectorF0.z());
    }

    public Vec3 vectorTo(Vec3 vec0) {
        return new Vec3(vec0.x - this.x, vec0.y - this.y, vec0.z - this.z);
    }

    public Vec3 normalize() {
        double $$0 = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return $$0 < 1.0E-4 ? ZERO : new Vec3(this.x / $$0, this.y / $$0, this.z / $$0);
    }

    public double dot(Vec3 vec0) {
        return this.x * vec0.x + this.y * vec0.y + this.z * vec0.z;
    }

    public Vec3 cross(Vec3 vec0) {
        return new Vec3(this.y * vec0.z - this.z * vec0.y, this.z * vec0.x - this.x * vec0.z, this.x * vec0.y - this.y * vec0.x);
    }

    public Vec3 subtract(Vec3 vec0) {
        return this.subtract(vec0.x, vec0.y, vec0.z);
    }

    public Vec3 subtract(double double0, double double1, double double2) {
        return this.add(-double0, -double1, -double2);
    }

    public Vec3 add(Vec3 vec0) {
        return this.add(vec0.x, vec0.y, vec0.z);
    }

    public Vec3 add(double double0, double double1, double double2) {
        return new Vec3(this.x + double0, this.y + double1, this.z + double2);
    }

    public boolean closerThan(Position position0, double double1) {
        return this.distanceToSqr(position0.x(), position0.y(), position0.z()) < double1 * double1;
    }

    public double distanceTo(Vec3 vec0) {
        double $$1 = vec0.x - this.x;
        double $$2 = vec0.y - this.y;
        double $$3 = vec0.z - this.z;
        return Math.sqrt($$1 * $$1 + $$2 * $$2 + $$3 * $$3);
    }

    public double distanceToSqr(Vec3 vec0) {
        double $$1 = vec0.x - this.x;
        double $$2 = vec0.y - this.y;
        double $$3 = vec0.z - this.z;
        return $$1 * $$1 + $$2 * $$2 + $$3 * $$3;
    }

    public double distanceToSqr(double double0, double double1, double double2) {
        double $$3 = double0 - this.x;
        double $$4 = double1 - this.y;
        double $$5 = double2 - this.z;
        return $$3 * $$3 + $$4 * $$4 + $$5 * $$5;
    }

    public Vec3 scale(double double0) {
        return this.multiply(double0, double0, double0);
    }

    public Vec3 reverse() {
        return this.scale(-1.0);
    }

    public Vec3 multiply(Vec3 vec0) {
        return this.multiply(vec0.x, vec0.y, vec0.z);
    }

    public Vec3 multiply(double double0, double double1, double double2) {
        return new Vec3(this.x * double0, this.y * double1, this.z * double2);
    }

    public Vec3 offsetRandom(RandomSource randomSource0, float float1) {
        return this.add((double) ((randomSource0.nextFloat() - 0.5F) * float1), (double) ((randomSource0.nextFloat() - 0.5F) * float1), (double) ((randomSource0.nextFloat() - 0.5F) * float1));
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double lengthSqr() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public double horizontalDistance() {
        return Math.sqrt(this.x * this.x + this.z * this.z);
    }

    public double horizontalDistanceSqr() {
        return this.x * this.x + this.z * this.z;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (!(object0 instanceof Vec3 $$1)) {
            return false;
        } else if (Double.compare($$1.x, this.x) != 0) {
            return false;
        } else {
            return Double.compare($$1.y, this.y) != 0 ? false : Double.compare($$1.z, this.z) == 0;
        }
    }

    public int hashCode() {
        long $$0 = Double.doubleToLongBits(this.x);
        int $$1 = (int) ($$0 ^ $$0 >>> 32);
        $$0 = Double.doubleToLongBits(this.y);
        $$1 = 31 * $$1 + (int) ($$0 ^ $$0 >>> 32);
        $$0 = Double.doubleToLongBits(this.z);
        return 31 * $$1 + (int) ($$0 ^ $$0 >>> 32);
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public Vec3 lerp(Vec3 vec0, double double1) {
        return new Vec3(Mth.lerp(double1, this.x, vec0.x), Mth.lerp(double1, this.y, vec0.y), Mth.lerp(double1, this.z, vec0.z));
    }

    public Vec3 xRot(float float0) {
        float $$1 = Mth.cos(float0);
        float $$2 = Mth.sin(float0);
        double $$3 = this.x;
        double $$4 = this.y * (double) $$1 + this.z * (double) $$2;
        double $$5 = this.z * (double) $$1 - this.y * (double) $$2;
        return new Vec3($$3, $$4, $$5);
    }

    public Vec3 yRot(float float0) {
        float $$1 = Mth.cos(float0);
        float $$2 = Mth.sin(float0);
        double $$3 = this.x * (double) $$1 + this.z * (double) $$2;
        double $$4 = this.y;
        double $$5 = this.z * (double) $$1 - this.x * (double) $$2;
        return new Vec3($$3, $$4, $$5);
    }

    public Vec3 zRot(float float0) {
        float $$1 = Mth.cos(float0);
        float $$2 = Mth.sin(float0);
        double $$3 = this.x * (double) $$1 + this.y * (double) $$2;
        double $$4 = this.y * (double) $$1 - this.x * (double) $$2;
        double $$5 = this.z;
        return new Vec3($$3, $$4, $$5);
    }

    public static Vec3 directionFromRotation(Vec2 vec0) {
        return directionFromRotation(vec0.x, vec0.y);
    }

    public static Vec3 directionFromRotation(float float0, float float1) {
        float $$2 = Mth.cos(-float1 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float $$3 = Mth.sin(-float1 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float $$4 = -Mth.cos(-float0 * (float) (Math.PI / 180.0));
        float $$5 = Mth.sin(-float0 * (float) (Math.PI / 180.0));
        return new Vec3((double) ($$3 * $$4), (double) $$5, (double) ($$2 * $$4));
    }

    public Vec3 align(EnumSet<Direction.Axis> enumSetDirectionAxis0) {
        double $$1 = enumSetDirectionAxis0.contains(Direction.Axis.X) ? (double) Mth.floor(this.x) : this.x;
        double $$2 = enumSetDirectionAxis0.contains(Direction.Axis.Y) ? (double) Mth.floor(this.y) : this.y;
        double $$3 = enumSetDirectionAxis0.contains(Direction.Axis.Z) ? (double) Mth.floor(this.z) : this.z;
        return new Vec3($$1, $$2, $$3);
    }

    public double get(Direction.Axis directionAxis0) {
        return directionAxis0.choose(this.x, this.y, this.z);
    }

    public Vec3 with(Direction.Axis directionAxis0, double double1) {
        double $$2 = directionAxis0 == Direction.Axis.X ? double1 : this.x;
        double $$3 = directionAxis0 == Direction.Axis.Y ? double1 : this.y;
        double $$4 = directionAxis0 == Direction.Axis.Z ? double1 : this.z;
        return new Vec3($$2, $$3, $$4);
    }

    public Vec3 relative(Direction direction0, double double1) {
        Vec3i $$2 = direction0.getNormal();
        return new Vec3(this.x + double1 * (double) $$2.getX(), this.y + double1 * (double) $$2.getY(), this.z + double1 * (double) $$2.getZ());
    }

    @Override
    public final double x() {
        return this.x;
    }

    @Override
    public final double y() {
        return this.y;
    }

    @Override
    public final double z() {
        return this.z;
    }

    public Vector3f toVector3f() {
        return new Vector3f((float) this.x, (float) this.y, (float) this.z);
    }
}