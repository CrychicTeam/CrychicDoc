package journeymap.client.cartography;

import com.mojang.blaze3d.platform.NativeImage;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.render.BaseRenderer;
import journeymap.client.cartography.render.BiomeRenderer;
import journeymap.client.cartography.render.CaveRenderer;
import journeymap.client.cartography.render.EndCaveRenderer;
import journeymap.client.cartography.render.EndSurfaceRenderer;
import journeymap.client.cartography.render.NetherRenderer;
import journeymap.client.cartography.render.SurfaceRenderer;
import journeymap.client.cartography.render.TopoRenderer;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionCoord;
import journeymap.client.model.RegionImageCache;
import journeymap.client.model.RegionImageSet;
import journeymap.client.texture.ComparableNativeImage;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.nbt.RegionData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class ChunkRenderController {

    private final SurfaceRenderer overWorldSurfaceRenderer = new SurfaceRenderer();

    private final BaseRenderer netherRenderer;

    private final SurfaceRenderer endSurfaceRenderer;

    private final BaseRenderer endCaveRenderer;

    private final BaseRenderer topoRenderer;

    private final BaseRenderer overWorldCaveRenderer = new CaveRenderer(this.overWorldSurfaceRenderer);

    private final BaseRenderer biomeRenderer;

    public ChunkRenderController() {
        this.netherRenderer = new NetherRenderer();
        this.endSurfaceRenderer = new EndSurfaceRenderer();
        this.endCaveRenderer = new EndCaveRenderer(this.endSurfaceRenderer);
        this.topoRenderer = new TopoRenderer();
        this.biomeRenderer = new BiomeRenderer();
    }

    public BaseRenderer getRenderer(RegionCoord rCoord, MapType mapType, ChunkMD chunkMd) {
        try {
            RegionImageSet regionImageSet = RegionImageCache.INSTANCE.getRegionImageSet(rCoord);
            if (!mapType.isUnderground()) {
                return this.overWorldSurfaceRenderer;
            } else {
                try (NativeImage image = regionImageSet.getChunkImage(chunkMd, mapType)) {
                    if (image == null) {
                        return null;
                    } else if (Level.NETHER.equals(rCoord.dimension)) {
                        return this.netherRenderer;
                    } else {
                        return Level.END.equals(rCoord.dimension) ? this.endCaveRenderer : this.overWorldCaveRenderer;
                    }
                }
            }
        } catch (Throwable var10) {
            Journeymap.getLogger().error("Unexpected error in ChunkRenderController: " + LogFormatter.toPartialString(var10));
            return null;
        }
    }

    public boolean renderChunk(RegionCoord rCoord, MapType mapType, ChunkMD chunkMd, RegionData regionData) {
        Minecraft mc = Minecraft.getInstance();
        if (JourneymapClient.getInstance().isMapping() && chunkMd.getChunk().getLevel().dimension() == mc.player.m_9236_().dimension() && mc.player.m_9236_() == mc.level) {
            boolean renderOkay = false;
            try {
                RegionImageSet regionImageSet = RegionImageCache.INSTANCE.getRegionImageSet(rCoord);
                if (mapType.isUnderground()) {
                    try (ComparableNativeImage chunkSliceImage = regionImageSet.getChunkImage(chunkMd, mapType)) {
                        if (chunkSliceImage != null) {
                            if (Level.NETHER.equals(rCoord.dimension)) {
                                renderOkay = this.netherRenderer.render(chunkSliceImage, regionData, chunkMd, mapType.vSlice);
                            } else if (Level.END.equals(rCoord.dimension)) {
                                renderOkay = this.endCaveRenderer.render(chunkSliceImage, regionData, chunkMd, mapType.vSlice);
                            } else {
                                renderOkay = this.overWorldCaveRenderer.render(chunkSliceImage, regionData, chunkMd, mapType.vSlice);
                            }
                            if (renderOkay) {
                                regionImageSet.setChunkImage(chunkMd, mapType, chunkSliceImage);
                            }
                        }
                    }
                } else if (!Level.NETHER.equals(rCoord.dimension)) {
                    if (mapType.isTopo()) {
                        try (ComparableNativeImage imageTopo = regionImageSet.getChunkImage(chunkMd, MapType.topo(rCoord.dimension))) {
                            renderOkay = this.topoRenderer.render(imageTopo, regionData, chunkMd, null);
                            if (renderOkay) {
                                regionImageSet.setChunkImage(chunkMd, MapType.topo(rCoord.dimension), imageTopo);
                            }
                        }
                    } else if (mapType.isBiome()) {
                        try (ComparableNativeImage imageBiome = regionImageSet.getChunkImage(chunkMd, MapType.biome(rCoord.dimension))) {
                            renderOkay = this.biomeRenderer.render(imageBiome, regionData, chunkMd, null);
                            if (renderOkay) {
                                regionImageSet.setChunkImage(chunkMd, MapType.biome(rCoord.dimension), imageBiome);
                            }
                        }
                    } else {
                        try (ComparableNativeImage imageDay = regionImageSet.getChunkImage(chunkMd, MapType.day(rCoord.dimension));
                            ComparableNativeImage imageNight = regionImageSet.getChunkImage(chunkMd, MapType.night(rCoord.dimension))) {
                            renderOkay = this.overWorldSurfaceRenderer.render(imageDay, imageNight, regionData, chunkMd);
                            if (renderOkay) {
                                regionImageSet.setChunkImage(chunkMd, MapType.day(rCoord.dimension), imageDay);
                                regionImageSet.setChunkImage(chunkMd, MapType.night(rCoord.dimension), imageNight);
                            }
                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException var22) {
                Journeymap.getLogger().log(org.apache.logging.log4j.Level.WARN, LogFormatter.toString(var22));
                return false;
            } catch (Throwable var23) {
                Journeymap.getLogger().error("Unexpected error in ChunkRenderController: " + LogFormatter.toString(var23));
            }
            if (!renderOkay && Journeymap.getLogger().isDebugEnabled()) {
                Journeymap.getLogger().debug(String.format("Chunk %s render failed for %s", chunkMd.getCoord(), mapType));
            }
            return renderOkay;
        } else {
            return false;
        }
    }
}