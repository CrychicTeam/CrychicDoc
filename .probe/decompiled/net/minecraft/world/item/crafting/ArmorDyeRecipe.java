package net.minecraft.world.item.crafting;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArmorDyeRecipe extends CustomRecipe {

    public ArmorDyeRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        ItemStack $$2 = ItemStack.EMPTY;
        List<ItemStack> $$3 = Lists.newArrayList();
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                if ($$5.getItem() instanceof DyeableLeatherItem) {
                    if (!$$2.isEmpty()) {
                        return false;
                    }
                    $$2 = $$5;
                } else {
                    if (!($$5.getItem() instanceof DyeItem)) {
                        return false;
                    }
                    $$3.add($$5);
                }
            }
        }
        return !$$2.isEmpty() && !$$3.isEmpty();
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        List<DyeItem> $$2 = Lists.newArrayList();
        ItemStack $$3 = ItemStack.EMPTY;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                Item $$6 = $$5.getItem();
                if ($$6 instanceof DyeableLeatherItem) {
                    if (!$$3.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    $$3 = $$5.copy();
                } else {
                    if (!($$6 instanceof DyeItem)) {
                        return ItemStack.EMPTY;
                    }
                    $$2.add((DyeItem) $$6);
                }
            }
        }
        return !$$3.isEmpty() && !$$2.isEmpty() ? DyeableLeatherItem.dyeArmor($$3, $$2) : ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 * int1 >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.ARMOR_DYE;
    }
}