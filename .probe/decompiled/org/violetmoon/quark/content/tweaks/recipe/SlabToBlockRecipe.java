package org.violetmoon.quark.content.tweaks.recipe;

import java.util.Optional;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.violetmoon.quark.content.tweaks.module.SlabsToBlocksModule;

public class SlabToBlockRecipe extends CustomRecipe {

    public static final SimpleCraftingRecipeSerializer<?> SERIALIZER = new SimpleCraftingRecipeSerializer(SlabToBlockRecipe::new);

    private boolean locked = false;

    public SlabToBlockRecipe(ResourceLocation id, CraftingBookCategory cat) {
        super(id, cat);
    }

    public boolean matches(CraftingContainer container, Level level) {
        if (this.locked) {
            return false;
        } else {
            Item target = null;
            boolean checked = false;
            boolean result = false;
            for (int i = 0; i < container.m_6643_(); i++) {
                ItemStack stack = container.m_8020_(i);
                if (!stack.isEmpty()) {
                    Item item = stack.getItem();
                    if (target != null) {
                        if (checked) {
                            return false;
                        }
                        result = item == target && this.checkForOtherRecipes(container, level);
                        checked = true;
                    } else {
                        if (!SlabsToBlocksModule.recipes.containsKey(item)) {
                            return false;
                        }
                        target = item;
                    }
                }
            }
            return result;
        }
    }

    private synchronized boolean checkForOtherRecipes(CraftingContainer container, Level level) {
        this.locked = true;
        boolean ret = false;
        MinecraftServer server = level.getServer();
        if (server != null) {
            Optional<CraftingRecipe> optional = server.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, container, level);
            ret = !optional.isPresent();
        }
        this.locked = false;
        return ret;
    }

    public ItemStack assemble(CraftingContainer container, RegistryAccess gaming) {
        for (int i = 0; i < container.m_6643_(); i++) {
            ItemStack stack = container.m_8020_(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                if (SlabsToBlocksModule.recipes.containsKey(item)) {
                    return new ItemStack((ItemLike) SlabsToBlocksModule.recipes.get(item));
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}