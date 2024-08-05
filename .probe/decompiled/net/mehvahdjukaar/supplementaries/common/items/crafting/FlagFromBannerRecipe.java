package net.mehvahdjukaar.supplementaries.common.items.crafting;

import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.supplementaries.common.items.FlagItem;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.mehvahdjukaar.supplementaries.reg.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

public class FlagFromBannerRecipe extends CustomRecipe {

    public FlagFromBannerRecipe(ResourceLocation idIn, CraftingBookCategory category) {
        super(idIn, category);
    }

    public boolean matches(CraftingContainer inv, Level world) {
        DyeColor dyecolor = null;
        ItemStack withPatterns = null;
        ItemStack empty = null;
        for (int i = 0; i < inv.m_6643_(); i++) {
            ItemStack itemStack = inv.m_8020_(i);
            Item item = itemStack.getItem();
            if (item instanceof FlagItem) {
                FlagItem flagItem = (FlagItem) item;
                if (dyecolor == null) {
                    dyecolor = flagItem.getColor();
                } else if (dyecolor != flagItem.getColor()) {
                    return false;
                }
                int j = BannerBlockEntity.getPatternCount(itemStack);
                if (j > getMaxBannerPatterns()) {
                    return false;
                }
                if (j > 0) {
                    if (withPatterns != null) {
                        return false;
                    }
                    withPatterns = itemStack;
                } else {
                    if (empty != null) {
                        return false;
                    }
                    empty = itemStack;
                }
            } else if (item instanceof BannerItem) {
                BannerItem banneritem = (BannerItem) item;
                if (dyecolor == null) {
                    dyecolor = banneritem.getColor();
                } else if (dyecolor != banneritem.getColor()) {
                    return false;
                }
                int jx = BannerBlockEntity.getPatternCount(itemStack);
                if (jx > getMaxBannerPatterns()) {
                    return false;
                }
                if (jx <= 0 || empty != null && empty.getItem() instanceof BannerItem) {
                    if (withPatterns == null || !(withPatterns.getItem() instanceof BannerItem)) {
                        if (empty != null) {
                            return false;
                        }
                        empty = itemStack;
                    }
                } else {
                    if (withPatterns != null) {
                        return false;
                    }
                    withPatterns = itemStack;
                }
            } else if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return withPatterns != null && empty != null;
    }

    private static int getMaxBannerPatterns() {
        return CompatHandler.QUARK ? QuarkCompat.getBannerPatternLimit(6) : 6;
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        for (int i = 0; i < inv.m_6643_(); i++) {
            ItemStack withPatterns = inv.m_8020_(i);
            if (!withPatterns.isEmpty()) {
                int patternCount = BannerBlockEntity.getPatternCount(withPatterns);
                if (patternCount > 0 && patternCount <= getMaxBannerPatterns()) {
                    for (int k = 0; k < inv.m_6643_(); k++) {
                        if (i != k) {
                            ItemStack empty = inv.m_8020_(k);
                            Item it = empty.getItem();
                            if (it instanceof FlagItem || it instanceof BannerItem) {
                                ItemStack result = empty.copy();
                                result.setCount(1);
                                result.setTag(withPatterns.getTag());
                                return result;
                            }
                        }
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(inv.m_6643_(), ItemStack.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack itemstack = inv.m_8020_(i);
            if (!itemstack.isEmpty()) {
                Optional<ItemStack> container = ForgeHelper.getCraftingRemainingItem(itemstack);
                if (container.isPresent()) {
                    stacks.set(i, (ItemStack) container.get());
                } else if (itemstack.hasTag() && BannerBlockEntity.getPatternCount(itemstack) > 0) {
                    ItemStack copy = itemstack.copy();
                    copy.setCount(1);
                    stacks.set(i, copy);
                }
            }
        }
        return stacks;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer<?>) ModRecipes.FLAG_FROM_BANNER.get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }
}