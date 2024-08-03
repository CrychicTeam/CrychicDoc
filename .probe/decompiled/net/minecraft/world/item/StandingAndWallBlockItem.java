package net.minecraft.world.item;

import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class StandingAndWallBlockItem extends BlockItem {

    protected final Block wallBlock;

    private final Direction attachmentDirection;

    public StandingAndWallBlockItem(Block block0, Block block1, Item.Properties itemProperties2, Direction direction3) {
        super(block0, itemProperties2);
        this.wallBlock = block1;
        this.attachmentDirection = direction3;
    }

    protected boolean canPlace(LevelReader levelReader0, BlockState blockState1, BlockPos blockPos2) {
        return blockState1.m_60710_(levelReader0, blockPos2);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = this.wallBlock.getStateForPlacement(blockPlaceContext0);
        BlockState $$2 = null;
        LevelReader $$3 = blockPlaceContext0.m_43725_();
        BlockPos $$4 = blockPlaceContext0.getClickedPos();
        for (Direction $$5 : blockPlaceContext0.getNearestLookingDirections()) {
            if ($$5 != this.attachmentDirection.getOpposite()) {
                BlockState $$6 = $$5 == this.attachmentDirection ? this.m_40614_().getStateForPlacement(blockPlaceContext0) : $$1;
                if ($$6 != null && this.canPlace($$3, $$6, $$4)) {
                    $$2 = $$6;
                    break;
                }
            }
        }
        return $$2 != null && $$3.m_45752_($$2, $$4, CollisionContext.empty()) ? $$2 : null;
    }

    @Override
    public void registerBlocks(Map<Block, Item> mapBlockItem0, Item item1) {
        super.registerBlocks(mapBlockItem0, item1);
        mapBlockItem0.put(this.wallBlock, item1);
    }
}