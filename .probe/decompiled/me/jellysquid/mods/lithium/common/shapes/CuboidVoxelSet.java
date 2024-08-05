package me.jellysquid.mods.lithium.common.shapes;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

public class CuboidVoxelSet extends DiscreteVoxelShape {

    private final int minX;

    private final int minY;

    private final int minZ;

    private final int maxX;

    private final int maxY;

    private final int maxZ;

    protected CuboidVoxelSet(int xSize, int ySize, int zSize, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super(xSize, ySize, zSize);
        this.minX = (int) Math.round(minX * (double) xSize);
        this.maxX = (int) Math.round(maxX * (double) xSize);
        this.minY = (int) Math.round(minY * (double) ySize);
        this.maxY = (int) Math.round(maxY * (double) ySize);
        this.minZ = (int) Math.round(minZ * (double) zSize);
        this.maxZ = (int) Math.round(maxZ * (double) zSize);
    }

    @Override
    public boolean isFull(int x, int y, int z) {
        return x >= this.minX && x < this.maxX && y >= this.minY && y < this.maxY && z >= this.minZ && z < this.maxZ;
    }

    @Override
    public void fill(int x, int y, int z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int firstFull(Direction.Axis axis) {
        return axis.choose(this.minX, this.minY, this.minZ);
    }

    @Override
    public int lastFull(Direction.Axis axis) {
        return axis.choose(this.maxX, this.maxY, this.maxZ);
    }

    @Override
    public boolean isEmpty() {
        return this.minX >= this.maxX || this.minY >= this.maxY || this.minZ >= this.maxZ;
    }
}