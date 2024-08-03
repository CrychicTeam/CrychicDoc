package journeymap.client.render.draw;

import com.google.common.cache.CacheLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import earcut4j.Earcut;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import journeymap.client.api.display.Context;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.TextProperties;
import journeymap.client.data.DataCache;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.minimap.DisplayVars;
import journeymap.client.ui.minimap.Shape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class DrawPolygonStep extends BaseOverlayDrawStep<PolygonOverlay> {

    protected List<Double> fillPoints = new ArrayList();

    protected List<List<Double>> strokePoints = new ArrayList();

    boolean doRender = true;

    boolean onScreen;

    public DrawPolygonStep(PolygonOverlay polygon) {
        super(polygon);
    }

    @Override
    public void draw(GuiGraphics graphics, MultiBufferSource buffers, DrawStep.Pass pass, double xOffset, double yOffset, GridRenderer gridRenderer, double fontScale, double rotation) {
        if (pass == DrawStep.Pass.Object) {
            if (this.overlay.getOuterArea().getPoints().isEmpty()) {
                this.onScreen = false;
                return;
            }
            this.onScreen = this.isOnScreen(graphics.pose(), xOffset, yOffset, gridRenderer, rotation);
            if (this.onScreen && this.doRender) {
                DrawUtil.drawPolygon(graphics.pose(), xOffset, yOffset, this.fillPoints, this.strokePoints, this.overlay.getShapeProperties(), gridRenderer.getZoom());
            }
        } else if (this.onScreen && this.doRender && this.isTextInMinimap(gridRenderer)) {
            super.drawText(graphics.pose(), buffers, pass, xOffset, yOffset, gridRenderer, fontScale, rotation);
        }
    }

    private boolean isTextInMinimap(GridRenderer gridRenderer) {
        if (gridRenderer.getUIState().ui == Context.UI.Minimap) {
            Minecraft mc = Minecraft.getInstance();
            DisplayVars dv = UIManager.INSTANCE.getMiniMap().getDisplayVars();
            Point2D centerPoint = gridRenderer.getPixel(mc.player.m_20185_(), mc.player.m_20189_());
            java.awt.geom.Rectangle2D.Double centerRect = new java.awt.geom.Rectangle2D.Double(centerPoint.getX() - (double) (dv.minimapWidth / 2), centerPoint.getY() - (double) (dv.minimapHeight / 2), (double) dv.minimapWidth, (double) dv.minimapHeight);
            return dv.getShape() == Shape.Circle ? centerPoint.distance(this.labelPosition) < (double) (dv.minimapWidth / 2) : centerRect.contains(gridRenderer.getWindowPosition(this.labelPosition));
        } else {
            return true;
        }
    }

    @Override
    protected void updatePositions(PoseStack poseStack, GridRenderer gridRenderer, double rotation) {
        if (!this.overlay.getOuterArea().getPoints().isEmpty()) {
            if (Context.UI.Minimap == gridRenderer.getUIState().ui) {
                Vec3 playerPos = Minecraft.getInstance().player.m_20182_();
                int zoom = gridRenderer.getZoom() <= 1 ? 2 : gridRenderer.getZoom();
                int limit = gridRenderer.getGridSize() * 512 / zoom;
                if (!((BlockPos) this.overlay.getOuterArea().getPoints().get(0)).m_123314_(new Vec3i(Mth.floor(playerPos.x()), Mth.floor(playerPos.y()), Mth.floor(playerPos.z())), (double) limit)) {
                    this.doRender = false;
                    return;
                }
            }
            this.doRender = true;
            List<BlockPos> points = DataCache.INSTANCE.getTriangulation(this.overlay);
            this.fillPoints.clear();
            for (BlockPos pos : points) {
                Double pixel = gridRenderer.getBlockPixelInGrid(pos);
                if (this.fillPoints.isEmpty()) {
                    this.screenBounds.setRect(pixel.x, pixel.y, 1.0, 1.0);
                } else {
                    this.screenBounds.add(pixel);
                }
                this.fillPoints.add(pixel);
            }
            this.strokePoints.clear();
            this.strokePoints.add(toScreen(gridRenderer, this.overlay.getOuterArea()));
            if (this.overlay.getHoles() != null) {
                for (MapPolygon hole : this.overlay.getHoles()) {
                    this.strokePoints.add(toScreen(gridRenderer, hole));
                }
            }
            TextProperties textProperties = this.overlay.getTextProperties();
            this.labelPosition.setLocation(this.screenBounds.getCenterX() + (double) textProperties.getOffsetX(), this.screenBounds.getCenterY() + (double) textProperties.getOffsetY());
        }
    }

    private static List<Double> toScreen(@NotNull GridRenderer gridRenderer, @NotNull MapPolygon polygon) {
        List<Double> points = new ArrayList();
        for (BlockPos pos : polygon.getPoints()) {
            points.add(gridRenderer.getBlockPixelInGrid(pos));
        }
        return points;
    }

    public static List<MapPolygon> triangulate(@NotNull PolygonOverlay overlay) {
        List<MapPolygon> holesList = (List<MapPolygon>) Optional.ofNullable(overlay.getHoles()).orElse(new ArrayList());
        List<BlockPos> blockPoints = (List<BlockPos>) Stream.concat(overlay.getOuterArea().getPoints().stream(), holesList.stream().flatMap(hole -> hole.getPoints().stream())).collect(Collectors.toList());
        double[] points = new double[blockPoints.size() * 2];
        for (int index = 0; index < blockPoints.size(); index++) {
            points[index * 2] = (double) ((BlockPos) blockPoints.get(index)).m_123341_();
            points[index * 2 + 1] = (double) ((BlockPos) blockPoints.get(index)).m_123343_();
        }
        int[] holes = new int[holesList.size()];
        int holeIndex = overlay.getOuterArea().getPoints().size();
        for (int index = 0; index < holesList.size(); index++) {
            holes[index] = holeIndex;
            holeIndex += ((MapPolygon) holesList.get(index)).getPoints().size();
        }
        List<Integer> triangles = Earcut.earcut(points, holes, 2);
        List<MapPolygon> trianglePolys = new ArrayList();
        for (int index = 0; index < triangles.size(); index += 3) {
            List<BlockPos> trianglePoints = new ArrayList();
            trianglePoints.add((BlockPos) blockPoints.get((Integer) triangles.get(index + 2)));
            trianglePoints.add((BlockPos) blockPoints.get((Integer) triangles.get(index + 1)));
            trianglePoints.add((BlockPos) blockPoints.get((Integer) triangles.get(index)));
            trianglePolys.add(new MapPolygon(trianglePoints));
        }
        return trianglePolys;
    }

    public static class SimpleCacheLoader extends CacheLoader<PolygonOverlay, DrawPolygonStep> {

        public DrawPolygonStep load(PolygonOverlay overlay) throws Exception {
            return new DrawPolygonStep(overlay);
        }
    }

    public static class TriangulationCacheLoader extends CacheLoader<PolygonOverlay, List<BlockPos>> {

        public List<BlockPos> load(PolygonOverlay overlay) throws Exception {
            return (List<BlockPos>) DrawPolygonStep.triangulate(overlay).stream().flatMap(poly -> poly.getPoints().stream()).collect(Collectors.toList());
        }
    }
}