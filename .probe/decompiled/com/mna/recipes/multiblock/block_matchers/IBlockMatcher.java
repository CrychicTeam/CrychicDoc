package com.mna.recipes.multiblock.block_matchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableBoolean;

public interface IBlockMatcher {

    boolean match(Level var1, BlockPos var2, BlockPos var3, BlockState var4, BlockState var5, boolean var6);

    ResourceLocation getId();

    default boolean MatchStateProperties(BlockState desired, BlockState inWorld, String... propertyNames) {
        MutableBoolean matched = new MutableBoolean(true);
        List<String> propNames = Arrays.asList(propertyNames);
        desired.m_61147_().forEach(p -> {
            if (propNames.size() == 0 || propNames.contains(p.getName())) {
                if (inWorld.m_61138_(p)) {
                    if (inWorld.m_61143_(p) != desired.m_61143_(p)) {
                        matched.setFalse();
                    }
                } else {
                    matched.setFalse();
                }
            }
        });
        return matched.booleanValue();
    }

    ArrayList<ItemStack> getValidBlocks(Block var1);

    default ArrayList<ItemStack> getValidItems(Block block) {
        return this.getValidBlocks(block);
    }
}