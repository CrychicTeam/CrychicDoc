package net.mehvahdjukaar.supplementaries.common.items.crafting;

import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.supplementaries.reg.ModRecipes;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class BlackboardDuplicateRecipe extends CustomRecipe {

    public BlackboardDuplicateRecipe(ResourceLocation idIn, CraftingBookCategory category) {
        super(idIn, category);
    }

    private boolean isDrawnBlackboard(ItemStack stack) {
        CompoundTag tag = stack.getTagElement("BlockEntityTag");
        return tag != null && tag.contains("Pixels");
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;
        for (int i = 0; i < inv.m_6643_(); i++) {
            ItemStack stack = inv.m_8020_(i);
            Item item = stack.getItem();
            if (item == ModRegistry.BLACKBOARD_ITEM.get()) {
                if (this.isDrawnBlackboard(stack)) {
                    if (itemstack != null) {
                        return false;
                    }
                    itemstack = stack;
                } else {
                    if (itemstack1 != null) {
                        return false;
                    }
                    itemstack1 = stack;
                }
            } else if (!stack.isEmpty()) {
                return false;
            }
        }
        return itemstack != null && itemstack1 != null;
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        for (int i = 0; i < inv.m_6643_(); i++) {
            ItemStack stack = inv.m_8020_(i);
            if (this.isDrawnBlackboard(stack)) {
                ItemStack s = stack.copy();
                s.setCount(1);
                return s;
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
                } else if (itemstack.hasTag() && this.isDrawnBlackboard(itemstack)) {
                    ItemStack copy = itemstack.copy();
                    copy.setCount(1);
                    stacks.set(i, copy);
                }
            }
        }
        return stacks;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer<?>) ModRecipes.BLACKBOARD_DUPLICATE.get();
    }
}