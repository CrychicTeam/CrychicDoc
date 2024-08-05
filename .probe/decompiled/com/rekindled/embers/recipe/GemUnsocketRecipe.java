package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.item.IInflictorGemHolder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GemUnsocketRecipe implements CraftingRecipe {

    public static final GemUnsocketRecipe.Serializer SERIALIZER = new GemUnsocketRecipe.Serializer();

    public final ResourceLocation id;

    public GemUnsocketRecipe(ResourceLocation id) {
        this.id = id;
    }

    public boolean matches(CraftingContainer container, Level level) {
        ItemStack cloak = ItemStack.EMPTY;
        int cloaks = 0;
        for (int i = 0; i < container.m_6643_(); i++) {
            ItemStack stack = container.m_8020_(i);
            if (!stack.isEmpty()) {
                if (!(stack.getItem() instanceof IInflictorGemHolder)) {
                    return false;
                }
                if (((IInflictorGemHolder) stack.getItem()).getAttachedGemCount(stack) > 0) {
                    cloak = stack;
                }
                cloaks++;
            }
        }
        return !cloak.isEmpty() && cloaks == 1 && container.m_6643_() >= ((IInflictorGemHolder) cloak.getItem()).getAttachedGemCount(cloak);
    }

    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack capeStack = ItemStack.EMPTY;
        for (int i = 0; i < container.m_6643_(); i++) {
            if (!container.m_8020_(i).isEmpty() && container.m_8020_(i).getItem() instanceof IInflictorGemHolder) {
                capeStack = container.m_8020_(i).copy();
            }
        }
        if (!capeStack.isEmpty()) {
            ((IInflictorGemHolder) capeStack.getItem()).clearGems(capeStack);
        }
        return capeStack;
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> gems = NonNullList.withSize(container.m_6643_(), ItemStack.EMPTY);
        int index = 0;
        for (int i = 0; i < container.m_6643_(); i++) {
            ItemStack stack = container.m_8020_(i);
            if (!stack.isEmpty() && stack.getItem() instanceof IInflictorGemHolder) {
                for (ItemStack gem : ((IInflictorGemHolder) stack.getItem()).getAttachedGems(stack)) {
                    if (!gem.isEmpty()) {
                        gems.set(index, gem);
                        index++;
                    }
                }
            }
        }
        return gems;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return new ItemStack(RegistryManager.ASHEN_CLOAK.get());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.EQUIPMENT;
    }

    public static class Serializer implements RecipeSerializer<GemUnsocketRecipe> {

        public GemUnsocketRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return new GemUnsocketRecipe(recipeId);
        }

        @Nullable
        public GemUnsocketRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return new GemUnsocketRecipe(recipeId);
        }

        public void toNetwork(FriendlyByteBuf buffer, GemUnsocketRecipe recipe) {
        }
    }
}