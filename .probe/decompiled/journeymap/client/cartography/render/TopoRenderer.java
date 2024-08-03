package journeymap.client.cartography.render;

import com.mojang.blaze3d.platform.NativeImage;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.IChunkRenderer;
import journeymap.client.cartography.color.RGB;
import journeymap.client.log.JMLogger;
import journeymap.client.log.StatTimer;
import journeymap.client.model.BlockCoordIntPair;
import journeymap.client.model.BlockFlag;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.MapType;
import journeymap.client.properties.TopoProperties;
import journeymap.client.texture.ComparableNativeImage;
import journeymap.client.world.JmBlockAccess;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.nbt.RegionData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import org.apache.logging.log4j.Level;

public class TopoRenderer extends BaseRenderer implements IChunkRenderer {

    private static final String PROP_SHORE = "isShore";

    private Integer[] waterPalette;

    private Integer[] landPalette;

    private int waterPaletteRange;

    private int landPaletteRange;

    private long lastTopoFileUpdate;

    protected StatTimer renderTopoTimer = StatTimer.get("TopoRenderer.renderSurface");

    private Integer landContourColor;

    private Integer waterContourColor;

    private double waterContourInterval;

    private double landContourInterval;

    TopoProperties topoProperties;

    public TopoRenderer() {
        this.primarySlopeOffsets.clear();
        this.secondarySlopeOffsets.clear();
        this.primarySlopeOffsets.add(new BlockCoordIntPair(0, -1));
        this.primarySlopeOffsets.add(new BlockCoordIntPair(-1, 0));
        this.primarySlopeOffsets.add(new BlockCoordIntPair(0, 1));
        this.primarySlopeOffsets.add(new BlockCoordIntPair(1, 0));
    }

    @Override
    protected boolean updateOptions(ChunkMD chunkMd, MapType mapType) {
        boolean needUpdate = false;
        if (super.updateOptions(chunkMd, mapType)) {
            double worldHeight = (double) JmBlockAccess.INSTANCE.getMaxBuildHeight();
            if (chunkMd != null) {
                worldHeight = (double) chunkMd.getWorld().m_151558_();
            }
            this.topoProperties = JourneymapClient.getInstance().getTopoProperties();
            if (System.currentTimeMillis() - this.lastTopoFileUpdate > 5000L && this.lastTopoFileUpdate < this.topoProperties.lastModified()) {
                needUpdate = true;
                this.topoProperties.load();
                this.lastTopoFileUpdate = this.topoProperties.lastModified();
                this.landContourColor = this.topoProperties.getLandContourColor();
                this.waterContourColor = this.topoProperties.getWaterContourColor();
                this.waterPalette = this.topoProperties.getWaterColors();
                this.waterPaletteRange = this.waterPalette.length - 1;
                this.waterContourInterval = worldHeight / (double) Math.max(1, this.waterPalette.length);
                this.landPalette = this.topoProperties.getLandColors();
                this.landPaletteRange = this.landPalette.length - 1;
                this.landContourInterval = worldHeight / (double) Math.max(1, this.landPalette.length);
            }
            if (chunkMd != null) {
                Long lastUpdate = (Long) chunkMd.getProperty("lastTopoPropFileUpdate", this.lastTopoFileUpdate);
                if (needUpdate || lastUpdate < this.lastTopoFileUpdate) {
                    needUpdate = true;
                    chunkMd.resetBlockData(this.getCurrentMapType());
                }
                chunkMd.setProperty("lastTopoPropFileUpdate", this.lastTopoFileUpdate);
            }
        }
        return needUpdate;
    }

    @Override
    public boolean render(ComparableNativeImage chunkImage, RegionData regionData, ChunkMD chunkMd, Integer vSlice) {
        StatTimer timer = this.renderTopoTimer;
        if (this.landPalette != null && this.landPalette.length >= 1 && this.waterPalette != null && this.waterPalette.length >= 1) {
            boolean var7;
            try {
                timer.start();
                this.updateOptions(chunkMd, MapType.from(MapType.Name.topo, null, chunkMd.getDimension()));
                if (!this.hasSlopes(chunkMd, null)) {
                    this.populateSlopes(chunkMd);
                }
                return this.renderSurface(chunkImage, regionData, chunkMd, vSlice, false);
            } catch (Throwable var11) {
                JMLogger.throwLogOnce("Chunk Error", var11);
                var7 = false;
            } finally {
                timer.stop();
            }
            return var7;
        } else {
            return false;
        }
    }

    protected boolean renderSurface(NativeImage chunkImage, RegionData regionData, ChunkMD chunkMd, Integer vSlice, boolean cavePrePass) {
        boolean chunkOk = false;
        try {
            CompoundTag chunkNbt = regionData.getChunkNbt(chunkMd.getCoord());
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    CompoundTag blockNbt = regionData.getBlockDataFromBlockPos(chunkMd.getCoord(), chunkNbt, x, z);
                    BlockMD topBlockMd = null;
                    int y = Math.max(chunkMd.getMinY(), this.getBlockHeight(chunkMd, x, null, z, null, null));
                    if (this.mapBathymetry) {
                        Integer[][] waterHeights = this.getFluidHeights(chunkMd, null);
                        if (waterHeights[z] != null && waterHeights[z][x] != null) {
                            y = this.getFluidHeights(chunkMd, null)[z][x];
                        }
                    }
                    topBlockMd = chunkMd.getBlockMD(x, y, z);
                    if (topBlockMd == null) {
                        this.paintBadBlock(chunkImage, x, y, z);
                    } else {
                        chunkOk = this.paintContour(chunkImage, regionData, blockNbt, chunkMd, topBlockMd, x, y, z) || chunkOk;
                        regionData.setBiome(blockNbt, chunkMd.getBiome(chunkMd.getBlockPos(x, y, z)));
                        regionData.setY(blockNbt, y);
                    }
                }
            }
            regionData.writeChunk(chunkMd.getCoord(), chunkNbt);
        } catch (Throwable var14) {
            Journeymap.getLogger().log(Level.WARN, "Error in renderSurface: " + LogFormatter.toString(var14));
        }
        return chunkOk;
    }

    @Override
    public Integer getBlockHeight(ChunkMD chunkMd, int localX, Integer vSlice, int localZ, Integer sliceMinY, Integer sliceMaxY) {
        Integer[][] heights = this.getHeights(chunkMd, null);
        if (heights == null) {
            return null;
        } else {
            Integer y = heights[localX][localZ];
            if (y != null) {
                return y;
            } else {
                y = Math.max(chunkMd.getMinY(), chunkMd.getPrecipitationHeight(localX, localZ));
                try {
                    for (BlockMD blockMD = BlockMD.getBlockMDFromChunkLocal(chunkMd, localX, y, localZ); y > chunkMd.getMinY(); blockMD = BlockMD.getBlockMDFromChunkLocal(chunkMd, localX, y, localZ)) {
                        if (!blockMD.isWater() && !blockMD.isIce()) {
                            if (!blockMD.hasAnyFlag(BlockMD.FlagsPlantAndCrop) && !blockMD.isIgnore() && !blockMD.hasFlag(BlockFlag.NoTopo)) {
                                break;
                            }
                        } else {
                            if (!this.mapBathymetry) {
                                break;
                            }
                            this.getFluidHeights(chunkMd, null)[localZ][localX] = y;
                        }
                        y = y - 1;
                    }
                } catch (Exception var11) {
                    Journeymap.getLogger().debug("Couldn't get safe surface block height at " + localX + "," + localZ + ": " + var11);
                }
                y = Math.max(chunkMd.getMinY(), y);
                heights[localX][localZ] = y;
                return y;
            }
        }
    }

    protected Float[][] populateSlopes(ChunkMD chunkMd) {
        Float[][] slopes = this.getSlopes(chunkMd, null);
        float nearZero = 1.0E-4F;
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                float h = (float) this.getBlockHeight(chunkMd, x, null, z, null, null).intValue();
                BlockMD blockMD = BlockMD.getBlockMDFromChunkLocal(chunkMd, x, (int) h, z);
                boolean isWater = false;
                double contourInterval;
                if (!blockMD.isWater() && !blockMD.isIce() && (!this.mapBathymetry || this.getFluidHeights(chunkMd, null)[z][x] == null)) {
                    contourInterval = this.landContourInterval;
                } else {
                    isWater = true;
                    contourInterval = this.waterContourInterval;
                }
                float[] heights = new float[this.primarySlopeOffsets.size()];
                Float lastOffsetHeight = null;
                boolean flatOffsets = true;
                boolean isShore = false;
                for (int i = 0; i < heights.length; i++) {
                    BlockCoordIntPair offset = (BlockCoordIntPair) this.primarySlopeOffsets.get(i);
                    float offsetHeight = (float) this.getOffsetBlockHeight(chunkMd, x, null, z, null, null, offset, (int) h);
                    if (isWater && !isShore) {
                        ChunkMD targetChunkMd = this.getOffsetChunk(chunkMd, x, z, offset);
                        int newX = (chunkMd.getCoord().x << 4) + x + offset.x & 15;
                        int newZ = (chunkMd.getCoord().z << 4) + z + offset.z & 15;
                        if (targetChunkMd != null) {
                            if (this.mapBathymetry && this.mapBathymetry && this.getFluidHeights(chunkMd, null)[z][x] == null) {
                                isShore = true;
                            } else {
                                int ceiling = targetChunkMd.ceiling(newX, newZ);
                                BlockMD offsetBlock = targetChunkMd.getBlockMD(newX, ceiling, newZ);
                                if (!offsetBlock.isWater() && !offsetBlock.isIce()) {
                                    isShore = true;
                                }
                            }
                        }
                    }
                    offsetHeight = (float) Math.max((double) nearZero, (double) offsetHeight - (double) offsetHeight % contourInterval);
                    heights[i] = offsetHeight;
                    if (lastOffsetHeight == null) {
                        lastOffsetHeight = offsetHeight;
                    } else if (flatOffsets) {
                        flatOffsets = lastOffsetHeight == offsetHeight;
                    }
                }
                if (isWater) {
                    this.getShore(chunkMd)[z][x] = isShore;
                }
                h = (float) Math.max((double) nearZero, (double) h - (double) h % contourInterval);
                Float slope;
                if (flatOffsets) {
                    slope = 1.0F;
                } else {
                    slope = 0.0F;
                    for (float offsetHeightx : heights) {
                        slope = slope + h / offsetHeightx;
                    }
                    slope = slope / (float) heights.length;
                }
                if (slope.isNaN() || slope.isInfinite()) {
                    slope = 1.0F;
                }
                slopes[x][z] = slope;
            }
        }
        return slopes;
    }

    @Override
    public int getBlockHeight(ChunkMD chunkMd, BlockPos blockPos) {
        return chunkMd.getPrecipitationHeight(blockPos);
    }

    protected boolean paintContour(NativeImage chunkImage, RegionData regionData, CompoundTag blockNbt, ChunkMD chunkMd, BlockMD topBlockMd, int x, int y, int z) {
        if (!chunkMd.hasChunk()) {
            return false;
        } else {
            try {
                float slope = this.getSlope(chunkMd, x, null, z);
                boolean isWater = topBlockMd.isWater() || topBlockMd.isIce();
                Integer color;
                if (slope > 1.0F && this.waterContourColor != null && this.landContourColor != null) {
                    color = isWater ? this.waterContourColor : this.landContourColor;
                } else if (topBlockMd.isLava()) {
                    color = topBlockMd.getTextureColor();
                } else if (isWater) {
                    if (this.getShore(chunkMd)[z][x] == Boolean.TRUE && this.waterContourColor != null) {
                        color = this.waterContourColor;
                    } else {
                        int index = (int) Math.floor(((double) y - (double) y % this.waterContourInterval) / this.waterContourInterval);
                        index = Math.max(0, Math.min(index, this.waterPaletteRange));
                        color = this.waterPalette[index];
                        if (slope < 1.0F) {
                            color = RGB.adjustBrightness(color, 0.9F);
                        }
                    }
                } else {
                    int index = (int) Math.floor(((double) y - (double) y % this.landContourInterval) / this.landContourInterval);
                    index = Math.max(0, Math.min(index, this.landPaletteRange));
                    color = this.landPalette[index];
                    if (slope < 1.0F && this.waterContourColor != null && this.landContourColor != null) {
                        color = RGB.adjustBrightness(color, 0.85F);
                    }
                }
                int blockColor = this.paintBlock(chunkImage, x, z, color);
                regionData.setBlockState(blockNbt, chunkMd, chunkMd.getBlockPos(x, y, z));
                regionData.setBlockColor(blockNbt, blockColor, MapType.Name.topo);
            } catch (Exception var13) {
                var13.printStackTrace();
            }
            return true;
        }
    }

    protected final Boolean[][] getShore(ChunkMD chunkMd) {
        return chunkMd.getBlockDataBooleans(this.getCurrentMapType()).get("isShore");
    }

    protected final boolean hasShore(ChunkMD chunkMd) {
        return chunkMd.getBlockDataBooleans(this.getCurrentMapType()).has("isShore");
    }

    protected final void resetShore(ChunkMD chunkMd) {
        chunkMd.getBlockDataBooleans(this.getCurrentMapType()).clear("isShore");
    }
}