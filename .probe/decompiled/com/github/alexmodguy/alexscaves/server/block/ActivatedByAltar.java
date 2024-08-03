package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.message.WorldEventMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface ActivatedByAltar {

    int MAX_DISTANCE = 15;

    BooleanProperty ACTIVE = BooleanProperty.create("active");

    IntegerProperty DISTANCE = IntegerProperty.create("distance", 1, 15);

    static int getDistanceAt(BlockState blockState) {
        if (blockState.m_60713_(ACBlockRegistry.ABYSSAL_ALTAR.get()) && (Boolean) blockState.m_61143_(AbyssalAltarBlock.ACTIVE)) {
            return 0;
        } else {
            return blockState.m_60734_() instanceof ActivatedByAltar ? (Integer) blockState.m_61143_(DISTANCE) : 15;
        }
    }

    default boolean activeDistance(BlockState state) {
        return (Integer) state.m_61143_(DISTANCE) < 15;
    }

    default boolean activeDistance(int distance) {
        return distance < 15;
    }

    default void updateDistanceShape(Level accessor, BlockState state, BlockPos pos) {
        int i = getDistanceAt(state) + 1;
        if (i != 1 || (Integer) state.m_61143_(DISTANCE) != i) {
            accessor.m_186460_(pos, (Block) this, 1);
        }
    }

    default BlockState updateDistance(BlockState state, Level level, BlockPos blockPos) {
        int i = 15;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            blockpos$mutableblockpos.setWithOffset(blockPos, direction);
            i = Math.min(i, getDistanceAt(level.getBlockState(blockpos$mutableblockpos)) + 1);
            if (i == 1) {
                break;
            }
        }
        boolean prevActive = (Boolean) state.m_61143_(ACTIVE);
        int prevDist = (Integer) state.m_61143_(DISTANCE);
        BlockState state1 = (BlockState) state.m_61124_(DISTANCE, i);
        if (i <= 1 && !prevActive) {
            AlexsCaves.sendMSGToAll(new WorldEventMessage(1, blockPos.m_123341_(), blockPos.m_123342_(), blockPos.m_123343_()));
        }
        if (prevDist <= 1 && prevActive) {
            AlexsCaves.sendMSGToAll(new WorldEventMessage(2, blockPos.m_123341_(), blockPos.m_123342_(), blockPos.m_123343_()));
        }
        return this.activeDistance(i) ? (BlockState) state1.m_61124_(ACTIVE, true) : (BlockState) state1.m_61124_(ACTIVE, false);
    }
}