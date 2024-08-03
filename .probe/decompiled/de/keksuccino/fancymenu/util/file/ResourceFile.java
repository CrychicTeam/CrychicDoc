package de.keksuccino.fancymenu.util.file;

import com.google.common.io.Files;
import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.util.file.type.FileMediaType;
import de.keksuccino.fancymenu.util.file.type.FileType;
import de.keksuccino.fancymenu.util.file.type.types.FileTypes;
import de.keksuccino.fancymenu.util.resource.ResourceSourceType;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceFile {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final File ASSETS_DIR = new File(FancyMenu.MOD_DIR, "/assets");

    protected String shortPath;

    protected File file;

    protected FileType<?> type;

    @Nullable
    protected ResourceSourceType resourceSourceType;

    @Nullable
    public static ResourceFile asset(@NotNull File gameDirectoryFile) {
        return asset(gameDirectoryFile.getAbsolutePath());
    }

    @Nullable
    public static ResourceFile asset(@NotNull String gameDirectoryFilePath) {
        ResourceFile resourceFile = of(gameDirectoryFilePath);
        if (!resourceFile.isExistingAsset()) {
            LOGGER.error("[FANCYMENU] Asset ResourceFile does not exist or is not in '/config/fancymenu/assets/': " + gameDirectoryFilePath);
            return null;
        } else {
            return resourceFile;
        }
    }

    @NotNull
    public static ResourceFile of(@NotNull File gameDirectoryFile) {
        return of(gameDirectoryFile.getAbsolutePath());
    }

    @NotNull
    public static ResourceFile of(@NotNull String gameDirectoryFilePath) {
        ResourceFile resourceFile = new ResourceFile();
        if (ResourceSourceType.hasSourcePrefix(gameDirectoryFilePath)) {
            resourceFile.resourceSourceType = ResourceSourceType.getSourceTypeOf(gameDirectoryFilePath);
        }
        gameDirectoryFilePath = ResourceSourceType.getWithoutSourcePrefix(gameDirectoryFilePath);
        gameDirectoryFilePath = gameDirectoryFilePath.replace("\\", "/");
        gameDirectoryFilePath = GameDirectoryUtils.getPathWithoutGameDirectory(gameDirectoryFilePath).replace("\\", "/");
        if (!gameDirectoryFilePath.startsWith("/")) {
            gameDirectoryFilePath = "/" + gameDirectoryFilePath;
        }
        if (gameDirectoryFilePath.replace(" ", "").replace("/", "").isEmpty()) {
            gameDirectoryFilePath = "";
        }
        if (gameDirectoryFilePath.startsWith("/./")) {
            gameDirectoryFilePath = gameDirectoryFilePath.substring(2);
        }
        resourceFile.file = new File(GameDirectoryUtils.getGameDirectory(), gameDirectoryFilePath);
        resourceFile.shortPath = gameDirectoryFilePath;
        resourceFile.type = FileTypes.getLocalType(resourceFile.file);
        if (resourceFile.type == null) {
            resourceFile.type = FileTypes.UNKNOWN;
        }
        if (resourceFile.resourceSourceType == null) {
            resourceFile.resourceSourceType = ResourceSourceType.getSourceTypeOf(gameDirectoryFilePath);
        }
        return resourceFile;
    }

    protected ResourceFile() {
    }

    @Nullable
    public ResourceSourceType getResourceSourceType() {
        return this.resourceSourceType;
    }

    @NotNull
    public String getAsResourceSource() {
        String prefix = this.resourceSourceType != null ? this.resourceSourceType.getSourcePrefix() : "";
        return prefix + this.getShortPath();
    }

    @NotNull
    public String getShortPath() {
        return this.shortPath;
    }

    @NotNull
    public String getAbsolutePath() {
        return this.file.getAbsolutePath();
    }

    @NotNull
    public File getFile() {
        return this.file;
    }

    @NotNull
    public String getFileExtension() {
        return Files.getFileExtension(this.shortPath);
    }

    @NotNull
    public String getFileNameWithoutExtension() {
        return Files.getNameWithoutExtension(this.shortPath);
    }

    @NotNull
    public String getFileName() {
        return this.file.getName();
    }

    public boolean exists() {
        return this.file.exists();
    }

    public boolean isFile() {
        return this.file.isFile();
    }

    public boolean isDirectory() {
        return this.file.isDirectory();
    }

    public boolean isExistingAsset() {
        return this.exists() && this.isAsset();
    }

    public boolean isAsset() {
        return this.shortPath.startsWith("/config/fancymenu/assets/");
    }

    @NotNull
    public FileType<?> getType() {
        return this.type;
    }

    @NotNull
    public FileMediaType getMediaType() {
        return this.type.getMediaType();
    }
}