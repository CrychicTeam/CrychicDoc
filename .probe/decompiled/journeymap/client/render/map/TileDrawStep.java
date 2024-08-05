package journeymap.client.render.map;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.mojang.blaze3d.platform.NativeImage;
import java.util.Set;
import java.util.concurrent.Future;
import journeymap.client.log.StatTimer;
import journeymap.client.model.GridSpec;
import journeymap.client.model.ImageHolder;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionCoord;
import journeymap.client.model.RegionImageCache;
import journeymap.client.model.RegionImageSet;
import journeymap.client.render.RenderWrapper;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.task.main.ExpireTextureTask;
import journeymap.client.texture.ImageUtil;
import journeymap.client.texture.RegionTexture;
import journeymap.client.texture.TextureCache;
import journeymap.common.Journeymap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.level.ChunkPos;
import org.apache.logging.log4j.Logger;

public class TileDrawStep implements RegionTexture.Listener<RegionTexture> {

    private static final Integer bgColor = 2236962;

    private static final Logger logger = Journeymap.getLogger();

    private static final RegionImageCache regionImageCache = RegionImageCache.INSTANCE;

    private boolean debug = false;

    private final RegionCoord regionCoord;

    private final MapType mapType;

    private final Integer zoom;

    private final boolean highQuality;

    private final StatTimer drawTimer;

    private final StatTimer updateRegionTimer = StatTimer.get("TileDrawStep.updateRegionTexture", 5, 50);

    private final StatTimer updateScaledTimer = StatTimer.get("TileDrawStep.updateScaledTexture", 5, 50);

    private final int theHashCode;

    private final String theCacheKey;

    private final RegionImageSet.Key regionImageSetKey;

    private int sx1;

    private int sy1;

    private int sx2;

    private int sy2;

    private volatile RegionTexture scaledTexture;

    private volatile Future<RegionTexture> regionFuture;

    private volatile Future<RegionTexture> scaledFuture;

    private volatile boolean needsScaledUpdate;

    private int lastTextureFilter;

    private int lastTextureWrap;

    public TileDrawStep(RegionCoord regionCoord, MapType mapType, Integer zoom, boolean highQuality, int sx1, int sy1, int sx2, int sy2) {
        this.mapType = mapType;
        this.regionCoord = regionCoord;
        this.regionImageSetKey = RegionImageSet.Key.from(regionCoord);
        this.zoom = zoom;
        this.sx1 = sx1;
        this.sx2 = sx2;
        this.sy1 = sy1;
        this.sy2 = sy2;
        this.highQuality = highQuality && zoom != 0;
        this.drawTimer = this.highQuality ? StatTimer.get("TileDrawStep.draw(high)") : StatTimer.get("TileDrawStep.draw(low)");
        this.theCacheKey = toCacheKey(regionCoord, mapType, zoom, highQuality, sx1, sy1, sx2, sy2);
        this.theHashCode = this.theCacheKey.hashCode();
        this.updateRegionTexture();
        if (highQuality) {
            this.updateScaledTexture();
        }
    }

    public static String toCacheKey(RegionCoord regionCoord, MapType mapType, Integer zoom, boolean highQuality, int sx1, int sy1, int sx2, int sy2) {
        return regionCoord.cacheKey() + mapType.toCacheKey() + zoom + highQuality + sx1 + "," + sy1 + "," + sx2 + "," + sy2;
    }

    ImageHolder getRegionTextureHolder() {
        return regionImageCache.getRegionImageSet(this.regionImageSetKey).getHolder(this.mapType);
    }

    boolean draw(GuiGraphics graphics, TilePos pos, double offsetX, double offsetZ, float alpha, float bgAlpha, int textureFilter, int textureWrap, GridSpec gridSpec) {
        boolean regionUpdatePending = this.updateRegionTexture();
        if (this.highQuality && !regionUpdatePending) {
            this.updateScaledTexture();
        }
        boolean useScaled = false;
        Integer textureId;
        if (this.highQuality && this.scaledTexture != null) {
            textureId = this.scaledTexture.m_117963_();
            useScaled = true;
        } else if (!regionUpdatePending) {
            textureId = this.getRegionTextureHolder().getTexture().m_117963_();
        } else {
            textureId = -1;
        }
        if (textureFilter != this.lastTextureFilter) {
            this.lastTextureFilter = textureFilter;
        }
        if (textureWrap != this.lastTextureWrap) {
            this.lastTextureWrap = textureWrap;
        }
        this.drawTimer.start();
        double startX = offsetX + pos.startX;
        double startY = (double) Math.round(offsetZ + pos.startZ);
        double endX = offsetX + pos.endX;
        double endY = (double) Math.round(offsetZ + pos.endZ);
        double z = 0.0;
        double size = 512.0;
        double startU = useScaled ? 0.0 : (double) this.sx1 / 512.0;
        double startV = useScaled ? 0.0 : (double) this.sy1 / 512.0;
        double endU = useScaled ? 1.0 : (double) this.sx2 / 512.0;
        double endV = useScaled ? 1.0 : (double) this.sy2 / 512.0;
        DrawUtil.drawRectangle(graphics.pose(), startX, startY, endX - startX, endY - startY, bgColor, bgAlpha);
        if (textureId != -1) {
            RenderWrapper.setShader(GameRenderer::m_172817_);
            RenderWrapper.activeTexture(33984);
            RenderWrapper.bindTexture(textureId);
            RenderWrapper.setShaderTexture(0, textureId);
            RenderWrapper.setColor4f(1.0F, 1.0F, 1.0F, alpha);
            RenderWrapper.enableBlend();
            RenderWrapper.defaultBlendFunc();
            RenderWrapper.enableTexture();
            RenderWrapper.texParameter(3553, 10241, textureFilter);
            RenderWrapper.texParameter(3553, 10240, textureFilter);
            RenderWrapper.texParameter(3553, 10242, textureWrap);
            RenderWrapper.texParameter(3553, 10243, textureWrap);
            DrawUtil.drawBoundTexture(graphics.pose(), startU, startV, startX, startY, 0.0, endU, endV, endX, endY);
            RenderWrapper.disableBlend();
            RenderWrapper.disableTexture();
        }
        if (gridSpec != null) {
            gridSpec.beginTexture(9728, 33071, alpha);
            DrawUtil.drawBoundTexture(graphics.pose(), (double) this.sx1 / 512.0, (double) this.sy1 / 512.0, startX, startY, 0.0, (double) this.sx2 / 512.0, (double) this.sy2 / 512.0, endX, endY);
            gridSpec.finishTexture();
        }
        if (this.debug) {
            int debugX = (int) startX;
            int debugY = (int) startY;
            DrawUtil.drawRectangle(graphics.pose(), (double) debugX, (double) debugY, 3.0, endV * 512.0, 65280, 0.8F);
            DrawUtil.drawRectangle(graphics.pose(), (double) debugX, (double) debugY, endU * 512.0, 3.0, 16711680, 0.8F);
            DrawUtil.drawLabel(graphics, this.toString(), (double) (debugX + 5), (double) (debugY + 10), DrawUtil.HAlign.Right, DrawUtil.VAlign.Below, 16777215, 255.0F, 255, 255.0F, 1.0, false);
            DrawUtil.drawLabel(graphics, String.format("Tile Render Type: %s, Scaled: %s", Tile.debugGlSettings, useScaled), (double) (debugX + 5), (double) (debugY + 20), DrawUtil.HAlign.Right, DrawUtil.VAlign.Below, 16777215, 255.0F, 255, 255.0F, 1.0, false);
            long imageTimestamp = useScaled ? this.scaledTexture.getLastImageUpdate() : this.getRegionTextureHolder().getImageTimestamp();
            long age = (System.currentTimeMillis() - imageTimestamp) / 1000L;
            DrawUtil.drawLabel(graphics, this.mapType + " tile age: " + age + " seconds old", (double) (debugX + 5), (double) (debugY + 30), DrawUtil.HAlign.Right, DrawUtil.VAlign.Below, 16777215, 255.0F, 255, 255.0F, 1.0, false);
        }
        RenderWrapper.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderWrapper.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTimer.stop();
        int glErr = RenderWrapper.getError();
        if (glErr != 0) {
            Journeymap.getLogger().warn("GL Error in TileDrawStep: " + glErr);
            this.clearTexture();
        }
        return textureId != 1;
    }

    public void clearTexture() {
        ExpireTextureTask.queue(this.scaledTexture);
        this.scaledTexture = null;
        if (this.scaledFuture != null && !this.scaledFuture.isDone()) {
            this.scaledFuture.cancel(true);
        }
        this.scaledFuture = null;
        if (this.regionFuture != null && !this.regionFuture.isDone()) {
            this.regionFuture.cancel(true);
        }
        this.regionFuture = null;
    }

    public MapType getMapType() {
        return this.mapType;
    }

    public Integer getZoom() {
        return this.zoom;
    }

    public String cacheKey() {
        return this.theCacheKey;
    }

    public int hashCode() {
        return this.theHashCode;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("rc", this.regionCoord).add("type", this.mapType).add("high", this.highQuality).add("zoom", this.zoom).add("sx1", this.sx1).add("sy1", this.sy1).toString();
    }

    boolean hasTexture(MapType mapType) {
        if (!Objects.equal(this.mapType, mapType)) {
            return false;
        } else {
            return !this.highQuality ? this.getRegionTextureHolder().getTexture().isBound() : this.scaledTexture != null && this.scaledTexture.isBound();
        }
    }

    private boolean updateRegionTexture() {
        this.updateRegionTimer.start();
        if (this.regionFuture != null) {
            if (!this.regionFuture.isDone()) {
                this.updateRegionTimer.stop();
                return true;
            }
            this.regionFuture = null;
        }
        ImageHolder imageHolder = this.getRegionTextureHolder();
        if (imageHolder.hasTexture()) {
            RegionTexture tex = imageHolder.getTexture();
            tex.addListener(this);
            if (tex.bindNeeded()) {
                tex.bind();
            }
            this.updateRegionTimer.stop();
            return false;
        } else {
            this.regionFuture = TextureCache.scheduleTextureTask(() -> {
                RegionTexture texx = this.getRegionTextureHolder().getTexture();
                texx.addListener(this);
                return texx;
            });
            this.updateRegionTimer.stop();
            return true;
        }
    }

    private boolean updateScaledTexture() {
        this.updateScaledTimer.start();
        if (this.scaledFuture != null) {
            if (!this.scaledFuture.isDone()) {
                this.updateScaledTimer.stop();
                return true;
            } else {
                try {
                    this.scaledTexture = (RegionTexture) this.scaledFuture.get();
                    this.scaledTexture.bind();
                } catch (Throwable var2) {
                    logger.error(var2);
                }
                this.scaledFuture = null;
                this.updateScaledTimer.stop();
                return false;
            }
        } else {
            if (this.scaledTexture == null) {
                this.needsScaledUpdate = false;
                this.scaledFuture = TextureCache.scheduleTextureTask(() -> new RegionTexture(this.getScaledRegionArea(), "Scaled " + this));
            } else if (this.needsScaledUpdate) {
                this.needsScaledUpdate = false;
                RegionTexture temp = this.scaledTexture;
                this.scaledFuture = TextureCache.scheduleTextureTask(() -> {
                    temp.setNativeImage(this.getScaledRegionArea(), false);
                    return temp;
                });
            }
            this.updateScaledTimer.stop();
            return true;
        }
    }

    public NativeImage getScaledRegionArea() {
        int scale = (int) Math.pow(2.0, (double) this.zoom.intValue());
        int scaledSize = 512 / scale;
        try {
            NativeImage subImage = this.getRegionTextureHolder().getTexture().getSubImage(this.sx1, this.sy1, scaledSize, scaledSize);
            NativeImage sizedImage = ImageUtil.getSizedImage(512, 512, subImage, false);
            ImageUtil.clearAndClose(subImage);
            return sizedImage;
        } catch (Throwable var5) {
            logger.error(var5);
            return null;
        }
    }

    public void textureImageUpdated(RegionTexture regionTexture) {
        if (this.highQuality && this.zoom > 0) {
            Set<ChunkPos> dirtyAreas = regionTexture.getDirtyAreas();
            if (dirtyAreas.isEmpty()) {
                this.needsScaledUpdate = true;
            } else {
                for (ChunkPos area : dirtyAreas) {
                    if (area.x >= this.sx1 && area.z >= this.sy1 && area.x + 16 <= this.sx2 && area.z + 16 <= this.sy2) {
                        this.needsScaledUpdate = true;
                        return;
                    }
                }
            }
        }
    }
}