package team.lodestar.lodestone.helpers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockStateHelper {

    public static BlockState getBlockStateWithExistingProperties(BlockState oldState, BlockState newState) {
        BlockState finalState = newState;
        for (Property<?> property : oldState.m_61147_()) {
            if (newState.m_61138_(property)) {
                finalState = newStateWithOldProperty(oldState, finalState, property);
            }
        }
        return finalState;
    }

    public static BlockState setBlockStateWithExistingProperties(Level level, BlockPos pos, BlockState newState, int flags) {
        BlockState oldState = level.getBlockState(pos);
        BlockState finalState = getBlockStateWithExistingProperties(oldState, newState);
        level.sendBlockUpdated(pos, oldState, finalState, flags);
        level.setBlock(pos, finalState, flags);
        return finalState;
    }

    public static <T extends Comparable<T>> BlockState newStateWithOldProperty(BlockState oldState, BlockState newState, Property<T> property) {
        return (BlockState) newState.m_61124_(property, oldState.m_61143_(property));
    }

    public static void updateState(Level level, BlockPos pos) {
        updateState(level.getBlockState(pos), level, pos);
    }

    public static void updateState(BlockState state, Level level, BlockPos pos) {
        level.sendBlockUpdated(pos, state, state, 2);
        level.blockEntityChanged(pos);
    }

    public static void updateAndNotifyState(Level level, BlockPos pos) {
        updateAndNotifyState(level.getBlockState(pos), level, pos);
    }

    public static void updateAndNotifyState(BlockState state, Level level, BlockPos pos) {
        updateState(state, level, pos);
        state.m_60701_(level, pos, 2);
        level.updateNeighbourForOutputSignal(pos, state.m_60734_());
    }
}