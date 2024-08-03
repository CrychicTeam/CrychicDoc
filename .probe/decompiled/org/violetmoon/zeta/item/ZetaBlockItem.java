package org.violetmoon.zeta.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.violetmoon.zeta.item.ext.IZetaItemExtensions;

public class ZetaBlockItem extends BlockItem implements IZetaItemExtensions {

    public ZetaBlockItem(Block toPlace, Item.Properties props) {
        super(toPlace, props);
    }
}