package io.redspace.ironsspellbooks.block.alchemist_cauldron;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface AlchemistCauldronInteraction {

    @Nullable
    ItemStack interact(BlockState var1, Level var2, BlockPos var3, int var4, ItemStack var5);
}