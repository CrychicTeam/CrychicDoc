package journeymap.client.ui.minimap;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import journeymap.client.JourneymapClient;
import journeymap.client.api.display.Context;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.api.util.UIState;
import journeymap.client.data.DataCache;
import journeymap.client.feature.Feature;
import journeymap.client.feature.FeatureManager;
import journeymap.client.log.JMLogger;
import journeymap.client.log.StatTimer;
import journeymap.client.model.EntityDTO;
import journeymap.client.model.MapState;
import journeymap.client.model.MapType;
import journeymap.client.properties.CoreProperties;
import journeymap.client.properties.MiniMapProperties;
import journeymap.client.render.RenderWrapper;
import journeymap.client.render.draw.DrawEntityStep;
import journeymap.client.render.draw.DrawStep;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.render.draw.DrawWayPointStep;
import journeymap.client.render.draw.RadarDrawStepFactory;
import journeymap.client.render.draw.WaypointDrawStepFactory;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.UIManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class MiniMap implements Selectable {

    private static final MapState state = new MapState();

    private static final GridRenderer gridRenderer = new GridRenderer(Context.UI.Minimap);

    private final Minecraft mc = Minecraft.getInstance();

    private final WaypointDrawStepFactory waypointRenderer = new WaypointDrawStepFactory();

    private final RadarDrawStepFactory radarRenderer = new RadarDrawStepFactory();

    private Texture playerArrowFg;

    private Texture playerArrowBg;

    private int playerArrowColor;

    private MiniMapProperties miniMapProperties;

    private StatTimer drawTimer;

    private StatTimer refreshStateTimer;

    private DisplayVars dv;

    private boolean minimapDragging = false;

    private int mouseDragOffsetX = 0;

    private int mouseDragOffsetY = 0;

    private Double centerPoint;

    private java.awt.geom.Rectangle2D.Double centerRect;

    private long initTime;

    private long lastAutoDayNightTime = -1L;

    private Boolean lastPlayerUnderground;

    private boolean drawing = false;

    public MiniMap(MiniMapProperties miniMapProperties) {
        gridRenderer.setGridSize(3);
        this.initTime = System.currentTimeMillis();
        this.setMiniMapProperties(miniMapProperties);
    }

    public static synchronized MapState state() {
        return state;
    }

    public static synchronized UIState uiState() {
        return gridRenderer.getUIState();
    }

    public static void updateUIState(boolean isActive) {
        if (Minecraft.getInstance().level != null) {
            gridRenderer.updateUIState(isActive);
        }
    }

    public DisplayVars getDisplayVars() {
        return this.dv;
    }

    public boolean withinBounds(double mouseX, double mouseY) {
        double scale = Minecraft.getInstance().getWindow().getGuiScale();
        double posX = mouseX * scale;
        double posY = mouseY * scale;
        DisplayVars vars = UIManager.INSTANCE.getMiniMap().getDisplayVars();
        return posX > (double) vars.textureX && posX < (double) (vars.textureX + vars.minimapWidth) && posY > (double) vars.textureY && posY < (double) (vars.textureY + vars.minimapHeight);
    }

    private void initGridRenderer() {
        gridRenderer.clear();
        state.requireRefresh();
        if (this.mc.player != null && this.mc.player.m_6084_()) {
            state.refresh(this.mc, this.mc.player, this.miniMapProperties);
            MapType mapType = state.getMapType();
            int gridSize = this.miniMapProperties.getSize() <= 768 ? 3 : 5;
            gridRenderer.setGridSize(gridSize);
            gridRenderer.setContext(state.getWorldDir(), mapType);
            gridRenderer.center(state.getWorldDir(), mapType, this.mc.player.m_20185_(), this.mc.player.m_20189_(), this.miniMapProperties.zoomLevel.get());
            boolean highQuality = JourneymapClient.getInstance().getCoreProperties().tileHighDisplayQuality.get();
            gridRenderer.updateTiles(state.getMapType(), state.getZoom(), highQuality, this.mc.getWindow().getScreenWidth(), this.mc.getWindow().getScreenHeight(), true, 0.0, 0.0);
        }
    }

    public void resetInitTime() {
        this.initTime = System.currentTimeMillis();
    }

    public void setMiniMapProperties(MiniMapProperties miniMapProperties) {
        this.miniMapProperties = miniMapProperties;
        state().requireRefresh();
        this.reset();
    }

    public MiniMapProperties getCurrentMinimapProperties() {
        return this.miniMapProperties;
    }

    public void drawMap(GuiGraphics graphics) {
        this.drawMap(graphics, false);
    }

    public void drawMap(GuiGraphics graphics, boolean preview) {
        StatTimer timer = this.drawTimer;
        try {
            MultiBufferSource.BufferSource buffers = graphics.bufferSource();
            buffers.endBatch();
            graphics.pose().pushPose();
            if (this.mc.player == null || !this.mc.player.m_6084_()) {
                return;
            }
            gridRenderer.clearGlErrors(false);
            boolean doStateRefresh = state.shouldRefresh(this.mc, this.miniMapProperties);
            if (doStateRefresh) {
                timer = this.refreshStateTimer.start();
                this.autoDayNight();
                gridRenderer.setContext(state.getWorldDir(), state.getMapType());
                if (!preview) {
                    state.refresh(this.mc, this.mc.player, this.miniMapProperties);
                }
                ClientAPI.INSTANCE.flagOverlaysForRerender();
            } else {
                timer.start();
            }
            int width = this.mc.getWindow().getScreenWidth();
            int height = this.mc.getWindow().getScreenHeight();
            if (height != 0 && width != 0) {
                boolean moved = gridRenderer.center(state.getWorldDir(), state.getMapType(), this.mc.player.m_20185_(), this.mc.player.m_20189_(), this.miniMapProperties.zoomLevel.get());
                if (moved || doStateRefresh) {
                    gridRenderer.updateTiles(state.getMapType(), state.getZoom(), state.isHighQuality(), this.mc.getWindow().getScreenWidth(), this.mc.getWindow().getScreenHeight(), doStateRefresh || preview, 0.0, 0.0);
                }
                if (doStateRefresh) {
                    boolean checkWaypointDistance = JourneymapClient.getInstance().getWaypointProperties().maxDistance.get() > 0;
                    state.generateDrawSteps(this.mc, gridRenderer, this.waypointRenderer, this.radarRenderer, this.miniMapProperties, checkWaypointDistance);
                    state.updateLastRefresh();
                }
                this.updateDisplayVars(false);
                long now = System.currentTimeMillis();
                DrawUtil.sizeDisplay(graphics.pose(), (double) width, (double) height);
                RenderWrapper.enableBlend();
                RenderWrapper.blendFunc(770, 0);
                RenderWrapper.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderWrapper.enableDepthTest();
                this.beginStencil(graphics.pose());
                double rotation = 0.0;
                switch(this.dv.orientation) {
                    case North:
                        rotation = 0.0;
                        break;
                    case OldNorth:
                        rotation = 90.0;
                        break;
                    case PlayerHeading:
                        if (this.dv.shape == Shape.Circle) {
                            rotation = (double) (180.0F - this.mc.player.m_146908_());
                        }
                }
                this.startMapRotation(graphics.pose(), rotation);
                try {
                    graphics.pose().translate((float) this.dv.translateX, (float) this.dv.translateY, 0.0F);
                    gridRenderer.draw(graphics, this.dv.terrainAlpha, this.miniMapProperties.backgroundAlpha.get(), 0.0, 0.0, this.miniMapProperties.showGrid.get());
                    this.centerPoint = gridRenderer.getPixel(this.mc.player.m_20185_(), this.mc.player.m_20189_());
                    this.centerRect = new java.awt.geom.Rectangle2D.Double(this.centerPoint.x - (double) (this.dv.minimapWidth / 2), this.centerPoint.y - (double) (this.dv.minimapHeight / 2), (double) this.dv.minimapWidth, (double) this.dv.minimapHeight);
                    this.drawOnMapEntities(graphics, buffers, rotation);
                    graphics.pose().translate((float) (-this.dv.translateX), (float) (-this.dv.translateY), 0.0F);
                    ReticleOrientation reticleOrientation = null;
                    if (this.dv.showReticle) {
                        reticleOrientation = this.dv.minimapFrame.getReticleOrientation();
                        if (reticleOrientation == ReticleOrientation.Compass) {
                            this.dv.minimapFrame.drawReticle(graphics.pose());
                        } else {
                            this.startMapRotation(graphics.pose(), (double) this.mc.player.m_146908_());
                            this.dv.minimapFrame.drawReticle(graphics.pose());
                            this.stopMapRotation(graphics.pose(), (double) this.mc.player.m_146908_());
                        }
                    }
                    long lastMapChangeTime = state.getLastMapTypeChange();
                    if (now - lastMapChangeTime <= 1000L) {
                        this.stopMapRotation(graphics.pose(), rotation);
                        graphics.pose().translate((float) this.dv.translateX, (float) this.dv.translateY, 0.0F);
                        float alpha = (float) Math.min(255L, Math.max(0L, 1100L - (now - lastMapChangeTime))) / 255.0F;
                        Double windowCenter = gridRenderer.getWindowPosition(this.centerPoint);
                        this.dv.getMapTypeStatus(state.getMapType()).draw(graphics.pose(), windowCenter, alpha, 0.0);
                        graphics.pose().translate((float) (-this.dv.translateX), (float) (-this.dv.translateY), 0.0F);
                        this.startMapRotation(graphics.pose(), rotation);
                    }
                    if (now - this.initTime <= 1000L) {
                        this.stopMapRotation(graphics.pose(), rotation);
                        graphics.pose().translate((float) this.dv.translateX, (float) this.dv.translateY, 1000.0F);
                        float alpha = (float) Math.min(255L, Math.max(0L, 1100L - (now - this.initTime))) / 255.0F;
                        Double windowCenter = gridRenderer.getWindowPosition(this.centerPoint);
                        this.dv.getMapPresetStatus(state.getMapType(), this.miniMapProperties.getId()).draw(graphics, windowCenter, alpha, 0.0);
                        graphics.pose().translate((float) (-this.dv.translateX), (float) (-this.dv.translateY), -1000.0F);
                        this.startMapRotation(graphics.pose(), rotation);
                    }
                    this.endStencil();
                    if (!this.dv.frameRotates && rotation != 0.0) {
                        this.stopMapRotation(graphics.pose(), rotation);
                    }
                    this.dv.minimapFrame.drawFrame(graphics.pose());
                    if (!this.dv.frameRotates && rotation != 0.0) {
                        this.startMapRotation(graphics.pose(), rotation);
                    }
                    if (this.dv.showCompass) {
                        this.dv.minimapCompassPoints.drawPoints(graphics.pose(), rotation);
                    }
                    graphics.pose().translate((float) this.dv.translateX, (float) this.dv.translateY, 0.0F);
                    this.drawOnMapWaypoints(graphics, buffers, rotation);
                    if (this.miniMapProperties.showSelf.get() && this.playerArrowFg != null) {
                        float playerArrowScale = this.miniMapProperties.selfDisplayScale.get();
                        if (this.centerPoint != null) {
                            DrawUtil.drawColoredEntity(graphics.pose(), this.centerPoint.getX(), this.centerPoint.getY(), this.playerArrowBg, 16777215, 1.0F, playerArrowScale, (double) this.mc.player.m_146908_());
                            DrawUtil.drawColoredEntity(graphics.pose(), this.centerPoint.getX(), this.centerPoint.getY(), this.playerArrowFg, this.playerArrowColor, 1.0F, playerArrowScale, (double) this.mc.player.m_146908_());
                        }
                    }
                    if (this.dv.showCompass) {
                        graphics.pose().translate((float) (-this.dv.translateX), (float) (-this.dv.translateY), 0.0F);
                        this.dv.minimapCompassPoints.drawLabels(graphics.pose(), buffers, rotation);
                    }
                    graphics.pose().popPose();
                    this.dv.drawInfoLabels(graphics.pose(), buffers, now);
                } finally {
                    this.stopMapRotation(graphics.pose(), rotation);
                    buffers.endBatch();
                }
                DrawUtil.sizeDisplay(graphics.pose(), (double) this.dv.mainWindow.getWidth() / this.dv.mainWindow.getGuiScale(), (double) this.dv.mainWindow.getHeight() / this.dv.mainWindow.getGuiScale());
                return;
            }
        } catch (Throwable var27) {
            JMLogger.throwLogOnce("Error during MiniMap.drawMap(): " + var27.getMessage(), var27);
            return;
        } finally {
            this.cleanup();
            timer.stop();
            gridRenderer.clearGlErrors(true);
        }
    }

    private void drawOnMapEntities(GuiGraphics graphics, MultiBufferSource buffers, double rotation) {
        for (DrawStep.Pass pass : DrawStep.Pass.values()) {
            for (DrawStep drawStep : state.getDrawSteps()) {
                boolean onScreen;
                if (drawStep instanceof DrawEntityStep) {
                    Double position = ((DrawEntityStep) drawStep).getPosition(0.0, 0.0, gridRenderer, true);
                    onScreen = this.isOnScreen(position, this.centerPoint, this.centerRect);
                } else {
                    onScreen = true;
                }
                if (onScreen) {
                    drawStep.draw(graphics, buffers, pass, 0.0, 0.0, gridRenderer, this.dv.fontScale, rotation);
                }
            }
        }
    }

    private void drawOnMapWaypoints(GuiGraphics graphics, MultiBufferSource buffers, double rotation) {
        boolean showLabel = this.miniMapProperties.showWaypointLabels.get();
        for (DrawStep.Pass pass : DrawStep.Pass.values()) {
            for (DrawWayPointStep drawWayPointStep : state.getDrawWaypointSteps()) {
                drawWayPointStep.setIconScale(this.miniMapProperties.waypointIconScale.get());
                boolean onScreen = false;
                if (pass == DrawStep.Pass.Object) {
                    Double waypointPos = drawWayPointStep.getPosition(0.0, 0.0, gridRenderer, true);
                    onScreen = this.isOnScreen(waypointPos, this.centerPoint, this.centerRect);
                    drawWayPointStep.setOnScreen(onScreen);
                } else {
                    onScreen = drawWayPointStep.isOnScreen();
                }
                if (onScreen) {
                    drawWayPointStep.setLabelScale(this.miniMapProperties.waypointLabelScale.get());
                    drawWayPointStep.setShowLabel(showLabel);
                    drawWayPointStep.draw(graphics, buffers, pass, 0.0, 0.0, gridRenderer, this.dv.fontScale, rotation);
                } else {
                    Double point = this.getPointOnFrame(drawWayPointStep.getPosition(0.0, 0.0, gridRenderer, true), this.centerPoint, 8.0);
                    drawWayPointStep.drawOffscreen(graphics.pose(), DrawStep.Pass.Object, point, rotation);
                }
            }
        }
    }

    private void startMapRotation(PoseStack poseStack, double rotation) {
        poseStack.pushPose();
        if (rotation % 360.0 != 0.0) {
            double width = (double) ((this.dv.displayWidth >> 1) + this.dv.translateX);
            double height = (double) ((this.dv.displayHeight >> 1) + this.dv.translateY);
            poseStack.translate(width, height, 0.0);
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) rotation));
            poseStack.translate(-width, -height, 0.0);
        }
        gridRenderer.updateRotation(poseStack, rotation);
    }

    private void stopMapRotation(PoseStack poseStack, double rotation) {
        poseStack.popPose();
        gridRenderer.updateRotation(poseStack, rotation);
    }

    private boolean isOnScreen(Double objectPixel, Point2D centerPixel, java.awt.geom.Rectangle2D.Double centerRect) {
        return this.dv.shape == Shape.Circle ? centerPixel.distance(objectPixel) < (double) (this.dv.minimapWidth >> 1) : centerRect.contains(gridRenderer.getWindowPosition(objectPixel));
    }

    private Double getPointOnFrame(Double objectPixel, Point2D centerPixel, double offset) {
        if (this.dv.shape == Shape.Circle) {
            Double point1 = new Double(centerPixel.getX() + offset, centerPixel.getY() + offset);
            double bearing = Math.atan2(objectPixel.getY() - point1.getY(), objectPixel.getX() - point1.getX());
            return new Double((double) (this.dv.minimapWidth >> 1) * Math.cos(bearing) + point1.getX(), (double) (this.dv.minimapHeight >> 1) * Math.sin(bearing) + point1.getY());
        } else {
            java.awt.geom.Rectangle2D.Double rect = new java.awt.geom.Rectangle2D.Double((double) (this.dv.textureX - this.dv.translateX), (double) (this.dv.textureY - this.dv.translateY), (double) this.dv.minimapWidth, (double) this.dv.minimapHeight);
            if (objectPixel.x > rect.getMaxX()) {
                objectPixel.x = rect.getMaxX() + offset;
            } else if (objectPixel.x < rect.getMinX()) {
                objectPixel.x = rect.getMinX() + offset;
            }
            if (objectPixel.y > rect.getMaxY()) {
                objectPixel.y = rect.getMaxY() + offset;
            } else if (objectPixel.y < rect.getMinY()) {
                objectPixel.y = rect.getMinY() + offset;
            }
            return objectPixel;
        }
    }

    private void beginStencil(PoseStack poseStack) {
        try {
            this.cleanup();
            DrawUtil.zLevel = 1000.0;
            this.drawing = true;
            RenderWrapper.colorMask(false, false, false, false);
            this.dv.minimapFrame.drawMask(poseStack);
            RenderWrapper.colorMask(true, true, true, true);
            DrawUtil.zLevel = 0.0;
            RenderWrapper.depthMask(false);
            RenderWrapper.depthFunc(516);
        } catch (Throwable var3) {
            JMLogger.throwLogOnce("Error during MiniMap.beginStencil()", var3);
        }
    }

    private void endStencil() {
        try {
            RenderWrapper.disableDepthTest();
        } catch (Throwable var2) {
            JMLogger.throwLogOnce("Error during MiniMap.endStencil()", var2);
        }
    }

    private void cleanup() {
        try {
            DrawUtil.zLevel = 0.0;
            this.drawing = false;
            RenderWrapper.depthMask(true);
            RenderWrapper.clear(256);
            RenderWrapper.enableDepthTest();
            RenderWrapper.depthFunc(515);
            RenderWrapper.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderWrapper.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        } catch (Throwable var2) {
            JMLogger.throwLogOnce("Error during MiniMap.cleanup()", var2);
        }
    }

    private void autoDayNight() {
        if (this.mc.level != null) {
            boolean wasInCaves = false;
            if (this.miniMapProperties.showCaves.get() && FeatureManager.getInstance().isAllowed(Feature.MapCaves)) {
                EntityDTO player = DataCache.getPlayer();
                boolean neverChecked = this.lastPlayerUnderground == null;
                boolean playerUnderground = player.underground;
                if (neverChecked || playerUnderground != this.lastPlayerUnderground) {
                    this.lastPlayerUnderground = playerUnderground;
                    if (playerUnderground) {
                        state.setMapType(MapType.underground(player));
                    } else {
                        state.setMapType(MapType.from(this.miniMapProperties.preferredMapType.get(), player));
                        wasInCaves = true;
                    }
                }
                MapType currentMapType = state.getMapType();
                if (playerUnderground && currentMapType.isUnderground() && currentMapType.vSlice != player.chunkCoordY) {
                    state.setMapType(MapType.underground(player));
                }
            }
            if (this.miniMapProperties.showDayNight.get() && (wasInCaves || state.getMapType().isDayOrNight())) {
                long NIGHT = 13800L;
                long worldTime = this.mc.level.m_46468_() % 24000L;
                boolean neverCheckedx = this.lastAutoDayNightTime == -1L;
                if (worldTime < 13800L || !neverCheckedx && this.lastAutoDayNightTime >= 13800L) {
                    if (worldTime < 13800L && (neverCheckedx || this.lastAutoDayNightTime >= 13800L)) {
                        this.lastAutoDayNightTime = worldTime;
                        state.setMapType(MapType.day(this.mc.level.m_46472_()));
                    }
                } else {
                    this.lastAutoDayNightTime = worldTime;
                    state.setMapType(MapType.night(this.mc.level.m_46472_()));
                }
            }
        }
    }

    public void resetState() {
        state.setMapType(MapType.day(this.mc.level.m_46472_()));
    }

    public void reset() {
        this.initTime = System.currentTimeMillis();
        this.lastAutoDayNightTime = -1L;
        this.initGridRenderer();
        this.updateDisplayVars(this.miniMapProperties.shape.get(), this.miniMapProperties.positionX.get(), this.miniMapProperties.positionY.get(), this.miniMapProperties.position.get(), true, true);
        GridRenderer.clearDebugMessages();
        CoreProperties coreProperties = JourneymapClient.getInstance().getCoreProperties();
        this.playerArrowColor = coreProperties.getColor(coreProperties.colorSelf);
        this.playerArrowBg = TextureCache.getTexture(TextureCache.PlayerArrowBG);
        this.playerArrowFg = TextureCache.getTexture(TextureCache.PlayerArrow);
    }

    public void updateDisplayVars(boolean force) {
        this.updateDisplayVars(force, true);
    }

    public void updateDisplayVars(boolean force, boolean save) {
        if (this.dv != null) {
            this.updateDisplayVars(this.dv.shape, this.dv.positionX, this.dv.positionY, this.dv.position, force, save);
        }
    }

    public void updateDisplayVars(Shape shape, float posX, float posY, Position position, boolean force, boolean save) {
        if (this.dv == null || force || this.mc.getWindow().getScreenHeight() != this.dv.displayHeight || this.mc.getWindow().getScreenWidth() != this.dv.displayWidth || this.dv.shape != shape || this.dv.positionX != posX || this.dv.positionY != posY || this.dv.position != position || this.dv.fontScale != (double) this.miniMapProperties.fontScale.get().floatValue()) {
            if (save) {
                this.initGridRenderer();
            }
            if (force) {
                shape = this.miniMapProperties.shape.get();
                posX = this.miniMapProperties.positionX.get();
                posY = this.miniMapProperties.positionY.get();
                position = this.miniMapProperties.position.get();
                state.setForceRefreshState(true);
            }
            this.miniMapProperties.shape.set(shape);
            this.miniMapProperties.positionX.set(Float.valueOf(posX));
            this.miniMapProperties.position.set(position);
            this.miniMapProperties.positionY.set(Float.valueOf(posY));
            this.miniMapProperties.save();
            DisplayVars oldDv = this.dv;
            this.dv = new DisplayVars(this.mc, this.miniMapProperties);
            if (oldDv == null || oldDv.shape != this.dv.shape) {
                String timerName = String.format("MiniMap%s.%s", this.miniMapProperties.getId(), shape.name());
                this.drawTimer = StatTimer.get(timerName, 100);
                this.drawTimer.reset();
                this.refreshStateTimer = StatTimer.get(timerName + "+refreshState", 5);
                this.refreshStateTimer.reset();
            }
            double xpad = 0.0;
            double ypad = 0.0;
            java.awt.geom.Rectangle2D.Double viewPort = new java.awt.geom.Rectangle2D.Double((double) this.dv.textureX + xpad, (double) this.dv.textureY + ypad, (double) this.dv.minimapWidth - 2.0 * xpad, (double) this.dv.minimapHeight - 2.0 * ypad);
            gridRenderer.setViewPort(viewPort);
            updateUIState(true);
        }
    }

    public boolean isDrawing() {
        return this.drawing;
    }

    public String getLocation() {
        int playerX = Mth.floor(this.mc.player.m_20185_());
        int playerZ = Mth.floor(this.mc.player.m_20189_());
        int playerY = Mth.floor(this.mc.player.m_20191_().minY);
        return this.dv.locationFormatKeys.format(this.dv.locationFormatVerbose, playerX, playerZ, playerY, this.mc.player.m_146904_() >> 4);
    }

    public String getBiome() {
        return state.getPlayerBiome();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.withinBounds(pMouseX, pMouseY) && !this.minimapDragging) {
            this.minimapDragging = true;
            double scale = Minecraft.getInstance().getWindow().getGuiScale();
            double posX = pMouseX * scale;
            double posY = pMouseY * scale;
            this.mouseDragOffsetX = (int) (posX - (double) this.getDisplayVars().textureX);
            this.mouseDragOffsetY = (int) (posY - (double) this.getDisplayVars().textureY);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.minimapDragging) {
            int screenHeight = Minecraft.getInstance().getWindow().getScreenHeight();
            int screenWidth = Minecraft.getInstance().getWindow().getScreenWidth();
            Vec2 vec2 = this.validateScreenBounds(pMouseX, pMouseY);
            this.miniMapProperties.positionX.set(Float.valueOf(vec2.x / (float) screenWidth));
            this.miniMapProperties.positionY.set(Float.valueOf(vec2.y / (float) screenHeight));
            this.updateDisplayVars(true, false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (this.minimapDragging) {
            this.mouseDragOffsetX = 0;
            this.mouseDragOffsetY = 0;
            this.minimapDragging = false;
            this.updateDisplayVars(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        long windowId = Minecraft.getInstance().getWindow().getWindow();
        float speed = JourneymapClient.getInstance().getActiveMiniMapProperties().minimapKeyMovementSpeed.get();
        if (InputConstants.isKeyDown(windowId, 265)) {
            this.moveMiniMapOnKey(0.0F, -speed);
        } else if (InputConstants.isKeyDown(windowId, 264)) {
            this.moveMiniMapOnKey(0.0F, speed);
        } else if (InputConstants.isKeyDown(windowId, 263)) {
            this.moveMiniMapOnKey(-speed, 0.0F);
        } else if (InputConstants.isKeyDown(windowId, 262)) {
            this.moveMiniMapOnKey(speed, 0.0F);
        }
    }

    private void moveMiniMapOnKey(float incX, float incY) {
        float pX = this.miniMapProperties.positionX.get();
        float pY = this.miniMapProperties.positionY.get();
        int screenHeight = Minecraft.getInstance().getWindow().getHeight();
        int screenWidth = Minecraft.getInstance().getWindow().getWidth();
        float texX = (float) screenWidth * (pX + incX);
        float texY = (float) screenHeight * (pY + incY);
        DisplayVars vars = UIManager.INSTANCE.getMiniMap().getDisplayVars();
        float x = texX < 0.0F ? 0.0F : (texX + (float) vars.minimapWidth > (float) screenWidth ? pX : pX + incX);
        float y = texY < 0.0F ? 0.0F : (texY + (float) vars.minimapHeight > (float) screenHeight ? pY : pY + incY);
        this.miniMapProperties.positionX.set(Float.valueOf(x));
        this.miniMapProperties.positionY.set(Float.valueOf(y));
        this.updateDisplayVars(true);
    }

    @Override
    public void renderBorder(GuiGraphics graphics, int color) {
        int scaleFactor = (int) this.mc.getWindow().getGuiScale();
        int startX = this.getDisplayVars().textureX / scaleFactor;
        int startY = this.getDisplayVars().textureY / scaleFactor;
        int width = this.getDisplayVars().minimapWidth / scaleFactor;
        int height = this.getDisplayVars().minimapHeight / scaleFactor;
        int endX = startX + width;
        int endY = startY + height;
        color = Position.Custom.equals(this.miniMapProperties.position.get()) ? color : -65536;
        graphics.fill(startX, startY - 1, endX + 1, startY + 1, color);
        graphics.fill(startX - 1, startY - 1, startX + 1, endY + 2, color);
        graphics.fill(startX, endY, endX + 1, endY + 2, color);
        graphics.fill(endX, startY - 1, endX + 2, endY + 2, color);
    }

    public Vec2 validateScreenBounds(double pMouseX, double pMouseY) {
        int screenHeight = Minecraft.getInstance().getWindow().getHeight();
        int screenWidth = Minecraft.getInstance().getWindow().getWidth();
        int scale = (int) Minecraft.getInstance().getWindow().getGuiScale();
        float posX = (float) (pMouseX * (double) scale - (double) this.mouseDragOffsetX);
        float posY = (float) (pMouseY * (double) scale - (double) this.mouseDragOffsetY);
        DisplayVars vars = UIManager.INSTANCE.getMiniMap().getDisplayVars();
        float x = posX < 0.0F ? 0.0F : (posX + (float) vars.minimapWidth > (float) screenWidth ? (float) (screenWidth - vars.minimapWidth) : posX);
        float y = posY < 0.0F ? 0.0F : (posY + (float) vars.minimapHeight > (float) screenHeight ? (float) (screenHeight - vars.minimapHeight) : posY);
        return new Vec2(x, y);
    }
}