package com.mna.api.events;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;

@HasResult
public class RunicAnvilShouldActivateEvent extends Event {

    public final ItemStack pattern;

    public final ItemStack material;

    public RunicAnvilShouldActivateEvent(ItemStack pattern, ItemStack material) {
        this.pattern = pattern.copy();
        this.material = material.copy();
    }
}