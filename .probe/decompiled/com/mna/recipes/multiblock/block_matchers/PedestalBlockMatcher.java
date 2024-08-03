package com.mna.recipes.multiblock.block_matchers;

import com.mna.api.tools.RLoc;
import com.mna.blocks.BlockInit;
import com.mna.blocks.runeforging.PedestalBlock;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PedestalBlockMatcher implements IBlockMatcher {

    private final ResourceLocation id = RLoc.create("pedestals");

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public boolean match(Level world, BlockPos offset, BlockPos worldPos, BlockState desired, BlockState inWorld, boolean matchBlock) {
        return desired.m_60734_() instanceof PedestalBlock && inWorld.m_60734_() instanceof PedestalBlock;
    }

    @Override
    public ArrayList<ItemStack> getValidBlocks(Block block) {
        ArrayList<ItemStack> valid = new ArrayList();
        if (block instanceof PedestalBlock) {
            valid.add(new ItemStack(BlockInit.PEDESTAL.get()));
            valid.add(new ItemStack(BlockInit.PEDESTAL_WITH_SIGN.get()));
        }
        return valid;
    }
}