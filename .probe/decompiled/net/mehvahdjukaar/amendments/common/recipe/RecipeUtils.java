package net.mehvahdjukaar.amendments.common.recipe;

import com.mojang.datafixers.util.Pair;
import net.mehvahdjukaar.amendments.common.item.DyeBottleItem;
import net.mehvahdjukaar.moonlight.api.fluids.FluidContainerList;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RecipeUtils {

    public static Pair<ItemStack, Float> craftWithFluidAndDye(Level level, SoftFluidStack fluid, ItemStack toRecolor) {
        CompoundTag tag = fluid.getTag();
        if (tag == null) {
            return null;
        } else {
            Pair<ItemStack, Float> c = craftWithFluid(level, fluid, toRecolor, true);
            if (c != null) {
                return c;
            } else {
                Item dyeItem = DyeItem.byColor(DyeBottleItem.getClosestDye(fluid));
                ItemStack recolored = simulateCrafting(level, toRecolor, dyeItem.getDefaultInstance(), false);
                return recolored != null ? Pair.of(recolored, 1.0F) : null;
            }
        }
    }

    public static Pair<ItemStack, Float> craftWithFluid(Level level, SoftFluidStack fluidStack, ItemStack playerItem, boolean try9x9) {
        SoftFluid sf = fluidStack.fluid();
        for (FluidContainerList.Category category : sf.getContainerList().getCategories()) {
            int capacity = category.getCapacity();
            if (capacity <= fluidStack.getCount()) {
                Pair<ItemStack, FluidContainerList.Category> p = fluidStack.toItem(category.getEmptyContainer().getDefaultInstance(), true);
                if (p != null) {
                    ItemStack crafted = simulateCrafting(level, (ItemStack) p.getFirst(), playerItem, false);
                    if (crafted != null) {
                        return Pair.of(crafted, (float) capacity);
                    }
                    if (try9x9) {
                        ItemStack crafted8 = simulateCrafting(level, (ItemStack) p.getFirst(), playerItem, true);
                        if (crafted8 != null) {
                            int actualCapacity = 8 / crafted8.getCount();
                            if (actualCapacity > 0) {
                                return Pair.of(crafted8, (float) actualCapacity);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static ItemStack simulateCrafting(Level level, ItemStack dye, ItemStack playerItem, boolean surround) {
        DummyContainer container = surround ? DummyContainer.surround(dye.copy(), playerItem.copy()) : DummyContainer.of(dye.copy(), playerItem.copy());
        for (CraftingRecipe r : level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, container, level)) {
            ItemStack recolored = r.m_5874_(container, level.registryAccess());
            if (!recolored.isEmpty() && !playerItem.equals(recolored)) {
                NonNullList<ItemStack> remainingItems = r.m_7457_(container);
                remainingItems.remove(Items.GLASS_BOTTLE.getDefaultInstance());
                if (remainingItems.stream().noneMatch(i -> !i.isEmpty() && !i.is(Items.GLASS_BOTTLE))) {
                    return recolored;
                }
            }
        }
        return null;
    }
}