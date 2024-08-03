package journeymap.client.model;

import com.google.common.cache.Cache;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import journeymap.client.data.DataCache;
import journeymap.client.io.FileHandler;
import journeymap.client.io.nbt.RegionLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class RegionCoord implements Comparable<RegionCoord> {

    public static final transient int SIZE = 5;

    private static final transient int chunkSqRt = (int) Math.pow(2.0, 5.0);

    public final File worldDir;

    public final Path dimDir;

    public final int regionX;

    public final int regionZ;

    public final ResourceKey<Level> dimension;

    private final int theHashCode;

    private final String theCacheKey;

    public RegionCoord(File worldDir, int regionX, int regionZ, ResourceKey<Level> dimension) {
        this.worldDir = worldDir;
        this.dimDir = FileHandler.getDimPath(worldDir, dimension);
        this.regionX = regionX;
        this.regionZ = regionZ;
        this.dimension = dimension;
        this.theCacheKey = toCacheKey(this.dimDir, regionX, regionZ);
        this.theHashCode = this.theCacheKey.hashCode();
    }

    public static RegionCoord fromChunkPos(File worldDir, MapType mapType, int chunkX, int chunkZ) {
        int regionX = getRegionPos(chunkX);
        int regionZ = getRegionPos(chunkZ);
        return fromRegionPos(worldDir, regionX, regionZ, mapType.dimension);
    }

    public static RegionCoord fromRegionPos(File worldDir, int regionX, int regionZ, ResourceKey<Level> dimension) {
        Cache<String, RegionCoord> cache = DataCache.INSTANCE.getRegionCoords();
        RegionCoord regionCoord = (RegionCoord) cache.getIfPresent(toCacheKey(FileHandler.getDimPath(worldDir, dimension), regionX, regionZ));
        if (regionCoord == null || regionX != regionCoord.regionX || regionZ != regionCoord.regionZ || dimension != regionCoord.dimension) {
            regionCoord = new RegionCoord(worldDir, regionX, regionZ, dimension);
            cache.put(regionCoord.theCacheKey, regionCoord);
        }
        return regionCoord;
    }

    public static int getMinChunkX(int rX) {
        return rX << 5;
    }

    public static int getMaxChunkX(int rX) {
        return getMinChunkX(rX) + (int) Math.pow(2.0, 5.0) - 1;
    }

    public static int getMinChunkZ(int rZ) {
        return rZ << 5;
    }

    public static int getMaxChunkZ(int rZ) {
        return getMinChunkZ(rZ) + (int) Math.pow(2.0, 5.0) - 1;
    }

    public static int getRegionPos(int chunkPos) {
        return chunkPos >> 5;
    }

    public static String toCacheKey(Path dimDir, int regionX, int regionZ) {
        return regionX + dimDir.toString() + regionZ;
    }

    public boolean exists() {
        return RegionLoader.getRegionFile(Minecraft.getInstance(), this.getMinChunkX(), this.getMinChunkZ()).exists();
    }

    public int getXOffset(int chunkX) {
        if (chunkX >> 5 != this.regionX) {
            throw new IllegalArgumentException("chunkX " + chunkX + " out of bounds for regionX " + this.regionX);
        } else {
            int offset = chunkX % chunkSqRt * 16;
            if (offset < 0) {
                offset += chunkSqRt * 16;
            }
            return offset;
        }
    }

    public int getZOffset(int chunkZ) {
        if (getRegionPos(chunkZ) != this.regionZ) {
            throw new IllegalArgumentException("chunkZ " + chunkZ + " out of bounds for regionZ " + this.regionZ);
        } else {
            int offset = chunkZ % chunkSqRt * 16;
            if (offset < 0) {
                offset += chunkSqRt * 16;
            }
            return offset;
        }
    }

    public int getMinChunkX() {
        return getMinChunkX(this.regionX);
    }

    public int getMaxChunkX() {
        return getMaxChunkX(this.regionX);
    }

    public int getMinChunkZ() {
        return getMinChunkZ(this.regionZ);
    }

    public int getMaxChunkZ() {
        return getMaxChunkZ(this.regionZ);
    }

    public ChunkPos getMinChunkCoord() {
        return new ChunkPos(this.getMinChunkX(), this.getMinChunkZ());
    }

    public ChunkPos getMaxChunkCoord() {
        return new ChunkPos(this.getMaxChunkX(), this.getMaxChunkZ());
    }

    public List<ChunkPos> getChunkCoordsInRegion() {
        List<ChunkPos> list = new ArrayList(1024);
        ChunkPos min = this.getMinChunkCoord();
        ChunkPos max = this.getMaxChunkCoord();
        for (int x = min.x; x <= max.x; x++) {
            for (int z = min.z; z <= max.z; z++) {
                list.add(new ChunkPos(x, z));
            }
        }
        return list;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RegionCoord [");
        builder.append(this.regionX);
        builder.append(",");
        builder.append(this.regionZ);
        builder.append("]");
        return builder.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            RegionCoord that = (RegionCoord) o;
            if (this.dimension != that.dimension) {
                return false;
            } else if (this.regionX != that.regionX) {
                return false;
            } else if (this.regionZ != that.regionZ) {
                return false;
            } else {
                return !this.dimDir.equals(that.dimDir) ? false : this.worldDir.equals(that.worldDir);
            }
        } else {
            return false;
        }
    }

    public String cacheKey() {
        return this.theCacheKey;
    }

    public int hashCode() {
        return this.theHashCode;
    }

    public int compareTo(RegionCoord o) {
        int cx = Double.compare((double) this.regionX, (double) o.regionX);
        return cx == 0 ? Double.compare((double) this.regionZ, (double) o.regionZ) : cx;
    }
}