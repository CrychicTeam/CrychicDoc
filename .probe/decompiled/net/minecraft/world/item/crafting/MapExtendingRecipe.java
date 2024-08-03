package net.minecraft.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapExtendingRecipe extends ShapedRecipe {

    public MapExtendingRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, "", craftingBookCategory1, 3, 3, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.FILLED_MAP), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER)), new ItemStack(Items.MAP));
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        if (!super.matches(craftingContainer0, level1)) {
            return false;
        } else {
            ItemStack $$2 = findFilledMap(craftingContainer0);
            if ($$2.isEmpty()) {
                return false;
            } else {
                MapItemSavedData $$3 = MapItem.getSavedData($$2, level1);
                if ($$3 == null) {
                    return false;
                } else {
                    return $$3.isExplorationMap() ? false : $$3.scale < 4;
                }
            }
        }
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        ItemStack $$2 = findFilledMap(craftingContainer0).copyWithCount(1);
        $$2.getOrCreateTag().putInt("map_scale_direction", 1);
        return $$2;
    }

    private static ItemStack findFilledMap(CraftingContainer craftingContainer0) {
        for (int $$1 = 0; $$1 < craftingContainer0.m_6643_(); $$1++) {
            ItemStack $$2 = craftingContainer0.m_8020_($$1);
            if ($$2.is(Items.FILLED_MAP)) {
                return $$2;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.MAP_EXTENDING;
    }
}