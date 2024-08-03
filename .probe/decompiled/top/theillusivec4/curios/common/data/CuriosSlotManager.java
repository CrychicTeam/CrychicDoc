package top.theillusivec4.curios.common.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.apache.commons.lang3.EnumUtils;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.common.CuriosConfig;
import top.theillusivec4.curios.common.slottype.LegacySlotManager;
import top.theillusivec4.curios.common.slottype.SlotType;

public class CuriosSlotManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static CuriosSlotManager SERVER = new CuriosSlotManager();

    public static CuriosSlotManager CLIENT = new CuriosSlotManager();

    private Map<String, ISlotType> slots = ImmutableMap.of();

    private Set<String> configSlots = ImmutableSet.of();

    private Map<String, ResourceLocation> icons = ImmutableMap.of();

    private Map<String, Set<String>> idToMods = ImmutableMap.of();

    private ICondition.IContext ctx = ICondition.IContext.EMPTY;

    public CuriosSlotManager() {
        super(GSON, "curios/slots");
    }

    public CuriosSlotManager(ICondition.IContext ctx) {
        super(GSON, "curios/slots");
        this.ctx = ctx;
    }

    protected void apply(@Nonnull Map<ResourceLocation, JsonElement> pObject, @Nonnull ResourceManager pResourceManager, @Nonnull ProfilerFiller pProfiler) {
        Map<String, SlotType.Builder> map = new HashMap();
        Map<String, Builder<String>> modMap = new HashMap();
        Map<ResourceLocation, JsonElement> sorted = new LinkedHashMap();
        pResourceManager.listPacks().forEach(packResources -> {
            Set<String> namespaces = packResources.getNamespaces(PackType.SERVER_DATA);
            namespaces.forEach(namespace -> packResources.listResources(PackType.SERVER_DATA, namespace, "curios/slots", (resourceLocation, inputStreamIoSupplier) -> {
                String path = resourceLocation.getPath();
                ResourceLocation rl = new ResourceLocation(namespace, path.substring("curios/slots/".length(), path.length() - ".json".length()));
                JsonElement el = (JsonElement) pObject.get(rl);
                if (el != null) {
                    sorted.put(rl, el);
                }
            }));
        });
        for (Entry<ResourceLocation, JsonElement> entry : sorted.entrySet()) {
            ResourceLocation resourcelocation = (ResourceLocation) entry.getKey();
            if (resourcelocation.getNamespace().equals("curios")) {
                try {
                    String id = resourcelocation.getPath();
                    if (!CraftingHelper.processConditions(GsonHelper.getAsJsonArray(((JsonElement) entry.getValue()).getAsJsonObject(), "conditions", new JsonArray()), this.ctx)) {
                        Curios.LOGGER.debug("Skipping loading slot {} as its conditions were not met", resourcelocation);
                    } else {
                        fromJson((SlotType.Builder) map.computeIfAbsent(id, k -> new SlotType.Builder(id)), GsonHelper.convertToJsonObject((JsonElement) entry.getValue(), "top element"));
                        ((Builder) modMap.computeIfAbsent(id, k -> ImmutableSet.builder())).add(resourcelocation.getNamespace());
                    }
                } catch (JsonParseException | IllegalArgumentException var12) {
                    Curios.LOGGER.error("Parsing error loading curio slot {}", resourcelocation, var12);
                }
            }
        }
        for (Entry<String, SlotType.Builder> entryx : LegacySlotManager.getImcBuilders().entrySet()) {
            SlotType.Builder builder = (SlotType.Builder) map.computeIfAbsent((String) entryx.getKey(), k -> new SlotType.Builder((String) entry.getKey()));
            builder.apply((SlotType.Builder) entryx.getValue());
        }
        for (Entry<String, Set<String>> entryx : LegacySlotManager.getIdsToMods().entrySet()) {
            ((Builder) modMap.computeIfAbsent((String) entryx.getKey(), k -> ImmutableSet.builder())).addAll((Iterable) entryx.getValue());
        }
        for (Entry<ResourceLocation, JsonElement> entryx : sorted.entrySet()) {
            ResourceLocation resourcelocation = (ResourceLocation) entryx.getKey();
            if (!resourcelocation.getPath().startsWith("_") && !resourcelocation.getNamespace().equals("curios")) {
                try {
                    String id = resourcelocation.getPath();
                    if (!CraftingHelper.processConditions(GsonHelper.getAsJsonArray(((JsonElement) entryx.getValue()).getAsJsonObject(), "conditions", new JsonArray()), this.ctx)) {
                        Curios.LOGGER.debug("Skipping loading slot {} as its conditions were not met", resourcelocation);
                    } else {
                        fromJson((SlotType.Builder) map.computeIfAbsent(id, k -> new SlotType.Builder(id)), GsonHelper.convertToJsonObject((JsonElement) entryx.getValue(), "top element"));
                        ((Builder) modMap.computeIfAbsent(id, k -> ImmutableSet.builder())).add(resourcelocation.getNamespace());
                    }
                } catch (JsonParseException | IllegalArgumentException var11) {
                    Curios.LOGGER.error("Parsing error loading curio slot {}", resourcelocation, var11);
                }
            }
        }
        try {
            Set<String> configs = fromConfig(map);
            this.configSlots = ImmutableSet.copyOf(configs);
            for (String id : configs) {
                ((Builder) modMap.computeIfAbsent(id, k -> ImmutableSet.builder())).add("config");
            }
        } catch (IllegalArgumentException var13) {
            Curios.LOGGER.error("Config parsing error", var13);
        }
        this.slots = (Map<String, ISlotType>) map.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, entryxx -> ((SlotType.Builder) entryxx.getValue()).build()));
        this.idToMods = (Map<String, Set<String>>) modMap.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, entryxx -> ((Builder) entryxx.getValue()).build()));
        Curios.LOGGER.info("Loaded {} curio slots", map.size());
    }

    public Map<String, ISlotType> getSlots() {
        return this.slots;
    }

    public Optional<ISlotType> getSlot(String id) {
        return Optional.ofNullable((ISlotType) this.slots.get(id));
    }

    public static ListTag getSyncPacket() {
        ListTag tag = new ListTag();
        for (Entry<String, ISlotType> entry : SERVER.slots.entrySet()) {
            tag.add(((ISlotType) entry.getValue()).writeNbt());
        }
        return tag;
    }

    public static void applySyncPacket(ListTag tag) {
        com.google.common.collect.ImmutableMap.Builder<String, ISlotType> map = ImmutableMap.builder();
        for (Tag tag1 : tag) {
            if (tag1 instanceof CompoundTag slotType) {
                ISlotType type = SlotType.from(slotType);
                map.put(type.getIdentifier(), type);
            }
        }
        CLIENT.slots = map.build();
    }

    public void setIcons(Map<String, ResourceLocation> icons) {
        this.icons = ImmutableMap.copyOf(icons);
    }

    public Set<String> getConfigSlots() {
        return this.configSlots;
    }

    public Map<String, ResourceLocation> getIcons() {
        return this.icons;
    }

    public ResourceLocation getIcon(String identifier) {
        return (ResourceLocation) this.icons.getOrDefault(identifier, new ResourceLocation("curios", "slot/empty_curio_slot"));
    }

    public Map<String, Set<String>> getModsFromSlots() {
        return this.idToMods;
    }

    public static Set<String> fromConfig(Map<String, SlotType.Builder> map) throws IllegalArgumentException {
        List<Map<String, String>> parsed = new ArrayList();
        List<? extends String> list = CuriosConfig.COMMON.slots.get();
        Set<String> results = new HashSet();
        for (String s : list) {
            StringTokenizer tokenizer = new StringTokenizer(s, ";");
            Map<String, String> subMap = new HashMap();
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                String[] keyValue = token.split("=");
                subMap.put(keyValue[0], keyValue[1]);
            }
            if (!subMap.containsKey("id")) {
                throw new IllegalArgumentException("Cannot load config entry " + s + " due to missing id field");
            }
            parsed.add(subMap);
        }
        for (Map<String, String> entry : parsed) {
            String id = (String) entry.get("id");
            SlotType.Builder builder = (SlotType.Builder) map.computeIfAbsent(id, k -> new SlotType.Builder(id));
            Integer size = entry.containsKey("size") ? Integer.parseInt((String) entry.get("size")) : null;
            if (size != null && size < 0) {
                throw new IllegalArgumentException("Size cannot be less than 0!");
            }
            String operation = (String) entry.getOrDefault("operation", "SET");
            if (!operation.equals("SET") && !operation.equals("ADD") && !operation.equals("REMOVE")) {
                throw new IllegalArgumentException(operation + " is not a valid operation!");
            }
            String dropRule = (String) entry.getOrDefault("drop_rule", "");
            if (!dropRule.isEmpty() && !EnumUtils.isValidEnum(ICurio.DropRule.class, dropRule)) {
                throw new IllegalArgumentException(dropRule + " is not a valid drop rule!");
            }
            results.add(id);
            boolean replace = true;
            Integer order = entry.containsKey("order") ? Integer.parseInt((String) entry.get("order")) : null;
            String icon = (String) entry.getOrDefault("icon", "");
            Boolean toggle = entry.containsKey("render_toggle") ? Boolean.parseBoolean((String) entry.get("render_toggle")) : null;
            Boolean cosmetic = entry.containsKey("add_cosmetic") ? Boolean.parseBoolean((String) entry.get("add_cosmetic")) : null;
            Boolean nativeGui = entry.containsKey("use_native_gui") ? Boolean.parseBoolean((String) entry.get("use_native_gui")) : null;
            if (order != null) {
                builder.order(order, replace);
            }
            if (!icon.isEmpty()) {
                builder.icon(new ResourceLocation(icon));
            }
            if (!dropRule.isEmpty()) {
                builder.dropRule(dropRule);
            }
            if (size != null) {
                builder.size(size, operation, replace);
            }
            if (cosmetic != null) {
                builder.hasCosmetic(cosmetic, replace);
            }
            if (nativeGui != null) {
                builder.useNativeGui(nativeGui, replace);
            }
            if (toggle != null) {
                builder.renderToggle(toggle, replace);
            }
        }
        return results;
    }

    public static void fromJson(SlotType.Builder builder, JsonObject jsonObject) throws IllegalArgumentException, JsonParseException {
        Integer jsonSize = jsonObject.has("size") ? GsonHelper.getAsInt(jsonObject, "size") : null;
        if (jsonSize != null && jsonSize < 0) {
            throw new IllegalArgumentException("Size cannot be less than 0!");
        } else {
            String operation = GsonHelper.getAsString(jsonObject, "operation", "SET");
            if (!operation.equals("SET") && !operation.equals("ADD") && !operation.equals("REMOVE")) {
                throw new IllegalArgumentException(operation + " is not a valid operation!");
            } else {
                String jsonDropRule = GsonHelper.getAsString(jsonObject, "drop_rule", "");
                if (!jsonDropRule.isEmpty() && !EnumUtils.isValidEnum(ICurio.DropRule.class, jsonDropRule)) {
                    throw new IllegalArgumentException(jsonDropRule + " is not a valid drop rule!");
                } else {
                    boolean replace = GsonHelper.getAsBoolean(jsonObject, "replace", false);
                    Integer jsonOrder = jsonObject.has("order") ? GsonHelper.getAsInt(jsonObject, "order") : null;
                    String jsonIcon = GsonHelper.getAsString(jsonObject, "icon", "");
                    Boolean jsonToggle = jsonObject.has("render_toggle") ? GsonHelper.getAsBoolean(jsonObject, "render_toggle") : null;
                    Boolean jsonCosmetic = jsonObject.has("add_cosmetic") ? GsonHelper.getAsBoolean(jsonObject, "add_cosmetic") : null;
                    Boolean jsonNative = jsonObject.has("use_native_gui") ? GsonHelper.getAsBoolean(jsonObject, "use_native_gui") : null;
                    JsonArray jsonSlotResultPredicate = jsonObject.has("validators") ? GsonHelper.getAsJsonArray(jsonObject, "validators") : null;
                    if (jsonOrder != null) {
                        builder.order(jsonOrder, replace);
                    }
                    if (!jsonIcon.isEmpty()) {
                        builder.icon(new ResourceLocation(jsonIcon));
                    }
                    if (!jsonDropRule.isEmpty()) {
                        builder.dropRule(jsonDropRule);
                    }
                    if (jsonSize != null) {
                        builder.size(jsonSize, operation, replace);
                    }
                    if (jsonCosmetic != null) {
                        builder.hasCosmetic(jsonCosmetic, replace);
                    }
                    if (jsonNative != null) {
                        builder.useNativeGui(jsonNative, replace);
                    }
                    if (jsonToggle != null) {
                        builder.renderToggle(jsonToggle, replace);
                    }
                    if (jsonSlotResultPredicate != null) {
                        for (JsonElement jsonElement : jsonSlotResultPredicate) {
                            builder.validator(new ResourceLocation(jsonElement.getAsString()));
                        }
                    }
                }
            }
        }
    }
}