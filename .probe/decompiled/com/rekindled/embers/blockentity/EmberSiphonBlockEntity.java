package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.upgrade.EmberSiphonUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class EmberSiphonBlockEntity extends BlockEntity {

    public EmberSiphonUpgrade upgrade = new EmberSiphonUpgrade(this);

    public EmberSiphonBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.EMBER_SIPHON_ENTITY.get(), pPos, pBlockState);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.f_58859_) {
            if (cap == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY && (side == null || side == Direction.UP)) {
                return this.upgrade.getCapability(cap, side);
            }
            if (cap == EmbersCapabilities.EMBER_CAPABILITY && (side == null || side.getAxis() != Direction.Axis.Y)) {
                BlockEntity tile = this.f_58857_.getBlockEntity(this.f_58858_.above());
                if (tile != null) {
                    return tile.getCapability(cap, Direction.DOWN);
                }
            }
        }
        return super.getCapability(cap, side);
    }
}