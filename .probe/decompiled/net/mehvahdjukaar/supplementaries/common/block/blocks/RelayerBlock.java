package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class RelayerBlock extends DirectionalBlock {

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public RelayerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52588_, Direction.NORTH)).m_61124_(POWER, 0)).m_61124_(POWERED, false));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(f_52588_, rotation.rotate((Direction) state.m_61143_(f_52588_)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(f_52588_)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER, POWERED, f_52588_);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction dir = context.getNearestLookingDirection();
        BlockState state = (BlockState) this.m_49966_().m_61124_(f_52588_, dir);
        int p = this.getSignalInFront(context.m_43725_(), context.getClickedPos(), dir);
        return (BlockState) ((BlockState) state.m_61124_(POWER, p)).m_61124_(POWERED, p != 0);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (!worldIn.isClientSide) {
            this.updatePowerNextTick(state, worldIn, pos);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.m_61143_(f_52588_) == direction && level instanceof Level l) {
            this.updatePowerNextTick(state, l, currentPos);
        }
        return super.m_7417_(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide() && isMoving) {
            this.updatePowerNextTick(state, level, pos);
        }
    }

    private void updatePowerNextTick(BlockState state, Level level, BlockPos pos) {
        if (!level.m_183326_().m_183582_(pos, this)) {
            level.m_186460_(pos, this, 1);
        }
    }

    private int getSignalInFront(Level level, BlockPos pos, Direction dir) {
        BlockPos behind = pos.relative(dir);
        int pow = level.m_277185_(behind, dir);
        BlockState b = level.getBlockState(behind);
        if (b.m_60734_() instanceof RedStoneWireBlock) {
            pow = Math.max((Integer) b.m_61143_(RedStoneWireBlock.POWER), pow);
        } else if (b.m_60734_() instanceof DiodeBlock repeaterBlock) {
            pow = Math.max(repeaterBlock.getSignal(b, level, behind, (Direction) b.m_61143_(DiodeBlock.f_54117_)), pow);
        } else if (b.m_60713_(this)) {
            pow = Math.max((Integer) b.m_61143_(POWER), pow);
        }
        return pow;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        Direction front = (Direction) state.m_61143_(f_52588_);
        Direction back = front.getOpposite();
        int pow = this.getSignalInFront(level, pos, front);
        level.m_7731_(pos, (BlockState) ((BlockState) state.m_61124_(POWERED, pow != 0)).m_61124_(POWER, Mth.clamp(pow, 0, 15)), 7);
        BlockPos blockPos = pos.relative(back);
        level.neighborChanged(blockPos, this, pos);
        level.updateNeighborsAtExceptFromFacing(blockPos, this, front);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.m_60746_(level, pos, direction);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.m_61143_(POWERED) && state.m_61143_(f_52588_) == direction ? (Integer) state.m_61143_(POWER) : 0;
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return direction == null ? false : direction == state.m_61143_(ObserverBlock.f_52588_);
    }
}