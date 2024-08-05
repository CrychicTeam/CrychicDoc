package journeymap.client.render.draw;

import com.google.common.cache.CacheLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.geom.Point2D.Double;
import java.util.Locale;
import java.util.concurrent.Future;
import journeymap.client.api.display.ImageOverlay;
import journeymap.client.api.model.MapImage;
import journeymap.client.api.model.TextProperties;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.texture.DynamicTextureImpl;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.common.CommonConstants;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;

public class DrawImageStep extends BaseOverlayDrawStep<ImageOverlay> {

    private Double northWestPosition;

    private Double southEastPosition;

    private volatile Future<Texture> iconFuture;

    private Texture iconTexture;

    private boolean hasError;

    public DrawImageStep(ImageOverlay marker) {
        super(marker);
    }

    @Override
    public void draw(GuiGraphics graphics, MultiBufferSource buffers, DrawStep.Pass pass, double xOffset, double yOffset, GridRenderer gridRenderer, double fontScale, double rotation) {
        if (this.isOnScreen(graphics.pose(), xOffset, yOffset, gridRenderer, rotation)) {
            if (pass == DrawStep.Pass.Object) {
                this.ensureTexture();
                if (!this.hasError && this.iconTexture != null) {
                    MapImage icon = this.overlay.getImage();
                    double width = this.screenBounds.width;
                    double height = this.screenBounds.height;
                    DrawUtil.drawColoredSprite(graphics.pose(), this.iconTexture, width, height, 0.0, 0.0, icon.getDisplayWidth(), icon.getDisplayHeight(), icon.getColor(), icon.getOpacity(), this.northWestPosition.x + xOffset, this.northWestPosition.y + yOffset, 1.0F, (double) icon.getRotation());
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
                        MapImage image = this.overlay.getImage();
                        ResourceLocation resourceLocation = image.getImageLocation();
                        if (resourceLocation == null) {
                            resourceLocation = new ResourceLocation("fake", CommonConstants.getSafeString(this.overlay.getGuid(), "-").toLowerCase(Locale.ROOT));
                            DynamicTextureImpl texture = new DynamicTextureImpl(image.getImage());
                            Minecraft.getInstance().getTextureManager().register(resourceLocation, texture);
                            return texture;
                        } else {
                            return TextureCache.getTexture(resourceLocation);
                        }
                    });
                } else if (this.iconFuture.isDone()) {
                    this.iconTexture = (Texture) this.iconFuture.get();
                    ((AbstractTexture) this.iconTexture).bind();
                    this.iconFuture = null;
                }
            } catch (Exception var2) {
                Journeymap.getLogger().error("Error getting ImageOverlay marimage upperTexture: " + var2, var2);
                this.hasError = true;
            }
        }
    }

    @Override
    protected void updatePositions(PoseStack poseStack, GridRenderer gridRenderer, double rotation) {
        this.northWestPosition = gridRenderer.getBlockPixelInGrid(this.overlay.getNorthWestPoint());
        this.southEastPosition = gridRenderer.getBlockPixelInGrid(this.overlay.getSouthEastPoint());
        this.screenBounds = new java.awt.geom.Rectangle2D.Double(this.northWestPosition.x, this.northWestPosition.y, 0.0, 0.0);
        this.screenBounds.add(this.southEastPosition);
        TextProperties textProperties = this.overlay.getTextProperties();
        this.labelPosition.setLocation(this.screenBounds.getCenterX() + (double) textProperties.getOffsetX(), this.screenBounds.getCenterY() + (double) textProperties.getOffsetY());
    }

    public static class SimpleCacheLoader extends CacheLoader<ImageOverlay, DrawImageStep> {

        public DrawImageStep load(ImageOverlay overlay) throws Exception {
            return new DrawImageStep(overlay);
        }
    }
}