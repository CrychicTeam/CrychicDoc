package net.minecraft.world.item.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MapCloningRecipe extends CustomRecipe {

    public MapCloningRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        int $$2 = 0;
        ItemStack $$3 = ItemStack.EMPTY;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                if ($$5.is(Items.FILLED_MAP)) {
                    if (!$$3.isEmpty()) {
                        return false;
                    }
                    $$3 = $$5;
                } else {
                    if (!$$5.is(Items.MAP)) {
                        return false;
                    }
                    $$2++;
                }
            }
        }
        return !$$3.isEmpty() && $$2 > 0;
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        int $$2 = 0;
        ItemStack $$3 = ItemStack.EMPTY;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                if ($$5.is(Items.FILLED_MAP)) {
                    if (!$$3.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    $$3 = $$5;
                } else {
                    if (!$$5.is(Items.MAP)) {
                        return ItemStack.EMPTY;
                    }
                    $$2++;
                }
            }
        }
        return !$$3.isEmpty() && $$2 >= 1 ? $$3.copyWithCount($$2 + 1) : ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 >= 3 && int1 >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.MAP_CLONING;
    }
}