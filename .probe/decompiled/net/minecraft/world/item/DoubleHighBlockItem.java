package net.minecraft.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DoubleHighBlockItem extends BlockItem {

    public DoubleHighBlockItem(Block block0, Item.Properties itemProperties1) {
        super(block0, itemProperties1);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext blockPlaceContext0, BlockState blockState1) {
        Level $$2 = blockPlaceContext0.m_43725_();
        BlockPos $$3 = blockPlaceContext0.getClickedPos().above();
        BlockState $$4 = $$2.m_46801_($$3) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
        $$2.setBlock($$3, $$4, 27);
        return super.placeBlock(blockPlaceContext0, blockState1);
    }
}