package journeymap.client.render.draw;

import com.google.common.cache.CacheLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import journeymap.client.JourneymapClient;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.waypoint.Waypoint;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

public class DrawWayPointStep implements DrawStep {

    public final Waypoint waypoint;

    final Integer color;

    final Integer fontColor;

    final Texture texture;

    final boolean isEdit;

    Double lastPosition;

    boolean lastOnScreen;

    boolean showLabel;

    double labelScale = 0.0;

    float iconScale = 0.0F;

    public DrawWayPointStep(Waypoint waypoint) {
        this(waypoint, waypoint.getIconColor(), waypoint.isDeathPoint() ? 16711680 : waypoint.getSafeColor(), false);
    }

    public DrawWayPointStep(Waypoint waypoint, Integer color, Integer fontColor, boolean isEdit) {
        this.waypoint = waypoint;
        this.color = color;
        this.fontColor = fontColor;
        this.isEdit = isEdit;
        this.texture = waypoint.getTexture();
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    public void setLabelScale(float labelScale) {
        this.labelScale = (double) labelScale;
    }

    public void setIconScale(float iconScale) {
        this.iconScale = iconScale;
    }

    @Override
    public void draw(GuiGraphics graphics, MultiBufferSource buffers, DrawStep.Pass pass, double xOffset, double yOffset, GridRenderer gridRenderer, double fontScale, double rotation) {
        if (this.waypoint.isInPlayerDimension() && JourneymapClient.getInstance().getStateHandler().isWaypointsAllowed()) {
            Double pixel = this.getPosition(xOffset, yOffset, gridRenderer, true);
            if (gridRenderer.isOnScreen(pixel)) {
                if (this.showLabel && pass == DrawStep.Pass.Text) {
                    int labelOffset = rotation == 0.0 ? -this.texture.getHeight() : this.texture.getHeight();
                    labelOffset = this.iconScale <= 1.0F ? labelOffset : (int) ((double) labelOffset + (double) ((float) labelOffset * this.iconScale) * 0.2);
                    this.labelScale = this.labelScale == 0.0 ? fontScale : this.labelScale;
                    Point2D labelPoint = gridRenderer.shiftWindowPosition(pixel.getX(), pixel.getY(), 0, labelOffset);
                    String waypointName = this.waypoint.getName();
                    if (this.waypoint.isDeathPoint() && !JourneymapClient.getInstance().getWaypointProperties().showDeathpointlabel.get()) {
                        waypointName = "";
                    }
                    DrawUtil.drawBatchLabel(graphics.pose(), Component.literal(waypointName), buffers, labelPoint.getX(), labelPoint.getY(), DrawUtil.HAlign.Center, DrawUtil.VAlign.Middle, 0, 0.7F, this.fontColor, 1.0F, this.labelScale, false, rotation);
                } else if (this.isEdit && pass == DrawStep.Pass.Object) {
                    Texture editTex = TextureCache.getTexture(TextureCache.WaypointEdit);
                    DrawUtil.drawWaypointIcon(graphics.pose(), editTex, this.iconScale, this.color, 1.0F, pixel.getX(), pixel.getY(), -rotation);
                }
                if (pass == DrawStep.Pass.Object) {
                    DrawUtil.drawWaypointIcon(graphics.pose(), this.texture, this.iconScale, this.color, 1.0F, pixel.getX(), pixel.getY(), -rotation);
                }
            } else if (!this.isEdit && pass == DrawStep.Pass.Object) {
                gridRenderer.ensureOnScreen(pixel);
                DrawUtil.drawWaypointIcon(graphics.pose(), this.texture, this.iconScale, this.color, 1.0F, pixel.getX(), pixel.getY(), -rotation);
            }
        }
    }

    public void drawOffscreen(PoseStack poseStack, DrawStep.Pass pass, Point2D pixel, double rotation) {
        if (pass == DrawStep.Pass.Object) {
            DrawUtil.drawWaypointIcon(poseStack, this.texture, this.iconScale, this.color, 1.0F, pixel.getX() - (double) (this.texture.getWidth() >> 1), pixel.getY() - (double) (this.texture.getHeight() >> 1), -rotation);
        }
    }

    public Double getPosition(double xOffset, double yOffset, GridRenderer gridRenderer, boolean forceUpdate) {
        if (!forceUpdate && this.lastPosition != null) {
            return this.lastPosition;
        } else {
            double x = (double) this.waypoint.getX();
            double z = (double) this.waypoint.getZ();
            double halfBlock = Math.pow(2.0, (double) gridRenderer.getZoom()) / 2.0;
            Double pixel = gridRenderer.getBlockPixelInGrid(x, z);
            pixel.setLocation(pixel.getX() + halfBlock + xOffset, pixel.getY() + halfBlock + yOffset);
            this.lastPosition = pixel;
            return pixel;
        }
    }

    public boolean isOnScreen() {
        return this.lastOnScreen;
    }

    public void setOnScreen(boolean lastOnScreen) {
        this.lastOnScreen = lastOnScreen;
    }

    @Override
    public int getDisplayOrder() {
        return 0;
    }

    @Override
    public String getModId() {
        return this.waypoint.getOrigin();
    }

    public static class SimpleCacheLoader extends CacheLoader<Waypoint, DrawWayPointStep> {

        public DrawWayPointStep load(Waypoint waypoint) throws Exception {
            return new DrawWayPointStep(waypoint);
        }
    }
}