package vectorwing.farmersdelight.common.item;

import javax.annotation.Nullable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;

public class MushroomColonyItem extends BlockItem {

    public MushroomColonyItem(Block blockIn, Item.Properties properties) {
        super(blockIn, properties);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState originalState = this.m_40614_().getStateForPlacement(context);
        if (originalState != null) {
            BlockState matureState = (BlockState) originalState.m_61124_(MushroomColonyBlock.COLONY_AGE, 3);
            return this.m_40610_(context, matureState) ? matureState : null;
        } else {
            return null;
        }
    }
}