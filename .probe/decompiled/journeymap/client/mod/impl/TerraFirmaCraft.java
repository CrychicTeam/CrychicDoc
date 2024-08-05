package journeymap.client.mod.impl;

import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import journeymap.client.mod.IBlockColorProxy;
import journeymap.client.mod.IModBlockHandler;
import journeymap.client.mod.ModBlockDelegate;
import journeymap.client.model.BlockFlag;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class TerraFirmaCraft implements IModBlockHandler, IBlockColorProxy {

    private static final int WATER_COLOR = 727360;

    @Override
    public void initialize(BlockMD blockMD) {
        blockMD.setBlockColorProxy(this);
        String name = blockMD.getBlockId().toLowerCase();
        if (name.contains("loose") || name.contains("looserock") || name.contains("loose_rock") || name.contains("rubble") || name.contains("vegetation")) {
            blockMD.addFlags(BlockFlag.Ignore, BlockFlag.NoShadow, BlockFlag.NoTopo);
        } else if (name.contains("seagrass")) {
            blockMD.addFlags(BlockFlag.Plant);
        } else if (name.contains("grass")) {
            blockMD.addFlags(BlockFlag.Grass);
        } else if (name.contains("water")) {
            blockMD.setAlpha(0.3F);
            blockMD.addFlags(BlockFlag.Water, BlockFlag.NoShadow);
        } else if (name.contains("leaves")) {
            blockMD.addFlags(BlockFlag.NoTopo, BlockFlag.Foliage);
        }
    }

    @Nullable
    @Override
    public int deriveBlockColor(BlockMD blockMD, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        return ModBlockDelegate.INSTANCE.getDefaultBlockColorProxy().deriveBlockColor(blockMD, chunkMD, blockPos);
    }

    @Override
    public int getBlockColor(ChunkMD chunkMD, BlockMD blockMD, BlockPos blockPos) {
        int color = Minecraft.getInstance().getBlockColors().getColor(blockMD.getBlockState(), chunkMD.getWorld(), blockPos);
        if (color == -1) {
            color = blockMD.getBlockState().m_284242_(chunkMD.getWorld(), blockPos).col;
        }
        return color;
    }
}