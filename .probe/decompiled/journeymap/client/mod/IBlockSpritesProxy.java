package journeymap.client.mod;

import java.util.Collection;
import javax.annotation.Nullable;
import journeymap.client.cartography.color.ColoredSprite;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import net.minecraft.core.BlockPos;

public interface IBlockSpritesProxy {

    @Nullable
    Collection<ColoredSprite> getSprites(BlockMD var1, @Nullable ChunkMD var2, @Nullable BlockPos var3);
}