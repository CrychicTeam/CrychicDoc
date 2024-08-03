package com.mna.api.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TieredBlockItem extends BlockItem implements ITieredItem<TieredBlockItem> {

    private int _tier = -1;

    public TieredBlockItem(Block p_i48527_1_, Item.Properties p_i48527_2_) {
        super(p_i48527_1_, p_i48527_2_);
    }

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    @Override
    public int getCachedTier() {
        return this._tier;
    }
}