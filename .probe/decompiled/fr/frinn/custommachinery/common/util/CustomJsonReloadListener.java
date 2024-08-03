package fr.frinn.custommachinery.common.util;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import fr.frinn.custommachinery.CustomMachinery;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.Logger;

public abstract class CustomJsonReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, JsonElement>> {

    private static final Gson GSON = new GsonBuilder().create();

    private static final int PATH_SUFFIX_LENGTH = ".json".length();

    private static final Logger LOGGER = CustomMachinery.LOGGER;

    private final String directory;

    public CustomJsonReloadListener(String string) {
        this.directory = string;
    }

    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager manager, ProfilerFiller profiler) {
        LOGGER.info("Parsing all .json files in {} folder.", this.directory);
        Map<ResourceLocation, JsonElement> map = Maps.newHashMap();
        int i = this.directory.length() + 1;
        for (Entry<ResourceLocation, Resource> entry : manager.listResources(this.directory, locx -> locx.getPath().endsWith(".json")).entrySet()) {
            ResourceLocation loc = (ResourceLocation) entry.getKey();
            String path = loc.getPath();
            ResourceLocation id = new ResourceLocation(loc.getNamespace(), path.substring(i, path.length() - PATH_SUFFIX_LENGTH));
            try {
                Reader reader = ((Resource) entry.getValue()).openAsReader();
                try {
                    JsonElement jsonElement = GsonHelper.fromJson(GSON, reader, JsonElement.class);
                    if (jsonElement != null) {
                        JsonElement replaced = (JsonElement) map.put(id, jsonElement);
                        if (replaced != null) {
                            throw new IllegalStateException("Duplicate data file ignored with ID " + id);
                        }
                    } else {
                        LOGGER.error("Couldn't load data file {} from {} as it's null or empty", id, loc);
                    }
                } catch (Throwable var14) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var13) {
                            var14.addSuppressed(var13);
                        }
                    }
                    throw var14;
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException | JsonParseException | IllegalArgumentException var15) {
                LOGGER.error("Couldn't parse data file {} from {}\n{}", id, loc, var15);
            }
        }
        LOGGER.info("Finished, {} .json files successfully parsed in {} folder.", map.size(), this.directory);
        return map;
    }
}