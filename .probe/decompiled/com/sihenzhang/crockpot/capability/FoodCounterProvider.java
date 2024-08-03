package com.sihenzhang.crockpot.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class FoodCounterProvider implements ICapabilitySerializable<CompoundTag> {

    private final IFoodCounter foodCounter = new FoodCounter();

    private final LazyOptional<IFoodCounter> foodCounterOptional = LazyOptional.of(() -> this.foodCounter);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY.orEmpty(cap, this.foodCounterOptional);
    }

    public CompoundTag serializeNBT() {
        return this.foodCounter.serializeNBT();
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.foodCounter.deserializeNBT(nbt);
    }
}