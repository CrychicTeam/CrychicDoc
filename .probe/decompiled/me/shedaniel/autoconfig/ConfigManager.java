package me.shedaniel.autoconfig;

import java.util.ArrayList;
import java.util.List;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.event.ConfigSerializeEvent;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import net.minecraft.world.InteractionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class ConfigManager<T extends ConfigData> implements ConfigHolder<T> {

    private final Logger logger;

    private final Config definition;

    private final Class<T> configClass;

    private final ConfigSerializer<T> serializer;

    private final List<ConfigSerializeEvent.Save<T>> saveEvent = new ArrayList();

    private final List<ConfigSerializeEvent.Load<T>> loadEvent = new ArrayList();

    private T config;

    ConfigManager(Config definition, Class<T> configClass, ConfigSerializer<T> serializer) {
        this.logger = LogManager.getLogger();
        this.definition = definition;
        this.configClass = configClass;
        this.serializer = serializer;
        if (this.load()) {
            this.save();
        }
    }

    public Config getDefinition() {
        return this.definition;
    }

    @NotNull
    @Override
    public Class<T> getConfigClass() {
        return this.configClass;
    }

    public ConfigSerializer<T> getSerializer() {
        return this.serializer;
    }

    @Override
    public void save() {
        for (ConfigSerializeEvent.Save<T> save : this.saveEvent) {
            InteractionResult result = save.onSave(this, this.config);
            if (result == InteractionResult.FAIL) {
                return;
            }
            if (result != InteractionResult.PASS) {
                break;
            }
        }
        try {
            this.serializer.serialize(this.config);
        } catch (ConfigSerializer.SerializationException var4) {
            this.logger.error("Failed to save config '{}'", this.configClass, var4);
        }
    }

    @Override
    public boolean load() {
        try {
            T deserialized = this.serializer.deserialize();
            for (ConfigSerializeEvent.Load<T> load : this.loadEvent) {
                InteractionResult result = load.onLoad(this, deserialized);
                if (result == InteractionResult.FAIL) {
                    this.config = this.serializer.createDefault();
                    this.config.validatePostLoad();
                    return false;
                }
                if (result != InteractionResult.PASS) {
                    break;
                }
            }
            this.config = deserialized;
            this.config.validatePostLoad();
            return true;
        } catch (ConfigData.ValidationException | ConfigSerializer.SerializationException var5) {
            this.logger.error("Failed to load config '{}', using default!", this.configClass, var5);
            this.resetToDefault();
            return false;
        }
    }

    @Override
    public T getConfig() {
        return this.config;
    }

    @Override
    public void registerLoadListener(ConfigSerializeEvent.Load<T> load) {
        this.loadEvent.add(load);
    }

    @Override
    public void resetToDefault() {
        this.config = this.serializer.createDefault();
        try {
            this.config.validatePostLoad();
        } catch (ConfigData.ValidationException var2) {
            throw new RuntimeException("result of createDefault() was invalid!", var2);
        }
    }

    @Override
    public void setConfig(T config) {
        this.config = config;
    }

    @Override
    public void registerSaveListener(ConfigSerializeEvent.Save<T> save) {
        this.saveEvent.add(save);
    }
}