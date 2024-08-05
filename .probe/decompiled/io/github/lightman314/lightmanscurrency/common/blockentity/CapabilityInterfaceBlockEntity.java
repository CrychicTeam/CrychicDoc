package io.github.lightman314.lightmanscurrency.common.blockentity;

import io.github.lightman314.lightmanscurrency.api.misc.blocks.ICapabilityBlock;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityInterfaceBlockEntity extends BlockEntity {

    public CapabilityInterfaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CAPABILITY_INTERFACE.get(), pos, state);
    }

    @Nonnull
    public final <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (this.m_58900_().m_60734_() instanceof ICapabilityBlock handlerBlock) {
            BlockEntity blockEntity = handlerBlock.getCapabilityBlockEntity(this.m_58900_(), this.f_58857_, this.f_58858_);
            if (blockEntity != null && !(blockEntity instanceof CapabilityInterfaceBlockEntity)) {
                return blockEntity.getCapability(cap, side);
            }
        }
        return super.getCapability(cap, side);
    }
}