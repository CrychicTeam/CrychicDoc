package top.theillusivec4.curios.common.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.common.slottype.LegacySlotManager;

public class CuriosEntityManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static CuriosEntityManager SERVER = new CuriosEntityManager();

    public static CuriosEntityManager CLIENT = new CuriosEntityManager();

    private Map<EntityType<?>, Map<String, ISlotType>> entitySlots = ImmutableMap.of();

    private Map<String, Set<String>> idToMods = ImmutableMap.of();

    private ICondition.IContext ctx = ICondition.IContext.EMPTY;

    public CuriosEntityManager() {
        super(GSON, "curios/entities");
    }

    public CuriosEntityManager(ICondition.IContext ctx) {
        super(GSON, "curios/entities");
        this.ctx = ctx;
    }

    protected void apply(Map<ResourceLocation, JsonElement> pObject, @Nonnull ResourceManager pResourceManager, @Nonnull ProfilerFiller pProfiler) {
        Map<EntityType<?>, Builder<String, ISlotType>> map = new HashMap();
        Map<String, com.google.common.collect.ImmutableSet.Builder<String>> modMap = new HashMap();
        Map<ResourceLocation, JsonElement> sorted = new LinkedHashMap();
        pResourceManager.listPacks().forEach(packResources -> {
            Set<String> namespaces = packResources.getNamespaces(PackType.SERVER_DATA);
            namespaces.forEach(namespace -> packResources.listResources(PackType.SERVER_DATA, namespace, "curios/entities", (resourceLocation, inputStreamIoSupplier) -> {
                String path = resourceLocation.getPath();
                ResourceLocation rl = new ResourceLocation(namespace, path.substring("curios/entities/".length(), path.length() - ".json".length()));
                JsonElement el = (JsonElement) pObject.get(rl);
                if (el != null) {
                    sorted.put(rl, el);
                }
            }));
        });
        for (String s : LegacySlotManager.getImcBuilders().keySet()) {
            Builder<String, ISlotType> builder = (Builder<String, ISlotType>) map.computeIfAbsent(EntityType.PLAYER, k -> ImmutableMap.builder());
            CuriosSlotManager.SERVER.getSlot(s).ifPresentOrElse(slot -> builder.put(s, slot), () -> Curios.LOGGER.error("{} is not a registered slot type!", s));
        }
        for (Entry<ResourceLocation, JsonElement> entry : sorted.entrySet()) {
            ResourceLocation resourcelocation = (ResourceLocation) entry.getKey();
            if (!resourcelocation.getPath().startsWith("_")) {
                try {
                    JsonObject jsonObject = GsonHelper.convertToJsonObject((JsonElement) entry.getValue(), "top element");
                    for (Entry<EntityType<?>, Map<String, ISlotType>> entry1 : getSlotsForEntities(jsonObject, resourcelocation, this.ctx).entrySet()) {
                        if (GsonHelper.getAsBoolean(jsonObject, "replace", false)) {
                            Builder<String, ISlotType> builder = ImmutableMap.builder();
                            builder.putAll((Map) entry1.getValue());
                            map.put((EntityType) entry1.getKey(), builder);
                        } else {
                            ((Builder) map.computeIfAbsent((EntityType) entry1.getKey(), k -> ImmutableMap.builder())).putAll((Map) entry1.getValue());
                        }
                        ((com.google.common.collect.ImmutableSet.Builder) modMap.computeIfAbsent(resourcelocation.getPath(), k -> ImmutableSet.builder())).add(resourcelocation.getNamespace());
                    }
                } catch (JsonParseException | IllegalArgumentException var14) {
                    Curios.LOGGER.error("Parsing error loading curio entity {}", resourcelocation, var14);
                }
            }
        }
        Map<String, ISlotType> configSlots = new HashMap();
        for (String configSlot : CuriosSlotManager.SERVER.getConfigSlots()) {
            CuriosSlotManager.SERVER.getSlot(configSlot).ifPresentOrElse(slot -> configSlots.put(configSlot, slot), () -> Curios.LOGGER.error("{} is not a registered slot type!", configSlot));
        }
        ((Builder) map.computeIfAbsent(EntityType.PLAYER, k -> ImmutableMap.builder())).putAll(configSlots);
        this.entitySlots = (Map<EntityType<?>, Map<String, ISlotType>>) map.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, entryx -> ((Builder) entryx.getValue()).buildKeepingLast()));
        this.idToMods = (Map<String, Set<String>>) modMap.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, entryx -> ((com.google.common.collect.ImmutableSet.Builder) entryx.getValue()).build()));
        Curios.LOGGER.info("Loaded {} curio entities", map.size());
    }

    public static ListTag getSyncPacket() {
        ListTag tag = new ListTag();
        for (Entry<EntityType<?>, Map<String, ISlotType>> entry : SERVER.entitySlots.entrySet()) {
            ResourceLocation rl = ForgeRegistries.ENTITY_TYPES.getKey((EntityType<?>) entry.getKey());
            if (rl != null) {
                CompoundTag entity = new CompoundTag();
                entity.putString("Entity", rl.toString());
                ListTag tag1 = new ListTag();
                for (Entry<String, ISlotType> val : ((Map) entry.getValue()).entrySet()) {
                    tag1.add(StringTag.valueOf((String) val.getKey()));
                }
                entity.put("Slots", tag1);
                tag.add(entity);
            }
        }
        return tag;
    }

    public static void applySyncPacket(ListTag tag) {
        Map<EntityType<?>, Builder<String, ISlotType>> map = new HashMap();
        for (Tag tag1 : tag) {
            if (tag1 instanceof CompoundTag) {
                CompoundTag entity = (CompoundTag) tag1;
                EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entity.getString("Entity")));
                if (type != null) {
                    for (Tag slot : entity.getList("Slots", 8)) {
                        if (slot instanceof StringTag stringTag) {
                            String id = stringTag.getAsString();
                            CuriosSlotManager.CLIENT.getSlot(id).ifPresent(slotType -> ((Builder) map.computeIfAbsent(type, k -> ImmutableMap.builder())).put(id, slotType));
                        }
                    }
                }
            }
        }
        CLIENT.entitySlots = (Map<EntityType<?>, Map<String, ISlotType>>) map.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, entry -> ((Builder) entry.getValue()).build()));
    }

    private static Map<EntityType<?>, Map<String, ISlotType>> getSlotsForEntities(JsonObject jsonObject, ResourceLocation resourceLocation, ICondition.IContext ctx) {
        Map<EntityType<?>, Map<String, ISlotType>> map = new HashMap();
        if (!CraftingHelper.processConditions(GsonHelper.getAsJsonArray(jsonObject, "conditions", new JsonArray()), ctx)) {
            Curios.LOGGER.debug("Skipping loading entity file {} as its conditions were not met", resourceLocation);
            return map;
        } else {
            JsonArray jsonEntities = GsonHelper.getAsJsonArray(jsonObject, "entities", new JsonArray());
            ITagManager<EntityType<?>> tagManager = (ITagManager<EntityType<?>>) Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.tags());
            Set<EntityType<?>> toAdd = new HashSet();
            for (JsonElement jsonEntity : jsonEntities) {
                String entity = jsonEntity.getAsString();
                if (entity.startsWith("#")) {
                    for (EntityType<?> entityType : tagManager.getTag(tagManager.createTagKey(new ResourceLocation(entity)))) {
                        toAdd.add(entityType);
                    }
                } else {
                    EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entity));
                    if (type != null) {
                        toAdd.add(type);
                    } else {
                        Curios.LOGGER.error("{} is not a registered entity type!", entity);
                    }
                }
            }
            JsonArray jsonSlots = GsonHelper.getAsJsonArray(jsonObject, "slots", new JsonArray());
            Map<String, ISlotType> slots = new HashMap();
            for (JsonElement jsonSlot : jsonSlots) {
                String id = jsonSlot.getAsString();
                CuriosSlotManager.SERVER.getSlot(id).ifPresentOrElse(slot -> slots.put(id, slot), () -> Curios.LOGGER.error("{} is not a registered slot type!", id));
            }
            for (EntityType<?> entityType : toAdd) {
                ((Map) map.computeIfAbsent(entityType, k -> new HashMap())).putAll(slots);
            }
            return map;
        }
    }

    public Map<String, ISlotType> getEntitySlots(EntityType<?> type) {
        return (Map<String, ISlotType>) (this.entitySlots.containsKey(type) ? (Map) this.entitySlots.get(type) : ImmutableMap.of());
    }

    public Map<String, Set<String>> getModsFromSlots() {
        return ImmutableMap.copyOf(this.idToMods);
    }
}