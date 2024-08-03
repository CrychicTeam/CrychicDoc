package net.blay09.mods.balm.api.config;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.balm.api.network.SyncConfigMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBalmConfig implements BalmConfig {

    private final Map<Class<?>, BalmConfigData> activeConfigs = new HashMap();

    private final Map<Class<?>, BalmConfigData> defaultConfigs = new HashMap();

    private final Map<Class<?>, Function<?, ?>> syncMessageFactories = new HashMap();

    public void initialize() {
        Balm.getEvents().onEvent(PlayerLoginEvent.class, event -> {
            for (BalmConfigData config : this.activeConfigs.values()) {
                SyncConfigMessage<? extends BalmConfigData> message = this.getConfigSyncMessage(config.getClass());
                if (message != null) {
                    Balm.getNetworking().sendTo(event.getPlayer(), message);
                }
            }
        });
        Balm.getEvents().onEvent(ConfigReloadedEvent.class, event -> {
            MinecraftServer server = Balm.getHooks().getServer();
            if (server != null) {
                for (BalmConfigData config : this.activeConfigs.values()) {
                    SyncConfigMessage<? extends BalmConfigData> message = this.getConfigSyncMessage(config.getClass());
                    if (message != null) {
                        Balm.getNetworking().sendToAll(server, message);
                    }
                }
            }
        });
    }

    @Override
    public <T extends BalmConfigData> T getActive(Class<T> clazz) {
        return (T) this.activeConfigs.get(clazz);
    }

    public <T extends BalmConfigData> SyncConfigMessage<T> getConfigSyncMessage(Class<T> clazz) {
        Function<BalmConfigData, SyncConfigMessage<BalmConfigData>> factory = this.getConfigSyncMessageFactory(clazz);
        return factory != null ? (SyncConfigMessage) factory.apply(this.getBackingConfig(clazz)) : null;
    }

    public <T extends BalmConfigData> Function<BalmConfigData, SyncConfigMessage<BalmConfigData>> getConfigSyncMessageFactory(Class<T> clazz) {
        return (Function<BalmConfigData, SyncConfigMessage<BalmConfigData>>) this.syncMessageFactories.get(clazz);
    }

    public <T extends BalmConfigData> void setActiveConfig(Class<T> clazz, T config) {
        this.activeConfigs.put(clazz, config);
    }

    @Override
    public <T extends BalmConfigData> void handleSync(Player player, SyncConfigMessage<T> message) {
        T data = (T) message.getData();
        this.setActiveConfig(data.getClass(), data);
    }

    @Override
    public <T extends BalmConfigData> void registerConfig(Class<T> clazz, Function<T, SyncConfigMessage<T>> syncMessageFactory) {
        this.setActiveConfig(clazz, Balm.getConfig().initializeBackingConfig(clazz));
        this.defaultConfigs.put(clazz, (BalmConfigData) this.createConfigDataInstance(clazz));
        if (syncMessageFactory != null) {
            this.registerSyncMessageFactory(clazz, syncMessageFactory);
        }
    }

    private <T> void registerSyncMessageFactory(Class<T> clazz, Function<T, SyncConfigMessage<T>> syncMessageFactory) {
        this.syncMessageFactories.put(clazz, syncMessageFactory);
    }

    @Override
    public <T extends BalmConfigData> void updateConfig(Class<T> clazz, Consumer<T> consumer) {
        T backingConfig = this.getBackingConfig(clazz);
        consumer.accept(backingConfig);
        Balm.getConfig().saveBackingConfig(clazz);
        T activeConfig = this.getActive(clazz);
        if (activeConfig != backingConfig) {
            consumer.accept(this.getActive(clazz));
        }
    }

    @Override
    public <T extends BalmConfigData> void resetToBackingConfig(Class<T> clazz) {
        this.setActiveConfig(clazz, this.getBackingConfig(clazz));
    }

    @Override
    public void resetToBackingConfigs() {
        for (Class<?> clazz : this.activeConfigs.keySet()) {
            this.resetToBackingConfig(clazz);
        }
    }

    @NotNull
    protected <T> T createConfigDataInstance(Class<T> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException var3) {
            throw new IllegalArgumentException("Config class or sub-class missing a public no-arg constructor.", var3);
        }
    }

    @Override
    public File getConfigFile(String configName) {
        return new File(this.getConfigDir(), configName + "-common.toml");
    }

    @Override
    public <T extends BalmConfigData> String getConfigName(Class<T> clazz) {
        Config configAnnotation = (Config) clazz.getAnnotation(Config.class);
        if (configAnnotation == null) {
            throw new IllegalArgumentException("Config class " + clazz.getName() + " is missing @Config annotation");
        } else {
            return configAnnotation.value();
        }
    }

    @Override
    public <T extends BalmConfigData> Table<String, String, BalmConfigProperty<?>> getConfigProperties(Class<T> clazz) {
        T backingConfig = Balm.getConfig().getBackingConfig(clazz);
        BalmConfigData defaultConfig = (BalmConfigData) this.defaultConfigs.get(clazz);
        Table<String, String, BalmConfigProperty<?>> properties = HashBasedTable.create();
        for (Field rootField : clazz.getFields()) {
            String category = "";
            Class<?> fieldType = rootField.getType();
            if (isPropertyType(fieldType)) {
                String property = rootField.getName();
                properties.put(category, property, createConfigProperty(backingConfig, null, rootField, defaultConfig));
            } else {
                category = rootField.getName();
                for (Field propertyField : fieldType.getFields()) {
                    String property = propertyField.getName();
                    properties.put(category, property, createConfigProperty(backingConfig, rootField, propertyField, defaultConfig));
                }
            }
        }
        return properties;
    }

    private static BalmConfigProperty<?> createConfigProperty(BalmConfigData configData, Field categoryField, Field propertyField, BalmConfigData defaultConfig) {
        return new BalmConfigPropertyImpl(configData, categoryField, propertyField, defaultConfig);
    }

    private static boolean isPropertyType(Class<?> type) {
        return type.isPrimitive() || type == String.class || type == Integer.class || type == Boolean.class || type == Float.class || type == Double.class || type == List.class || Enum.class.isAssignableFrom(type);
    }
}