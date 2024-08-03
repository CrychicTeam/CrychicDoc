package journeymap.client.mod.vanilla;

import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import journeymap.client.mod.IBlockColorProxy;
import journeymap.client.mod.ModBlockDelegate;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MaterialBlockColorProxy implements IBlockColorProxy {

    @Nullable
    @Override
    public int deriveBlockColor(BlockMD blockMD, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        return ModBlockDelegate.INSTANCE.getDefaultBlockColorProxy().deriveBlockColor(blockMD, chunkMD, blockPos);
    }

    @Override
    public int getBlockColor(ChunkMD chunkMD, BlockMD blockMD, BlockPos blockPos) {
        int color = Minecraft.getInstance().getBlockColors().getColor(blockMD.getBlockState(), chunkMD.getWorld(), blockPos);
        if (color == -1) {
            BlockState blockState = blockMD.getBlockState();
            ClientLevel level = Minecraft.getInstance().level;
            return blockState.m_284242_(level, blockPos).col;
        } else {
            return color;
        }
    }
}