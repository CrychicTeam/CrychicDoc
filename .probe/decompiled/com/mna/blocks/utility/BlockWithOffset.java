package com.mna.blocks.utility;

import com.mna.blocks.BlockInit;
import com.mna.blocks.IOffsetPlace;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockWithOffset extends Block implements IOffsetPlace {

    private BlockPos[] offsets;

    public BlockWithOffset(BlockBehaviour.Properties properties, BlockPos... offsets) {
        super(properties);
        this.offsets = offsets;
    }

    @Override
    public void onPlace(BlockState oldState, Level world, BlockPos pos, BlockState newState, boolean p_220082_5_) {
        for (BlockPos offset : this.offsets) {
            BlockPos offsetPos = pos.offset(offset);
            if (world.m_46859_(offsetPos)) {
                FillerBlock.setAtOffsetFrom(world, pos, offset);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean doDrops) {
        if (state.m_60734_() != newState.m_60734_()) {
            for (BlockPos offset : this.offsets) {
                BlockPos offsetPos = pos.offset(offset);
                BlockState offsetState = world.getBlockState(offsetPos);
                if (offsetState.m_60734_() == BlockInit.EMPTY_FILLER_BLOCK.get()) {
                    world.m_46961_(offsetPos, false);
                }
            }
        }
        super.m_6810_(state, world, pos, newState, doDrops);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        for (BlockPos offset : this.offsets) {
            BlockPos offsetPos = pos.offset(offset);
            if (!context.m_43725_().m_46859_(offsetPos)) {
                return null;
            }
        }
        return this.m_49966_();
    }

    @Override
    protected void spawnDestroyParticles(Level world, Player player, BlockPos pos, BlockState state) {
        for (BlockPos offset : this.offsets) {
            BlockPos offsetPos = pos.offset(offset);
            BlockState offsetState = world.getBlockState(offsetPos);
            if (offsetState.m_60734_() == BlockInit.EMPTY_FILLER_BLOCK.get()) {
                world.m_5898_(player, 2001, offsetPos, m_49956_(state));
            }
        }
        world.m_5898_(player, 2001, pos, m_49956_(state));
    }

    @Override
    public BlockPlaceContext adjustPlacement(BlockPlaceContext attempted) {
        return attempted;
    }
}