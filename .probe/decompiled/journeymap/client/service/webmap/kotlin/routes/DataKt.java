package journeymap.client.service.webmap.kotlin.routes;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.Pair;
import info.journeymap.shaded.kotlin.kotlin.TuplesKt;
import info.journeymap.shaded.kotlin.kotlin.collections.CollectionsKt;
import info.journeymap.shaded.kotlin.kotlin.collections.MapsKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.spark.kotlin.RouteHandler;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import journeymap.client.data.AllData;
import journeymap.client.data.DataCache;
import journeymap.client.data.ImagesData;
import journeymap.client.model.EntityDTO;
import journeymap.client.waypoint.Waypoint;
import journeymap.common.Journeymap;
import org.apache.logging.log4j.Logger;

@Metadata(mv = { 1, 8, 0 }, k = 2, xi = 48, d1 = { "\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0000\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0000\u001a\u0016\u0010\r\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u000e*\u0004\u0018\u00010\nH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f" }, d2 = { "GSON", "Lcom/google/gson/Gson;", "dataTypesRequiringSince", "", "", "getDataTypesRequiringSince", "()Ljava/util/List;", "logger", "Lorg/apache/logging/log4j/Logger;", "dataGet", "", "handler", "Linfo/journeymap/shaded/kotlin/spark/kotlin/RouteHandler;", "modulesAreTerrible", "", "journeymap" })
public final class DataKt {

    @NotNull
    private static final Gson GSON;

    @NotNull
    private static final Logger logger;

    @NotNull
    private static final List<String> dataTypesRequiringSince;

    @NotNull
    public static final List<String> getDataTypesRequiringSince() {
        return dataTypesRequiringSince;
    }

    private static final Map<?, ?> modulesAreTerrible(Object $this$modulesAreTerrible) {
        Map entry = (Map) $this$modulesAreTerrible != null ? MapsKt.toMutableMap((Map) $this$modulesAreTerrible) : null;
        Map var10000;
        if (entry == null || entry.isEmpty()) {
            Pair[] var2 = new Pair[] { TuplesKt.to("//", null) };
            var10000 = MapsKt.mutableMapOf(var2);
        } else {
            var10000 = entry;
        }
        return var10000;
    }

    @NotNull
    public static final Object dataGet(@NotNull RouteHandler handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        Long since = handler.queryMap("images.since").longValue();
        String type = handler.params("type");
        if (dataTypesRequiringSince.contains(type) && since == null) {
            logger.warn("Data type '" + type + "' requested without 'images.since' parameter");
            handler.status(400);
            return "Data type '" + type + "' requires 'images.since' parameter.";
        } else {
            DataCache var14;
            label68: {
                switch(type.hashCode()) {
                    case -1185250696:
                        if (type.equals("images")) {
                            Intrinsics.checkNotNull(since);
                            var14 = new ImagesData(since);
                            break label68;
                        }
                        break;
                    case -985752863:
                        if (type.equals("player")) {
                            var14 = DataCache.INSTANCE.getPlayer(false);
                            break label68;
                        }
                        break;
                    case -856935945:
                        if (type.equals("animals")) {
                            var14 = modulesAreTerrible(DataCache.INSTANCE.getAnimals(false));
                            break label68;
                        }
                        break;
                    case -493567566:
                        if (type.equals("players")) {
                            var14 = modulesAreTerrible(DataCache.INSTANCE.getPlayers(false));
                            break label68;
                        }
                        break;
                    case -462094004:
                        if (type.equals("messages")) {
                            var14 = DataCache.INSTANCE.getMessages(false);
                            break label68;
                        }
                        break;
                    case 96673:
                        if (type.equals("all")) {
                            var14 = DataCache.INSTANCE;
                            Intrinsics.checkNotNull(since);
                            Map it = var14.getAll(since);
                            ???;
                            Intrinsics.checkNotNullExpressionValue(it, "it");
                            Map map = MapsKt.toMutableMap(it);
                            map.put(AllData.Key.animals, modulesAreTerrible(map.get(AllData.Key.animals)));
                            map.put(AllData.Key.mobs, modulesAreTerrible(map.get(AllData.Key.mobs)));
                            map.put(AllData.Key.players, modulesAreTerrible(map.get(AllData.Key.players)));
                            map.put(AllData.Key.villagers, modulesAreTerrible(map.get(AllData.Key.villagers)));
                            map.put(AllData.Key.waypoints, modulesAreTerrible(map.get(AllData.Key.waypoints)));
                            var14 = map;
                            break label68;
                        }
                        break;
                    case 3357043:
                        if (type.equals("mobs")) {
                            var14 = modulesAreTerrible(DataCache.INSTANCE.getMobs(false));
                            break label68;
                        }
                        break;
                    case 31078381:
                        if (type.equals("villagers")) {
                            var14 = modulesAreTerrible(DataCache.INSTANCE.getVillagers(false));
                            break label68;
                        }
                        break;
                    case 113318802:
                        if (type.equals("world")) {
                            var14 = DataCache.INSTANCE.getWorld(false);
                            break label68;
                        }
                        break;
                    case 241170578:
                        if (type.equals("waypoints")) {
                            var14 = DataCache.INSTANCE.getWaypoints(false);
                            Intrinsics.checkNotNullExpressionValue(var14, "INSTANCE.getWaypoints(false)");
                            Collection waypoints = (Collection) var14;
                            Map wpMap = (Map) (new LinkedHashMap());
                            for (Waypoint waypoint : waypoints) {
                                var14 = waypoint.getId();
                                Intrinsics.checkNotNullExpressionValue(var14, "waypoint.id");
                                wpMap.put(var14, waypoint);
                            }
                            var14 = modulesAreTerrible(MapsKt.toMap(wpMap));
                            break label68;
                        }
                }
                var14 = null;
            }
            Object data = var14;
            if (data == null) {
                logger.warn("Unknown data type '" + type + "'");
                handler.status(400);
                return "Unknown data type '" + type + "'";
            } else {
                handler.getResponse().raw().setContentType("application/json");
                String var16 = GSON.toJson(data);
                Intrinsics.checkNotNullExpressionValue(var16, "GSON.toJson(data)");
                return var16;
            }
        }
    }

    static {
        GsonBuilder var10000 = new GsonBuilder().setPrettyPrinting();
        ExclusionStrategy[] var1 = new ExclusionStrategy[] { new EntityDTO.EntityDTOExclusionStrategy() };
        Gson var2 = var10000.setExclusionStrategies(var1).create();
        Intrinsics.checkNotNullExpressionValue(var2, "GsonBuilder().setPrettyP…usionStrategy()).create()");
        GSON = var2;
        Logger var3 = Journeymap.getLogger("webmap/routes/data");
        Intrinsics.checkNotNullExpressionValue(var3, "getLogger(\"webmap/routes/data\")");
        logger = var3;
        String[] var0 = new String[] { "all", "images" };
        dataTypesRequiringSince = CollectionsKt.listOf(var0);
    }
}