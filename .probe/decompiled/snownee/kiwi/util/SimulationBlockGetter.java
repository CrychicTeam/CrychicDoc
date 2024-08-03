package snownee.kiwi.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SimulationBlockGetter extends WrappedBlockGetter {

    private BlockEntity simulatedBlockEntity;

    private BlockPos simulatedPos;

    private boolean useSelfLight;

    private int globalLight = -1;

    public void setBlockEntity(BlockEntity blockEntity) {
        this.simulatedBlockEntity = blockEntity;
    }

    public void setPos(BlockPos pos) {
        this.simulatedPos = pos;
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return this.simulatedBlockEntity != null && pos.equals(this.simulatedPos) ? this.simulatedBlockEntity : super.getBlockEntity(pos);
    }

    public void useSelfLight(boolean useSelfLight) {
        this.useSelfLight = useSelfLight;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        if (this.globalLight != -1 && !pos.equals(this.simulatedPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return this.useSelfLight && this.simulatedPos != null && this.simulatedPos.m_123333_(pos) < 3 ? Blocks.AIR.defaultBlockState() : super.getBlockState(pos);
        }
    }

    @Override
    public int getBrightness(LightLayer lightType, BlockPos pos) {
        if (this.globalLight != -1) {
            return this.globalLight;
        } else {
            if (this.useSelfLight && this.simulatedPos != null && this.simulatedPos.m_123333_(pos) < 3) {
                pos = this.simulatedPos;
            }
            return super.m_45517_(lightType, pos);
        }
    }

    public void setOverrideLight(int i) {
        this.globalLight = i;
    }
}