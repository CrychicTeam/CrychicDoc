package net.mehvahdjukaar.moonlight.api.item;

import com.google.common.base.Suppliers;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BlockTypeBasedBlockItem<T extends BlockType> extends FuelBlockItem {

    private final T blockType;

    public BlockTypeBasedBlockItem(Block pBlock, Item.Properties pProperties, T blockType, Supplier<Integer> burnTime) {
        super(pBlock, pProperties, burnTime);
        this.blockType = blockType;
    }

    public BlockTypeBasedBlockItem(Block pBlock, Item.Properties pProperties, T blockType) {
        this(pBlock, pProperties, blockType, Suppliers.memoize(() -> PlatHelper.getBurnTime(blockType.mainChild().asItem().getDefaultInstance())));
    }

    public T getBlockType() {
        return this.blockType;
    }
}