package me.lucko.spark.common.platform.world;

public abstract class AbstractChunkInfo<E> implements ChunkInfo<E> {

    private final int x;

    private final int z;

    protected AbstractChunkInfo(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof AbstractChunkInfo)) {
            return false;
        } else {
            AbstractChunkInfo<?> that = (AbstractChunkInfo<?>) obj;
            return this.x == that.x && this.z == that.z;
        }
    }

    public int hashCode() {
        return this.x ^ this.z;
    }
}