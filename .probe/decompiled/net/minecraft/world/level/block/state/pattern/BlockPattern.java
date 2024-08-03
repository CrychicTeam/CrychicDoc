package net.minecraft.world.level.block.state.pattern;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelReader;

public class BlockPattern {

    private final Predicate<BlockInWorld>[][][] pattern;

    private final int depth;

    private final int height;

    private final int width;

    public BlockPattern(Predicate<BlockInWorld>[][][] predicateBlockInWorld0) {
        this.pattern = predicateBlockInWorld0;
        this.depth = predicateBlockInWorld0.length;
        if (this.depth > 0) {
            this.height = predicateBlockInWorld0[0].length;
            if (this.height > 0) {
                this.width = predicateBlockInWorld0[0][0].length;
            } else {
                this.width = 0;
            }
        } else {
            this.height = 0;
            this.width = 0;
        }
    }

    public int getDepth() {
        return this.depth;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    @VisibleForTesting
    public Predicate<BlockInWorld>[][][] getPattern() {
        return this.pattern;
    }

    @Nullable
    @VisibleForTesting
    public BlockPattern.BlockPatternMatch matches(LevelReader levelReader0, BlockPos blockPos1, Direction direction2, Direction direction3) {
        LoadingCache<BlockPos, BlockInWorld> $$4 = createLevelCache(levelReader0, false);
        return this.matches(blockPos1, direction2, direction3, $$4);
    }

    @Nullable
    private BlockPattern.BlockPatternMatch matches(BlockPos blockPos0, Direction direction1, Direction direction2, LoadingCache<BlockPos, BlockInWorld> loadingCacheBlockPosBlockInWorld3) {
        for (int $$4 = 0; $$4 < this.width; $$4++) {
            for (int $$5 = 0; $$5 < this.height; $$5++) {
                for (int $$6 = 0; $$6 < this.depth; $$6++) {
                    if (!this.pattern[$$6][$$5][$$4].test((BlockInWorld) loadingCacheBlockPosBlockInWorld3.getUnchecked(translateAndRotate(blockPos0, direction1, direction2, $$4, $$5, $$6)))) {
                        return null;
                    }
                }
            }
        }
        return new BlockPattern.BlockPatternMatch(blockPos0, direction1, direction2, loadingCacheBlockPosBlockInWorld3, this.width, this.height, this.depth);
    }

    @Nullable
    public BlockPattern.BlockPatternMatch find(LevelReader levelReader0, BlockPos blockPos1) {
        LoadingCache<BlockPos, BlockInWorld> $$2 = createLevelCache(levelReader0, false);
        int $$3 = Math.max(Math.max(this.width, this.height), this.depth);
        for (BlockPos $$4 : BlockPos.betweenClosed(blockPos1, blockPos1.offset($$3 - 1, $$3 - 1, $$3 - 1))) {
            for (Direction $$5 : Direction.values()) {
                for (Direction $$6 : Direction.values()) {
                    if ($$6 != $$5 && $$6 != $$5.getOpposite()) {
                        BlockPattern.BlockPatternMatch $$7 = this.matches($$4, $$5, $$6, $$2);
                        if ($$7 != null) {
                            return $$7;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static LoadingCache<BlockPos, BlockInWorld> createLevelCache(LevelReader levelReader0, boolean boolean1) {
        return CacheBuilder.newBuilder().build(new BlockPattern.BlockCacheLoader(levelReader0, boolean1));
    }

    protected static BlockPos translateAndRotate(BlockPos blockPos0, Direction direction1, Direction direction2, int int3, int int4, int int5) {
        if (direction1 != direction2 && direction1 != direction2.getOpposite()) {
            Vec3i $$6 = new Vec3i(direction1.getStepX(), direction1.getStepY(), direction1.getStepZ());
            Vec3i $$7 = new Vec3i(direction2.getStepX(), direction2.getStepY(), direction2.getStepZ());
            Vec3i $$8 = $$6.cross($$7);
            return blockPos0.offset($$7.getX() * -int4 + $$8.getX() * int3 + $$6.getX() * int5, $$7.getY() * -int4 + $$8.getY() * int3 + $$6.getY() * int5, $$7.getZ() * -int4 + $$8.getZ() * int3 + $$6.getZ() * int5);
        } else {
            throw new IllegalArgumentException("Invalid forwards & up combination");
        }
    }

    static class BlockCacheLoader extends CacheLoader<BlockPos, BlockInWorld> {

        private final LevelReader level;

        private final boolean loadChunks;

        public BlockCacheLoader(LevelReader levelReader0, boolean boolean1) {
            this.level = levelReader0;
            this.loadChunks = boolean1;
        }

        public BlockInWorld load(BlockPos blockPos0) {
            return new BlockInWorld(this.level, blockPos0, this.loadChunks);
        }
    }

    public static class BlockPatternMatch {

        private final BlockPos frontTopLeft;

        private final Direction forwards;

        private final Direction up;

        private final LoadingCache<BlockPos, BlockInWorld> cache;

        private final int width;

        private final int height;

        private final int depth;

        public BlockPatternMatch(BlockPos blockPos0, Direction direction1, Direction direction2, LoadingCache<BlockPos, BlockInWorld> loadingCacheBlockPosBlockInWorld3, int int4, int int5, int int6) {
            this.frontTopLeft = blockPos0;
            this.forwards = direction1;
            this.up = direction2;
            this.cache = loadingCacheBlockPosBlockInWorld3;
            this.width = int4;
            this.height = int5;
            this.depth = int6;
        }

        public BlockPos getFrontTopLeft() {
            return this.frontTopLeft;
        }

        public Direction getForwards() {
            return this.forwards;
        }

        public Direction getUp() {
            return this.up;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public int getDepth() {
            return this.depth;
        }

        public BlockInWorld getBlock(int int0, int int1, int int2) {
            return (BlockInWorld) this.cache.getUnchecked(BlockPattern.translateAndRotate(this.frontTopLeft, this.getForwards(), this.getUp(), int0, int1, int2));
        }

        public String toString() {
            return MoreObjects.toStringHelper(this).add("up", this.up).add("forwards", this.forwards).add("frontTopLeft", this.frontTopLeft).toString();
        }
    }
}