package net.minecraft.world.item;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BedItem extends BlockItem {

    public BedItem(Block block0, Item.Properties itemProperties1) {
        super(block0, itemProperties1);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext blockPlaceContext0, BlockState blockState1) {
        return blockPlaceContext0.m_43725_().setBlock(blockPlaceContext0.getClickedPos(), blockState1, 26);
    }
}