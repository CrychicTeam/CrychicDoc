package dev.ftb.mods.ftblibrary.math;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class ChunkDimPos implements Comparable<ChunkDimPos> {

    private final ResourceKey<Level> dimension;

    private final ChunkPos chunkPos;

    private int hash;

    public ChunkDimPos(ResourceKey<Level> dim, int x, int z) {
        this.dimension = dim;
        this.chunkPos = new ChunkPos(x, z);
        int h = Objects.hash(new Object[] { this.dimension.location(), this.chunkPos });
        this.hash = h == 0 ? 1 : h;
    }

    public ChunkDimPos(ResourceKey<Level> dim, ChunkPos pos) {
        this(dim, pos.x, pos.z);
    }

    public ChunkDimPos(Level world, BlockPos pos) {
        this(world.dimension(), pos.m_123341_() >> 4, pos.m_123343_() >> 4);
    }

    public ChunkDimPos(Entity entity) {
        this(entity.level(), entity.blockPosition());
    }

    public ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    public int x() {
        return this.chunkPos.x;
    }

    public int z() {
        return this.chunkPos.z;
    }

    public ResourceKey<Level> dimension() {
        return this.dimension;
    }

    public String toString() {
        return "[" + this.dimension.location() + ":" + this.x() + ":" + this.z() + "]";
    }

    public int hashCode() {
        return this.hash;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            return !(obj instanceof ChunkDimPos p) ? false : this.dimension == p.dimension && this.chunkPos.equals(p.chunkPos);
        }
    }

    public int compareTo(ChunkDimPos o) {
        int i = this.dimension.location().compareTo(o.dimension.location());
        return i == 0 ? Long.compare(this.getChunkPos().toLong(), o.getChunkPos().toLong()) : i;
    }

    public ChunkDimPos offset(int ox, int oz) {
        return new ChunkDimPos(this.dimension, this.x() + ox, this.z() + oz);
    }
}