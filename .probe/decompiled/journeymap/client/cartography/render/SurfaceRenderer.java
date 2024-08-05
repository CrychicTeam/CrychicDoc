package journeymap.client.cartography.render;

import com.mojang.blaze3d.platform.NativeImage;
import journeymap.client.cartography.IChunkRenderer;
import journeymap.client.cartography.Strata;
import journeymap.client.cartography.Stratum;
import journeymap.client.cartography.color.RGB;
import journeymap.client.log.JMLogger;
import journeymap.client.log.StatTimer;
import journeymap.client.model.BlockCoordIntPair;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.MapType;
import journeymap.client.texture.ComparableNativeImage;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.nbt.RegionData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class SurfaceRenderer extends BaseRenderer implements IChunkRenderer {

    protected StatTimer renderSurfaceTimer = StatTimer.get("SurfaceRenderer.renderSurface");

    protected StatTimer renderSurfacePrepassTimer = StatTimer.get("SurfaceRenderer.renderSurface.CavePrepass");

    protected Strata strata = new Strata("Surface", 40, 8, false);

    protected float maxDepth = 8.0F;

    public SurfaceRenderer() {
        this.updateOptions(null, null);
    }

    @Override
    protected boolean updateOptions(ChunkMD chunkMd, MapType mapType) {
        if (super.updateOptions(chunkMd, mapType)) {
            this.ambientColor = RGB.floats(this.tweakSurfaceAmbientColor);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getBlockHeight(ChunkMD chunkMd, BlockPos blockPos) {
        Integer y = this.getBlockHeight(chunkMd, blockPos.m_123341_() & 15, null, blockPos.m_123343_() & 15, null, null);
        return y == null ? blockPos.m_123342_() : y;
    }

    @Override
    public boolean render(ComparableNativeImage dayChunkImage, RegionData regionData, ChunkMD chunkMd, Integer ignored) {
        return this.render(dayChunkImage, null, regionData, chunkMd, null, false);
    }

    public boolean render(ComparableNativeImage dayChunkImage, NativeImage nightChunkImage, RegionData regionData, ChunkMD chunkMd) {
        return this.render(dayChunkImage, nightChunkImage, regionData, chunkMd, null, false);
    }

    public synchronized boolean render(ComparableNativeImage dayChunkImage, NativeImage nightChunkImage, RegionData regionData, ChunkMD chunkMd, Integer vSlice, boolean cavePrePass) {
        StatTimer timer = cavePrePass ? this.renderSurfacePrepassTimer : this.renderSurfaceTimer;
        boolean var9;
        try {
            timer.start();
            this.updateOptions(chunkMd, MapType.from(MapType.Name.surface, null, chunkMd.getDimension()));
            if (!this.hasSlopes(chunkMd, vSlice)) {
                this.populateSlopes(chunkMd, vSlice, this.getSlopes(chunkMd, vSlice));
            }
            return this.renderSurface(dayChunkImage, nightChunkImage, regionData, chunkMd, vSlice, cavePrePass);
        } catch (Throwable var13) {
            JMLogger.throwLogOnce("Chunk Error", var13);
            var9 = false;
        } finally {
            this.strata.reset();
            timer.stop();
        }
        return var9;
    }

    protected boolean renderSurface(NativeImage dayChunkImage, NativeImage nightChunkImage, RegionData regionData, ChunkMD chunkMd, Integer vSlice, boolean cavePrePass) {
        boolean chunkOk = false;
        try {
            int sliceMaxY = 0;
            if (cavePrePass) {
                int[] sliceBounds = this.getVSliceBounds(chunkMd, vSlice);
                sliceMaxY = sliceBounds[1];
            }
            CompoundTag chunkNbt = regionData.getChunkNbt(chunkMd.getCoord());
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    CompoundTag blockNbt = regionData.getBlockDataFromBlockPos(chunkMd.getCoord(), chunkNbt, x, z);
                    this.strata.reset();
                    int blockHeight = this.getBlockHeight(chunkMd, x, null, z, null, null);
                    int upperY = Math.max(chunkMd.getMinY(), chunkMd.getPrecipitationHeight(x, z));
                    int lowerY = Math.max(chunkMd.getMinY(), blockHeight);
                    BlockMD blockMd = chunkMd.getBlockMD(x, blockHeight, z);
                    if ((upperY == chunkMd.getMinY() || lowerY < chunkMd.getMinY()) && blockMd.getBlockState().m_60795_()) {
                        int dayVoid = this.paintVoidBlock(dayChunkImage, x, z);
                        regionData.setBlockColor(blockNbt, dayVoid, MapType.Name.day);
                        if (!cavePrePass && nightChunkImage != null) {
                            int nightVoid = this.paintVoidBlock(nightChunkImage, x, z);
                            regionData.setBlockColor(blockNbt, nightVoid, MapType.Name.night);
                        }
                        chunkOk = true;
                    } else if (cavePrePass && upperY > sliceMaxY && (float) (upperY - sliceMaxY) > this.maxDepth) {
                        chunkOk = true;
                        this.paintBlackBlock(dayChunkImage, x, z);
                    } else {
                        boolean showSlope = !chunkMd.getBlockMD(x, lowerY, z).hasNoShadow();
                        if (this.mapBathymetry) {
                            Integer[][] waterHeights = this.getFluidHeights(chunkMd, null);
                            Integer waterHeight = waterHeights[z][x];
                            if (waterHeight != null) {
                                upperY = waterHeight;
                            }
                        }
                        this.buildStrata(this.strata, upperY, chunkMd, regionData, blockNbt, x, lowerY, z);
                        BlockPos pos = chunkMd.getBlockPos(x, upperY, z);
                        regionData.setBiome(blockNbt, chunkMd.getBiome(pos));
                        regionData.setY(blockNbt, this.strata.getTopY() == null ? regionData.getTopY(pos) : this.strata.getTopY());
                        chunkOk = this.paintStrata(this.strata, dayChunkImage, nightChunkImage, regionData, blockNbt, chunkMd, x, z, showSlope, cavePrePass) || chunkOk;
                    }
                }
            }
            regionData.writeChunk(chunkMd.getCoord(), chunkNbt);
        } catch (Throwable var23) {
            Journeymap.getLogger().warn("Error rendering surface: ", var23);
        } finally {
            this.strata.reset();
        }
        return chunkOk;
    }

    public int getSurfaceBlockHeight(ChunkMD chunkMd, int x, int z, BlockCoordIntPair offset, int defaultVal) {
        ChunkMD targetChunkMd = this.getOffsetChunk(chunkMd, x, z, offset);
        int newX = (chunkMd.getCoord().x << 4) + x + offset.x & 15;
        int newZ = (chunkMd.getCoord().z << 4) + z + offset.z & 15;
        if (targetChunkMd != null) {
            Integer height = this.getBlockHeight(targetChunkMd, newX, null, newZ, null, null);
            return height == null ? defaultVal : height;
        } else {
            return defaultVal;
        }
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
                if (y.equals(chunkMd.getMinY())) {
                    return chunkMd.getMinY();
                } else {
                    boolean setFluidHeight = true;
                    try {
                        while (y > chunkMd.getMinY()) {
                            BlockMD blockMD = BlockMD.getBlockMDFromChunkLocal(chunkMd, localX, y, localZ);
                            if (blockMD.isIgnore()) {
                                y = y - 1;
                            } else if (!blockMD.isWater() && !blockMD.isFluid()) {
                                if (!blockMD.hasTransparency() || !this.mapTransparency) {
                                    if (!blockMD.isLava() && blockMD.hasNoShadow()) {
                                        y = y - 1;
                                    }
                                    break;
                                }
                                y = y - 1;
                            } else {
                                if (!this.mapBathymetry) {
                                    break;
                                }
                                if (setFluidHeight) {
                                    this.getFluidHeights(chunkMd, null)[localZ][localX] = y;
                                    setFluidHeight = false;
                                }
                                y = y - 1;
                            }
                        }
                    } catch (Exception var12) {
                        Journeymap.getLogger().warn(String.format("Couldn't get safe surface block height for %s coords %s,%s: %s", chunkMd, localX, localZ, LogFormatter.toString(var12)));
                    }
                    y = Math.max(chunkMd.getMinY(), y);
                    heights[localX][localZ] = y;
                    return y;
                }
            }
        }
    }

    protected void buildStrata(Strata strata, int upperY, ChunkMD chunkMd, RegionData regionData, CompoundTag blockNbt, int x, int lowerY, int z) {
        while (upperY > lowerY) {
            BlockMD topBlockMd = BlockMD.getBlockMDFromChunkLocal(chunkMd, x, upperY, z);
            if (!topBlockMd.isIgnore()) {
                if (topBlockMd.hasTransparency()) {
                    strata.push(chunkMd, topBlockMd, x, upperY, z);
                    if (!this.mapTransparency) {
                        break;
                    }
                }
                if (topBlockMd.hasNoShadow()) {
                    lowerY = upperY;
                    break;
                }
            }
            upperY--;
        }
        if (this.mapTransparency || strata.isEmpty()) {
            for (; lowerY >= chunkMd.getMinY() && !((float) (upperY - lowerY) >= this.maxDepth); lowerY--) {
                BlockMD bottomBlockMd = BlockMD.getBlockMDFromChunkLocal(chunkMd, x, lowerY, z);
                if (!bottomBlockMd.isIgnore()) {
                    strata.push(chunkMd, bottomBlockMd, x, lowerY, z);
                    if (!bottomBlockMd.hasTransparency() || !this.mapTransparency) {
                        break;
                    }
                }
            }
        }
        regionData.setBlockState(blockNbt, chunkMd, chunkMd.getBlockPos(x, upperY, z));
        regionData.setBlockState(blockNbt, chunkMd, chunkMd.getBlockPos(x, lowerY, z));
    }

    protected boolean paintStrata(Strata strata, NativeImage dayChunkImage, NativeImage nightChunkImage, RegionData regionData, CompoundTag blockNbt, ChunkMD chunkMd, int x, int z, boolean showSlope, boolean cavePrePass) {
        Integer y = strata.getTopY();
        if (!strata.isEmpty() && y != null) {
            try {
                while (!strata.isEmpty()) {
                    Stratum stratum = strata.nextUp(this, true);
                    if (strata.getRenderDayColor() != null && strata.getRenderNightColor() != null) {
                        strata.setRenderDayColor(RGB.blendWith(strata.getRenderDayColor(), stratum.getDayColor(), stratum.getBlockMD().getAlpha()));
                        if (!cavePrePass) {
                            strata.setRenderNightColor(RGB.blendWith(strata.getRenderNightColor(), stratum.getNightColor(), stratum.getBlockMD().getAlpha()));
                        }
                    } else {
                        strata.setRenderDayColor(stratum.getDayColor());
                        if (!cavePrePass) {
                            strata.setRenderNightColor(stratum.getNightColor());
                        }
                    }
                    strata.release(stratum);
                }
                if (strata.getRenderDayColor() == null) {
                    this.paintBadBlock(dayChunkImage, x, y, z);
                    this.paintBadBlock(nightChunkImage, x, y, z);
                    return false;
                } else if (nightChunkImage != null && strata.getRenderNightColor() == null) {
                    this.paintBadBlock(nightChunkImage, x, y, z);
                    return false;
                } else {
                    if (showSlope) {
                        float slope = this.getSlope(chunkMd, x, null, z);
                        if (slope != 1.0F) {
                            strata.setRenderDayColor(RGB.bevelSlope(strata.getRenderDayColor(), slope));
                            if (!cavePrePass) {
                                strata.setRenderNightColor(RGB.bevelSlope(strata.getRenderNightColor(), slope));
                            }
                        }
                    }
                    int dayColor = this.paintBlock(dayChunkImage, x, z, strata.getRenderDayColor());
                    regionData.setBlockColor(blockNbt, dayColor, MapType.Name.day);
                    if (nightChunkImage != null) {
                        int nightColor = this.paintBlock(nightChunkImage, x, z, strata.getRenderNightColor());
                        regionData.setBlockColor(blockNbt, nightColor, MapType.Name.night);
                    }
                    return true;
                }
            } catch (RuntimeException var15) {
                throw var15;
            }
        } else {
            y = 0;
            if (dayChunkImage != null) {
                this.paintBadBlock(dayChunkImage, x, y, z);
            }
            if (nightChunkImage != null) {
                this.paintBadBlock(nightChunkImage, x, y, z);
            }
            return false;
        }
    }
}