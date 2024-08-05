package net.minecraftforge.common;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.Nullable;

public final class UsernameCache {

    private static Map<UUID, String> map = new HashMap();

    private static final Path saveFile = FMLLoader.getGamePath().resolve("usernamecache.json");

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final Logger LOGGER = LogManager.getLogger(UsernameCache.class);

    private static final Marker USRCACHE = MarkerManager.getMarker("USERNAMECACHE");

    private UsernameCache() {
    }

    protected static void setUsername(UUID uuid, String username) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(username);
        if (!username.equals(map.get(uuid))) {
            map.put(uuid, username);
            save();
        }
    }

    protected static boolean removeUsername(UUID uuid) {
        Objects.requireNonNull(uuid);
        if (map.remove(uuid) != null) {
            save();
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public static String getLastKnownUsername(UUID uuid) {
        Objects.requireNonNull(uuid);
        return (String) map.get(uuid);
    }

    public static boolean containsUUID(UUID uuid) {
        Objects.requireNonNull(uuid);
        return map.containsKey(uuid);
    }

    public static Map<UUID, String> getMap() {
        return ImmutableMap.copyOf(map);
    }

    protected static void save() {
        new UsernameCache.SaveThread(gson.toJson(map)).start();
    }

    protected static void load() {
        if (Files.exists(saveFile, new LinkOption[0])) {
            try {
                BufferedReader reader = Files.newBufferedReader(saveFile, Charsets.UTF_8);
                try {
                    Type type = (new TypeToken<Map<UUID, String>>() {
                    }).getType();
                    map = (Map<UUID, String>) gson.fromJson(reader, type);
                } catch (Throwable var11) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }
                    }
                    throw var11;
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException | JsonSyntaxException var12) {
                LOGGER.error(USRCACHE, "Could not parse username cache file as valid json, deleting file {}", saveFile, var12);
                try {
                    Files.delete(saveFile);
                } catch (IOException var9) {
                    LOGGER.error(USRCACHE, "Could not delete file {}", saveFile.toString());
                }
            } finally {
                if (map == null) {
                    map = new HashMap();
                }
            }
        }
    }

    private static class SaveThread extends Thread {

        private final String data;

        public SaveThread(String data) {
            this.data = data;
        }

        public void run() {
            try {
                synchronized (UsernameCache.saveFile) {
                    Files.write(UsernameCache.saveFile, this.data.getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
                }
            } catch (IOException var4) {
                UsernameCache.LOGGER.error(UsernameCache.USRCACHE, "Failed to save username cache to file!", var4);
            }
        }
    }
}