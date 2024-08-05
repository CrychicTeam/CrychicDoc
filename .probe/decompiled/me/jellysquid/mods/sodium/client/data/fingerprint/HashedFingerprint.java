package me.jellysquid.mods.sodium.client.data.fingerprint;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Objects;
import me.jellysquid.mods.sodium.client.util.FileUtil;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record HashedFingerprint(@SerializedName("v") int version, @NotNull @SerializedName("s") String saltHex, @NotNull @SerializedName("u") String uuidHashHex, @NotNull @SerializedName("p") String pathHashHex, @SerializedName("t") long timestamp) {

    public static final int CURRENT_VERSION = 1;

    @Nullable
    public static HashedFingerprint loadFromDisk() {
        Path path = getFilePath();
        if (!Files.exists(path, new LinkOption[0])) {
            return null;
        } else {
            HashedFingerprint data;
            try {
                data = (HashedFingerprint) new Gson().fromJson(Files.readString(path), HashedFingerprint.class);
            } catch (IOException var3) {
                throw new RuntimeException("Failed to load data file", var3);
            }
            return data.version() != 1 ? null : data;
        }
    }

    public static void writeToDisk(@NotNull HashedFingerprint data) {
        Objects.requireNonNull(data);
        try {
            FileUtil.writeTextRobustly(new Gson().toJson(data), getFilePath());
        } catch (IOException var2) {
            throw new RuntimeException("Failed to save data file", var2);
        }
    }

    private static Path getFilePath() {
        return FMLPaths.CONFIGDIR.get().resolve("embeddium-fingerprint.json");
    }
}