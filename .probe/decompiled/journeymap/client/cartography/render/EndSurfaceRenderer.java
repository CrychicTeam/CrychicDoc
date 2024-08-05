package journeymap.client.cartography.render;

import journeymap.client.cartography.IChunkRenderer;
import journeymap.client.cartography.color.RGB;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.MapType;

public class EndSurfaceRenderer extends SurfaceRenderer implements IChunkRenderer {

    @Override
    protected boolean updateOptions(ChunkMD chunkMd, MapType mapType) {
        if (super.updateOptions(chunkMd, mapType)) {
            this.ambientColor = RGB.floats(this.tweakEndAmbientColor);
            this.tweakMoonlightLevel = 5.0F;
            return true;
        } else {
            return false;
        }
    }
}