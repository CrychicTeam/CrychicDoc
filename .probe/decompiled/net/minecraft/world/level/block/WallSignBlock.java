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
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallSignBlock extends SignBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected static final float AABB_THICKNESS = 2.0F;

    protected static final float AABB_BOTTOM = 4.5F;

    protected static final float AABB_TOP = 12.5F;

    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(0.0, 4.5, 14.0, 16.0, 12.5, 16.0), Direction.SOUTH, Block.box(0.0, 4.5, 0.0, 16.0, 12.5, 2.0), Direction.EAST, Block.box(0.0, 4.5, 0.0, 2.0, 12.5, 16.0), Direction.WEST, Block.box(14.0, 4.5, 0.0, 16.0, 12.5, 16.0)));

    public WallSignBlock(BlockBehaviour.Properties blockBehaviourProperties0, WoodType woodType1) {
        super(blockBehaviourProperties0.sound(woodType1.soundType()), woodType1);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(f_56268_, false));
    }

    @Override
    public String getDescriptionId() {
        return this.m_5456_().getDescriptionId();
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return (VoxelShape) AABBS.get(blockState0.m_61143_(FACING));
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return levelReader1.m_8055_(blockPos2.relative(((Direction) blockState0.m_61143_(FACING)).getOpposite())).m_280296_();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = this.m_49966_();
        FluidState $$2 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        LevelReader $$3 = blockPlaceContext0.m_43725_();
        BlockPos $$4 = blockPlaceContext0.getClickedPos();
        Direction[] $$5 = blockPlaceContext0.getNearestLookingDirections();
        for (Direction $$6 : $$5) {
            if ($$6.getAxis().isHorizontal()) {
                Direction $$7 = $$6.getOpposite();
                $$1 = (BlockState) $$1.m_61124_(FACING, $$7);
                if ($$1.m_60710_($$3, $$4)) {
                    return (BlockState) $$1.m_61124_(f_56268_, $$2.getType() == Fluids.WATER);
                }
            }
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1.getOpposite() == blockState0.m_61143_(FACING) && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public float getYRotationDegrees(BlockState blockState0) {
        return ((Direction) blockState0.m_61143_(FACING)).toYRot();
    }

    @Override
    public Vec3 getSignHitboxCenterPosition(BlockState blockState0) {
        VoxelShape $$1 = (VoxelShape) AABBS.get(blockState0.m_61143_(FACING));
        return $$1.bounds().getCenter();
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
        stateDefinitionBuilderBlockBlockState0.add(FACING, f_56268_);
    }
}