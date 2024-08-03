package journeymap.client.mod.impl;

import javax.annotation.Nullable;
import journeymap.client.mod.IBlockColorProxy;
import journeymap.client.mod.IModBlockHandler;
import journeymap.client.mod.ModBlockDelegate;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@Deprecated
public class CodeChickenLibMod implements IModBlockHandler, IBlockColorProxy {

    @Override
    public void initialize(BlockMD blockMD) {
        blockMD.setBlockColorProxy(this);
    }

    @Override
    public int deriveBlockColor(BlockMD blockMD, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        return ModBlockDelegate.INSTANCE.getDefaultBlockColorProxy().deriveBlockColor(blockMD, chunkMD, blockPos);
    }

    @Override
    public int getBlockColor(ChunkMD chunkMD, BlockMD blockMD, BlockPos blockPos) {
        BlockState blockState = blockMD.getBlockState();
        try {
            blockState = chunkMD.getWorld().m_8055_(blockPos);
        } catch (Exception var6) {
        }
        return Minecraft.getInstance().getBlockColors().getColor(blockState, chunkMD.getWorld(), blockPos, blockMD.getBlockState().m_60799_().ordinal());
    }
}