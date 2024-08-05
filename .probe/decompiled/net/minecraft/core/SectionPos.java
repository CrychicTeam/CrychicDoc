package net.minecraft.core;

import it.unimi.dsi.fastutil.longs.LongConsumer;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.entity.EntityAccess;

public class SectionPos extends Vec3i {

    public static final int SECTION_BITS = 4;

    public static final int SECTION_SIZE = 16;

    public static final int SECTION_MASK = 15;

    public static final int SECTION_HALF_SIZE = 8;

    public static final int SECTION_MAX_INDEX = 15;

    private static final int PACKED_X_LENGTH = 22;

    private static final int PACKED_Y_LENGTH = 20;

    private static final int PACKED_Z_LENGTH = 22;

    private static final long PACKED_X_MASK = 4194303L;

    private static final long PACKED_Y_MASK = 1048575L;

    private static final long PACKED_Z_MASK = 4194303L;

    private static final int Y_OFFSET = 0;

    private static final int Z_OFFSET = 20;

    private static final int X_OFFSET = 42;

    private static final int RELATIVE_X_SHIFT = 8;

    private static final int RELATIVE_Y_SHIFT = 0;

    private static final int RELATIVE_Z_SHIFT = 4;

    SectionPos(int int0, int int1, int int2) {
        super(int0, int1, int2);
    }

    public static SectionPos of(int int0, int int1, int int2) {
        return new SectionPos(int0, int1, int2);
    }

    public static SectionPos of(BlockPos blockPos0) {
        return new SectionPos(blockToSectionCoord(blockPos0.m_123341_()), blockToSectionCoord(blockPos0.m_123342_()), blockToSectionCoord(blockPos0.m_123343_()));
    }

    public static SectionPos of(ChunkPos chunkPos0, int int1) {
        return new SectionPos(chunkPos0.x, int1, chunkPos0.z);
    }

    public static SectionPos of(EntityAccess entityAccess0) {
        return of(entityAccess0.blockPosition());
    }

    public static SectionPos of(Position position0) {
        return new SectionPos(blockToSectionCoord(position0.x()), blockToSectionCoord(position0.y()), blockToSectionCoord(position0.z()));
    }

    public static SectionPos of(long long0) {
        return new SectionPos(x(long0), y(long0), z(long0));
    }

    public static SectionPos bottomOf(ChunkAccess chunkAccess0) {
        return of(chunkAccess0.getPos(), chunkAccess0.m_151560_());
    }

    public static long offset(long long0, Direction direction1) {
        return offset(long0, direction1.getStepX(), direction1.getStepY(), direction1.getStepZ());
    }

    public static long offset(long long0, int int1, int int2, int int3) {
        return asLong(x(long0) + int1, y(long0) + int2, z(long0) + int3);
    }

    public static int posToSectionCoord(double double0) {
        return blockToSectionCoord(Mth.floor(double0));
    }

    public static int blockToSectionCoord(int int0) {
        return int0 >> 4;
    }

    public static int blockToSectionCoord(double double0) {
        return Mth.floor(double0) >> 4;
    }

    public static int sectionRelative(int int0) {
        return int0 & 15;
    }

    public static short sectionRelativePos(BlockPos blockPos0) {
        int $$1 = sectionRelative(blockPos0.m_123341_());
        int $$2 = sectionRelative(blockPos0.m_123342_());
        int $$3 = sectionRelative(blockPos0.m_123343_());
        return (short) ($$1 << 8 | $$3 << 4 | $$2 << 0);
    }

    public static int sectionRelativeX(short short0) {
        return short0 >>> 8 & 15;
    }

    public static int sectionRelativeY(short short0) {
        return short0 >>> 0 & 15;
    }

    public static int sectionRelativeZ(short short0) {
        return short0 >>> 4 & 15;
    }

    public int relativeToBlockX(short short0) {
        return this.minBlockX() + sectionRelativeX(short0);
    }

    public int relativeToBlockY(short short0) {
        return this.minBlockY() + sectionRelativeY(short0);
    }

    public int relativeToBlockZ(short short0) {
        return this.minBlockZ() + sectionRelativeZ(short0);
    }

    public BlockPos relativeToBlockPos(short short0) {
        return new BlockPos(this.relativeToBlockX(short0), this.relativeToBlockY(short0), this.relativeToBlockZ(short0));
    }

    public static int sectionToBlockCoord(int int0) {
        return int0 << 4;
    }

    public static int sectionToBlockCoord(int int0, int int1) {
        return sectionToBlockCoord(int0) + int1;
    }

    public static int x(long long0) {
        return (int) (long0 << 0 >> 42);
    }

    public static int y(long long0) {
        return (int) (long0 << 44 >> 44);
    }

    public static int z(long long0) {
        return (int) (long0 << 22 >> 42);
    }

    public int x() {
        return this.m_123341_();
    }

    public int y() {
        return this.m_123342_();
    }

    public int z() {
        return this.m_123343_();
    }

    public int minBlockX() {
        return sectionToBlockCoord(this.x());
    }

    public int minBlockY() {
        return sectionToBlockCoord(this.y());
    }

    public int minBlockZ() {
        return sectionToBlockCoord(this.z());
    }

    public int maxBlockX() {
        return sectionToBlockCoord(this.x(), 15);
    }

    public int maxBlockY() {
        return sectionToBlockCoord(this.y(), 15);
    }

    public int maxBlockZ() {
        return sectionToBlockCoord(this.z(), 15);
    }

    public static long blockToSection(long long0) {
        return asLong(blockToSectionCoord(BlockPos.getX(long0)), blockToSectionCoord(BlockPos.getY(long0)), blockToSectionCoord(BlockPos.getZ(long0)));
    }

    public static long getZeroNode(int int0, int int1) {
        return getZeroNode(asLong(int0, 0, int1));
    }

    public static long getZeroNode(long long0) {
        return long0 & -1048576L;
    }

    public BlockPos origin() {
        return new BlockPos(sectionToBlockCoord(this.x()), sectionToBlockCoord(this.y()), sectionToBlockCoord(this.z()));
    }

    public BlockPos center() {
        int $$0 = 8;
        return this.origin().offset(8, 8, 8);
    }

    public ChunkPos chunk() {
        return new ChunkPos(this.x(), this.z());
    }

    public static long asLong(BlockPos blockPos0) {
        return asLong(blockToSectionCoord(blockPos0.m_123341_()), blockToSectionCoord(blockPos0.m_123342_()), blockToSectionCoord(blockPos0.m_123343_()));
    }

    public static long asLong(int int0, int int1, int int2) {
        long $$3 = 0L;
        $$3 |= ((long) int0 & 4194303L) << 42;
        $$3 |= ((long) int1 & 1048575L) << 0;
        return $$3 | ((long) int2 & 4194303L) << 20;
    }

    public long asLong() {
        return asLong(this.x(), this.y(), this.z());
    }

    public SectionPos offset(int int0, int int1, int int2) {
        return int0 == 0 && int1 == 0 && int2 == 0 ? this : new SectionPos(this.x() + int0, this.y() + int1, this.z() + int2);
    }

    public Stream<BlockPos> blocksInside() {
        return BlockPos.betweenClosedStream(this.minBlockX(), this.minBlockY(), this.minBlockZ(), this.maxBlockX(), this.maxBlockY(), this.maxBlockZ());
    }

    public static Stream<SectionPos> cube(SectionPos sectionPos0, int int1) {
        int $$2 = sectionPos0.x();
        int $$3 = sectionPos0.y();
        int $$4 = sectionPos0.z();
        return betweenClosedStream($$2 - int1, $$3 - int1, $$4 - int1, $$2 + int1, $$3 + int1, $$4 + int1);
    }

    public static Stream<SectionPos> aroundChunk(ChunkPos chunkPos0, int int1, int int2, int int3) {
        int $$4 = chunkPos0.x;
        int $$5 = chunkPos0.z;
        return betweenClosedStream($$4 - int1, int2, $$5 - int1, $$4 + int1, int3 - 1, $$5 + int1);
    }

    public static Stream<SectionPos> betweenClosedStream(final int int0, final int int1, final int int2, final int int3, final int int4, final int int5) {
        return StreamSupport.stream(new AbstractSpliterator<SectionPos>((long) ((int3 - int0 + 1) * (int4 - int1 + 1) * (int5 - int2 + 1)), 64) {

            final Cursor3D cursor = new Cursor3D(int0, int1, int2, int3, int4, int5);

            public boolean tryAdvance(Consumer<? super SectionPos> p_123271_) {
                if (this.cursor.advance()) {
                    p_123271_.accept(new SectionPos(this.cursor.nextX(), this.cursor.nextY(), this.cursor.nextZ()));
                    return true;
                } else {
                    return false;
                }
            }
        }, false);
    }

    public static void aroundAndAtBlockPos(BlockPos blockPos0, LongConsumer longConsumer1) {
        aroundAndAtBlockPos(blockPos0.m_123341_(), blockPos0.m_123342_(), blockPos0.m_123343_(), longConsumer1);
    }

    public static void aroundAndAtBlockPos(long long0, LongConsumer longConsumer1) {
        aroundAndAtBlockPos(BlockPos.getX(long0), BlockPos.getY(long0), BlockPos.getZ(long0), longConsumer1);
    }

    public static void aroundAndAtBlockPos(int int0, int int1, int int2, LongConsumer longConsumer3) {
        int $$4 = blockToSectionCoord(int0 - 1);
        int $$5 = blockToSectionCoord(int0 + 1);
        int $$6 = blockToSectionCoord(int1 - 1);
        int $$7 = blockToSectionCoord(int1 + 1);
        int $$8 = blockToSectionCoord(int2 - 1);
        int $$9 = blockToSectionCoord(int2 + 1);
        if ($$4 == $$5 && $$6 == $$7 && $$8 == $$9) {
            longConsumer3.accept(asLong($$4, $$6, $$8));
        } else {
            for (int $$10 = $$4; $$10 <= $$5; $$10++) {
                for (int $$11 = $$6; $$11 <= $$7; $$11++) {
                    for (int $$12 = $$8; $$12 <= $$9; $$12++) {
                        longConsumer3.accept(asLong($$10, $$11, $$12));
                    }
                }
            }
        }
    }
}