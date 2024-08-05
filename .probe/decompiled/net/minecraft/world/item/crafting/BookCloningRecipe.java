package net.minecraft.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Level;

public class BookCloningRecipe extends CustomRecipe {

    public BookCloningRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        int $$2 = 0;
        ItemStack $$3 = ItemStack.EMPTY;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                if ($$5.is(Items.WRITTEN_BOOK)) {
                    if (!$$3.isEmpty()) {
                        return false;
                    }
                    $$3 = $$5;
                } else {
                    if (!$$5.is(Items.WRITABLE_BOOK)) {
                        return false;
                    }
                    $$2++;
                }
            }
        }
        return !$$3.isEmpty() && $$3.hasTag() && $$2 > 0;
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        int $$2 = 0;
        ItemStack $$3 = ItemStack.EMPTY;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                if ($$5.is(Items.WRITTEN_BOOK)) {
                    if (!$$3.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    $$3 = $$5;
                } else {
                    if (!$$5.is(Items.WRITABLE_BOOK)) {
                        return ItemStack.EMPTY;
                    }
                    $$2++;
                }
            }
        }
        if (!$$3.isEmpty() && $$3.hasTag() && $$2 >= 1 && WrittenBookItem.getGeneration($$3) < 2) {
            ItemStack $$6 = new ItemStack(Items.WRITTEN_BOOK, $$2);
            CompoundTag $$7 = $$3.getTag().copy();
            $$7.putInt("generation", WrittenBookItem.getGeneration($$3) + 1);
            $$6.setTag($$7);
            return $$6;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer craftingContainer0) {
        NonNullList<ItemStack> $$1 = NonNullList.withSize(craftingContainer0.m_6643_(), ItemStack.EMPTY);
        for (int $$2 = 0; $$2 < $$1.size(); $$2++) {
            ItemStack $$3 = craftingContainer0.m_8020_($$2);
            if ($$3.getItem().hasCraftingRemainingItem()) {
                $$1.set($$2, new ItemStack($$3.getItem().getCraftingRemainingItem()));
            } else if ($$3.getItem() instanceof WrittenBookItem) {
                $$1.set($$2, $$3.copyWithCount(1));
                break;
            }
        }
        return $$1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.BOOK_CLONING;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 >= 3 && int1 >= 3;
    }
}