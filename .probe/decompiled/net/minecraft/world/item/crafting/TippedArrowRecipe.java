package net.minecraft.world.item.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;

public class TippedArrowRecipe extends CustomRecipe {

    public TippedArrowRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        if (craftingContainer0.getWidth() == 3 && craftingContainer0.getHeight() == 3) {
            for (int $$2 = 0; $$2 < craftingContainer0.getWidth(); $$2++) {
                for (int $$3 = 0; $$3 < craftingContainer0.getHeight(); $$3++) {
                    ItemStack $$4 = craftingContainer0.m_8020_($$2 + $$3 * craftingContainer0.getWidth());
                    if ($$4.isEmpty()) {
                        return false;
                    }
                    if ($$2 == 1 && $$3 == 1) {
                        if (!$$4.is(Items.LINGERING_POTION)) {
                            return false;
                        }
                    } else if (!$$4.is(Items.ARROW)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        ItemStack $$2 = craftingContainer0.m_8020_(1 + craftingContainer0.getWidth());
        if (!$$2.is(Items.LINGERING_POTION)) {
            return ItemStack.EMPTY;
        } else {
            ItemStack $$3 = new ItemStack(Items.TIPPED_ARROW, 8);
            PotionUtils.setPotion($$3, PotionUtils.getPotion($$2));
            PotionUtils.setCustomEffects($$3, PotionUtils.getCustomEffects($$2));
            return $$3;
        }
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 >= 2 && int1 >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.TIPPED_ARROW;
    }
}