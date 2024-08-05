package net.mehvahdjukaar.supplementaries.common.items.crafting;

import net.mehvahdjukaar.supplementaries.reg.ModRecipes;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class RopeArrowAddRecipe extends CustomRecipe {

    public RopeArrowAddRecipe(ResourceLocation idIn, CraftingBookCategory category) {
        super(idIn, category);
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack arrow = null;
        ItemStack rope = null;
        int missingRopes = 0;
        for (int i = 0; i < inv.m_6643_(); i++) {
            ItemStack stack = inv.m_8020_(i);
            if (stack.getItem() == ModRegistry.ROPE_ARROW_ITEM.get() && stack.getDamageValue() != 0) {
                if (arrow != null) {
                    return false;
                }
                arrow = stack;
                missingRopes += stack.getDamageValue();
            } else if (stack.is(ModTags.ROPES)) {
                rope = stack;
                missingRopes--;
            } else if (!stack.isEmpty()) {
                return false;
            }
        }
        return arrow != null && rope != null && missingRopes >= 0;
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        int ropes = 0;
        ItemStack arrow = null;
        for (int i = 0; i < inv.m_6643_(); i++) {
            ItemStack stack = inv.m_8020_(i);
            if (stack.is(ModTags.ROPES)) {
                ropes++;
            }
            if (stack.getItem() == ModRegistry.ROPE_ARROW_ITEM.get()) {
                arrow = stack;
            }
        }
        ItemStack returnArrow = arrow.copy();
        returnArrow.setDamageValue(arrow.getDamageValue() - ropes);
        return returnArrow;
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
        return (RecipeSerializer<?>) ModRecipes.ROPE_ARROW_ADD.get();
    }
}