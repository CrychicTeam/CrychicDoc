package com.yungnickyoung.minecraft.yungsapi.math;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

public class ColPos {

    protected int x;

    protected int z;

    private static final int NUM_X_BITS = 32;

    private static final int NUM_Z_BITS = 32;

    private static final long X_MASK = 4294967295L;

    private static final long Z_MASK = 4294967295L;

    public ColPos() {
        this(0, 0);
    }

    public ColPos(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public ColPos(BlockPos source) {
        this.x = source.m_123341_();
        this.z = source.m_123343_();
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public long toLong() {
        long i = 0L;
        i |= (long) this.getX() & 4294967295L;
        return i | ((long) this.getZ() & 4294967295L) << 32;
    }

    public static ColPos fromLong(long packedPos) {
        int x = (int) (packedPos << 32 >> 32);
        int z = (int) (packedPos >> 32);
        return new ColPos(x, z);
    }

    public ColPos up() {
        return this.up(1);
    }

    public ColPos up(int n) {
        return this.offset(Direction.UP, n);
    }

    public ColPos down() {
        return this.down(1);
    }

    public ColPos down(int n) {
        return this.offset(Direction.DOWN, n);
    }

    public ColPos north() {
        return this.north(1);
    }

    public ColPos north(int n) {
        return this.offset(Direction.NORTH, n);
    }

    public ColPos south() {
        return this.south(1);
    }

    public ColPos south(int n) {
        return this.offset(Direction.SOUTH, n);
    }

    public ColPos west() {
        return this.west(1);
    }

    public ColPos west(int n) {
        return this.offset(Direction.WEST, n);
    }

    public ColPos east() {
        return this.east(1);
    }

    public ColPos east(int n) {
        return this.offset(Direction.EAST, n);
    }

    public ColPos offset(Direction facing) {
        return this.offset(facing, 1);
    }

    public ColPos offset(Direction facing, int n) {
        return n == 0 ? this : new ColPos(this.getX() + facing.getStepX() * n, this.getZ() + facing.getStepZ() * n);
    }

    public ColPos rotate(Rotation rotationIn) {
        switch(rotationIn) {
            case NONE:
            default:
                return this;
            case CLOCKWISE_90:
                return new ColPos(-this.getZ(), this.getX());
            case CLOCKWISE_180:
                return new ColPos(-this.getX(), -this.getZ());
            case COUNTERCLOCKWISE_90:
                return new ColPos(this.getZ(), -this.getX());
        }
    }

    public BlockPos toBlockPos() {
        return new BlockPos(this.getX(), 1, this.getZ());
    }

    public static ColPos fromBlockPos(BlockPos blockPos) {
        return new ColPos(blockPos);
    }

    public static class Mutable extends ColPos {

        public Mutable() {
        }

        public Mutable(BlockPos source) {
            this(source.m_123341_(), source.m_123343_());
        }

        public Mutable(ColPos pos) {
            this(pos.getX(), pos.getZ());
        }

        public Mutable(int x, int z) {
            super(x, z);
        }

        public ColPos.Mutable set(int x, int z) {
            this.x = x;
            this.z = z;
            return this;
        }

        public ColPos.Mutable set(ColPos source) {
            this.set(source.getX(), source.getZ());
            return this;
        }

        public ColPos.Mutable set(BlockPos source) {
            this.set(source.m_123341_(), source.m_123343_());
            return this;
        }

        public ColPos.Mutable move(Direction facing, int n) {
            return this.set(this.x + facing.getStepX() * n, this.z + facing.getStepZ() * n);
        }

        public ColPos.Mutable move(Direction facing) {
            return this.move(facing, 1);
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setZ(int z) {
            this.z = z;
        }
    }
}