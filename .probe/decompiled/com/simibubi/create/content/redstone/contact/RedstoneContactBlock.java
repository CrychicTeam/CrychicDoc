package com.simibubi.create.content.redstone.contact;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.elevator.ElevatorColumn;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.utility.BlockHelper;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RedstoneContactBlock extends WrenchableDirectionalBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public RedstoneContactBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(POWERED, false)).m_61124_(f_52588_, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = (BlockState) this.m_49966_().m_61124_(f_52588_, context.getNearestLookingDirection().getOpposite());
        Direction placeDirection = context.m_43719_().getOpposite();
        if (context.m_43723_() != null && context.m_43723_().m_6144_() || hasValidContact(context.m_43725_(), context.getClickedPos(), placeDirection)) {
            state = (BlockState) state.m_61124_(f_52588_, placeDirection);
        }
        if (hasValidContact(context.m_43725_(), context.getClickedPos(), (Direction) state.m_61143_(f_52588_))) {
            state = (BlockState) state.m_61124_(POWERED, true);
        }
        return state;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult onWrenched = super.onWrenched(state, context);
        if (onWrenched != InteractionResult.SUCCESS) {
            return onWrenched;
        } else {
            Level level = context.getLevel();
            if (level.isClientSide()) {
                return onWrenched;
            } else {
                BlockPos pos = context.getClickedPos();
                state = level.getBlockState(pos);
                Direction facing = (Direction) state.m_61143_(f_52588_);
                if (facing.getAxis() == Direction.Axis.Y) {
                    return onWrenched;
                } else if (ElevatorColumn.get(level, new ElevatorColumn.ColumnCoords(pos.m_123341_(), pos.m_123343_(), facing)) == null) {
                    return onWrenched;
                } else {
                    level.setBlockAndUpdate(pos, BlockHelper.copyProperties(state, AllBlocks.ELEVATOR_CONTACT.getDefaultState()));
                    return onWrenched;
                }
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing != stateIn.m_61143_(f_52588_)) {
            return stateIn;
        } else {
            boolean hasValidContact = hasValidContact(worldIn, currentPos, facing);
            return stateIn.m_61143_(POWERED) != hasValidContact ? (BlockState) stateIn.m_61124_(POWERED, hasValidContact) : stateIn;
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() == this && newState.m_60734_() == this && state == newState.m_61122_(POWERED)) {
            worldIn.updateNeighborsAt(pos, this);
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        boolean hasValidContact = hasValidContact(worldIn, pos, (Direction) state.m_61143_(f_52588_));
        if ((Boolean) state.m_61143_(POWERED) != hasValidContact) {
            worldIn.m_46597_(pos, (BlockState) state.m_61124_(POWERED, hasValidContact));
        }
    }

    public static boolean hasValidContact(LevelAccessor world, BlockPos pos, Direction direction) {
        BlockState blockState = world.m_8055_(pos.relative(direction));
        return (AllBlocks.REDSTONE_CONTACT.has(blockState) || AllBlocks.ELEVATOR_CONTACT.has(blockState)) && blockState.m_61143_(f_52588_) == direction.getOpposite();
    }

    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return false;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return (Boolean) state.m_61143_(POWERED);
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return side != null && state.m_61143_(f_52588_) != side.getOpposite();
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return state.m_61143_(POWERED) && side != ((Direction) state.m_61143_(f_52588_)).getOpposite() ? 15 : 0;
    }
}