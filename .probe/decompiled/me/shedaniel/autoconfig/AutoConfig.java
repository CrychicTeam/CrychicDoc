package me.shedaniel.autoconfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.gui.ConfigScreenProvider;
import me.shedaniel.autoconfig.gui.DefaultGuiProviders;
import me.shedaniel.autoconfig.gui.DefaultGuiTransformers;
import me.shedaniel.autoconfig.gui.registry.ComposedGuiRegistryAccess;
import me.shedaniel.autoconfig.gui.registry.DefaultGuiRegistryAccess;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AutoConfig {

    public static final String MOD_ID = "autoconfig1u";

    private static final Map<Class<? extends ConfigData>, ConfigHolder<?>> holders = new HashMap();

    private static final Map<Class<? extends ConfigData>, GuiRegistry> guiRegistries = new HashMap();

    private AutoConfig() {
    }

    public static <T extends ConfigData> ConfigHolder<T> register(Class<T> configClass, ConfigSerializer.Factory<T> serializerFactory) {
        Objects.requireNonNull(configClass);
        Objects.requireNonNull(serializerFactory);
        if (holders.containsKey(configClass)) {
            throw new RuntimeException(String.format("Config '%s' already registered", configClass));
        } else {
            Config definition = (Config) configClass.getAnnotation(Config.class);
            if (definition == null) {
                throw new RuntimeException(String.format("No @Config annotation on %s!", configClass));
            } else {
                ConfigSerializer<T> serializer = serializerFactory.create(definition, configClass);
                ConfigManager<T> manager = new ConfigManager<>(definition, configClass, serializer);
                holders.put(configClass, manager);
                return manager;
            }
        }
    }

    public static <T extends ConfigData> ConfigHolder<T> getConfigHolder(Class<T> configClass) {
        Objects.requireNonNull(configClass);
        if (holders.containsKey(configClass)) {
            return (ConfigHolder<T>) holders.get(configClass);
        } else {
            throw new RuntimeException(String.format("Config '%s' has not been registered", configClass));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static <T extends ConfigData> GuiRegistry getGuiRegistry(Class<T> configClass) {
        return (GuiRegistry) guiRegistries.computeIfAbsent(configClass, n -> new GuiRegistry());
    }

    @OnlyIn(Dist.CLIENT)
    public static <T extends ConfigData> Supplier<Screen> getConfigScreen(Class<T> configClass, Screen parent) {
        return new ConfigScreenProvider<>((ConfigManager<ConfigData>) getConfigHolder(configClass), new ComposedGuiRegistryAccess(getGuiRegistry(configClass), AutoConfig.ClientOnly.defaultGuiRegistry, new DefaultGuiRegistryAccess()), parent);
    }

    @OnlyIn(Dist.CLIENT)
    private static class ClientOnly {

        private static final GuiRegistry defaultGuiRegistry = DefaultGuiTransformers.apply(DefaultGuiProviders.apply(new GuiRegistry()));
    }
}