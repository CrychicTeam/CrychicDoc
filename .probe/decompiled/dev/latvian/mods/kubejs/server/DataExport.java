package dev.latvian.mods.kubejs.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.Registrar;
import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ConsoleLine;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.KubeJSPlugins;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.mod.util.JsonUtils;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class DataExport {

    @HideFromJS
    public static DataExport export = null;

    public CommandSourceStack source;

    private final Map<String, Callable<byte[]>> exportedFiles = new ConcurrentHashMap();

    public static void exportData() {
        if (export != null) {
            try {
                export.exportData0();
            } catch (Exception var1) {
                var1.printStackTrace();
            }
            export = null;
        }
    }

    private static <T> void addRegistry(JsonObject o, String name, Registrar<T> r) {
        JsonArray a = new JsonArray();
        for (ResourceLocation id : r.getIds()) {
            a.add(id.toString());
        }
        o.add(name, a);
    }

    public void add(String path, Callable<byte[]> data) {
        try {
            this.exportedFiles.put(path, data);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    public void addString(String path, String data) {
        this.add(path, () -> data.getBytes(StandardCharsets.UTF_8));
    }

    public void addJson(String path, JsonElement json) {
        this.add(path, () -> JsonUtils.toPrettyString(json).getBytes(StandardCharsets.UTF_8));
    }

    private void appendLine(StringBuilder sb, Calendar calendar, ConsoleLine line) {
        calendar.setTimeInMillis(line.timestamp);
        sb.append('[');
        UtilsJS.appendTimestamp(sb, calendar);
        sb.append(']');
        sb.append(' ');
        sb.append('[');
        sb.append(line.type);
        sb.append(']');
        sb.append(' ');
        if (line.type.equals("ERROR")) {
            sb.append('!');
            sb.append(' ');
        }
        sb.append(line.getText());
        sb.append('\n');
    }

    private void exportData0() throws Exception {
        this.source.registryAccess().registries().forEach(reg -> {
            ResourceKey<? extends Registry<?>> key = reg.key();
            Registry<?> registry = reg.value();
            JsonObject j = new JsonObject();
            for (Entry<? extends ResourceKey<?>, ?> entryx : registry.entrySet()) {
                j.addProperty(((ResourceKey) entryx.getKey()).location().toString(), entryx.getValue() == null ? "null" : entryx.getValue().getClass().getName());
            }
            this.addJson("registries/" + key.location().getPath() + ".json", j);
        });
        StringBuilder logStringBuilder = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        for (ConsoleLine line : ConsoleJS.SERVER.errors) {
            this.appendLine(logStringBuilder, calendar, line);
        }
        if (logStringBuilder.length() > 0) {
            logStringBuilder.setLength(logStringBuilder.length() - 1);
            this.addString("errors.log", logStringBuilder.toString());
        }
        logStringBuilder.setLength(0);
        for (ConsoleLine line : ConsoleJS.SERVER.warnings) {
            this.appendLine(logStringBuilder, calendar, line);
        }
        if (logStringBuilder.length() > 0) {
            logStringBuilder.setLength(logStringBuilder.length() - 1);
            this.addString("warnings.log", logStringBuilder.toString());
        }
        JsonArray modArr = new JsonArray();
        for (Mod mod : Platform.getMods()) {
            JsonObject o = new JsonObject();
            o.addProperty("id", mod.getModId().trim());
            o.addProperty("name", mod.getName().trim());
            o.addProperty("version", mod.getVersion().trim());
            o.addProperty("description", mod.getDescription().trim());
            o.addProperty("authors", String.join(", ", mod.getAuthors()).trim());
            o.addProperty("homepage", ((String) mod.getHomepage().orElse("")).trim());
            o.addProperty("sources", ((String) mod.getSources().orElse("")).trim());
            o.addProperty("issue_tracker", ((String) mod.getIssueTracker().orElse("")).trim());
            o.addProperty("license", mod.getLicense() == null ? "" : String.join(", ", mod.getLicense()).trim());
            o.entrySet().removeIf(e -> {
                if (e.getValue() instanceof JsonPrimitive p && p.isString() && p.getAsString().isEmpty()) {
                    return true;
                }
                return false;
            });
            modArr.add(o);
        }
        this.addJson("mods.json", modArr);
        KubeJSPlugins.forEachPlugin(this, KubeJSPlugin::exportServerData);
        JsonArray index = new JsonArray();
        this.exportedFiles.keySet().stream().sorted(String.CASE_INSENSITIVE_ORDER).forEach(index::add);
        this.addJson("index.json", index);
        Files.walk(KubeJSPaths.EXPORT).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        Files.createDirectory(KubeJSPaths.EXPORT);
        CompletableFuture[] arr = new CompletableFuture[this.exportedFiles.size()];
        int i = 0;
        for (Entry<String, Callable<byte[]>> entry : this.exportedFiles.entrySet()) {
            arr[i++] = CompletableFuture.runAsync(() -> {
                try {
                    Path path = KubeJSPaths.EXPORT.resolve(((String) entry.getKey()).replace(':', '/'));
                    Path parent = path.getParent();
                    if (Files.notExists(parent, new LinkOption[0])) {
                        Files.createDirectories(parent);
                    }
                    if (Files.notExists(path, new LinkOption[0])) {
                        Files.createFile(path);
                    }
                    Files.write(path, (byte[]) ((Callable) entry.getValue()).call(), new OpenOption[0]);
                } catch (Exception var3) {
                    var3.printStackTrace();
                }
            }, Util.ioPool());
        }
        CompletableFuture.allOf(arr).join();
        if (this.source.getServer().isSingleplayer()) {
            this.source.sendSuccess(() -> Component.literal("Done! Export in local/kubejs/export").kjs$clickOpenFile(KubeJSPaths.EXPORT.toAbsolutePath().toString()), false);
        } else {
            this.source.sendSuccess(() -> Component.literal("Done! Export in local/kubejs/export"), false);
        }
    }
}