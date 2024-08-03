package de.keksuccino.fancymenu.util.file.type;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileTypeRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, FileType<?>> FILE_TYPES = new LinkedHashMap();

    public static void register(@NotNull String fileTypeName, @NotNull FileType<?> fileType) {
        if (FILE_TYPES.containsKey(Objects.requireNonNull(fileTypeName))) {
            throw new RuntimeException("[FANCYMENU] Failed to register FileType! FileType name already registered: " + fileTypeName);
        } else {
            FILE_TYPES.put(fileTypeName, (FileType) Objects.requireNonNull(fileType));
        }
    }

    @Nullable
    public static FileType<?> getFileType(@NotNull String fileTypeName) {
        return (FileType<?>) FILE_TYPES.get(fileTypeName);
    }

    @NotNull
    public static List<FileType<?>> getFileTypes() {
        return new ArrayList(FILE_TYPES.values());
    }
}