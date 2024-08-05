package me.jellysquid.mods.lithium.common.shapes;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import java.util.List;
import net.minecraft.core.AxisCycle;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeSimpleCube extends VoxelShape implements VoxelShapeCaster {

    static final double EPSILON = 1.0E-7;

    final double minX;

    final double minY;

    final double minZ;

    final double maxX;

    final double maxY;

    final double maxZ;

    public final boolean isTiny;

    public VoxelShapeSimpleCube(DiscreteVoxelShape voxels, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super(voxels);
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.isTiny = this.minX + 3.0E-7 >= this.maxX || this.minY + 3.0E-7 >= this.maxY || this.minZ + 3.0E-7 >= this.maxZ;
    }

    @Override
    public VoxelShape move(double x, double y, double z) {
        return new VoxelShapeSimpleCube(this.f_83211_, this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    @Override
    public double collideX(AxisCycle cycleDirection, AABB box, double maxDist) {
        if (Math.abs(maxDist) < 1.0E-7) {
            return 0.0;
        } else {
            double penetration = this.calculatePenetration(cycleDirection, box, maxDist);
            return penetration != maxDist && this.intersects(cycleDirection, box) ? penetration : maxDist;
        }
    }

    private double calculatePenetration(AxisCycle dir, AABB box, double maxDist) {
        switch(dir) {
            case NONE:
                return calculatePenetration(this.minX, this.maxX, box.minX, box.maxX, maxDist);
            case FORWARD:
                return calculatePenetration(this.minZ, this.maxZ, box.minZ, box.maxZ, maxDist);
            case BACKWARD:
                return calculatePenetration(this.minY, this.maxY, box.minY, box.maxY, maxDist);
            default:
                throw new IllegalArgumentException();
        }
    }

    boolean intersects(AxisCycle dir, AABB box) {
        switch(dir) {
            case NONE:
                return lessThan(this.minY, box.maxY) && lessThan(box.minY, this.maxY) && lessThan(this.minZ, box.maxZ) && lessThan(box.minZ, this.maxZ);
            case FORWARD:
                return lessThan(this.minX, box.maxX) && lessThan(box.minX, this.maxX) && lessThan(this.minY, box.maxY) && lessThan(box.minY, this.maxY);
            case BACKWARD:
                return lessThan(this.minZ, box.maxZ) && lessThan(box.minZ, this.maxZ) && lessThan(this.minX, box.maxX) && lessThan(box.minX, this.maxX);
            default:
                throw new IllegalArgumentException();
        }
    }

    private static double calculatePenetration(double a1, double a2, double b1, double b2, double maxDist) {
        double penetration;
        if (maxDist > 0.0) {
            penetration = a1 - b2;
            if (penetration < -1.0E-7 || maxDist < penetration) {
                return maxDist;
            }
        } else {
            penetration = a2 - b1;
            if (penetration > 1.0E-7 || maxDist > penetration) {
                return maxDist;
            }
        }
        return penetration;
    }

    @Override
    public List<AABB> toAabbs() {
        return Lists.newArrayList(new AABB[] { this.bounds() });
    }

    @Override
    public AABB bounds() {
        return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    @Override
    public double min(Direction.Axis axis) {
        return axis.choose(this.minX, this.minY, this.minZ);
    }

    @Override
    public double max(Direction.Axis axis) {
        return axis.choose(this.maxX, this.maxY, this.maxZ);
    }

    @Override
    protected double get(Direction.Axis axis, int index) {
        if (index >= 0 && index <= 1) {
            switch(axis) {
                case X:
                    return index == 0 ? this.minX : this.maxX;
                case Y:
                    return index == 0 ? this.minY : this.maxY;
                case Z:
                    return index == 0 ? this.minZ : this.maxZ;
                default:
                    throw new IllegalArgumentException();
            }
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public DoubleList getCoords(Direction.Axis axis) {
        switch(axis) {
            case X:
                return DoubleArrayList.wrap(new double[] { this.minX, this.maxX });
            case Y:
                return DoubleArrayList.wrap(new double[] { this.minY, this.maxY });
            case Z:
                return DoubleArrayList.wrap(new double[] { this.minZ, this.maxZ });
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isEmpty() {
        return this.minX >= this.maxX || this.minY >= this.maxY || this.minZ >= this.maxZ;
    }

    @Override
    protected int findIndex(Direction.Axis axis, double coord) {
        if (coord < this.min(axis)) {
            return -1;
        } else {
            return coord >= this.max(axis) ? 1 : 0;
        }
    }

    private static boolean lessThan(double a, double b) {
        return a + 1.0E-7 < b;
    }

    @Override
    public boolean intersects(AABB box, double blockX, double blockY, double blockZ) {
        return box.minX + 1.0E-7 < this.maxX + blockX && box.maxX - 1.0E-7 > this.minX + blockX && box.minY + 1.0E-7 < this.maxY + blockY && box.maxY - 1.0E-7 > this.minY + blockY && box.minZ + 1.0E-7 < this.maxZ + blockZ && box.maxZ - 1.0E-7 > this.minZ + blockZ;
    }

    @Override
    public void forAllBoxes(Shapes.DoubleLineConsumer boxConsumer) {
        boxConsumer.consume(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }
}