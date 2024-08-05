package net.minecraft.world.item.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class CustomRecipe implements CraftingRecipe {

    private final ResourceLocation id;

    private final CraftingBookCategory category;

    public CustomRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        this.id = resourceLocation0;
        this.category = craftingBookCategory1;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess0) {
        return ItemStack.EMPTY;
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }
}