package journeymap.client.service.webmap.kotlin.routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.spark.kotlin.RouteHandler;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import journeymap.client.JourneymapClient;
import journeymap.client.io.FileHandler;
import journeymap.client.io.MapSaver;
import journeymap.client.model.MapType;
import journeymap.client.task.multi.MapRegionTask;
import journeymap.client.task.multi.SaveMapTask;
import journeymap.common.Journeymap;
import journeymap.common.helper.DimensionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.Logger;

@Metadata(mv = { 1, 8, 0 }, k = 2, xi = 48, d1 = { "\u0000*\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0000\u001a \u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0000\u001a \u0010\r\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e" }, d2 = { "GSON", "Lcom/google/gson/Gson;", "logger", "Lorg/apache/logging/log4j/Logger;", "actionGet", "", "handler", "Linfo/journeymap/shaded/kotlin/spark/kotlin/RouteHandler;", "autoMap", "minecraft", "Lnet/minecraft/client/Minecraft;", "level", "Lnet/minecraft/world/level/Level;", "saveMap", "journeymap" })
public final class ActionKt {

    @NotNull
    private static final Gson GSON;

    @NotNull
    private static final Logger logger;

    @NotNull
    public static final Object actionGet(@NotNull RouteHandler handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        Minecraft var10000 = Minecraft.getInstance();
        Intrinsics.checkNotNullExpressionValue(var10000, "getInstance()");
        Minecraft minecraft = var10000;
        Level level = minecraft.level;
        if (level == null) {
            logger.warn("Action requested before world loaded");
            handler.status(400);
            return "World not loaded";
        } else if (!JourneymapClient.getInstance().isMapping()) {
            logger.warn("Action requested before Journeymap started");
            handler.status(400);
            return "JourneyMap is still starting";
        } else {
            String type = handler.params("type");
            if (Intrinsics.areEqual(type, "automap")) {
                var10000 = (Minecraft) autoMap(handler, minecraft, level);
            } else if (Intrinsics.areEqual(type, "savemap")) {
                var10000 = (Minecraft) saveMap(handler, minecraft, level);
            } else {
                logger.warn("Unknown action type '" + type + "'");
                handler.status(400);
                var10000 = "Unknown action type '" + type + "'";
            }
            return var10000;
        }
    }

    @NotNull
    public static final Object saveMap(@NotNull RouteHandler handler, @NotNull Minecraft minecraft, @NotNull Level level) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        Intrinsics.checkNotNullParameter(minecraft, "minecraft");
        Intrinsics.checkNotNullParameter(level, "level");
        File var10000 = FileHandler.getJMWorldDir(minecraft);
        Intrinsics.checkNotNullExpressionValue(var10000, "getJMWorldDir(minecraft)");
        File worldDir = var10000;
        if (worldDir.exists() && worldDir.isDirectory()) {
            String var17 = handler.getRequest().queryParamOrDefault("dim", "overworld");
            Intrinsics.checkNotNullExpressionValue(var17, "handler.request.queryPar…fault(\"dim\", \"overworld\")");
            String dimension = var17;
            String var18 = handler.getRequest().queryParamOrDefault("mapType", MapType.Name.day.name());
            Intrinsics.checkNotNullExpressionValue(var18, "handler.request.queryPar…\", MapType.Name.day.name)");
            String mapTypeString = var18;
            Integer vSlice = handler.queryMap("depth").integerValue();
            MapType.Name mapTypeName = null;
            try {
                mapTypeName = MapType.Name.valueOf(mapTypeString);
            } catch (IllegalArgumentException var15) {
                logger.warn("Invalid map type '" + mapTypeString + "'");
                handler.status(400);
                return "Invalid map type '" + mapTypeString + "'";
            }
            if (mapTypeName != MapType.Name.underground) {
                vSlice = null;
            }
            boolean hardcore = level.getLevelData().isHardcore();
            MapType var19 = MapType.from(mapTypeName, vSlice, DimensionHelper.getWorldKeyForName(dimension));
            Intrinsics.checkNotNullExpressionValue(var19, "from(mapTypeName, vSlice…rldKeyForName(dimension))");
            MapType mapType = var19;
            if (mapType.isUnderground() && hardcore) {
                logger.warn("Cave mapping is not allowed on hardcore servers");
                handler.status(400);
                return "Cave mapping is not allowed on hardcore servers";
            } else {
                MapSaver mapSaver = new MapSaver(worldDir, mapType);
                if (!mapSaver.isValid()) {
                    logger.info("No image files to save");
                    handler.status(400);
                    return "No image files to save";
                } else {
                    JourneymapClient.getInstance().toggleTask(SaveMapTask.Manager.class, true, mapSaver);
                    Map data = (Map) (new LinkedHashMap());
                    String var13 = "filename";
                    String var20 = mapSaver.getSaveFileName();
                    Intrinsics.checkNotNullExpressionValue(var20, "mapSaver.saveFileName");
                    String var14 = var20;
                    data.put(var13, var14);
                    handler.getResponse().raw().setContentType("application/json");
                    String var21 = GSON.toJson(data);
                    Intrinsics.checkNotNullExpressionValue(var21, "GSON.toJson(data)");
                    return var21;
                }
            }
        } else {
            logger.warn("JM world directory not found");
            handler.status(500);
            return "Unable to find JourneyMap world directory";
        }
    }

    @NotNull
    public static final Object autoMap(@NotNull RouteHandler handler, @NotNull Minecraft minecraft, @NotNull Level level) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        Intrinsics.checkNotNullParameter(minecraft, "minecraft");
        Intrinsics.checkNotNullParameter(level, "level");
        Map data = (Map) (new LinkedHashMap());
        boolean enabled = JourneymapClient.getInstance().isTaskManagerEnabled(MapRegionTask.Manager.class);
        String var10000 = handler.getRequest().queryParamOrDefault("scope", "stop");
        Intrinsics.checkNotNullExpressionValue(var10000, "handler.request.queryPar…rDefault(\"scope\", \"stop\")");
        String scope = var10000;
        if (Intrinsics.areEqual(scope, "stop") && enabled) {
            JourneymapClient.getInstance().toggleTask(MapRegionTask.Manager.class, false, false);
            data.put("message", "automap_complete");
        } else if (!enabled) {
            boolean doAll = Intrinsics.areEqual(scope, "all");
            JourneymapClient.getInstance().toggleTask(MapRegionTask.Manager.class, true, doAll);
            data.put("message", "automap_started");
        } else {
            data.put("message", "automap_already_started");
        }
        handler.getResponse().raw().setContentType("application/json");
        var10000 = GSON.toJson(data);
        Intrinsics.checkNotNullExpressionValue(var10000, "GSON.toJson(data)");
        return var10000;
    }

    static {
        Gson var10000 = new GsonBuilder().setPrettyPrinting().create();
        Intrinsics.checkNotNullExpressionValue(var10000, "GsonBuilder().setPrettyPrinting().create()");
        GSON = var10000;
        Logger var0 = Journeymap.getLogger("webmap/routes/action");
        Intrinsics.checkNotNullExpressionValue(var0, "getLogger(\"webmap/routes/action\")");
        logger = var0;
    }
}