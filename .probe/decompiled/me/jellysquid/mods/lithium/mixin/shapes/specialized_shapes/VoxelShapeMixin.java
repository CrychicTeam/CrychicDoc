package me.jellysquid.mods.lithium.mixin.shapes.specialized_shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.AxisCycle;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ VoxelShape.class })
public abstract class VoxelShapeMixin {

    private static final double POSITIVE_EPSILON = 1.0E-7;

    private static final double NEGATIVE_EPSILON = -1.0E-7;

    @Shadow
    @Final
    public DiscreteVoxelShape shape;

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    protected abstract double get(Direction.Axis var1, int var2);

    @Shadow
    public abstract DoubleList getCoords(Direction.Axis var1);

    @Overwrite
    public double collideX(AxisCycle cycleDirection, AABB box, double maxDist) {
        if (this.isEmpty()) {
            return maxDist;
        } else if (Math.abs(maxDist) < 1.0E-7) {
            return 0.0;
        } else {
            AxisCycle cycle = cycleDirection.inverse();
            Direction.Axis axisX = cycle.cycle(Direction.Axis.X);
            Direction.Axis axisY = cycle.cycle(Direction.Axis.Y);
            Direction.Axis axisZ = cycle.cycle(Direction.Axis.Z);
            int minY = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int minZ = Integer.MIN_VALUE;
            int maxZ = Integer.MIN_VALUE;
            if (maxDist > 0.0) {
                double max = box.max(axisX);
                int maxIdx = this.findIndex(axisX, max - 1.0E-7);
                int maxX = this.shape.getSize(axisX);
                for (int x = maxIdx + 1; x < maxX; x++) {
                    minY = minY == Integer.MIN_VALUE ? Math.max(0, this.findIndex(axisY, box.min(axisY) + 1.0E-7)) : minY;
                    maxY = maxY == Integer.MIN_VALUE ? Math.min(this.shape.getSize(axisY), this.findIndex(axisY, box.max(axisY) - 1.0E-7) + 1) : maxY;
                    for (int y = minY; y < maxY; y++) {
                        minZ = minZ == Integer.MIN_VALUE ? Math.max(0, this.findIndex(axisZ, box.min(axisZ) + 1.0E-7)) : minZ;
                        maxZ = maxZ == Integer.MIN_VALUE ? Math.min(this.shape.getSize(axisZ), this.findIndex(axisZ, box.max(axisZ) - 1.0E-7) + 1) : maxZ;
                        for (int z = minZ; z < maxZ; z++) {
                            if (this.shape.isFullWide(cycle, x, y, z)) {
                                double dist = this.get(axisX, x) - max;
                                if (dist >= -1.0E-7) {
                                    maxDist = Math.min(maxDist, dist);
                                }
                                return maxDist;
                            }
                        }
                    }
                }
            } else if (maxDist < 0.0) {
                double min = box.min(axisX);
                int minIdx = this.findIndex(axisX, min + 1.0E-7);
                for (int x = minIdx - 1; x >= 0; x--) {
                    minY = minY == Integer.MIN_VALUE ? Math.max(0, this.findIndex(axisY, box.min(axisY) + 1.0E-7)) : minY;
                    maxY = maxY == Integer.MIN_VALUE ? Math.min(this.shape.getSize(axisY), this.findIndex(axisY, box.max(axisY) - 1.0E-7) + 1) : maxY;
                    for (int y = minY; y < maxY; y++) {
                        minZ = minZ == Integer.MIN_VALUE ? Math.max(0, this.findIndex(axisZ, box.min(axisZ) + 1.0E-7)) : minZ;
                        maxZ = maxZ == Integer.MIN_VALUE ? Math.min(this.shape.getSize(axisZ), this.findIndex(axisZ, box.max(axisZ) - 1.0E-7) + 1) : maxZ;
                        for (int zx = minZ; zx < maxZ; zx++) {
                            if (this.shape.isFullWide(cycle, x, y, zx)) {
                                double dist = this.get(axisX, x + 1) - min;
                                if (dist <= 1.0E-7) {
                                    maxDist = Math.max(maxDist, dist);
                                }
                                return maxDist;
                            }
                        }
                    }
                }
            }
            return maxDist;
        }
    }

    @Overwrite
    public int findIndex(Direction.Axis axis, double coord) {
        DoubleList list = this.getCoords(axis);
        int size = this.shape.getSize(axis);
        int start = 0;
        int end = size + 1 - start;
        while (end > 0) {
            int middle = end / 2;
            int idx = start + middle;
            if (idx < 0 || idx <= size && !(coord < list.getDouble(idx))) {
                start = idx + 1;
                end -= middle + 1;
            } else {
                end = middle;
            }
        }
        return start - 1;
    }
}