package dev.xkmc.l2complements.content.item.misc;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BurntItem extends Item {

    public BurntItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }
}