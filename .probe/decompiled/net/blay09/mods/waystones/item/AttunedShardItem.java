package net.blay09.mods.waystones.item;

import net.minecraft.world.item.Item;

public class AttunedShardItem extends AbstractAttunedShardItem {

    public AttunedShardItem(Item.Properties properties) {
        super(properties.stacksTo(1));
    }
}