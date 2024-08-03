package me.lucko.spark.forge;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.LinkedHashMap;
import java.util.Map;
import me.lucko.spark.common.platform.MetadataProvider;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;

public class ForgeExtraMetadataProvider implements MetadataProvider {

    private final PackRepository resourcePackManager;

    public ForgeExtraMetadataProvider(PackRepository resourcePackManager) {
        this.resourcePackManager = resourcePackManager;
    }

    @Override
    public Map<String, JsonElement> get() {
        Map<String, JsonElement> metadata = new LinkedHashMap();
        metadata.put("datapacks", this.datapackMetadata());
        return metadata;
    }

    private JsonElement datapackMetadata() {
        JsonObject datapacks = new JsonObject();
        for (Pack profile : this.resourcePackManager.getSelectedPacks()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", profile.getTitle().getString());
            obj.addProperty("description", profile.getDescription().getString());
            obj.addProperty("source", resourcePackSource(profile.getPackSource()));
            datapacks.add(profile.getId(), obj);
        }
        return datapacks;
    }

    private static String resourcePackSource(PackSource source) {
        if (source == PackSource.DEFAULT) {
            return "none";
        } else if (source == PackSource.BUILT_IN) {
            return "builtin";
        } else if (source == PackSource.WORLD) {
            return "world";
        } else {
            return source == PackSource.SERVER ? "server" : "unknown";
        }
    }
}