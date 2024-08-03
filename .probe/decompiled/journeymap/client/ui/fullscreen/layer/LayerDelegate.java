package journeymap.client.ui.fullscreen.layer;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.ChunkRenderController;
import journeymap.client.cartography.render.BaseRenderer;
import journeymap.client.data.DataCache;
import journeymap.client.io.FileHandler;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.RegionCoord;
import journeymap.client.render.draw.DrawStep;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.nbt.RegionData;
import journeymap.common.nbt.RegionDataStorageHandler;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

public class LayerDelegate {

    long lastClick = 0L;

    BlockPos lastBlockPos = null;

    private final List<DrawStep> drawSteps = new ArrayList();

    private final List<Layer> layers = new ArrayList();

    long lastHover = 0L;

    private final long hoverDelay = 10L;

    public LayerDelegate(Fullscreen fullscreen) {
        this.layers.add(new WaypointLayer(fullscreen));
        this.layers.add(new ModOverlayLayer(fullscreen));
        this.layers.add(new BlockInfoLayer(fullscreen));
        this.layers.add(new KeybindingInfoLayer(fullscreen));
    }

    public void onMouseMove(Minecraft mc, GridRenderer gridRenderer, Double mousePosition, float fontScale, boolean isScrolling) {
        long now = Util.getMillis();
        if (this.lastBlockPos == null || !isScrolling && now - this.lastHover > 10L) {
            this.lastBlockPos = this.getBlockPos(mc, gridRenderer, mousePosition);
            this.lastHover = now;
        }
        this.drawSteps.clear();
        for (Layer layer : this.layers) {
            try {
                this.drawSteps.addAll(layer.onMouseMove(mc, gridRenderer, mousePosition, this.lastBlockPos, fontScale, isScrolling));
            } catch (Exception var11) {
                Journeymap.getLogger().error(LogFormatter.toString(var11));
            }
        }
    }

    public void onMouseClicked(Minecraft mc, GridRenderer gridRenderer, Double mousePosition, int button, float fontScale) {
        this.lastBlockPos = gridRenderer.fullscreen.getBlockAtMouse();
        long sysTime = Util.getMillis();
        boolean doubleClick = sysTime - this.lastClick < 200L;
        this.lastClick = sysTime;
        this.drawSteps.clear();
        for (Layer layer : this.layers) {
            try {
                this.drawSteps.addAll(layer.onMouseClick(mc, gridRenderer, mousePosition, this.lastBlockPos, button, doubleClick, fontScale));
                if (!layer.propagateClick()) {
                    break;
                }
            } catch (Exception var12) {
                Journeymap.getLogger().error(LogFormatter.toString(var12));
            }
        }
    }

    public BlockPos getBlockPos(Minecraft mc, GridRenderer gridRenderer, Double mousePosition) {
        BlockPos seaLevel = gridRenderer.getBlockAtPixel(mousePosition);
        ChunkMD chunkMD = DataCache.INSTANCE.getChunkMD(seaLevel);
        int y = seaLevel.m_123342_();
        RegionData regionData = RegionDataStorageHandler.getInstance().getRegionDataAsyncNoCache(seaLevel, gridRenderer.getMapType());
        if (regionData != null && JourneymapClient.getInstance().getCoreProperties().dataCachingEnabled.get()) {
            y = regionData.getTopY(seaLevel);
            return new BlockPos(seaLevel.m_123341_(), y, seaLevel.m_123343_());
        } else {
            if (chunkMD != null) {
                ChunkRenderController crc = JourneymapClient.getInstance().getChunkRenderController();
                if (crc != null) {
                    ChunkPos chunkCoord = chunkMD.getCoord();
                    RegionCoord rCoord = RegionCoord.fromChunkPos(FileHandler.getJMWorldDir(mc), gridRenderer.getMapType(), chunkCoord.x, chunkCoord.z);
                    BaseRenderer chunkRenderer = crc.getRenderer(rCoord, gridRenderer.getMapType(), chunkMD);
                    int blockY = chunkRenderer.getBlockHeight(chunkMD, seaLevel);
                    return new BlockPos(seaLevel.m_123341_(), blockY, seaLevel.m_123343_());
                }
            }
            return new BlockPos(seaLevel.m_123341_(), y + 1, seaLevel.m_123343_());
        }
    }

    public List<DrawStep> getDrawSteps() {
        return this.drawSteps;
    }
}