package com.mna.recipes.multiblock.block_matchers;

import com.mna.api.tools.RLoc;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ExactBlockMatcher implements IBlockMatcher {

    private final ResourceLocation id = RLoc.create("exact");

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public boolean match(Level world, BlockPos offset, BlockPos worldPos, BlockState desired, BlockState inWorld, boolean matchBlock) {
        return matchBlock ? desired.equals(inWorld) : this.MatchStateProperties(desired, inWorld, new String[0]);
    }

    @Override
    public ArrayList<ItemStack> getValidBlocks(Block block) {
        ArrayList<ItemStack> valid = new ArrayList();
        valid.add(new ItemStack(block));
        return valid;
    }
}