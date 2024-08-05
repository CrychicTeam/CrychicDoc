package journeymap.client.service.webmap.kotlin.routes;

import com.mojang.blaze3d.platform.NativeImage;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.io.FilesKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.math.MathKt;
import info.journeymap.shaded.kotlin.spark.kotlin.RouteHandler;
import info.journeymap.shaded.org.eclipse.jetty.io.EofException;
import info.journeymap.shaded.org.javax.servlet.ServletOutputStream;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.OutputStream;
import java.nio.channels.Channels;
import javax.imageio.IIOException;
import journeymap.client.JourneymapClient;
import journeymap.client.data.WorldData;
import journeymap.client.io.FileHandler;
import journeymap.client.io.RegionImageHandler;
import journeymap.client.model.MapType;
import journeymap.common.Journeymap;
import journeymap.common.helper.DimensionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.Logger;

@Metadata(mv = { 1, 8, 0 }, k = 2, xi = 48, d1 = { "\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006" }, d2 = { "logger", "Lorg/apache/logging/log4j/Logger;", "tilesGet", "", "handler", "Linfo/journeymap/shaded/kotlin/spark/kotlin/RouteHandler;", "journeymap" })
public final class TilesKt {

    @NotNull
    private static final Logger logger;

    @NotNull
    public static final Object tilesGet(@NotNull RouteHandler handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        String var10000 = handler.getRequest().queryParamOrDefault("x", "0");
        Intrinsics.checkNotNullExpressionValue(var10000, "handler.request.queryParamOrDefault(\"x\", \"0\")");
        int x = Integer.parseInt(var10000);
        var10000 = handler.getRequest().queryParamOrDefault("y", "0");
        Intrinsics.checkNotNullExpressionValue(var10000, "handler.request.queryParamOrDefault(\"y\", \"0\")");
        Integer y = Integer.parseInt(var10000);
        var10000 = handler.getRequest().queryParamOrDefault("z", "0");
        Intrinsics.checkNotNullExpressionValue(var10000, "handler.request.queryParamOrDefault(\"z\", \"0\")");
        int z = Integer.parseInt(var10000);
        var10000 = handler.getRequest().queryParamOrDefault("dimension", "overworld");
        Intrinsics.checkNotNullExpressionValue(var10000, "handler.request.queryPar…\"dimension\", \"overworld\")");
        String dimension = var10000;
        var10000 = handler.getRequest().queryParamOrDefault("mapTypeString", MapType.Name.day.name());
        Intrinsics.checkNotNullExpressionValue(var10000, "handler.request.queryPar…\", MapType.Name.day.name)");
        String mapTypeString = var10000;
        var10000 = handler.getRequest().queryParamOrDefault("zoom", "0");
        Intrinsics.checkNotNullExpressionValue(var10000, "handler.request.queryParamOrDefault(\"zoom\", \"0\")");
        int zoom = Integer.parseInt(var10000);
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft != null ? minecraft.level : null;
        if (level == null) {
            logger.warn("Tiles requested before world loaded");
            handler.status(400);
            return "World not loaded";
        } else if (!JourneymapClient.getInstance().isMapping()) {
            logger.warn("Tiles requested before JourneyMap started");
            handler.status(400);
            return "JourneyMap is still starting";
        } else {
            File var35 = FileHandler.getJMWorldDir(minecraft);
            Intrinsics.checkNotNullExpressionValue(var35, "getJMWorldDir(minecraft)");
            File worldDir = var35;
            try {
                if (!worldDir.exists() || !worldDir.isDirectory()) {
                    logger.warn("JM world directory not found");
                    handler.status(404);
                    return "World not found";
                }
            } catch (NullPointerException var27) {
                logger.warn("NPE occurred while locating JM world directory");
                handler.status(404);
                return "World not found";
            }
            MapType.Name mapTypeName = null;
            try {
                mapTypeName = MapType.Name.valueOf(mapTypeString);
            } catch (IllegalArgumentException var26) {
                logger.warn("Invalid map type supplied during tiles request: " + mapTypeString);
                handler.status(400);
                return "Invalid map type: " + mapTypeString;
            }
            if (mapTypeName != MapType.Name.underground) {
                y = null;
            }
            if (mapTypeName == MapType.Name.underground && WorldData.isHardcoreAndMultiplayer()) {
                logger.debug("Blank tile returned for underground view on a hardcore server");
                ServletOutputStream var39 = handler.getResponse().raw().getOutputStream();
                Intrinsics.checkNotNullExpressionValue(var39, "handler.response.raw().outputStream");
                OutputStream output = var39;
                handler.getResponse().raw().setContentType("image/png");
                File var10001 = RegionImageHandler.getBlank512x512ImageFile();
                Intrinsics.checkNotNullExpressionValue(var10001, "getBlank512x512ImageFile()");
                output.write(FilesKt.readBytes(var10001));
                output.flush();
                return handler.getResponse();
            } else {
                int scale = MathKt.roundToInt(Math.pow(2.0, (double) zoom));
                int distance = 32 / scale;
                int minChunkX = x * distance;
                int minChunkY = z * distance;
                int maxChunkX = minChunkX + distance - 1;
                int maxChunkY = minChunkY + distance - 1;
                ChunkPos startCoord = new ChunkPos(minChunkX, minChunkY);
                ChunkPos endCoord = new ChunkPos(maxChunkX, maxChunkY);
                Boolean var36 = JourneymapClient.getInstance().getFullMapProperties().showGrid.get();
                Intrinsics.checkNotNullExpressionValue(var36, "getInstance().fullMapProperties.showGrid.get()");
                boolean showGrid = var36;
                MapType mapType = new MapType(mapTypeName, y, DimensionHelper.getWorldKeyForName(dimension));
                NativeImage var37 = RegionImageHandler.getMergedChunks(worldDir, startCoord, endCoord, mapType, true, null, 512, 512, false, showGrid);
                Intrinsics.checkNotNullExpressionValue(var37, "getMergedChunks(\n       …ZE, false, showGrid\n    )");
                NativeImage img = var37;
                ServletOutputStream var38 = handler.getResponse().raw().getOutputStream();
                Intrinsics.checkNotNullExpressionValue(var38, "handler.response.raw().outputStream");
                OutputStream output = var38;
                try {
                    handler.getResponse().raw().setContentType("image/png");
                    img.writeToChannel(Channels.newChannel(handler.getResponse().raw().getOutputStream()));
                    output.flush();
                } catch (EofException var24) {
                    logger.info("Connection closed while writing image response. Webmap probably reloaded.");
                    return "";
                } catch (IIOException var25) {
                    logger.info("Connection closed while writing image response. Webmap probably reloaded.");
                    return "";
                }
                return handler.getResponse();
            }
        }
    }

    static {
        Logger var10000 = Journeymap.getLogger("webmap/routes/tiles");
        Intrinsics.checkNotNullExpressionValue(var10000, "getLogger(\"webmap/routes/tiles\")");
        logger = var10000;
    }
}