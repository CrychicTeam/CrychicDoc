package com.simibubi.create.content.logistics.chute;

import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ChuteItem extends BlockItem {

    public ChuteItem(Block p_i48527_1_, Item.Properties p_i48527_2_) {
        super(p_i48527_1_, p_i48527_2_);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Direction face = context.m_43719_();
        BlockPos placedOnPos = context.getClickedPos().relative(face.getOpposite());
        Level world = context.m_43725_();
        BlockState placedOnState = world.getBlockState(placedOnPos);
        if (!AbstractChuteBlock.isChute(placedOnState) || context.m_7078_()) {
            return super.place(context);
        } else if (face.getAxis().isVertical()) {
            return super.place(context);
        } else {
            BlockPos correctPos = context.getClickedPos().above();
            BlockState blockState = world.getBlockState(correctPos);
            if (blockState.m_247087_()) {
                context = BlockPlaceContext.at(context, correctPos, face);
                return super.place(context);
            } else if (blockState.m_60734_() instanceof ChuteBlock && !world.isClientSide) {
                AbstractChuteBlock block = (AbstractChuteBlock) blockState.m_60734_();
                if (block.getFacing(blockState) == Direction.DOWN) {
                    world.setBlockAndUpdate(correctPos, ProperWaterloggedBlock.withWater(world, block.updateChuteState((BlockState) blockState.m_61124_(ChuteBlock.FACING, face), world.getBlockState(correctPos.above()), world, correctPos), correctPos));
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.FAIL;
                }
            } else {
                return InteractionResult.FAIL;
            }
        }
    }
}