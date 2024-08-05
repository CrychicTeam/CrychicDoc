package dev.ftb.mods.ftblibrary.math;

import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public record XZ(int x, int z) {

    public static XZ of(int x, int z) {
        return new XZ(x, z);
    }

    public static XZ of(long singleLong) {
        return of((int) singleLong, (int) (singleLong >> 32));
    }

    public static XZ of(ChunkPos pos) {
        return of(pos.x, pos.z);
    }

    public static XZ chunkFromBlock(int x, int z) {
        return of(x >> 4, z >> 4);
    }

    public static XZ chunkFromBlock(Vec3i pos) {
        return chunkFromBlock(pos.getX(), pos.getZ());
    }

    public static XZ regionFromChunk(int x, int z) {
        return of(x >> 5, z >> 5);
    }

    public static XZ regionFromChunk(ChunkPos p) {
        return of(p.x >> 5, p.z >> 5);
    }

    public static XZ regionFromBlock(int x, int z) {
        return of(x >> 9, z >> 9);
    }

    public static XZ regionFromBlock(Vec3i pos) {
        return regionFromBlock(pos.getX(), pos.getZ());
    }

    public int hashCode() {
        int x1 = 1664525 * this.x + 1013904223;
        int z1 = 1664525 * (this.z ^ -559038737) + 1013904223;
        return x1 ^ z1;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else {
            return !(o instanceof XZ p) ? false : this.x == p.x && this.z == p.z;
        }
    }

    public String toString() {
        return "[" + this.x + ", " + this.z + "]";
    }

    public ChunkDimPos dim(ResourceKey<Level> type) {
        return new ChunkDimPos(type, this.x, this.z);
    }

    public ChunkDimPos dim(Level world) {
        return this.dim(world.dimension());
    }

    public XZ offset(int ox, int oz) {
        return of(this.x + ox, this.z + oz);
    }

    public long toLong() {
        return ChunkPos.asLong(this.x, this.z);
    }

    public String toRegionString() {
        return String.format("%05X-%05X", this.x + 60000, this.z + 60000);
    }
}