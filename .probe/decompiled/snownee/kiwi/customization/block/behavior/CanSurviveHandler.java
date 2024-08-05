package snownee.kiwi.customization.block.behavior;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public interface CanSurviveHandler {

    boolean isSensitiveSide(BlockState var1, Direction var2);

    boolean canSurvive(BlockState var1, LevelReader var2, BlockPos var3);

    static CanSurviveHandler checkFloor() {
        return CanSurviveHandler.Impls.CHECK_FLOOR;
    }

    static CanSurviveHandler checkCeiling() {
        return CanSurviveHandler.Impls.CHECK_CEILING;
    }

    static CanSurviveHandler checkFace(DirectionProperty property) {
        return (CanSurviveHandler) CanSurviveHandler.Impls.CHECK_FACE.computeIfAbsent(property, key -> new CanSurviveHandler() {

            @Override
            public boolean isSensitiveSide(BlockState state, Direction side) {
                return side == ((Direction) state.m_61143_(key)).getOpposite();
            }

            @Override
            public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
                Direction direction = (Direction) state.m_61143_(key);
                BlockPos neighbor = pos.relative(direction);
                return world.m_8055_(neighbor).m_60659_(world, neighbor, direction.getOpposite(), SupportType.RIGID);
            }
        });
    }

    static CanSurviveHandler.Compound any(List<CanSurviveHandler> handlers) {
        return new CanSurviveHandler.Compound(true, handlers);
    }

    static CanSurviveHandler.Compound all(List<CanSurviveHandler> handlers) {
        return new CanSurviveHandler.Compound(false, handlers);
    }

    public static record Compound(boolean any, List<CanSurviveHandler> handlers) implements CanSurviveHandler {

        @Override
        public boolean isSensitiveSide(BlockState state, Direction side) {
            for (CanSurviveHandler handler : this.handlers) {
                if (handler.isSensitiveSide(state, side)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
            for (CanSurviveHandler handler : this.handlers) {
                if (handler.canSurvive(state, world, pos) == this.any) {
                    return this.any;
                }
            }
            return !this.any;
        }
    }

    public static final class Impls {

        private static final CanSurviveHandler CHECK_FLOOR = new CanSurviveHandler() {

            @Override
            public boolean isSensitiveSide(BlockState state, Direction side) {
                return side == Direction.DOWN;
            }

            @Override
            public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
                return Block.canSupportRigidBlock(world, pos.below());
            }
        };

        private static final CanSurviveHandler CHECK_CEILING = new CanSurviveHandler() {

            @Override
            public boolean isSensitiveSide(BlockState state, Direction side) {
                return side == Direction.UP;
            }

            @Override
            public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
                pos = pos.above();
                return world.m_8055_(pos).m_60659_(world, pos, Direction.DOWN, SupportType.RIGID);
            }
        };

        private static final Map<DirectionProperty, CanSurviveHandler> CHECK_FACE = Maps.newHashMap();

        private Impls() {
        }
    }
}