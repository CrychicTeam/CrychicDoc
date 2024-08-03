package se.mickelus.mutil.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.Environment;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ParametersAreNonnullByDefault
public class DataStore<V> extends SimplePreparableReloadListener<Map<ResourceLocation, JsonElement>> {

    protected static final int jsonExtLength = ".json".length();

    private static final Logger logger = LogManager.getLogger();

    protected Gson gson;

    protected String namespace;

    protected String directory;

    protected Class<V> dataClass;

    protected Map<ResourceLocation, JsonElement> rawData;

    protected Map<ResourceLocation, V> dataMap;

    protected List<Runnable> listeners;

    private DataDistributor syncronizer;

    public DataStore(Gson gson, String namespace, String directory, Class<V> dataClass, DataDistributor synchronizer) {
        this.gson = gson;
        this.namespace = namespace;
        this.directory = directory;
        this.dataClass = dataClass;
        this.syncronizer = synchronizer;
        this.rawData = Collections.emptyMap();
        this.dataMap = Collections.emptyMap();
        this.listeners = new LinkedList();
    }

    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        logger.debug("Reading data for {} data store...", this.directory);
        Map<ResourceLocation, JsonElement> map = Maps.newHashMap();
        int i = this.directory.length() + 1;
        for (Entry<ResourceLocation, Resource> entry : resourceManager.listResources(this.directory, rl -> rl.getPath().endsWith(".json")).entrySet()) {
            if (this.namespace.equals(((ResourceLocation) entry.getKey()).getNamespace())) {
                String path = ((ResourceLocation) entry.getKey()).getPath();
                ResourceLocation location = new ResourceLocation(((ResourceLocation) entry.getKey()).getNamespace(), path.substring(i, path.length() - jsonExtLength));
                try {
                    Reader reader = ((Resource) entry.getValue()).openAsReader();
                    try {
                        JsonElement json;
                        if (this.dataClass.isArray()) {
                            JsonArray sources = this.getSources((Resource) entry.getValue());
                            json = GsonHelper.fromJson(this.gson, reader, JsonArray.class);
                            json.getAsJsonArray().forEach(element -> {
                                if (element.isJsonObject()) {
                                    element.getAsJsonObject().add("sources", sources);
                                }
                            });
                        } else {
                            json = GsonHelper.fromJson(this.gson, reader, JsonElement.class);
                            json.getAsJsonObject().add("sources", this.getSources((Resource) entry.getValue()));
                        }
                        if (json != null) {
                            if (this.shouldLoad(json)) {
                                JsonElement duplicate = (JsonElement) map.put(location, json);
                                if (duplicate != null) {
                                    throw new IllegalStateException("Duplicate data ignored with ID " + location);
                                }
                            } else {
                                logger.debug("Skipping data '{}' due to condition", entry.getKey());
                            }
                        } else {
                            logger.error("Couldn't load data from '{}' as it's null or empty", entry.getKey());
                        }
                    } catch (Throwable var13) {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (Throwable var12) {
                                var13.addSuppressed(var12);
                            }
                        }
                        throw var13;
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException | JsonParseException | IllegalArgumentException var14) {
                    logger.error("Couldn't parse data '{}' from '{}'", location, entry.getKey(), var14);
                }
            }
        }
        return map;
    }

    protected JsonArray getSources(Resource resource) {
        String fileId = resource.sourcePackId();
        JsonArray result = new JsonArray();
        ModList.get().getModFiles().stream().filter(modInfo -> fileId.equals(modInfo.getFile().getFileName())).flatMap(fileInfo -> fileInfo.getMods().stream()).map(IModInfo::getDisplayName).forEach(result::add);
        if (result.size() == 0) {
            result.add(fileId);
        }
        return result;
    }

    protected void apply(Map<ResourceLocation, JsonElement> splashList, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.rawData = splashList;
        if (Environment.get().getDist().isDedicatedServer() && ServerLifecycleHooks.getCurrentServer() != null) {
            this.syncronizer.sendToAll(this.directory, this.rawData);
        }
        this.parseData(this.rawData);
    }

    public void sendToPlayer(ServerPlayer player) {
        this.syncronizer.sendToPlayer(player, this.directory, this.rawData);
    }

    public void loadFromPacket(Map<ResourceLocation, String> data) {
        Map<ResourceLocation, JsonElement> splashList = (Map<ResourceLocation, JsonElement>) data.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> this.dataClass.isArray() ? GsonHelper.fromJson(this.gson, (String) entry.getValue(), JsonArray.class) : GsonHelper.fromJson(this.gson, (String) entry.getValue(), JsonElement.class)));
        this.parseData(splashList);
    }

    public void parseData(Map<ResourceLocation, JsonElement> splashList) {
        logger.info("Loaded {} {}", String.format("%3d", splashList.values().size()), this.directory);
        this.dataMap = (Map<ResourceLocation, V>) splashList.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> this.gson.fromJson((JsonElement) entry.getValue(), this.dataClass)));
        this.processData();
        this.listeners.forEach(Runnable::run);
    }

    protected boolean shouldLoad(JsonElement json) {
        if (json.isJsonArray()) {
            JsonArray arr = json.getAsJsonArray();
            if (arr.size() > 0) {
                json = arr.get(0);
            }
        }
        if (!json.isJsonObject()) {
            return true;
        } else {
            JsonObject jsonObject = json.getAsJsonObject();
            return !jsonObject.has("conditions") || CraftingHelper.processConditions(GsonHelper.getAsJsonArray(jsonObject, "conditions"), ICondition.IContext.EMPTY);
        }
    }

    protected void processData() {
    }

    public Map<ResourceLocation, JsonElement> getRawData() {
        return this.rawData;
    }

    public String getDirectory() {
        return this.directory;
    }

    public V getData(ResourceLocation resourceLocation) {
        return (V) this.dataMap.get(resourceLocation);
    }

    public Map<ResourceLocation, V> getData() {
        return this.dataMap;
    }

    public Collection<V> getDataIn(ResourceLocation resourceLocation) {
        return (Collection<V>) this.getData().entrySet().stream().filter(entry -> resourceLocation.getNamespace().equals(((ResourceLocation) entry.getKey()).getNamespace()) && ((ResourceLocation) entry.getKey()).getPath().startsWith(resourceLocation.getPath())).map(Entry::getValue).collect(Collectors.toList());
    }

    public void onReload(Runnable callback) {
        this.listeners.add(callback);
    }
}