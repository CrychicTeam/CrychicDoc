package dev.lambdaurora.lambdynlights.config;

import net.minecraft.network.chat.Component;

public enum QualityMode {

    OFF("Off"), SLOW("Slow"), FAST("Fast"), REALTIME("Realtime");

    private final String name;

    private QualityMode(String name) {
        this.name = name;
    }

    public Component getLocalizedName() {
        return Component.nullToEmpty(this.name);
    }
}