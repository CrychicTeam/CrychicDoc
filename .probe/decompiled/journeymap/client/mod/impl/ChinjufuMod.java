package journeymap.client.mod.impl;

import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Collections;
import journeymap.client.cartography.color.ColoredSprite;
import journeymap.client.mod.IBlockSpritesProxy;
import journeymap.client.mod.IModBlockHandler;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;

public class ChinjufuMod implements IModBlockHandler, IBlockSpritesProxy {

    BlockModelShaper bms = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();

    @Override
    public void initialize(BlockMD blockMD) {
        if (blockMD.getBlockId().contains("oakkare_leaf") || blockMD.getBlockId().contains("kaede_leaf")) {
            blockMD.setBlockSpritesProxy(this);
        }
    }

    @Nullable
    @Override
    public Collection<ColoredSprite> getSprites(BlockMD blockMD, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        BakedModel model = this.bms.getBlockModel(blockMD.getBlockState());
        return Collections.singletonList(new ColoredSprite(model.getParticleIcon(), null));
    }
}