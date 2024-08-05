package net.minecraft.core;

import com.google.common.collect.AbstractIterator;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.concurrent.Immutable;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

@Immutable
public class BlockPos extends Vec3i {

    public static final Codec<BlockPos> CODEC = Codec.INT_STREAM.comapFlatMap(p_121967_ -> Util.fixedSize(p_121967_, 3).map(p_175270_ -> new BlockPos(p_175270_[0], p_175270_[1], p_175270_[2])), p_121924_ -> IntStream.of(new int[] { p_121924_.m_123341_(), p_121924_.m_123342_(), p_121924_.m_123343_() })).stable();

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final BlockPos ZERO = new BlockPos(0, 0, 0);

    private static final int PACKED_X_LENGTH = 1 + Mth.log2(Mth.smallestEncompassingPowerOfTwo(30000000));

    private static final int PACKED_Z_LENGTH = PACKED_X_LENGTH;

    public static final int PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH;

    private static final long PACKED_X_MASK = (1L << PACKED_X_LENGTH) - 1L;

    private static final long PACKED_Y_MASK = (1L << PACKED_Y_LENGTH) - 1L;

    private static final long PACKED_Z_MASK = (1L << PACKED_Z_LENGTH) - 1L;

    private static final int Y_OFFSET = 0;

    private static final int Z_OFFSET = PACKED_Y_LENGTH;

    private static final int X_OFFSET = PACKED_Y_LENGTH + PACKED_Z_LENGTH;

    public BlockPos(int int0, int int1, int int2) {
        super(int0, int1, int2);
    }

    public BlockPos(Vec3i vecI0) {
        this(vecI0.getX(), vecI0.getY(), vecI0.getZ());
    }

    public static long offset(long long0, Direction direction1) {
        return offset(long0, direction1.getStepX(), direction1.getStepY(), direction1.getStepZ());
    }

    public static long offset(long long0, int int1, int int2, int int3) {
        return asLong(getX(long0) + int1, getY(long0) + int2, getZ(long0) + int3);
    }

    public static int getX(long long0) {
        return (int) (long0 << 64 - X_OFFSET - PACKED_X_LENGTH >> 64 - PACKED_X_LENGTH);
    }

    public static int getY(long long0) {
        return (int) (long0 << 64 - PACKED_Y_LENGTH >> 64 - PACKED_Y_LENGTH);
    }

    public static int getZ(long long0) {
        return (int) (long0 << 64 - Z_OFFSET - PACKED_Z_LENGTH >> 64 - PACKED_Z_LENGTH);
    }

    public static BlockPos of(long long0) {
        return new BlockPos(getX(long0), getY(long0), getZ(long0));
    }

    public static BlockPos containing(double double0, double double1, double double2) {
        return new BlockPos(Mth.floor(double0), Mth.floor(double1), Mth.floor(double2));
    }

    public static BlockPos containing(Position position0) {
        return containing(position0.x(), position0.y(), position0.z());
    }

    public long asLong() {
        return asLong(this.m_123341_(), this.m_123342_(), this.m_123343_());
    }

    public static long asLong(int int0, int int1, int int2) {
        long $$3 = 0L;
        $$3 |= ((long) int0 & PACKED_X_MASK) << X_OFFSET;
        $$3 |= ((long) int1 & PACKED_Y_MASK) << 0;
        return $$3 | ((long) int2 & PACKED_Z_MASK) << Z_OFFSET;
    }

    public static long getFlatIndex(long long0) {
        return long0 & -16L;
    }

    public BlockPos offset(int int0, int int1, int int2) {
        return int0 == 0 && int1 == 0 && int2 == 0 ? this : new BlockPos(this.m_123341_() + int0, this.m_123342_() + int1, this.m_123343_() + int2);
    }

    public Vec3 getCenter() {
        return Vec3.atCenterOf(this);
    }

    public BlockPos offset(Vec3i vecI0) {
        return this.offset(vecI0.getX(), vecI0.getY(), vecI0.getZ());
    }

    public BlockPos subtract(Vec3i vecI0) {
        return this.offset(-vecI0.getX(), -vecI0.getY(), -vecI0.getZ());
    }

    public BlockPos multiply(int int0) {
        if (int0 == 1) {
            return this;
        } else {
            return int0 == 0 ? ZERO : new BlockPos(this.m_123341_() * int0, this.m_123342_() * int0, this.m_123343_() * int0);
        }
    }

    public BlockPos above() {
        return this.relative(Direction.UP);
    }

    public BlockPos above(int int0) {
        return this.relative(Direction.UP, int0);
    }

    public BlockPos below() {
        return this.relative(Direction.DOWN);
    }

    public BlockPos below(int int0) {
        return this.relative(Direction.DOWN, int0);
    }

    public BlockPos north() {
        return this.relative(Direction.NORTH);
    }

    public BlockPos north(int int0) {
        return this.relative(Direction.NORTH, int0);
    }

    public BlockPos south() {
        return this.relative(Direction.SOUTH);
    }

    public BlockPos south(int int0) {
        return this.relative(Direction.SOUTH, int0);
    }

    public BlockPos west() {
        return this.relative(Direction.WEST);
    }

    public BlockPos west(int int0) {
        return this.relative(Direction.WEST, int0);
    }

    public BlockPos east() {
        return this.relative(Direction.EAST);
    }

    public BlockPos east(int int0) {
        return this.relative(Direction.EAST, int0);
    }

    public BlockPos relative(Direction direction0) {
        return new BlockPos(this.m_123341_() + direction0.getStepX(), this.m_123342_() + direction0.getStepY(), this.m_123343_() + direction0.getStepZ());
    }

    public BlockPos relative(Direction direction0, int int1) {
        return int1 == 0 ? this : new BlockPos(this.m_123341_() + direction0.getStepX() * int1, this.m_123342_() + direction0.getStepY() * int1, this.m_123343_() + direction0.getStepZ() * int1);
    }

    public BlockPos relative(Direction.Axis directionAxis0, int int1) {
        if (int1 == 0) {
            return this;
        } else {
            int $$2 = directionAxis0 == Direction.Axis.X ? int1 : 0;
            int $$3 = directionAxis0 == Direction.Axis.Y ? int1 : 0;
            int $$4 = directionAxis0 == Direction.Axis.Z ? int1 : 0;
            return new BlockPos(this.m_123341_() + $$2, this.m_123342_() + $$3, this.m_123343_() + $$4);
        }
    }

    public BlockPos rotate(Rotation rotation0) {
        switch(rotation0) {
            case NONE:
            default:
                return this;
            case CLOCKWISE_90:
                return new BlockPos(-this.m_123343_(), this.m_123342_(), this.m_123341_());
            case CLOCKWISE_180:
                return new BlockPos(-this.m_123341_(), this.m_123342_(), -this.m_123343_());
            case COUNTERCLOCKWISE_90:
                return new BlockPos(this.m_123343_(), this.m_123342_(), -this.m_123341_());
        }
    }

    public BlockPos cross(Vec3i vecI0) {
        return new BlockPos(this.m_123342_() * vecI0.getZ() - this.m_123343_() * vecI0.getY(), this.m_123343_() * vecI0.getX() - this.m_123341_() * vecI0.getZ(), this.m_123341_() * vecI0.getY() - this.m_123342_() * vecI0.getX());
    }

    public BlockPos atY(int int0) {
        return new BlockPos(this.m_123341_(), int0, this.m_123343_());
    }

    public BlockPos immutable() {
        return this;
    }

    public BlockPos.MutableBlockPos mutable() {
        return new BlockPos.MutableBlockPos(this.m_123341_(), this.m_123342_(), this.m_123343_());
    }

    public static Iterable<BlockPos> randomInCube(RandomSource randomSource0, int int1, BlockPos blockPos2, int int3) {
        return randomBetweenClosed(randomSource0, int1, blockPos2.m_123341_() - int3, blockPos2.m_123342_() - int3, blockPos2.m_123343_() - int3, blockPos2.m_123341_() + int3, blockPos2.m_123342_() + int3, blockPos2.m_123343_() + int3);
    }

    @Deprecated
    public static Stream<BlockPos> squareOutSouthEast(BlockPos blockPos0) {
        return Stream.of(blockPos0, blockPos0.south(), blockPos0.east(), blockPos0.south().east());
    }

    public static Iterable<BlockPos> randomBetweenClosed(RandomSource randomSource0, int int1, int int2, int int3, int int4, int int5, int int6, int int7) {
        int $$8 = int5 - int2 + 1;
        int $$9 = int6 - int3 + 1;
        int $$10 = int7 - int4 + 1;
        return () -> new AbstractIterator<BlockPos>() {

            final BlockPos.MutableBlockPos nextPos = new BlockPos.MutableBlockPos();

            int counter = int1;

            protected BlockPos computeNext() {
                if (this.counter <= 0) {
                    return (BlockPos) this.endOfData();
                } else {
                    BlockPos $$0 = this.nextPos.set(int2 + randomSource0.nextInt($$8), int3 + randomSource0.nextInt($$9), int4 + randomSource0.nextInt($$10));
                    this.counter--;
                    return $$0;
                }
            }
        };
    }

    public static Iterable<BlockPos> withinManhattan(BlockPos blockPos0, int int1, int int2, int int3) {
        int $$4 = int1 + int2 + int3;
        int $$5 = blockPos0.m_123341_();
        int $$6 = blockPos0.m_123342_();
        int $$7 = blockPos0.m_123343_();
        return () -> new AbstractIterator<BlockPos>() {

            private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

            private int currentDepth;

            private int maxX;

            private int maxY;

            private int x;

            private int y;

            private boolean zMirror;

            protected BlockPos computeNext() {
                if (this.zMirror) {
                    this.zMirror = false;
                    this.cursor.setZ($$7 - (this.cursor.m_123343_() - $$7));
                    return this.cursor;
                } else {
                    BlockPos $$0;
                    for ($$0 = null; $$0 == null; this.y++) {
                        if (this.y > this.maxY) {
                            this.x++;
                            if (this.x > this.maxX) {
                                this.currentDepth++;
                                if (this.currentDepth > $$4) {
                                    return (BlockPos) this.endOfData();
                                }
                                this.maxX = Math.min(int1, this.currentDepth);
                                this.x = -this.maxX;
                            }
                            this.maxY = Math.min(int2, this.currentDepth - Math.abs(this.x));
                            this.y = -this.maxY;
                        }
                        int $$1 = this.x;
                        int $$2 = this.y;
                        int $$3 = this.currentDepth - Math.abs($$1) - Math.abs($$2);
                        if ($$3 <= int3) {
                            this.zMirror = $$3 != 0;
                            $$0 = this.cursor.set($$5 + $$1, $$6 + $$2, $$7 + $$3);
                        }
                    }
                    return $$0;
                }
            }
        };
    }

    public static Optional<BlockPos> findClosestMatch(BlockPos blockPos0, int int1, int int2, Predicate<BlockPos> predicateBlockPos3) {
        for (BlockPos $$4 : withinManhattan(blockPos0, int1, int2, int1)) {
            if (predicateBlockPos3.test($$4)) {
                return Optional.of($$4);
            }
        }
        return Optional.empty();
    }

    public static Stream<BlockPos> withinManhattanStream(BlockPos blockPos0, int int1, int int2, int int3) {
        return StreamSupport.stream(withinManhattan(blockPos0, int1, int2, int3).spliterator(), false);
    }

    public static Iterable<BlockPos> betweenClosed(BlockPos blockPos0, BlockPos blockPos1) {
        return betweenClosed(Math.min(blockPos0.m_123341_(), blockPos1.m_123341_()), Math.min(blockPos0.m_123342_(), blockPos1.m_123342_()), Math.min(blockPos0.m_123343_(), blockPos1.m_123343_()), Math.max(blockPos0.m_123341_(), blockPos1.m_123341_()), Math.max(blockPos0.m_123342_(), blockPos1.m_123342_()), Math.max(blockPos0.m_123343_(), blockPos1.m_123343_()));
    }

    public static Stream<BlockPos> betweenClosedStream(BlockPos blockPos0, BlockPos blockPos1) {
        return StreamSupport.stream(betweenClosed(blockPos0, blockPos1).spliterator(), false);
    }

    public static Stream<BlockPos> betweenClosedStream(BoundingBox boundingBox0) {
        return betweenClosedStream(Math.min(boundingBox0.minX(), boundingBox0.maxX()), Math.min(boundingBox0.minY(), boundingBox0.maxY()), Math.min(boundingBox0.minZ(), boundingBox0.maxZ()), Math.max(boundingBox0.minX(), boundingBox0.maxX()), Math.max(boundingBox0.minY(), boundingBox0.maxY()), Math.max(boundingBox0.minZ(), boundingBox0.maxZ()));
    }

    public static Stream<BlockPos> betweenClosedStream(AABB aABB0) {
        return betweenClosedStream(Mth.floor(aABB0.minX), Mth.floor(aABB0.minY), Mth.floor(aABB0.minZ), Mth.floor(aABB0.maxX), Mth.floor(aABB0.maxY), Mth.floor(aABB0.maxZ));
    }

    public static Stream<BlockPos> betweenClosedStream(int int0, int int1, int int2, int int3, int int4, int int5) {
        return StreamSupport.stream(betweenClosed(int0, int1, int2, int3, int4, int5).spliterator(), false);
    }

    public static Iterable<BlockPos> betweenClosed(int int0, int int1, int int2, int int3, int int4, int int5) {
        int $$6 = int3 - int0 + 1;
        int $$7 = int4 - int1 + 1;
        int $$8 = int5 - int2 + 1;
        int $$9 = $$6 * $$7 * $$8;
        return () -> new AbstractIterator<BlockPos>() {

            private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

            private int index;

            protected BlockPos computeNext() {
                if (this.index == $$9) {
                    return (BlockPos) this.endOfData();
                } else {
                    int $$0 = this.index % $$6;
                    int $$1 = this.index / $$6;
                    int $$2 = $$1 % $$7;
                    int $$3 = $$1 / $$7;
                    this.index++;
                    return this.cursor.set(int0 + $$0, int1 + $$2, int2 + $$3);
                }
            }
        };
    }

    public static Iterable<BlockPos.MutableBlockPos> spiralAround(BlockPos blockPos0, int int1, Direction direction2, Direction direction3) {
        Validate.validState(direction2.getAxis() != direction3.getAxis(), "The two directions cannot be on the same axis", new Object[0]);
        return () -> new AbstractIterator<BlockPos.MutableBlockPos>() {

            private final Direction[] directions = new Direction[] { direction2, direction3, direction2.getOpposite(), direction3.getOpposite() };

            private final BlockPos.MutableBlockPos cursor = blockPos0.mutable().move(direction3);

            private final int legs = 4 * int1;

            private int leg = -1;

            private int legSize;

            private int legIndex;

            private int lastX = this.cursor.m_123341_();

            private int lastY = this.cursor.m_123342_();

            private int lastZ = this.cursor.m_123343_();

            protected BlockPos.MutableBlockPos computeNext() {
                this.cursor.set(this.lastX, this.lastY, this.lastZ).move(this.directions[(this.leg + 4) % 4]);
                this.lastX = this.cursor.m_123341_();
                this.lastY = this.cursor.m_123342_();
                this.lastZ = this.cursor.m_123343_();
                if (this.legIndex >= this.legSize) {
                    if (this.leg >= this.legs) {
                        return (BlockPos.MutableBlockPos) this.endOfData();
                    }
                    this.leg++;
                    this.legIndex = 0;
                    this.legSize = this.leg / 2 + 1;
                }
                this.legIndex++;
                return this.cursor;
            }
        };
    }

    public static int breadthFirstTraversal(BlockPos blockPos0, int int1, int int2, BiConsumer<BlockPos, Consumer<BlockPos>> biConsumerBlockPosConsumerBlockPos3, Predicate<BlockPos> predicateBlockPos4) {
        Queue<Pair<BlockPos, Integer>> $$5 = new ArrayDeque();
        LongSet $$6 = new LongOpenHashSet();
        $$5.add(Pair.of(blockPos0, 0));
        int $$7 = 0;
        while (!$$5.isEmpty()) {
            Pair<BlockPos, Integer> $$8 = (Pair<BlockPos, Integer>) $$5.poll();
            BlockPos $$9 = (BlockPos) $$8.getLeft();
            int $$10 = (Integer) $$8.getRight();
            long $$11 = $$9.asLong();
            if ($$6.add($$11) && predicateBlockPos4.test($$9)) {
                if (++$$7 >= int2) {
                    return $$7;
                }
                if ($$10 < int1) {
                    biConsumerBlockPosConsumerBlockPos3.accept($$9, (Consumer) p_277234_ -> $$5.add(Pair.of(p_277234_, $$10 + 1)));
                }
            }
        }
        return $$7;
    }

    public static class MutableBlockPos extends BlockPos {

        public MutableBlockPos() {
            this(0, 0, 0);
        }

        public MutableBlockPos(int int0, int int1, int int2) {
            super(int0, int1, int2);
        }

        public MutableBlockPos(double double0, double double1, double double2) {
            this(Mth.floor(double0), Mth.floor(double1), Mth.floor(double2));
        }

        @Override
        public BlockPos offset(int int0, int int1, int int2) {
            return super.offset(int0, int1, int2).immutable();
        }

        @Override
        public BlockPos multiply(int int0) {
            return super.multiply(int0).immutable();
        }

        @Override
        public BlockPos relative(Direction direction0, int int1) {
            return super.relative(direction0, int1).immutable();
        }

        @Override
        public BlockPos relative(Direction.Axis directionAxis0, int int1) {
            return super.relative(directionAxis0, int1).immutable();
        }

        @Override
        public BlockPos rotate(Rotation rotation0) {
            return super.rotate(rotation0).immutable();
        }

        public BlockPos.MutableBlockPos set(int int0, int int1, int int2) {
            this.setX(int0);
            this.setY(int1);
            this.setZ(int2);
            return this;
        }

        public BlockPos.MutableBlockPos set(double double0, double double1, double double2) {
            return this.set(Mth.floor(double0), Mth.floor(double1), Mth.floor(double2));
        }

        public BlockPos.MutableBlockPos set(Vec3i vecI0) {
            return this.set(vecI0.getX(), vecI0.getY(), vecI0.getZ());
        }

        public BlockPos.MutableBlockPos set(long long0) {
            return this.set(m_121983_(long0), m_122008_(long0), m_122015_(long0));
        }

        public BlockPos.MutableBlockPos set(AxisCycle axisCycle0, int int1, int int2, int int3) {
            return this.set(axisCycle0.cycle(int1, int2, int3, Direction.Axis.X), axisCycle0.cycle(int1, int2, int3, Direction.Axis.Y), axisCycle0.cycle(int1, int2, int3, Direction.Axis.Z));
        }

        public BlockPos.MutableBlockPos setWithOffset(Vec3i vecI0, Direction direction1) {
            return this.set(vecI0.getX() + direction1.getStepX(), vecI0.getY() + direction1.getStepY(), vecI0.getZ() + direction1.getStepZ());
        }

        public BlockPos.MutableBlockPos setWithOffset(Vec3i vecI0, int int1, int int2, int int3) {
            return this.set(vecI0.getX() + int1, vecI0.getY() + int2, vecI0.getZ() + int3);
        }

        public BlockPos.MutableBlockPos setWithOffset(Vec3i vecI0, Vec3i vecI1) {
            return this.set(vecI0.getX() + vecI1.getX(), vecI0.getY() + vecI1.getY(), vecI0.getZ() + vecI1.getZ());
        }

        public BlockPos.MutableBlockPos move(Direction direction0) {
            return this.move(direction0, 1);
        }

        public BlockPos.MutableBlockPos move(Direction direction0, int int1) {
            return this.set(this.m_123341_() + direction0.getStepX() * int1, this.m_123342_() + direction0.getStepY() * int1, this.m_123343_() + direction0.getStepZ() * int1);
        }

        public BlockPos.MutableBlockPos move(int int0, int int1, int int2) {
            return this.set(this.m_123341_() + int0, this.m_123342_() + int1, this.m_123343_() + int2);
        }

        public BlockPos.MutableBlockPos move(Vec3i vecI0) {
            return this.set(this.m_123341_() + vecI0.getX(), this.m_123342_() + vecI0.getY(), this.m_123343_() + vecI0.getZ());
        }

        public BlockPos.MutableBlockPos clamp(Direction.Axis directionAxis0, int int1, int int2) {
            switch(directionAxis0) {
                case X:
                    return this.set(Mth.clamp(this.m_123341_(), int1, int2), this.m_123342_(), this.m_123343_());
                case Y:
                    return this.set(this.m_123341_(), Mth.clamp(this.m_123342_(), int1, int2), this.m_123343_());
                case Z:
                    return this.set(this.m_123341_(), this.m_123342_(), Mth.clamp(this.m_123343_(), int1, int2));
                default:
                    throw new IllegalStateException("Unable to clamp axis " + directionAxis0);
            }
        }

        public BlockPos.MutableBlockPos setX(int int0) {
            super.m_142451_(int0);
            return this;
        }

        public BlockPos.MutableBlockPos setY(int int0) {
            super.m_142448_(int0);
            return this;
        }

        public BlockPos.MutableBlockPos setZ(int int0) {
            super.m_142443_(int0);
            return this;
        }

        @Override
        public BlockPos immutable() {
            return new BlockPos(this);
        }
    }
}