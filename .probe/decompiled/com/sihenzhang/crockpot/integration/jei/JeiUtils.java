package com.sihenzhang.crockpot.integration.jei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

public final class JeiUtils {

    private JeiUtils() {
    }

    public static List<List<ItemStack>> getPagedItemStacks(List<ItemStack> stacks, IFocusGroup focuses, RecipeIngredientRole role, int size) {
        List<ItemStack> focusedStacks = getFocusedItemStacks(stacks, focuses, role);
        if (focusedStacks.size() <= size) {
            return focusedStacks.stream().map(List::of).toList();
        } else {
            List<List<ItemStack>> pagedItemStacks = new ArrayList();
            for (int i = 0; i < size; i++) {
                List<ItemStack> expandedStacks = new ArrayList();
                expandedStacks.add((ItemStack) focusedStacks.get(i));
                pagedItemStacks.add(expandedStacks);
            }
            int pages = focusedStacks.size() / size + (focusedStacks.size() % size == 0 ? 0 : 1);
            for (int i = 1; i < pages; i++) {
                for (int j = 0; j < size; j++) {
                    ((List) pagedItemStacks.get(j)).add(i * size + j < focusedStacks.size() ? (ItemStack) focusedStacks.get(i * size + j) : null);
                }
            }
            return pagedItemStacks;
        }
    }

    private static List<ItemStack> getFocusedItemStacks(List<ItemStack> stacks, IFocusGroup focuses, RecipeIngredientRole role) {
        return focuses.getFocuses(VanillaTypes.ITEM_STACK, role).findAny().isPresent() ? stacks.stream().filter(stack -> focuses.getFocuses(VanillaTypes.ITEM_STACK, role).anyMatch(focus -> ItemStack.isSameItem(stack, (ItemStack) focus.getTypedValue().getIngredient()))).toList() : List.copyOf(stacks);
    }

    public static List<ItemStack> getItemsFromIngredientWithoutEmptyTag(Ingredient ingredient) {
        return Arrays.stream(ingredient.getItems()).filter(stack -> !stack.is(Blocks.BARRIER.asItem()) || !stack.getHoverName().getContents().toString().contains("Empty Tag: ")).toList();
    }
}