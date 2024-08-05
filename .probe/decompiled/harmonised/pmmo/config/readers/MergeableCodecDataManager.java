package harmonised.pmmo.config.readers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.config.codecs.DataSource;
import harmonised.pmmo.config.codecs.ObjectData;
import harmonised.pmmo.util.MsLoggy;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.Logger;

public class MergeableCodecDataManager<T extends DataSource<T>, V> extends SimplePreparableReloadListener<Map<ResourceLocation, T>> {

    protected static final String JSON_EXTENSION = ".json";

    protected static final int JSON_EXTENSION_LENGTH = ".json".length();

    protected static final Gson STANDARD_GSON = new Gson();

    @Nonnull
    protected Map<ResourceLocation, T> data = new HashMap();

    private final String folderName;

    private final Logger logger;

    private final Codec<T> codec;

    private final Function<List<T>, T> merger;

    private final Consumer<Map<ResourceLocation, T>> finalizer;

    private final Gson gson;

    private final Supplier<T> defaultImpl;

    private final ResourceKey<Registry<V>> registry;

    private Map<ResourceLocation, T> defaultSettings = new HashMap();

    private Map<ResourceLocation, T> overrideSettings = new HashMap();

    public MergeableCodecDataManager(String folderName, Logger logger, Codec<T> codec, Function<List<T>, T> merger, Consumer<Map<ResourceLocation, T>> finalizer, Supplier<T> defaultImpl, ResourceKey<Registry<V>> registry) {
        this(folderName, logger, codec, merger, finalizer, STANDARD_GSON, defaultImpl, registry);
    }

    public MergeableCodecDataManager(String folderName, Logger logger, Codec<T> codec, Function<List<T>, T> merger, Consumer<Map<ResourceLocation, T>> finalizer, Gson gson, Supplier<T> defaultImpl, ResourceKey<Registry<V>> registry) {
        this.folderName = folderName;
        this.logger = logger;
        this.codec = codec;
        this.merger = merger;
        this.finalizer = finalizer;
        this.gson = gson;
        this.defaultImpl = defaultImpl;
        this.registry = registry;
    }

    public Map<ResourceLocation, T> getData() {
        return this.data;
    }

    public void clearData() {
        this.data = new HashMap();
    }

    public T getData(ResourceLocation id) {
        return (T) this.data.computeIfAbsent(id, res -> this.getGenericTypeInstance());
    }

    public T getGenericTypeInstance() {
        return (T) this.defaultImpl.get();
    }

    public void registerDefault(ResourceLocation id, DataSource<?> data) {
        this.defaultSettings.merge(id, data, (currID, currData) -> currData.combine(data));
    }

    public void registerOverride(ResourceLocation id, DataSource<?> data) {
        this.overrideSettings.merge(id, data, (currID, currData) -> currData.combine(data));
    }

    protected Map<ResourceLocation, T> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, List<T>> map = new HashMap();
        this.defaultSettings.forEach((id, data) -> map.put(id, new ArrayList(List.of(data))));
        for (ResourceLocation resourceLocation : resourceManager.listResources(this.folderName, MergeableCodecDataManager::isStringJsonFile).keySet()) {
            String namespace = resourceLocation.getNamespace();
            String filePath = resourceLocation.getPath();
            String dataPath = filePath.substring(this.folderName.length() + 1, filePath.length() - JSON_EXTENSION_LENGTH);
            ResourceLocation jsonIdentifier = new ResourceLocation(namespace, dataPath);
            List<T> unmergedRaws = new ArrayList();
            for (Resource resource : resourceManager.getResourceStack(resourceLocation)) {
                try {
                    Reader reader = resource.openAsReader();
                    try {
                        JsonElement jsonElement = GsonHelper.fromJson(this.gson, reader, JsonElement.class);
                        this.codec.parse(JsonOps.INSTANCE, jsonElement).resultOrPartial(MergeableCodecDataManager::throwJsonParseException).ifPresent(unmergedRaws::add);
                    } catch (Throwable var17) {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (Throwable var16) {
                                var17.addSuppressed(var16);
                            }
                        }
                        throw var17;
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException | RuntimeException var18) {
                    this.logger.error("Data loader for {} could not read data {} from file {} in data pack {}", this.folderName, jsonIdentifier, resourceLocation, resource.sourcePackId(), var18);
                }
            }
            map.put(jsonIdentifier, unmergedRaws);
        }
        return mapValues(map, this.merger::apply);
    }

    static boolean isStringJsonFile(ResourceLocation file) {
        return file.getPath().endsWith(".json");
    }

    static void throwJsonParseException(String codecParseFailure) {
        throw new JsonParseException(codecParseFailure);
    }

    static <Key, In, Out> Map<Key, Out> mapValues(Map<Key, In> inputs, Function<In, Out> mapper) {
        Map<Key, Out> newMap = new HashMap();
        inputs.forEach((key, input) -> {
            Out output = (Out) mapper.apply(input);
            if (output != null) {
                newMap.put(key, output);
            }
        });
        return newMap;
    }

    protected void apply(Map<ResourceLocation, T> processedData, ResourceManager resourceManager, ProfilerFiller profiler) {
        MsLoggy.INFO.log(MsLoggy.LOG_CODE.DATA, "Beginning loading of data for data loader: {}", this.folderName);
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.DATA, processedData, "Loaded Data For: {} of: {}");
        this.data.putAll(processedData);
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.DATA, this.overrideSettings, "Loaded Override For: {} of: {}");
        this.data.putAll(this.overrideSettings);
    }

    public void postProcess(RegistryAccess registryAccess) {
        Registry<V> activeRegistry = registryAccess.registryOrThrow(this.registry);
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.DATA, "Begin PostProcessing for {}", this.folderName);
        for (Entry<ResourceLocation, T> dataRaw : new HashMap(this.data).entrySet()) {
            DataSource<T> dataValue = (DataSource<T>) dataRaw.getValue();
            if (!dataValue.getTagValues().isEmpty()) {
                MsLoggy.INFO.log(MsLoggy.LOG_CODE.DATA, "Tag Data Found for {}", ((ResourceLocation) dataRaw.getKey()).toString());
                List<ResourceLocation> tags = new ArrayList();
                for (String str : dataValue.getTagValues()) {
                    MsLoggy.INFO.log(MsLoggy.LOG_CODE.DATA, "Applying Setting to Tag: {}", str);
                    if (str.startsWith("#")) {
                        HolderSet.Named<V> tag = (HolderSet.Named<V>) activeRegistry.getTag(TagKey.create(this.registry, new ResourceLocation(str.substring(1)))).get();
                        if (tag != null) {
                            tags.addAll(tag.m_203614_().map(holder -> ((ResourceKey) holder.unwrapKey().get()).location()).toList());
                        }
                    } else if (str.endsWith(":*")) {
                        tags.addAll(activeRegistry.keySet().stream().filter(key -> key.getNamespace().equals(str.replace(":*", ""))).toList());
                    } else {
                        tags.add(new ResourceLocation(str));
                    }
                }
                dataValue.getTagValues().clear();
                if (dataValue instanceof ObjectData entityData) {
                    List<ResourceLocation> damageMembers = new ArrayList();
                    for (EventType event : List.of(EventType.RECEIVE_DAMAGE, EventType.DEAL_DAMAGE)) {
                        Map<String, Map<String, Long>> values = (Map<String, Map<String, Long>>) entityData.damageXpValues().getOrDefault(event, new HashMap());
                        for (String strx : values.keySet()) {
                            damageMembers.clear();
                            if (strx.startsWith("#")) {
                                damageMembers.addAll(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getTag(TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(strx.substring(1)))).stream().map(type -> ((TagKey) type.unwrapKey().get()).location()).toList());
                                damageMembers.forEach(damageType -> values.put(damageType.toString(), (Map) values.get(str)));
                            }
                        }
                    }
                }
                tags.forEach(rl -> this.data.merge(rl, dataValue, (o, n) -> o.combine(n)));
            }
        }
        this.finalizer.accept(this.data);
    }

    public <PACKET> MergeableCodecDataManager<T, V> subscribeAsSyncable(SimpleChannel channel, Function<Map<ResourceLocation, T>, PACKET> packetFactory) {
        MinecraftForge.EVENT_BUS.addListener(this.getDatapackSyncListener(channel, packetFactory));
        return this;
    }

    private <PACKET> Consumer<OnDatapackSyncEvent> getDatapackSyncListener(SimpleChannel channel, Function<Map<ResourceLocation, T>, PACKET> packetFactory) {
        return event -> {
            ServerPlayer player = event.getPlayer();
            List<PACKET> packets = new ArrayList();
            for (Entry<ResourceLocation, T> entry : new HashMap(this.data).entrySet()) {
                if (entry.getKey() != null) {
                    packets.add(packetFactory.apply(Map.of((ResourceLocation) entry.getKey(), (DataSource) entry.getValue())));
                }
            }
            PacketDistributor.PacketTarget target = player == null ? PacketDistributor.ALL.noArg() : PacketDistributor.PLAYER.with(() -> player);
            packets.forEach(packet -> channel.send(target, packet));
        };
    }
}