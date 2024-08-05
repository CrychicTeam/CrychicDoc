package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.power.DefaultEmberCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class CreativeEmberBlockEntity extends BlockEntity {

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            CreativeEmberBlockEntity.this.m_6596_();
        }

        @Override
        public boolean acceptsVolatile() {
            return true;
        }

        @Override
        public double getEmber() {
            return this.getEmberCapacity() / 2.0;
        }

        @Override
        public double addAmount(double value, boolean doAdd) {
            return value;
        }

        @Override
        public double removeAmount(double value, boolean doRemove) {
            return value;
        }
    };

    public CreativeEmberBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.CREATIVE_EMBER_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(80000.0);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == EmbersCapabilities.EMBER_CAPABILITY ? this.capability.getCapability(cap, side) : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.capability.invalidate();
    }
}