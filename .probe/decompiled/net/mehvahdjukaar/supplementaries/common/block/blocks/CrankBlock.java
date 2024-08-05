package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CrankBlock extends WaterBlock {

    protected static final VoxelShape SHAPE_DOWN = Block.box(2.0, 11.0, 2.0, 14.0, 16.0, 14.0);

    protected static final VoxelShape SHAPE_UP = Block.box(2.0, 0.0, 2.0, 14.0, 5.0, 14.0);

    protected static final VoxelShape SHAPE_NORTH = Block.box(2.0, 2.0, 11.0, 14.0, 14.0, 16.0);

    protected static final VoxelShape SHAPE_SOUTH = Block.box(2.0, 2.0, 0.0, 14.0, 14.0, 5.0);

    protected static final VoxelShape SHAPE_EAST = Block.box(0.0, 2.0, 2.0, 5.0, 14.0, 14.0);

    protected static final VoxelShape SHAPE_WEST = Block.box(11.0, 2.0, 2.0, 16.0, 14.0, 14.0);

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public CrankBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(POWER, 0)).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return facing.getOpposite() == stateIn.m_61143_(FACING) && !stateIn.m_60710_(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        Direction direction = (Direction) state.m_61143_(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        BlockState blockstate = worldIn.m_8055_(blockpos);
        return direction != Direction.UP && direction != Direction.DOWN ? blockstate.m_60783_(worldIn, blockpos, direction) : m_49863_(worldIn, blockpos, direction);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            Direction direction = ((Direction) state.m_61143_(FACING)).getOpposite();
            double d0 = (double) pos.m_123341_() + 0.5 + 0.1 * (double) direction.getStepX() + 0.2 * (double) direction.getStepX();
            double d1 = (double) pos.m_123342_() + 0.5 + 0.1 * (double) direction.getStepY() + 0.2 * (double) direction.getStepY();
            double d2 = (double) pos.m_123343_() + 0.5 + 0.1 * (double) direction.getStepZ() + 0.2 * (double) direction.getStepZ();
            worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
            return InteractionResult.SUCCESS;
        } else {
            boolean ccw = player.m_6144_();
            this.activate(state, worldIn, pos, ccw);
            float f = 0.55F + (float) ((Integer) state.m_61143_(POWER)).intValue() * 0.04F;
            worldIn.playSound(null, pos, (SoundEvent) ModSounds.CRANK.get(), SoundSource.BLOCKS, 0.5F, f);
            worldIn.m_142346_(player, GameEvent.BLOCK_ACTIVATE, pos);
            Direction dir = ((Direction) state.m_61143_(FACING)).getOpposite();
            if (dir.getAxis() != Direction.Axis.Y) {
                BlockPos behind = pos.relative(dir);
                BlockState backState = worldIn.getBlockState(behind);
                if (backState.m_60713_((Block) ModRegistry.PULLEY_BLOCK.get()) && dir.getAxis() == backState.m_61143_(PulleyBlock.f_55923_)) {
                    ((PulleyBlock) backState.m_60734_()).windPulley(backState, behind, worldIn, ccw ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90, dir);
                }
            }
            return InteractionResult.CONSUME;
        }
    }

    public void activate(BlockState state, Level world, BlockPos pos, boolean ccw) {
        state = (BlockState) state.m_61124_(POWER, (16 + (Integer) state.m_61143_(POWER) + (ccw ? -1 : 1)) % 16);
        world.setBlock(pos, state, 3);
        this.updateNeighbors(state, world, pos);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return (Integer) blockState.m_61143_(POWER);
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.m_61143_(FACING) == side ? (Integer) blockState.m_61143_(POWER) : 0;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    private void updateNeighbors(BlockState state, Level world, BlockPos pos) {
        world.updateNeighborsAt(pos, this);
        world.updateNeighborsAt(pos.relative(((Direction) state.m_61143_(FACING)).getOpposite()), this);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && state.m_60734_() != newState.m_60734_()) {
            if ((Integer) state.m_61143_(POWER) != 0) {
                this.updateNeighbors(state, worldIn, pos);
            }
            super.m_6810_(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if ((Integer) stateIn.m_61143_(POWER) > 0 && rand.nextFloat() < 0.25F) {
            Direction direction = ((Direction) stateIn.m_61143_(FACING)).getOpposite();
            double d0 = (double) pos.m_123341_() + 0.5 + 0.1 * (double) direction.getStepX() + 0.2 * (double) direction.getStepX();
            double d1 = (double) pos.m_123342_() + 0.5 + 0.1 * (double) direction.getStepY() + 0.2 * (double) direction.getStepY();
            double d2 = (double) pos.m_123343_() + 0.5 + 0.1 * (double) direction.getStepZ() + 0.2 * (double) direction.getStepZ();
            worldIn.addParticle(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 0.5F), d0, d1, d2, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch((Direction) state.m_61143_(FACING)) {
            case NORTH ->
                SHAPE_NORTH;
            case WEST ->
                SHAPE_WEST;
            case EAST ->
                SHAPE_EAST;
            case UP ->
                SHAPE_UP;
            case DOWN ->
                SHAPE_DOWN;
            default ->
                SHAPE_SOUTH;
        };
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType pathType) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWER, WATERLOGGED);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        BlockState blockstate = this.m_49966_();
        LevelReader level = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        Direction[] directions = context.getNearestLookingDirections();
        for (Direction direction : directions) {
            Direction direction1 = direction.getOpposite();
            blockstate = (BlockState) blockstate.m_61124_(FACING, direction1);
            if (blockstate.m_60710_(level, blockpos)) {
                return (BlockState) blockstate.m_61124_(WATERLOGGED, flag);
            }
        }
        return null;
    }
}