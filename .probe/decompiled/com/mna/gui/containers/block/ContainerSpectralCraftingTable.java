package com.mna.gui.containers.block;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerSpectralCraftingTable extends CraftingMenu {

    public ContainerSpectralCraftingTable(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, ContainerLevelAccess.NULL);
    }

    public ContainerSpectralCraftingTable(int id, Inventory playerInventory, ContainerLevelAccess p_i50090_3_) {
        super(id, playerInventory, p_i50090_3_);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }
}