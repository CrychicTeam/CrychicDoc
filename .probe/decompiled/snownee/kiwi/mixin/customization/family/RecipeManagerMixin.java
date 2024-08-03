package snownee.kiwi.mixin.customization.family;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import snownee.kiwi.customization.block.family.StonecutterRecipeMaker;

@Mixin({ RecipeManager.class })
public class RecipeManagerMixin {

    @ModifyReturnValue(method = { "getRecipesFor" }, at = { @At("RETURN") })
    private <C extends Container, T extends Recipe<C>> List<T> kiwi$addFakeStonecutterRecipes(List<T> recipes, RecipeType<T> pRecipeType, C pInventory) {
        return (List<T>) (pRecipeType == RecipeType.STONECUTTING ? StonecutterRecipeMaker.appendRecipesFor(recipes, pInventory) : recipes);
    }

    @ModifyReturnValue(method = { "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;" }, at = { @At("RETURN") })
    private <C extends Container, T extends Recipe<C>> Optional<Pair<ResourceLocation, T>> kiwi$injectFakeStonecutterRecipe(Optional<Pair<ResourceLocation, T>> original, RecipeType<T> pRecipeType, C pInventory, Level level, @Nullable ResourceLocation pLastRecipe) {
        return pRecipeType == RecipeType.STONECUTTING && original.isEmpty() ? StonecutterRecipeMaker.appendRecipesFor(List.of(), pInventory).stream().findAny().map(recipe -> Pair.of(recipe.m_6423_(), recipe)) : original;
    }

    @ModifyReturnValue(method = { "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;" }, at = { @At("RETURN") })
    private <C extends Container, T extends Recipe<C>> Optional<T> kiwi$injectFakeStonecutterRecipe(Optional<T> original, RecipeType<T> pRecipeType, C pInventory, Level level) {
        return pRecipeType == RecipeType.STONECUTTING && original.isEmpty() ? StonecutterRecipeMaker.appendRecipesFor(List.of(), pInventory).stream().findAny() : original;
    }
}