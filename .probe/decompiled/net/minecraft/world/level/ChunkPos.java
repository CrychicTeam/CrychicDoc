package net.minecraft.world.level;

import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;

public class ChunkPos {

    private static final int SAFETY_MARGIN = 1056;

    public static final long INVALID_CHUNK_POS = asLong(1875066, 1875066);

    public static final ChunkPos ZERO = new ChunkPos(0, 0);

    private static final long COORD_BITS = 32L;

    private static final long COORD_MASK = 4294967295L;

    private static final int REGION_BITS = 5;

    public static final int REGION_SIZE = 32;

    private static final int REGION_MASK = 31;

    public static final int REGION_MAX_INDEX = 31;

    public final int x;

    public final int z;

    private static final int HASH_A = 1664525;

    private static final int HASH_C = 1013904223;

    private static final int HASH_Z_XOR = -559038737;

    public ChunkPos(int int0, int int1) {
        this.x = int0;
        this.z = int1;
    }

    public ChunkPos(BlockPos blockPos0) {
        this.x = SectionPos.blockToSectionCoord(blockPos0.m_123341_());
        this.z = SectionPos.blockToSectionCoord(blockPos0.m_123343_());
    }

    public ChunkPos(long long0) {
        this.x = (int) long0;
        this.z = (int) (long0 >> 32);
    }

    public static ChunkPos minFromRegion(int int0, int int1) {
        return new ChunkPos(int0 << 5, int1 << 5);
    }

    public static ChunkPos maxFromRegion(int int0, int int1) {
        return new ChunkPos((int0 << 5) + 31, (int1 << 5) + 31);
    }

    public long toLong() {
        return asLong(this.x, this.z);
    }

    public static long asLong(int int0, int int1) {
        return (long) int0 & 4294967295L | ((long) int1 & 4294967295L) << 32;
    }

    public static long asLong(BlockPos blockPos0) {
        return asLong(SectionPos.blockToSectionCoord(blockPos0.m_123341_()), SectionPos.blockToSectionCoord(blockPos0.m_123343_()));
    }

    public static int getX(long long0) {
        return (int) (long0 & 4294967295L);
    }

    public static int getZ(long long0) {
        return (int) (long0 >>> 32 & 4294967295L);
    }

    public int hashCode() {
        return hash(this.x, this.z);
    }

    public static int hash(int int0, int int1) {
        int $$2 = 1664525 * int0 + 1013904223;
        int $$3 = 1664525 * (int1 ^ -559038737) + 1013904223;
        return $$2 ^ $$3;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return !(object0 instanceof ChunkPos $$1) ? false : this.x == $$1.x && this.z == $$1.z;
        }
    }

    public int getMiddleBlockX() {
        return this.getBlockX(8);
    }

    public int getMiddleBlockZ() {
        return this.getBlockZ(8);
    }

    public int getMinBlockX() {
        return SectionPos.sectionToBlockCoord(this.x);
    }

    public int getMinBlockZ() {
        return SectionPos.sectionToBlockCoord(this.z);
    }

    public int getMaxBlockX() {
        return this.getBlockX(15);
    }

    public int getMaxBlockZ() {
        return this.getBlockZ(15);
    }

    public int getRegionX() {
        return this.x >> 5;
    }

    public int getRegionZ() {
        return this.z >> 5;
    }

    public int getRegionLocalX() {
        return this.x & 31;
    }

    public int getRegionLocalZ() {
        return this.z & 31;
    }

    public BlockPos getBlockAt(int int0, int int1, int int2) {
        return new BlockPos(this.getBlockX(int0), int1, this.getBlockZ(int2));
    }

    public int getBlockX(int int0) {
        return SectionPos.sectionToBlockCoord(this.x, int0);
    }

    public int getBlockZ(int int0) {
        return SectionPos.sectionToBlockCoord(this.z, int0);
    }

    public BlockPos getMiddleBlockPosition(int int0) {
        return new BlockPos(this.getMiddleBlockX(), int0, this.getMiddleBlockZ());
    }

    public String toString() {
        return "[" + this.x + ", " + this.z + "]";
    }

    public BlockPos getWorldPosition() {
        return new BlockPos(this.getMinBlockX(), 0, this.getMinBlockZ());
    }

    public int getChessboardDistance(ChunkPos chunkPos0) {
        return Math.max(Math.abs(this.x - chunkPos0.x), Math.abs(this.z - chunkPos0.z));
    }

    public static Stream<ChunkPos> rangeClosed(ChunkPos chunkPos0, int int1) {
        return rangeClosed(new ChunkPos(chunkPos0.x - int1, chunkPos0.z - int1), new ChunkPos(chunkPos0.x + int1, chunkPos0.z + int1));
    }

    public static Stream<ChunkPos> rangeClosed(final ChunkPos chunkPos0, final ChunkPos chunkPos1) {
        int $$2 = Math.abs(chunkPos0.x - chunkPos1.x) + 1;
        int $$3 = Math.abs(chunkPos0.z - chunkPos1.z) + 1;
        final int $$4 = chunkPos0.x < chunkPos1.x ? 1 : -1;
        final int $$5 = chunkPos0.z < chunkPos1.z ? 1 : -1;
        return StreamSupport.stream(new AbstractSpliterator<ChunkPos>((long) ($$2 * $$3), 64) {

            @Nullable
            private ChunkPos pos;

            public boolean tryAdvance(Consumer<? super ChunkPos> p_45630_) {
                if (this.pos == null) {
                    this.pos = chunkPos0;
                } else {
                    int $$1 = this.pos.x;
                    int $$2 = this.pos.z;
                    if ($$1 == chunkPos1.x) {
                        if ($$2 == chunkPos1.z) {
                            return false;
                        }
                        this.pos = new ChunkPos(chunkPos0.x, $$2 + $$5);
                    } else {
                        this.pos = new ChunkPos($$1 + $$4, $$2);
                    }
                }
                p_45630_.accept(this.pos);
                return true;
            }
        }, false);
    }
}