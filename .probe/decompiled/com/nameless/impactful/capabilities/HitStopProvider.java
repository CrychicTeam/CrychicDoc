package com.nameless.impactful.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class HitStopProvider implements ICapabilityProvider {

    private final LazyOptional<HitStopCap> optional;

    private final HitStopCap hitStopCap = new HitStopCap();

    public HitStopProvider() {
        this.optional = LazyOptional.of(() -> this.hitStopCap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ImpactfulCapabilities.INSTANCE ? this.optional.cast() : LazyOptional.empty();
    }
}