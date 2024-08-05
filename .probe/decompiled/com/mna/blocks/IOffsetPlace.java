package com.mna.blocks;

import net.minecraft.world.item.context.BlockPlaceContext;

public interface IOffsetPlace {

    BlockPlaceContext adjustPlacement(BlockPlaceContext var1);
}