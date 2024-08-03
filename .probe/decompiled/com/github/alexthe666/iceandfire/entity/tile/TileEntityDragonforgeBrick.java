package com.github.alexthe666.iceandfire.entity.tile;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class TileEntityDragonforgeBrick extends BlockEntity {

    public TileEntityDragonforgeBrick(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DRAGONFORGE_BRICK.get(), pos, state);
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return this.getConnectedTileEntity() != null && capability == ForgeCapabilities.ITEM_HANDLER ? this.getConnectedTileEntity().getCapability(capability, facing) : super.getCapability(capability, facing);
    }

    private ICapabilityProvider getConnectedTileEntity() {
        for (Direction facing : Direction.values()) {
            if (this.f_58857_.getBlockEntity(this.f_58858_.relative(facing)) instanceof TileEntityDragonforge) {
                return this.f_58857_.getBlockEntity(this.f_58858_.relative(facing));
            }
        }
        return null;
    }
}