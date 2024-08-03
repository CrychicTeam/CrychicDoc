package net.blay09.mods.balm.api.config;

import com.google.common.collect.Table;
import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;
import net.blay09.mods.balm.api.network.SyncConfigMessage;
import net.minecraft.world.entity.player.Player;

public interface BalmConfig {

    <T extends BalmConfigData> T initializeBackingConfig(Class<T> var1);

    <T extends BalmConfigData> T getBackingConfig(Class<T> var1);

    <T extends BalmConfigData> void saveBackingConfig(Class<T> var1);

    <T extends BalmConfigData> T getActive(Class<T> var1);

    <T extends BalmConfigData> void handleSync(Player var1, SyncConfigMessage<T> var2);

    <T extends BalmConfigData> void registerConfig(Class<T> var1, Function<T, SyncConfigMessage<T>> var2);

    <T extends BalmConfigData> void updateConfig(Class<T> var1, Consumer<T> var2);

    <T extends BalmConfigData> void resetToBackingConfig(Class<T> var1);

    void resetToBackingConfigs();

    File getConfigDir();

    File getConfigFile(String var1);

    <T extends BalmConfigData> Table<String, String, BalmConfigProperty<?>> getConfigProperties(Class<T> var1);

    <T extends BalmConfigData> String getConfigName(Class<T> var1);
}