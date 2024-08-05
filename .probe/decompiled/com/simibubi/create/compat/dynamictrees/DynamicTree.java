package com.simibubi.create.compat.dynamictrees;

import com.simibubi.create.foundation.utility.AbstractBlockBreakQueue;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class DynamicTree extends AbstractBlockBreakQueue {

    public DynamicTree(BlockPos startCutPos) {
    }

    public static boolean isDynamicBranch(Block block) {
        return false;
    }

    @Override
    public void destroyBlocks(Level world, ItemStack toDamage, @Nullable Player playerEntity, BiConsumer<BlockPos, ItemStack> drop) {
    }
}