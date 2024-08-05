package com.clientcrafting.mixin;

import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ CraftingMenu.class })
public class CraftingMixin {

    @Unique
    private static boolean queued = false;

    @Shadow
    @Final
    @Mutable
    private ContainerLevelAccess access;

    @Shadow
    @Final
    private Player player;

    @Inject(method = { "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V" }, at = { @At("RETURN") })
    private void onInitContainerAccess(int int0, Inventory inventory1, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
        if (containerLevelAccess == ContainerLevelAccess.NULL) {
            this.access = ContainerLevelAccess.create(this.player.m_9236_(), this.player.m_20183_());
        }
    }

    @Inject(method = { "slotChangedCraftingGrid" }, at = { @At("RETURN") })
    private static void showClientRecipe(AbstractContainerMenu menu, Level level, Player player, CraftingContainer container, ResultContainer resultContainer, CallbackInfo ci) {
        if (level.isClientSide() && !queued) {
            queued = true;
            Minecraft.getInstance().m_18707_(() -> {
                Optional<CraftingRecipe> optional = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, container, level);
                if (optional.isPresent()) {
                    CraftingRecipe craftingrecipe = (CraftingRecipe) optional.get();
                    if (setRecipeUsedClientCheck(level, (LocalPlayer) player, craftingrecipe)) {
                        ItemStack itemstack = craftingrecipe.m_5874_(container, level.registryAccess());
                        resultContainer.setItem(0, itemstack);
                        menu.setRemoteSlot(0, itemstack);
                    }
                } else {
                    resultContainer.setItem(0, ItemStack.EMPTY);
                    menu.setRemoteSlot(0, ItemStack.EMPTY);
                }
                queued = false;
            });
        }
    }

    @Unique
    private static boolean setRecipeUsedClientCheck(Level level, LocalPlayer player, CraftingRecipe craftingrecipe) {
        return craftingrecipe.m_5598_() || !level.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) || player.getRecipeBook().m_12709_(craftingrecipe);
    }
}