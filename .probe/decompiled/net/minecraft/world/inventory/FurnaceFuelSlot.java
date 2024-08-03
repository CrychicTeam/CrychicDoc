package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FurnaceFuelSlot extends Slot {

    private final AbstractFurnaceMenu menu;

    public FurnaceFuelSlot(AbstractFurnaceMenu abstractFurnaceMenu0, Container container1, int int2, int int3, int int4) {
        super(container1, int2, int3, int4);
        this.menu = abstractFurnaceMenu0;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack0) {
        return this.menu.isFuel(itemStack0) || isBucket(itemStack0);
    }

    @Override
    public int getMaxStackSize(ItemStack itemStack0) {
        return isBucket(itemStack0) ? 1 : super.getMaxStackSize(itemStack0);
    }

    public static boolean isBucket(ItemStack itemStack0) {
        return itemStack0.is(Items.BUCKET);
    }
}