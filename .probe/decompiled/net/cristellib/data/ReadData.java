package net.cristellib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.cristellib.CristelLib;
import net.cristellib.CristelLibExpectPlatform;
import net.cristellib.CristelLibRegistry;
import net.cristellib.StructureConfig;
import net.cristellib.builtinpacks.BuiltInDataPacks;
import net.cristellib.config.ConfigType;
import net.cristellib.config.ConfigUtil;
import net.cristellib.util.JanksonUtil;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.FileUtils;

public class ReadData {

    private static boolean checkedConfigFiles = false;

    public static void getBuiltInPacks(String modid) {
        for (Path path : getPathsInDir(modid, "data_packs")) {
            InputStream stream;
            try {
                stream = Files.newInputStream(path);
            } catch (IOException var12) {
                throw new RuntimeException(var12);
            }
            if (JsonParser.parseReader(new InputStreamReader(stream)) instanceof JsonObject object) {
                String location = object.get("location").getAsString();
                String name = object.get("display_name").getAsString();
                ResourceLocation rl = ResourceLocation.tryParse(location);
                Component component = Component.literal(name);
                try {
                    component = ComponentArgument.textComponent().parse(new StringReader(name));
                } catch (CommandSyntaxException var11) {
                    CristelLib.LOGGER.debug("Couldn't parse: \"" + name + "\" to a component", var11);
                }
                boolean b = Conditions.readConditions(object);
                BuiltInDataPacks.registerPack(rl, modid, component, () -> b);
            }
        }
        checkedConfigFiles = false;
    }

    public static void copyFile(String modid) {
        for (Path path : getPathsInDir(modid, "copy_file")) {
            InputStream stream;
            try {
                stream = Files.newInputStream(path);
            } catch (IOException var8) {
                throw new RuntimeException(var8);
            }
            if (JsonParser.parseReader(new InputStreamReader(stream)) instanceof JsonObject object) {
                String location = object.get("location").getAsString();
                String destination = object.get("destination").getAsString();
                if (Conditions.readConditions(object)) {
                    copyFileFromJar(location, destination, modid);
                }
            }
        }
        checkedConfigFiles = false;
    }

    public static void modifyJson5File(String modid) {
        for (Path path : getPathsInDir(modid, "modify_file")) {
            InputStream stream;
            try {
                stream = Files.newInputStream(path);
            } catch (IOException var13) {
                throw new RuntimeException(var13);
            }
            if (JsonParser.parseReader(new InputStreamReader(stream)) instanceof JsonObject object && Conditions.readConditions(object)) {
                String location = object.get("location").getAsString();
                String destination = object.get("path").getAsString();
                JsonObject objects = object.get("objects").getAsJsonObject();
                List<Pair<String, String>> strings = new ArrayList();
                for (String s : objects.keySet()) {
                    JsonElement e = objects.get(s);
                    strings.add(new Pair(s, e.getAsString()));
                }
                modifyObject(destination, strings, location);
            }
        }
        checkedConfigFiles = false;
    }

    public static void modifyObject(String modifier, List<Pair<String, String>> strings, String at) {
        Path toFile = ConfigUtil.CONFIG_DIR.resolve(at);
        if (toFile.toFile().exists() && toFile.endsWith("json5")) {
            try {
                blue.endless.jankson.JsonObject load = ConfigUtil.JANKSON.load(toFile.toFile());
                JanksonUtil.addToObject(load, modifier, strings);
                Files.createDirectories(toFile.getParent());
                String output = load.toJson(ConfigUtil.JSON_GRAMMAR);
                Files.write(toFile, output.getBytes(), new OpenOption[0]);
            } catch (Exception var6) {
                CristelLib.LOGGER.error("Couldn't read " + toFile + "can't modify it");
            }
        }
    }

    public static void copyFileFromJar(String from, String to, String fromModId) {
        for (Path p : CristelLibExpectPlatform.getRootPaths(fromModId)) {
            Path fromFile = p.resolve(from);
            File toFile = ConfigUtil.CONFIG_DIR.resolve(to).toFile();
            if (fromFile != null && toFile != null && !toFile.exists()) {
                try {
                    FileUtils.copyURLToFile(fromFile.toUri().toURL(), toFile);
                } catch (IOException var9) {
                    CristelLib.LOGGER.error("Couldn't copy file from: " + fromFile + " to: " + toFile, var9);
                }
            }
        }
    }

    public static void getStructureConfigs(String modid, Map<String, Set<StructureConfig>> modidAndConfigs, CristelLibRegistry registry) {
        Set<StructureConfig> configs = new HashSet();
        for (Path path : getPathsInDir(modid, "structure_configs")) {
            InputStream stream;
            try {
                stream = Files.newInputStream(path);
            } catch (IOException var21) {
                throw new RuntimeException(var21);
            }
            if (JsonParser.parseReader(new InputStreamReader(stream)) instanceof JsonObject object) {
                String subPath = object.get("subPath").getAsString();
                String name = object.get("name").getAsString();
                StructureConfig config = StructureConfig.createWithDefaultConfigPath(subPath, name, ConfigType.valueOf(object.get("config_type").getAsString().toUpperCase()));
                for (JsonElement e : object.get("structure_sets").getAsJsonArray()) {
                    if (e instanceof JsonObject o && o.has("structure_set") && o.has("modid")) {
                        String mS = o.get("modid").getAsString();
                        for (JsonElement s : o.get("structure_set").getAsJsonArray()) {
                            if (s instanceof JsonPrimitive primitive) {
                                registry.registerSetToConfig(mS, ResourceLocation.tryParse(primitive.getAsString()), config);
                            }
                        }
                    }
                }
                if (object.has("header")) {
                    String header = object.get("header").getAsString();
                    if (!header.isEmpty()) {
                        config.setHeader(header + "\n");
                    }
                }
                if (object.has("comments")) {
                    JsonObject comments = object.get("comments").getAsJsonObject();
                    HashMap<String, String> commentFinalMap = new HashMap();
                    Map<String, JsonElement> commentMap = comments.asMap();
                    for (String sx : commentMap.keySet()) {
                        if (commentMap.get(sx) instanceof JsonPrimitive p) {
                            commentFinalMap.put(sx, p.getAsString());
                        }
                    }
                    if (!commentFinalMap.isEmpty()) {
                        config.setComments(commentFinalMap);
                    }
                }
                configs.add(config);
            }
        }
        checkedConfigFiles = false;
        if (!configs.isEmpty()) {
            modidAndConfigs.put(modid, configs);
        }
    }

    public static List<Path> getPathsInDir(String modid, String subPath) {
        List<Path> paths = new ArrayList();
        findFiles(CristelLibExpectPlatform.getRootPaths(modid), modid, subPath, x$0 -> Files.exists(x$0, new LinkOption[0]), (path, file) -> {
            if (Files.isRegularFile(file, new LinkOption[0]) && file.getFileName().toString().endsWith(".json")) {
                paths.add(file);
            }
            return true;
        }, true, Integer.MAX_VALUE);
        return paths;
    }

    public static void findFiles(List<Path> rootPaths, String modid, String subPath, Predicate<Path> rootFilter, BiFunction<Path, Path, Boolean> processor, boolean visitAllFiles, int maxDepth) {
        if (!modid.equals("minecraft")) {
            if (!checkedConfigFiles) {
                findInConfigFiles(subPath, rootFilter, processor, visitAllFiles, maxDepth);
                checkedConfigFiles = true;
            }
            try {
                for (Path root : rootPaths) {
                    walk(root.resolve(String.format("data/cristellib/%s", subPath)), rootFilter, processor, visitAllFiles, maxDepth);
                }
            } catch (IOException var9) {
                throw new UncheckedIOException(var9);
            }
        }
    }

    public static void findInConfigFiles(String subPath, Predicate<Path> rootFilter, BiFunction<Path, Path, Boolean> processor, boolean visitAllFiles, int maxDepth) {
        try {
            walk(ConfigUtil.CONFIG_LIB.resolve(String.format("data/%s", subPath)), rootFilter, processor, visitAllFiles, maxDepth);
        } catch (IOException var6) {
            throw new UncheckedIOException(var6);
        }
    }

    private static void walk(Path root, Predicate<Path> rootFilter, BiFunction<Path, Path, Boolean> processor, boolean visitAllFiles, int maxDepth) throws IOException {
        if (root != null && Files.exists(root, new LinkOption[0]) && rootFilter.test(root)) {
            if (processor != null) {
                Stream<Path> stream = Files.walk(root, maxDepth, new FileVisitOption[0]);
                label65: {
                    try {
                        Iterator<Path> itr = stream.iterator();
                        while (itr.hasNext()) {
                            boolean keepGoing = (Boolean) processor.apply(root, (Path) itr.next());
                            if (!visitAllFiles && !keepGoing) {
                                break label65;
                            }
                        }
                    } catch (Throwable var9) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Throwable var8) {
                                var9.addSuppressed(var8);
                            }
                        }
                        throw var9;
                    }
                    if (stream != null) {
                        stream.close();
                    }
                    return;
                }
                if (stream != null) {
                    stream.close();
                }
            }
        }
    }
}