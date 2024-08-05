package journeymap.client.mod.impl;

import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.List;
import journeymap.client.mod.IBlockColorProxy;
import journeymap.client.mod.IModBlockHandler;
import journeymap.client.mod.ModBlockDelegate;
import journeymap.client.model.BlockFlag;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import net.minecraft.core.BlockPos;

public class CreateMod implements IModBlockHandler, IBlockColorProxy {

    private final List<String> blocks = Arrays.asList("railway_casing", "bell", "valve_handle");

    @Override
    public void initialize(BlockMD blockMD) {
        String blockId = blockMD.getBlockId();
        for (String block : this.blocks) {
            if (blockId.contains(block)) {
                blockMD.setBlockColorProxy(this);
            }
        }
        if (blockMD.getBlockId().contains("fake_track")) {
            blockMD.addFlags(BlockFlag.Force);
            blockMD.setBlockColorProxy(this);
        }
    }

    @Nullable
    @Override
    public int deriveBlockColor(BlockMD blockMD, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        return ModBlockDelegate.INSTANCE.getDefaultBlockColorProxy().deriveBlockColor(blockMD, chunkMD, blockPos);
    }

    @Override
    public int getBlockColor(ChunkMD chunkMD, BlockMD blockMD, BlockPos blockPos) {
        return ModBlockDelegate.INSTANCE.getMaterialBlockColorProxy().getBlockColor(chunkMD, blockMD, blockPos);
    }
}