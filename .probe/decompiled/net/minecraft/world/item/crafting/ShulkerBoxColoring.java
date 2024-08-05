package net.minecraft.world.item.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class ShulkerBoxColoring extends CustomRecipe {

    public ShulkerBoxColoring(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        int $$2 = 0;
        int $$3 = 0;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                if (Block.byItem($$5.getItem()) instanceof ShulkerBoxBlock) {
                    $$2++;
                } else {
                    if (!($$5.getItem() instanceof DyeItem)) {
                        return false;
                    }
                    $$3++;
                }
                if ($$3 > 1 || $$2 > 1) {
                    return false;
                }
            }
        }
        return $$2 == 1 && $$3 == 1;
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        ItemStack $$2 = ItemStack.EMPTY;
        DyeItem $$3 = (DyeItem) Items.WHITE_DYE;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                Item $$6 = $$5.getItem();
                if (Block.byItem($$6) instanceof ShulkerBoxBlock) {
                    $$2 = $$5;
                } else if ($$6 instanceof DyeItem) {
                    $$3 = (DyeItem) $$6;
                }
            }
        }
        ItemStack $$7 = ShulkerBoxBlock.getColoredItemStack($$3.getDyeColor());
        if ($$2.hasTag()) {
            $$7.setTag($$2.getTag().copy());
        }
        return $$7;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 * int1 >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHULKER_BOX_COLORING;
    }
}