package org.violetmoon.zeta.block.ext;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockExtensionFactory {

    BlockExtensionFactory DEFAULT = block -> IZetaBlockExtensions.DEFAULT;

    IZetaBlockExtensions getInternal(Block var1);

    default IZetaBlockExtensions get(Block block) {
        return block instanceof IZetaBlockExtensions ? (IZetaBlockExtensions) block : this.getInternal(block);
    }

    default IZetaBlockExtensions get(BlockState state) {
        return this.get(state.m_60734_());
    }
}