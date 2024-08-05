package net.mehvahdjukaar.supplementaries.common.block.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SconceWallBlock extends SconceBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, m_49796_(6.0, 2.0, 10.0, 10.0, 13.0, 16.0), Direction.SOUTH, m_49796_(6.0, 2.0, 0.0, 10.0, 13.0, 6.0), Direction.WEST, m_49796_(10.0, 2.0, 6.0, 16.0, 13.0, 10.0), Direction.EAST, m_49796_(0.0, 2.0, 6.0, 6.0, 13.0, 10.0)));

    public <T extends ParticleType<?>> SconceWallBlock(BlockBehaviour.Properties properties, Supplier<T> particleData) {
        super(properties, particleData);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false)).m_61124_(LIT, true));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return facing.getOpposite() == stateIn.m_61143_(FACING) && !stateIn.m_60710_(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if ((Boolean) stateIn.m_61143_(LIT)) {
            Direction direction = (Direction) stateIn.m_61143_(FACING);
            double d0 = (double) pos.m_123341_() + 0.5;
            double d1 = (double) pos.m_123342_() + 0.7;
            double d2 = (double) pos.m_123343_() + 0.5;
            Direction direction1 = direction.getOpposite();
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + 0.25 * (double) direction1.getStepX(), d1 + 0.15, d2 + 0.25 * (double) direction1.getStepZ(), 0.0, 0.0, 0.0);
            worldIn.addParticle((ParticleOptions) this.particleData.get(), d0 + 0.25 * (double) direction1.getStepX(), d1 + 0.15, d2 + 0.25 * (double) direction1.getStepZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        Direction direction = (Direction) state.m_61143_(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        BlockState blockstate = worldIn.m_8055_(blockpos);
        return blockstate.m_60783_(worldIn, blockpos, direction);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (VoxelShape) SHAPES.get(state.m_61143_(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        BlockState blockstate = this.m_49966_();
        LevelReader level = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        Direction[] nearestLookingDirections = context.getNearestLookingDirections();
        for (Direction direction : nearestLookingDirections) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction1 = direction.getOpposite();
                blockstate = (BlockState) blockstate.m_61124_(FACING, direction1);
                if (blockstate.m_60710_(level, blockpos)) {
                    return (BlockState) ((BlockState) blockstate.m_61124_(WATERLOGGED, flag)).m_61124_(LIT, !flag);
                }
            }
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING, WATERLOGGED);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }
}