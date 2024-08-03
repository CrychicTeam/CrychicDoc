package net.minecraft.world.level.block.state.predicate;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPredicate implements Predicate<BlockState> {

    private final Block block;

    public BlockPredicate(Block block0) {
        this.block = block0;
    }

    public static BlockPredicate forBlock(Block block0) {
        return new BlockPredicate(block0);
    }

    public boolean test(@Nullable BlockState blockState0) {
        return blockState0 != null && blockState0.m_60713_(this.block);
    }
}