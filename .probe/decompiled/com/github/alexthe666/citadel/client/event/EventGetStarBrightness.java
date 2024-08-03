package com.github.alexthe666.citadel.client.event;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;

@OnlyIn(Dist.CLIENT)
@HasResult
public class EventGetStarBrightness extends Event {

    private ClientLevel clientLevel;

    private float brightness;

    private float partialTicks;

    public EventGetStarBrightness(ClientLevel clientLevel, float brightness, float partialTicks) {
        this.clientLevel = clientLevel;
        this.brightness = brightness;
        this.partialTicks = partialTicks;
    }

    public ClientLevel getLevel() {
        return this.clientLevel;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public float getBrightness() {
        return this.brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }
}