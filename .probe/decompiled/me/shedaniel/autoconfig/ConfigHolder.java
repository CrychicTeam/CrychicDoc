package me.shedaniel.autoconfig;

import java.util.function.Supplier;
import me.shedaniel.autoconfig.event.ConfigSerializeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@NonExtendable
public interface ConfigHolder<T extends ConfigData> extends Supplier<T> {

    @NotNull
    Class<T> getConfigClass();

    void save();

    boolean load();

    T getConfig();

    void registerSaveListener(ConfigSerializeEvent.Save<T> var1);

    void registerLoadListener(ConfigSerializeEvent.Load<T> var1);

    default T get() {
        return this.getConfig();
    }

    void resetToDefault();

    void setConfig(T var1);
}