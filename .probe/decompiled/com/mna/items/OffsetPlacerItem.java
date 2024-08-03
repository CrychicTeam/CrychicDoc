package com.mna.items;

import com.mna.api.items.TieredBlockItem;
import java.util.function.Function;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class OffsetPlacerItem extends TieredBlockItem {

    Function<BlockPlaceContext, BlockPlaceContext> adjuster;

    public OffsetPlacerItem(Block forBlock, Item.Properties properties, Function<BlockPlaceContext, BlockPlaceContext> adjuster) {
        super(forBlock, properties);
        this.adjuster = adjuster;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        return this.adjuster != null ? super.m_40576_((BlockPlaceContext) this.adjuster.apply(context)) : super.m_40576_(context);
    }
}