package dev.xkmc.l2complements.content.item.create;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;

public class RefinedRadianceItem extends NoGravMagicalDohickyItem {

    public RefinedRadianceItem(Item.Properties properties, Supplier<MutableComponent> sup) {
        super(properties, sup);
    }

    @Override
    protected void onCreated(ItemEntity entity, CompoundTag persistentData) {
        super.onCreated(entity, persistentData);
        entity.m_20256_(entity.m_20184_().add(0.0, 0.25, 0.0));
    }
}