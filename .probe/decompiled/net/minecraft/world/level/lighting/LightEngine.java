package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Arrays;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LightChunk;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class LightEngine<M extends DataLayerStorageMap<M>, S extends LayerLightSectionStorage<M>> implements LayerLightEventListener {

    public static final int MAX_LEVEL = 15;

    protected static final int MIN_OPACITY = 1;

    protected static final long PULL_LIGHT_IN_ENTRY = LightEngine.QueueEntry.decreaseAllDirections(1);

    private static final int MIN_QUEUE_SIZE = 512;

    protected static final Direction[] PROPAGATION_DIRECTIONS = Direction.values();

    protected final LightChunkGetter chunkSource;

    protected final S storage;

    private final LongOpenHashSet blockNodesToCheck = new LongOpenHashSet(512, 0.5F);

    private final LongArrayFIFOQueue decreaseQueue = new LongArrayFIFOQueue();

    private final LongArrayFIFOQueue increaseQueue = new LongArrayFIFOQueue();

    private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

    private static final int CACHE_SIZE = 2;

    private final long[] lastChunkPos = new long[2];

    private final LightChunk[] lastChunk = new LightChunk[2];

    protected LightEngine(LightChunkGetter lightChunkGetter0, S s1) {
        this.chunkSource = lightChunkGetter0;
        this.storage = s1;
        this.clearChunkCache();
    }

    public static boolean hasDifferentLightProperties(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, BlockState blockState3) {
        return blockState3 == blockState2 ? false : blockState3.m_60739_(blockGetter0, blockPos1) != blockState2.m_60739_(blockGetter0, blockPos1) || blockState3.m_60791_() != blockState2.m_60791_() || blockState3.m_60787_() || blockState2.m_60787_();
    }

    public static int getLightBlockInto(BlockGetter blockGetter0, BlockState blockState1, BlockPos blockPos2, BlockState blockState3, BlockPos blockPos4, Direction direction5, int int6) {
        boolean $$7 = isEmptyShape(blockState1);
        boolean $$8 = isEmptyShape(blockState3);
        if ($$7 && $$8) {
            return int6;
        } else {
            VoxelShape $$9 = $$7 ? Shapes.empty() : blockState1.m_60768_(blockGetter0, blockPos2);
            VoxelShape $$10 = $$8 ? Shapes.empty() : blockState3.m_60768_(blockGetter0, blockPos4);
            return Shapes.mergedFaceOccludes($$9, $$10, direction5) ? 16 : int6;
        }
    }

    public static VoxelShape getOcclusionShape(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, Direction direction3) {
        return isEmptyShape(blockState2) ? Shapes.empty() : blockState2.m_60655_(blockGetter0, blockPos1, direction3);
    }

    protected static boolean isEmptyShape(BlockState blockState0) {
        return !blockState0.m_60815_() || !blockState0.m_60787_();
    }

    protected BlockState getState(BlockPos blockPos0) {
        int $$1 = SectionPos.blockToSectionCoord(blockPos0.m_123341_());
        int $$2 = SectionPos.blockToSectionCoord(blockPos0.m_123343_());
        LightChunk $$3 = this.getChunk($$1, $$2);
        return $$3 == null ? Blocks.BEDROCK.defaultBlockState() : $$3.m_8055_(blockPos0);
    }

    protected int getOpacity(BlockState blockState0, BlockPos blockPos1) {
        return Math.max(1, blockState0.m_60739_(this.chunkSource.getLevel(), blockPos1));
    }

    protected boolean shapeOccludes(long long0, BlockState blockState1, long long2, BlockState blockState3, Direction direction4) {
        VoxelShape $$5 = this.getOcclusionShape(blockState1, long0, direction4);
        VoxelShape $$6 = this.getOcclusionShape(blockState3, long2, direction4.getOpposite());
        return Shapes.faceShapeOccludes($$5, $$6);
    }

    protected VoxelShape getOcclusionShape(BlockState blockState0, long long1, Direction direction2) {
        return getOcclusionShape(this.chunkSource.getLevel(), this.mutablePos.set(long1), blockState0, direction2);
    }

    @Nullable
    protected LightChunk getChunk(int int0, int int1) {
        long $$2 = ChunkPos.asLong(int0, int1);
        for (int $$3 = 0; $$3 < 2; $$3++) {
            if ($$2 == this.lastChunkPos[$$3]) {
                return this.lastChunk[$$3];
            }
        }
        LightChunk $$4 = this.chunkSource.getChunkForLighting(int0, int1);
        for (int $$5 = 1; $$5 > 0; $$5--) {
            this.lastChunkPos[$$5] = this.lastChunkPos[$$5 - 1];
            this.lastChunk[$$5] = this.lastChunk[$$5 - 1];
        }
        this.lastChunkPos[0] = $$2;
        this.lastChunk[0] = $$4;
        return $$4;
    }

    private void clearChunkCache() {
        Arrays.fill(this.lastChunkPos, ChunkPos.INVALID_CHUNK_POS);
        Arrays.fill(this.lastChunk, null);
    }

    @Override
    public void checkBlock(BlockPos blockPos0) {
        this.blockNodesToCheck.add(blockPos0.asLong());
    }

    public void queueSectionData(long long0, @Nullable DataLayer dataLayer1) {
        this.storage.queueSectionData(long0, dataLayer1);
    }

    public void retainData(ChunkPos chunkPos0, boolean boolean1) {
        this.storage.retainData(SectionPos.getZeroNode(chunkPos0.x, chunkPos0.z), boolean1);
    }

    @Override
    public void updateSectionStatus(SectionPos sectionPos0, boolean boolean1) {
        this.storage.updateSectionStatus(sectionPos0.asLong(), boolean1);
    }

    @Override
    public void setLightEnabled(ChunkPos chunkPos0, boolean boolean1) {
        this.storage.setLightEnabled(SectionPos.getZeroNode(chunkPos0.x, chunkPos0.z), boolean1);
    }

    @Override
    public int runLightUpdates() {
        LongIterator $$0 = this.blockNodesToCheck.iterator();
        while ($$0.hasNext()) {
            this.checkNode($$0.nextLong());
        }
        this.blockNodesToCheck.clear();
        this.blockNodesToCheck.trim(512);
        int $$1 = 0;
        $$1 += this.propagateDecreases();
        $$1 += this.propagateIncreases();
        this.clearChunkCache();
        this.storage.markNewInconsistencies(this);
        this.storage.swapSectionMap();
        return $$1;
    }

    private int propagateIncreases() {
        int $$0;
        for ($$0 = 0; !this.increaseQueue.isEmpty(); $$0++) {
            long $$1 = this.increaseQueue.dequeueLong();
            long $$2 = this.increaseQueue.dequeueLong();
            int $$3 = this.storage.getStoredLevel($$1);
            int $$4 = LightEngine.QueueEntry.getFromLevel($$2);
            if (LightEngine.QueueEntry.isIncreaseFromEmission($$2) && $$3 < $$4) {
                this.storage.setStoredLevel($$1, $$4);
                $$3 = $$4;
            }
            if ($$3 == $$4) {
                this.propagateIncrease($$1, $$2, $$3);
            }
        }
        return $$0;
    }

    private int propagateDecreases() {
        int $$0;
        for ($$0 = 0; !this.decreaseQueue.isEmpty(); $$0++) {
            long $$1 = this.decreaseQueue.dequeueLong();
            long $$2 = this.decreaseQueue.dequeueLong();
            this.propagateDecrease($$1, $$2);
        }
        return $$0;
    }

    protected void enqueueDecrease(long long0, long long1) {
        this.decreaseQueue.enqueue(long0);
        this.decreaseQueue.enqueue(long1);
    }

    protected void enqueueIncrease(long long0, long long1) {
        this.increaseQueue.enqueue(long0);
        this.increaseQueue.enqueue(long1);
    }

    @Override
    public boolean hasLightWork() {
        return this.storage.hasInconsistencies() || !this.blockNodesToCheck.isEmpty() || !this.decreaseQueue.isEmpty() || !this.increaseQueue.isEmpty();
    }

    @Nullable
    @Override
    public DataLayer getDataLayerData(SectionPos sectionPos0) {
        return this.storage.getDataLayerData(sectionPos0.asLong());
    }

    @Override
    public int getLightValue(BlockPos blockPos0) {
        return this.storage.getLightValue(blockPos0.asLong());
    }

    public String getDebugData(long long0) {
        return this.getDebugSectionType(long0).display();
    }

    public LayerLightSectionStorage.SectionType getDebugSectionType(long long0) {
        return this.storage.getDebugSectionType(long0);
    }

    protected abstract void checkNode(long var1);

    protected abstract void propagateIncrease(long var1, long var3, int var5);

    protected abstract void propagateDecrease(long var1, long var3);

    public static class QueueEntry {

        private static final int FROM_LEVEL_BITS = 4;

        private static final int DIRECTION_BITS = 6;

        private static final long LEVEL_MASK = 15L;

        private static final long DIRECTIONS_MASK = 1008L;

        private static final long FLAG_FROM_EMPTY_SHAPE = 1024L;

        private static final long FLAG_INCREASE_FROM_EMISSION = 2048L;

        public static long decreaseSkipOneDirection(int int0, Direction direction1) {
            long $$2 = withoutDirection(1008L, direction1);
            return withLevel($$2, int0);
        }

        public static long decreaseAllDirections(int int0) {
            return withLevel(1008L, int0);
        }

        public static long increaseLightFromEmission(int int0, boolean boolean1) {
            long $$2 = 1008L;
            $$2 |= 2048L;
            if (boolean1) {
                $$2 |= 1024L;
            }
            return withLevel($$2, int0);
        }

        public static long increaseSkipOneDirection(int int0, boolean boolean1, Direction direction2) {
            long $$3 = withoutDirection(1008L, direction2);
            if (boolean1) {
                $$3 |= 1024L;
            }
            return withLevel($$3, int0);
        }

        public static long increaseOnlyOneDirection(int int0, boolean boolean1, Direction direction2) {
            long $$3 = 0L;
            if (boolean1) {
                $$3 |= 1024L;
            }
            $$3 = withDirection($$3, direction2);
            return withLevel($$3, int0);
        }

        public static long increaseSkySourceInDirections(boolean boolean0, boolean boolean1, boolean boolean2, boolean boolean3, boolean boolean4) {
            long $$5 = withLevel(0L, 15);
            if (boolean0) {
                $$5 = withDirection($$5, Direction.DOWN);
            }
            if (boolean1) {
                $$5 = withDirection($$5, Direction.NORTH);
            }
            if (boolean2) {
                $$5 = withDirection($$5, Direction.SOUTH);
            }
            if (boolean3) {
                $$5 = withDirection($$5, Direction.WEST);
            }
            if (boolean4) {
                $$5 = withDirection($$5, Direction.EAST);
            }
            return $$5;
        }

        public static int getFromLevel(long long0) {
            return (int) (long0 & 15L);
        }

        public static boolean isFromEmptyShape(long long0) {
            return (long0 & 1024L) != 0L;
        }

        public static boolean isIncreaseFromEmission(long long0) {
            return (long0 & 2048L) != 0L;
        }

        public static boolean shouldPropagateInDirection(long long0, Direction direction1) {
            return (long0 & 1L << direction1.ordinal() + 4) != 0L;
        }

        private static long withLevel(long long0, int int1) {
            return long0 & -16L | (long) int1 & 15L;
        }

        private static long withDirection(long long0, Direction direction1) {
            return long0 | 1L << direction1.ordinal() + 4;
        }

        private static long withoutDirection(long long0, Direction direction1) {
            return long0 & ~(1L << direction1.ordinal() + 4);
        }
    }
}