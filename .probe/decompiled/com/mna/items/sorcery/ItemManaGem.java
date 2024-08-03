package com.mna.items.sorcery;

import com.mna.api.items.INoSoulsRecharge;
import com.mna.api.items.ManaBatteryItem;
import net.minecraft.world.item.Item;

public class ItemManaGem extends ManaBatteryItem implements INoSoulsRecharge {

    public ItemManaGem(Item.Properties properties, float maxMana) {
        super(properties, maxMana);
    }
}