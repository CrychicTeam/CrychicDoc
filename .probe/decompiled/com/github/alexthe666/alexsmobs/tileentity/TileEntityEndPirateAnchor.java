package com.github.alexthe666.alexsmobs.tileentity;

import com.github.alexthe666.alexsmobs.block.BlockEndPirateAnchor;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityEndPirateAnchor extends BlockEntity {

    private static final List<BlockPos> VALID_OFFSET_BOXES_NS = Lists.newArrayList(new BlockPos[] { new BlockPos(0, 0, 0), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(1, 1, 0), new BlockPos(-1, 1, 0), new BlockPos(0, 1, 0), new BlockPos(0, 2, 0) });

    private static final List<BlockPos> VALID_OFFSET_BOXES_EW = Lists.newArrayList(new BlockPos[] { new BlockPos(0, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(0, 1, 1), new BlockPos(0, 1, -1), new BlockPos(0, 1, 0), new BlockPos(0, 2, 0) });

    public TileEntityEndPirateAnchor(BlockPos pos, BlockState state) {
        super(AMTileEntityRegistry.END_PIRATE_ANCHOR.get(), pos, state);
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, TileEntityEndPirateAnchor entity) {
        entity.tick();
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(-1, 0, -1), this.f_58858_.offset(1, 3, 1));
    }

    public static List<BlockPos> getValidBBPositions(boolean eastOrWest) {
        return eastOrWest ? VALID_OFFSET_BOXES_EW : VALID_OFFSET_BOXES_NS;
    }

    public boolean hasAllAnchorBlocks() {
        for (BlockPos pos : getValidBBPositions((Boolean) this.m_58900_().m_61143_(BlockEndPirateAnchor.EASTORWEST))) {
            if (!(this.m_58904_().getBlockState(this.m_58899_().offset(pos)).m_60734_() instanceof BlockEndPirateAnchor)) {
                return false;
            }
        }
        return true;
    }

    private void tick() {
    }
}