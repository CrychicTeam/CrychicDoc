package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class FaceAttachedHorizontalDirectionalBlock extends HorizontalDirectionalBlock {

    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;

    protected FaceAttachedHorizontalDirectionalBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return canAttach(levelReader1, blockPos2, getConnectedDirection(blockState0).getOpposite());
    }

    public static boolean canAttach(LevelReader levelReader0, BlockPos blockPos1, Direction direction2) {
        BlockPos $$3 = blockPos1.relative(direction2);
        return levelReader0.m_8055_($$3).m_60783_(levelReader0, $$3, direction2.getOpposite());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        for (Direction $$1 : blockPlaceContext0.getNearestLookingDirections()) {
            BlockState $$2;
            if ($$1.getAxis() == Direction.Axis.Y) {
                $$2 = (BlockState) ((BlockState) this.m_49966_().m_61124_(FACE, $$1 == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)).m_61124_(f_54117_, blockPlaceContext0.m_8125_());
            } else {
                $$2 = (BlockState) ((BlockState) this.m_49966_().m_61124_(FACE, AttachFace.WALL)).m_61124_(f_54117_, $$1.getOpposite());
            }
            if ($$2.m_60710_(blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos())) {
                return $$2;
            }
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return getConnectedDirection(blockState0).getOpposite() == direction1 && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    protected static Direction getConnectedDirection(BlockState blockState0) {
        switch((AttachFace) blockState0.m_61143_(FACE)) {
            case CEILING:
                return Direction.DOWN;
            case FLOOR:
                return Direction.UP;
            default:
                return (Direction) blockState0.m_61143_(f_54117_);
        }
    }
}