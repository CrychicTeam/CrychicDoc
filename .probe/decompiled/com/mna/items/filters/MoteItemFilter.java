package com.mna.items.filters;

import com.mna.items.ItemInit;
import java.util.ArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MoteItemFilter extends ItemFilter {

    private ArrayList<Item> validItems = new ArrayList();

    public MoteItemFilter() {
        this.validItems.add(ItemInit.MOTE_AIR.get());
        this.validItems.add(ItemInit.MOTE_ARCANE.get());
        this.validItems.add(ItemInit.MOTE_EARTH.get());
        this.validItems.add(ItemInit.MOTE_ENDER.get());
        this.validItems.add(ItemInit.MOTE_FIRE.get());
        this.validItems.add(ItemInit.MOTE_WATER.get());
        this.validItems.add(ItemInit.GREATER_MOTE_AIR.get());
        this.validItems.add(ItemInit.GREATER_MOTE_ARCANE.get());
        this.validItems.add(ItemInit.GREATER_MOTE_EARTH.get());
        this.validItems.add(ItemInit.GREATER_MOTE_ENDER.get());
        this.validItems.add(ItemInit.GREATER_MOTE_FIRE.get());
        this.validItems.add(ItemInit.GREATER_MOTE_WATER.get());
    }

    @Override
    public boolean IsValidItem(ItemStack stack) {
        return this.validItems.contains(stack.getItem());
    }
}