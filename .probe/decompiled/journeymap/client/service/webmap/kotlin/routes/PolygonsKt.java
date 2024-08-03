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
import java.util.ArrayList;
import java.util.List;
import journeymap.client.api.display.Context;
import journeymap.client.api.display.Overlay;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.TextProperties;
import journeymap.client.api.util.UIState;
import journeymap.client.cartography.color.RGB;
import journeymap.client.render.draw.DrawPolygonStep;
import journeymap.client.render.draw.OverlayDrawStep;
import net.minecraft.core.BlockPos;

@Metadata(mv = { 1, 8, 0 }, k = 2, xi = 48, d1 = { "\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006" }, d2 = { "GSON", "Lcom/google/gson/Gson;", "polygonsGet", "", "handler", "Linfo/journeymap/shaded/kotlin/spark/kotlin/RouteHandler;", "journeymap" })
@SourceDebugExtension({ "SMAP\npolygons.kt\nKotlin\n*S Kotlin\n*F\n+ 1 polygons.kt\njourneymap/client/service/webmap/kotlin/routes/PolygonsKt\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,96:1\n1849#2,2:97\n*S KotlinDebug\n*F\n+ 1 polygons.kt\njourneymap/client/service/webmap/kotlin/routes/PolygonsKt\n*L\n44#1:97,2\n*E\n" })
public final class PolygonsKt {

    @NotNull
    private static final Gson GSON;

    @NotNull
    public static final Object polygonsGet(@NotNull RouteHandler handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        List data = (List) (new ArrayList());
        List steps = (List) (new ArrayList());
        UIState fullscreenState = ClientAPI.INSTANCE.getUIState(Context.UI.Fullscreen);
        UIState minimapState = ClientAPI.INSTANCE.getUIState(Context.UI.Minimap);
        UIState uiState = (fullscreenState != null ? !fullscreenState.active : false) && (minimapState != null ? minimapState.active : false) ? minimapState : fullscreenState;
        ClientAPI var10000 = ClientAPI.INSTANCE;
        Intrinsics.checkNotNull(uiState);
        var10000.getDrawSteps(steps, uiState);
        for (OverlayDrawStep step : steps) {
            if (step instanceof DrawPolygonStep) {
                Overlay var27 = ((DrawPolygonStep) step).overlay;
                Intrinsics.checkNotNull(((DrawPolygonStep) step).overlay, "null cannot be cast to non-null type journeymap.client.api.display.PolygonOverlay");
                PolygonOverlay polygon = (PolygonOverlay) var27;
                List points = (List) (new ArrayList());
                String label = polygon.getLabel();
                TextProperties var28 = polygon.getTextProperties();
                Integer fontColor = var28 != null ? var28.getColor() : null;
                if (fontColor == null) {
                    fontColor = 0;
                }
                List var29 = polygon.getOuterArea().getPoints();
                Intrinsics.checkNotNullExpressionValue(var29, "polygon.outerArea.points");
                Iterable $this$forEach$iv = (Iterable) var29;
                int $i$f$forEach = 0;
                for (Object element$iv : $this$forEach$iv) {
                    BlockPos point = (BlockPos) element$iv;
                    ???;
                    Pair[] var18 = new Pair[] { TuplesKt.to("x", point.m_123341_()), TuplesKt.to("y", point.m_123342_()), TuplesKt.to("z", point.m_123343_()) };
                    points.add(MapsKt.mapOf(var18));
                }
                List holes = (List) (new ArrayList());
                if (polygon.getHoles() != null) {
                    for (MapPolygon hole : polygon.getHoles()) {
                        List holePoints = (List) (new ArrayList());
                        for (BlockPos holePoint : hole.getPoints()) {
                            Pair[] var26 = new Pair[] { TuplesKt.to("x", holePoint.m_123341_()), TuplesKt.to("y", holePoint.m_123342_()), TuplesKt.to("z", holePoint.m_123343_()) };
                            holePoints.add(MapsKt.mapOf(var26));
                        }
                        holes.add(holePoints);
                    }
                }
                Pair[] var21 = new Pair[] { TuplesKt.to("fillColor", RGB.toHexString(polygon.getShapeProperties().getFillColor())), TuplesKt.to("fillOpacity", polygon.getShapeProperties().getFillOpacity()), TuplesKt.to("strokeColor", RGB.toHexString(polygon.getShapeProperties().getStrokeColor())), TuplesKt.to("strokeOpacity", polygon.getShapeProperties().getStrokeOpacity()), TuplesKt.to("strokeWidth", polygon.getShapeProperties().getStrokeWidth()), TuplesKt.to("fontColor", RGB.toHexString(fontColor)), TuplesKt.to("label", label), TuplesKt.to("holes", holes), TuplesKt.to("points", points) };
                data.add(MapsKt.mapOf(var21));
            }
        }
        handler.getResponse().raw().setContentType("application/json");
        String var30 = GSON.toJson(data);
        Intrinsics.checkNotNullExpressionValue(var30, "GSON.toJson(data)");
        return var30;
    }

    static {
        Gson var10000 = new GsonBuilder().setPrettyPrinting().create();
        Intrinsics.checkNotNullExpressionValue(var10000, "GsonBuilder().setPrettyPrinting().create()");
        GSON = var10000;
    }
}