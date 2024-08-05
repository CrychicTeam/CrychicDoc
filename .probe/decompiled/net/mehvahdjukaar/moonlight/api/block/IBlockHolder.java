package net.mehvahdjukaar.moonlight.api.block;

import net.minecraft.world.level.block.state.BlockState;

public interface IBlockHolder {

    BlockState getHeldBlock(int var1);

    boolean setHeldBlock(BlockState var1, int var2);

    default BlockState getHeldBlock() {
        return this.getHeldBlock(0);
    }

    default boolean setHeldBlock(BlockState state) {
        return this.setHeldBlock(state, 0);
    }
}