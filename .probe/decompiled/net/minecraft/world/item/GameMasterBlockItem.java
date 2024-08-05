package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class GameMasterBlockItem extends BlockItem {

    public GameMasterBlockItem(Block block0, Item.Properties itemProperties1) {
        super(block0, itemProperties1);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext blockPlaceContext0) {
        Player $$1 = blockPlaceContext0.m_43723_();
        return $$1 != null && !$$1.canUseGameMasterBlocks() ? null : super.getPlacementState(blockPlaceContext0);
    }
}