package team.lodestar.lodestone.systems.multiblock;

import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MultiBlockItem extends BlockItem {

    public final Supplier<? extends MultiBlockStructure> structure;

    public MultiBlockItem(Block block, Item.Properties properties, Supplier<? extends MultiBlockStructure> structure) {
        super(block, properties);
        this.structure = structure;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        return !((MultiBlockStructure) this.structure.get()).canPlace(context) ? false : super.canPlace(context, state);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        ((MultiBlockStructure) this.structure.get()).place(context);
        return super.placeBlock(context, state);
    }
}