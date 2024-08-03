package se.mickelus.mutil.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MergingDataStore<V, U> extends DataStore<V> {

    private static final Logger logger = LogManager.getLogger();

    protected Class<U> arrayClass;

    public MergingDataStore(Gson gson, String namespace, String directory, Class<V> entryClass, Class<U> arrayClass, DataDistributor synchronizer) {
        super(gson, namespace, directory, entryClass, synchronizer);
        this.arrayClass = arrayClass;
    }

    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        logger.debug("Reading data for {} data store...", this.directory);
        Map<ResourceLocation, JsonElement> map = Maps.newHashMap();
        int i = this.directory.length() + 1;
        for (Entry<ResourceLocation, List<Resource>> entry : resourceManager.listResourceStacks(this.directory, rl -> rl.getPath().endsWith(".json")).entrySet()) {
            if (this.namespace.equals(((ResourceLocation) entry.getKey()).getNamespace())) {
                String path = ((ResourceLocation) entry.getKey()).getPath();
                ResourceLocation location = new ResourceLocation(((ResourceLocation) entry.getKey()).getNamespace(), path.substring(i, path.length() - jsonExtLength));
                JsonArray allResources = new JsonArray();
                for (Resource resource : (List) entry.getValue()) {
                    try {
                        Reader reader = resource.openAsReader();
                        try {
                            JsonObject json = GsonHelper.fromJson(this.gson, reader, JsonObject.class);
                            json.add("sources", this.getSources(resource));
                            if (json != null) {
                                if (this.shouldLoad(json)) {
                                    allResources.add(json);
                                } else {
                                    logger.debug("Skipping data '{}' from '{}' due to condition", entry.getKey(), resource.sourcePackId());
                                }
                            } else {
                                logger.error("Couldn't load data from '{}' in data pack '{}' as it's empty or null", entry.getKey(), resource.sourcePackId());
                            }
                        } catch (Throwable var16) {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (Throwable var15) {
                                    var16.addSuppressed(var15);
                                }
                            }
                            throw var16;
                        }
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException | RuntimeException var17) {
                        logger.error("Couldn't load data from '{}' in data pack '{}'", entry.getKey(), resource.sourcePackId(), var17);
                    }
                }
                if (allResources.size() > 0) {
                    map.put(location, allResources);
                }
            }
        }
        return map;
    }

    @Override
    public void loadFromPacket(Map<ResourceLocation, String> data) {
        Map<ResourceLocation, JsonElement> splashList = (Map<ResourceLocation, JsonElement>) data.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> GsonHelper.fromJson(this.gson, (String) entry.getValue(), JsonArray.class)));
        this.parseData(splashList);
    }

    @Override
    public void parseData(Map<ResourceLocation, JsonElement> splashList) {
        logger.info("Loaded {} {}", String.format("%3d", splashList.values().size()), this.directory);
        this.dataMap = (Map<ResourceLocation, V>) splashList.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> this.mergeData((U) this.gson.fromJson((JsonElement) entry.getValue(), this.arrayClass))));
        this.processData();
        this.listeners.forEach(Runnable::run);
    }

    protected abstract V mergeData(U var1);
}