package com.simibubi.create.content.redstone.diodes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.ticks.TickPriority;

public class PoweredLatchBlock extends ToggleLatchBlock {

    public static BooleanProperty POWERED_SIDE = BooleanProperty.create("powered_side");

    public PoweredLatchBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(POWERED_SIDE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED_SIDE));
    }

    @Override
    protected void checkTickOnNeighbor(Level worldIn, BlockPos pos, BlockState state) {
        boolean back = (Boolean) state.m_61143_(f_52496_);
        boolean shouldBack = this.m_7320_(worldIn, pos, state);
        boolean side = (Boolean) state.m_61143_(POWERED_SIDE);
        boolean shouldSide = this.isPoweredOnSides(worldIn, pos, state);
        TickPriority tickpriority = TickPriority.HIGH;
        if (this.m_52573_(worldIn, pos, state)) {
            tickpriority = TickPriority.EXTREMELY_HIGH;
        } else if (side || back) {
            tickpriority = TickPriority.VERY_HIGH;
        }
        if (!worldIn.m_183326_().willTickThisTick(pos, this)) {
            if (back != shouldBack || side != shouldSide) {
                worldIn.m_186464_(pos, this, this.m_6112_(state), tickpriority);
            }
        }
    }

    protected boolean isPoweredOnSides(Level worldIn, BlockPos pos, BlockState state) {
        Direction direction = (Direction) state.m_61143_(f_54117_);
        Direction left = direction.getClockWise();
        Direction right = direction.getCounterClockWise();
        for (Direction d : new Direction[] { left, right }) {
            BlockPos blockpos = pos.relative(d);
            int i = worldIn.m_277185_(blockpos, d);
            if (i > 0) {
                return true;
            }
            BlockState blockstate = worldIn.getBlockState(blockpos);
            if (blockstate.m_60734_() == Blocks.REDSTONE_WIRE && (Integer) blockstate.m_61143_(RedStoneWireBlock.POWER) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        boolean back = (Boolean) state.m_61143_(f_52496_);
        boolean shouldBack = this.m_7320_(worldIn, pos, state);
        boolean side = (Boolean) state.m_61143_(POWERED_SIDE);
        boolean shouldSide = this.isPoweredOnSides(worldIn, pos, state);
        BlockState stateIn = state;
        if (back != shouldBack) {
            state = (BlockState) state.m_61124_(f_52496_, shouldBack);
            if (shouldBack) {
                state = (BlockState) state.m_61124_(POWERING, true);
            } else if (side) {
                state = (BlockState) state.m_61124_(POWERING, false);
            }
        }
        if (side != shouldSide) {
            state = (BlockState) state.m_61124_(POWERED_SIDE, shouldSide);
            if (shouldSide) {
                state = (BlockState) state.m_61124_(POWERING, false);
            } else if (back) {
                state = (BlockState) state.m_61124_(POWERING, true);
            }
        }
        if (state != stateIn) {
            worldIn.m_7731_(pos, state, 2);
        }
    }

    @Override
    protected InteractionResult activated(Level worldIn, BlockPos pos, BlockState state) {
        if (state.m_61143_(f_52496_) != state.m_61143_(POWERED_SIDE)) {
            return InteractionResult.PASS;
        } else {
            if (!worldIn.isClientSide) {
                float f = !state.m_61143_(POWERING) ? 0.6F : 0.5F;
                worldIn.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
                worldIn.setBlock(pos, (BlockState) state.m_61122_(POWERING), 2);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return side == null ? false : side.getAxis().isHorizontal();
    }
}