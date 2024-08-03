package com.mna.api.events;

import com.mna.api.recipes.IRunescribeRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class RunescribeCraftingEvent extends Event {

    private ItemStack output;

    private Player crafter;

    private IRunescribeRecipe recipe;

    public RunescribeCraftingEvent(IRunescribeRecipe recipe, ItemStack output, Player crafter) {
        this.recipe = recipe;
        this.output = output;
        this.crafter = crafter;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public Player getCrafter() {
        return this.crafter;
    }

    public IRunescribeRecipe getRecipe() {
        return this.recipe;
    }
}