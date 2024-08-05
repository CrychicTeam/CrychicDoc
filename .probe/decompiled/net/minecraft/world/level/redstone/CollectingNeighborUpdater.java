package net.minecraft.world.level.redstone;

import com.mojang.logging.LogUtils;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public class CollectingNeighborUpdater implements NeighborUpdater {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Level level;

    private final int maxChainedNeighborUpdates;

    private final ArrayDeque<CollectingNeighborUpdater.NeighborUpdates> stack = new ArrayDeque();

    private final List<CollectingNeighborUpdater.NeighborUpdates> addedThisLayer = new ArrayList();

    private int count = 0;

    public CollectingNeighborUpdater(Level level0, int int1) {
        this.level = level0;
        this.maxChainedNeighborUpdates = int1;
    }

    @Override
    public void shapeUpdate(Direction direction0, BlockState blockState1, BlockPos blockPos2, BlockPos blockPos3, int int4, int int5) {
        this.addAndRun(blockPos2, new CollectingNeighborUpdater.ShapeUpdate(direction0, blockState1, blockPos2.immutable(), blockPos3.immutable(), int4, int5));
    }

    @Override
    public void neighborChanged(BlockPos blockPos0, Block block1, BlockPos blockPos2) {
        this.addAndRun(blockPos0, new CollectingNeighborUpdater.SimpleNeighborUpdate(blockPos0, block1, blockPos2.immutable()));
    }

    @Override
    public void neighborChanged(BlockState blockState0, BlockPos blockPos1, Block block2, BlockPos blockPos3, boolean boolean4) {
        this.addAndRun(blockPos1, new CollectingNeighborUpdater.FullNeighborUpdate(blockState0, blockPos1.immutable(), block2, blockPos3.immutable(), boolean4));
    }

    @Override
    public void updateNeighborsAtExceptFromFacing(BlockPos blockPos0, Block block1, @Nullable Direction direction2) {
        this.addAndRun(blockPos0, new CollectingNeighborUpdater.MultiNeighborUpdate(blockPos0.immutable(), block1, direction2));
    }

    private void addAndRun(BlockPos blockPos0, CollectingNeighborUpdater.NeighborUpdates collectingNeighborUpdaterNeighborUpdates1) {
        boolean $$2 = this.count > 0;
        boolean $$3 = this.maxChainedNeighborUpdates >= 0 && this.count >= this.maxChainedNeighborUpdates;
        this.count++;
        if (!$$3) {
            if ($$2) {
                this.addedThisLayer.add(collectingNeighborUpdaterNeighborUpdates1);
            } else {
                this.stack.push(collectingNeighborUpdaterNeighborUpdates1);
            }
        } else if (this.count - 1 == this.maxChainedNeighborUpdates) {
            LOGGER.error("Too many chained neighbor updates. Skipping the rest. First skipped position: " + blockPos0.m_123344_());
        }
        if (!$$2) {
            this.runUpdates();
        }
    }

    private void runUpdates() {
        try {
            while (!this.stack.isEmpty() || !this.addedThisLayer.isEmpty()) {
                for (int $$0 = this.addedThisLayer.size() - 1; $$0 >= 0; $$0--) {
                    this.stack.push((CollectingNeighborUpdater.NeighborUpdates) this.addedThisLayer.get($$0));
                }
                this.addedThisLayer.clear();
                CollectingNeighborUpdater.NeighborUpdates $$1 = (CollectingNeighborUpdater.NeighborUpdates) this.stack.peek();
                while (this.addedThisLayer.isEmpty()) {
                    if (!$$1.runNext(this.level)) {
                        this.stack.pop();
                        break;
                    }
                }
            }
        } finally {
            this.stack.clear();
            this.addedThisLayer.clear();
            this.count = 0;
        }
    }

    static record FullNeighborUpdate(BlockState f_230670_, BlockPos f_230671_, Block f_230672_, BlockPos f_230673_, boolean f_230674_) implements CollectingNeighborUpdater.NeighborUpdates {

        private final BlockState state;

        private final BlockPos pos;

        private final Block block;

        private final BlockPos neighborPos;

        private final boolean movedByPiston;

        FullNeighborUpdate(BlockState f_230670_, BlockPos f_230671_, Block f_230672_, BlockPos f_230673_, boolean f_230674_) {
            this.state = f_230670_;
            this.pos = f_230671_;
            this.block = f_230672_;
            this.neighborPos = f_230673_;
            this.movedByPiston = f_230674_;
        }

        @Override
        public boolean runNext(Level p_230683_) {
            NeighborUpdater.executeUpdate(p_230683_, this.state, this.pos, this.block, this.neighborPos, this.movedByPiston);
            return false;
        }
    }

    static final class MultiNeighborUpdate implements CollectingNeighborUpdater.NeighborUpdates {

        private final BlockPos sourcePos;

        private final Block sourceBlock;

        @Nullable
        private final Direction skipDirection;

        private int idx = 0;

        MultiNeighborUpdate(BlockPos blockPos0, Block block1, @Nullable Direction direction2) {
            this.sourcePos = blockPos0;
            this.sourceBlock = block1;
            this.skipDirection = direction2;
            if (NeighborUpdater.UPDATE_ORDER[this.idx] == direction2) {
                this.idx++;
            }
        }

        @Override
        public boolean runNext(Level level0) {
            BlockPos $$1 = this.sourcePos.relative(NeighborUpdater.UPDATE_ORDER[this.idx++]);
            BlockState $$2 = level0.getBlockState($$1);
            $$2.m_60690_(level0, $$1, this.sourceBlock, this.sourcePos, false);
            if (this.idx < NeighborUpdater.UPDATE_ORDER.length && NeighborUpdater.UPDATE_ORDER[this.idx] == this.skipDirection) {
                this.idx++;
            }
            return this.idx < NeighborUpdater.UPDATE_ORDER.length;
        }
    }

    interface NeighborUpdates {

        boolean runNext(Level var1);
    }

    static record ShapeUpdate(Direction f_230703_, BlockState f_230704_, BlockPos f_230705_, BlockPos f_230706_, int f_230707_, int f_276599_) implements CollectingNeighborUpdater.NeighborUpdates {

        private final Direction direction;

        private final BlockState state;

        private final BlockPos pos;

        private final BlockPos neighborPos;

        private final int updateFlags;

        private final int updateLimit;

        ShapeUpdate(Direction f_230703_, BlockState f_230704_, BlockPos f_230705_, BlockPos f_230706_, int f_230707_, int f_276599_) {
            this.direction = f_230703_;
            this.state = f_230704_;
            this.pos = f_230705_;
            this.neighborPos = f_230706_;
            this.updateFlags = f_230707_;
            this.updateLimit = f_276599_;
        }

        @Override
        public boolean runNext(Level p_230716_) {
            NeighborUpdater.executeShapeUpdate(p_230716_, this.direction, this.state, this.pos, this.neighborPos, this.updateFlags, this.updateLimit);
            return false;
        }
    }

    static record SimpleNeighborUpdate(BlockPos f_230725_, Block f_230726_, BlockPos f_230727_) implements CollectingNeighborUpdater.NeighborUpdates {

        private final BlockPos pos;

        private final Block block;

        private final BlockPos neighborPos;

        SimpleNeighborUpdate(BlockPos f_230725_, Block f_230726_, BlockPos f_230727_) {
            this.pos = f_230725_;
            this.block = f_230726_;
            this.neighborPos = f_230727_;
        }

        @Override
        public boolean runNext(Level p_230734_) {
            BlockState $$1 = p_230734_.getBlockState(this.pos);
            NeighborUpdater.executeUpdate(p_230734_, $$1, this.pos, this.block, this.neighborPos, false);
            return false;
        }
    }
}