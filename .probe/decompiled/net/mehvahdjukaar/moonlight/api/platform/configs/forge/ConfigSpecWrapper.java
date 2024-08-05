package net.mehvahdjukaar.moonlight.api.platform.configs.forge;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ConfigFileTypeHandler;
import net.minecraftforge.fml.config.IConfigEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

public final class ConfigSpecWrapper extends ConfigSpec {

    private static final Method SET_CONFIG_DATA = ObfuscationReflectionHelper.findMethod(ModConfig.class, "setConfigData", new Class[] { CommentedConfig.class });

    private static final Method SETUP_CONFIG_FILE = ObfuscationReflectionHelper.findMethod(ConfigFileTypeHandler.class, "setupConfigFile", new Class[] { ModConfig.class, Path.class, ConfigFormat.class });

    private final ForgeConfigSpec spec;

    private final ModConfig modConfig;

    private final ModContainer modContainer;

    private final Map<ForgeConfigSpec.ConfigValue<?>, Object> requireRestartValues;

    private final List<ConfigBuilderImpl.SpecialValue<?, ?>> specialValues;

    public ConfigSpecWrapper(ResourceLocation name, ForgeConfigSpec spec, ConfigType type, boolean synced, @Nullable Runnable onChange, List<ForgeConfigSpec.ConfigValue<?>> requireRestart, List<ConfigBuilderImpl.SpecialValue<?, ?>> specialValues) {
        super(name.getNamespace(), name.getNamespace() + "-" + name.getPath() + ".toml", FMLPaths.CONFIGDIR.get(), type, synced, onChange);
        this.spec = spec;
        this.specialValues = specialValues;
        Type t = this.getConfigType() == ConfigType.COMMON ? Type.COMMON : Type.CLIENT;
        this.modContainer = ModLoadingContext.get().getActiveContainer();
        this.modConfig = new ModConfig(t, spec, this.modContainer, this.getFileName());
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (onChange != null || this.isSynced() || !specialValues.isEmpty()) {
            bus.addListener(this::onConfigChange);
        }
        if (this.isSynced()) {
            MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
            MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedOut);
        }
        ConfigSpec.addTrackedSpec(this);
        if (!requireRestart.isEmpty()) {
            this.loadFromFile();
        }
        this.requireRestartValues = (Map<ForgeConfigSpec.ConfigValue<?>, Object>) requireRestart.stream().collect(Collectors.toMap(e -> e, ForgeConfigSpec.ConfigValue::get));
    }

    @Override
    public Component getName() {
        return Component.literal(this.getFileName());
    }

    @Override
    public Path getFullPath() {
        return FMLPaths.CONFIGDIR.get().resolve(this.getFileName());
    }

    @Override
    public void register() {
        ModContainer modContainer = ModLoadingContext.get().getActiveContainer();
        modContainer.addConfig(this.modConfig);
    }

    @Override
    public void loadFromFile() {
        try {
            CommentedFileConfig configData = this.readConfig(this.modConfig.getHandler(), FMLPaths.CONFIGDIR.get(), this.modConfig);
            SET_CONFIG_DATA.setAccessible(true);
            SET_CONFIG_DATA.invoke(this.modConfig, configData);
            this.modContainer.dispatchConfigEvent(IConfigEvent.loading(this.modConfig));
            this.modConfig.save();
        } catch (Exception var2) {
            throw new ConfigSpecWrapper.ConfigLoadingException(this.modConfig, var2);
        }
    }

    private CommentedFileConfig readConfig(ConfigFileTypeHandler handler, Path configBasePath, ModConfig c) {
        Path configPath = configBasePath.resolve(c.getFileName());
        CommentedFileConfig configData = (CommentedFileConfig) CommentedFileConfig.builder(configPath).sync().preserveInsertionOrder().autosave().onFileNotFound((newfile, configFormat) -> {
            try {
                return (Boolean) SETUP_CONFIG_FILE.invoke(handler, c, newfile, configFormat);
            } catch (Exception var5x) {
                throw new ConfigSpecWrapper.ConfigLoadingException(c, var5x);
            }
        }).writingMode(WritingMode.REPLACE).build();
        configData.load();
        return configData;
    }

    public ForgeConfigSpec getSpec() {
        return this.spec;
    }

    @Nullable
    public ModConfig getModConfig() {
        return this.modConfig;
    }

    public Type getModConfigType() {
        return this.getConfigType() == ConfigType.CLIENT ? Type.CLIENT : Type.COMMON;
    }

    @Override
    public boolean isLoaded() {
        return this.spec.isLoaded();
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public Screen makeScreen(Screen parent, @Nullable ResourceLocation background) {
        Optional<? extends ModContainer> container = ModList.get().getModContainerById(this.getModId());
        if (container.isPresent()) {
            Optional<ConfigScreenHandler.ConfigScreenFactory> factory = ((ModContainer) container.get()).getCustomExtension(ConfigScreenHandler.ConfigScreenFactory.class);
            if (factory.isPresent()) {
                return (Screen) ((ConfigScreenHandler.ConfigScreenFactory) factory.get()).screenFunction().apply(Minecraft.getInstance(), parent);
            }
        }
        return null;
    }

    @Override
    public boolean hasConfigScreen() {
        return (Boolean) ModList.get().getModContainerById(this.getModId()).map(container -> container.getCustomExtension(ConfigScreenHandler.ConfigScreenFactory.class).isPresent()).orElse(false);
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            this.syncConfigsToPlayer(serverPlayer);
        }
    }

    protected void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity().m_9236_().isClientSide) {
            this.onRefresh();
        }
    }

    protected void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == this.getSpec()) {
            if (this.isSynced() && PlatHelper.getPhysicalSide().isServer()) {
                this.sendSyncedConfigsToAllPlayers();
            }
            this.onRefresh();
            this.specialValues.forEach(ConfigBuilderImpl.SpecialValue::clearCache);
        }
    }

    @Override
    public void loadFromBytes(InputStream stream) {
        try {
            byte[] b = stream.readAllBytes();
            this.modConfig.acceptSyncedConfig(b);
        } catch (Exception var3) {
            Moonlight.LOGGER.warn("Failed to sync config file {}:", this.getFileName(), var3);
        }
    }

    public boolean requiresGameRestart(ForgeConfigSpec.ConfigValue<?> value) {
        Object v = this.requireRestartValues.get(value);
        return v == null ? false : v != value.get();
    }

    private static class ConfigLoadingException extends RuntimeException {

        public ConfigLoadingException(ModConfig config, Exception cause) {
            super("Failed loading config file " + config.getFileName() + " of type " + config.getType() + " for modid " + config.getModId() + ". Try deleting it", cause);
        }
    }
}