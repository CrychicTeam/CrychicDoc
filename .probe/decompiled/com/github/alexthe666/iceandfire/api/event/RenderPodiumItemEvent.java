package com.github.alexthe666.iceandfire.api.event;

import com.github.alexthe666.iceandfire.client.render.tile.RenderPodium;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class RenderPodiumItemEvent extends Event {

    float partialTicks;

    double x;

    double y;

    double z;

    private final RenderPodium<?> render;

    private final TileEntityPodium podium;

    public RenderPodiumItemEvent(RenderPodium<?> renderPodium, TileEntityPodium podium, float partialTicks, double x, double y, double z) {
        this.render = renderPodium;
        this.podium = podium;
        this.partialTicks = partialTicks;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public RenderPodium<?> getRender() {
        return this.render;
    }

    public ItemStack getItemStack() {
        return this.podium.getItem(0);
    }

    public TileEntityPodium getPodium() {
        return this.podium;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}