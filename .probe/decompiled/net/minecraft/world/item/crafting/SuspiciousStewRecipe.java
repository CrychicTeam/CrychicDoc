package net.minecraft.world.item.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SuspiciousEffectHolder;

public class SuspiciousStewRecipe extends CustomRecipe {

    public SuspiciousStewRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        boolean $$2 = false;
        boolean $$3 = false;
        boolean $$4 = false;
        boolean $$5 = false;
        for (int $$6 = 0; $$6 < craftingContainer0.m_6643_(); $$6++) {
            ItemStack $$7 = craftingContainer0.m_8020_($$6);
            if (!$$7.isEmpty()) {
                if ($$7.is(Blocks.BROWN_MUSHROOM.asItem()) && !$$4) {
                    $$4 = true;
                } else if ($$7.is(Blocks.RED_MUSHROOM.asItem()) && !$$3) {
                    $$3 = true;
                } else if ($$7.is(ItemTags.SMALL_FLOWERS) && !$$2) {
                    $$2 = true;
                } else {
                    if (!$$7.is(Items.BOWL) || $$5) {
                        return false;
                    }
                    $$5 = true;
                }
            }
        }
        return $$2 && $$4 && $$3 && $$5;
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        ItemStack $$2 = new ItemStack(Items.SUSPICIOUS_STEW, 1);
        for (int $$3 = 0; $$3 < craftingContainer0.m_6643_(); $$3++) {
            ItemStack $$4 = craftingContainer0.m_8020_($$3);
            if (!$$4.isEmpty()) {
                SuspiciousEffectHolder $$5 = SuspiciousEffectHolder.tryGet($$4.getItem());
                if ($$5 != null) {
                    SuspiciousStewItem.saveMobEffect($$2, $$5.getSuspiciousEffect(), $$5.getEffectDuration());
                    break;
                }
            }
        }
        return $$2;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 >= 2 && int1 >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SUSPICIOUS_STEW;
    }
}