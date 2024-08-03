package com.almostreliable.summoningrituals.inventory;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class VanillaWrapper extends RecipeWrapper {

    private final ItemHandler delegate;

    VanillaWrapper(ItemHandler delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    public List<ItemStack> getItems() {
        return this.delegate.getNoneEmptyItems();
    }

    public ItemStack getCatalyst() {
        return this.delegate.getCatalyst();
    }
}