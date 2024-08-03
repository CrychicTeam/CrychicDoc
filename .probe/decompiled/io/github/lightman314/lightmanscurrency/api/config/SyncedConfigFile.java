package io.github.lightman314.lightmanscurrency.api.config;

import com.google.common.collect.ImmutableMap;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.config.options.ConfigOption;
import io.github.lightman314.lightmanscurrency.network.message.config.SPacketSyncConfig;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

public abstract class SyncedConfigFile extends ConfigFile {

    private static final Map<ResourceLocation, SyncedConfigFile> fileMap = new HashMap();

    protected final ResourceLocation id;

    private boolean loadedSyncData = false;

    @Override
    public boolean isServerOnly() {
        return true;
    }

    public static void playerJoined(@Nonnull ServerPlayer player) {
        PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> player);
        fileMap.values().forEach(c -> c.sendSyncPacket(target));
    }

    public static void handleSyncData(@Nonnull ResourceLocation configID, @Nonnull Map<String, String> data) {
        if (fileMap.containsKey(configID)) {
            ((SyncedConfigFile) fileMap.get(configID)).loadSyncData(data);
        } else {
            LightmansCurrency.LogError("Received config data for '" + configID + "' from the server, however this config is not present on the client!");
        }
    }

    public static void onClientLeavesServer() {
        fileMap.values().forEach(SyncedConfigFile::clearSyncedData);
    }

    protected SyncedConfigFile(@Nonnull String fileName, @Nonnull ResourceLocation id) {
        super(fileName, ConfigFile.LoadPhase.GAME_START);
        this.id = id;
        if (fileMap.containsKey(this.id)) {
            throw new IllegalArgumentException("Synced Config " + this.id + " already exists!");
        } else {
            fileMap.put(this.id, this);
        }
    }

    @Override
    public boolean isLoaded() {
        return super.isLoaded() || this.loadedSyncData;
    }

    @Override
    protected void afterReload() {
        this.sendSyncPacket(PacketDistributor.ALL.noArg());
    }

    @Override
    protected void afterOptionChanged(@Nonnull ConfigOption<?> option) {
        this.sendSyncPacket(PacketDistributor.ALL.noArg());
    }

    public final void clearSyncedData() {
        this.forEach(ConfigOption::clearSyncedData);
        this.loadedSyncData = false;
    }

    @Nonnull
    private Map<String, String> getSyncData() {
        Map<String, String> map = new HashMap();
        this.getAllOptions().forEach((id, option) -> map.put(id, option.write()));
        return ImmutableMap.copyOf(map);
    }

    private void sendSyncPacket(@Nonnull PacketDistributor.PacketTarget target) {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            new SPacketSyncConfig(this.id, this.getSyncData()).sendToTarget(target);
        }
    }

    private void loadSyncData(@Nonnull Map<String, String> syncData) {
        LightmansCurrency.LogInfo("Received config data for '" + this.id + "' from the server!");
        this.getAllOptions().forEach((id, option) -> {
            if (syncData.containsKey(id)) {
                option.load((String) syncData.get(id), ConfigOption.LoadSource.SYNC);
            } else {
                LightmansCurrency.LogWarning("Received data for config option '" + id + "' but it is not present on the client!");
            }
            this.loadedSyncData = true;
        });
    }
}