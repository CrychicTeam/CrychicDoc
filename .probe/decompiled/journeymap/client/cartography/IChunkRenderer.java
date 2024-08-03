package journeymap.client.cartography;

import journeymap.client.model.ChunkMD;
import journeymap.client.texture.ComparableNativeImage;
import journeymap.common.nbt.RegionData;

public interface IChunkRenderer {

    boolean render(ComparableNativeImage var1, RegionData var2, ChunkMD var3, Integer var4);

    void setStratumColors(Stratum var1, int var2, Integer var3, boolean var4, boolean var5, boolean var6);

    float[] getAmbientColor();
}