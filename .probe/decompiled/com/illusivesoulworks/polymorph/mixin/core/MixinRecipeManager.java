package com.illusivesoulworks.polymorph.mixin.core;

import com.illusivesoulworks.polymorph.common.crafting.RecipeSelection;
import com.mojang.datafixers.util.Pair;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { RecipeManager.class }, priority = 900)
public class MixinRecipeManager {

    @Inject(at = { @At("HEAD") }, method = { "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;" }, cancellable = true)
    private <C extends Container, T extends Recipe<C>> void polymorph$getRecipe(RecipeType<T> recipeType, C inventory, Level level, ResourceLocation resourceLocation, CallbackInfoReturnable<Optional<Pair<ResourceLocation, T>>> cb) {
        if (inventory instanceof BlockEntity) {
            RecipeSelection.getBlockEntityRecipe(recipeType, inventory, level, (BlockEntity) inventory).ifPresent(recipe -> cb.setReturnValue(Optional.of(Pair.of(resourceLocation, recipe))));
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;" }, cancellable = true)
    private <C extends Container, T extends Recipe<C>> void polymorph$getRecipe(RecipeType<T> recipeType, C inventory, Level level, CallbackInfoReturnable<Optional<T>> cb) {
        if (inventory instanceof BlockEntity) {
            RecipeSelection.getBlockEntityRecipe(recipeType, inventory, level, (BlockEntity) inventory).ifPresent(recipe -> cb.setReturnValue(Optional.of(recipe)));
        }
    }
}