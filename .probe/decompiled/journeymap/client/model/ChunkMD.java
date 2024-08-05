package journeymap.client.model;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Random;
import javax.annotation.Nullable;
import journeymap.client.world.JmBlockAccess;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;

public class ChunkMD {

    public static final String PROP_IS_SLIME_CHUNK = "isSlimeChunk";

    public static final String PROP_LOADED = "loaded";

    public static final String PROP_LAST_RENDERED = "lastRendered";

    protected final WeakReference<LevelChunk> chunkReference;

    private final ChunkPos coord;

    private final HashMap<String, Serializable> properties = new HashMap();

    private BlockDataArrays blockDataArrays = new BlockDataArrays();

    private final Random random;

    protected LevelChunk retainedChunk;

    private byte[][][][] lights;

    public ChunkMD(LevelChunk chunk) {
        this(chunk, false);
    }

    public ChunkMD(LevelChunk chunk, boolean forceRetain, byte[][][][] lights) {
        this(chunk, forceRetain);
        this.lights = lights;
    }

    public ChunkMD(LevelChunk chunk, boolean forceRetain) {
        if (chunk == null) {
            throw new IllegalArgumentException("Chunk can't be null");
        } else {
            this.random = new Random();
            this.coord = new ChunkPos(chunk.m_7697_().x, chunk.m_7697_().z);
            this.setProperty("loaded", System.currentTimeMillis());
            this.properties.put("isSlimeChunk", isSlimeChunk(chunk));
            this.chunkReference = new WeakReference(chunk);
            if (forceRetain) {
                this.retainedChunk = chunk;
            }
        }
    }

    public BlockState getBlockState(int localX, int y, int localZ) {
        if (localX < 0 || localX > 15 || localZ < 0 || localZ > 15) {
            Journeymap.getLogger().warn("Expected local coords, got global coords");
        }
        return this.getBlockState(new BlockPos(this.toWorldX(localX), y, this.toWorldZ(localZ)));
    }

    public BlockState getChunkBlockState(BlockPos blockPos) {
        return this.getChunk().getBlockState(blockPos);
    }

    public BlockState getBlockState(BlockPos blockPos) {
        return JmBlockAccess.INSTANCE.getBlockState(blockPos);
    }

    public BlockMD getBlockMD(BlockPos blockPos) {
        return BlockMD.getBlockMD(this, blockPos);
    }

    @Nullable
    public Biome getBiome(BlockPos pos) {
        return (Biome) this.getChunk().m_203495_(pos.m_123341_() >> 2, pos.m_123342_() >> 2, pos.m_123343_() >> 2).value();
    }

    public int getSavedLightValue(int localX, int y, int localZ) {
        if (this.lights != null) {
            try {
                int localY = SectionPos.sectionRelative(y);
                int sectionIndex = this.getChunk().getLevel().m_151564_(y);
                return this.lights[sectionIndex][localX][localY][localZ];
            } catch (Exception var8) {
            }
        }
        try {
            return this.getChunk().getLevel().getLightEngine().getLayerListener(LightLayer.BLOCK).getLightValue(this.getBlockPos(localX, y, localZ));
        } catch (ArrayIndexOutOfBoundsException var7) {
            return 1;
        }
    }

    public BlockMD getBlockMD(int localX, int y, int localZ) {
        return BlockMD.getBlockMD(this, this.getBlockPos(localX, y, localZ));
    }

    public int ceiling(int localX, int localZ) {
        int chunkHeight = this.getPrecipitationHeight(this.getBlockPos(localX, 0, localZ));
        int y = chunkHeight;
        BlockPos blockPos = null;
        try {
            while (y >= this.getMinY()) {
                blockPos = this.getBlockPos(localX, y, localZ);
                BlockMD blockMD = this.getBlockMD(blockPos);
                if (blockMD == null) {
                    y--;
                } else if (!blockMD.isIgnore() && !blockMD.hasFlag(BlockFlag.OpenToSky)) {
                    if (!this.canBlockSeeTheSky(localX, y, localZ)) {
                        break;
                    }
                    y--;
                } else {
                    y--;
                }
            }
        } catch (Exception var7) {
            Journeymap.getLogger().warn(var7 + " at " + blockPos, var7);
        }
        return Math.max(this.getMinY(), y);
    }

    public boolean hasChunk() {
        LevelChunk chunk = (LevelChunk) this.chunkReference.get();
        return chunk != null && !(chunk instanceof EmptyLevelChunk);
    }

    public int getHeight(BlockPos blockPos) {
        LevelChunk chunk = this.getChunk();
        return chunk.m_5885_(Heightmap.Types.WORLD_SURFACE, blockPos.m_123341_(), blockPos.m_123343_());
    }

    public int getPrecipitationHeight(int localX, int localZ) {
        return this.getPrecipitationHeight(this.getBlockPos(localX, 0, localZ));
    }

    public int getPrecipitationHeight(BlockPos blockPos) {
        return this.getChunk().m_5885_(Heightmap.Types.WORLD_SURFACE, blockPos.m_123341_(), blockPos.m_123343_());
    }

    public int getLightOpacity(BlockMD blockMD, int localX, int y, int localZ) {
        BlockPos pos = this.getBlockPos(localX, y, localZ);
        return blockMD.getBlockState().m_60734_().m_7753_(blockMD.getBlockState(), JmBlockAccess.INSTANCE, pos);
    }

    public Serializable getProperty(String name) {
        return (Serializable) this.properties.get(name);
    }

    public Serializable getProperty(String name, Serializable defaultValue) {
        Serializable currentValue = this.getProperty(name);
        if (currentValue == null) {
            this.setProperty(name, defaultValue);
            currentValue = defaultValue;
        }
        return currentValue;
    }

    public Serializable setProperty(String name, Serializable value) {
        return (Serializable) this.properties.put(name, value);
    }

    public int hashCode() {
        return this.getCoord().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            ChunkMD other = (ChunkMD) obj;
            return this.getCoord().equals(other.getCoord());
        }
    }

    public LevelChunk getChunk() {
        LevelChunk chunk = (LevelChunk) this.chunkReference.get();
        if (chunk == null) {
            throw new ChunkMD.ChunkMissingException(this.getCoord());
        } else {
            return chunk;
        }
    }

    public ClientLevel getWorld() {
        return Minecraft.getInstance().level;
    }

    public int getWorldActualHeight() {
        return this.getWorld().m_6042_().logicalHeight() + 1;
    }

    public Boolean hasNoSky() {
        return !this.getWorld().m_6042_().natural();
    }

    public boolean canBlockSeeTheSky(int localX, int y, int localZ) {
        int i = localX & 15;
        int k = localZ & 15;
        return y >= this.getChunk().m_5885_(Heightmap.Types.WORLD_SURFACE, i, k);
    }

    public long getLongCoord() {
        return this.coord.toLong();
    }

    public ChunkPos getCoord() {
        return this.coord;
    }

    public static boolean isSlimeChunk(LevelChunk chunk) {
        return chunk.getLevel() instanceof ServerLevel ? WorldgenRandom.seedSlimeChunk(chunk.m_7697_().x, chunk.m_7697_().z, ((ServerLevel) chunk.getLevel()).getSeed(), 987234911L).nextInt(10) == 0 : false;
    }

    public long getLoaded() {
        return (Long) this.getProperty("loaded", 0L);
    }

    public void resetRenderTimes() {
        this.getRenderTimes().clear();
    }

    public void resetRenderTime(MapType mapType) {
        this.getRenderTimes().put(mapType, 0L);
    }

    public void resetBlockData(MapType mapType) {
        this.getBlockData().get(mapType).clear();
    }

    protected HashMap<MapType, Long> getRenderTimes() {
        Serializable obj = (Serializable) this.properties.get("lastRendered");
        if (!(obj instanceof HashMap)) {
            obj = new HashMap();
            this.properties.put("lastRendered", obj);
        }
        return (HashMap<MapType, Long>) obj;
    }

    public long getLastRendered(MapType mapType) {
        return (Long) this.getRenderTimes().getOrDefault(mapType, 0L);
    }

    public long setRendered(MapType mapType) {
        long now = System.currentTimeMillis();
        this.getRenderTimes().put(mapType, now);
        return now;
    }

    public BlockPos getBlockPos(int localX, int y, int localZ) {
        return new BlockPos(this.toWorldX(localX), y, this.toWorldZ(localZ));
    }

    public int toWorldX(int localX) {
        return (this.coord.x << 4) + localX;
    }

    public int toWorldZ(int localZ) {
        return (this.coord.z << 4) + localZ;
    }

    public BlockDataArrays getBlockData() {
        return this.blockDataArrays;
    }

    public BlockDataArrays.DataArray<Integer> getBlockDataInts(MapType mapType) {
        return this.blockDataArrays.get(mapType).ints();
    }

    public BlockDataArrays.DataArray<Float> getBlockDataFloats(MapType mapType) {
        return this.blockDataArrays.get(mapType).floats();
    }

    public BlockDataArrays.DataArray<Boolean> getBlockDataBooleans(MapType mapType) {
        return this.blockDataArrays.get(mapType).booleans();
    }

    public String toString() {
        return "ChunkMD{coord=" + this.coord + ", properties=" + this.properties + "}";
    }

    public ResourceKey<Level> getDimension() {
        return this.getWorld().m_46472_();
    }

    public void stopChunkRetention() {
        this.retainedChunk = null;
    }

    public boolean hasRetainedChunk() {
        return this.retainedChunk != null;
    }

    public Integer getMinY() {
        return this.getWorld().m_141937_();
    }

    public boolean fromNbt() {
        return false;
    }

    public static class ChunkMissingException extends RuntimeException {

        ChunkMissingException(ChunkPos coord) {
            super("Chunk missing: " + coord);
        }
    }
}