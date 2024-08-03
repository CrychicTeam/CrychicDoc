package journeymap.client.render.map;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Point2D.Float;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.Map.Entry;
import journeymap.client.JourneymapClient;
import journeymap.client.api.display.Context;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.event.DisplayUpdateEvent;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.api.impl.ClientEventManager;
import journeymap.client.api.util.UIState;
import journeymap.client.data.DataCache;
import journeymap.client.log.StatTimer;
import journeymap.client.model.GridSpec;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionImageCache;
import journeymap.client.render.RenderWrapper;
import journeymap.client.render.draw.DrawStep;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class GridRenderer {

    private static boolean enabled = true;

    private static HashMap<String, String> messages = new HashMap();

    private final TilePos centerPos = new TilePos(0, 0);

    private final Logger logger = Journeymap.getLogger();

    private final boolean debug = this.logger.isDebugEnabled();

    private final TreeMap<TilePos, Tile> grid = new TreeMap();

    private final Double centerPixelOffset = new Double();

    private final int maxGlErrors = 20;

    private final Context.UI contextUi;

    StatTimer updateTilesTimer1 = StatTimer.get("GridRenderer.updateTiles(1)", 5, 500);

    StatTimer updateTilesTimer2 = StatTimer.get("GridRenderer.updateTiles(2)", 5, 500);

    private UIState uiState;

    private int glErrors = 0;

    private int gridSize;

    private double srcSize;

    private java.awt.geom.Rectangle2D.Double viewPort = null;

    private java.awt.geom.Rectangle2D.Double screenBounds = null;

    private AABB blockBounds = null;

    private int lastHeight = -1;

    private int lastWidth = -1;

    private MapType mapType;

    private String centerTileKey = "";

    private int zoom;

    private double centerBlockX;

    private double centerBlockZ;

    private File worldDir;

    private double currentRotation;

    private FloatBuffer modelMatrixBuf;

    private FloatBuffer projMatrixBuf;

    private final Vector3f windowPos;

    private final Vector3f objPose;

    private final int[] viewport;

    private final Matrix4f modelMatrix;

    private final Matrix4f projectionMatrix;

    public int mouseX = 0;

    public int mouseY = 0;

    public Fullscreen fullscreen = null;

    public GridRenderer(Context.UI contextUi) {
        this.contextUi = contextUi;
        this.uiState = UIState.newInactive(contextUi, Minecraft.getInstance());
        this.modelMatrixBuf = BufferUtils.createFloatBuffer(16);
        this.projMatrixBuf = BufferUtils.createFloatBuffer(16);
        this.windowPos = new Vector3f();
        this.objPose = new Vector3f();
        this.viewport = new int[4];
        this.modelMatrix = new Matrix4f(this.modelMatrixBuf);
        this.projectionMatrix = new Matrix4f(this.projMatrixBuf);
    }

    public static void addDebugMessage(String key, String message) {
        messages.put(key, message);
    }

    public static void removeDebugMessage(String key, String message) {
        messages.remove(key);
    }

    public static void clearDebugMessages() {
        messages.clear();
    }

    public static void setEnabled(boolean enabled) {
        GridRenderer.enabled = enabled;
        if (!enabled) {
            TileDrawStepCache.clear();
        }
    }

    public Context.UI getDisplay() {
        return this.contextUi;
    }

    public void setViewPort(java.awt.geom.Rectangle2D.Double viewPort) {
        this.viewPort = viewPort;
        this.screenBounds = null;
        this.updateBounds(this.lastWidth, this.lastHeight);
    }

    private void populateGrid(Tile centerTile) {
        int endRow = (this.gridSize - 1) / 2;
        int endCol = (this.gridSize - 1) / 2;
        int startRow = -endRow;
        int startCol = -endCol;
        for (int z = startRow; z <= endRow; z++) {
            for (int x = startCol; x <= endCol; x++) {
                TilePos pos = new TilePos(x, z);
                Tile tile = this.findNeighbor(centerTile, pos);
                this.grid.put(pos, tile);
            }
        }
    }

    public void move(double deltaBlockX, double deltaBlockZ) {
        this.center(this.worldDir, this.mapType, this.centerBlockX + deltaBlockX, this.centerBlockZ + deltaBlockZ, this.zoom);
    }

    public boolean center() {
        return this.center(this.worldDir, this.mapType, this.centerBlockX, this.centerBlockZ, this.zoom);
    }

    public boolean hasUnloadedTile() {
        return this.hasUnloadedTile(false);
    }

    public int getGridSize() {
        return this.gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
        this.srcSize = (double) (gridSize * 512);
    }

    public boolean hasUnloadedTile(boolean preview) {
        for (Entry<TilePos, Tile> entry : this.grid.entrySet()) {
            if (this.isOnScreen((TilePos) entry.getKey())) {
                Tile tile = (Tile) entry.getValue();
                if (tile == null || !tile.hasTexture(this.mapType)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean center(File worldDir, MapType mapType, double blockX, double blockZ, int zoom) {
        boolean mapTypeChanged = !Objects.equals(worldDir, this.worldDir) || !Objects.equals(mapType, this.mapType);
        if (!Objects.equals(worldDir, this.worldDir)) {
            this.worldDir = worldDir;
        }
        if (blockX == this.centerBlockX && blockZ == this.centerBlockZ && zoom == this.zoom && !mapTypeChanged && !this.grid.isEmpty()) {
            if (!Objects.equals(mapType.apiMapType, this.uiState.mapType)) {
                this.updateUIState(true);
            }
            return false;
        } else {
            this.centerBlockX = blockX;
            this.centerBlockZ = blockZ;
            this.zoom = zoom;
            int tileX = Tile.blockPosToTile((int) Math.floor(blockX), this.zoom);
            int tileZ = Tile.blockPosToTile((int) Math.floor(blockZ), this.zoom);
            String newCenterKey = Tile.toCacheKey(tileX, tileZ, zoom);
            boolean centerTileChanged = !newCenterKey.equals(this.centerTileKey);
            this.centerTileKey = newCenterKey;
            if (mapTypeChanged || centerTileChanged || this.grid.isEmpty()) {
                Tile newCenterTile = this.findTile(tileX, tileZ, zoom);
                this.populateGrid(newCenterTile);
            }
            this.updateUIState(true);
            return true;
        }
    }

    public void updateTiles(MapType mapType, int zoom, boolean highQuality, int width, int height, boolean fullUpdate, double xOffset, double yOffset) {
        this.updateTilesTimer1.start();
        this.mapType = mapType;
        this.zoom = zoom;
        this.updateBounds(width, height);
        Tile centerTile = (Tile) this.grid.get(this.centerPos);
        if (centerTile == null || centerTile.zoom != this.zoom) {
            int tileX = Tile.blockPosToTile((int) Math.floor(this.centerBlockX), this.zoom);
            int tileZ = Tile.blockPosToTile((int) Math.floor(this.centerBlockZ), this.zoom);
            centerTile = this.findTile(tileX, tileZ, this.zoom);
            this.populateGrid(centerTile);
        }
        Point2D blockPixelOffset = centerTile.blockPixelOffsetInTile(this.centerBlockX, this.centerBlockZ);
        double blockSizeOffset = Math.pow(2.0, (double) zoom) / 2.0;
        int magic = (this.gridSize >> 1) * 512;
        double displayOffsetX = xOffset + (double) magic - (this.srcSize - (double) this.lastWidth) / 2.0;
        if (this.centerBlockX < 0.0) {
            displayOffsetX -= blockSizeOffset;
        } else {
            displayOffsetX += blockSizeOffset;
        }
        double displayOffsetY = yOffset + (double) magic - (this.srcSize - (double) this.lastHeight) / 2.0;
        if (this.centerBlockZ < 0.0) {
            displayOffsetY -= blockSizeOffset;
        } else {
            displayOffsetY += blockSizeOffset;
        }
        this.centerPixelOffset.setLocation(displayOffsetX + blockPixelOffset.getX(), displayOffsetY + blockPixelOffset.getY());
        this.updateTilesTimer1.stop();
        if (fullUpdate) {
            this.updateTilesTimer2.start();
            for (Entry<TilePos, Tile> entry : this.grid.entrySet()) {
                TilePos pos = (TilePos) entry.getKey();
                Tile tile = (Tile) entry.getValue();
                if (tile == null) {
                    tile = this.findNeighbor(centerTile, pos);
                    this.grid.put(pos, tile);
                }
                if (!tile.hasTexture(this.mapType)) {
                    tile.updateTexture(this.worldDir, this.mapType, highQuality);
                }
            }
            this.updateTilesTimer2.stop();
        }
    }

    public Double getCenterPixelOffset() {
        return this.centerPixelOffset;
    }

    public AABB getBlockBounds() {
        return this.blockBounds;
    }

    public BlockPos getBlockAtPixel(Double pixel) {
        double centerPixelX = (double) this.lastWidth / 2.0;
        double centerPixelZ = (double) this.lastHeight / 2.0;
        double deltaX = (centerPixelX - pixel.x) / this.uiState.blockSize;
        double deltaZ = (centerPixelZ - ((double) this.lastHeight - pixel.y)) / this.uiState.blockSize;
        int x = Mth.floor(this.centerBlockX - deltaX);
        int z = Mth.floor(this.centerBlockZ + deltaZ);
        int y = 0;
        if (DataCache.getPlayer().underground) {
            y = Mth.floor(DataCache.getPlayer().posY);
        } else {
            y = Minecraft.getInstance().level.m_5736_();
        }
        return new BlockPos(x, y, z);
    }

    public Double getBlockPixelInGrid(BlockPos pos) {
        return this.getBlockPixelInGrid((double) pos.m_123341_(), (double) pos.m_123343_());
    }

    public Double getBlockPixelInGrid(double blockX, double blockZ) {
        Minecraft mc = Minecraft.getInstance();
        double localBlockX = blockX - this.centerBlockX;
        double localBlockZ = blockZ - this.centerBlockZ;
        int blockSize = (int) Math.pow(2.0, (double) this.zoom);
        double pixelOffsetX = (double) mc.getWindow().getScreenWidth() / 2.0 + localBlockX * (double) blockSize;
        double pixelOffsetZ = (double) mc.getWindow().getScreenHeight() / 2.0 + localBlockZ * (double) blockSize;
        return new Double(pixelOffsetX, pixelOffsetZ);
    }

    public void draw(GuiGraphics graphics, List<? extends DrawStep> drawStepList, Fullscreen fullscreen, int mouseX, int mouseY, double xOffset, double yOffset, double fontScale, double rotation) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.fullscreen = fullscreen;
        this.draw(graphics, drawStepList, xOffset, yOffset, fontScale, rotation);
    }

    public void draw(GuiGraphics graphics, List<? extends DrawStep> drawStepList, double xOffset, double yOffset, double fontScale, double rotation) {
        if (enabled && drawStepList != null && !drawStepList.isEmpty()) {
            this.draw(graphics, xOffset, yOffset, fontScale, rotation, (DrawStep[]) drawStepList.toArray(new DrawStep[drawStepList.size()]));
        }
    }

    public void draw(GuiGraphics graphics, double xOffset, double yOffset, double fontScale, double rotation, DrawStep... drawSteps) {
        if (enabled) {
            MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
            for (DrawStep.Pass pass : DrawStep.Pass.values()) {
                for (DrawStep drawStep : drawSteps) {
                    drawStep.draw(graphics, buffers, pass, xOffset, yOffset, this, fontScale, rotation);
                }
            }
            buffers.endBatch();
        }
    }

    public void draw(GuiGraphics graphics, float alpha, float bgAlpha, double offsetX, double offsetZ, boolean showGrid) {
        if (enabled && !this.grid.isEmpty()) {
            double centerX = offsetX + this.centerPixelOffset.x;
            double centerZ = offsetZ + this.centerPixelOffset.y;
            GridSpec gridSpec = showGrid ? JourneymapClient.getInstance().getCoreProperties().gridSpecs.getSpec(this.mapType) : null;
            boolean somethingDrew = false;
            for (Entry<TilePos, Tile> entry : this.grid.entrySet()) {
                TilePos pos = (TilePos) entry.getKey();
                Tile tile = (Tile) entry.getValue();
                if (tile != null && tile.draw(graphics, pos, centerX, centerZ, alpha, bgAlpha, gridSpec)) {
                    somethingDrew = true;
                }
            }
            if (!somethingDrew) {
                RegionImageCache.INSTANCE.clear();
            }
        }
        if (!messages.isEmpty()) {
            double centerX = offsetX + this.centerPixelOffset.x + (this.centerPos.endX - this.centerPos.startX) / 2.0;
            double centerZ = offsetZ + this.centerPixelOffset.y + (this.centerPos.endZ - this.centerPos.startZ) / 2.0 - 60.0;
            for (String message : messages.values()) {
                DrawUtil.drawLabel(graphics, message, centerX, centerZ += 20.0, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, 0, 1.0F, 16777215, 1.0F, 1.0, true);
            }
        }
    }

    public void clearGlErrors(boolean report) {
        int err;
        while ((err = RenderWrapper.getError()) != 0) {
            if (report && this.glErrors <= 20) {
                this.glErrors++;
                if (this.glErrors < 20) {
                    this.logger.warn("GL Error occurred during JourneyMap draw: " + err);
                } else {
                    this.logger.warn("GL Error reporting during JourneyMap will be suppressed after max errors: 20");
                }
            }
        }
    }

    public Double getPixel(double blockX, double blockZ) {
        Double pixel = this.getBlockPixelInGrid(blockX, blockZ);
        return this.isOnScreen(pixel) ? pixel : null;
    }

    public void ensureOnScreen(Point2D pixel) {
        if (this.screenBounds != null) {
            double x = pixel.getX();
            if (x < this.screenBounds.x) {
                x = this.screenBounds.x;
            } else if (x > this.screenBounds.getMaxX()) {
                x = this.screenBounds.getMaxX();
            }
            double y = pixel.getY();
            if (y < this.screenBounds.y) {
                y = this.screenBounds.y;
            } else if (y > this.screenBounds.getMaxY()) {
                y = this.screenBounds.getMaxY();
            }
            pixel.setLocation(x, y);
        }
    }

    private boolean isOnScreen(TilePos pos) {
        return true;
    }

    public boolean isOnScreen(Double pixel) {
        return this.screenBounds.contains(pixel);
    }

    public boolean isOnScreen(java.awt.geom.Rectangle2D.Double bounds) {
        return this.screenBounds.intersects(bounds);
    }

    public boolean isOnScreen(double x, double y) {
        return this.screenBounds.contains(x, y);
    }

    public boolean isOnScreen(double startX, double startY, int width, int height) {
        return this.screenBounds == null ? false : this.screenBounds.intersects(startX, startY, (double) width, (double) height);
    }

    private void updateBounds(int width, int height) {
        if (this.screenBounds == null || this.lastWidth != width || this.lastHeight != height || this.blockBounds == null) {
            this.lastWidth = width;
            this.lastHeight = height;
            if (this.viewPort == null) {
                int pad = 32;
                this.screenBounds = new java.awt.geom.Rectangle2D.Double((double) (-pad), (double) (-pad), (double) (width + pad), (double) (height + pad));
            } else {
                this.screenBounds = new java.awt.geom.Rectangle2D.Double(((double) width - this.viewPort.width) / 2.0, ((double) height - this.viewPort.height) / 2.0, this.viewPort.width, this.viewPort.height);
            }
            ClientAPI.INSTANCE.flagOverlaysForRerender();
        }
    }

    public void updateUIState(boolean isActive) {
        if (!isActive || this.screenBounds != null) {
            UIState newState = null;
            if (isActive) {
                int worldHeight = Minecraft.getInstance().level.m_6042_().logicalHeight();
                int pad = 32;
                BlockPos upperLeft = this.getBlockAtPixel(new Double(this.screenBounds.getMinX(), this.screenBounds.getMinY()));
                BlockPos lowerRight = this.getBlockAtPixel(new Double(this.screenBounds.getMaxX(), this.screenBounds.getMaxY()));
                this.blockBounds = new AABB(upperLeft.offset(-pad, 0, -pad), lowerRight.offset(pad, worldHeight, pad));
                try {
                    newState = new UIState(this.contextUi, true, this.mapType.dimension, this.zoom, this.mapType.apiMapType, new BlockPos(Mth.floor(this.centerBlockX), 0, Mth.floor(this.centerBlockZ)), this.mapType.vSlice, this.blockBounds, this.screenBounds);
                } catch (Exception var8) {
                    this.logger.error("Error Creating new UIState: ", var8);
                }
            } else {
                newState = UIState.newInactive(this.uiState);
            }
            if (this.uiState == null && newState != null || newState != null && !newState.equals(this.uiState)) {
                this.uiState = newState;
                ClientEventManager clientEventManager = ClientAPI.INSTANCE.getClientEventManager();
                if (clientEventManager.canFireClientEvent(ClientEvent.Type.DISPLAY_UPDATE)) {
                    clientEventManager.fireDisplayUpdateEvent(new DisplayUpdateEvent(this.uiState));
                }
            }
        }
    }

    private Tile findNeighbor(Tile tile, TilePos pos) {
        return pos.deltaX == 0 && pos.deltaZ == 0 ? tile : this.findTile(tile.tileX + pos.deltaX, tile.tileZ + pos.deltaZ, tile.zoom);
    }

    private Tile findTile(int tileX, int tileZ, int zoom) {
        return Tile.create(tileX, tileZ, zoom, this.worldDir, this.mapType, JourneymapClient.getInstance().getCoreProperties().tileHighDisplayQuality.get());
    }

    public void setContext(File worldDir, MapType mapType) {
        this.worldDir = worldDir;
        this.mapType = mapType;
        TileDrawStepCache.setContext(worldDir, mapType);
    }

    public void updateRotation(PoseStack poseStack, double rotation) {
        this.currentRotation = rotation;
        this.viewport[0] = 0;
        this.viewport[1] = 0;
        this.viewport[2] = Minecraft.getInstance().getWindow().getWidth();
        this.viewport[3] = Minecraft.getInstance().getWindow().getHeight();
        poseStack.last().pose().get(this.modelMatrixBuf);
        RenderWrapper.getProjectionMatrix().get(this.projMatrixBuf);
        this.modelMatrix.set(this.modelMatrixBuf);
        this.projectionMatrix.set(this.projMatrixBuf).mul(this.modelMatrix);
    }

    public Point2D shiftWindowPosition(double x, double y, int shiftX, int shiftY) {
        if (this.currentRotation % 360.0 == 0.0) {
            return new Double(x + (double) shiftX, y + (double) shiftY);
        } else {
            this.projectionMatrix.project((float) x, (float) y, 0.0F, this.viewport, this.windowPos);
            this.projectionMatrix.unproject(this.windowPos.get(0) + (float) shiftX, this.windowPos.get(1) + (float) shiftY, 0.0F, this.viewport, this.objPose);
            return new Float(this.objPose.get(0), this.objPose.get(1));
        }
    }

    public Double getWindowPosition(Double matrixPixel) {
        if (this.currentRotation % 360.0 == 0.0) {
            return matrixPixel;
        } else {
            this.projectionMatrix.project((float) matrixPixel.getX(), (float) matrixPixel.getY(), 0.0F, this.viewport, this.windowPos);
            return new Double((double) this.windowPos.get(0), (double) this.windowPos.get(1));
        }
    }

    public Double getMatrixPosition(Double windowPixel) {
        this.projectionMatrix.unproject((float) windowPixel.x, (float) windowPixel.y, 0.0F, this.viewport, this.objPose);
        return new Double((double) this.objPose.get(0), (double) this.objPose.get(1));
    }

    public double getCenterBlockX() {
        return this.centerBlockX;
    }

    public double getCenterBlockZ() {
        return this.centerBlockZ;
    }

    public File getWorldDir() {
        return this.worldDir;
    }

    public MapType getMapType() {
        return this.mapType;
    }

    public int getZoom() {
        return this.zoom;
    }

    public boolean setZoom(int zoom) {
        return this.center(this.worldDir, this.mapType, this.centerBlockX, this.centerBlockZ, zoom);
    }

    public int getRenderSize() {
        return this.gridSize * 512;
    }

    public void clear() {
        this.grid.clear();
        messages.clear();
    }

    public int getWidth() {
        return this.lastWidth;
    }

    public int getHeight() {
        return this.lastHeight;
    }

    public UIState getUIState() {
        return this.uiState;
    }
}