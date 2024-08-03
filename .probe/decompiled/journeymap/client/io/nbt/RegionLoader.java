package journeymap.client.io.nbt;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import journeymap.client.io.FileHandler;
import journeymap.client.io.RegionImageHandler;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionCoord;
import journeymap.client.model.RegionImageCache;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;
import org.apache.logging.log4j.Logger;

public class RegionLoader {

    private static final Pattern anvilPattern = Pattern.compile("r\\.([^\\.]+)\\.([^\\.]+)\\.mca");

    final Logger logger = Journeymap.getLogger();

    final MapType mapType;

    final Stack<RegionCoord> regions;

    final int regionsFound;

    public RegionLoader(Minecraft minecraft, MapType mapType, boolean all) throws IOException {
        this.mapType = mapType;
        this.regions = this.findRegions(minecraft, mapType, all);
        this.regionsFound = this.regions.size();
    }

    public static File getRegionFile(Minecraft minecraft, int dimension, int chunkX, int chunkZ) {
        File regionDir = new File(FileHandler.getWorldSaveDir(minecraft), "region");
        return new File(regionDir, String.format("r.%s.%s.mca", chunkX >> 5, chunkZ >> 5));
    }

    public static File getRegionFile(Minecraft minecraft, int chunkX, int chunkZ) {
        File regionDir = new File(FileHandler.getWorldSaveDir(minecraft), "region");
        return new File(regionDir, String.format("r.%s.%s.mca", chunkX >> 5, chunkZ >> 5));
    }

    public Iterator<RegionCoord> regionIterator() {
        return this.regions.iterator();
    }

    public Stack<RegionCoord> getRegions() {
        return this.regions;
    }

    public int getRegionsFound() {
        return this.regionsFound;
    }

    public boolean isUnderground() {
        return this.mapType.isUnderground();
    }

    public Integer getVSlice() {
        return this.mapType.vSlice;
    }

    Stack<RegionCoord> findRegions(Minecraft mc, MapType mapType, boolean all) {
        File mcWorldDir = FileHandler.getMCWorldDir(mc, mapType.dimension);
        File regionDir = new File(mcWorldDir, "region");
        if (!regionDir.exists() && !regionDir.mkdirs()) {
            this.logger.warn("MC world region directory isn't usable: " + regionDir);
            return new Stack();
        } else {
            RegionImageCache.INSTANCE.flushToDisk(false);
            RegionImageCache.INSTANCE.clear();
            File jmImageWorldDir = FileHandler.getJMWorldDir(mc);
            Stack<RegionCoord> rcStack = new Stack();
            int validFileCount = 0;
            int existingImageCount = 0;
            File[] anvilFiles = regionDir.listFiles();
            for (File anvilFile : anvilFiles) {
                try {
                    Matcher matcher = anvilPattern.matcher(anvilFile.getName());
                    if (!anvilFile.isDirectory() && matcher.matches()) {
                        validFileCount++;
                        String x = matcher.group(1);
                        String z = matcher.group(2);
                        if (x != null && z != null) {
                            RegionCoord rc = new RegionCoord(jmImageWorldDir, Integer.parseInt(x), Integer.parseInt(z), mapType.dimension);
                            if (all) {
                                rcStack.add(rc);
                            } else if (!RegionImageHandler.getRegionImageFile(rc, mapType).exists()) {
                                for (ChunkPos coord : rc.getChunkCoordsInRegion()) {
                                    if (mc.level.hasChunk(coord.x, coord.z)) {
                                        rcStack.add(rc);
                                        break;
                                    }
                                }
                            } else {
                                existingImageCount++;
                            }
                        }
                    }
                } catch (Throwable var22) {
                    this.logger.warn("Error processing Region File {}, skipping!", anvilFile.getName(), var22);
                }
            }
            if (rcStack.isEmpty() && validFileCount != existingImageCount) {
                this.logger.warn("Anvil region files in " + regionDir + ": " + validFileCount + ", matching image files: " + existingImageCount + ", but found nothing to do for mapType " + mapType);
            }
            final RegionCoord playerRc = RegionCoord.fromChunkPos(jmImageWorldDir, mapType, mc.player.m_146902_().x, mc.player.m_146902_().z);
            if (rcStack.contains(playerRc)) {
                rcStack.remove(playerRc);
            }
            Collections.sort(rcStack, new Comparator<RegionCoord>() {

                public int compare(RegionCoord o1, RegionCoord o2) {
                    Float d1 = this.distanceToPlayer(o1);
                    Float d2 = this.distanceToPlayer(o2);
                    int comp = d2.compareTo(d1);
                    return comp == 0 ? o2.compareTo(o1) : comp;
                }

                float distanceToPlayer(RegionCoord rc) {
                    float x = (float) (rc.regionX - playerRc.regionX);
                    float z = (float) (rc.regionZ - playerRc.regionZ);
                    return x * x + z * z;
                }
            });
            rcStack.add(playerRc);
            return rcStack;
        }
    }

    public MapType getMapType() {
        return this.mapType;
    }
}