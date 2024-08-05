package journeymap.client.task.multi;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.ChunkRenderController;
import journeymap.client.data.DataCache;
import journeymap.client.log.StatTimer;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionCoord;
import journeymap.client.model.RegionImageCache;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.nbt.RegionData;
import journeymap.common.nbt.RegionDataStorageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.Logger;

public abstract class BaseMapTask implements ITask {

    static final Logger logger = Journeymap.getLogger();

    protected static ChunkPos[] keepAliveOffsets = new ChunkPos[] { new ChunkPos(0, -1), new ChunkPos(-1, 0), new ChunkPos(-1, -1) };

    final Level world;

    final Collection<ChunkPos> chunkCoords;

    final boolean flushCacheWhenDone;

    final ChunkRenderController renderController;

    final int elapsedLimit;

    final MapType mapType;

    final boolean asyncFileWrites;

    public BaseMapTask(ChunkRenderController renderController, Level world, MapType mapType, Collection<ChunkPos> chunkCoords, boolean flushCacheWhenDone, boolean asyncFileWrites, int elapsedLimit) {
        this.renderController = renderController;
        this.world = world;
        this.mapType = mapType;
        this.chunkCoords = chunkCoords;
        this.asyncFileWrites = asyncFileWrites;
        this.flushCacheWhenDone = flushCacheWhenDone;
        this.elapsedLimit = elapsedLimit;
    }

    public void initTask(Minecraft mc, JourneymapClient jm, File jmWorldDir, boolean threadLogging) throws InterruptedException {
    }

    @Override
    public void performTask(Minecraft mc, JourneymapClient jm, File jmWorldDir, boolean threadLogging) throws InterruptedException {
        if (!this.mapType.isAllowed()) {
            this.complete(0, true, false);
        } else {
            StatTimer timer = StatTimer.get(this.getClass().getSimpleName() + ".performTask", 5, this.elapsedLimit).start();
            this.initTask(mc, jm, jmWorldDir, threadLogging);
            int count = 0;
            try {
                if (mc.level == null) {
                    this.complete(count, true, false);
                } else {
                    Iterator<ChunkPos> chunkIter = this.chunkCoords.iterator();
                    ResourceKey<Level> currentDimension = Minecraft.getInstance().player.m_9236_().dimension();
                    if (!currentDimension.equals(this.mapType.dimension)) {
                        if (threadLogging) {
                            logger.debug("Dimension changed, map task obsolete.");
                        }
                        timer.cancel();
                        this.complete(count, true, false);
                    } else {
                        while (chunkIter.hasNext()) {
                            if (!jm.isMapping()) {
                                if (threadLogging) {
                                    logger.debug("JM isn't mapping, aborting");
                                }
                                timer.cancel();
                                this.complete(count, true, false);
                                return;
                            }
                            if (Thread.interrupted()) {
                                throw new InterruptedException();
                            }
                            ChunkPos coord = (ChunkPos) chunkIter.next();
                            ChunkMD chunkMd = DataCache.INSTANCE.getChunkMD(coord.toLong());
                            if (chunkMd != null && chunkMd.hasChunk()) {
                                try {
                                    RegionCoord rCoord = RegionCoord.fromChunkPos(jmWorldDir, this.mapType, chunkMd.getCoord().x, chunkMd.getCoord().z);
                                    RegionDataStorageHandler.Key key = new RegionDataStorageHandler.Key(rCoord, this.mapType);
                                    RegionData regionData = RegionDataStorageHandler.getInstance().getRegionData(key);
                                    boolean rendered = this.renderController.renderChunk(rCoord, this.mapType, chunkMd, regionData);
                                    if (rendered) {
                                        count++;
                                    }
                                } catch (Throwable var20) {
                                    logger.warn("Error rendering chunk " + chunkMd + ": " + var20.getMessage());
                                }
                            }
                        }
                        if (jm.isMapping()) {
                            if (Thread.interrupted()) {
                                timer.cancel();
                                throw new InterruptedException();
                            } else {
                                RegionImageCache.INSTANCE.updateTextures(this.flushCacheWhenDone, this.asyncFileWrites);
                                this.chunkCoords.clear();
                                this.complete(count, false, false);
                                timer.stop();
                            }
                        } else {
                            if (threadLogging) {
                                logger.debug("JM isn't mapping, aborting.");
                            }
                            timer.cancel();
                            this.complete(count, true, false);
                        }
                    }
                }
            } catch (InterruptedException var21) {
                Journeymap.getLogger().warn("Task thread interrupted: " + this);
                timer.cancel();
                throw var21;
            } catch (Throwable var22) {
                String error = "Unexpected error in BaseMapTask: " + LogFormatter.toString(var22);
                Journeymap.getLogger().error(error);
                this.complete(count, false, true);
                timer.cancel();
            } finally {
                if (threadLogging) {
                    timer.report();
                }
            }
        }
    }

    protected abstract void complete(int var1, boolean var2, boolean var3);

    public String toString() {
        return this.getClass().getSimpleName() + "{world=" + this.world + ", mapType=" + this.mapType + ", chunkCoords=" + this.chunkCoords + ", flushCacheWhenDone=" + this.flushCacheWhenDone + "}";
    }
}