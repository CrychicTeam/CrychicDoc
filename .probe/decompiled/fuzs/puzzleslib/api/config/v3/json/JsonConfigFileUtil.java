package fuzs.puzzleslib.api.config.v3.json;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.impl.PuzzlesLib;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;

public class JsonConfigFileUtil {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final int SEARCH_DEPTH = 3;

    public static boolean mkdirs(String modId) {
        return mkdirs(getConfigPath(modId));
    }

    public static boolean mkdirs(@Nullable File dir) {
        return dir != null && !dir.exists() ? dir.mkdirs() : false;
    }

    public static void getAndLoad(String jsonName, Consumer<File> serializer, Consumer<FileReader> deserializer) {
        File jsonFile = getConfigPath(jsonName);
        load(jsonFile, serializer, deserializer);
    }

    public static void getAndLoad(String jsonName, String modId, Consumer<File> serializer, Consumer<FileReader> deserializer) {
        File jsonFileInDir = getSpecialConfigPath(jsonName, modId);
        load(jsonFileInDir, serializer, deserializer);
    }

    public static void getAllAndLoad(String jsonName, Consumer<File> serializer, Consumer<FileReader> deserializer, Runnable prepareForLoad) {
        File jsonDir = getConfigPath(jsonName);
        List<File> files = Lists.newArrayList();
        createAllIfAbsent(jsonDir, serializer, files);
        loadAllFiles(jsonDir, deserializer, prepareForLoad, files);
    }

    private static void createAllIfAbsent(File jsonDir, Consumer<File> serializer, List<File> files) {
        mkdirs(jsonDir);
        getAllFilesRecursive(jsonDir, 3, files, name -> name.endsWith(".json"));
        if (files.isEmpty()) {
            serializer.accept(jsonDir);
        }
    }

    private static void loadAllFiles(File jsonDir, Consumer<FileReader> deserializer, Runnable prepareForLoad, List<File> files) {
        if (files.isEmpty()) {
            getAllFilesRecursive(jsonDir, 3, files, name -> name.endsWith(".json"));
        }
        prepareForLoad.run();
        files.forEach(file -> loadFromFile(file, deserializer));
    }

    private static void load(File jsonFile, Consumer<File> serializer, Consumer<FileReader> deserializer) {
        createIfAbsent(jsonFile, serializer);
        loadFromFile(jsonFile, deserializer);
    }

    private static void createIfAbsent(File jsonFile, Consumer<File> serializer) {
        if (!jsonFile.exists()) {
            serializer.accept(jsonFile);
        }
    }

    public static boolean copyToFile(File jsonFile) {
        mkdirs(jsonFile.getParentFile());
        try {
            InputStream input = JsonConfigFileUtil.class.getResourceAsStream("/" + jsonFile.getName());
            boolean var10;
            label85: {
                try {
                    FileOutputStream output;
                    label77: {
                        output = new FileOutputStream(jsonFile);
                        try {
                            if (input == null) {
                                break label77;
                            }
                            jsonFile.createNewFile();
                            byte[] buffer = new byte[16384];
                            for (int lengthRead = input.read(buffer); lengthRead > 0; lengthRead = input.read(buffer)) {
                                output.write(buffer, 0, lengthRead);
                            }
                            var10 = true;
                        } catch (Throwable var7) {
                            try {
                                output.close();
                            } catch (Throwable var6) {
                                var7.addSuppressed(var6);
                            }
                            throw var7;
                        }
                        output.close();
                        break label85;
                    }
                    output.close();
                } catch (Throwable var8) {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (Throwable var5) {
                            var8.addSuppressed(var5);
                        }
                    }
                    throw var8;
                }
                if (input != null) {
                    input.close();
                }
                return false;
            }
            if (input != null) {
                input.close();
            }
            return var10;
        } catch (Exception var9) {
            PuzzlesLib.LOGGER.error("Failed to copy {} in config directory: {}", jsonFile.getName(), var9);
            return false;
        }
    }

    public static boolean saveToFile(File jsonFile, JsonElement jsonElement) {
        mkdirs(jsonFile.getParentFile());
        try {
            FileWriter writer = new FileWriter(jsonFile);
            boolean var3;
            try {
                GSON.toJson(jsonElement, writer);
                var3 = true;
            } catch (Throwable var6) {
                try {
                    writer.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
                throw var6;
            }
            writer.close();
            return var3;
        } catch (Exception var7) {
            PuzzlesLib.LOGGER.error("Failed to create {} in config directory: {}", jsonFile.getName(), var7);
            return false;
        }
    }

    private static void loadFromFile(File file, Consumer<FileReader> deserializer) {
        try {
            FileReader reader = new FileReader(file);
            try {
                deserializer.accept(reader);
            } catch (Throwable var6) {
                try {
                    reader.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
                throw var6;
            }
            reader.close();
        } catch (Exception var7) {
            PuzzlesLib.LOGGER.error("Failed to read {} in config directory: {}", file.getName(), var7);
        }
    }

    private static void getAllFilesRecursive(File directory, int searchLayers, List<File> fileList, Predicate<String> fileNamePredicate) {
        File[] allFilesAndDirs = directory.listFiles();
        if (allFilesAndDirs != null) {
            for (File file : allFilesAndDirs) {
                if (file.isDirectory()) {
                    if (searchLayers > 0) {
                        getAllFilesRecursive(file, searchLayers - 1, fileList, fileNamePredicate);
                    }
                } else if (fileList.size() < 128 && fileNamePredicate.test(file.getName())) {
                    try {
                        fileList.add(file);
                    } catch (Exception var10) {
                        PuzzlesLib.LOGGER.error("Failed to locate files in {} directory: {}", directory.getName(), var10);
                    }
                }
            }
        }
    }

    public static File getConfigPath(String jsonName) {
        return ModLoaderEnvironment.INSTANCE.getConfigDirectory().resolve(jsonName).toFile();
    }

    public static File getSpecialConfigPath(String jsonName, String modId) {
        return ModLoaderEnvironment.INSTANCE.getConfigDirectory().resolve(modId).resolve(jsonName).toFile();
    }
}