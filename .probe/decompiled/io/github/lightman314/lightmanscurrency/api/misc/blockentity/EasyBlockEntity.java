package io.github.lightman314.lightmanscurrency.api.misc.blockentity;

import io.github.lightman314.lightmanscurrency.common.util.IClientTracker;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class EasyBlockEntity extends BlockEntity implements IClientTracker {

    public EasyBlockEntity(@Nonnull BlockEntityType<?> type, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        super(type, pos, state);
    }

    @Override
    public boolean isClient() {
        return this.f_58857_ == null || this.f_58857_.isClientSide;
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }
}