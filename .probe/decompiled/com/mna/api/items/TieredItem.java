package com.mna.api.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;

public class TieredItem extends Item implements ITieredItem<TieredItem> {

    private int _tier = -1;

    private boolean sneakBypass = false;

    public TieredItem(Item.Properties itemProperties) {
        super(itemProperties);
    }

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    public TieredItem setSneakBypass() {
        this.sneakBypass = true;
        return this;
    }

    @Override
    public int getCachedTier() {
        return this._tier;
    }

    public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return this.sneakBypass;
    }
}