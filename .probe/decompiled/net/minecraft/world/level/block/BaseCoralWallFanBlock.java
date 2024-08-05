package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BaseCoralWallFanBlock extends BaseCoralFanBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(0.0, 4.0, 5.0, 16.0, 12.0, 16.0), Direction.SOUTH, Block.box(0.0, 4.0, 0.0, 16.0, 12.0, 11.0), Direction.WEST, Block.box(5.0, 4.0, 0.0, 16.0, 12.0, 16.0), Direction.EAST, Block.box(0.0, 4.0, 0.0, 11.0, 12.0, 16.0)));

    protected BaseCoralWallFanBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(f_49158_, true));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return (VoxelShape) SHAPES.get(blockState0.m_61143_(FACING));
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING, f_49158_);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(f_49158_)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return direction1.getOpposite() == blockState0.m_61143_(FACING) && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : blockState0;
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        Direction $$3 = (Direction) blockState0.m_61143_(FACING);
        BlockPos $$4 = blockPos2.relative($$3.getOpposite());
        BlockState $$5 = levelReader1.m_8055_($$4);
        return $$5.m_60783_(levelReader1, $$4, $$3);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = super.m_5573_(blockPlaceContext0);
        LevelReader $$2 = blockPlaceContext0.m_43725_();
        BlockPos $$3 = blockPlaceContext0.getClickedPos();
        Direction[] $$4 = blockPlaceContext0.getNearestLookingDirections();
        for (Direction $$5 : $$4) {
            if ($$5.getAxis().isHorizontal()) {
                $$1 = (BlockState) $$1.m_61124_(FACING, $$5.getOpposite());
                if ($$1.m_60710_($$2, $$3)) {
                    return $$1;
                }
            }
        }
        return null;
    }
}