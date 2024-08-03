package net.mehvahdjukaar.modelfix.moonlight_configs.forge;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import javax.annotation.Nullable;
import net.mehvahdjukaar.modelfix.moonlight_configs.ConfigSpec;
import net.mehvahdjukaar.modelfix.moonlight_configs.ConfigType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
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
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

public class ConfigSpecWrapper extends ConfigSpec {

    private final ForgeConfigSpec spec;

    private final ModConfig modConfig;

    public ConfigSpecWrapper(ResourceLocation name, ForgeConfigSpec spec, ConfigType type, boolean synced, @Nullable Runnable onChange) {
        super(name, FMLPaths.CONFIGDIR.get(), type, synced, onChange);
        this.spec = spec;
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (onChange != null || this.isSynced()) {
            bus.addListener(this::onConfigChange);
        }
        if (this.isSynced()) {
            MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
            MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedOut);
        }
        Type t = this.getConfigType() == ConfigType.COMMON ? Type.COMMON : Type.CLIENT;
        ModContainer modContainer = ModLoadingContext.get().getActiveContainer();
        this.modConfig = new ModConfig(t, spec, modContainer, name.getNamespace() + "-" + name.getPath() + ".toml");
        ConfigSpec.addTrackedSpec(this);
    }

    @Override
    public String getFileName() {
        return this.modConfig.getFileName();
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
            CommentedFileConfig configFile = (CommentedFileConfig) CommentedFileConfig.builder(this.getFullPath()).sync().preserveInsertionOrder().writingMode(WritingMode.REPLACE).build();
            configFile.load();
            configFile.save();
            this.spec.setConfig(configFile);
        } catch (Exception var2) {
            throw new RuntimeException(new IOException("Failed to load " + this.getFileName() + " config. Try deleting it: " + var2));
        }
    }

    public ForgeConfigSpec getSpec() {
        return this.spec;
    }

    @org.jetbrains.annotations.Nullable
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
    @org.jetbrains.annotations.Nullable
    @Override
    public Screen makeScreen(Screen parent, @org.jetbrains.annotations.Nullable ResourceLocation background) {
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

    protected void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
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
            if (this.isSynced()) {
                this.sendSyncedConfigsToAllPlayers();
            }
            this.onRefresh();
        }
    }

    @Override
    public void loadFromBytes(InputStream stream) {
        this.getSpec().setConfig((CommentedConfig) TomlFormat.instance().createParser().parse(stream));
        this.onRefresh();
    }
}