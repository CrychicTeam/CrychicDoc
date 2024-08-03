package dev.ftb.mods.ftblibrary.sidebar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import dev.architectury.platform.Platform;
import dev.ftb.mods.ftblibrary.FTBLibrary;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public enum SidebarButtonManager implements ResourceManagerReloadListener {

    INSTANCE;

    private final List<SidebarButtonGroup> groups = new ArrayList();

    public List<SidebarButtonGroup> getGroups() {
        return this.groups;
    }

    private JsonElement readJson(Resource resource) {
        try {
            BufferedReader reader = resource.openAsReader();
            JsonElement var3;
            try {
                var3 = JsonParser.parseReader(reader);
            } catch (Throwable var6) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }
                throw var6;
            }
            if (reader != null) {
                reader.close();
            }
            return var3;
        } catch (IOException | JsonParseException var7) {
            FTBLibrary.LOGGER.warn("can't read {}: {}", resource.sourcePackId(), var7.getMessage());
            return JsonNull.INSTANCE;
        }
    }

    private JsonElement readJson(File file) {
        try {
            FileReader reader = new FileReader(file);
            JsonElement var3;
            try {
                var3 = JsonParser.parseReader(reader);
            } catch (Throwable var6) {
                try {
                    reader.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
                throw var6;
            }
            reader.close();
            return var3;
        } catch (IOException | JsonParseException var7) {
            FTBLibrary.LOGGER.warn("can't read {}: {}", file.getAbsolutePath(), var7.getMessage());
            return JsonNull.INSTANCE;
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        this.groups.clear();
        JsonElement element = this.readJson(Platform.getConfigFolder().resolve("sidebar_buttons.json").toFile());
        JsonObject sidebarButtonConfig;
        if (element.isJsonObject()) {
            sidebarButtonConfig = element.getAsJsonObject();
        } else {
            sidebarButtonConfig = new JsonObject();
        }
        Map<ResourceLocation, SidebarButtonGroup> groupMap = new HashMap();
        for (String domain : manager.getNamespaces()) {
            try {
                for (Resource resource : manager.getResourceStack(new ResourceLocation(domain, "sidebar_button_groups.json"))) {
                    JsonElement json = this.readJson(resource);
                    for (Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
                        if (((JsonElement) entry.getValue()).isJsonObject()) {
                            JsonObject groupJson = ((JsonElement) entry.getValue()).getAsJsonObject();
                            int y = 0;
                            boolean pinned = true;
                            if (groupJson.has("y")) {
                                y = groupJson.get("y").getAsInt();
                            }
                            if (groupJson.has("pinned")) {
                                pinned = groupJson.get("pinned").getAsBoolean();
                            }
                            SidebarButtonGroup group = new SidebarButtonGroup(new ResourceLocation(domain, (String) entry.getKey()), y, pinned);
                            groupMap.put(group.getId(), group);
                        }
                    }
                }
            } catch (Exception var17) {
                var17.printStackTrace();
            }
        }
        for (String domain : manager.getNamespaces()) {
            try {
                for (Resource resource : manager.getResourceStack(new ResourceLocation(domain, "sidebar_buttons.json"))) {
                    JsonElement json = this.readJson(resource);
                    if (json.isJsonObject()) {
                        for (Entry<String, JsonElement> entryx : json.getAsJsonObject().entrySet()) {
                            if (((JsonElement) entryx.getValue()).isJsonObject()) {
                                JsonObject buttonJson = ((JsonElement) entryx.getValue()).getAsJsonObject();
                                if (buttonJson.has("group") && (!buttonJson.has("dev_only") || !buttonJson.get("dev_only").getAsBoolean())) {
                                    SidebarButtonGroup group = (SidebarButtonGroup) groupMap.get(new ResourceLocation(buttonJson.get("group").getAsString()));
                                    if (group != null) {
                                        SidebarButton button = new SidebarButton(new ResourceLocation(domain, (String) entryx.getKey()), group, buttonJson);
                                        group.getButtons().add(button);
                                        if (sidebarButtonConfig.has(button.getId().getNamespace())) {
                                            JsonElement e = sidebarButtonConfig.get(button.getId().getNamespace());
                                            if (e.isJsonObject() && e.getAsJsonObject().has(button.getId().getPath())) {
                                                button.setConfig(e.getAsJsonObject().get(button.getId().getPath()).getAsBoolean());
                                            }
                                        } else if (sidebarButtonConfig.has(button.getId().toString())) {
                                            button.setConfig(sidebarButtonConfig.get(button.getId().toString()).getAsBoolean());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception var16) {
                var16.printStackTrace();
            }
        }
        for (SidebarButtonGroup group : groupMap.values()) {
            if (!group.getButtons().isEmpty()) {
                group.getButtons().sort(null);
                this.groups.add(group);
            }
        }
        this.groups.sort(null);
        for (SidebarButtonGroup groupx : this.groups) {
            for (SidebarButton button : groupx.getButtons()) {
                SidebarButtonCreatedEvent.EVENT.invoker().accept(new SidebarButtonCreatedEvent(button));
            }
        }
        this.saveConfig();
    }

    public void saveConfig() {
        JsonObject o = new JsonObject();
        for (SidebarButtonGroup group : this.groups) {
            for (SidebarButton button : group.getButtons()) {
                JsonObject o1 = o.getAsJsonObject(button.getId().getNamespace());
                if (o1 == null) {
                    o1 = new JsonObject();
                    o.add(button.getId().getNamespace(), o1);
                }
                o1.addProperty(button.getId().getPath(), button.getConfig());
            }
        }
        File file = Platform.getConfigFolder().resolve("sidebar_buttons.json").toFile();
        try {
            FileWriter writer = new FileWriter(file);
            try {
                Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
                JsonWriter jsonWriter = new JsonWriter(writer);
                jsonWriter.setIndent("\t");
                gson.toJson(o, jsonWriter);
            } catch (Throwable var8) {
                try {
                    writer.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
                throw var8;
            }
            writer.close();
        } catch (Exception var9) {
            var9.printStackTrace();
        }
    }
}