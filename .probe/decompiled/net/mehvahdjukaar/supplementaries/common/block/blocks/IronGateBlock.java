package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class IronGateBlock extends FenceGateBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final boolean gold;

    public IronGateBlock(BlockBehaviour.Properties properties, boolean gold) {
        super(properties, WoodType.OAK);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, Boolean.FALSE));
        this.gold = gold;
    }

    @Override
    public SoundType getSoundType(BlockState state) {
        return SoundType.METAL;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        if ((Boolean) CommonConfigs.Building.DOUBLE_IRON_GATE.get() && facing.getAxis().isVertical() && facingState.m_60713_(this) && !(Boolean) stateIn.m_61143_(f_53342_)) {
            boolean open = (Boolean) facingState.m_61143_(f_53341_);
            if (open != (Boolean) stateIn.m_61143_(f_53341_) && stateIn.m_61143_(f_54117_) == facingState.m_61143_(f_54117_)) {
                stateIn = (BlockState) stateIn.m_61124_(f_53341_, open);
            }
        }
        return stateIn;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_54117_, f_53341_, f_53342_, f_53343_, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        boolean flag = world.m_276867_(blockpos);
        Direction direction = context.m_8125_();
        FluidState fluidstate = context.m_43725_().getFluidState(context.getClickedPos());
        BlockState state = (BlockState) this.m_49966_().m_61124_(WATERLOGGED, fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8);
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) state.m_61124_(f_54117_, direction)).m_61124_(f_53341_, flag)).m_61124_(f_53342_, flag)).m_61124_(f_53343_, this.canConnect(world, blockpos, direction));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext collisionContext) {
        return ((Direction) state.m_61143_(f_54117_)).getAxis() == Direction.Axis.X ? f_53345_ : f_53344_;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
        if (!world.isClientSide) {
            boolean flag = world.m_276867_(pos);
            if ((Boolean) state.m_61143_(f_53342_) != flag) {
                state = (BlockState) state.m_61124_(f_53342_, flag);
                if ((!this.gold || !(Boolean) CommonConfigs.Building.CONSISTENT_GATE.get()) && (Boolean) state.m_61143_(f_53341_) != flag) {
                    state = (BlockState) state.m_61124_(f_53341_, flag);
                    soundAndEvent(state, world, pos, null);
                }
            }
            boolean connect = this.canConnect(world, pos, (Direction) state.m_61143_(f_54117_));
            world.setBlock(pos, (BlockState) state.m_61124_(f_53343_, connect), 2);
        }
    }

    private boolean canConnect(LevelAccessor world, BlockPos pos, Direction dir) {
        return this.canConnectUp(world.m_8055_(pos.above()), world, pos.above()) || this.canConnectSide(world.m_8055_(pos.relative(dir.getClockWise()))) || this.canConnectSide(world.m_8055_(pos.relative(dir.getCounterClockWise())));
    }

    private boolean canConnectSide(BlockState state) {
        return state.m_60734_() instanceof IronBarsBlock;
    }

    private boolean canConnectUp(BlockState state, LevelAccessor world, BlockPos pos) {
        return state.m_60783_(world, pos, Direction.DOWN) || state.m_60713_(this) || state.m_60734_() instanceof IronBarsBlock;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (((Boolean) state.m_61143_(f_53342_) || !this.gold) && (Boolean) CommonConfigs.Building.CONSISTENT_GATE.get()) {
            return InteractionResult.PASS;
        } else {
            Direction dir = player.m_6350_();
            if ((Boolean) CommonConfigs.Building.DOUBLE_IRON_GATE.get()) {
                BlockPos up = pos.above();
                BlockState stateUp = level.getBlockState(up);
                if (stateUp.m_60713_(this) && stateUp.m_61124_(f_53343_, false) == state.m_61124_(f_53343_, false)) {
                    this.openGate(stateUp, level, up, dir);
                }
                BlockPos down = pos.below();
                BlockState stateDown = level.getBlockState(down);
                if (stateDown.m_60713_(this) && stateDown.m_61124_(f_53343_, false) == state.m_61124_(f_53343_, false)) {
                    this.openGate(stateDown, level, down, dir);
                }
            }
            this.openGate(state, level, pos, dir);
            soundAndEvent(state, level, pos, player);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    private static void soundAndEvent(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
        boolean open = (Boolean) state.m_61143_(f_53341_);
        level.playSound(player, pos, open ? BlockSetType.IRON.trapdoorOpen() : BlockSetType.IRON.trapdoorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
        level.m_142346_(player, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
    }

    private void openGate(BlockState state, Level world, BlockPos pos, Direction dir) {
        if ((Boolean) state.m_61143_(f_53341_)) {
            state = (BlockState) state.m_61124_(f_53341_, Boolean.FALSE);
        } else {
            if (state.m_61143_(f_54117_) == dir.getOpposite()) {
                state = (BlockState) state.m_61124_(f_54117_, dir);
            }
            state = (BlockState) state.m_61124_(f_53341_, Boolean.TRUE);
        }
        world.setBlock(pos, state, 10);
    }

    public static BlockState messWithIronBarsState(LevelAccessor level, BlockPos clickedPos, BlockState returnValue) {
        boolean altered = false;
        for (Direction d : Direction.Plane.HORIZONTAL) {
            BooleanProperty prop = (BooleanProperty) CrossCollisionBlock.PROPERTY_BY_DIRECTION.get(d);
            if (!(Boolean) returnValue.m_61143_(prop)) {
                BlockState blockState = level.m_8055_(clickedPos.relative(d));
                if (blockState.m_60734_() instanceof FenceGateBlock && ((Direction) blockState.m_61143_(FenceGateBlock.f_54117_)).getAxis() != d.getAxis()) {
                    altered = true;
                    returnValue = (BlockState) returnValue.m_61124_(prop, true);
                }
            }
        }
        return altered ? returnValue : null;
    }
}