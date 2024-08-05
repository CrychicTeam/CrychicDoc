package com.mna.blocks.utility;

import com.mna.blocks.BlockInit;
import com.mna.blocks.IOffsetPlace;
import com.mna.blocks.WaterloggableBlock;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public abstract class WaterloggableBlockWithOffset extends WaterloggableBlock implements IOffsetPlace {

    private final BlockPos[] offsets;

    public WaterloggableBlockWithOffset(BlockBehaviour.Properties properties, boolean startWaterlogged, BlockPos... offsets) {
        super(properties, startWaterlogged);
        this.offsets = offsets;
    }

    @Override
    public void onPlace(BlockState oldState, Level world, BlockPos pos, BlockState newState, boolean p_220082_5_) {
        for (BlockPos offset : this.getOffsets(oldState)) {
            BlockPos offsetPos = pos.offset(offset);
            if (world.m_46859_(offsetPos)) {
                FillerBlock.setAtOffsetFrom(world, pos, offset);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            for (BlockPos offset : this.getOffsets(state)) {
                BlockPos offsetPos = pos.offset(offset);
                BlockState offsetState = world.getBlockState(offsetPos);
                if (offsetState.m_60734_() == BlockInit.EMPTY_FILLER_BLOCK.get()) {
                    world.removeBlock(offsetPos, false);
                }
            }
        }
        super.m_6810_(state, world, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        for (BlockPos offset : this.getOffsets(context.m_43725_().getBlockState(pos))) {
            BlockPos offsetPos = pos.offset(offset);
            if (!context.m_43725_().m_46859_(offsetPos)) {
                FluidState flState = context.m_43725_().getFluidState(offsetPos);
                if (flState == null || flState.isEmpty() || flState.getType() != Fluids.WATER && flState.getType() != Fluids.FLOWING_WATER) {
                    return null;
                }
            }
        }
        return this.m_49966_();
    }

    @Override
    protected void spawnDestroyParticles(Level world, Player player, BlockPos pos, BlockState state) {
        for (BlockPos offset : this.getOffsets(state)) {
            BlockPos offsetPos = pos.offset(offset);
            BlockState offsetState = world.getBlockState(offsetPos);
            if (offsetState.m_60734_() == BlockInit.EMPTY_FILLER_BLOCK.get()) {
                world.m_5898_(player, 2001, offsetPos, m_49956_(state));
            }
        }
        world.m_5898_(player, 2001, pos, m_49956_(state));
    }

    protected BlockPos[] getOffsets(BlockState state) {
        return this.offsets;
    }

    @Override
    public BlockPlaceContext adjustPlacement(BlockPlaceContext attempted) {
        return attempted;
    }
}