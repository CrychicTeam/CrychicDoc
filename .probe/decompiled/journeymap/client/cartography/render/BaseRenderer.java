package journeymap.client.cartography.render;

import com.mojang.blaze3d.platform.NativeImage;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.IChunkRenderer;
import journeymap.client.cartography.Stratum;
import journeymap.client.cartography.color.RGB;
import journeymap.client.data.DataCache;
import journeymap.client.model.BlockCoordIntPair;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.MapType;
import journeymap.client.properties.CoreProperties;
import journeymap.common.Journeymap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseRenderer implements IChunkRenderer {

    public static final int COLOR_BLACK = Color.black.getRGB();

    public static volatile AtomicLong badBlockCount = new AtomicLong(0L);

    protected static final float[] DEFAULT_FOG = new float[] { 0.0F, 0.0F, 0.1F };

    protected final DataCache dataCache = DataCache.INSTANCE;

    protected CoreProperties coreProperties;

    protected boolean mapBathymetry;

    protected boolean mapTransparency;

    protected boolean mapCaveLighting;

    protected boolean mapAntialiasing;

    protected float[] ambientColor;

    protected long lastPropFileUpdate;

    protected ArrayList<BlockCoordIntPair> primarySlopeOffsets = new ArrayList(3);

    protected ArrayList<BlockCoordIntPair> secondarySlopeOffsets = new ArrayList(4);

    protected float shadingSlopeMin;

    protected float shadingSlopeMax;

    protected float shadingPrimaryDownslopeMultiplier;

    protected float shadingPrimaryUpslopeMultiplier;

    protected float shadingSecondaryDownslopeMultiplier;

    protected float shadingSecondaryUpslopeMultiplier;

    protected float tweakMoonlightLevel;

    protected float tweakBrightenDaylightDiff;

    protected float tweakBrightenLightsourceBlock;

    protected float tweakBlendShallowWater;

    protected float tweakMinimumDarkenNightWater;

    protected float tweakWaterColorBlend;

    protected int tweakSurfaceAmbientColor;

    protected int tweakCaveAmbientColor;

    protected int tweakNetherAmbientColor;

    protected int tweakEndAmbientColor;

    private static final String PROP_SLOPES = "slopes";

    private static final String PROP_HEIGHTS = "heights";

    private static final String PROP_WATER_HEIGHTS = "waterHeights";

    private MapType currentMapType;

    public BaseRenderer() {
        this.updateOptions(null, null);
        this.shadingSlopeMin = 0.2F;
        this.shadingSlopeMax = 1.7F;
        this.shadingPrimaryDownslopeMultiplier = 0.65F;
        this.shadingPrimaryUpslopeMultiplier = 1.2F;
        this.shadingSecondaryDownslopeMultiplier = 0.95F;
        this.shadingSecondaryUpslopeMultiplier = 1.05F;
        this.tweakMoonlightLevel = 3.5F;
        this.tweakBrightenDaylightDiff = 0.06F;
        this.tweakBrightenLightsourceBlock = 1.2F;
        this.tweakBlendShallowWater = 0.15F;
        this.tweakMinimumDarkenNightWater = 0.25F;
        this.tweakWaterColorBlend = 0.5F;
        this.tweakSurfaceAmbientColor = 26;
        this.tweakCaveAmbientColor = 0;
        this.tweakNetherAmbientColor = 3344392;
        this.tweakEndAmbientColor = 26;
        this.primarySlopeOffsets.add(new BlockCoordIntPair(0, -1));
        this.primarySlopeOffsets.add(new BlockCoordIntPair(-1, -1));
        this.primarySlopeOffsets.add(new BlockCoordIntPair(-1, 0));
        this.secondarySlopeOffsets.add(new BlockCoordIntPair(-1, -2));
        this.secondarySlopeOffsets.add(new BlockCoordIntPair(-2, -1));
        this.secondarySlopeOffsets.add(new BlockCoordIntPair(-2, -2));
        this.secondarySlopeOffsets.add(new BlockCoordIntPair(-2, 0));
        this.secondarySlopeOffsets.add(new BlockCoordIntPair(0, -2));
    }

    protected boolean updateOptions(ChunkMD chunkMd, MapType mapType) {
        this.currentMapType = mapType;
        boolean updateNeeded = false;
        this.coreProperties = JourneymapClient.getInstance().getCoreProperties();
        long lastUpdate = JourneymapClient.getInstance().getCoreProperties().lastModified();
        if (lastUpdate == 0L || this.lastPropFileUpdate != lastUpdate) {
            updateNeeded = true;
            this.lastPropFileUpdate = lastUpdate;
            this.mapBathymetry = this.coreProperties.mapBathymetry.get();
            this.mapTransparency = this.coreProperties.mapTransparency.get();
            this.mapAntialiasing = this.coreProperties.mapAntialiasing.get();
            this.mapCaveLighting = this.coreProperties.mapCaveLighting.get();
            this.ambientColor = new float[] { 0.0F, 0.0F, 0.0F };
        }
        if (chunkMd != null) {
            Long lastChunkUpdate = (Long) chunkMd.getProperty("lastPropFileUpdate", this.lastPropFileUpdate);
            updateNeeded = true;
            chunkMd.resetBlockData(this.getCurrentMapType());
            chunkMd.setProperty("lastPropFileUpdate", lastChunkUpdate);
        }
        return updateNeeded;
    }

    @Override
    public float[] getAmbientColor() {
        return DEFAULT_FOG;
    }

    @Override
    public void setStratumColors(Stratum stratum, int lightAttenuation, Integer waterColor, boolean waterAbove, boolean underground, boolean mapCaveLighting) {
        if (stratum.isUninitialized()) {
            throw new IllegalStateException("Stratum wasn't initialized for setStratumColors");
        } else {
            float dayAmbient = 15.0F;
            boolean noSky = stratum.getWorldHasNoSky();
            float daylightDiff;
            float nightLightDiff;
            if (noSky) {
                dayAmbient = stratum.getWorldAmbientLight();
                daylightDiff = Math.max(1.0F, Math.max((float) stratum.getLightLevel(), dayAmbient - (float) lightAttenuation)) / 15.0F;
                nightLightDiff = daylightDiff;
            } else {
                daylightDiff = Math.max(1.0F, Math.max((float) stratum.getLightLevel(), dayAmbient - (float) lightAttenuation)) / 15.0F;
                daylightDiff += this.tweakBrightenDaylightDiff;
                nightLightDiff = Math.max(this.tweakMoonlightLevel, Math.max((float) stratum.getLightLevel(), this.tweakMoonlightLevel - (float) lightAttenuation)) / 15.0F;
            }
            int basicColor = stratum.getBlockMD().getBlockColor(stratum.getChunkMd(), stratum.getBlockPos());
            Block block = stratum.getBlockMD().getBlockState().m_60734_();
            BlockState state = stratum.getBlockMD().getBlockState();
            if (block == Blocks.GLOWSTONE || block == Blocks.REDSTONE_LAMP && (Boolean) state.m_61143_(RedstoneLampBlock.LIT)) {
                basicColor = RGB.adjustBrightness(basicColor, this.tweakBrightenLightsourceBlock);
            }
            if (waterAbove && waterColor != null) {
                int adjustedWaterColor = waterColor;
                int adjustedBasicColor = RGB.adjustBrightness(basicColor, Math.max(daylightDiff, nightLightDiff));
                stratum.setDayColor(RGB.blendWith(adjustedBasicColor, adjustedWaterColor, this.tweakWaterColorBlend));
                if (noSky) {
                    stratum.setNightColor(stratum.getDayColor());
                } else {
                    stratum.setNightColor(RGB.adjustBrightness(stratum.getDayColor(), Math.max(nightLightDiff, this.tweakMinimumDarkenNightWater)));
                }
            } else {
                stratum.setDayColor(RGB.adjustBrightness(basicColor, daylightDiff));
                if (noSky) {
                    stratum.setNightColor(stratum.getDayColor());
                } else {
                    stratum.setNightColor(RGB.darkenAmbient(basicColor, nightLightDiff, this.getAmbientColor()));
                }
            }
            if (underground) {
                stratum.setCaveColor(mapCaveLighting ? stratum.getNightColor() : stratum.getDayColor());
            }
        }
    }

    protected Float[][] populateSlopes(ChunkMD chunkMd, Integer vSlice, Float[][] slopes) {
        int y = 0;
        int sliceMinY = 0;
        int sliceMaxY = 0;
        boolean isSurface = vSlice == null;
        if (!isSurface) {
            int[] sliceBounds = this.getVSliceBounds(chunkMd, vSlice);
            sliceMinY = sliceBounds[0];
            sliceMaxY = sliceBounds[1];
        }
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                y = this.getBlockHeight(chunkMd, x, vSlice, z, sliceMinY, sliceMaxY);
                Float primarySlope = this.calculateSlope(chunkMd, this.primarySlopeOffsets, x, y, z, isSurface, vSlice, sliceMinY, sliceMaxY);
                Float slope = primarySlope;
                if (primarySlope < 1.0F) {
                    slope = primarySlope * this.shadingPrimaryDownslopeMultiplier;
                } else if (primarySlope > 1.0F) {
                    slope = primarySlope * this.shadingPrimaryUpslopeMultiplier;
                }
                if (this.mapAntialiasing && primarySlope == 1.0F) {
                    Float secondarySlope = this.calculateSlope(chunkMd, this.secondarySlopeOffsets, x, y, z, isSurface, vSlice, sliceMinY, sliceMaxY);
                    if (secondarySlope > primarySlope) {
                        slope = slope * this.shadingSecondaryUpslopeMultiplier;
                    } else if (secondarySlope < primarySlope) {
                        slope = slope * this.shadingSecondaryDownslopeMultiplier;
                    }
                }
                if (slope.isNaN()) {
                    slope = 1.0F;
                }
                slopes[x][z] = Math.min(this.shadingSlopeMax, Math.max(this.shadingSlopeMin, slope));
            }
        }
        return slopes;
    }

    protected MapType getCurrentMapType() {
        return this.currentMapType;
    }

    public abstract int getBlockHeight(ChunkMD var1, BlockPos var2);

    protected abstract Integer getBlockHeight(ChunkMD var1, int var2, Integer var3, int var4, Integer var5, Integer var6);

    protected int getOffsetBlockHeight(ChunkMD chunkMd, int x, Integer vSlice, int z, Integer sliceMinY, Integer sliceMaxY, BlockCoordIntPair offset, int defaultVal) {
        int blockX = (chunkMd.getCoord().x << 4) + x + offset.x;
        int blockZ = (chunkMd.getCoord().z << 4) + z + offset.z;
        long targetCoord = ChunkPos.asLong(blockX >> 4, blockZ >> 4);
        ChunkMD targetChunkMd;
        if (targetCoord == chunkMd.getLongCoord()) {
            targetChunkMd = chunkMd;
        } else {
            targetChunkMd = this.dataCache.getChunkMD(targetCoord);
        }
        return targetChunkMd != null ? this.getBlockHeight(targetChunkMd, blockX & 15, vSlice, blockZ & 15, sliceMinY, sliceMaxY) : defaultVal;
    }

    protected float calculateSlope(ChunkMD chunkMd, Collection<BlockCoordIntPair> offsets, int x, int y, int z, boolean isSurface, Integer vSlice, int sliceMinY, int sliceMaxY) {
        if (y <= chunkMd.getMinY()) {
            return 1.0F;
        } else {
            int relY = y - chunkMd.getMinY();
            float slopeSum = 0.0F;
            int defaultHeight = y;
            for (BlockCoordIntPair offset : offsets) {
                float offsetHeight = (float) this.getOffsetBlockHeight(chunkMd, x, vSlice, z, sliceMinY, sliceMaxY, offset, defaultHeight);
                if (offsetHeight < 0.0F) {
                    slopeSum += (offsetHeight - (float) relY) / ((float) (y - relY) * 1.0F);
                } else {
                    slopeSum += (float) (y + relY) * 1.0F / (offsetHeight + (float) relY);
                }
            }
            Float slope = slopeSum / (float) offsets.size();
            if (slope.isNaN()) {
                slope = 1.0F;
            }
            return slope;
        }
    }

    protected int[] getVSliceBounds(ChunkMD chunkMd, Integer vSlice) {
        if (vSlice == null) {
            return null;
        } else {
            int sliceMinY = vSlice << 4;
            int hardSliceMaxY = (vSlice + 1 << 4) - 1;
            int sliceMaxY = Math.min(hardSliceMaxY, chunkMd.getWorld().m_6042_().logicalHeight());
            if (sliceMinY >= sliceMaxY) {
                sliceMaxY = sliceMinY + 2;
            }
            return new int[] { sliceMinY, sliceMaxY };
        }
    }

    protected float getSlope(ChunkMD chunkMd, int x, Integer vSlice, int z) {
        Float[][] slopes = this.getSlopes(chunkMd, vSlice);
        Float slope = slopes[x][z];
        if (slope == null) {
            this.populateSlopes(chunkMd, vSlice, slopes);
            slope = slopes[x][z];
        }
        if (slope == null || slope.isNaN()) {
            Journeymap.getLogger().warn(String.format("Bad slope for %s at %s,%s: %s", chunkMd.getCoord(), x, z, slope));
            slope = 1.0F;
        }
        return slope;
    }

    protected final String getKey(String propName, Integer vSlice) {
        return vSlice == null ? propName : propName + vSlice;
    }

    protected final Integer[][] getHeights(ChunkMD chunkMd, Integer vSlice) {
        return chunkMd.getBlockDataInts(this.getCurrentMapType()).get(this.getKey("heights", vSlice));
    }

    protected final boolean hasHeights(ChunkMD chunkMd, Integer vSlice) {
        return chunkMd.getBlockDataInts(this.getCurrentMapType()).has(this.getKey("heights", vSlice));
    }

    protected final void resetHeights(ChunkMD chunkMd, Integer vSlice) {
        chunkMd.getBlockDataInts(this.getCurrentMapType()).clear(this.getKey("heights", vSlice));
    }

    protected final Float[][] getSlopes(ChunkMD chunkMd, Integer vSlice) {
        return chunkMd.getBlockDataFloats(this.getCurrentMapType()).get(this.getKey("slopes", vSlice));
    }

    protected final boolean hasSlopes(ChunkMD chunkMd, Integer vSlice) {
        return chunkMd.getBlockDataFloats(this.getCurrentMapType()).has(this.getKey("slopes", vSlice));
    }

    protected final void resetSlopes(ChunkMD chunkMd, Integer vSlice) {
        chunkMd.getBlockDataFloats(this.getCurrentMapType()).clear(this.getKey("slopes", vSlice));
    }

    protected final Integer[][] getFluidHeights(ChunkMD chunkMd, Integer vSlice) {
        return chunkMd.getBlockDataInts(this.getCurrentMapType()).get(this.getKey("waterHeights", vSlice));
    }

    protected final boolean hasWaterHeights(ChunkMD chunkMd, Integer vSlice) {
        return chunkMd.getBlockDataInts(this.getCurrentMapType()).has(this.getKey("waterHeights", vSlice));
    }

    protected final void resetWaterHeights(ChunkMD chunkMd, Integer vSlice) {
        chunkMd.getBlockDataInts(this.getCurrentMapType()).clear(this.getKey("waterHeights", vSlice));
    }

    public ChunkMD getOffsetChunk(ChunkMD chunkMd, int x, int z, BlockCoordIntPair offset) {
        int blockX = (chunkMd.getCoord().x << 4) + x + offset.x;
        int blockZ = (chunkMd.getCoord().z << 4) + z + offset.z;
        long targetCoord = ChunkPos.asLong(blockX >> 4, blockZ >> 4);
        return targetCoord == chunkMd.getLongCoord() ? chunkMd : this.dataCache.getChunkMD(targetCoord);
    }

    public void paintDimOverlay(NativeImage image, int x, int z, float alpha) {
        Integer color = image.getPixelRGBA(x, z);
        this.paintBlock(image, x, z, RGB.adjustBrightness(color, alpha));
    }

    public int paintDimOverlay(NativeImage sourceImage, NativeImage targetImage, int x, int z, float alpha) {
        int color = sourceImage.getPixelRGBA(x, z);
        targetImage.blendPixel(x, z, RGB.adjustBrightness(color, alpha));
        return RGB.adjustBrightness(color, alpha);
    }

    public int paintBlock(NativeImage image, int x, int z, int color) {
        image.setPixelRGBA(x, z, RGB.toRgba(color, 1.0F));
        return 0xFF000000 | color;
    }

    public int paintVoidBlock(NativeImage image, int x, int z) {
        this.paintBlock(image, x, z, RGB.toInteger(this.getAmbientColor()));
        return RGB.toInteger(this.getAmbientColor());
    }

    public int paintBlackBlock(NativeImage image, int x, int z) {
        this.paintBlock(image, x, z, COLOR_BLACK);
        return COLOR_BLACK;
    }

    public int paintClearBlock(NativeImage image, int x, int z) {
        int clear = RGB.toRgba(COLOR_BLACK, 0.0F);
        image.setPixelRGBA(x, z, clear);
        return clear;
    }

    public void paintBadBlock(NativeImage image, int x, int y, int z) {
        long count = badBlockCount.incrementAndGet();
        if (count == 1L || count % 2046L == 0L) {
            Journeymap.getLogger().warn("Bad block at " + x + "," + y + "," + z + ". Total bad blocks: " + count);
        }
    }
}