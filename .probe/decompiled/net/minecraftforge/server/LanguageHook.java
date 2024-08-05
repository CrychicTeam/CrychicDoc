package net.minecraftforge.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.ForgeI18n;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanguageHook {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new Gson();

    private static final Pattern PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");

    private static List<Map<String, String>> capturedTables = new ArrayList(2);

    private static Map<String, String> modTable;

    public static void captureLanguageMap(Map<String, String> table) {
        capturedTables.add(table);
        if (modTable != null) {
            capturedTables.forEach(t -> t.putAll(modTable));
        }
    }

    private static void loadLocaleData(List<Resource> allResources) {
        allResources.forEach(res -> {
            try {
                loadLocaleData(res.open());
            } catch (IOException var2) {
            }
        });
    }

    private static void loadLocaleData(InputStream inputstream) {
        try {
            JsonElement jsonelement = (JsonElement) GSON.fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonElement.class);
            JsonObject jsonobject = GsonHelper.convertToJsonObject(jsonelement, "strings");
            jsonobject.entrySet().forEach(entry -> {
                String s = PATTERN.matcher(GsonHelper.convertToString((JsonElement) entry.getValue(), (String) entry.getKey())).replaceAll("%$1s");
                modTable.put((String) entry.getKey(), s);
            });
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }

    private static void loadLanguage(String langName, MinecraftServer server) {
        String langFile = String.format(Locale.ROOT, "lang/%s.json", langName);
        ResourceManager resourceManager = server.getServerResources().resourceManager();
        resourceManager.getNamespaces().forEach(namespace -> {
            try {
                ResourceLocation langResource = new ResourceLocation(namespace, langFile);
                loadLocaleData(resourceManager.getResourceStack(langResource));
            } catch (Exception var4) {
                LOGGER.warn("Skipped language file: {}:{}", namespace, langFile, var4);
            }
        });
    }

    public static void loadForgeAndMCLangs() {
        modTable = new HashMap(5000);
        InputStream mc = Thread.currentThread().getContextClassLoader().getResourceAsStream("assets/minecraft/lang/en_us.json");
        InputStream forge = Thread.currentThread().getContextClassLoader().getResourceAsStream("assets/forge/lang/en_us.json");
        loadLocaleData(mc);
        loadLocaleData(forge);
        capturedTables.forEach(t -> t.putAll(modTable));
        ForgeI18n.loadLanguageData(modTable);
    }

    static void loadLanguagesOnServer(MinecraftServer server) {
        modTable = new HashMap(5000);
        for (String lang : Arrays.asList("en_us")) {
            loadLanguage(lang, server);
        }
        capturedTables.forEach(t -> t.putAll(modTable));
        ForgeI18n.loadLanguageData(modTable);
    }
}