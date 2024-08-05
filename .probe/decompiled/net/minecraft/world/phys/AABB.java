package net.minecraft.world.phys;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class AABB {

    private static final double EPSILON = 1.0E-7;

    public final double minX;

    public final double minY;

    public final double minZ;

    public final double maxX;

    public final double maxY;

    public final double maxZ;

    public AABB(double double0, double double1, double double2, double double3, double double4, double double5) {
        this.minX = Math.min(double0, double3);
        this.minY = Math.min(double1, double4);
        this.minZ = Math.min(double2, double5);
        this.maxX = Math.max(double0, double3);
        this.maxY = Math.max(double1, double4);
        this.maxZ = Math.max(double2, double5);
    }

    public AABB(BlockPos blockPos0) {
        this((double) blockPos0.m_123341_(), (double) blockPos0.m_123342_(), (double) blockPos0.m_123343_(), (double) (blockPos0.m_123341_() + 1), (double) (blockPos0.m_123342_() + 1), (double) (blockPos0.m_123343_() + 1));
    }

    public AABB(BlockPos blockPos0, BlockPos blockPos1) {
        this((double) blockPos0.m_123341_(), (double) blockPos0.m_123342_(), (double) blockPos0.m_123343_(), (double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_());
    }

    public AABB(Vec3 vec0, Vec3 vec1) {
        this(vec0.x, vec0.y, vec0.z, vec1.x, vec1.y, vec1.z);
    }

    public static AABB of(BoundingBox boundingBox0) {
        return new AABB((double) boundingBox0.minX(), (double) boundingBox0.minY(), (double) boundingBox0.minZ(), (double) (boundingBox0.maxX() + 1), (double) (boundingBox0.maxY() + 1), (double) (boundingBox0.maxZ() + 1));
    }

    public static AABB unitCubeFromLowerCorner(Vec3 vec0) {
        return new AABB(vec0.x, vec0.y, vec0.z, vec0.x + 1.0, vec0.y + 1.0, vec0.z + 1.0);
    }

    public AABB setMinX(double double0) {
        return new AABB(double0, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public AABB setMinY(double double0) {
        return new AABB(this.minX, double0, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public AABB setMinZ(double double0) {
        return new AABB(this.minX, this.minY, double0, this.maxX, this.maxY, this.maxZ);
    }

    public AABB setMaxX(double double0) {
        return new AABB(this.minX, this.minY, this.minZ, double0, this.maxY, this.maxZ);
    }

    public AABB setMaxY(double double0) {
        return new AABB(this.minX, this.minY, this.minZ, this.maxX, double0, this.maxZ);
    }

    public AABB setMaxZ(double double0) {
        return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, double0);
    }

    public double min(Direction.Axis directionAxis0) {
        return directionAxis0.choose(this.minX, this.minY, this.minZ);
    }

    public double max(Direction.Axis directionAxis0) {
        return directionAxis0.choose(this.maxX, this.maxY, this.maxZ);
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (!(object0 instanceof AABB $$1)) {
            return false;
        } else if (Double.compare($$1.minX, this.minX) != 0) {
            return false;
        } else if (Double.compare($$1.minY, this.minY) != 0) {
            return false;
        } else if (Double.compare($$1.minZ, this.minZ) != 0) {
            return false;
        } else if (Double.compare($$1.maxX, this.maxX) != 0) {
            return false;
        } else {
            return Double.compare($$1.maxY, this.maxY) != 0 ? false : Double.compare($$1.maxZ, this.maxZ) == 0;
        }
    }

    public int hashCode() {
        long $$0 = Double.doubleToLongBits(this.minX);
        int $$1 = (int) ($$0 ^ $$0 >>> 32);
        $$0 = Double.doubleToLongBits(this.minY);
        $$1 = 31 * $$1 + (int) ($$0 ^ $$0 >>> 32);
        $$0 = Double.doubleToLongBits(this.minZ);
        $$1 = 31 * $$1 + (int) ($$0 ^ $$0 >>> 32);
        $$0 = Double.doubleToLongBits(this.maxX);
        $$1 = 31 * $$1 + (int) ($$0 ^ $$0 >>> 32);
        $$0 = Double.doubleToLongBits(this.maxY);
        $$1 = 31 * $$1 + (int) ($$0 ^ $$0 >>> 32);
        $$0 = Double.doubleToLongBits(this.maxZ);
        return 31 * $$1 + (int) ($$0 ^ $$0 >>> 32);
    }

    public AABB contract(double double0, double double1, double double2) {
        double $$3 = this.minX;
        double $$4 = this.minY;
        double $$5 = this.minZ;
        double $$6 = this.maxX;
        double $$7 = this.maxY;
        double $$8 = this.maxZ;
        if (double0 < 0.0) {
            $$3 -= double0;
        } else if (double0 > 0.0) {
            $$6 -= double0;
        }
        if (double1 < 0.0) {
            $$4 -= double1;
        } else if (double1 > 0.0) {
            $$7 -= double1;
        }
        if (double2 < 0.0) {
            $$5 -= double2;
        } else if (double2 > 0.0) {
            $$8 -= double2;
        }
        return new AABB($$3, $$4, $$5, $$6, $$7, $$8);
    }

    public AABB expandTowards(Vec3 vec0) {
        return this.expandTowards(vec0.x, vec0.y, vec0.z);
    }

    public AABB expandTowards(double double0, double double1, double double2) {
        double $$3 = this.minX;
        double $$4 = this.minY;
        double $$5 = this.minZ;
        double $$6 = this.maxX;
        double $$7 = this.maxY;
        double $$8 = this.maxZ;
        if (double0 < 0.0) {
            $$3 += double0;
        } else if (double0 > 0.0) {
            $$6 += double0;
        }
        if (double1 < 0.0) {
            $$4 += double1;
        } else if (double1 > 0.0) {
            $$7 += double1;
        }
        if (double2 < 0.0) {
            $$5 += double2;
        } else if (double2 > 0.0) {
            $$8 += double2;
        }
        return new AABB($$3, $$4, $$5, $$6, $$7, $$8);
    }

    public AABB inflate(double double0, double double1, double double2) {
        double $$3 = this.minX - double0;
        double $$4 = this.minY - double1;
        double $$5 = this.minZ - double2;
        double $$6 = this.maxX + double0;
        double $$7 = this.maxY + double1;
        double $$8 = this.maxZ + double2;
        return new AABB($$3, $$4, $$5, $$6, $$7, $$8);
    }

    public AABB inflate(double double0) {
        return this.inflate(double0, double0, double0);
    }

    public AABB intersect(AABB aABB0) {
        double $$1 = Math.max(this.minX, aABB0.minX);
        double $$2 = Math.max(this.minY, aABB0.minY);
        double $$3 = Math.max(this.minZ, aABB0.minZ);
        double $$4 = Math.min(this.maxX, aABB0.maxX);
        double $$5 = Math.min(this.maxY, aABB0.maxY);
        double $$6 = Math.min(this.maxZ, aABB0.maxZ);
        return new AABB($$1, $$2, $$3, $$4, $$5, $$6);
    }

    public AABB minmax(AABB aABB0) {
        double $$1 = Math.min(this.minX, aABB0.minX);
        double $$2 = Math.min(this.minY, aABB0.minY);
        double $$3 = Math.min(this.minZ, aABB0.minZ);
        double $$4 = Math.max(this.maxX, aABB0.maxX);
        double $$5 = Math.max(this.maxY, aABB0.maxY);
        double $$6 = Math.max(this.maxZ, aABB0.maxZ);
        return new AABB($$1, $$2, $$3, $$4, $$5, $$6);
    }

    public AABB move(double double0, double double1, double double2) {
        return new AABB(this.minX + double0, this.minY + double1, this.minZ + double2, this.maxX + double0, this.maxY + double1, this.maxZ + double2);
    }

    public AABB move(BlockPos blockPos0) {
        return new AABB(this.minX + (double) blockPos0.m_123341_(), this.minY + (double) blockPos0.m_123342_(), this.minZ + (double) blockPos0.m_123343_(), this.maxX + (double) blockPos0.m_123341_(), this.maxY + (double) blockPos0.m_123342_(), this.maxZ + (double) blockPos0.m_123343_());
    }

    public AABB move(Vec3 vec0) {
        return this.move(vec0.x, vec0.y, vec0.z);
    }

    public boolean intersects(AABB aABB0) {
        return this.intersects(aABB0.minX, aABB0.minY, aABB0.minZ, aABB0.maxX, aABB0.maxY, aABB0.maxZ);
    }

    public boolean intersects(double double0, double double1, double double2, double double3, double double4, double double5) {
        return this.minX < double3 && this.maxX > double0 && this.minY < double4 && this.maxY > double1 && this.minZ < double5 && this.maxZ > double2;
    }

    public boolean intersects(Vec3 vec0, Vec3 vec1) {
        return this.intersects(Math.min(vec0.x, vec1.x), Math.min(vec0.y, vec1.y), Math.min(vec0.z, vec1.z), Math.max(vec0.x, vec1.x), Math.max(vec0.y, vec1.y), Math.max(vec0.z, vec1.z));
    }

    public boolean contains(Vec3 vec0) {
        return this.contains(vec0.x, vec0.y, vec0.z);
    }

    public boolean contains(double double0, double double1, double double2) {
        return double0 >= this.minX && double0 < this.maxX && double1 >= this.minY && double1 < this.maxY && double2 >= this.minZ && double2 < this.maxZ;
    }

    public double getSize() {
        double $$0 = this.getXsize();
        double $$1 = this.getYsize();
        double $$2 = this.getZsize();
        return ($$0 + $$1 + $$2) / 3.0;
    }

    public double getXsize() {
        return this.maxX - this.minX;
    }

    public double getYsize() {
        return this.maxY - this.minY;
    }

    public double getZsize() {
        return this.maxZ - this.minZ;
    }

    public AABB deflate(double double0, double double1, double double2) {
        return this.inflate(-double0, -double1, -double2);
    }

    public AABB deflate(double double0) {
        return this.inflate(-double0);
    }

    public Optional<Vec3> clip(Vec3 vec0, Vec3 vec1) {
        double[] $$2 = new double[] { 1.0 };
        double $$3 = vec1.x - vec0.x;
        double $$4 = vec1.y - vec0.y;
        double $$5 = vec1.z - vec0.z;
        Direction $$6 = getDirection(this, vec0, $$2, null, $$3, $$4, $$5);
        if ($$6 == null) {
            return Optional.empty();
        } else {
            double $$7 = $$2[0];
            return Optional.of(vec0.add($$7 * $$3, $$7 * $$4, $$7 * $$5));
        }
    }

    @Nullable
    public static BlockHitResult clip(Iterable<AABB> iterableAABB0, Vec3 vec1, Vec3 vec2, BlockPos blockPos3) {
        double[] $$4 = new double[] { 1.0 };
        Direction $$5 = null;
        double $$6 = vec2.x - vec1.x;
        double $$7 = vec2.y - vec1.y;
        double $$8 = vec2.z - vec1.z;
        for (AABB $$9 : iterableAABB0) {
            $$5 = getDirection($$9.move(blockPos3), vec1, $$4, $$5, $$6, $$7, $$8);
        }
        if ($$5 == null) {
            return null;
        } else {
            double $$10 = $$4[0];
            return new BlockHitResult(vec1.add($$10 * $$6, $$10 * $$7, $$10 * $$8), $$5, blockPos3, false);
        }
    }

    @Nullable
    private static Direction getDirection(AABB aABB0, Vec3 vec1, double[] double2, @Nullable Direction direction3, double double4, double double5, double double6) {
        if (double4 > 1.0E-7) {
            direction3 = clipPoint(double2, direction3, double4, double5, double6, aABB0.minX, aABB0.minY, aABB0.maxY, aABB0.minZ, aABB0.maxZ, Direction.WEST, vec1.x, vec1.y, vec1.z);
        } else if (double4 < -1.0E-7) {
            direction3 = clipPoint(double2, direction3, double4, double5, double6, aABB0.maxX, aABB0.minY, aABB0.maxY, aABB0.minZ, aABB0.maxZ, Direction.EAST, vec1.x, vec1.y, vec1.z);
        }
        if (double5 > 1.0E-7) {
            direction3 = clipPoint(double2, direction3, double5, double6, double4, aABB0.minY, aABB0.minZ, aABB0.maxZ, aABB0.minX, aABB0.maxX, Direction.DOWN, vec1.y, vec1.z, vec1.x);
        } else if (double5 < -1.0E-7) {
            direction3 = clipPoint(double2, direction3, double5, double6, double4, aABB0.maxY, aABB0.minZ, aABB0.maxZ, aABB0.minX, aABB0.maxX, Direction.UP, vec1.y, vec1.z, vec1.x);
        }
        if (double6 > 1.0E-7) {
            direction3 = clipPoint(double2, direction3, double6, double4, double5, aABB0.minZ, aABB0.minX, aABB0.maxX, aABB0.minY, aABB0.maxY, Direction.NORTH, vec1.z, vec1.x, vec1.y);
        } else if (double6 < -1.0E-7) {
            direction3 = clipPoint(double2, direction3, double6, double4, double5, aABB0.maxZ, aABB0.minX, aABB0.maxX, aABB0.minY, aABB0.maxY, Direction.SOUTH, vec1.z, vec1.x, vec1.y);
        }
        return direction3;
    }

    @Nullable
    private static Direction clipPoint(double[] double0, @Nullable Direction direction1, double double2, double double3, double double4, double double5, double double6, double double7, double double8, double double9, Direction direction10, double double11, double double12, double double13) {
        double $$14 = (double5 - double11) / double2;
        double $$15 = double12 + $$14 * double3;
        double $$16 = double13 + $$14 * double4;
        if (0.0 < $$14 && $$14 < double0[0] && double6 - 1.0E-7 < $$15 && $$15 < double7 + 1.0E-7 && double8 - 1.0E-7 < $$16 && $$16 < double9 + 1.0E-7) {
            double0[0] = $$14;
            return direction10;
        } else {
            return direction1;
        }
    }

    public double distanceToSqr(Vec3 vec0) {
        double $$1 = Math.max(Math.max(this.minX - vec0.x, vec0.x - this.maxX), 0.0);
        double $$2 = Math.max(Math.max(this.minY - vec0.y, vec0.y - this.maxY), 0.0);
        double $$3 = Math.max(Math.max(this.minZ - vec0.z, vec0.z - this.maxZ), 0.0);
        return Mth.lengthSquared($$1, $$2, $$3);
    }

    public String toString() {
        return "AABB[" + this.minX + ", " + this.minY + ", " + this.minZ + "] -> [" + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    public boolean hasNaN() {
        return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
    }

    public Vec3 getCenter() {
        return new Vec3(Mth.lerp(0.5, this.minX, this.maxX), Mth.lerp(0.5, this.minY, this.maxY), Mth.lerp(0.5, this.minZ, this.maxZ));
    }

    public static AABB ofSize(Vec3 vec0, double double1, double double2, double double3) {
        return new AABB(vec0.x - double1 / 2.0, vec0.y - double2 / 2.0, vec0.z - double3 / 2.0, vec0.x + double1 / 2.0, vec0.y + double2 / 2.0, vec0.z + double3 / 2.0);
    }
}