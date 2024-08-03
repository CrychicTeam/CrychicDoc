package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MusselBlock extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final IntegerProperty MUSSELS = IntegerProperty.create("mussels", 1, 5);

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape SHAPE_UP = Block.box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0);

    private static final VoxelShape SHAPE_DOWN = Block.box(1.0, 12.0, 1.0, 15.0, 16.0, 15.0);

    private static final VoxelShape SHAPE_WEST = Block.box(12.0, 1.0, 1.0, 16.0, 15.0, 15.0);

    private static final VoxelShape SHAPE_EAST = Block.box(0.0, 1.0, 1.0, 4.0, 15.0, 15.0);

    private static final VoxelShape SHAPE_NORTH = Block.box(1.0, 1.0, 12.0, 15.0, 15.0, 16.0);

    private static final VoxelShape SHAPE_SOUTH = Block.box(1.0, 1.0, 0.0, 15.0, 15.0, 4.0);

    public MusselBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(1.0F, 1.0F).sound(SoundType.BASALT).noOcclusion().noCollission().dynamicShape().randomTicks());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.UP)).m_61124_(MUSSELS, 1)).m_61124_(WATERLOGGED, true));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = (Direction) state.m_61143_(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        return level.m_8055_(blockpos).m_60783_(level, blockpos, direction);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        return direction == ((Direction) state.m_61143_(FACING)).getOpposite() && !state.m_60710_(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch((Direction) state.m_61143_(FACING)) {
            case UP:
                return SHAPE_UP;
            case DOWN:
                return SHAPE_DOWN;
            case EAST:
                return SHAPE_EAST;
            case WEST:
                return SHAPE_WEST;
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            default:
                return SHAPE_UP;
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        Direction direction = (Direction) state.m_61143_(FACING);
        BlockPos connectedToPos = blockPos.relative(direction.getOpposite());
        BlockState connectedState = serverLevel.m_8055_(connectedToPos);
        int mussels = (Integer) state.m_61143_(MUSSELS);
        if (randomSource.nextInt(20) == 0 && connectedState.m_204336_(ACTagRegistry.GROWS_MUSSELS)) {
            if (mussels >= 5) {
                BlockPos randomOffsetPos = connectedToPos.offset(randomSource.nextInt(6) - 3, randomSource.nextInt(6) - 3, randomSource.nextInt(6) - 3);
                BlockState randomOffsetState = serverLevel.m_8055_(randomOffsetPos);
                if (randomOffsetState.m_60713_(Blocks.WATER) || randomOffsetState.m_60713_(this) && (Boolean) randomOffsetState.m_61143_(WATERLOGGED)) {
                    List<Direction> possiblities = new ArrayList();
                    for (Direction possible : Direction.values()) {
                        BlockPos check = randomOffsetPos.relative(possible);
                        if (serverLevel.m_8055_(check).m_60783_(serverLevel, check, possible.getOpposite())) {
                            possiblities.add(possible.getOpposite());
                        }
                    }
                    Direction chosen = null;
                    if (!possiblities.isEmpty()) {
                        if (possiblities.size() <= 1) {
                            chosen = (Direction) possiblities.get(0);
                        } else {
                            chosen = (Direction) possiblities.get(randomSource.nextInt(possiblities.size() - 1));
                        }
                    }
                    if (chosen != null) {
                        int taxicab = Mth.clamp(6 - (int) Math.ceil(randomOffsetPos.m_203202_((double) blockPos.m_123341_(), (double) blockPos.m_123342_(), (double) blockPos.m_123343_())), 1, 5);
                        int currentMussels = randomOffsetState.m_60713_(this) ? (Integer) randomOffsetState.m_61143_(MUSSELS) : 0;
                        int setMussels = Math.max(currentMussels, taxicab);
                        int musselCountOf = randomOffsetState.m_60713_(this) ? Math.min((Integer) randomOffsetState.m_61143_(MUSSELS) + 1, setMussels) : 1;
                        Direction musselDirectionOf = randomOffsetState.m_60713_(this) ? (Direction) randomOffsetState.m_61143_(FACING) : chosen;
                        serverLevel.m_46597_(randomOffsetPos, (BlockState) ((BlockState) ((BlockState) ACBlockRegistry.MUSSEL.get().defaultBlockState().m_61124_(FACING, musselDirectionOf)).m_61124_(WATERLOGGED, true)).m_61124_(MUSSELS, musselCountOf));
                    }
                }
            } else {
                serverLevel.m_46597_(blockPos, (BlockState) state.m_61124_(MUSSELS, mussels + 1));
            }
        }
    }

    protected void removeOneMussel(Level worldIn, BlockPos pos, BlockState state) {
        int i = (Integer) state.m_61143_(MUSSELS);
        if (i <= 1) {
            worldIn.m_46961_(pos, false);
        } else {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(MUSSELS, i - 1), 2);
            worldIn.m_220407_(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
            worldIn.m_46796_(2001, pos, Block.getId(state));
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos());
        if (blockstate.m_60713_(this)) {
            return (BlockState) blockstate.m_61124_(MUSSELS, Math.min(5, (Integer) blockstate.m_61143_(MUSSELS) + 1));
        } else {
            LevelAccessor levelaccessor = context.m_43725_();
            BlockPos blockpos = context.getClickedPos();
            return (BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, levelaccessor.m_6425_(blockpos).getType() == Fluids.WATER)).m_61124_(FACING, context.m_43719_());
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return !context.m_7078_() && context.m_43722_().is(this.m_5456_()) && state.m_61143_(MUSSELS) < 5 ? true : super.m_6864_(state, context);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(FACING, rotation.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(WATERLOGGED, FACING, MUSSELS);
    }

    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.playerDestroy(worldIn, player, pos, state, te, stack);
        this.removeOneMussel(worldIn, pos, state);
    }
}