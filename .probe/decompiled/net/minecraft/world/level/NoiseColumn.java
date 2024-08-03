package net.minecraft.world.level;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BlockColumn;

public final class NoiseColumn implements BlockColumn {

    private final int minY;

    private final BlockState[] column;

    public NoiseColumn(int int0, BlockState[] blockState1) {
        this.minY = int0;
        this.column = blockState1;
    }

    @Override
    public BlockState getBlock(int int0) {
        int $$1 = int0 - this.minY;
        return $$1 >= 0 && $$1 < this.column.length ? this.column[$$1] : Blocks.AIR.defaultBlockState();
    }

    @Override
    public void setBlock(int int0, BlockState blockState1) {
        int $$2 = int0 - this.minY;
        if ($$2 >= 0 && $$2 < this.column.length) {
            this.column[$$2] = blockState1;
        } else {
            throw new IllegalArgumentException("Outside of column height: " + int0);
        }
    }
}