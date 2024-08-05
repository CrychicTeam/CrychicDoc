package com.mna.items.ritual;

import java.util.HashMap;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemPractitionersPatch extends Item {

    private static final HashMap<String, Supplier<ItemStack>> itemMap = new HashMap();

    private final PractitionersPouchPatches patch;

    private final int level;

    private final boolean countsTowardLimit;

    public ItemPractitionersPatch(PractitionersPouchPatches patch, int level, boolean countsTowardsLimit) {
        super(new Item.Properties());
        this.patch = patch;
        this.level = level;
        this.countsTowardLimit = countsTowardsLimit;
        itemMap.put(patch.name() + level, (Supplier) () -> new ItemStack(this));
    }

    public int getLevel() {
        return this.level;
    }

    public PractitionersPouchPatches getPatch() {
        return this.patch;
    }

    public boolean countsTowardsLimit() {
        return this.countsTowardLimit;
    }

    public static ItemStack getItemFor(PractitionersPouchPatches patch, int level) {
        return (ItemStack) ((Supplier) itemMap.getOrDefault(patch.name() + Math.min(level, patch.getLevels()), (Supplier) () -> ItemStack.EMPTY)).get();
    }
}