package me.shedaniel.autoconfig.event;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.world.InteractionResult;

public final class ConfigSerializeEvent {

    private ConfigSerializeEvent() {
    }

    @FunctionalInterface
    public interface Load<T extends ConfigData> {

        InteractionResult onLoad(ConfigHolder<T> var1, T var2);
    }

    @FunctionalInterface
    public interface Save<T extends ConfigData> {

        InteractionResult onSave(ConfigHolder<T> var1, T var2);
    }
}