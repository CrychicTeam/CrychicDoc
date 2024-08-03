package com.rekindled.embers.item;

import com.rekindled.embers.block.MechEdgeBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CaminiteRingBlockItem extends BlockItem {

    public CaminiteRingBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult interactionresult = null;
        if (context.getClickedFace() == Direction.UP) {
            BlockState clickedState = context.getLevel().getBlockState(context.getClickedPos());
            if (clickedState.m_61138_(MechEdgeBlockBase.EDGE)) {
                BlockPos pos = context.getClickedPos().offset(((MechEdgeBlockBase.MechEdge) clickedState.m_61143_(MechEdgeBlockBase.EDGE)).centerPos);
                interactionresult = this.m_40576_(new BlockPlaceContext(context.getLevel(), context.getPlayer(), context.getHand(), context.getItemInHand(), context.getHitResult().withPosition(pos)));
            }
        }
        if (interactionresult == null) {
            interactionresult = this.m_40576_(new BlockPlaceContext(context));
        }
        if (!interactionresult.consumesAction() && this.m_41472_()) {
            InteractionResult interactionresult1 = this.m_7203_(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
            return interactionresult1 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionresult1;
        } else {
            return interactionresult;
        }
    }
}