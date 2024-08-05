package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.FurnaceMenu;

public class FurnaceScreen extends AbstractFurnaceScreen<FurnaceMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");

    public FurnaceScreen(FurnaceMenu furnaceMenu0, Inventory inventory1, Component component2) {
        super(furnaceMenu0, new SmeltingRecipeBookComponent(), inventory1, component2, TEXTURE);
    }
}