package com.mna.recipes.multiblock.block_matchers;

import com.mna.api.tools.RLoc;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StairsShape;

public class StairsBlockMatcher implements IBlockMatcher {

    private final ResourceLocation id = RLoc.create("stairs");

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public boolean match(Level world, BlockPos offset, BlockPos worldPos, BlockState desired, BlockState inWorld, boolean matchBlock) {
        if (desired.m_60734_() instanceof StairBlock && inWorld.m_60734_() instanceof StairBlock) {
            if (matchBlock && desired.m_60734_() != inWorld.m_60734_()) {
                return false;
            }
            StairsShape desired_shape = (StairsShape) desired.m_61143_(StairBlock.SHAPE);
            if (desired_shape == StairsShape.STRAIGHT) {
                return this.MatchStateProperties(desired, inWorld, new String[0]);
            }
            if (this.MatchStateProperties(desired, inWorld, new String[] { StairBlock.HALF.m_61708_(), StairBlock.WATERLOGGED.m_61708_() })) {
                int[] PALETTE_SHAPE_MAP = new int[] { 12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8 };
                int desiredPalletteId = ((StairsShape) desired.m_61143_(StairBlock.SHAPE)).ordinal() * 4 + ((Direction) desired.m_61143_(StairBlock.FACING)).get2DDataValue();
                int existingPalletteId = ((StairsShape) inWorld.m_61143_(StairBlock.SHAPE)).ordinal() * 4 + ((Direction) inWorld.m_61143_(StairBlock.FACING)).get2DDataValue();
                return PALETTE_SHAPE_MAP[desiredPalletteId] == PALETTE_SHAPE_MAP[existingPalletteId];
            }
        }
        return false;
    }

    @Override
    public ArrayList<ItemStack> getValidBlocks(Block block) {
        ArrayList<ItemStack> valid = new ArrayList();
        if (block instanceof StairBlock) {
            valid.add(new ItemStack(block));
        }
        return valid;
    }
}