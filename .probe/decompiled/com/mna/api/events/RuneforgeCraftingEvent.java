package com.mna.api.events;

import com.mna.api.recipes.IRuneforgeRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class RuneforgeCraftingEvent extends Event {

    private ItemStack output;

    private Player crafter;

    private IRuneforgeRecipe recipe;

    public RuneforgeCraftingEvent(IRuneforgeRecipe recipe, ItemStack output, Player crafter) {
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

    public IRuneforgeRecipe getRecipe() {
        return this.recipe;
    }
}