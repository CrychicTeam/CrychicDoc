package com.github.alexthe666.citadel.client.event;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;

@OnlyIn(Dist.CLIENT)
@HasResult
public class EventGetFluidRenderType extends Event {

    private FluidState fluidState;

    private RenderType renderType;

    public EventGetFluidRenderType(FluidState fluidState, RenderType renderType) {
        this.fluidState = fluidState;
        this.renderType = renderType;
    }

    public FluidState getFluidState() {
        return this.fluidState;
    }

    public RenderType getRenderType() {
        return this.renderType;
    }

    public void setRenderType(RenderType renderType) {
        this.renderType = renderType;
    }
}