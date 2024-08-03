package journeymap.client.mod;

import javax.annotation.Nullable;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import net.minecraft.core.BlockPos;

public interface IBlockColorProxy {

    @Nullable
    int deriveBlockColor(BlockMD var1, @Nullable ChunkMD var2, @Nullable BlockPos var3);

    int getBlockColor(ChunkMD var1, BlockMD var2, BlockPos var3);
}