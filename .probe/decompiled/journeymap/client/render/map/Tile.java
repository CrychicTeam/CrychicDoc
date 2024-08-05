package journeymap.client.render.map;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.File;
import java.util.ArrayList;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.io.RegionImageHandler;
import journeymap.client.log.ChatLog;
import journeymap.client.model.GridSpec;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionImageCache;
import journeymap.client.properties.CoreProperties;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.minimap.MiniMap;
import journeymap.common.Journeymap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.ChunkPos;
import org.apache.logging.log4j.Logger;

public class Tile {

    public static final int TILESIZE = 512;

    public static final int LOAD_RADIUS = 768;

    static String debugGlSettings = "";

    final int zoom;

    final int tileX;

    final int tileZ;

    final ChunkPos ulChunk;

    final ChunkPos lrChunk;

    final Point ulBlock;

    final Point lrBlock;

    final ArrayList<TileDrawStep> drawSteps = new ArrayList();

    private final Logger logger = Journeymap.getLogger();

    private final int theHashCode;

    private final String theCacheKey;

    int renderType = 0;

    int textureFilter = 0;

    int textureWrap = 0;

    private Tile(int tileX, int tileZ, int zoom) {
        this.tileX = tileX;
        this.tileZ = tileZ;
        this.zoom = zoom;
        this.theCacheKey = toCacheKey(tileX, tileZ, zoom);
        this.theHashCode = this.theCacheKey.hashCode();
        int distance = 32 / (int) Math.pow(2.0, (double) zoom);
        this.ulChunk = new ChunkPos(tileX * distance, tileZ * distance);
        this.lrChunk = new ChunkPos(this.ulChunk.x + distance - 1, this.ulChunk.z + distance - 1);
        this.ulBlock = new Point(this.ulChunk.x * 16, this.ulChunk.z * 16);
        this.lrBlock = new Point(this.lrChunk.x * 16 + 15, this.lrChunk.z * 16 + 15);
        this.updateRenderType();
    }

    public static Tile create(int tileX, int tileZ, int zoom, File worldDir, MapType mapType, boolean highQuality) {
        Tile tile = new Tile(tileX, tileZ, zoom);
        tile.updateTexture(worldDir, mapType, highQuality);
        return tile;
    }

    public static int blockPosToTile(int b, int zoom) {
        return b >> 9 - zoom;
    }

    public static int tileToBlock(int t, int zoom) {
        return t << 9 - zoom;
    }

    public static String toCacheKey(int tileX, int tileZ, int zoom) {
        return tileX + "," + tileZ + "@" + zoom;
    }

    public static void switchTileRenderType() {
        CoreProperties coreProperties = JourneymapClient.getInstance().getCoreProperties();
        int type = coreProperties.tileRenderType.incrementAndGet();
        if (type > 4) {
            type = 1;
            coreProperties.tileRenderType.set(Integer.valueOf(type));
        }
        coreProperties.save();
        String msg = String.format("%s: %s (%s)", Constants.getString("jm.advanced.tile_render_type"), type, debugGlSettings);
        ChatLog.announceError(msg);
        resetTileDisplay();
    }

    public static void switchTileDisplayQuality() {
        CoreProperties coreProperties = JourneymapClient.getInstance().getCoreProperties();
        boolean high = !coreProperties.tileHighDisplayQuality.get();
        coreProperties.tileHighDisplayQuality.set(high);
        coreProperties.save();
        ChatLog.announceError(Constants.getString("jm.common.tile_display_quality") + ": " + (high ? Constants.getString("jm.common.on") : Constants.getString("jm.common.off")));
        resetTileDisplay();
    }

    private static void resetTileDisplay() {
        TileDrawStepCache.instance().invalidateAll();
        RegionImageCache.INSTANCE.clear();
        MiniMap.state().requireRefresh();
        Fullscreen.state().requireRefresh();
    }

    public boolean updateTexture(File worldDir, MapType mapType, boolean highQuality) {
        this.updateRenderType();
        this.drawSteps.clear();
        this.drawSteps.addAll(RegionImageHandler.getTileDrawSteps(worldDir, this.ulChunk, this.lrChunk, mapType, this.zoom, highQuality));
        return this.drawSteps.size() > 1;
    }

    public boolean hasTexture(MapType mapType) {
        if (this.drawSteps.isEmpty()) {
            return false;
        } else {
            for (TileDrawStep tileDrawStep : this.drawSteps) {
                if (tileDrawStep.hasTexture(mapType)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void clear() {
        this.drawSteps.clear();
    }

    private void updateRenderType() {
        this.renderType = JourneymapClient.getInstance().getCoreProperties().tileRenderType.get();
        switch(this.renderType) {
            case 1:
            default:
                this.textureFilter = 9729;
                this.textureWrap = 33648;
                debugGlSettings = "GL_LINEAR, GL_MIRRORED_REPEAT";
                break;
            case 2:
                this.textureFilter = 9729;
                this.textureWrap = 33071;
                debugGlSettings = "GL_LINEAR, GL_CLAMP_TO_EDGE";
                break;
            case 3:
                this.textureFilter = 9728;
                this.textureWrap = 33648;
                debugGlSettings = "GL_NEAREST, GL_MIRRORED_REPEAT";
                break;
            case 4:
                this.textureFilter = 9728;
                this.textureWrap = 33071;
                debugGlSettings = "GL_NEAREST, GL_CLAMP_TO_EDGE";
        }
    }

    public String toString() {
        return "Tile [ r" + this.tileX + ", r" + this.tileZ + " (zoom " + this.zoom + ") ]";
    }

    public String cacheKey() {
        return this.theCacheKey;
    }

    public int hashCode() {
        return this.theHashCode;
    }

    public Point2D blockPixelOffsetInTile(double x, double z) {
        if (!(x < (double) this.ulBlock.x) && !(Math.floor(x) > (double) this.lrBlock.x) && !(z < (double) this.ulBlock.y) && !(Math.floor(z) > (double) this.lrBlock.y)) {
            double localBlockX = (double) this.ulBlock.x - x;
            if (x < 0.0) {
                localBlockX++;
            }
            double localBlockZ = (double) this.ulBlock.y - z;
            if (z < 0.0) {
                localBlockZ++;
            }
            int blockSize = (int) Math.pow(2.0, (double) this.zoom);
            double pixelOffsetX = 256.0 + localBlockX * (double) blockSize - (double) (blockSize / 2);
            double pixelOffsetZ = 256.0 + localBlockZ * (double) blockSize - (double) (blockSize / 2);
            return new Double(pixelOffsetX, pixelOffsetZ);
        } else {
            throw new RuntimeException("Block " + x + "," + z + " isn't in " + this);
        }
    }

    boolean draw(GuiGraphics graphics, TilePos pos, double offsetX, double offsetZ, float alpha, float bgAlpha, GridSpec gridSpec) {
        boolean somethingDrew = false;
        for (TileDrawStep tileDrawStep : this.drawSteps) {
            boolean ok = tileDrawStep.draw(graphics, pos, offsetX, offsetZ, alpha, bgAlpha, this.textureFilter, this.textureWrap, gridSpec);
            if (ok) {
                somethingDrew = true;
            }
        }
        return somethingDrew;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Tile tile = (Tile) o;
            if (this.tileX != tile.tileX) {
                return false;
            } else {
                return this.tileZ != tile.tileZ ? false : this.zoom == tile.zoom;
            }
        } else {
            return false;
        }
    }
}