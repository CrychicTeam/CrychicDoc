package net.mehvahdjukaar.modelfix.moonlight_configs;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigSpec {

    private static final Map<String, Map<ConfigType, ConfigSpec>> CONFIG_STORAGE = new ConcurrentHashMap();

    private final String fileName;

    private final String modId;

    private final Path filePath;

    private final ConfigType type;

    private final boolean synced;

    @Nullable
    private final Runnable changeCallback;

    public static void addTrackedSpec(ConfigSpec spec) {
        Map<ConfigType, ConfigSpec> map = (Map<ConfigType, ConfigSpec>) CONFIG_STORAGE.computeIfAbsent(spec.getModId(), n -> new HashMap());
        map.put(spec.getConfigType(), spec);
    }

    @Nullable
    public static ConfigSpec getSpec(String modId, ConfigType type) {
        Map<ConfigType, ConfigSpec> map = (Map<ConfigType, ConfigSpec>) CONFIG_STORAGE.get(modId);
        return map != null ? (ConfigSpec) map.getOrDefault(type, null) : null;
    }

    protected ConfigSpec(ResourceLocation name, Path configDirectory, ConfigType type) {
        this(name, configDirectory, type, false, null);
    }

    protected ConfigSpec(ResourceLocation name, Path configDirectory, ConfigType type, boolean synced, @Nullable Runnable changeCallback) {
        this.fileName = name.getNamespace() + "-" + name.getPath() + ".json";
        this.modId = name.getNamespace();
        this.filePath = configDirectory.resolve(this.fileName);
        this.type = type;
        this.synced = synced;
        this.changeCallback = changeCallback;
    }

    protected void onRefresh() {
        if (this.changeCallback != null) {
            this.changeCallback.run();
        }
    }

    public boolean isLoaded() {
        return true;
    }

    public abstract void loadFromFile();

    public abstract void register();

    public ConfigType getConfigType() {
        return this.type;
    }

    public String getModId() {
        return this.modId;
    }

    public boolean isSynced() {
        return this.synced;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Path getFullPath() {
        return this.filePath;
    }

    public abstract void loadFromBytes(InputStream var1);

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public Screen makeScreen(Screen parent) {
        return this.makeScreen(parent, null);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public abstract Screen makeScreen(Screen var1, @Nullable ResourceLocation var2);

    public abstract boolean hasConfigScreen();

    public void syncConfigsToPlayer(ServerPlayer player) {
    }

    public void sendSyncedConfigsToAllPlayers() {
    }
}