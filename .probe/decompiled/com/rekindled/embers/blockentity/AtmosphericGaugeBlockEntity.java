package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AtmosphericGaugeBlockEntity extends BlockEntity {

    int power = 0;

    public AtmosphericGaugeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.ATMOSPHERIC_GAUGE_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.power = nbt.getInt("power");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("power", this.power);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AtmosphericGaugeBlockEntity blockEntity) {
        if (blockEntity.power != state.m_60674_(level, pos)) {
            level.updateNeighbourForOutputSignal(pos, state.m_60734_());
            blockEntity.power = state.m_60674_(level, pos);
            blockEntity.m_6596_();
        }
    }
}