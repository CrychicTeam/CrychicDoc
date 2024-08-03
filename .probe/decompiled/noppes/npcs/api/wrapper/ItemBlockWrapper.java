package noppes.npcs.api.wrapper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.api.item.IItemBlock;

public class ItemBlockWrapper extends ItemStackWrapper implements IItemBlock {

    protected String blockName;

    protected ItemBlockWrapper(ItemStack item) {
        super(item);
        Block b = Block.byItem(item.getItem());
        this.blockName = ForgeRegistries.BLOCKS.getKey(b) + "";
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public String getBlockName() {
        return this.blockName;
    }
}