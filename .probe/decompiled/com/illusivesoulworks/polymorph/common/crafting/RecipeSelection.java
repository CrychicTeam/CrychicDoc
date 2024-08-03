package com.illusivesoulworks.polymorph.common.crafting;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.common.capability.IBlockEntityRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IPlayerRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IStackRecipeData;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RecipeSelection {

    private static <T extends Recipe<C>, C extends Container> Optional<T> getDefaultRecipe(RecipeType<T> type, C inventory, Level level) {
        return level.getRecipeManager().getRecipeFor(type, inventory, level);
    }

    public static <T extends Recipe<C>, C extends Container> Optional<T> getPlayerRecipe(AbstractContainerMenu containerMenu, RecipeType<T> type, C inventory, Level level, List<Slot> slots) {
        Player player = null;
        for (Slot slot : slots) {
            if (slot.container instanceof Inventory inv) {
                player = inv.player;
                break;
            }
        }
        return player != null ? getPlayerRecipe(containerMenu, type, inventory, level, player, new ArrayList()) : level.getRecipeManager().getRecipesFor(type, inventory, level).stream().findFirst();
    }

    public static <T extends Recipe<C>, C extends Container> Optional<T> getPlayerRecipe(AbstractContainerMenu containerMenu, RecipeType<T> type, C inventory, Level level, Player player) {
        return getPlayerRecipe(containerMenu, type, inventory, level, player, new ArrayList());
    }

    public static <T extends Recipe<C>, C extends Container> Optional<T> getPlayerRecipe(AbstractContainerMenu containerMenu, RecipeType<T> type, C inventory, Level level, Player player, List<T> recipes) {
        Optional<? extends IPlayerRecipeData> maybeData = PolymorphApi.common().getRecipeData(player);
        maybeData.ifPresent(playerRecipeData -> playerRecipeData.setContainerMenu(containerMenu));
        return getRecipe(type, inventory, level, maybeData, recipes);
    }

    public static <T extends Recipe<C>, C extends Container> Optional<T> getStackRecipe(RecipeType<T> type, C inventory, Level level, ItemStack stack) {
        Optional<? extends IStackRecipeData> maybeData = PolymorphApi.common().getRecipeData(stack);
        return getRecipe(type, inventory, level, maybeData, new ArrayList());
    }

    public static <T extends Recipe<C>, C extends Container> Optional<T> getBlockEntityRecipe(RecipeType<T> type, C inventory, Level level, BlockEntity blockEntity) {
        Optional<? extends IBlockEntityRecipeData> maybeData = PolymorphApi.common().getRecipeData(blockEntity);
        return getRecipe(type, inventory, level, maybeData, new ArrayList());
    }

    private static <T extends Recipe<C>, C extends Container> Optional<T> getRecipe(RecipeType<T> type, C inventory, Level level, Optional<? extends IRecipeData<?>> pOpt, List<T> recipes) {
        return pOpt.isPresent() ? (Optional) pOpt.map(recipeData -> recipeData.getRecipe(type, inventory, level, recipes)).orElse(Optional.empty()) : level.getRecipeManager().getRecipesFor(type, inventory, level).stream().findFirst();
    }
}