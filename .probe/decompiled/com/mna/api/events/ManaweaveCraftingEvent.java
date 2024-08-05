package com.mna.api.events;

import com.mna.api.recipes.IManaweavingRecipe;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class ManaweaveCraftingEvent extends Event {

    private ItemStack output;

    private Player crafter;

    private IManaweavingRecipe recipe;

    public ManaweaveCraftingEvent(IManaweavingRecipe recipe, ItemStack output, @Nullable Player crafter) {
        this.recipe = recipe;
        this.output = output;
        this.crafter = crafter;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    @Nullable
    public Player getCrafter() {
        return this.crafter;
    }

    public IManaweavingRecipe getRecipe() {
        return this.recipe;
    }
}