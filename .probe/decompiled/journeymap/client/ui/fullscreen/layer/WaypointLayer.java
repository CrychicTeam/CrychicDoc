package journeymap.client.ui.fullscreen.layer;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import journeymap.client.JourneymapClient;
import journeymap.client.data.DataCache;
import journeymap.client.data.WaypointsData;
import journeymap.client.render.draw.DrawStep;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.render.draw.DrawWayPointStep;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.waypoint.Waypoint;
import journeymap.common.nbt.RegionData;
import journeymap.common.nbt.RegionDataStorageHandler;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class WaypointLayer extends Layer {

    private final long hoverDelay = 5L;

    private final List<DrawStep> drawStepList;

    private final WaypointLayer.BlockOutlineDrawStep clickDrawStep;

    BlockPos lastCoord = null;

    float iconScale = 1.0F;

    long startHover = 0L;

    DrawWayPointStep selectedWaypointStep = null;

    Waypoint selected = null;

    public WaypointLayer(Fullscreen fullscreen) {
        super(fullscreen);
        this.drawStepList = new ArrayList(1);
        this.clickDrawStep = new WaypointLayer.BlockOutlineDrawStep(BlockPos.ZERO);
        this.iconScale = JourneymapClient.getInstance().getFullMapProperties().waypointIconScale.get();
    }

    @Override
    public List<DrawStep> onMouseMove(Minecraft mc, GridRenderer gridRenderer, Double mousePosition, BlockPos blockCoord, float fontScale, boolean isScrolling) {
        this.drawStepList.clear();
        if (!WaypointsData.isManagerEnabled()) {
            return this.drawStepList;
        } else {
            if (this.lastCoord == null) {
                this.lastCoord = blockCoord;
            }
            long now = Util.getMillis();
            this.iconScale = JourneymapClient.getInstance().getFullMapProperties().waypointIconScale.get();
            int proximity = (int) Math.max(1.0, 8.0 / gridRenderer.getUIState().blockSize * (double) this.iconScale);
            if (this.clickDrawStep.blockCoord == null || blockCoord.equals(this.clickDrawStep.blockCoord) && !blockCoord.equals(BlockPos.ZERO)) {
                this.drawStepList.add(this.clickDrawStep);
            } else {
                this.unclick();
            }
            AABB area = new AABB((double) (blockCoord.m_123341_() - proximity), (double) (mc.level.m_6042_().minY() - 1), (double) (blockCoord.m_123343_() - proximity), (double) (blockCoord.m_123341_() + proximity), (double) (mc.level.m_6042_().logicalHeight() + 1), (double) (blockCoord.m_123343_() + proximity));
            if (!this.lastCoord.equals(blockCoord)) {
                if (!area.contains(new Vec3((double) this.lastCoord.m_123341_(), 1.0, (double) this.lastCoord.m_123343_()))) {
                    this.selected = null;
                    this.lastCoord = blockCoord;
                    this.startHover = now;
                    return this.drawStepList;
                }
            } else if (this.selected != null) {
                this.select(this.selected);
                return this.drawStepList;
            }
            if (now - this.startHover < 5L) {
                return this.drawStepList;
            } else {
                Collection<Waypoint> waypoints = DataCache.INSTANCE.getWaypoints(false);
                ArrayList<Waypoint> proximal = new ArrayList();
                for (Waypoint waypoint : waypoints) {
                    if (waypoint.isEnable() && waypoint.isInPlayerDimension() && area.contains(new Vec3((double) waypoint.getX(), (double) waypoint.getY(), (double) waypoint.getZ()))) {
                        proximal.add(waypoint);
                    }
                }
                if (!proximal.isEmpty()) {
                    if (proximal.size() > 1) {
                        this.sortByDistance(proximal, blockCoord);
                    }
                    this.select((Waypoint) proximal.get(0));
                }
                return this.drawStepList;
            }
        }
    }

    @Override
    public List<DrawStep> onMouseClick(Minecraft mc, GridRenderer gridRenderer, Double mousePosition, BlockPos blockCoord, int button, boolean doubleClick, float fontScale) {
        if (!WaypointsData.isManagerEnabled()) {
            return this.drawStepList;
        } else {
            if (!this.drawStepList.contains(this.clickDrawStep)) {
                this.drawStepList.add(this.clickDrawStep);
            }
            if (!doubleClick) {
                this.click(gridRenderer, blockCoord, button);
            } else {
                if (this.selected != null) {
                    UIManager.INSTANCE.openWaypointEditor(this.selected, false, this.fullscreen);
                    return this.drawStepList;
                }
                if (button == 0 && JourneymapClient.getInstance().getWaypointProperties().fullscreenDoubleClickToCreate.get()) {
                    RegionData regionData = RegionDataStorageHandler.getInstance().getRegionDataAsyncNoCache(blockCoord, gridRenderer.getMapType());
                    int y = blockCoord.m_123342_();
                    if (regionData != null) {
                        y = regionData.getTopY(blockCoord);
                    }
                    Waypoint waypoint = Waypoint.at(new BlockPos(blockCoord.m_123341_(), y + 1, blockCoord.m_123343_()), Waypoint.Type.Normal, mc.player.m_9236_().dimension().location().toString());
                    UIManager.INSTANCE.openWaypointEditor(waypoint, true, this.fullscreen);
                }
            }
            return this.drawStepList;
        }
    }

    @Override
    public boolean propagateClick() {
        return true;
    }

    private void sortByDistance(List<Waypoint> waypoints, final BlockPos blockCoord) {
        Collections.sort(waypoints, new Comparator<Waypoint>() {

            public int compare(Waypoint o1, Waypoint o2) {
                return java.lang.Double.compare(this.getDistance(o1), this.getDistance(o2));
            }

            private double getDistance(Waypoint waypoint) {
                double dx = (double) (waypoint.getX() - blockCoord.m_123341_());
                double dz = (double) (waypoint.getZ() - blockCoord.m_123343_());
                return Math.sqrt(dx * dx + dz * dz);
            }
        });
    }

    private void select(Waypoint waypoint) {
        this.selected = waypoint;
        this.selectedWaypointStep = new DrawWayPointStep(waypoint, waypoint.getIconColor(), 16777215, true);
        this.selectedWaypointStep.setIconScale(this.iconScale);
        this.drawStepList.add(this.selectedWaypointStep);
    }

    private void click(GridRenderer gridRenderer, BlockPos blockCoord, int button) {
        this.clickDrawStep.blockCoord = this.lastCoord = blockCoord;
        this.clickDrawStep.pixel = gridRenderer.getBlockPixelInGrid(blockCoord);
        if (!this.drawStepList.contains(this.clickDrawStep)) {
            this.drawStepList.add(this.clickDrawStep);
        }
        if (button == 1 && this.selected != null && JourneymapClient.getInstance().getStateHandler().isWaypointsAllowed()) {
            Waypoint wp = Waypoint.fromString(this.selected.toString());
            RegionData regionData = RegionDataStorageHandler.getInstance().getRegionDataAsyncNoCache(blockCoord, gridRenderer.getMapType());
            if (regionData != null) {
                int y = regionData.getTopY(blockCoord);
                wp.setY(y + 1);
            }
            this.fullscreen.popupMenu.displayWaypointOptions(blockCoord, wp);
        }
    }

    private void unclick() {
        this.clickDrawStep.blockCoord = null;
        this.drawStepList.remove(this.clickDrawStep);
    }

    class BlockOutlineDrawStep implements DrawStep {

        BlockPos blockCoord;

        Double pixel;

        BlockOutlineDrawStep(BlockPos blockCoord) {
            this.blockCoord = blockCoord;
        }

        @Override
        public void draw(GuiGraphics graphics, MultiBufferSource buffers, DrawStep.Pass pass, double xOffset, double yOffset, GridRenderer gridRenderer, double fontScale, double rotation) {
            if (pass == DrawStep.Pass.Object) {
                if (this.blockCoord != null) {
                    if (!Minecraft.getInstance().mouseHandler.isLeftPressed()) {
                        if (xOffset == 0.0 && yOffset == 0.0) {
                            double size = gridRenderer.getUIState().blockSize;
                            double thick = gridRenderer.getZoom() < 2 ? 1.0 : 2.0;
                            double x = this.pixel.x + xOffset;
                            double y = this.pixel.y + yOffset;
                            if (gridRenderer.isOnScreen(this.pixel)) {
                                DrawUtil.drawRectangle(graphics.pose(), x - thick * thick, y - thick * thick, size + thick * 4.0, thick, 0, 0.6F);
                                DrawUtil.drawRectangle(graphics.pose(), x - thick, y - thick, size + thick * thick, thick, 16777215, 0.6F);
                                DrawUtil.drawRectangle(graphics.pose(), x - thick * thick, y - thick, thick, size + thick * thick, 0, 0.6F);
                                DrawUtil.drawRectangle(graphics.pose(), x - thick, y, thick, size, 16777215, 0.6F);
                                DrawUtil.drawRectangle(graphics.pose(), x + size, y, thick, size, 16777215, 0.6F);
                                DrawUtil.drawRectangle(graphics.pose(), x + size + thick, y - thick, thick, size + thick * thick, 0, 0.6F);
                                DrawUtil.drawRectangle(graphics.pose(), x - thick, y + size, size + thick * thick, thick, 16777215, 0.6F);
                                DrawUtil.drawRectangle(graphics.pose(), x - thick * thick, y + size + thick, size + thick * 4.0, thick, 0, 0.6F);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public int getDisplayOrder() {
            return 0;
        }

        @Override
        public String getModId() {
            return "journeymap";
        }
    }
}