package net.mehvahdjukaar.supplementaries.integration.forge.dynamictrees;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class DynamicTreesCompat {

    @Nullable
    public static Block getOptionalDynamicSapling(Item item, Level world, BlockPos pos) {
        return null;
    }
}