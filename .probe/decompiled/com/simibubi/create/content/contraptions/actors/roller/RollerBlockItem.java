package com.simibubi.create.content.contraptions.actors.roller;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RollerBlockItem extends BlockItem {

    public RollerBlockItem(Block pBlock, Item.Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        BlockPos clickedPos = ctx.getClickedPos();
        Level level = ctx.m_43725_();
        BlockState blockStateBelow = level.getBlockState(clickedPos.below());
        if (!Block.isFaceFull(blockStateBelow.m_60812_(level, clickedPos.below()), Direction.UP)) {
            return super.place(ctx);
        } else {
            Direction clickedFace = ctx.m_43719_();
            return super.place(BlockPlaceContext.at(ctx, clickedPos.relative(Direction.UP), clickedFace));
        }
    }
}