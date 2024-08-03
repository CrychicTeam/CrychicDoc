package journeymap.client.service.webmap.kotlin.routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.Pair;
import info.journeymap.shaded.kotlin.kotlin.TuplesKt;
import info.journeymap.shaded.kotlin.kotlin.collections.MapsKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.SourceDebugExtension;
import info.journeymap.shaded.kotlin.spark.kotlin.RouteHandler;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import journeymap.client.JourneymapClient;
import journeymap.client.model.MapState;
import journeymap.client.service.webmap.kotlin.enums.WebmapStatus;
import journeymap.client.ui.minimap.MiniMap;
import net.minecraft.client.Minecraft;

@Metadata(mv = { 1, 8, 0 }, k = 2, xi = 48, d1 = { "\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006" }, d2 = { "GSON", "Lcom/google/gson/Gson;", "statusGet", "", "handler", "Linfo/journeymap/shaded/kotlin/spark/kotlin/RouteHandler;", "journeymap" })
@SourceDebugExtension({ "SMAP\nstatus.kt\nKotlin\n*S Kotlin\n*F\n+ 1 status.kt\njourneymap/client/service/webmap/kotlin/routes/StatusKt\n+ 2 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n*L\n1#1,50:1\n479#2,7:51\n*S KotlinDebug\n*F\n+ 1 status.kt\njourneymap/client/service/webmap/kotlin/routes/StatusKt\n*L\n38#1:51,7\n*E\n" })
public final class StatusKt {

    @NotNull
    private static final Gson GSON;

    @NotNull
    public static final Object statusGet(@NotNull RouteHandler handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        Map data = (Map) (new LinkedHashMap());
        Minecraft var10000 = Minecraft.getInstance();
        WebmapStatus status = (var10000 != null ? var10000.level : null) == null ? WebmapStatus.NO_WORLD : (!JourneymapClient.getInstance().isMapping() ? WebmapStatus.STARTING : WebmapStatus.READY);
        if (status == WebmapStatus.READY) {
            MapState mapState = MiniMap.state();
            String $this$filterValues$iv = "mapType";
            String var14 = mapState.getMapType().name();
            Intrinsics.checkNotNullExpressionValue(var14, "mapState.mapType.name()");
            String $i$f$filterValues = var14;
            data.put($this$filterValues$iv, $i$f$filterValues);
            Pair[] var12 = new Pair[] { TuplesKt.to("cave", mapState.isCaveMappingAllowed() && mapState.isCaveMappingEnabled()), TuplesKt.to("surface", mapState.isSurfaceMappingAllowed()), TuplesKt.to("topo", mapState.isTopoMappingAllowed()) };
            Map allowedMapTypes = MapsKt.mapOf(var12);
            int $i$f$filterValuesx = 0;
            LinkedHashMap result$iv = new LinkedHashMap();
            for (Entry entry$iv : allowedMapTypes.entrySet()) {
                boolean it = (Boolean) entry$iv.getValue();
                ???;
                if (it) {
                    result$iv.put(entry$iv.getKey(), entry$iv.getValue());
                }
            }
            if (((Map) result$iv).isEmpty()) {
                status = WebmapStatus.DISABLED;
            }
            data.put("allowedMapTypes", allowedMapTypes);
        }
        data.put("status", status.getStatus());
        String var15 = GSON.toJson(data);
        Intrinsics.checkNotNullExpressionValue(var15, "GSON.toJson(data)");
        return var15;
    }

    static {
        Gson var10000 = new GsonBuilder().setPrettyPrinting().create();
        Intrinsics.checkNotNullExpressionValue(var10000, "GsonBuilder().setPrettyPrinting().create()");
        GSON = var10000;
    }
}