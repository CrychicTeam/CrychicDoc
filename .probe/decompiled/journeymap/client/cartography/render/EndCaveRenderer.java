package journeymap.client.cartography.render;

import journeymap.client.cartography.IChunkRenderer;
import journeymap.client.cartography.color.RGB;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.MapType;

public class EndCaveRenderer extends CaveRenderer implements IChunkRenderer {

    public EndCaveRenderer(SurfaceRenderer endSurfaceRenderer) {
        super(endSurfaceRenderer);
    }

    @Override
    protected boolean updateOptions(ChunkMD chunkMd, MapType mapType) {
        if (super.updateOptions(chunkMd, mapType)) {
            this.ambientColor = RGB.floats(this.tweakEndAmbientColor);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int getSliceLightLevel(ChunkMD chunkMd, int x, int y, int z, boolean adjusted) {
        return this.mapCaveLighting ? Math.max(adjusted ? (int) this.surfaceRenderer.tweakMoonlightLevel : 0, chunkMd.getSavedLightValue(x, y + 1, z)) : 15;
    }
}