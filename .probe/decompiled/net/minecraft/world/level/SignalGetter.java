package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface SignalGetter extends BlockGetter {

    Direction[] DIRECTIONS = Direction.values();

    default int getDirectSignal(BlockPos blockPos0, Direction direction1) {
        return this.m_8055_(blockPos0).m_60775_(this, blockPos0, direction1);
    }

    default int getDirectSignalTo(BlockPos blockPos0) {
        int $$1 = 0;
        $$1 = Math.max($$1, this.getDirectSignal(blockPos0.below(), Direction.DOWN));
        if ($$1 >= 15) {
            return $$1;
        } else {
            $$1 = Math.max($$1, this.getDirectSignal(blockPos0.above(), Direction.UP));
            if ($$1 >= 15) {
                return $$1;
            } else {
                $$1 = Math.max($$1, this.getDirectSignal(blockPos0.north(), Direction.NORTH));
                if ($$1 >= 15) {
                    return $$1;
                } else {
                    $$1 = Math.max($$1, this.getDirectSignal(blockPos0.south(), Direction.SOUTH));
                    if ($$1 >= 15) {
                        return $$1;
                    } else {
                        $$1 = Math.max($$1, this.getDirectSignal(blockPos0.west(), Direction.WEST));
                        if ($$1 >= 15) {
                            return $$1;
                        } else {
                            $$1 = Math.max($$1, this.getDirectSignal(blockPos0.east(), Direction.EAST));
                            return $$1 >= 15 ? $$1 : $$1;
                        }
                    }
                }
            }
        }
    }

    default int getControlInputSignal(BlockPos blockPos0, Direction direction1, boolean boolean2) {
        BlockState $$3 = this.m_8055_(blockPos0);
        if (boolean2) {
            return DiodeBlock.isDiode($$3) ? this.getDirectSignal(blockPos0, direction1) : 0;
        } else if ($$3.m_60713_(Blocks.REDSTONE_BLOCK)) {
            return 15;
        } else if ($$3.m_60713_(Blocks.REDSTONE_WIRE)) {
            return (Integer) $$3.m_61143_(RedStoneWireBlock.POWER);
        } else {
            return $$3.m_60803_() ? this.getDirectSignal(blockPos0, direction1) : 0;
        }
    }

    default boolean hasSignal(BlockPos blockPos0, Direction direction1) {
        return this.getSignal(blockPos0, direction1) > 0;
    }

    default int getSignal(BlockPos blockPos0, Direction direction1) {
        BlockState $$2 = this.m_8055_(blockPos0);
        int $$3 = $$2.m_60746_(this, blockPos0, direction1);
        return $$2.m_60796_(this, blockPos0) ? Math.max($$3, this.getDirectSignalTo(blockPos0)) : $$3;
    }

    default boolean hasNeighborSignal(BlockPos blockPos0) {
        if (this.getSignal(blockPos0.below(), Direction.DOWN) > 0) {
            return true;
        } else if (this.getSignal(blockPos0.above(), Direction.UP) > 0) {
            return true;
        } else if (this.getSignal(blockPos0.north(), Direction.NORTH) > 0) {
            return true;
        } else if (this.getSignal(blockPos0.south(), Direction.SOUTH) > 0) {
            return true;
        } else {
            return this.getSignal(blockPos0.west(), Direction.WEST) > 0 ? true : this.getSignal(blockPos0.east(), Direction.EAST) > 0;
        }
    }

    default int getBestNeighborSignal(BlockPos blockPos0) {
        int $$1 = 0;
        for (Direction $$2 : DIRECTIONS) {
            int $$3 = this.getSignal(blockPos0.relative($$2), $$2);
            if ($$3 >= 15) {
                return 15;
            }
            if ($$3 > $$1) {
                $$1 = $$3;
            }
        }
        return $$1;
    }
}