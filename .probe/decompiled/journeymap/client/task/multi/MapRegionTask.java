package journeymap.client.task.multi;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.api.display.Context;
import journeymap.client.api.display.DisplayType;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.ShapeProperties;
import journeymap.client.api.model.TextProperties;
import journeymap.client.cartography.ChunkRenderController;
import journeymap.client.data.DataCache;
import journeymap.client.feature.Feature;
import journeymap.client.feature.FeatureManager;
import journeymap.client.io.FileHandler;
import journeymap.client.io.nbt.JMChunkLoader;
import journeymap.client.io.nbt.RegionLoader;
import journeymap.client.log.ChatLog;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.EntityDTO;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionCoord;
import journeymap.client.model.RegionImageCache;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.nbt.RegionDataStorageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import org.apache.logging.log4j.Logger;

public class MapRegionTask extends BaseMapTask {

    private static final int MAX_RUNTIME = 30000;

    private static final Logger logger = Journeymap.getLogger();

    private static volatile long lastTaskCompleted;

    public static MapType MAP_TYPE;

    public static boolean active = false;

    final PolygonOverlay regionOverlay;

    final RegionCoord rCoord;

    final Collection<ChunkPos> retainedCoords;

    private MapRegionTask(ChunkRenderController renderController, Level world, MapType mapType, RegionCoord rCoord, Collection<ChunkPos> chunkCoords, Collection<ChunkPos> retainCoords) {
        super(renderController, world, mapType, chunkCoords, true, false, 5000);
        this.rCoord = rCoord;
        this.retainedCoords = retainCoords;
        this.regionOverlay = this.createOverlay();
    }

    public static BaseMapTask create(ChunkRenderController renderController, RegionCoord rCoord, MapType mapType, Minecraft minecraft) {
        Level world = minecraft.level;
        List<ChunkPos> renderCoords = rCoord.getChunkCoordsInRegion();
        List<ChunkPos> retainedCoords = new ArrayList(renderCoords.size());
        HashMap<RegionCoord, Boolean> existingRegions = new HashMap();
        for (ChunkPos coord : renderCoords) {
            for (ChunkPos keepAliveOffset : keepAliveOffsets) {
                ChunkPos keepAliveCoord = new ChunkPos(coord.x + keepAliveOffset.x, coord.z + keepAliveOffset.z);
                RegionCoord neighborRCoord = RegionCoord.fromChunkPos(rCoord.worldDir, mapType, keepAliveCoord.x, keepAliveCoord.z);
                if (!existingRegions.containsKey(neighborRCoord)) {
                    existingRegions.put(neighborRCoord, neighborRCoord.exists());
                }
                if (!renderCoords.contains(keepAliveCoord) && (Boolean) existingRegions.get(neighborRCoord)) {
                    retainedCoords.add(keepAliveCoord);
                }
            }
        }
        return new MapRegionTask(renderController, world, mapType, rCoord, renderCoords, retainedCoords);
    }

    @Override
    public final void performTask(Minecraft mc, JourneymapClient jm, File jmWorldDir, boolean threadLogging) throws InterruptedException {
        if (active) {
            ClientAPI.INSTANCE.show(this.regionOverlay);
            try (ChunkStorage loader = new ChunkStorage(new File(FileHandler.getWorldSaveDir(mc), "region").toPath(), DataFixers.getDataFixer(), true)) {
                int missing = 0;
                Set<ChunkMD> chuckSet = new HashSet();
                for (ChunkPos coord : this.retainedCoords) {
                    ChunkMD chunkMD = JMChunkLoader.getChunkMD(loader, mc, coord, true);
                    if (chunkMD != null && chunkMD.hasChunk()) {
                        chuckSet.add(chunkMD);
                    }
                }
                for (ChunkPos coordx : this.chunkCoords) {
                    ChunkMD chunkMD = JMChunkLoader.getChunkMD(loader, mc, coordx, true);
                    if (chunkMD != null && chunkMD.hasChunk()) {
                        chuckSet.add(chunkMD);
                    } else {
                        missing++;
                    }
                }
                if (this.chunkCoords.size() - missing > 0) {
                    try {
                        chuckSet.forEach(DataCache.INSTANCE::addChunkMD);
                        logger.info(String.format("Potential chunks to map in %s: %s of %s", this.rCoord, this.chunkCoords.size() - missing, this.chunkCoords.size()));
                        super.performTask(mc, jm, jmWorldDir, threadLogging);
                        chuckSet.forEach(DataCache.INSTANCE::removeChunkMD);
                        chuckSet.clear();
                    } finally {
                        this.regionOverlay.getShapeProperties().setFillColor(16777215).setFillOpacity(0.15F).setStrokeColor(16777215);
                        String label = String.format("%s\nRegion [%s,%s]", Constants.getString("jm.common.automap_region_complete"), this.rCoord.regionX, this.rCoord.regionZ);
                        this.regionOverlay.setLabel(label);
                        this.regionOverlay.flagForRerender();
                    }
                } else {
                    this.regionOverlay.getShapeProperties().setFillColor(16711680).setFillOpacity(0.15F).setStrokeColor(16711680);
                    String label = String.format("%s\nRegion [%s,%s]", Constants.getString("jm.common.automap_region_complete"), this.rCoord.regionX, this.rCoord.regionZ);
                    this.regionOverlay.setLabel(label);
                    this.regionOverlay.flagForRerender();
                    chuckSet.clear();
                    logger.info(String.format("Skipping empty region: %s", this.rCoord));
                }
            } catch (IOException var20) {
                logger.error("Failed closing loader", var20);
            }
        }
    }

    protected PolygonOverlay createOverlay() {
        String displayId = "AutoMap" + this.rCoord;
        String groupName = "AutoMap";
        String label = String.format("%s\nRegion [%s,%s]", Constants.getString("jm.common.automap_region_start"), this.rCoord.regionX, this.rCoord.regionZ);
        ShapeProperties shapeProps = new ShapeProperties().setStrokeWidth(2.0F).setStrokeColor(255).setStrokeOpacity(0.7F).setFillColor(65280).setFillOpacity(0.2F);
        TextProperties textProps = new TextProperties().setBackgroundColor(34).setBackgroundOpacity(0.5F).setColor(65280).setOpacity(1.0F).setFontShadow(true);
        int x = this.rCoord.getMinChunkX() << 4;
        int y = 70;
        int z = this.rCoord.getMinChunkZ() << 4;
        int maxX = (this.rCoord.getMaxChunkX() << 4) + 15;
        int maxZ = (this.rCoord.getMaxChunkZ() << 4) + 15;
        BlockPos sw = new BlockPos(x, y, maxZ);
        BlockPos se = new BlockPos(maxX, y, maxZ);
        BlockPos ne = new BlockPos(maxX, y, z);
        BlockPos nw = new BlockPos(x, y, z);
        MapPolygon polygon = new MapPolygon(sw, se, ne, nw);
        PolygonOverlay regionOverlay = new PolygonOverlay("journeymap", displayId, this.rCoord.dimension, shapeProps, polygon);
        regionOverlay.setOverlayGroupName(groupName).setLabel(label).setTextProperties(textProps).setActiveUIs(EnumSet.of(Context.UI.Fullscreen, Context.UI.Webmap)).setActiveMapTypes(EnumSet.of(Context.MapType.Any));
        return regionOverlay;
    }

    @Override
    protected void complete(int mappedChunks, boolean cancelled, boolean hadError) {
        lastTaskCompleted = System.currentTimeMillis();
        RegionImageCache.INSTANCE.flushToDiskAsync(true);
        DataCache.INSTANCE.stopChunkMDRetention();
        if (!hadError && !cancelled) {
            logger.info(String.format("Actual chunks mapped in %s: %s ", this.rCoord, mappedChunks));
            this.regionOverlay.setTitle(Constants.getString("jm.common.automap_region_chunks", mappedChunks));
        } else {
            logger.warn("MapRegionTask cancelled {} hadError {}}", cancelled, hadError);
        }
        long usedPct = getMemoryUsage();
        if (usedPct >= 85L) {
            logger.warn(String.format("Memory usage at %2d%%, forcing garbage collection", usedPct));
            System.gc();
            usedPct = getMemoryUsage();
        }
        logger.info(String.format("Memory usage at %2d%%", usedPct));
    }

    public static long getMemoryUsage() {
        long max = Runtime.getRuntime().maxMemory();
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        return (total - free) * 100L / max;
    }

    @Override
    public int getMaxRuntime() {
        return 30000;
    }

    public static class Manager implements ITaskManager {

        final int mapTaskDelay = 0;

        RegionLoader regionLoader;

        boolean enabled;

        @Override
        public Class<? extends ITask> getTaskClass() {
            return MapRegionTask.class;
        }

        @Override
        public boolean enableTask(Minecraft minecraft, Object params) {
            EntityDTO player = DataCache.getPlayer();
            boolean cavesAllowed = FeatureManager.getInstance().isAllowed(Feature.MapCaves);
            boolean underground = player.underground;
            if (underground && !cavesAllowed) {
                MapRegionTask.logger.info("Cave mapping not permitted.");
                return false;
            } else {
                this.enabled = params != null;
                if (!this.enabled) {
                    return false;
                } else if (System.currentTimeMillis() - MapRegionTask.lastTaskCompleted < (long) JourneymapClient.getInstance().getCoreProperties().autoMapPoll.get().intValue()) {
                    return false;
                } else {
                    this.enabled = false;
                    if (minecraft.isLocalServer()) {
                        try {
                            MapType mapType = MapRegionTask.MAP_TYPE;
                            if (mapType == null) {
                                mapType = Fullscreen.state().getMapType();
                            }
                            Boolean mapAll = params == null ? false : (Boolean) params;
                            MapRegionTask.active = true;
                            this.regionLoader = new RegionLoader(minecraft, mapType, mapAll);
                            if (this.regionLoader.getRegionsFound() == 0) {
                                this.disableTask(minecraft);
                            } else {
                                this.enabled = true;
                            }
                        } catch (Throwable var8) {
                            String error = "Couldn't Auto-Map: " + var8.getMessage();
                            ChatLog.announceError(error);
                            MapRegionTask.logger.error(error + ": " + LogFormatter.toString(var8));
                        }
                    }
                    return this.enabled;
                }
            }
        }

        @Override
        public boolean isEnabled(Minecraft minecraft) {
            return this.enabled;
        }

        @Override
        public void disableTask(Minecraft minecraft) {
            if (this.regionLoader != null) {
                if (this.regionLoader.isUnderground()) {
                    ChatLog.announceI18N("jm.common.automap_complete_underground", this.regionLoader.getVSlice());
                } else {
                    ChatLog.announceI18N("jm.common.automap_complete");
                }
            }
            this.enabled = false;
            MapRegionTask.active = false;
            if (this.regionLoader != null) {
                RegionImageCache.INSTANCE.flushToDisk(true);
                RegionImageCache.INSTANCE.clear();
                DataCache.INSTANCE.invalidateChunkMDCache();
                this.regionLoader.getRegions().clear();
                this.regionLoader = null;
            }
            RegionDataStorageHandler.getInstance().flushDataCache();
            ClientAPI.INSTANCE.removeAll("journeymap", DisplayType.Polygon);
        }

        public BaseMapTask getTask(Minecraft minecraft) {
            if (!this.enabled) {
                return null;
            } else if (this.regionLoader != null && this.regionLoader.getRegions() != null && !this.regionLoader.getRegions().isEmpty()) {
                RegionCoord rCoord = (RegionCoord) this.regionLoader.getRegions().peek();
                ChunkRenderController chunkRenderController = JourneymapClient.getInstance().getChunkRenderController();
                return MapRegionTask.create(chunkRenderController, rCoord, this.regionLoader.getMapType(), minecraft);
            } else {
                this.disableTask(minecraft);
                return null;
            }
        }

        @Override
        public void taskAccepted(ITask task, boolean accepted) {
            if (accepted) {
                this.regionLoader.getRegions().pop();
                float total = 1.0F * (float) this.regionLoader.getRegionsFound();
                float remaining = total - (float) this.regionLoader.getRegions().size();
                String percent = new DecimalFormat("##.#").format((double) (remaining * 100.0F / total)) + "%";
                if (this.regionLoader.isUnderground()) {
                    ChatLog.announceI18N("jm.common.automap_status_underground", this.regionLoader.getVSlice(), percent);
                } else {
                    ChatLog.announceI18N("jm.common.automap_status", percent);
                }
            }
        }
    }
}