package journeymap.client.task.multi;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.ImmutableListMultimap.Builder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.api.option.KeyedEnum;
import journeymap.client.data.DataCache;
import journeymap.client.model.MapType;
import journeymap.client.properties.CoreProperties;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;

public class RenderSpec {

    private static DecimalFormat decFormat = new DecimalFormat("##.#");

    private static volatile RenderSpec lastSurfaceRenderSpec;

    private static volatile RenderSpec lastTopoRenderSpec;

    private static volatile RenderSpec lastUndergroundRenderSpec;

    private static Minecraft minecraft = Minecraft.getInstance();

    private final Player player;

    private final MapType mapType;

    private final int primaryRenderDistance;

    private final int maxSecondaryRenderDistance;

    private final RenderSpec.RevealShape revealShape;

    private ListMultimap<Integer, RenderSpec.Offset> offsets = null;

    private ArrayList<ChunkPos> primaryRenderCoords;

    private Comparator<ChunkPos> comparator;

    private int lastSecondaryRenderDistance;

    private ChunkPos lastPlayerCoord;

    private long lastTaskTime;

    private int lastTaskChunks;

    private double lastTaskAvgChunkTime;

    private RenderSpec(Minecraft minecraft, MapType mapType) {
        this.player = minecraft.player;
        CoreProperties props = JourneymapClient.getInstance().getCoreProperties();
        int gameRenderDistance = Math.max(1, minecraft.options.getEffectiveRenderDistance() - 1);
        int mapRenderDistanceMax = mapType.isUnderground() ? props.renderDistanceCaveMax.get() : props.renderDistanceSurfaceMax.get();
        if (JourneymapClient.getInstance().getStateHandler().isJourneyMapServerConnection()) {
            int serverDistance = JourneymapClient.getInstance().getStateHandler().getMaxRenderDistance();
            mapRenderDistanceMax = serverDistance == 0 ? mapRenderDistanceMax : Math.min(serverDistance, mapRenderDistanceMax);
        }
        if (mapRenderDistanceMax == 0) {
            mapRenderDistanceMax = gameRenderDistance;
        }
        Journeymap.getLogger().debug("Map Render distance set to: {}", mapRenderDistanceMax);
        this.mapType = mapType;
        int renderDistance = Math.min(gameRenderDistance, mapRenderDistanceMax);
        this.primaryRenderDistance = renderDistance++;
        this.maxSecondaryRenderDistance = renderDistance;
        this.revealShape = JourneymapClient.getInstance().getCoreProperties().revealShape.get();
        this.lastPlayerCoord = new ChunkPos(minecraft.player.m_146902_().x, minecraft.player.m_146902_().z);
        this.lastSecondaryRenderDistance = this.primaryRenderDistance;
    }

    private static Double blockDistance(ChunkPos playerCoord, ChunkPos coord) {
        int x = (playerCoord.x << 4) + 8 - ((coord.x << 4) + 8);
        int z = (playerCoord.z << 4) + 8 - ((coord.z << 4) + 8);
        return Math.sqrt((double) (x * x + z * z));
    }

    private static Double chunkDistance(ChunkPos playerCoord, ChunkPos coord) {
        int x = playerCoord.x - coord.x;
        int z = playerCoord.z - coord.z;
        return Math.sqrt((double) (x * x + z * z));
    }

    static boolean inRange(ChunkPos playerCoord, ChunkPos coord, int renderDistance, RenderSpec.RevealShape revealShape) {
        if (revealShape == RenderSpec.RevealShape.Circle) {
            double distance = blockDistance(playerCoord, coord);
            double diff = distance - (double) (renderDistance * 16);
            return diff <= 8.0;
        } else {
            float x = (float) Math.abs(playerCoord.x - coord.x);
            float z = (float) Math.abs(playerCoord.z - coord.z);
            return x <= (float) renderDistance && z <= (float) renderDistance;
        }
    }

    private static ListMultimap<Integer, RenderSpec.Offset> calculateOffsets(int minOffset, int maxOffset, RenderSpec.RevealShape revealShape) {
        ListMultimap<Integer, RenderSpec.Offset> multimap = ArrayListMultimap.create();
        int offset = maxOffset;
        int baseX = 0;
        int baseZ = 0;
        for (ChunkPos baseCoord = new ChunkPos(0, 0); offset >= minOffset; offset--) {
            for (int x = 0 - offset; x <= 0 + offset; x++) {
                for (int z = 0 - offset; z <= 0 + offset; z++) {
                    ChunkPos coord = new ChunkPos(x, z);
                    if (revealShape == RenderSpec.RevealShape.Square || inRange(baseCoord, coord, offset, revealShape)) {
                        multimap.put(offset, new RenderSpec.Offset(coord.x, coord.z));
                    }
                }
            }
            if (offset < maxOffset) {
                List<RenderSpec.Offset> oneUp = multimap.get(offset + 1);
                oneUp.removeAll(multimap.get(offset));
            }
        }
        for (int i = minOffset; i <= maxOffset; i++) {
            multimap.get(i).sort((o1, o2) -> Double.compare(o1.distance(), o2.distance()));
        }
        return new Builder().putAll(multimap).build();
    }

    public static RenderSpec getSurfaceSpec() {
        if (lastSurfaceRenderSpec == null || lastSurfaceRenderSpec.lastPlayerCoord.x != minecraft.player.m_146902_().x || lastSurfaceRenderSpec.lastPlayerCoord.z != minecraft.player.m_146902_().z) {
            RenderSpec newSpec = new RenderSpec(minecraft, MapType.day(DataCache.getPlayer()));
            newSpec.copyLastStatsFrom(lastSurfaceRenderSpec);
            lastSurfaceRenderSpec = newSpec;
        }
        return lastSurfaceRenderSpec;
    }

    public static RenderSpec getTopoSpec() {
        if (lastTopoRenderSpec == null || lastTopoRenderSpec.lastPlayerCoord.x != minecraft.player.m_146902_().x || lastTopoRenderSpec.lastPlayerCoord.z != minecraft.player.m_146902_().z) {
            RenderSpec newSpec = new RenderSpec(minecraft, MapType.topo(DataCache.getPlayer()));
            newSpec.copyLastStatsFrom(lastTopoRenderSpec);
            lastTopoRenderSpec = newSpec;
        }
        return lastTopoRenderSpec;
    }

    public static RenderSpec getUndergroundSpec() {
        if (lastUndergroundRenderSpec == null || lastUndergroundRenderSpec.lastPlayerCoord.x != minecraft.player.m_146902_().x || lastUndergroundRenderSpec.lastPlayerCoord.z != minecraft.player.m_146902_().z) {
            RenderSpec newSpec = new RenderSpec(minecraft, MapType.underground(DataCache.getPlayer()));
            newSpec.copyLastStatsFrom(lastUndergroundRenderSpec);
            lastUndergroundRenderSpec = newSpec;
        }
        return lastUndergroundRenderSpec;
    }

    public static void resetRenderSpecs() {
        lastUndergroundRenderSpec = null;
        lastSurfaceRenderSpec = null;
        lastTopoRenderSpec = null;
    }

    protected List<ChunkPos> getRenderAreaCoords() {
        if (this.offsets == null) {
            this.offsets = calculateOffsets(this.primaryRenderDistance, this.maxSecondaryRenderDistance, this.revealShape);
        }
        DataCache dataCache = DataCache.INSTANCE;
        if (this.lastPlayerCoord == null || this.lastPlayerCoord.x != this.player.m_146902_().x || this.lastPlayerCoord.z != this.player.m_146902_().z) {
            this.primaryRenderCoords = null;
            this.lastSecondaryRenderDistance = this.primaryRenderDistance;
        }
        this.lastPlayerCoord = new ChunkPos(minecraft.player.m_146902_().x, minecraft.player.m_146902_().z);
        if (this.primaryRenderCoords == null || this.primaryRenderCoords.isEmpty()) {
            List<RenderSpec.Offset> primaryOffsets = this.offsets.get(this.primaryRenderDistance);
            this.primaryRenderCoords = new ArrayList(primaryOffsets.size());
            for (RenderSpec.Offset offset : primaryOffsets) {
                ChunkPos primaryCoord = offset.from(this.lastPlayerCoord);
                this.primaryRenderCoords.add(primaryCoord);
                dataCache.getChunkMD(primaryCoord.toLong());
            }
        }
        if (this.maxSecondaryRenderDistance == this.primaryRenderDistance) {
            return new ArrayList(this.primaryRenderCoords);
        } else {
            if (this.lastSecondaryRenderDistance == this.maxSecondaryRenderDistance) {
                this.lastSecondaryRenderDistance = this.primaryRenderDistance;
            }
            this.lastSecondaryRenderDistance++;
            List<RenderSpec.Offset> secondaryOffsets = this.offsets.get(this.lastSecondaryRenderDistance);
            ArrayList<ChunkPos> renderCoords = new ArrayList(this.primaryRenderCoords.size() + secondaryOffsets.size());
            for (RenderSpec.Offset offset : secondaryOffsets) {
                ChunkPos secondaryCoord = offset.from(this.lastPlayerCoord);
                renderCoords.add(secondaryCoord);
                dataCache.getChunkMD(secondaryCoord.toLong());
            }
            renderCoords.addAll(0, this.primaryRenderCoords);
            return renderCoords;
        }
    }

    public Boolean isUnderground() {
        return this.mapType.isUnderground();
    }

    public Boolean isTopo() {
        return this.mapType.isTopo();
    }

    public Boolean getSurface() {
        return this.mapType.isSurface();
    }

    public int getPrimaryRenderDistance() {
        return this.primaryRenderDistance;
    }

    public int getMaxSecondaryRenderDistance() {
        return this.maxSecondaryRenderDistance;
    }

    public int getLastSecondaryRenderDistance() {
        return this.lastSecondaryRenderDistance;
    }

    public RenderSpec.RevealShape getRevealShape() {
        return this.revealShape;
    }

    public int getLastSecondaryRenderSize() {
        if (this.primaryRenderDistance == this.maxSecondaryRenderDistance) {
            return 0;
        } else {
            return this.offsets == null ? 0 : this.offsets.get(this.lastSecondaryRenderDistance).size();
        }
    }

    public int getPrimaryRenderSize() {
        return this.offsets == null ? 0 : this.offsets.get(this.primaryRenderDistance).size();
    }

    public void setLastTaskInfo(int chunks, long elapsedNs) {
        this.lastTaskChunks = chunks;
        this.lastTaskTime = TimeUnit.NANOSECONDS.toMillis(elapsedNs);
        this.lastTaskAvgChunkTime = (double) (elapsedNs / (long) Math.max(1, chunks)) / 1000000.0;
    }

    public int getLastTaskChunks() {
        return this.lastTaskChunks;
    }

    public void copyLastStatsFrom(RenderSpec other) {
        if (other != null) {
            this.lastTaskChunks = other.lastTaskChunks;
            this.lastTaskTime = other.lastTaskTime;
            this.lastTaskAvgChunkTime = other.lastTaskAvgChunkTime;
        }
    }

    public String getDebugStats() {
        String debugString;
        if (this.isUnderground()) {
            debugString = "jm.common.renderstats_debug_cave";
        } else if (this.isTopo()) {
            debugString = "jm.common.renderstats_debug_topo";
        } else {
            debugString = "jm.common.renderstats_debug_surface";
        }
        debugString = debugString + "_simple";
        return Constants.getString(debugString, this.primaryRenderDistance, this.lastTaskChunks, this.lastTaskTime, decFormat.format(this.lastTaskAvgChunkTime));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            RenderSpec that = (RenderSpec) o;
            if (this.maxSecondaryRenderDistance != that.maxSecondaryRenderDistance) {
                return false;
            } else if (this.primaryRenderDistance != that.primaryRenderDistance) {
                return false;
            } else {
                return this.revealShape != that.revealShape ? false : this.mapType.equals(that.mapType);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.mapType.hashCode();
        result = 31 * result + this.primaryRenderDistance;
        result = 31 * result + this.maxSecondaryRenderDistance;
        return 31 * result + this.revealShape.hashCode();
    }

    private static class Offset {

        final int x;

        final int z;

        private Offset(int x, int z) {
            this.x = x;
            this.z = z;
        }

        ChunkPos from(ChunkPos coord) {
            return new ChunkPos(coord.x + this.x, coord.z + this.z);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                RenderSpec.Offset offset = (RenderSpec.Offset) o;
                return this.x != offset.x ? false : this.z == offset.z;
            } else {
                return false;
            }
        }

        public double distance() {
            return Math.sqrt((double) (this.x * this.x + this.z * this.z));
        }

        public int hashCode() {
            int result = this.x;
            return 31 * result + this.z;
        }
    }

    public static enum RevealShape implements KeyedEnum {

        Square("jm.minimap.shape_square"), Circle("jm.minimap.shape_circle");

        public final String key;

        private RevealShape(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        public String toString() {
            return Constants.getString(this.key);
        }
    }
}