package me.jellysquid.mods.lithium.common.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import me.jellysquid.mods.lithium.common.shapes.lists.OffsetFractionalDoubleList;
import net.minecraft.core.AxisCycle;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeAlignedCuboidOffset extends VoxelShapeAlignedCuboid {

    private final double xOffset;

    private final double yOffset;

    private final double zOffset;

    public VoxelShapeAlignedCuboidOffset(VoxelShapeAlignedCuboid originalShape, DiscreteVoxelShape voxels, double xOffset, double yOffset, double zOffset) {
        super(voxels, originalShape.xSegments, originalShape.ySegments, originalShape.zSegments, originalShape.minX + xOffset, originalShape.minY + yOffset, originalShape.minZ + zOffset, originalShape.maxX + xOffset, originalShape.maxY + yOffset, originalShape.maxZ + zOffset);
        if (originalShape instanceof VoxelShapeAlignedCuboidOffset) {
            this.xOffset = ((VoxelShapeAlignedCuboidOffset) originalShape).xOffset + xOffset;
            this.yOffset = ((VoxelShapeAlignedCuboidOffset) originalShape).yOffset + yOffset;
            this.zOffset = ((VoxelShapeAlignedCuboidOffset) originalShape).zOffset + zOffset;
        } else {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.zOffset = zOffset;
        }
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
                return calculatePenetration(this.minX, this.maxX, this.xSegments, this.xOffset, box.minX, box.maxX, maxDist);
            case FORWARD:
                return calculatePenetration(this.minZ, this.maxZ, this.zSegments, this.zOffset, box.minZ, box.maxZ, maxDist);
            case BACKWARD:
                return calculatePenetration(this.minY, this.maxY, this.ySegments, this.yOffset, box.minY, box.maxY, maxDist);
            default:
                throw new IllegalArgumentException();
        }
    }

    private static double calculatePenetration(double aMin, double aMax, int segmentsPerUnit, double shapeOffset, double bMin, double bMax, double maxDist) {
        if (maxDist > 0.0) {
            double gap = aMin - bMax;
            if (gap >= -1.0E-7) {
                return Math.min(gap, maxDist);
            } else if (segmentsPerUnit == 1) {
                return maxDist;
            } else {
                int segment = Mth.ceil((bMax - 1.0E-6 - shapeOffset) * (double) segmentsPerUnit);
                double wallPos = (double) segment / (double) segmentsPerUnit + shapeOffset;
                if (wallPos < bMax - 1.0E-7) {
                    segment++;
                    wallPos = (double) segment / (double) segmentsPerUnit + shapeOffset;
                }
                return wallPos < aMax - 1.0E-6 ? Math.min(maxDist, wallPos - bMax) : maxDist;
            }
        } else {
            double gap = aMax - bMin;
            if (gap <= 1.0E-7) {
                return Math.max(gap, maxDist);
            } else if (segmentsPerUnit == 1) {
                return maxDist;
            } else {
                int segment = Mth.floor((bMin + 1.0E-6 - shapeOffset) * (double) segmentsPerUnit);
                double wallPos = (double) segment / (double) segmentsPerUnit + shapeOffset;
                if (wallPos > bMin + 1.0E-7) {
                    segment--;
                    wallPos = (double) segment / (double) segmentsPerUnit + shapeOffset;
                }
                return wallPos > aMin + 1.0E-6 ? Math.max(maxDist, wallPos - bMin) : maxDist;
            }
        }
    }

    @Override
    public DoubleList getCoords(Direction.Axis axis) {
        return new OffsetFractionalDoubleList(axis.choose(this.xSegments, this.ySegments, this.zSegments), axis.choose(this.xOffset, this.yOffset, this.zOffset));
    }

    @Override
    protected double get(Direction.Axis axis, int index) {
        return axis.choose(this.xOffset, this.yOffset, this.zOffset) + (double) index / (double) axis.choose(this.xSegments, this.ySegments, this.zSegments);
    }

    @Override
    protected int findIndex(Direction.Axis axis, double coord) {
        coord -= axis.choose(this.xOffset, this.yOffset, this.zOffset);
        int numSegments = axis.choose(this.xSegments, this.ySegments, this.zSegments);
        return Mth.clamp(Mth.floor(coord * (double) numSegments), -1, numSegments);
    }
}