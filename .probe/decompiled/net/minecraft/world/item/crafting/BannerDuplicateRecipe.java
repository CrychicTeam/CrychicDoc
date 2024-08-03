package net.minecraft.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

public class BannerDuplicateRecipe extends CustomRecipe {

    public BannerDuplicateRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        DyeColor $$2 = null;
        ItemStack $$3 = null;
        ItemStack $$4 = null;
        for (int $$5 = 0; $$5 < craftingContainer0.m_6643_(); $$5++) {
            ItemStack $$6 = craftingContainer0.m_8020_($$5);
            if (!$$6.isEmpty()) {
                Item $$7 = $$6.getItem();
                if (!($$7 instanceof BannerItem)) {
                    return false;
                }
                BannerItem $$8 = (BannerItem) $$7;
                if ($$2 == null) {
                    $$2 = $$8.getColor();
                } else if ($$2 != $$8.getColor()) {
                    return false;
                }
                int $$9 = BannerBlockEntity.getPatternCount($$6);
                if ($$9 > 6) {
                    return false;
                }
                if ($$9 > 0) {
                    if ($$3 != null) {
                        return false;
                    }
                    $$3 = $$6;
                } else {
                    if ($$4 != null) {
                        return false;
                    }
                    $$4 = $$6;
                }
            }
        }
        return $$3 != null && $$4 != null;
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        for (int $$2 = 0; $$2 < craftingContainer0.m_6643_(); $$2++) {
            ItemStack $$3 = craftingContainer0.m_8020_($$2);
            if (!$$3.isEmpty()) {
                int $$4 = BannerBlockEntity.getPatternCount($$3);
                if ($$4 > 0 && $$4 <= 6) {
                    return $$3.copyWithCount(1);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer craftingContainer0) {
        NonNullList<ItemStack> $$1 = NonNullList.withSize(craftingContainer0.m_6643_(), ItemStack.EMPTY);
        for (int $$2 = 0; $$2 < $$1.size(); $$2++) {
            ItemStack $$3 = craftingContainer0.m_8020_($$2);
            if (!$$3.isEmpty()) {
                if ($$3.getItem().hasCraftingRemainingItem()) {
                    $$1.set($$2, new ItemStack($$3.getItem().getCraftingRemainingItem()));
                } else if ($$3.hasTag() && BannerBlockEntity.getPatternCount($$3) > 0) {
                    $$1.set($$2, $$3.copyWithCount(1));
                }
            }
        }
        return $$1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.BANNER_DUPLICATE;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 * int1 >= 2;
    }
}