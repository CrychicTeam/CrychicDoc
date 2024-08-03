package com.illusivesoulworks.polymorph.mixin.core;

import com.illusivesoulworks.polymorph.common.crafting.RecipeSelection;
import java.util.Optional;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ CraftingMenu.class })
public class MixinCraftingMenu {

    @Redirect(at = @At(value = "INVOKE", target = "net/minecraft/world/item/crafting/RecipeManager.getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"), method = { "slotChangedCraftingGrid" })
    private static <C extends Container, T extends Recipe<C>> Optional<T> polymorph$getRecipe(RecipeManager recipeManager, RecipeType<T> type, C inventory, Level world, AbstractContainerMenu abstractContainerMenu0, Level level1, Player player, CraftingContainer craftingContainer2, ResultContainer resultContainer3) {
        return RecipeSelection.getPlayerRecipe(abstractContainerMenu0, type, inventory, world, player);
    }
}