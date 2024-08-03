package journeymap.client.task.multi;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.ChunkRenderController;
import journeymap.client.data.DataCache;
import journeymap.client.feature.Feature;
import journeymap.client.feature.FeatureManager;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.EntityDTO;
import journeymap.client.model.MapType;
import journeymap.client.properties.CoreProperties;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityInLevelCallback;

public class MapPlayerTask extends BaseMapTask {

    private static int MAX_STALE_MILLISECONDS = 30000;

    private static int MAX_BATCH_SIZE = 32;

    private static final DecimalFormat decFormat = new DecimalFormat("##.#");

    private static volatile long lastTaskCompleted;

    private static long lastTaskTime;

    private static double lastTaskAvgChunkTime;

    private static final Cache<String, String> tempDebugLines = CacheBuilder.newBuilder().maximumSize(20L).expireAfterWrite(1500L, TimeUnit.MILLISECONDS).build();

    private final int maxRuntime = JourneymapClient.getInstance().getCoreProperties().renderDelay.get() * 3000;

    private int scheduledChunks = 0;

    private long startNs;

    private long elapsedNs;

    private MapPlayerTask(ChunkRenderController chunkRenderController, Level world, MapType mapType, Collection<ChunkPos> chunkCoords) {
        super(chunkRenderController, world, mapType, chunkCoords, false, true, 10000);
    }

    public static void forceNearbyRemap() {
        synchronized (MapPlayerTask.class) {
            DataCache.INSTANCE.invalidateChunkMDCache();
        }
    }

    public static MapPlayerTask.MapPlayerTaskBatch create(ChunkRenderController chunkRenderController, EntityDTO player) {
        boolean surfaceAllowed = FeatureManager.getInstance().isAllowed(Feature.MapSurface);
        boolean cavesAllowed = FeatureManager.getInstance().isAllowed(Feature.MapCaves);
        boolean topoAllowed = FeatureManager.getInstance().isAllowed(Feature.MapTopo);
        boolean biomeAllowed = FeatureManager.getInstance().isAllowed(Feature.MapBiome);
        if (!surfaceAllowed && !cavesAllowed && !topoAllowed && !biomeAllowed) {
            return null;
        } else {
            LivingEntity playerEntity = (LivingEntity) player.entityLivingRef.get();
            if (playerEntity == null) {
                return null;
            } else {
                boolean underground = player.underground;
                MapType mapType;
                if (underground) {
                    mapType = MapType.underground(player);
                } else {
                    long time = playerEntity.m_9236_().getLevelData().getDayTime() % 24000L;
                    mapType = time < 13800L ? MapType.day(player) : MapType.night(player);
                }
                List<ITask> tasks = new ArrayList(2);
                tasks.add(new MapPlayerTask(chunkRenderController, playerEntity.m_9236_(), mapType, new ArrayList()));
                if (underground) {
                    if (surfaceAllowed && JourneymapClient.getInstance().getCoreProperties().alwaysMapSurface.get()) {
                        tasks.add(new MapPlayerTask(chunkRenderController, playerEntity.m_9236_(), MapType.day(player), new ArrayList()));
                    }
                } else if (cavesAllowed && JourneymapClient.getInstance().getCoreProperties().alwaysMapCaves.get()) {
                    tasks.add(new MapPlayerTask(chunkRenderController, playerEntity.m_9236_(), MapType.underground(player), new ArrayList()));
                }
                if (topoAllowed && JourneymapClient.getInstance().getCoreProperties().mapTopography.get()) {
                    tasks.add(new MapPlayerTask(chunkRenderController, playerEntity.m_9236_(), MapType.topo(player), new ArrayList()));
                }
                if (biomeAllowed && JourneymapClient.getInstance().getCoreProperties().mapBiome.get()) {
                    tasks.add(new MapPlayerTask(chunkRenderController, playerEntity.m_9236_(), MapType.biome(player), new ArrayList()));
                }
                return new MapPlayerTask.MapPlayerTaskBatch(tasks);
            }
        }
    }

    public static String[] getDebugStats() {
        try {
            CoreProperties coreProperties = JourneymapClient.getInstance().getCoreProperties();
            boolean underground = DataCache.getPlayer().underground;
            ArrayList<String> lines = new ArrayList(tempDebugLines.asMap().values());
            if (underground || coreProperties.alwaysMapCaves.get()) {
                lines.add(RenderSpec.getUndergroundSpec().getDebugStats());
            }
            if (!underground || coreProperties.alwaysMapSurface.get()) {
                lines.add(RenderSpec.getSurfaceSpec().getDebugStats());
            }
            if (!underground && coreProperties.mapTopography.get()) {
                lines.add(RenderSpec.getTopoSpec().getDebugStats());
            }
            return (String[]) lines.toArray(new String[lines.size()]);
        } catch (Throwable var3) {
            logger.error(var3);
            return new String[0];
        }
    }

    public static void addTempDebugMessage(String key, String message) {
        if (Minecraft.getInstance().options.renderFpsChart) {
            tempDebugLines.put(key, message);
        }
    }

    public static void removeTempDebugMessage(String key) {
        tempDebugLines.invalidate(key);
    }

    public static String getSimpleStats() {
        int primaryRenderSize = 0;
        int secondaryRenderSize = 0;
        int totalChunks = 0;
        if (DataCache.getPlayer().underground || JourneymapClient.getInstance().getCoreProperties().alwaysMapCaves.get()) {
            RenderSpec spec = RenderSpec.getUndergroundSpec();
            if (spec != null) {
                primaryRenderSize += spec.getPrimaryRenderSize();
                secondaryRenderSize += spec.getLastSecondaryRenderSize();
                totalChunks += spec.getLastTaskChunks();
            }
        }
        if (!DataCache.getPlayer().underground || JourneymapClient.getInstance().getCoreProperties().alwaysMapSurface.get()) {
            RenderSpec spec = RenderSpec.getSurfaceSpec();
            if (spec != null) {
                primaryRenderSize += spec.getPrimaryRenderSize();
                secondaryRenderSize += spec.getLastSecondaryRenderSize();
                totalChunks += spec.getLastTaskChunks();
            }
        }
        return Constants.getString("jm.common.renderstats", totalChunks, primaryRenderSize, secondaryRenderSize, lastTaskTime, decFormat.format(lastTaskAvgChunkTime));
    }

    public static long getlastTaskCompleted() {
        return lastTaskCompleted;
    }

    @Override
    public void initTask(Minecraft minecraft, JourneymapClient jm, File jmWorldDir, boolean threadLogging) {
        this.startNs = System.nanoTime();
        RenderSpec renderSpec;
        if (this.mapType.isUnderground()) {
            renderSpec = RenderSpec.getUndergroundSpec();
        } else if (this.mapType.isTopo()) {
            renderSpec = RenderSpec.getTopoSpec();
        } else {
            renderSpec = RenderSpec.getSurfaceSpec();
        }
        long now = System.currentTimeMillis();
        List<ChunkPos> renderArea = renderSpec.getRenderAreaCoords();
        int maxBatchSize = renderArea.size() / 4;
        renderArea.removeIf(chunkPos -> {
            ChunkMD chunkMD = DataCache.INSTANCE.getChunkMD(chunkPos.toLong());
            if (chunkMD == null || !chunkMD.hasChunk() || now - chunkMD.getLastRendered(this.mapType) < 30000L) {
                return true;
            } else if (!chunkMD.getDimension().equals(this.mapType.dimension)) {
                return true;
            } else {
                chunkMD.resetBlockData(this.mapType);
                return false;
            }
        });
        if (renderArea.size() <= maxBatchSize) {
            this.chunkCoords.addAll(renderArea);
        } else {
            List<ChunkPos> list = Arrays.asList((ChunkPos[]) renderArea.toArray(new ChunkPos[renderArea.size()]));
            this.chunkCoords.addAll(list.subList(0, maxBatchSize));
        }
        this.scheduledChunks = this.chunkCoords.size();
    }

    @Override
    protected void complete(int mappedChunks, boolean cancelled, boolean hadError) {
        this.elapsedNs = System.nanoTime() - this.startNs;
    }

    @Override
    public int getMaxRuntime() {
        return this.maxRuntime;
    }

    public static class Manager implements ITaskManager {

        final int mapTaskDelay = JourneymapClient.getInstance().getCoreProperties().renderDelay.get() * 1000;

        boolean enabled;

        @Override
        public Class<? extends BaseMapTask> getTaskClass() {
            return MapPlayerTask.class;
        }

        @Override
        public boolean enableTask(Minecraft minecraft, Object params) {
            this.enabled = true;
            return this.enabled;
        }

        @Override
        public boolean isEnabled(Minecraft minecraft) {
            return this.enabled;
        }

        @Override
        public void disableTask(Minecraft minecraft) {
            this.enabled = false;
        }

        @Override
        public ITask getTask(Minecraft minecraft) {
            if (this.enabled && minecraft.player.f_146801_ != EntityInLevelCallback.NULL && System.currentTimeMillis() - MapPlayerTask.lastTaskCompleted >= (long) this.mapTaskDelay) {
                ChunkRenderController chunkRenderController = JourneymapClient.getInstance().getChunkRenderController();
                return MapPlayerTask.create(chunkRenderController, DataCache.getPlayer());
            } else {
                return null;
            }
        }

        @Override
        public void taskAccepted(ITask task, boolean accepted) {
        }
    }

    public static class MapPlayerTaskBatch extends TaskBatch {

        public MapPlayerTaskBatch(List<ITask> tasks) {
            super(tasks);
        }

        @Override
        public void performTask(Minecraft mc, JourneymapClient jm, File jmWorldDir, boolean threadLogging) throws InterruptedException {
            if (mc.player != null) {
                this.startNs = System.nanoTime();
                List<ITask> tasks = new ArrayList(this.taskList);
                super.performTask(mc, jm, jmWorldDir, threadLogging);
                this.elapsedNs = System.nanoTime() - this.startNs;
                MapPlayerTask.lastTaskTime = TimeUnit.NANOSECONDS.toMillis(this.elapsedNs);
                MapPlayerTask.lastTaskCompleted = System.currentTimeMillis();
                int chunkCount = 0;
                for (ITask task : tasks) {
                    if (task instanceof MapPlayerTask) {
                        MapPlayerTask mapPlayerTask = (MapPlayerTask) task;
                        chunkCount += mapPlayerTask.scheduledChunks;
                        if (mapPlayerTask.mapType.isUnderground()) {
                            RenderSpec.getUndergroundSpec().setLastTaskInfo(mapPlayerTask.scheduledChunks, mapPlayerTask.elapsedNs);
                        } else if (mapPlayerTask.mapType.isTopo()) {
                            RenderSpec.getTopoSpec().setLastTaskInfo(mapPlayerTask.scheduledChunks, mapPlayerTask.elapsedNs);
                        } else {
                            RenderSpec.getSurfaceSpec().setLastTaskInfo(mapPlayerTask.scheduledChunks, mapPlayerTask.elapsedNs);
                        }
                    } else {
                        Journeymap.getLogger().warn("Unexpected task in batch: " + task);
                    }
                }
                MapPlayerTask.lastTaskAvgChunkTime = (double) (this.elapsedNs / (long) Math.max(1, chunkCount)) / 1000000.0;
            }
        }
    }
}