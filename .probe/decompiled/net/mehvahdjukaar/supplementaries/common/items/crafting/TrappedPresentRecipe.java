package net.mehvahdjukaar.supplementaries.common.items.crafting;

import java.util.function.Supplier;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PresentBlock;
import net.mehvahdjukaar.supplementaries.common.items.PresentItem;
import net.mehvahdjukaar.supplementaries.reg.ModRecipes;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class TrappedPresentRecipe extends CustomRecipe {

    public TrappedPresentRecipe(ResourceLocation resourceLocation, CraftingBookCategory category) {
        super(resourceLocation, category);
    }

    public boolean matches(CraftingContainer craftingContainer, Level level) {
        int i = 0;
        int j = 0;
        for (int k = 0; k < craftingContainer.m_6643_(); k++) {
            ItemStack itemstack = craftingContainer.m_8020_(k);
            if (!itemstack.isEmpty()) {
                label58: {
                    if (itemstack.getItem() instanceof PresentItem pi && pi.m_40614_() instanceof PresentBlock) {
                        if (itemstack.hasTag()) {
                            ListTag list = itemstack.getTag().getList("Items", 10);
                            if (list.size() != 1) {
                                return false;
                            }
                            if (ItemStack.of(list.getCompound(0)).getCount() != 1) {
                                return false;
                            }
                        }
                        i++;
                        break label58;
                    }
                    if (!itemstack.is(Items.TRIPWIRE_HOOK)) {
                        return false;
                    }
                    j++;
                }
                if (j > 1 || i > 1) {
                    return false;
                }
            }
        }
        return i == 1 && j == 1;
    }

    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        ItemStack itemstack = ItemStack.EMPTY;
        DyeColor dyecolor = DyeColor.WHITE;
        for (int i = 0; i < craftingContainer.m_6643_(); i++) {
            ItemStack stack = craftingContainer.m_8020_(i);
            if (!stack.isEmpty() && stack.getItem() instanceof PresentItem pi) {
                itemstack = stack;
                dyecolor = pi.getColor();
            }
        }
        ItemStack result = new ItemStack((ItemLike) ((Supplier) ModRegistry.TRAPPED_PRESENTS.get(dyecolor)).get());
        if (itemstack.hasTag()) {
            CompoundTag tag = itemstack.getTag().copy();
            CompoundTag com = tag.getCompound("BlockEntityTag");
            com.remove("Recipient");
            com.remove("Sender");
            result.setTag(tag);
        }
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return x * y >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer<?>) ModRecipes.TRAPPED_PRESENT.get();
    }
}