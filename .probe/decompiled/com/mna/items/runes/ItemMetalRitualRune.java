package com.mna.items.runes;

import com.mna.blocks.BlockInit;
import com.mna.blocks.ritual.ChalkRuneBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ItemMetalRitualRune extends ItemRune {

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().getBlockState(context.getClickedPos()).m_60783_(context.getLevel(), context.getClickedPos(), Direction.UP) || !context.getLevel().m_46859_(context.getClickedPos().above()) && !context.getLevel().m_46801_(context.getClickedPos().above())) {
            return InteractionResult.FAIL;
        } else {
            context.getLevel().setBlockAndUpdate(context.getClickedPos().above(), (BlockState) ((BlockState) ((BlockState) ((BlockState) BlockInit.CHALK_RUNE.get().defaultBlockState().m_61124_(ChalkRuneBlock.RUNEINDEX, (int) Math.floor(Math.random() * (double) (ChalkRuneBlock.RUNEINDEX.getPossibleValues().size() - 1)))).m_61124_(ChalkRuneBlock.METAL, true)).m_61124_(ChalkRuneBlock.ACTIVATED, false)).m_61124_(BlockStateProperties.WATERLOGGED, context.getLevel().m_46801_(context.getClickedPos().above())));
            context.getItemInHand().shrink(1);
            return InteractionResult.SUCCESS;
        }
    }
}