package net.minecraft.world.item;

import net.minecraft.world.level.block.Block;

public class ItemNameBlockItem extends BlockItem {

    public ItemNameBlockItem(Block block0, Item.Properties itemProperties1) {
        super(block0, itemProperties1);
    }

    @Override
    public String getDescriptionId() {
        return this.m_41467_();
    }
}