package com.simibubi.create.content.equipment;

import com.simibubi.create.foundation.utility.worldWrappers.PlacementSimulationServerWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MangrovePropaguleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class TreeFertilizerItem extends Item {

    public TreeFertilizerItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        Block block = state.m_60734_();
        if (!(block instanceof BonemealableBlock bonemealableBlock) || !state.m_204336_(BlockTags.SAPLINGS)) {
            return super.useOn(context);
        }
        if ((Boolean) state.m_61145_(MangrovePropaguleBlock.HANGING).orElse(false)) {
            return InteractionResult.PASS;
        } else if (context.getLevel().isClientSide) {
            BoneMealItem.addGrowthParticles(context.getLevel(), context.getClickedPos(), 100);
            return InteractionResult.SUCCESS;
        } else {
            BlockPos saplingPos = context.getClickedPos();
            TreeFertilizerItem.TreesDreamWorld world = new TreeFertilizerItem.TreesDreamWorld((ServerLevel) context.getLevel(), saplingPos);
            for (BlockPos pos : BlockPos.betweenClosed(-1, 0, -1, 1, 0, 1)) {
                if (context.getLevel().getBlockState(saplingPos.offset(pos)).m_60734_() == block) {
                    world.m_46597_(pos.above(10), this.withStage(state, 1));
                }
            }
            bonemealableBlock.performBonemeal(world, world.m_213780_(), BlockPos.ZERO.above(10), this.withStage(state, 1));
            for (BlockPos posx : world.blocksAdded.keySet()) {
                BlockPos actualPos = posx.offset(saplingPos).below(10);
                BlockState newState = (BlockState) world.blocksAdded.get(posx);
                if (context.getLevel().getBlockState(actualPos).m_60800_(context.getLevel(), actualPos) != -1.0F && (newState.m_60796_(world, posx) || context.getLevel().getBlockState(actualPos).m_60812_(context.getLevel(), actualPos).isEmpty())) {
                    context.getLevel().setBlockAndUpdate(actualPos, newState);
                }
            }
            if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
                context.getItemInHand().shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
    }

    private BlockState withStage(BlockState original, int stage) {
        return !original.m_61138_(BlockStateProperties.STAGE) ? original : (BlockState) original.m_61124_(BlockStateProperties.STAGE, 1);
    }

    private static class TreesDreamWorld extends PlacementSimulationServerWorld {

        private final BlockState soil;

        protected TreesDreamWorld(ServerLevel wrapped, BlockPos saplingPos) {
            super(wrapped);
            BlockState stateUnderSapling = wrapped.m_8055_(saplingPos.below());
            if (stateUnderSapling.m_204336_(BlockTags.DIRT)) {
                stateUnderSapling = Blocks.DIRT.defaultBlockState();
            }
            this.soil = stateUnderSapling;
        }

        @Override
        public BlockState getBlockState(BlockPos pos) {
            return pos.m_123342_() <= 9 ? this.soil : super.getBlockState(pos);
        }

        @Override
        public boolean setBlock(BlockPos pos, BlockState newState, int flags) {
            return newState.m_60734_() == Blocks.PODZOL ? true : super.setBlock(pos, newState, flags);
        }
    }
}