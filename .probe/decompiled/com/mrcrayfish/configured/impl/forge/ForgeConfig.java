package com.mrcrayfish.configured.impl.forge;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.mrcrayfish.configured.Constants;
import com.mrcrayfish.configured.api.ConfigType;
import com.mrcrayfish.configured.api.IConfigEntry;
import com.mrcrayfish.configured.api.IConfigValue;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.client.SessionData;
import com.mrcrayfish.configured.network.ForgeNetwork;
import com.mrcrayfish.configured.network.message.play.MessageSyncForgeConfig;
import com.mrcrayfish.configured.util.ConfigHelper;
import com.mrcrayfish.configured.util.ForgeConfigHelper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class ForgeConfig implements IModConfig {

    protected static final EnumMap<Type, ConfigType> TYPE_RESOLVER = Util.make(new EnumMap(Type.class), map -> {
        map.put(Type.CLIENT, ConfigType.CLIENT);
        map.put(Type.COMMON, ConfigType.UNIVERSAL);
        map.put(Type.SERVER, ConfigType.WORLD_SYNC);
    });

    protected final ModConfig config;

    protected final ForgeConfigSpec spec;

    protected final List<ForgeConfig.ForgeValueEntry> allConfigValues;

    public ForgeConfig(ModConfig config, ForgeConfigSpec spec) {
        Objects.requireNonNull(spec, "ForgeConfigSpec cannot be null");
        this.config = config;
        this.spec = spec;
        this.allConfigValues = getAllConfigValues(this.spec);
    }

    @Override
    public void update(IConfigEntry entry) {
        Set<IConfigValue<?>> changedValues = ConfigHelper.getChangedValues(entry);
        if (!changedValues.isEmpty()) {
            CommentedConfig newConfig = CommentedConfig.copy(this.config.getConfigData());
            changedValues.forEach(value -> {
                if (value instanceof ForgeValue<?> forge) {
                    if (forge instanceof ForgeListValue<?> forgeList) {
                        List<?> converted = forgeList.getConverted();
                        if (converted != null) {
                            newConfig.set(forge.configValue.getPath(), converted);
                            return;
                        }
                    }
                    newConfig.set(forge.configValue.getPath(), value.get());
                }
            });
            this.config.getConfigData().putAll(newConfig);
        }
        if (this.getType() == ConfigType.WORLD_SYNC) {
            if (!ConfigHelper.isPlayingGame()) {
                this.config.getHandler().unload(this.config.getFullPath().getParent(), this.config);
                ForgeConfigHelper.setForgeConfigData(this.config, null);
            } else {
                this.syncToServer();
            }
        } else if (!changedValues.isEmpty()) {
            Constants.LOG.info("Sending config reloading event for {}", this.config.getFileName());
            this.spec.afterReload();
            ForgeConfigHelper.fireForgeConfigEvent(this.config, new ModConfigEvent.Reloading(this.config));
        }
    }

    @Override
    public IConfigEntry getRoot() {
        return new ForgeFolderEntry(this.spec.getValues(), this.spec);
    }

    @Override
    public ConfigType getType() {
        return (ConfigType) TYPE_RESOLVER.get(this.config.getType());
    }

    @Override
    public String getFileName() {
        return this.config.getFileName();
    }

    @Override
    public String getModId() {
        return this.config.getModId();
    }

    @Override
    public void loadWorldConfig(Path path, Consumer<IModConfig> result) {
        CommentedFileConfig data = (CommentedFileConfig) this.config.getHandler().reader(path).apply(this.config);
        ForgeConfigHelper.setForgeConfigData(this.config, data);
        result.accept(this);
    }

    @Override
    public void stopEditing() {
        if (this.config != null && this.getType() == ConfigType.WORLD && !ConfigHelper.isPlayingGame()) {
            this.config.getHandler().unload(this.config.getFullPath().getParent(), this.config);
            ForgeConfigHelper.setForgeConfigData(this.config, null);
        }
    }

    @Override
    public boolean isChanged() {
        return ConfigHelper.isWorldConfig(this) && this.config.getConfigData() == null ? false : this.allConfigValues.stream().anyMatch(entry -> !Objects.equals(entry.value.get(), entry.spec.getDefault()));
    }

    @Override
    public void restoreDefaults() {
        if (!ConfigHelper.isWorldConfig(this) || this.config.getConfigData() != null) {
            CommentedConfig newConfig = CommentedConfig.copy(this.config.getConfigData());
            this.allConfigValues.forEach(entry -> newConfig.set(entry.value.getPath(), entry.spec.getDefault()));
            this.config.getConfigData().putAll(newConfig);
            this.allConfigValues.forEach(pair -> pair.value.clearCache());
        }
    }

    private void syncToServer() {
        if (this.config != null) {
            if (ConfigHelper.isPlayingGame()) {
                if (ConfigHelper.isConfiguredInstalledOnServer()) {
                    if (this.getType() == ConfigType.WORLD_SYNC) {
                        Player player = ConfigHelper.getClientPlayer();
                        if (ConfigHelper.isOperator(player) && SessionData.isDeveloper(player)) {
                            try {
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                TomlFormat.instance().createWriter().write(this.config.getConfigData(), stream);
                                ForgeNetwork.getPlay().sendToServer(new MessageSyncForgeConfig(this.config.getFileName(), stream.toByteArray()));
                                stream.close();
                            } catch (IOException var3) {
                                Constants.LOG.error("Failed to close byte stream when sending config to server");
                            }
                        }
                    }
                }
            }
        }
    }

    protected static List<ForgeConfig.ForgeValueEntry> getAllConfigValues(ForgeConfigSpec spec) {
        return ForgeConfigHelper.gatherAllForgeConfigValues(spec.getValues(), spec).stream().map(pair -> new ForgeConfig.ForgeValueEntry((ForgeConfigSpec.ConfigValue<?>) pair.getLeft(), (ForgeConfigSpec.ValueSpec) pair.getRight())).toList();
    }

    protected static record ForgeValueEntry(ForgeConfigSpec.ConfigValue<?> value, ForgeConfigSpec.ValueSpec spec) {
    }
}