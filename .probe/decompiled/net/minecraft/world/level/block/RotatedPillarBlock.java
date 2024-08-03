package net.minecraft.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class RotatedPillarBlock extends Block {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public RotatedPillarBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(AXIS, Direction.Axis.Y));
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return rotatePillar(blockState0, rotation1);
    }

    public static BlockState rotatePillar(BlockState blockState0, Rotation rotation1) {
        switch(rotation1) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch((Direction.Axis) blockState0.m_61143_(AXIS)) {
                    case X:
                        return (BlockState) blockState0.m_61124_(AXIS, Direction.Axis.Z);
                    case Z:
                        return (BlockState) blockState0.m_61124_(AXIS, Direction.Axis.X);
                    default:
                        return blockState0;
                }
            default:
                return blockState0;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(AXIS, blockPlaceContext0.m_43719_().getAxis());
    }
}