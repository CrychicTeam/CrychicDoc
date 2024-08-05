package harmonised.pmmo.config.writers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import harmonised.pmmo.api.APIUtils;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.config.codecs.EnhancementsData;
import harmonised.pmmo.config.codecs.LocationData;
import harmonised.pmmo.config.codecs.ObjectData;
import harmonised.pmmo.config.codecs.PlayerData;
import harmonised.pmmo.config.codecs.VeinData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.nbt.LogicEntry;
import harmonised.pmmo.features.autovalues.AutoValues;
import harmonised.pmmo.util.Functions;
import harmonised.pmmo.util.Reference;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagFile;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistries;

public class PackGenerator {

    public static final String PACKNAME = "generated_pack";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static boolean applyOverride = false;

    public static boolean applyDefaults = false;

    public static boolean applyDisabler = false;

    public static boolean applySimple = false;

    public static List<String> namespaceFilter = new ArrayList();

    public static Set<ServerPlayer> players = new HashSet();

    private static final PackGenerator.Filter defaultFilter = new PackGenerator.Filter(List.of(new PackGenerator.BlockFilter(Optional.empty(), Optional.of("pmmo"))));

    public static int generatePack(MinecraftServer server) {
        Path filepath = server.getWorldPath(LevelResource.DATAPACK_DIR).resolve("generated_pack");
        filepath.toFile().mkdirs();
        Path packPath = filepath.resolve("pack.mcmeta");
        try {
            Files.writeString(packPath, gson.toJson(getPackObject(applyDisabler)), Charset.defaultCharset(), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException var15) {
            System.out.println("Error While Generating pack.mcmeta for Generated Data: " + var15.toString());
        }
        for (PackGenerator.Category category : PackGenerator.Category.values()) {
            for (ResourceLocation id : !namespaceFilter.isEmpty() && category != PackGenerator.Category.TAGS ? ((Set) category.valueList.apply(server)).stream().filter(idx -> namespaceFilter.contains(idx.getNamespace())).toList() : (Collection) category.valueList.apply(server)) {
                int index = id.getPath().lastIndexOf(47);
                String pathRoute = id.getPath().substring(0, index >= 0 ? index : 0);
                Path finalPath = filepath.resolve("data/" + id.getNamespace() + "/" + category.route + "/" + pathRoute);
                finalPath.toFile().mkdirs();
                try {
                    Files.writeString(finalPath.resolve(id.getPath().substring(id.getPath().lastIndexOf(47) + 1) + ".json"), (CharSequence) category.defaultData.apply(id), Charset.defaultCharset(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
                } catch (IOException var14) {
                    System.out.println("Error While Generating Pack File For: " + id.toString() + " (" + var14.toString() + ")");
                }
            }
        }
        generatePlayerConfigs(server, players);
        return 0;
    }

    public static int generatePlayerConfigs(MinecraftServer server, Collection<ServerPlayer> players) {
        Path filepath = server.getWorldPath(LevelResource.DATAPACK_DIR).resolve("generated_pack/data/minecraft/pmmo/players/");
        filepath.toFile().mkdirs();
        for (ServerPlayer player : players) {
            String idString = player.m_20148_().toString();
            try {
                Files.writeString(filepath.resolve(idString + ".json"), gson.toJson((JsonElement) PlayerData.CODEC.encodeStart(JsonOps.INSTANCE, new PlayerData()).result().get()), Charset.defaultCharset(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            } catch (IOException var7) {
                System.out.println("Error While Generating Pack File For: " + idString.toString() + " (" + var7.toString() + ")");
            }
        }
        return 0;
    }

    private static JsonElement getPackObject(boolean isDisabler) {
        PackGenerator.McMeta pack = new PackGenerator.McMeta(new PackGenerator.Pack(isDisabler ? "Generated Resources including a disabler filter for PMMO's defaults" : "Generated Resources", 9), isDisabler ? Optional.of(defaultFilter) : Optional.empty());
        return (JsonElement) PackGenerator.McMeta.CODEC.encodeStart(JsonOps.INSTANCE, pack).result().get();
    }

    private static record BlockFilter(Optional<String> namespace, Optional<String> path) {

        public static final Codec<PackGenerator.BlockFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.optionalFieldOf("namespace").forGetter(PackGenerator.BlockFilter::namespace), Codec.STRING.optionalFieldOf("path").forGetter(PackGenerator.BlockFilter::path)).apply(instance, PackGenerator.BlockFilter::new));
    }

    private static enum Category {

        ITEMS("pmmo/items", server -> ForgeRegistries.ITEMS.getKeys(), id -> {
            Core core = Core.get(LogicalSide.SERVER);
            ObjectData existing = core.getLoader().ITEM_LOADER.getData(id);
            ObjectData data = new ObjectData(PackGenerator.applyOverride, new HashSet(), (Map<ReqType, Map<String, Integer>>) ((Map) Arrays.stream(ReqType.ITEM_APPLICABLE_EVENTS).collect(Collectors.toMap(r -> r, r -> (Map) (PackGenerator.applyDefaults ? (Map) existing.reqs().getOrDefault(r, AutoValues.getRequirements(r, id, ObjectType.ITEM)) : new HashMap())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((Map) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (Map) e.getValue())), (Map<ReqType, List<LogicEntry>>) ((Map) Arrays.stream(ReqType.ITEM_APPLICABLE_EVENTS).collect(Collectors.toMap(r -> r, r -> (List) (PackGenerator.applyDefaults ? (List) existing.nbtReqs().getOrDefault(r, new ArrayList()) : new ArrayList())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((List) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (List) e.getValue())), (Map<ResourceLocation, Integer>) (PackGenerator.applyDefaults ? existing.negativeEffects() : new HashMap()), (Map<EventType, Map<String, Long>>) ((Map) Arrays.stream(EventType.ITEM_APPLICABLE_EVENTS).collect(Collectors.toMap(e -> e, e -> (Map) (PackGenerator.applyDefaults ? (Map) existing.xpValues().getOrDefault(e, AutoValues.getExperienceAward(e, id, ObjectType.ITEM)) : new HashMap())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((Map) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (Map) e.getValue())), (Map<EventType, Map<String, Map<String, Long>>>) Stream.of(EventType.RECEIVE_DAMAGE, EventType.DEAL_DAMAGE).collect(Collectors.toMap(e -> e, e -> (Map) (PackGenerator.applyDefaults ? (Map) existing.damageXpValues().getOrDefault(e, new HashMap()) : new HashMap()))), (Map<EventType, List<LogicEntry>>) ((Map) Arrays.stream(EventType.ITEM_APPLICABLE_EVENTS).collect(Collectors.toMap(e -> e, e -> (List) (PackGenerator.applyDefaults ? (List) existing.nbtXpValues().getOrDefault(e, new ArrayList()) : new ArrayList())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((List) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (List) e.getValue())), (Map<ModifierDataType, Map<String, Double>>) ((Map) Arrays.stream(new ModifierDataType[] { ModifierDataType.WORN, ModifierDataType.HELD }).collect(Collectors.toMap(m -> m, m -> (Map) (PackGenerator.applyDefaults ? (Map) existing.bonuses().getOrDefault(m, new HashMap()) : new HashMap())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((Map) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (Map) e.getValue())), (Map<ModifierDataType, List<LogicEntry>>) ((Map) Arrays.stream(new ModifierDataType[] { ModifierDataType.WORN, ModifierDataType.HELD }).collect(Collectors.toMap(m -> m, m -> (List) (PackGenerator.applyDefaults ? (List) existing.nbtBonuses().getOrDefault(m, new ArrayList()) : new ArrayList())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((List) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, m -> (List) m.getValue())), PackGenerator.applyDefaults ? existing.salvage() : Map.of(new ResourceLocation("modid:item"), APIUtils.SalvageBuilder.start().build()), PackGenerator.applyDefaults ? existing.veinData() : VeinData.EMPTY);
            JsonObject raw = ((JsonElement) ObjectData.CODEC.encodeStart(JsonOps.INSTANCE, data).result().get()).getAsJsonObject();
            return PackGenerator.gson.toJson(raw);
        }),
        BLOCKS("pmmo/blocks", server -> ForgeRegistries.BLOCKS.getKeys(), id -> {
            Core core = Core.get(LogicalSide.SERVER);
            ObjectData existing = core.getLoader().BLOCK_LOADER.getData(id);
            ObjectData data = new ObjectData(PackGenerator.applyOverride, new HashSet(), (Map<ReqType, Map<String, Integer>>) ((Map) Arrays.stream(ReqType.BLOCK_APPLICABLE_EVENTS).collect(Collectors.toMap(r -> r, r -> (Map) (PackGenerator.applyDefaults ? (Map) existing.reqs().getOrDefault(r, AutoValues.getRequirements(r, id, ObjectType.BLOCK)) : new HashMap())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((Map) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (Map) e.getValue())), (Map<ReqType, List<LogicEntry>>) ((Map) Arrays.stream(ReqType.BLOCK_APPLICABLE_EVENTS).collect(Collectors.toMap(r -> r, r -> (List) (PackGenerator.applyDefaults ? (List) existing.nbtReqs().getOrDefault(r, new ArrayList()) : new ArrayList())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((List) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (List) e.getValue())), new HashMap(), (Map<EventType, Map<String, Long>>) ((Map) Arrays.stream(EventType.BLOCK_APPLICABLE_EVENTS).collect(Collectors.toMap(e -> e, e -> (Map) (PackGenerator.applyDefaults ? (Map) existing.xpValues().getOrDefault(e, AutoValues.getExperienceAward(e, id, ObjectType.BLOCK)) : new HashMap())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((Map) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (Map) e.getValue())), new HashMap(), (Map<EventType, List<LogicEntry>>) ((Map) Arrays.stream(EventType.BLOCK_APPLICABLE_EVENTS).collect(Collectors.toMap(e -> e, e -> (List) (PackGenerator.applyDefaults ? (List) existing.nbtXpValues().getOrDefault(e, new ArrayList()) : new ArrayList())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((List) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (List) e.getValue())), new HashMap(), new HashMap(), new HashMap(), PackGenerator.applyDefaults ? existing.veinData() : new VeinData(Optional.empty(), Optional.empty(), Optional.of(1)));
            JsonObject raw = ((JsonElement) ObjectData.CODEC.encodeStart(JsonOps.INSTANCE, data).result().get()).getAsJsonObject();
            raw.remove("negative_effect");
            raw.remove("bonuses");
            raw.remove("dealt_damage_xp");
            raw.remove("received_damage_xp");
            raw.remove("nbt_bonuses");
            raw.remove("salvage");
            return PackGenerator.gson.toJson(raw);
        }),
        ENTITIES("pmmo/entities", server -> ForgeRegistries.ENTITY_TYPES.getKeys(), id -> {
            Core core = Core.get(LogicalSide.SERVER);
            ObjectData existing = core.getLoader().ENTITY_LOADER.getData(id);
            ObjectData data = new ObjectData(PackGenerator.applyOverride, new HashSet(), (Map<ReqType, Map<String, Integer>>) ((Map) Arrays.stream(ReqType.ENTITY_APPLICABLE_EVENTS).collect(Collectors.toMap(r -> r, r -> (Map) (PackGenerator.applyDefaults ? (Map) existing.reqs().getOrDefault(r, AutoValues.getRequirements(r, id, ObjectType.ENTITY)) : new HashMap())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((Map) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (Map) e.getValue())), (Map<ReqType, List<LogicEntry>>) ((Map) Arrays.stream(ReqType.ENTITY_APPLICABLE_EVENTS).collect(Collectors.toMap(r -> r, r -> (List) (PackGenerator.applyDefaults ? (List) existing.nbtReqs().getOrDefault(r, new ArrayList()) : new ArrayList())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((List) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (List) e.getValue())), new HashMap(), (Map<EventType, Map<String, Long>>) ((Map) Arrays.stream(EventType.ENTITY_APPLICABLE_EVENTS).collect(Collectors.toMap(e -> e, e -> (Map) (PackGenerator.applyDefaults ? (Map) existing.xpValues().getOrDefault(e, AutoValues.getExperienceAward(e, id, ObjectType.ENTITY)) : new HashMap())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((Map) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (Map) e.getValue())), (Map<EventType, Map<String, Map<String, Long>>>) Stream.of(EventType.RECEIVE_DAMAGE, EventType.DEAL_DAMAGE).collect(Collectors.toMap(e -> e, e -> (Map) (PackGenerator.applyDefaults ? (Map) existing.damageXpValues().getOrDefault(e, new HashMap()) : new HashMap()))), (Map<EventType, List<LogicEntry>>) ((Map) Arrays.stream(EventType.ENTITY_APPLICABLE_EVENTS).collect(Collectors.toMap(e -> e, e -> (List) (PackGenerator.applyDefaults ? (List) existing.nbtXpValues().getOrDefault(e, new ArrayList()) : new ArrayList())))).entrySet().stream().filter(entry -> PackGenerator.applySimple && !((List) entry.getValue()).isEmpty() || !PackGenerator.applySimple).collect(Collectors.toMap(Entry::getKey, e -> (List) e.getValue())), new HashMap(), new HashMap(), new HashMap(), VeinData.EMPTY);
            JsonObject raw = ((JsonElement) ObjectData.CODEC.encodeStart(JsonOps.INSTANCE, data).result().get()).getAsJsonObject();
            raw.remove("negative_effect");
            raw.remove("bonuses");
            raw.remove("nbt_bonuses");
            raw.remove("salvage");
            raw.remove("vein_data");
            return PackGenerator.gson.toJson(raw);
        }),
        DIMENSIONS("pmmo/dimensions", server -> new HashSet(server.levelKeys().stream().map(key -> key.location()).toList()), id -> {
            Core core = Core.get(LogicalSide.SERVER);
            LocationData existing = core.getLoader().DIMENSION_LOADER.getData(id);
            LocationData data = new LocationData(PackGenerator.applyOverride, new HashSet(), PackGenerator.applyDefaults ? existing.bonusMap() : Map.of(ModifierDataType.DIMENSION, new HashMap()), new HashMap(), new HashMap(), (List<ResourceLocation>) (PackGenerator.applyDefaults ? existing.veinBlacklist() : new ArrayList()), (Map<String, Integer>) (PackGenerator.applyDefaults ? existing.travelReq() : new HashMap()), (Map<ResourceLocation, Map<String, Double>>) (PackGenerator.applyDefaults ? existing.mobModifiers() : new HashMap()));
            JsonObject raw = ((JsonElement) LocationData.CODEC.encodeStart(JsonOps.INSTANCE, data).result().get()).getAsJsonObject();
            raw.remove("positive_effect");
            raw.remove("negative_effect");
            raw.remove("isTagFor");
            return PackGenerator.gson.toJson(raw);
        }),
        BIOMES("pmmo/biomes", server -> server.registryAccess().m_175515_(Registries.BIOME).keySet(), id -> {
            Core core = Core.get(LogicalSide.SERVER);
            LocationData existing = core.getLoader().BIOME_LOADER.getData(id);
            LocationData data = new LocationData(PackGenerator.applyOverride, new HashSet(), PackGenerator.applyDefaults ? existing.bonusMap() : Map.of(ModifierDataType.BIOME, new HashMap()), (Map<ResourceLocation, Integer>) (PackGenerator.applyDefaults ? existing.positive() : new HashMap()), (Map<ResourceLocation, Integer>) (PackGenerator.applyDefaults ? existing.negative() : new HashMap()), (List<ResourceLocation>) (PackGenerator.applyDefaults ? existing.veinBlacklist() : new ArrayList()), (Map<String, Integer>) (PackGenerator.applyDefaults ? existing.travelReq() : new HashMap()), (Map<ResourceLocation, Map<String, Double>>) (PackGenerator.applyDefaults ? existing.mobModifiers() : new HashMap()));
            JsonObject raw = ((JsonElement) LocationData.CODEC.encodeStart(JsonOps.INSTANCE, data).result().get()).getAsJsonObject();
            return PackGenerator.gson.toJson(raw);
        }),
        ENCHANTMENTS("pmmo/enchantments", server -> ForgeRegistries.ENCHANTMENTS.getKeys(), id -> {
            Core core = Core.get(LogicalSide.SERVER);
            EnhancementsData existing = core.getLoader().ENCHANTMENT_LOADER.getData(id);
            return PackGenerator.gson.toJson((JsonElement) EnhancementsData.CODEC.encodeStart(JsonOps.INSTANCE, new EnhancementsData(PackGenerator.applyOverride, (Map<Integer, Map<String, Integer>>) (PackGenerator.applyDefaults ? existing.skillArray() : new HashMap()))).result().get());
        }),
        EFFECTS("pmmo/effects", server -> ForgeRegistries.MOB_EFFECTS.getKeys(), id -> {
            Core core = Core.get(LogicalSide.SERVER);
            EnhancementsData existing = core.getLoader().EFFECT_LOADER.getData(id);
            return PackGenerator.gson.toJson((JsonElement) EnhancementsData.CODEC.encodeStart(JsonOps.INSTANCE, new EnhancementsData(PackGenerator.applyOverride, (Map<Integer, Map<String, Integer>>) (PackGenerator.applyDefaults ? existing.skillArray() : new HashMap()))).result().get());
        }),
        TAGS("tags", server -> Set.of(Functions.pathPrepend(Reference.CROPS.location(), "blocks"), Functions.pathPrepend(Reference.CASCADING_BREAKABLES.location(), "blocks"), Functions.pathPrepend(Reference.BREEDABLE_TAG.location(), "entity_types"), Functions.pathPrepend(Reference.NO_XP_DAMAGE_DEALT.location(), "entity_types"), Functions.pathPrepend(Reference.MOB_TAG.location(), "entity_types"), Functions.pathPrepend(Reference.RIDEABLE_TAG.location(), "entity_types"), Functions.pathPrepend(Reference.TAMABLE_TAG.location(), "entity_types"), Functions.pathPrepend(Reference.BREWABLES.location(), "items"), Functions.pathPrepend(Reference.SMELTABLES.location(), "items"), Functions.pathPrepend(Reference.FROM_ENVIRONMENT.location(), "damage_type"), Functions.pathPrepend(Reference.FROM_IMPACT.location(), "damage_type"), Functions.pathPrepend(Reference.FROM_MAGIC.location(), "damage_type")), id -> PackGenerator.gson.toJson((JsonElement) TagFile.CODEC.encodeStart(JsonOps.INSTANCE, new TagFile(List.of(), false)).result().get()));

        public String route;

        public Function<MinecraftServer, Set<ResourceLocation>> valueList;

        private Function<ResourceLocation, String> defaultData;

        private Category(String route, Function<MinecraftServer, Set<ResourceLocation>> values, Function<ResourceLocation, String> defaultData) {
            this.route = route;
            this.valueList = values;
            this.defaultData = defaultData;
        }
    }

    private static record Filter(List<PackGenerator.BlockFilter> block) {

        public static final Codec<PackGenerator.Filter> CODEC = RecordCodecBuilder.create(instance -> instance.group(PackGenerator.BlockFilter.CODEC.listOf().fieldOf("block").forGetter(PackGenerator.Filter::block)).apply(instance, PackGenerator.Filter::new));
    }

    private static record McMeta(PackGenerator.Pack pack, Optional<PackGenerator.Filter> filter) {

        public static final Codec<PackGenerator.McMeta> CODEC = RecordCodecBuilder.create(instance -> instance.group(PackGenerator.Pack.CODEC.fieldOf("pack").forGetter(PackGenerator.McMeta::pack), PackGenerator.Filter.CODEC.optionalFieldOf("filter").forGetter(PackGenerator.McMeta::filter)).apply(instance, PackGenerator.McMeta::new));
    }

    private static record Pack(String description, int format) {

        public static final Codec<PackGenerator.Pack> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("description").forGetter(PackGenerator.Pack::description), Codec.INT.fieldOf("pack_format").forGetter(PackGenerator.Pack::format)).apply(instance, PackGenerator.Pack::new));
    }
}