package com.mna.blocks.tileentities.wizard_lab;

import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BookStandTile extends TileEntityWithInventory {

    public static final int INVENTORY_SIZE = 1;

    public BookStandTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.BOOK_STAND.get(), pos, state, 1);
    }
}