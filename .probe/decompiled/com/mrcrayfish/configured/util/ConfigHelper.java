package com.mrcrayfish.configured.util;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.FileWatcher;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.google.common.collect.ImmutableList;
import com.mrcrayfish.configured.api.ConfigType;
import com.mrcrayfish.configured.api.Environment;
import com.mrcrayfish.configured.api.IConfigEntry;
import com.mrcrayfish.configured.api.IConfigValue;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.client.ClientConfigHelper;
import com.mrcrayfish.configured.platform.Services;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.player.Player;

public class ConfigHelper {

    private static final Set<Path> WATCHED_PATHS = new HashSet();

    public static List<IConfigEntry> gatherAllConfigEntries(IConfigEntry entry) {
        List<IConfigEntry> entries = new ObjectArrayList();
        Queue<IConfigEntry> queue = new ArrayDeque(entry.getChildren());
        while (!queue.isEmpty()) {
            IConfigEntry e = (IConfigEntry) queue.poll();
            entries.add(e);
            if (!e.isLeaf()) {
                queue.addAll(e.getChildren());
            }
        }
        return entries;
    }

    public static List<IConfigValue<?>> gatherAllConfigValues(IModConfig config) {
        return gatherAllConfigValues(config.getRoot());
    }

    public static List<IConfigValue<?>> gatherAllConfigValues(IConfigEntry entry) {
        List<IConfigValue<?>> values = new ObjectArrayList();
        gatherValuesFromForgeConfig(entry, values);
        return ImmutableList.copyOf(values);
    }

    private static void gatherValuesFromForgeConfig(IConfigEntry entry, List<IConfigValue<?>> values) {
        if (entry.isLeaf()) {
            IConfigValue<?> value = entry.getValue();
            if (value != null) {
                values.add(value);
            }
        } else {
            for (IConfigEntry children : entry.getChildren()) {
                gatherValuesFromForgeConfig(children, values);
            }
        }
    }

    public static boolean isWorldConfig(IModConfig config) {
        return config.getType() == ConfigType.WORLD || config.getType() == ConfigType.WORLD_SYNC;
    }

    public static boolean isServerConfig(IModConfig config) {
        return config.getType().isServer() && !isWorldConfig(config);
    }

    public static boolean isConfiguredInstalledOnServer() {
        ClientPacketListener listener = Minecraft.getInstance().getConnection();
        return listener != null && Services.PLATFORM.isConnectionActive(listener);
    }

    public static Set<IConfigValue<?>> getChangedValues(IConfigEntry entry) {
        Set<IConfigValue<?>> changed = new HashSet();
        Queue<IConfigEntry> found = new ArrayDeque();
        found.add(entry);
        while (!found.isEmpty()) {
            IConfigEntry toSave = (IConfigEntry) found.poll();
            if (!toSave.isLeaf()) {
                found.addAll(toSave.getChildren());
            } else {
                IConfigValue<?> value = toSave.getValue();
                if (value != null && value.isChanged()) {
                    changed.add(value);
                }
            }
        }
        return changed;
    }

    public static boolean isPlayingGame() {
        return Services.PLATFORM.getEnvironment() != Environment.CLIENT ? false : ClientConfigHelper.isPlayingGame();
    }

    public static boolean isServerOwnedByPlayer(@Nullable Player player) {
        return player != null && player.m_20194_() != null && !player.m_20194_().isDedicatedServer() && player.m_20194_().isSingleplayerOwner(player.getGameProfile());
    }

    public static boolean hasPermissionToEdit(@Nullable Player player, IModConfig config) {
        return !config.getType().isServer() || player != null && (player.m_20310_(4) || isServerOwnedByPlayer(player));
    }

    public static boolean isOperator(@Nullable Player player) {
        return player != null && player.m_20310_(4);
    }

    public static void createBackup(UnmodifiableConfig config) {
        if (config instanceof FileConfig fileConfig) {
            try {
                Path configPath = fileConfig.getNioPath();
                if (Files.exists(configPath, new LinkOption[0]) && fileConfig.getFile().length() > 0L) {
                    Path backupPath = configPath.getParent().resolve(fileConfig.getFile().getName() + ".bak");
                    Files.copy(configPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        }
    }

    public static void closeConfig(UnmodifiableConfig config) {
        if (config instanceof FileConfig fileConfig) {
            Path path = fileConfig.getNioPath();
            if (WATCHED_PATHS.contains(path)) {
                FileWatcher.defaultInstance().removeWatch(path);
                WATCHED_PATHS.remove(path);
            }
            fileConfig.close();
        }
    }

    public static void loadConfig(UnmodifiableConfig config) {
        if (config instanceof FileConfig fileConfig) {
            try {
                fileConfig.load();
            } catch (Exception var3) {
            }
        }
    }

    public static void saveConfig(UnmodifiableConfig config) {
        if (config instanceof FileConfig fileConfig) {
            fileConfig.save();
        }
    }

    public static void watchConfig(UnmodifiableConfig config, Runnable callback) {
        if (config instanceof FileConfig fileConfig) {
            try {
                Path path = fileConfig.getNioPath();
                WATCHED_PATHS.add(path);
                FileWatcher.defaultInstance().setWatch(path, callback);
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        }
    }

    public static byte[] readBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static byte[] getBytes(UnmodifiableConfig config) {
        if (config instanceof FileConfig fc) {
            return readBytes(fc.getNioPath());
        } else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            TomlFormat.instance().createWriter().write(config, stream);
            return stream.toByteArray();
        }
    }

    public static Player getClientPlayer() {
        return Services.PLATFORM.getEnvironment() != Environment.CLIENT ? null : ClientConfigHelper.getClientPlayer();
    }

    public static boolean isRunningLocalServer() {
        return Services.PLATFORM.getEnvironment() != Environment.CLIENT ? false : ClientConfigHelper.isRunningLocalServer();
    }
}