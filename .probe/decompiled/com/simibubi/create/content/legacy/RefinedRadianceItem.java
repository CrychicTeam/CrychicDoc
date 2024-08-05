package com.simibubi.create.content.legacy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RefinedRadianceItem extends NoGravMagicalDohickyItem {

    public RefinedRadianceItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    protected void onCreated(ItemEntity entity, CompoundTag persistentData) {
        super.onCreated(entity, persistentData);
        entity.m_20256_(entity.m_20184_().add(0.0, 0.25, 0.0));
    }
}