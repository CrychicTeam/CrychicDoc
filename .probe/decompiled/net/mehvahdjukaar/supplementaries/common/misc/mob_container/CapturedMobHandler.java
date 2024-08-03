package net.mehvahdjukaar.supplementaries.common.misc.mob_container;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.SuppPlatformStuff;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.api.ICatchableMob;
import net.mehvahdjukaar.supplementaries.common.network.ClientBoundSyncCapturedMobsPacket;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class CapturedMobHandler extends SimpleJsonResourceReloadListener {

    private static final Set<String> COMMAND_MOBS = new HashSet();

    private static final Map<EntityType<?>, DataDefinedCatchableMob> CUSTOM_MOB_PROPERTIES = new IdentityHashMap();

    private static DataDefinedCatchableMob moddedFishProperty;

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final CapturedMobHandler RELOAD_INSTANCE = new CapturedMobHandler();

    private CapturedMobHandler() {
        super(GSON, "catchable_mobs_properties");
    }

    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
        CUSTOM_MOB_PROPERTIES.clear();
        ArrayList<DataDefinedCatchableMob> list = new ArrayList();
        jsons.forEach((key, json) -> {
            DataResult<DataDefinedCatchableMob> v = DataDefinedCatchableMob.CODEC.parse(JsonOps.INSTANCE, json);
            DataDefinedCatchableMob data = (DataDefinedCatchableMob) v.getOrThrow(false, e -> Supplementaries.LOGGER.error("failed to parse captured mob properties: {}", e));
            if (key.getPath().equals("generic_fish")) {
                moddedFishProperty = data;
            } else {
                list.add(data);
            }
        });
        for (DataDefinedCatchableMob c : list) {
            for (ResourceLocation o : c.getOwners()) {
                BuiltInRegistries.ENTITY_TYPE.m_6612_(o).ifPresent(e -> CUSTOM_MOB_PROPERTIES.put(e, c));
            }
        }
        if (moddedFishProperty == null) {
            Supplementaries.LOGGER.error("Failed to find json for 'generic_fish'. How? Found jsons were : {}", jsons.keySet());
        }
    }

    public static void sendDataToClient(ServerPlayer player) {
        Set<DataDefinedCatchableMob> set = new HashSet(CUSTOM_MOB_PROPERTIES.values());
        ModNetwork.CHANNEL.sendToClientPlayer(player, new ClientBoundSyncCapturedMobsPacket(set, moddedFishProperty));
    }

    public static void acceptClientData(Set<DataDefinedCatchableMob> list, @Nullable DataDefinedCatchableMob defaultFish) {
        if (defaultFish != null) {
            moddedFishProperty = defaultFish;
        }
        CUSTOM_MOB_PROPERTIES.clear();
        for (DataDefinedCatchableMob c : list) {
            for (ResourceLocation o : c.getOwners()) {
                BuiltInRegistries.ENTITY_TYPE.m_6612_(o).ifPresent(e -> CUSTOM_MOB_PROPERTIES.put(e, c));
            }
        }
    }

    public static ICatchableMob getDataCap(EntityType<?> type, boolean isFish) {
        DataDefinedCatchableMob c = (DataDefinedCatchableMob) CUSTOM_MOB_PROPERTIES.get(type);
        return c == null && isFish ? moddedFishProperty : c;
    }

    public static ICatchableMob getCatchableMobCapOrDefault(Entity entity) {
        if (entity instanceof ICatchableMob) {
            return (ICatchableMob) entity;
        } else {
            ICatchableMob forgeCap = SuppPlatformStuff.getForgeCap(entity, ICatchableMob.class);
            if (forgeCap != null) {
                return forgeCap;
            } else {
                ICatchableMob prop = getDataCap(entity.getType(), BucketHelper.isModdedFish(entity));
                return prop != null ? prop : ICatchableMob.DEFAULT;
            }
        }
    }

    public static void saveFile(DataDefinedCatchableMob data) {
        File folder = PlatHelper.getGamePath().resolve("test_cap").toFile();
        if (!folder.exists()) {
            folder.mkdir();
        }
        try {
            File exportPath = new File(folder, ((ResourceLocation) data.getOwners().get(0)).toString().replace(":", "_") + ".json");
            FileWriter writer = new FileWriter(exportPath);
            try {
                DataResult<JsonElement> j = DataDefinedCatchableMob.CODEC.encodeStart(JsonOps.INSTANCE, data);
                GSON.toJson(sortJson(((JsonElement) j.result().get()).getAsJsonObject()), writer);
            } catch (Throwable var7) {
                try {
                    writer.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
                throw var7;
            }
            writer.close();
        } catch (IOException var8) {
            var8.printStackTrace();
        }
    }

    private static JsonObject sortJson(JsonObject jsonObject) {
        try {
            Map<String, JsonElement> joToMap = new TreeMap();
            jsonObject.entrySet().forEach(e -> {
                JsonElement j = (JsonElement) e.getValue();
                if (j instanceof JsonObject jo) {
                    j = sortJson(jo);
                }
                joToMap.put((String) e.getKey(), j);
            });
            JsonObject sortedJSON = new JsonObject();
            joToMap.forEach(sortedJSON::add);
            return sortedJSON;
        } catch (Exception var3) {
            return jsonObject;
        }
    }

    public static boolean isCommandMob(String entity) {
        return COMMAND_MOBS.contains(entity);
    }

    public static void addCommandMob(String name) {
        COMMAND_MOBS.add(name);
    }
}