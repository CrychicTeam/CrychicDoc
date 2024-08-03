package me.jellysquid.mods.lithium.common.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.AxisCycle;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CubePointRange;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeAlignedCuboid extends VoxelShapeSimpleCube {

    static final double LARGE_EPSILON = 1.0E-6;

    protected final int xSegments;

    protected final int ySegments;

    protected final int zSegments;

    public VoxelShapeAlignedCuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int xRes, int yRes, int zRes) {
        super(new CuboidVoxelSet(1 << xRes, 1 << yRes, 1 << zRes, minX, minY, minZ, maxX, maxY, maxZ), minX, minY, minZ, maxX, maxY, maxZ);
        this.xSegments = 1 << xRes;
        this.ySegments = 1 << yRes;
        this.zSegments = 1 << zRes;
    }

    public VoxelShapeAlignedCuboid(DiscreteVoxelShape voxels, int xSegments, int ySegments, int zSegments, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super(voxels, minX, minY, minZ, maxX, maxY, maxZ);
        this.xSegments = xSegments;
        this.ySegments = ySegments;
        this.zSegments = zSegments;
    }

    @Override
    public VoxelShape move(double x, double y, double z) {
        return new VoxelShapeAlignedCuboidOffset(this, this.f_83211_, x, y, z);
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
                return calculatePenetration(this.minX, this.maxX, this.xSegments, box.minX, box.maxX, maxDist);
            case FORWARD:
                return calculatePenetration(this.minZ, this.maxZ, this.zSegments, box.minZ, box.maxZ, maxDist);
            case BACKWARD:
                return calculatePenetration(this.minY, this.maxY, this.ySegments, box.minY, box.maxY, maxDist);
            default:
                throw new IllegalArgumentException();
        }
    }

    private static double calculatePenetration(double aMin, double aMax, int segmentsPerUnit, double bMin, double bMax, double maxDist) {
        if (maxDist > 0.0) {
            double gap = aMin - bMax;
            if (gap >= -1.0E-7) {
                return Math.min(gap, maxDist);
            } else if (segmentsPerUnit == 1) {
                return maxDist;
            } else {
                double wallPos = (double) Mth.ceil((bMax - 1.0E-7) * (double) segmentsPerUnit) / (double) segmentsPerUnit;
                return wallPos < aMax - 1.0E-6 ? Math.min(maxDist, wallPos - bMax) : maxDist;
            }
        } else {
            double gap = aMax - bMin;
            if (gap <= 1.0E-7) {
                return Math.max(gap, maxDist);
            } else if (segmentsPerUnit == 1) {
                return maxDist;
            } else {
                double wallPos = (double) Mth.floor((bMin + 1.0E-7) * (double) segmentsPerUnit) / (double) segmentsPerUnit;
                return wallPos > aMin + 1.0E-6 ? Math.max(maxDist, wallPos - bMin) : maxDist;
            }
        }
    }

    @Override
    public DoubleList getCoords(Direction.Axis axis) {
        return new CubePointRange(axis.choose(this.xSegments, this.ySegments, this.zSegments));
    }

    @Override
    protected double get(Direction.Axis axis, int index) {
        return (double) index / (double) axis.choose(this.xSegments, this.ySegments, this.zSegments);
    }

    @Override
    protected int findIndex(Direction.Axis axis, double coord) {
        int i = axis.choose(this.xSegments, this.ySegments, this.zSegments);
        return Mth.clamp(Mth.floor(coord * (double) i), -1, i);
    }
}