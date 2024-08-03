package com.illusivesoulworks.polymorph.mixin.core;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.common.crafting.RecipeSelection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At.Shift;

@Mixin({ SmithingMenu.class })
public abstract class MixinSmithingMenu extends ItemCombinerMenu {

    @Unique
    private List<SmithingRecipe> matchingRecipes;

    @Shadow
    private SmithingRecipe selectedRecipe;

    public MixinSmithingMenu(@Nullable MenuType<?> p_i231587_1_, int p_i231587_2_, Inventory p_i231587_3_, ContainerLevelAccess p_i231587_4_) {
        super(p_i231587_1_, p_i231587_2_, p_i231587_3_, p_i231587_4_);
    }

    @ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/world/item/crafting/RecipeManager.getRecipesFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/List;"), method = { "createResult" })
    private List<SmithingRecipe> polymorph$getRecipes(List<SmithingRecipe> recipes) {
        this.matchingRecipes = recipes;
        if (this.f_39771_ instanceof ServerPlayer && recipes.isEmpty()) {
            PolymorphApi.common().getPacketDistributor().sendRecipesListS2C((ServerPlayer) this.f_39771_);
        }
        return recipes;
    }

    @ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "java/util/List.get(I)Ljava/lang/Object;", shift = Shift.BY, by = 3), method = { "createResult" })
    private SmithingRecipe polymorph$updateRepairOutput(SmithingRecipe smithingRecipe) {
        return (SmithingRecipe) RecipeSelection.getPlayerRecipe((SmithingMenu) this, RecipeType.SMITHING, this.f_39769_, this.f_39771_.m_9236_(), this.f_39771_, this.matchingRecipes).orElse(smithingRecipe);
    }
}