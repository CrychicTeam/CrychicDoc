package journeymap.client.mod.impl;

import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import journeymap.client.mod.IBlockColorProxy;
import journeymap.client.mod.IModBlockHandler;
import journeymap.client.mod.ModBlockDelegate;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class ImmersiveRailroading implements IModBlockHandler, IBlockColorProxy {

    private static final int RAIL_COLOR = 14474460;

    private static final int RAIL_COLOR_PREVIEW = 8224125;

    @Override
    public void initialize(BlockMD blockMD) {
        blockMD.setBlockColorProxy(this);
        String name = blockMD.getBlockId().toLowerCase();
        if (name.equalsIgnoreCase("block_rail_preview")) {
            blockMD.setColor(8224125);
        } else if (name.contains("block_rail") || name.contains("multiblock")) {
            blockMD.setColor(14474460);
        }
    }

    @Nullable
    @Override
    public int deriveBlockColor(BlockMD blockMD, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        return ModBlockDelegate.INSTANCE.getDefaultBlockColorProxy().deriveBlockColor(blockMD, chunkMD, blockPos);
    }

    @Override
    public int getBlockColor(ChunkMD chunkMD, BlockMD blockMD, BlockPos blockPos) {
        String name = blockMD.getBlockId().toLowerCase();
        if (name.equalsIgnoreCase("block_rail_preview")) {
            return 8224125;
        } else {
            return !name.contains("block_rail") && !name.contains("multiblock") ? Minecraft.getInstance().getBlockColors().getColor(blockMD.getBlockState(), chunkMD.getWorld(), blockPos) : 14474460;
        }
    }
}