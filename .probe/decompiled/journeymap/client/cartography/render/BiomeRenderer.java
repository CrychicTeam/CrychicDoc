package journeymap.client.cartography.render;

import com.mojang.blaze3d.platform.NativeImage;
import journeymap.client.cartography.IChunkRenderer;
import journeymap.client.cartography.color.RGB;
import journeymap.client.log.JMLogger;
import journeymap.client.log.StatTimer;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.MapType;
import journeymap.client.texture.ComparableNativeImage;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.nbt.RegionData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.biome.Biome;
import org.apache.logging.log4j.Level;

public class BiomeRenderer extends SurfaceRenderer implements IChunkRenderer {

    private static final String DEFAULT_LAND_CONTOUR_COLOR = "#3F250B";

    protected StatTimer renderTopoTimer = StatTimer.get("BiomeRenderer.renderSurface");

    @Override
    public boolean render(ComparableNativeImage chunkImage, RegionData regionData, ChunkMD chunkMd, Integer vSlice) {
        StatTimer timer = this.renderTopoTimer;
        boolean var7;
        try {
            timer.start();
            this.updateOptions(chunkMd, MapType.from(MapType.Name.biome, null, chunkMd.getDimension()));
            return this.renderSurface(chunkImage, regionData, chunkMd, vSlice, false);
        } catch (Throwable var11) {
            JMLogger.throwLogOnce("Chunk Error", var11);
            var7 = false;
        } finally {
            timer.stop();
        }
        return var7;
    }

    protected boolean renderSurface(NativeImage chunkImage, RegionData regionData, ChunkMD chunkMd, Integer vSlice, boolean cavePrePass) {
        boolean chunkOk = false;
        try {
            CompoundTag chunkNbt = regionData.getChunkNbt(chunkMd.getCoord());
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    CompoundTag blockNbt = regionData.getBlockDataFromBlockPos(chunkMd.getCoord(), chunkNbt, x, z);
                    BlockMD topBlockMd = null;
                    int y = Math.max(0, this.getBlockHeight(chunkMd, x, null, z, null, null));
                    topBlockMd = chunkMd.getBlockMD(x, y, z);
                    if (topBlockMd == null) {
                        this.paintBadBlock(chunkImage, x, y, z);
                    } else {
                        chunkOk = this.paintBiome(chunkImage, regionData, blockNbt, chunkMd, topBlockMd, x, y, z) || chunkOk;
                    }
                }
            }
            regionData.writeChunk(chunkMd.getCoord(), chunkNbt);
        } catch (Throwable var13) {
            Journeymap.getLogger().log(Level.WARN, "Error in renderSurface: " + LogFormatter.toString(var13));
        }
        return chunkOk;
    }

    private boolean paintBiome(NativeImage chunkImage, RegionData regionData, CompoundTag blockNbt, ChunkMD chunkMd, BlockMD topBlockMd, int x, int y, int z) {
        if (!chunkMd.hasChunk()) {
            return false;
        } else {
            BlockPos blockPos = chunkMd.getBlockPos(x, y, z);
            Biome biome = chunkMd.getBiome(blockPos);
            if (biome == null) {
                return false;
            } else {
                try {
                    boolean isWater = topBlockMd.isWater() || topBlockMd.isIce();
                    boolean isFoliage = topBlockMd.isFoliage();
                    int contourColor = RGB.hexToInt("#3F250B");
                    Integer color;
                    if (this.isBiomeEdge(chunkMd, blockPos, biome)) {
                        color = contourColor;
                    } else if (topBlockMd.isLava()) {
                        color = topBlockMd.getTextureColor();
                    } else if (isWater) {
                        color = biome.getWaterColor();
                    } else if (isFoliage) {
                        color = biome.getFoliageColor();
                    } else {
                        color = biome.getGrassColor((double) chunkMd.getCoord().x, (double) chunkMd.getCoord().z);
                    }
                    int blockColor = this.paintBlock(chunkImage, x, z, color);
                    regionData.setBlockColor(blockNbt, blockColor, MapType.Name.biome);
                } catch (Exception var16) {
                    var16.printStackTrace();
                }
                return true;
            }
        }
    }

    private boolean isBiomeEdge(ChunkMD chunkMD, BlockPos pos, Biome blockBiome) {
        return false;
    }
}