package journeymap.client.render.draw;

import com.google.common.cache.CacheLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.concurrent.Future;
import journeymap.client.api.display.MarkerOverlay;
import journeymap.client.api.model.MapImage;
import journeymap.client.api.model.TextProperties;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.texture.DynamicTextureImpl;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.common.Journeymap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.AbstractTexture;

public class DrawMarkerStep extends BaseOverlayDrawStep<MarkerOverlay> {

    private Double markerPosition;

    private volatile Future<Texture> iconFuture;

    private Texture iconTexture;

    private boolean hasError;

    public DrawMarkerStep(MarkerOverlay marker) {
        super(marker);
    }

    @Override
    public void draw(GuiGraphics graphics, MultiBufferSource buffers, DrawStep.Pass pass, double xOffset, double yOffset, GridRenderer gridRenderer, double fontScale, double rotation) {
        if (this.isOnScreen(graphics.pose(), xOffset, yOffset, gridRenderer, rotation)) {
            if (pass == DrawStep.Pass.Object) {
                this.ensureTexture();
                if (!this.hasError && this.iconTexture != null && this.iconTexture.hasImage()) {
                    MapImage icon = this.overlay.getIcon();
                    DrawUtil.drawColoredSprite(graphics.pose(), this.iconTexture, icon.getDisplayWidth(), icon.getDisplayHeight(), (double) icon.getTextureX(), (double) icon.getTextureY(), (double) icon.getTextureWidth(), (double) icon.getTextureHeight(), icon.getColor(), icon.getOpacity(), this.markerPosition.x + xOffset - icon.getAnchorX(), this.markerPosition.y + yOffset - icon.getAnchorY(), 1.0F, (double) icon.getRotation() - rotation);
                }
            } else {
                super.drawText(graphics.pose(), buffers, pass, xOffset, yOffset, gridRenderer, fontScale, rotation);
            }
        }
    }

    protected void ensureTexture() {
        if (this.iconTexture == null) {
            try {
                if (this.iconFuture == null || this.iconFuture.isCancelled()) {
                    this.iconFuture = TextureCache.scheduleTextureTask(() -> {
                        MapImage icon = this.overlay.getIcon();
                        if (icon.getImageLocation() != null) {
                            return TextureCache.getTexture(icon.getImageLocation());
                        } else {
                            return icon.getImage() != null ? new DynamicTextureImpl(icon.getImage()) : null;
                        }
                    });
                } else if (this.iconFuture.isDone()) {
                    this.iconTexture = (Texture) this.iconFuture.get();
                    ((AbstractTexture) this.iconTexture).bind();
                    this.iconFuture = null;
                }
            } catch (Exception var2) {
                Journeymap.getLogger().error("Error getting MarkerOverlay image upperTexture: " + var2, var2);
                this.hasError = true;
            }
        }
    }

    @Override
    protected void updatePositions(PoseStack poseStack, GridRenderer gridRenderer, double rotation) {
        MapImage icon = this.overlay.getIcon();
        this.markerPosition = gridRenderer.getBlockPixelInGrid(this.overlay.getPoint());
        int halfBlock = (int) this.lastUiState.blockSize / 2;
        this.markerPosition.setLocation(this.markerPosition.x + (double) halfBlock, this.markerPosition.y + (double) halfBlock);
        TextProperties textProperties = this.overlay.getTextProperties();
        int xShift = rotation % 360.0 == 0.0 ? -textProperties.getOffsetX() : textProperties.getOffsetX();
        int yShift = rotation % 360.0 == 0.0 ? -textProperties.getOffsetY() : textProperties.getOffsetY();
        if (xShift == 0 && yShift == 0) {
            this.labelPosition.setLocation(this.markerPosition.x, this.markerPosition.y);
        } else {
            Point2D shiftedPoint = gridRenderer.shiftWindowPosition(this.markerPosition.x, this.markerPosition.y, xShift, yShift);
            this.labelPosition.setLocation(shiftedPoint.getX(), shiftedPoint.getY());
        }
        this.screenBounds.setRect(this.markerPosition.x, this.markerPosition.y, this.lastUiState.blockSize, this.lastUiState.blockSize);
        this.screenBounds.add(this.labelPosition);
        java.awt.geom.Rectangle2D.Double iconBounds = new java.awt.geom.Rectangle2D.Double(this.markerPosition.x - icon.getAnchorX(), this.markerPosition.y - icon.getAnchorY(), icon.getDisplayWidth(), icon.getDisplayHeight());
        this.screenBounds.add(iconBounds);
    }

    public static class SimpleCacheLoader extends CacheLoader<MarkerOverlay, DrawMarkerStep> {

        public DrawMarkerStep load(MarkerOverlay overlay) throws Exception {
            return new DrawMarkerStep(overlay);
        }
    }
}