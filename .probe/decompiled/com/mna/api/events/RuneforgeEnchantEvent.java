package com.mna.api.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class RuneforgeEnchantEvent extends Event {

    private ItemStack output;

    private Player crafter;

    public RuneforgeEnchantEvent(ItemStack output, Player crafter) {
        this.output = output;
        this.crafter = crafter;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public Player getCrafter() {
        return this.crafter;
    }
}