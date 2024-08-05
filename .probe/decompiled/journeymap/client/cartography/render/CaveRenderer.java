package journeymap.client.cartography.render;

import com.mojang.blaze3d.platform.NativeImage;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.IChunkRenderer;
import journeymap.client.cartography.Strata;
import journeymap.client.cartography.Stratum;
import journeymap.client.cartography.color.RGB;
import journeymap.client.log.JMLogger;
import journeymap.client.log.StatTimer;
import journeymap.client.model.BlockFlag;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionImageCache;
import journeymap.client.model.RegionImageSet;
import journeymap.client.texture.ComparableNativeImage;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.nbt.RegionData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class CaveRenderer extends BaseRenderer implements IChunkRenderer {

    protected SurfaceRenderer surfaceRenderer;

    protected StatTimer renderCaveTimer = StatTimer.get("CaveRenderer.render");

    protected Strata strata = new Strata("Cave", 40, 8, true);

    protected float defaultDim = 0.2F;

    protected boolean mapSurfaceAboveCaves;

    protected boolean clearBlacks;

    RegionData regionData;

    public CaveRenderer(SurfaceRenderer surfaceRenderer) {
        this.surfaceRenderer = surfaceRenderer;
        this.updateOptions(null, null);
        this.shadingSlopeMin = 0.2F;
        this.shadingSlopeMax = 1.1F;
        this.shadingPrimaryDownslopeMultiplier = 0.7F;
        this.shadingPrimaryUpslopeMultiplier = 1.05F;
        this.shadingSecondaryDownslopeMultiplier = 0.99F;
        this.shadingSecondaryUpslopeMultiplier = 1.01F;
    }

    @Override
    protected boolean updateOptions(ChunkMD chunkMd, MapType mapType) {
        if (super.updateOptions(chunkMd, mapType)) {
            this.mapSurfaceAboveCaves = JourneymapClient.getInstance().getCoreProperties().mapSurfaceAboveCaves.get();
            this.clearBlacks = JourneymapClient.getInstance().getCoreProperties().caveBlackAsClear.get();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getBlockHeight(ChunkMD chunkMd, BlockPos blockPos) {
        if (chunkMd.fromNbt()) {
            return chunkMd.getPrecipitationHeight(blockPos.m_123341_(), blockPos.m_123343_());
        } else {
            Integer vSlice = blockPos.m_123342_() >> 4;
            int[] sliceBounds = this.getVSliceBounds(chunkMd, vSlice);
            int sliceMinY = sliceBounds[0];
            int sliceMaxY = sliceBounds[1];
            Integer y = this.getBlockHeight(chunkMd, blockPos.m_123341_() & 15, vSlice, blockPos.m_123343_() & 15, sliceMinY, sliceMaxY);
            return y == null ? blockPos.m_123342_() : y;
        }
    }

    @Override
    public synchronized boolean render(ComparableNativeImage chunkImage, RegionData regionData, ChunkMD chunkMd, Integer vSlice) {
        if (vSlice == null) {
            Journeymap.getLogger().warn("ChunkOverworldCaveRenderer is for caves. vSlice can't be null");
            return false;
        } else {
            this.regionData = regionData;
            this.updateOptions(chunkMd, MapType.underground(vSlice, chunkMd.getDimension()));
            this.renderCaveTimer.start();
            boolean success;
            try {
                if (!this.hasSlopes(chunkMd, vSlice)) {
                    this.populateSlopes(chunkMd, vSlice, this.getSlopes(chunkMd, vSlice));
                }
                NativeImage chunkSurfaceImage = null;
                if (this.mapSurfaceAboveCaves) {
                    MapType mapType = MapType.day(chunkMd.getDimension());
                    RegionImageSet ris = RegionImageCache.INSTANCE.getRegionImageSet(chunkMd, mapType);
                    if (ris != null && ris.getHolder(mapType).hasTexture()) {
                        chunkSurfaceImage = ris.getChunkImage(chunkMd, mapType);
                    }
                }
                success = this.renderUnderground(chunkSurfaceImage, chunkImage, regionData, chunkMd, vSlice);
                if (chunkSurfaceImage != null) {
                    chunkSurfaceImage.close();
                }
                return success;
            } catch (Throwable var11) {
                JMLogger.throwLogOnce("Chunk Error", var11);
                success = false;
            } finally {
                this.renderCaveTimer.stop();
            }
            return success;
        }
    }

    protected void mask(NativeImage chunkSurfaceImage, NativeImage chunkImage, RegionData regionData, CompoundTag blockNbt, ChunkMD chunkMd, int x, int y, int z) {
        if (chunkSurfaceImage != null && this.mapSurfaceAboveCaves) {
            BlockPos pos = chunkMd.getBlockPos(x, y, z);
            int surfaceY = Math.max(chunkMd.getMinY(), chunkMd.getHeight(new BlockPos(x, y, z)));
            int distance = Math.max(0, surfaceY - y);
            if (distance > 16) {
                int minY = this.getBlockHeight(chunkMd, new BlockPos(x, y, z));
                BlockMD blockMD = chunkMd.getBlockMD(pos);
                boolean isAir = blockMD.getBlockState().m_60795_();
                if (y > chunkMd.getMinY() && minY > chunkMd.getMinY() || !isAir && y == chunkMd.getMinY() && minY == chunkMd.getMinY()) {
                    if (this.clearBlacks) {
                        this.paintClearBlock(chunkImage, x, z);
                    } else {
                        this.paintBlackBlock(chunkImage, x, z);
                    }
                } else {
                    int var15 = this.paintVoidBlock(chunkImage, x, z);
                }
            } else {
                int var16 = this.paintDimOverlay(chunkSurfaceImage, chunkImage, x, z, 0.5F);
            }
        } else if (this.clearBlacks) {
            this.paintClearBlock(chunkImage, x, z);
        } else {
            this.paintBlackBlock(chunkImage, x, z);
        }
        regionData.setBlockState(blockNbt, chunkMd, chunkMd.getBlockPos(x, y, z));
    }

    protected boolean renderUnderground(NativeImage chunkSurfaceImage, NativeImage chunkSliceImage, RegionData regionData, ChunkMD chunkMd, int vSlice) {
        int[] sliceBounds = this.getVSliceBounds(chunkMd, Integer.valueOf(vSlice));
        int sliceMinY = sliceBounds[0];
        int sliceMaxY = sliceBounds[1];
        boolean chunkOk = false;
        CompoundTag chunkNbt = regionData.getChunkNbt(chunkMd.getCoord());
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                try {
                    this.strata.reset();
                    CompoundTag blockNbt = regionData.getBlockDataFromBlockPos(chunkMd.getCoord(), chunkNbt, x, z);
                    regionData.setSurfaceY(blockNbt, chunkMd.getHeight(new BlockPos(x, 0, z)));
                    int ceiling = chunkMd.fromNbt() ? chunkMd.getPrecipitationHeight(x, z) : this.getBlockHeight(chunkMd, x, vSlice, z, sliceMinY, sliceMaxY);
                    if (ceiling < chunkMd.getMinY()) {
                        int voidColor = this.paintVoidBlock(chunkSliceImage, x, z);
                        chunkOk = true;
                    } else {
                        int y = Math.min(ceiling, sliceMaxY);
                        this.buildStrata(this.strata, regionData, blockNbt, sliceMinY - 1, chunkMd, x, y, z);
                        if (this.strata.isEmpty() && !chunkMd.fromNbt()) {
                            this.mask(chunkSurfaceImage, chunkSliceImage, regionData, blockNbt, chunkMd, x, y, z);
                            chunkOk = true;
                        } else {
                            chunkOk = this.paintStrata(this.strata, chunkSliceImage, regionData, blockNbt, chunkMd, vSlice, x, ceiling, z) || chunkOk;
                        }
                    }
                } catch (Throwable var17) {
                    this.paintBadBlock(chunkSliceImage, x, vSlice, z);
                    String error = "CaveRenderer error at x,vSlice,z = " + x + "," + vSlice + "," + z + " : " + LogFormatter.toString(var17);
                    Journeymap.getLogger().error(error);
                }
            }
        }
        regionData.writeChunk(chunkMd.getCoord(), chunkNbt);
        this.strata.reset();
        return chunkOk;
    }

    protected void buildStrata(Strata strata, RegionData regionData, CompoundTag blockNbt, int minY, ChunkMD chunkMd, int x, int topY, int z) {
        BlockMD blockMD = null;
        BlockMD blockAboveMD = null;
        BlockMD lavaBlockMD = null;
        try {
            int y;
            for (y = this.getBlockHeight(chunkMd, x, topY >> 4, z, minY, topY); y >= chunkMd.getMinY(); y--) {
                blockMD = BlockMD.getBlockMDFromChunkLocal(chunkMd, x, y, z);
                if (!blockMD.isIgnore() && !blockMD.hasFlag(BlockFlag.OpenToSky)) {
                    strata.setBlocksFound(true);
                    blockAboveMD = BlockMD.getBlockMDFromChunkLocal(chunkMd, x, y + 1, z);
                    if (blockMD.isLava() && blockAboveMD.isLava()) {
                        lavaBlockMD = blockMD;
                    }
                    if (!blockAboveMD.isIgnore() && !blockAboveMD.hasFlag(BlockFlag.OpenToSky) && !chunkMd.fromNbt()) {
                        if (strata.isEmpty() && y < minY) {
                            break;
                        }
                    } else if (chunkMd.hasNoSky() || !chunkMd.canBlockSeeTheSky(x, y + 1, z)) {
                        int lightLevel = this.getSliceLightLevel(chunkMd, x, y, z, true);
                        if (lightLevel > 0) {
                            strata.push(chunkMd, blockMD, x, y, z, lightLevel);
                            BlockPos pos = chunkMd.getBlockPos(x, strata.getTopY(), z);
                            regionData.setY(blockNbt, strata.getTopY());
                            regionData.setBlockState(blockNbt, chunkMd, pos);
                            regionData.setLightValue(blockNbt, lightLevel);
                            if (!blockMD.hasTransparency() || !this.mapTransparency) {
                                break;
                            }
                        } else if (y < minY) {
                            break;
                        }
                    }
                }
            }
            regionData.setBiome(blockNbt, chunkMd.getBiome(chunkMd.getBlockPos(z, y, z)));
        } finally {
            if (strata.isEmpty() && lavaBlockMD != null && chunkMd.getWorld().m_46472_().equals(Level.END)) {
                strata.push(chunkMd, lavaBlockMD, x, topY, z, 14);
                BlockPos pos = chunkMd.getBlockPos(x, strata.getTopY(), z);
                regionData.setBlockState(blockNbt, chunkMd, pos);
                regionData.setY(blockNbt, strata.getTopY());
            }
        }
    }

    protected boolean paintStrata(Strata strata, NativeImage chunkSliceImage, RegionData regionData, CompoundTag blockNbt, ChunkMD chunkMd, Integer vSlice, int x, int y, int z) {
        if (strata.isEmpty() && !chunkMd.fromNbt()) {
            this.paintBadBlock(chunkSliceImage, x, y, z);
            return false;
        } else {
            try {
                if (chunkMd.fromNbt() && strata.isEmpty()) {
                    this.paintBlackBlock(chunkSliceImage, x, z);
                    return true;
                } else {
                    Stratum stratum = null;
                    BlockMD blockMD = null;
                    while (!strata.isEmpty()) {
                        stratum = strata.nextUp(this, true);
                        if (strata.getRenderCaveColor() == null) {
                            strata.setRenderCaveColor(stratum.getCaveColor());
                        } else {
                            strata.setRenderCaveColor(RGB.blendWith(strata.getRenderCaveColor(), stratum.getCaveColor(), stratum.getBlockMD().getAlpha()));
                        }
                        blockMD = stratum.getBlockMD();
                        strata.release(stratum);
                    }
                    if (strata.getRenderCaveColor() == null) {
                        this.paintBadBlock(chunkSliceImage, x, y, z);
                        return false;
                    } else {
                        if (blockMD != null && !blockMD.hasNoShadow()) {
                            float slope = this.getSlope(chunkMd, x, vSlice, z);
                            if (slope != 1.0F) {
                                strata.setRenderCaveColor(RGB.bevelSlope(strata.getRenderCaveColor(), slope));
                            }
                        }
                        int var15 = this.paintBlock(chunkSliceImage, x, z, strata.getRenderCaveColor());
                        return true;
                    }
                }
            } catch (RuntimeException var13) {
                this.paintBadBlock(chunkSliceImage, x, y, z);
                throw var13;
            }
        }
    }

    @Override
    protected Integer getBlockHeight(ChunkMD chunkMd, int x, Integer vSlice, int z, Integer sliceMinY, Integer sliceMaxY) {
        Integer[][] blockSliceHeights = this.getHeights(chunkMd, vSlice);
        if (blockSliceHeights == null) {
            return null;
        } else {
            Integer y = blockSliceHeights[x][z];
            if (y != null) {
                return y;
            } else {
                try {
                    y = Math.min(chunkMd.getHeight(new BlockPos(x, chunkMd.getMinY(), z)), sliceMaxY) - 1;
                    if (y <= sliceMinY) {
                        return y;
                    }
                    if (y + 1 < sliceMaxY) {
                        while (y > chunkMd.getMinY() && y > sliceMinY) {
                            BlockMD blockMDAbove = BlockMD.getBlockMDFromChunkLocal(chunkMd, x, y + 1, z);
                            if (!blockMDAbove.isIgnore() && !blockMDAbove.hasFlag(BlockFlag.OpenToSky)) {
                                break;
                            }
                            y = y - 1;
                        }
                    }
                    BlockMD blockMDAbove = BlockMD.getBlockMDFromChunkLocal(chunkMd, x, y + 1, z);
                    BlockMD blockMD = BlockMD.getBlockMDFromChunkLocal(chunkMd, x, y, z);
                    boolean inAirPocket = false;
                    while (y > chunkMd.getMinY() && y > sliceMinY) {
                        if (this.mapBathymetry && blockMD.isWater()) {
                            y = y - 1;
                        }
                        inAirPocket = blockMD.isIgnore();
                        if ((blockMDAbove.isIgnore() || blockMDAbove.hasTransparency() || blockMDAbove.hasFlag(BlockFlag.OpenToSky)) && (!blockMD.isIgnore() || !blockMD.hasTransparency() || !blockMD.hasFlag(BlockFlag.OpenToSky))) {
                            break;
                        }
                        y = y - 1;
                        blockMD = BlockMD.getBlockMDFromChunkLocal(chunkMd, x, y, z);
                        blockMDAbove = BlockMD.getBlockMDFromChunkLocal(chunkMd, x, y + 1, z);
                        if (y < sliceMinY && !inAirPocket) {
                            break;
                        }
                    }
                } catch (Exception var13) {
                    Journeymap.getLogger().warn("Couldn't get safe slice block height at " + x + "," + z + ": " + var13);
                    y = sliceMaxY;
                }
                y = Math.max(chunkMd.getMinY(), y);
                blockSliceHeights[x][z] = y;
                return y;
            }
        }
    }

    protected int getSliceLightLevel(ChunkMD chunkMd, int x, int y, int z, boolean adjusted) {
        return this.mapCaveLighting ? chunkMd.getSavedLightValue(x, y + 1, z) : 15;
    }
}