package dev.latvian.mods.kubejs.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.bindings.event.ClientEvents;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.PlatformWrapper;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.data.GeneratedData;
import dev.latvian.mods.kubejs.script.data.GeneratedResourcePack;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.KubeJSPlugins;
import dev.latvian.mods.rhino.mod.util.JsonUtils;
import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;

public class GeneratedClientResourcePack extends GeneratedResourcePack {

    public final Minecraft client;

    public static List<PackResources> inject(Minecraft client, List<PackResources> packs) {
        if (KubeJS.instance != null) {
            packs = new ArrayList(packs);
            int i = packs.size();
            for (int j = 1; j < packs.size(); j++) {
                if (packs.get(j) instanceof FilePackResources) {
                    i = j;
                    break;
                }
            }
            ArrayList<PackResources> injected = new ArrayList(2);
            injected.add(new GeneratedClientResourcePack(client));
            for (File file : (File[]) Objects.requireNonNull(KubeJSPaths.ASSETS.toFile().listFiles())) {
                if (file.isFile() && file.getName().endsWith(".zip")) {
                    injected.add(new FilePackResources(file.getName(), file, false));
                }
            }
            packs.addAll(i, injected);
        }
        return packs;
    }

    public GeneratedClientResourcePack(Minecraft client) {
        super(PackType.CLIENT_RESOURCES);
        this.client = client;
        this.getGenerated();
    }

    @Override
    public void generate(Map<ResourceLocation, GeneratedData> map) {
        AssetJsonGenerator generator = new AssetJsonGenerator(map);
        for (BuilderBase<?> builder : RegistryInfo.ALL_BUILDERS) {
            builder.generateAssetJsons(generator);
        }
        KubeJSPlugins.forEachPlugin(generator, KubeJSPlugin::generateAssetJsons);
        HashMap<LangEventJS.Key, String> langMap = new HashMap();
        HashMap<String, LangEventJS> langEvents = new HashMap();
        LangEventJS enUsLangEvent = (LangEventJS) langEvents.computeIfAbsent("en_us", s -> new LangEventJS(s, langMap));
        if (Platform.isModLoaded("jade")) {
            for (PlatformWrapper.ModInfo mod : PlatformWrapper.getMods().values()) {
                if (!mod.getCustomName().isEmpty()) {
                    enUsLangEvent.add("kubejs", "jade.modName." + mod.getId(), mod.getCustomName());
                }
            }
        }
        for (BuilderBase<?> builder : RegistryInfo.ALL_BUILDERS) {
            builder.generateLang(enUsLangEvent);
        }
        KubeJSPlugins.forEachPlugin(enUsLangEvent, KubeJSPlugin::generateLang);
        ClientEvents.HIGH_ASSETS.post(ScriptType.CLIENT, new GenerateClientAssetsEventJS(generator));
        for (Object lang : ClientEvents.LANG.findUniqueExtraIds(ScriptType.CLIENT)) {
            String l = String.valueOf(lang);
            if (LangEventJS.PATTERN.matcher(l).matches()) {
                ClientEvents.LANG.post(ScriptType.CLIENT, l, (EventJS) langEvents.computeIfAbsent(l, k -> new LangEventJS(k, langMap)));
            } else {
                ConsoleJS.CLIENT.error("Invalid language key: " + l);
            }
        }
        try {
            for (Path dir : Files.list(KubeJSPaths.ASSETS).filter(x$0 -> Files.isDirectory(x$0, new LinkOption[0])).toList()) {
                String ns = dir.getFileName().toString();
                Path langDir = dir.resolve("lang");
                if (Files.exists(langDir, new LinkOption[0]) && Files.isDirectory(langDir, new LinkOption[0])) {
                    for (Path path : Files.list(langDir).filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).filter(Files::isReadable).toList()) {
                        String fileName = path.getFileName().toString();
                        if (fileName.endsWith(".json")) {
                            try {
                                BufferedReader reader = Files.newBufferedReader(path);
                                try {
                                    JsonObject json = (JsonObject) JsonUtils.GSON.fromJson(reader, JsonObject.class);
                                    String langx = fileName.substring(0, fileName.length() - 5);
                                    for (Entry<String, JsonElement> entry : json.entrySet()) {
                                        langMap.put(new LangEventJS.Key(ns, langx, (String) entry.getKey()), ((JsonElement) entry.getValue()).getAsString());
                                    }
                                } catch (Throwable var19) {
                                    if (reader != null) {
                                        try {
                                            reader.close();
                                        } catch (Throwable var18) {
                                            var19.addSuppressed(var18);
                                        }
                                    }
                                    throw var19;
                                }
                                if (reader != null) {
                                    reader.close();
                                }
                            } catch (Exception var20) {
                                var20.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception var21) {
            var21.printStackTrace();
        }
        HashMap<String, Map<String, JsonObject>> finalMap = new HashMap();
        for (Entry<LangEventJS.Key, String> entry : langMap.entrySet()) {
            Map<String, JsonObject> ns = (Map<String, JsonObject>) finalMap.computeIfAbsent(((LangEventJS.Key) entry.getKey()).namespace(), s -> new HashMap());
            JsonObject langx = (JsonObject) ns.computeIfAbsent(((LangEventJS.Key) entry.getKey()).lang(), s -> new JsonObject());
            langx.addProperty(((LangEventJS.Key) entry.getKey()).key(), (String) entry.getValue());
        }
        for (Entry<String, Map<String, JsonObject>> e1 : finalMap.entrySet()) {
            for (Entry<String, JsonObject> e2 : ((Map) e1.getValue()).entrySet()) {
                generator.json(new ResourceLocation((String) e1.getKey() + ":lang/" + (String) e2.getKey()), (JsonElement) e2.getValue());
            }
        }
    }

    @Override
    protected boolean forgetFile(String path) {
        return super.forgetFile(path);
    }

    @Override
    protected boolean skipFile(GeneratedData data) {
        return data.id().getPath().startsWith("lang/");
    }
}