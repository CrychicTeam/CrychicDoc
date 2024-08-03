package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public interface IFrameBlock {

    @Nullable
    Block getFilledBlock(Block var1);
}