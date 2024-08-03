package vectorwing.farmersdelight.common.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;

public class FoodServingRecipe extends CustomRecipe {

    public FoodServingRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    public boolean matches(CraftingContainer container, Level level) {
        ItemStack cookingPotStack = ItemStack.EMPTY;
        ItemStack containerStack = ItemStack.EMPTY;
        ItemStack secondStack = ItemStack.EMPTY;
        for (int index = 0; index < container.m_6643_(); index++) {
            ItemStack selectedStack = container.m_8020_(index);
            if (!selectedStack.isEmpty()) {
                if (cookingPotStack.isEmpty()) {
                    ItemStack mealStack = CookingPotBlockEntity.getMealFromItem(selectedStack);
                    if (!mealStack.isEmpty()) {
                        cookingPotStack = selectedStack;
                        containerStack = CookingPotBlockEntity.getContainerFromItem(selectedStack);
                        continue;
                    }
                }
                if (!secondStack.isEmpty()) {
                    return false;
                }
                secondStack = selectedStack;
            }
        }
        return !cookingPotStack.isEmpty() && !secondStack.isEmpty() && secondStack.is(containerStack.getItem());
    }

    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        for (int i = 0; i < container.m_6643_(); i++) {
            ItemStack selectedStack = container.m_8020_(i);
            if (!selectedStack.isEmpty() && selectedStack.is(ModItems.COOKING_POT.get())) {
                ItemStack resultStack = CookingPotBlockEntity.getMealFromItem(selectedStack).copy();
                resultStack.setCount(1);
                return resultStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(container.m_6643_(), ItemStack.EMPTY);
        for (int i = 0; i < remainders.size(); i++) {
            ItemStack selectedStack = container.m_8020_(i);
            if (selectedStack.hasCraftingRemainingItem()) {
                remainders.set(i, selectedStack.getCraftingRemainingItem());
            } else if (selectedStack.is(ModItems.COOKING_POT.get())) {
                CookingPotBlockEntity.takeServingFromItem(selectedStack);
                ItemStack newCookingPotStack = selectedStack.copy();
                newCookingPotStack.setCount(1);
                remainders.set(i, newCookingPotStack);
                break;
            }
        }
        return remainders;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.FOOD_SERVING.get();
    }
}