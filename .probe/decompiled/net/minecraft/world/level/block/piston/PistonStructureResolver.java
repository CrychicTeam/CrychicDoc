package net.minecraft.world.level.block.piston;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class PistonStructureResolver {

    public static final int MAX_PUSH_DEPTH = 12;

    private final Level level;

    private final BlockPos pistonPos;

    private final boolean extending;

    private final BlockPos startPos;

    private final Direction pushDirection;

    private final List<BlockPos> toPush = Lists.newArrayList();

    private final List<BlockPos> toDestroy = Lists.newArrayList();

    private final Direction pistonDirection;

    public PistonStructureResolver(Level level0, BlockPos blockPos1, Direction direction2, boolean boolean3) {
        this.level = level0;
        this.pistonPos = blockPos1;
        this.pistonDirection = direction2;
        this.extending = boolean3;
        if (boolean3) {
            this.pushDirection = direction2;
            this.startPos = blockPos1.relative(direction2);
        } else {
            this.pushDirection = direction2.getOpposite();
            this.startPos = blockPos1.relative(direction2, 2);
        }
    }

    public boolean resolve() {
        this.toPush.clear();
        this.toDestroy.clear();
        BlockState $$0 = this.level.getBlockState(this.startPos);
        if (!PistonBaseBlock.isPushable($$0, this.level, this.startPos, this.pushDirection, false, this.pistonDirection)) {
            if (this.extending && $$0.m_60811_() == PushReaction.DESTROY) {
                this.toDestroy.add(this.startPos);
                return true;
            } else {
                return false;
            }
        } else if (!this.addBlockLine(this.startPos, this.pushDirection)) {
            return false;
        } else {
            for (int $$1 = 0; $$1 < this.toPush.size(); $$1++) {
                BlockPos $$2 = (BlockPos) this.toPush.get($$1);
                if (isSticky(this.level.getBlockState($$2)) && !this.addBranchingBlocks($$2)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean isSticky(BlockState blockState0) {
        return blockState0.m_60713_(Blocks.SLIME_BLOCK) || blockState0.m_60713_(Blocks.HONEY_BLOCK);
    }

    private static boolean canStickToEachOther(BlockState blockState0, BlockState blockState1) {
        if (blockState0.m_60713_(Blocks.HONEY_BLOCK) && blockState1.m_60713_(Blocks.SLIME_BLOCK)) {
            return false;
        } else {
            return blockState0.m_60713_(Blocks.SLIME_BLOCK) && blockState1.m_60713_(Blocks.HONEY_BLOCK) ? false : isSticky(blockState0) || isSticky(blockState1);
        }
    }

    private boolean addBlockLine(BlockPos blockPos0, Direction direction1) {
        BlockState $$2 = this.level.getBlockState(blockPos0);
        if ($$2.m_60795_()) {
            return true;
        } else if (!PistonBaseBlock.isPushable($$2, this.level, blockPos0, this.pushDirection, false, direction1)) {
            return true;
        } else if (blockPos0.equals(this.pistonPos)) {
            return true;
        } else if (this.toPush.contains(blockPos0)) {
            return true;
        } else {
            int $$3 = 1;
            if ($$3 + this.toPush.size() > 12) {
                return false;
            } else {
                while (isSticky($$2)) {
                    BlockPos $$4 = blockPos0.relative(this.pushDirection.getOpposite(), $$3);
                    BlockState $$5 = $$2;
                    $$2 = this.level.getBlockState($$4);
                    if ($$2.m_60795_() || !canStickToEachOther($$5, $$2) || !PistonBaseBlock.isPushable($$2, this.level, $$4, this.pushDirection, false, this.pushDirection.getOpposite()) || $$4.equals(this.pistonPos)) {
                        break;
                    }
                    if (++$$3 + this.toPush.size() > 12) {
                        return false;
                    }
                }
                int $$6 = 0;
                for (int $$7 = $$3 - 1; $$7 >= 0; $$7--) {
                    this.toPush.add(blockPos0.relative(this.pushDirection.getOpposite(), $$7));
                    $$6++;
                }
                int $$8 = 1;
                while (true) {
                    BlockPos $$9 = blockPos0.relative(this.pushDirection, $$8);
                    int $$10 = this.toPush.indexOf($$9);
                    if ($$10 > -1) {
                        this.reorderListAtCollision($$6, $$10);
                        for (int $$11 = 0; $$11 <= $$10 + $$6; $$11++) {
                            BlockPos $$12 = (BlockPos) this.toPush.get($$11);
                            if (isSticky(this.level.getBlockState($$12)) && !this.addBranchingBlocks($$12)) {
                                return false;
                            }
                        }
                        return true;
                    }
                    $$2 = this.level.getBlockState($$9);
                    if ($$2.m_60795_()) {
                        return true;
                    }
                    if (!PistonBaseBlock.isPushable($$2, this.level, $$9, this.pushDirection, true, this.pushDirection) || $$9.equals(this.pistonPos)) {
                        return false;
                    }
                    if ($$2.m_60811_() == PushReaction.DESTROY) {
                        this.toDestroy.add($$9);
                        return true;
                    }
                    if (this.toPush.size() >= 12) {
                        return false;
                    }
                    this.toPush.add($$9);
                    $$6++;
                    $$8++;
                }
            }
        }
    }

    private void reorderListAtCollision(int int0, int int1) {
        List<BlockPos> $$2 = Lists.newArrayList();
        List<BlockPos> $$3 = Lists.newArrayList();
        List<BlockPos> $$4 = Lists.newArrayList();
        $$2.addAll(this.toPush.subList(0, int1));
        $$3.addAll(this.toPush.subList(this.toPush.size() - int0, this.toPush.size()));
        $$4.addAll(this.toPush.subList(int1, this.toPush.size() - int0));
        this.toPush.clear();
        this.toPush.addAll($$2);
        this.toPush.addAll($$3);
        this.toPush.addAll($$4);
    }

    private boolean addBranchingBlocks(BlockPos blockPos0) {
        BlockState $$1 = this.level.getBlockState(blockPos0);
        for (Direction $$2 : Direction.values()) {
            if ($$2.getAxis() != this.pushDirection.getAxis()) {
                BlockPos $$3 = blockPos0.relative($$2);
                BlockState $$4 = this.level.getBlockState($$3);
                if (canStickToEachOther($$4, $$1) && !this.addBlockLine($$3, $$2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Direction getPushDirection() {
        return this.pushDirection;
    }

    public List<BlockPos> getToPush() {
        return this.toPush;
    }

    public List<BlockPos> getToDestroy() {
        return this.toDestroy;
    }
}