package net.mehvahdjukaar.supplementaries.common.items.crafting;

import java.util.List;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRecipes;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class SoapClearRecipe extends CustomRecipe {

    public SoapClearRecipe(ResourceLocation resourceLocation, CraftingBookCategory category) {
        super(resourceLocation, category);
    }

    public boolean matches(CraftingContainer craftingContainer, Level level) {
        int i = 0;
        int j = 0;
        for (int k = 0; k < craftingContainer.m_6643_(); k++) {
            ItemStack itemstack = craftingContainer.m_8020_(k);
            if (!itemstack.isEmpty()) {
                Item item = itemstack.getItem();
                boolean d = BlocksColorAPI.getColor(item) != null && !itemstack.is(ModTags.SOAP_BLACKLIST) && !((List) CommonConfigs.Functional.SOAP_DYE_CLEAN_BLACKLIST.get()).contains(BlocksColorAPI.getKey(item));
                if (!d && !(item instanceof DyeableLeatherItem) && !this.hasTrim(item)) {
                    if (!itemstack.is((Item) ModRegistry.SOAP.get())) {
                        return false;
                    }
                    j++;
                } else {
                    i++;
                }
                if (j > 1 || i > 1) {
                    return false;
                }
            }
        }
        return i == 1 && j == 1;
    }

    private boolean hasTrim(Item item) {
        return false;
    }

    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        ItemStack toRecolor = ItemStack.EMPTY;
        for (int i = 0; i < craftingContainer.m_6643_(); i++) {
            ItemStack stack = craftingContainer.m_8020_(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                if (BlocksColorAPI.getColor(item) != null || item instanceof DyeableLeatherItem) {
                    toRecolor = stack.copyWithCount(1);
                }
            }
        }
        Item ix = toRecolor.getItem();
        if (ix instanceof DyeableLeatherItem leatherItem) {
            ItemStack result = toRecolor.copy();
            leatherItem.clearColor(result);
            return result;
        } else {
            Item recolored = BlocksColorAPI.changeColor(ix, null);
            ItemStack result;
            if (recolored != null) {
                result = recolored.getDefaultInstance();
            } else {
                result = toRecolor.copy();
            }
            CompoundTag tag = toRecolor.getTag();
            if (tag != null) {
                result.setTag(tag.copy());
            }
            result.setCount(1);
            return result;
        }
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return x * y >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer<?>) ModRecipes.SOAP_CLEARING.get();
    }
}