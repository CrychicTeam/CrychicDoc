package journeymap.client.mod.vanilla;

import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import journeymap.client.cartography.color.RGB;
import journeymap.client.mod.IBlockColorProxy;
import journeymap.client.mod.ModBlockDelegate;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import journeymap.client.world.JmBlockAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BedPart;

public enum BedBlockProxy implements IBlockColorProxy {

    INSTANCE;

    @Override
    public int deriveBlockColor(BlockMD blockMD, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        return ModBlockDelegate.INSTANCE.getDefaultBlockColorProxy().deriveBlockColor(blockMD, chunkMD, blockPos);
    }

    @Override
    public int getBlockColor(ChunkMD chunkMD, BlockMD blockMD, BlockPos blockPos) {
        if (blockMD.getBlock() instanceof BedBlock) {
            BlockEntity tileentity = JmBlockAccess.INSTANCE.getBlockEntity(blockPos);
            if (tileentity instanceof BedBlockEntity) {
                int bedColor = ((BedBlockEntity) tileentity).getColor().getMapColor().col;
                if (blockMD.getBlockState().m_61143_(BedBlock.PART) == BedPart.FOOT) {
                    return RGB.multiply(13421772, bedColor);
                }
                return RGB.multiply(16777215, bedColor);
            }
        }
        return ModBlockDelegate.INSTANCE.getDefaultBlockColorProxy().getBlockColor(chunkMD, blockMD, blockPos);
    }
}