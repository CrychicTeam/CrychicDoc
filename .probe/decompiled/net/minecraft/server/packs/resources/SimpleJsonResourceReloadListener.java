package net.minecraft.server.packs.resources;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

public abstract class SimpleJsonResourceReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, JsonElement>> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Gson gson;

    private final String directory;

    public SimpleJsonResourceReloadListener(Gson gson0, String string1) {
        this.gson = gson0;
        this.directory = string1;
    }

    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager0, ProfilerFiller profilerFiller1) {
        Map<ResourceLocation, JsonElement> $$2 = new HashMap();
        scanDirectory(resourceManager0, this.directory, this.gson, $$2);
        return $$2;
    }

    public static void scanDirectory(ResourceManager resourceManager0, String string1, Gson gson2, Map<ResourceLocation, JsonElement> mapResourceLocationJsonElement3) {
        FileToIdConverter $$4 = FileToIdConverter.json(string1);
        for (Entry<ResourceLocation, Resource> $$5 : $$4.listMatchingResources(resourceManager0).entrySet()) {
            ResourceLocation $$6 = (ResourceLocation) $$5.getKey();
            ResourceLocation $$7 = $$4.fileToId($$6);
            try {
                Reader $$8 = ((Resource) $$5.getValue()).openAsReader();
                try {
                    JsonElement $$9 = GsonHelper.fromJson(gson2, $$8, JsonElement.class);
                    JsonElement $$10 = (JsonElement) mapResourceLocationJsonElement3.put($$7, $$9);
                    if ($$10 != null) {
                        throw new IllegalStateException("Duplicate data file ignored with ID " + $$7);
                    }
                } catch (Throwable var13) {
                    if ($$8 != null) {
                        try {
                            $$8.close();
                        } catch (Throwable var12) {
                            var13.addSuppressed(var12);
                        }
                    }
                    throw var13;
                }
                if ($$8 != null) {
                    $$8.close();
                }
            } catch (IllegalArgumentException | IOException | JsonParseException var14) {
                LOGGER.error("Couldn't parse data file {} from {}", new Object[] { $$7, $$6, var14 });
            }
        }
    }
}