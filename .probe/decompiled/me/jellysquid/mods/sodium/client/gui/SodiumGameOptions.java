package me.jellysquid.mods.sodium.client.gui;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.network.chat.Component;
import org.embeddedt.embeddium.config.ConfigMigrator;

public class SodiumGameOptions {

    private static final String DEFAULT_FILE_NAME = "embeddium-options.json";

    public final SodiumGameOptions.QualitySettings quality = new SodiumGameOptions.QualitySettings();

    public final SodiumGameOptions.AdvancedSettings advanced = new SodiumGameOptions.AdvancedSettings();

    public final SodiumGameOptions.PerformanceSettings performance = new SodiumGameOptions.PerformanceSettings();

    public final SodiumGameOptions.NotificationSettings notifications = new SodiumGameOptions.NotificationSettings();

    private boolean readOnly;

    private Path configPath;

    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().excludeFieldsWithModifiers(new int[] { 2 }).create();

    public static SodiumGameOptions defaults() {
        SodiumGameOptions options = new SodiumGameOptions();
        options.configPath = getConfigPath("embeddium-options.json");
        return options;
    }

    public static SodiumGameOptions load() {
        return load("embeddium-options.json");
    }

    public static SodiumGameOptions load(String name) {
        Path path = getConfigPath(name);
        boolean resaveConfig = true;
        SodiumGameOptions config;
        if (Files.exists(path, new LinkOption[0])) {
            try {
                FileReader reader = new FileReader(path.toFile());
                try {
                    config = (SodiumGameOptions) GSON.fromJson(reader, SodiumGameOptions.class);
                } catch (Throwable var9) {
                    try {
                        reader.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                    throw var9;
                }
                reader.close();
            } catch (IOException var10) {
                throw new RuntimeException("Could not parse config", var10);
            } catch (JsonSyntaxException var11) {
                SodiumClientMod.logger().error("Could not parse config, will fallback to default settings", var11);
                config = new SodiumGameOptions();
                resaveConfig = false;
            }
        } else {
            config = new SodiumGameOptions();
        }
        config.configPath = path;
        config.notifications.forceDisableDonationPrompts = false;
        try {
            if (resaveConfig) {
                config.writeChanges();
            }
            return config;
        } catch (IOException var7) {
            throw new RuntimeException("Couldn't update config file", var7);
        }
    }

    private static Path getConfigPath(String name) {
        return ConfigMigrator.handleConfigMigration(name);
    }

    @Deprecated
    public void writeChanges() throws IOException {
        writeToDisk(this);
    }

    public static void writeToDisk(SodiumGameOptions config) throws IOException {
        if (config.isReadOnly()) {
            throw new IllegalStateException("Config file is read-only");
        } else {
            Path dir = config.configPath.getParent();
            if (!Files.exists(dir, new LinkOption[0])) {
                Files.createDirectories(dir);
            } else if (!Files.isDirectory(dir, new LinkOption[0])) {
                throw new IOException("Not a directory: " + dir);
            }
            Path tempPath = config.configPath.resolveSibling(config.configPath.getFileName() + ".tmp");
            Files.writeString(tempPath, GSON.toJson(config));
            Files.move(tempPath, config.configPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly() {
        this.readOnly = true;
    }

    public String getFileName() {
        return this.configPath.getFileName().toString();
    }

    public static class AdvancedSettings {

        public boolean enableMemoryTracing = false;

        public boolean useAdvancedStagingBuffers = true;

        public boolean disableIncompatibleModWarnings = false;

        public int cpuRenderAheadLimit = 3;
    }

    public static enum GraphicsQuality implements TextProvider {

        DEFAULT("options.gamma.default"), FANCY("options.clouds.fancy"), FAST("options.clouds.fast");

        private final Component name;

        private GraphicsQuality(String name) {
            this.name = Component.translatable(name);
        }

        @Override
        public Component getLocalizedName() {
            return this.name;
        }

        public boolean isFancy(GraphicsStatus graphicsMode) {
            return this == FANCY || this == DEFAULT && (graphicsMode == GraphicsStatus.FANCY || graphicsMode == GraphicsStatus.FABULOUS);
        }
    }

    public static class NotificationSettings {

        public boolean forceDisableDonationPrompts = false;

        public boolean hasClearedDonationButton = false;

        public boolean hasSeenDonationPrompt = false;
    }

    public static class PerformanceSettings {

        public int chunkBuilderThreads = 0;

        @SerializedName("always_defer_chunk_updates_v2")
        public boolean alwaysDeferChunkUpdates = true;

        public boolean animateOnlyVisibleTextures = true;

        public boolean useEntityCulling = true;

        public boolean useFogOcclusion = true;

        public boolean useBlockFaceCulling = true;

        public boolean useCompactVertexFormat = true;

        @SerializedName("use_translucent_face_sorting_v2")
        public boolean useTranslucentFaceSorting = true;

        public boolean useNoErrorGLContext = true;
    }

    public static class QualitySettings {

        public SodiumGameOptions.GraphicsQuality weatherQuality = SodiumGameOptions.GraphicsQuality.DEFAULT;

        public SodiumGameOptions.GraphicsQuality leavesQuality = SodiumGameOptions.GraphicsQuality.DEFAULT;

        public boolean enableVignette = true;
    }
}