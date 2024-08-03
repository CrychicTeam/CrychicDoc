package com.mna.api.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class RunicAnvilItemUsedEvent extends Event {

    public ItemStack pattern;

    public ItemStack material;

    public ItemStack catalyst;

    private final Player crafter;

    public RunicAnvilItemUsedEvent(ItemStack pattern, ItemStack material, ItemStack catalyst, Player crafter) {
        this.pattern = pattern;
        this.material = material;
        this.catalyst = catalyst;
        this.crafter = crafter;
    }

    public boolean isCancelable() {
        return true;
    }

    public Player getPlayer() {
        return this.crafter;
    }
}