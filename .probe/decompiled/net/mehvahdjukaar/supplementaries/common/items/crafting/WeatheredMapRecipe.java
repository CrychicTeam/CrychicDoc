package net.mehvahdjukaar.supplementaries.common.items.crafting;

import java.lang.ref.WeakReference;
import net.mehvahdjukaar.supplementaries.common.items.AntiqueInkItem;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.WeatheredMap;
import net.mehvahdjukaar.supplementaries.reg.ModRecipes;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class WeatheredMapRecipe extends CustomRecipe {

    private static WeakReference<ServerLevel> lastLevelHack = null;

    public WeatheredMapRecipe(ResourceLocation idIn, CraftingBookCategory category) {
        super(idIn, category);
    }

    public static void onWorldUnload() {
        lastLevelHack = null;
    }

    public boolean matches(CraftingContainer inv, Level level) {
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;
        for (int i = 0; i < inv.m_6643_(); i++) {
            ItemStack stack = inv.m_8020_(i);
            if (!stack.isEmpty()) {
                if (isMap(stack)) {
                    if (itemstack != null) {
                        return false;
                    }
                    itemstack = stack;
                } else {
                    if (stack.getItem() != ModRegistry.ANTIQUE_INK.get()) {
                        return false;
                    }
                    if (itemstack1 != null) {
                        return false;
                    }
                    itemstack1 = stack;
                }
            }
        }
        boolean match = itemstack != null && itemstack1 != null;
        if (match && level instanceof ServerLevel serverLevel) {
            lastLevelHack = new WeakReference(serverLevel);
        }
        return match;
    }

    private static boolean isMap(ItemStack stack) {
        return stack.getItem() == Items.FILLED_MAP;
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        boolean antique = true;
        for (int i = 0; i < inv.m_6643_(); i++) {
            if (inv.m_8020_(i).getItem() == ModRegistry.SOAP.get()) {
                antique = false;
                break;
            }
        }
        for (int ix = 0; ix < inv.m_6643_(); ix++) {
            ItemStack stack = inv.m_8020_(ix);
            if (stack.getItem() instanceof MapItem) {
                ItemStack s = stack.copy();
                s.setCount(1);
                if (lastLevelHack != null) {
                    WeatheredMap.setAntique((Level) lastLevelHack.get(), s, antique, false);
                    AntiqueInkItem.setAntiqueInk(s, true);
                }
                return s;
            }
        }
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        return NonNullList.withSize(inv.m_6643_(), ItemStack.EMPTY);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer<?>) ModRecipes.ANTIQUE_MAP.get();
    }
}